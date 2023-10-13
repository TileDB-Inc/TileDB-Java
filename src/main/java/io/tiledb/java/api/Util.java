package io.tiledb.java.api;

import java.math.BigInteger;
import java.util.Arrays;

/** Contains helper-functions */
public class Util {
  /**
   * Converts an input array of bytes to a list of Strings, according to the offsets
   *
   * @param offsets The offsets array
   * @param data THe data array
   * @return The list of Strings
   */
  public static String[] bytesToStrings(long[] offsets, byte[] data) {
    String[] results = new String[offsets.length];
    int start = 0, end;

    // Convert bytes to string array
    for (int i = 0; i < offsets.length; ++i) {
      if (i < offsets.length - 1) {
        end = (int) offsets[i + 1];
        results[i] = new String(Arrays.copyOfRange(data, start, end));
        start = end;
      } else {
        end = data.length;
        results[i] = new String(Arrays.copyOfRange(data, start, end));
      }
    }

    return results;
  }

  public static int castLongToInt(long num) throws TileDBError {
    if (num > Integer.MAX_VALUE)
      throw new TileDBError(num + " is larger that the integer max value");

    return (int) num;
  }

  /**
   * Checks if a given bigInteger value is in the UInt64 range
   *
   * @param bigInteger the bigInteger
   * @return true if the range is ok
   */
  public static boolean checkBigIntegerRange(BigInteger bigInteger) throws TileDBError {
    BigInteger upperLimit = new BigInteger("18446744073709551615");
    if ((bigInteger.compareTo(upperLimit) > 0)
        || (bigInteger.compareTo(BigInteger.valueOf(-1L)) <= 0)) {
      throw new TileDBError(bigInteger + " is outside the uint64 range");
    }
    return true;
  }

  /**
   * Returns the Datatype of the input field
   *
   * @param array The TileDB array
   * @param fieldName The field name
   * @return The Datatype
   * @throws TileDBError A TileDBError
   */
  public static Datatype getFieldDatatype(Array array, String fieldName) throws TileDBError {
    Datatype dt;
    try (ArraySchema schema = array.getSchema()) {
      try (Domain domain = schema.getDomain()) {
        if (domain.hasDimension(fieldName)) {
          Dimension dim = domain.getDimension(fieldName);
          dt = dim.getType();
          dim.close();
        } else {
          try (Attribute attribute = schema.getAttribute(fieldName)) {
            dt = attribute.getType();
          }
        }
      }
    }

    return dt;
  }
}
