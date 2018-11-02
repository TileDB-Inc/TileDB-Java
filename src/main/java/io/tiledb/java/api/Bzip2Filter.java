package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class Bzip2Filter extends CompressionFilter {

  public Bzip2Filter(Context ctx) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BZIP2);
  }

  public Bzip2Filter(Context ctx, int level) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BZIP2, level);
  }

  protected Bzip2Filter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }
}
