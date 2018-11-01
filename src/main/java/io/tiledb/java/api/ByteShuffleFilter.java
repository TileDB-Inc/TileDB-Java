package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class ByteShuffleFilter extends Filter {

  public ByteShuffleFilter(Context ctx) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BYTESHUFFLE);
  }

  protected ByteShuffleFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }
}
