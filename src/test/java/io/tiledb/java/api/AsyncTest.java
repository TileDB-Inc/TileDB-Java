package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_SPARSE;
import static io.tiledb.java.api.Layout.*;
import static io.tiledb.java.api.QueryType.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AsyncTest {
  private Context ctx;
  private String arrayURI = "async";
  private boolean callbackCalled = false;

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

  @Test
  public void test() throws Exception {
    arrayCreate();
    arrayWrite();
    arrayRead();
  }

  public void arrayCreate() throws Exception {
    // Create getDimensions
    Dimension<Integer> rows = new Dimension(ctx, "rows", Integer.class, new Pair(1, 4), 2);
    Dimension<Integer> cols = new Dimension(ctx, "cols", Integer.class, new Pair(1, 4), 2);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);

    // Create and add getAttributes
    Attribute a = new Attribute(ctx, "a", Integer.class);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {
    NativeArray d1_data = new NativeArray(ctx, new int[] {1, 2, 2, 4}, Integer.class);
    NativeArray d2_data = new NativeArray(ctx, new int[] {1, 1, 2, 3}, Integer.class);

    // Prepare cell buffers
    NativeArray data = new NativeArray(ctx, new int[] {1, 2, 3, 4}, Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setBuffer("rows", d1_data);
    query.setBuffer("cols", d2_data);
    query.setBuffer("a", data);

    // Submit query
    query.submitAsync();

    // Wait for query to complete
    QueryStatus status;
    do {
      // Wait till query is done
      status = query.getQueryStatus();
    } while (status == QueryStatus.TILEDB_INPROGRESS);
    query.finalizeQuery();
    query.close();
  }

  private void arrayRead() throws Exception {

    Array array = new Array(ctx, arrayURI);

    // Create query
    Query query = new Query(array, TILEDB_READ);
    HashMap<String, Pair<Long, Long>> max_sizes = query.getResultEstimations();

    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer(
        "rows", new NativeArray(ctx, max_sizes.get("rows").getSecond().intValue(), Integer.class));
    query.setBuffer(
        "cols", new NativeArray(ctx, max_sizes.get("cols").getSecond().intValue(), Integer.class));
    query.setBuffer(
        "a", new NativeArray(ctx, max_sizes.get("a").getSecond().intValue(), Integer.class));

    // Submit query with callback
    query.submitAsync(new ReadCallback());

    // Wait for query to complete
    QueryStatus status;
    do {
      // Wait till query is done
      status = query.getQueryStatus();
    } while (status == QueryStatus.TILEDB_INPROGRESS);

    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    int[] rows = (int[]) query.getBuffer("rows");
    int[] cols = (int[]) query.getBuffer("cols");
    int[] data = (int[]) query.getBuffer("a");

    query.close();
    array.close();

    Assert.assertTrue(callbackCalled);
    Assert.assertArrayEquals(rows, new int[] {1, 2, 2, 4});
    Assert.assertArrayEquals(cols, new int[] {1, 1, 2, 3});
    Assert.assertArrayEquals(data, new int[] {1, 2, 3, 4});
  }

  private class ReadCallback implements Callback {

    public ReadCallback() {}

    public void call() {
      callbackCalled = true;
    }
  }
}
