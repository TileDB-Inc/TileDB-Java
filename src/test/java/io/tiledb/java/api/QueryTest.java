package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.ArrayType.TILEDB_SPARSE;
import static io.tiledb.java.api.Constants.TILEDB_VAR_NUM;
import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.math.BigInteger;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
      Attribute a1 = new Attribute(ctx, "a1", String.class);
      Attribute a2 = new Attribute(ctx, "a2", Float.class);
      Attribute a3 = new Attribute(ctx, "a3", Boolean.class);
      a1.setFilterList(new FilterList(ctx).addFilter(new CheckSumMD5Filter(ctx)));
      a2.setFilterList(new FilterList(ctx).addFilter(new CheckSumSHA256Filter(ctx)));
      a2.setCellValNum(2);

      ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
      schema.setTileOrder(TILEDB_ROW_MAJOR);
      schema.setCellOrder(TILEDB_ROW_MAJOR);
      schema.setDomain(domain);
      schema.addAttribute(a1);
      schema.addAttribute(a2);
      schema.addAttribute(a3);

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
      NativeArray a3 =
          new NativeArray(
              ctx,
              new short[] {
                1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
              },
              Datatype.TILEDB_BOOL);

      // Create query
      try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
          Query query = new Query(array)) {
        query.setLayout(TILEDB_ROW_MAJOR);
        query.setBuffer("a1", a1);
        query.setBuffer("a2", a2);
        query.setBuffer("a3", a3);
        // Submit query
        query.submit();

        // Get the number of written fragments
        long num = query.getFragmentNum();

        // Get the fragment URI by index (0 <= idx < num)
        String uri = query.getFragmentURI(BigInteger.ZERO);

        // Get the timestamp range by index (0 <= idx < num)
        Pair<Long, Long> range = query.getFragmentTimestampRange(BigInteger.ZERO);
      }
    }

    @Test
    public void statsTest() throws Exception {
      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          ArraySchema schema = array.getSchema();
          Query query = new Query(array, TILEDB_READ)) {
        query.addRange(0, 1, 2);
        query.addRange(1, 2, 4);

        query.setLayout(TILEDB_ROW_MAJOR);

        NativeArray dim1Array = new NativeArray(ctx, 6, Integer.class);
        NativeArray dim2Array = new NativeArray(ctx, 6, Integer.class);
        NativeArray a1Array = new NativeArray(ctx, 12, String.class);
        NativeArray a2Array = new NativeArray(ctx, 6, Float.class);

        query.setBuffer("rows", dim1Array);
        query.setBuffer("cols", dim2Array);
        query.setBuffer("a1", a1Array);
        query.setBuffer("a2", a2Array);

        // Submit query
        query.submit();
        String stats = query.getStats();
        // System.out.println(query.getStats());
        Assert.assertTrue(
            stats.contains("unfilter_attr_tiles.sum")
                && stats.contains("read_unfiltered_byte_num")
                && stats.contains("timers")
                && stats.contains("counters")); // if not empty
      }
    }

    @Test
    public void arrayReadTest() throws Exception {
      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          ArraySchema schema = array.getSchema();
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
        NativeArray a1Array = new NativeArray(ctx, 12, String.class);
        NativeArray a2Array = new NativeArray(ctx, 6, Float.class);
        NativeArray a3Array = new NativeArray(ctx, 6, Boolean.class);

        query.setBuffer("rows", dim1Array);
        query.setBuffer("cols", dim2Array);
        query.setBuffer("a1", a1Array);
        query.setBuffer("a2", a2Array);
        query.setBuffer("a3", a3Array);

        // Submit query
        query.submit();

        try (Attribute attr1 = schema.getAttribute(0);
            Attribute attr2 = schema.getAttribute(1)) {
          Assert.assertFalse(attr1.getNullable());
          Assert.assertFalse(attr2.getNullable());
        }

        HashMap<String, Pair<Long, Long>> resultElements = query.resultBufferElements();

        Assert.assertEquals(Long.valueOf(3), resultElements.get("a1").getSecond());
        Assert.assertEquals(Long.valueOf(6), resultElements.get("a2").getSecond());

        int[] dim1 = (int[]) query.getBuffer("rows");
        int[] dim2 = (int[]) query.getBuffer("cols");
        byte[] a1 = (byte[]) query.getBuffer("a1");
        float[] a2 = (float[]) query.getBuffer("a2");
        short[] a3 = (short[]) query.getBuffer("a3");

        Assert.assertArrayEquals(new int[] {1, 1, 1}, dim1);
        Assert.assertArrayEquals(new int[] {2, 3, 4}, dim2);
        Assert.assertArrayEquals(new byte[] {'b', 'c', 'd'}, a1);
        Assert.assertArrayEquals(new float[] {1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f}, a2, 0.01f);
        Assert.assertArrayEquals(new short[] {0, 1, 0}, a3);
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
    public void queryTestNIOReadArrayRange() throws Exception {
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
    public void queryTestNIOReadArraySubArray() throws Exception {
      arrayCreate();
      arrayWrite();

      Array array = new Array(ctx, arrayURI, TILEDB_READ);

      Query query = new Query(array, TILEDB_READ);

      int bufferSize = 4;

      query.setBuffer("rows", bufferSize);
      query.setBuffer("cols", bufferSize);
      ByteBuffer d1 = query.getByteBuffer("rows").getSecond();
      ByteBuffer d2 = query.getByteBuffer("cols").getSecond();

      ByteBuffer subarray = ByteBuffer.allocateDirect(4 * Datatype.TILEDB_INT32.getNativeSize());

      subarray.order(ByteOrder.nativeOrder()).putInt(1).putInt(4).putInt(1).putInt(4);

      query.setSubarray(subarray);

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
      query.setBuffer("rows", ByteBuffer.allocateDirect(10).order(ByteOrder.nativeOrder()));
      query.setBuffer("cols", ByteBuffer.allocateDirect(10).order(ByteOrder.nativeOrder()));
      ByteBuffer d1 = query.getByteBuffer("rows").getSecond();
      ByteBuffer d2 = query.getByteBuffer("cols").getSecond();

      ByteBuffer subarray = ByteBuffer.allocateDirect(4 * Datatype.TILEDB_INT32.getNativeSize());
      subarray.order(ByteOrder.nativeOrder()).putInt(1).putInt(4).putInt(1).putInt(4);
      query.setSubarray(subarray);

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

        ByteBuffer subarray = ByteBuffer.allocateDirect(4 * Datatype.TILEDB_INT32.getNativeSize());
        subarray.order(ByteOrder.nativeOrder()).putInt(1).putInt(2).putInt(2).putInt(4);
        query.setSubarray(subarray);
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

    @Test(expected = TileDBError.class)
    public void arrayReadTestCustomBufferWithDifferentOrder() throws Exception {
      arrayCreate();
      arrayWrite();

      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          Query query = new Query(array, TILEDB_READ)) {

        ByteBuffer subarray = ByteBuffer.allocateDirect(4 * Datatype.TILEDB_INT32.getNativeSize());
        subarray.order(ByteOrder.nativeOrder()).putInt(1).putInt(2).putInt(2).putInt(4);
        query.setSubarray(subarray);
        query.setLayout(TILEDB_ROW_MAJOR);

        // Set the opposite byte order from the native system
        ByteOrder order =
            ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)
                ? ByteOrder.LITTLE_ENDIAN
                : ByteOrder.BIG_ENDIAN;

        query.setBuffer("rows", ByteBuffer.allocateDirect(3 * 4).order(order));
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

    @Test(expected = TileDBError.class)
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
    }

    @Test()
    public void queryTestNIOSetBufferVarChar() throws Exception {
      arrayWithVarAttrCreate();
      arrayWithVarAttrWrite();

      ByteBuffer offsetsBuffer = ByteBuffer.allocateDirect(1000);
      ByteBuffer dataBuffer = ByteBuffer.allocateDirect(1000);

      Query q = new Query(new Array(ctx, arrayURI), TILEDB_READ);
      ByteBuffer subarray = ByteBuffer.allocateDirect(4 * Datatype.TILEDB_INT32.getNativeSize());
      subarray.order(ByteOrder.nativeOrder()).putInt(1).putInt(8);
      q.setSubarray(subarray);
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

    @Test()
    public void queryTestNIOGetBufferDataType1() throws Exception {
      arrayCreate();
      arrayWrite();

      ByteBuffer rows = ByteBuffer.allocateDirect(1000);
      ByteBuffer cols = ByteBuffer.allocateDirect(1000);
      ByteBuffer a2 = ByteBuffer.allocateDirect(1000);

      rows.order(ByteOrder.nativeOrder());
      cols.order(ByteOrder.nativeOrder());
      a2.order(ByteOrder.nativeOrder());

      Query q = new Query(new Array(ctx, arrayURI), TILEDB_READ);
      ByteBuffer subarray = ByteBuffer.allocateDirect(4 * Datatype.TILEDB_INT32.getNativeSize());

      subarray.order(ByteOrder.nativeOrder()).putInt(1).putInt(4).putInt(1).putInt(4);

      q.setSubarray(subarray);

      q.setBuffer("rows", rows);
      q.setBuffer("cols", cols);
      q.setBuffer("a2", a2);
      q.submit();

      int idx;
      int[] rowsExpected = new int[] {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4};
      int[] colsExpected = new int[] {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};
      float[] a2Expected =
          new float[] {
            0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
            4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
            8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
            12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
          };

      idx = 0;
      IntBuffer intBuffer = q.getIntBuffer("rows").getSecond();
      while (intBuffer.hasRemaining()) {
        Assert.assertEquals(rowsExpected[idx++], intBuffer.get());
      }

      idx = 0;
      intBuffer = q.getIntBuffer("cols").getSecond();
      while (intBuffer.hasRemaining()) {
        Assert.assertEquals(colsExpected[idx++], intBuffer.get());
      }

      idx = 0;
      FloatBuffer r = q.getFloatBuffer("a2").getSecond();
      while (r.hasRemaining()) {
        Assert.assertEquals(a2Expected[idx++], r.get(), 0);
      }
    }

    @Test()
    public void queryTestNIOGetBufferDataType2() throws Exception {
      arrayCreate();
      arrayWrite();

      ByteBuffer a1 = ByteBuffer.allocateDirect(1000);

      a1.order(ByteOrder.nativeOrder());

      Query q = new Query(new Array(ctx, arrayURI), TILEDB_READ);
      ByteBuffer subarray = ByteBuffer.allocateDirect(4 * Datatype.TILEDB_INT32.getNativeSize());

      subarray.order(ByteOrder.nativeOrder()).putInt(1).putInt(4).putInt(1).putInt(4);

      q.setSubarray(subarray);

      q.setBuffer("a1", a1);
      q.submit();

      CharBuffer charBuffer = q.getCharBuffer("a1").getSecond();

      char c = 'a';
      while (charBuffer.hasRemaining()) {
        Assert.assertEquals(c, charBuffer.get());
        c++;
      }
    }

    @Test()
    public void queryTestNIOGetBufferDataType3() throws Exception {
      arrayWithVarAttrCreate();
      arrayWithVarAttrWrite();

      ByteBuffer rows = ByteBuffer.allocateDirect(1000);
      ByteBuffer a1 = ByteBuffer.allocateDirect(1000);
      ByteBuffer a1Off = ByteBuffer.allocateDirect(1000);

      rows.order(ByteOrder.nativeOrder());
      a1.order(ByteOrder.nativeOrder());

      Query q = new Query(new Array(ctx, arrayURI), TILEDB_READ);
      ByteBuffer subarray = ByteBuffer.allocateDirect(4 * Datatype.TILEDB_INT32.getNativeSize());

      subarray.order(ByteOrder.nativeOrder()).putInt(1).putInt(8);

      q.setSubarray(subarray);

      q.setBuffer("rows", rows);
      q.setBuffer("a1", a1Off, a1);
      q.submit();

      int idx;
      int[] rowsExpected = new int[] {1, 2, 3, 4, 5, 6, 7, 8};

      idx = 0;
      IntBuffer intBuffer = q.getIntBuffer("rows").getSecond();
      while (intBuffer.hasRemaining()) {
        Assert.assertEquals(rowsExpected[idx++], intBuffer.get());
      }

      for (long i : q.getOffsetArray("a1")) System.out.println(i);

      for (byte b : q.getByteArray("a1")) System.out.println((char) b);

      for (String str : Util.bytesToStrings(q.getOffsetArray("a1"), q.getByteArray("a1"))) {
        System.out.println(str);
      }
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

  public static class NullableAttributesDenseTest {

    @Rule public TemporaryFolder temp = new TemporaryFolder();

    private String arrayURI;

    @Before
    public void setup() throws Exception {
      ctx = new Context();
      arrayURI = temp.getRoot().toPath().resolve("query").toString();
    }

    public void denseArrayCreateNullableAttrs(boolean nullable) throws Exception {
      // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
      Dimension<Integer> rows =
          new Dimension<>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 2), 2);
      Dimension<Integer> cols =
          new Dimension<>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 2), 2);

      // Create and set getDomain
      Domain domain = new Domain(ctx);
      domain.addDimension(rows);
      domain.addDimension(cols);

      // Add two attributes "a1" and "a2", so each (i,j) cell can store
      // a character on "a1" and a vector of two floats on "a2".
      Attribute a1 = new Attribute(ctx, "a1", String.class);
      Attribute a2 = new Attribute(ctx, "a2", Float.class);
      a2.setCellValNum(1);

      if (nullable) {
        a1.setNullable(true);
        a2.setNullable(true);
      }

      ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
      schema.setTileOrder(TILEDB_ROW_MAJOR);
      schema.setCellOrder(TILEDB_ROW_MAJOR);
      schema.setDomain(domain);
      schema.addAttribute(a1);
      schema.addAttribute(a2);

      Array.create(arrayURI, schema);
    }

    public void denseArrayWrite() throws Exception {
      // Prepare cell buffers
      NativeArray a1 = new NativeArray(ctx, "abcd", String.class);
      NativeArray a2 = new NativeArray(ctx, new float[] {0.1f, 0.2f, 1.1f, 1.2f}, Float.class);

      // Create query
      try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
          Query query = new Query(array)) {
        query.setLayout(TILEDB_ROW_MAJOR);
        NativeArray a1Bytemap =
            new NativeArray(ctx, new short[] {1, 0, 0, 1}, Datatype.TILEDB_UINT8);
        NativeArray a2Bytemap =
            new NativeArray(ctx, new short[] {1, 1, 1, 0}, Datatype.TILEDB_UINT8);

        query.setBufferNullable("a1", a1, a1Bytemap);
        query.setBufferNullable("a2", a2, a2Bytemap);

        // Submit query
        query.submit();
      }
    }

    public void sparseArrayCreateNullableAttrs(boolean nullable) throws TileDBError {
      // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
      Dimension<Integer> d1 =
          new Dimension<Integer>(ctx, "d1", Datatype.TILEDB_STRING_ASCII, null, null);

      // Create and set getDomain
      Domain domain = new Domain(ctx);
      domain.addDimension(d1);

      // Add two attributes "a1" and "a2", so each (i,j) cell can store
      // a character on "a1" and a vector of two floats on "a2".
      Attribute a1 = new Attribute(ctx, "a1", Integer.class);
      Attribute a2 = new Attribute(ctx, "a2", Datatype.TILEDB_STRING_ASCII);
      a2.setCellVar();

      if (nullable) {
        a1.setNullable(true);
        a2.setNullable(true);
      }

      ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
      schema.setTileOrder(TILEDB_ROW_MAJOR);
      schema.setCellOrder(TILEDB_ROW_MAJOR);
      schema.setDomain(domain);
      schema.addAttribute(a1);
      schema.addAttribute(a2);

      Array.create(arrayURI, schema);
    }

    public void sparseArrayWrite() throws TileDBError {

      NativeArray d_data = new NativeArray(ctx, "aabbccddee", Datatype.TILEDB_STRING_ASCII);
      NativeArray d_off = new NativeArray(ctx, new long[] {0, 2, 4, 6, 8}, Datatype.TILEDB_UINT64);

      // Prepare cell buffers
      NativeArray a1 = new NativeArray(ctx, new int[] {1, 2, 3, 4, 5}, Integer.class);

      NativeArray a2_data = new NativeArray(ctx, "aabbccddee", Datatype.TILEDB_STRING_ASCII);
      NativeArray a2_off = new NativeArray(ctx, new long[] {0, 2, 4, 6, 8}, Datatype.TILEDB_UINT64);

      // Create query
      Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
      Query query = new Query(array);
      query.setLayout(TILEDB_GLOBAL_ORDER);

      NativeArray a1ByteMap =
          new NativeArray(ctx, new short[] {0, 0, 0, 1, 1}, Datatype.TILEDB_UINT8);
      NativeArray a2ByteMap =
          new NativeArray(ctx, new short[] {1, 1, 1, 0, 0}, Datatype.TILEDB_UINT8);

      query.setBuffer("d1", d_off, d_data);
      query.setBufferNullable("a1", a1, a1ByteMap);
      query.setBufferNullable("a2", a2_off, a2_data, a2ByteMap);

      // Submit query
      query.submit();

      query.finalizeQuery();
      query.close();
      array.close();
    }

    @Test
    public void testDense() throws Exception {
      denseArrayCreateNullableAttrs(true);
      denseArrayWrite();
    }

    @Test(expected = TileDBError.class)
    public void testDenseErrorExpeted() throws Exception {
      denseArrayCreateNullableAttrs(false);
      denseArrayWrite();
    }

    @Test
    public void denseArrayReadTest() throws Exception {
      denseArrayCreateNullableAttrs(true);
      denseArrayWrite();

      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          ArraySchema schema = array.getSchema();
          Query query = new Query(array, TILEDB_READ)) {

        // Fetch all cells
        query.addRange(0, 1, 2);
        query.addRange(1, 1, 2);
        query.setLayout(TILEDB_ROW_MAJOR);

        NativeArray dim1Array = new NativeArray(ctx, 100, Integer.class);
        NativeArray dim2Array = new NativeArray(ctx, 100, Integer.class);
        NativeArray a1Array = new NativeArray(ctx, 100, String.class);
        NativeArray a1byteMap = new NativeArray(ctx, 100, Datatype.TILEDB_UINT8);
        NativeArray a2Array = new NativeArray(ctx, 100, Float.class);
        NativeArray a2byteMap = new NativeArray(ctx, 100, Datatype.TILEDB_UINT8);

        query.setBuffer("rows", dim1Array);
        query.setBuffer("cols", dim2Array);
        query.setBufferNullable("a1", a1Array, a1byteMap);
        query.setBufferNullable("a2", a2Array, a2byteMap);

        Pair<Long, Long> estimated = query.getEstResultSizeNullable(ctx, "a1");
        Assert.assertEquals((long) estimated.getFirst(), 4);
        Assert.assertEquals((long) estimated.getSecond(), 4);

        // Submit query
        query.submit();

        HashMap<String, Pair<Long, Long>> resultElements = query.resultBufferElements();

        try (Attribute a1 = schema.getAttribute(0);
            Attribute a2 = schema.getAttribute(1)) {
          Assert.assertTrue(a1.getNullable());
          Assert.assertTrue(a2.getNullable());
        }

        Assert.assertEquals(Long.valueOf(4), resultElements.get("a1").getSecond());
        Assert.assertEquals(Long.valueOf(4), resultElements.get("a2").getSecond());

        int[] dim1 = (int[]) query.getBuffer("rows");
        int[] dim2 = (int[]) query.getBuffer("cols");
        byte[] a1 = (byte[]) query.getBuffer("a1");
        float[] a2 = (float[]) query.getBuffer("a2");

        short[] a1ValidityByteMap = query.getValidityByteMap("a1");
        short[] a2ValidityByteMap = query.getValidityByteMap("a2");

        Assert.assertArrayEquals(new int[] {1, 1, 2, 2}, dim1);
        Assert.assertArrayEquals(new int[] {1, 2, 1, 2}, dim2);
        Assert.assertArrayEquals(new byte[] {'a', 'b', 'c', 'd'}, a1);
        Assert.assertArrayEquals(new float[] {0.1f, 0.2f, 1.1f, 1.2f}, a2, 0.01f);
        Assert.assertArrayEquals(new short[] {1, 0, 0, 1}, a1ValidityByteMap);
        Assert.assertArrayEquals(new short[] {1, 1, 1, 0}, a2ValidityByteMap);
      }
    }

    @Test
    public void denseArrayNIOReadTest() throws Exception {
      denseArrayCreateNullableAttrs(true);
      denseArrayWrite();

      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          ArraySchema schema = array.getSchema();
          Query query = new Query(array, TILEDB_READ)) {

        // Fetch all cells
        query.addRange(0, 1, 2);
        query.addRange(1, 1, 2);
        query.setLayout(TILEDB_ROW_MAJOR);

        ByteBuffer dim1Array = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer dim2Array = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a1Array = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a1byteMap = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a2Array = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a2byteMap = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());

        query.setBuffer("rows", dim1Array);
        query.setBuffer("cols", dim2Array);
        query.setBufferNullableNIO("a1", a1Array, a1byteMap);
        query.setBufferNullableNIO("a2", a2Array, a2byteMap);

        // Submit query
        query.submit();

        int[] dim1 = new int[4];
        int[] dim2 = new int[4];
        byte[] a1 = new byte[4];
        float[] a2 = new float[4];

        byte[] a1ValidityByteMap = new byte[4];
        byte[] a2ValidityByteMap = new byte[4];

        int dimIdx = 0;
        while (dim1Array.hasRemaining() && dim2Array.hasRemaining()) {
          dim1[dimIdx] = dim1Array.getInt();
          dim2[dimIdx] = dim2Array.getInt();
          a1[dimIdx] = a1Array.get();
          a2[dimIdx] = a2Array.getFloat();
          a1ValidityByteMap[dimIdx] = a1byteMap.get();
          a2ValidityByteMap[dimIdx] = a2byteMap.get();
          dimIdx++;
        }

        try (Attribute attr1 = schema.getAttribute(0);
            Attribute attr2 = schema.getAttribute(1)) {
          Assert.assertTrue(attr1.getNullable());
          Assert.assertTrue(attr2.getNullable());
        }

        Assert.assertEquals(
            4L,
            (long)
                query
                    .resultBufferElementsNIO("cols", Datatype.TILEDB_INT32.getNativeSize())
                    .getSecond());
        Assert.assertEquals(
            4L,
            (long)
                query
                    .resultBufferElementsNIO("rows", Datatype.TILEDB_INT32.getNativeSize())
                    .getSecond());
        Assert.assertEquals(
            4L,
            (long)
                query
                    .resultBufferElementsNIO("a1", Datatype.TILEDB_STRING_ASCII.getNativeSize())
                    .getSecond());
        Assert.assertEquals(
            4L,
            (long)
                query
                    .resultBufferElementsNIO("a2", Datatype.TILEDB_FLOAT32.getNativeSize())
                    .getSecond());

        Assert.assertArrayEquals(new int[] {1, 1, 2, 2}, dim1);
        Assert.assertArrayEquals(new int[] {1, 2, 1, 2}, dim2);
        Assert.assertArrayEquals(new byte[] {'a', 'b', 'c', 'd'}, a1);
        Assert.assertArrayEquals(new float[] {0.1f, 0.2f, 1.1f, 1.2f}, a2, 0.01f);
        Assert.assertArrayEquals(new byte[] {1, 0, 0, 1}, a1ValidityByteMap);
        Assert.assertArrayEquals(new byte[] {1, 1, 1, 0}, a2ValidityByteMap);
      }
    }

    @Test
    public void testSparse() throws Exception {
      sparseArrayCreateNullableAttrs(true);
      sparseArrayWrite();
    }

    @Test(expected = TileDBError.class)
    public void testSparseErrorExpected() throws Exception {
      sparseArrayCreateNullableAttrs(false);
      sparseArrayWrite();
    }

    @Test
    public void sparseArrayReadTest() throws Exception {
      sparseArrayCreateNullableAttrs(true);
      sparseArrayWrite();

      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          ArraySchema schema = array.getSchema();
          Query query = new Query(array, TILEDB_READ)) {

        query.setLayout(TILEDB_ROW_MAJOR);

        NativeArray a1Array = new NativeArray(ctx, 5, Datatype.TILEDB_INT32);
        NativeArray a1byteMap = new NativeArray(ctx, 5, Datatype.TILEDB_UINT8);
        NativeArray a2Array = new NativeArray(ctx, 10, Datatype.TILEDB_STRING_ASCII);
        NativeArray a2Offsets = new NativeArray(ctx, 5, Datatype.TILEDB_UINT64);
        NativeArray a2byteMap = new NativeArray(ctx, 10, Datatype.TILEDB_UINT8);

        query.setBufferNullable("a1", a1Array, a1byteMap);
        query.setBufferNullable("a2", a2Offsets, a2Array, a2byteMap);

        Pair<Pair<Long, Long>, Long> estimated = query.getEstResultSizeVarNullable(ctx, "a2");
        Assert.assertEquals(40, (long) estimated.getFirst().getFirst());
        Assert.assertEquals(40, (long) estimated.getFirst().getFirst());
        Assert.assertEquals(10, (long) estimated.getSecond());

        // Submit query
        query.submit();

        HashMap<String, Pair<Long, Long>> resultElements = query.resultBufferElements();

        int[] a1 = (int[]) query.getBuffer("a1");
        byte[] a2 = (byte[]) query.getBuffer("a2");
        long[] a2Off = query.getVarBuffer("a2");

        try (Attribute attr1 = schema.getAttribute(0);
            Attribute attr2 = schema.getAttribute(1)) {
          Assert.assertTrue(attr1.getNullable());
          Assert.assertTrue(attr2.getNullable());
        }

        Assert.assertEquals(Long.valueOf(5), resultElements.get("a1").getSecond());
        Assert.assertEquals(Long.valueOf(10), resultElements.get("a2").getSecond());

        short[] a1ValidityByteMap = (short[]) query.getValidityByteMap("a1");
        short[] a2ValidityByteMap = (short[]) query.getValidityByteMap("a2");

        Assert.assertArrayEquals(new int[] {1, 2, 3, 4, 5}, a1);
        Assert.assertArrayEquals(
            new String[] {"aa", "bb", "cc", "dd", "ee"}, Util.bytesToStrings(a2Off, a2));
        Assert.assertArrayEquals(new short[] {0, 0, 0, 1, 1}, a1ValidityByteMap);
        Assert.assertArrayEquals(new short[] {1, 1, 1, 0, 0}, Arrays.copyOf(a2ValidityByteMap, 5));
      }
    }

    @Test
    public void sparseArrayNIOReadTest() throws Exception {
      sparseArrayCreateNullableAttrs(true);
      sparseArrayWrite();

      // Create array and query
      try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
          ArraySchema schema = array.getSchema();
          Query query = new Query(array, TILEDB_READ)) {

        query.setLayout(TILEDB_ROW_MAJOR);

        ByteBuffer dim1Array = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer dim1Offsets = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a1Buffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a1byteMap = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a2Array = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a2Offsets = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());
        ByteBuffer a2byteMapBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());

        query.setBuffer("d1", dim1Array, dim1Offsets);
        query.setBufferNullableNIO("a1", a1Buffer, a1byteMap);
        query.setBufferNullableNIO("a2", a2Offsets, a2Array, a2byteMapBuffer);

        // Submit query
        query.submit();

        Assert.assertEquals(
            5L,
            (long)
                query
                    .resultBufferElementsNIO("d1", Datatype.TILEDB_STRING_ASCII.getNativeSize())
                    .getFirst());

        Assert.assertEquals(
            5L,
            (long)
                query
                    .resultBufferElementsNIO("a1", Datatype.TILEDB_INT32.getNativeSize())
                    .getSecond());
        Assert.assertEquals(
            5L,
            (long)
                query
                    .resultBufferElementsNIO("a2", Datatype.TILEDB_STRING_ASCII.getNativeSize())
                    .getFirst());

        try (Attribute attr1 = schema.getAttribute(0);
            Attribute attr2 = schema.getAttribute(1)) {
          Assert.assertTrue(attr1.getNullable());
          Assert.assertTrue(attr2.getNullable());
        }

        int[] a1Values = new int[5];
        byte[] a1ByteMapValues = new byte[5];

        byte[] a2Bytes = new byte[10];
        long[] a2Off = new long[5];
        byte[] a2ByteMap = new byte[5];

        int idx = 0;
        int bytesIdx = 0;
        while (a1Buffer.hasRemaining()) {
          a1Values[idx] = a1Buffer.getInt();
          a1ByteMapValues[idx] = a1byteMap.get();

          a2Bytes[bytesIdx++] = a2Array.get();
          a2Bytes[bytesIdx++] = a2Array.get();

          a2ByteMap[idx] = a2byteMapBuffer.get();
          a2Off[idx] = a2Offsets.getLong();
          idx++;
        }

        Assert.assertArrayEquals(new int[] {1, 2, 3, 4, 5}, a1Values);
        Assert.assertArrayEquals(new byte[] {0, 0, 0, 1, 1}, a1ByteMapValues);
        Assert.assertArrayEquals(
            new String[] {"aa", "bb", "cc", "dd", "ee"}, Util.bytesToStrings(a2Off, a2Bytes));
        Assert.assertArrayEquals(new byte[] {1, 1, 1, 0, 0}, a2ByteMap);
      }
    }
  }
}
