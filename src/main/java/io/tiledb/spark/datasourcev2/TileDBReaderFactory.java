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
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRow;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.sources.v2.reader.DataReader;
import org.apache.spark.sql.sources.v2.reader.DataReaderFactory;
import org.apache.spark.sql.sources.v2.reader.DataSourceReader;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;
import java.util.*;

public class TileDBReaderFactory implements DataReaderFactory<Row>, DataReader<Row> {
  private StructField[] attributes;
  private String arrayName;
  private long[] subarray;
  private boolean initilized;
  private Query query;
  private Context ctx;

  TileDBReaderFactory(long[] subarray, StructType requiredSchema, DataSourceOptions options) {
    this.subarray = subarray;
    attributes = requiredSchema.fields();
    arrayName = options.get("array").orElse("");
    initilized = false;
    

//      query.setBuffer("a1",
//          new NativeArray(ctx, max_sizes.get("a1").getSecond().intValue(),Integer.class));
//      query.setBuffer("a2",
//          new NativeArray(ctx, max_sizes.get("a2").getFirst().intValue(), Long.class),
//          new NativeArray(ctx, max_sizes.get("a2").getSecond().intValue(), String.class));
//      query.setBuffer("a3", new NativeArray(ctx, max_sizes.get("a3").getSecond().intValue(), Float.class));

//      // Submit query
//      System.out.println("Query submitted: " + query.submit() );
//
//      // Print cell values (assumes all getAttributes are read)
//      HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();
////      a1_buff = (int[]) query.getBuffer("a1");
//      a2_offsets = (long[]) query.getVarBuffer("a2");
//      a2_data = (byte[]) query.getBuffer("a2");
////      a3_buff = (float[]) query.getBuffer("a3");
////        coords = (long[]) query.getBuffer(tiledb.tiledb_coords());
//
//      index = 0;
//      query.close();
  }

  public TileDBReaderFactory(long[] subarray, StructField[] attributes, String arrayName) {
    this.subarray = subarray;
    this.attributes = attributes;
    this.arrayName = arrayName;
  }


  @Override
  public DataReader<Row> createDataReader() {
    return new TileDBReaderFactory(subarray, attributes, arrayName);
  }

  @Override
  public boolean next() {
//    System.out.println(a2_offsets.length);
//    if(index>=a2_offsets.length){
//      return false;
//    }
//    return true;
    boolean firstRecord = false;
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
        for(StructField field : attributes){
          String name = field.name();
          if(max_sizes.get(name).getFirst()!=0){
            //var length
          }
          else{
            query.setBuffer(name,
                new NativeArray(ctx, 1,arraySchema.getAttribute(name).getType()));
          }
        }
        firstRecord = true;
      } catch (Exception tileDBError) {
        tileDBError.printStackTrace();
      }
    }
    try {
      query.submit();
      return query.getQueryStatus() == Status.INCOMPLETE;
    } catch (TileDBError tileDBError) {
      tileDBError.printStackTrace();
    }
    return false;
  }

  @Override
  public Row get() {
//    int end = (index==a2_offsets.length-1)? a2_data.length : (int) a2_offsets[index+1];
//    GenericRow ret = new GenericRow(new Object[] {
////        null,
////        null,
////        a1_buff[index],
//        new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[index], end))
////        new float[]{a3_buff[2*index], a3_buff[2*index+1]}
//    });
//    index++;
//    return ret;
    GenericRow ret = null;
    try {
      List<Object> row = new ArrayList<Object>();
      for(StructField field : attributes) {
        String name = field.name();
        int[] a1_buff = (int[]) query.getBuffer(name);
        row.add(a1_buff[0]);
      }
      ret = new GenericRow(row.toArray());
    } catch (TileDBError tileDBError) {
      tileDBError.printStackTrace();
    }
    return ret;
  }

  @Override
  public void close() throws IOException {
//    try {
//      query.close();
//      ctx.close();
//    } catch (TileDBError tileDBError) {
//      tileDBError.printStackTrace();
//    }
  }
}
