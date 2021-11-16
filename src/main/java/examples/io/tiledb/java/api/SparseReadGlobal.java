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
* It shows how to read a complete sparse array in the global cell order.
*
* You need to run the following to make it work:
*
* SparseCreate
* SparseWriteGlobal1
* SparseReadGlobal

*/

package examples.io.tiledb.java.api;

import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.QueryType.TILEDB_READ;

import io.tiledb.java.api.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SparseReadGlobal {
  public static void main(String[] args) throws Exception {
    // Create TileDB context
    Context ctx = new Context();

    // Print non-empty getDomain
    Array my_sparse_array = new Array(ctx, "my_sparse_array");
    HashMap<String, Pair> dom = my_sparse_array.nonEmptyDomain();
    for (Map.Entry<String, Pair> e : dom.entrySet()) {
      System.out.println(
          e.getKey() + ": (" + e.getValue().getFirst() + ", " + e.getValue().getSecond() + ")");
    }

    // Print maximum buffer elements for the query results per attribute
    NativeArray subarray = new NativeArray(ctx, new long[] {1l, 4l, 1l, 4l}, Long.class);

    // Create query
    Query query = new Query(my_sparse_array, TILEDB_READ);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setSubarray(subarray);
    HashMap<String, Pair<Long, Long>> max_sizes = query.getResultEstimations();
    for (Map.Entry<String, Pair<Long, Long>> e : max_sizes.entrySet()) {
      System.out.println(
          e.getKey() + " (" + e.getValue().getFirst() + ", " + e.getValue().getSecond() + ")");
    }

    query.setBuffer(
        "a1", new NativeArray(ctx, max_sizes.get("a1").getSecond().intValue(), Integer.class));
    query.setBuffer(
        "a2",
        new NativeArray(ctx, max_sizes.get("a2").getFirst().intValue(), Datatype.TILEDB_UINT64),
        new NativeArray(ctx, max_sizes.get("a2").getSecond().intValue(), String.class));
    query.setBuffer(
        "a3", new NativeArray(ctx, max_sizes.get("a3").getSecond().intValue(), Float.class));

    query.setBuffer(
        "d1", new NativeArray(ctx, max_sizes.get("d1").getSecond().intValue(), Long.class));

    query.setBuffer(
        "d2", new NativeArray(ctx, max_sizes.get("d2").getSecond().intValue(), Long.class));

    // Submit query
    System.out.println("Query submitted: " + query.submit());

    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();
    int[] a1_buff = (int[]) query.getBuffer("a1");
    long[] a2_offsets = (long[]) query.getVarBuffer("a2");
    byte[] a2_data = (byte[]) query.getBuffer("a2");
    float[] a3_buff = (float[]) query.getBuffer("a3");
    long[] d1_buff = (long[]) query.getBuffer("d1");
    long[] d2_buff = (long[]) query.getBuffer("d2");

    System.out.println("Result num: " + a1_buff.length);
    System.out.println(
        "d1"
            + String.format("%9s", "d2")
            + String.format("%9s", "a1")
            + String.format("%11s", "a2")
            + String.format("%11s", "a3[0]")
            + String.format("%10s", "a3[1]"));

    for (int i = 0; i < a1_buff.length; i++) {
      int end = (i == a1_buff.length - 1) ? a2_data.length : (int) a2_offsets[i + 1];
      System.out.println(
          String.format("%8s", "(" + d1_buff[2 * i] + ", " + d2_buff[2 * i] + ")")
              + String.format("%9s", a1_buff[i])
              + String.format(
                  "%11s", new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[i], end)))
              + String.format("%11s", a3_buff[2 * i])
              + String.format("%10s", a3_buff[2 * i + 1]));
    }
  }
}
