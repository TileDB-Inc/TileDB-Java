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
import java.util.List;

import static io.tiledb.java.api.TileDBWalkOrderEnum.*;

/**
 * Enables listing TileDB objects in a directory or walking recursively an
 * entire directory tree.
 */
public class TileDBObjectIterator {
  private final Context ctx;
  private boolean group = true, array = true, kv = true, recursive = false;
  private TileDBWalkOrderEnum walkOrder = TILEDB_PREORDER;
  private String root = ".";

  /**
   * Creates an object iterator. Unless `set_recursive` is invoked, this
   * iterator will iterate only over the children of `root`. It will
   * also retrieve only TileDB-related objects.
   *
   * @param ctx The TileDB context.
   * @param root The root directory where the iteration will begin.
   */
  public TileDBObjectIterator(Context ctx, String root){
    this.ctx = ctx;
    this.root = root;
  }

  /**
   * Constructor with all fields
   * @param ctx The TileDB context.
   * @param group If `true`, groups will be considered.
   * @param array If `true`, arrays will be considered.
   * @param kv If `true`, key-values will be considered.
   * @param recursive If `true`, the object will be listed for all nested folders.
   * @param walkOrder The walk order.
   * @param root The root directory where the iteration will begin.
   */
  public TileDBObjectIterator(Context ctx, boolean group, boolean array, boolean kv, boolean recursive, TileDBWalkOrderEnum walkOrder, String root) {
    this.ctx = ctx;
    this.group = group;
    this.array = array;
    this.kv = kv;
    this.recursive = recursive;
    this.walkOrder = walkOrder;
    this.root = root;
  }

  /**
   * Determines whether group, array and key-value objects will be iterated
   * on during the walk. The default (if the function is not invoked) is
   * `true` for all objects.
   *
   * @param group If `true`, groups will be considered.
   * @param array If `true`, arrays will be considered.
   * @param kv If `true`, key-values will be considered.
   */
  public void setIteratorPolicy(boolean group, boolean array, boolean kv){
    this.group = group;
    this.array = array;
    this.kv = kv;
  }

  /**
   * Specifies that the iteration will be over all the directories in the
   * tree rooted at `root`.
   *
   * @param walkOrder The walk order.
   */
  public void setRecursive(TileDBWalkOrderEnum walkOrder){
    recursive = true;
    setWalkOrder(walkOrder);
  }

  /**
   * Specifies that the iteration will be over all the directories in the
   * tree rooted at `root`. Default order is preorder
   */
  public void setRecursive(){
    recursive = true;
    setWalkOrder(TILEDB_PREORDER);
  }

  /** Disables recursive traversal. */
  public void setNonRecursive(){
    recursive = false;
  }

  public boolean isGroup() {
    return group;
  }

  public void setGroup(boolean group) {
    this.group = group;
  }

  public boolean isArray() {
    return array;
  }

  public void setArray(boolean array) {
    this.array = array;
  }

  public boolean isKv() {
    return kv;
  }

  public void setKv(boolean kv) {
    this.kv = kv;
  }

  public TileDBWalkOrderEnum getWalkOrder() {
    return walkOrder;
  }

  public List<TileDBObject> getAllObjects() throws TileDBError {
    ObjectGetter objectGetter = new ObjectGetter();
    if (recursive) {
      ctx.handleError(
          Utils.tiledb_object_walk(ctx.getCtxp(), root, walkOrder.toSwigEnum(), objectGetter));
    } else {
      ctx.handleError(
          Utils.tiledb_object_ls(ctx.getCtxp(), root, objectGetter));
    }
    return objectGetter.getObjects();
  }

  public void setWalkOrder(TileDBWalkOrderEnum walkOrder) {
    this.walkOrder = walkOrder;
  }

  private class ObjectGetter extends PathCallback {
    List<TileDBObject> objects;

    public ObjectGetter() {
      objects = new ArrayList<>();
    }

    public List<TileDBObject> getObjects() {
      return objects;
    }

    public void setObjects(List<TileDBObject> objects) {
      this.objects = objects;
    }

    @Override
    public int call(String path, tiledb_object_t type) {
      if ((type == tiledb_object_t.TILEDB_ARRAY && array) ||
          (type == tiledb_object_t.TILEDB_GROUP && group) ||
          (type == tiledb_object_t.TILEDB_KEY_VALUE && kv)) {
        TileDBObject object = null;
        try {
          object = new TileDBObject(ctx, path, TileDBObjectEnum.fromSwigEnum(type));
        } catch (TileDBError tileDBError) {
          tileDBError.printStackTrace();
        }
        objects.add(object);
      }
      // Always iterate till the end
      return 1;
    }
  }
}
