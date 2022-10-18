package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_query_type_t;

public enum QueryType {
  TILEDB_READ,
  TILEDB_WRITE,
  TILEDB_DELETE,
  TILEDB_UPDATE,
  TILEDB_MODIFY_EXCLUSIVE;

  protected tiledb_query_type_t toSwigEnum() throws TileDBError {
    switch (this) {
      case TILEDB_READ:
        return tiledb_query_type_t.TILEDB_READ;
      case TILEDB_WRITE:
        return tiledb_query_type_t.TILEDB_WRITE;
      case TILEDB_DELETE:
        return tiledb_query_type_t.TILEDB_DELETE;
      case TILEDB_UPDATE:
        return tiledb_query_type_t.TILEDB_UPDATE;
      case TILEDB_MODIFY_EXCLUSIVE:
        return tiledb_query_type_t.TILEDB_MODIFY_EXCLUSIVE;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static QueryType fromSwigEnum(tiledb_query_type_t e) throws TileDBError {
    switch (e) {
      case TILEDB_READ:
        return TILEDB_READ;
      case TILEDB_WRITE:
        return TILEDB_WRITE;
      case TILEDB_DELETE:
        return TILEDB_DELETE;
      case TILEDB_UPDATE:
        return TILEDB_UPDATE;
      case TILEDB_MODIFY_EXCLUSIVE:
        return TILEDB_MODIFY_EXCLUSIVE;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
