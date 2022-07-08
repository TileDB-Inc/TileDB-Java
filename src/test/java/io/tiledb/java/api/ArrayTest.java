package io.tiledb.java.api;

import static io.tiledb.java.api.Datatype.*;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.Layout.TILEDB_UNORDERED;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class ArrayTest {

  private Context ctx;
  private String arrayURI;
  private String dimName;
  private String attributeName;
  private byte[] key;

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toString();
    dimName = "d1";
    attributeName = "a1";
    String keyString = "0123456789abcdeF0123456789abcdeF";
    key = keyString.getBytes(StandardCharsets.US_ASCII);
  }

  @After
  public void tearDown() throws Exception {
    ctx.close();
  }

  private String testArrayURIString(String arrayName) {
    Path arraysPath = Paths.get("src", "test", "resources", "data", arrayName);
    return "file://".concat(arraysPath.toAbsolutePath().toString());
  }

  private Object[] getArray(Object val) {
    if (val instanceof Object[]) return (Object[]) val;
    int arrlength = java.lang.reflect.Array.getLength(val);
    Object[] outputArray = new Object[arrlength];
    for (int i = 0; i < arrlength; i++) {
      outputArray[i] = java.lang.reflect.Array.get(val, i);
    }
    return outputArray;
  }

  public ArraySchema schemaCreate(ArrayType type) throws TileDBError {
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Attribute a1 = new Attribute(ctx, attributeName, Long.class);
    ArraySchema schema = new ArraySchema(ctx, type);
    schema.setTileOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setCellOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.check();
    return schema;
  }

  public ArraySchema schemaCreate() throws TileDBError {
    return schemaCreate(ArrayType.TILEDB_DENSE);
  }

  public ArraySchema schemaStringDimsCreate(ArrayType arrayType) throws Exception {
    Dimension<Long> d1 = new Dimension<Long>(ctx, "d1", TILEDB_STRING_ASCII, null, null);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    ArraySchema schema = new ArraySchema(ctx, arrayType);

    schema.setDomain(domain);
    schema.check();
    return schema;
  }

  public void insertArbitraryValuesVarSize(
      Array array, String attrName, NativeArray a_data, NativeArray a_offsets, Layout layout)
      throws TileDBError {
    // Create query
    try (Query query = new Query(array, TILEDB_WRITE)) {
      query.setLayout(layout).setBuffer(attrName, a_offsets, a_data);
      query.submit();
      query.finalizeQuery();
    }
  }

  public void insertArbitraryValuesMeth(Array array, NativeArray a_data) throws TileDBError {
    // Create query
    try (Query query = new Query(array, TILEDB_WRITE)) {
      query.setLayout(TILEDB_ROW_MAJOR).setBuffer(attributeName, a_data);
      query.submit();
    }
  }

  public void insertArbitraryValues(NativeArray a_data) throws TileDBError {
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE)) {
      insertArbitraryValuesMeth(array, a_data);
    }
  }

  public void insertArbitraryValues(NativeArray a_data, BigInteger timestamp_end)
      throws TileDBError {
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE, timestamp_end)) {
      insertArbitraryValuesMeth(array, a_data);
    }
  }

  public void insertArbitraryValuesEncrypted(NativeArray a_data) throws TileDBError {
    try (Array array =
        new Array(ctx, arrayURI, TILEDB_WRITE, EncryptionType.TILEDB_AES_256_GCM, key)) {
      insertArbitraryValuesMeth(array, a_data);
    }
  }

  public void insertArbitraryValuesEncrypted(NativeArray a_data, BigInteger timestamp)
      throws TileDBError {
    try (Array array =
        new Array(ctx, arrayURI, TILEDB_WRITE, EncryptionType.TILEDB_AES_256_GCM, key, timestamp)) {
      insertArbitraryValuesMeth(array, a_data);
    }
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

  public long[] readArrayBetween(BigInteger timestamp_start, BigInteger timestamp_end)
      throws TileDBError {
    return readArray(new Array(ctx, arrayURI, TILEDB_READ, timestamp_start, timestamp_end));
  }

  public long[] readArrayBetweenEncrypted(BigInteger timestamp_start, BigInteger timestamp_end)
      throws TileDBError {
    return readArray(
        new Array(
            ctx,
            arrayURI,
            TILEDB_READ,
            timestamp_start,
            timestamp_end,
            EncryptionType.TILEDB_AES_256_GCM,
            key));
  }

  public long[] readArrayAt(BigInteger timestamp) throws TileDBError {
    return readArray(new Array(ctx, arrayURI, TILEDB_READ, timestamp));
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
    try (ArraySchema schema = new ArraySchema(ctx, arrayURI)) {
      Assert.assertEquals(schema.getArrayType(), ArrayType.TILEDB_DENSE);
    }
  }

  @Test
  public void testEncryptedArrayExists() throws Exception {
    // Test that we can create the encrypted array
    Assert.assertFalse(Array.exists(ctx, arrayURI));
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    Assert.assertTrue(Array.exists(ctx, arrayURI));

    // Test that we can decrypt the array
    try (ArraySchema schema =
        new ArraySchema(ctx, arrayURI, EncryptionType.TILEDB_AES_256_GCM, key)) {
      Assert.assertEquals(schema.getArrayType(), ArrayType.TILEDB_DENSE);
    }
  }

  @Test(expected = TileDBError.class)
  public void testLoadingEncryptedArrayNoKeyErrors() throws Exception {
    // Test that we can create the encrypted array
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    new ArraySchema(ctx, arrayURI).close();
  }

  @Test(expected = TileDBError.class)
  public void testLoadingEncryptedArrayWrongKeyErrors() throws Exception {
    // Test that we can create the encrypted array
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    String keyString = "0123456789abcdeF0123456789abcdeZ";
    new ArraySchema(
            ctx,
            arrayURI,
            EncryptionType.TILEDB_AES_256_GCM,
            keyString.getBytes(StandardCharsets.US_ASCII))
        .close();
  }

  @Test(expected = TileDBError.class)
  public void testLoadingEncryptedArrayWrongKeyLenErrors() throws Exception {
    // Test that we can create the encrypted array
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    String keyString = "0123456789abcdeF0123456789a";
    new ArraySchema(
            ctx,
            arrayURI,
            EncryptionType.TILEDB_AES_256_GCM,
            keyString.getBytes(StandardCharsets.US_ASCII))
        .close();
  }

  @Test
  public void testArrayOpenAt() throws Exception {
    Array.create(arrayURI, schemaCreate());
    long[] result;

    long[] array_a = new long[] {1, 2, 3, 6};
    BigInteger ts_a = BigInteger.valueOf(10L);
    insertArbitraryValues(new NativeArray(ctx, array_a, Long.class), ts_a);

    long[] array_b = new long[] {1, 1, 1, 1};
    BigInteger ts_b = BigInteger.valueOf(20L);
    insertArbitraryValues(new NativeArray(ctx, array_b, Long.class), ts_b);

    long[] array_c = new long[] {0, 0, 0, 0};
    BigInteger ts_c = BigInteger.valueOf(30L);
    insertArbitraryValues(new NativeArray(ctx, array_c, Long.class), ts_c);

    Assert.assertArrayEquals(array_a, readArrayAt(ts_a));
    Assert.assertArrayEquals(
        array_a, readArrayBetween(ts_a, ts_b.subtract(BigInteger.valueOf(1L))));

    Assert.assertArrayEquals(array_b, readArrayAt(ts_b));
    Assert.assertArrayEquals(
        array_b, readArrayBetween(ts_b, ts_c.subtract(BigInteger.valueOf(1L))));

    Assert.assertArrayEquals(array_c, readArrayAt(ts_c));
  }

  @Test
  public void testArrayOpenAtEncrypted() throws Exception {
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);

    long[] array_a = new long[] {1, 2, 3, 6};
    BigInteger ts_a = BigInteger.valueOf(10L);
    insertArbitraryValuesEncrypted(new NativeArray(ctx, array_a, Long.class), ts_a);

    long[] array_b = new long[] {1, 1, 1, 1};
    BigInteger ts_b = BigInteger.valueOf(20L);
    insertArbitraryValuesEncrypted(new NativeArray(ctx, array_b, Long.class), ts_b);

    long[] array_c = new long[] {0, 0, 0, 0};
    BigInteger ts_c = BigInteger.valueOf(30L);
    insertArbitraryValuesEncrypted(new NativeArray(ctx, array_c, Long.class), ts_c);

    Assert.assertArrayEquals(array_a, readArrayAtEncrypted(ts_a));
    Assert.assertArrayEquals(
        array_a, readArrayBetweenEncrypted(ts_a, ts_b.subtract(BigInteger.valueOf(1L))));

    Assert.assertArrayEquals(array_b, readArrayAtEncrypted(ts_b));
    Assert.assertArrayEquals(
        array_b, readArrayBetweenEncrypted(ts_b, ts_c.subtract(BigInteger.valueOf(1L))));

    Assert.assertArrayEquals(array_c, readArrayAtEncrypted(ts_c));
  }

  @Test
  public void testArraygetNonEmptyDomainFromIndex() throws Exception {
    Array.create(arrayURI, schemaCreate());

    long[] array_a = new long[] {1, 2, 3, 6};
    insertArbitraryValues(new NativeArray(ctx, array_a, Long.class));

    try (Array array = new Array(ctx, arrayURI, TILEDB_READ)) {

      Assert.assertEquals(1L, array.getNonEmptyDomainFromIndex(0).getFirst());
      Assert.assertEquals(4L, array.getNonEmptyDomainFromIndex(0).getSecond());

      try {
        array.getNonEmptyDomainFromIndex(1);
        Assert.fail();
      } catch (TileDBError error) {
      }
    }
  }

  @Test
  public void testArraygetNonEmptyDomainFromName() throws Exception {
    Array.create(arrayURI, schemaCreate());

    long[] array_a = new long[] {1, 2, 3, 6};
    insertArbitraryValues(new NativeArray(ctx, array_a, Long.class));

    try (Array array = new Array(ctx, arrayURI, TILEDB_READ)) {

      Assert.assertEquals(1L, array.getNonEmptyDomainFromName("d1").getFirst());
      Assert.assertEquals(4L, array.getNonEmptyDomainFromName("d1").getSecond());

      try {
        array.getNonEmptyDomainFromName("d2");
        Assert.fail();
      } catch (TileDBError error) {
      }
    }
  }

  @Test
  public void testArrayGetNonEmptyDomainVarSizeFromIndex() throws Exception {
    Array.create(arrayURI, schemaStringDimsCreate(ArrayType.TILEDB_SPARSE));
    NativeArray data = new NativeArray(ctx, "aabbccddee", TILEDB_STRING_ASCII);
    NativeArray offsets = new NativeArray(ctx, new long[] {0, 2, 4, 6}, TILEDB_UINT64);
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE)) {
      insertArbitraryValuesVarSize(array, dimName, data, offsets, TILEDB_UNORDERED);
    }

    try (Array array = new Array(ctx, arrayURI, TILEDB_READ)) {

      Pair<BigInteger, BigInteger> size = array.getNonEmptyDomainVarSizeFromIndex(0);

      Assert.assertEquals(2, size.getFirst().intValue());
      Assert.assertEquals(4, size.getSecond().intValue());
    }
  }

  @Test
  public void testArrayGetNonEmptyDomainVarSizeFromName() throws Exception {
    Array.create(arrayURI, schemaStringDimsCreate(ArrayType.TILEDB_SPARSE));
    NativeArray data = new NativeArray(ctx, "aabbccddee", TILEDB_STRING_ASCII);
    NativeArray offsets = new NativeArray(ctx, new long[] {0, 2, 4, 6}, TILEDB_UINT64);
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE)) {
      insertArbitraryValuesVarSize(array, dimName, data, offsets, TILEDB_UNORDERED);
    }

    try (Array array = new Array(ctx, arrayURI, TILEDB_READ)) {

      Pair<BigInteger, BigInteger> size = array.getNonEmptyDomainVarSizeFromName(dimName);

      Assert.assertEquals(2, size.getFirst().intValue());
      Assert.assertEquals(4, size.getSecond().intValue());
    }
  }

  @Test
  public void testArrayGetNonEmptyDomainVarFromIndex() throws Exception {
    Array.create(arrayURI, schemaStringDimsCreate(ArrayType.TILEDB_SPARSE));
    NativeArray data = new NativeArray(ctx, "aabbccddee", TILEDB_STRING_ASCII);
    NativeArray offsets = new NativeArray(ctx, new long[] {0, 2, 4, 6}, TILEDB_UINT64);
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE)) {
      insertArbitraryValuesVarSize(array, dimName, data, offsets, TILEDB_UNORDERED);
    }

    try (Array array = new Array(ctx, arrayURI, TILEDB_READ)) {

      Pair<String, String> size1 = array.getNonEmptyDomainVarFromIndex(0);
      Pair<String, String> size2 = array.getNonEmptyDomainFromIndex(0);

      Assert.assertEquals("aa", size1.getFirst());
      Assert.assertEquals("ddee", size1.getSecond());

      Assert.assertEquals("aa", size2.getFirst());
      Assert.assertEquals("ddee", size2.getSecond());
    }
  }

  @Test
  public void testArrayGetNonEmptyDomainVarFromName() throws Exception {
    Array.create(arrayURI, schemaStringDimsCreate(ArrayType.TILEDB_SPARSE));
    NativeArray data = new NativeArray(ctx, "aabbccddee", TILEDB_STRING_ASCII);
    NativeArray offsets = new NativeArray(ctx, new long[] {0, 2, 4, 6}, TILEDB_UINT64);
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE)) {
      insertArbitraryValuesVarSize(array, dimName, data, offsets, TILEDB_UNORDERED);
    }

    try (Array array = new Array(ctx, arrayURI, TILEDB_READ)) {

      Pair<String, String> size1 = array.getNonEmptyDomainVarFromName(dimName);
      Pair<String, String> size2 = array.getNonEmptyDomainFromName(dimName);

      Assert.assertEquals("aa", size1.getFirst());
      Assert.assertEquals("ddee", size1.getSecond());

      Assert.assertEquals("aa", size2.getFirst());
      Assert.assertEquals("ddee", size2.getSecond());
    }
  }

  @Test
  public void testArrayMetadata() throws Exception {
    Array.create(arrayURI, schemaCreate());

    long[] array_a = new long[] {1, 2, 3, 6};
    insertArbitraryValues(new NativeArray(ctx, array_a, Long.class));

    Array arrayw = new Array(ctx, arrayURI, TILEDB_WRITE);
    Array array = new Array(ctx, arrayURI, TILEDB_READ);

    NativeArray metadataByte = new NativeArray(ctx, new byte[] {-7, -6, -5, 0, 100}, Byte.class);

    NativeArray metadataShort = new NativeArray(ctx, new short[] {18, 19, 20, 21}, Short.class);

    NativeArray metadataInt =
        new NativeArray(
            ctx,
            new int[] {
              0, 1, 2, 3, 4, 5, 6, 7,
              8, 9, 10, 11, 12, 13, 14, 15
            },
            Integer.class);

    NativeArray metadataFloat =
        new NativeArray(
            ctx,
            new float[] {
              0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
              4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
              8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
              12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
            },
            Float.class);

    NativeArray metadataDouble =
        new NativeArray(
            ctx,
            new double[] {
              1.1d, 1.2d, 2.1d, 2.2d, 3.1d, 3.2d, 4.1d, 4.2d,
              5.1d, 5.2d, 6.1d, 6.2d, 7.1d, 7.2d, 8.1d, 8.2d,
              9.1d, 9.2d, 10.1d, 10.2d, 11.1d, 11.2d, 12.1d, 12.2d,
              13.1d, 14.2d, 14.1d, 14.2d, 15.1d, 15.2d, 16.1d, 16.2d
            },
            Double.class);

    NativeArray metadataString = new NativeArray(ctx, "русский", String.class);
    NativeArray metadataStringAscii = new NativeArray(ctx, "Russia", TILEDB_STRING_ASCII);
    NativeArray metadataStringUtf8 = new NativeArray(ctx, "русский", TILEDB_STRING_UTF8);

    String byteKey = "md-byte";
    String shortKey = "md-short";
    String intKey = "md-int";
    String floatKey = "md-float";
    String doubleKey = "md-double";
    String stringKey = "md-string";
    String stringAsciiKey = "md-string-ascii";
    String stringUtf8Key = "md-string-utf8";

    // metadata keys sorted in a lexicographic ordering
    String[] keys =
        new String[] {
          byteKey, doubleKey, floatKey, intKey, shortKey, stringKey, stringAsciiKey, stringUtf8Key
        };
    Datatype[] types =
        new Datatype[] {
          TILEDB_INT8,
          TILEDB_FLOAT64,
          TILEDB_FLOAT32,
          TILEDB_INT32,
          TILEDB_INT16,
          TILEDB_STRING_ASCII,
          TILEDB_STRING_ASCII,
          TILEDB_STRING_UTF8
        };
    int keysNum = keys.length;
    NativeArray[] nativeArrays =
        new NativeArray[] {
          metadataByte,
          metadataDouble,
          metadataFloat,
          metadataInt,
          metadataShort,
          metadataString,
          metadataStringAscii,
          metadataStringUtf8
        };
    Object[] expectedArrays =
        new Object[] {
          metadataByte.toJavaArray(),
          metadataDouble.toJavaArray(),
          metadataFloat.toJavaArray(),
          metadataInt.toJavaArray(),
          metadataShort.toJavaArray(),
          metadataString.toJavaArray(),
          metadataStringAscii.toJavaArray(),
          metadataStringUtf8.toJavaArray()
        };

    for (int i = 0; i < keysNum; i++) {
      Assert.assertFalse(array.hasMetadataKey(keys[i]));
    }

    Assert.assertEquals(0, array.getMetadataNum().intValue());
    array.close();

    for (int i = 0; i < keysNum; i++) {
      arrayw.putMetadata(keys[i], nativeArrays[i]);
    }
    // submit changes
    arrayw.close();

    // open a new session
    Array arrayn = new Array(ctx, arrayURI, TILEDB_READ);

    for (int i = 0; i < keysNum; i++) {
      Assert.assertTrue(arrayn.hasMetadataKey(keys[i]));
    }

    Assert.assertEquals(keysNum, arrayn.getMetadataNum().intValue());

    // manual extraction of metadata
    NativeArray metadataByteActual = arrayn.getMetadata(byteKey, TILEDB_INT8);
    NativeArray metadataShortActual = arrayn.getMetadata(shortKey, TILEDB_INT16);
    NativeArray metadataIntActual = arrayn.getMetadata(intKey, TILEDB_INT32);
    NativeArray metadataFloatActual = arrayn.getMetadata(floatKey, TILEDB_FLOAT32);
    NativeArray metadataDoubleActual = arrayn.getMetadata(doubleKey, TILEDB_FLOAT64);
    NativeArray metadataStringActual = arrayn.getMetadata(stringKey, TILEDB_CHAR);
    NativeArray metadataStringAsciiActual = arrayn.getMetadata(stringAsciiKey, TILEDB_STRING_ASCII);
    NativeArray metadataStringUtf8Actual = arrayn.getMetadata(stringUtf8Key, TILEDB_STRING_UTF8);

    Assert.assertNotNull(metadataByteActual);
    Assert.assertNotNull(metadataShortActual);
    Assert.assertNotNull(metadataIntActual);
    Assert.assertNotNull(metadataFloatActual);
    Assert.assertNotNull(metadataDoubleActual);
    Assert.assertNotNull(metadataStringActual);
    Assert.assertNotNull(metadataStringAsciiActual);
    Assert.assertNotNull(metadataStringUtf8Actual);

    Assert.assertArrayEquals(
        (byte[]) metadataByte.toJavaArray(), (byte[]) metadataByteActual.toJavaArray());
    Assert.assertArrayEquals(
        (short[]) metadataShort.toJavaArray(), (short[]) metadataShortActual.toJavaArray());
    Assert.assertArrayEquals(
        (int[]) metadataInt.toJavaArray(), (int[]) metadataIntActual.toJavaArray());
    Assert.assertArrayEquals(
        (float[]) metadataFloat.toJavaArray(), (float[]) metadataFloatActual.toJavaArray(), 1e-10f);
    Assert.assertArrayEquals(
        (double[]) metadataDouble.toJavaArray(),
        (double[]) metadataDoubleActual.toJavaArray(),
        1e-10d);
    Assert.assertArrayEquals(
        (byte[]) metadataString.toJavaArray(), (byte[]) metadataStringActual.toJavaArray());
    Assert.assertEquals(
        "???????", new String((byte[]) metadataString.toJavaArray(), StandardCharsets.ISO_8859_1));
    Assert.assertArrayEquals(
        (byte[]) metadataStringAscii.toJavaArray(),
        (byte[]) metadataStringAsciiActual.toJavaArray());
    Assert.assertEquals(
        "Russia",
        new String((byte[]) metadataStringAscii.toJavaArray(), StandardCharsets.ISO_8859_1));
    Assert.assertArrayEquals(
        (byte[]) metadataStringUtf8.toJavaArray(), (byte[]) metadataStringUtf8Actual.toJavaArray());
    Assert.assertEquals(
        "русский", new String((byte[]) metadataStringUtf8.toJavaArray(), StandardCharsets.UTF_8));

    // exctracion of metadata without specifying the Datatype
    for (int i = 0; i < keysNum; i++) {
      NativeArray a = arrayn.getMetadata(keys[i]);
      Assert.assertNotNull(a);
      Assert.assertEquals(types[i], a.getNativeType());
      Assert.assertEquals(nativeArrays[i].getNativeType(), a.getNativeType());
      Assert.assertArrayEquals(getArray(expectedArrays[i]), getArray(a.toJavaArray()));
    }

    // fromIndex tests
    for (int i = 0; i < arrayn.getMetadataNum().intValue(); i++) {
      Pair<String, NativeArray> p = arrayn.getMetadataFromIndex(i);
      NativeArray a = p.getSecond();
      Assert.assertEquals(keys[i], p.getFirst());
      Assert.assertEquals(types[i], a.getNativeType());
      Assert.assertEquals(nativeArrays[i].getNativeType(), a.getNativeType());
      Assert.assertArrayEquals(getArray(expectedArrays[i]), getArray(a.toJavaArray()));
    }

    arrayn.close();

    // open a new write session
    Array arrayd = new Array(ctx, arrayURI, TILEDB_WRITE);

    for (int i = 0; i < keysNum; i++) {
      arrayd.deleteMetadata(keys[i]);
    }

    arrayd.close();

    // open a new session to check the deletion
    Array arraydn = new Array(ctx, arrayURI, TILEDB_READ);

    for (int i = 0; i < keysNum; i++) {
      Assert.assertFalse(arraydn.hasMetadataKey(keys[i]));
    }

    Assert.assertEquals(0, arraydn.getMetadataNum().intValue());

    arraydn.close();
  }

  @Test
  public void testArrayGetMetadataMap() throws Exception {
    Array.create(arrayURI, schemaCreate());

    NativeArray metadataByte = new NativeArray(ctx, new byte[] {-7, -6, -5, 0, 100}, Byte.class);

    NativeArray metadataShort = new NativeArray(ctx, new short[] {18, 19, 20, 21}, Short.class);

    NativeArray metadataInt =
        new NativeArray(
            ctx,
            new int[] {
              0, 1, 2, 3, 4, 5, 6, 7,
              8, 9, 10, 11, 12, 13, 14, 15
            },
            Integer.class);

    NativeArray metadataFloat =
        new NativeArray(
            ctx,
            new float[] {
              0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
              4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
              8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
              12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
            },
            Float.class);

    NativeArray metadataDouble =
        new NativeArray(
            ctx,
            new double[] {
              1.1d, 1.2d, 2.1d, 2.2d, 3.1d, 3.2d, 4.1d, 4.2d,
              5.1d, 5.2d, 6.1d, 6.2d, 7.1d, 7.2d, 8.1d, 8.2d,
              9.1d, 9.2d, 10.1d, 10.2d, 11.1d, 11.2d, 12.1d, 12.2d,
              13.1d, 14.2d, 14.1d, 14.2d, 15.1d, 15.2d, 16.1d, 16.2d
            },
            Double.class);

    NativeArray metadataByteSingle = new NativeArray(ctx, new byte[] {-7}, Byte.class);
    NativeArray metadataShortSingle = new NativeArray(ctx, new short[] {1}, Short.class);
    NativeArray metadataIntSingle = new NativeArray(ctx, new int[] {1}, Integer.class);
    NativeArray metadataDoubleSingle = new NativeArray(ctx, new double[] {1.0}, Double.class);
    NativeArray metadataFloatSingle = new NativeArray(ctx, new float[] {1.0f}, Float.class);

    String byteKey = "md-byte";
    String shortKey = "md-short";
    String intKey = "md-int";
    String floatKey = "md-float";
    String doubleKey = "md-double";

    String byteSingleKey = "byte-single";
    String shortSingleKey = "short-single";
    String intSingleKey = "int-single";
    String floatSingleKey = "float-single";
    String doubleSingleKey = "double-single";

    // metadata keys sorted in a lexicographic ordering
    String[] keys =
        new String[] {
          byteKey,
          doubleKey,
          floatKey,
          intKey,
          shortKey,
          byteSingleKey,
          doubleSingleKey,
          floatSingleKey,
          shortSingleKey,
          intSingleKey
        };
    Datatype[] types =
        new Datatype[] {
          TILEDB_INT8,
          TILEDB_FLOAT64,
          TILEDB_FLOAT32,
          TILEDB_INT32,
          TILEDB_INT16,
          TILEDB_INT8,
          TILEDB_FLOAT64,
          TILEDB_FLOAT32,
          TILEDB_INT32,
          TILEDB_INT16
        };

    int keysNum = keys.length;
    NativeArray[] nativeArrays =
        new NativeArray[] {
          metadataByte,
          metadataDouble,
          metadataFloat,
          metadataInt,
          metadataShort,
          metadataByteSingle,
          metadataDoubleSingle,
          metadataFloatSingle,
          metadataIntSingle,
          metadataShortSingle
        };

    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE)) {
      for (int i = 0; i < keysNum; i++) {
        array.putMetadata(keys[i], nativeArrays[i]);
      }
    }

    // open a new session
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ)) {

      Map<String, Object> metadata = array.getMetadataMap();

      for (int i = 0; i < keys.length; ++i) {
        String key = keys[i];
        Object value = metadata.get(key);

        // Check if the key is contained in the metadata
        Assert.assertTrue(metadata.containsKey(key));

        Class<?> c = types[i].javaClass();

        if (value instanceof NativeArray) {
          // Check array types
          Assert.assertEquals(c, ((NativeArray) value).getJavaType());

          // Check array elements
          for (int idx = 0; idx < nativeArrays[i].getSize(); ++idx) {
            Assert.assertEquals(((NativeArray) value).getItem(idx), nativeArrays[i].getItem(idx));
          }
        } else {
          Assert.assertEquals(c, value.getClass());
          Assert.assertEquals(nativeArrays[i].getItem(0), value);
        }
      }
    }
  }

  @Test
  public void testArrayPutMetadataOverload() throws Exception {
    Array.create(arrayURI, schemaCreate());

    long[] array_a = new long[] {1, 2, 3, 6};
    insertArbitraryValues(new NativeArray(ctx, array_a, Long.class));

    Array arrayw = new Array(ctx, arrayURI, TILEDB_WRITE);

    String floatKey = "md-float";
    float[] metadataFloat =
        new float[] {
          0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
          4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
          8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
          12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
        };

    arrayw.putMetadata(floatKey, metadataFloat);
    arrayw.close();

    Array array = new Array(ctx, arrayURI, TILEDB_READ);

    float[] metadataFloatActual = (float[]) array.getMetadata(floatKey).toJavaArray();

    Assert.assertArrayEquals(metadataFloat, metadataFloatActual, 1e-10f);

    array.close();
  }

  @Test
  public void testArrayReopen() throws Exception {
    Array.create(arrayURI, schemaCreate());

    try (Array array = new Array(ctx, arrayURI)) {
      array.reopen();
    }
  }

  @Test
  public void testArrayUpgrade() throws TileDBError, IOException {

    // move old array to new temp folder for the upgrade.
    String source = "src/test/resources/data/1.6/quickstart_sparse_array";
    File srcDir = new File(source);

    String destination = "src/test/resources/data/quickstart_sparse_array_v2";
    File destDir = new File(destination);

    try {
      FileUtils.copyDirectory(srcDir, destDir);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // READ BEFORE UPGRADE
    Array array = new Array(ctx, testArrayURIString("quickstart_sparse_array_v2"), TILEDB_READ);
    NativeArray subarray = new NativeArray(ctx, new int[] {1, 4, 1, 4}, Integer.class);

    // Create query
    Query query = new Query(array, TILEDB_READ);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setSubarray(subarray);
    query.setBuffer("a", new NativeArray(ctx, 16, Integer.class));
    query.setBuffer("rows", new NativeArray(ctx, 16, Integer.class));
    query.setBuffer("cols", new NativeArray(ctx, 16, Integer.class));

    // Submit query
    query.submit();

    int[] rows = (int[]) query.getBuffer("rows");
    int[] cols = (int[]) query.getBuffer("cols");
    int[] data = (int[]) query.getBuffer("a");
    query.close();

    // upgrade array
    array.upgradeVersion(ctx, array.getConfig());

    // check schema
    array.getSchema().check();

    // READ AFTER UPGRADE
    query = new Query(array, TILEDB_READ);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setSubarray(subarray);
    query.setBuffer("a", new NativeArray(ctx, 16, Integer.class));
    query.setBuffer("rows", new NativeArray(ctx, 16, Integer.class));
    query.setBuffer("cols", new NativeArray(ctx, 16, Integer.class));

    // Submit query
    query.submit();

    int[] rowsAfter = (int[]) query.getBuffer("rows");
    int[] colsAfter = (int[]) query.getBuffer("cols");
    int[] dataAfter = (int[]) query.getBuffer("a");

    // clean up
    query.close();
    array.close();

    // compare
    Assert.assertArrayEquals(cols, colsAfter);
    Assert.assertArrayEquals(rows, rowsAfter);
    Assert.assertArrayEquals(data, dataAfter);

    FileUtils.deleteDirectory(destDir);
  }
}
