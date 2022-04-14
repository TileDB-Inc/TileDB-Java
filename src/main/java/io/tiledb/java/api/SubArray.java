package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_subarray_t;
import io.tiledb.libtiledb.SWIGTYPE_p_p_void;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_subarray_t;
import io.tiledb.libtiledb.SWIGTYPE_p_unsigned_long_long;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.uint64_tArray;
import java.math.BigInteger;

public class SubArray implements AutoCloseable {
  private SWIGTYPE_p_tiledb_subarray_t subArrayp;
  private SWIGTYPE_p_p_tiledb_subarray_t subArraypp;

  private Context ctx;
  private Array array;

  public SubArray(Context ctx, Array array) throws TileDBError {
    this.ctx = ctx;
    this.array = array;
    subArraypp = tiledb.new_tiledb_subarray_tpp();
    try {
      ctx.handleError(tiledb.tiledb_subarray_alloc(ctx.getCtxp(), array.getArrayp(), subArraypp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_subarray_tpp(subArraypp);
      throw err;
    }
    subArrayp = tiledb.tiledb_subarray_tpp_value(subArraypp);
  }

  /**
   * Getter for subArray pointer.
   *
   * @return the subarray pointer
   */
  public SWIGTYPE_p_tiledb_subarray_t getSubArrayp() {
    return subArrayp;
  }

  /**
   * Set the query config.
   *
   * <p>Setting configuration with this function overrides the following Query-level parameters
   * only:
   *
   * <p>- `sm.memory_budget` - `sm.memory_budget_var` - `sm.sub_partitioner_memory_budget` -
   * `sm.var_offsets.mode` - `sm.var_offsets.extra_element` - `sm.var_offsets.bitsize` -
   * `sm.check_coord_dups` - `sm.check_coord_oob` - `sm.check_global_order` - `sm.dedup_coords`
   *
   * @param config The input configuration
   */
  public void setConfig(Config config) throws TileDBError {
    try {
      ctx.handleError(
          tiledb.tiledb_subarray_set_config(ctx.getCtxp(), this.subArrayp, config.getConfigp()));
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * Adds a 1D range along a subarray dimension index, which is in the form (start, end, stride).
   * The datatype of the range components must be the same as the type of the domain of the array in
   * the query.
   *
   * @param dimIdx The index of the dimension to add the range to
   * @param start The range start
   * @param end The range end
   * @param stride The range stride
   * @return This subArray
   *     <p>Note: The stride is currently unsupported. Use 0/NULL/nullptr as the stride argument.
   * @throws TileDBError A TileDB exception
   */
  public synchronized SubArray addRange(int dimIdx, Object start, Object end, Object stride)
      throws TileDBError {
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
          tiledb.tiledb_subarray_add_range(
              ctx.getCtxp(),
              subArrayp,
              dimIdx,
              startArr.toVoidPointer(),
              endArr.toVoidPointer(),
              null));
    }

    return this;
  }

  /**
   * Adds point ranges to the given dimension index of the subarray Effectively `add_range(x_i,
   * x_i)` for `count` points in the target array, but set in bulk to amortize expensive steps.
   *
   * @param dimIdx The dimension index
   * @param start The range start
   * @param count Number of ranges to add
   * @return This query
   * @throws TileDBError A TileDB exception
   */
  public synchronized SubArray addPointRanges(int dimIdx, Object start, BigInteger count)
      throws TileDBError {
    Datatype dimType;
    int values[];
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(dimIdx).getType();
      values = (int[]) start;
    }

    try (NativeArray arr = new NativeArray(ctx, values.length, dimType)) {
      int i = 0;
      for (int value : values) {
        arr.setItem(i, value);
        i++;
      }
      ctx.handleError(
          tiledb.tiledb_subarray_add_point_ranges(
              ctx.getCtxp(), subArrayp, dimIdx, arr.toVoidPointer(), count));
    }
    return this;
  }

  /**
   * Adds a 1D range along a subarray dimension index, which is in the form (start, end, stride).
   * The datatype of the range components must be the same as the type of the domain of the array in
   * the query.
   *
   * @param name The name of the dimension to add the range to
   * @param start The range start
   * @param end The range end
   * @param stride The range stride
   * @return This subArray
   *     <p>Note: The stride is currently unsupported. Use 0/NULL/nullptr as the stride argument.
   * @throws TileDBError A TileDB exception
   */
  public synchronized SubArray addRangeByName(String name, Object start, Object end, Object stride)
      throws TileDBError {
    Datatype dimType;
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(name).getType();
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
          tiledb.tiledb_subarray_add_range_by_name(
              ctx.getCtxp(),
              subArrayp,
              name,
              startArr.toVoidPointer(),
              endArr.toVoidPointer(),
              null));
    }

    return this;
  }

  /**
   * Adds a 1D string range along a subarray dimension index, in the form (start, end). Applicable
   * only to variable-sized dimensions
   *
   * @param dimIdx The index of the dimension to add the range to
   * @param start The range start
   * @param end The range end
   * @return This query
   * @throws TileDBError A TileDB exception
   */
  public synchronized SubArray addRangeVar(int dimIdx, String start, String end)
      throws TileDBError {
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
          tiledb.tiledb_subarray_add_range_var(
              ctx.getCtxp(),
              subArrayp,
              dimIdx,
              startArr.toVoidPointer(),
              BigInteger.valueOf(start.length()),
              endArr.toVoidPointer(),
              BigInteger.valueOf(end.length())));
    }

    return this;
  }

  /**
   * Adds a 1D string range along a subarray dimension index, in the form (start, end). Applicable
   * only to variable-sized dimensions
   *
   * @param name The name of the dimension to add the range to
   * @param start The range start
   * @param end The range end
   * @return This query
   * @throws TileDBError A TileDB exception
   */
  public synchronized SubArray addRangeVarByName(String name, String start, String end)
      throws TileDBError {
    Datatype dimType;
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(name).getType();
    }

    Types.javaTypeCheck(start.getClass(), dimType.javaClass());
    Types.javaTypeCheck(end.getClass(), dimType.javaClass());

    try (NativeArray startArr = new NativeArray(ctx, 1, dimType);
        NativeArray endArr = new NativeArray(ctx, 1, dimType)) {
      startArr.setItem(0, start);
      endArr.setItem(0, end);

      ctx.handleError(
          tiledb.tiledb_subarray_add_range_var_by_name(
              ctx.getCtxp(),
              subArrayp,
              name,
              startArr.toVoidPointer(),
              BigInteger.valueOf(start.length()),
              endArr.toVoidPointer(),
              BigInteger.valueOf(end.length())));
    }

    return this;
  }

  /**
   * Retrieves the number of ranges for a given dimension index.
   *
   * @param dimIdx The index of the dimension whose range number to retrieve
   * @return The number of ranges of the dimension
   * @throws TileDBError A TileDB exception
   */
  public long getRangeNum(int dimIdx) throws TileDBError {
    uint64_tArray resultArr = new uint64_tArray(1);
    try {
      ctx.handleError(
          tiledb.tiledb_subarray_get_range_num(ctx.getCtxp(), subArrayp, dimIdx, resultArr.cast()));
      return resultArr.getitem(0).longValue();
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * Retrieves the number of ranges for a given dimension index.
   *
   * @param name The name of the dimension whose range number to retrieve
   * @return The number of ranges of the dimension
   * @throws TileDBError A TileDB exception
   */
  public long getRangeNumFromName(String name) throws TileDBError {
    uint64_tArray resultArr = new uint64_tArray(1);
    try {
      ctx.handleError(
          tiledb.tiledb_subarray_get_range_num_from_name(
              ctx.getCtxp(), subArrayp, name, resultArr.cast()));
      return resultArr.getitem(0).longValue();
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * Retrieves a specific range of the subarray along a given dimension.
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
          tiledb.tiledb_subarray_get_range(
              ctx.getCtxp(),
              subArrayp,
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
   * Retrieves a specific range of the subarray along a given dimension.
   *
   * @param name The name of the dimension to retrieve the range from
   * @param rangeIdx The index of the range to retrieve
   * @return Pair of (start, end) of the range.
   * @throws TileDBError A TileDB exception
   */
  public Pair<Object, Object> getRangeFromName(String name, long rangeIdx) throws TileDBError {
    Datatype dimType;
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(name).getType();
    }

    SWIGTYPE_p_p_void startArrpp = tiledb.new_voidpArray(1);
    SWIGTYPE_p_p_void endArrpp = tiledb.new_voidpArray(1);
    SWIGTYPE_p_p_void strideArrpp = tiledb.new_voidpArray(1);

    try {
      ctx.handleError(
          tiledb.tiledb_subarray_get_range_from_name(
              ctx.getCtxp(),
              subArrayp,
              name,
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
   * Retrieves a range for a given variable length string dimension index and range id.
   *
   * @param dimIdx The index of the dimension to get the range from
   * @return This subArray
   * @throws TileDBError A TileDB exception
   */
  public synchronized Pair<Long, Long> getRangeVarSize(int dimIdx, BigInteger rangeIdx)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();
    try {
      ctx.handleError(
          tiledb.tiledb_subarray_get_range_var_size(
              ctx.getCtxp(), subArrayp, dimIdx, rangeIdx, startSize, endSize));

      return new Pair(
          tiledb.ullp_value(startSize).longValue(), tiledb.ullp_value(endSize).longValue());
    } catch (TileDBError error) {
      throw error;
    }
  }

  /**
   * Retrieves a range for a given variable length string dimension index and range id.
   *
   * @param name The index of the dimension to get the range from
   * @return This subArray
   * @throws TileDBError A TileDB exception
   */
  public synchronized Pair<Long, Long> getRangeVarSizeByName(String name, BigInteger rangeIdx)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();
    try {
      ctx.handleError(
          tiledb.tiledb_subarray_get_range_var_size_from_name(
              ctx.getCtxp(), subArrayp, name, rangeIdx, startSize, endSize));

      return new Pair(
          tiledb.ullp_value(startSize).longValue(), tiledb.ullp_value(endSize).longValue());
    } catch (TileDBError error) {
      throw error;
    }
  }

  /**
   * Retrieves a range's start and end size for a given variable-length dimension index at a given
   * range index.
   *
   * @param dimIdx The index of the dimension to get the range from
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
          tiledb.tiledb_subarray_get_range_var(
              ctx.getCtxp(),
              subArrayp,
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
   * Retrieves a range's start and end size for a given variable-length dimension index at a given
   * range index.
   *
   * @param name The index of the dimension to get the range from
   * @return This query
   * @throws TileDBError A TileDB exception
   */
  public synchronized Pair<String, String> getRangeVarByName(String name, BigInteger rangeIdx)
      throws TileDBError {
    Datatype dimType;
    try (ArraySchema schema = array.getSchema();
        Domain domain = schema.getDomain()) {
      dimType = domain.getDimension(name).getType();
    }

    Pair<Long, Long> size = this.getRangeVarSizeByName(name, rangeIdx);

    try (NativeArray startArr = new NativeArray(ctx, size.getFirst().intValue(), dimType);
        NativeArray endArr = new NativeArray(ctx, size.getSecond().intValue(), dimType)) {

      ctx.handleError(
          tiledb.tiledb_subarray_get_range_var_from_name(
              ctx.getCtxp(),
              subArrayp,
              name,
              rangeIdx,
              startArr.toVoidPointer(),
              endArr.toVoidPointer()));

      Object start = new String((byte[]) startArr.toJavaArray());
      Object end = new String((byte[]) endArr.toJavaArray());
      return new Pair(start, end);
    }
  }

  /**
   * Sets coalesce_ranges flag, intended for use by CAPI, to alloc matching default
   * coalesce-ranges=true semantics of internal class constructor, but giving capi clients ability
   * to turn off if desired.
   *
   * @param flag boolean input flag
   * @throws TileDBError
   */
  public void setCoalesceRanges(boolean flag) throws TileDBError {
    short coalesce = flag ? (short) 1 : (short) 0;

    try {
      ctx.handleError(
          tiledb.tiledb_subarray_set_coalesce_ranges(ctx.getCtxp(), this.subArrayp, coalesce));
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * Sets a subarray, defined in the order dimensions were added. Coordinates are inclusive.
   *
   * @param subarray The targeted subarray.
   * @exception TileDBError A TileDB exception
   */
  public synchronized SubArray setSubarray(NativeArray subarray) throws TileDBError {
    Types.typeCheck(subarray.getNativeType(), array.getSchema().getDomain().getType());
    try {
      ctx.handleError(
          tiledb.tiledb_subarray_set_subarray(ctx.getCtxp(), subArrayp, subarray.toVoidPointer()));
      return this;
    } catch (TileDBError err) {
      throw err;
    }
  }

  @Override
  public void close() throws Exception {
    if (subArrayp != null && subArraypp != null) {
      tiledb.tiledb_subarray_free(subArraypp);
      subArrayp = null;
      subArraypp = null;
      if (array != null) {
        array.close();
      }
    }
  }
}