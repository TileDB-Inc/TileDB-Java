package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;

public class Range implements AutoCloseable {
  private tiledb_range_t range;
  private Domain domain;
  private Context ctx;
  private Long dimIdx;
  private NativeArray min, max;

  public Range(Context cxt, Domain domain, long dimIdx) {
    this.domain = domain;
    this.ctx = cxt;
    this.range = new tiledb_range_t();
    this.dimIdx = dimIdx;
  }

  public Range(Context ctx, tiledb_range_t range, Domain domain, long dimIdx) {
    this.ctx = ctx;
    this.range = range;
    this.domain = domain;
    this.dimIdx = dimIdx;
  }

  /**
   * @param min
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
    range.setMin_size(BigInteger.valueOf(dimType.getNativeSize()));
    range.setMin(minArr.toVoidPointer());
  }

  /** @param size */
  public synchronized void setMinSize(long size) {
    range.setMin_size(BigInteger.valueOf(size));
  }

  /** @param size */
  public synchronized void setMinSize(BigInteger size) { // change to bigint
    range.setMin_size(size);
  }

  /** @return */
  public synchronized long getMinSize() {
    return range.getMin_size().longValue();
  }

  /** @return */
  public synchronized BigInteger getMinSizeBI() {
    return range.getMin_size();
  }

  /**
   * @param max
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

    range.setMax_size(BigInteger.valueOf(dimType.getNativeSize()));
    range.setMax(maxArr.toVoidPointer());
  }

  /** @param size */
  public synchronized void setMaxSize(long size) {
    range.setMax_size(BigInteger.valueOf(size));
  }

  /** @param size */
  public synchronized void setMaxSize(BigInteger size) {
    range.setMax_size(size);
  }

  /** @return */
  public synchronized long getMaxSize() {
    return range.getMax_size().longValue();
  }

  /** @return */
  public synchronized BigInteger getMaxSizeBI() {
    return range.getMax_size();
  }

  /**
   * @return
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
   * @return
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

  /** @return */
  public tiledb_range_t getRange_t() {
    return range;
  }

  @Override
  public void close() {
    range.delete();
  }
}
