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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents the domain of an array.
 *
 * @details
 * A Domain defines the set of Dimension objects for a given array. The
 * properties of a Domain derive from the underlying dimensions. A
 * Domain is a component of an ArraySchema.
 *
 * @note The dimension can only be integral types, as well as floating point for sparse array domains.
 *
 * **Example:**
 *
 * @code{.java}
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
 * ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_SPARSE);
 * schema.setDomain(domain); // Set the array's domain
 *
 * @endcode
 **/
public class Domain implements AutoCloseable {
  private SWIGTYPE_p_p_tiledb_domain_t domainpp;
  private SWIGTYPE_p_tiledb_domain_t domainp;
  private Context ctx;
  private List<Dimension> dimensions;

  protected Domain(Context ctx, SWIGTYPE_p_p_tiledb_domain_t domainpp) {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    this.domainpp = domainpp;
    this.domainp = tiledb.tiledb_domain_tpp_value(domainpp);
  }

  public Domain(Context ctx) throws TileDBError {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    domainpp = tiledb.new_tiledb_domain_tpp();
    ctx.handleError(tiledb.tiledb_domain_alloc(ctx.getCtxp(), domainpp));
    this.domainp = tiledb.tiledb_domain_tpp_value(domainpp);
  }

  protected SWIGTYPE_p_p_tiledb_domain_t getDomainpp() {
    return domainpp;
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

  /** Dumps the Domain in an ASCII representation to stdout. */
  public void dump() throws TileDBError {
    ctx.handleError(tiledb.tiledb_domain_dump_stdout(ctx.getCtxp(), domainp));
  }

  /** Dumps the Domain in an ASCII representation to stdout. */
  public void dump(String filename) throws TileDBError {
    ctx.handleError(tiledb.tiledb_domain_dump_file(ctx.getCtxp(), domainp, filename));
  }

  /**
   *
   * @return The domain Enumerated type.
   * @throws TileDBError
   */
  public tiledb_datatype_t getType() throws TileDBError {
    SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
    ctx.handleError(tiledb.tiledb_domain_get_type(ctx.getCtxp(), domainp, typep));
    tiledb_datatype_t type = tiledb.tiledb_datatype_tp_value(typep);
    tiledb.delete_tiledb_datatype_tp(typep);
    return type;
  }

  /**
   *
   * @return The rank of the domain (number of dimensions)
   * @throws TileDBError
   */
  public long getRank() throws TileDBError {
    SWIGTYPE_p_unsigned_int np = tiledb.new_uintp();
    ctx.handleError(tiledb.tiledb_domain_get_ndim(ctx.getCtxp(), domainp, np));
    long rank = tiledb.uintp_value(np);
    tiledb.delete_uintp(np);
    return rank;
  }

  /**
   *
   * @return A List containing the Dimensions in domain.
   * @throws TileDBError
   */
  public List<Dimension> getDimensions() throws TileDBError {
    if(dimensions==null){
      long rank = getRank();
      dimensions = new ArrayList<Dimension>();
      for (long i=0; i<rank; i++){
        SWIGTYPE_p_p_tiledb_dimension_t dimpp = tiledb.new_tiledb_dimension_tpp();
        ctx.handleError(
            tiledb.tiledb_domain_get_dimension_from_index(ctx.getCtxp(), domainp, i, dimpp));
        Dimension dim = new Dimension(ctx, dimpp);
        dimensions.add(dim);
      }
    }
    return dimensions;
  }

  /**
   * Adds a new dimension to the domain.
   * @param dimension The Dimension object to be added.
   * @throws TileDBError
   */
  public void addDimension(Dimension dimension) throws TileDBError {
    if(dimensions==null){
      dimensions = new ArrayList<Dimension>();
    }
    dimensions.add(dimension);
    ctx.handleError(tiledb.tiledb_domain_add_dimension(ctx.getCtxp(), domainp, dimension.getDimensionp()));
  }

  /**
   * Adds multiple Dimensions.
   * @param dims A list of Dimension objects to be added.
   * @throws TileDBError
   */
  public void addDimensions(Collection<Dimension> dims) throws TileDBError {
    for (Dimension d : dims) {
      addDimension(d);
    }
  }

  /**
   * Delete the native object.
   */
  public void close() throws TileDBError {
    if(domainp!=null)
      tiledb.tiledb_domain_free(domainpp);

    if(dimensions!=null) {
      for (Dimension d : dimensions) {
        d.close();
      }
    }
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
