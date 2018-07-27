package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_layout_t;

public enum TileDBLayoutEnum {
  TILEDB_ROW_MAJOR,
  TILEDB_COL_MAJOR,
  TILEDB_GLOBAL_ORDER,
  TILEDB_UNORDERED;

  protected tiledb_layout_t toSwigEnum() throws TileDBError {
    switch (this){
      case TILEDB_ROW_MAJOR:
        return tiledb_layout_t.TILEDB_ROW_MAJOR;
      case TILEDB_COL_MAJOR:
        return tiledb_layout_t.TILEDB_COL_MAJOR;
      case TILEDB_GLOBAL_ORDER:
        return tiledb_layout_t.TILEDB_GLOBAL_ORDER;
      case TILEDB_UNORDERED:
        return tiledb_layout_t.TILEDB_UNORDERED;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static TileDBLayoutEnum fromSwigEnum(tiledb_layout_t e) throws TileDBError{
    switch (e){
      case TILEDB_ROW_MAJOR:
        return TILEDB_ROW_MAJOR;
      case TILEDB_COL_MAJOR:
        return TILEDB_COL_MAJOR;
      case TILEDB_GLOBAL_ORDER:
        return TILEDB_GLOBAL_ORDER;
      case TILEDB_UNORDERED:
        return TILEDB_UNORDERED;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
