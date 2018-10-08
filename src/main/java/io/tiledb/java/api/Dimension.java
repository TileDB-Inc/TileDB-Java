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
 * Describes one dimension of an Array. The dimension consists of a type, lower and upper bound, and
 * tile-extent describing the memory ordering. Dimensions are added to a Domain.
 *
 * <p><b>Example:</b>
 *
 * <pre>{@code
 * Context ctx = new Context();
 * Domain domain = new Domain(ctx);
 * // Create a dimension with inclusive domain [0,1000] and tile extent 100.
 * Dimension<Integer> d = new Dimension<Integer>(ctx, "d", Integer.class, new Pair<Integer, Integer>(0, 1000), 100);
 * domain.addDimension(d);
 * }</pre>
 */
public class Dimension<T> implements AutoCloseable {
  private Context ctx;
  private Datatype type;
  private String name;
  private Pair<T, T> domain;

  private SWIGTYPE_p_tiledb_dimension_t dimensionp;
  private SWIGTYPE_p_p_tiledb_dimension_t dimensionpp;

  /**
   * Constructor from native object
   *
   * @param ctx A TileDB context
   * @param dimensionpp A Swig wrapper object to a tiledb_dimension_t pointer
   */
  protected Dimension(Context ctx, SWIGTYPE_p_p_tiledb_dimension_t dimensionpp) {
    this.ctx = ctx;
    this.dimensionpp = dimensionpp;
    this.dimensionp = tiledb.tiledb_dimension_tpp_value(dimensionpp);
  }

  /**
   * Constructor for creating a new dimension with datatype
   *
   * @param ctx The TileDB context.
   * @param name The dimension name.
   * @param type The Dimension Java scalar type class
   * @param domain The dimension domain (A Pair containing the lower and upper bound).
   * @param extent The tile extent on the dimension.
   * @exception TileDBError A TileDB exception
   */
  public Dimension(Context ctx, String name, Class<T> type, Pair<T, T> domain, T extent)
      throws TileDBError {
    createImpl(ctx, name, type, domain, extent);
  }

  private void createImpl(Context ctx, String name, Class<T> type, Pair<T, T> domain, T extent)
      throws TileDBError {
    this.ctx = ctx;
    dimensionpp = tiledb.new_tiledb_dimension_tpp();
    this.type = Types.getNativeType(type);
    this.name = name;
    this.domain = domain;
    NativeArray domainBuffer = new NativeArray(ctx, 2, this.type);
    domainBuffer.setItem(0, (Object) domain.getFirst());
    domainBuffer.setItem(1, (Object) domain.getSecond());
    NativeArray tileExtent = new NativeArray(ctx, 1, this.type);
    tileExtent.setItem(0, (Object) extent);
    //    SWIGTYPE_p_void tile_extent = Types.createNativeArrayExtent(this.getType, extent);
    ctx.handleError(
        tiledb.tiledb_dimension_alloc(
            ctx.getCtxp(),
            name,
            this.type.toSwigEnum(),
            domainBuffer.toVoidPointer(),
            tileExtent.toVoidPointer(),
            dimensionpp));
    this.dimensionp = tiledb.tiledb_dimension_tpp_value(dimensionpp);
  }

  protected SWIGTYPE_p_tiledb_dimension_t getDimensionp() {
    return dimensionp;
  }

  /**
   * @return The String name of the dimension.
   * @throws TileDBError A TileDB exception
   */
  public String getName() throws TileDBError {
    if (name == null) {
      SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
      try {
        ctx.handleError(tiledb.tiledb_dimension_get_name(ctx.getCtxp(), dimensionp, namepp));
      } catch (TileDBError err) {
        tiledb.delete_charpp(namepp);
        throw err;
      }
      name = tiledb.charpp_value(namepp);
      tiledb.delete_charpp(namepp);
    }
    return name;
  }

  /**
   * @return The dimension datatype.
   * @throws TileDBError A TileDB exception
   */
  public Datatype getType() throws TileDBError {
    if (type == null) {
      SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
      try {
        ctx.handleError(tiledb.tiledb_dimension_get_type(ctx.getCtxp(), dimensionp, typep));
      } catch (TileDBError err) {
        tiledb.delete_tiledb_datatype_tp(typep);
        throw err;
      }
      type = Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(typep));
      tiledb.delete_tiledb_datatype_tp(typep);
    }
    return type;
  }

  /**
   * @return The domain of the dimension (A Pair containing the lower and upper bound).
   * @throws TileDBError A TileDB exception
   */
  public Pair<T, T> getDomain() throws TileDBError {
    if (domain == null) {
      SWIGTYPE_p_p_void domainpp = tiledb.new_voidpArray(1);
      try {
        ctx.handleError(tiledb.tiledb_dimension_get_domain(ctx.getCtxp(), dimensionp, domainpp));
      } catch (TileDBError err) {
        tiledb.delete_voidpArray(domainpp);
        throw err;
      }
      NativeArray domainBuffer = new NativeArray(ctx, getType(), domainpp);
      domain = new Pair<T, T>((T) domainBuffer.getItem(0), (T) domainBuffer.getItem(1));
    }
    return domain;
  }

  /**
   * @return A string representation of the domain.
   * @throws TileDBError A TileDB exception
   */
  public String domainToStr() throws TileDBError {
    Pair<T, T> d = getDomain();
    return "(" + d.getFirst() + ", " + d.getSecond() + ")";
  }

  /**
   * @return The tile extent of the dimension.
   * @throws TileDBError A TileDB exception
   */
  public T getTileExtent() throws TileDBError {
    getType();
    SWIGTYPE_p_p_void tileExtent = tiledb.new_voidpArray(1);
    try {
      ctx.handleError(
          tiledb.tiledb_dimension_get_tile_extent(ctx.getCtxp(), dimensionp, tileExtent));
    } catch (TileDBError err) {
      tiledb.delete_voidpArray(tileExtent);
      throw err;
    }
    NativeArray tileExtentBuffer = new NativeArray(ctx, getType(), tileExtent);
    return (T) tileExtentBuffer.getItem(0);
  }

  /**
   * @return A string representation of the extent.
   * @throws TileDBError A TileDB exception
   */
  public String tileExtentToStr() throws TileDBError {
    return getTileExtent().toString();
  }

  /** Free's native TileDB resources associated with the Dimension object */
  public void close() {
    if (dimensionp != null) {
      tiledb.tiledb_dimension_free(dimensionpp);
    }
  }
}
