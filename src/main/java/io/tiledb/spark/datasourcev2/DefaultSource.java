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
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_query_type_t;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.sources.Filter;
import org.apache.spark.sql.sources.GreaterThan;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.sources.v2.DataSourceV2;
import org.apache.spark.sql.sources.v2.ReadSupport;
import org.apache.spark.sql.sources.v2.WriteSupport;
import org.apache.spark.sql.sources.v2.reader.*;
import org.apache.spark.sql.sources.v2.writer.DataSourceWriter;
import org.apache.spark.sql.sources.v2.writer.DataWriter;
import org.apache.spark.sql.sources.v2.writer.DataWriterFactory;
import org.apache.spark.sql.sources.v2.writer.WriterCommitMessage;
import org.apache.spark.sql.types.*;
import org.apache.spark.sql.vectorized.ColumnarBatch;

import java.io.IOException;
import java.util.*;


public class DefaultSource implements DataSourceV2, ReadSupport, WriteSupport {

  class Reader implements DataSourceReader, SupportsPushDownRequiredColumns, SupportsScanColumnarBatch, SupportsPushDownFilters {
    private Context ctx;
    private DataSourceOptions options;
    private StructType requiredSchema;
    private SubarrayBuilder subarrayBuilder;

    public Reader(DataSourceOptions options) {
      try {
        this.options = options;
        ctx = new Context();
        subarrayBuilder = new SubarrayBuilder(ctx, options);
      } catch (Exception tileDBError) {
        tileDBError.printStackTrace();
      }
    }

    @Override
    public StructType readSchema() {
      try {
        TileDBSchemaConverter tileDBSchemaConverter = new TileDBSchemaConverter(ctx, options);
        tileDBSchemaConverter.setRequiredSchema(requiredSchema);
        return tileDBSchemaConverter.getSchema();
      } catch (TileDBError tileDBError) {
        tileDBError.printStackTrace();
        return null;
      }
    }

    @Override
    public List<DataReaderFactory<ColumnarBatch>> createBatchDataReaderFactories() {
      List<Object> partitions = new ArrayList<>();
      try {
        partitions = getSubarrayPartitions(subarrayBuilder.getSubArray(), requiredSchema, options);
      } catch (Exception e) {
        e.printStackTrace();
      }
      if(partitions.isEmpty()) {
        return java.util.Arrays.asList(
            new TileDBReaderFactory(subarrayBuilder.getSubArray(), requiredSchema, options));
      }
      else{
        List<DataReaderFactory<ColumnarBatch>> ret = new ArrayList<>(partitions.size());
        for(Object partition : partitions){
          ret.add(new TileDBReaderFactory(partition, requiredSchema, options));
        }
        return ret;
      }
    }

    private List<Object> getSubarrayPartitions(Object subarray, StructType requiredSchema, DataSourceOptions options) throws Exception {
      TileDBReaderFactory readerFactory = new TileDBReaderFactory(subarray, requiredSchema, options, true);
      readerFactory.next();
      List<Object> ret = readerFactory.getPartitions();
      readerFactory.close();
      return ret;
    }

    @Override
    public void pruneColumns(StructType requiredSchema) {
      this.requiredSchema = requiredSchema;
    }

    @Override
    public Filter[] pushFilters(Filter[] filters) {
      subarrayBuilder.pushFilters(filters);
      return subarrayBuilder.getNotPushedFilters();
    }

    @Override
    public Filter[] pushedFilters() {
      return subarrayBuilder.getPushedFilters();
    }
  }

  @Override
  public DataSourceReader createReader(DataSourceOptions options) {
    return new DefaultSource.Reader(options);
  }

  @Override
  public Optional<DataSourceWriter> createWriter(String jobId, StructType schema, SaveMode mode, DataSourceOptions options) {
    return TileDBWriterFactory.getWriter(jobId, schema, mode, options);
  }

}