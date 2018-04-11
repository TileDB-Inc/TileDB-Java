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

/**
 * Construct and execute read/write queries on a tiledb::Array.
 *
 * @details
 * See examples for more usage details.
 *
 * **Example:**
 *
 * @code{.cpp}
 * Query query(ctx, "my_dense_array", TILEDB_WRITE);
 * query.set_layout(TILEDB_GLOBAL_ORDER);
 * std::vector a1_data = {1, 2, 3};
 * query.set_buffer("a1", a1_data);
 * query.submit();
 * @endcode
 */
public class Query {
  private tiledb_query_type_t type;
  private Array array;
  private Context ctx;
  private SWIGTYPE_p_p_tiledb_query_t querypp;
  private SWIGTYPE_p_tiledb_query_t queryp;


  /** The attribute names that will be passed to a TileDB C query. */
  private SWIGTYPE_p_p_char attributeNames_;

  /** The buffers that will be passed to a TileDB C query. */
  private SWIGTYPE_p_p_void buffers_;

  /** The attribute names that will be passed to a TileDB C query. */
  private SWIGTYPE_p_p_char attributes_;

  /** The buffer sizes that will be passed to a TileDB C query. */
  private uint64_tArray buffer_sizes_;

  /** Keeps track of vector value_type sizes to convert back at return. */
  private List<Integer> sub_tsize_;

  /** Number of cells set by `set_subarray`, influences `resize_buffer`. */
  long subarray_cell_num_ = 0;

  /**
   * Keeps track the offsets buffer of a variable-sized attribute.
   *
   * Format:
   * Size of the vector, size of vector::value_type, vector.data()
   */
   HashMap<String, Pair<Integer, Pair<Integer, SWIGTYPE_p_void>>> var_offsets_;

  /**
   * Keeps track the data buffer for an attribute.
   *
   * Format:
   * Size of the vector, size of vector::value_type, vector.data()
   */
  HashMap<String, Pair<Integer, Pair<Integer, SWIGTYPE_p_void>>> attr_buffs_;
  private HashMap<String, Pair<Long, Long>> result_buffer_elements;

  public Query(Array array, tiledb_query_type_t type) throws TileDBError {
    this.type =type;
    this.array=array;
    ctx = array.getCtx();
    querypp = Utils.new_tiledb_query_tpp();
    ctx.handle_error(tiledb.tiledb_query_create(ctx.getCtxp(), querypp, array.getUri(), type));
    queryp = Utils.tiledb_query_tpp_value(querypp);
    var_offsets_ = new HashMap<String, Pair<Integer, Pair<Integer, SWIGTYPE_p_void>>>();
    attr_buffs_ = new HashMap<String, Pair<Integer, Pair<Integer, SWIGTYPE_p_void>>>();
  }

  /** Sets the data layout of the buffers.  */
  public void set_layout(tiledb_layout_t layout) throws TileDBError {
    ctx.handle_error(tiledb.tiledb_query_set_layout(ctx.getCtxp(), queryp, layout));
  }

  /** Returns the query status. */
  public Status query_status() throws TileDBError {
    SWIGTYPE_p_tiledb_query_status_t statusp = tiledb.new_tiledb_query_status_tp();
    ctx.handle_error(tiledb.tiledb_query_get_status(ctx.getCtxp(), queryp, statusp));
    Status status = Status.toStatus(statusp);
    tiledb.delete_tiledb_query_status_tp(statusp);
    return status;
  }

  /** Returns the query status for a particular attribute. */
  public Status attribute_status(String attr) throws TileDBError {
    SWIGTYPE_p_tiledb_query_status_t statusp = tiledb.new_tiledb_query_status_tp();
    ctx.handle_error(tiledb.tiledb_query_get_attribute_status(ctx.getCtxp(), queryp, attr, statusp));
    Status status = Status.toStatus(statusp);
    tiledb.delete_tiledb_query_status_tp(statusp);
    return status;
  }

  /** Submits the query. Call will block until query is complete. */
  public Status submit() throws TileDBError {
    prepare_submission();
    ctx.handle_error(tiledb.tiledb_query_submit(ctx.getCtxp(), queryp));
    return query_status();
  }

  /** Submit an async query (non-blocking). */
  public void submit_async() throws TileDBError {
    submit_async(new DefaultCallback());
  }

  /**
   * Submit an async query, with callback.
   *
   * @param callback Callback function.
   * @return Status of submitted query.
   */
  public void submit_async(Callback callback) throws TileDBError {
    prepare_submission();
    ctx.handle_error(Utils.tiledb_query_submit_async(
        ctx.getCtxp(), queryp, callback));
  }

  /**
   * Returns the number of elements in the result buffers. This is a map
   * from the attribute name to a pair of values.
   *
   * The first is number of elements for var size attributes, and the second
   * is number of elements in the data buffer. For fixed sized attributes
   * (and coordinates), the first is always 0.
   */
  HashMap<String, Pair<Long, Long>> result_buffer_elements() throws TileDBError {
    if(result_buffer_elements==null) {
      result_buffer_elements = new HashMap<String, Pair<Long, Long>>();
      int bid = 0;
      for (Attribute attr : array.getSchema().attributes().values()) {
        boolean var =
            (!attr.getName().equals(tiledb.tiledb_coords()) &&
                attr.getCellValNum() == tiledb.tiledb_var_num());
        result_buffer_elements.put(attr.getName(),
            (var) ? new Pair<Long, Long>(
                buffer_sizes_.getitem(bid).longValue() / sub_tsize_.get(bid),
                buffer_sizes_.getitem(bid + 1).longValue() / sub_tsize_.get(bid + 1)) :
                new Pair<Long, Long>(
                    0l, buffer_sizes_.getitem(bid).longValue() / sub_tsize_.get(bid)));
        bid += (var) ? 2 : 1;
      }
    }
    return result_buffer_elements;

  }

  /**
   * Sets a subarray, defined in the order dimensions were added.
   * Coordinates are inclusive.
   *
   * @tparam T Array domain datatype
   * @param pairs The subarray defined as pairs of [start, stop] per dimension.
   */
  public void set_subarray(Object pairs) throws TileDBError {
    Types.typeCheckArray(pairs, array.getSchema().domain().type());
    SWIGTYPE_p_void subarray = Types.createNativeArray(array.getSchema().domain().type(), pairs, 2);
    ctx.handle_error(
        tiledb.tiledb_query_set_subarray(ctx.getCtxp(), queryp, subarray));
  }

  /**
   * Sets a buffer for a fixed-sized attribute.
   *
   * @tparam Vec buffer type. Should always be a vector of the attribute type.
   * @param attr Attribute name
   * @param buf Buffer vector with elements of the attribute type.
   **/
  public void set_buffer(String attr, Object buf, int size) throws TileDBError {
    HashMap<String, Attribute> schemaAttributes = array.getSchema().attributes();
    tiledb_datatype_t attribute_t;
    if(schemaAttributes.containsKey(attr)){
      attribute_t = schemaAttributes.get(attr).getType();
      Types.typeCheckArray(buf, attribute_t);
    } else if (attr.equals(tiledb.tiledb_coords())) {
      attribute_t = array.getSchema().domain().type();
      Types.typeCheckArray(buf, attribute_t);
    } else {
      throw new TileDBError("Attribute does not exist: " + attr);
    }
    attr_buffs_.put(attr,
        new Pair<Integer, Pair<Integer, SWIGTYPE_p_void>>(
            size,  // num elements
            new Pair<Integer, SWIGTYPE_p_void> ( tiledb.tiledb_datatype_size(attribute_t).intValue(),
                Types.createNativeArray(attribute_t, buf, size))));
  }

  /**
   * Sets a buffer for a variable-sized attribute.
   *
   * @tparam Vec buffer type. Should always be a vector of the attribute type.
   * @param attr Attribute name
   * @param offsets Offsets where a new element begins in the data buffer.
   * @param data Buffer vector with elements of the attribute type.
   **/
  public void set_buffer(String attr, long[] offsets, int offsetsSize, Object data, int size) throws TileDBError {
    if (attr.equals(tiledb.tiledb_coords())) {
      throw new TileDBError("Cannot set coordinate buffer as variable sized.");
    }
    set_buffer(attr, data, size);

    var_offsets_.put(attr,
        new Pair<Integer, Pair<Integer, SWIGTYPE_p_void>>(
            offsetsSize,
            new Pair<Integer, SWIGTYPE_p_void>(tiledb.tiledb_datatype_size(tiledb_datatype_t.TILEDB_UINT64).intValue(),
                PointerUtils.toVoid(
                    (offsets==null)? new uint64_tArray(offsetsSize) :
                    Utils.newUint64Array(offsets)
                ))));
  }

  /** Set the coordinate buffer for sparse arrays **/
  public void set_coordinates(Object buf, int size) throws TileDBError {
    if (array.getSchema().getArrayType() != tiledb_array_type_t.TILEDB_SPARSE)
      throw new TileDBError("Cannot set coordinates for a dense array query");
    set_buffer(tiledb.tiledb_coords(), buf, size);
  }


  /** Clears all attribute buffers. */
  public void reset_buffers(){
    attr_buffs_.clear();
    var_offsets_.clear();
    if(sub_tsize_!=null) {
      buffer_sizes_.delete();
      tiledb.delete_charpArray(attributeNames_);
      tiledb.delete_voidpArray(buffers_);
    }
    sub_tsize_= new ArrayList<Integer>();
  }

  private void prepare_submission() throws TileDBError {
    if(sub_tsize_!=null) {
      buffer_sizes_.delete();
      tiledb.delete_charpArray(attributeNames_);
      tiledb.delete_voidpArray(buffers_);
    }
    sub_tsize_= new ArrayList<Integer>();
    int numBuffers = attr_buffs_.size()+var_offsets_.size();
    buffers_ = tiledb.new_voidpArray(numBuffers);
    attributeNames_ = tiledb.new_charpArray(numBuffers);

    long buffer_sizes[] = new long[numBuffers];

    int bufferId=0, attrId=0;
    for (String a : attr_buffs_.keySet()) {
      if (var_offsets_.containsKey(a)) {
        Pair<Integer, Pair<Integer, SWIGTYPE_p_void>> p = var_offsets_.get(a);
        tiledb.voidpArray_setitem(buffers_, bufferId, p.getSecond().getSecond());
        buffer_sizes[bufferId]=p.getFirst() * p.getSecond().getFirst();
        sub_tsize_.add(p.getSecond().getFirst());
        bufferId++;
      }
      Pair<Integer, Pair<Integer, SWIGTYPE_p_void>> p = attr_buffs_.get(a);
      tiledb.voidpArray_setitem(buffers_, bufferId, p.getSecond().getSecond());
      buffer_sizes[bufferId]=p.getFirst() * p.getSecond().getFirst();
      tiledb.charpArray_setitem(attributeNames_, attrId, a);
      sub_tsize_.add(p.getSecond().getFirst());
      bufferId++;
      attrId++;
    }
    buffer_sizes_ = Utils.newUint64Array(buffer_sizes);
    ctx.handle_error(tiledb.tiledb_query_set_buffers(
        ctx.getCtxp(),
        queryp,
        attributeNames_,
        attrId,
        buffers_,
        buffer_sizes_.cast()));

  }

  public void free() throws TileDBError {
    ctx.handle_error(
        tiledb.tiledb_query_free(ctx.getCtxp(), querypp));
  }

  public Object get_buffer(String attr) throws TileDBError {
    return Types.toJavaArray(
        attr_buffs_.get(attr).getSecond().getSecond(),
        array.getSchema().attributes().get(attr).getType(),
        result_buffer_elements.get(attr).getSecond().intValue());
  }

  public Object get_var_buffer(String attr) throws TileDBError {
    return Types.toJavaArray(
        var_offsets_.get(attr).getSecond().getSecond(),
        tiledb_datatype_t.TILEDB_UINT64,
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
}
