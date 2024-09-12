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

import io.tiledb.libtiledb.*;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * Array schema describing an array.
 *
 * <p>The ArraySchema is an independent description of an array. A schema can be used to create
 * multiple array's, and stores information about its domain, cell types, and compression details.
 * An array schema is composed of:
 *
 * <ul>
 *   <li>A Domain
 *   <li>A set of Attributes
 *   <li>Memory layout definitions: tile and cell
 *   <li>Compression details for Array level factors like offsets and coordinates
 * </ul>
 *
 * <b>Example:</b>
 *
 * <pre>{@code
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
 *
 * // Specify cell memory layout within each tile
 * schema.setCellOrder(TILEDB_GLOBAL_ORDER);
 * schema.setCapacity(10); // For sparse, set capacity of each tile
 *
 * // Make array with schema
 * Array my_dense_array = new Array(ctx, "my_array", schema);
 *
 * // Load schema from array
 * ArraySchema s = ArraySchema(ctx, "my_array"); // Load schema from array
 * }</pre>
 */
public class ArraySchema implements AutoCloseable {

  private Context ctx;
  private ArrayType arrayType;
  private boolean domainIsSet = false;
  private HashMap<String, Attribute> attributes;

  private SWIGTYPE_p_tiledb_array_schema_t schemap;
  private SWIGTYPE_p_p_tiledb_array_schema_t schemapp;

  /**
   * Creates a new TileDB ArraySchema object
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
   * }
   * </pre>
   *
   * @param ctx TileDB context
   * @param type Array type, sparse or dense
   * @exception TileDBError A TileDB exception
   */
  public ArraySchema(Context ctx, ArrayType type) throws TileDBError {
    SWIGTYPE_p_p_tiledb_array_schema_t _schemapp = tiledb.new_tiledb_array_schema_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_alloc(ctx.getCtxp(), type.toSwigEnum(), _schemapp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_tpp(_schemapp);
      throw err;
    }
    this.ctx = ctx;
    this.arrayType = type;
    this.schemap = tiledb.tiledb_array_schema_tpp_value(_schemapp);
    this.schemapp = _schemapp;
  }

  protected ArraySchema(Context ctx, SWIGTYPE_p_p_tiledb_array_schema_t schemapp) {
    this.ctx = ctx;
    this.schemap = tiledb.tiledb_array_schema_tpp_value(schemapp);
    this.schemapp = schemapp;
  }

  /**
   * Loads the ArraySchema of an existing array with the given URI string. TODO ADD ENCRYPTION
   * comments
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   ArraySchema schema = new ArraySchema(ctx, "s3://bucket-name/array-name");
   * }
   * </pre>
   *
   * @param ctx TileDB context
   * @param uri URI string of array
   * @exception TileDBError A TileDB exception
   */
  public ArraySchema(Context ctx, String uri) throws TileDBError {
    SWIGTYPE_p_p_tiledb_array_schema_t _schemapp = tiledb.new_tiledb_array_schema_tpp();
    try {
      ctx.handleError(tiledb.tiledb_array_schema_load(ctx.getCtxp(), uri, _schemapp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_tpp(_schemapp);
      throw err;
    }
    this.ctx = ctx;
    this.schemap = tiledb.tiledb_array_schema_tpp_value(_schemapp);
    this.schemapp = _schemapp;
  }

  /**
   * Dumps the array schema in an ASCII representation to STDOUT.
   *
   * @exception TileDBError A TileDB exception
   */
  public void dump() throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_dump_stdout(ctx.getCtxp(), getSchemap()));
  }

  /**
   * Dumps the array schema text representation to a file.
   *
   * @param filename The local file path to save the schema text representation
   * @exception TileDBError A TileDB exception
   */
  public void dump(String filename) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_dump_file(ctx.getCtxp(), getSchemap(), filename));
  }

  /**
   * Returns the type of the TileDB Array
   *
   * @return ArrayType enum value
   * @exception TileDBError A TileDB exception
   */
  public ArrayType getArrayType() throws TileDBError {
    if (arrayType == null) {
      SWIGTYPE_p_tiledb_array_type_t typep = tiledb.new_tiledb_array_type_tp();
      try {
        ctx.handleError(
            tiledb.tiledb_array_schema_get_array_type(ctx.getCtxp(), getSchemap(), typep));
        arrayType = ArrayType.fromSwigEnum(tiledb.tiledb_array_type_tp_value(typep));
      } finally {
        tiledb.delete_tiledb_array_type_tp(typep);
      }
    }
    return arrayType;
  }

  /**
   * Returns true if the array schema describes a sparse array
   *
   * @return true if the ArrayType is TILEDB_SPARSE
   * @exception TileDBError A TileDB exception
   */
  public boolean isSparse() throws TileDBError {
    return getArrayType() == ArrayType.TILEDB_SPARSE;
  }

  /**
   * Returns the tile capacity for the array. The tile capacity is associated with the array schema.
   *
   * @return The ArraySchema tile capacity
   * @exception TileDBError A TileDB exception
   */
  public long getCapacity() throws TileDBError {
    long capacity;
    SWIGTYPE_p_unsigned_long_long capacityp = tiledb.new_ullp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_capacity(ctx.getCtxp(), getSchemap(), capacityp));
      capacity = tiledb.ullp_value(capacityp).longValue();
    } finally {
      tiledb.delete_ullp(capacityp);
    }
    return capacity;
  }

  /**
   * Sets the tile capacity.
   *
   * @param capacity Capacity value to set
   * @exception TileDBError A TileDB exception
   */
  public void setCapacity(long capacity) throws TileDBError {
    setCapacity(BigInteger.valueOf(capacity));
  }

  /**
   * Sets the tile capacity.
   *
   * @param capacity value to set
   * @exception TileDBError A T
   */
  public void setCapacity(BigInteger capacity) throws TileDBError {
    Util.checkBigIntegerRange(capacity);
    ctx.handleError(tiledb.tiledb_array_schema_set_capacity(ctx.getCtxp(), schemap, capacity));
  }

  /**
   * Returns the tile layout order.
   *
   * @return The Layout order
   * @exception TileDBError A TileDB exception
   */
  public Layout getTileOrder() throws TileDBError {
    Layout tileOrder;
    SWIGTYPE_p_tiledb_layout_t layoutpp = tiledb.new_tiledb_layout_tp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_tile_order(ctx.getCtxp(), getSchemap(), layoutpp));
      tileOrder = Layout.fromSwigEnum(tiledb.tiledb_layout_tp_value(layoutpp));
    } finally {
      tiledb.delete_tiledb_layout_tp(layoutpp);
    }
    return tileOrder;
  }

  /**
   * Sets the tile order.
   *
   * @param layout tile Layout order
   * @exception TileDBError A TileDB exception
   */
  public ArraySchema setTileOrder(Layout layout) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_tile_order(
            ctx.getCtxp(), getSchemap(), layout.toSwigEnum()));
    return this;
  }

  /**
   * Returns the cell layout for the array schema.
   *
   * @return cell Layout order
   * @exception TileDBError A TileDB exception
   */
  public Layout getCellOrder() throws TileDBError {
    Layout cellOrder;
    SWIGTYPE_p_tiledb_layout_t layoutpp = tiledb.new_tiledb_layout_tp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_cell_order(ctx.getCtxp(), getSchemap(), layoutpp));
      cellOrder = Layout.fromSwigEnum(tiledb.tiledb_layout_tp_value(layoutpp));
    } finally {
      tiledb.delete_tiledb_layout_tp(layoutpp);
    }
    return cellOrder;
  }

  /**
   * Sets the cell order for the array schema.
   *
   * @param layout cell Layout order
   * @exception TileDBError A TileDB exception
   */
  public ArraySchema setCellOrder(Layout layout) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_cell_order(
            ctx.getCtxp(), getSchemap(), layout.toSwigEnum()));
    return this;
  }

  /**
   * @return The array Domain object.
   * @exception TileDBError A TileDB exception
   */
  public Domain getDomain() throws TileDBError {
    SWIGTYPE_p_p_tiledb_domain_t domainpp = tiledb.new_tiledb_domain_tpp();
    try {
      ctx.handleError(tiledb.tiledb_array_schema_get_domain(ctx.getCtxp(), getSchemap(), domainpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_domain_tpp(domainpp);
      throw err;
    }
    return new Domain(ctx, domainpp);
  }

  /**
   * Returns a copy of the schema's array currentDomain. To change the currentDomain, use
   * `set_current_domain()`
   *
   * @return The current domain
   * @throws TileDBError
   */
  public CurrentDomain getCurrentDomain() throws TileDBError {
    SWIGTYPE_p_p_tiledb_current_domain_t currentDomainpp = tiledb.new_tiledb_current_domain_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_current_domain(
              ctx.getCtxp(), getSchemap(), currentDomainpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_current_domain_tpp(currentDomainpp);
      throw err;
    }
    return new CurrentDomain(ctx, getDomain(), currentDomainpp);
  }

  /**
   * Sets the array Domain.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
   *   // Create a Domain
   *   Domain domain = new Domain(ctx);
   *   domain.addDimension(...);
   *   schema.setDomain(domain);
   * }</pre>
   *
   * @param domain Domain to use
   * @exception TileDBError A TileDB exception
   */
  public void setDomain(Domain domain) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_domain(ctx.getCtxp(), getSchemap(), domain.getDomainp()));
    domainIsSet = true;
  }

  /**
   * Sets the currentDomain.
   *
   * @param currentDomain The current domain to set
   * @throws TileDBError
   */
  public void setCurrentDomain(CurrentDomain currentDomain) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_current_domain(
            ctx.getCtxp(), getSchemap(), currentDomain.getCurrentDomainp()));
    domainIsSet = true;
  }

  /**
   * Adds an Attribute to the array.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
   *   Attribute attr = new Attribute(ctx, "a", Integer.class);
   *   schema.addAttribute(attr);
   * }</pre>
   *
   * @param attr The Attribute to add
   * @exception TileDBError A TileDB exception
   */
  public void addAttribute(Attribute attr) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_add_attribute(
            ctx.getCtxp(), getSchemap(), attr.getAttributep()));
  }

  /**
   * Validates the schema, throws a TileDBError if the ArraySchema is invalid.
   *
   * @exception TileDBError A TileDB exception
   */
  public void check() throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_schema_check(ctx.getCtxp(), getSchemap()));
  }

  /**
   * Get all Attributes of the array.
   *
   * @return HashMap of attribute names along with the corresponding Attribute objects.
   * @exception TileDBError A TileDB exception
   */
  public HashMap<String, Attribute> getAttributes() throws TileDBError {
    if (attributes == null) {
      attributes = new HashMap<String, Attribute>();
      SWIGTYPE_p_unsigned_int nattrp = tiledb.new_uintp();
      long nattr;
      try {
        ctx.handleError(
            tiledb.tiledb_array_schema_get_attribute_num(ctx.getCtxp(), schemap, nattrp));
        nattr = tiledb.uintp_value(nattrp);
      } finally {
        tiledb.delete_uintp(nattrp);
      }
      for (long i = 0; i < nattr; ++i) {
        SWIGTYPE_p_p_tiledb_attribute_t attrpp = tiledb.new_tiledb_attribute_tpp();
        try {
          ctx.handleError(
              tiledb.tiledb_array_schema_get_attribute_from_index(
                  ctx.getCtxp(), schemap, i, attrpp));
        } catch (TileDBError err) {
          tiledb.delete_tiledb_attribute_tpp(attrpp);
          throw err;
        }
        Attribute attr = new Attribute(ctx, attrpp);
        attributes.put(attr.getName(), attr);
      }
    }
    return attributes;
  }

  /**
   * @return The number of attributes of the array.
   * @throws TileDBError A TileDB exception
   */
  public long getAttributeNum() throws TileDBError {
    long num;
    SWIGTYPE_p_unsigned_int nump = tiledb.new_uintp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_attribute_num(ctx.getCtxp(), getSchemap(), nump));
      num = tiledb.uintp_value(nump);
    } finally {
      tiledb.delete_uintp(nump);
    }
    return num;
  }

  /**
   * Checks if the ArraySchema has the given attribute with name
   *
   * @param name The name of the attribute
   * @return True if the array schema has an attribute with the given name
   * @throws TileDBError
   */
  public boolean hasAttribute(String name) throws TileDBError {
    SWIGTYPE_p_int hasAttribute = tiledb.new_intp();

    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_has_attribute(
              ctx.getCtxp(), getSchemap(), name, hasAttribute));
      boolean result = tiledb.intp_value(hasAttribute) > 0;
      return result;
    } finally {
      tiledb.delete_intp(hasAttribute);
    }
  }

  /**
   * Get an Attribute by name
   *
   * @param name The name of the attribute.
   * @return Attribute object.
   * @exception TileDBError A TileDB exception
   */
  public Attribute getAttribute(String name) throws TileDBError {
    Attribute attr;
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = tiledb.new_tiledb_attribute_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_attribute_from_name(
              ctx.getCtxp(), getSchemap(), name, attrpp));
      attr = new Attribute(ctx, attrpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_attribute_tpp(attrpp);
      throw err;
    }
    return attr;
  }

  /**
   * Get an Attribute by index
   *
   * @param index The attribute index.
   * @return Attribute object.
   * @throws TileDBError A TileDB exception
   */
  public Attribute getAttribute(long index) throws TileDBError {
    Attribute attr;
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = tiledb.new_tiledb_attribute_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_attribute_from_index(
              ctx.getCtxp(), schemap, index, attrpp));
      attr = new Attribute(ctx, attrpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_attribute_tpp(attrpp);
      throw err;
    }
    return attr;
  }

  /**
   * Sets the filter list to use for the validity array of nullable attribute values.
   *
   * @param filterList FilterList to use
   * @return This ArraySchema instance
   * @throws TileDBError
   */
  public ArraySchema setValidityFilterList(FilterList filterList) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_validity_filter_list(
            ctx.getCtxp(), getSchemap(), filterList.getFilterListp()));
    return this;
  }

  /**
   * Retrieves the filter list used for validity maps.
   *
   * @return coordinates FilterList
   * @throws TileDBError
   */
  public FilterList getValidityFilterList() throws TileDBError {
    FilterList filterlist;
    SWIGTYPE_p_p_tiledb_filter_list_handle_t filterlistpp = tiledb.new_tiledb_filter_list_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_validity_filter_list(
              ctx.getCtxp(), getSchemap(), filterlistpp));
      filterlist = new FilterList(ctx, filterlistpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_filter_list_tpp(filterlistpp);
      throw err;
    }
    return filterlist;
  }

  /**
   * Sets the FilterList for the coordinates, which is an ordered list of filters that will be used
   * to process and/or transform the coordinate data (such as compression).
   *
   * @param filterList FilterList to use
   * @return This ArraySchema instance
   * @throws TileDBError A TileDB exception
   */
  public ArraySchema setCoodsFilterList(FilterList filterList) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_coords_filter_list(
            ctx.getCtxp(), getSchemap(), filterList.getFilterListp()));
    return this;
  }

  /**
   * Returns a copy of the FilterList of the coordinates.
   *
   * @return coordinates FilterList
   * @throws TileDBError A TileDB exception
   */
  public FilterList getCoordsFilterList() throws TileDBError {
    FilterList filterlist;
    SWIGTYPE_p_p_tiledb_filter_list_handle_t filterlistpp = tiledb.new_tiledb_filter_list_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_coords_filter_list(
              ctx.getCtxp(), getSchemap(), filterlistpp));
      filterlist = new FilterList(ctx, filterlistpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_filter_list_tpp(filterlistpp);
      throw err;
    }
    return filterlist;
  }

  /**
   * Sets the FilterList for the offsets, which is an ordered list of filters that will be used to
   * process and/or transform the offsets data (such as compression).
   *
   * @param filterList FilterList to use
   * @return This ArraySchema instance
   * @throws TileDBError A TileDB exception
   */
  public ArraySchema setOffsetsFilterList(FilterList filterList) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_offsets_filter_list(
            ctx.getCtxp(), getSchemap(), filterList.getFilterListp()));
    return this;
  }

  /**
   * Get timestamp range of schema.
   *
   * @return timestamp range of schema
   * @throws TileDBError
   */
  public Pair<Long, Long> getTimestampRange() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long t1 = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long t2 = tiledb.new_ullp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_timestamp_range(ctx.getCtxp(), getSchemap(), t1, t2));
      return new Pair(tiledb.ullp_value(t1), tiledb.ullp_value(t2));
    } finally {
      tiledb.delete_ullp(t1);
      tiledb.delete_ullp(t2);
    }
  }

  /**
   * Returns a copy of the FilterList of the offsets.
   *
   * @return offsets FilterList
   * @throws TileDBError A TileDB exception
   */
  public FilterList getOffsetsFilterList() throws TileDBError {
    FilterList filterlist;
    SWIGTYPE_p_p_tiledb_filter_list_handle_t filterlistpp = tiledb.new_tiledb_filter_list_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_offsets_filter_list(
              ctx.getCtxp(), getSchemap(), filterlistpp));
      filterlist = new FilterList(ctx, filterlistpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_filter_list_tpp(filterlistpp);
      throw err;
    }
    return filterlist;
  }

  /**
   * Sets whether the array can allow coordinate duplicates or not. Applicable only to sparse arrays
   * (it errors out if set to `1` for dense arrays).
   *
   * @param allowsDups The allowDups parameter, which allows duplicate coordinates to be inserted
   *     it's set to `1`
   * @throws TileDBError
   */
  public void setAllowDups(int allowsDups) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_allows_dups(ctx.getCtxp(), getSchemap(), allowsDups));
  }

  @Deprecated
  /**
   * Checks wether duplicate coordinates are allowed in the array schema
   *
   * @return `1` if duplicate coordinates are allowed in that array schema
   * @throws TileDBError
   */
  public int getAllowDups() throws TileDBError {
    SWIGTYPE_p_int allowsDupsPtr = tiledb.new_intp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_allows_dups(ctx.getCtxp(), getSchemap(), allowsDupsPtr));

      return tiledb.intp_value(allowsDupsPtr);
    } finally {
      tiledb.delete_intp(allowsDupsPtr);
    }
  }

  /**
   * Checks wether duplicate coordinates are allowed in the array schema
   *
   * @return `1` if duplicate coordinates are allowed in that array schema
   * @throws TileDBError
   */
  public boolean getAllowDuplicates() throws TileDBError {
    SWIGTYPE_p_int allowsDupsPtr = tiledb.new_intp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_allows_dups(ctx.getCtxp(), getSchemap(), allowsDupsPtr));

      return tiledb.intp_value(allowsDupsPtr) > 0;
    } finally {
      tiledb.delete_intp(allowsDupsPtr);
    }
  }

  /**
   * Sets a filter on a dimension label filter in an array schema.
   *
   * @param name The dimension label name.
   * @param filterList The filter_list to be set.
   */
  public void setDimensionLabelFilterList(String name, FilterList filterList) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_set_dimension_label_filter_list(
            ctx.getCtxp(), getSchemap(), name, filterList.getFilterListp()));
  }

  /**
   * Sets the tile extent on a dimension label in an array schema.
   *
   * @param name The dimension label name.
   * @param labelType The type of the dimension the tile extent is being set on.
   * @param tileExtend The tile extent for the dimension of the dimension label.
   *     <p>Note: The dimension label tile extent must be the same datatype as the dimension it is
   *     set on, not as the label.
   */
  public void setDimensionLabelTileExtend(String name, long tileExtend, Datatype labelType)
      throws TileDBError {
    NativeArray arr = new NativeArray(ctx, 1, Datatype.TILEDB_UINT64);
    arr.setItem(0, tileExtend);
    ctx.handleError(
        tiledb.tiledb_array_schema_set_dimension_label_tile_extent(
            ctx.getCtxp(), getSchemap(), name, labelType.toSwigEnum(), arr.toVoidPointer()));
    arr.close();
  }

  /**
   * Adds a DimensionLabel to the array schema.
   *
   * @param dimensionLabel The dimension label object
   */
  public void addDimensionLabel(DimensionLabel dimensionLabel) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_add_dimension_label(
            ctx.getCtxp(),
            getSchemap(),
            dimensionLabel.getDimensionIndex(),
            dimensionLabel.getName(),
            dimensionLabel.getLabelOrder(),
            dimensionLabel.getLabelType().toSwigEnum()));
  }

  /**
   * Retrieves a dimension label from an array schema with the requested name.
   *
   * @param name Name of the target dimension label to return.
   * @return a DimensionLabel object
   */
  public DimensionLabel getDimensionLabelFromName(String name) {
    SWIGTYPE_p_p_tiledb_dimension_label_handle_t dimensioLabelpp =
        tiledb.new_tiledb_dimension_label_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_dimension_label_from_name(
              ctx.getCtxp(), getSchemap(), name, dimensioLabelpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_dimension_label_tpp(dimensioLabelpp);
    }
    return new DimensionLabel(ctx, dimensioLabelpp);
  }

  /**
   * Checks if the schema has a dimension label of the given name.
   *
   * @param name Name of the target dimension label to check for.
   * @return True if the ArraySchema has a label with the given name
   */
  public boolean hasDimensionLabel(String name) throws TileDBError {
    SWIGTYPE_p_int hasDimLabel = tiledb.new_intp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_has_dimension_label(
              ctx.getCtxp(), getSchemap(), name, hasDimLabel));
      return tiledb.intp_value(hasDimLabel) > 0;
    } finally {
      tiledb.delete_intp(hasDimLabel);
    }
  }

  /**
   * Add an enumeration to the array schema.
   *
   * @param enumeration The enumeration to add
   * @throws TileDBError
   */
  public void addEnumeration(Enumeration enumeration) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_add_enumeration(
            ctx.getCtxp(), getSchemap(), enumeration.getEnumerationp()));
  }

  /**
   * Returns the array schema version.
   *
   * @return the array schema version
   * @throws TileDBError
   */
  public long getVersion() throws TileDBError {
    SWIGTYPE_p_unsigned_int versionPtr = tiledb.new_uintp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_get_version(ctx.getCtxp(), getSchemap(), versionPtr));

      return tiledb.uintp_value(versionPtr);
    } finally {
      tiledb.delete_uintp(versionPtr);
    }
  }

  protected SWIGTYPE_p_tiledb_array_schema_t getSchemap() {
    return schemap;
  }

  public SWIGTYPE_p_p_tiledb_array_schema_t getSchemapp() {
    return schemapp;
  }

  /** @return The schema Context. */
  public Context getCtx() {
    return ctx;
  }

  /** @return A String representation for the schema. */
  @Override
  public String toString() {
    SWIGTYPE_p_p_tiledb_string_handle_t dump = tiledb.new_tiledb_string_handle_tpp();
    TileDBString ts = null;

    try {
      ctx.handleError(tiledb.tiledb_array_schema_dump_str(ctx.getCtxp(), schemap, dump));
      ts = new TileDBString(ctx, dump);
      return ts.getView().getFirst();
    } catch (TileDBError error) {
      return "Dump not available";
    } finally {
      if (ts != null) ts.close();
    }
  }

  /** Free's native TileDB resources associated with the ArraySchema object */
  public void close() {
    if (schemap != null) {
      tiledb.tiledb_array_schema_free(schemapp);
      tiledb.delete_tiledb_array_schema_tpp(schemapp);
      schemap = null;
      schemapp = null;
    }
  }
}
