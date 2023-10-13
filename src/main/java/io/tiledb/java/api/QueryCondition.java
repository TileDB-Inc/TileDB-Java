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

  @Deprecated
  /** Use new constructor instead. */
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

      array.close();
    } catch (TileDBError err) {
      tiledb.delete_tiledb_query_condition_tpp(conditionpp);
      throw err;
    }
    this.ctx = ctx;
  }

  /**
   * Disable the use of enumerations on the given QueryCondition
   *
   * @param flag
   */
  public void setUseEnumeration(boolean flag) throws TileDBError {
    int useEnum = 0;
    if (flag) useEnum = 1;
    ctx.handleError(
        tiledb.tiledb_query_condition_set_use_enumeration(
            this.ctx.getCtxp(), this.conditionp, useEnum));
  }

  /**
   * Initializes a TileDB query condition set membership object.
   *
   * @param name The field name.
   * @param data A pointer to the set member data.
   * @param dataSize The length of the data buffer.
   * @param offsets A pointer to the array of offsets of members.
   * @param offsetsSize The length of the offsets array in bytes.
   * @param OP The set membership operator to use.
   * @throws TileDBError
   */
  public void allocSetMembership(
      String name,
      NativeArray data,
      BigInteger dataSize,
      NativeArray offsets,
      BigInteger offsetsSize,
      tiledb_query_condition_op_t OP)
      throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_query_condition_alloc_set_membership(
            this.ctx.getCtxp(),
            name,
            data.toVoidPointer(),
            dataSize,
            offsets.toVoidPointer(),
            offsetsSize,
            OP,
            this.getConditionpp()));
  }

  /**
   * Constructor
   *
   * @param ctx The context
   * @param attributeName The name of the field this operation applies to
   * @param type The datatype
   * @param value The value to compare to. Can also be null.
   * @param OP The relational operation between the value of the field and `condition_value`
   * @throws TileDBError
   */
  public QueryCondition(
      Context ctx,
      Datatype type,
      String attributeName,
      Object value,
      tiledb_query_condition_op_t OP)
      throws TileDBError {
    try {
      this.ctx = ctx;

      if (type == null) {
        throw new TileDBError("Datatype can not be null");
      }

      conditionpp = tiledb.new_tiledb_query_condition_tpp();
      ctx.handleError(tiledb.tiledb_query_condition_alloc(ctx.getCtxp(), conditionpp));
      conditionp = tiledb.tiledb_query_condition_tpp_value(conditionpp);

      if (value == null) {
        ctx.handleError(
            tiledb.tiledb_query_condition_init(
                ctx.getCtxp(), conditionp, attributeName, null, BigInteger.valueOf(0), OP));
        return;
      }

      NativeArray array;
      if (value.getClass().isArray()) {
        array = new NativeArray(ctx, value, type);
      } else {
        int byteSize;
        if (value instanceof String) byteSize = ((String) value).length();
        else byteSize = type.getNativeSize();
        array = new NativeArray(ctx, byteSize, type);
        array.setItem(0, value);
      }

      ctx.handleError(
          tiledb.tiledb_query_condition_init(
              ctx.getCtxp(),
              conditionp,
              attributeName,
              array.toVoidPointer(),
              BigInteger.valueOf(array.getSize()),
              OP));

      array.close();

    } catch (TileDBError err) {
      tiledb.delete_tiledb_query_condition_tpp(conditionpp);
      throw err;
    }
  }

  public SWIGTYPE_p_tiledb_query_condition_t getConditionp() {
    return this.conditionp;
  }

  public SWIGTYPE_p_p_tiledb_query_condition_t getConditionpp() {
    return conditionpp;
  }

  protected Context getCtx() {
    return this.ctx;
  }

  public void close() {
    if (conditionp != null && conditionpp != null) {
      tiledb.tiledb_query_condition_free(conditionpp);
      tiledb.delete_tiledb_query_condition_tpp(conditionpp);
      conditionpp = null;
      conditionp = null;
    }
  }

  /**
   * Combines this instance with another instance to form a multi-clause condition object.
   *
   * @param rhs The right-hand-side query condition object.
   * @param OP The logical combination operator that combines this instance with `rhs`
   * @return The result Query Condition
   * @throws TileDBError
   */
  public QueryCondition combine(QueryCondition rhs, tiledb_query_condition_combination_op_t OP)
      throws TileDBError {
    SWIGTYPE_p_p_tiledb_query_condition_t combinedCondition =
        tiledb.new_tiledb_query_condition_tpp();

    try {
      ctx.handleError(tiledb.tiledb_query_condition_alloc(ctx.getCtxp(), conditionpp));
      ctx.handleError(
          tiledb.tiledb_query_condition_combine(
              ctx.getCtxp(), conditionp, rhs.getConditionp(), OP, combinedCondition));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_query_condition_tpp(conditionpp);
      throw err;
    }
    return new QueryCondition(ctx, combinedCondition);
  }

  /**
   * Create a query condition representing a negation of the input query condition. Currently, this
   * is performed by applying De Morgan's theorem recursively to the query condition's internal
   * representation.
   *
   * @return The negated Query Condition
   * @throws TileDBError
   */
  public QueryCondition negate() throws TileDBError {
    SWIGTYPE_p_p_tiledb_query_condition_t negatedCondition =
        tiledb.new_tiledb_query_condition_tpp();
    try {
      ctx.handleError(tiledb.tiledb_query_condition_alloc(ctx.getCtxp(), conditionpp));
      ctx.handleError(
          tiledb.tiledb_query_condition_negate(ctx.getCtxp(), conditionp, negatedCondition));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_query_condition_tpp(conditionpp);
      throw err;
    }
    return new QueryCondition(ctx, negatedCondition);
  }
}
