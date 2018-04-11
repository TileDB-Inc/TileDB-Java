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
 * Describes an attribute of an Array cell.
 *
 * @details
 * An attribute specifies a datatype for a particular parameter in each
 * array cell. There are 3 supported attribute types:
 *
 * - Fundamental types, such as char, int, double, uint64_t, etc..
 * - Fixed sized arrays: std::array<T, N> where T is fundamental
 * - Variable length data: std::string, std::vector<T> where T is fundamental
 *
 * **Example:**
 *
 * @code{.java}
 * tiledb::Context ctx;
 * Attribute<Integer> a1 = new Attribute<Integer>(ctx, "a1");
 * Attribute<String> a2 = new Attribute<String>(ctx, "a2");
 * Attribute<Integer>  a3 = tiledb::Attribute::create<std::array<float, 3>>(ctx, "a3");
 *
 * // Change compression scheme
 * a1.set_compressor({TILEDB_BLOSC, -1});
 * a1.cell_val_num(); // 1
 * a2.cell_val_num(); // Variable sized, TILEDB_VAR_NUM
 * a3.cell_val_num(); // sizeof(std::array<float, 3>), Objects stored as char
 * array a3.cell_size(); // same as cell_val_num since type is char
 * a3.type_size(); // sizeof(char)
 *
 * tiledb::ArraySchema schema(ctx);
 * schema.add_attributes(a1, a2, a3);
 * @endcode
 */
public class Attribute<T> {

  private SWIGTYPE_p_tiledb_attribute_t attributep;
  private SWIGTYPE_p_p_tiledb_attribute_t attributepp;
  private int size;
  private boolean isArray;
  private Context ctx;
  private String name;
  private tiledb_datatype_t type;

  /* Constructor from native object */
  public Attribute(Context ctx, SWIGTYPE_p_p_tiledb_attribute_t attributepp) throws TileDBError {
    this.ctx =ctx;
    this.attributepp = attributepp;
    this.attributep = Utils.tiledb_attribute_tpp_value(attributepp);
    getName();
  }

  /* Constructor from native object */
  public Attribute(Context ctx, String name, Class<T> atrrType) throws TileDBError {
    this.ctx =ctx;
    this.name = name;
    this.attributepp = Utils.new_tiledb_attribute_tpp();
    type =  Types.getNativeType(atrrType);
    ctx.handle_error(tiledb.tiledb_attribute_create(ctx.getCtxp(), attributepp, name, type));
    this.attributep = Utils.tiledb_attribute_tpp_value(attributepp);
  }

  public SWIGTYPE_p_tiledb_attribute_t getAttributep() {
    return attributep;
  }

  /** Returns the name of the attribute. */
  public String getName() throws TileDBError {
    if(name==null){
      SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
      ctx.handle_error(tiledb.tiledb_attribute_get_name(ctx.getCtxp(), attributep, namepp));
      name = tiledb.charpp_value(namepp);
      tiledb.delete_charpp(namepp);
    }
    return name;
  }

  /** Returns the attribute datatype. */
  public tiledb_datatype_t getType() throws TileDBError {
    if(type==null){
      SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
      ctx.handle_error(tiledb.tiledb_attribute_get_type(ctx.getCtxp(), attributep, typep));
      type = tiledb.tiledb_datatype_tp_value(typep);
      tiledb.delete_tiledb_datatype_tp(typep);
    }
    return type;
  }

  /** Returns the size (in bytes) of one cell on this attribute. */
  public long getCellSize() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long sizep = tiledb.new_ullp();
    ctx.handle_error(tiledb.tiledb_attribute_get_cell_size(ctx.getCtxp(), attributep, sizep));
    long size = tiledb.ullp_value(sizep).longValue();
    tiledb.delete_ullp(sizep);
    return size;
  }

  /**
   * Returns the number of values stored in each cell.
   * This is equal to the size of the attribute * sizeof(attr_type).
   * For variable size attributes this is TILEDB_VAR_NUM.
   */
  public long getCellValNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int sizep = tiledb.new_uintp();
    ctx.handle_error(tiledb.tiledb_attribute_get_cell_val_num(ctx.getCtxp(), attributep, sizep));
    long size = tiledb.uintp_value(sizep);
    tiledb.delete_uintp(sizep);
    return size;
  }

  /** Sets the number of attribute values per cell. */
  public void setCellValNum(long size) throws TileDBError {
    ctx.handle_error(tiledb.tiledb_attribute_set_cell_val_num(ctx.getCtxp(), attributep, size));
  }

  /** Returns the attribute compressor. */
  public Compressor getCompressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressor = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int level = tiledb.new_intp();
    ctx.handle_error(
        tiledb.tiledb_attribute_get_compressor(ctx.getCtxp(), attributep, compressor, level));
    Compressor cmp = new Compressor(tiledb.tiledb_compressor_tp_value(compressor), tiledb.intp_value(level));
    tiledb.delete_intp(level);
    tiledb.delete_tiledb_compressor_tp(compressor);
    return cmp;
  }

  /** Sets the attribute compressor. */
  public void setCompressor(Compressor c) throws TileDBError {
    ctx.handle_error(tiledb.tiledb_attribute_set_compressor(
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
}
