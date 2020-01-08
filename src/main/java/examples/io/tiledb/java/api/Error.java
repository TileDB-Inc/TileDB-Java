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
 *
 * @section DESCRIPTION
 *
 * This example shows how to catch errors.
 *
 */

package examples.io.tiledb.java.api;

import io.tiledb.java.api.Context;
import io.tiledb.java.api.ContextCallback;
import io.tiledb.java.api.Group;
import io.tiledb.java.api.TileDBError;

public class Error {

  public static void main(String[] args) throws TileDBError {
    // Create TileDB context
    Context ctx = new Context();

    // Catch an error
    try {
      Group group = new Group(ctx, "my_group");
      group = new Group(ctx, "my_group");
    } catch (TileDBError e) {
      System.out.println("TileDB exception: " + e.toString());
    }

    // Set a different error handler
    ctx.setErrorHandler(new CustomCallback());
    Group group = new Group(ctx, "my_group");
  }

  public static class CustomCallback extends ContextCallback {
    /** The default error handler callback. */
    public void call(String msg) throws TileDBError {
      throw new TileDBError("Callback: " + msg);
    }
  }
}
