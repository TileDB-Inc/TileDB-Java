package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.*;
import static io.tiledb.java.api.Layout.*;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FragmentsConsolidationTest {

  private Context ctx;
  private String arrayURI = "fragments_consolidation";

  @Before
  public void setup() throws Exception {
    this.ctx = new Context();
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
  }

  @After
  public void teardown() throws Exception {
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
    ctx.close();
  }

  @Test
  public void testConsolidate() throws Exception {
    // create array
    arrayCreate();
    // updates
    arrayWrite1();
    arrayWrite2();
    arrayWrite3();
    // consolidate
    Array.consolidate(ctx, arrayURI);
    // verify consolidation
    arrayRead();
  }

  @Test
  public void testVacuum() throws Exception {
    // create array
    arrayCreate();
    // updates
    arrayWrite1();
    arrayWrite2();
    arrayWrite3();
    // consolidate
    Array.consolidate(ctx, arrayURI);
    Array.vacuum(ctx, arrayURI);
    // verify consolidation
    arrayRead();

    // verify vacuum
    File f = new File(arrayURI);
    int nFiles = 0;
    for (File file : f.listFiles())
      if (file.isDirectory() && !file.getName().equals("__meta")) {
        System.out.println(file.getAbsolutePath());
        nFiles++;
      }

    Assert.assertEquals(1, nFiles);
  }

  public void arrayCreate() throws Exception {

    // Create getDimensions
    Dimension<Integer> rows =
        new Dimension<Integer>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols =
        new Dimension<Integer>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);

    // Create and add getAttributes
    Attribute a = new Attribute(ctx, "a", Integer.class);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite1() throws Exception {
    // Prepare cell buffers
    NativeArray data = new NativeArray(ctx, new int[] {1, 2, 3, 4, 5, 6, 7, 8}, Integer.class);

    NativeArray subarray = new NativeArray(ctx, new int[] {1, 2, 1, 4}, Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer("a", data);
    query.setSubarray(subarray);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  public void arrayWrite2() throws Exception {
    // Prepare cell buffers
    NativeArray data = new NativeArray(ctx, new int[] {101, 102, 103, 104}, Integer.class);

    NativeArray subarray = new NativeArray(ctx, new int[] {2, 3, 2, 3}, Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer("a", data);
    query.setSubarray(subarray);

    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  public void arrayWrite3() throws Exception {
    // Prepare cell buffers
    NativeArray rows = new NativeArray(ctx, new int[] {1, 3}, Integer.class);
    NativeArray cols = new NativeArray(ctx, new int[] {1, 4}, Integer.class);
    NativeArray data = new NativeArray(ctx, new int[] {201, 202}, Integer.class);

    NativeArray coords = new NativeArray(ctx, new int[] {1, 1, 3, 4}, Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(TILEDB_UNORDERED);
    query.setBuffer("rows", rows);
    query.setBuffer("cols", cols);
    query.setBuffer("a", data);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  private void arrayRead() throws Exception {

    Array array = new Array(ctx, arrayURI, TILEDB_READ);

    // Calcuate maximum buffer sizes for the query results per attribute
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
    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    int[] rows = (int[]) query.getBuffer("rows");
    int[] cols = (int[]) query.getBuffer("cols");
    int[] data = (int[]) query.getBuffer("a");
    query.close();
    array.close();

    Assert.assertArrayEquals(rows, new int[] {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4});
    Assert.assertArrayEquals(cols, new int[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4});

    Assert.assertArrayEquals(
        data,
        new int[] {
          201,
          2,
          3,
          4,
          5,
          101,
          102,
          8,
          -2147483648,
          103,
          104,
          202,
          -2147483648,
          -2147483648,
          -2147483648,
          -2147483648
        });
  }
}
