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
import org.apache.spark.sql.execution.vectorized.OffHeapColumnVector;
import org.apache.spark.sql.execution.vectorized.OnHeapColumnVector;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.sources.v2.reader.DataReader;
import org.apache.spark.sql.sources.v2.reader.DataReaderFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.vectorized.ColumnarBatch;

import java.io.IOException;
import java.util.*;

import static org.apache.spark.sql.types.DataTypes.*;


public class TileDBReaderFactory implements DataReaderFactory<ColumnarBatch>, DataReader<ColumnarBatch> {
  private StructField[] attributes;
  private long[] subarray;
  private boolean initilized;
  private Query query;
  private Context ctx;
  private TileDBOptions options;
  private ArraySchema arraySchema;

  private boolean hasNext;
  private OnHeapColumnVector[] vectors;
  private ColumnarBatch batch;

  TileDBReaderFactory(long[] subarray, StructType requiredSchema, DataSourceOptions options) {
    this.subarray = subarray;
    attributes = requiredSchema.fields();
    initilized = false;
    this.options = new TileDBOptions(options);
  }

  public TileDBReaderFactory(long[] subarray, StructField[] attributes, TileDBOptions options) {
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
        NativeArray nsubarray = new NativeArray(ctx, subarray, Long.class);
        HashMap<String, Pair<Long,Long>> max_sizes = array.maxBufferElements(nsubarray);
        arraySchema = array.getSchema();
        // Create query
        query = new Query(array, tiledb_query_type_t.TILEDB_READ);
        query.setLayout(tiledb_layout_t.TILEDB_GLOBAL_ORDER);
        query.setSubarray(nsubarray);

        vectors = OnHeapColumnVector.allocateColumns(options.BATCH_SIZE, attributes);
        int i = 0;
        for(StructField field : attributes){
          String name = field.name();
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
    Attribute attribute = arraySchema.getAttribute(name);
    int numValues = 0;
    int bufferLength = 0;
    if(attribute.getCellValNum()>1){
      //variable length values added as arrays
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
        case TILEDB_INT16: {
          short[] buff = (short[]) query.getBuffer(name);
          bufferLength = buff.length;
          vectors[index].getChild(0).reserve(bufferLength);
          vectors[index].getChild(0).putShorts(0,bufferLength, buff,0);
          break;
        }
        case TILEDB_INT32: {
          int[] buff = (int[]) query.getBuffer(name);
          bufferLength = buff.length;
          vectors[index].getChild(0).reserve(bufferLength);
          vectors[index].getChild(0).putInts(0,bufferLength, buff,0);
          break;
        }
        case TILEDB_INT64: {
          long[] buff = (long[]) query.getBuffer(name);
          bufferLength = buff.length;
          vectors[index].getChild(0).reserve(bufferLength);
          vectors[index].getChild(0).putLongs(0,bufferLength, buff,0);
          break;
        }
        case TILEDB_UINT8: {
          short[] buff = (short[]) query.getBuffer(name);
          bufferLength = buff.length;
          vectors[index].getChild(0).reserve(bufferLength);
          vectors[index].getChild(0).putShorts(0,bufferLength, buff,0);
          break;
        }
        case TILEDB_UINT16: {
          int[] buff = (int[]) query.getBuffer(name);
          bufferLength = buff.length;
          vectors[index].getChild(0).reserve(bufferLength);
          vectors[index].getChild(0).putInts(0,bufferLength, buff,0);
          break;
        }
        case TILEDB_UINT32: {
          long[] buff = (long[]) query.getBuffer(name);
          bufferLength = buff.length;
          vectors[index].getChild(0).reserve(bufferLength);
          vectors[index].getChild(0).putLongs(0,bufferLength, buff,0);
          break;
        }
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
    }
    else{
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
        case TILEDB_INT16: {
          short[] buff = (short[]) query.getBuffer(name);
          bufferLength = buff.length;
          numValues = bufferLength;
          vectors[index].reset();
          vectors[index].putShorts(0, bufferLength, buff,0);
          break;
        }
        case TILEDB_INT32: {
          int[] buff = (int[]) query.getBuffer(name);
          bufferLength = buff.length;
          numValues = bufferLength;
          vectors[index].reset();
          vectors[index].putInts(0, bufferLength, buff,0);
          break;
        }
        case TILEDB_INT64: {
          long[] buff = (long[]) query.getBuffer(name);
          bufferLength = buff.length;
          numValues = bufferLength;
          vectors[index].reset();
          vectors[index].putLongs(0, bufferLength, buff,0);
          break;
        }
        case TILEDB_UINT8: {
          short[] buff = (short[]) query.getBuffer(name);
          bufferLength = buff.length;
          numValues = bufferLength;
          vectors[index].reset();
          vectors[index].putShorts(0, bufferLength, buff,0);
          break;
        }
        case TILEDB_UINT16: {
          int[] buff = (int[]) query.getBuffer(name);
          bufferLength = buff.length;
          numValues = bufferLength;
          vectors[index].reset();
          vectors[index].putInts(0, bufferLength, buff,0);
          break;
        }
        case TILEDB_UINT32: {
          long[] buff = (long[]) query.getBuffer(name);
          bufferLength = buff.length;
          numValues = bufferLength;
          vectors[index].reset();
          vectors[index].putLongs(0, bufferLength, buff,0);
          break;
        }
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
