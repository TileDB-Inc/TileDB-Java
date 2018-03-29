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
import java.util.HashMap;
import java.util.Map;

/**
 * Schema describing an array.
 *
 * @details The schema is an independent description of an array. A schema can be
 * used to create multiple array's, and stores information about its
 * domain, cell types, and compression details. An array schema is composed of:
 * <p>
 * - A Domain
 * - A set of Attributes
 * - Memory layout definitions: tile and cell
 * - Compression details for Array level factors like offsets and coordinates
 * <p>
 * **Example:**
 * @code{.java} Context ctx = new Context();
 * ArraySchema schema = new ArraySchema(ctx, tiledb.TILEDB_SPARSE); // Or TILEDB_DENSE
 * <p>
 * // Create a Domain
 * Domain domain = new Domain(...);
 * <p>
 * // Create Attributes
 * Attribute a1 = Attribute.create(...);
 * <p>
 * schema.set_domain(domain);
 * schema.add_attribute(a1);
 * <p>
 * // Specify tile memory layout
 * schema.set_tile_order(tiledb.TILEDB_GLOBAL_ORDER);
 * // Specify cell memory layout within each tile
 * schema.set_cell_order(tiledb.TILEDB_GLOBAL_ORDER);
 * schema.set_capacity(10); // For sparse, set capacity of each tile
 * <p>
 * Array.create(schema, "my_array"); // Make array with schema
 * ArraySchema s = ArraySchema(ctx, "my_array"); // Load schema from array
 * @endcode
 */
public class ArraySchema {

  private SWIGTYPE_p_p_tiledb_array_schema_t schemapp;
  private SWIGTYPE_p_tiledb_array_schema_t schemap;
  private Context ctx;

  /**
   * Creates a new array schema.
   */
  public ArraySchema(Context ctx, tiledb_array_type_t type) throws TileDBError {
    this.ctx = ctx;
    schemapp = Utils.new_tiledb_array_schema_tpp();
    ctx.handle_error(tiledb.tiledb_array_schema_create(ctx.getCtxp(), schemapp, type));
    schemap = Utils.tiledb_array_schema_tpp_value(schemapp);
  }

  /**
   * Loads the schema of an existing array with the input URI.
   */
  public ArraySchema(Context ctx, String uri) throws TileDBError {
    this.ctx = ctx;
    schemapp = Utils.new_tiledb_array_schema_tpp();
    ctx.handle_error(tiledb.tiledb_array_schema_load(ctx.getCtxp(), schemapp, uri));
    schemap = Utils.tiledb_array_schema_tpp_value(schemapp);
  }

  /**
   * Dumps the array schema in an ASCII representation to stdout.
   */
  public void dump() throws TileDBError {
    ctx.handle_error(tiledb.tiledb_array_schema_dump_stdout(ctx.getCtxp(), schemap));
  }

  /**
   * Returns the array type.
   */
  public tiledb_array_type_t getArrayType() throws TileDBError {
    SWIGTYPE_p_tiledb_array_type_t typep = tiledb.new_tiledb_array_type_tp();
    ctx.handle_error(
        tiledb.tiledb_array_schema_get_array_type(ctx.getCtxp(), schemap, typep));
    tiledb_array_type_t type = tiledb.tiledb_array_type_tp_value(typep);
    tiledb.delete_tiledb_array_type_tp(typep);
    return type;
  }

  /**
   * Returns the tile capacity.
   */
  public long capacity() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long capacityp = tiledb.new_ullp();
    ctx.handle_error(
        tiledb.tiledb_array_schema_get_capacity(ctx.getCtxp(), schemap, capacityp));
    long capacity = tiledb.ullp_value(capacityp).longValue();
    tiledb.delete_ullp(capacityp);
    return capacity;
  }

  /**
   * Sets the tile capacity.
   */
  public void set_capacity(long capacity) throws TileDBError {
    ctx.handle_error(
        tiledb.tiledb_array_schema_set_capacity(ctx.getCtxp(), schemap, new BigInteger(capacity+"")));
  }

  /**
   * Returns the tile order.
   */
  public tiledb_layout_t tile_order() throws TileDBError {
    SWIGTYPE_p_tiledb_layout_t layoutpp = tiledb.new_tiledb_layout_tp();
    ctx.handle_error(
        tiledb.tiledb_array_schema_get_tile_order(ctx.getCtxp(), schemap, layoutpp));
    tiledb_layout_t layout = tiledb.tiledb_layout_tp_value(layoutpp);
    tiledb.delete_tiledb_layout_tp(layoutpp);
    return layout;
  }

  /**
   * Sets the tile order.
   */
  public void set_tile_order(tiledb_layout_t layout) throws TileDBError {
    ctx.handle_error(
        tiledb.tiledb_array_schema_set_tile_order(ctx.getCtxp(), schemap, layout));
  }

  /**
   * Sets both the tile and cell layouts
   *
   * @param tile_layout Tile layout
   * @param cell_layout Cell layout
   */
  public void set_order(tiledb_layout_t tile_layout, tiledb_layout_t cell_layout) throws TileDBError {
    set_tile_order(tile_layout);
    set_cell_order(cell_layout);
  }

  /**
   * Returns the cell order.
   */
  public tiledb_layout_t cell_order() throws TileDBError {
    SWIGTYPE_p_tiledb_layout_t layoutpp = tiledb.new_tiledb_layout_tp();
    ctx.handle_error(
        tiledb.tiledb_array_schema_get_cell_order(ctx.getCtxp(), schemap, layoutpp));
    tiledb_layout_t layout = tiledb.tiledb_layout_tp_value(layoutpp);
    tiledb.delete_tiledb_layout_tp(layoutpp);
    return layout;
  }

  /**
   * Sets the cell order.
   */
  public void set_cell_order(tiledb_layout_t layout) throws TileDBError {
    ctx.handle_error(
        tiledb.tiledb_array_schema_set_cell_order(ctx.getCtxp(), schemap, layout));
  }

  /**
   * Returns the compressor of the coordinates.
   */
  Compressor coords_compressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressorp = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int levelp = tiledb.new_intp();
    ctx.handle_error(tiledb.tiledb_array_schema_get_coords_compressor(
        ctx.getCtxp(), schemap, compressorp, levelp));
    Compressor compressor = new Compressor(tiledb.tiledb_compressor_tp_value(compressorp), tiledb.intp_value(levelp));
    tiledb.delete_tiledb_compressor_tp(compressorp);
    tiledb.delete_intp(levelp);
    return compressor;
  }

  /**
   * Sets the compressor for the coordinates.
   */
  public void set_coords_compressor(Compressor compressor) throws TileDBError {
    ctx.handle_error(tiledb.tiledb_array_schema_set_coords_compressor(
        ctx.getCtxp(), schemap, compressor.getCompressor(), compressor.getLevel()));
  }

  /**
   * Returns the compressor of the offsets.
   */
  Compressor offsets_compressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressorp = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int levelp = tiledb.new_intp();
    ctx.handle_error(tiledb.tiledb_array_schema_get_offsets_compressor(
        ctx.getCtxp(), schemap, compressorp, levelp));
    Compressor compressor = new Compressor(tiledb.tiledb_compressor_tp_value(compressorp), tiledb.intp_value(levelp));
    tiledb.delete_tiledb_compressor_tp(compressorp);
    tiledb.delete_intp(levelp);
    return compressor;
  }

  /**
   * Sets the compressor for the offsets.
   */
  public void set_offsets_compressor(Compressor compressor) throws TileDBError {
    ctx.handle_error(tiledb.tiledb_array_schema_set_offsets_compressor(
        ctx.getCtxp(), schemap, compressor.getCompressor(), compressor.getLevel()));
  }

  /**
   * Retruns the array domain of array.
   */
  Domain domain() throws TileDBError {
    SWIGTYPE_p_p_tiledb_domain_t domainpp = Utils.new_tiledb_domain_tpp();
    ctx.handle_error(tiledb.tiledb_array_schema_get_domain(ctx.getCtxp(), schemap, domainpp));
    return new Domain(ctx, domainpp);
  }

  /**
   * Sets the array domain.
   */
  public void set_domain(Domain domain) throws TileDBError {
    ctx.handle_error(
        tiledb.tiledb_array_schema_set_domain(ctx.getCtxp(), schemap, domain.getDomainp()));
  }

  /**
   * Adds an attribute to the array.
   */
  public void add_attribute(Attribute attr) throws TileDBError {
    ctx.handle_error(tiledb.tiledb_array_schema_add_attribute(ctx.getCtxp(), schemap, attr.getAttributep()));
  }

  /**
   * Validates the schema.
   */
  public void check() throws TileDBError {
    ctx.handle_error(tiledb.tiledb_array_schema_check(ctx.getCtxp(), schemap));
  }

  /**
   * Gets all attributes in the array.
   */
  HashMap<String, Attribute> attributes() throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = Utils.new_tiledb_attribute_tpp();
    HashMap<String,Attribute> result = new HashMap<String, Attribute>();
    SWIGTYPE_p_unsigned_int nattrp = tiledb.new_uintp();
    ctx.handle_error(
        tiledb.tiledb_array_schema_get_attribute_num(ctx.getCtxp(), schemap, nattrp));
    long nattr = tiledb.uintp_value(nattrp);
    for (long i = 0; i < nattr; ++i) {
      ctx.handle_error(tiledb.tiledb_array_schema_get_attribute_from_index(
          ctx.getCtxp(), schemap, i, attrpp));
      Attribute attr = new Attribute(ctx, attrpp);
      result.put(attr.getName(), attr);
    }
    return result;
  }

  /**
   * Gets an attribute by name.
   **/
  Attribute attribute(String name) throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = Utils.new_tiledb_attribute_tpp();
    ctx.handle_error(tiledb.tiledb_array_schema_get_attribute_from_name(
        ctx.getCtxp(), schemap, name, attrpp));
    return new Attribute(ctx, attrpp);
  }

  /**
   * Number of attributes.
   **/
  long attribute_num() throws TileDBError {
    SWIGTYPE_p_unsigned_int nump = tiledb.new_uintp();
    ctx.handle_error(
        tiledb.tiledb_array_schema_get_attribute_num(ctx.getCtxp(), schemap, nump));
    long num = tiledb.uintp_value(nump);
    tiledb.delete_uintp(nump);
    return num;
  }

  /**
   * Get an attribute by index
   **/
  Attribute attribute(long i) throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = Utils.new_tiledb_attribute_tpp();
    ctx.handle_error(tiledb.tiledb_array_schema_get_attribute_from_index(
        ctx.getCtxp(), schemap, i, attrpp));
    return new Attribute(ctx, attrpp);
  }

  public SWIGTYPE_p_p_tiledb_array_schema_t getSchemapp() {
    return schemapp;
  }

  public SWIGTYPE_p_tiledb_array_schema_t getSchemap() {
    return schemap;
  }

  public Context getCtx() {
    return ctx;
  }

  public String toString(tiledb_layout_t layout) {
    switch (layout) {
      case TILEDB_GLOBAL_ORDER:
        return "GLOBAL";
      case TILEDB_ROW_MAJOR:
        return "ROW-MAJOR";
      case TILEDB_COL_MAJOR:
        return "COL-MAJOR";
      case TILEDB_UNORDERED:
        return "UNORDERED";
    }
    return "";
  }

  public String toString(tiledb_array_type_t type) {
    return type == tiledb_array_type_t.TILEDB_DENSE ? "DENSE" : "SPARSE";
  }

  @Override
  public String toString() {
    try {
      StringBuilder s = new StringBuilder("ArraySchema<");
      s.append(toString(getArrayType()));
      s.append(" ");
      s.append(domain());
      for(Map.Entry e: attributes().entrySet()){
        s.append(" ");
        s.append(e.getValue());
      }
      s.append(">");
      return s.toString();
    }catch (Exception e){
      e.printStackTrace();
      return "";
    }
  }

  public void free() {
  }
}
