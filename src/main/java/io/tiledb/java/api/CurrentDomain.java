package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

public class CurrentDomain implements AutoCloseable {
  private Context ctx;
  private Domain domain;
  private SWIGTYPE_p_p_tiledb_current_domain_t currentDomainpp;
  private SWIGTYPE_p_tiledb_current_domain_t currentDomainp;

  /**
   * @param ctx
   * @param domain
   * @param currentDomainpp
   */
  protected CurrentDomain(
      Context ctx, Domain domain, SWIGTYPE_p_p_tiledb_current_domain_t currentDomainpp) {
    this.ctx = ctx;
    this.currentDomainpp = currentDomainpp;
    this.currentDomainp = tiledb.tiledb_current_domain_tpp_value(currentDomainpp);
    this.domain = domain;
  }

  /**
   * @param ctx
   * @param domain
   * @throws TileDBError
   */
  public CurrentDomain(Context ctx, Domain domain) throws TileDBError {
    SWIGTYPE_p_p_tiledb_current_domain_t _currentDomainpp = tiledb.new_tiledb_current_domain_tpp();
    try {
      ctx.handleError(tiledb.tiledb_current_domain_create(ctx.getCtxp(), _currentDomainpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_current_domain_tpp(_currentDomainpp);
      throw err;
    }
    this.ctx = ctx;
    this.currentDomainp = tiledb.tiledb_current_domain_tpp_value(_currentDomainpp);
    this.currentDomainpp = _currentDomainpp;
    this.domain = domain;
  }

  /** @return */
  protected SWIGTYPE_p_tiledb_current_domain_t getCurrentDomainp() {
    return currentDomainp;
  }

  /**
   * @param ndRectangle
   * @throws TileDBError
   */
  public void setNDRectangle(NDRectangle ndRectangle) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_current_domain_set_ndrectangle(
            currentDomainp, ndRectangle.getndrectanglep()));
  }

  /**
   * @return
   * @throws TileDBError
   */
  public CurrentDomainType getType() throws TileDBError {
    SWIGTYPE_p_tiledb_current_domain_type_t typep = tiledb.new_tiledb_current_domain_type_tp();
    CurrentDomainType currentDomainType;
    try {
      ctx.handleError(tiledb.tiledb_current_domain_get_type(getCurrentDomainp(), typep));
      currentDomainType =
          CurrentDomainType.fromSwigEnum(tiledb.tiledb_current_domain_type_tp_value(typep));
    } finally {
      tiledb.delete_tiledb_current_domain_type_tp(typep);
    }
    return currentDomainType;
  }

  /**
   * @return
   * @throws TileDBError
   */
  public NDRectangle getNDRectangle() throws TileDBError {
    NDRectangle nd;
    SWIGTYPE_p_p_tiledb_ndrectangle_t ndpp = tiledb.new_tiledb_ndrectangle_tpp();
    try {
      ctx.handleError(tiledb.tiledb_current_domain_get_ndrectangle(currentDomainp, ndpp));
      nd = new NDRectangle(ctx, domain, ndpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_ndrectangle_tpp(ndpp);
      throw err;
    }
    return nd;
  }

  /**
   * @return
   * @throws TileDBError
   */
  public boolean isEmpty() throws TileDBError {
    boolean isEmpty;
    SWIGTYPE_p_unsigned_int retp = tiledb.new_uintp();
    try {
      ctx.handleError(tiledb.tiledb_current_domain_get_is_empty(currentDomainp, retp));
      isEmpty = tiledb.uintp_value(retp) != 0;
    } finally {
      tiledb.delete_uintp(retp);
    }
    return isEmpty;
  }

  @Override
  public void close() {
    if (currentDomainp != null) {
      tiledb.tiledb_current_domain_free(currentDomainpp);
      tiledb.delete_tiledb_current_domain_tpp(currentDomainpp);
      currentDomainpp = null;
      currentDomainp = null;
    }
  }
}
