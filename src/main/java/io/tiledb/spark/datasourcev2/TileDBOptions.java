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

import org.apache.spark.sql.sources.v2.DataSourceOptions;

import java.io.Serializable;

public class TileDBOptions implements Serializable {
  private static final String ARRAY_URI_KEY = "arrayURI";
  private static final String BATCH_SIZE_KEY = "batchSize";
  private static final String SUBARRAY_KEY = "subarray";

  private static final String DEFAULT_ARRAY_URI = "";
  private static final String DEFAULT_BATCH_SIZE = "5";
  private static final String DEFAULT_SUBARRAY = "";

  public String ARRAY_URI;
  public int BATCH_SIZE;
  public long[] subarray;

  public TileDBOptions(DataSourceOptions options){
    ARRAY_URI = options.get(ARRAY_URI_KEY).orElse(DEFAULT_ARRAY_URI);
    BATCH_SIZE = Integer.parseInt(options.get(BATCH_SIZE_KEY).orElse(DEFAULT_BATCH_SIZE));
    subarray = parseLineToLongArray(options.get(SUBARRAY_KEY).orElse(DEFAULT_SUBARRAY));
  }

  private long[] toLongArray(String[] arr) {
    long[] ints = new long[arr.length];
    for (int i = 0; i < arr.length; i++) {
      ints[i] = Long.parseLong(arr[i]);
    }
    return ints;
  }

  private long[] parseLineToLongArray(String line) {
    if(line.isEmpty())
      return null;
    return toLongArray(line.split(","));
  }
}
