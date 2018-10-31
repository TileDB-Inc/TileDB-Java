package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_filter_type_t;

public class DoubleDeltaFilter extends CompressionFilter {

   public DoubleDeltaFilter(Context ctx) throws TileDBError {
      super(ctx, tiledb_filter_type_t.TILEDB_FILTER_DOUBLE_DELTA);
   }

   public DoubleDeltaFilter(Context ctx, int level) throws TileDBError {
      super(ctx, tiledb_filter_type_t.TILEDB_FILTER_DOUBLE_DELTA, level);
   }
}
