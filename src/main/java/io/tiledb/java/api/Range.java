package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;

public class Range implements AutoCloseable {
  private tiledb_range_t range;
  private Domain domain;
  private Context ctx;
  private Long dimIdx;
  private NativeArray min, max;

  /**
   * Constructor
   *
   * @param ctx The TileDB context
   * @param domain The array domain
   * @param dimIdx The dimension index to add the range to
   */
  public Range(Context ctx, Domain domain, long dimIdx) {
    this.domain = domain;
    this.ctx = ctx;
    this.range = new tiledb_range_t();
    this.dimIdx = dimIdx;
  }

  /**
   * Constructor
   *
   * @param ctx The TileDB context
   * @param range C pointer to the range
   * @param domain The array domain
   * @param dimIdx The dimension index to add the range to
   */
  public Range(Context ctx, tiledb_range_t range, Domain domain, long dimIdx) {
    this.ctx = ctx;
    this.range = range;
    this.domain = domain;
    this.dimIdx = dimIdx;
  }

  /**
   * Sets the min value for this range
   *
   * @param min The min value
   * @throws TileDBError
   */
  public synchronized void setMin(Object min) throws TileDBError {
    Datatype dimType;

    dimType = domain.getDimension(dimIdx).getType();

    // We use java type check here because we can not tell the difference between unsigned and
    // signed
    // values coming from java, i.e. A UINT16 and INT32 are both Integer classes in java.
    Types.javaTypeCheck(min.getClass(), dimType.javaClass());

    NativeArray minArr = new NativeArray(ctx, 1, dimType);
    minArr.setItem(0, min);
    if (!dimType.isStringType()) {
      setMinSize(BigInteger.valueOf(dimType.getNativeSize()));
    }
    range.setMin(minArr.toVoidPointer());
  }

  /**
   * Sets the min size
   *
   * @param size The size
   */
  public synchronized void setMinSize(long size) {
    range.setMin_size(BigInteger.valueOf(size));
  }

  /**
   * Sets the min size
   *
   * @param size The size
   */
  public synchronized void setMinSize(BigInteger size) {
    range.setMin_size(size);
  }

  /**
   * Retrieves the min value size
   *
   * @return The size as a long
   */
  public synchronized long getMinSize() {
    return range.getMin_size().longValue();
  }

  /**
   * Retrieves the min value size
   *
   * @return The size as a BigInteger
   */
  public synchronized BigInteger getMinSizeBI() {
    return range.getMin_size();
  }

  /**
   * Sets the max value for this range
   *
   * @param max The max value
   * @throws TileDBError
   */
  public synchronized void setMax(Object max) throws TileDBError {
    Datatype dimType;

    dimType = domain.getDimension(dimIdx).getType();

    // We use java type check here because we can not tell the difference between unsigned and
    // signed
    // values coming from java, i.e. A UINT16 and INT32 are both Integer classes in java.
    Types.javaTypeCheck(max.getClass(), dimType.javaClass());

    NativeArray maxArr = new NativeArray(ctx, 1, dimType);
    maxArr.setItem(0, max);

    if (!dimType.isStringType()) {
      setMaxSize(BigInteger.valueOf(dimType.getNativeSize()));
    }
    range.setMax(maxArr.toVoidPointer());
  }

  /**
   * Sets the max size
   *
   * @param size The size
   */
  public synchronized void setMaxSize(long size) {
    range.setMax_size(BigInteger.valueOf(size));
  }

  /**
   * Sets the max size
   *
   * @param size The size
   */
  public synchronized void setMaxSize(BigInteger size) {
    range.setMax_size(size);
  }

  /**
   * Retrieves the max value size
   *
   * @return The size as a long
   */
  public synchronized long getMaxSize() {
    return range.getMax_size().longValue();
  }

  /**
   * Retrieves the max value size
   *
   * @return The size as a BigInteger
   */
  public synchronized BigInteger getMaxSizeBI() {
    return range.getMax_size();
  }

  /**
   * Returns the min range value
   *
   * @return the value
   * @throws TileDBError
   */
  public synchronized Object getMin() throws TileDBError {
    Datatype dimType;
    try (Dimension dim = domain.getDimension(dimIdx)) {
      dimType = dim.getType();
      SWIGTYPE_p_void minp = range.getMin();
      Object ret;
      min = new NativeArray(ctx, dimType, minp, 1);
      ret = min.getItem(0);
      return ret;
    }
  }

  /**
   * Returns the min range value for var dims
   *
   * @return The value
   * @throws TileDBError
   */
  public synchronized String getMinVar() throws TileDBError {
    SWIGTYPE_p_void minp = range.getMin();
    min = new NativeArray(ctx, Datatype.TILEDB_BLOB, minp, (int) getMinSize());
    return new String((byte[]) min.toJavaArray());
  }

  /**
   * Returns the max range value
   *
   * @return the max value
   * @throws TileDBError
   */
  public synchronized Object getMax() throws TileDBError {
    Datatype dimType;
    try (Dimension dim = domain.getDimension(dimIdx)) {
      dimType = dim.getType();
      SWIGTYPE_p_void maxp = range.getMax();
      Object ret;
      max = new NativeArray(ctx, dimType, maxp, 1);
      ret = max.getItem(0);
      return ret;
    }
  }

  /**
   * Returns the max value fo a var dim
   *
   * @return The max value
   * @throws TileDBError
   */
  public synchronized String getMaxVar() throws TileDBError {
    SWIGTYPE_p_void maxp = range.getMax();
    max = new NativeArray(ctx, Datatype.TILEDB_BLOB, maxp, (int) getMaxSize());
    return new String((byte[]) max.toJavaArray());
  }

  /**
   * Returns the c pointer fot his range
   *
   * @return The c pointer
   */
  public tiledb_range_t getRange_t() {
    return range;
  }

  @Override
  public void close() {
    range.delete();
  }
}
