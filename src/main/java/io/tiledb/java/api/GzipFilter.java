package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_filter_type_t;

public class GzipFilter extends CompressionFilter {

    public GzipFilter(Context ctx) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_GZIP);
    }

    public GzipFilter(Context ctx, int level) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_GZIP, level);
    }
}
