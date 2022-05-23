/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 TileDB, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;

public class QueryCondition implements AutoCloseable {
  private Context ctx;
  private SWIGTYPE_p_tiledb_query_condition_t conditionp;
  private SWIGTYPE_p_p_tiledb_query_condition_t conditionpp;
  private Datatype type;

  public QueryCondition(Context ctx, SWIGTYPE_p_p_tiledb_query_condition_t conditionpp) {
    this.ctx = ctx;
    this.conditionp = tiledb.tiledb_query_condition_tpp_value(conditionpp);
    this.conditionpp = conditionpp;
  }

  public QueryCondition(
      Context ctx,
      String attributeName,
      Object value,
      Class conditionType,
      tiledb_query_condition_op_t OP)
      throws TileDBError {
    try {
      if (conditionType != null) {
        this.type = Types.getNativeType(conditionType);
      }
      conditionpp = tiledb.new_tiledb_query_condition_tpp();
      ctx.handleError(tiledb.tiledb_query_condition_alloc(ctx.getCtxp(), conditionpp));
      NativeArray array = null;
      if (value.getClass().isArray()) {
        array = new NativeArray(ctx, value, this.type.javaClass());
      } else {
        if (this.type != null) {
          int byteSize = this.type.getNativeSize();
          array = new NativeArray(ctx, byteSize, this.type.javaClass());
          array.setItem(0, value);
        }
      }
      conditionp = tiledb.tiledb_query_condition_tpp_value(conditionpp);
      if (this.type == null) {
        ctx.handleError(
            tiledb.tiledb_query_condition_init(
                ctx.getCtxp(), conditionp, attributeName, null, BigInteger.valueOf(0), OP));
      } else {
        ctx.handleError(
            tiledb.tiledb_query_condition_init(
                ctx.getCtxp(),
                conditionp,
                attributeName,
                array.toVoidPointer(),
                BigInteger.valueOf(array.getSize()),
                OP));
      }
    } catch (TileDBError err) {
      tiledb.delete_tiledb_query_condition_tpp(conditionpp);
      throw err;
    }
    this.ctx = ctx;
  }

  public SWIGTYPE_p_tiledb_query_condition_t getConditionp() {
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

  public QueryCondition combine(QueryCondition con2, tiledb_query_condition_combination_op_t OP)
      throws TileDBError {
    SWIGTYPE_p_p_tiledb_query_condition_t combinedCondition;
    try {
      combinedCondition = tiledb.new_tiledb_query_condition_tpp();
      ctx.handleError(tiledb.tiledb_query_condition_alloc(ctx.getCtxp(), conditionpp));
      ctx.handleError(
          tiledb.tiledb_query_condition_combine(
              ctx.getCtxp(), conditionp, con2.getConditionp(), OP, combinedCondition));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_query_condition_tpp(conditionpp);
      throw err;
    }
    return new QueryCondition(ctx, combinedCondition);
  }
}
