package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_array_type_t;
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_query_type_t;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

public class MultiAttributeTest {
  private Context ctx;
  private String arrayURI = "multi_attribute";

  @Test
  public void test() throws Exception {
    ctx = new Context();
    File arrayDir = new File(arrayURI);
    if (arrayDir.exists())
      TileDBObject.remove(ctx, arrayURI);
    arrayCreate();
    arrayWrite();
    arrayRead();
    arrayReadSubselect();
  }

  public void arrayCreate() throws Exception {
    // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
    Dimension<Integer> rows = new Dimension<Integer>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols = new Dimension<Integer>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);

    // Add two attributes "a1" and "a2", so each (i,j) cell can store
    // a character on "a1" and a vector of two floats on "a2".
    Attribute a1 = new Attribute(ctx, "a1", Character.class);
    Attribute a2 = new Attribute(ctx, "a2", Float.class);
    a2.setCellValNum(2);

    ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_DENSE);
    schema.setTileOrder(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.setCellOrder(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {
    // Prepare cell buffers
    NativeArray a1 = new NativeArray(
        ctx,
        "abcdefghijklmnop",
        String.class);
    NativeArray a2 = new NativeArray(
        ctx,
        new float[]{
            0.1f,  0.2f,  1.1f,  1.2f,  2.1f,  2.2f,  3.1f,  3.2f,
            4.1f,  4.2f,  5.1f,  5.2f,  6.1f,  6.2f,  7.1f,  7.2f,
            8.1f,  8.2f,  9.1f,  9.2f,  10.1f, 10.2f, 11.1f, 11.2f,
            12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
        },
        Float.class);

    // Create query
    Array array = new Array(ctx, arrayURI, tiledb_query_type_t.TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(tiledb_layout_t.TILEDB_ROW_MAJOR);
    query.setBuffer("a1", a1);
    query.setBuffer("a2", a2);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  private void arrayRead() throws Exception {

    Array array = new Array(ctx, arrayURI, tiledb_query_type_t.TILEDB_READ);

    // Slice only rows 1, 2 and cols 2, 3, 4
    NativeArray subarray = new NativeArray(ctx, new int[]{1, 2, 2, 4}, Integer.class);

    // Create query
    Query query = new Query(array, tiledb_query_type_t.TILEDB_READ);
    query.setLayout(tiledb_layout_t.TILEDB_ROW_MAJOR);

    // Prepare the vector that will hold the result
    // (of size 6 elements for "a1" and 12 elements for "a2" since
    // it stores two floats per cell)
    query.setSubarray(subarray);
    query.setBuffer("a1",
        new NativeArray(ctx, 6, Character.class));
    query.setBuffer("a2",
        new NativeArray(ctx, 12, Float.class));

    // Submit query
    query.submit();

    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();
    byte[] a1 = (byte[]) query.getBuffer("a1");
    float[] a2 = (float[]) query.getBuffer("a2");
    for (int i =0; i < 6; i++){
      System.out.println("a1: " + (char) a1[i] + ", a2: (" + a2[2 * i ] + ", " + a2[2 * i + 1] + ") ");
    }
    query.close();
    array.close();
  }

  private void arrayReadSubselect() throws Exception {

    Array array = new Array(ctx, arrayURI, tiledb_query_type_t.TILEDB_READ);

    // Slice only rows 1, 2 and cols 2, 3, 4
    NativeArray subarray = new NativeArray(ctx, new int[]{1, 2, 2, 4}, Integer.class);

    // Create query
    Query query = new Query(array, tiledb_query_type_t.TILEDB_READ);
    query.setLayout(tiledb_layout_t.TILEDB_ROW_MAJOR);

    // Prepare the query - subselect over "a1" only
    query.setSubarray(subarray);
    query.setBuffer("a1",
        new NativeArray(ctx, 6, Character.class));

    // Submit query
    query.submit();

    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    byte[] a1 = (byte[]) query.getBuffer("a1");

    for (int i =0; i < 6; i++){
      System.out.println("a1: " + (char) a1[i] );
    }
    query.close();
    array.close();
  }
}
