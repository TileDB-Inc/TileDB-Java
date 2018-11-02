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

import static io.tiledb.java.api.Constants.TILEDB_VAR_NUM;

import io.tiledb.libtiledb.tiledb;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

  @Test
  public void testArraySchema() throws Exception {
    try (Context ctx = new Context();
        Attribute a = new Attribute(ctx, "a1", Long.class)) {
      try (FilterList filterList = new FilterList(ctx).addFilter(new GzipFilter(ctx, 1))) {
        a.setFilterList(filterList);
      }
      a.setCellValNum(tiledb.tiledb_var_num());
      Assert.assertEquals(a.getName(), "a1");
      Assert.assertEquals(a.getType(), Datatype.TILEDB_INT64);
      Assert.assertEquals(a.getCellValNum(), TILEDB_VAR_NUM);
      try (FilterList filterList = a.getFilterList()) {
        Assert.assertEquals(filterList.getNumFilters(), 1L);
        try (Filter filter = filterList.getFilter(0L)) {
          Assert.assertTrue(filter instanceof GzipFilter);
          Assert.assertEquals(((GzipFilter) filter).getLevel(), 1L);
        }
      }
    }
  }
}
