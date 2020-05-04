package io.tiledb.java.api;

import static io.tiledb.java.api.Datatype.*;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
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

  private Object[] getArray(Object val) {
    if (val instanceof Object[]) return (Object[]) val;
    int arrlength = java.lang.reflect.Array.getLength(val);
    Object[] outputArray = new Object[arrlength];
    for (int i = 0; i < arrlength; i++) {
      outputArray[i] = java.lang.reflect.Array.get(val, i);
    }
    return outputArray;
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

  @Test
  public void testArraygetNonEmptyDomainFromIndex() throws Exception {
    Array.create(arrayURI, schemaCreate());

    long[] array_a = new long[] {1, 2, 3, 6};
    insertArbitraryValues(new NativeArray(ctx, array_a, Long.class));

    Array array = new Array(ctx, arrayURI, TILEDB_READ);

    Assert.assertEquals(1L, array.getNonEmptyDomainFromIndex(0).getFirst());
    Assert.assertEquals(4L, array.getNonEmptyDomainFromIndex(0).getSecond());

    try {
      array.getNonEmptyDomainFromIndex(1);
      Assert.fail();
    } catch (TileDBError error) {
    }
  }

  @Test
  public void testArraygetNonEmptyDomainFromName() throws Exception {
    Array.create(arrayURI, schemaCreate());

    long[] array_a = new long[] {1, 2, 3, 6};
    insertArbitraryValues(new NativeArray(ctx, array_a, Long.class));

    Array array = new Array(ctx, arrayURI, TILEDB_READ);

    Assert.assertEquals(1L, array.getNonEmptyDomainFromName("d1").getFirst());
    Assert.assertEquals(4L, array.getNonEmptyDomainFromName("d1").getSecond());

    try {
      array.getNonEmptyDomainFromName("d2");
      Assert.fail();
    } catch (TileDBError error) {
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

    String byteKey = "md-byte";
    String shortKey = "md-short";
    String intKey = "md-int";
    String floatKey = "md-float";
    String doubleKey = "md-double";

    // metadata keys sorted in a lexicographic ordering
    String[] keys = new String[] {byteKey, doubleKey, floatKey, intKey, shortKey};
    Datatype[] types =
        new Datatype[] {TILEDB_INT8, TILEDB_FLOAT64, TILEDB_FLOAT32, TILEDB_INT32, TILEDB_INT16};
    int keysNum = keys.length;
    NativeArray[] nativeArrays =
        new NativeArray[] {metadataByte, metadataDouble, metadataFloat, metadataInt, metadataShort};
    Object[] expectedArrays =
        new Object[] {
          metadataByte.toJavaArray(),
          metadataDouble.toJavaArray(),
          metadataFloat.toJavaArray(),
          metadataInt.toJavaArray(),
          metadataShort.toJavaArray()
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

    Assert.assertNotNull(metadataByteActual);
    Assert.assertNotNull(metadataShortActual);
    Assert.assertNotNull(metadataIntActual);
    Assert.assertNotNull(metadataFloatActual);
    Assert.assertNotNull(metadataDoubleActual);

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

    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);

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

    for (int i = 0; i < keysNum; i++) {
      array.putMetadata(keys[i], nativeArrays[i]);
    }
    // submit changes
    array.close();

    // open a new session
    array = new Array(ctx, arrayURI, TILEDB_READ);

    Map<String, Object> metadata = array.getMetadataMap();

    for (int i = 0; i < keys.length; ++i) {
      String key = keys[i];
      Object value = metadata.get(key);

      // Check if the key is contained in the metadata
      Assert.assertTrue(metadata.containsKey(key));

      Class c = types[i].javaClass();

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
}
