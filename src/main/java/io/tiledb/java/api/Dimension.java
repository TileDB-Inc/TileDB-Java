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

public class Dimension<T> {
  private SWIGTYPE_p_p_tiledb_dimension_t dimensionpp;
  private SWIGTYPE_p_tiledb_dimension_t dimensionp;
  private Context ctx;
  private tiledb_datatype_t type;
  private String name;
  private Pair<T,T> domain;

  public Dimension(Context ctx, SWIGTYPE_p_p_tiledb_dimension_t dimensionpp) {
    this.ctx = ctx;
    this.dimensionpp = dimensionpp;
    this.dimensionp = Utils.tiledb_dimension_tpp_value(dimensionpp);
  }

  /**
   * Constructor for creating a new dimension with datatype
   *
   * @param ctx The TileDB context.
   * @param name The dimension name.
   * @param domain The dimension domain.
   * @param extent The tile extent on the dimension.
   * @return A new `Dimension` object.
   */
  public Dimension( Context ctx, String name, Class<T> type, Pair<T,T> domain, T extent) throws TileDBError {
    create_impl(ctx, name, type,domain,extent);
  }

  private void create_impl(Context ctx, String name, Class<T> type, Pair<T,T> domain, T extent) throws TileDBError {
    this.ctx = ctx;
    dimensionpp = Utils.new_tiledb_dimension_tpp();
    this.type =  Types.getNativeType(type);
    this.name =name;
    this.domain = domain;
    SWIGTYPE_p_void domainp = Types.createNativeArrayPair(this.type, domain);
    SWIGTYPE_p_void tile_extent = Types.createNativeArrayExtent(this.type, extent);
    ctx.handle_error(tiledb.tiledb_dimension_create(
        ctx.getCtxp(), dimensionpp, name, this.type, domainp, tile_extent));
    this.dimensionp = Utils.tiledb_dimension_tpp_value(dimensionpp);

  }


  public SWIGTYPE_p_tiledb_dimension_t getDimensionp() {
    return dimensionp;
  }

  /** Returns the name of the dimension. */
  String name() throws TileDBError {
    if(name==null){
      SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
      ctx.handle_error(tiledb.tiledb_dimension_get_name(ctx.getCtxp(), dimensionp, namepp));
      name = tiledb.charpp_value(namepp);
      tiledb.delete_charpp(namepp);
    }
    return name;
  }

  /** Returns the dimension datatype. */
  public tiledb_datatype_t getType() throws TileDBError {
    if(type==null){
      SWIGTYPE_p_tiledb_datatype_t typep = tiledb.new_tiledb_datatype_tp();
      ctx.handle_error(tiledb.tiledb_dimension_get_type(ctx.getCtxp(), dimensionp, typep));
      type = tiledb.tiledb_datatype_tp_value(typep);
      tiledb.delete_tiledb_datatype_tp(typep);
    }
    return type;
  }

  /** Returns the domain of the dimension. **/
  Pair<T, T> domain() throws TileDBError {
    if(domain==null){
      getType();
      SWIGTYPE_p_p_void domainpp = tiledb.new_voidpArray(1);
      ctx.handle_error(tiledb.tiledb_dimension_get_domain(ctx.getCtxp(), dimensionp, domainpp));
      domain = Types.getPairFromNativeArray(type, domainpp);
    }
    return domain;
  }

  /** Returns a string representation of the domain. */
  public String domain_to_str(){
    return "";
  }

  /** Returns the tile extent of the dimension. */
//  public T tile_extent() const {
//    impl::type_check<T>(type(), 1);
//    return *static_cast<T*>(_tile_extent());
//  }

  /** Returns a string representation of the extent. */
  public String tile_extent_to_str(){
    return "";
  }

  public String getName() throws TileDBError {
    if(name==null){
      SWIGTYPE_p_p_char namepp = tiledb.new_charpp();
      ctx.handle_error(tiledb.tiledb_dimension_get_name(ctx.getCtxp(), dimensionp, namepp));
      name = tiledb.charpp_value(namepp);
      tiledb.delete_charpp(namepp);
    }
    return name;
  }

}
