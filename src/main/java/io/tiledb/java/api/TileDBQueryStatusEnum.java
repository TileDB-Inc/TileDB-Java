package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_query_status_t;

public enum TileDBQueryStatusEnum {
  TILEDB_FAILED,
  TILEDB_COMPLETED,
  TILEDB_INPROGRESS,
  TILEDB_INCOMPLETE,
  TILEDB_UNINITIALIZED;

  protected tiledb_query_status_t toSwigEnum() throws TileDBError {
    switch (this){
      case TILEDB_FAILED:
        return tiledb_query_status_t.TILEDB_FAILED;
      case TILEDB_COMPLETED:
        return tiledb_query_status_t.TILEDB_COMPLETED;
      case TILEDB_INPROGRESS:
        return tiledb_query_status_t.TILEDB_INPROGRESS;
      case TILEDB_INCOMPLETE:
        return tiledb_query_status_t.TILEDB_INCOMPLETE;
      case TILEDB_UNINITIALIZED:
        return tiledb_query_status_t.TILEDB_UNINITIALIZED;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static TileDBQueryStatusEnum fromSwigEnum(tiledb_query_status_t e) throws TileDBError{
    switch (e){
      case TILEDB_FAILED:
        return TILEDB_FAILED;
      case TILEDB_COMPLETED:
        return TILEDB_COMPLETED;
      case TILEDB_INPROGRESS:
        return TILEDB_INPROGRESS;
      case TILEDB_INCOMPLETE:
        return TILEDB_INCOMPLETE;
      case TILEDB_UNINITIALIZED:
        return TILEDB_UNINITIALIZED;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
