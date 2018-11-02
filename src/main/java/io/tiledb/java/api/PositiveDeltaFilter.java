package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_filter_option_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class PositiveDeltaFilter extends Filter {

  public PositiveDeltaFilter(Context ctx) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_POSITIVE_DELTA);
  }

  public PositiveDeltaFilter(Context ctx, int window) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_POSITIVE_DELTA);
    try (NativeArray windowArray =
        new NativeArray(
            ctx,
            new int[] {
              window,
            },
            Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_POSITIVE_DELTA_MAX_WINDOW,
              windowArray.toVoidPointer()));
    } catch (TileDBError err) {
      super.close();
      throw err;
    }
  }

  protected PositiveDeltaFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }

  public int getWindow() throws TileDBError {
    Context ctx = getCtx();
    int window;
    try (NativeArray windowArray = new NativeArray(ctx, 1, Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_POSITIVE_DELTA_MAX_WINDOW,
              windowArray.toVoidPointer()));
      window = (int) windowArray.getItem(0);
    }
    return window;
  }
};
