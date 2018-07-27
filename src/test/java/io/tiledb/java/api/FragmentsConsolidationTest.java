package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_array_type_t;
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_query_type_t;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

public class FragmentsConsolidationTest {
  private Context ctx;
  private String arrayURI = "fragments_consolidation";

  @Test
  public void test() throws Exception {
    ctx = new Context();
    File arrayDir = new File(arrayURI);
    if (arrayDir.exists())
      TileDBObject.remove(ctx, arrayURI);
    arrayCreate();
    arrayWrite1();
    arrayWrite2();
    arrayWrite3();

    Array.consolidate(ctx,arrayURI);

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

    ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_DENSE);
    schema.setTileOrder(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.setCellOrder(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite1() throws Exception {
    // Prepare cell buffers
    NativeArray data = new NativeArray(
        ctx,
        new int[] {1, 2, 3, 4, 5, 6, 7, 8},
        Integer.class);

    NativeArray subarray = new NativeArray(
        ctx,
        new int[]{1, 2, 1, 4},
        Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, tiledb_query_type_t.TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(tiledb_layout_t.TILEDB_ROW_MAJOR);
    query.setBuffer("a", data);
    query.setSubarray(subarray);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  public void arrayWrite2() throws Exception {
    // Prepare cell buffers
    NativeArray data = new NativeArray(
        ctx,
        new int[] {101, 102, 103, 104},
        Integer.class);

    NativeArray subarray = new NativeArray(
        ctx,
        new int[]{2, 3, 2, 3},
        Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, tiledb_query_type_t.TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(tiledb_layout_t.TILEDB_ROW_MAJOR);
    query.setBuffer("a", data);
    query.setSubarray(subarray);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  public void arrayWrite3() throws Exception {
    // Prepare cell buffers
    NativeArray data = new NativeArray(
        ctx,
        new int[] {201, 202},
        Integer.class);

    NativeArray coords = new NativeArray(
        ctx,
        new int[]{1, 1, 3, 4},
        Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURI, tiledb_query_type_t.TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(tiledb_layout_t.TILEDB_UNORDERED);
    query.setBuffer("a", data);
    query.setCoordinates(coords);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }


  private void arrayRead() throws Exception {

    Array array = new Array(ctx, arrayURI, tiledb_query_type_t.TILEDB_READ);

    // Calcuate maximum buffer sizes for the query results per attribute
    NativeArray subarray = new NativeArray(ctx, new int[]{1, 4, 1, 4}, Integer.class);

    // Create query
    Query query = new Query(array, tiledb_query_type_t.TILEDB_READ);
    query.setLayout(tiledb_layout_t.TILEDB_ROW_MAJOR);
    query.setBuffer("a",
        new NativeArray(ctx, 16,Integer.class));
    query.setCoordinates(new NativeArray(ctx, 32, Integer.class));

    // Submit query
    query.submit();
    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    int[] data = (int[]) query.getBuffer("a");
    int[] coords = (int[]) query.getBuffer(tiledb.tiledb_coords());

    for (int i =0; i< data.length; i++){
      System.out.println("Cell (" + coords[2 * i] + ", " + coords[2 * i + 1] + ") has data " + data[i]);
    }

    query.close();
    array.close();
  }
}
