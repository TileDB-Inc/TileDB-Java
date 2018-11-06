package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class WriteDenseGlobalTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private String arrayURI;

  @Before
  public void setup() {
    arrayURI = temp.getRoot().toString();
  }

  public void createArray() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = new ArraySchema(ctx, ArrayType.TILEDB_DENSE)) {
      schema.setTileOrder(Layout.TILEDB_ROW_MAJOR).setCellOrder(Layout.TILEDB_ROW_MAJOR);
      try (Domain domain = new Domain(ctx);
          Dimension dim1 = new Dimension(ctx, "rows", Integer.class, new Pair<>(1, 4), 2);
          Dimension dim2 = new Dimension(ctx, "cols", Integer.class, new Pair<>(1, 4), 2)) {
        domain.addDimension(dim1).addDimension(dim2);
        schema.setDomain(domain);
      }
      try (Attribute attr = new Attribute(ctx, "a", Integer.class)) {
        schema.addAttribute(attr);
      }
      Array.create(arrayURI, schema);
    }
  }

  public void writeArray() throws Exception {
    try (Context ctx = new Context();
        Array array = new Array(ctx, arrayURI, QueryType.TILEDB_WRITE);
        Query query = new Query(array);
        NativeArray subarray = new NativeArray(ctx, new int[] {1, 4, 1, 2}, Integer.class)) {

      try (NativeArray data = new NativeArray(ctx, new int[] {1, 2, 3, 4}, Integer.class)) {
        query.setLayout(Layout.TILEDB_GLOBAL_ORDER).setBuffer("a", data).setSubarray(subarray);
        query.submit();
        for (int i = 0; i < 4; i++) {
          data.setItem(i, 5 + i);
        }
        query.submit();
        query.finalizeQuery();
      }
    }
  }

  public int[] readArray() throws Exception {
    try (Context ctx = new Context();
        Array array = new Array(ctx, arrayURI, QueryType.TILEDB_READ);
        Query query = new Query(array);
        NativeArray data = new NativeArray(ctx, 16, Integer.class)) {

      try (NativeArray subarray = new NativeArray(ctx, new int[] {1, 4, 1, 4}, Integer.class)) {
        query.setSubarray(subarray).setBuffer("a", data).setLayout(Layout.TILEDB_ROW_MAJOR);
        query.submit();
      }

      return (int[]) data.toJavaArray();
    }
  }

  @Test
  public void testWriteDenseGlobal() throws Exception {
    createArray();
    writeArray();
    int[] expected = {
      1,
      2,
      -2147483648,
      -2147483648,
      3,
      4,
      -2147483648,
      -2147483648,
      5,
      6,
      -2147483648,
      -2147483648,
      7,
      8,
      -2147483648,
      -2147483648
    };
    Assert.assertArrayEquals(readArray(), expected);
  }
}
