package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

public class NDRectangle implements AutoCloseable {
  private SWIGTYPE_p_tiledb_ndrectangle_t ndrectanglep;
  private SWIGTYPE_p_p_tiledb_ndrectangle_t ndrectanglepp;
  private Context ctx;
  private Domain domain;

  /**
   * @param ctx
   * @param domain
   * @throws TileDBError
   */
  public NDRectangle(Context ctx, Domain domain) throws TileDBError {
    this.ctx = ctx;
    this.domain = domain;
    this.ndrectanglepp = tiledb.new_tiledb_ndrectangle_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_ndrectangle_alloc(ctx.getCtxp(), domain.getDomainp(), ndrectanglepp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_ndrectangle_tpp(ndrectanglepp);
      throw err;
    }
    ndrectanglep = tiledb.tiledb_ndrectangle_tpp_value(ndrectanglepp);
  }

  /**
   * @param ctx
   * @param domain
   * @param ndpp
   */
  protected NDRectangle(Context ctx, Domain domain, SWIGTYPE_p_p_tiledb_ndrectangle_t ndpp) {
    this.ctx = ctx;
    this.ndrectanglepp = ndpp;
    this.ndrectanglep = tiledb.tiledb_ndrectangle_tpp_value(ndrectanglepp);
    this.domain = domain;
  }

  public SWIGTYPE_p_tiledb_ndrectangle_t getndrectanglep() {
    return ndrectanglep;
  }

  /**
   * @param dimIdx
   * @param range
   * @throws TileDBError
   */
  public void setRange(long dimIdx, Range range) throws TileDBError { // todo + name
    ctx.handleError(
        tiledb.tiledb_ndrectangle_set_range(
            ctx.getCtxp(), this.ndrectanglep, dimIdx, range.getRange_t()));
  }

  /**
   * @param dimIdx
   * @return
   * @throws TileDBError
   */
  public Range getRange(long dimIdx) throws TileDBError { // todo + name
    tiledb_range_t range = new tiledb_range_t();

    ctx.handleError(
        tiledb.tiledb_ndrectangle_get_range(ctx.getCtxp(), this.ndrectanglep, dimIdx, range));

    return new Range(this.ctx, range, this.domain, dimIdx);
  }

  @Override
  public void close() {
    if (ndrectanglep != null && ndrectanglepp != null) {
      tiledb.tiledb_ndrectangle_free(ndrectanglepp);
      tiledb.delete_tiledb_ndrectangle_tpp(ndrectanglepp);
      ndrectanglep = null;
      ndrectanglepp = null;
    }
  }
}
