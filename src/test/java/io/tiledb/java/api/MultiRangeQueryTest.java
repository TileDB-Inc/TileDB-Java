package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.math.BigInteger;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MultiRangeQueryTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private Context ctx;
  private String arrayURI;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toPath().resolve("multi_range_query").toString();
    arrayCreate();
    arrayWrite();
  }

  @Test
  public void test() throws Exception {
    arrayRead(false);
    arrayRead(true);
  }

  @Test
  public void testPointRanges() throws Exception {
    arrayReadPointRanges(false);
    arrayReadPointRanges(true);
  }

  public void arrayCreate() throws Exception {
    // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
    Dimension<Integer> rows =
        new Dimension<Integer>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols =
        new Dimension<Integer>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);

    // Add two attributes "a1" and "a2", so each (i,j) cell can store
    // a character on "a1" and a vector of two floats on "a2".
    Attribute a1 = new Attribute(ctx, "a1", Character.class);
    Attribute a2 = new Attribute(ctx, "a2", Float.class);
    a2.setCellValNum(2);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {
    // Prepare cell buffers
    NativeArray a1 = new NativeArray(ctx, "abcdefghijklmnop", String.class);
    NativeArray a2 =
        new NativeArray(
            ctx,
            new float[] {
              0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
              4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
              8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
              12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
            },
            Float.class);

    // Create query
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
        Query query = new Query(array)) {
      query.setLayout(TILEDB_ROW_MAJOR);
      query.setBuffer("a1", a1);
      query.setBuffer("a2", a2);
      // Submit query
      query.submit();
    }
  }

  private void arrayRead(boolean useSubArray) throws Exception {
    // Create array and query
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
        Query query = new Query(array, TILEDB_READ)) {

      // Slice only rows 1, 2 and cols 2, 3, 4

      if (useSubArray) {
        SubArray subArray = new SubArray(ctx, array);
        subArray.addRange(0, (int) 1, (int) 2, null);
        subArray.addRange(1, (int) 2, (int) 4, null);
        Assert.assertEquals(1, subArray.getRangeNum(0));
        Assert.assertEquals(1, subArray.getRangeNum(1));
        Assert.assertEquals(1, subArray.getRange(0, 0).getFirst());
        Assert.assertEquals(2, subArray.getRange(0, 0).getSecond());
        Assert.assertEquals(2, subArray.getRange(1, 0).getFirst());
        Assert.assertEquals(4, subArray.getRange(1, 0).getSecond());
        query.setSubarray(subArray);
      } else {
        query.addRange(0, (int) 1, (int) 2);
        query.addRange(1, (int) 2, (int) 4);
      }

      query.setLayout(TILEDB_ROW_MAJOR);

      Assert.assertEquals(1, query.getRangeNum(0));
      Assert.assertEquals(1, query.getRangeNum(1));
      Assert.assertEquals(1, query.getRange(0, 0).getFirst());
      Assert.assertEquals(2, query.getRange(0, 0).getSecond());
      Assert.assertEquals(2, query.getRange(1, 0).getFirst());
      Assert.assertEquals(4, query.getRange(1, 0).getSecond());

      // Prepare the vector that will hold the result
      // (of size 6 elements for "a1" and 12 elements for "a2" since
      // it stores two floats per cell)
      query.setBuffer("a1", new NativeArray(ctx, 6, Character.class));
      query.setBuffer("a2", new NativeArray(ctx, 12, Float.class));

      // Submit query
      query.submit();

      HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();
      Assert.assertNotNull(result_el);

      byte[] a1 = (byte[]) query.getBuffer("a1");
      float[] a2 = (float[]) query.getBuffer("a2");

      Assert.assertArrayEquals(a1, new byte[] {'b', 'c', 'd', 'f', 'g', 'h'});

      float[] expected_a2 =
          new float[] {1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f};
      for (int i = 0; i < a2.length; i++) {
        Assert.assertEquals(a2[i], expected_a2[i], 0.01f);
      }
    }
  }

  private void arrayReadPointRanges(boolean useSubArray) throws Exception {
    // Create array and query
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
        Query query = new Query(array, TILEDB_READ)) {

      // Slice only rows 1, 2 and cols 2, 3, 4
      int[] rangesRows = {1, 2};
      int[] rangeColumns = {2, 3, 4};
      if (useSubArray) {
        SubArray subArray = new SubArray(ctx, array);
        subArray.addPointRanges(0, rangesRows, BigInteger.valueOf(2));
        subArray.addPointRanges(1, rangeColumns, BigInteger.valueOf(3));
        Assert.assertEquals(1, subArray.getRangeNum(0));
        Assert.assertEquals(1, subArray.getRangeNum(1));
        Assert.assertEquals(1, subArray.getRange(0, 0).getFirst());
        Assert.assertEquals(2, subArray.getRange(0, 0).getSecond());
        Assert.assertEquals(2, subArray.getRange(1, 0).getFirst());
        Assert.assertEquals(4, subArray.getRange(1, 0).getSecond());
        query.setSubarray(subArray);
      } else {
        query.addPointRanges(0, rangesRows, BigInteger.valueOf(2));
        query.addPointRanges(1, rangeColumns, BigInteger.valueOf(3));
      }
      query.setLayout(TILEDB_ROW_MAJOR);

      Assert.assertEquals(1, query.getRangeNum(0));
      Assert.assertEquals(1, query.getRangeNum(1));
      Assert.assertEquals(1, query.getRange(0, 0).getFirst());
      Assert.assertEquals(2, query.getRange(0, 0).getSecond());
      Assert.assertEquals(2, query.getRange(1, 0).getFirst());
      Assert.assertEquals(4, query.getRange(1, 0).getSecond());

      // Prepare the vector that will hold the result
      // (of size 6 elements for "a1" and 12 elements for "a2" since
      // it stores two floats per cell)
      query.setBuffer("a1", new NativeArray(ctx, 6, Character.class));
      query.setBuffer("a2", new NativeArray(ctx, 12, Float.class));

      // Submit query
      query.submit();

      HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();
      Assert.assertNotNull(result_el);

      byte[] a1 = (byte[]) query.getBuffer("a1");
      float[] a2 = (float[]) query.getBuffer("a2");

      Assert.assertArrayEquals(a1, new byte[] {'b', 'c', 'd', 'f', 'g', 'h'});

      float[] expected_a2 =
          new float[] {1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f};
      for (int i = 0; i < a2.length; i++) {
        Assert.assertEquals(a2[i], expected_a2[i], 0.01f);
      }
    }
  }

  @Test(expected = TileDBError.class)
  public void wrongDatatype() throws Exception {
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
        Query query = new Query(array, TILEDB_READ)) {
      query.addRange(0, (long) 1, (long) 2);
    }
  }
}
