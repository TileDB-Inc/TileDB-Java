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

import static io.tiledb.java.api.TileDBArrayTypeEnum.TILEDB_DENSE;
import static io.tiledb.java.api.TileDBArrayTypeEnum.TILEDB_SPARSE;

/**
 * Schema describing an array.
 *
 * @details The schema is an independent description of an array. A schema can be
 * used to create multiple array's, and stores information about its
 * domain, cell types, and compression details. An array schema is composed of:
 *
 * - A Domain
 * - A set of Attributes
 * - Memory layout definitions: tile and cell
 * - Compression details for Array level factors like offsets and coordinates
 *
 * **Example:**
 * @code{.java}
 * Context ctx = new Context();
 * ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
 *
 * // Create a Domain
 * Domain<Integer> domain = new Domain<Integer>(ctx);
 *
 * // Create Attribute
 * Attribute<Integer> a1 = new Attribute<Integer>(ctx, "a1", Integer.class);
 *
 * schema.setDomain(domain);
 * schema.addAttribute(a1);
 *
 * // Specify tile memory layout
 * schema.setTileOrder(TILEDB_GLOBAL_ORDER);
 * // Specify cell memory layout within each tile
 * schema.setCellOrder(TILEDB_GLOBAL_ORDER);
 * schema.setCapacity(10); // For sparse, set capacity of each tile
 *
 * Array my_dense_array = new Array(ctx, "my_array", schema); // Make array with schema
 * ArraySchema s = ArraySchema(ctx, "my_array"); // Load schema from array
 * @endcode
 */
public class ArraySchema implements AutoCloseable {

  private SWIGTYPE_p_p_tiledb_array_schema_t schemapp;
  private SWIGTYPE_p_tiledb_array_schema_t schemap;
  private Context ctx;
  private HashMap<String,Attribute> attributes;

  /**
   *
   * Creates a new array schema.
   *
   * **Example:**
   * @code{.java}
   * Context ctx = new Context();
   * ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
   * @endcode
   *
   * @param ctx TileDB context
   * @param type Array type, sparse or dense.
   * @throws TileDBError
   */
  public ArraySchema(Context ctx, TileDBArrayTypeEnum type) throws TileDBError {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    schemapp = tiledb.new_tiledb_array_schema_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_alloc(ctx.getCtxp(), type.toSwigEnum(), schemapp));
    schemap = tiledb.tiledb_array_schema_tpp_value(schemapp);
  }

  /**
   *
   * Loads the schema of an existing array with the input URI.
   *
   * **Example:**
   * @code{.java}
   * Context ctx = new Context();
   * ArraySchema schema = new ArraySchema(ctx, "s3://bucket-name/array-name");
   * @endcode
   *
   * @param ctx TileDB context
   * @param uri URI of array
   * @throws TileDBError
   */
  public ArraySchema(Context ctx, String uri) throws TileDBError {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    schemapp = tiledb.new_tiledb_array_schema_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_load(ctx.getCtxp(), uri, schemapp));
    schemap = tiledb.tiledb_array_schema_tpp_value(schemapp);
  }

  /**
   * Dumps the array schema in an ASCII representation to stdout.
   * @throws TileDBError
   */
  public void dump() throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_dump_stdout(ctx.getCtxp(), schemap));
  }

  /**
   * Dumps the array schema in an ASCII representation to a file.
   * @throws TileDBError
   */
  public void dump(String filename) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_dump_file(ctx.getCtxp(), schemap, filename));
  }

  /**
   * @return The array type.
   * @throws TileDBError
   */
  public TileDBArrayTypeEnum getArrayType() throws TileDBError {
    SWIGTYPE_p_tiledb_array_type_t typep = tiledb.new_tiledb_array_type_tp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_array_type(ctx.getCtxp(), schemap, typep));
    tiledb_array_type_t type = tiledb.tiledb_array_type_tp_value(typep);
    tiledb.delete_tiledb_array_type_tp(typep);
    return TileDBArrayTypeEnum.fromSwigEnum(type);
  }

  /**
   *
   * @return True if the array is sparse.
   * @throws TileDBError
   */
  public boolean isSparse() throws TileDBError {
    return getArrayType() == TILEDB_SPARSE;
  }

  /**
   *
   * @return The tile capacity.
   * @throws TileDBError
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
   *
   * @param capacity Capacity value to set.
   * @throws TileDBError
   */
  public void setCapacity(long capacity) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_capacity(ctx.getCtxp(), schemap, new BigInteger(capacity+"")));
  }

  /**
   *
   * @return The tile layout order.
   * @throws TileDBError
   */
  public TileDBLayoutEnum getTileOrder() throws TileDBError {
    SWIGTYPE_p_tiledb_layout_t layoutpp = tiledb.new_tiledb_layout_tp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_tile_order(ctx.getCtxp(), schemap, layoutpp));
    tiledb_layout_t layout = tiledb.tiledb_layout_tp_value(layoutpp);
    tiledb.delete_tiledb_layout_tp(layoutpp);
    return TileDBLayoutEnum.fromSwigEnum(layout);
  }

  /**
   * Sets the tile order.
   *
   * @param layout Tile order to set.
   * @throws TileDBError
   */
  public void setTileOrder(TileDBLayoutEnum layout) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_tile_order(ctx.getCtxp(), schemap, layout.toSwigEnum()));
  }

  /**
   * Sets both the tile and cell orders.
   *
   * @param tile_layout Tile order.
   * @param cell_layout Cell order.
   * @throws TileDBError
   */
  public void setOrder(TileDBLayoutEnum tile_layout, TileDBLayoutEnum cell_layout) throws TileDBError {
    setTileOrder(tile_layout);
    setCellOrder(cell_layout);
  }

  /**
   *
   * @return The cell order.
   * @throws TileDBError
   */
  public TileDBLayoutEnum getCellOrder() throws TileDBError {
    SWIGTYPE_p_tiledb_layout_t layoutpp = tiledb.new_tiledb_layout_tp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_cell_order(ctx.getCtxp(), schemap, layoutpp));
    tiledb_layout_t layout = tiledb.tiledb_layout_tp_value(layoutpp);
    tiledb.delete_tiledb_layout_tp(layoutpp);
    return TileDBLayoutEnum.fromSwigEnum(layout);
  }

  /**
   * Sets the cell order.
   *
   * @param layout Cell order to set.
   * @throws TileDBError
   */
  public void setCellOrder(TileDBLayoutEnum layout) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_cell_order(ctx.getCtxp(), schemap, layout.toSwigEnum()));
  }

  /**
   *
   * @return The Compressor of the coordinates.
   * @throws TileDBError
   */
  public Compressor getCoordsCompressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressorp = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int levelp = tiledb.new_intp();
    ctx.handleError(tiledb.tiledb_array_schema_get_coords_compressor(
        ctx.getCtxp(), schemap, compressorp, levelp));
    Compressor compressor = new Compressor(
        TileDBCompressorEnum.fromSwigEnum(tiledb.tiledb_compressor_tp_value(compressorp)),
        tiledb.intp_value(levelp));
    tiledb.delete_tiledb_compressor_tp(compressorp);
    tiledb.delete_intp(levelp);
    return compressor;
  }

  /**
   *
   * Sets the compressor for the coordinates.
   *
   * **Example:**
   * @code{.java}
   * Context ctx = new Context();
   * ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
   * schema.setCoordsCompressor(new Compressor(TILEDB_GZIP, -1));
   * @endcode
   *
   * @param compressor Compressor to use
   * @throws TileDBError
   */
  public void setCoordsCompressor(Compressor compressor) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_set_coords_compressor(
        ctx.getCtxp(), schemap, compressor.getCompressor().toSwigEnum(), compressor.getLevel()));
  }

  /**
   *
   * @return The Compressor of the offsets for variable-length attributes.
   * @throws TileDBError
   */
  public Compressor getOffsetsCompressor() throws TileDBError {
    SWIGTYPE_p_tiledb_compressor_t compressorp = tiledb.new_tiledb_compressor_tp();
    SWIGTYPE_p_int levelp = tiledb.new_intp();
    ctx.handleError(tiledb.tiledb_array_schema_get_offsets_compressor(
        ctx.getCtxp(), schemap, compressorp, levelp));
    Compressor compressor = new Compressor(
        TileDBCompressorEnum.fromSwigEnum(tiledb.tiledb_compressor_tp_value(compressorp)),
        tiledb.intp_value(levelp));
    tiledb.delete_tiledb_compressor_tp(compressorp);
    tiledb.delete_intp(levelp);
    return compressor;
  }

  /**
   * Sets the compressor for the offsets of variable-length attributes.
   *
   * **Example:**
   * @code{.java}
   * Context ctx = new Context();
   * ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
   * schema.setOffsetsCompressor(new Compressor(TILEDB_GZIP, -1));
   * @endcode
   *
   * @param compressor Compressor to use
   * @throws TileDBError
   */
  public void setOffsetsCompressor(Compressor compressor) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_set_offsets_compressor(
        ctx.getCtxp(), schemap, compressor.getCompressor().toSwigEnum(), compressor.getLevel()));
  }

  /**
   *
   * @return The array Domain object.
   * @throws TileDBError
   */
  public Domain getDomain() throws TileDBError {
    SWIGTYPE_p_p_tiledb_domain_t domainpp = tiledb.new_tiledb_domain_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_get_domain(ctx.getCtxp(), schemap, domainpp));
    return new Domain(ctx, domainpp);
  }

  /**
   * Sets the array domain.
   *
   * **Example:**
   * @code{.java}
   * Context ctx = new Context();
   * ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
   * // Create a Domain
   * Domain domain = new Domain(ctx);
   * domain.addDimension(...);
   * schema.setDomain(domain);
   * @endcode
   *
   * @param domain Domain to use
   */
  public void setDomain(Domain domain) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_domain(ctx.getCtxp(), schemap, domain.getDomainp()));
  }

  /**
   * Adds an Attribute to the array.
   *
   * **Example:**
   * @code{.java}
   * Context ctx = new Context();
   * ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
   * Attribute attr = new Attribute(ctx, "a", Integer.class);
   * schema.addAttribute(attr);
   * @endcode
   *
   * @param attr The Attribute to add
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
   * Get all Attributes of the array.
   *
   * @return HashMap of names along with the corresponding Attribute objects.
   * @throws TileDBError
   */
  public HashMap<String, Attribute> getAttributes() throws TileDBError {
    if(attributes == null) {
      attributes = new HashMap<String, Attribute>();
      SWIGTYPE_p_p_tiledb_attribute_t attrpp = tiledb.new_tiledb_attribute_tpp();
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
   * Get an Attribute by name.
   * @param name The name of the attribute.
   * @return Attribute object.
   * @throws TileDBError
   */
  public Attribute getAttribute(String name) throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = tiledb.new_tiledb_attribute_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_get_attribute_from_name(
        ctx.getCtxp(), schemap, name, attrpp));
    return new Attribute(ctx, attrpp);
  }

  /**
   *
   * @return The number of attributes of the array.
   * @throws TileDBError
   */
  public long getAttributeNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int nump = tiledb.new_uintp();
    ctx.handleError(
        tiledb.tiledb_array_schema_get_attribute_num(ctx.getCtxp(), schemap, nump));
    long num = tiledb.uintp_value(nump);
    tiledb.delete_uintp(nump);
    return num;
  }

  /**
   *
   * Get an Attribute by index
   * @param index The attribute index.
   * @return Attribute object.
   * @throws TileDBError
   */
  public Attribute getAttribute(long index) throws TileDBError {
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = tiledb.new_tiledb_attribute_tpp();
    ctx.handleError(tiledb.tiledb_array_schema_get_attribute_from_index(
        ctx.getCtxp(), schemap, index, attrpp));
    return new Attribute(ctx, attrpp);
  }

  protected SWIGTYPE_p_p_tiledb_array_schema_t getSchemapp() {
    return schemapp;
  }

  protected SWIGTYPE_p_tiledb_array_schema_t getSchemap() {
    return schemap;
  }

  /**
   *
   * @return The schema Context.
   */
  public Context getCtx() {
    return ctx;
  }

  /**
   *
   * @param layout The layout to be printed.
   * @return A String represantion of the layout.
   */
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

  /**
   *
   * @param type The type to be printed.
   * @return A String representation of the type.
   */
  public String toString(TileDBArrayTypeEnum type) {
    return type == TILEDB_DENSE ? "DENSE" : "SPARSE";
  }

  /**
   *
   * @return A String representation for the schema.
   */
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
    if(schemap!=null)
      tiledb.tiledb_array_schema_free(schemapp);
    if(attributes!=null) {
      for (Map.Entry<String, Attribute> e : attributes.entrySet()) {
        e.getValue().close();
      }
    }
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
