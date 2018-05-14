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
import io.tiledb.libtiledb.tiledb_datatype_t;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import static org.apache.spark.sql.types.DataTypes.*;

public class TileDBSchemaConverter {

  private final Context ctx;
  private final TileDBOptions options;
  private final StructType requiredSchema;

  public TileDBSchemaConverter(Context ctx, DataSourceOptions options, StructType requiredSchema) {
    this.ctx = ctx;
    this.options = new TileDBOptions(options);
    this.requiredSchema = requiredSchema;
  }

  public StructType getSchema() throws TileDBError {
    String arrayURI = options.ARRAY_URI;
    Array array = new Array(ctx, arrayURI);
    ArraySchema arraySchema = array.getSchema();
    StructType schema = new StructType();
    for (Dimension dimension : arraySchema.getDomain().getDimensions()){
      if(requiredSchema==null || requiredSchema.getFieldIndex(dimension.getName()).isDefined()) {
        schema = schema.add(toStructField(dimension.getType(), 1l, dimension.getName()));
      }

    }
    for( Attribute attribute : arraySchema.getAttributes().values()){
      if(requiredSchema==null || requiredSchema.getFieldIndex(attribute.getName()).isDefined()) {
        schema = schema.add(toStructField(attribute.getType(), attribute.getCellValNum(), attribute.getName()));
      }
    }
    return schema;
  }

  private StructField toStructField(tiledb_datatype_t type, long cellValNum, String name) throws TileDBError {
    StructField field = null;
    switch (type) {
      case TILEDB_FLOAT32: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(FloatType), true, Metadata.empty());
        else
          field = new StructField(name, FloatType, true, Metadata.empty());
        break;
      }
      case TILEDB_FLOAT64: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(DoubleType), true, Metadata.empty());
        else
          field = new StructField(name, DoubleType, true, Metadata.empty());
        break;
      }
      case TILEDB_INT8: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(ByteType), true, Metadata.empty());
        else
          field = new StructField(name, ByteType, true, Metadata.empty());
        break;
      }
      case TILEDB_INT16: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(ShortType), true, Metadata.empty());
        else
          field = new StructField(name, ShortType, true, Metadata.empty());
        break;
      }
      case TILEDB_INT32: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(IntegerType), true, Metadata.empty());
        else
          field = new StructField(name, IntegerType, true, Metadata.empty());
        break;
      }
      case TILEDB_INT64: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(LongType), true, Metadata.empty());
        else
          field = new StructField(name, LongType, true, Metadata.empty());
        break;
      }
      case TILEDB_UINT8: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(ShortType), true, Metadata.empty());
        else
          field = new StructField(name, ShortType, true, Metadata.empty());
        break;
      }
      case TILEDB_UINT16: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(IntegerType), true, Metadata.empty());
        else
          field = new StructField(name, IntegerType, true, Metadata.empty());
        break;
      }
      case TILEDB_UINT32: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(LongType), true, Metadata.empty());
        else
          field = new StructField(name, LongType, true, Metadata.empty());
        break;
      }
      case TILEDB_UINT64: {
        if (cellValNum > 1)
          field = new StructField(name, DataTypes.createArrayType(LongType), true, Metadata.empty());
        else
          field = new StructField(name, LongType, true, Metadata.empty());
        break;
      }
      case TILEDB_CHAR: {
        field = new StructField(name, StringType, true, Metadata.empty());
        break;
      }
      default: {
        throw new TileDBError("Not supported getDomain getType " + type);
      }
    }
    return field;
  }
}
