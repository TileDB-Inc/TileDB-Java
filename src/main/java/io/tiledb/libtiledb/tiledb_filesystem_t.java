/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.libtiledb;

public enum tiledb_filesystem_t {
  TILEDB_HDFS(0),
  TILEDB_S3(1),
  TILEDB_AZURE(2),
  TILEDB_GCS(3),
  TILEDB_MEMFS(4);

  public final int swigValue() {
    return swigValue;
  }

  public static tiledb_filesystem_t swigToEnum(int swigValue) {
    tiledb_filesystem_t[] swigValues = tiledb_filesystem_t.class.getEnumConstants();
    if (swigValue < swigValues.length
        && swigValue >= 0
        && swigValues[swigValue].swigValue == swigValue) return swigValues[swigValue];
    for (tiledb_filesystem_t swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue) return swigEnum;
    throw new IllegalArgumentException(
        "No enum " + tiledb_filesystem_t.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private tiledb_filesystem_t() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private tiledb_filesystem_t(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue + 1;
  }

  @SuppressWarnings("unused")
  private tiledb_filesystem_t(tiledb_filesystem_t swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue + 1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}
