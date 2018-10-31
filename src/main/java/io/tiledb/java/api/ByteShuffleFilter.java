package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_filter_type_t;

public class ByteShuffleFilter extends Filter {

    public ByteShuffleFilter(Context ctx) throws TileDBError {
        super(ctx, tiledb_filter_type_t.TILEDB_FILTER_BYTESHUFFLE);
    }
}
