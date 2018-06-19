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

package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class ArraySchemaTest {

  @Test
  public void testArraySchema() throws Throwable {
    testArraySchemaCreate();
    testArraySchemaRead();
  }


  public void testArraySchemaCreate() throws Throwable {
    Context ctx = new Context();
    // Create getDimensions
    Dimension<Long> d1 = new Dimension<Long>(ctx,"d1",Long.class, new Pair<Long, Long>(1l,4l),2l);
    Dimension<Long> d2 = new Dimension<Long>(ctx,"d2",Long.class, new Pair<Long, Long>(1l,4l),2l);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);
    domain.addDimension(d2);

    // Create and add getAttributes
    Attribute a1 = new Attribute(ctx,"a1",Integer.class);
    Attribute a2 = new Attribute(ctx,"a2",Character.class);
    a2.setCellValNum(tiledb.tiledb_var_num());
    Attribute a3 = new Attribute(ctx,"a3",Float.class);
    a3.setCellValNum(2);
    a1.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_BLOSC_LZ4, -1));
    a2.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_GZIP, -1));
    a3.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_ZSTD, -1));

    ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_DENSE);
    schema.setTileOrder(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.setCellOrder(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);
    schema.addAttribute(a3);

    try {
      schema.check();
    }catch (Exception e){
      e.printStackTrace();
    }
    // Print array schema contents
    schema.dump();

    Array my_dense_array = new Array(ctx, "my_dense_array", schema);
    my_dense_array.close()
  
//    Array my_dense_array = new Array(ctx,"my_dense_array");

    // Prepare cell buffers
    NativeArray a1_data = new NativeArray(
        ctx,
        new int[]{
            0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 10, 11, 12, 13, 14, 15
        },
        Integer.class);
    NativeArray a2_offsets = new NativeArray(
        ctx,
        new long[]{
            0, 1, 3, 6, 10, 11, 13, 16,
            20, 21, 23, 26, 30, 31, 33, 36
        },
        Long.class);
    NativeArray buffer_var_a2 = new NativeArray(
        ctx,
        "abbcccdddd"+
            "effggghhhh"+
            "ijjkkkllll"+
            "mnnooopppp",
        String.class);

    NativeArray buffer_a3 = new NativeArray(
        ctx,
        new float[]{
            0.1f,  0.2f,  1.1f,  1.2f,  2.1f,  2.2f,  3.1f,  3.2f,
            4.1f,  4.2f,  5.1f,  5.2f,  6.1f,  6.2f,  7.1f,  7.2f,
            8.1f,  8.2f,  9.1f,  9.2f,  10.1f, 10.2f, 11.1f, 11.2f,
            12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
        },
        Float.class);

    // Create query
    Query query = new Query(my_dense_array, tiledb_query_type_t.TILEDB_WRITE);
    query.setLayout(tiledb_layout_t.TILEDB_GLOBAL_ORDER);
    query.setBuffer("a1", a1_data);
    query.setBuffer("a2", a2_offsets, buffer_var_a2);
    query.setBuffer("a3", buffer_a3);

    // Submit query
    query.submit();
    query.close();
  }

  private void testArraySchemaRead() throws Exception {
    // Create TileDB context
    Context ctx = new Context();

    // Print non-empty getDomain

    Array my_dense_array = new Array(ctx, "my_dense_array");
    HashMap<String, Pair> dom = my_dense_array.nonEmptyDomain();
    for (Map.Entry<String, Pair> e : dom.entrySet()){
      System.out.println(e.getKey() + ": ("+e.getValue().getFirst()+", "+e.getValue().getSecond()+")");
    }


    // Print maximum buffer elements for the query results per attribute
    NativeArray subarray = new NativeArray(ctx, new long[]{1l, 4l, 1l, 4l}, Long.class);
    HashMap<String, Pair<Long,Long>> max_sizes = my_dense_array.maxBufferElements(subarray);

    for (Map.Entry<String, Pair<Long,Long>> e : max_sizes.entrySet()){
      System.out.println(e.getKey() + " ("+e.getValue().getFirst()+", "+e.getValue().getSecond()+")");
    }

    // Create query
    Query query = new Query(my_dense_array, tiledb_query_type_t.TILEDB_READ);
    query.setLayout(tiledb_layout_t.TILEDB_GLOBAL_ORDER);
    query.setSubarray(subarray);
    query.setBuffer("a1",
        new NativeArray(ctx, max_sizes.get("a1").getSecond().intValue(),Integer.class));
    query.setBuffer("a2",
        new NativeArray(ctx, max_sizes.get("a2").getFirst().intValue(), Long.class),
        new NativeArray(ctx, max_sizes.get("a2").getSecond().intValue(), String.class));
    query.setBuffer("a3", new NativeArray(ctx, max_sizes.get("a3").getSecond().intValue(), Float.class));

    // Submit query
    System.out.println("Query submitted: " + query.submit() );

    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    int[] a1_buff = (int[]) query.getBuffer("a1");
    long[] a2_offsets = (long[]) query.getVarBuffer("a2");
    byte[] a2_data = (byte[]) query.getBuffer("a2");
    float[] a3_buff = (float[]) query.getBuffer("a3");

    System.out.println("Result num: " + a1_buff.length );
    System.out.println(
        String.format("%9s","a1") +
            String.format("%11s","a2") +
            String.format("%11s","a3[0]") +
            String.format("%10s","a3[1]"));

    for (int i =0; i< a1_buff.length; i++){
      int end = (i==a1_buff.length-1)? a2_data.length : (int) a2_offsets[i+1];
      System.out.println(
          String.format("%9s",a1_buff[i])+
              String.format("%11s",new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[i], end)))+
              String.format("%11s",a3_buff[2*i])+
              String.format("%10s",a3_buff[2*i+1]));
    }
  }
}
