package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_char;
import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_dimension_label_handle_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_data_order_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_datatype_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_dimension_label_handle_t;
import io.tiledb.libtiledb.SWIGTYPE_p_unsigned_int;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_data_order_t;
import io.tiledb.libtiledb.tiledb_datatype_t;
import jdk.jfr.Experimental;

@Experimental
public class DimensionLabel implements AutoCloseable {
  private Context ctx;
  private SWIGTYPE_p_tiledb_dimension_label_handle_t dimlabelp;
  private SWIGTYPE_p_p_tiledb_dimension_label_handle_t dimlabelpp;

  /** The index of the dimension the labels are applied to */
  private long dimIndex = -1;

  /** The name of the dimension label */
  private String name;

  /** The ordering/sort of the label data */
  private tiledb_data_order_t labelOrder;

  /** The datatype of the label data */
  private tiledb_datatype_t labelType;

  /** Constructor */
  public DimensionLabel(
      Context ctx,
      long dimIndex,
      String name,
      tiledb_data_order_t labelOrder,
      tiledb_datatype_t labelType) {
    this.ctx = ctx;
    this.dimIndex = dimIndex;
    this.name = name;
    this.labelOrder = labelOrder;
    this.labelType = labelType;
  }

  /** Constructor */
  protected DimensionLabel(Context ctx, SWIGTYPE_p_p_tiledb_dimension_label_handle_t dimlabelpp) {
    this.ctx = ctx;
    this.dimlabelp = tiledb.tiledb_dimension_label_tpp_value(dimlabelpp);
    this.dimlabelpp = dimlabelpp;
  }

  protected SWIGTYPE_p_tiledb_dimension_label_handle_t getDimLabelp() {
    return this.dimlabelp;
  }

  protected Context getCtx() {
    return this.ctx;
  }

  /**
   * Returns the index of the dimension the DimensionLabel provides labels for
   *
   * @return The dimension index
   */
  public long getDimensionIndex() throws TileDBError {
    if (this.dimIndex != -1) return dimIndex;

    long result;
    SWIGTYPE_p_unsigned_int indexp = tiledb.new_uintp();
    try {
      ctx.handleError(
          tiledb.tiledb_dimension_label_get_dimension_index(ctx.getCtxp(), this.dimlabelp, indexp));
      result = tiledb.uintp_value(indexp);
    } finally {
      tiledb.delete_uintp(indexp);
    }
    return result;
  }

  /**
   * Returns the name of the DimensionLabel
   *
   * @return The name of the DimensionLabel
   */
  public String getName() throws TileDBError {
    if (this.name != null) return name;

    String name;
    SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
    try {
      ctx.handleError(
          tiledb.tiledb_dimension_label_get_name(ctx.getCtxp(), this.dimlabelp, namepp));
      name = tiledb.charpp_value(namepp);
    } finally {
      tiledb.delete_charpp(namepp);
    }
    return name;
  }

  /**
   * Returns the name of the attribute the label data is stored on
   *
   * @return The name of the attribute
   */
  public String getLabelAttrName() throws TileDBError {
    String name;
    SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
    try {
      ctx.handleError(
          tiledb.tiledb_dimension_label_get_label_attr_name(ctx.getCtxp(), this.dimlabelp, namepp));
      name = tiledb.charpp_value(namepp);
    } finally {
      tiledb.delete_charpp(namepp);
    }
    return name;
  }

  /**
   * Returns the number of values per cell for the labels on the dimension label For variable-sized
   * labels the result is TILEDB_VAR_NUM
   *
   * @return The number of values per cell
   */
  public long getLabelCellValNum() throws TileDBError {
    long result;
    SWIGTYPE_p_unsigned_int nump = tiledb.new_uintp();
    try {
      ctx.handleError(
          tiledb.tiledb_dimension_label_get_label_cell_val_num(
              ctx.getCtxp(), this.dimlabelp, nump));
      result = tiledb.uintp_value(nump);
    } finally {
      tiledb.delete_uintp(nump);
    }
    return result;
  }

  /**
   * Returns the URI of the dimension label array
   *
   * @return The URI
   */
  public String getURI() throws TileDBError {
    String name;
    SWIGTYPE_p_p_char uripp = tiledb.new_charpp();
    try {
      ctx.handleError(tiledb.tiledb_dimension_label_get_uri(ctx.getCtxp(), this.dimlabelp, uripp));
      name = tiledb.charpp_value(uripp);
    } finally {
      tiledb.delete_charpp(uripp);
    }
    return name;
  }

  /**
   * Returns the order of the labels on the dimension label
   *
   * @return The label order
   */
  public tiledb_data_order_t getLabelOrder() throws TileDBError {
    if (this.labelOrder != null) return this.labelOrder;

    SWIGTYPE_p_tiledb_data_order_t orderp = tiledb.new_tiledb_data_order_tp();

    try {
      ctx.handleError(
          tiledb.tiledb_dimension_label_get_label_order(ctx.getCtxp(), this.dimlabelp, orderp));
      tiledb_data_order_t type = tiledb.tiledb_data_order_tp_value(orderp);
      return type;
    } finally {
      tiledb.delete_tiledb_data_order_tp(orderp);
    }
  }

  /**
   * Returns the type of the labels on the dimension label
   *
   * @return The label type
   */
  public Datatype getLabelType() throws TileDBError {
    if (this.labelType != null) return Datatype.fromSwigEnum(labelType);

    SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
    try {
      ctx.handleError(
          tiledb.tiledb_dimension_label_get_label_type(ctx.getCtxp(), this.dimlabelp, typep));
      tiledb_datatype_t type = tiledb.tiledb_datatype_tp_value(typep);
      return Datatype.fromSwigEnum(type);
    } finally {
      tiledb.delete_tiledb_datatype_tp(typep);
    }
  }

  public void close() {
    if (dimlabelp != null && dimlabelpp != null) {
      tiledb.tiledb_dimension_label_free(dimlabelpp);
      dimlabelpp = null;
      dimlabelp = null;
    }
  }
}
