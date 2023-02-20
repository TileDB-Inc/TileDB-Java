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

import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import io.tiledb.java.api.*;

public class DenseWriteOrderedSubarray {
  public static void main(String[] args) throws Exception {

    // Create TileDB context
    Context ctx = new Context();

    // Prepare cell buffers
    NativeArray a1_data = new NativeArray(ctx, new int[] {9, 12, 13, 11, 14, 15}, Integer.class);
    NativeArray a2_offsets =
        new NativeArray(ctx, new long[] {0, 2, 3, 5, 9, 12}, Datatype.TILEDB_UINT64);
    NativeArray buffer_var_a2 = new NativeArray(ctx, "jjmnnllllooopppp", String.class);

    NativeArray buffer_a3 =
        new NativeArray(
            ctx,
            new float[] {
              9.1f, 9.2f, 12.1f, 12.2f, 13.1f, 13.2f, 11.1f, 11.2f, 14.1f, 14.2f, 15.1f, 15.2f
            },
            Float.class);

    // Create query
    Array my_dense_array = new Array(ctx, "my_dense_array", TILEDB_WRITE);
    Query query = new Query(my_dense_array);
    query.setLayout(TILEDB_ROW_MAJOR);

    SubArray subArray = new SubArray(ctx, my_dense_array);
    subArray.addRange(0, 3L, 4L, null);
    subArray.addRange(1, 2L, 4L, null);
    query.setSubarray(subArray);

    query.setBuffer("a1", a1_data);
    query.setBuffer("a2", a2_offsets, buffer_var_a2);
    query.setBuffer("a3", buffer_a3);

    // Submit query
    query.submit();
  }
}
