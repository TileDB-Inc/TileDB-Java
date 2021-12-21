package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_datatype_t;

public enum Datatype {
  TILEDB_INT32,
  TILEDB_INT64,
  TILEDB_FLOAT32,
  TILEDB_FLOAT64,
  TILEDB_CHAR,
  TILEDB_INT8,
  TILEDB_UINT8,
  TILEDB_INT16,
  TILEDB_UINT16,
  TILEDB_UINT32,
  TILEDB_UINT64,
  TILEDB_STRING_ASCII,
  TILEDB_STRING_UTF8,
  TILEDB_STRING_UTF16,
  TILEDB_STRING_UTF32,
  TILEDB_STRING_UCS2,
  TILEDB_STRING_UCS4,
  TILEDB_ANY,
  TILEDB_DATETIME_YEAR,
  TILEDB_DATETIME_MONTH,
  TILEDB_DATETIME_WEEK,
  TILEDB_DATETIME_DAY,
  TILEDB_DATETIME_HR,
  TILEDB_DATETIME_MIN,
  TILEDB_DATETIME_SEC,
  TILEDB_DATETIME_MS,
  TILEDB_DATETIME_US,
  TILEDB_DATETIME_NS,
  TILEDB_DATETIME_PS,
  TILEDB_DATETIME_FS,
  TILEDB_DATETIME_AS,
  TILEDB_TIME_HR,
  TILEDB_TIME_MIN,
  TILEDB_TIME_SEC,
  TILEDB_TIME_MS,
  TILEDB_TIME_US,
  TILEDB_TIME_NS,
  TILEDB_TIME_PS,
  TILEDB_TIME_FS,
  TILEDB_TIME_AS;

  /** @return Returns the TileDB Datatype size in Bytes * */
  public int getNativeSize() throws TileDBError {
    return tiledb.tiledb_datatype_size(this.toSwigEnum()).intValue();
  }

  /** @return True if the TileDB Datatype is a scalar, false otherwise (ex. String) * */
  public boolean isStringType() {
    switch (this) {
      case TILEDB_STRING_ASCII:
      case TILEDB_STRING_UTF8:
      case TILEDB_STRING_UTF16:
      case TILEDB_STRING_UTF32:
      case TILEDB_STRING_UCS2:
      case TILEDB_STRING_UCS4:
        return true;
      default:
        return false;
    }
  }

  /** @return True if the TileDB Datatype is an integer, false otherwise * */
  public boolean isIntegerType() {
    switch (this) {
      case TILEDB_CHAR:
      case TILEDB_INT8:
      case TILEDB_UINT8:
      case TILEDB_INT16:
      case TILEDB_UINT16:
      case TILEDB_INT32:
      case TILEDB_UINT32:
      case TILEDB_INT64:
      case TILEDB_UINT64:
        return true;
      default:
        return false;
    }
  }

  /** @return True if the TileDB Datatype is an datetype, false otherwise * */
  public boolean isDateType() {
    switch (this) {
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_DATETIME_AS:
        return true;
      default:
        return false;
    }
  }

  /** @return True if the TileDB Datatype is a timetype, false otherwise * */
  public boolean isTimeType() {
    switch (this) {
      case TILEDB_DATETIME_AS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        return true;
      default:
        return false;
    }
  }

  /** @return True if the TileDB Datatype is a float or double, false otherwise * */
  public boolean isRealType() {
    switch (this) {
      case TILEDB_FLOAT32:
      case TILEDB_FLOAT64:
        return true;
      default:
        return false;
    }
  }

  /**
   * Returns the equivalent java class for a given TileDB Type
   *
   * @return class
   * @throws TileDBError
   */
  public Class javaClass() throws TileDBError {
    switch (this) {
      case TILEDB_INT32:
        return Integer.class;
      case TILEDB_INT64:
        return Long.class;
      case TILEDB_FLOAT32:
        return Float.class;
      case TILEDB_FLOAT64:
        return Double.class;
      case TILEDB_CHAR:
        return Byte.class;
      case TILEDB_INT8:
        return Byte.class;
      case TILEDB_UINT8:
        return Short.class;
      case TILEDB_INT16:
        return Short.class;
      case TILEDB_UINT16:
        return Integer.class;
      case TILEDB_UINT32:
        return Long.class;
      case TILEDB_UINT64:
        return Long.class;
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_DATETIME_AS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        return Long.class;
      case TILEDB_STRING_UTF8:
        return String.class;
      case TILEDB_STRING_UTF16:
        return String.class;
      case TILEDB_STRING_UTF32:
        return String.class;
      case TILEDB_STRING_UCS2:
        return String.class;
      case TILEDB_STRING_UCS4:
        return String.class;
      case TILEDB_STRING_ASCII:
        return String.class;
      case TILEDB_ANY:
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected tiledb_datatype_t toSwigEnum() throws TileDBError {
    switch (this) {
      case TILEDB_INT32:
        return tiledb_datatype_t.TILEDB_INT32;
      case TILEDB_INT64:
        return tiledb_datatype_t.TILEDB_INT64;
      case TILEDB_FLOAT32:
        return tiledb_datatype_t.TILEDB_FLOAT32;
      case TILEDB_FLOAT64:
        return tiledb_datatype_t.TILEDB_FLOAT64;
      case TILEDB_CHAR:
        return tiledb_datatype_t.TILEDB_CHAR;
      case TILEDB_INT8:
        return tiledb_datatype_t.TILEDB_INT8;
      case TILEDB_UINT8:
        return tiledb_datatype_t.TILEDB_UINT8;
      case TILEDB_INT16:
        return tiledb_datatype_t.TILEDB_INT16;
      case TILEDB_UINT16:
        return tiledb_datatype_t.TILEDB_UINT16;
      case TILEDB_UINT32:
        return tiledb_datatype_t.TILEDB_UINT32;
      case TILEDB_UINT64:
        return tiledb_datatype_t.TILEDB_UINT64;
      case TILEDB_STRING_ASCII:
        return tiledb_datatype_t.TILEDB_STRING_ASCII;
      case TILEDB_STRING_UTF8:
        return tiledb_datatype_t.TILEDB_STRING_UTF8;
      case TILEDB_STRING_UTF16:
        return tiledb_datatype_t.TILEDB_STRING_UTF16;
      case TILEDB_STRING_UTF32:
        return tiledb_datatype_t.TILEDB_STRING_UTF32;
      case TILEDB_STRING_UCS2:
        return tiledb_datatype_t.TILEDB_STRING_UCS2;
      case TILEDB_STRING_UCS4:
        return tiledb_datatype_t.TILEDB_STRING_UCS4;
      case TILEDB_ANY:
        return tiledb_datatype_t.TILEDB_ANY;
      case TILEDB_DATETIME_YEAR:
        return tiledb_datatype_t.TILEDB_DATETIME_YEAR;
      case TILEDB_DATETIME_MONTH:
        return tiledb_datatype_t.TILEDB_DATETIME_MONTH;
      case TILEDB_DATETIME_WEEK:
        return tiledb_datatype_t.TILEDB_DATETIME_WEEK;
      case TILEDB_DATETIME_DAY:
        return tiledb_datatype_t.TILEDB_DATETIME_DAY;
      case TILEDB_DATETIME_HR:
        return tiledb_datatype_t.TILEDB_DATETIME_HR;
      case TILEDB_DATETIME_MIN:
        return tiledb_datatype_t.TILEDB_DATETIME_MIN;
      case TILEDB_DATETIME_SEC:
        return tiledb_datatype_t.TILEDB_DATETIME_SEC;
      case TILEDB_DATETIME_MS:
        return tiledb_datatype_t.TILEDB_DATETIME_MS;
      case TILEDB_DATETIME_US:
        return tiledb_datatype_t.TILEDB_DATETIME_US;
      case TILEDB_DATETIME_NS:
        return tiledb_datatype_t.TILEDB_DATETIME_NS;
      case TILEDB_DATETIME_PS:
        return tiledb_datatype_t.TILEDB_DATETIME_PS;
      case TILEDB_DATETIME_FS:
        return tiledb_datatype_t.TILEDB_DATETIME_FS;
      case TILEDB_DATETIME_AS:
        return tiledb_datatype_t.TILEDB_DATETIME_AS;
      case TILEDB_TIME_HR:
        return tiledb_datatype_t.TILEDB_TIME_HR;
      case TILEDB_TIME_MIN:
        return tiledb_datatype_t.TILEDB_TIME_MIN;
      case TILEDB_TIME_SEC:
        return tiledb_datatype_t.TILEDB_TIME_SEC;
      case TILEDB_TIME_MS:
        return tiledb_datatype_t.TILEDB_TIME_MS;
      case TILEDB_TIME_US:
        return tiledb_datatype_t.TILEDB_TIME_US;
      case TILEDB_TIME_NS:
        return tiledb_datatype_t.TILEDB_TIME_NS;
      case TILEDB_TIME_PS:
        return tiledb_datatype_t.TILEDB_TIME_PS;
      case TILEDB_TIME_FS:
        return tiledb_datatype_t.TILEDB_TIME_FS;
      case TILEDB_TIME_AS:
        return tiledb_datatype_t.TILEDB_TIME_AS;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static Datatype fromSwigEnum(tiledb_datatype_t e) throws TileDBError {
    switch (e) {
      case TILEDB_INT32:
        return TILEDB_INT32;
      case TILEDB_INT64:
        return TILEDB_INT64;
      case TILEDB_FLOAT32:
        return TILEDB_FLOAT32;
      case TILEDB_FLOAT64:
        return TILEDB_FLOAT64;
      case TILEDB_CHAR:
        return TILEDB_CHAR;
      case TILEDB_INT8:
        return TILEDB_INT8;
      case TILEDB_UINT8:
        return TILEDB_UINT8;
      case TILEDB_INT16:
        return TILEDB_INT16;
      case TILEDB_UINT16:
        return TILEDB_UINT16;
      case TILEDB_UINT32:
        return TILEDB_UINT32;
      case TILEDB_UINT64:
        return TILEDB_UINT64;
      case TILEDB_STRING_ASCII:
        return TILEDB_STRING_ASCII;
      case TILEDB_STRING_UTF8:
        return TILEDB_STRING_UTF8;
      case TILEDB_STRING_UTF16:
        return TILEDB_STRING_UTF16;
      case TILEDB_STRING_UTF32:
        return TILEDB_STRING_UTF32;
      case TILEDB_STRING_UCS2:
        return TILEDB_STRING_UCS2;
      case TILEDB_STRING_UCS4:
        return TILEDB_STRING_UCS4;
      case TILEDB_ANY:
        return TILEDB_ANY;
      case TILEDB_DATETIME_YEAR:
        return TILEDB_DATETIME_YEAR;
      case TILEDB_DATETIME_MONTH:
        return TILEDB_DATETIME_MONTH;
      case TILEDB_DATETIME_WEEK:
        return TILEDB_DATETIME_WEEK;
      case TILEDB_DATETIME_DAY:
        return TILEDB_DATETIME_DAY;
      case TILEDB_DATETIME_HR:
        return TILEDB_DATETIME_HR;
      case TILEDB_DATETIME_MIN:
        return TILEDB_DATETIME_MIN;
      case TILEDB_DATETIME_SEC:
        return TILEDB_DATETIME_SEC;
      case TILEDB_DATETIME_MS:
        return TILEDB_DATETIME_MS;
      case TILEDB_DATETIME_US:
        return TILEDB_DATETIME_US;
      case TILEDB_DATETIME_NS:
        return TILEDB_DATETIME_NS;
      case TILEDB_DATETIME_PS:
        return TILEDB_DATETIME_PS;
      case TILEDB_DATETIME_FS:
        return TILEDB_DATETIME_FS;
      case TILEDB_DATETIME_AS:
        return TILEDB_DATETIME_AS;
      case TILEDB_TIME_HR:
        return TILEDB_TIME_HR;
      case TILEDB_TIME_MIN:
        return TILEDB_TIME_MIN;
      case TILEDB_TIME_SEC:
        return TILEDB_TIME_SEC;
      case TILEDB_TIME_MS:
        return TILEDB_TIME_MS;
      case TILEDB_TIME_US:
        return TILEDB_TIME_US;
      case TILEDB_TIME_NS:
        return TILEDB_TIME_NS;
      case TILEDB_TIME_PS:
        return TILEDB_TIME_PS;
      case TILEDB_TIME_FS:
        return TILEDB_TIME_FS;
      case TILEDB_TIME_AS:
        return TILEDB_TIME_AS;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
