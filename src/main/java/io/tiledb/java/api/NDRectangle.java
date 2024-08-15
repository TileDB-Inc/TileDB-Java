package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

public class NDRectangle implements AutoCloseable {
  private SWIGTYPE_p_tiledb_ndrectangle_t ndrectanglep;
  private SWIGTYPE_p_p_tiledb_ndrectangle_t ndrectanglepp;
  private Context ctx;
  private Domain domain;

  /**
   * Constructor
   *
   * @param ctx The TileDB context
   * @param domain The array domain
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
   * Constructor
   *
   * @param ctx The TileDB context
   * @param domain The array domain
   * @param ndpp The c pointer to the ndrectangle object
   */
  protected NDRectangle(Context ctx, Domain domain, SWIGTYPE_p_p_tiledb_ndrectangle_t ndpp) {
    this.ctx = ctx;
    this.ndrectanglepp = ndpp;
    this.ndrectanglep = tiledb.tiledb_ndrectangle_tpp_value(ndrectanglepp);
    this.domain = domain;
  }

  /**
   * Getter for the c pointer
   *
   * @return The c pointer
   */
  public SWIGTYPE_p_tiledb_ndrectangle_t getndrectanglep() {
    return ndrectanglep;
  }

  /**
   * Adds an 1D range along a dimension name, in the form (start, end).
   *
   * @param dimIdx The index of the dimension to add the range to
   * @param range The range
   * @throws TileDBError
   */
  public void setRange(long dimIdx, Range range) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_ndrectangle_set_range(
            ctx.getCtxp(), this.ndrectanglep, dimIdx, range.getRange_t()));
  }

  /**
   * Retrieves a range for a given dimension index.
   *
   * @param dimIdx The dimension index
   * @return The range
   * @throws TileDBError
   */
  public Range getRange(long dimIdx) throws TileDBError {
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
