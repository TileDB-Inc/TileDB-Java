/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.libtiledb;

public enum tiledb_array_type_t {
  TILEDB_DENSE(0),
  TILEDB_SPARSE(1);

  public final int swigValue() {
    return swigValue;
  }

  public static tiledb_array_type_t swigToEnum(int swigValue) {
    tiledb_array_type_t[] swigValues = tiledb_array_type_t.class.getEnumConstants();
    if (swigValue < swigValues.length
        && swigValue >= 0
        && swigValues[swigValue].swigValue == swigValue) return swigValues[swigValue];
    for (tiledb_array_type_t swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue) return swigEnum;
    throw new IllegalArgumentException(
        "No enum " + tiledb_array_type_t.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private tiledb_array_type_t() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private tiledb_array_type_t(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue + 1;
  }

  @SuppressWarnings("unused")
  private tiledb_array_type_t(tiledb_array_type_t swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue + 1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}
