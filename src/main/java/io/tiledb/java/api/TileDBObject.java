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
 *
 */

package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_object_t;
import io.tiledb.libtiledb.tiledb;

/** Represents a TileDB object: array, group, key-value, or none (invalid). */
public class TileDBObject {

  private final Context ctx;
  private TileDBObjectType type;
  private String uri;

  /**
   * Constructs an object that contains the type along with the URI.
   *
   * @param ctx The TileDB context
   * @param uri The path to the object.
   */
  public TileDBObject(Context ctx, String uri) throws TileDBError {
    SWIGTYPE_p_tiledb_object_t typep = tiledb.new_tiledb_object_tp();
    ctx.handleError(tiledb.tiledb_object_type(ctx.getCtxp(), uri, typep));
    this.uri = uri;
    this.ctx = ctx;
    this.type = TileDBObjectType.fromSwigEnum(tiledb.tiledb_object_tp_value(typep));
    tiledb.delete_tiledb_object_tp(typep);
  }

  protected TileDBObject(Context ctx, String uri, TileDBObjectType type){
    this.ctx = ctx;
    this.uri = uri;
    this.type = type;
  }

  /**
   * Deletes a tiledb object.
   *
   * @param ctx The TileDB context
   * @param uri The path to the object to be deleted.
   */
  public static void remove(Context ctx, String uri) throws TileDBError {
    ctx.handleError(tiledb.tiledb_object_remove(ctx.getCtxp(), uri));
  }

  /**
   * Moves/renames a tiledb object.
   *
   * @param old_uri The path to the old object.
   * @param new_uri The path to the new object.
   */
  public static void move(
      Context ctx,
      String old_uri,
      String new_uri) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_object_move(ctx.getCtxp(), old_uri, new_uri));
  }

  /**
   * Returns a string representation of the object, including its type
   * and URI.
   */
  @Override
  public String toString() {
    String ret = "Obj<";
    switch (type) {
      case TILEDB_ARRAY:
        ret += "ARRAY";
        break;
      case TILEDB_GROUP:
        ret += "GROUP";
        break;
      case TILEDB_INVALID:
        ret += "INVALID";
        break;
      case TILEDB_KEY_VALUE:
        ret += "KEYVALUE";
        break;
    }
    ret += " \"" + uri + "\">";
    return ret;
  }

  /** Returns the object type. */
  public TileDBObjectType getType(){
    return type;
  }

  /** Returns the object uri. */
  public String getUri(){
    return uri;
  }
}
