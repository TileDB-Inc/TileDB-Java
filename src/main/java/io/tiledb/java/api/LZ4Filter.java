package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_filter_type_t;

public class LZ4Filter extends CompressionFilter {

    public LZ4Filter(Context ctx) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_LZ4);
    }

    public LZ4Filter(Context ctx, int level) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_LZ4, level);
    }
}
