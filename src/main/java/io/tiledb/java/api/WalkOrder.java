package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_walk_order_t;

public enum WalkOrder {
  TILEDB_PREORDER,
  TILEDB_POSTORDER;

  protected tiledb_walk_order_t toSwigEnum() throws TileDBError {
    switch (this) {
      case TILEDB_PREORDER:
        return tiledb_walk_order_t.TILEDB_PREORDER;
      case TILEDB_POSTORDER:
        return tiledb_walk_order_t.TILEDB_POSTORDER;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static WalkOrder fromSwigEnum(tiledb_walk_order_t e) throws TileDBError {
    switch (e) {
      case TILEDB_PREORDER:
        return TILEDB_PREORDER;
      case TILEDB_POSTORDER:
        return TILEDB_POSTORDER;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
