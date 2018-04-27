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

import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRow;
import org.apache.spark.sql.sources.Filter;
import org.apache.spark.sql.sources.GreaterThan;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.sources.v2.DataSourceV2;
import org.apache.spark.sql.sources.v2.ReadSupport;
import org.apache.spark.sql.sources.v2.reader.*;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultSourceAdvanced implements DataSourceV2, ReadSupport {

  @Override
  public DataSourceReader createReader(DataSourceOptions options) {
    return new Reader();
  }

  static class JavaAdvancedDataReaderFactory implements DataReaderFactory<Row>, DataReader<Row> {
    private int start;
    private int end;
    private StructType requiredSchema;

    JavaAdvancedDataReaderFactory(int start, int end, StructType requiredSchema) {
      this.start = start;
      this.end = end;
      this.requiredSchema = requiredSchema;
    }

    @Override
    public DataReader<Row> createDataReader() {
      return new JavaAdvancedDataReaderFactory(start - 1, end, requiredSchema);
    }

    @Override
    public boolean next() {
      start += 1;
      return start < end;
    }

    @Override
    public Row get() {
      Object[] values = new Object[requiredSchema.size()];
      for (int i = 0; i < values.length; i++) {
        if ("i".equals(requiredSchema.apply(i).name())) {
          values[i] = start;
        } else if ("j".equals(requiredSchema.apply(i).name())) {
          values[i] = -start;
        }
      }
      return new GenericRow(values);
    }

    @Override
    public void close() throws IOException {

    }
  }

  public class Reader implements DataSourceReader, SupportsPushDownRequiredColumns,
      SupportsPushDownFilters {

    // Exposed for testing.
    public StructType requiredSchema = new StructType().add("i", "int").add("j", "int");
    public Filter[] filters = new Filter[0];

    @Override
    public StructType readSchema() {
      return requiredSchema;
    }

    @Override
    public void pruneColumns(StructType requiredSchema) {
      this.requiredSchema = requiredSchema;
    }

    @Override
    public Filter[] pushFilters(Filter[] filters) {
      Filter[] supported = Arrays.stream(filters).filter(f -> {
        if (f instanceof GreaterThan) {
          GreaterThan gt = (GreaterThan) f;
          return gt.attribute().equals("i") && gt.value() instanceof Integer;
        } else {
          return false;
        }
      }).toArray(Filter[]::new);

      Filter[] unsupported = Arrays.stream(filters).filter(f -> {
        if (f instanceof GreaterThan) {
          GreaterThan gt = (GreaterThan) f;
          return !gt.attribute().equals("i") || !(gt.value() instanceof Integer);
        } else {
          return true;
        }
      }).toArray(Filter[]::new);

      this.filters = supported;
      return unsupported;
    }

    @Override
    public Filter[] pushedFilters() {
      return filters;
    }

    @Override
    public List<DataReaderFactory<Row>> createDataReaderFactories() {
      List<DataReaderFactory<Row>> res = new ArrayList<>();

      Integer lowerBound = null;
      for (Filter filter : filters) {
        if (filter instanceof GreaterThan) {
          GreaterThan f = (GreaterThan) filter;
          if ("i".equals(f.attribute()) && f.value() instanceof Integer) {
            lowerBound = (Integer) f.value();
            break;
          }
        }
      }

      if (lowerBound == null) {
        res.add(new JavaAdvancedDataReaderFactory(0, 5, requiredSchema));
        res.add(new JavaAdvancedDataReaderFactory(5, 10, requiredSchema));
      } else if (lowerBound < 4) {
        res.add(new JavaAdvancedDataReaderFactory(lowerBound + 1, 5, requiredSchema));
        res.add(new JavaAdvancedDataReaderFactory(5, 10, requiredSchema));
      } else if (lowerBound < 9) {
        res.add(new JavaAdvancedDataReaderFactory(lowerBound + 1, 10, requiredSchema));
      }

      return res;
    }
  }
}