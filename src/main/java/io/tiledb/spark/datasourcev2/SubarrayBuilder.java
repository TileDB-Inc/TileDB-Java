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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubarrayBuilder {
  public Filter[] filters;
  public ArrayList<Filter> pushedFilters;
  public ArrayList<Filter> notPushedFilters;
  private TileDBOptions options;
  private List<Dimension> dimensions;
  private Object subarray;
  private Context ctx;

  public SubarrayBuilder(Context ctx, DataSourceOptions dataSourceOptions) throws Exception {
    this.ctx = ctx;
    this.options = new TileDBOptions(dataSourceOptions);
    String arrayURI = options.ARRAY_URI;
    Array array = new Array(ctx, arrayURI);
    subarray = initSubarray(array.getSchema());
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

  private Object initSubarray(ArraySchema arraySchema) throws Exception {
    List<Dimension> dimensions = arraySchema.getDomain().getDimensions();
    switch (arraySchema.getDomain().getType()) {
      case TILEDB_FLOAT32: {
        float[] subarrayTmp = new float[dimensions.size()*2];
        int i = 0;
        for(Dimension dimension : dimensions){
          String name = dimension.getName();
          Pair domain = dimension.getDomain();
          float min = Math.max(
              Float.parseFloat(options.get(TileDBOptions.SUBARRAY_MIN_KEY.replace("{}",name))
                  .orElse(Float.MIN_VALUE + ""))
              ,(float) domain.getFirst());
          float max = Math.min(
              Float.parseFloat(options.get(TileDBOptions.SUBARRAY_MAX_KEY.replace("{}",name))
              .orElse(Float.MAX_VALUE + ""))
              ,(float) domain.getSecond());
          subarrayTmp[i] = min;
          subarrayTmp[i+1] = max;
          i+=2;
        }
        return subarrayTmp;
      }
      case TILEDB_FLOAT64: {
        double[] subarrayTmp = new double[dimensions.size()*2];
        int i = 0;
        for(Dimension dimension : dimensions){
          String name = dimension.getName();
          Pair domain = dimension.getDomain();
          double min = Math.max(
              Double.parseDouble(options.get(TileDBOptions.SUBARRAY_MIN_KEY.replace("{}",name))
                  .orElse(Double.MIN_VALUE + ""))
              ,(double) domain.getFirst());
          double max = Math.min(
              Double.parseDouble(options.get(TileDBOptions.SUBARRAY_MAX_KEY.replace("{}",name))
                  .orElse(Double.MAX_VALUE + ""))
              ,(double) domain.getSecond());
          subarrayTmp[i] = min;
          subarrayTmp[i+1] = max;
          i+=2;
        }
        return subarrayTmp;
      }
      case TILEDB_INT8:{
        byte[] subarrayTmp = new byte[dimensions.size()*2];
        int i = 0;
        for(Dimension dimension : dimensions){
          String name = dimension.getName();
          Pair domain = dimension.getDomain();
          int min = Math.max(
              Integer.parseInt(options.get(TileDBOptions.SUBARRAY_MIN_KEY.replace("{}",name))
                  .orElse("-128"))
              ,((Byte) domain.getFirst()).intValue());
          int max = Math.min(
              Integer.parseInt(options.get(TileDBOptions.SUBARRAY_MAX_KEY.replace("{}",name))
                  .orElse("128"))
              ,((Byte) domain.getSecond()).intValue());
          subarrayTmp[i] = (byte) min;
          subarrayTmp[i+1] = (byte) max;
          i+=2;
        }
        return subarrayTmp;
      }
      case TILEDB_UINT8:
      case TILEDB_INT16: {
        short[] subarrayTmp = new short[dimensions.size()*2];
        int i = 0;
        for(Dimension dimension : dimensions){
          String name = dimension.getName();
          Pair domain = dimension.getDomain();
          int min = Math.max(
              Short.parseShort(options.get(TileDBOptions.SUBARRAY_MIN_KEY.replace("{}",name))
                  .orElse(Short.MIN_VALUE + ""))
              ,(short) domain.getFirst());
          int max = Math.min(
              Short.parseShort(options.get(TileDBOptions.SUBARRAY_MAX_KEY.replace("{}",name))
                  .orElse(Short.MAX_VALUE + ""))
              ,(short) domain.getSecond());
          subarrayTmp[i] = (short) min;
          subarrayTmp[i+1] = (short) max;
          i+=2;
        }
        return subarrayTmp;
      }
      case TILEDB_UINT16:
      case TILEDB_INT32: {
        int[] subarrayTmp = new int[dimensions.size()*2];
        int i = 0;
        for(Dimension dimension : dimensions){
          String name = dimension.getName();
          Pair domain = dimension.getDomain();
          int min = Math.max(
              Integer.parseInt(options.get(TileDBOptions.SUBARRAY_MIN_KEY.replace("{}",name))
                  .orElse(Integer.MIN_VALUE + ""))
              ,(int) domain.getFirst());
          int max = Math.min(
              Integer.parseInt(options.get(TileDBOptions.SUBARRAY_MAX_KEY.replace("{}",name))
                  .orElse(Integer.MAX_VALUE + ""))
              ,(int) domain.getSecond());
          subarrayTmp[i] = min;
          subarrayTmp[i+1] = max;
          i+=2;
        }
        return subarrayTmp;
      }
      case TILEDB_INT64:
      case TILEDB_UINT32:
      case TILEDB_UINT64: {
        long[] subarrayTmp = new long[dimensions.size()*2];
        int i = 0;
        for(Dimension dimension : dimensions){
          String name = dimension.getName();
          Pair domain = dimension.getDomain();
          long min = Math.max(
              Long.parseLong(options.get(TileDBOptions.SUBARRAY_MIN_KEY.replace("{}",name))
                  .orElse(Long.MIN_VALUE + ""))
              ,((BigInteger) domain.getFirst()).longValue());
          long max = Math.min(
              Long.parseLong(options.get(TileDBOptions.SUBARRAY_MAX_KEY.replace("{}",name))
                  .orElse(Long.MAX_VALUE + ""))
              ,((BigInteger) domain.getSecond()).longValue());
          subarrayTmp[i] = min;
          subarrayTmp[i+1] = max;
          i+=2;
        }
        return subarrayTmp;
      }
      default: {
        throw new TileDBError("Not supported getDomain getType " + arraySchema.getDomain().getType());
      }
    }
  }

  public Filter[] getPushedFilters() {
    return pushedFilters.toArray(new Filter[pushedFilters.size()]);
  }

  public Filter[] getNotPushedFilters() {
    return notPushedFilters.toArray(new Filter[notPushedFilters.size()]);
  }

  public Object getSubArray() {
    return subarray;
  }
}
