/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.libtiledb;

public enum tiledb_filter_option_t {
  TILEDB_COMPRESSION_LEVEL(0),
  TILEDB_BIT_WIDTH_MAX_WINDOW(1),
  TILEDB_POSITIVE_DELTA_MAX_WINDOW(2),
  TILEDB_SCALE_FLOAT_BYTEWIDTH(3),
  TILEDB_SCALE_FLOAT_FACTOR(4),
  TILEDB_SCALE_FLOAT_OFFSET(5),
  TILEDB_WEBP_QUALITY(6),
  TILEDB_WEBP_INPUT_FORMAT(7),
  TILEDB_WEBP_LOSSLESS(8),
  TILEDB_COMPRESSION_REINTERPRET_DATATYPE(9);

  public final int swigValue() {
    return swigValue;
  }

  public static tiledb_filter_option_t swigToEnum(int swigValue) {
    tiledb_filter_option_t[] swigValues = tiledb_filter_option_t.class.getEnumConstants();
    if (swigValue < swigValues.length
        && swigValue >= 0
        && swigValues[swigValue].swigValue == swigValue) return swigValues[swigValue];
    for (tiledb_filter_option_t swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue) return swigEnum;
    throw new IllegalArgumentException(
        "No enum " + tiledb_filter_option_t.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private tiledb_filter_option_t() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private tiledb_filter_option_t(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue + 1;
  }

  @SuppressWarnings("unused")
  private tiledb_filter_option_t(tiledb_filter_option_t swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue + 1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}
