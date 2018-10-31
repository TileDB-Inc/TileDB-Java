package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_filter_type_t;

public class Bzip2Filter extends CompressionFilter {

    public Bzip2Filter(Context ctx) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BZIP2);
    }

    public Bzip2Filter(Context ctx, int level) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BZIP2, level);
    }
}
