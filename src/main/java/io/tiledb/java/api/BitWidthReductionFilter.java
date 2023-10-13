package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_filter_option_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class BitWidthReductionFilter extends Filter {

  public BitWidthReductionFilter(Context ctx) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BIT_WIDTH_REDUCTION);
  }

  public BitWidthReductionFilter(Context ctx, int window) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BIT_WIDTH_REDUCTION);
    NativeArray windowArray =
        new NativeArray(
            ctx,
            new int[] {
              window,
            },
            Integer.class);

    ctx.handleError(
        tiledb.tiledb_filter_set_option(
            ctx.getCtxp(),
            getFilterp(),
            tiledb_filter_option_t.TILEDB_BIT_WIDTH_MAX_WINDOW,
            windowArray.toVoidPointer()));

    windowArray.close();
  }

  protected BitWidthReductionFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }

  public int getWindow() throws TileDBError {
    Context ctx = getCtx();
    int window;
    NativeArray windowArray = new NativeArray(ctx, 1, Integer.class);
    ctx.handleError(
        tiledb.tiledb_filter_get_option(
            ctx.getCtxp(),
            getFilterp(),
            tiledb_filter_option_t.TILEDB_BIT_WIDTH_MAX_WINDOW,
            windowArray.toVoidPointer()));
    window = (int) windowArray.getItem(0);
    windowArray.close();
    return window;
  }
}
