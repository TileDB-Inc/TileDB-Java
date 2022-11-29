package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

public class WebPFilter extends Filter {

  /**
   * Constructor.
   *
   * @param ctx The TileDB context
   * @throws TileDBError
   */
  public WebPFilter(Context ctx) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_WEBP);
  }

  /**
   * Constructor.
   *
   * @param ctx The context param
   * @param quality The quality param
   * @param inputFormat The input format param as a tiledb_filter_webp_format_t enum value
   * @param lossless The lossless param as an 1(yes) or 0(no). if lossless is set quality is ignored
   * @throws TileDBError
   */
  public WebPFilter(
      Context ctx, float quality, tiledb_filter_webp_format_t inputFormat, short lossless)
      throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_WEBP);
    try (NativeArray qualityArray =
            new NativeArray(
                ctx,
                new float[] {
                  quality,
                },
                Float.class);
        NativeArray inputFormatArray =
            new NativeArray(
                ctx,
                new int[] {
                  inputFormat.swigValue(),
                },
                Integer.class);
        NativeArray losslessArray =
            new NativeArray(
                ctx,
                new short[] {
                  lossless,
                },
                Long.class); ) {
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_WEBP_INPUT_FORMAT,
              inputFormatArray.toVoidPointer()));
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_WEBP_LOSSLESS,
              losslessArray.toVoidPointer()));
      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_WEBP_QUALITY,
              qualityArray.toVoidPointer()));
    } catch (TileDBError err) {
      super.close();
      throw err;
    }
  }

  protected WebPFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }

  public float getQuality() throws TileDBError {
    Context ctx = getCtx();
    float quality;
    try (NativeArray qualityArray = new NativeArray(ctx, 1, Float.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_WEBP_QUALITY,
              qualityArray.toVoidPointer()));
      quality = (float) qualityArray.getItem(0);
    }
    return quality;
  }

  public short getlossless() throws TileDBError {
    Context ctx = getCtx();
    short lossless;
    try (NativeArray losslessArray = new NativeArray(ctx, 1, Short.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_WEBP_LOSSLESS,
              losslessArray.toVoidPointer()));
      lossless = (short) losslessArray.getItem(0);
    }
    return lossless;
  }

  public tiledb_filter_webp_format_t getInputFormat() throws TileDBError {
    Context ctx = getCtx();
    int format;
    try (NativeArray inputFormatArray = new NativeArray(ctx, 1, Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_WEBP_INPUT_FORMAT,
              inputFormatArray.toVoidPointer()));
      format = (int) inputFormatArray.getItem(0);
    }
    return tiledb_filter_webp_format_t.swigToEnum(format);
  }
}
