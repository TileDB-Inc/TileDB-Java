/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 TileDB, Inc.
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
import java.math.BigInteger;

public class Group implements AutoCloseable {
  private Context ctx;
  private final String uri;
  private QueryType queryType;
  private SWIGTYPE_p_tiledb_group_handle_t groupp;
  private SWIGTYPE_p_p_tiledb_group_handle_t grouppp;

  public Group(Context ctx, String uri, QueryType queryType) throws TileDBError {
    SWIGTYPE_p_p_tiledb_group_handle_t grouppp = tiledb.new_tiledb_group_tpp();
    try {
      ctx.handleError(tiledb.tiledb_group_alloc(ctx.getCtxp(), uri, grouppp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_group_tpp(grouppp);
      throw err;
    }
    this.ctx = ctx;
    this.groupp = tiledb.tiledb_group_tpp_value(grouppp);
    this.grouppp = grouppp;
    this.uri = uri;
    this.queryType = queryType;
    open(queryType);
  }

  /**
   * Gets group pointer.
   *
   * @return the group pointer
   */
  protected SWIGTYPE_p_tiledb_group_handle_t getGroupp() {
    return this.groupp;
  }

  /**
   * Gets the Context.
   *
   * @return the context
   */
  protected Context getCtx() {
    return this.ctx;
  }

  /**
   * Sets the group config.
   *
   * @param config the configuration to set.
   * @throws TileDBError
   */
  public void setConfig(Config config) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_group_set_config(ctx.getCtxp(), getGroupp(), config.getConfigp()));
  }

  /**
   * Returns the query type
   *
   * @return The query type
   */
  public QueryType getQueryType() {
    return queryType;
  }

  /**
   * It deletes a metadata key-value item from an open group. The group must be opened in WRITE
   * mode, otherwise the function will error out.
   *
   * @param key the key to delete.
   * @throws TileDBError
   */
  public void deleteMetadata(String key) throws TileDBError {
    if (!isOpen()) throw new TileDBError("Group with URI: " + uri + " is closed");
    ctx.handleError(tiledb.tiledb_group_delete_metadata(ctx.getCtxp(), getGroupp(), key));
  }

  /**
   * It gets a metadata key-value item from an open group. The group must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @param key the key
   * @param nativeType the datatype of the value.
   * @return
   * @throws TileDBError
   */
  public NativeArray getMetadata(String key, Datatype nativeType) throws TileDBError {
    if (!isOpen()) throw new TileDBError("Group with URI: " + uri + " is closed");
    SWIGTYPE_p_p_void resultArrpp = tiledb.new_voidpArray(0);
    SWIGTYPE_p_unsigned_int value_num = tiledb.new_uintp();
    SWIGTYPE_p_tiledb_datatype_t value_type =
        (nativeType == null)
            ? tiledb.new_tiledb_datatype_tp()
            : tiledb.copy_tiledb_datatype_tp(nativeType.toSwigEnum());

    try {
      ctx.handleError(
          tiledb.tiledb_group_get_metadata(
              ctx.getCtxp(), getGroupp(), key, value_type, value_num, resultArrpp));

      Datatype derivedNativeType =
          Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(value_type));

      long value = tiledb.uintp_value(value_num);
      return new NativeArray(ctx, derivedNativeType, resultArrpp, (int) value);
    } finally {
      tiledb.delete_voidpArray(resultArrpp);
      tiledb.delete_uintp(value_num);
      tiledb.delete_tiledb_datatype_tp(value_type);
    }
  }

  /**
   * Gets the group config.
   *
   * @return the group config.
   * @throws TileDBError
   */
  public Config getConfig() throws TileDBError {
    SWIGTYPE_p_p_tiledb_config_t configpp = tiledb.new_tiledb_config_tpp();
    try {
      ctx.handleError(tiledb.tiledb_group_get_config(ctx.getCtxp(), getGroupp(), configpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_config_tpp(configpp);
    }
    return new Config(configpp);
  }

  /**
   * Gets the number of metadata items in an open group. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @return the number of metadata items
   * @throws TileDBError A TileDB exception
   */
  public BigInteger getMetadataNum() throws TileDBError {
    if (!isOpen()) throw new TileDBError("Group with URI: " + uri + " is closed");
    SWIGTYPE_p_unsigned_long_long value_num = tiledb.new_ullp();

    try {
      ctx.handleError(tiledb.tiledb_group_get_metadata_num(ctx.getCtxp(), getGroupp(), value_num));
      return tiledb.ullp_value(value_num);
    } finally {
      tiledb.delete_ullp(value_num);
    }
  }

  /**
   * Get the count of members in a group.
   *
   * @return the number of members in a group.
   * @throws TileDBError
   */
  public long getMemberCount() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long mc = tiledb.new_ullp();

    try {
      ctx.handleError(tiledb.tiledb_group_get_member_count(ctx.getCtxp(), getGroupp(), mc));
      return tiledb.ullp_value(mc).longValue();
    } finally {
      tiledb.delete_ullp(mc);
    }
  }

  @Deprecated
  /**
   * Get the URI of a member of a group by index and details of group
   *
   * @param index the index of the member.
   * @return the corresponding member in the group.
   * @throws TileDBError
   */
  public String getMemberByIndexV2(BigInteger index) throws TileDBError {
    return getMemberByIndex(index);
  }

  /**
   * Get the URI of a member of a group by index and details of group
   *
   * @param index the index of the member.
   * @return the corresponding member in the group.
   * @throws TileDBError
   */
  public String getMemberByIndex(BigInteger index) throws TileDBError {
    Util.checkBigIntegerRange(index);
    SWIGTYPE_p_tiledb_object_t objtypep = tiledb.new_tiledb_object_tp();
    SWIGTYPE_p_p_tiledb_string_handle_t uripp = tiledb.new_tiledb_string_handle_tpp();
    TileDBString ts = null;

    try {
      ctx.handleError(
          tiledb.tiledb_group_get_member_by_index_v2(
              ctx.getCtxp(), getGroupp(), index, uripp, objtypep, uripp));
      ts = new TileDBString(ctx, uripp);
      return ts.getView().getFirst();
    } finally {
      if (ts != null) ts.close();
      tiledb.delete_tiledb_object_tp(objtypep);
    }
  }

  /**
   * Get the URI of a member of a group by name
   *
   * @param name the name of the member
   * @return the URI of the member with the given name
   * @throws TileDBError
   */
  public String getMemberByName(String name) throws TileDBError {
    SWIGTYPE_p_tiledb_object_t objtypep = tiledb.new_tiledb_object_tp();
    SWIGTYPE_p_p_tiledb_string_handle_t uripp = tiledb.new_tiledb_string_handle_tpp();
    TileDBString ts = null;

    try {
      ctx.handleError(
          tiledb.tiledb_group_get_member_by_name_v2(
              ctx.getCtxp(), getGroupp(), name, uripp, objtypep));
      ts = new TileDBString(ctx, uripp);
      return ts.getView().getFirst();
    } finally {
      if (ts != null) ts.close();
      tiledb.delete_tiledb_object_tp(objtypep);
    }
  }

  /**
   * Add a member to a group.
   *
   * @param uri The uri of the member to add.
   * @param relative is the URI relative to the group.
   * @param name name of member, The caller takes ownership of the c-string. NULL if name was not
   *     set
   * @throws TileDBError
   */
  public void addMember(String uri, boolean relative, String name) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_group_add_member(
            ctx.getCtxp(), getGroupp(), uri, relative ? (short) 1 : (short) 0, name));
  }

  /**
   * Remove a member from a group.
   *
   * @param nameOrUri The name or URI of the member to remove
   * @throws TileDBError
   */
  public void removeMember(String nameOrUri) throws TileDBError {
    ctx.handleError(tiledb.tiledb_group_remove_member(ctx.getCtxp(), getGroupp(), nameOrUri));
  }

  /**
   * Gets a metadata item from an open group using an index. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @param index index to retrieve metadata from
   * @return a pair, key and the metadata
   * @throws TileDBError A TileDB exception
   */
  public Pair<String, NativeArray> getMetadataFromIndex(BigInteger index) throws TileDBError {
    Util.checkBigIntegerRange(index);
    if (!isOpen()) throw new TileDBError("Group with URI: " + uri + " is closed");
    SWIGTYPE_p_p_char key = tiledb.new_charpp();
    SWIGTYPE_p_unsigned_int key_len = tiledb.new_uintp();
    SWIGTYPE_p_tiledb_datatype_t value_type = tiledb.new_tiledb_datatype_tp();
    SWIGTYPE_p_unsigned_int value_num = tiledb.new_uintp();
    SWIGTYPE_p_p_void value = tiledb.new_voidpArray(0);

    try {
      ctx.handleError(
          tiledb.tiledb_group_get_metadata_from_index(
              ctx.getCtxp(), getGroupp(), index, key, key_len, value_type, value_num, value));

      String keyString = tiledb.charpp_value(key);
      long valueLength = tiledb.uintp_value(value_num);
      Datatype nativeType = Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(value_type));

      NativeArray result = new NativeArray(ctx, nativeType, value, (int) valueLength);
      return new Pair<String, NativeArray>(keyString, result);
    } finally {
      tiledb.delete_uintp(value_num);
      tiledb.delete_uintp(key_len);
      tiledb.delete_charpp(key);
      tiledb.delete_tiledb_datatype_tp(value_type);
    }
  }

  /**
   * Checks if the key is present in the group metadata. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @param key a key to retrieve from the metadata key-value
   * @return true if the key is present in the metadata, false if it is not
   * @throws TileDBError A TileDB exception
   */
  public Boolean hasMetadataKey(String key) throws TileDBError {
    if (!isOpen()) throw new TileDBError("Group with URI: " + uri + " is closed");
    SWIGTYPE_p_tiledb_datatype_t value_type = tiledb.new_tiledb_datatype_tp();
    SWIGTYPE_p_int has_key = tiledb.new_intp();

    try {
      ctx.handleError(
          tiledb.tiledb_group_has_metadata_key(
              ctx.getCtxp(), getGroupp(), key, value_type, has_key));
      return tiledb.intp_value(has_key) > 0;
    } finally {
      tiledb.delete_intp(has_key);
      tiledb.delete_tiledb_datatype_tp(value_type);
    }
  }

  /**
   * Check if the group is open.
   *
   * @return True if the group is open.
   * @throws TileDBError
   */
  public boolean isOpen() throws TileDBError {
    SWIGTYPE_p_int ret = tiledb.new_intp();
    try {
      ctx.handleError(tiledb.tiledb_group_is_open(ctx.getCtxp(), getGroupp(), ret));
      return tiledb.intp_value(ret) != 0;
    } finally {
      tiledb.delete_intp(ret);
    }
  }

  /**
   * * It puts a metadata key-value item to an open group. The group must * be opened in WRITE mode,
   * otherwise the function will error out.
   *
   * @param key The key of the metadata item to be added.
   * @param value The metadata value.
   * @throws TileDBError
   */
  public void putMetadata(String key, NativeArray value) throws TileDBError {
    if (!isOpen()) throw new TileDBError("Group with URI: " + uri + " is closed");
    ctx.handleError(
        tiledb.tiledb_group_put_metadata(
            ctx.getCtxp(),
            getGroupp(),
            key,
            value.getNativeType().toSwigEnum(),
            value.getSize(),
            value.toVoidPointer()));
  }

  /**
   * Get a member of a group by name and relative characteristic of that name
   *
   * @param name name of member to fetch
   * @return True if relative
   * @throws TileDBError
   */
  public boolean getIsRelativeURIByName(String name) throws TileDBError {
    NativeArray arr = new NativeArray(ctx, 1, Datatype.TILEDB_UINT8);
    SWIGTYPE_p_unsigned_char relative = arr.getUint8_tArray().cast();

    try {
      ctx.handleError(
          tiledb.tiledb_group_get_is_relative_uri_by_name(ctx.getCtxp(), groupp, name, relative));
      return ((short) arr.getItem(0) == 1);
    } finally {
      arr.close();
    }
  }

  /**
   * Cleans up the group metadata Note that this will coarsen the granularity of time traveling (see
   * docs for more information).
   *
   * @param config Configuration parameters for the vacuuming. (`null` means default, which will use
   *     the config from `ctx`).
   * @throws TileDBError
   */
  public void vacuumMetadata(Config config) throws TileDBError {
    if (config == null) throw new TileDBError("The Config can not be null");
    ctx.handleError(tiledb.tiledb_group_vacuum_metadata(ctx.getCtxp(), uri, config.getConfigp()));
  }

  /**
   * Consolidates the group metadata into a single group metadata file.
   *
   * @param config Configuration parameters for the vacuuming. (`null` means default, which will use
   *     the config from `ctx`).
   * @throws TileDBError
   */
  public void consolidateMetadata(Config config) throws TileDBError {
    if (config == null) throw new TileDBError("The Config can not be null");
    ctx.handleError(
        tiledb.tiledb_group_consolidate_metadata(ctx.getCtxp(), uri, config.getConfigp()));
  }

  /** Close resources */
  public void close() {
    if (groupp != null && grouppp != null) {
      tiledb.tiledb_group_close(ctx.getCtxp(), groupp);
      tiledb.tiledb_group_free(grouppp);
      tiledb.delete_tiledb_group_tpp(grouppp);
      grouppp = null;
      groupp = null;
    }
  }

  /**
   * Returns the URI of the group
   *
   * @return The URI of the group.
   * @exception TileDBError A TileDB exception
   */
  public String getUri() throws TileDBError {
    return uri;
  }

  /**
   * Reopens a TileDB group (the group must be already open). This is useful when the group got
   * updated after it got opened or when the user wants to reopen with a different queryType.
   *
   * @param queryType the queryType that will be used when the group opens.
   * @throws TileDBError
   */
  public void reopen(Context ctx, QueryType queryType) throws TileDBError {
    if (!isOpen()) throw new TileDBError("Can not reopen group. Group is closed");
    ctx.handleError(tiledb.tiledb_group_close(ctx.getCtxp(), groupp));
    this.ctx = ctx;
    open(queryType);
  }

  /**
   * * Opens a TileDB group. The group is opened using a query type as input. * This is to indicate
   * that queries created for this `tiledb_group_t` * object will inherit the query type. In other
   * words, `tiledb_group_t` * objects are opened to receive only one type of queries. * They can
   * always be closed and be re-opened with another query type. * Also there may be many different
   * `tiledb_group_t` * objects created and opened with different query types.
   *
   * @param queryType the query type to open the group.
   * @throws TileDBError
   */
  private void open(QueryType queryType) throws TileDBError {
    try {
      ctx.handleError(tiledb.tiledb_group_open(ctx.getCtxp(), groupp, queryType.toSwigEnum()));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_group_tpp(grouppp);
      throw err;
    }
  }

  /**
   * Creates a new group. A Group is a logical grouping of TileDB objects on the storage system with
   * the sample path prefix.
   *
   * @param ctx The TileDB context.
   * @param uri The group URI.
   * @exception TileDBError A TileDB exception
   */
  public static void create(Context ctx, String uri) throws TileDBError {
    ctx.handleError(tiledb.tiledb_group_create(ctx.getCtxp(), uri));
  }

  /**
   * Deletes written data from an open group. The group must be opened in MODIFY_EXCLUSIVE mode,
   * otherwise the function will error out.
   *
   * @param recursive 1 if all data inside the group is to be deleted
   * @throws TileDBError
   */
  public void delete(short recursive) throws TileDBError {
    this.reopen(ctx, QueryType.TILEDB_MODIFY_EXCLUSIVE);
    ctx.handleError(
        tiledb.tiledb_group_delete_group(ctx.getCtxp(), this.groupp, this.uri, recursive));
  }
}
