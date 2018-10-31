package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

public class Filter implements AutoCloseable {

    private Context ctx;
    private SWIGTYPE_p_tiledb_filter_t filterp;
    private SWIGTYPE_p_p_tiledb_filter_t filterpp;

    protected Filter(Context ctx, tiledb_filter_type_t filter_type) throws TileDBError {
        SWIGTYPE_p_p_tiledb_filter_t filterpp = tiledb.new_tiledb_filter_tpp();
        try {
            ctx.handleError(tiledb.tiledb_filter_alloc(ctx.getCtxp(), filter_type, filterpp));
        } catch (TileDBError err) {
            tiledb.delete_tiledb_filter_tpp(filterpp);
            throw err;
        }
        this.ctx = ctx;
        this.filterp = tiledb.tiledb_filter_tpp_value(filterpp);
        this.filterpp = filterpp;
    }

    protected SWIGTYPE_p_tiledb_filter_t getFilterp()  {
        return this.filterp;
    }

    protected Context getCtx() {
        return this.ctx;
    }

    public void close() {
        if (filterp != null && filterpp != null) {
            tiledb.tiledb_filter_free(filterpp);
            filterpp = null;
            filterp = null;
        }
    }
}
