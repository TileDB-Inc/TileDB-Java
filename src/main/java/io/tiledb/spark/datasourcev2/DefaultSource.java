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

import io.tiledb.java.api.Context;
import io.tiledb.java.api.TileDBError;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.sources.v2.DataSourceV2;
import org.apache.spark.sql.sources.v2.ReadSupport;
import org.apache.spark.sql.sources.v2.reader.*;
import org.apache.spark.sql.types.*;
import org.apache.spark.sql.vectorized.ColumnarBatch;

import java.util.List;


public class DefaultSource implements DataSourceV2, ReadSupport {
  class Reader implements DataSourceReader, SupportsPushDownRequiredColumns, SupportsScanColumnarBatch {
    private Context ctx;
    private DataSourceOptions options;
    private StructType requiredSchema;

    public Reader(DataSourceOptions options) {
      try {
        this.options = options;
        ctx = new Context();
      } catch (TileDBError tileDBError) {
        tileDBError.printStackTrace();
      }
    }

    @Override
    public StructType readSchema() {
      try {
        TileDBSchemaConverter tileDBSchemaConverter = new TileDBSchemaConverter(ctx, options, requiredSchema);
        return tileDBSchemaConverter.getSchema();
      } catch (TileDBError tileDBError) {
        tileDBError.printStackTrace();
        return null;
      }
    }

    public List<DataReaderFactory<ColumnarBatch>> createBatchDataReaderFactories() {

      return java.util.Arrays.asList(
          new TileDBReaderFactory(new long[]{1l, 2l, 1l, 4l}, requiredSchema, options),
          new TileDBReaderFactory(new long[]{3l, 4l, 1l, 4l}, requiredSchema, options));
    }

    @Override
    public void pruneColumns(StructType requiredSchema) {
      System.out.println("pruning: "+requiredSchema);
      this.requiredSchema = requiredSchema;
    }
  }

  @Override
  public DataSourceReader createReader(DataSourceOptions options) {
    return new DefaultSource.Reader(options);
  }
}