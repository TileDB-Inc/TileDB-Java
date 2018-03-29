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

import io.tiledb.api.tiledb;
import io.tiledb.api.tiledb_array_type_t;
import io.tiledb.api.tiledb_compressor_t;
import io.tiledb.api.tiledb_layout_t;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ArraySchemaTest {

  @Test
  public void testArraySchema() throws Throwable {
    Context ctx = new Context();
    ArraySchema arraySchema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_SPARSE);

    Domain<Integer> domain = new Domain<Integer>(ctx);
    Dimension<Integer> d1 = new Dimension<Integer>(ctx,"d1",Integer.class, new Pair<Integer, Integer>(1,10),1);
    Dimension<Integer> d2 = new Dimension<Integer>(ctx,"d2",Integer.class, new Pair<Integer, Integer>(1,10),1);
    System.out.println(d1.getType());
    domain.add_dimension(d1);
    domain.add_dimension(d2);

    Attribute<Integer> a1 = new Attribute<Integer>(ctx,"a1",Integer.class);
    Attribute<Double> a2 = new Attribute<Double>(ctx,"a2",Double.class);
    a2.setCellValNum(2);
    Attribute<Character> a3 = new Attribute<Character>(ctx,"a3",Character.class);
    a3.setCellValNum(tiledb.tiledb_var_num());

    arraySchema.add_attribute(a1);
    arraySchema.add_attribute(a2);
    arraySchema.add_attribute(a3);

    arraySchema.set_domain(domain);
    arraySchema.set_cell_order(tiledb_layout_t.TILEDB_UNORDERED);
    arraySchema.set_tile_order(tiledb_layout_t.TILEDB_UNORDERED);
    arraySchema.set_offsets_compressor(new Compressor(tiledb_compressor_t.TILEDB_GZIP,2));
    arraySchema.set_coords_compressor(new Compressor(tiledb_compressor_t.TILEDB_GZIP,2));
    arraySchema.dump();

    System.out.println(arraySchema.getArrayType());
    arraySchema.free();
  }
}
