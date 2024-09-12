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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents the domain of an array. <br>
 * A Domain defines the set of Dimension objects for a given array. The properties of a Domain
 * derive from the underlying dimensions. A Domain is a component of an ArraySchema. <br>
 * <b>Note:</b> The dimension can only be integral types, as well as floating point for sparse array
 * domains.
 *
 * <p><b>Example:</b>
 *
 * <pre>{@code
 * Context ctx = new Context();
 * Domain domain = new Domain(ctx);
 *
 * // Note the dimension bounds are inclusive.
 * Dimension<Integer> d1 = new Dimension<Integer>(ctx, "d1", Integer.class, new Pair<Integer, Integer>(-10, 10), 1);
 * Dimension<Long> d2 = new Dimension<Long>(ctx, "d2", Long.class, new Pair<Long, Long>(1l, 10l), 1l);
 * Dimension<Integer> d3 = new Dimension<Integer>(ctx, "d3", Integer.class, new Pair<Integer, Integer>(-100, 100), 10);
 *
 * domain.addDimension(d1);
 * domain.addDimension(d2); // Throws error, all dims must be same type
 * domain.addDimension(d3);
 *
 * domain.getType(); // TILEDB_INT32, determined from the dimensions
 * domain.getRank(); // 2, d1 and d3
 *
 * ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
 * schema.setDomain(domain); // Set the array's domain
 * }</pre>
 */
public class Domain implements AutoCloseable {
  private Context ctx;
  private List<Dimension> dimensions;

  private SWIGTYPE_p_p_tiledb_domain_t domainpp;
  private SWIGTYPE_p_tiledb_domain_t domainp;

  protected Domain(Context ctx, SWIGTYPE_p_p_tiledb_domain_t domainpp) {
    this.ctx = ctx;
    this.domainpp = domainpp;
    this.domainp = tiledb.tiledb_domain_tpp_value(domainpp);
  }

  public Domain(Context ctx) throws TileDBError {
    SWIGTYPE_p_p_tiledb_domain_t _domainpp = tiledb.new_tiledb_domain_tpp();
    try {
      ctx.handleError(tiledb.tiledb_domain_alloc(ctx.getCtxp(), _domainpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_domain_tpp(_domainpp);
      throw err;
    }
    this.ctx = ctx;
    this.domainp = tiledb.tiledb_domain_tpp_value(_domainpp);
    this.domainpp = _domainpp;
  }

  protected SWIGTYPE_p_tiledb_domain_t getDomainp() {
    return domainp;
  }

  //  /**
  //   * Returns the total number of cells in the getDomain. Throws an exception
  //   * if the getDomain getType is `float32` or `float64`.
  //   */
  //  public long getCellNum() throws TileDBError {
  //    long ret = 1;
  //    for (Dimension dim : getDimensions()) {
  //      Pair d = dim.getDomain();
  //      ret *= (d.getSecond() - d.getFirst() + 1);
  //    }
  //    return ret;
  //
  //  }

  /**
   * Dumps the Domain in an ASCII representation to stdout.
   *
   * @exception TileDBError A TileDB exception
   */
  public void dump() throws TileDBError {
    ctx.handleError(tiledb.tiledb_domain_dump_stdout(ctx.getCtxp(), domainp));
  }

  /**
   * Dumps the Domain in an ASCII representation to stdout.
   *
   * @param filename A string filename
   * @exception TileDBError A TileDB exception
   */
  public void dump(String filename) throws TileDBError {
    ctx.handleError(tiledb.tiledb_domain_dump_file(ctx.getCtxp(), domainp, filename));
  }

  /**
   * @return The domain Enumerated type.
   * @exception TileDBError A TileDB exception
   */
  public Datatype getType() throws TileDBError {
    SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
    try {
      ctx.handleError(tiledb.tiledb_domain_get_type(ctx.getCtxp(), domainp, typep));
      tiledb_datatype_t type = tiledb.tiledb_datatype_tp_value(typep);
      return Datatype.fromSwigEnum(type);
    } finally {
      tiledb.delete_tiledb_datatype_tp(typep);
    }
  }

  @Deprecated
  /**
   * @return The rank of the domain (number of dimensions)
   * @exception TileDBError A TileDB exception @Note is replaced by getNDim();
   */
  public long getRank() throws TileDBError {
    SWIGTYPE_p_unsigned_int np = tiledb.new_uintp();
    try {
      ctx.handleError(tiledb.tiledb_domain_get_ndim(ctx.getCtxp(), domainp, np));
    } catch (TileDBError err) {
      tiledb.delete_uintp(np);
      throw err;
    }
    long rank = tiledb.uintp_value(np);
    tiledb.delete_uintp(np);
    return rank;
  }

  /**
   * @return The number of dimensions in the domain
   * @throws TileDBError A TileDB exception
   */
  public long getNDim() throws TileDBError {
    SWIGTYPE_p_unsigned_int np = tiledb.new_uintp();
    try {
      ctx.handleError(tiledb.tiledb_domain_get_ndim(ctx.getCtxp(), domainp, np));
      return tiledb.uintp_value(np);
    } finally {
      tiledb.delete_uintp(np);
    }
  }

  /**
   * @return A List containing the Dimensions in domain.
   * @exception TileDBError A TileDB exception
   */
  public List<Dimension> getDimensions() throws TileDBError {
    if (dimensions == null) {
      long rank = getNDim();
      dimensions = new ArrayList<Dimension>();
      for (long i = 0; i < rank; i++) {
        SWIGTYPE_p_p_tiledb_dimension_t dimpp = tiledb.new_tiledb_dimension_tpp();
        try {
          ctx.handleError(
              tiledb.tiledb_domain_get_dimension_from_index(ctx.getCtxp(), domainp, i, dimpp));
        } catch (TileDBError err) {
          tiledb.delete_tiledb_dimension_tpp(dimpp);
          throw err;
        }
        Dimension dim = new Dimension(ctx, dimpp);
        dimensions.add(dim);
      }
    }
    // return shallow copy of dimensions
    return new ArrayList<>(dimensions);
  }

  /**
   * Checks if the Domain has the given dimension with name
   *
   * @param name Name of the dimension in the domain
   * @return True if the dimension exists in the domain
   * @throws TileDBError
   */
  public boolean hasDimension(String name) throws TileDBError {
    SWIGTYPE_p_int hasDimension = tiledb.new_intp();
    try {
      ctx.handleError(
          tiledb.tiledb_domain_has_dimension(ctx.getCtxp(), getDomainp(), name, hasDimension));
      return tiledb.intp_value(hasDimension) > 0;
    } finally {
      tiledb.delete_intp(hasDimension);
    }
  }

  /**
   * Retrieves a Dimension Object from a Domain by name / label
   *
   * @param name The name of the domain dimension
   * @return A TileDB Dimension object
   * @throws TileDBError
   */
  public Dimension getDimension(String name) throws TileDBError {
    Dimension dim;
    SWIGTYPE_p_p_tiledb_dimension_t dimpp = tiledb.new_tiledb_dimension_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_domain_get_dimension_from_name(ctx.getCtxp(), getDomainp(), name, dimpp));
      dim = new Dimension(ctx, dimpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_dimension_tpp(dimpp);
      throw err;
    }
    return dim;
  }

  /**
   * Retrieves a Dimension Object from a Domain by index
   *
   * @param idx The index of the domain dimension
   * @return A TileDB Dimension object
   * @throws TileDBError
   */
  public Dimension getDimension(Long idx) throws TileDBError {
    Dimension dim;
    SWIGTYPE_p_p_tiledb_dimension_t dimpp = tiledb.new_tiledb_dimension_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_domain_get_dimension_from_index(ctx.getCtxp(), getDomainp(), idx, dimpp));
      dim = new Dimension(ctx, dimpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_dimension_tpp(dimpp);
      throw err;
    }
    return dim;
  }

  /**
   * Retrieves a Dimension Object from a Domain by index
   *
   * @param idx The index of the domain dimension
   * @return A TileDB Dimension object
   * @throws TileDBError
   */
  public Dimension getDimension(Integer idx) throws TileDBError {
    return getDimension(idx.longValue());
  }

  /**
   * Adds a new dimension to the domain.
   *
   * @param dimension The Dimension object to be added.
   * @exception TileDBError A TileDB exception
   */
  public Domain addDimension(Dimension dimension) throws TileDBError {
    if (dimensions == null) {
      dimensions = new ArrayList<Dimension>();
    }
    dimensions.add(dimension);
    ctx.handleError(
        tiledb.tiledb_domain_add_dimension(ctx.getCtxp(), domainp, dimension.getDimensionp()));
    return this;
  }

  /**
   * Adds multiple Dimensions.
   *
   * @param dims A list of Dimension objects to be added.
   * @exception TileDBError A TileDB exception
   */
  public void addDimensions(Collection<Dimension> dims) throws TileDBError {
    for (Dimension d : dims) {
      addDimension(d);
    }
  }

  /** @return A String representation for the Domain. */
  @Override
  public String toString() {
    SWIGTYPE_p_p_tiledb_string_handle_t dump = tiledb.new_tiledb_string_handle_tpp();
    TileDBString ts = null;

    try {
      ctx.handleError(tiledb.tiledb_domain_dump_str(ctx.getCtxp(), domainp, dump));
      ts = new TileDBString(ctx, dump);
      return ts.getView().getFirst();
    } catch (TileDBError error) {
      return "Dump not available";
    } finally {
      if (ts != null) ts.close();
    }
  }

  /** Free's native TileDB resources associated with the Domain object */
  public void close() {
    if (domainp != null) {
      tiledb.tiledb_domain_free(domainpp);
      tiledb.delete_tiledb_domain_tpp(domainpp);
      domainpp = null;
      domainp = null;
    }
  }
}
