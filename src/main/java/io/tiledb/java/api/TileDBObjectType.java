package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_object_t;

public enum TileDBObjectType {
  TILEDB_INVALID,
  TILEDB_GROUP,
  TILEDB_ARRAY;

  protected tiledb_object_t toSwigEnum() throws TileDBError {
    switch (this) {
      case TILEDB_INVALID:
        return tiledb_object_t.TILEDB_INVALID;
      case TILEDB_GROUP:
        return tiledb_object_t.TILEDB_GROUP;
      case TILEDB_ARRAY:
        return tiledb_object_t.TILEDB_ARRAY;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static TileDBObjectType fromSwigEnum(tiledb_object_t e) throws TileDBError {
    switch (e) {
      case TILEDB_INVALID:
        return TILEDB_INVALID;
      case TILEDB_GROUP:
        return TILEDB_GROUP;
      case TILEDB_ARRAY:
        return TILEDB_ARRAY;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
