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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TileDB array class.
 */
public class Array implements AutoCloseable {
  private SWIGTYPE_p_p_tiledb_array_t arraypp;
  private SWIGTYPE_p_tiledb_array_t arrayp;
  
  private Context ctx;
  private String uri;
  private ArraySchema schema;
  private tiledb_query_type_t query_type;
  private boolean initialized = false;

  /** Construct an Array object, opening the array for reading / writing
   */ 
  public Array(Context ctx, String uri, tiledb_query_type_t query_type) throws TileDBError {
    ctx.deleterAdd(this);
    schema = new ArraySchema(ctx, uri);
    this.ctx = ctx;
    this.uri = uri;
    this.query_type = query_type;
    openArray(query_type);
  }
  
  /** Construct an Array object, opening the array for reading
   */
  public Array(Context ctx, String uri) throws TileDBError {
    this(ctx, uri, tiledb_query_type_t.TILEDB_READ);
  }

  private void openArray() throws TileDBError {
    openArray(tiledb_query_type_t.TILEDB_READ);
  }

  private void openArray(tiledb_query_type_t query_type) throws TileDBError {
    arraypp = tiledb.new_tiledb_array_tpp();
    ctx.handleError(tiledb.tiledb_array_alloc(ctx.getCtxp(), uri, arraypp)); 
    arrayp = tiledb.tiledb_array_tpp_value(arraypp);
    ctx.handleError(
      tiledb.tiledb_array_open(
        ctx.getCtxp(), 
	arrayp, 
	query_type));
    initialized = true;
  }

  /** Consolidates the fragments of an array. **/
  public void consolidate() throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_consolidate(ctx.getCtxp(), uri));
  }

  /** Creates an array on persistent storage from a schema definition. **/
  public static void create(String uri, ArraySchema schema) throws TileDBError {
    Context ctx = schema.getCtx();
    ctx.handleError(tiledb.tiledb_array_schema_check(ctx.getCtxp(), schema.getSchemap()));
    ctx.handleError(tiledb.tiledb_array_create(ctx.getCtxp(), uri, schema.getSchemap()));
  }

  /**
   * Get the non-empty getDomain of an array. This returns the bounding
   * coordinates for each dimension.
   *
   * @return HashMap of dim names with a {lower, upper} pair. Inclusive.
   *         Empty HashMap if the array has no data.
   */
  public HashMap<String, Pair> nonEmptyDomain() throws TileDBError {
    HashMap<String, Pair> ret = new HashMap<String, Pair>();

    SWIGTYPE_p_int emptyp = tiledb.new_intp();
    List<Dimension> dimensions = schema.getDomain().getDimensions();
    NativeArray buffer = new NativeArray(ctx,2*dimensions.size(), schema.getDomain().getType());
    ctx.handleError(tiledb.tiledb_array_get_non_empty_domain(
        ctx.getCtxp(), arrayp, buffer.toVoidPointer(), emptyp));
    if(tiledb.intp_value(emptyp)==1){
      return ret;
    }
    tiledb.delete_intp(emptyp);
    int i=0;
    for (Dimension d : dimensions){
      ret.put(d.getName(), new Pair(buffer.getItem(i), buffer.getItem(i+1)));
      i+=2;
    }
    return ret;
  }

  /**
   * Compute an upper bound on the buffer elements needed to read a subarray.
   *
   * @param subarray Targeted subarray.
   * @return The maximum number of elements for each array getAttribute (plus
   *     coordinates for sparse arrays).
   *     Note that two numbers are returned per getAttribute. The first
   *     is the maximum number of elements in the offset buffer
   *     (0 for fixed-sized getAttributes and coordinates),
   *     and the second is the maximum number of elements of the value buffer.
   */
  public HashMap<String,Pair<Long,Long>> maxBufferElements(NativeArray subarray) throws TileDBError {
    
    Types.typeCheck(subarray.getNativeType(), schema.getDomain().getType());
    
    uint64_tArray off_nbytes = new uint64_tArray(1);
    uint64_tArray val_nbytes = new uint64_tArray(1);

    HashMap<String, Pair<Long,Long>> ret = new HashMap<String, Pair<Long,Long>>();
    
    for(Map.Entry<String, Attribute> a : schema.getAttributes().entrySet()) {
      if (a.getValue().isVar()) {
      	ctx.handleError(tiledb.tiledb_array_max_buffer_size_var(
	  ctx.getCtxp(),
	  arrayp, 
          a.getKey(),
	  subarray.toVoidPointer(), 
	  off_nbytes.cast(), 
	  val_nbytes.cast()));
	ret.put(a.getKey(),
		new Pair(off_nbytes.getitem(0).longValue() / 
			 tiledb.tiledb_offset_size().longValue(),
			 val_nbytes.getitem(0).longValue() / 
		     	 tiledb.tiledb_datatype_size(a.getValue().getType()).longValue()));
      } else {
        ctx.handleError(tiledb.tiledb_array_max_buffer_size(
	  ctx.getCtxp(),
	  arrayp,
	  a.getKey(),
	  subarray.toVoidPointer(), 
	  val_nbytes.cast()));
	ret.put(a.getKey(), 
		new Pair(0l, val_nbytes.getitem(0).longValue() /
			     tiledb.tiledb_datatype_size(a.getValue().getType()).longValue()));
      } 
    }
    if (schema.isSparse()) {
	ctx.handleError(tiledb.tiledb_array_max_buffer_size(
	  ctx.getCtxp(),
	  arrayp, 
	  tiledb.tiledb_coords(), 
	  subarray.toVoidPointer(), 
	  val_nbytes.cast()));
  	ret.put(tiledb.tiledb_coords(),
          	new Pair(0l, val_nbytes.getitem(0).longValue() / 
			     tiledb.tiledb_datatype_size(schema.getDomain().getType()).longValue()));

    } 
    off_nbytes.delete();
    val_nbytes.delete();
    return ret;
  }

  public Context getCtx() {
    return ctx;
  }

  public String getUri() {
    return uri; }

  public ArraySchema getSchema() {
    return schema;
  }
  
  public tiledb_query_type_t getQueryType() {
    return query_type;
  }

  public SWIGTYPE_p_tiledb_array_t getArrayp() {
    return arrayp;
  }

  /**
   * Delete the native object.
   */
  public void close() throws TileDBError {
    if(initialized) {
      initialized = false;
      tiledb.tiledb_array_close(ctx.getCtxp(), arrayp);
      tiledb.tiledb_array_free(arraypp);
      if(schema!=null)
        schema.close();
    }
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
