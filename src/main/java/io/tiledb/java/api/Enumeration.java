package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;

public class Enumeration implements AutoCloseable {
  private Context ctx;
  private SWIGTYPE_p_tiledb_enumeration_t enumerationp;
  private SWIGTYPE_p_p_tiledb_enumeration_t enumerationpp;
  private String name;
  private long cellValNumber = -1;
  private Datatype datatype;

  protected Enumeration(Context ctx, SWIGTYPE_p_p_tiledb_enumeration_t enumerationpp) {
    this.ctx = ctx;
    this.enumerationp = tiledb.tiledb_enumeration_tpp_value(enumerationpp);
    this.enumerationpp = enumerationpp;
  }

  protected Enumeration(
      Context ctx,
      String name,
      long cellValNum,
      Datatype datatype,
      boolean ordered,
      NativeArray data,
      BigInteger dataSize,
      NativeArray offsets,
      BigInteger offsetsSize)
      throws TileDBError {
    this.datatype = datatype;
    this.name = name;
    this.cellValNumber = cellValNum;
    int ord = 0;
    if (ordered) ord = 1;
    SWIGTYPE_p_p_tiledb_enumeration_t enumerationpp = tiledb.new_tiledb_enumeration_tpp();
    try {

      if (offsets == null) {
        ctx.handleError(
            tiledb.tiledb_enumeration_alloc(
                ctx.getCtxp(),
                name,
                datatype.toSwigEnum(),
                cellValNum,
                ord,
                data.toVoidPointer(),
                dataSize,
                null,
                offsetsSize,
                enumerationpp));
      } else {
        ctx.handleError(
            tiledb.tiledb_enumeration_alloc(
                ctx.getCtxp(),
                name,
                datatype.toSwigEnum(),
                cellValNum,
                ord,
                data.toVoidPointer(),
                dataSize,
                offsets.toVoidPointer(),
                offsetsSize,
                enumerationpp));
      }

    } catch (TileDBError err) {
      tiledb.delete_tiledb_enumeration_tpp(enumerationpp);
      throw err;
    }
    this.ctx = ctx;
    this.enumerationp = tiledb.tiledb_enumeration_tpp_value(enumerationpp);
    this.enumerationpp = enumerationpp;
  }

  /**
   * Returns the enumeration object pointer
   *
   * @return the enumeration object object pointer
   */
  public SWIGTYPE_p_tiledb_enumeration_t getEnumerationp() {
    return enumerationp;
  }

  /**
   * Return the cell value number of the enumeration values
   *
   * @return The cell value number of the enumeration values
   * @throws TileDBError
   */
  public long getCellValNum() throws TileDBError {
    if (this.cellValNumber != -1) return this.cellValNumber;
    long cellValNumber;
    SWIGTYPE_p_unsigned_int nump = tiledb.new_uintp();
    try {
      ctx.handleError(
          tiledb.tiledb_enumeration_get_cell_val_num(ctx.getCtxp(), getEnumerationp(), nump));
      cellValNumber = tiledb.uintp_value(nump);
    } finally {
      tiledb.delete_uintp(nump);
    }
    return cellValNumber;
  }

  /**
   * Return whether the enumeration values should be considered ordered
   *
   * @return Whether the enumeration values should be considered ordered
   * @throws TileDBError
   */
  public boolean getOrdered() throws TileDBError {
    SWIGTYPE_p_int isOrdered = tiledb.new_intp();
    try {
      ctx.handleError(
          tiledb.tiledb_enumeration_get_ordered(ctx.getCtxp(), getEnumerationp(), isOrdered));
      return tiledb.intp_value(isOrdered) > 0;
    } finally {
      tiledb.delete_intp(isOrdered);
    }
  }

  /**
   * Extend an Enumeration.
   *
   * @param data A pointer to the enumeration value data to add.
   * @param dataSize The length of the data buffer provided.
   * @param offsets A pointer to the offsets buffer if enumeration is var sized.
   * @param offsetsSize The length of the offsets buffer, zero if no offsets.
   */
  public Enumeration extend(
      NativeArray data, BigInteger dataSize, NativeArray offsets, BigInteger offsetsSize) {
    SWIGTYPE_p_p_tiledb_enumeration_t enumeration_t = tiledb.new_tiledb_enumeration_tpp();

    try {
      ctx.handleError(
          tiledb.tiledb_enumeration_extend(
              ctx.getCtxp(),
              this.getEnumerationp(),
              data.toVoidPointer(),
              dataSize,
              offsets.toVoidPointer(),
              offsetsSize,
              enumeration_t));
    } catch (TileDBError e) {
      tiledb.delete_tiledb_enumeration_tpp(enumeration_t);
    }
    return new Enumeration(ctx, enumeration_t);
  }

  /**
   * Return the datatype of the enumeration values
   *
   * @return The datatype of the enumeration values
   * @throws TileDBError
   */
  public Datatype getType() throws TileDBError {
    if (datatype != null) return datatype;
    SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
    try {
      ctx.handleError(tiledb.tiledb_enumeration_get_type(ctx.getCtxp(), getEnumerationp(), typep));
      return Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(typep));
    } finally {
      tiledb.delete_tiledb_datatype_tp(typep);
    }
  }

  /**
   * Return the name of the enumeration values
   *
   * @return The name of the enumeration values
   * @throws TileDBError
   */
  public String getName() throws TileDBError {
    if (this.name != null) return this.name;
    SWIGTYPE_p_p_tiledb_string_handle_t name = tiledb.new_tiledb_string_handle_tpp();
    TileDBString ts = null;

    try {
      ctx.handleError(tiledb.tiledb_enumeration_get_name(ctx.getCtxp(), enumerationp, name));
      ts = new TileDBString(ctx, name);
      return ts.getView().getFirst();
    } finally {
      if (ts != null) ts.close();
    }
  }

  /**
   * Return an Object of the enumerations underlying value data
   *
   * @return The enumeration data
   * @throws TileDBError
   */
  public Object getData() throws TileDBError {
    SWIGTYPE_p_p_void datapp = tiledb.new_voidpArray(0);
    SWIGTYPE_p_unsigned_long_long size = tiledb.new_ullp();
    try {
      ctx.handleError(
          tiledb.tiledb_enumeration_get_data(ctx.getCtxp(), getEnumerationp(), datapp, size));
      // TODO revisit casting here, needs a warning. revisit throughout the API
      int byteSize = tiledb.ullp_value(size).intValue();
      Datatype type = this.getType();
      int numElements = byteSize / type.getNativeSize();
      return new NativeArray(ctx, type, datapp, numElements).toJavaArray();
    } finally {
      tiledb.delete_ullp(size);
      tiledb.delete_voidpArray(datapp);
    }
  }

  /**
   * Return an Object of the enumerations underlying value offsets
   *
   * @return The enumeration offsets
   * @throws TileDBError
   */
  public Object getOffsets() throws TileDBError {
    SWIGTYPE_p_p_void datapp = tiledb.new_voidpArray(0);
    SWIGTYPE_p_unsigned_long_long size = tiledb.new_ullp();

    try {
      ctx.handleError(
          tiledb.tiledb_enumeration_get_offsets(ctx.getCtxp(), getEnumerationp(), datapp, size));
      // TODO revisit casting here, needs a warning. revisit throughout the API
      int byteSize = tiledb.ullp_value(size).intValue();
      Datatype type = this.getType();
      int numElements = byteSize / type.getNativeSize();
      return new NativeArray(ctx, type, datapp, numElements).toJavaArray();
    } finally {
      tiledb.delete_voidpArray(datapp);
      tiledb.delete_ullp(size);
    }
  }

  /** @return A String representation for the Attribute. */
  @Override
  public String toString() {
    SWIGTYPE_p_p_tiledb_string_handle_t dump = tiledb.new_tiledb_string_handle_tpp();
    TileDBString ts = null;

    try {
      ctx.handleError(tiledb.tiledb_enumeration_dump_str(ctx.getCtxp(), enumerationp, dump));
      ts = new TileDBString(ctx, dump);
      return ts.getView().getFirst();
    } catch (TileDBError error) {
      return "Dump not available";
    } finally {
      if (ts != null) ts.close();
    }
  }

  /** Releases resources */
  public void close() {
    if (enumerationp != null && enumerationpp != null) {
      tiledb.tiledb_enumeration_free(enumerationpp);
      tiledb.delete_tiledb_enumeration_tpp(enumerationpp);
      enumerationpp = null;
      enumerationp = null;
    }
  }
}
