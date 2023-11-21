package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_channel_operator_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_channel_operator_t;
import io.tiledb.libtiledb.tiledb;

public class ChannelOperator {
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
  public ChannelOperator(Context ctx, AggregationOperator op) {
    this.aggregationOperator = op;
    operatorpp = tiledb.new_tiledb_channel_operator_tpp();
    switch (op) {
      case TILEDB_MIN:
        tiledb.tiledb_channel_operator_min_get(ctx.getCtxp(), operatorpp);
        break;
      case TILEDB_MAX:
        tiledb.tiledb_channel_operator_max_get(ctx.getCtxp(), operatorpp);
        break;
      case TILEDB_SUM:
        tiledb.tiledb_channel_operator_sum_get(ctx.getCtxp(), operatorpp);
        break;
      case TILEDB_MEAN:
        tiledb.tiledb_channel_operator_mean_get(ctx.getCtxp(), operatorpp);
        break;
      case TILEDB_NULL_COUNT:
        tiledb.tiledb_channel_operator_null_count_get(ctx.getCtxp(), operatorpp);
        break;
      case TILEDB_COUNT:
        isCount = true;
        break;
    }

    operatorp = tiledb.tiledb_channel_operator_tpp_value(operatorpp);
  }

  public boolean isCount() {
    return isCount;
  }

  public SWIGTYPE_p_tiledb_channel_operator_t getOperatorp() {
    return operatorp;
  }
}
