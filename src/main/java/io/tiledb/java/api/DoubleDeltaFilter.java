package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class DoubleDeltaFilter extends CompressionFilter {

  public DoubleDeltaFilter(Context ctx) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_DOUBLE_DELTA);
  }

  public DoubleDeltaFilter(Context ctx, int level) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_DOUBLE_DELTA, level);
  }

  protected DoubleDeltaFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }
}
