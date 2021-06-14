package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

public class Condition implements AutoCloseable {

  private Context ctx;
  private SWIGTYPE_p_tiledb_query_condition_t conditionp;
  private SWIGTYPE_p_p_tiledb_query_condition_t conditionpp;

  protected Condition(Context ctx, SWIGTYPE_p_p_tiledb_query_condition_t conditionpp) {
    this.ctx = ctx;
    //        this.conditionp = tiledb.tiledb_filter_tpp_value(conditionpp);
    this.conditionp = tiledb.tiledb_query_condition_tpp_value(conditionpp);
    this.conditionpp = conditionpp;
  }

  protected Condition(Context ctx) throws TileDBError {
    SWIGTYPE_p_p_tiledb_query_condition_t conditionpp = tiledb.new_tiledb_query_condition_tpp();
    try {
      ctx.handleError(tiledb.tiledb_query_condition_alloc(ctx.getCtxp(), conditionpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_query_condition_tpp(conditionpp);
      throw err;
    }
    this.ctx = ctx;
    this.conditionp = tiledb.tiledb_query_condition_tpp_value(conditionpp);
    this.conditionpp = conditionpp;
  }

  protected SWIGTYPE_p_tiledb_query_condition_t getConditionp() {
    return this.conditionp;
  }

  protected Context getCtx() {
    return this.ctx;
  }

  public void close() {
    if (conditionp != null && conditionpp != null) {
      tiledb.tiledb_query_condition_free(conditionpp);
      conditionpp = null;
      conditionp = null;
    }
  }
}
