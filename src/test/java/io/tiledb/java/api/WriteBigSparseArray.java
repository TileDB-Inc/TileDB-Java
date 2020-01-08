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

package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_SPARSE;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.Layout.TILEDB_UNORDERED;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.util.HashSet;
import java.util.Random;

public class WriteBigSparseArray {
  private static Array array;

  public static void main(String[] args) throws Exception {
    create();

    HashSet<Long> set = new HashSet<>();
    for (int i = 0; i < 100; i++) {
      write(i, set);
    }
  }

  private static void read() throws Exception {}

  public static void create() throws Exception {
    // Create TileDB context
    Context ctx = new Context();
    // Create getDimensions
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(0l, 10000l), 100l);
    Dimension<Long> d2 =
        new Dimension<Long>(ctx, "d2", Long.class, new Pair<Long, Long>(0l, 10000l), 100l);
    // Create getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);
    domain.addDimension(d2);

    // Create and add getAttributes
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    a1.setFilterList(new FilterList(ctx).addFilter(new GzipFilter(ctx)));

    // Create array schema
    ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setCapacity(100);
    schema.setDomain(domain);
    schema.addAttribute(a1);

    // Check array schema
    try {
      schema.check();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Print array schema contents
    schema.dump();
    Array.create("my_big_array", schema);
  }

  public static void write(int index, HashSet<Long> set) throws Exception {
    // Create TileDB context
    Context ctx = ctx = new Context();
    int size = 1000;
    // Prepare cell buffers
    int[] d = new int[size];
    long[] coords = new long[size * 2];
    Random random = new Random();
    for (int i = 0; i < size; i++) {
      d[i] = i;
      Pair<Long, Long> p = nextUnique(random, set, 10000l);
      coords[2 * i] = p.getFirst();
      coords[2 * i + 1] = p.getSecond();
    }
    NativeArray a1_data = new NativeArray(ctx, d, Integer.class);

    NativeArray coords_buff = new NativeArray(ctx, coords, Long.class);

    // Create query
    Query query = new Query(array, TILEDB_WRITE);
    query.setLayout(TILEDB_UNORDERED);
    query.setBuffer("a1", a1_data);
    query.setCoordinates(coords_buff);

    // Submit query
    query.submit();
    query.close();
  }

  private static Pair<Long, Long> nextUnique(Random random, HashSet<Long> set, long max) {
    Long l = Math.abs(random.nextLong()) % (max * max);
    long x = l % max;
    long y = l / max;
    Pair<Long, Long> p = new Pair<>(x, y);
    while (set.contains(l)) {
      l = Math.abs(random.nextLong()) % (max * max);
      x = l % max;
      y = l / max;
      p = new Pair<>(x, y);
    }
    set.add(l);
    return p;
  }
}
