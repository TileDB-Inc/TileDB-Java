package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_datatype_t;
import io.tiledb.libtiledb.tiledb_filter_option_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class DeltaFilter extends CompressionFilter {

  public DeltaFilter(Context ctx, int level, tiledb_datatype_t type) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_DELTA, level);

    try (NativeArray reint = new NativeArray(ctx, new int[] {type.swigValue()}, Integer.class); ) {

      ctx.handleError(
          tiledb.tiledb_filter_set_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_COMPRESSION_REINTERPRET_DATATYPE,
              reint.toVoidPointer()));

    } catch (TileDBError e) {
      throw e;
    }
  }

  public tiledb_datatype_t getCompressionReinterpretDatatype() throws TileDBError {
    Context ctx = getCtx();
    int datatype;
    try (NativeArray datatypetArray = new NativeArray(ctx, 1, Integer.class)) {
      ctx.handleError(
          tiledb.tiledb_filter_get_option(
              ctx.getCtxp(),
              getFilterp(),
              tiledb_filter_option_t.TILEDB_COMPRESSION_REINTERPRET_DATATYPE,
              datatypetArray.toVoidPointer()));
      datatype = (int) datatypetArray.getItem(0);
    }
    return tiledb_datatype_t.swigToEnum(datatype);
  }

  protected DeltaFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }
}
