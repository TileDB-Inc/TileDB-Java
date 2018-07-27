package io.tiledb.java.api;

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
  TILEDB_UINT64;


  protected tiledb_datatype_t toSwigEnum() throws TileDBError {
    switch (this){
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
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static Datatype fromSwigEnum(tiledb_datatype_t e) throws TileDBError{
    switch (e){
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
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
