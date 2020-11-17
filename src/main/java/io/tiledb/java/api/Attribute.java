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

import static io.tiledb.java.api.Constants.TILEDB_VAR_NUM;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;

/**
 * Describes an Attribute of an Array cell.
 *
 * <p>An Attribute specifies a datatype for a particular parameter in each array cell. There are 3
 * supported Attribute types:
 *
 * <ul>
 *   <li>Fundamental types: Character, String, Byte, Boolean, Short, Integer, Long, Double and
 *       Float.
 *   <li>Fixed sized arrays of the above types.
 *   <li>Variable length arrays of the above types.
 * </ul>
 *
 * <b>Example:</b>
 *
 * <pre>{@code
 * Context ctx = new Context();
 * Attribute a1 = new Attribute(ctx,"a1",Integer.class);
 * Attribute a2 = new Attribute(ctx,"a2",Character.class);
 * Attribute a3 = new Attribute(ctx,"a3",Float.class);
 *
 * // Change compression scheme
 * a1.setFilterList(new FilterList(ctx).addFilter(new LZ4Filter(ctx)));
 * a2.setCellValNum(TILEDB_VAR_NUM); // Variable sized character attribute (String)
 * a3.setCellValNum(2); // 2 floats stored per cell
 *
 * ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
 * schema.setDomain(domain);
 * schema.addAttribute(a1);
 * schema.addAttribute(a2);
 * schema.addAttribute(a3);
 * }</pre>
 */
public class Attribute implements AutoCloseable {

  private Context ctx;
  private String name;
  private Datatype type;

  private SWIGTYPE_p_tiledb_attribute_t attributep;
  private SWIGTYPE_p_p_tiledb_attribute_t attributepp;

  /**
   * Construct an attribute with a name and java class type. `cellValNum` will be set to 1.
   *
   * @param ctx TileDB context
   * @param name Name of the attribute
   * @param attrType Java class type of the attribute
   * @exception TileDBError A TileDB exception
   */
  public Attribute(Context ctx, String name, Class attrType) throws TileDBError {
    Datatype _type;
    SWIGTYPE_p_p_tiledb_attribute_t _attributepp = tiledb.new_tiledb_attribute_tpp();
    try {
      _type = Types.getNativeType(attrType);
      ctx.handleError(
          tiledb.tiledb_attribute_alloc(ctx.getCtxp(), name, _type.toSwigEnum(), _attributepp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_attribute_tpp(_attributepp);
      throw err;
    }
    this.ctx = ctx;
    this.name = name;
    this.type = _type;
    this.attributep = tiledb.tiledb_attribute_tpp_value(_attributepp);
    this.attributepp = _attributepp;
  }

  /**
   * Construct an attribute with name and TileDB Datatype. `cellValNum` will be set to 1.
   *
   * @param ctx TileDB Context
   * @param name Name of the attribute
   * @param attrType TileDB Datatype of attribute
   * @throws TileDBError A TileDB exception
   */
  public Attribute(Context ctx, String name, Datatype attrType) throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t _attributepp = tiledb.new_tiledb_attribute_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_attribute_alloc(ctx.getCtxp(), name, attrType.toSwigEnum(), _attributepp));
    } catch (TileDBError error) {
      tiledb.delete_tiledb_attribute_tpp(_attributepp);
    }
    this.ctx = ctx;
    this.name = name;
    this.type = attrType;
    this.attributep = tiledb.tiledb_attribute_tpp_value(_attributepp);
    this.attributepp = _attributepp;
  }

  /* Constructor from native object */
  protected Attribute(Context ctx, SWIGTYPE_p_p_tiledb_attribute_t attributepp) throws TileDBError {
    this.ctx = ctx;
    this.attributep = tiledb.tiledb_attribute_tpp_value(attributepp);
    this.attributepp = attributepp;
  }

  protected SWIGTYPE_p_tiledb_attribute_t getAttributep() {
    return attributep;
  }

  /**
   * @return The name of the Attribute.
   * @exception TileDBError A TileDB exception
   */
  public String getName() throws TileDBError {
    if (name == null) {
      SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
      try {
        ctx.handleError(tiledb.tiledb_attribute_get_name(ctx.getCtxp(), getAttributep(), namepp));
        name = tiledb.charpp_value(namepp);
      } finally {
        tiledb.delete_charpp(namepp);
      }
    }
    return name;
  }

  /**
   * @return The Attribute Enumerated datatype (TileDB type).
   * @exception TileDBError A TileDB exception
   */
  public Datatype getType() throws TileDBError {
    if (type == null) {
      SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
      try {
        ctx.handleError(tiledb.tiledb_attribute_get_type(ctx.getCtxp(), getAttributep(), typep));
        type = Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(typep));
      } finally {
        tiledb.delete_tiledb_datatype_tp(typep);
      }
    }
    return type;
  }

  /**
   * @return The size (in bytes) of one cell on this Attribute.
   * @exception TileDBError A TileDB exception
   */
  public long getCellSize() throws TileDBError {
    long cellSize;
    SWIGTYPE_p_unsigned_long_long sizep = tiledb.new_ullp();
    try {
      ctx.handleError(tiledb.tiledb_attribute_get_cell_size(ctx.getCtxp(), getAttributep(), sizep));
      cellSize = tiledb.ullp_value(sizep).longValue();
    } finally {
      tiledb.delete_ullp(sizep);
    }
    return cellSize;
  }

  /**
   * @return The number of values stored in each cell. This is equal to the size of the Attribute *
   *     sizeof(attributeType). For variable size attributes this is TILEDB_VAR_NUM.
   * @exception TileDBError A TileDB exception
   */
  public long getCellValNum() throws TileDBError {
    long cellValNum;
    SWIGTYPE_p_unsigned_int nump = tiledb.new_uintp();
    try {
      ctx.handleError(
          tiledb.tiledb_attribute_get_cell_val_num(ctx.getCtxp(), getAttributep(), nump));
      cellValNum = tiledb.uintp_value(nump);
    } finally {
      tiledb.delete_uintp(nump);
    }
    return cellValNum;
  }

  /**
   * @return True if this is a variable length Attribute.
   * @exception TileDBError A TileDB exception
   */
  public boolean isVar() throws TileDBError {
    return getCellValNum() == TILEDB_VAR_NUM;
  }

  /**
   * Sets the number of Attribute values per cell.
   *
   * @param size The number of values per cell. Use TILEDB_VAR_NUM for variable length.
   * @exception TileDBError A TileDB exception
   */
  public Attribute setCellValNum(long size) throws TileDBError {
    ctx.handleError(tiledb.tiledb_attribute_set_cell_val_num(ctx.getCtxp(), attributep, size));
    return this;
  }

  /**
   * Sets the Attribute to have variable length cell representation
   *
   * @return Attribute
   * @exception TileDBError A TileDB exception
   */
  public Attribute setCellVar() throws TileDBError {
    return setCellValNum(TILEDB_VAR_NUM);
  }

  /**
   * Sets the Attribute FilterList.
   *
   * @param filters A TileDB FilterList
   * @throws TileDBError A TileDB exception
   */
  public Attribute setFilterList(FilterList filters) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_attribute_set_filter_list(
            ctx.getCtxp(), attributep, filters.getFilterListp()));
    return this;
  }

  /**
   * Gets the list of filtes associated with the attribute
   *
   * @return A FilterList instance
   * @throws TileDBError A TileDB exception
   */
  public FilterList getFilterList() throws TileDBError {
    FilterList filterlist;
    SWIGTYPE_p_p_tiledb_filter_list_t filterlistpp = tiledb.new_tiledb_filter_list_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_attribute_get_filter_list(ctx.getCtxp(), attributep, filterlistpp));
      filterlist = new FilterList(ctx, filterlistpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_filter_list_tpp(filterlistpp);
      throw err;
    }
    return filterlist;
  }

  public void setFillValue(NativeArray value, BigInteger size) throws TileDBError {
    try {
      ctx.handleError(
          tiledb.tiledb_attribute_set_fill_value(
              ctx.getCtxp(), attributep, value.toVoidPointer(), size));
    } catch (TileDBError err) {
      throw err;
    }
  }

  public Pair<Object, Long> getFillValue() throws TileDBError {

    try (NativeArray value = new NativeArray(ctx, this.type.getNativeSize(), this.type)) {
      SWIGTYPE_p_unsigned_long_long size = tiledb.new_ullp();
      SWIGTYPE_p_p_void v = tiledb.new_voidpArray(1);

      ctx.handleError(tiledb.tiledb_attribute_get_fill_value(ctx.getCtxp(), attributep, v, size));

      Object fillValue;
      try (NativeArray fillValueArray = new NativeArray(ctx, getType(), v, 1)) {
        fillValue = fillValueArray.getItem(0);
      }

      return new Pair(fillValue, tiledb.ullp_value(size));
    } catch (TileDBError err) {
      throw err;
    }
  }

  public int setNullable(short nullable) throws TileDBError {

    try {
      int res;
      ctx.handleError(
          res = tiledb.tiledb_attribute_set_nullable(ctx.getCtxp(), this.attributep, nullable));
      return res;
    } catch (TileDBError err) {
      throw err;
    }
  }

  /** @return A String representation for the Attribute. */
  @Override
  public String toString() {
    try {
      return "Attr<"
          + getName()
          + ','
          + getType()
          + ','
          + ((getCellValNum() == TILEDB_VAR_NUM) ? "VAR" : getCellValNum())
          + '>';
    } catch (TileDBError err) {
      err.printStackTrace();
    }
    return "";
  }

  /** Free's native TileDB resources associated with the Attribute object */
  public void close() {
    if (attributep != null) {
      tiledb.tiledb_attribute_free(attributepp);
      attributep = null;
      attributepp = null;
    }
  }
}
