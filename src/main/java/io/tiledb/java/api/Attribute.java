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

import io.tiledb.libtiledb.*;

import javax.xml.crypto.Data;

import static io.tiledb.java.api.Constants.TILEDB_VAR_NUM;

/**
 * Describes an Attribute of an Array cell.
 *
 * An Attribute specifies a datatype for a particular parameter in each
 * array cell. There are 3 supported Attribute types:
 *
 * <ul>
 *   <li> Fundamental types: Character, String, Byte, Boolean, Short, Integer, Long, Double and Float. </li>
 *   <li> Fixed sized arrays of the above types. </li>
 *   <li> Variable length arrays of the above types. </li>
 * </ul>
 *
 * <b>Example:</b>
 * <pre>{@code
 *   Context ctx = new Context();
 *   Attribute a1 = new Attribute(ctx,"a1",Integer.class);
 *   Attribute a2 = new Attribute(ctx,"a2",Character.class);
 *   Attribute a3 = new Attribute(ctx,"a3",Float.class);
 *
 *   // Change compression scheme
 *   a1.setCompressor(new Compressor(TILEDB_BLOSC_LZ4, -1));
 *   a2.setCellValNum(TILEDB_VAR_NUM); // Variable sized character attribute (String)
 *   a3.setCellValNum(2); // 2 floats stored per cell
 *
 *   ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
 *   schema.setDomain(domain);
 *   schema.addAttribute(a1);
 *   schema.addAttribute(a2);
 *   schema.addAttribute(a3);
 * }</pre>
 */
public class Attribute implements AutoCloseable {

  private Context ctx;
  private String name;
  private Datatype type;

  private SWIGTYPE_p_tiledb_attribute_t attributep;
  private SWIGTYPE_p_p_tiledb_attribute_t attributepp;

  /**
   *
   * Construct an attribute with a name and type. `cellValNum` will
   * be set to 1.
   *
   * @param ctx TileDB Context
   * @param name Name of the Attribute
   * @param attrType Java type of the Attribute
   * @exception TileDBError A TileDB exception
   */
  public Attribute(Context ctx, String name, Class attrType) throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t _attributepp = tiledb.new_tiledb_attribute_tpp();
    Datatype _type = Types.getNativeType(attrType);
    try {
      ctx.handleError(tiledb.tiledb_attribute_alloc(ctx.getCtxp(), name, _type.toSwigEnum(), _attributepp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_attribute_tpp(_attributepp);
      throw err;
    }
    ctx.deleterAdd(this);
    this.ctx = ctx;
    this.name = name;
    this.type = _type;
    this.attributep = tiledb.tiledb_attribute_tpp_value(_attributepp);
    this.attributepp = _attributepp;
  }

  /* Constructor from native object */
  protected Attribute(Context ctx, SWIGTYPE_p_p_tiledb_attribute_t attributepp) throws TileDBError {
    SWIGTYPE_p_tiledb_attribute_t _attributep = tiledb.tiledb_attribute_tpp_value(attributepp);
    SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
    SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
    String _name;
    Datatype _type;
    try {
      ctx.handleError(tiledb.tiledb_attribute_get_name(ctx.getCtxp(), _attributep, namepp));
      ctx.handleError(tiledb.tiledb_attribute_get_type(ctx.getCtxp(), _attributep, typep));
      _name = tiledb.charpp_value(namepp);
      _type = Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(typep));
      tiledb.delete_charpp(namepp);
      tiledb.delete_tiledb_datatype_tp(typep);
    } catch (TileDBError err) {
      tiledb.delete_charpp(namepp);
      tiledb.delete_tiledb_datatype_tp(typep);
      throw err;
    }
    ctx.deleterAdd(this);
    this.ctx = ctx;
    this.name = _name;
    this.type = _type;
    this.attributepp = attributepp;
    this.attributep = tiledb.tiledb_attribute_tpp_value(attributepp);
  }

  protected SWIGTYPE_p_tiledb_attribute_t getAttributep() {
    return attributep;
  }

  /**
   *
   * @return The name of the Attribute.
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @return The Attribute Enumerated datatype (C type).
   */
  public Datatype getType() {
    return type;
  }

  /**
   *
   * @return The size (in bytes) of one cell on this Attribute.
   * @exception TileDBError A TileDB exception
   */
  public long getCellSize() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long sizep = tiledb.new_ullp();
    try {
      ctx.handleError(tiledb.tiledb_attribute_get_cell_size(ctx.getCtxp(), attributep, sizep));
    } catch (TileDBError err) {
      tiledb.delete_ullp(sizep);
      throw err;
    }
    long size = tiledb.ullp_value(sizep).longValue();
    tiledb.delete_ullp(sizep);
    return size;
  }

  /**
   *
   * @return The number of values stored in each cell.
   * This is equal to the size of the Attribute * sizeof(attributeType).
   * For variable size attributes this is TILEDB_VAR_NUM.
   * @exception TileDBError A TileDB exception
   */
  public long getCellValNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int sizep = tiledb.new_uintp();
    try {
      ctx.handleError(tiledb.tiledb_attribute_get_cell_val_num(ctx.getCtxp(), attributep, sizep));
    } catch (TileDBError err) {
      tiledb.delete_uintp(sizep);
      throw err;
    }
    long size = tiledb.uintp_value(sizep);
    tiledb.delete_uintp(sizep);
    return size;
  }

  /**
   *
   * @return True if this is a variable length Attribute.
   * @exception TileDBError A TileDB exception
   */
  public boolean isVar() throws TileDBError {
    return getCellValNum() == TILEDB_VAR_NUM;
  }

  /**
   * Sets the number of Attribute values per cell.
   * @param size The number of values per cell. Use TILEDB_VAR_NUM for variable length.
   * @exception TileDBError A TileDB exception
   */
  public void setCellValNum(long size) throws TileDBError {
    ctx.handleError(tiledb.tiledb_attribute_set_cell_val_num(ctx.getCtxp(), attributep, size));
  }

  /**
   *
   * @return The Attribute compressor.
   * @exception TileDBError A TileDB exception
   */
  public Compressor getCompressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressor = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int level = tiledb.new_intp();
    Compressor cmp;
    try {
      ctx.handleError(tiledb.tiledb_attribute_get_compressor(ctx.getCtxp(), attributep, compressor, level));
      cmp = new Compressor(CompressorType.fromSwigEnum(tiledb.tiledb_compressor_tp_value(compressor)),
                           tiledb.intp_value(level));
    } catch (TileDBError err) {
      tiledb.delete_intp(level);
      tiledb.delete_tiledb_compressor_tp(compressor);
      throw err;
    }
    tiledb.delete_intp(level);
    tiledb.delete_tiledb_compressor_tp(compressor);
    return cmp;
  }

  /**
   * Sets the Attribute compressor.
   * @param compressor The Compressor object to be used.
   * @exception TileDBError A TileDB exception
   */
  public void setCompressor(Compressor compressor) throws TileDBError {
    ctx.handleError(tiledb.tiledb_attribute_set_compressor(
        ctx.getCtxp(), attributep, compressor.getCompressor().toSwigEnum(), compressor.getLevel()));
  }

  /**
   *
   * @return A String representation for the Attribute.
   */
  @Override
  public String toString() {
    try {
      return "Attr<" + getName() + ',' + getType() + ',' + ((getCellValNum() == TILEDB_VAR_NUM) ? "VAR" : getCellValNum()) + '>';
    } catch (TileDBError err) {
      err.printStackTrace();
    }
    return "";
  }

  /**
   * Free's native TileDB resources associated with the Attribute object
   */
  public void close() {
    if (attributep != null) {
      tiledb.tiledb_attribute_free(attributepp);
    }
  }

}
