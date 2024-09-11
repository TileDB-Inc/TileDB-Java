package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

public class CurrentDomain implements AutoCloseable {
  private Context ctx;
  private Domain domain;
  private SWIGTYPE_p_p_tiledb_current_domain_t currentDomainpp;
  private SWIGTYPE_p_tiledb_current_domain_t currentDomainp;

  /**
   * Constructor
   *
   * @param ctx The context
   * @param domain The array domain
   * @param currentDomainpp The current domain c pointer
   */
  protected CurrentDomain(
      Context ctx, Domain domain, SWIGTYPE_p_p_tiledb_current_domain_t currentDomainpp) {
    this.ctx = ctx;
    this.currentDomainpp = currentDomainpp;
    this.currentDomainp = tiledb.tiledb_current_domain_tpp_value(currentDomainpp);
    this.domain = domain;
  }

  /**
   * Constructor
   *
   * @param ctx The context
   * @param domain The array domain
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

  /**
   * Returns the c pointer for this current domain object
   *
   * @return
   */
  protected SWIGTYPE_p_tiledb_current_domain_t getCurrentDomainp() {
    return currentDomainp;
  }

  /**
   * Set a N-dimensional rectangle representation on a current domain
   *
   * @param ndRectangle The ndrectangle
   * @throws TileDBError
   */
  public void setNDRectangle(NDRectangle ndRectangle) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_current_domain_set_ndrectangle(
            ctx.getCtxp(), currentDomainp, ndRectangle.getndrectanglep()));
  }

  /**
   * Returns the currentDomain type.
   *
   * @return The type
   * @throws TileDBError
   */
  public CurrentDomainType getType() throws TileDBError {
    SWIGTYPE_p_tiledb_current_domain_type_t typep = tiledb.new_tiledb_current_domain_type_tp();
    CurrentDomainType currentDomainType;
    try {
      ctx.handleError(
          tiledb.tiledb_current_domain_get_type(ctx.getCtxp(), getCurrentDomainp(), typep));
      currentDomainType =
          CurrentDomainType.fromSwigEnum(tiledb.tiledb_current_domain_type_tp_value(typep));
    } finally {
      tiledb.delete_tiledb_current_domain_type_tp(typep);
    }
    return currentDomainType;
  }

  /**
   * Get the N-dimensional rectangle associated with the current domain object, error if the current
   * domain is empty or a different representation is set.
   *
   * @return The ndrectangle
   * @throws TileDBError
   */
  public NDRectangle getNDRectangle() throws TileDBError {
    NDRectangle nd;
    SWIGTYPE_p_p_tiledb_ndrectangle_t ndpp = tiledb.new_tiledb_ndrectangle_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_current_domain_get_ndrectangle(ctx.getCtxp(), currentDomainp, ndpp));
      nd = new NDRectangle(ctx, domain, ndpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_ndrectangle_tpp(ndpp);
      throw err;
    }
    return nd;
  }

  /**
   * Return true if the current domain is empty
   *
   * @return True if empty
   * @throws TileDBError
   */
  public boolean isEmpty() throws TileDBError {
    boolean isEmpty;
    SWIGTYPE_p_unsigned_int retp = tiledb.new_uintp();
    try {
      ctx.handleError(
          tiledb.tiledb_current_domain_get_is_empty(ctx.getCtxp(), currentDomainp, retp));
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
