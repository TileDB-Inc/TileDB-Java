/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 TileDB, Inc.
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

package examples.io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.Constants.TILEDB_VAR_NUM;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;

import io.tiledb.java.api.*;

public class ArrayDenseCreate {
  public static void main(String[] args) throws Exception {

    // Create TileDB context
    System.out.println("context before");

    Context ctx = new Context();
    System.out.println("context done");
    // Create getDimensions
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Dimension<Long> d2 =
        new Dimension<Long>(ctx, "d2", Long.class, new Pair<Long, Long>(1l, 4l), 2l);

    System.out.println("dim done");

    // Create getDomain
    Domain domain = new Domain(ctx);
    System.out.println("domain done");
    domain.addDimension(d1);
    domain.addDimension(d2);

    System.out.println("added dims");

    // Create and add getAttributes
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    Attribute a2 = new Attribute(ctx, "a2", String.class);
    a2.setCellValNum(TILEDB_VAR_NUM);
    Attribute a3 = new Attribute(ctx, "a3", Float.class);
    a3.setCellValNum(2);
    a1.setFilterList(new FilterList(ctx).addFilter(new LZ4Filter(ctx)));
    a2.setFilterList(new FilterList(ctx).addFilter(new GzipFilter(ctx)));
    a3.setFilterList(new FilterList(ctx).addFilter(new ZstdFilter(ctx)));

    // Create array schema
    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);
    schema.addAttribute(a3);

    // Check array schema
    schema.check();

    // Print array schema contents
    schema.dump();

    Array.create("my_dense_array", schema);
  }
}
