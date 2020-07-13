package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.ArrayType.TILEDB_SPARSE;
import static io.tiledb.java.api.Constants.TILEDB_VAR_NUM;
import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class QueryTest {

  private static Context ctx;

  public static class DenseTests {

    @Rule public TemporaryFolder temp = new TemporaryFolder();

    private String arrayURI;

    @Before
    public void setup() throws Exception {
      ctx = new Context();
      arrayURI = temp.getRoot().toPath().resolve("query").toString();
      arrayCreate();
      arrayWrite();
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
      try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
          Query query = new Query(array)) {
        query.setLayout(TILEDB_ROW_MAJOR);
        query.setBuffer("a1", a1);
        query.setBuffer("a2", a2);
        // Submit query
        query.submit();
      }
    }

    @Test
    public void arrayReadTest() throws Exception {
      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          Query query = new Query(array, TILEDB_READ)) {

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
      }
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

  public static class NIODenseTests {

    @Rule public TemporaryFolder temp = new TemporaryFolder();

    private String arrayURI;

    @Before
    public void setup() throws Exception {
      ctx = new Context();
      arrayURI = temp.getRoot().toPath().resolve("query").toString();
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
      Attribute a3 = new Attribute(ctx, "a3", Datatype.TILEDB_CHAR);
      a3.setCellValNum(TILEDB_VAR_NUM);

      ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
      schema.setTileOrder(TILEDB_ROW_MAJOR);
      schema.setCellOrder(TILEDB_ROW_MAJOR);
      schema.setDomain(domain);
      schema.addAttribute(a1);
      schema.addAttribute(a2);

      Array.create(arrayURI, schema);
    }

    public void arrayWrite() throws Exception {
      String str = "abcdefghijklmnop";
      float[] floatArr =
          new float[] {
            0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
            4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
            8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
            12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
          };

      ByteBuffer a1 = ByteBuffer.allocateDirect(1 * str.length());
      ByteBuffer a2 = ByteBuffer.allocateDirect(4 * floatArr.length);

      a1.order(ByteOrder.nativeOrder());
      a2.order(ByteOrder.nativeOrder());

      for (int i = 0; i < floatArr.length; ++i) {
        if (i < str.length()) a1.put((byte) str.charAt(i));
        a2.putFloat(floatArr[i]);
      }

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

    public void arrayWithVarAttrCreate() throws Exception {
      // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
      Dimension<Integer> rows =
          new Dimension<>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 8), 2);

      // Create and set getDomain
      Domain domain = new Domain(ctx);
      domain.addDimension(rows);

      // Add two attributes "a1" and "a2", so each (i,j) cell can store
      // a character on "a1" and a vector of two floats on "a2".
      Attribute a1 = new Attribute(ctx, "a1", Datatype.TILEDB_CHAR);
      a1.setCellValNum(TILEDB_VAR_NUM);

      ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
      schema.setTileOrder(TILEDB_ROW_MAJOR);
      schema.setCellOrder(TILEDB_ROW_MAJOR);
      schema.setDomain(domain);
      schema.addAttribute(a1);

      Array.create(arrayURI, schema);
    }

    public void arrayWithVarAttrWrite() throws Exception {
      // Prepare cell buffers
      String str = "aabbccddeeffgghh";
      long[] offsets = new long[] {0, 2, 4, 6, 8, 10, 12, 14};

      // NativeArray a2 = new NativeArray(ctx, "aabbccddeeffgghh", Datatype.TILEDB_CHAR);
      // NativeArray a2_off = new NativeArray(ctx, new long[]{0, 2, 4, 6, 8, 10, 12, 14},
      // Datatype.TILEDB_UINT64);

      ByteBuffer a1 = ByteBuffer.allocateDirect(str.length());
      ByteBuffer a1_off = ByteBuffer.allocateDirect(offsets.length * 8);

      a1.order(ByteOrder.nativeOrder());
      a1_off.order(ByteOrder.nativeOrder());

      a1.put(str.getBytes(StandardCharsets.US_ASCII));
      for (long x : offsets) a1_off.putLong(x);

      // Create query
      try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
          Query query = new Query(array)) {
        query.setLayout(TILEDB_ROW_MAJOR);
        query.setBuffer("a1", a1_off, a1);
        // Submit query
        query.submit();
      }
    }

    @Test
    public void queryTestNIOReadArray() throws Exception {
      arrayCreate();
      arrayWrite();

      Array array = new Array(ctx, arrayURI, TILEDB_READ);

      Query query = new Query(array, TILEDB_READ);

      int bufferSize = 4;

      query.setBuffer("rows", bufferSize);
      query.setBuffer("cols", bufferSize);
      ByteBuffer d1 = query.getByteBuffer("rows").getSecond();
      ByteBuffer d2 = query.getByteBuffer("cols").getSecond();

      query.addRange(0, 1, 4);
      query.addRange(1, 1, 4);

      query.setLayout(TILEDB_ROW_MAJOR);

      int[] d1_result = new int[16];
      int[] d2_result = new int[16];
      int idx = 0;

      while (query.getQueryStatus() != QueryStatus.TILEDB_COMPLETED) {
        query.submit();

        while (d1.hasRemaining() && d2.hasRemaining()) {
          d1_result[idx] = d1.getInt();
          d2_result[idx] = d2.getInt();
          idx++;
        }
        d1.clear();
        d2.clear();
      }

      Assert.assertArrayEquals(
          new int[] {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4}, d1_result);
      Assert.assertArrayEquals(
          new int[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}, d2_result);
    }

    @Test
    public void queryTestNIOReadArrayArbitrarySize() throws Exception {
      arrayCreate();
      arrayWrite();

      Array array = new Array(ctx, arrayURI, TILEDB_READ);

      Query query = new Query(array, TILEDB_READ);

      int bufferSize = 4;
      query.setBuffer("rows", ByteBuffer.allocateDirect(10));
      query.setBuffer("cols", ByteBuffer.allocateDirect(10));
      ByteBuffer d1 = query.getByteBuffer("rows").getSecond();
      ByteBuffer d2 = query.getByteBuffer("cols").getSecond();

      query.addRange(0, 1, 4);
      query.addRange(1, 1, 4);

      query.setLayout(TILEDB_ROW_MAJOR);

      int[] d1_result = new int[16];
      int[] d2_result = new int[16];
      int idx = 0;

      while (query.getQueryStatus() != QueryStatus.TILEDB_COMPLETED) {
        query.submit();

        while (d1.hasRemaining() && d2.hasRemaining()) {
          d1_result[idx] = d1.getInt();
          d2_result[idx] = d2.getInt();
          idx++;
        }
        d1.clear();
        d2.clear();
      }

      Assert.assertArrayEquals(
          new int[] {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4}, d1_result);
      Assert.assertArrayEquals(
          new int[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}, d2_result);
    }

    @Test
    public void arrayReadTest() throws Exception {
      arrayCreate();
      arrayWrite();

      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          Query query = new Query(array, TILEDB_READ)) {

        // Slice only rows 1, 2 and cols 2, 3, 4
        query.addRange(0, 1, 2);
        query.addRange(1, 2, 4);
        query.setLayout(TILEDB_ROW_MAJOR);

        query.setBuffer("rows", 3).setBuffer("cols", 3).setBuffer("a1", 3).setBuffer("a2", 6);

        ByteBuffer dim1Buffer = query.getByteBuffer("rows").getSecond();
        ByteBuffer dim2Buffer = query.getByteBuffer("cols").getSecond();
        ByteBuffer a1Buffer = query.getByteBuffer("a1").getSecond();
        ByteBuffer a2Buffer = query.getByteBuffer("a2").getSecond();

        // Submit query
        query.submit();

        int[] dim1 = new int[3];
        int[] dim2 = new int[3];
        byte[] a1 = new byte[3];
        float[] a2 = new float[6];

        int idx = 0;
        while (dim1Buffer.hasRemaining()) {
          dim1[idx++] = dim1Buffer.getInt();
        }

        idx = 0;
        while (dim2Buffer.hasRemaining()) {
          dim2[idx++] = dim2Buffer.getInt();
        }

        idx = 0;
        while (a1Buffer.hasRemaining()) {
          a1[idx++] = a1Buffer.get();
        }

        idx = 0;
        while (a2Buffer.hasRemaining()) {
          a2[idx++] = a2Buffer.getFloat();
        }

        Assert.assertArrayEquals(new int[] {1, 1, 1}, dim1);
        Assert.assertArrayEquals(new int[] {2, 3, 4}, dim2);
        Assert.assertArrayEquals(new byte[] {'b', 'c', 'd'}, a1);
        Assert.assertArrayEquals(new float[] {1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f}, a2, 0.01f);
      }
    }

    @Test
    public void arrayReadTestCustomBufferWithDifferentOrder() throws Exception {
      arrayCreate();
      arrayWrite();

      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          Query query = new Query(array, TILEDB_READ)) {

        // Slice only rows 1, 2 and cols 2, 3, 4
        query.addRange(0, 1, 2);
        query.addRange(1, 2, 4);
        query.setLayout(TILEDB_ROW_MAJOR);

        // Set the opposite byte order from the native system
        ByteOrder order =
            ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)
                ? ByteOrder.LITTLE_ENDIAN
                : ByteOrder.BIG_ENDIAN;

        query
            .setBuffer("rows", ByteBuffer.allocateDirect(3 * 4).order(order))
            .setBuffer("cols", ByteBuffer.allocateDirect(3 * 4).order(order))
            .setBuffer("a1", ByteBuffer.allocateDirect(3).order(order))
            .setBuffer("a2", ByteBuffer.allocateDirect(6 * 4).order(order));

        ByteBuffer dim1Buffer = query.getByteBuffer("rows").getSecond();
        ByteBuffer dim2Buffer = query.getByteBuffer("cols").getSecond();
        ByteBuffer a1Buffer = query.getByteBuffer("a1").getSecond();
        ByteBuffer a2Buffer = query.getByteBuffer("a2").getSecond();

        // Submit query
        query.submit();

        int[] dim1 = new int[3];
        int[] dim2 = new int[3];
        byte[] a1 = new byte[3];
        float[] a2 = new float[6];

        int idx = 0;
        while (dim1Buffer.hasRemaining()) {
          dim1[idx++] = dim1Buffer.getInt();
        }

        idx = 0;
        while (dim2Buffer.hasRemaining()) {
          dim2[idx++] = dim2Buffer.getInt();
        }

        idx = 0;
        while (a1Buffer.hasRemaining()) {
          a1[idx++] = a1Buffer.get();
        }

        idx = 0;
        while (a2Buffer.hasRemaining()) {
          a2[idx++] = a2Buffer.getFloat();
        }

        Assert.assertArrayEquals(new int[] {1, 1, 1}, dim1);
        Assert.assertArrayEquals(new int[] {2, 3, 4}, dim2);
        Assert.assertArrayEquals(new byte[] {'b', 'c', 'd'}, a1);
        Assert.assertArrayEquals(new float[] {1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f}, a2, 0.01f);
      }
    }

    @Test
    public void queryTestNIOGetByteBuffer() throws Exception {
      arrayCreate();
      arrayWrite();

      Array array = new Array(ctx, arrayURI, TILEDB_READ);

      Query query = new Query(array, TILEDB_READ);

      int bufferSize = 16;

      query.setBuffer("rows", bufferSize);

      Assert.assertEquals(query.getByteBuffer("rows").getSecond().capacity(), bufferSize * 4);
      Assert.assertTrue(query.getByteBuffer("rows").getSecond().isDirect());
      Assert.assertEquals(query.getByteBuffer("rows").getSecond().order(), ByteOrder.nativeOrder());
    }

    @Test()
    public void queryTestNIOGetByteBuffeErrors() throws Exception {
      arrayCreate();
      arrayWrite();

      Array array = new Array(ctx, arrayURI, TILEDB_READ);

      Query query = new Query(array, TILEDB_READ);

      int bufferSize = 16;

      try {
        query.setBuffer("rows", ByteBuffer.allocate(bufferSize));
        Assert.fail("An exception should be thrown in the ByteBuffer provided is not direct");
      } catch (TileDBError error) {

      }

      // Set the opposite byte order from the native system
      ByteOrder order =
          ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)
              ? ByteOrder.LITTLE_ENDIAN
              : ByteOrder.BIG_ENDIAN;

      // The Byte Order should be automatically changed to the native order
      query.setBuffer("rows", ByteBuffer.allocateDirect(bufferSize).order(order));
      ByteBuffer b = query.getByteBuffer("rows").getSecond();
      Assert.assertEquals(b.order(), ByteOrder.nativeOrder());
    }

    @Test()
    public void queryTestNIOSetBufferVarChar() throws Exception {
      arrayWithVarAttrCreate();
      arrayWithVarAttrWrite();

      ByteBuffer offsetsBuffer = ByteBuffer.allocateDirect(1000);
      ByteBuffer dataBuffer = ByteBuffer.allocateDirect(1000);

      Query q = new Query(new Array(ctx, arrayURI), TILEDB_READ);
      q.addRange(0, 1, 8);
      q.setBuffer("a1", offsetsBuffer, dataBuffer);
      q.submit();

      long[] offsets = new long[8];
      char[] data = new char[16];

      int idx = 0;

      while (q.getByteBuffer("a1").getFirst().hasRemaining())
        offsets[idx++] = offsetsBuffer.getLong();

      idx = 0;
      while (q.getByteBuffer("a1").getSecond().hasRemaining())
        data[idx++] = (char) dataBuffer.get();

      Assert.assertArrayEquals(new long[] {0, 2, 4, 6, 8, 10, 12, 14}, offsets);
      Assert.assertEquals("aabbccddeeffgghh", new String(data));
    }
  }

  public static class SparseTests {

    @Rule public TemporaryFolder temp = new TemporaryFolder();

    private String arrayURI;

    @Before
    public void setup() throws Exception {
      ctx = new Context();
      arrayURI = temp.getRoot().toPath().resolve("query").toString();
      arrayCreate();
      arrayWrite();
    }

    public void arrayCreate() throws TileDBError {
      // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
      Dimension<Integer> d1 =
          new Dimension<Integer>(ctx, "d1", Datatype.TILEDB_STRING_ASCII, null, null);

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

      try (Query q = new Query(new Array(ctx, arrayURI), TILEDB_READ)) {

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
    }

    @Test
    public void testAddRangeVar() throws TileDBError {
      Array arr = new Array(ctx, arrayURI);

      try (Query q = new Query(arr, TILEDB_READ)) {
        NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
        NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);
        q.setBuffer("d1", d_off, d_data);
        // Point-query
        q.addRangeVar(0, "aa", "aa");
        q.submit();

        byte[] data = (byte[]) q.getBuffer("d1");
        long[] offsets = q.getVarBuffer("d1");

        Assert.assertArrayEquals(new String[] {"aa"}, Util.bytesToStrings(offsets, data));
      }

      try (Query q = new Query(arr, TILEDB_READ)) {
        NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
        NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

        q.setBuffer("d1", d_off, d_data);
        // Range query ["dd", "ee"]
        q.addRangeVar(0, "dd", "ee");
        q.submit();

        byte[] data = (byte[]) q.getBuffer("d1");
        long[] offsets = q.getVarBuffer("d1");

        Assert.assertArrayEquals(new String[] {"dd", "ee"}, Util.bytesToStrings(offsets, data));
      }

      // An invalid dimentions should throw an error
      try (Query q = new Query(arr, TILEDB_READ)) {
        NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
        NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

        q.setBuffer("d1", d_off, d_data);
        // We expect an error here
        try {
          int dimIdx = 123;
          q.addRangeVar(dimIdx, "dd", "ee");
          Assert.fail("An error should be thrown for invalid dimension: " + dimIdx);
        } catch (TileDBError error) {
        }
      }
    }

    @Test(expected = TileDBError.class)
    public void testAddRangeVarInvalidDimension() throws TileDBError {
      Array arr = new Array(ctx, arrayURI);
      NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

      try (Query q = new Query(arr, TILEDB_READ)) {
        q.setBuffer("d1", d_off, d_data);
        // Point-query
        q.addRangeVar(123, "aa", "aa");
      }
    }

    @Test
    public void testGetRangeVar() throws TileDBError {
      Array arr = new Array(ctx, arrayURI);
      NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

      try (Query q = new Query(arr, TILEDB_READ)) {

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
    }

    @Test
    public void testGetRangeVarSize() throws TileDBError {
      Array arr = new Array(ctx, arrayURI);
      NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

      try (Query q = new Query(arr, TILEDB_READ)) {

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
}
