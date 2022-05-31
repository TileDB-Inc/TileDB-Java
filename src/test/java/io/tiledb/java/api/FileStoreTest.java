package io.tiledb.java.api;

import static io.tiledb.java.api.Datatype.TILEDB_BLOB;
import static io.tiledb.java.api.Datatype.TILEDB_UINT64;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileStoreTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private String source = "src/test/resources/data/text.txt";
  private Context ctx;
  private final String dimName = "position";
  private final String attName = "contents";
  private String arrayURI;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toPath().resolve("filestore_array").toString();
  }

  @After
  public void tearDown() throws Exception {
    ctx.close();
  }

  @Test
  public void testSchemaCreate() throws TileDBError {
    ArraySchema arraySchema = FileStore.schemaCreate(ctx, source);
    assert arraySchema != null;
    arraySchema.check();

    // check array_schema type
    Assert.assertEquals(ArrayType.TILEDB_DENSE, arraySchema.getArrayType());

    // check dimension
    Domain domain = arraySchema.getDomain();
    Assert.assertEquals(1, domain.getNDim());
    Assert.assertTrue(domain.hasDimension(dimName));

    // check attribute
    Assert.assertEquals(1, arraySchema.getAttributeNum());
    Assert.assertTrue(arraySchema.hasAttribute(attName));

    // cleanup
    arraySchema.close();
    domain.close();
  }

  @Test
  public void testURIImportExport() throws TileDBError, IOException {
    ArraySchema arraySchema = FileStore.schemaCreate(ctx, source);
    assert arraySchema != null;
    arraySchema.check();

    // create array
    Array.create(arrayURI, arraySchema);

    // import file to array
    FileStore.uriImport(ctx, arrayURI, source, MimeType.TILEDB_MIME_AUTODETECT);

    // check array
    readArray(ctx, arrayURI);

    // test export and compare to original file
    FileStore.uriExport(ctx, arrayURI, "exportedFile.txt");

    File originalFile = new File(source);
    File exportedFile = new File("exportedFile.txt");
    Assert.assertTrue(FileUtils.contentEquals(originalFile, exportedFile));
    exportedFile.delete();

    // cleanup
    arraySchema.close();
  }

  @Test
  public void testBufferImportExport() throws TileDBError, IOException {
    ArraySchema arraySchema = FileStore.schemaCreate(ctx, source);
    assert arraySchema != null;
    arraySchema.check();

    // create array
    Array.create(arrayURI, arraySchema);

    String stringToImport = "Simple text file.\nWith two lines.";
    NativeArray bufferToImport =
        new NativeArray(ctx, stringToImport.getBytes(StandardCharsets.UTF_8), TILEDB_BLOB);

    // import file to array
    FileStore.bufferImport(
        ctx, arrayURI, bufferToImport, bufferToImport.getSize(), MimeType.TILEDB_MIME_AUTODETECT);

    // check array
    readArray(ctx, arrayURI);

    // test export and compare to original file
    byte[] exportedBuffer = (byte[]) FileStore.bufferExport(ctx, arrayURI, 0, 33);
    String exportedBufferString = new String(exportedBuffer, StandardCharsets.UTF_8);

    Assert.assertEquals("Simple text file.\nWith two lines.", exportedBufferString);

    // cleanup
    arraySchema.close();
  }

  private void readArray(Context ctx, String arrayURI) throws TileDBError {
    // read array to check correctness
    Array array = new Array(ctx, arrayURI);
    // the file is known has a size of 33 bytes in mac/linux and 34 bytes in windows
    NativeArray subarray = new NativeArray(ctx, new long[] {0, 32}, TILEDB_UINT64);

    Query query = new Query(array, TILEDB_READ);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setSubarray(subarray);

    query.setBuffer("contents", new NativeArray(ctx, 100, TILEDB_BLOB));
    // Submit query
    query.submit();

    // check results
    byte[] contents_buf = (byte[]) query.getBuffer("contents");
    String contentsString = new String(contents_buf, StandardCharsets.UTF_8);

    Assert.assertTrue(contentsString.contains("Simple text file"));

    query.close();
    array.close();
  }

  public static boolean isWindows() {
    return System.getProperty("os.name").startsWith("Windows");
  }
}
