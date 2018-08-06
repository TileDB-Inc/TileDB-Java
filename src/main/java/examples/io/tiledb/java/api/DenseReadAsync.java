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

import io.tiledb.java.api.*;

import java.util.Arrays;
import java.util.HashMap;

import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.QueryStatus.TILEDB_INPROGRESS;
import static io.tiledb.java.api.QueryType.TILEDB_READ;

public class DenseReadAsync {

  public static void main(String[] args) throws Exception {

    // Create TileDB context
    Context ctx = new Context();


    Array my_dense_array = new Array(ctx, "my_dense_array");

    // Calcuate maximum buffer sizes for the query results per attribute
    NativeArray subarray = new NativeArray(ctx, new long[]{1l, 4l, 1l, 4l}, Long.class);
    HashMap<String, Pair<Long,Long>> max_sizes = my_dense_array.maxBufferElements(subarray);


    // Create query
    Query query = new Query(my_dense_array, TILEDB_READ);
    query.setLayout(TILEDB_GLOBAL_ORDER);
    query.setBuffer("a1",
        new NativeArray(ctx, max_sizes.get("a1").getSecond().intValue(),Integer.class));
    query.setBuffer("a2",
        new NativeArray(ctx, max_sizes.get("a2").getFirst().intValue(), Long.class),
        new NativeArray(ctx, max_sizes.get("a2").getSecond().intValue(), String.class));
    query.setBuffer("a3", new NativeArray(ctx, max_sizes.get("a3").getSecond().intValue(), Float.class));


    // Submit query with callback
    query.submitAsync(new ReadCallback("Java Callback: Query completed"));

    // Wait for query to complete
    System.out.printf("Query in progress\n");
    QueryStatus status;
    do {
      // Wait till query is done
      status = query.getQueryStatus();
    } while (status == TILEDB_INPROGRESS);

    // Print cell values (assumes all getAttributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

    int[] a1_buff = (int[]) query.getBuffer("a1");
    long[] a2_offsets = (long[]) query.getVarBuffer("a2");
    byte[] a2_data = (byte[]) query.getBuffer("a2");
    float[] a3_buff = (float[]) query.getBuffer("a3");
    System.out.println("Result num: " + a1_buff.length );
    System.out.println(
        String.format("%9s","a1") +
            String.format("%11s","a2") +
            String.format("%11s","a3[0]") +
            String.format("%10s","a3[1]"));

    for (int i =0; i< a1_buff.length; i++){
      int end = (i==a1_buff.length-1)? a2_data.length : (int) a2_offsets[i+1];
      System.out.println(
          String.format("%9s",a1_buff[i])+
              String.format("%11s",new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[i], end)))+
              String.format("%11s",a3_buff[2*i])+
              String.format("%10s",a3_buff[2*i+1]));
    }

  }

  private static class ReadCallback implements Callback {

    private final String data;

    public ReadCallback(String data) {
      this.data = data;
    }

    public void call() {
      System.out.println(data);
    }
  }
}
