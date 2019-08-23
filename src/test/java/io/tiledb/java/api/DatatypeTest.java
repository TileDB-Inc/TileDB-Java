package io.tiledb.java.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class DatatypeTest {

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

  private final Datatype[] otherDtypes = {
    Datatype.TILEDB_ANY,
  };

  private List<Datatype> allDatatypes() {
    ArrayList<Datatype> dtypes = new ArrayList<>();
    Collections.addAll(dtypes, stringDtypes);
    Collections.addAll(dtypes, integerDtypes);
    Collections.addAll(dtypes, realDtypes);
    Collections.addAll(dtypes, otherDtypes);
    return dtypes;
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
}
