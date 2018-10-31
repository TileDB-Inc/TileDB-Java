package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_filter_type_t;

public class RleFilter extends CompressionFilter {

    public RleFilter(Context ctx) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_RLE);
    }

    public RleFilter(Context ctx, int level) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_RLE, level);
    }
}
