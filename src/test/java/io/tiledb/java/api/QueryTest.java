package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.ArrayType.TILEDB_SPARSE;
import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class QueryTest {

  private static Context ctx;
  private static String arrayURI = "query";

  public static class DenseTests {
    @Before
    public void setup() throws Exception {
      ctx = new Context();
      if (Files.exists(Paths.get(arrayURI))) {
        TileDBObject.remove(ctx, arrayURI);
      }
      arrayCreate();
      arrayWrite();
    }

    @After
    public void teardown() throws Exception {
      if (Files.exists(Paths.get(arrayURI))) {
        TileDBObject.remove(ctx, arrayURI);
      }
    }

    public void arrayCreate() throws Exception {
      // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
      Dimension<Integer> rows =
          new Dimension<>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
      Dimension<Integer> cols =
          new Dimension<>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

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
      Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
      Query query = new Query(array);
      query.setLayout(TILEDB_ROW_MAJOR);
      query.setBuffer("a1", a1);
      query.setBuffer("a2", a2);
      // Submit query
      query.submit();
      query.close();
      array.close();
    }

    @Test
    public void arrayReadTest() throws Exception {
      Array array = new Array(ctx, arrayURI, TILEDB_READ);

      // Create query
      Query query = new Query(array, TILEDB_READ);

      // Slice only rows 1, 2 and cols 2, 3, 4
      query.addRange(0, 1, 2);
      query.addRange(1, 2, 4);
      query.setLayout(TILEDB_ROW_MAJOR);

      // Prepare the vector that will hold the result
      // (of size 6 elements for "a1" and 12 elements for "a2" since
      // it stores two floats per cell)

      // Get the first 6 elements of each attribute/dimension
      NativeArray dim1Array = new NativeArray(ctx, 6, Integer.class);
      NativeArray dim2Array = new NativeArray(ctx, 6, Integer.class);
      NativeArray a1Array = new NativeArray(ctx, 12, Character.class);
      NativeArray a2Array = new NativeArray(ctx, 6, Float.class);

      query.setBuffer("rows", dim1Array);
      query.setBuffer("cols", dim2Array);
      query.setBuffer("a1", a1Array);
      query.setBuffer("a2", a2Array);

      // Submit query
      query.submit();

      HashMap<String, Pair<Long, Long>> resultElements = query.resultBufferElements();

      Assert.assertEquals(Long.valueOf(3), resultElements.get("a1").getSecond());
      Assert.assertEquals(Long.valueOf(6), resultElements.get("a2").getSecond());

      int[] dim1 = (int[]) query.getBuffer("rows");
      int[] dim2 = (int[]) query.getBuffer("cols");
      byte[] a1 = (byte[]) query.getBuffer("a1");
      float[] a2 = (float[]) query.getBuffer("a2");

      Assert.assertArrayEquals(new int[] {1, 1, 1}, dim1);
      Assert.assertArrayEquals(new int[] {2, 3, 4}, dim2);
      Assert.assertArrayEquals(new byte[] {'b', 'c', 'd'}, a1);
      Assert.assertArrayEquals(new float[] {1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f}, a2, 0.01f);

      dim1Array.close();
      dim2Array.close();
      a1Array.close();
      a2Array.close();
      query.close();
      array.close();
    }

    @Test
    public void arrayReadDimensionsTest() throws Exception {
      Array array = new Array(ctx, arrayURI, TILEDB_READ);

      Query query = new Query(array, TILEDB_READ);

      query.addRange(0, 1, 4);
      query.addRange(1, 1, 4);
      query.setLayout(TILEDB_ROW_MAJOR);

      // Get the first 6 elements of each attribute/dimension
      NativeArray dim1Array = new NativeArray(ctx, 16, Integer.class);
      NativeArray dim2Array = new NativeArray(ctx, 16, Integer.class);

      query.setBuffer("rows", dim1Array);
      query.setBuffer("cols", dim2Array);

      // Submit query
      query.submit();

      HashMap<String, Pair<Long, Long>> resultElements = query.resultBufferElements();

      Assert.assertEquals(Long.valueOf(16), resultElements.get("rows").getSecond());
      Assert.assertEquals(Long.valueOf(16), resultElements.get("cols").getSecond());

      int[] dim1 = (int[]) query.getBuffer("rows");
      int[] dim2 = (int[]) query.getBuffer("cols");

      Assert.assertArrayEquals(new int[] {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4}, dim1);
      Assert.assertArrayEquals(new int[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}, dim2);

      query.close();
      array.close();
      dim1Array.close();
      dim2Array.close();
    }
  }

  public static class SparseTests {
    @Before
    public void setup() throws Exception {
      ctx = new Context();
      if (Files.exists(Paths.get(arrayURI))) {
        TileDBObject.remove(ctx, arrayURI);
      }
      arrayCreate();
      arrayWrite();
    }

    @After
    public void teardown() throws Exception {
      if (Files.exists(Paths.get(arrayURI))) {
        TileDBObject.remove(ctx, arrayURI);
      }
    }

    public void arrayCreate() throws TileDBError {
      // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
      Dimension d1 = new Dimension(ctx, "d1", Datatype.TILEDB_STRING_ASCII, null, null);

      // Create and set getDomain
      Domain domain = new Domain(ctx);
      domain.addDimension(d1);

      // Add two attributes "a1" and "a2", so each (i,j) cell can store
      // a character on "a1" and a vector of two floats on "a2".
      Attribute a1 = new Attribute(ctx, "a1", Integer.class);

      ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
      schema.setTileOrder(TILEDB_ROW_MAJOR);
      schema.setCellOrder(TILEDB_ROW_MAJOR);
      schema.setDomain(domain);
      schema.addAttribute(a1);

      Array.create(arrayURI, schema);
    }

    public void arrayWrite() throws TileDBError {

      NativeArray d_data = new NativeArray(ctx, "aabbccddee", Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, new long[] {0, 2, 4, 6, 8}, Datatype.TILEDB_UINT64);

      // Prepare cell buffers
      NativeArray a1 = new NativeArray(ctx, new int[] {1, 2, 3, 4, 5}, Integer.class);

      // Create query
      Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
      Query query = new Query(array);
      query.setLayout(TILEDB_GLOBAL_ORDER);

      query.setBuffer("d1", d_off, d_data);
      query.setBuffer("a1", a1);

      // Submit query
      query.submit();

      query.finalizeQuery();
      query.close();
      array.close();
    }

    @Test
    public void testReadStringDims() throws TileDBError {
      NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

      Query q = new Query(new Array(ctx, arrayURI), TILEDB_READ);

      q.setBuffer("d1", d_off, d_data);

      q.addRangeVar(0, "a", "z");

      while (q.getQueryStatus() != QueryStatus.TILEDB_COMPLETED) {
        q.submit();

        byte[] data = (byte[]) q.getBuffer("d1");
        long[] offsets = q.getVarBuffer("d1");

        String[] results = new String[offsets.length];
        int start = 0, end;

        results = Util.bytesToStrings(offsets, data);

        Assert.assertArrayEquals(new String[] {"aa", "bb", "cc", "dd", "ee"}, results);
      }
    }

    @Test
    public void testAddRangeVar() throws TileDBError {
      Array arr = new Array(ctx, arrayURI);
      NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

      Query q = new Query(arr, TILEDB_READ);

      q.setBuffer("d1", d_off, d_data);
      // Point-query
      q.addRangeVar(0, "aa", "aa");
      q.submit();

      byte[] data = (byte[]) q.getBuffer("d1");
      long[] offsets = q.getVarBuffer("d1");

      Assert.assertArrayEquals(new String[] {"aa"}, Util.bytesToStrings(offsets, data));

      q.close();
      d_data.close();
      d_off.close();

      d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);
      q = new Query(arr, TILEDB_READ);

      q.setBuffer("d1", d_off, d_data);
      // Range query ["dd", "ee"]
      q.addRangeVar(0, "dd", "ee");
      q.submit();

      data = (byte[]) q.getBuffer("d1");
      offsets = q.getVarBuffer("d1");

      Assert.assertArrayEquals(new String[] {"dd", "ee"}, Util.bytesToStrings(offsets, data));

      // An invalid dimentions should throw an error
      d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);
      q = new Query(arr, TILEDB_READ);

      q.setBuffer("d1", d_off, d_data);
      // We expect an error here
      try {
        int dimIdx = 123;
        q.addRangeVar(dimIdx, "dd", "ee");
        Assert.fail("An error should be thrown for invalid dimension: " + dimIdx);
      } catch (TileDBError error) {
      }
    }

    @Test(expected = TileDBError.class)
    public void testAddRangeVarInvalidDimension() throws TileDBError {
      Array arr = new Array(ctx, arrayURI);
      NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

      Query q = new Query(arr, TILEDB_READ);

      q.setBuffer("d1", d_off, d_data);
      // Point-query
      q.addRangeVar(123, "aa", "aa");
    }

    @Test
    public void testGetRangeVar() throws TileDBError {
      Array arr = new Array(ctx, arrayURI);
      NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

      Query q = new Query(arr, TILEDB_READ);

      q.setBuffer("d1", d_off, d_data);

      String rangeStart1 = "aaaaaa";
      String rangeEnd1 = "aaabb";

      String rangeStart2 = "aaaaaa";
      String rangeEnd2 = "aaabb";

      String rangeStart3 = "aaaaaa";
      String rangeEnd3 = "aaabb";

      // Point-query
      q.addRangeVar(0, rangeStart1, rangeEnd1);
      q.addRangeVar(0, rangeStart2, rangeEnd2);
      q.addRangeVar(0, rangeStart3, rangeEnd3);

      Pair<String, String> range1 = q.getRangeVar(0, BigInteger.valueOf(0));
      Pair<String, String> range2 = q.getRangeVar(0, BigInteger.valueOf(1));
      Pair<String, String> range3 = q.getRangeVar(0, BigInteger.valueOf(2));

      Assert.assertEquals(rangeStart1, range1.getFirst());
      Assert.assertEquals(rangeEnd1, range1.getSecond());
      Assert.assertEquals(rangeStart2, range2.getFirst());
      Assert.assertEquals(rangeEnd2, range2.getSecond());
      Assert.assertEquals(rangeStart3, range3.getFirst());
      Assert.assertEquals(rangeEnd3, range3.getSecond());
    }

    @Test
    public void testGetRangeVarSize() throws TileDBError {
      Array arr = new Array(ctx, arrayURI);
      NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

      Query q = new Query(arr, TILEDB_READ);

      q.setBuffer("d1", d_off, d_data);

      String rangeStart1 = "aaaaaa";
      String rangeEnd1 = "aaabb";

      String rangeStart2 = "aaaaaa";
      String rangeEnd2 = "aaabb";

      String rangeStart3 = "aaaaaa";
      String rangeEnd3 = "aaabb";

      // Point-query
      q.addRangeVar(0, rangeStart1, rangeEnd1);
      q.addRangeVar(0, rangeStart2, rangeEnd2);
      q.addRangeVar(0, rangeStart3, rangeEnd3);

      Pair<Long, Long> size1 = q.getRangeVarSize(0, BigInteger.valueOf(0));
      Pair<Long, Long> size2 = q.getRangeVarSize(0, BigInteger.valueOf(1));
      Pair<Long, Long> size3 = q.getRangeVarSize(0, BigInteger.valueOf(2));

      Assert.assertEquals(rangeStart1.length(), (long) size1.getFirst());
      Assert.assertEquals(rangeEnd1.length(), (long) size1.getSecond());
      Assert.assertEquals(rangeStart2.length(), (long) size2.getFirst());
      Assert.assertEquals(rangeEnd2.length(), (long) size2.getSecond());
      Assert.assertEquals(rangeStart3.length(), (long) size3.getFirst());
      Assert.assertEquals(rangeEnd3.length(), (long) size3.getSecond());
    }
  }
}
