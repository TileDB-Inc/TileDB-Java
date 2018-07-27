package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_vfs_mode_t;

public enum TileDBVFSModeEnum {
  TILEDB_VFS_READ,
  TILEDB_VFS_WRITE,
  TILEDB_VFS_APPEND;

  protected tiledb_vfs_mode_t toSwigEnum() throws TileDBError {
    switch (this){
      case TILEDB_VFS_READ:
        return tiledb_vfs_mode_t.TILEDB_VFS_READ;
      case TILEDB_VFS_WRITE:
        return tiledb_vfs_mode_t.TILEDB_VFS_WRITE;
      case TILEDB_VFS_APPEND:
        return tiledb_vfs_mode_t.TILEDB_VFS_APPEND;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static TileDBVFSModeEnum fromSwigEnum(tiledb_vfs_mode_t e) throws TileDBError{
    switch (e){
      case TILEDB_VFS_READ:
        return TILEDB_VFS_READ;
      case TILEDB_VFS_WRITE:
        return TILEDB_VFS_WRITE;
      case TILEDB_VFS_APPEND:
        return TILEDB_VFS_APPEND;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
