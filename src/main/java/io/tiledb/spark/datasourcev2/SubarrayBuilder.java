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
import org.apache.spark.sql.sources.*;
import org.apache.spark.sql.sources.v2.DataSourceOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubarrayBuilder {
  public Filter[] filters;
  public ArrayList<Filter> pushedFilters;
  public ArrayList<Filter> notPushedFilters;
  private TileDBOptions options;
  private List<Dimension> dimensions;

  public SubarrayBuilder(Context ctx, DataSourceOptions dataSourceOptions) throws TileDBError {
    this.options = new TileDBOptions(dataSourceOptions);
    String arrayURI = options.ARRAY_URI;
    Array array = new Array(ctx, arrayURI);
    dimensions = array.getSchema().getDomain().getDimensions();
    pushedFilters = new ArrayList<Filter>(1);
    notPushedFilters = new ArrayList<Filter>(1);
  }

  public void pushFilters(Filter[] filters) {
    this.filters = filters;
    for(Filter filter : filters){
      addFilter(filter);
    }
  }

  private void addFilter(Filter filter) {
    System.out.println("Filter: " + filter);
    for(String ref : filter.references())
      System.out.println("!!! "+ref);
    notPushedFilters.add(filter);
//    if (filter instanceof GreaterThan) {
//      GreaterThanOrEqual gt = (GreaterThanOrEqual) filter;
//      return gt.attribute().equals("i") && gt.value() instanceof Integer;
//    }

//      for(Filter filter : filters){
//        if (filter instanceof GreaterThan) {
//          GreaterThan gt = (GreaterThan) filter;
//          return gt.attribute().equals("i") && gt.value() instanceof Integer;
//        } else {
//          return false;
//        }
//      }
//      Filter[] supported = Arrays.stream(filters).filter(f -> {
//        if (f instanceof GreaterThan) {
//          GreaterThan gt = (GreaterThan) f;
//          return gt.attribute().equals("i") && gt.value() instanceof Integer;
//        } else {
//          return false;
//        }
//      }).toArray(Filter[]::new);
//
//      Filter[] unsupported = Arrays.stream(filters).filter(f -> {
//        if (f instanceof GreaterThan) {
//          GreaterThan gt = (GreaterThan) f;
//          return !gt.attribute().equals("i") || !(gt.value() instanceof Integer);
//        } else {
//          return true;
//        }
//      }).toArray(Filter[]::new);
//
//      this.filters = supported;
//      return unsupported;
  }

  public Filter[] getPushedFilters() {
    return pushedFilters.toArray(new Filter[pushedFilters.size()]);
  }

  public Filter[] getNotPushedFilters() {
    return notPushedFilters.toArray(new Filter[notPushedFilters.size()]);
  }

}
