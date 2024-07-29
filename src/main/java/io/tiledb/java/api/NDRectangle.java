package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_ndrectangle_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_ndrectangle_t;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_range_t;

public class NDRectangle implements AutoCloseable {
  private SWIGTYPE_p_tiledb_ndrectangle_t ndrectanglep;
  private SWIGTYPE_p_p_tiledb_ndrectangle_t ndrectanglepp;
  private Context ctx;
  private Domain domain;

  public NDRectangle(Context ctx, Domain domain) throws TileDBError {
    this.ctx = ctx;
    this.domain = domain;
    ndrectanglepp = tiledb.new_tiledb_ndrectangle_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_ndrectangle_alloc(ctx.getCtxp(), domain.getDomainp(), ndrectanglepp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_ndrectangle_tpp(ndrectanglepp);
      throw err;
    }
    ndrectanglep = tiledb.tiledb_ndrectangle_tpp_value(ndrectanglepp);
  }

  public SWIGTYPE_p_tiledb_ndrectangle_t getndrectanglep() {
    return ndrectanglep;
  }

  public void set_range(long dim_idx, tiledb_range_t range) {}

  @Override
  public void close() throws Exception {
    if (ndrectanglep != null && ndrectanglepp != null) {
      tiledb.tiledb_ndrectangle_free(ndrectanglepp);
      tiledb.delete_tiledb_ndrectangle_tpp(ndrectanglepp);
      ndrectanglep = null;
      ndrectanglepp = null;
    }
  }
}
