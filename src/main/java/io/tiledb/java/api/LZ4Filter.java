package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class LZ4Filter extends CompressionFilter {

    public LZ4Filter(Context ctx) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_LZ4);
    }

    public LZ4Filter(Context ctx, int level) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_LZ4, level);
    }

    protected LZ4Filter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
        super(ctx, filterpp);
    }
}
