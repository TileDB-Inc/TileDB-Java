package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_filter_type_t;

public class BitShuffleFilter extends Filter {

    public BitShuffleFilter(Context ctx) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BITSHUFFLE);
    }
}
