package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FilterTest {
  private Context ctx;
  private String arrayURI;

  @Before
  public void before() throws TileDBError {
    this.ctx = new Context();
    this.arrayURI = "dict_filter_array";
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
  }

  @After
  public void after() throws TileDBError {
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
  }

  @Test
  public void testNoneFiler() throws Exception {
    try (Context ctx = new Context();
        NoneFilter filter = new NoneFilter(ctx)) {}
  }

  @Test
  public void testGzipFilter() throws Exception {
    try (Context ctx = new Context()) {
      try (GzipFilter filter = new GzipFilter(ctx, 5)) {
        Assert.assertEquals(filter.getLevel(), 5);
      }
      try (GzipFilter filter = new GzipFilter(ctx)) {
        Assert.assertEquals(filter.getLevel(), -1);
      }
    }
  }

  @Test
  public void testZstdFilter() throws Exception {
    try (Context ctx = new Context()) {
      try (ZstdFilter filter = new ZstdFilter(ctx, 5)) {
        Assert.assertEquals(filter.getLevel(), 5);
      }
      try (ZstdFilter filter = new ZstdFilter(ctx)) {
        Assert.assertEquals(filter.getLevel(), -1);
      }
    }
  }

  @Test
  public void testLZ4Filter() throws Exception {
    try (Context ctx = new Context()) {
      try (LZ4Filter filter = new LZ4Filter(ctx, 5)) {
        Assert.assertEquals(filter.getLevel(), 5);
      }
      try (LZ4Filter filter = new LZ4Filter(ctx)) {
        Assert.assertEquals(filter.getLevel(), -1);
      }
    }
  }

  @Test
  public void testFloatScalingFilter() throws Exception {
    try (Context ctx = new Context()) {
      try (FloatScalingFilter filter = new FloatScalingFilter(ctx, 10.0, 10.0, 10L)) {
        Assert.assertEquals(filter.getFactor(), 10.0, 0);
        Assert.assertEquals(filter.getOffset(), 10.0, 0);
        Assert.assertEquals(filter.getByteWidth(), 10L, 0);
      }
      try (FloatScalingFilter filter = new FloatScalingFilter(ctx)) {
        Assert.assertEquals(filter.getFactor(), 1.0, 0);
        Assert.assertEquals(filter.getOffset(), 0.0, 0);
        Assert.assertEquals(filter.getByteWidth(), 8L, 0);
      }
    }
  }

  @Test
  public void testBzip2Filter() throws Exception {
    try (Context ctx = new Context()) {
      try (Bzip2Filter filter = new Bzip2Filter(ctx, 5)) {
        Assert.assertEquals(filter.getLevel(), 5);
      }
      try (Bzip2Filter filter = new Bzip2Filter(ctx)) {
        Assert.assertEquals(filter.getLevel(), -1);
      }
    }
  }

  @Test
  public void testRleFilter() throws Exception {
    // Rle accepts compression level, but it is an ignored parameter
    try (Context ctx = new Context()) {
      try (RleFilter filter = new RleFilter(ctx, 5)) {
        Assert.assertEquals(filter.getLevel(), 5);
      }
      try (RleFilter filter = new RleFilter(ctx)) {
        Assert.assertEquals(filter.getLevel(), -1);
      }
    }
  }

  @Test
  public void testDoubleDeltaFilter() throws Exception {
    // DD accepts compression level, but it is an ignored parameter
    try (Context ctx = new Context()) {
      try (DoubleDeltaFilter filter = new DoubleDeltaFilter(ctx, 5)) {
        Assert.assertEquals(filter.getLevel(), 5);
      }
      try (DoubleDeltaFilter filter = new DoubleDeltaFilter(ctx)) {
        Assert.assertEquals(filter.getLevel(), -1);
      }
    }
  }

  @Test
  public void testBitShuffle() throws Exception {
    try (Context ctx = new Context();
        BitShuffleFilter filter = new BitShuffleFilter(ctx)) {}
  }

  @Test
  public void testBitSort() throws Exception {
    Config config = new Config();
    config.set("tiledb.sm.bitsort_filter_enabled", "true");
    try (Context ctx = new Context(config);
        BitSortFilter filter = new BitSortFilter(ctx)) {}
    config.close();
  }

  @Test
  public void testByteShuffle() throws Exception {
    try (Context ctx = new Context();
        ByteShuffleFilter filter = new ByteShuffleFilter(ctx)) {}
  }

  @Test
  public void testBitWidthReductionFilter() throws Exception {
    try (Context ctx = new Context()) {
      try (BitWidthReductionFilter filter = new BitWidthReductionFilter(ctx, 1024)) {
        Assert.assertEquals(filter.getWindow(), 1024);
      }
      try (BitWidthReductionFilter filter = new BitWidthReductionFilter(ctx)) {
        Assert.assertTrue(filter.getWindow() > 0);
      }
    }
  }

  @Test
  public void testPositiveDeltaFilter() throws Exception {
    try (Context ctx = new Context()) {
      try (PositiveDeltaFilter filter = new PositiveDeltaFilter(ctx, 1024)) {
        Assert.assertEquals(filter.getWindow(), 1024);
      }
      try (PositiveDeltaFilter filter = new PositiveDeltaFilter(ctx)) {
        Assert.assertTrue(filter.getWindow() > 0);
      }
    }
  }

  @Test
  public void testCheckSumMD5() throws Exception {
    try (Context ctx = new Context();
        CheckSumMD5Filter filter = new CheckSumMD5Filter(ctx)) {}
  }

  @Test
  public void testCheckSumSHA256() throws Exception {
    try (Context ctx = new Context();
        CheckSumSHA256Filter filter = new CheckSumSHA256Filter(ctx)) {}
  }

  @Test
  public void testXORFilter() throws Exception {
    try (Context ctx = new Context();
        XORFilter filter = new XORFilter(ctx)) {}
  }

  @Test
  public void testDictionaryFilter() throws Exception {
    try (Context ctx = new Context()) {
      try (DictionaryFilter filter = new DictionaryFilter(ctx, 5)) {
        Assert.assertEquals(filter.getLevel(), 5);
      }
      try (DictionaryFilter filter = new DictionaryFilter(ctx)) {
        Assert.assertEquals(filter.getLevel(), -1);
      }
    }
  }

  public void arrayCreate() throws TileDBError {
    Dimension<Integer> d1 =
        new Dimension<Integer>(ctx, "d1", Datatype.TILEDB_INT32, new Pair<>(0, 5), 1);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Attribute a1 = new Attribute(ctx, "a1", Datatype.TILEDB_STRING_ASCII);
    a1.setCellVar();
    // add dictionary filter to variable length string attribute
    a1.setFilterList(new FilterList(ctx).addFilter(new DictionaryFilter(ctx)));

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws TileDBError {

    NativeArray a1_data =
        new NativeArray(
            ctx, "foo" + "foo" + "foobar" + "bar" + "bar" + "bar", Datatype.TILEDB_STRING_ASCII);
    NativeArray a1_off =
        new NativeArray(ctx, new long[] {0, 3, 6, 12, 15, 18}, Datatype.TILEDB_UINT64);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(TILEDB_GLOBAL_ORDER);

    query.setBuffer("a1", a1_off, a1_data);

    // Submit query
    query.submit();

    query.finalizeQuery();
    query.close();
    array.close();
  }

  @Test
  public void testDictionaryFilterWithData() throws TileDBError {
    arrayCreate();
    arrayWrite();

    NativeArray subarray = new NativeArray(ctx, new int[] {0, 5}, Integer.class);
    // Create array and query
    try (Array array = new Array(ctx, arrayURI, TILEDB_READ);
        ArraySchema schema = array.getSchema();
        Query query = new Query(array, TILEDB_READ)) {

      query.setSubarray(subarray);

      query.setLayout(TILEDB_ROW_MAJOR);

      NativeArray a1Data = new NativeArray(ctx, 100, Datatype.TILEDB_STRING_ASCII);
      NativeArray a1Offsets = new NativeArray(ctx, 100, Datatype.TILEDB_UINT64);

      query.setBuffer("a1", a1Offsets, a1Data);

      // Submit query
      query.submit();

      long[] a1_offsets = (long[]) query.getVarBuffer("a1");
      byte[] a1_data = (byte[]) query.getBuffer("a1");

      Assert.assertArrayEquals(a1_offsets, new long[] {0, 3, 6, 12, 15, 18});
      String[] results = Util.bytesToStrings(a1_offsets, a1_data);
      Assert.assertArrayEquals(new String[] {"foo", "foo", "foobar", "bar", "bar", "bar"}, results);
    }
  }
}
