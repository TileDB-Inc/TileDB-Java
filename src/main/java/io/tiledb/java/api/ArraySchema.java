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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Schema describing an array.
 *
 * @details The schema is an independent description of an array. A schema can be
 * used to create multiple array's, and stores information about its
 * getDomain, cell types, and compression details. An array schema is composed of:
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
 * Domain getDomain = new Domain(...);
 * <p>
 * // Create Attributes
 * Attribute a1 = Attribute.create(...);
 * <p>
 * schema.setDomain(getDomain);
 * schema.addAttribute(a1);
 * <p>
 * // Specify tile memory layout
 * schema.setTileOrder(tiledb.TILEDB_GLOBAL_ORDER);
 * // Specify cell memory layout within each tile
 * schema.setCellOrder(tiledb.TILEDB_GLOBAL_ORDER);
 * schema.setCapacity(10); // For sparse, set capacity of each tile
 * <p>
 * Array.create(schema, "my_array"); // Make array with schema
 * ArraySchema s = ArraySchema(ctx, "my_array"); // Load schema from array
 * @endcode
 */
public class ArraySchema implements AutoCloseable {

  private SWIGTYPE_p_p_tiledb_array_schema_t schemapp;
  private SWIGTYPE_p_tiledb_array_schema_t schemap;
  private Context ctx;
  private HashMap<String,Attribute> attributes;

  /**
   * Creates a new array schema.
   */
  public ArraySchema(Context ctx, tiledb_array_type_t type) throws TileDBError {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    schemapp = Utils.new_tiledb_array_schema_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_create(ctx.getCtxp(), schemapp, type));
    schemap = Utils.tiledb_array_schema_tpp_value(schemapp);
  }

  /**
   * Loads the schema of an existing array with the input URI.
   */
  public ArraySchema(Context ctx, String uri) throws TileDBError {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    schemapp = Utils.new_tiledb_array_schema_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_load(ctx.getCtxp(), schemapp, uri));
    schemap = Utils.tiledb_array_schema_tpp_value(schemapp);
  }

  /**
   * Dumps the array schema in an ASCII representation to stdout.
   */
  public void dump() throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_dump_stdout(ctx.getCtxp(), schemap));
  }

  /**
   * Returns the array getType.
   */
  public tiledb_array_type_t getArrayType() throws TileDBError {
    SWIGTYPE_p_tiledb_array_type_t typep = tiledb.new_tiledb_array_type_tp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_array_type(ctx.getCtxp(), schemap, typep));
    tiledb_array_type_t type = tiledb.tiledb_array_type_tp_value(typep);
    tiledb.delete_tiledb_array_type_tp(typep);
    return type;
  }

  /**
   * Returns the tile capacity.
   */
  public long getCapacity() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long capacityp = tiledb.new_ullp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_capacity(ctx.getCtxp(), schemap, capacityp));
    long capacity = tiledb.ullp_value(capacityp).longValue();
    tiledb.delete_ullp(capacityp);
    return capacity;
  }

  /**
   * Sets the tile capacity.
   */
  public void setCapacity(long capacity) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_capacity(ctx.getCtxp(), schemap, new BigInteger(capacity+"")));
  }

  /**
   * Returns the tile order.
   */
  public tiledb_layout_t getTileOrder() throws TileDBError {
    SWIGTYPE_p_tiledb_layout_t layoutpp = tiledb.new_tiledb_layout_tp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_tile_order(ctx.getCtxp(), schemap, layoutpp));
    tiledb_layout_t layout = tiledb.tiledb_layout_tp_value(layoutpp);
    tiledb.delete_tiledb_layout_tp(layoutpp);
    return layout;
  }

  /**
   * Sets the tile order.
   */
  public void setTileOrder(tiledb_layout_t layout) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_tile_order(ctx.getCtxp(), schemap, layout));
  }

  /**
   * Sets both the tile and cell layouts
   *
   * @param tile_layout Tile layout
   * @param cell_layout Cell layout
   */
  public void setOrder(tiledb_layout_t tile_layout, tiledb_layout_t cell_layout) throws TileDBError {
    setTileOrder(tile_layout);
    setCellOrder(cell_layout);
  }

  /**
   * Returns the cell order.
   */
  public tiledb_layout_t getCellOrder() throws TileDBError {
    SWIGTYPE_p_tiledb_layout_t layoutpp = tiledb.new_tiledb_layout_tp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_cell_order(ctx.getCtxp(), schemap, layoutpp));
    tiledb_layout_t layout = tiledb.tiledb_layout_tp_value(layoutpp);
    tiledb.delete_tiledb_layout_tp(layoutpp);
    return layout;
  }

  /**
   * Sets the cell order.
   */
  public void setCellOrder(tiledb_layout_t layout) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_cell_order(ctx.getCtxp(), schemap, layout));
  }

  /**
   * Returns the compressor of the coordinates.
   */
  public Compressor getCoordsCompressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressorp = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int levelp = tiledb.new_intp();
    ctx.handleError(tiledb.tiledb_array_schema_get_coords_compressor(
        ctx.getCtxp(), schemap, compressorp, levelp));
    Compressor compressor = new Compressor(tiledb.tiledb_compressor_tp_value(compressorp), tiledb.intp_value(levelp));
    tiledb.delete_tiledb_compressor_tp(compressorp);
    tiledb.delete_intp(levelp);
    return compressor;
  }

  /**
   * Sets the compressor for the coordinates.
   */
  public void setCoordsCompressor(Compressor compressor) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_set_coords_compressor(
        ctx.getCtxp(), schemap, compressor.getCompressor(), compressor.getLevel()));
  }

  /**
   * Returns the compressor of the offsets.
   */
  public Compressor getOffsetsCompressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressorp = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int levelp = tiledb.new_intp();
    ctx.handleError(tiledb.tiledb_array_schema_get_offsets_compressor(
        ctx.getCtxp(), schemap, compressorp, levelp));
    Compressor compressor = new Compressor(tiledb.tiledb_compressor_tp_value(compressorp), tiledb.intp_value(levelp));
    tiledb.delete_tiledb_compressor_tp(compressorp);
    tiledb.delete_intp(levelp);
    return compressor;
  }

  /**
   * Sets the compressor for the offsets.
   */
  public void setOffsetsCompressor(Compressor compressor) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_set_offsets_compressor(
        ctx.getCtxp(), schemap, compressor.getCompressor(), compressor.getLevel()));
  }

  /**
   * Retruns the array getDomain of array.
   */
  public Domain getDomain() throws TileDBError {
    SWIGTYPE_p_p_tiledb_domain_t domainpp = Utils.new_tiledb_domain_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_get_domain(ctx.getCtxp(), schemap, domainpp));
    return new Domain(ctx, domainpp);
  }

  /**
   * Sets the array getDomain.
   */
  public void setDomain(Domain domain) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_domain(ctx.getCtxp(), schemap, domain.getDomainp()));
  }

  /**
   * Adds an getAttribute to the array.
   */
  public void addAttribute(Attribute attr) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_add_attribute(ctx.getCtxp(), schemap, attr.getAttributep()));
  }

  /**
   * Validates the schema.
   */
  public void check() throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_check(ctx.getCtxp(), schemap));
  }

  /**
   * Gets all getAttributes in the array.
   */
  public HashMap<String, Attribute> getAttributes() throws TileDBError {
    if(attributes == null) {
      attributes = new HashMap<String, Attribute>();
      SWIGTYPE_p_p_tiledb_attribute_t attrpp = Utils.new_tiledb_attribute_tpp();
      SWIGTYPE_p_unsigned_int nattrp = tiledb.new_uintp();
      ctx.handleError(
          tiledb.tiledb_array_schema_get_attribute_num(ctx.getCtxp(), schemap, nattrp));
      long nattr = tiledb.uintp_value(nattrp);
      for (long i = 0; i < nattr; ++i) {
        ctx.handleError(tiledb.tiledb_array_schema_get_attribute_from_index(
            ctx.getCtxp(), schemap, i, attrpp));
        Attribute attr = new Attribute(ctx, attrpp);
        attributes.put(attr.getName(), attr);
      }
    }
    return attributes;
  }

  /**
   * Gets an getAttribute by getName.
   **/
  public Attribute getAttribute(String name) throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = Utils.new_tiledb_attribute_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_get_attribute_from_name(
        ctx.getCtxp(), schemap, name, attrpp));
    return new Attribute(ctx, attrpp);
  }

  /**
   * Number of getAttributes.
   **/
  public long getAttributeNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int nump = tiledb.new_uintp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_attribute_num(ctx.getCtxp(), schemap, nump));
    long num = tiledb.uintp_value(nump);
    tiledb.delete_uintp(nump);
    return num;
  }

  /**
   * Get an getAttribute by index
   **/
  public Attribute getAttribute(long i) throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = Utils.new_tiledb_attribute_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_get_attribute_from_index(
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
      s.append(getDomain());
      for(Map.Entry e: getAttributes().entrySet()){
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

  /**
   * Delete the native object.
   */
  public void close() throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_free(ctx.getCtxp(), schemapp));
    for(Map.Entry<String,Attribute> e : getAttributes().entrySet()){
      e.getValue().close();
    }
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
