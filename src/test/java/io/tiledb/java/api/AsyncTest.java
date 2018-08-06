package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static io.tiledb.java.api.Constants.TILEDB_COORDS;
import static io.tiledb.java.api.ArrayType.TILEDB_SPARSE;
import static io.tiledb.java.api.Layout.*;
import static io.tiledb.java.api.QueryType.*;

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
    Dimension<Integer> rows = new Dimension<Integer>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols = new Dimension<Integer>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

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
    // Prepare cell buffers
    NativeArray data = new NativeArray(
        ctx,
        new int[] {1, 2, 3, 4},
        Integer.class);

    NativeArray coords_buff = new NativeArray(
        ctx,
        new int[]{1, 1, 2, 1, 2, 2, 4, 3},
        Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setBuffer("a", data);
    query.setCoordinates(coords_buff);
    // Submit query
    query.submitAsync();

    // Wait for query to complete
    QueryStatus status;
    do {
      // Wait till query is done
      status = query.getQueryStatus();
    } while (status == QueryStatus.TILEDB_INPROGRESS);
    query.close();
  }

  private void arrayRead() throws Exception {

    Array array = new Array(ctx, arrayURI);

    // Calcuate maximum buffer sizes for the query results per attribute
    NativeArray subarray = new NativeArray(ctx, new int[]{1, 4, 1, 4}, Integer.class);
    HashMap<String, Pair<Long,Long>> max_sizes = array.maxBufferElements(subarray);


    // Create query
    Query query = new Query(array, TILEDB_READ);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer("a",
        new NativeArray(ctx, max_sizes.get("a").getSecond().intValue(),Integer.class));
    query.setCoordinates(new NativeArray(ctx, max_sizes.get(TILEDB_COORDS).getSecond().intValue(), Integer.class));

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

    int[] data = (int[]) query.getBuffer("a");
    int[] coords = (int[]) query.getCoordinates();

    query.close();
    array.close();

    Assert.assertTrue(callbackCalled);
    Assert.assertArrayEquals(coords, new int[]{1, 1, 2, 1, 2, 2, 4, 3});
    Assert.assertArrayEquals(data, new int[]{1, 2, 3, 4});
  }

  private class ReadCallback implements Callback {

    public ReadCallback() {
    }

    public void call() {
      callbackCalled = true;
    }
  }
}
