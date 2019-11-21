package io.tiledb.java.api;

import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Arrays;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class ArrayTest {

  private Context ctx;
  private String arrayURI;
  private String attributeName;
  private byte[] key;

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toString();
    attributeName = "a1";
    String keyString = "0123456789abcdeF0123456789abcdeF";
    key = keyString.getBytes(StandardCharsets.US_ASCII);
  }

  @After
  public void tearDown() throws Exception {
    ctx.close();
  }

  public ArraySchema schemaCreate() throws Exception {
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Attribute a1 = new Attribute(ctx, attributeName, Long.class);
    ArraySchema schema = new ArraySchema(ctx, ArrayType.TILEDB_DENSE);
    schema.setTileOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setCellOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.check();
    return schema;
  }

  public void insertArbitraryValuesMeth(Array array, NativeArray a_data) throws TileDBError {
    // Create query
    try (Query query = new Query(array, TILEDB_WRITE)) {
      query.setLayout(TILEDB_ROW_MAJOR).setBuffer(attributeName, a_data);
      query.submit();
    }
    array.close();
  }

  public void insertArbitraryValues(NativeArray a_data) throws TileDBError {
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
    insertArbitraryValuesMeth(array, a_data);
    array.close();
  }

  public void insertArbitraryValuesEncrypted(NativeArray a_data) throws TileDBError {
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE, EncryptionType.TILEDB_AES_256_GCM, key);
    insertArbitraryValuesMeth(array, a_data);
    array.close();
  }

  public long[] readArray(Array array) throws TileDBError {
    NativeArray sub_array = new NativeArray(ctx, new long[] {1, 4, 1, 2}, Long.class);
    // Create query
    Query query = new Query(array, TILEDB_READ);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setSubarray(sub_array);
    query.setBuffer(attributeName, new NativeArray(ctx, 10, Long.class));

    // Submit query
    query.submit();

    long[] a_buff = (long[]) query.getBuffer(attributeName);

    query.close();
    array.close();

    return a_buff;
  }

  public long[] readArray() throws TileDBError {
    return readArray(new Array(ctx, arrayURI));
  }

  public long[] readArrayAt(BigInteger timestamp) throws TileDBError {
    return readArray(new Array(ctx, arrayURI, timestamp));
  }

  public long[] readArrayAtEncrypted(BigInteger timestamp) throws TileDBError {
    return readArray(
        new Array(ctx, arrayURI, TILEDB_READ, EncryptionType.TILEDB_AES_256_GCM, key, timestamp));
  }

  @Test
  public void testArrayExists() throws Exception {
    // Test that we can create an array
    Assert.assertFalse(Array.exists(ctx, arrayURI));
    Array.create(arrayURI, schemaCreate());
    Assert.assertTrue(Array.exists(ctx, arrayURI));

    // Test that we can load the schema
    ArraySchema schema = new ArraySchema(ctx, arrayURI);
    Assert.assertEquals(schema.getArrayType(), ArrayType.TILEDB_DENSE);
  }

  @Test
  public void testEncryptedArrayExists() throws Exception {
    // Test that we can create the encrypted array
    Assert.assertFalse(Array.exists(ctx, arrayURI));
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    Assert.assertTrue(Array.exists(ctx, arrayURI));

    // Test that we can decrypt the array
    ArraySchema schema = new ArraySchema(ctx, arrayURI, EncryptionType.TILEDB_AES_256_GCM, key);
    Assert.assertEquals(schema.getArrayType(), ArrayType.TILEDB_DENSE);
  }

  @Test(expected = TileDBError.class)
  public void testLoadingEncryptedArrayNoKeyErrors() throws Exception {
    // Test that we can create the encrypted array
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    ArraySchema schema = new ArraySchema(ctx, arrayURI);
  }

  @Test(expected = TileDBError.class)
  public void testLoadingEncryptedArrayWrongKeyErrors() throws Exception {
    // Test that we can create the encrypted array
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    String keyString = "0123456789abcdeF0123456789abcdeZ";
    ArraySchema schema =
        new ArraySchema(
            ctx,
            arrayURI,
            EncryptionType.TILEDB_AES_256_GCM,
            keyString.getBytes(StandardCharsets.US_ASCII));
  }

  @Test(expected = TileDBError.class)
  public void testLoadingEncryptedArrayWrongKeyLenErrors() throws Exception {
    // Test that we can create the encrypted array
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    String keyString = "0123456789abcdeF0123456789a";
    ArraySchema schema =
        new ArraySchema(
            ctx,
            arrayURI,
            EncryptionType.TILEDB_AES_256_GCM,
            keyString.getBytes(StandardCharsets.US_ASCII));
  }

  @Test
  public void testArrayOpenAt() throws Exception {
    Array.create(arrayURI, schemaCreate());

    long[] array_a = new long[] {1, 2, 3, 6};
    insertArbitraryValues(new NativeArray(ctx, array_a, Long.class));
    long ts_a = new Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli();

    Thread.sleep(1000);

    long[] array_b = new long[] {1, 1, 1, 1};
    insertArbitraryValues(new NativeArray(ctx, array_b, Long.class));
    long ts_b = new Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli();

    Thread.sleep(1000);

    long[] array_c = new long[] {0, 0, 0, 0};
    insertArbitraryValues(new NativeArray(ctx, array_c, Long.class));
    long ts_c = new Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli();

    assert Arrays.equals(readArrayAt(BigInteger.valueOf(ts_a)), array_a);
    assert Arrays.equals(readArrayAt(BigInteger.valueOf(ts_b)), array_b);
    assert Arrays.equals(readArrayAt(BigInteger.valueOf(ts_c)), array_c);
  }

  @Test
  public void testArrayOpenAtEncrypted() throws Exception {
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);

    long[] array_a = new long[] {1, 2, 3, 6};
    insertArbitraryValuesEncrypted(new NativeArray(ctx, array_a, Long.class));
    long ts_a = new Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli();

    Thread.sleep(1000);

    long[] array_b = new long[] {1, 1, 1, 1};
    insertArbitraryValuesEncrypted(new NativeArray(ctx, array_b, Long.class));
    long ts_b = new Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli();

    Thread.sleep(1000);

    long[] array_c = new long[] {0, 0, 0, 0};
    insertArbitraryValuesEncrypted(new NativeArray(ctx, array_c, Long.class));
    long ts_c = new Timestamp(System.currentTimeMillis()).toInstant().toEpochMilli();

    assert Arrays.equals(readArrayAtEncrypted(BigInteger.valueOf(ts_a)), array_a);
    assert Arrays.equals(readArrayAtEncrypted(BigInteger.valueOf(ts_b)), array_b);
    assert Arrays.equals(readArrayAtEncrypted(BigInteger.valueOf(ts_c)), array_c);
  }
}
