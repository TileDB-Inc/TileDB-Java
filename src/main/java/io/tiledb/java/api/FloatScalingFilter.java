package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_filter_option_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class FloatScalingFilter extends Filter {

  /**
   * Constructor.
   *
   * @param ctx The TileDB context
   * @throws TileDBError
   */
  public FloatScalingFilter(Context ctx) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_SCALE_FLOAT);
  }

  /**
   * Constructor.
   *
   * @param ctx The TileDB context
   * @param offset The offset param
   * @param factor The factor param
   * @param byteWidth The byteWidth param
   * @throws TileDBError
   */
  public FloatScalingFilter(Context ctx, int offset, int factor, int byteWidth) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_SCALE_FLOAT);
    try (NativeArray offsetArray =
            new NativeArray(
                ctx,
                new int[] {
                  offset,
                },
                Integer.class);
        NativeArray factorArray =
            new NativeArray(
                ctx,
                new int[] {
                  factor,
                },
                Integer.class);
        NativeArray byteWidthArray =
            new NativeArray(
                ctx,
                new int[] {
                  byteWidth,
                },
                Integer.class); ) {
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_OFFSET,
              offsetArray.toVoidPointer()));
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_FACTOR,
              factorArray.toVoidPointer()));
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_BYTEWIDTH,
              byteWidthArray.toVoidPointer()));
    } catch (TileDBError err) {
      super.close();
      throw err;
    }
  }

  /**
   * Sets the offset param
   *
   * @param ctx The TileDB context.
   * @param offset The offset input.
   * @throws TileDBError
   */
  public void setOffset(Context ctx, int offset) throws TileDBError {
    try (NativeArray offsetArray =
        new NativeArray(
            ctx,
            new int[] {
              offset,
            },
            Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_OFFSET,
              offsetArray.toVoidPointer()));
    } catch (TileDBError err) {
      super.close();
      throw err;
    }
  }

  /**
   * Sets the factor param.
   *
   * @param ctx The TileDB context
   * @param factor The factor input
   * @throws TileDBError
   */
  public void setFactor(Context ctx, int factor) throws TileDBError {
    try (NativeArray offsetArray =
        new NativeArray(
            ctx,
            new int[] {
              factor,
            },
            Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_FACTOR,
              offsetArray.toVoidPointer()));
    } catch (TileDBError err) {
      super.close();
      throw err;
    }
  }

  /**
   * Sets the ByteWidth param.
   *
   * @param ctx The TileDB context
   * @param byteWidth The byteWidth param
   * @throws TileDBError
   */
  public void setByteWidth(Context ctx, int byteWidth) throws TileDBError {
    try (NativeArray offsetArray =
        new NativeArray(
            ctx,
            new int[] {
              byteWidth,
            },
            Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_BYTEWIDTH,
              offsetArray.toVoidPointer()));
    } catch (TileDBError err) {
      super.close();
      throw err;
    }
  }

  /**
   * Constructor.
   *
   * @param ctx The TileDB Context
   * @param filterpp
   */
  protected FloatScalingFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }

  /**
   * @return The ByteWidth param
   * @throws TileDBError
   */
  public int getByteWidth() throws TileDBError {
    Context ctx = getCtx();
    int window;
    try (NativeArray byteWidthArray = new NativeArray(ctx, 1, Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_BYTEWIDTH,
              byteWidthArray.toVoidPointer()));
      window = (int) byteWidthArray.getItem(0);
    }
    return window;
  }

  /**
   * @return The factor param
   * @throws TileDBError
   */
  public int getFactor() throws TileDBError {
    Context ctx = getCtx();
    int window;
    try (NativeArray factorArray = new NativeArray(ctx, 1, Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_FACTOR,
              factorArray.toVoidPointer()));
      window = (int) factorArray.getItem(0);
    }
    return window;
  }

  /**
   * @return The offset param
   * @throws TileDBError
   */
  public int getOffset() throws TileDBError {
    Context ctx = getCtx();
    int window;
    try (NativeArray offsetArray = new NativeArray(ctx, 1, Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_SCALE_FLOAT_OFFSET,
              offsetArray.toVoidPointer()));
      window = (int) offsetArray.getItem(0);
    }
    return window;
  }
}
