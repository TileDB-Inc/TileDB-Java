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
 * Represents the getDomain of an array.
 *
 * @details
 * A Domain defines the set of Dimension objects for a given array. The
 * properties of a Domain derive from the underlying getDimensions. A
 * Domain is a component of an ArraySchema.
 *
 * @note The dimension can only be signed or unsigned integral types.
 *
 * **Example:**
 *
 * @code{.cpp}
 *
 * tiledb::Context ctx;
 * tiledb::Domain getDomain;
 *
 * // Note the dimension bounds are inclusive.
 * auto d1 = tiledb::Dimension::create<int>(ctx, "d1", {-10, 10});
 * auto d2 = tiledb::Dimension::create<uint64_t>(ctx, "d2", {1, 10});
 * auto d3 = tiledb::Dimension::create<int>(ctx, "d3", {-100, 100});
 *
 * getDomain.addDimension(d1);
 * getDomain.addDimension(d2);
 * getDomain.addDimension(d3); // Invalid, all dims must be same getType
 *
 * getDomain.getCellNum(); // (10 - -10 + 1) * (10 - 1 + 1) = 210 max cells
 * getDomain.getType(); // TILEDB_UINT64, determined from the getDimensions
 * getDomain.getRank(); // 2, d1 and d2
 *
 * tiledb::ArraySchema schema(ctx, TILEDB_DENSE);
 * schema.setDomain(getDomain); // Set the array's getDomain
 *
 * @endcode
 *
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

  /** Dumps the getDomain in an ASCII representation to stdout. */
  public void dump() throws TileDBError {
    ctx.handleError(tiledb.tiledb_domain_dump_stdout(ctx.getCtxp(), domainp));
  }

  /** Returns the domain getType. */
  public tiledb_datatype_t getType() throws TileDBError {
    SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
    ctx.handleError(tiledb.tiledb_domain_get_type(ctx.getCtxp(), domainp, typep));
    tiledb_datatype_t type = tiledb.tiledb_datatype_tp_value(typep);
    tiledb.delete_tiledb_datatype_tp(typep);
    return type;
  }

  /** Get the rank (number of dimensions) **/
  public long getRank() throws TileDBError {
    SWIGTYPE_p_unsigned_int np = tiledb.new_uintp();
    ctx.handleError(tiledb.tiledb_domain_get_ndim(ctx.getCtxp(), domainp, np));
    long rank = tiledb.uintp_value(np);
    tiledb.delete_uintp(np);
    return rank;
  }

  /** Returns the current set of dimensions in domain. */
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

  /** Adds a new dimension to the domain. */
  public void addDimension(Dimension d) throws TileDBError {
    if(dimensions==null){
      dimensions = new ArrayList<Dimension>();
    }
    dimensions.add(d);
    ctx.handleError(tiledb.tiledb_domain_add_dimension(ctx.getCtxp(), domainp, d.getDimensionp()));
  }

  /** Add multiple Dimensions. **/
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
