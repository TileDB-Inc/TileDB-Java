/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 TileDB, Inc.
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

import static io.tiledb.java.api.Datatype.TILEDB_UINT64;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Construct and execute read/write queries on a TileDB Array.
 *
 * <p><b>Example:</b>
 *
 * <pre>{@code
 * Query query = new Query(my_dense_array, TILEDB_WRITE);
 * query.setLayout(TILEDB_GLOBAL_ORDER);
 * query.setBuffer("a1", a1_data);
 * NativeArray a1_data = new NativeArray(ctx, new int[] {1,2,3,4}, Integer.class);
 * query.setBuffer("a1", a1_data);
 * query.submit();
 * }</pre>
 */
public class Query implements AutoCloseable {

  private Context ctx;
  private Array array;
  private QueryType type;

  private SWIGTYPE_p_p_tiledb_query_t querypp;
  private SWIGTYPE_p_tiledb_query_t queryp;

  private NativeArray subarray;

  private HashMap<String, NativeArray> buffers_;
  private HashMap<String, Pair<NativeArray, NativeArray>> var_buffers_;
  private HashMap<String, Pair<uint64_tArray, uint64_tArray>> buffer_sizes_;
  private boolean executed;

  public Query(Array array, QueryType type) throws TileDBError {
    Context _ctx = array.getCtx();
    SWIGTYPE_p_p_tiledb_query_t _querypp = tiledb.new_tiledb_query_tpp();
    try {
      _ctx.handleError(
          tiledb.tiledb_query_alloc(
              _ctx.getCtxp(), array.getArrayp(), type.toSwigEnum(), _querypp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_query_tpp(_querypp);
      throw err;
    }
    this.ctx = _ctx;
    this.type = type;
    this.array = array;
    this.querypp = _querypp;
    this.queryp = tiledb.tiledb_query_tpp_value(_querypp);
    this.buffers_ = new HashMap<>();
    this.var_buffers_ = new HashMap<>();
    this.buffer_sizes_ = new HashMap<>();
    executed = false;
  }

  public Query(Array array) throws TileDBError {
    this(array, array.getQueryType());
  }

  /**
   * Sets the data layout of the buffers.
   *
   * @param layout The layout order to be set.
   * @exception TileDBError A TileDB exception
   */
  public void setLayout(Layout layout) throws TileDBError {
    ctx.handleError(tiledb.tiledb_query_set_layout(ctx.getCtxp(), queryp, layout.toSwigEnum()));
  }

  /**
   * @return The query Status.
   * @exception TileDBError A TileDB exception
   */
  public QueryStatus getQueryStatus() throws TileDBError {
    QueryStatus status;
    SWIGTYPE_p_tiledb_query_status_t statusp = tiledb.new_tiledb_query_status_tp();
    try {
      ctx.handleError(tiledb.tiledb_query_get_status(ctx.getCtxp(), queryp, statusp));
      status = QueryStatus.fromSwigEnum(tiledb.tiledb_query_status_tp_value(statusp));
    } finally {
      tiledb.delete_tiledb_query_status_tp(statusp);
    }
    return status;
  }

  /**
   * Submits the query. Call will block until query is complete.
   *
   * @return The query Status.
   * @exception TileDBError A TileDB exception
   */
  public QueryStatus submit() throws TileDBError {
    prepareSubmission();
    ctx.handleError(tiledb.tiledb_query_submit(ctx.getCtxp(), queryp));
    executed = true;
    return getQueryStatus();
  }

  /**
   * Submit an async query (non-blocking).
   *
   * @exception TileDBError A TileDB exception
   */
  public void submitAsync() throws TileDBError {
    submitAsync(new DefaultCallback());
  }

  /**
   * Submit an async query, with callback.
   *
   * @param callback Callback function.
   * @exception TileDBError A TileDB exception
   */
  public void submitAsync(Callback callback) throws TileDBError {
    prepareSubmission();
    ctx.handleError(Utils.tiledb_query_submit_async(ctx.getCtxp(), queryp, callback));
    executed = true;
  }

  /**
   * Sets a subarray, defined in the order dimensions were added. Coordinates are inclusive.
   *
   * @param subarray The targeted subarray.
   * @exception TileDBError A TileDB exception
   */
  public void setSubarray(NativeArray subarray) throws TileDBError {
    Types.typeCheck(subarray.getNativeType(), array.getSchema().getDomain().getType());
    ctx.handleError(
        tiledb.tiledb_query_set_subarray(ctx.getCtxp(), queryp, subarray.toVoidPointer()));
    this.subarray = subarray;
  }

  /**
   * Sets a buffer for a fixed-sized attribute.
   *
   * @param attr The attribute name.
   * @param buffer NativeBuffer to be used for the attribute values.
   * @exception TileDBError A TileDB exception
   */
  public void setBuffer(String attr, NativeArray buffer) throws TileDBError {
    try (ArraySchema schema = array.getSchema()) {
      if (attr.equals(tiledb.tiledb_coords())) {
        try (Domain domain = schema.getDomain()) {
          Types.typeCheck(domain.getType(), buffer.getNativeType());
        }
      } else {
        try (Attribute attribute = schema.getAttribute(attr)) {
          Types.typeCheck(attribute.getType(), buffer.getNativeType());
        }
      }
    }
    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<uint64_tArray, uint64_tArray>(new uint64_tArray(1), new uint64_tArray(1));
    buffer_sizes.getFirst().setitem(0, BigInteger.valueOf(0l));
    buffer_sizes.getSecond().setitem(0, BigInteger.valueOf(buffer.getNBytes()));
    buffers_.put(attr, buffer);
    buffer_sizes_.put(attr, buffer_sizes);
  }

  /**
   * Sets a buffer for a variable-sized getAttribute.
   *
   * @param attr Attribute name
   * @param offsets Offsets where a new element begins in the data buffer.
   * @param buffer Buffer vector with elements of the attribute type.
   * @exception TileDBError A TileDB exception
   */
  public void setBuffer(String attr, NativeArray offsets, NativeArray buffer) throws TileDBError {
    if (attr.equals(tiledb.tiledb_coords())) {
      throw new TileDBError("Cannot set coordinate buffer as variable sized.");
    }
    if (!offsets.getNativeType().equals(TILEDB_UINT64))
      throw new TileDBError(
          "Buffer offsets should be of getType TILEDB_UINT64 or Long. Found getType: "
              + offsets.getNativeType());
    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<uint64_tArray, uint64_tArray>(new uint64_tArray(1), new uint64_tArray(1));
    buffer_sizes.getFirst().setitem(0, BigInteger.valueOf(offsets.getNBytes()));
    buffer_sizes.getSecond().setitem(0, BigInteger.valueOf(buffer.getNBytes()));

    var_buffers_.put(attr, new Pair<NativeArray, NativeArray>(offsets, buffer));
    buffer_sizes_.put(attr, buffer_sizes);
  }

  /**
   * Set the coordinate buffer
   *
   * @param buffer A NativeArray to be used for the coordinates.
   * @exception TileDBError A TileDB exception
   */
  public void setCoordinates(NativeArray buffer) throws TileDBError {
    setBuffer(tiledb.tiledb_coords(), buffer);
  }

  /**
   * Get the coordinate result buffer
   *
   * @return The query result coordinate buffer.
   * @exception TileDBError A TileDB exception
   */
  public Object getCoordinates() throws TileDBError {
    return getBuffer(tiledb.tiledb_coords());
  }

  /**
   * @return The number of elements in the result buffers. This is a map from the attribute name to
   *     a pair of values.
   *     <p>The first is number of elements for var size attributes, and the second is number of
   *     elements in the data buffer. For fixed sized attributes (and coordinates), the first is
   *     always 0.
   * @exception TileDBError A TileDB exception
   */
  public HashMap<String, Pair<Long, Long>> resultBufferElements() throws TileDBError {
    HashMap<String, Pair<Long, Long>> result = new HashMap<String, Pair<Long, Long>>();
    for (Map.Entry<String, NativeArray> entry : buffers_.entrySet()) {
      String name = entry.getKey();
      NativeArray val_buffer = entry.getValue();
      BigInteger val_nbytes = buffer_sizes_.get(name).getSecond().getitem(0);
      Long nelements =
          val_nbytes.divide(BigInteger.valueOf(val_buffer.getNativeTypeSize())).longValue();
      result.put(name, new Pair<Long, Long>(0l, nelements));
    }
    for (Map.Entry<String, Pair<NativeArray, NativeArray>> entry : var_buffers_.entrySet()) {
      String name = entry.getKey();
      Pair<uint64_tArray, uint64_tArray> buffer_size = buffer_sizes_.get(name);

      NativeArray off_buffer = entry.getValue().getFirst();
      BigInteger off_nbytes = buffer_size.getFirst().getitem(0);
      Long off_nelements =
          off_nbytes.divide(BigInteger.valueOf(off_buffer.getNativeTypeSize())).longValue();

      NativeArray val_buffer = entry.getValue().getSecond();
      BigInteger val_nbytes = buffer_size.getSecond().getitem(0);
      Long val_nelements =
          val_nbytes.divide(BigInteger.valueOf(val_buffer.getNativeTypeSize())).longValue();
      result.put(name, new Pair<Long, Long>(off_nelements, val_nelements));
    }
    return result;
  }

  /** Clears all attribute buffers. */
  public void resetBuffers() {
    for (Pair<uint64_tArray, uint64_tArray> size_pair : buffer_sizes_.values()) {
      size_pair.getFirst().delete();
      size_pair.getSecond().delete();
    }
    for (NativeArray buffer : buffers_.values()) {
      buffer.close();
    }
    for (Pair<NativeArray, NativeArray> var_buffer : var_buffers_.values()) {
      var_buffer.getFirst().close();
      var_buffer.getSecond().close();
    }
    buffers_.clear();
    var_buffers_.clear();
    buffer_sizes_.clear();
    executed = false;
  }

  private void prepareSubmission() throws TileDBError {
    for (Map.Entry<String, NativeArray> entry : buffers_.entrySet()) {
      String name = entry.getKey();
      NativeArray buffer = entry.getValue();
      uint64_tArray buffer_size = buffer_sizes_.get(name).getSecond();
      ctx.handleError(
          tiledb.tiledb_query_set_buffer(
              ctx.getCtxp(), queryp, name, buffer.toVoidPointer(), buffer_size.cast()));
    }
    for (Map.Entry<String, Pair<NativeArray, NativeArray>> entry : var_buffers_.entrySet()) {
      String name = entry.getKey();
      Pair<uint64_tArray, uint64_tArray> buffer_size = buffer_sizes_.get(name);
      NativeArray off_buffer = entry.getValue().getFirst();
      uint64_tArray offsets = PointerUtils.uint64_tArrayFromVoid(off_buffer.toVoidPointer());
      uint64_tArray off_size = buffer_size.getFirst();
      NativeArray val_buffer = entry.getValue().getSecond();
      uint64_tArray val_size = buffer_size.getSecond();
      ctx.handleError(
          tiledb.tiledb_query_set_buffer_var(
              ctx.getCtxp(),
              queryp,
              name,
              offsets.cast(),
              off_size.cast(),
              val_buffer.toVoidPointer(),
              val_size.cast()));
    }
  }

  /**
   * Return a Java primitive array object as a copy of the attribute buffer
   *
   * @param attr attribute name
   * @return A Java array
   * @exception TileDBError A TileDB exception
   */
  public Object getBuffer(String attr) throws TileDBError {
    if (buffers_.containsKey(attr)) {
      NativeArray buffer = buffers_.get(attr);
      Integer nelements =
          (buffer_sizes_
                  .get(attr)
                  .getSecond()
                  .getitem(0)
                  .divide(BigInteger.valueOf(buffer.getNativeTypeSize())))
              .intValue();
      return buffer.toJavaArray(nelements);
    } else if (var_buffers_.containsKey(attr)) {
      NativeArray buffer = var_buffers_.get(attr).getSecond();
      Integer nelements =
          (buffer_sizes_
                  .get(attr)
                  .getSecond()
                  .getitem(0)
                  .divide(BigInteger.valueOf(buffer.getNativeTypeSize())))
              .intValue();
      return buffer.toJavaArray(nelements);
    } else {
      throw new TileDBError("Query attribute buffer does not exist: " + attr);
    }
  }

  /**
   * Return an array containing offsets for a variable attribute buffer
   *
   * @param attr attribute name
   * @return A Java long[] array
   * @throws TileDBError A TileDB exception
   */
  public long[] getVarBuffer(String attr) throws TileDBError {
    if (!var_buffers_.containsKey(attr)) {
      throw new TileDBError("Query variable attribute buffer does not exist: " + attr);
    }
    NativeArray buffer = var_buffers_.get(attr).getFirst();
    Integer nelements =
        (buffer_sizes_
                .get(attr)
                .getFirst()
                .getitem(0)
                .divide(BigInteger.valueOf(buffer.getNativeTypeSize())))
            .intValue();
    return (long[]) buffer.toJavaArray(nelements);
  }

  private static class DefaultCallback implements Callback {
    public DefaultCallback() {}

    public void call() {}
  }

  @Override
  public String toString() {
    switch (type) {
      case TILEDB_READ:
        return "READ";
      case TILEDB_WRITE:
        return "WRITE";
    }
    return ""; // silence error
  }

  /** Free's native TileDB resources associated with the Query object */
  public void close() throws TileDBError {
    if (queryp != null) {
      ctx.handleError(tiledb.tiledb_query_finalize(ctx.getCtxp(), queryp));
      for (Pair<uint64_tArray, uint64_tArray> size_pair : buffer_sizes_.values()) {
        size_pair.getFirst().delete();
        size_pair.getSecond().delete();
      }
      for (NativeArray buffer : buffers_.values()) {
        buffer.close();
      }
      for (Pair<NativeArray, NativeArray> var_buffer : var_buffers_.values()) {
        var_buffer.getFirst().close();
        var_buffer.getSecond().close();
      }
      if (subarray != null) {
        subarray.close();
      }
      tiledb.tiledb_query_free(querypp);
      queryp = null;
    }
  }
}
