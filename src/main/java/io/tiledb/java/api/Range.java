package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_range_t;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_range_t;

public class Range {
  private SWIGTYPE_p_p_tiledb_range_t rangepp;
  protected boolean swigCMemOwn;
  private tiledb_range_t range;

  private long swigCPtr;
  private Domain domain;
  private Context ctx;

  public Range(Context cxt, Domain domain) throws TileDBError {
    this.domain = domain;
    this.ctx = cxt;
    this.rangepp = tiledb.new_tiledb_range_tpp();
    this.swigCPtr = SWIGTYPE_p_p_tiledb_range_t.getCPtr(rangepp);
    this.range = new tiledb_range_t(swigCPtr, true);
  }

  public void setMin(long dimIdx, Object min) throws TileDBError {
    Datatype dimType;

    dimType = domain.getDimension(dimIdx).getType();

    // We use java type check here because we can not tell the difference between unsigned and
    // signed
    // values coming from java, i.e. A UINT16 and INT32 are both Integer classes in java.
    Types.javaTypeCheck(min.getClass(), dimType.javaClass());

    try (NativeArray minArr = new NativeArray(ctx, 1, dimType)) {
      minArr.setItem(0, min);

      range.setMin(minArr.toVoidPointer());
      //      range.setMin_size(BigInteger.valueOf(dimType.getNativeSize()));

    }
  }

  public void setMax(long dimIdx, Object max) throws TileDBError {
    Datatype dimType;

    dimType = domain.getDimension(dimIdx).getType();

    // We use java type check here because we can not tell the difference between unsigned and
    // signed
    // values coming from java, i.e. A UINT16 and INT32 are both Integer classes in java.
    Types.javaTypeCheck(max.getClass(), dimType.javaClass());

    try (NativeArray minArr = new NativeArray(ctx, 1, dimType)) {
      minArr.setItem(0, max);

      range.setMin(minArr.toVoidPointer());
      //      range.setMin_size(BigInteger.valueOf(dimType.getNativeSize()));

    }
  }
}
