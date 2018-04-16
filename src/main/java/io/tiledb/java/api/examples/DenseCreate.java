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
 *
 * @section DESCRIPTION
 *
 * It shows how to create a dense array. Make sure that no directory exists
 * with the name `my_dense_array` in the current working directory.
 */

package io.tiledb.java.api.examples;

import io.tiledb.java.api.*;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_array_type_t;
import io.tiledb.libtiledb.tiledb_compressor_t;
import io.tiledb.libtiledb.tiledb_layout_t;

import static io.tiledb.libtiledb.tiledb_compressor_t.TILEDB_GZIP;

public class DenseCreate {
  public static void main(String[] args) throws TileDBError {

    // Create TileDB context
    Context ctx = new Context();
    // Create dimensions
    Dimension<Long> d1 = new Dimension<Long>(ctx,"d1",Long.class, new Pair<Long, Long>(1l,4l),2l);
    Dimension<Long> d2 = new Dimension<Long>(ctx,"d2",Long.class, new Pair<Long, Long>(1l,4l),2l);

    // Create domain
    Domain<Integer> domain = new Domain<Integer>(ctx);
    domain.add_dimension(d1);
    domain.add_dimension(d2);


    // Create and add attributes
    Attribute<Integer> a1 = new Attribute<Integer>(ctx,"a1",Integer.class);
    Attribute<Character> a2 = new Attribute<Character>(ctx,"a2",Character.class);
    a2.setCellValNum(tiledb.tiledb_var_num());
    Attribute<Float> a3 = new Attribute<Float>(ctx,"a3",Float.class);
    a3.setCellValNum(2);
    a1.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_BLOSC_LZ4, -1));
    a2.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_GZIP, -1));
    a3.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_ZSTD, -1));

    // Create array schema
    ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_DENSE);
    schema.set_tile_order(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.set_cell_order(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.set_domain(domain);
    schema.add_attribute(a1);
    schema.add_attribute(a2);
    schema.add_attribute(a3);


    // Check array schema
    try {
      schema.check();
    }catch (Exception e){
      e.printStackTrace();
    }

    // Print array schema contents
    schema.dump();

    Array my_dense_array = new Array(ctx, "my_dense_array", schema);
  }
}
