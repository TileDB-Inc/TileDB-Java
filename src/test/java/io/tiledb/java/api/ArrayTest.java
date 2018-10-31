package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.charset.StandardCharsets;

public class ArrayTest {

  private Context ctx;
  private String arrayURI;
  private byte[] key;

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toString();
    String keyString = "0123456789abcdeF0123456789abcdeF";
    key = keyString.getBytes(StandardCharsets.US_ASCII);
  }

  public ArraySchema schemaCreate() throws Exception {
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    ArraySchema schema = new ArraySchema(ctx, ArrayType.TILEDB_DENSE);
    schema.setTileOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setCellOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.check();
    return schema;
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
    ArraySchema schema = new ArraySchema(ctx, arrayURI,
            EncryptionType.TILEDB_AES_256_GCM,
            keyString.getBytes(StandardCharsets.US_ASCII));
  }

  @Test(expected = TileDBError.class)
  public void testLoadingEncryptedArrayWrongKeyLenErrors() throws Exception {
    // Test that we can create the encrypted array
    Array.create(arrayURI, schemaCreate(), EncryptionType.TILEDB_AES_256_GCM, key);
    String keyString = "0123456789abcdeF0123456789a";
    ArraySchema schema = new ArraySchema(ctx, arrayURI,
            EncryptionType.TILEDB_AES_256_GCM,
            keyString.getBytes(StandardCharsets.US_ASCII));
  }
}
