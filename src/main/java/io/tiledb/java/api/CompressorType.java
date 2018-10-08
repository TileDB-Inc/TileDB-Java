package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_compressor_t;

public enum CompressorType {
  TILEDB_NO_COMPRESSION,
  TILEDB_GZIP,
  TILEDB_ZSTD,
  TILEDB_LZ4,
  TILEDB_BLOSC_LZ,
  TILEDB_BLOSC_LZ4,
  TILEDB_BLOSC_LZ4HC,
  TILEDB_BLOSC_SNAPPY,
  TILEDB_BLOSC_ZLIB,
  TILEDB_BLOSC_ZSTD,
  TILEDB_RLE,
  TILEDB_BZIP2,
  TILEDB_DOUBLE_DELTA;

  protected tiledb_compressor_t toSwigEnum() throws TileDBError {
    switch (this) {
      case TILEDB_NO_COMPRESSION:
        return tiledb_compressor_t.TILEDB_NO_COMPRESSION;
      case TILEDB_GZIP:
        return tiledb_compressor_t.TILEDB_GZIP;
      case TILEDB_ZSTD:
        return tiledb_compressor_t.TILEDB_ZSTD;
      case TILEDB_LZ4:
        return tiledb_compressor_t.TILEDB_LZ4;
      case TILEDB_BLOSC_LZ:
        return tiledb_compressor_t.TILEDB_BLOSC_LZ;
      case TILEDB_BLOSC_LZ4:
        return tiledb_compressor_t.TILEDB_BLOSC_LZ4;
      case TILEDB_BLOSC_LZ4HC:
        return tiledb_compressor_t.TILEDB_BLOSC_LZ4HC;
      case TILEDB_BLOSC_SNAPPY:
        return tiledb_compressor_t.TILEDB_BLOSC_SNAPPY;
      case TILEDB_BLOSC_ZLIB:
        return tiledb_compressor_t.TILEDB_BLOSC_ZLIB;
      case TILEDB_BLOSC_ZSTD:
        return tiledb_compressor_t.TILEDB_BLOSC_ZSTD;
      case TILEDB_RLE:
        return tiledb_compressor_t.TILEDB_RLE;
      case TILEDB_BZIP2:
        return tiledb_compressor_t.TILEDB_BZIP2;
      case TILEDB_DOUBLE_DELTA:
        return tiledb_compressor_t.TILEDB_DOUBLE_DELTA;
      default:
        throw new TileDBError("No such enum value" + this.name());
    }
  }

  protected static CompressorType fromSwigEnum(tiledb_compressor_t e) throws TileDBError {
    switch (e) {
      case TILEDB_NO_COMPRESSION:
        return TILEDB_NO_COMPRESSION;
      case TILEDB_GZIP:
        return TILEDB_GZIP;
      case TILEDB_ZSTD:
        return TILEDB_ZSTD;
      case TILEDB_LZ4:
        return TILEDB_LZ4;
      case TILEDB_BLOSC_LZ:
        return TILEDB_BLOSC_LZ;
      case TILEDB_BLOSC_LZ4:
        return TILEDB_BLOSC_LZ4;
      case TILEDB_BLOSC_LZ4HC:
        return TILEDB_BLOSC_LZ4HC;
      case TILEDB_BLOSC_SNAPPY:
        return TILEDB_BLOSC_SNAPPY;
      case TILEDB_BLOSC_ZLIB:
        return TILEDB_BLOSC_ZLIB;
      case TILEDB_BLOSC_ZSTD:
        return TILEDB_BLOSC_ZSTD;
      case TILEDB_RLE:
        return TILEDB_RLE;
      case TILEDB_BZIP2:
        return TILEDB_BZIP2;
      case TILEDB_DOUBLE_DELTA:
        return TILEDB_DOUBLE_DELTA;
      default:
        throw new TileDBError("No such enum value" + e.name());
    }
  }
}
