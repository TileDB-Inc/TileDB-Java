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

package examples.io.tiledb.java.api;

import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import io.tiledb.java.api.*;

public class DenseWriteSubarray {
  public static void main(String[] args) throws Exception {

    // Create TileDB context
    Context ctx = new Context();

    // Prepare cell buffers
    NativeArray a1_data = new NativeArray(ctx, new int[] {112, 113, 114, 115}, Integer.class);
    NativeArray a2_offsets = new NativeArray(ctx, new long[] {0, 1, 3, 6}, Long.class);
    NativeArray buffer_var_a2 = new NativeArray(ctx, "mnnooopppp", String.class);

    NativeArray buffer_a3 =
        new NativeArray(
            ctx,
            new float[] {112.1f, 112.2f, 113.1f, 113.2f, 114.1f, 114.2f, 115.1f, 115.2f},
            Float.class);

    // Create query
    Array my_dense_array = new Array(ctx, "my_dense_array", TILEDB_WRITE);
    Query query = new Query(my_dense_array);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setSubarray(new NativeArray(ctx, new long[] {3, 4, 3, 4}, Long.class));
    query.setBuffer("a1", a1_data);
    query.setBuffer("a2", a2_offsets, buffer_var_a2);
    query.setBuffer("a3", buffer_a3);

    // Submit query
    query.submit();
  }
}
