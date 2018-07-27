package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_array_type_t;
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_query_type_t;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static io.tiledb.java.api.TileDBArrayTypeEnum.TILEDB_DENSE;
import static io.tiledb.java.api.TileDBLayoutEnum.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.TileDBQueryTypeEnum.TILEDB_READ;
import static io.tiledb.java.api.TileDBQueryTypeEnum.TILEDB_WRITE;

public class WritingDensePadding {
  private Context ctx;
  private String arrayURI = "writing_dense_padding";

  @Test
  public void test() throws Exception {
    ctx = new Context();
    File arrayDir = new File(arrayURI);
    if (arrayDir.exists())
      TileDBObject.remove(ctx, arrayURI);
    arrayCreate();
    arrayWrite();
    arrayRead();
  }

  public void arrayCreate() throws Exception {

    // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4]
    // and space tiles 2x2
    Dimension<Integer> rows = new Dimension<Integer>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols = new Dimension<Integer>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

    // Create and set domain
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);

    // Add a single attribute "a" so each (i,j) cell can store an integer.
    Attribute a = new Attribute(ctx, "a", Integer.class);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
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

    NativeArray subarray = new NativeArray(
        ctx,
        new int[]{2, 3, 1, 2},
        Integer.class);

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

  private void arrayRead() throws Exception {

    Array array = new Array(ctx, arrayURI, TILEDB_READ);

    // Calcuate maximum buffer sizes for the query results per attribute
    NativeArray subarray = new NativeArray(ctx, new int[]{1, 4, 1, 4}, Integer.class);

    // Create query
    Query query = new Query(array, TILEDB_READ);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer("a",
        new NativeArray(ctx, 16,Integer.class));

    // Submit query
    query.submit();
    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    int[] data = (int[]) query.getBuffer("a");

    for (int i =0; i< data.length; i++){
      System.out.println(data[i]);
    }

    query.close();
    array.close();
  }
}
