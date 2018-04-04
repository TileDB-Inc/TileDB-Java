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

import io.tiledb.api.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ArraySchemaTest {

  @Test
  public void testArraySchema() throws Throwable {
//    testArraySchemaCreate();
    testArraySchemaRead();
  }


  public void testArraySchemaCreate() throws Throwable {
    Context ctx = new Context();
    // Create dimensions
    Dimension<Long> d1 = new Dimension<Long>(ctx,"d1",Long.class, new Pair<Long, Long>(1l,4l),2l);
    Dimension<Long> d2 = new Dimension<Long>(ctx,"d2",Long.class, new Pair<Long, Long>(1l,4l),2l);

    // Create and set domain
    Domain<Integer> domain = new Domain<Integer>(ctx);
    domain.add_dimension(d1);
    domain.add_dimension(d2);

    // Create and add attributes
    Attribute<Integer> a1 = new Attribute<Integer>(ctx,"a1",Integer.class);
    Attribute<Character> a2 = new Attribute<Character>(ctx,"a2",Character.class);
    a2.setCellValNum(tiledb.tiledb_var_num());
    Attribute<Float> a3 = new Attribute<Float>(ctx,"a3",Float.class);
//    a3.setCellValNum(2);
    a1.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_BLOSC_LZ4, -1));
    a2.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_GZIP, -1));
    a3.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_ZSTD, -1));

    ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_DENSE);
    schema.set_tile_order(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.set_cell_order(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.set_domain(domain);
    schema.add_attribute(a1);
    schema.add_attribute(a2);
    schema.add_attribute(a3);

    try {
      schema.check();
    }catch (Exception e){
      e.printStackTrace();
    }
    // Print array schema contents
    schema.dump();

    Array my_dense_array = new Array(ctx, "my_dense_array", schema);

//    Array my_dense_array = new Array(ctx,"my_dense_array");
    Query query = new Query(my_dense_array, tiledb_query_type_t.TILEDB_WRITE);


    // Prepare cell buffers
    int[] a1_data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
        15};
    long[] a2_offsets = {0, 1, 3, 6, 10, 11, 13, 16, 20, 21, 23, 26, 30,
        31, 33, 36};
    String buffer_var_a2 = "abbcccdddd" + "effggghhhh" + "ijjkkkllll"
        + "mnnooopppp";

    float buffer_a3[] = {0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
        4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f};

    query.set_layout(tiledb_layout_t.TILEDB_GLOBAL_ORDER);
    query.set_buffer("a1", a1_data, a1_data.length);
    query.set_buffer("a2", a2_offsets, buffer_var_a2, buffer_var_a2.length());
    query.set_buffer("a3", buffer_a3, buffer_a3.length);

    // Submit query
    query.submit();
    query.free();

    schema.free();
    ctx.free();
  }

  private void testArraySchemaRead() throws TileDBError {
    Context ctx = new Context();
    Array my_dense_array = new Array(ctx, "my_dense_array");
    HashMap<String, Pair> dom = my_dense_array.non_empty_domain();
    for (Map.Entry<String, Pair> e : dom.entrySet()){
      System.out.println("!"+e.getKey() + " ["+e.getValue().getFirst().getClass()+":"+e.getValue().getFirst()+","+e.getValue().getSecond().getClass()+":"+e.getValue().getSecond()+"]");
    }
    long[] subarray = {1l, 4l, 1l, 4l};
    HashMap<String, Pair<Long,Long>> max_sizes = my_dense_array.max_buffer_elements(subarray);

    for (Map.Entry<String, Pair<Long,Long>> e : max_sizes.entrySet()){
      System.out.println(e.getKey() + " ["+e.getValue().getFirst()+","+e.getValue().getSecond()+"]");
    }

  }
}