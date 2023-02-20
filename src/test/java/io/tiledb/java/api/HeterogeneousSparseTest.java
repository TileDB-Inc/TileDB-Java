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
import static io.tiledb.java.api.Layout.TILEDB_GLOBAL_ORDER;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class HeterogeneousSparseTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private Context ctx;
  private String arrayURI;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toPath().resolve("my_sparse_array").toString();
  }

  @Test
  public void testQuickstartSparse() throws Exception {
    arrayCreate();
    arrayWrite();
    arrayRead();
  }

  public void arrayCreate() throws Exception {
    Dimension<Integer> d1 =
        new Dimension<Integer>(ctx, "d1", Datatype.TILEDB_STRING_ASCII, null, null);
    Dimension<Integer> d2 =
        new Dimension<Integer>(
            ctx, "d2", Datatype.TILEDB_INT32, new Pair<Integer, Integer>(0, 100), 2);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1).addDimension(d2);

    // Add two attributes "a1" and "a2", so each (i,j) cell can store
    // a character on "a1" and a vector of two floats on "a2".
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {
    NativeArray d1_data = new NativeArray(ctx, "aabbccddee", Datatype.TILEDB_STRING_ASCII);
    NativeArray d1_off = new NativeArray(ctx, new long[] {0, 2, 4, 6, 8}, Datatype.TILEDB_UINT64);
    NativeArray d2_data = new NativeArray(ctx, new int[] {1, 4, 9, 10, 12}, Datatype.TILEDB_INT32);

    // Prepare cell buffers
    NativeArray a1 = new NativeArray(ctx, new int[] {1, 2, 3, 4, 5}, Integer.class);

    // Create query
    try (Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
        Query query = new Query(array)) {
      query.setLayout(TILEDB_GLOBAL_ORDER);

      query.setBuffer("d1", d1_off, d1_data);
      query.setBuffer("d2", d2_data);
      query.setBuffer("a1", a1);

      // Submit query
      query.submit();

      query.finalizeQuery();
    }
  }

  private void arrayRead() throws Exception {
    NativeArray d_data = new NativeArray(ctx, 20, Datatype.TILEDB_STRING_ASCII);
    NativeArray d_off = new NativeArray(ctx, 20, Datatype.TILEDB_UINT64);

    NativeArray d2_data = new NativeArray(ctx, 20, Datatype.TILEDB_INT32);

    try (Array array = new Array(ctx, arrayURI);
        Query q = new Query(array, TILEDB_READ)) {
      q.setLayout(TILEDB_GLOBAL_ORDER);

      q.setBuffer("d1", d_off, d_data);
      q.setBuffer("d2", d2_data);

      SubArray subArray = new SubArray(ctx, array);
      subArray.addRangeVar(0, "a", "z");
      q.setSubarray(subArray);

      q.submit();

      byte[] data = (byte[]) q.getBuffer("d1");
      long[] offsets = q.getVarBuffer("d1");
      int[] d2 = (int[]) q.getBuffer("d2");

      String[] results = new String[offsets.length];

      results = Util.bytesToStrings(offsets, data);

      Assert.assertArrayEquals(results, new String[] {"aa", "bb", "cc", "dd", "ee"});
      Assert.assertArrayEquals(d2, new int[] {1, 4, 9, 10, 12});
    }
  }
}
