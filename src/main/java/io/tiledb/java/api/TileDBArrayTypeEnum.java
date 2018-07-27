package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_array_type_t;

public enum TileDBArrayTypeEnum {
  TILEDB_DENSE,
  TILEDB_SPARSE;

  protected tiledb_array_type_t toSwigEnum() throws TileDBError{
    switch (this){
      case TILEDB_DENSE:
        return tiledb_array_type_t.TILEDB_DENSE;
      case TILEDB_SPARSE:
        return tiledb_array_type_t.TILEDB_SPARSE;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static TileDBArrayTypeEnum fromSwigEnum(tiledb_array_type_t e) throws TileDBError{
    switch (e){
      case TILEDB_DENSE:
        return TILEDB_DENSE;
      case TILEDB_SPARSE:
        return TILEDB_SPARSE;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
