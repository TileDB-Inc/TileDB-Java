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

import io.tiledb.api.*;

import java.math.BigInteger;

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

  /* Constructor from native object */
  public Attribute(Context ctx, SWIGTYPE_p_p_tiledb_attribute_t attributepp) {
    this.ctx =ctx;
    this.attributepp = attributepp;
    this.attributep = Utils.tiledb_attribute_tpp_value(attributepp);
  }

  /* Constructor from native object */
  public Attribute(Context ctx, String name, Class<T> atrrType) throws TileDBError {
    this.ctx =ctx;
    this.attributepp = Utils.new_tiledb_attribute_tpp();
    tiledb_datatype_t type =  getNativeType(atrrType);
    ctx.handle_error(tiledb.tiledb_attribute_create(ctx.getCtxp(), attributepp, name, type));
  }

  private tiledb_datatype_t getNativeType(Class<T> atrrType) throws TileDBError {
    if(atrrType.equals(Integer.class)) {
      return tiledb_datatype_t.TILEDB_INT32;
    } else if(atrrType.equals(Long.class)) {
      return tiledb_datatype_t.TILEDB_INT64;
    } else if(atrrType.equals(Character.class)) {
      return tiledb_datatype_t.TILEDB_CHAR;
    } else if(atrrType.equals(Float.class)) {
      return tiledb_datatype_t.TILEDB_FLOAT32;
    } else if(atrrType.equals(Double.class)) {
      return tiledb_datatype_t.TILEDB_FLOAT64;
    } else if(atrrType.equals(Byte.class)) {
      return tiledb_datatype_t.TILEDB_INT8;
    } else if(atrrType.equals(Short.class)) {
      return tiledb_datatype_t.TILEDB_INT16;
    } else if(atrrType.equals(BigInteger.class)) {
      return tiledb_datatype_t.TILEDB_UINT64;
    } else {
      throw new TileDBError("Not supported type: "+atrrType);
    }
  }

  public SWIGTYPE_p_tiledb_attribute_t getAttributep() {
    return attributep;
  }
  
  public String getName() {
    return "";
  }

}
