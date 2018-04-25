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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Construct and execute read/write queries on a tiledb Array.
 *
 * @details
 * See examples for more usage details.
 *
 * **Example:**
 *
 * @code{.cpp}
 * Query query = new Query(my_dense_array, tiledb_query_type_t.TILEDB_WRITE);
 * query.setLayout(tiledb_layout_t.TILEDB_GLOBAL_ORDER);
 * query.setBuffer("a1", a1_data);
 * NativeArray a1_data = new NativeArray(ctx, new int[] {1,2,3,4}, Integer.class);
 * query.setBuffer("a1", a1_data);
 * query.submit();
 * @endcode
 */
public class Query implements AutoCloseable {
  private tiledb_query_type_t type;
  private Array array;
  private Context ctx;
  private SWIGTYPE_p_p_tiledb_query_t querypp;
  private SWIGTYPE_p_tiledb_query_t queryp;


  /** The getAttribute names that will be passed to a TileDB C query. */
  private SWIGTYPE_p_p_char attributeNames_;

  /** The buffers that will be passed to a TileDB C query. */
  private SWIGTYPE_p_p_void buffers_;

  /** The getAttribute names that will be passed to a TileDB C query. */
  private SWIGTYPE_p_p_char attributes_;

  /** The buffer sizes that will be passed to a TileDB C query. */
  private uint64_tArray buffer_sizes_;

  /** Keeps track of vector value_type sizes to convert back at return. */
  private List<Integer> sub_tsize_;

  /** Number of cells set by `setSubarray`, influences `resize_buffer`. */
  long subarray_cell_num_ = 0;

  /**
   * Keeps track the offsets buffer of a variable-sized getAttribute.
   *
   * Format:
   * Size of the vector, size of vector::value_type, vector.data()
   */
   private HashMap<String, Pair<Integer, Pair<Integer, NativeArray>>> var_offsets_;

  /**
   * Keeps track the data buffer for an getAttribute.
   *
   * Format:
   * Size of the vector, size of vector::value_type, vector.data()
   */
  private HashMap<String, Pair<Integer, Pair<Integer, NativeArray>>> attr_buffs_;
  private HashMap<String, Pair<Long, Long>> result_buffer_elements;
  private boolean executed;

  public Query(Array array, tiledb_query_type_t type) throws TileDBError {
    this.type =type;
    this.array=array;
    ctx = array.getCtx();
    ctx.deleterAdd(this);
    querypp = Utils.new_tiledb_query_tpp();
    ctx.handleError(tiledb.tiledb_query_create(ctx.getCtxp(), querypp, array.getUri(), type));
    queryp = Utils.tiledb_query_tpp_value(querypp);
    var_offsets_ = new HashMap<String, Pair<Integer, Pair<Integer, NativeArray>>>();
    attr_buffs_ = new HashMap<String, Pair<Integer, Pair<Integer, NativeArray>>>();
    executed = false;
  }

  /** Sets the data layout of the buffers.  */
  public void setLayout(tiledb_layout_t layout) throws TileDBError {
    ctx.handleError(tiledb.tiledb_query_set_layout(ctx.getCtxp(), queryp, layout));
  }

  /** Returns the query status. */
  public Status getQueryStatus() throws TileDBError {
    SWIGTYPE_p_tiledb_query_status_t statusp = tiledb.new_tiledb_query_status_tp();
    ctx.handleError(tiledb.tiledb_query_get_status(ctx.getCtxp(), queryp, statusp));
    Status status = Status.toStatus(statusp);
    tiledb.delete_tiledb_query_status_tp(statusp);
    return status;
  }

  /**
   * Returns the query status for a particular attribute.
   * @param attr
   * @return
   * @throws TileDBError
   */
  public Status getAttributeStatus(String attr) throws TileDBError {
    SWIGTYPE_p_tiledb_query_status_t statusp = tiledb.new_tiledb_query_status_tp();
    ctx.handleError(tiledb.tiledb_query_get_attribute_status(ctx.getCtxp(), queryp, attr, statusp));
    Status status = Status.toStatus(statusp);
    tiledb.delete_tiledb_query_status_tp(statusp);
    return status;
  }

  /**
   * Submits the query. Call will block until query is complete.
   * @return
   * @throws TileDBError
   */
  public Status submit() throws TileDBError {
    prepareSubmission();
    ctx.handleError(tiledb.tiledb_query_submit(ctx.getCtxp(), queryp));
    executed = true;
    return getQueryStatus();
  }

  /** Submit an async query (non-blocking). */
  public void submitAsync() throws TileDBError {
    submitAsync(new DefaultCallback());
  }

  /**
   * Submit an async query, with callback.
   *
   * @param callback Callback function.
   */
  public void submitAsync(Callback callback) throws TileDBError {
    prepareSubmission();
    ctx.handleError(Utils.tiledb_query_submit_async(
        ctx.getCtxp(), queryp, callback));
    executed = true;
  }

  /**
   * Returns the number of elements in the result buffers. This is a map
   * from the attribute name to a pair of values.
   *
   * The first is number of elements for var size attributes, and the second
   * is number of elements in the data buffer. For fixed sized attributes
   * (and coordinates), the first is always 0.
   */
  public HashMap<String, Pair<Long, Long>> resultBufferElements() throws TileDBError {
    result_buffer_elements = new HashMap<String, Pair<Long, Long>>();
    int bid = 0;
    for (String attrName : attr_buffs_.keySet()) {
      Attribute attr = array.getSchema().getAttributes().get(attrName);
      boolean var =
          (!attrName.equals(tiledb.tiledb_coords()) &&
              attr.getCellValNum() == tiledb.tiledb_var_num());
      result_buffer_elements.put(attrName,
          (var) ? new Pair<Long, Long>(
              buffer_sizes_.getitem(bid).longValue() / sub_tsize_.get(bid),
              buffer_sizes_.getitem(bid + 1).longValue() / sub_tsize_.get(bid + 1)) :
              new Pair<Long, Long>(
                  0l, buffer_sizes_.getitem(bid).longValue() / sub_tsize_.get(bid)));
      bid += (var) ? 2 : 1;
    }
    return result_buffer_elements;

  }

  /**
   * Sets a subarray, defined in the order dimensions were added.
   * Coordinates are inclusive.
   *
   * @param subarray
   * @throws TileDBError
   */
  public void setSubarray(NativeArray subarray) throws TileDBError {
    Types.typeCheck(subarray.getNativeType(), array.getSchema().getDomain().getType());
    ctx.handleError(
        tiledb.tiledb_query_set_subarray(ctx.getCtxp(), queryp, subarray.toVoidPointer()));
  }

  /**
   * Sets a buffer for a fixed-sized attribute.
   * @param attr
   * @param buffer
   * @throws TileDBError
   */
  public void setBuffer(String attr, NativeArray buffer) throws TileDBError {
    HashMap<String, Attribute> schemaAttributes = array.getSchema().getAttributes();
    tiledb_datatype_t attribute_t;
    if(schemaAttributes.containsKey(attr)){
      attribute_t = schemaAttributes.get(attr).getType();
      Types.typeCheck(buffer.getNativeType(), attribute_t);
    } else if (attr.equals(tiledb.tiledb_coords())) {
      attribute_t = array.getSchema().getDomain().getType();
      Types.typeCheck(buffer.getNativeType(), attribute_t);
    } else {
      throw new TileDBError("Attribute does not exist: " + attr);
    }
    attr_buffs_.put(attr,
        new Pair<Integer, Pair<Integer, NativeArray>>(
            buffer.getSize(),  // num elements
            new Pair<Integer, NativeArray> ( tiledb.tiledb_datatype_size(attribute_t).intValue(),
                buffer)));
  }

  /**
   * Sets a buffer for a variable-sized getAttribute.
   *
   * @tparam Vec buffer type. Should always be a vector of the attribute rype.
   * @param attr Attribute name
   * @param offsets Offsets where a new element begins in the data buffer.
   * @param buffer Buffer vector with elements of the attribute type.
   **/
  public void setBuffer(String attr, NativeArray offsets, NativeArray buffer) throws TileDBError {
    if (attr.equals(tiledb.tiledb_coords())) {
      throw new TileDBError("Cannot set coordinate buffer as variable sized.");
    }
    if(!offsets.getNativeType().equals(tiledb_datatype_t.TILEDB_UINT64))
      throw new TileDBError("Buffer offsets should be of getType TILEDB_UINT64 or Long. Found getType: "
          +offsets.getNativeType());

    setBuffer(attr, buffer);

    var_offsets_.put(attr,
        new Pair<Integer, Pair<Integer, NativeArray>>(
            offsets.getSize(),
            new Pair<Integer, NativeArray>(tiledb.tiledb_datatype_size(tiledb_datatype_t.TILEDB_UINT64).intValue(),
                offsets)));
  }

  /** Set the coordinate buffer for sparse arrays **/
  public void setCoordinates(NativeArray buffer) throws TileDBError {
//    if (array.getSchema().getArrayType() != tiledb_array_type_t.TILEDB_SPARSE)
//      throw new TileDBError("Cannot set coordinates for a dense array query");
    setBuffer(tiledb.tiledb_coords(), buffer);
  }


  /** Clears all attribute buffers. */
  public void resetBuffers(){
    attr_buffs_.clear();
    var_offsets_.clear();
    executed=false;
    buffer_sizes_.delete();
    tiledb.delete_charpArray(attributeNames_);
    tiledb.delete_voidpArray(buffers_);
    sub_tsize_= new ArrayList<Integer>();
  }

  private void prepareSubmission() throws TileDBError {
    if(!executed) {
      int numBuffers = attr_buffs_.size()+var_offsets_.size();
      buffers_ = tiledb.new_voidpArray(numBuffers);
      attributeNames_ = tiledb.new_charpArray(numBuffers);

      sub_tsize_= new ArrayList<Integer>();
      long buffer_sizes[] = new long[numBuffers];
      int bufferId=0, attrId=0;
      for (String a : attr_buffs_.keySet()) {
        if (var_offsets_.containsKey(a)) {
          Pair<Integer, Pair<Integer, NativeArray>> p = var_offsets_.get(a);
          tiledb.voidpArray_setitem(buffers_, bufferId, p.getSecond().getSecond().toVoidPointer());
          buffer_sizes[bufferId]=p.getFirst() * p.getSecond().getFirst();
          sub_tsize_.add(p.getSecond().getFirst());
          bufferId++;
        }
        Pair<Integer, Pair<Integer, NativeArray>> p = attr_buffs_.get(a);
        tiledb.voidpArray_setitem(buffers_, bufferId, p.getSecond().getSecond().toVoidPointer());
        buffer_sizes[bufferId]=p.getFirst() * p.getSecond().getFirst();
        tiledb.charpArray_setitem(attributeNames_, attrId, a);
        sub_tsize_.add(p.getSecond().getFirst());
        bufferId++;
        attrId++;

        buffer_sizes_ = Utils.newUint64Array(buffer_sizes);
        ctx.handleError(tiledb.tiledb_query_set_buffers(
            ctx.getCtxp(),
            queryp,
            attributeNames_,
            attrId,
            buffers_,
            buffer_sizes_.cast()));
      }
    }
  }

  /**
   * Return a Java primitive array as a copy of the attribute buffer
   * @param attr attribute name
   * @return
   * @throws TileDBError
   */
  public Object getBuffer(String attr) throws TileDBError {
    resultBufferElements();
    return attr_buffs_.get(attr).getSecond().getSecond().toJavaArray(
        result_buffer_elements.get(attr).getSecond().intValue());
  }

  /**
   * Return an array containing offsets for a variable attribute buffer
   * @param attr attribute name
   * @return
   * @throws TileDBError
   */
  public long[] getVarBuffer(String attr) throws TileDBError {
    resultBufferElements();
    return (long[])var_offsets_.get(attr).getSecond().getSecond().toJavaArray(
        result_buffer_elements.get(attr).getSecond().intValue());
  }

  private static class DefaultCallback implements Callback {
    public DefaultCallback() {}
    public void call() {}
  }

  @Override
  public String toString() {
    switch (type) {
      case TILEDB_READ:
        return "READ";
      case TILEDB_WRITE:
        return "WRITE";
    }
    return "";  // silence error
  }

  /**
   * Delete the native object.
   */
  public void close() throws TileDBError {
    if(queryp!=null) {
      for (Map.Entry<String, Pair<Integer, Pair<Integer, NativeArray>>> e : attr_buffs_.entrySet()) {
        e.getValue().getSecond().getSecond().close();
      }
      buffer_sizes_.delete();
      tiledb.delete_charpArray(attributeNames_);
      tiledb.delete_voidpArray(buffers_);
      ctx.handleError(tiledb.tiledb_query_free(ctx.getCtxp(), querypp));
      queryp = null;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
