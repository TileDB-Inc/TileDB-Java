package io.tiledb.java.api;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringTypeTest {
  private Context ctx;
  private String arrayURI = "my_string_array";

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
  }

  @After
  public void teardown() throws Exception {
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
  }

  public void createStringVector3(Context ctx, Datatype tiledbType) throws TileDBError {
    try (Dimension<Long> dim1 = new Dimension<>(ctx, "dim1", Long.class, new Pair<>(1l, 3l), 3l);
        Domain domain = new Domain(ctx).addDimension(dim1);
        Attribute attr1 = new Attribute(ctx, "a1", tiledbType).setCellVar();
        ArraySchema schema = new ArraySchema(ctx, ArrayType.TILEDB_DENSE)) {
      schema.setDomain(domain);
      schema.addAttribute(attr1);
      schema.check();
      Array.create(arrayURI, schema);
    }
  }

  @Test
  public void testCharType() throws Exception {
    byte[] chars = new byte[] {'a', 'b', 'c'};
    try (Context ctx = new Context()) {
      createStringVector3(ctx, Datatype.TILEDB_CHAR);
      NativeArray a1_offs = new NativeArray(ctx, new long[] {0, 1, 2}, Datatype.TILEDB_UINT64);
      NativeArray a1_data = new NativeArray(ctx, new byte[] {'a', 'b', 'c'}, Datatype.TILEDB_CHAR);
      try (Array array = new Array(ctx, arrayURI, QueryType.TILEDB_WRITE);
          Query query = new Query(array, QueryType.TILEDB_WRITE)) {
        query.setLayout(Layout.TILEDB_COL_MAJOR).setBuffer("a1", a1_offs, a1_data);
        query.submit();
        query.finalizeQuery();
      }
      try (Array array = new Array(ctx, arrayURI, QueryType.TILEDB_READ);
          Query query = new Query(array, QueryType.TILEDB_READ)) {
        query.setLayout(Layout.TILEDB_COL_MAJOR);
        query.setBuffer(
            "a1",
            new NativeArray(ctx, 3 * Long.BYTES, Datatype.TILEDB_UINT64),
            new NativeArray(ctx, 3 * Byte.BYTES, Datatype.TILEDB_CHAR));
        query.submit();
        byte[] resultChars = (byte[]) query.getBuffer("a1");
        Assert.assertArrayEquals(chars, resultChars);
      }
    }
  }
}
