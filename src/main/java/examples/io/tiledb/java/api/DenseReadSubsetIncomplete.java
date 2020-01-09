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

package examples.io.tiledb.java.api;

import static io.tiledb.java.api.Layout.TILEDB_COL_MAJOR;
import static io.tiledb.java.api.QueryStatus.TILEDB_INCOMPLETE;
import static io.tiledb.java.api.QueryType.TILEDB_READ;

import io.tiledb.java.api.*;

public class DenseReadSubsetIncomplete {
  public static void main(String[] args) throws Exception {
    // Create TileDB context
    Context ctx = new Context();

    Array my_dense_array = new Array(ctx, "my_dense_array");

    // Create query
    Query query = new Query(my_dense_array, TILEDB_READ);
    query.setLayout(TILEDB_COL_MAJOR);
    long[] subarray = {1l, 4l, 1l, 4l};
    query.setSubarray(new NativeArray(ctx, subarray, Long.class));
    query.setBuffer("a1", new NativeArray(ctx, 34, Integer.class));

    // Loop until the query is completed

    System.out.println("a1\n---");
    do {
      System.out.println("Reading cells...");
      query.submit();

      int[] a1_buff = (int[]) query.getBuffer("a1");
      for (int i = 0; i < a1_buff.length; i++) {
        System.out.println(a1_buff[i]);
      }
    } while (query.getQueryStatus() == TILEDB_INCOMPLETE);
  }
}
