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

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PerformanceTestDenseArray {

  private Context ctx;
  private int max;
  int[] d;
  private NativeArray id_data;

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void test() throws Exception {
    int iterations = 10; // number of iterations for each experiment
    for (max = 1000; max <= 100000000; max *= 10) {
      double sumRead = 0, sumWrite = 0;
      for (int i = 0; i <= iterations; i++) {
        ctx = new Context();
        File arrayDir = temp.newFolder();
        String arrayURI = arrayDir.toString();
        create(arrayURI);
        long start = System.nanoTime();
        for (int k = 1; k <= max; k += max / 10) write(arrayURI, k);
        long write = System.nanoTime();
        read(arrayURI);
        long read = System.nanoTime();
        sumWrite += (double) (write - start) / 1000000;
        sumRead += (double) (read - write) / 1000000;
      }
      String size = "";
      switch (max) {
        case 1000:
          size = "1K";
          break;
        case 10000:
          size = "10K";
          break;
        case 100000:
          size = "100K";
          break;
        case 1000000:
          size = "1M";
          break;
        case 10000000:
          size = "10M";
          break;
        case 100000000:
          size = "100M";
          break;
      }
      System.out.println("Size: " + size + " Write time (ms): " + sumWrite / 100);
      System.out.println("Size: " + size + " Read time (ms): " + sumRead / 100);
    }
  }

  public void create(String arrayURI) throws Exception {
    Dimension<Integer> d1 =
        new Dimension<Integer>(
            ctx, "d1", Integer.class, new Pair<Integer, Integer>(1, max), max / 10);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);
    Attribute id = new Attribute(ctx, "id", Integer.class);
    ArraySchema schema = new ArraySchema(ctx, ArrayType.TILEDB_DENSE);
    schema.setDomain(domain);
    schema.addAttribute(id);
    Array.create(arrayURI, schema);
  }

  public void write(String arrayURI, int offset) throws Exception {
    Array array = new Array(ctx, arrayURI, QueryType.TILEDB_WRITE);
    Query query = new Query(array, QueryType.TILEDB_WRITE);
    d = new int[max / 10];
    for (int k = offset; k < offset + max / 10; k++) {
      d[k - offset] = k;
    }
    id_data = new NativeArray(ctx, d, Integer.class);
    query.setBuffer("id", id_data);
    query.setSubarray(
        new NativeArray(ctx, new int[] {offset, offset - 1 + max / 10}, Integer.class));
    query.submit();
    query.close();
    array.close();
  }

  private void read(String arrayURI) throws Exception {
    Array array = new Array(ctx, arrayURI);
    // Create query
    Query query = new Query(array, QueryType.TILEDB_READ);
    query.setBuffer("id", new NativeArray(ctx, (int) max, Integer.class));
    query.submit();
    int[] id_buff = (int[]) query.getBuffer("id");
    int test = id_buff[100];
    query.close();
    array.close();
  }
}
