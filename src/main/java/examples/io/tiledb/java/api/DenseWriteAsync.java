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

import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.QueryStatus.TILEDB_INPROGRESS;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import io.tiledb.java.api.*;

public class DenseWriteAsync {
  public static void main(String[] args) throws Exception {
    // Create TileDB context
    Context ctx = new Context();

    // Prepare cell buffers
    NativeArray a1_data =
        new NativeArray(
            ctx,
            new int[] {
              0, 1, 2, 3, 4, 5, 6, 7,
              8, 9, 10, 11, 12, 13, 14, 15
            },
            Integer.class);
    NativeArray a2_offsets =
        new NativeArray(
            ctx,
            new long[] {
              0, 1, 3, 6, 10, 11, 13, 16,
              20, 21, 23, 26, 30, 31, 33, 36
            },
            Datatype.TILEDB_UINT64);
    NativeArray buffer_var_a2 =
        new NativeArray(
            ctx, "abbcccdddd" + "effggghhhh" + "ijjkkkllll" + "mnnooopppp", String.class);

    NativeArray buffer_a3 =
        new NativeArray(
            ctx,
            new float[] {
              0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f, 4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f,
              7.1f, 7.2f, 8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f, 12.1f, 12.2f, 13.1f,
              13.2f, 14.1f, 14.2f, 15.1f, 15.2f
            },
            Float.class);

    // Create query
    Array my_dense_array = new Array(ctx, "my_dense_array", TILEDB_WRITE);
    Query query = new Query(my_dense_array);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setBuffer("a1", a1_data);
    query.setBuffer("a2", a2_offsets, buffer_var_a2);
    query.setBuffer("a3", buffer_a3);

    // Submit query
    query.submitAsync();

    // Wait for query to complete
    System.out.printf("Query in progress\n");
    QueryStatus status;
    do {
      // Wait till query is done
      status = query.getQueryStatus();
    } while (status == TILEDB_INPROGRESS);
  }
}
