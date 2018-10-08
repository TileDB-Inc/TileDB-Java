package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_filesystem_t;

public enum Filesystem {
  TILEDB_HDFS,
  TILEDB_S3;

  protected tiledb_filesystem_t toSwigEnum() throws TileDBError {
    switch (this) {
      case TILEDB_HDFS:
        return tiledb_filesystem_t.TILEDB_HDFS;
      case TILEDB_S3:
        return tiledb_filesystem_t.TILEDB_S3;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static Filesystem fromSwigEnum(tiledb_filesystem_t e) throws TileDBError {
    switch (e) {
      case TILEDB_HDFS:
        return TILEDB_HDFS;
      case TILEDB_S3:
        return TILEDB_S3;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
