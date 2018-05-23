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
 * It creates a hierarchical directory structure with three groups:
 *     my_group
 *        |_ dense_arrays
 *        |_ sparse_arrays
 *
 * Make sure that no folder with name `my_group` exists in the working
 * directory before running this example.
 */

package examples.io.tiledb.java.api;

import io.tiledb.java.api.Context;
import io.tiledb.java.api.Group;
import io.tiledb.java.api.TileDBError;

public class GroupCreate {

  public static void main (String[] args) throws TileDBError {
    Context ctx = new Context();
    Group group1 = new Group(ctx, "my_group");
    Group group2 = new Group(ctx, "my_group/dense_arrays");
    Group group3 = new Group(ctx, "my_group/sparse_arrays");
  }
}
