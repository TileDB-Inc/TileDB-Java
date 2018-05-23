package io.tiledb.spark.datasourcev2;

import io.tiledb.java.api.*;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_query_type_t;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.sources.v2.DataSourceOptions;
import org.apache.spark.sql.sources.v2.writer.DataSourceWriter;
import org.apache.spark.sql.sources.v2.writer.DataWriter;
import org.apache.spark.sql.sources.v2.writer.DataWriterFactory;
import org.apache.spark.sql.sources.v2.writer.WriterCommitMessage;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.vectorized.ColumnarBatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TileDBWriterFactory implements DataWriterFactory, DataSourceWriter {
  private transient Context ctx;
  private String jobId;
  private StructType schema;
  private boolean createTable, deleteTable;
  private TileDBOptions tileDBOptions;

  public TileDBWriterFactory(String jobId, StructType schema, boolean createTable, boolean deleteTable, TileDBOptions tileDBOptions) {
    this.jobId = jobId;
    this.schema = schema;
    this.createTable = createTable;
    this.deleteTable = deleteTable;
    this.tileDBOptions = tileDBOptions;
  }

  public TileDBWriterFactory(String jobId, StructType schema, boolean createTable, boolean deleteTable, TileDBOptions tileDBOptions, boolean init) throws Exception {
    this.jobId = jobId;
    this.schema = schema;
    this.createTable = createTable;
    this.deleteTable = deleteTable;
    this.tileDBOptions = tileDBOptions;
    this.ctx = new Context();
    if(init) {
      if (deleteTable)
        deleteTable();
      if (createTable)
        createTable();
    }
  }

  public static Optional<DataSourceWriter> getWriter(String jobId, StructType schema, SaveMode mode, DataSourceOptions options) {
    boolean createTable = false, deleteTable = false;
    TileDBOptions tileDBOptions = null;
    Context ctx = null;
    try {
      ctx = new Context();
      tileDBOptions = new TileDBOptions(options);
      Array array = new Array(ctx, tileDBOptions.ARRAY_URI);
    } catch (Exception tileDBError) {
      if(tileDBError.getMessage().contains("Schema file not found"))
        createTable = true;
    }
    try {
      switch (mode){
        case Append:
          return Optional.of(new TileDBWriterFactory(jobId, schema, createTable, false, tileDBOptions, true));
        case Overwrite:
          if(!createTable)
            return Optional.of(new TileDBWriterFactory(jobId, schema, true, true, tileDBOptions, true));
          else
            return Optional.of(new TileDBWriterFactory(jobId, schema, true, false, tileDBOptions, true));
        case ErrorIfExists:
          if(!createTable)
            return Optional.empty();
          else
            return Optional.of(new TileDBWriterFactory(jobId, schema, createTable, deleteTable, tileDBOptions, true));
        case Ignore:
          if(!createTable)
            return Optional.empty();
          else
            return Optional.of(new TileDBWriterFactory(jobId, schema, createTable, deleteTable, tileDBOptions, true));
      }
    } catch (Exception tileDBError) {
      if(tileDBError.getMessage().contains("Schema file not found"))
        createTable = true;
    }
    return Optional.empty();
  }

  @Override
  public DataWriter<Row> createDataWriter(int partitionId, int attemptNumber) {
    return new TileDBWriter(schema, tileDBOptions);
  }

  @Override
  public DataWriterFactory createWriterFactory() {
    return new TileDBWriterFactory(jobId, schema, createTable, deleteTable, tileDBOptions);
  }

  @Override
  public void commit(WriterCommitMessage[] messages) {
  }

  @Override
  public void abort(WriterCommitMessage[] messages) {
  }

  private void deleteTable() throws Exception{
    TileDBObject.remove(ctx, tileDBOptions.ARRAY_URI);
  }

  private void createTable() throws Exception {
    TileDBSchemaConverter tileDBSchemaConverter = new TileDBSchemaConverter(ctx, tileDBOptions);
    ArraySchema arraySchema = tileDBSchemaConverter.toTileDBSchema(schema);
    new Array(ctx, tileDBOptions.ARRAY_URI, arraySchema);
  }


  class TileDBWriter implements DataWriter<Row> {
    private TileDBOptions options;
    private StructType schema;
    private Array array;
    private ArraySchema arraySchema;
    private SubarrayBuilder subarrayBuilder;
    private Query query;
    private List<String> dimensionNames, attributeNames;

    public TileDBWriter(StructType schema, TileDBOptions tileDBOptions)  {
      this.schema = schema;
      this.options = tileDBOptions;
      try {
        init();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    private void init() throws Exception {
      ctx = new Context();
      subarrayBuilder = new SubarrayBuilder(ctx, options);
      array = new Array(ctx, options.ARRAY_URI);
      arraySchema = array.getSchema();

      // Create query
      query = new Query(array, tiledb_query_type_t.TILEDB_READ);
      NativeArray nsubarray = new NativeArray(ctx, subarrayBuilder.getSubArray(), arraySchema.getDomain().getType());
      query.setSubarray(nsubarray);

      attributeNames = new ArrayList<>();
      for(Attribute attribute : arraySchema.getAttributes().values()){
        String name = attribute.getName();
        attributeNames.add(name);
        long cellValNum = arraySchema.getAttribute(name).getCellValNum();
        if(cellValNum == tiledb.tiledb_var_num()){
          query.setBuffer(name,
              new NativeArray(ctx, options.BATCH_SIZE, Long.class),
              new NativeArray(ctx, options.BATCH_SIZE*5, arraySchema.getAttribute(name).getType()));
        } else {
          query.setBuffer(name,
              new NativeArray(ctx, options.BATCH_SIZE * cellValNum, arraySchema.getAttribute(name).getType()));
        }
      }
      query.setCoordinates(new NativeArray(ctx, options.BATCH_SIZE * arraySchema.getDomain().getDimensions().size(),
          arraySchema.getDomain().getType()));
      dimensionNames = new ArrayList<>();
      for(Dimension dimension : arraySchema.getDomain().getDimensions()){
        dimensionNames.add(dimension.getName());
      }
    }

    @Override
    public void write(Row record) throws IOException {
      for(String dimension : dimensionNames){
        long coord = record.getAs(dimension);

      }
      System.out.println(record.toString());
    }

    @Override
    public WriterCommitMessage commit() throws IOException {
      return null;
    }

    @Override
    public void abort() throws IOException {
    }
  }
}
