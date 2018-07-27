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
 * @section DESCRIPTION
 *
 * List/Walk a directory for TileDB Objects.
 *
 * Create some object hierarchy and then run:
 *
 */

package examples.io.tiledb.java.api;

import io.tiledb.java.api.Context;
import io.tiledb.java.api.TileDBError;
import io.tiledb.java.api.TileDBObject;
import io.tiledb.java.api.TileDBObjectIterator;
import io.tiledb.libtiledb.tiledb_walk_order_t;

import static io.tiledb.java.api.TileDBWalkOrderEnum.TILEDB_POSTORDER;

public class ObjectLsWalk {
  public static void main (String[] args) throws TileDBError {
    // Create TileDB context
    Context ctx = new Context();
    // List children
    System.out.println( "List children: ");
    TileDBObjectIterator obj_iter = new TileDBObjectIterator(ctx, "my_group");
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);


    // Walk in a path with a pre- and post-order traversal
    System.out.println( "\nPreorder traversal: ");
    obj_iter.setRecursive(); // Default order is preorder
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);

    System.out.println( "\nPostorder traversal: ");
    obj_iter.setRecursive(TILEDB_POSTORDER);
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);

    // Walk in a path, but list only groups
    System.out.println( "\nOnly groups: ");
    obj_iter.setRecursive(); // Default order is preorder
    obj_iter.setIteratorPolicy(true, false, false);
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);

    // Walk in a path, but list only arrays
    System.out.println( "\nOnly arrays: ");
    obj_iter.setRecursive(); // Default order is preorder
    obj_iter.setIteratorPolicy(false, true, false);
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);

    // Walk in a path, but list only groups and arrays
    System.out.println( "\nOnly groups and arrays: ");
    obj_iter.setRecursive(); // Default order is preorder
    obj_iter.setIteratorPolicy(true, true, false);
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);
  }
}
