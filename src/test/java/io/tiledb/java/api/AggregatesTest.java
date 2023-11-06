package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class AggregatesTest {
  private static Context ctx;

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private String arrayURI;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toPath().resolve("query").toString();
    arrayCreate();
    arrayWrite();
  }

  public void arrayCreate() throws Exception {
    // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
    Dimension<Integer> rows =
        new Dimension<>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols =
        new Dimension<>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);

    Attribute a2 = new Attribute(ctx, "a2", Datatype.TILEDB_INT32);
    //    a2.setNullable(true);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a2);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {
    // Prepare cell buffers
    NativeArray a2 =
        new NativeArray(
            ctx,
            new int[] {
              1, 2, 3, 4, 5, 6, 7, 8,
              9, 10, 11, 12, 13, 14, 15, 16
            },
            Datatype.TILEDB_INT32);

    NativeArray a2Validity =
        new NativeArray(
            ctx,
            new short[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            Datatype.TILEDB_UINT8);
    // Create query
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
        Query query = new Query(array)) {
      query.setLayout(TILEDB_ROW_MAJOR);
      query.setDataBuffer("a2", a2);
      //      query.setValidityBuffer("a2", a2Validity);
      // Submit query
      query.submit();
    }
  }

  @Test
  public void testSUM() throws TileDBError {
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
        Query query = new Query(array, TILEDB_READ)) {

      SubArray subArray = new SubArray(ctx, array);
      subArray.addRange(0, 1, 4, null);
      subArray.addRange(1, 1, 4, null);

      query.setSubarray(subArray);
      query.setLayout(TILEDB_ROW_MAJOR);

      ByteBuffer a2Array = ByteBuffer.allocateDirect(8);
      a2Array.order(ByteOrder.nativeOrder());

      ChannelOperator operator =
          new ChannelOperator(ctx, ChannelOperator.AggregationOperator.TILEDB_SUM);
      ChannelOperation operation = new ChannelOperation(ctx, operator, query, "a2");
      QueryChannel queryChannel = query.getDefaultChannel();
      queryChannel.applyAggregate("a2", operation);

      query.setDataBuffer("a2", a2Array);

      // Submit query
      query.submit();

      Assert.assertEquals(136, a2Array.getLong(0));
    }
  }

  @Test
  public void testMIN() throws TileDBError {
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
        Query query = new Query(array, TILEDB_READ)) {

      SubArray subArray = new SubArray(ctx, array);
      subArray.addRange(0, 1, 4, null);
      subArray.addRange(1, 1, 4, null);

      query.setSubarray(subArray);
      query.setLayout(TILEDB_ROW_MAJOR);

      ByteBuffer a2Array = ByteBuffer.allocateDirect(4);
      a2Array.order(ByteOrder.nativeOrder());

      ChannelOperator operator =
          new ChannelOperator(ctx, ChannelOperator.AggregationOperator.TILEDB_MIN);
      ChannelOperation operation = new ChannelOperation(ctx, operator, query, "a2");
      QueryChannel queryChannel = query.getDefaultChannel();
      queryChannel.applyAggregate("a2", operation);

      query.setDataBuffer("a2", a2Array);

      // Submit query
      query.submit();

      Assert.assertEquals(1, a2Array.getInt(0));
    }
  }

  @Test
  public void testMAX() throws TileDBError {
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
        Query query = new Query(array, TILEDB_READ)) {

      SubArray subArray = new SubArray(ctx, array);
      subArray.addRange(0, 1, 4, null);
      subArray.addRange(1, 1, 4, null);

      query.setSubarray(subArray);
      query.setLayout(TILEDB_ROW_MAJOR);

      ByteBuffer a2Array = ByteBuffer.allocateDirect(4);
      a2Array.order(ByteOrder.nativeOrder());

      ChannelOperator operator =
          new ChannelOperator(ctx, ChannelOperator.AggregationOperator.TILEDB_MAX);
      ChannelOperation operation = new ChannelOperation(ctx, operator, query, "a2");
      QueryChannel queryChannel = query.getDefaultChannel();
      queryChannel.applyAggregate("a2", operation);

      query.setDataBuffer("a2", a2Array);

      // Submit query
      query.submit();

      Assert.assertEquals(16, a2Array.getInt(0));
    }
  }

  @Test
  public void testMEAN() throws TileDBError {
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
        Query query = new Query(array, TILEDB_READ)) {

      SubArray subArray = new SubArray(ctx, array);
      subArray.addRange(0, 1, 4, null);
      subArray.addRange(1, 1, 4, null);

      query.setSubarray(subArray);
      query.setLayout(TILEDB_ROW_MAJOR);

      ByteBuffer a2Array = ByteBuffer.allocateDirect(8);
      a2Array.order(ByteOrder.nativeOrder());

      ChannelOperator operator =
          new ChannelOperator(ctx, ChannelOperator.AggregationOperator.TILEDB_MEAN);
      ChannelOperation operation = new ChannelOperation(ctx, operator, query, "a2");
      QueryChannel queryChannel = query.getDefaultChannel();
      queryChannel.applyAggregate("a2", operation);

      query.setDataBuffer("a2", a2Array);

      // Submit query
      query.submit();

      Assert.assertEquals(8.5, a2Array.getDouble(0), 0);
    }
  }

  @Test
  public void testNULLCount() throws TileDBError {
    //    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
    //        Query query = new Query(array, TILEDB_READ)) {
    //
    //      SubArray subArray = new SubArray(ctx, array);
    //      subArray.addRange(0, 1, 4, null);
    //      subArray.addRange(1, 1, 4, null);
    //
    //      query.setSubarray(subArray);
    //      query.setLayout(TILEDB_ROW_MAJOR);
    //
    //      ByteBuffer a2Array = ByteBuffer.allocateDirect(8);
    //      a2Array.order(ByteOrder.nativeOrder());
    //
    //      ChannelOperator operator =
    //          new ChannelOperator(ctx, ChannelOperator.AggregationOperator.TILEDB_NULL_COUNT);
    //      ChannelOperation operation = new ChannelOperation(ctx, operator, query, "a2");
    //      QueryChannel queryChannel = query.getDefaultChannel();
    //      queryChannel.applyAggregate("a2", operation);
    //
    //      query.setDataBuffer("a2", a2Array);
    //
    //      // Submit query
    //      query.submit();
    //
    //      Assert.assertEquals(1, a2Array.getInt(0));
    //    }
  }

  @Test
  public void testCOUNT() {
    // TODO
  }
}
