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
 * It shows how to write to a sparse array with a single write operation,
 * assuming that the user provides the cells ordered in the array global
 * cell order.
 *
 * You need to run the following to make this work:
 *
 * SparseCreate
 * SparseWriteGlobal1
 */

package examples.io.tiledb.java.api;

import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import io.tiledb.java.api.*;

public class SparseWriteGlobal1 {
  public static void main(String[] args) throws Exception {
    // Create TileDB context
    Context ctx = ctx = new Context();

    // Prepare cell buffers
    NativeArray a1_data =
        new NativeArray(ctx, new int[] {0, 1, 2, 3, 4, 5, 6, 7}, Datatype.TILEDB_INT32);
    NativeArray a2_offsets =
        new NativeArray(ctx, new long[] {0, 1, 3, 6, 10, 11, 13, 16}, Datatype.TILEDB_UINT64);
    NativeArray buffer_var_a2 =
        new NativeArray(ctx, "abbcccdddd" + "effggghhhh", Datatype.TILEDB_CHAR);

    NativeArray buffer_a3 =
        new NativeArray(
            ctx,
            new float[] {
              0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f, 4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f,
              7.1f, 7.2f
            },
            Float.class);

    NativeArray d1_buff =
        new NativeArray(ctx, new long[] {1, 1, 1, 2, 3, 4, 3, 3}, Datatype.TILEDB_INT64);

    NativeArray d2_buff =
        new NativeArray(ctx, new long[] {1, 2, 4, 3, 1, 2, 3, 4}, Datatype.TILEDB_INT64);

    // Create query
    Array my_sparse_array = new Array(ctx, "my_sparse_array", TILEDB_WRITE);
    Query query = new Query(my_sparse_array);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setBuffer("d1", d1_buff);
    query.setBuffer("d2", d2_buff);
    query.setBuffer("a1", a1_data);
    query.setBuffer("a2", a2_offsets, buffer_var_a2);
    query.setBuffer("a3", buffer_a3);

    // Submit query
    query.submit();

    query.close();
  }
}
