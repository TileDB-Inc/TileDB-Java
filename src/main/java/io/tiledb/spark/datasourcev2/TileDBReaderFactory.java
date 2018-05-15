/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 TileDB, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.tiledb.spark.datasourcev2;

import io.tiledb.java.api.*;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_query_type_t;
import org.apache.spark.sql.execution.vectorized.OnHeapColumnVector;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.sources.v2.reader.DataReader;
import org.apache.spark.sql.sources.v2.reader.DataReaderFactory;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.vectorized.ColumnarBatch;

import java.io.IOException;
import java.util.*;


public class TileDBReaderFactory implements DataReaderFactory<ColumnarBatch>, DataReader<ColumnarBatch> {
  private StructField[] attributes;
  private Object subarray;
  private boolean initilized;
  private Query query;
  private Context ctx;
  private TileDBOptions options;
  private ArraySchema arraySchema;

  private boolean hasNext;
  private OnHeapColumnVector[] vectors;
  private ColumnarBatch batch;

  TileDBReaderFactory(Object subarray, StructType requiredSchema, DataSourceOptions options) {
    this.subarray = subarray;
    attributes = requiredSchema.fields();
    initilized = false;
    this.options = new TileDBOptions(options);
  }

  public TileDBReaderFactory(Object subarray, StructField[] attributes, TileDBOptions options) {
    this.subarray = subarray;
    this.attributes = attributes;
    this.options = options;
  }




  @Override
  public DataReader<ColumnarBatch> createDataReader() {
    return new TileDBReaderFactory(subarray, attributes, options);
  }

  @Override
  public boolean next() {
    if(query==null){
      //initialize
      try {
        ctx = new Context();
        // Compute maximum buffer elements for the query results per attribute
        Array array = new Array(ctx, options.ARRAY_URI);
        arraySchema = array.getSchema();
        NativeArray nsubarray = new NativeArray(ctx, subarray, arraySchema.getDomain().getType());
        HashMap<String, Pair<Long,Long>> max_sizes = array.maxBufferElements(nsubarray);

        // Create query
        query = new Query(array, tiledb_query_type_t.TILEDB_READ);
        query.setLayout(tiledb_layout_t.TILEDB_GLOBAL_ORDER);
        query.setSubarray(nsubarray);

        vectors = OnHeapColumnVector.allocateColumns(options.BATCH_SIZE, attributes);
        int i = 0;
        for(StructField field : attributes){
          String name = field.name();
          if(!arraySchema.getAttributes().containsKey(name)){
            //dimension column
            continue;
          }
          long cellValNum = arraySchema.getAttribute(name).getCellValNum();
          int valPerRow = (int) ((cellValNum == tiledb.tiledb_var_num())? max_sizes.get(name).getFirst() : cellValNum);
          if(cellValNum == tiledb.tiledb_var_num()){
            query.setBuffer(name,
                new NativeArray(ctx, options.BATCH_SIZE, Long.class),
                new NativeArray(ctx, options.BATCH_SIZE * max_sizes.get(name).getSecond().intValue(),
                    arraySchema.getAttribute(name).getType()));
          } else {
            query.setBuffer(name,
                new NativeArray(ctx, options.BATCH_SIZE * valPerRow, arraySchema.getAttribute(name).getType()));
          }
        }
        query.setCoordinates(new NativeArray(ctx, options.BATCH_SIZE * 2, arraySchema.getDomain().getType()));
        batch = new ColumnarBatch(vectors);
        hasNext = true;
      } catch (Exception tileDBError) {
        tileDBError.printStackTrace();
      }
    }
    try {
      query.submit();
      boolean ret = hasNext;
      hasNext = query.getQueryStatus() == Status.INCOMPLETE;
      return ret;
    } catch (TileDBError tileDBError) {
      tileDBError.printStackTrace();
    }
    return false;
  }

  @Override
  public ColumnarBatch get() {
    try {
      int i = 0, currentSize=0;
      for(StructField field : attributes) {
        currentSize = getColumnBatch(field, i);
        i++;
      }
      batch.setNumRows(currentSize);
    } catch (TileDBError tileDBError) {
      tileDBError.printStackTrace();
    }
    return batch;
  }

  private int getColumnBatch(StructField field, int index) throws TileDBError {
    String name = field.name();
    if(!arraySchema.getAttributes().containsKey(name)){
      //dimension column
      return getDimensionColumn(name, index);
    }
    else{
      //attribute column
      return getAttributeColumn(name, index);
    }
  }

  private int getAttributeColumn(String name, int index) throws TileDBError {
    int numValues = 0;
    int bufferLength = 0;
    Attribute attribute = arraySchema.getAttribute(name);
    if(attribute.getCellValNum()>1){
      //variable length values added as arrays
      return getVarLengthAttributeColumn(name, attribute, index);
    }
    else{
      //one value per cell
      return getSingleValueAttributeColumn(name, attribute, index);
    }
  }

  private int getSingleValueAttributeColumn(String name, Attribute attribute, int index) throws TileDBError {
    int numValues = 0;
    int bufferLength = 0;
    switch (attribute.getType()) {
      case TILEDB_FLOAT32: {
        float[] buff = (float[]) query.getBuffer(name);
        bufferLength = buff.length;
        numValues = bufferLength;
        vectors[index].reset();
        vectors[index].putFloats(0, bufferLength, buff,0);
        break;
      }
      case TILEDB_FLOAT64: {
        double[] buff = (double[]) query.getBuffer(name);
        bufferLength = buff.length;
        numValues = bufferLength;
        vectors[index].reset();
        vectors[index].putDoubles(0, bufferLength, buff,0);
        break;
      }
      case TILEDB_INT8: {
        byte[] buff = (byte[]) query.getBuffer(name);
        bufferLength = buff.length;
        numValues = bufferLength;
        vectors[index].reset();
        vectors[index].putBytes(0, bufferLength, buff,0);
        break;
      }
      case TILEDB_INT16:
      case TILEDB_UINT8: {
        short[] buff = (short[]) query.getBuffer(name);
        bufferLength = buff.length;
        numValues = bufferLength;
        vectors[index].reset();
        vectors[index].putShorts(0, bufferLength, buff,0);
        break;
      }
      case TILEDB_INT32:
      case TILEDB_UINT16: {
        int[] buff = (int[]) query.getBuffer(name);
        bufferLength = buff.length;
        numValues = bufferLength;
        vectors[index].reset();
        vectors[index].putInts(0, bufferLength, buff,0);
        break;
      }
      case TILEDB_INT64:
      case TILEDB_UINT32:
      case TILEDB_UINT64: {
        long[] buff = (long[]) query.getBuffer(name);
        bufferLength = buff.length;
        numValues = bufferLength;
        vectors[index].reset();
        vectors[index].putLongs(0, bufferLength, buff,0);
        break;
      }
      case TILEDB_CHAR: {
        byte[] buff = (byte[]) query.getBuffer(name);
        bufferLength = buff.length;
        numValues = bufferLength;
        vectors[index].reset();
        vectors[index].putBytes(0, bufferLength, buff,0);
        break;
      }
      default: {
        throw new TileDBError("Not supported getDomain getType " + attribute.getType());
      }
    }
    return numValues;
  }

  private int getVarLengthAttributeColumn(String name, Attribute attribute , int index) throws TileDBError {
    int numValues = 0;
    int bufferLength = 0;
    vectors[index].reset();
    vectors[index].getChild(0).reset();
    switch (attribute.getType()) {
      case TILEDB_FLOAT32: {
        float[] buff = (float[]) query.getBuffer(name);
        bufferLength = buff.length;
        vectors[index].getChild(0).reserve(bufferLength);
        vectors[index].getChild(0).putFloats(0,bufferLength, buff,0);
        break;
      }
      case TILEDB_FLOAT64: {
        double[] buff = (double[]) query.getBuffer(name);
        bufferLength = buff.length;
        vectors[index].getChild(0).reserve(bufferLength);
        vectors[index].getChild(0).putDoubles(0,bufferLength, buff,0);
        break;
      }
      case TILEDB_INT8: {
        byte[] buff = (byte[]) query.getBuffer(name);
        bufferLength = buff.length;
        vectors[index].getChild(0).reserve(bufferLength);
        vectors[index].getChild(0).putBytes(0,bufferLength, buff,0);
        break;
      }
      case TILEDB_INT16:
      case TILEDB_UINT8: {
        short[] buff = (short[]) query.getBuffer(name);
        bufferLength = buff.length;
        vectors[index].getChild(0).reserve(bufferLength);
        vectors[index].getChild(0).putShorts(0,bufferLength, buff,0);
        break;
      }
      case TILEDB_INT32:
      case TILEDB_UINT16: {
        int[] buff = (int[]) query.getBuffer(name);
        bufferLength = buff.length;
        vectors[index].getChild(0).reserve(bufferLength);
        vectors[index].getChild(0).putInts(0,bufferLength, buff,0);
        break;
      }
      case TILEDB_INT64:
      case TILEDB_UINT32:
      case TILEDB_UINT64: {
        long[] buff = (long[]) query.getBuffer(name);
        bufferLength = buff.length;
        vectors[index].getChild(0).reserve(bufferLength);
        vectors[index].getChild(0).putLongs(0,bufferLength, buff,0);
        break;
      }
      case TILEDB_CHAR: {
        byte[] buff = (byte[]) query.getBuffer(name);
        bufferLength = buff.length;
        vectors[index].getChild(0).reserve(bufferLength);
        vectors[index].getChild(0).putBytes(0,bufferLength, buff,0);
        break;
      }
      default: {
        throw new TileDBError("Not supported getDomain getType " + attribute.getType());
      }
    }
    if(attribute.getCellValNum()==tiledb.tiledb_var_num()) {
      //add var length offsets
      long[] offsets = (long[]) query.getVarBuffer(name);
      for (int j = 0; j < offsets.length; j++) {
        int length = (j == offsets.length - 1) ? bufferLength - (int) offsets[j] : (int) offsets[j + 1] - (int) offsets[j];
        vectors[index].putArray(j, (int) offsets[j], length);
      }
      numValues = offsets.length;
    }
    else{
      int cellNum = (int)attribute.getCellValNum();
      numValues = bufferLength/cellNum;
      for(int j=0; j<numValues; j++){
        vectors[index].putArray(j,cellNum*j,cellNum);
      }
    }
    return numValues;
  }

  private int getDimensionColumn(String name, int index) throws TileDBError {
    int numValues = 0;
    int bufferLength = 0;

    switch (arraySchema.getDomain().getType()) {
      case TILEDB_FLOAT32: {
        float[] coords = (float[]) query.getBuffer(tiledb.tiledb_coords());
        List<Dimension> dimensions = arraySchema.getDomain().getDimensions();
        int dimensionIndex = 0;
        for(dimensionIndex = 0; dimensionIndex < dimensions.size(); dimensionIndex++){
          if(dimensions.get(dimensionIndex).getName().equals(name))
            break;
        }
        bufferLength = coords.length;
        numValues = bufferLength/2;
        vectors[index].reset();
        for (int i = dimensionIndex; i<bufferLength; i+=2){
          vectors[index].putFloat(i/2,coords[i]);
        }
        break;
      }
      case TILEDB_FLOAT64: {
        double[] coords = (double[]) query.getBuffer(tiledb.tiledb_coords());
        List<Dimension> dimensions = arraySchema.getDomain().getDimensions();
        int dimensionIndex = 0;
        for(dimensionIndex = 0; dimensionIndex < dimensions.size(); dimensionIndex++){
          if(dimensions.get(dimensionIndex).getName().equals(name))
            break;
        }
        bufferLength = coords.length;
        numValues = bufferLength/2;
        vectors[index].reset();
        for (int i = dimensionIndex; i<bufferLength; i+=2){
          vectors[index].putDouble(i/2,coords[i]);
        }
        break;
      }
      case TILEDB_INT8: {
        byte[] coords = (byte[]) query.getBuffer(tiledb.tiledb_coords());
        List<Dimension> dimensions = arraySchema.getDomain().getDimensions();
        int dimensionIndex = 0;
        for(dimensionIndex = 0; dimensionIndex < dimensions.size(); dimensionIndex++){
          if(dimensions.get(dimensionIndex).getName().equals(name))
            break;
        }
        bufferLength = coords.length;
        numValues = bufferLength/2;
        vectors[index].reset();
        for (int i = dimensionIndex; i<bufferLength; i+=2){
          vectors[index].putByte(i/2,coords[i]);
        }
        break;
      }
      case TILEDB_INT16:
      case TILEDB_UINT8: {
        short[] coords = (short[]) query.getBuffer(tiledb.tiledb_coords());
        List<Dimension> dimensions = arraySchema.getDomain().getDimensions();
        int dimensionIndex = 0;
        for(dimensionIndex = 0; dimensionIndex < dimensions.size(); dimensionIndex++){
          if(dimensions.get(dimensionIndex).getName().equals(name))
            break;
        }
        bufferLength = coords.length;
        numValues = bufferLength/2;
        vectors[index].reset();
        for (int i = dimensionIndex; i<bufferLength; i+=2){
          vectors[index].putShort(i/2,coords[i]);
        }
        break;
      }
      case TILEDB_UINT16:
      case TILEDB_INT32: {
        int[] coords = (int[]) query.getBuffer(tiledb.tiledb_coords());
        List<Dimension> dimensions = arraySchema.getDomain().getDimensions();
        int dimensionIndex = 0;
        for(dimensionIndex = 0; dimensionIndex < dimensions.size(); dimensionIndex++){
          if(dimensions.get(dimensionIndex).getName().equals(name))
            break;
        }
        bufferLength = coords.length;
        numValues = bufferLength/2;
        vectors[index].reset();
        for (int i = dimensionIndex; i<bufferLength; i+=2){
          vectors[index].putInt(i/2,coords[i]);
        }
        break;
      }
      case TILEDB_INT64:
      case TILEDB_UINT32:
      case TILEDB_UINT64: {
        long[] coords = (long[]) query.getBuffer(tiledb.tiledb_coords());
        List<Dimension> dimensions = arraySchema.getDomain().getDimensions();
        int dimensionIndex = 0;
        for(dimensionIndex = 0; dimensionIndex < dimensions.size(); dimensionIndex++){
          if(dimensions.get(dimensionIndex).getName().equals(name))
            break;
        }
        bufferLength = coords.length;
        numValues = bufferLength/2;
        vectors[index].reset();
        for (int i = dimensionIndex; i<bufferLength; i+=2){
          vectors[index].putLong(i/2,coords[i]);
        }
        break;
      }
      default: {
        throw new TileDBError("Not supported type for domain " + arraySchema.getDomain().getType());
      }
    }
    return numValues;
  }

  @Override
  public void close() throws IOException {
    try {
      batch.close();
      if(query!=null) {
        query.close();
        ctx.close();
      }
    } catch (TileDBError tileDBError) {
      tileDBError.printStackTrace();
    }
  }
}
