package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_channel_operator_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_channel_operator_t;
import io.tiledb.libtiledb.tiledb;

public class ChannelOperator implements AutoCloseable {
  public enum AggregationOperator {
    TILEDB_MIN,
    TILEDB_SUM,
    TILEDB_MEAN,
    TILEDB_MAX,
    TILEDB_NULL_COUNT,
    TILEDB_COUNT
  }

  private SWIGTYPE_p_tiledb_channel_operator_t operatorp;
  private SWIGTYPE_p_p_tiledb_channel_operator_t operatorpp;
  private AggregationOperator aggregationOperator;
  private boolean isCount = false;

  /**
   * Constructor
   *
   * @param ctx The context
   * @param op The aggregation operator
   */
  public ChannelOperator(Context ctx, AggregationOperator op) throws TileDBError {
    this.aggregationOperator = op;
    operatorpp = tiledb.new_tiledb_channel_operator_tpp();

    try {
      switch (op) {
        case TILEDB_MIN:
          ctx.handleError(tiledb.tiledb_channel_operator_min_get(ctx.getCtxp(), operatorpp));
          break;
        case TILEDB_MAX:
          ctx.handleError(tiledb.tiledb_channel_operator_max_get(ctx.getCtxp(), operatorpp));
          break;
        case TILEDB_SUM:
          ctx.handleError(tiledb.tiledb_channel_operator_sum_get(ctx.getCtxp(), operatorpp));
          break;
        case TILEDB_MEAN:
          ctx.handleError(tiledb.tiledb_channel_operator_mean_get(ctx.getCtxp(), operatorpp));
          break;
        case TILEDB_NULL_COUNT:
          ctx.handleError(tiledb.tiledb_channel_operator_null_count_get(ctx.getCtxp(), operatorpp));
          break;
        case TILEDB_COUNT:
          isCount = true;
          break;
      }
    } catch (TileDBError error) {
      tiledb.delete_tiledb_channel_operator_tpp(operatorpp);
      throw error;
    }
    operatorp = tiledb.tiledb_channel_operator_tpp_value(operatorpp);
  }

  public boolean isCount() {
    return isCount;
  }

  public SWIGTYPE_p_tiledb_channel_operator_t getOperatorp() {
    return operatorp;
  }

  @Override
  public void close() throws Exception {
    if (operatorp != null) {
      tiledb.delete_tiledb_channel_operator_tpp(operatorpp);
      operatorp = null;
      operatorpp = null;
    }
  }
}
