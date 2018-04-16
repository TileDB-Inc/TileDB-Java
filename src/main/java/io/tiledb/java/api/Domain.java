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
 * @note The dimension can only be signed or unsigned integral types.
 *
 * **Example:**
 *
 * @code{.cpp}
 *
 * tiledb::Context ctx;
 * tiledb::Domain domain;
 *
 * // Note the dimension bounds are inclusive.
 * auto d1 = tiledb::Dimension::create<int>(ctx, "d1", {-10, 10});
 * auto d2 = tiledb::Dimension::create<uint64_t>(ctx, "d2", {1, 10});
 * auto d3 = tiledb::Dimension::create<int>(ctx, "d3", {-100, 100});
 *
 * domain.add_dimension(d1);
 * domain.add_dimension(d2);
 * domain.add_dimension(d3); // Invalid, all dims must be same type
 *
 * domain.cell_num(); // (10 - -10 + 1) * (10 - 1 + 1) = 210 max cells
 * domain.type(); // TILEDB_UINT64, determined from the dimensions
 * domain.rank(); // 2, d1 and d2
 *
 * tiledb::ArraySchema schema(ctx, TILEDB_DENSE);
 * schema.set_domain(domain); // Set the array's domain
 *
 * @endcode
 *
 **/
public class Domain<T> implements Finalizable {
  private SWIGTYPE_p_p_tiledb_domain_t domainpp;
  private SWIGTYPE_p_tiledb_domain_t domainp;
  private Context ctx;
  private List<Dimension> dimensions;

  public Domain(Context ctx, SWIGTYPE_p_p_tiledb_domain_t domainpp) {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    this.domainpp = domainpp;
    this.domainp = Utils.tiledb_domain_tpp_value(domainpp);
  }

  public Domain(Context ctx) throws TileDBError {
    ctx.deleterAdd(this);
    this.ctx = ctx;
    domainpp = Utils.new_tiledb_domain_tpp();
    ctx.handle_error(tiledb.tiledb_domain_create(ctx.getCtxp(), domainpp));
    this.domainp = Utils.tiledb_domain_tpp_value(domainpp);
  }

  public SWIGTYPE_p_p_tiledb_domain_t getDomainpp() {
    return domainpp;
  }

  public SWIGTYPE_p_tiledb_domain_t getDomainp() {
    return domainp;
  }

  /**
   * Returns the total number of cells in the domain. Throws an exception
   * if the domain type is `float32` or `float64`.
   */
  public long cell_num() throws TileDBError {
    long ret = 1;
    for (Dimension dim : dimensions) {
//      Pair<T,T> d = dim.domain();
//      ret *= (d.getSecond() - d.getFirst() + 1);
    }
    return ret;

  }

  /** Dumps the domain in an ASCII representation to an output. */
  public void dump() throws TileDBError {
    ctx.handle_error(tiledb.tiledb_domain_dump_stdout(ctx.getCtxp(), domainp));
  }

  /** Returns the domain type. */
  public tiledb_datatype_t type() throws TileDBError {
    SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
    ctx.handle_error(tiledb.tiledb_domain_get_type(ctx.getCtxp(), domainp, typep));
    tiledb_datatype_t type = tiledb.tiledb_datatype_tp_value(typep);
    tiledb.delete_tiledb_datatype_tp(typep);
    return type;
  }

  /** Get the rank (number of dimensions) **/
  public long rank() throws TileDBError {
    SWIGTYPE_p_unsigned_int np = tiledb.new_uintp();
    ctx.handle_error(tiledb.tiledb_domain_get_rank(ctx.getCtxp(), domainp, np));
    long rank = tiledb.uintp_value(np);
    tiledb.delete_uintp(np);
    return rank;
  }

  /** Returns the current set of dimensions in domain. */
  public List<Dimension> dimensions() throws TileDBError {
    if(dimensions==null){
      long rank = rank();
      dimensions = new ArrayList<Dimension>();
      for (long i=0; i<rank; i++){
        SWIGTYPE_p_p_tiledb_dimension_t dimpp = Utils.new_tiledb_dimension_tpp();
        ctx.handle_error(
            tiledb.tiledb_domain_get_dimension_from_index(ctx.getCtxp(), domainp, i, dimpp));
        Dimension dim = new Dimension(ctx, dimpp);
        dimensions.add(dim);
      }
    }
    return dimensions;
  }

  /** Adds a new dimension to the domain. */
  public void add_dimension(Dimension d) throws TileDBError {
    if(dimensions==null){
      dimensions = new ArrayList<Dimension>();
    }
    dimensions.add(d);
    ctx.handle_error(tiledb.tiledb_domain_add_dimension(ctx.getCtxp(), domainp, d.getDimensionp()));
  }

  /** Add multiple Dimension's. **/
  public void add_dimensions(Collection<Dimension> dims) throws TileDBError {
    for (Dimension d : dims) {
      add_dimension(d);
    }
  }


  public void free() {

  }
}
