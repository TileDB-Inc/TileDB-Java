/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.libtiledb;

public enum tiledb_compressor_t {
  TILEDB_NO_COMPRESSION(0),
  TILEDB_GZIP(1),
  TILEDB_ZSTD(2),
  TILEDB_LZ4(3),
  TILEDB_RLE(4),
  TILEDB_BZIP2(5),
  TILEDB_DOUBLE_DELTA(6);

  public final int swigValue() {
    return swigValue;
  }

  public static tiledb_compressor_t swigToEnum(int swigValue) {
    tiledb_compressor_t[] swigValues = tiledb_compressor_t.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (tiledb_compressor_t swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + tiledb_compressor_t.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private tiledb_compressor_t() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private tiledb_compressor_t(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private tiledb_compressor_t(tiledb_compressor_t swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}

