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

/**
 * Describes an Attribute of an Array cell.
 *
 * @details
 * An Attribute specifies a datatype for a particular parameter in each
 * array cell. There are 3 supported Attribute types:
 *
 * - Fundamental types, such as char, int, double, uint64_t, etc..
 * - Fixed sized arrays
 * - Variable length data
 *
 * **Example:**
 *
 * @code{.java}
 * tiledb::Context ctx;
 * Attribute a1 = new Attribute(ctx,"a1",Integer.class);
 * Attribute a2 = new Attribute(ctx,"a2",String.class);
 * Attribute a3 = new Attribute(ctx,"a3",Float.class);
 *
 * // Change compression scheme
 * a1.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_BLOSC_LZ4, -1));
 * a1.setCellValNum(1); // 1
 * a2.setCellValNum(tiledb.tiledb_var_num()); // Variable sized, TILEDB_VAR_NUM
 * a3.setCellValNum(2); // 3 Objects stored as per cell
 *
 * ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_DENSE);
 * schema.setDomain(domain);
 * schema.addAttribute(a1);
 * schema.addAttribute(a2);
 * schema.addAttribute(a3);
 * @endcode
 */
public class Attribute implements AutoCloseable {

  private SWIGTYPE_p_tiledb_attribute_t attributep;
  private SWIGTYPE_p_p_tiledb_attribute_t attributepp;
  
  private Context ctx;
  private String name;
  private tiledb_datatype_t type;
  private int size;
  private boolean isArray;

  /* Constructor from native object */
  protected Attribute(Context ctx, SWIGTYPE_p_p_tiledb_attribute_t attributepp) throws TileDBError {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    this.attributepp = attributepp;
    this.attributep = tiledb.tiledb_attribute_tpp_value(attributepp);
    getName();
  }

  /* Default Constructor */
  public Attribute(Context ctx, String name, Class atrrType) throws TileDBError {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    this.name = name;
    this.attributepp = tiledb.new_tiledb_attribute_tpp();
    this.type = Types.getNativeType(atrrType);
    ctx.handleError(tiledb.tiledb_attribute_alloc(ctx.getCtxp(), name, type, attributepp));
    this.attributep = tiledb.tiledb_attribute_tpp_value(attributepp);
  }

  protected SWIGTYPE_p_tiledb_attribute_t getAttributep() {
    return attributep;
  }

  /** Returns the name of the Attribute. */
  public String getName() throws TileDBError {
    if(name==null){
      SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
      ctx.handleError(tiledb.tiledb_attribute_get_name(ctx.getCtxp(), attributep, namepp));
      name = tiledb.charpp_value(namepp);
      tiledb.delete_charpp(namepp);
    }
    return name;
  }

  /** Returns the Attribute datatype. */
  public tiledb_datatype_t getType() throws TileDBError {
    if(type==null){
      SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
      ctx.handleError(tiledb.tiledb_attribute_get_type(ctx.getCtxp(), attributep, typep));
      type = tiledb.tiledb_datatype_tp_value(typep);
      tiledb.delete_tiledb_datatype_tp(typep);
    }
    return type;
  }

  /** Returns the size (in bytes) of one cell on this Attribute. */
  public long getCellSize() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long sizep = tiledb.new_ullp();
    ctx.handleError(tiledb.tiledb_attribute_get_cell_size(ctx.getCtxp(), attributep, sizep));
    long size = tiledb.ullp_value(sizep).longValue();
    tiledb.delete_ullp(sizep);
    return size;
  }

  /**
   * Returns the number of values stored in each cell.
   * This is equal to the size of the Attribute * sizeof(attr_type).
   * For variable size getAttributes this is TILEDB_VAR_NUM.
   */
  public long getCellValNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int sizep = tiledb.new_uintp();
    ctx.handleError(tiledb.tiledb_attribute_get_cell_val_num(ctx.getCtxp(), attributep, sizep));
    long size = tiledb.uintp_value(sizep);
    tiledb.delete_uintp(sizep);
    return size;
  }
  
  public boolean isVar() throws TileDBError {
    return getCellValNum() == tiledb.tiledb_var_num();
  }

  /** Sets the number of Attribute values per cell. */
  public void setCellValNum(long size) throws TileDBError {
    ctx.handleError(tiledb.tiledb_attribute_set_cell_val_num(ctx.getCtxp(), attributep, size));
  }

  /** Returns the Attribute compressor. */
  public Compressor getCompressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressor = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int level = tiledb.new_intp();
    ctx.handleError(
        tiledb.tiledb_attribute_get_compressor(ctx.getCtxp(), attributep, compressor, level));
    Compressor cmp = new Compressor(tiledb.tiledb_compressor_tp_value(compressor), tiledb.intp_value(level));
    tiledb.delete_intp(level);
    tiledb.delete_tiledb_compressor_tp(compressor);
    return cmp;
  }

  /** Sets the Attribute compressor. */
  public void setCompressor(Compressor c) throws TileDBError {
    ctx.handleError(tiledb.tiledb_attribute_set_compressor(
        ctx.getCtxp(), attributep, c.getCompressor(), c.getLevel()));
  }

  @Override
  public String toString() {
    try {
      return "Attr<" + getName() + ',' + getType() + ',' + ((getCellValNum() == tiledb.tiledb_var_num()) ? "VAR" : getCellValNum()) + '>';
    } catch (TileDBError tileDBError) {
      tileDBError.printStackTrace();
    }
    return "";
  }

  /**
   * Delete the native object.
   */
  public void close() throws TileDBError {
    if(attributep!=null)
      tiledb.tiledb_attribute_free(attributepp);
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
