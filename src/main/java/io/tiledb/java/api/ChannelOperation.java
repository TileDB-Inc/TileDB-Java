package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_channel_operation_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_channel_operation_t;
import io.tiledb.libtiledb.tiledb;

public class ChannelOperation {
  private Context ctx;
  private SWIGTYPE_p_tiledb_channel_operation_t operationp;
  private SWIGTYPE_p_p_tiledb_channel_operation_t operationpp;

  public ChannelOperation(Context ctx, ChannelOperator operator, Query query, String fieldName)
      throws TileDBError {
    this.ctx = ctx;
    this.operationpp = tiledb.new_tiledb_channel_operation_tpp();
    ctx.handleError(
        tiledb.tiledb_create_unary_aggregate(
            ctx.getCtxp(),
            query.getQueryp(),
            operator.getOperatorp(),
            fieldName,
            this.operationpp));
    this.operationp = tiledb.tiledb_channel_operation_tpp_value(operationpp);
  }

  public SWIGTYPE_p_tiledb_channel_operation_t getOperationp() {
    return operationp;
  }
}
