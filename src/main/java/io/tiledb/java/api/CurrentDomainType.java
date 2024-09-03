package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_current_domain_type_t;

public enum CurrentDomainType {
  TILEDB_NDRECTANGLE;

  protected tiledb_current_domain_type_t toSwigEnum() throws TileDBError {
    switch (this) {
      case TILEDB_NDRECTANGLE:
        return tiledb_current_domain_type_t.TILEDB_NDRECTANGLE;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static CurrentDomainType fromSwigEnum(tiledb_current_domain_type_t e)
      throws TileDBError {
    switch (e) {
      case TILEDB_NDRECTANGLE:
        return TILEDB_NDRECTANGLE;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
