package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_filter_option_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class CompressionFilter extends Filter {

  protected CompressionFilter(Context ctx, tiledb_filter_type_t filter_type, int level)
      throws TileDBError {
    super(ctx, filter_type);
    try (NativeArray levelArray =
        new NativeArray(
            ctx,
            new int[] {
              level,
            },
            Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_COMPRESSION_LEVEL,
              levelArray.toVoidPointer()));
    } catch (TileDBError err) {
      super.close();
      throw err;
    }
  }

  protected CompressionFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }

  protected CompressionFilter(Context ctx, tiledb_filter_type_t filter_type) throws TileDBError {
    this(ctx, filter_type, -1);
  }

  public int getLevel() throws TileDBError {
    Context ctx = getCtx();
    int level;
    try (NativeArray levelArray = new NativeArray(ctx, 1, Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_COMPRESSION_LEVEL,
              levelArray.toVoidPointer()));
      level = (int) levelArray.getItem(0);
    }
    return level;
  }
}
