package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.*;
import static io.tiledb.java.api.Layout.*;
import static io.tiledb.java.api.QueryType.*;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FragmentsTest {

  private Context ctx;
  private String arrayURI = "fragments_test";

  @Before
  public void setup() throws Exception {
    this.ctx = new Context();
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
    arrayCreate();
    // updates
    arrayWrite1();
    arrayWrite2();
    arrayWrite3();
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
    // consolidate
    Array.consolidate(ctx, arrayURI);
    // verify consolidation
    arrayRead();
  }

  @Test
  public void testConsolidationPlan() throws TileDBError {
    ConsolidationPlan consolidationPlan =
        new ConsolidationPlan(ctx, BigInteger.valueOf(1024 * 1024), arrayURI);
    Assert.assertEquals(1, consolidationPlan.getNumNodes());
    Assert.assertEquals(3, consolidationPlan.getNumFragments(BigInteger.ZERO));

    String notNull = consolidationPlan.getFragmentURI(BigInteger.ZERO, BigInteger.ZERO);
    notNull = consolidationPlan.dumpJSONString();
  }

  //  @Test
  //  public void testConsolidateStartEnd() throws Exception {
  //    Config config = new Config();
  //    config.set("sm.consolidation.timestamp_start", "15");
  //    config.set("sm.consolidation.timestamp_end", "30");
  //    ctx = new Context(config);
  //    arrayCreate();
  //    // updates
  //    arrayWrite1();
  //    arrayWrite2();
  //    arrayWrite3();
  //    // consolidate
  //    Array.consolidate(ctx, arrayURI);
  //    // verify consolidation
  //    arrayRead();
  //  }

  @Test
  public void testVacuum() throws Exception {
    // consolidate
    Array.consolidate(ctx, arrayURI);
    Array.vacuum(ctx, arrayURI);
    // verify consolidation
    arrayRead();

    // verify vacuum
    File f = new File(arrayURI);
    int nFiles = 0;
    for (File file : Objects.requireNonNull(f.listFiles())) {
      if (file.isDirectory() && file.getName().equals("__fragments")) {
        nFiles = Objects.requireNonNull(file.listFiles()).length;
      }
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

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE, BigInteger.valueOf(10L));

    SubArray subArray = new SubArray(ctx, array);
    subArray.addRange(0, 1, 2, null);
    subArray.addRange(1, 1, 4, null);

    Query query = new Query(array);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer("a", data);
    query.setSubarray(subArray);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  public void arrayWrite2() throws Exception {
    // Prepare cell buffers
    NativeArray data = new NativeArray(ctx, new int[] {101, 102, 103, 104}, Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE, BigInteger.valueOf(20L));

    SubArray subArray = new SubArray(ctx, array);
    subArray.addRange(0, 2, 3, null);
    subArray.addRange(1, 2, 3, null);

    Query query = new Query(array);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer("a", data);
    query.setSubarray(subArray);

    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  public void arrayWrite3() throws Exception {
    // Prepare cell buffers

    NativeArray data =
        new NativeArray(
            ctx,
            new int[] {
              201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216
            },
            Integer.class);

    NativeArray coords = new NativeArray(ctx, new int[] {1, 1, 3, 4}, Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE, BigInteger.valueOf(30L));
    Query query = new Query(array);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer("a", data);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  private void arrayRead() throws Exception {

    Array array = new Array(ctx, arrayURI, TILEDB_READ);

    // Calcuate maximum buffer sizes for the query results per attribute
    SubArray subArray = new SubArray(ctx, array);
    subArray.addRange(0, 1, 4, null);
    subArray.addRange(1, 1, 4, null);

    // Create query
    Query query = new Query(array, TILEDB_READ);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setSubarray(subArray);
    query.setBuffer("a", new NativeArray(ctx, 16, Integer.class));
    query.setBuffer("rows", new NativeArray(ctx, 16, Integer.class));
    query.setBuffer("cols", new NativeArray(ctx, 16, Integer.class));

    // Submit query
    query.submit();
    // Print cell values (assumes all getAttributes are read)

    int[] rows = (int[]) query.getBuffer("rows");
    int[] cols = (int[]) query.getBuffer("cols");
    int[] data = (int[]) query.getBuffer("a");
    query.close();
    array.close();

    Assert.assertArrayEquals(rows, new int[] {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4});
    Assert.assertArrayEquals(cols, new int[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4});

    Assert.assertArrayEquals(
        data,
        new int[] {201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216});
  }

  @Test
  public void testDeleteFragments() throws Exception {
    Array array = new Array(ctx, arrayURI, TILEDB_MODIFY_EXCLUSIVE);

    array.deleteFragments(BigInteger.valueOf(10L), BigInteger.valueOf(20L));

    File f = new File(arrayURI);
    int nFiles = 0;
    File frag = null;
    for (File file : Objects.requireNonNull(f.listFiles())) {
      if (file.isDirectory() && file.getName().equals("__fragments")) {
        frag = Objects.requireNonNull(file.listFiles())[0];
        nFiles = Objects.requireNonNull(file.listFiles()).length;
      }
    }
    Assert.assertEquals(1, nFiles);
    Assert.assertTrue(frag.getName().startsWith("__30_30_"));
    array.close();
  }
}
