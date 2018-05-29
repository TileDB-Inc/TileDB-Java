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
import java.util.*;

public class TileDBOptions implements Serializable {
  public static final String ARRAY_URI_KEY = "arrayURI";
  public static final String DIMENSIONS_KEY = "dimensions";
  public static final String BATCH_SIZE_KEY = "batchSize";
  public static final String PARTITION_SIZE_KEY = "partitionSize";
  public static final String SUBARRAY_MIN_KEY = "subarray.{}.min";
  public static final String SUBARRAY_MAX_KEY = "subarray.{}.max";
  public static final String SUBARRAY_EXTENT_KEY = "subarray.{}.extent";

  public static final String DEFAULT_ARRAY_URI = "";
  public static final String DEFAULT_DIMENSIONS = "";
  public static final String DEFAULT_BATCH_SIZE = "5000";
  public static final String DEFAULT_PARTITION_SIZE = "100000";

  public String ARRAY_URI;
  public List<String> DIMENSIONS;
  public int BATCH_SIZE;
  public int PARTITION_SIZE;

  private final Map<String, String> options;

  public TileDBOptions(DataSourceOptions options){
    this.options = options.asMap();
    ARRAY_URI = options.get(ARRAY_URI_KEY).orElse(DEFAULT_ARRAY_URI);
    DIMENSIONS = Arrays.asList(options.get(DIMENSIONS_KEY).orElse(DEFAULT_DIMENSIONS).split(","));
    BATCH_SIZE = Integer.parseInt(options.get(BATCH_SIZE_KEY).orElse(DEFAULT_BATCH_SIZE));
    PARTITION_SIZE = Integer.parseInt(options.get(PARTITION_SIZE_KEY).orElse(DEFAULT_PARTITION_SIZE));
  }

  public Optional<String> get(String key) {
    return Optional.ofNullable(options.get(key));
  }
}
