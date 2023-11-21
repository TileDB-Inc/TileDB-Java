package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_query_channel_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_query_channel_t;
import io.tiledb.libtiledb.tiledb;

public class QueryChannel implements AutoCloseable {

  private SWIGTYPE_p_tiledb_query_channel_t queryChannelp;
  private SWIGTYPE_p_p_tiledb_query_channel_t queryChannelpp;
  private Context ctx;

  protected QueryChannel(Context ctx, SWIGTYPE_p_p_tiledb_query_channel_t queryChannelpp) {
    this.ctx = ctx;
    this.queryChannelpp = queryChannelpp;
    this.queryChannelp = tiledb.tiledb_query_channel_tpp_value(queryChannelpp);
  }

  /**
   * Apply an aggregate operation on this channel which will produce the results on the output field
   * passed as argument
   *
   * @param fieldName The field name
   * @param channelOperation the aggregate operation to be applied on the channel
   * @throws TileDBError
   */
  public void applyAggregate(String fieldName, ChannelOperation channelOperation)
      throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_channel_apply_aggregate(
            this.ctx.getCtxp(), this.queryChannelp, fieldName, channelOperation.getOperationp()));
  }

  @Override
  public void close() throws Exception {
    if (queryChannelp != null) {
      tiledb.tiledb_query_channel_free(ctx.getCtxp(), queryChannelpp);
      tiledb.delete_tiledb_query_channel_tpp(queryChannelpp);
      queryChannelpp = null;
      queryChannelp = null;
    }
  }
}
