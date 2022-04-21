package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GroupTest {
  private Context ctx;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    if (Files.exists(Paths.get("test_group"))) {
      TileDBObject.remove(ctx, "test_group");
    }
    if (Files.exists(Paths.get("array1"))) {
      TileDBObject.remove(ctx, "array1");
    }
    if (Files.exists(Paths.get("array2"))) {
      TileDBObject.remove(ctx, "array2");
    }
    if (Files.exists(Paths.get("array3"))) {
      TileDBObject.remove(ctx, "array3");
    }
  }

  @After
  public void teardown() throws Exception {
    if (Files.exists(Paths.get("test_group"))) {
      TileDBObject.remove(ctx, "test_group");
    }
    if (Files.exists(Paths.get("array1"))) {
      TileDBObject.remove(ctx, "array1");
    }
    if (Files.exists(Paths.get("array2"))) {
      TileDBObject.remove(ctx, "array2");
    }
    if (Files.exists(Paths.get("array3"))) {
      TileDBObject.remove(ctx, "array3");
    }
  }

  public void arrayCreate(String arrayURI) throws Exception {
    // Create getDimensions
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 3l), 2l);
    Dimension<Long> d2 =
        new Dimension<Long>(ctx, "d2", Long.class, new Pair<Long, Long>(1l, 3l), 2l);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);
    domain.addDimension(d2);

    // Create and add getAttributes
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    a1.setNullable(true);
    Attribute a2 = new Attribute(ctx, "a2", Float.class);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);

    schema.check();

    Array.create(arrayURI, schema);
  }

  public void arrayWrite(String arrayURI) throws Exception {

    Array my_dense_array = new Array(ctx, arrayURI, TILEDB_WRITE);

    // Prepare cell buffers
    NativeArray a1_data =
        new NativeArray(ctx, new int[] {8, 9, 10, 11, 12, 13, 14, 15, 16}, Integer.class);

    NativeArray buffer_a2 =
        new NativeArray(
            ctx,
            new float[] {13.2f, 14.1f, 14.2f, 15.1f, 15.2f, 15.3f, 16.1f, 18.3f, 19.1f},
            Float.class);

    // Create query
    NativeArray a1Bytemap =
        new NativeArray(ctx, new short[] {0, 1, 1, 1, 1, 0, 1, 1, 0}, Datatype.TILEDB_UINT8);
    try (Query query = new Query(my_dense_array, TILEDB_WRITE)) {
      query
          .setLayout(TILEDB_ROW_MAJOR)
          .setBufferNullable("a1", a1_data, a1Bytemap)
          .setBuffer("a2", buffer_a2);
      // Submit query
      query.submit();
      query.finalizeQuery();
    }
  }

  @Test
  public void membersTest() throws Exception {
    arrayCreate("array1");
    arrayWrite("array1");

    arrayCreate("array2");
    arrayWrite("array2");

    arrayCreate("array3");
    arrayWrite("array3");

    Group.create(ctx, "test_group");
    Group group = new Group(ctx, "test_group", TILEDB_WRITE);
    group.addMember("array1", false, "array1Name");
    group.addMember("array2", false, "array2Name");
    group.addMember("array3", false, "array3Name");

    // reopen group
    group.reopen(ctx, QueryType.TILEDB_READ);
    Assert.assertEquals(3, group.getMemberCount());

    // remove a member
    group.reopen(ctx, TILEDB_WRITE);
    group.removeMember("array2");

    // check if member is removed
    group.reopen(ctx, QueryType.TILEDB_READ);
    Assert.assertEquals(2, group.getMemberCount());

    group.close();
  }

  @Test
  public void metadataTest() throws TileDBError {
    Group.create(ctx, "test_group");
    Group group = new Group(ctx, "test_group", TILEDB_WRITE);

    NativeArray shortArray = new NativeArray(ctx, new short[] {18, 19, 20, 21}, Short.class);
    NativeArray longArray = new NativeArray(ctx, new long[] {8L, 9L, 0L, 1L}, Long.class);
    NativeArray intArray = new NativeArray(ctx, new int[] {7, 1, 3, 2}, Integer.class);

    // put metadata in closed group
    try {
      group.putMetadata("key1", shortArray);
      throw new TileDBError("Put metadata in closed group");
    } catch (TileDBError error) {
    }

    // put metadata in open group
    group.putMetadata("key1", shortArray);
    group.putMetadata("key2", longArray);
    group.putMetadata("key3", intArray);

    // close group and reopen in read mode
    group.reopen(ctx, TILEDB_READ);

    // check if the correct metadata keys are present
    Assert.assertEquals(BigInteger.valueOf(3), group.getMetadataNum());
    Assert.assertTrue(group.hasMetadataKey("key1"));
    Assert.assertTrue(group.hasMetadataKey("key2"));
    Assert.assertTrue(group.hasMetadataKey("key3"));
    Assert.assertFalse(group.hasMetadataKey("key4"));

    // check values of metadata keys
    NativeArray metadataShortArray = group.getMetadata("key1", Datatype.TILEDB_INT16);
    NativeArray metadataLongArray = group.getMetadata("key2", Datatype.TILEDB_INT64);
    NativeArray metadataIntArray = group.getMetadata("key3", Datatype.TILEDB_INT32);
    Assert.assertNotNull(metadataShortArray);
    Assert.assertNotNull(metadataIntArray);
    Assert.assertNotNull(metadataLongArray);
    Assert.assertArrayEquals(
        (short[]) shortArray.toJavaArray(), (short[]) metadataShortArray.toJavaArray());
    Assert.assertArrayEquals(
        (long[]) longArray.toJavaArray(), (long[]) metadataLongArray.toJavaArray());
    Assert.assertArrayEquals(
        (int[]) intArray.toJavaArray(), (int[]) metadataIntArray.toJavaArray());

    // close group and reopen in write mode
    group.reopen(ctx, TILEDB_WRITE);
    // delete a metadata entry
    group.deleteMetadata("key2");
    // close group and reopen in read mode to check if the correct metadata keys are present.

    group.reopen(ctx, TILEDB_READ);
    Assert.assertEquals(BigInteger.valueOf(2), group.getMetadataNum());
    Assert.assertTrue(group.hasMetadataKey("key1"));
    Assert.assertFalse(group.hasMetadataKey("key2"));
    Assert.assertTrue(group.hasMetadataKey("key3"));
    Assert.assertFalse(group.hasMetadataKey("key4"));

    group.close();
  }
}
