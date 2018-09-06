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

/**
 * Java Callback class used to throw exceptions on native method errors.
 *
 * <b>Example:</b>
 *
 * <pre>{@code
 *   Context ctx = new Context();
 *
 *   // Set a custom error handler:
 *   ctx.setErrorHandler(new MyContextCallback());
 *
 *   //Context custom callback class example
 *   private static class MyContextCallback extends ContextCallback {
 *     {@literal @Override}
 *     public void call(String msg) throws TileDBError {
 *       System.out.println("Callback error message: "+msg);
 *     }
 *   }
 * }</pre>
 */
public class ContextCallback {

  /**
   * The default error handler callback.
   *
   * @param msg Error message from native tiledb (JNI) operation.
   * @exception  TileDBError A TileDB exception
   */
  public void call(String msg) throws TileDBError {
    throw new TileDBError(msg);
  }

}