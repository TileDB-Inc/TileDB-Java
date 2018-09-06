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

import static io.tiledb.java.api.QueryType.*;

/**
 * Class representing a TileDB array object.
 *
 * An Array object represents array data in TileDB at some persisted location,
 * e.g. on disk, in an S3 bucket, etc. Once an array has been opened for reading
 * or writing, interact with the data through {@link Query} objects.
 *
 * <pre><b>Example:</b>
 * {@code
 *   // Create an ArraySchema, add attributes, domain, etc.
 *   Context ctx = new Context();
 *   ArraySchema schema = new ArraySchema(...);
 *   // Create empty array named "my_array" on persistent storage.
 *   Array.create("my_array", schema);
 * }
 * </pre>
 */
public class Array implements AutoCloseable {

  private Context ctx;
  private String uri;
  private ArraySchema schema;
  private QueryType query_type;
  private boolean initialized = false;

  private SWIGTYPE_p_tiledb_array_t arrayp;
  private SWIGTYPE_p_p_tiledb_array_t arraypp;

  /**
   * Constructs an Array object, opening the array for the given query type.
   *
   * <pre><b>Example:</b>
   * {@code
   * Context ctx = new Context();
   * Array array new Array(ctx, "s3://bucket-name/array-name", TILEDB_READ);
   * }
   * </pre>
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @param query_type Query type to open the array for.
   * @exception TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri, QueryType query_type) throws TileDBError {
    ctx.deleterAdd(this);
    schema = new ArraySchema(ctx, uri);
    this.ctx = ctx;
    this.uri = uri;
    this.query_type = query_type;
    openArray(query_type);
  }

  /**
   * Constructs an Array object opening the array for reading.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   Array array new Array(ctx, "s3://bucket-name/array-name");
   * }
   * </pre>
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @exception TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri) throws TileDBError {
    this(ctx, uri, TILEDB_READ);
  }

  private synchronized void openArray(QueryType query_type) throws TileDBError {
    arraypp = tiledb.new_tiledb_array_tpp();
    ctx.handleError(tiledb.tiledb_array_alloc(ctx.getCtxp(), uri, arraypp)); 
    arrayp = tiledb.tiledb_array_tpp_value(arraypp);
    ctx.handleError(
      tiledb.tiledb_array_open(
        ctx.getCtxp(), 
	    arrayp,
        query_type.toSwigEnum()));
    initialized = true;
  }

  /**
   * Consolidates the fragments of an array into a single fragment.
   *
   * All queries to the array before consolidation must be finalized before consolidation can begin.
   * Consolidation temporarily aquires an exclusive lock on the array when finalizing the resulting merged fragment.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   Array.consolidate(ctx, "s3://bucket-name/array-name");
   * }
   * </pre>
   *
   * @param ctx TileDB context object
   * @param uri TileDB URI string
   * @exception TileDBError A TileDB exception
   */
  public static void consolidate(Context ctx, String uri) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_consolidate(ctx.getCtxp(), uri));
  }

  /**
   * Creates a persisted TileDB array given an input {@link ArraySchema}
   *
   * <pre><b>Example:</b>
   * {@code
   *   Array.create("my_array", schema);
   * }
   * </pre>
   *
   * @param uri The array URI string
   * @param schema The TileDB ArraySchema
   * @exception TileDBError A TileDB exception
   */
  public static void create(String uri, ArraySchema schema) throws TileDBError {
    Context ctx = schema.getCtx();
    ctx.handleError(tiledb.tiledb_array_schema_check(ctx.getCtxp(), schema.getSchemap()));
    ctx.handleError(tiledb.tiledb_array_create(ctx.getCtxp(), uri, schema.getSchemap()));
  }

  /**
   * Get the non-empty domain of an array, returning the bounding coordinates for each dimension.
   *
   * @return A HashMap of dimension names and (lower, upper) inclusive bounding coordinate range pair.
   *         Empty HashMap if the array has no data.
   * @exception TileDBError A TileDB exception
   */
  public HashMap<String, Pair> nonEmptyDomain() throws TileDBError {
    HashMap<String, Pair> ret = new HashMap<String, Pair>();
    SWIGTYPE_p_int emptyp = tiledb.new_intp();
    List<Dimension> dimensions = schema.getDomain().getDimensions();
    NativeArray buffer = new NativeArray(ctx,2 * dimensions.size(), schema.getDomain().getType());
    ctx.handleError(tiledb.tiledb_array_get_non_empty_domain(
        ctx.getCtxp(), arrayp, buffer.toVoidPointer(), emptyp));
    if (tiledb.intp_value(emptyp) ==  1) {
      return ret;
    }
    tiledb.delete_intp(emptyp);
    int i = 0;
    for (Dimension d : dimensions) {
      ret.put(d.getName(), new Pair(buffer.getItem(i), buffer.getItem(i + 1)));
      i += 2;
    }
    return ret;
  }

  /**
   * Compute an upper bound on the buffer elements needed to read a subarray.
   *
   * @param subarray Domain subarray
   * @return The maximum number of elements for each array attribute.
   *     If the Array is sparse, max number of coordinates are also returned.
   *     Note that two numbers are returned per attribute. The first
   *     is the maximum number of elements in the offset buffer
   *     (0 for fixed-sized attributes and coordinates),
   *     the second is the maximum number of elements of the value buffer.
   * @throws TileDBError A TileDB exception
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
		     	   tiledb.tiledb_datatype_size(a.getValue().getType().toSwigEnum()).longValue()));
      } else {
        ctx.handleError(tiledb.tiledb_array_max_buffer_size(
	  ctx.getCtxp(),
	  arrayp,
	  a.getKey(),
	  subarray.toVoidPointer(), 
	  val_nbytes.cast()));
	ret.put(a.getKey(), 
		new Pair(0l, val_nbytes.getitem(0).longValue() /
			     tiledb.tiledb_datatype_size(a.getValue().getType().toSwigEnum()).longValue()));
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
			     tiledb.tiledb_datatype_size(schema.getDomain().getType().toSwigEnum()).longValue()));

    } 
    off_nbytes.delete();
    val_nbytes.delete();
    return ret;
  }

  /**
   *
   * @return The TileDB Context object associated with the Array instance.
   */
  public Context getCtx() {
    return ctx;
  }

  /**
   *
   * @return The URI string of the array
   */
  public String getUri() {
    return uri;
  }

  /**
   *
   * @return The TileDB ArraySchema of the Array instance.
   */
  public ArraySchema getSchema() {
    return schema;
  }

  /**
   *
   * @return The TileDB QueryType enum value that the Array instance.
   */
  public QueryType getQueryType() {
    return query_type;
  }

  /**
   *
   * @return SWIG tiledb_array_t pointer wrapper object.
   */
  protected SWIGTYPE_p_tiledb_array_t getArrayp() {
    return arrayp;
  }

  /**
   * Free's the native objects and closes the Array.
   */
  public synchronized void close() {
    if (initialized) {
      initialized = false;
      tiledb.tiledb_array_close(ctx.getCtxp(), arrayp);
      tiledb.tiledb_array_free(arraypp);
      if (schema != null) {
        schema.close();
      }
    }
  }


  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
