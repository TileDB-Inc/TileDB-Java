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
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_query_type_t;
import org.apache.spark.sql.execution.vectorized.OffHeapColumnVector;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.sources.v2.reader.DataReader;
import org.apache.spark.sql.sources.v2.reader.DataReaderFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.vectorized.ColumnarBatch;

import java.io.IOException;
import java.util.*;

public class TileDBReaderFactory implements DataReaderFactory<ColumnarBatch>, DataReader<ColumnarBatch> {
  private static final int BATCH_SIZE = 5;
  private StructField[] attributes;
  private String arrayName;
  private long[] subarray;
  private boolean initilized;
  private Query query;
  private Context ctx;

  private boolean hasNext;
  private OffHeapColumnVector[] vectors;
  private ColumnarBatch batch;

  TileDBReaderFactory(long[] subarray, StructType requiredSchema, DataSourceOptions options) {
    this.subarray = subarray;
    attributes = requiredSchema.fields();
    arrayName = options.get("array").orElse("");
    initilized = false;
  }

  public TileDBReaderFactory(long[] subarray, StructField[] attributes, String arrayName) {
    this.subarray = subarray;
    this.attributes = attributes;
    this.arrayName = arrayName;
  }


  @Override
  public DataReader<ColumnarBatch> createDataReader() {
    return new TileDBReaderFactory(subarray, attributes, arrayName);
  }

  @Override
  public boolean next() {
    if(query==null){
      //initialize
      try {
        ctx = new Context();
        // Compute maximum buffer elements for the query results per attribute
        Array array = new Array(ctx, arrayName);
        NativeArray nsubarray = new NativeArray(ctx, subarray, Long.class);
        HashMap<String, Pair<Long,Long>> max_sizes = array.maxBufferElements(nsubarray);
        ArraySchema arraySchema = array.getSchema();
        // Create query
        query = new Query(array, tiledb_query_type_t.TILEDB_READ);
        query.setLayout(tiledb_layout_t.TILEDB_GLOBAL_ORDER);
        query.setSubarray(nsubarray);

        vectors = new OffHeapColumnVector[attributes.length];
        int i = 0;
        for(StructField field : attributes){
          String name = field.name();
          if(max_sizes.get(name).getFirst()!=0){
            //var length
          }
          else{
            query.setBuffer(name,
                new NativeArray(ctx, BATCH_SIZE,arraySchema.getAttribute(name).getType()));
            //ToDo
            vectors[i] = new OffHeapColumnVector(BATCH_SIZE, DataTypes.IntegerType);
          }
          i++;
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
        String name = field.name();
        int[] a1_buff = (int[]) query.getBuffer(name);
        currentSize = a1_buff.length;
        vectors[i].reset();
        vectors[i].putInts(0, currentSize, a1_buff,0);
        i++;
      }
      batch.setNumRows(currentSize);
    } catch (TileDBError tileDBError) {
      tileDBError.printStackTrace();
    }
    return batch;
  }

  @Override
  public void close() throws IOException {
    try {
      if(query!=null) {
        query.close();
        ctx.close();
      }
    } catch (TileDBError tileDBError) {
      tileDBError.printStackTrace();
    }
  }
}
