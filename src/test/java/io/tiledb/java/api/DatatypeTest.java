package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Test;

public class DatatypeTest {

  private final Datatype[] dtypes = {
    Datatype.TILEDB_ANY,
    Datatype.TILEDB_FLOAT32,
    Datatype.TILEDB_FLOAT64,
    Datatype.TILEDB_INT8,
    Datatype.TILEDB_UINT8,
    Datatype.TILEDB_INT16,
    Datatype.TILEDB_UINT16,
    Datatype.TILEDB_INT32,
    Datatype.TILEDB_UINT32,
    Datatype.TILEDB_INT64,
    Datatype.TILEDB_UINT64,
    Datatype.TILEDB_STRING_ASCII,
    Datatype.TILEDB_STRING_UTF8,
    Datatype.TILEDB_STRING_UTF16,
    Datatype.TILEDB_STRING_UTF32,
    Datatype.TILEDB_STRING_UCS2,
    Datatype.TILEDB_STRING_UCS4
  };

  @Test
  public void testDatatypeJNIConversion() throws Exception {
    // roundtrip to and from swig enum representation
    for (Datatype dtype : dtypes) {
      Assert.assertEquals(dtype, Datatype.fromSwigEnum(dtype.toSwigEnum()));
    }
  }

  @Test
  public void testNativeStringTypeConversion() throws Exception {
    Assert.assertEquals(String.class, Types.getJavaType(Datatype.TILEDB_STRING_ASCII));
    Assert.assertEquals(String.class, Types.getJavaType(Datatype.TILEDB_STRING_UTF8));
    Assert.assertEquals(String.class, Types.getJavaType(Datatype.TILEDB_STRING_UTF16));
    Assert.assertEquals(String.class, Types.getJavaType(Datatype.TILEDB_STRING_UTF32));
    Assert.assertEquals(String.class, Types.getJavaType(Datatype.TILEDB_STRING_UCS2));
    Assert.assertEquals(String.class, Types.getJavaType(Datatype.TILEDB_STRING_UCS4));
  }

  @Test
  public void testJavaStringTypeConversion() throws Exception {
    Assert.assertEquals(Datatype.TILEDB_STRING_UTF16, Types.getNativeType(String.class));
  }
}
