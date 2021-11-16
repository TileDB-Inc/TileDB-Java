/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 TileDB, Inc.
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
import java.nio.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

  private Map<String, Pair<ByteBuffer, ByteBuffer>> byteBuffers_;
  private Map<String, Pair<NativeArray, NativeArray>> buffers_;
  private Map<String, Pair<uint64_tArray, uint64_tArray>> buffer_sizes_;
  private Map<String, NativeArray> validityByteMaps_;
  private Map<String, ByteBuffer> validityByteMapsByteBuffers_;
  private Map<String, uint64_tArray> validityByteMapSizes_;

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
    this.buffers_ = Collections.synchronizedMap(new HashMap<>());
    this.byteBuffers_ = Collections.synchronizedMap(new HashMap<>());
    this.buffer_sizes_ = Collections.synchronizedMap(new HashMap<>());
    this.validityByteMaps_ = Collections.synchronizedMap(new HashMap<>());
    this.validityByteMapsByteBuffers_ = Collections.synchronizedMap(new HashMap<>());
    this.validityByteMapSizes_ = Collections.synchronizedMap(new HashMap<>());
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
  public synchronized Query setLayout(Layout layout) throws TileDBError {
    ctx.handleError(tiledb.tiledb_query_set_layout(ctx.getCtxp(), queryp, layout.toSwigEnum()));
    return this;
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
    ctx.handleError(tiledb.tiledb_query_submit(ctx.getCtxp(), queryp));

    // Set the actual number of bytes received to each ByteBuffer
    for (String attribute : byteBuffers_.keySet()) {
      boolean isVar;

      try (ArraySchema arraySchema = array.getSchema()) {
        if (arraySchema.hasAttribute(attribute)) {
          try (Attribute attr = arraySchema.getAttribute(attribute)) {
            isVar = attr.isVar();
          }
        } else {
          try (Dimension dim = arraySchema.getDomain().getDimension(attribute)) {
            isVar = dim.isVar();
          }
        }
      }

      if (isVar) {
        int offset_nbytes = this.buffer_sizes_.get(attribute).getFirst().getitem(0).intValue();
        int data_nbytes = this.buffer_sizes_.get(attribute).getSecond().getitem(0).intValue();
        this.byteBuffers_.get(attribute).getFirst().limit(offset_nbytes);
        this.byteBuffers_.get(attribute).getSecond().limit(data_nbytes);
      } else {
        int nbytes = this.buffer_sizes_.get(attribute).getSecond().getitem(0).intValue();
        this.byteBuffers_.get(attribute).getSecond().limit(nbytes);
      }
    }

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
    ctx.handleError(Utils.tiledb_query_submit_async(ctx.getCtxp(), queryp, callback));
  }

  /**
   * Sets a subarray, defined in the order dimensions were added. Coordinates are inclusive.
   *
   * @param subarray The targeted subarray.
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setSubarray(NativeArray subarray) throws TileDBError {
    Types.typeCheck(subarray.getNativeType(), array.getSchema().getDomain().getType());
    ctx.handleError(
        tiledb.tiledb_query_set_subarray(ctx.getCtxp(), queryp, subarray.toVoidPointer()));
    if (this.subarray != null) {
      this.subarray.close();
    }
    this.subarray = subarray;
    return this;
  }

  /**
   * Sets a subarray, defined in the order dimensions were added. Coordinates are inclusive.
   *
   * @param subarray The targeted subarray.
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setSubarray(ByteBuffer subarray) throws TileDBError {
    ctx.handleError(Utils.tiledb_query_set_subarray_nio(ctx.getCtxp(), queryp, subarray));
    return this;
  }

  /**
   * Adds a 1D range along a subarray dimension, which is in the form (start, end). The datatype of
   * the range components must be the same as the type of the domain of the array in the query.
   *
   * @param dimIdx The index of the dimension to add the range to
   * @param start The range start
   * @param end The range end
   * @return This query
   * @throws TileDBError A TileDB exception
   */
  public synchronized Query addRange(int dimIdx, Object start, Object end) throws TileDBError {
    Datatype dimType;
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(dimIdx).getType();
    }

    // We use java type check here because we can not tell the difference between unsigned and
    // signed
    // values coming from java, i.e. A UINT16 and INT32 are both Integer classes in java.
    Types.javaTypeCheck(start.getClass(), dimType.javaClass());
    Types.javaTypeCheck(end.getClass(), dimType.javaClass());

    try (NativeArray startArr = new NativeArray(ctx, 1, dimType);
        NativeArray endArr = new NativeArray(ctx, 1, dimType)) {
      startArr.setItem(0, start);
      endArr.setItem(0, end);

      ctx.handleError(
          tiledb.tiledb_query_add_range(
              ctx.getCtxp(),
              queryp,
              dimIdx,
              startArr.toVoidPointer(),
              endArr.toVoidPointer(),
              null));
    }

    return this;
  }

  /**
   * Adds a 1D variable-sized range along a subarray dimension, which is in the form (start, end).
   * Applicable only to variable-sized dimensions.
   *
   * @param dimIdx The index of the dimension to add the range to
   * @param start The range start
   * @param end The range end
   * @return This query
   * @throws TileDBError A TileDB exception
   */
  public synchronized Query addRangeVar(int dimIdx, String start, String end) throws TileDBError {
    Datatype dimType;
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(dimIdx).getType();
    }

    Types.javaTypeCheck(start.getClass(), dimType.javaClass());
    Types.javaTypeCheck(end.getClass(), dimType.javaClass());

    try (NativeArray startArr = new NativeArray(ctx, 1, dimType);
        NativeArray endArr = new NativeArray(ctx, 1, dimType)) {
      startArr.setItem(0, start);
      endArr.setItem(0, end);

      ctx.handleError(
          tiledb.tiledb_query_add_range_var(
              ctx.getCtxp(),
              queryp,
              dimIdx,
              startArr.toVoidPointer(),
              BigInteger.valueOf(start.length()),
              endArr.toVoidPointer(),
              BigInteger.valueOf(end.length())));
    }

    return this;
  }

  /**
   * Retrieves a range's start and end size for a given variable-length dimensions at a given range
   * index.
   *
   * @param dimIdx The index of the dimension to add the range to
   * @return This query
   * @throws TileDBError A TileDB exception
   */
  public synchronized Pair<Long, Long> getRangeVarSize(int dimIdx, BigInteger rangeIdx)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();
    try {
      ctx.handleError(
          tiledb.tiledb_query_get_range_var_size(
              ctx.getCtxp(), queryp, dimIdx, rangeIdx, startSize, endSize));

      return new Pair(
          tiledb.ullp_value(startSize).longValue(), tiledb.ullp_value(endSize).longValue());
    } catch (TileDBError error) {
      throw error;
    }
  }

  /**
   * Retrieves a specific range of the query subarray along a given variable-length dimension.
   *
   * @param dimIdx The index of the dimension to add the range to
   * @return This query
   * @throws TileDBError A TileDB exception
   */
  public synchronized Pair<String, String> getRangeVar(int dimIdx, BigInteger rangeIdx)
      throws TileDBError {
    Datatype dimType;
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(dimIdx).getType();
    }

    Pair<Long, Long> size = this.getRangeVarSize(dimIdx, rangeIdx);

    try (NativeArray startArr = new NativeArray(ctx, size.getFirst().intValue(), dimType);
        NativeArray endArr = new NativeArray(ctx, size.getSecond().intValue(), dimType)) {

      ctx.handleError(
          tiledb.tiledb_query_get_range_var(
              ctx.getCtxp(),
              queryp,
              dimIdx,
              rangeIdx,
              startArr.toVoidPointer(),
              endArr.toVoidPointer()));

      Object start = new String((byte[]) startArr.toJavaArray());
      Object end = new String((byte[]) endArr.toJavaArray());
      return new Pair(start, end);
    }
  }

  /**
   * Retrieves the estimated result size for a fixed-sized attribute/dimension.
   *
   * @param ctx The TileDB Context
   * @param column The attribute/dimension name
   * @return The estimated result size
   * @throws TileDBError
   */
  public synchronized long getEstResultSize(Context ctx, String column) throws TileDBError {
    SWIGTYPE_p_unsigned_long_long size = tiledb.new_ullp();

    ctx.handleError(tiledb.tiledb_query_get_est_result_size(ctx.getCtxp(), queryp, column, size));

    return tiledb.ullp_value(size).longValue();
  }

  /**
   * Retrieves the estimated result size for a var-sized attribute/dimension.
   *
   * @param ctx The TileDB Context
   * @param column The attribute/dimension name
   * @return A Pair containing the estimated result size of the offsets and the data buffers
   * @throws TileDBError
   */
  public synchronized Pair<Long, Long> getEstResultSizeVar(Context ctx, String column)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long offsetsSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long dataSize = tiledb.new_ullp();

    ctx.handleError(
        tiledb.tiledb_query_get_est_result_size_var(
            ctx.getCtxp(), queryp, column, offsetsSize, dataSize));

    return new Pair(
        tiledb.ullp_value(offsetsSize).longValue(), tiledb.ullp_value(dataSize).longValue());
  }

  /**
   * Retrieves the estimated result size for a var-sized nullable attribute.
   *
   * @param ctx The TileDB Context
   * @param column The attribute/dimension name
   * @return A Pair containing another Pair with the estimated result size of the offsets and the
   *     data buffers, and the estimated result size of the validity buffer
   * @throws TileDBError
   */
  public synchronized Pair<Pair<Long, Long>, Long> getEstResultSizeVarNullable(
      Context ctx, String column) throws TileDBError {
    SWIGTYPE_p_unsigned_long_long size = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long offsets = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long validity = tiledb.new_ullp();

    ctx.handleError(
        tiledb.tiledb_query_get_est_result_size_var_nullable(
            ctx.getCtxp(), queryp, column, offsets, size, validity));

    return new Pair(
        new Pair(tiledb.ullp_value(offsets).longValue(), tiledb.ullp_value(size).longValue()),
        tiledb.ullp_value(validity).longValue());
  }

  /**
   * Retrieves the estimated result size for a fixed-sized nullable attribute.
   *
   * @param ctx The TileDB Context
   * @param column The attribute/dimension name
   * @return The estimated result size
   * @throws TileDBError
   */
  public synchronized Pair<Long, Long> getEstResultSizeNullable(Context ctx, String column)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long size = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long validity = tiledb.new_ullp();

    ctx.handleError(
        tiledb.tiledb_query_get_est_result_size_nullable(
            ctx.getCtxp(), queryp, column, size, validity));

    return new Pair(tiledb.ullp_value(size).longValue(), tiledb.ullp_value(validity).longValue());
  }

  /**
   * Retrieves the number of ranges of the query subarray along a given dimension.
   *
   * @param dimIdx The index of the dimension whose range number to retrieve
   * @return The number of ranges of the dimension
   * @throws TileDBError A TileDB exception
   */
  public long getRangeNum(int dimIdx) throws TileDBError {
    uint64_tArray resultArr = new uint64_tArray(1);
    ctx.handleError(
        tiledb.tiledb_query_get_range_num(ctx.getCtxp(), queryp, dimIdx, resultArr.cast()));
    return resultArr.getitem(0).longValue();
  }

  /**
   * Retrieves a specific range of the query subarray along a given dimension.
   *
   * @param dimIdx The index of the dimension to retrieve the range from
   * @param rangeIdx The index of the range to retrieve
   * @return Pair of (start, end) of the range.
   * @throws TileDBError A TileDB exception
   */
  public Pair<Object, Object> getRange(int dimIdx, long rangeIdx) throws TileDBError {
    Datatype dimType;
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(dimIdx).getType();
    }

    SWIGTYPE_p_p_void startArrpp = tiledb.new_voidpArray(1);
    SWIGTYPE_p_p_void endArrpp = tiledb.new_voidpArray(1);
    SWIGTYPE_p_p_void strideArrpp = tiledb.new_voidpArray(1);

    try {
      ctx.handleError(
          tiledb.tiledb_query_get_range(
              ctx.getCtxp(),
              queryp,
              dimIdx,
              BigInteger.valueOf(rangeIdx),
              startArrpp,
              endArrpp,
              strideArrpp));

      try (NativeArray startArr = new NativeArray(ctx, dimType, startArrpp, 1);
          NativeArray endArr = new NativeArray(ctx, dimType, endArrpp, 1)) {
        Object start = startArr.getItem(0);
        Object end = endArr.getItem(0);
        return new Pair<>(start, end);
      }

    } finally {
      tiledb.delete_voidpArray(startArrpp);
      tiledb.delete_voidpArray(endArrpp);
      tiledb.delete_voidpArray(strideArrpp);
    }
  }

  /**
   * Sets a buffer for a fixed-sized attribute or dimension.
   *
   * @param attr The attribute/dimension name.
   * @param buffer NativeBuffer to be used for the attribute values.
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBuffer(String attr, NativeArray buffer) throws TileDBError {
    try (ArraySchema schema = array.getSchema()) {
      try (Domain domain = schema.getDomain()) {
        if (attr.equals(tiledb.tiledb_coords())) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else if (domain.hasDimension(attr)) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else {
          try (Attribute attribute = schema.getAttribute(attr)) {
            Types.typeCheck(attribute.getType(), buffer.getNativeType());
          }
        }
      }
    }

    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(0l));
    values_array_size.setitem(0, BigInteger.valueOf(buffer.getNBytes()));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    // Close previous buffers if they exist for this attribute
    if (buffers_.containsKey(attr)) {
      buffers_.get(attr).getSecond().close();
    }

    buffers_.put(attr, new Pair(null, buffer));
    buffer_sizes_.put(attr, buffer_sizes);

    // Set the actual TileDB buffer
    uint64_tArray buffer_size = buffer_sizes.getSecond();
    ctx.handleError(
        tiledb.tiledb_query_set_buffer(
            ctx.getCtxp(), queryp, attr, buffer.toVoidPointer(), buffer_size.cast()));

    return this;
  }

  /**
   * Sets a buffer for a fixed-sized attribute.
   *
   * @param attr The attribute name.
   * @param buffer NativeBuffer to be used for the attribute values.
   * @param bufferElements The actual number of buffer elements
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBuffer(String attr, NativeArray buffer, long bufferElements)
      throws TileDBError {
    if (bufferElements <= 0) {
      throw new TileDBError("Number of buffer elements must be >= 1");
    }
    if (bufferElements > buffer.getSize()) {
      throw new TileDBError(
          "Number of elements requested exceeds the number of elements in allocated buffer: "
              + bufferElements
              + " > "
              + buffer.getSize());
    }

    try (ArraySchema schema = array.getSchema()) {
      try (Domain domain = schema.getDomain()) {
        if (attr.equals(tiledb.tiledb_coords())) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else if (domain.hasDimension(attr)) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else {
          try (Attribute attribute = schema.getAttribute(attr)) {
            Types.typeCheck(attribute.getType(), buffer.getNativeType());
          }
        }
      }
    }

    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(0l));
    values_array_size.setitem(0, BigInteger.valueOf(bufferElements * buffer.getNativeTypeSize()));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    // Close previous buffers if they exist for this attribute
    if (buffers_.containsKey(attr)) {
      buffers_.get(attr).getSecond().close();
    }

    buffers_.put(attr, new Pair(null, buffer));
    buffer_sizes_.put(attr, buffer_sizes);

    // Set the actual TileDB buffer
    uint64_tArray buffer_size = buffer_sizes.getSecond();

    ctx.handleError(
        tiledb.tiledb_query_set_buffer(
            ctx.getCtxp(), queryp, attr, buffer.toVoidPointer(), buffer_size.cast()));

    return this;
  }

  /**
   * Sets a nullable buffer for a fixed-sized attribute.
   *
   * @param attr The attribute name.
   * @param buffer NativeBuffer to be used for the attribute values.
   * @param bytemap The byte-map
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBufferNullable(String attr, NativeArray buffer, NativeArray bytemap)
      throws TileDBError {

    try (ArraySchema schema = array.getSchema()) {
      try (Domain domain = schema.getDomain()) {
        if (attr.equals(tiledb.tiledb_coords())) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else if (domain.hasDimension(attr)) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else {
          try (Attribute attribute = schema.getAttribute(attr)) {
            Types.typeCheck(attribute.getType(), buffer.getNativeType());
          }
        }
      }
    }

    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);
    uint64_tArray buffer_validity_bytemap_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(0l));
    values_array_size.setitem(0, BigInteger.valueOf(buffer.getNBytes()));
    buffer_validity_bytemap_size.setitem(0, BigInteger.valueOf(bytemap.getNBytes()));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    // Close previous buffers if they exist for this attribute
    if (buffers_.containsKey(attr)) {
      buffers_.get(attr).getSecond().close();
    }

    buffers_.put(attr, new Pair(null, buffer));
    buffer_sizes_.put(attr, buffer_sizes);
    validityByteMaps_.put(attr, bytemap);
    validityByteMapSizes_.put(attr, buffer_validity_bytemap_size);

    // Set the actual TileDB buffer
    uint64_tArray buffer_size = buffer_sizes.getSecond();

    ctx.handleError(
        tiledb.tiledb_query_set_buffer_nullable(
            ctx.getCtxp(),
            queryp,
            attr,
            buffer.toVoidPointer(),
            buffer_size.cast(),
            bytemap.getUint8_tArray().cast(),
            buffer_validity_bytemap_size.cast()));

    return this;
  }

  /**
   * Sets a nullable buffer for a fixed-sized, nullable attribute.
   *
   * @param attr The attribute name.
   * @param buffer NativeBuffer to be used for the attribute values.
   * @param bytemap The byte-map
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBufferNullableNIO(String attr, ByteBuffer buffer, ByteBuffer bytemap)
      throws TileDBError {

    if (buffer.capacity() <= 0) {
      throw new TileDBError("Number of buffer elements must be >= 1");
    }

    if (!buffer.isDirect()) {
      throw new TileDBError(
          "The ByteBuffer provided is not direct. Please provide a direct buffer (ByteBuffer.allocateDirect(...))");
    }

    if (!buffer.order().equals(ByteOrder.nativeOrder())) {
      throw new TileDBError(
          "The order of the data ByteBuffer should be the same as the native order (ByteOrder.nativeOrder()).");
    }

    this.byteBuffers_.put(attr, new Pair<>(null, buffer));

    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);
    uint64_tArray buffer_validity_bytemap_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(0));
    values_array_size.setitem(0, BigInteger.valueOf(buffer.capacity()));
    buffer_validity_bytemap_size.setitem(0, BigInteger.valueOf(bytemap.capacity()));

    buffer_sizes_.put(attr, new Pair<>(offsets_array_size, values_array_size));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    this.byteBuffers_.put(attr, new Pair<>(null, buffer));

    buffer_sizes_.put(attr, buffer_sizes);
    validityByteMapsByteBuffers_.put(attr, bytemap);

    // Set the actual TileDB buffer
    uint64_tArray buffer_size = buffer_sizes.getSecond();

    ctx.handleError(
        Utils.tiledb_query_set_buffer_nullable_nio(
            ctx.getCtxp(),
            queryp,
            attr,
            buffer,
            buffer_size.cast(),
            bytemap,
            buffer_validity_bytemap_size.cast()));

    return this;
  }

  /**
   * Sets a nullable buffer for a variable-sized getAttribute.
   *
   * @param attr Attribute name
   * @param offsets Offsets where a new element begins in the data buffer.
   * @param buffer Buffer vector with elements of the attribute type.
   * @param bytemap The byte-map
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBufferNullable(
      String attr, NativeArray offsets, NativeArray buffer, NativeArray bytemap)
      throws TileDBError {

    if (attr.equals(tiledb.tiledb_coords())) {
      throw new TileDBError("Cannot set coordinate buffer as variable sized.");
    }

    if (!offsets.getNativeType().equals(TILEDB_UINT64)) {
      throw new TileDBError(
          "Buffer offsets should be of getType TILEDB_UINT64 or Long. Found getType: "
              + offsets.getNativeType());
    }

    // Type check the buffer native type matches the schema attribute type
    try (ArraySchema schema = array.getSchema()) {
      try (Domain domain = schema.getDomain()) {
        if (attr.equals(tiledb.tiledb_coords())) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else if (domain.hasDimension(attr)) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else {
          try (Attribute attribute = schema.getAttribute(attr)) {
            Types.typeCheck(attribute.getType(), buffer.getNativeType());
          }
        }
      }
    }

    uint64_tArray offsets_array = PointerUtils.uint64_tArrayFromVoid(offsets.toVoidPointer());
    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);
    uint64_tArray buffer_validity_bytemap_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(offsets.getNBytes()));
    values_array_size.setitem(0, BigInteger.valueOf(buffer.getNBytes()));
    buffer_validity_bytemap_size.setitem(
        0, BigInteger.valueOf(bytemap.getSize() * bytemap.getNativeTypeSize()));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    // Close previous buffers if they exist for this attribute
    if (buffers_.containsKey(attr)) {
      Pair<NativeArray, NativeArray> prev_buffers = buffers_.get(attr);
      prev_buffers.getFirst().close();
      prev_buffers.getSecond().close();
    }

    buffers_.put(attr, new Pair<>(offsets, buffer));
    buffer_sizes_.put(attr, buffer_sizes);
    validityByteMaps_.put(attr, bytemap);
    validityByteMapSizes_.put(attr, buffer_validity_bytemap_size);

    ctx.handleError(
        tiledb.tiledb_query_set_buffer_var_nullable(
            ctx.getCtxp(),
            queryp,
            attr,
            offsets_array.cast(),
            offsets_array_size.cast(),
            buffer.toVoidPointer(),
            values_array_size.cast(),
            bytemap.getUint8_tArray().cast(),
            buffer_validity_bytemap_size.cast()));

    return this;
  }

  /**
   * Sets a ByteBuffer buffer for a variable-sized, nullable attribute.
   *
   * @param attr Attribute name
   * @param offsets Offsets where a new element begins in the data buffer.
   * @param buffer Buffer vector with elements of the attribute type.
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBufferNullableNIO(
      String attr, ByteBuffer offsets, ByteBuffer buffer, ByteBuffer bytemap) throws TileDBError {

    if (attr.equals(tiledb.tiledb_coords())) {
      throw new TileDBError("Cannot set coordinate buffer as variable sized.");
    }

    if (!offsets.order().equals(ByteOrder.nativeOrder()) && offsets.position() > 0) {
      throw new TileDBError(
          "The order of the offsets ByteBuffer should be the same as the native order (ByteOrder.nativeOrder()) before values are inserted.");
    }

    if (!buffer.order().equals(ByteOrder.nativeOrder()) && buffer.position() > 0) {
      throw new TileDBError(
          "The order of the data ByteBuffer should be the same as the native order (ByteOrder.nativeOrder()) before values are inserted.");
    }

    offsets.order(ByteOrder.nativeOrder());
    buffer.order(ByteOrder.nativeOrder());

    uint64_tArray offsets_array_size = new uint64_tArray(0);
    uint64_tArray values_array_size = new uint64_tArray(0);
    uint64_tArray buffer_validity_bytemap_size = new uint64_tArray(0);

    offsets_array_size.setitem(0, BigInteger.valueOf(offsets.capacity()));
    values_array_size.setitem(0, BigInteger.valueOf(buffer.capacity()));
    buffer_validity_bytemap_size.setitem(0, BigInteger.valueOf(bytemap.capacity()));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    // Close previous buffers if they exist for this attribute
    if (buffers_.containsKey(attr)) {
      Pair<NativeArray, NativeArray> prev_buffers = buffers_.get(attr);
      prev_buffers.getFirst().close();
      prev_buffers.getSecond().close();
    }

    buffer_sizes_.put(attr, buffer_sizes);
    this.byteBuffers_.put(attr, new Pair(offsets, buffer));

    ctx.handleError(
        Utils.tiledb_query_set_buffer_var_nullable_nio(
            ctx.getCtxp(),
            queryp,
            attr,
            offsets,
            offsets_array_size.cast(),
            buffer,
            values_array_size.cast(),
            bytemap,
            buffer_validity_bytemap_size.cast()));

    return this;
  }

  /**
   * * Sets a NIO ByteBuffer
   *
   * @param attr The attribute
   * @param bufferElements
   * @return The NIO ByteBuffer
   * @throws TileDBError
   */
  public synchronized Query setBuffer(String attr, long bufferElements) throws TileDBError {
    if (bufferElements <= 0) {
      throw new TileDBError("Number of buffer elements must be >= 1");
    }

    Datatype dt = Util.getFieldDatatype(array, attr);

    int size = Util.castLongToInt(bufferElements * dt.getNativeSize());

    ByteBuffer buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());

    this.setBuffer(attr, buffer);

    return this;
  }

  /**
   * * Sets a NIO ByteBuffer
   *
   * @param attr The attribute
   * @param buffer The input NIO ByteBuffer
   * @return The NIO ByteBuffer
   * @throws TileDBError
   */
  public synchronized Query setBuffer(String attr, ByteBuffer buffer) throws TileDBError {
    if (buffer.capacity() <= 0) {
      throw new TileDBError("Number of buffer elements must be >= 1");
    }

    if (!buffer.isDirect()) {
      throw new TileDBError(
          "The ByteBuffer provided is not direct. Please provide a direct buffer (ByteBuffer.allocateDirect(...))");
    }

    if (!buffer.order().equals(ByteOrder.nativeOrder())) {
      throw new TileDBError(
          "The order of the data ByteBuffer should be the same as the native order (ByteOrder.nativeOrder()).");
    }

    this.byteBuffers_.put(attr, new Pair<>(null, buffer));

    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(0));
    values_array_size.setitem(0, BigInteger.valueOf(buffer.capacity()));

    buffer_sizes_.put(attr, new Pair<>(offsets_array_size, values_array_size));

    ctx.handleError(
        Utils.tiledb_query_set_buffer_nio(
            ctx.getCtxp(), queryp, attr, buffer, values_array_size.cast()));

    return this;
  }

  /**
   * Sets a buffer for a variable-sized getAttribute.
   *
   * @param attr Attribute name
   * @param offsets Offsets where a new element begins in the data buffer.
   * @param buffer Buffer vector with elements of the attribute type.
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBuffer(String attr, NativeArray offsets, NativeArray buffer)
      throws TileDBError {

    if (attr.equals(tiledb.tiledb_coords())) {
      throw new TileDBError("Cannot set coordinate buffer as variable sized.");
    }

    if (!offsets.getNativeType().equals(TILEDB_UINT64)) {
      throw new TileDBError(
          "Buffer offsets should be of getType TILEDB_UINT64 or Long. Found getType: "
              + offsets.getNativeType());
    }

    // Type check the buffer native type matches the schema attribute type
    try (ArraySchema schema = array.getSchema()) {
      try (Domain domain = schema.getDomain()) {
        if (attr.equals(tiledb.tiledb_coords())) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else if (domain.hasDimension(attr)) {
          Types.typeCheck(domain.getDimension(attr).getType(), buffer.getNativeType());
        } else {
          try (Attribute attribute = schema.getAttribute(attr)) {
            Types.typeCheck(attribute.getType(), buffer.getNativeType());
          }
        }
      }
    }

    uint64_tArray offsets_array = PointerUtils.uint64_tArrayFromVoid(offsets.toVoidPointer());
    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(offsets.getNBytes()));
    values_array_size.setitem(0, BigInteger.valueOf(buffer.getNBytes()));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    // Close previous buffers if they exist for this attribute
    if (buffers_.containsKey(attr)) {
      Pair<NativeArray, NativeArray> prev_buffers = buffers_.get(attr);
      prev_buffers.getFirst().close();
      prev_buffers.getSecond().close();
    }

    buffers_.put(attr, new Pair<>(offsets, buffer));
    buffer_sizes_.put(attr, buffer_sizes);

    ctx.handleError(
        tiledb.tiledb_query_set_buffer_var(
            ctx.getCtxp(),
            queryp,
            attr,
            offsets_array.cast(),
            offsets_array_size.cast(),
            buffer.toVoidPointer(),
            values_array_size.cast()));

    return this;
  }

  /**
   * Sets a ByteBuffer buffer for a variable-sized getAttribute.
   *
   * @param attr Attribute name
   * @param offsets Offsets where a new element begins in the data buffer.
   * @param buffer Buffer vector with elements of the attribute type.
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBuffer(String attr, ByteBuffer offsets, ByteBuffer buffer)
      throws TileDBError {

    if (attr.equals(tiledb.tiledb_coords())) {
      throw new TileDBError("Cannot set coordinate buffer as variable sized.");
    }

    if (!offsets.order().equals(ByteOrder.nativeOrder()) && offsets.position() > 0) {
      throw new TileDBError(
          "The order of the offsets ByteBuffer should be the same as the native order (ByteOrder.nativeOrder()) before values are inserted.");
    }

    if (!buffer.order().equals(ByteOrder.nativeOrder()) && buffer.position() > 0) {
      throw new TileDBError(
          "The order of the data ByteBuffer should be the same as the native order (ByteOrder.nativeOrder()) before values are inserted.");
    }

    offsets.order(ByteOrder.nativeOrder());
    buffer.order(ByteOrder.nativeOrder());

    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(offsets.capacity()));
    values_array_size.setitem(0, BigInteger.valueOf(buffer.capacity()));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    // Close previous buffers if they exist for this attribute
    if (buffers_.containsKey(attr)) {
      Pair<NativeArray, NativeArray> prev_buffers = buffers_.get(attr);
      prev_buffers.getFirst().close();
      prev_buffers.getSecond().close();
    }

    buffer_sizes_.put(attr, buffer_sizes);
    this.byteBuffers_.put(attr, new Pair(offsets, buffer));

    ctx.handleError(
        Utils.tiledb_query_set_buffer_var_nio(
            ctx.getCtxp(),
            queryp,
            attr,
            offsets,
            offsets_array_size.cast(),
            buffer,
            values_array_size.cast()));

    return this;
  }

  /**
   * Sets a buffer for a variable-sized getAttribute.
   *
   * @param attr Attribute name
   * @param offsets Offsets where a new element begins in the data buffer.
   * @param buffer Buffer vector with elements of the attribute type.
   * @param offsetElements The actual number of offset elements
   * @param bufferElements The actual number of buffer elements
   * @exception TileDBError A TileDB exception
   */
  public synchronized Query setBuffer(
      String attr,
      NativeArray offsets,
      NativeArray buffer,
      long offsetElements,
      long bufferElements)
      throws TileDBError {
    Integer offsetSize = buffer.getSize();
    Integer bufferSize = buffer.getSize();

    if (offsetElements > offsetSize) {
      throw new TileDBError(
          "Number of offset elements requested exceeds the number of elements in allocated offset buffer: "
              + offsetElements
              + " > "
              + offsetSize);
    }
    if (bufferElements > bufferSize) {
      throw new TileDBError(
          "Number of buffer elements requested exceeds the number of elements in allocated buffer"
              + bufferElements
              + " > "
              + bufferSize);
    }
    if (attr.equals(tiledb.tiledb_coords())) {
      throw new TileDBError("Cannot set coordinate buffer as variable sized.");
    }

    if (!offsets.getNativeType().equals(TILEDB_UINT64)) {
      throw new TileDBError(
          "Buffer offsets should be of getType TILEDB_UINT64 or Long. Found getType: "
              + offsets.getNativeType());
    }
    if (offsetElements <= 0 || bufferElements <= 0) {
      throw new TileDBError("Number of buffer elements must be >= 1");
    }

    // Type check the buffer native type matches the schema attribute type
    try (ArraySchema schema = array.getSchema();
        Attribute attribute = schema.getAttribute(attr)) {
      Types.typeCheck(attribute.getType(), buffer.getNativeType());
    }

    uint64_tArray offsets_array = PointerUtils.uint64_tArrayFromVoid(offsets.toVoidPointer());
    uint64_tArray offsets_array_size = new uint64_tArray(1);
    uint64_tArray values_array_size = new uint64_tArray(1);

    offsets_array_size.setitem(0, BigInteger.valueOf(offsetElements * offsets.getNativeTypeSize()));
    values_array_size.setitem(0, BigInteger.valueOf(bufferElements * buffer.getNativeTypeSize()));

    Pair<uint64_tArray, uint64_tArray> buffer_sizes =
        new Pair<>(offsets_array_size, values_array_size);

    // Close previous buffers if they exist for this attribute
    if (buffers_.containsKey(attr)) {
      Pair<NativeArray, NativeArray> prev_buffers = buffers_.get(attr);
      prev_buffers.getFirst().close();
      prev_buffers.getSecond().close();
    }

    buffers_.put(attr, new Pair<>(offsets, buffer));
    buffer_sizes_.put(attr, buffer_sizes);

    ctx.handleError(
        tiledb.tiledb_query_set_buffer_var(
            ctx.getCtxp(),
            queryp,
            attr,
            offsets_array.cast(),
            offsets_array_size.cast(),
            buffer.toVoidPointer(),
            values_array_size.cast()));

    return this;
  }

  private Query setBufferSizeUnsafe(String attribute, long offsetSize, long bufferSize) {
    buffer_sizes_.get(attribute).getFirst().setitem(0, BigInteger.valueOf(offsetSize));
    buffer_sizes_.get(attribute).getSecond().setitem(0, BigInteger.valueOf(bufferSize));
    return this;
  }

  public synchronized Query setBufferByteSize(String attribute, Long offsetSize, Long bufferSize)
      throws TileDBError {
    if (!buffers_.containsKey(attribute)) {
      throw new TileDBError("Query var attribute buffer does not exist: " + attribute);
    }
    if (offsetSize <= 0 || bufferSize <= 0) {
      throw new TileDBError("Number of buffer bytes must be >= 1");
    }
    Pair<NativeArray, NativeArray> varBuffers = buffers_.get(attribute);
    NativeArray offsetBuffer = varBuffers.getFirst();
    Long offsetNBytes = offsetBuffer.getNBytes();
    NativeArray buffer = varBuffers.getSecond();
    Long bufferNBytes = buffer.getNBytes();
    if (offsetSize > offsetNBytes) {
      throw new TileDBError(
          "Number of offset bytes requested exceeds the number bytes of in allocated offset buffer: "
              + offsetNBytes
              + " > "
              + offsetSize);
    }
    if (bufferSize > bufferNBytes) {
      throw new TileDBError(
          "Number of buffer bytes requested exceeds the number of bytes in allocated buffer"
              + bufferNBytes
              + " > "
              + bufferSize);
    }
    return setBufferSizeUnsafe(attribute, offsetSize, bufferSize);
  }

  public synchronized Query setBufferByteSize(String attribute, Long bufferSize)
      throws TileDBError {
    if (!buffers_.containsKey(attribute)) {
      throw new TileDBError("Query attrbute buffer does not exist: " + attribute);
    }
    if (bufferSize <= 0) {
      throw new TileDBError("Number of buffer bytes must be >= 1");
    }
    NativeArray buffer = buffers_.get(attribute).getSecond();
    Long bufferNBytes = buffer.getNBytes();
    if (bufferSize > bufferNBytes) {
      throw new TileDBError(
          "Number of bytes requested exceeds the number of bytes in allocated buffer: "
              + bufferSize
              + " > "
              + bufferNBytes);
    }
    return setBufferSizeUnsafe(attribute, 0l, bufferSize);
  }

  public synchronized Query setBufferElements(String attribute, Integer bufferElements)
      throws TileDBError {
    if (!buffers_.containsKey(attribute)) {
      throw new TileDBError("Query attribute buffer does not exist: " + attribute);
    }
    if (bufferElements <= 0) {
      throw new TileDBError("Number of buffer elements must be >= 1");
    }
    NativeArray buffer = buffers_.get(attribute).getSecond();
    Integer bufferSize = buffer.getSize();
    if (bufferElements > bufferSize) {
      throw new TileDBError(
          "Number of elements requested exceeds the number of elements in allocated buffer: "
              + bufferElements
              + " > "
              + bufferSize);
    }
    return setBufferSizeUnsafe(attribute, 0l, (long) (bufferElements * buffer.getNativeTypeSize()));
  }

  public synchronized Query setBufferElements(
      String attribute, Integer offsetElements, Integer bufferElements) throws TileDBError {
    if (!buffers_.containsKey(attribute)) {
      throw new TileDBError("Query var attribute buffer does not exist: " + attribute);
    }
    if (offsetElements <= 0 || bufferElements <= 0) {
      throw new TileDBError("Number of buffer elements must be >= 1");
    }
    Pair<NativeArray, NativeArray> varBuffers = buffers_.get(attribute);
    NativeArray offsetBuffer = varBuffers.getFirst();
    Integer offsetSize = offsetBuffer.getSize();
    NativeArray buffer = varBuffers.getSecond();
    Integer bufferSize = buffer.getSize();
    if (offsetElements > offsetSize) {
      throw new TileDBError(
          "Number of offset elements requested exceeds the number of elements in allocated offset buffer: "
              + offsetElements
              + " > "
              + offsetSize);
    }
    if (bufferElements > bufferSize) {
      throw new TileDBError(
          "Number of buffer elements requested exceeds the number of elements in allocated buffer"
              + bufferElements
              + " > "
              + bufferSize);
    }
    return setBufferSizeUnsafe(
        attribute,
        (long) (offsetElements * offsetBuffer.getNativeTypeSize()),
        (long) (bufferElements * buffer.getNativeTypeSize()));
  }

  /**
   * Set the coordinate buffer
   *
   * @param buffer A NativeArray to be used for the coordinates.
   * @exception TileDBError A TileDB exception
   */
  @Deprecated
  public Query setCoordinates(NativeArray buffer) throws TileDBError {
    setBuffer(tiledb.tiledb_coords(), buffer);
    return this;
  }

  /**
   * Get the coordinate result buffer
   *
   * @return The query result coordinate buffer.
   * @exception TileDBError A TileDB exception
   */
  @Deprecated
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
    for (Map.Entry<String, Pair<NativeArray, NativeArray>> entry : buffers_.entrySet()) {
      String name = entry.getKey();

      // Fixed-sized
      if (entry.getValue().getFirst() == null) {
        NativeArray val_buffer = entry.getValue().getSecond();
        BigInteger val_nbytes = buffer_sizes_.get(name).getSecond().getitem(0);
        Long nelements =
            val_nbytes.divide(BigInteger.valueOf(val_buffer.getNativeTypeSize())).longValue();
        result.put(name, new Pair<>(0l, nelements));
      }
      // Var-sized
      else {
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
    }
    return result;
  }

  /**
   * It is highly likely you want to use resultBufferElements.
   *
   * <p>resultBufferSizes used mostly for metric reporting.
   *
   * @return The size in bytes of the result buffers. This is a map from the attribute name to a
   *     pair of values.
   *     <p>The first is size in bytes for offsets of var size attributes, and the second is size in
   *     bytes in the data buffer. For fixed sized attributes (and coordinates), the first is always
   *     0.
   * @exception TileDBError A TileDB exception
   */
  public HashMap<String, Pair<Long, Long>> resultBufferSizes() throws TileDBError {
    HashMap<String, Pair<Long, Long>> result = new HashMap<String, Pair<Long, Long>>();
    for (String name : buffers_.keySet()) {
      BigInteger val_nbytes = buffer_sizes_.get(name).getSecond().getitem(0);
      result.put(name, new Pair<>(0l, val_nbytes.longValue()));
    }
    for (Map.Entry<String, Pair<NativeArray, NativeArray>> entry : buffers_.entrySet()) {
      String name = entry.getKey();
      Pair<uint64_tArray, uint64_tArray> buffer_size = buffer_sizes_.get(name);

      BigInteger off_nbytes = buffer_size.getFirst().getitem(0);

      BigInteger val_nbytes = buffer_size.getSecond().getitem(0);
      result.put(name, new Pair<Long, Long>(off_nbytes.longValue(), val_nbytes.longValue()));
    }
    return result;
  }

  /** Clears all attribute buffers. */
  public synchronized void resetBuffers() {
    for (Pair<NativeArray, NativeArray> buffer : buffers_.values()) {
      buffer.getSecond().close();
    }
    buffers_.clear();
    for (Pair<NativeArray, NativeArray> var_buffer : buffers_.values()) {
      var_buffer.getFirst().close();
      var_buffer.getSecond().close();
    }
    buffers_.clear();
    for (Pair<uint64_tArray, uint64_tArray> size_pair : buffer_sizes_.values()) {
      size_pair.getFirst().delete();
      size_pair.getSecond().delete();
    }
    buffer_sizes_.clear();
  }

  public synchronized Query resetBufferSizes(Long val) {
    BigInteger sizeVal = BigInteger.valueOf(val);
    for (Pair<uint64_tArray, uint64_tArray> size_pair : buffer_sizes_.values()) {
      size_pair.getFirst().setitem(0, sizeVal);
      size_pair.getSecond().setitem(0, sizeVal);
    }
    return this;
  }

  /** Resets all attribute buffer sizes to zero */
  public Query resetBufferSizes() {
    return resetBufferSizes(0l);
  }

  /**
   * Return a Java primitive array object as a copy of the attribute buffer
   *
   * @param bufferName attribute name
   * @return A Java array
   * @exception TileDBError A TileDB exception
   */
  public Object getBuffer(String bufferName) throws TileDBError {
    if (buffers_.containsKey(bufferName)) {
      NativeArray buffer = buffers_.get(bufferName).getSecond();
      Integer nelements =
          (buffer_sizes_
                  .get(bufferName)
                  .getSecond()
                  .getitem(0)
                  .divide(BigInteger.valueOf(buffer.getNativeTypeSize())))
              .intValue();
      return buffer.toJavaArray(nelements);
    } else if (buffers_.containsKey(bufferName)) {
      NativeArray buffer = buffers_.get(bufferName).getSecond();
      Integer nelements =
          (buffer_sizes_
                  .get(bufferName)
                  .getSecond()
                  .getitem(0)
                  .divide(BigInteger.valueOf(buffer.getNativeTypeSize())))
              .intValue();
      return buffer.toJavaArray(nelements);
    } else {
      throw new TileDBError("Query attribute buffer does not exist: " + bufferName);
    }
  }

  /**
   * Retrieves the ByteBuffer of attribute attr
   *
   * @param attr The attribute name
   * @return The ByteBuffer
   * @throws TileDBError A TileDB exception
   */
  public Pair<ByteBuffer, ByteBuffer> getByteBuffer(String attr) throws TileDBError {
    if (byteBuffers_.containsKey(attr)) return this.byteBuffers_.get(attr);
    else throw new TileDBError("ByteBuffer does not exist for attribute: " + attr);
  }

  /**
   * Return an array containing offsets for a variable attribute buffer
   *
   * @param bufferName attribute name
   * @return A Java long[] array
   * @throws TileDBError A TileDB exception
   */
  public long[] getVarBuffer(String bufferName) throws TileDBError {
    if (!buffers_.containsKey(bufferName)) {
      throw new TileDBError("Query variable attribute buffer does not exist: " + bufferName);
    }
    NativeArray buffer = buffers_.get(bufferName).getFirst();
    Integer nelements =
        (buffer_sizes_
                .get(bufferName)
                .getFirst()
                .getitem(0)
                .divide(BigInteger.valueOf(buffer.getNativeTypeSize())))
            .intValue();
    return (long[]) buffer.toJavaArray(nelements);
  }

  /**
   * Retrieves an IntBuffer of an attribute attr of type Integer
   *
   * @param bufferName The attribute name
   * @return The IntBuffer
   * @throws TileDBError A TileDB exception
   */
  public Pair<LongBuffer, IntBuffer> getIntBuffer(String bufferName) throws TileDBError {
    Datatype dt = Util.getFieldDatatype(array, bufferName);

    if (dt.javaClass() != Integer.class)
      throw new TileDBError(
          "IntBuffer requested, but attribute " + bufferName + " has type " + dt.name());

    Pair<ByteBuffer, ByteBuffer> buffer = this.byteBuffers_.get(bufferName);
    if (byteBuffers_.containsKey(bufferName)) {
      LongBuffer offsets = null;
      if (buffer.getFirst() != null) offsets = buffer.getFirst().asLongBuffer();
      return new Pair(offsets, buffer.getSecond().asIntBuffer());
    } else throw new TileDBError("ByteBuffer does not exist for attribute: " + bufferName);
  }

  /**
   * Retrieves a LongBuffer of an attribute bufferName of type Long
   *
   * @param bufferName The attribute name
   * @return The IntBuffer
   * @throws TileDBError A TileDB exception
   */
  public Pair<LongBuffer, LongBuffer> getLongBuffer(String bufferName) throws TileDBError {
    Datatype dt = Util.getFieldDatatype(array, bufferName);

    if (dt.javaClass() != Long.class)
      throw new TileDBError(
          "LongBuffer requested, but attribute " + bufferName + " has type " + dt.name());

    Pair<ByteBuffer, ByteBuffer> buffer = this.byteBuffers_.get(bufferName);
    if (byteBuffers_.containsKey(bufferName))
      return new Pair(buffer.getFirst().asLongBuffer(), buffer.getSecond().asLongBuffer());
    else throw new TileDBError("ByteBuffer does not exist for attribute: " + bufferName);
  }

  /**
   * Retrieves the CharBuffer of an attribute bufferName of type Char
   *
   * @param bufferName The attribute name
   * @return The CharBuffer
   * @throws TileDBError A TileDB exception
   */
  public Pair<LongBuffer, ShortBuffer> getShortBuffer(String bufferName) throws TileDBError {
    Datatype dt = Util.getFieldDatatype(array, bufferName);

    if (dt.javaClass() != Short.class)
      throw new TileDBError(
          "ShortBuffer requested, but attribute " + bufferName + " has type " + dt.name());

    Pair<ByteBuffer, ByteBuffer> buffer = this.byteBuffers_.get(bufferName);
    if (byteBuffers_.containsKey(bufferName)) {
      LongBuffer offsets = null;
      if (buffer.getFirst() != null) offsets = buffer.getFirst().asLongBuffer();
      return new Pair(offsets, buffer.getSecond().asShortBuffer());
    } else throw new TileDBError("ByteBuffer does not exist for attribute: " + bufferName);
  }

  /**
   * Retrieves the CharBuffer of an attribute bufferName of type Char
   *
   * @param bufferName The attribute name
   * @return The CharBuffer
   * @throws TileDBError A TileDB exception
   */
  public Pair<LongBuffer, CharBuffer> getCharBuffer(String bufferName) throws TileDBError {
    Datatype dt = Util.getFieldDatatype(array, bufferName);

    if (dt.javaClass() != Byte.class)
      throw new TileDBError(
          "CharBuffer requested, but attribute " + bufferName + " has type " + dt.name());

    Pair<ByteBuffer, ByteBuffer> buffer = this.byteBuffers_.get(bufferName);
    if (byteBuffers_.containsKey(bufferName)) {
      LongBuffer offsets = null;
      if (buffer.getFirst() != null) offsets = buffer.getFirst().asLongBuffer();

      // Set the US_ASCII charset and decode, so each character is treated as a single byte instead
      // of two.
      Charset charset = StandardCharsets.US_ASCII;
      CharBuffer charBuffer = charset.decode(buffer.getSecond());
      return new Pair(offsets, charBuffer);
    } else throw new TileDBError("ByteBuffer does not exist for attribute: " + bufferName);
  }

  /**
   * Retrieves the an FloatBuffer of an attribute bufferName of type Float
   *
   * @param bufferName The attribute name
   * @return The FloatBuffer
   * @throws TileDBError A TileDB exception
   */
  public Pair<LongBuffer, FloatBuffer> getFloatBuffer(String bufferName) throws TileDBError {
    Datatype dt = Util.getFieldDatatype(array, bufferName);

    if (dt.javaClass() != Float.class)
      throw new TileDBError(
          "FloatBuffer requested, but attribute " + bufferName + " has type " + dt.name());

    Pair<ByteBuffer, ByteBuffer> buffer = this.byteBuffers_.get(bufferName);
    if (byteBuffers_.containsKey(bufferName)) {
      LongBuffer offsets = null;
      if (buffer.getFirst() != null) offsets = buffer.getFirst().asLongBuffer();
      return new Pair(offsets, buffer.getSecond().asFloatBuffer());
    } else throw new TileDBError("ByteBuffer does not exist for attribute: " + bufferName);
  }

  /**
   * Retrieves the an DoubleBuffer of an attribute bufferName of type Double
   *
   * @param bufferName The attribute name
   * @return The DoubleBuffer
   * @throws TileDBError A TileDB exception
   */
  public Pair<LongBuffer, DoubleBuffer> getDoubleBuffer(String bufferName) throws TileDBError {
    Datatype dt = Util.getFieldDatatype(array, bufferName);

    if (dt.javaClass() != Double.class)
      throw new TileDBError(
          "DoubleBuffer requested, but attribute " + bufferName + " has type " + dt.name());

    Pair<ByteBuffer, ByteBuffer> buffer = this.byteBuffers_.get(bufferName);
    if (byteBuffers_.containsKey(bufferName)) {
      LongBuffer offsets = null;
      if (buffer.getFirst() != null) offsets = buffer.getFirst().asLongBuffer();
      return new Pair(offsets, buffer.getSecond().asDoubleBuffer());
    } else throw new TileDBError("ByteBuffer does not exist for attribute: " + bufferName);
  }

  /**
   * Drains a ByteBuffer and returns its contents as a byte[] array
   *
   * @param bufferName The attribute name
   * @return The byte[] array
   * @throws TileDBError A TileDB exception
   */
  public byte[] getByteArray(String bufferName) throws TileDBError {
    ByteBuffer buffer = this.byteBuffers_.get(bufferName).getSecond();
    if (byteBuffers_.containsKey(bufferName)) {

      byte[] bytes = new byte[buffer.limit()];
      int idx = 0;
      while (buffer.hasRemaining()) bytes[idx++] = buffer.get();

      // Reset buffer position after draining, so it can be reused.
      buffer.flip();

      return bytes;
    }

    throw new TileDBError("ByteBuffer does not exist for attribute: " + bufferName);
  }

  /**
   * Drains a variable-sized buffer and returns its offsets as a byte[] Array
   *
   * @param bufferName The attribute name
   * @return The byte[] array
   * @throws TileDBError A TileDB exception
   */
  public long[] getOffsetArray(String bufferName) throws TileDBError {
    Pair<ByteBuffer, ByteBuffer> buffer = this.byteBuffers_.get(bufferName);
    if (byteBuffers_.containsKey(bufferName)) {
      LongBuffer offsets = null;
      if (buffer.getFirst() != null) {
        offsets = buffer.getFirst().asLongBuffer();

        long[] offsetArr = new long[offsets.limit()];
        int idx = 0;
        while (offsets.hasRemaining()) offsetArr[idx++] = offsets.get();

        return offsetArr;
      }
    }

    throw new TileDBError("ByteBuffer does not exist for attribute: " + bufferName);
  }

  /** @param attribute */
  public short[] getValidityByteMap(String attribute) throws TileDBError {
    if (this.validityByteMaps_.containsKey(attribute)) {
      int nelements =
          this.validityByteMapSizes_
              .get(attribute)
              .getitem(0)
              .divide(BigInteger.valueOf(Datatype.TILEDB_UINT8.getNativeSize()))
              .intValue();
      return (short[]) this.validityByteMaps_.get(attribute).toJavaArray(nelements);
    }

    throw new TileDBError("Attribute " + attribute + " is not nullable");
  }

  /**
   * Returns the result size estimate for each attribute/dimension
   *
   * @return A HashMap with Pairs, where the first value of the pair is the estimated size of the
   *     offsets (in case of variable-sized attributes) and the second value represents the data
   *     size estimate.
   * @throws TileDBError
   */
  public HashMap<String, Pair<Long, Long>> getResultEstimations() throws TileDBError {
    HashMap<String, Pair<Long, Long>> estimations = new HashMap<>();
    String name;
    try (ArraySchema schema = this.array.getSchema();
        Domain domain = schema.getDomain(); ) {
      for (Dimension dimension : domain.getDimensions()) {
        name = dimension.getName();
        if (dimension.isVar()) {
          estimations.put(name, this.getEstResultSizeVar(ctx, name));
        } else {
          estimations.put(name, new Pair<>(null, this.getEstResultSize(ctx, name)));
        }
      }

      for (Attribute attribute : schema.getAttributes().values()) {
        name = attribute.getName();
        if (attribute.isVar()) {
          estimations.put(name, this.getEstResultSizeVar(ctx, name));
        } else {
          estimations.put(name, new Pair<>(null, this.getEstResultSize(ctx, name)));
        }
      }
    }

    return estimations;
  }

  /**
   * Flushes all internal state of a query object and finalizes the query. This is applicable only
   * to global layout writes. It has no effect for any other query type.
   *
   * @return Finalized query instance
   * @throws TileDBError A TileDB excdeption
   */
  public Query finalizeQuery() throws TileDBError {
    ctx.handleError(tiledb.tiledb_query_finalize(ctx.getCtxp(), queryp));
    return this;
  }

  /**
   * Retrieves the number of written fragments. Applicable only to WRITE queries.
   *
   * @return The number of written fragments
   * @throws TileDBError
   */
  public long getFragmentNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int fragmentNum = tiledb.new_uintp();
    ctx.handleError(tiledb.tiledb_query_get_fragment_num(ctx.getCtxp(), queryp, fragmentNum));

    return tiledb.uintp_value(fragmentNum);
  }

  /**
   * Retrieves the URI of the written fragment with the input index. Applicable only to WRITE
   * queries.
   *
   * @return The URI
   * @throws TileDBError
   */
  public String getFragmentURI(BigInteger idx) throws TileDBError {
    SWIGTYPE_p_p_char uri = tiledb.new_charpp();
    ctx.handleError(tiledb.tiledb_query_get_fragment_uri(ctx.getCtxp(), queryp, idx, uri));

    return tiledb.charpp_value(uri);
  }

  public Pair<Long, Long> getFragmentTimestampRange(BigInteger idx) throws TileDBError {
    SWIGTYPE_p_unsigned_long_long t1 = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long t2 = tiledb.new_ullp();
    ctx.handleError(
        tiledb.tiledb_query_get_fragment_timestamp_range(ctx.getCtxp(), queryp, idx, t1, t2));

    return new Pair(tiledb.ullp_value(t1), tiledb.ullp_value(t2));
  }

  // Default noop async completion callback
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

  protected SWIGTYPE_p_tiledb_query_t getQueryp() {
    return queryp;
  }

  /**
   * @return Retrieves the stats from a Query.
   * @exception TileDBError A TileDB exception
   */
  public String getStats() throws TileDBError {
    String stats;
    SWIGTYPE_p_p_char statspp = tiledb.new_charpp();
    try {
      ctx.handleError(tiledb.tiledb_query_get_stats(ctx.getCtxp(), getQueryp(), statspp));
      stats = tiledb.charpp_value(statspp);
    } finally {
      tiledb.delete_charpp(statspp);
    }

    return stats;
  }

  /**
   * TODO
   *
   * @param queryCondition
   * @throws TileDBError
   */
  public void setCondition(QueryCondition queryCondition) throws TileDBError {
    SWIGTYPE_p_tiledb_query_condition_t condition_t = queryCondition.getConditionp();
    try {
      ctx.handleError(tiledb.tiledb_query_set_condition(ctx.getCtxp(), getQueryp(), condition_t));
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * @return Retrieves the config from a Query.
   * @exception TileDBError A TileDB exception
   */
  public Config getConfig() throws TileDBError {
    SWIGTYPE_p_p_tiledb_config_t configpp = tiledb.new_tiledb_config_tpp();
    Config config;
    try {
      ctx.handleError(tiledb.tiledb_query_get_config(ctx.getCtxp(), this.queryp, configpp));
      config = new Config(configpp);
    } finally {
      tiledb.delete_tiledb_config_tpp(configpp);
    }

    return config;
  }

  /** Free's native TileDB resources associated with the Query object */
  @Override
  public synchronized void close() {
    if (queryp != null) {
      for (Pair<uint64_tArray, uint64_tArray> size_pair : buffer_sizes_.values()) {
        size_pair.getFirst().delete();
        size_pair.getSecond().delete();
      }
      for (Pair<NativeArray, NativeArray> buffer : buffers_.values()) {
        if (buffer.getFirst() != null) buffer.getFirst().close();
        if (buffer.getSecond() != null) buffer.getSecond().close();
      }
      if (subarray != null) {
        subarray.close();
      }
      tiledb.tiledb_query_free(querypp);
      queryp = null;
    }
  }
}
