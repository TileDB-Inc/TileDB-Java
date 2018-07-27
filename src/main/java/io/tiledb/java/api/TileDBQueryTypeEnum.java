package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_query_type_t;

public enum TileDBQueryTypeEnum {
  TILEDB_READ,
  TILEDB_WRITE;

  protected tiledb_query_type_t toSwigEnum() throws TileDBError {
    switch (this){
      case TILEDB_READ:
        return tiledb_query_type_t.TILEDB_READ;
      case TILEDB_WRITE:
        return tiledb_query_type_t.TILEDB_WRITE;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static TileDBQueryTypeEnum fromSwigEnum(tiledb_query_type_t e) throws TileDBError{
    switch (e){
      case TILEDB_READ:
        return TILEDB_READ;
      case TILEDB_WRITE:
        return TILEDB_WRITE;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
