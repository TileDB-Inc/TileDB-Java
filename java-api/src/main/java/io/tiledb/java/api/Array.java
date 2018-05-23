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
  private Context ctx;
  private String uri;
  private ArraySchema schema;

  public Array(Context ctx, String uri, ArraySchema schema) throws TileDBError {
    ctx.deleterAdd(this);
    create(uri, schema);
    this.ctx = ctx;
    this.uri = uri;
    this.schema = schema;
  }

  public Array(Context ctx, String uri) throws TileDBError {
    ctx.deleterAdd(this);
    schema = new ArraySchema(ctx, uri);
    this.ctx = ctx;
    this.uri = uri;
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
        ctx.getCtxp(), uri, buffer.toVoidPointer(), emptyp));
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
    HashMap<String, Pair<Long,Long>> ret = new HashMap<String, Pair<Long,Long>>();
    Types.typeCheck(subarray.getNativeType(), schema.getDomain().getType());
    SWIGTYPE_p_p_char names = tiledb.new_charpArray(
        (schema.getArrayType() == tiledb_array_type_t.TILEDB_SPARSE)?
            (int)schema.getAttributeNum()+1 :
            (int)schema.getAttributeNum());
    int attr_num = 0, nbuffs = 0 ;
    for(Map.Entry<String,Attribute> a : schema.getAttributes().entrySet()) {
      tiledb.charpArray_setitem(names, attr_num, a.getKey());
      nbuffs += a.getValue().getCellValNum() == tiledb.tiledb_var_num() ? 2 : 1;
      attr_num++;
    }
    if (schema.getArrayType() == tiledb_array_type_t.TILEDB_SPARSE) {
      nbuffs++;
      tiledb.charpArray_setitem(names, attr_num, tiledb.tiledb_coords());
      attr_num++;
    }
    uint64_tArray sizes = new uint64_tArray(nbuffs);
    SWIGTYPE_p_void nativeSubarray = subarray.toVoidPointer();

    ctx.handleError(tiledb.tiledb_array_compute_max_read_buffer_sizes(
        ctx.getCtxp(),
        uri,
        nativeSubarray,
        names,
        attr_num,
        sizes.cast()));

    int sid = 0;
    for(Map.Entry<String,Attribute> a : schema.getAttributes().entrySet()) {
      boolean var = a.getValue().getCellValNum() == tiledb.tiledb_var_num();
      ret.put(a.getKey(),
          var ?
              new Pair(sizes.getitem(sid).longValue() / tiledb.tiledb_offset_size().longValue(),
                  sizes.getitem(sid+1).longValue() / tiledb.tiledb_datatype_size(a.getValue().getType()).longValue()) :
              new Pair(0l,sizes.getitem(sid).longValue())
      );
      sid += var ? 2 : 1;
    }
    if (schema.getArrayType() == tiledb_array_type_t.TILEDB_SPARSE) {
      ret.put(tiledb.tiledb_coords(),
          new Pair(0l, sizes.getitem(sid).longValue() / tiledb.tiledb_datatype_size(schema.getDomain().getType()).longValue()));
    }
    sizes.delete();
    return ret;
  }

  public Context getCtx() {
    return ctx;
  }

  public String getUri() {
    return uri;
  }

  public ArraySchema getSchema() {
    return schema;
  }

  /**
   * Delete the native object.
   */
  public void close() throws TileDBError {
    if(schema!=null)
      schema.close();
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
