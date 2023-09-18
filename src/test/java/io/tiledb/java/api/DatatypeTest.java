package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.Constants.TILEDB_VAR_NUM;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;
import static io.tiledb.libtiledb.tiledb_query_condition_op_t.TILEDB_EQ;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DatatypeTest {
  private Context ctx;
  private String arrayURI;

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toString();
  }

  @After
  public void tearDown() throws Exception {
    ctx.close();
  }

  private final Datatype[] stringDtypes = {
    Datatype.TILEDB_STRING_ASCII,
    Datatype.TILEDB_STRING_UTF8,
    Datatype.TILEDB_STRING_UTF16,
    Datatype.TILEDB_STRING_UTF32,
    Datatype.TILEDB_STRING_UCS2,
    Datatype.TILEDB_STRING_UCS4
  };

  private final Datatype[] realDtypes = {
    Datatype.TILEDB_FLOAT32, Datatype.TILEDB_FLOAT64,
  };

  private final Datatype[] integerDtypes = {
    Datatype.TILEDB_CHAR,
    Datatype.TILEDB_INT8,
    Datatype.TILEDB_INT8,
    Datatype.TILEDB_UINT8,
    Datatype.TILEDB_INT16,
    Datatype.TILEDB_UINT16,
    Datatype.TILEDB_INT32,
    Datatype.TILEDB_UINT32,
    Datatype.TILEDB_INT64,
    Datatype.TILEDB_UINT64,
  };

  private final Datatype[] dateDtypes = {
    Datatype.TILEDB_DATETIME_YEAR,
    Datatype.TILEDB_DATETIME_MONTH,
    Datatype.TILEDB_DATETIME_WEEK,
    Datatype.TILEDB_DATETIME_DAY,
    Datatype.TILEDB_DATETIME_HR,
    Datatype.TILEDB_DATETIME_MIN,
    Datatype.TILEDB_DATETIME_SEC,
    Datatype.TILEDB_DATETIME_MS,
    Datatype.TILEDB_DATETIME_US,
    Datatype.TILEDB_DATETIME_NS,
    Datatype.TILEDB_DATETIME_PS,
    Datatype.TILEDB_DATETIME_FS,
    Datatype.TILEDB_DATETIME_AS,
  };

  private final Datatype[] timeDtypes = {
    Datatype.TILEDB_DATETIME_AS,
    Datatype.TILEDB_TIME_HR,
    Datatype.TILEDB_TIME_MIN,
    Datatype.TILEDB_TIME_SEC,
    Datatype.TILEDB_TIME_MS,
    Datatype.TILEDB_TIME_US,
    Datatype.TILEDB_TIME_NS,
    Datatype.TILEDB_TIME_PS,
    Datatype.TILEDB_TIME_FS,
    Datatype.TILEDB_TIME_AS,
  };

  private final Datatype[] otherDtypes = {
    Datatype.TILEDB_ANY,
  };

  private List<Datatype> allDatatypes() {
    ArrayList<Datatype> dtypes = new ArrayList<>();
    Collections.addAll(dtypes, stringDtypes);
    Collections.addAll(dtypes, integerDtypes);
    Collections.addAll(dtypes, realDtypes);
    Collections.addAll(dtypes, dateDtypes);
    Collections.addAll(dtypes, otherDtypes);
    Collections.addAll(dtypes, timeDtypes);
    return dtypes;
  }

  @Test
  public void testEnumeratedDatatype() throws Exception {
    // test api
    Dimension<Integer> rows =
        new Dimension<>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

    Domain domain = new Domain(ctx);
    domain.addDimension(rows);

    Attribute a1 = new Attribute(ctx, "a1", Datatype.TILEDB_INT32);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);

    NativeArray enumsOffsets =
        new NativeArray(ctx, new long[] {0, 2, 4, 6}, Datatype.TILEDB_UINT64);
    NativeArray enums = new NativeArray(ctx, "aabbccdd", Datatype.TILEDB_STRING_ASCII);

    Enumeration en =
        new Enumeration(
            ctx,
            "test_enum",
            TILEDB_VAR_NUM,
            Datatype.TILEDB_INT32,
            false,
            enums,
            BigInteger.valueOf(enums.getSize() * Datatype.TILEDB_STRING_ASCII.getNativeSize()),
            enumsOffsets,
            BigInteger.valueOf(enumsOffsets.getSize() * Datatype.TILEDB_UINT64.getNativeSize()));

    schema.addEnumeration(en);
    a1.setEnumerationName("test_enum");

    schema.addAttribute(a1);

    Array.create(arrayURI, schema);

    Array array = new Array(ctx, arrayURI);
    String attGetEnumName = array.getSchema().getAttribute("a1").getEnumerationName();
    Assert.assertEquals("test_enum", attGetEnumName);

    Enumeration e = array.getEnumeration("test_enum");

    Assert.assertEquals("test_enum", e.getName());
    Assert.assertEquals(Datatype.TILEDB_INT32, e.getType());
    Assert.assertFalse(e.getOrdered());
    //    Assert.assertArrayEquals(new int[] {0, 1, 2, 3}, (int[]) e.getData());
    array.close();

    // test data write
    NativeArray na1 = new NativeArray(ctx, new int[] {0, 1, 2, 0}, Datatype.TILEDB_INT32);
    array = new Array(ctx, arrayURI, TILEDB_WRITE);
    Query query = new Query(array, TILEDB_WRITE);

    query.setDataBuffer("a1", na1);
    query.submit();

    query.close();
    array.close();

    // test data read
    array = new Array(ctx, arrayURI, TILEDB_READ);
    query = new Query(array, TILEDB_READ);
    SubArray sub = new SubArray(ctx, array);
    sub.addRange(0, 1, 4, null);
    query.setSubarray(sub);
    query.setDataBuffer("a1", new NativeArray(ctx, 4, Datatype.TILEDB_INT32));
    query.submit();

    int[] a1Result = (int[]) query.getBuffer("a1");
    Assert.assertArrayEquals(new int[] {0, 1, 2, 0}, a1Result);

    // test data read with QC
    query = new Query(array, TILEDB_READ);
    query.setSubarray(sub);
    QueryCondition qc =
        new QueryCondition(ctx, Datatype.TILEDB_STRING_ASCII, "a1", "aa", TILEDB_EQ);
    query.setCondition(qc);
    query.setDataBuffer("a1", new NativeArray(ctx, 4, Datatype.TILEDB_INT32));
    query.submit();
    int[] a1ResultAfterQC = (int[]) query.getBuffer("a1");
    Assert.assertArrayEquals(
        new int[] {0, Integer.MIN_VALUE, Integer.MIN_VALUE, 0}, a1ResultAfterQC);

    // test Schema evolution
    //    ArraySchemaEvolution evolution = new ArraySchemaEvolution(ctx);
    //    Enumeration en2 =
    //        new Enumeration(
    //            ctx,
    //            "test_enum2",
    //            TILEDB_VAR_NUM,
    //            Datatype.TILEDB_INT32,
    //            false,
    //            enums,
    //            BigInteger.valueOf(enums.getSize() *
    // Datatype.TILEDB_STRING_ASCII.getNativeSize()),
    //            enumsOffsets,
    //            BigInteger.valueOf(enumsOffsets.getSize() *
    // Datatype.TILEDB_UINT64.getNativeSize()));
    //    evolution.addEnumeration(en2);
    //    evolution.evolveArray(arrayURI);
    //
    //    // reopen array
    //    array = new Array(ctx, arrayURI);
    //    e = array.getEnumeration("test_enum2");
    //    Assert.assertEquals(e.getName(), "test_enum2");
  }

  public void arrayCreate() throws Exception {
    Dimension<Integer> rows =
        new Dimension<>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

    Domain domain = new Domain(ctx);
    domain.addDimension(rows);

    Attribute a1 = new Attribute(ctx, "a1", Datatype.TILEDB_TIME_AS);
    Attribute a2 = new Attribute(ctx, "a2", Datatype.TILEDB_TIME_MIN);
    Attribute a3 = new Attribute(ctx, "a3", Datatype.TILEDB_TIME_HR);
    Attribute a4 = new Attribute(ctx, "a4", Datatype.TILEDB_TIME_SEC);
    Attribute a5 = new Attribute(ctx, "a5", Datatype.TILEDB_TIME_MS);
    Attribute a6 = new Attribute(ctx, "a6", Datatype.TILEDB_TIME_US);
    Attribute a7 = new Attribute(ctx, "a7", Datatype.TILEDB_TIME_NS);
    Attribute a8 = new Attribute(ctx, "a8", Datatype.TILEDB_TIME_PS);
    Attribute a9 = new Attribute(ctx, "a9", Datatype.TILEDB_TIME_FS);
    Attribute a10 = new Attribute(ctx, "a10", Datatype.TILEDB_TIME_AS);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);
    schema.addAttribute(a3);
    schema.addAttribute(a4);
    schema.addAttribute(a5);
    schema.addAttribute(a6);
    schema.addAttribute(a7);
    schema.addAttribute(a8);
    schema.addAttribute(a9);
    schema.addAttribute(a10);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {
    NativeArray a1 = new NativeArray(ctx, new long[] {3, 5, 6, 9}, Datatype.TILEDB_TIME_AS);
    NativeArray a2 = new NativeArray(ctx, new long[] {13, 15, 16, 19}, Datatype.TILEDB_TIME_MIN);
    NativeArray a3 = new NativeArray(ctx, new long[] {23, 25, 26, 29}, Datatype.TILEDB_TIME_HR);
    NativeArray a4 = new NativeArray(ctx, new long[] {33, 35, 36, 39}, Datatype.TILEDB_TIME_SEC);
    NativeArray a5 = new NativeArray(ctx, new long[] {43, 45, 46, 49}, Datatype.TILEDB_TIME_MS);
    NativeArray a6 = new NativeArray(ctx, new long[] {53, 55, 56, 59}, Datatype.TILEDB_TIME_US);
    NativeArray a7 = new NativeArray(ctx, new long[] {63, 65, 66, 69}, Datatype.TILEDB_TIME_NS);
    NativeArray a8 = new NativeArray(ctx, new long[] {73, 75, 76, 79}, Datatype.TILEDB_TIME_PS);
    NativeArray a9 = new NativeArray(ctx, new long[] {83, 85, 86, 89}, Datatype.TILEDB_TIME_FS);
    NativeArray a10 = new NativeArray(ctx, new long[] {93, 95, 96, 99}, Datatype.TILEDB_TIME_AS);

    // Create query
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
        Query query = new Query(array)) {
      query.setLayout(TILEDB_ROW_MAJOR);
      query.setBuffer("a1", a1);
      query.setBuffer("a2", a2);
      query.setBuffer("a3", a3);
      query.setBuffer("a4", a4);
      query.setBuffer("a5", a5);
      query.setBuffer("a6", a6);
      query.setBuffer("a7", a7);
      query.setBuffer("a8", a8);
      query.setBuffer("a9", a9);
      query.setBuffer("a10", a10);
      // Submit query
      query.submit();
    }
  }

  @Test
  public void testTileDBTimeTypes() throws Exception {
    arrayCreate();
    arrayWrite();
    Array array = new Array(ctx, arrayURI);
    try (Query query = new Query(array, TILEDB_READ)) {
      SubArray subArray = new SubArray(ctx, array);
      subArray.addRange(0, 1, 4, null);

      query.setBuffer("a1", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_AS));
      query.setBuffer("a2", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_MIN));
      query.setBuffer("a3", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_HR));
      query.setBuffer("a4", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_SEC));
      query.setBuffer("a5", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_MS));
      query.setBuffer("a6", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_US));
      query.setBuffer("a7", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_NS));
      query.setBuffer("a8", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_PS));
      query.setBuffer("a9", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_FS));
      query.setBuffer("a10", new NativeArray(ctx, 4, Datatype.TILEDB_TIME_AS));

      query.setSubarray(subArray).setLayout(TILEDB_ROW_MAJOR);
      while (query.getQueryStatus() != QueryStatus.TILEDB_COMPLETED) {
        query.submit();
      }
      long[] a1_buff = (long[]) query.getBuffer("a1");
      long[] a2_buff = (long[]) query.getBuffer("a2");
      long[] a3_buff = (long[]) query.getBuffer("a3");
      long[] a4_buff = (long[]) query.getBuffer("a4");
      long[] a5_buff = (long[]) query.getBuffer("a5");
      long[] a6_buff = (long[]) query.getBuffer("a6");
      long[] a7_buff = (long[]) query.getBuffer("a7");
      long[] a8_buff = (long[]) query.getBuffer("a8");
      long[] a9_buff = (long[]) query.getBuffer("a9");
      long[] a10_buff = (long[]) query.getBuffer("a10");

      Assert.assertArrayEquals(new long[] {3, 5, 6, 9}, a1_buff);
      Assert.assertArrayEquals(new long[] {13, 15, 16, 19}, a2_buff);
      Assert.assertArrayEquals(new long[] {23, 25, 26, 29}, a3_buff);
      Assert.assertArrayEquals(new long[] {33, 35, 36, 39}, a4_buff);
      Assert.assertArrayEquals(new long[] {43, 45, 46, 49}, a5_buff);
      Assert.assertArrayEquals(new long[] {53, 55, 56, 59}, a6_buff);
      Assert.assertArrayEquals(new long[] {63, 65, 66, 69}, a7_buff);
      Assert.assertArrayEquals(new long[] {73, 75, 76, 79}, a8_buff);
      Assert.assertArrayEquals(new long[] {83, 85, 86, 89}, a9_buff);
      Assert.assertArrayEquals(new long[] {93, 95, 96, 99}, a10_buff);
    }
  }

  @Test
  public void testDatatypeJNIConversion() throws Exception {
    // roundtrip to and from swig enum representation
    for (Datatype dtype : allDatatypes()) {
      Assert.assertEquals(dtype, Datatype.fromSwigEnum(dtype.toSwigEnum()));
    }
  }

  private void checkIfType(Datatype[] trueDtypes) {}

  @Test
  public void testIsStringDtypes() {
    List<Datatype> stringDtypesList = Arrays.asList(stringDtypes);
    for (Datatype dtype : allDatatypes()) {
      if (stringDtypesList.contains(dtype)) {
        Assert.assertTrue(dtype.isStringType());
      } else {
        Assert.assertFalse(dtype.isStringType());
      }
    }
  }

  @Test
  public void testIsTimeDtypes() {
    List<Datatype> timeDtypesList = Arrays.asList(timeDtypes);
    for (Datatype dtype : allDatatypes()) {
      if (timeDtypesList.contains(dtype)) {
        Assert.assertTrue(dtype.isTimeType());
      } else {
        Assert.assertFalse(dtype.isTimeType());
      }
    }
  }

  @Test
  public void testIsIntegerDtypes() {
    List<Datatype> integerDtypesList = Arrays.asList(integerDtypes);
    for (Datatype dtype : allDatatypes()) {
      if (integerDtypesList.contains(dtype)) {
        Assert.assertTrue(dtype.isIntegerType());
      } else {
        Assert.assertFalse(dtype.isIntegerType());
      }
    }
  }

  @Test
  public void testIsDateDtypes() {
    List<Datatype> dateDtypesList = Arrays.asList(dateDtypes);
    for (Datatype dtype : allDatatypes()) {
      if (dateDtypesList.contains(dtype)) {
        Assert.assertTrue(dtype.isDateType());
      } else {
        Assert.assertFalse(dtype.isDateType());
      }
    }
  }

  @Test
  public void testIsRealDtypes() {
    List<Datatype> realDtypesList = Arrays.asList(realDtypes);
    for (Datatype dtype : allDatatypes()) {
      if (realDtypesList.contains(dtype)) {
        Assert.assertTrue(dtype.isRealType());
      } else {
        Assert.assertFalse(dtype.isRealType());
      }
    }
  }

  @Test
  public void testSize() throws TileDBError {
    Assert.assertEquals(4, Datatype.TILEDB_INT32.size());
    Assert.assertEquals(8, Datatype.TILEDB_INT64.size());
    Assert.assertEquals(4, Datatype.TILEDB_FLOAT32.size());
    Assert.assertEquals(8, Datatype.TILEDB_FLOAT64.size());
    Assert.assertEquals(1, Datatype.TILEDB_CHAR.size());
    Assert.assertEquals(1, Datatype.TILEDB_INT8.size());
    Assert.assertEquals(1, Datatype.TILEDB_UINT8.size());
    Assert.assertEquals(2, Datatype.TILEDB_INT16.size());
    Assert.assertEquals(2, Datatype.TILEDB_UINT16.size());
    Assert.assertEquals(4, Datatype.TILEDB_UINT32.size());
    Assert.assertEquals(8, Datatype.TILEDB_UINT64.size());
    Assert.assertEquals(1, Datatype.TILEDB_STRING_ASCII.size());
    Assert.assertEquals(1, Datatype.TILEDB_STRING_UTF8.size());
    Assert.assertEquals(2, Datatype.TILEDB_STRING_UTF16.size());
    Assert.assertEquals(4, Datatype.TILEDB_STRING_UTF32.size());
    Assert.assertEquals(2, Datatype.TILEDB_STRING_UCS2.size());
    Assert.assertEquals(4, Datatype.TILEDB_STRING_UCS4.size());
    Assert.assertEquals(8, Datatype.TILEDB_DATETIME_YEAR.size());
    Assert.assertEquals(8, Datatype.TILEDB_TIME_HR.size());
    Assert.assertEquals(1, Datatype.TILEDB_BLOB.size());
    Assert.assertEquals(1, Datatype.TILEDB_BOOL.size());
  }
}
