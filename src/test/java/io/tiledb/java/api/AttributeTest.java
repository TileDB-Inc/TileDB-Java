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

import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_compressor_t;
import org.junit.Test;

public class AttributeTest {

  @Test
  public void testArraySchema() throws Exception {
    Context ctx = new Context();
    Attribute a = new Attribute(ctx, "a1", Long.class);
    System.out.println(a.getName());
    System.out.println(a.getType());
    a.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_GZIP, 1));

    a.setCellValNum(tiledb.tiledb_var_num());
    System.out.println(a.getCellValNum());
    System.out.println(a.getCellSize());

    System.out.println(a.getCompressor());
    System.out.println(a);
  }
}
