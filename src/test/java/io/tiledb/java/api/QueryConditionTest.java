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

import static io.tiledb.java.api.ArrayType.*;
import static io.tiledb.java.api.Datatype.TILEDB_UINT8;
import static io.tiledb.java.api.Layout.*;
import static io.tiledb.java.api.QueryType.*;
import static io.tiledb.libtiledb.tiledb_query_condition_combination_op_t.TILEDB_AND;
import static io.tiledb.libtiledb.tiledb_query_condition_combination_op_t.TILEDB_OR;
import static io.tiledb.libtiledb.tiledb_query_condition_op_t.TILEDB_EQ;
import static io.tiledb.libtiledb.tiledb_query_condition_op_t.TILEDB_GT;

import java.util.HashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class QueryConditionTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private Context ctx;
  private String arrayURI;
  private String arrayURISparse;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toPath().resolve("my_dense_array").toString();
    arrayURISparse = temp.getRoot().toPath().resolve("my_sparse_array").toString();
  }

  public void arrayCreateSparse() throws TileDBError {
    // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
    Dimension<Integer> d1 =
        new Dimension<Integer>(ctx, "d1", Datatype.TILEDB_STRING_ASCII, null, null);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    // Add two attributes "a1" and "a2", so each (i,j) cell can store
    // a character on "a1" and a vector of two floats on "a2".
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);

    Array.create(arrayURISparse, schema);
  }

  public void arrayWriteSparse() throws TileDBError {

    NativeArray d_data = new NativeArray(ctx, "aabbccddee", Datatype.TILEDB_STRING_ASCII);
    NativeArray d_off = new NativeArray(ctx, new long[] {0, 2, 4, 6, 8}, Datatype.TILEDB_UINT64);

    // Prepare cell buffers
    NativeArray a1 = new NativeArray(ctx, new int[] {1, 2, 3, 4, 5}, Integer.class);

    // Create query
    Array array = new Array(ctx, arrayURISparse, TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(TILEDB_GLOBAL_ORDER);

    query.setBuffer("d1", d_off, d_data);
    query.setBuffer("a1", a1);

    // Submit query
    query.submit();

    query.finalizeQuery();
    query.close();
    array.close();
  }

  public void arrayCreate() throws Exception {
    // Create getDimensions
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 3l), 2l);
    Dimension<Long> d2 =
        new Dimension<Long>(ctx, "d2", Long.class, new Pair<Long, Long>(1l, 3l), 2l);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);
    domain.addDimension(d2);

    // Create and add getAttributes
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    a1.setNullable(true);
    Attribute a2 = new Attribute(ctx, "a2", Float.class);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);

    schema.check();

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {

    Array my_dense_array = new Array(ctx, arrayURI, TILEDB_WRITE);

    // Prepare cell buffers
    NativeArray a1_data =
        new NativeArray(ctx, new int[] {8, 9, 10, 11, 12, 13, 14, 15, 16}, Integer.class);

    NativeArray buffer_a2 =
        new NativeArray(
            ctx,
            new float[] {13.2f, 14.1f, 14.2f, 15.1f, 15.2f, 15.3f, 16.1f, 18.3f, 19.1f},
            Float.class);

    // Create query
    NativeArray a1Bytemap =
        new NativeArray(ctx, new short[] {0, 1, 1, 1, 1, 0, 1, 1, 0}, Datatype.TILEDB_UINT8);
    try (Query query = new Query(my_dense_array, TILEDB_WRITE)) {
      query
          .setLayout(TILEDB_ROW_MAJOR)
          .setBufferNullable("a1", a1_data, a1Bytemap)
          .setBuffer("a2", buffer_a2);
      // Submit query
      query.submit();
      query.finalizeQuery();
    }
  }

  private void arrayRead() throws Exception {
    // Print non-empty getDomain

    Array my_dense_array = new Array(ctx, arrayURI);

    SubArray subArray = new SubArray(ctx, my_dense_array);
    subArray.addRange(0, 1L, 3L, null);
    subArray.addRange(1, 1L, 3L, null);

    // Create query
    try (Query query = new Query(my_dense_array, TILEDB_READ)) {
      query.setLayout(TILEDB_ROW_MAJOR);
      query.setSubarray(subArray);
      HashMap<String, Pair<Long, Long>> max_sizes = new HashMap<>();
      max_sizes.put("a1", query.getEstResultSizeNullable(ctx, "a1"));
      max_sizes.put("a2", new Pair<>(0L, query.getEstResultSize(ctx, "a2")));
      query.setBufferNullable(
          "a1",
          new NativeArray(ctx, max_sizes.get("a1").getSecond().intValue(), Integer.class),
          new NativeArray(ctx, 16, TILEDB_UINT8));
      query.setBuffer(
          "a2", new NativeArray(ctx, max_sizes.get("a2").getSecond().intValue(), Float.class));
      // null + normal + combined condition test
      QueryCondition con1 = new QueryCondition(ctx, "a2", 15.0f, Float.class, TILEDB_GT);
      QueryCondition con2 = new QueryCondition(ctx, "a1", 0, null, TILEDB_EQ);
      QueryCondition con3 = con1.combine(con2, TILEDB_AND);
      QueryCondition con4 = new QueryCondition(ctx, "a1", 9, Integer.class, TILEDB_EQ);
      QueryCondition con5 = con4.combine(con3, TILEDB_OR);
      query.setCondition(con5);

      // Submit query
      query.submit();

      // Print cell values (assumes all getAttributes are read)
      HashMap<String, Pair<Long, Long>> result_el = query.resultBufferElements();

      int[] a1_buff = (int[]) query.getBuffer("a1");
      float[] a2_buff = (float[]) query.getBuffer("a2");
      //      for (int i = 0; i < a1_buff.length; i++) {
      //        System.out.println(a1_buff[i] + " " + a2_buff[i]);
      //      }

      // In the legacy 'sm.query.dense.reader' we expect all cells that satisfy the QC to be
      // filtered out. For the refactored reader, which is the default after 2.7, filtered out means
      // the value is replaced with the fill value. UPDATE: 2.7.2 went back to the legacy reader due
      // to some bugs.
      // check a1
      Assert.assertArrayEquals(
          a1_buff,
          new int[] {
            -2147483648, 9, -2147483648, -2147483648, -2147483648, 13, -2147483648, -2147483648, 16
          });

      // check a2
      Assert.assertArrayEquals(
          a2_buff,
          new float[] {
            Float.NaN, 14.1f, Float.NaN, Float.NaN, Float.NaN, 15.3f, Float.NaN, Float.NaN, 19.1f
          },
          0.1f);
    }
  }

  @Test
  public void testCondition() throws Exception {
    arrayCreate();
    arrayWrite();
    arrayRead();
  }

  @Test
  public void testDataDeletion() throws TileDBError {
    // create array
    arrayCreateSparse();
    arrayWriteSparse();

    // delete data with appropriate QC
    Array array = new Array(ctx, arrayURISparse, TILEDB_DELETE);
    Query query = new Query(array, TILEDB_DELETE);
    QueryCondition deleteQc = new QueryCondition(ctx, "a1", 3, Integer.class, TILEDB_GT);
    query.setCondition(deleteQc);
    query.submit();
    // close resources
    query.close();
    deleteQc.close();
    array.close();

    // check if data was deleted
    array = new Array(ctx, arrayURISparse, TILEDB_READ);

    query = new Query(array, TILEDB_READ);
    query.setBuffer("a1", new NativeArray(ctx, 40, Integer.class));

    while (query.getQueryStatus() != QueryStatus.TILEDB_COMPLETED) {
      query.submit();
    }
    int[] a1_buff = (int[]) query.getBuffer("a1");
    Assert.assertArrayEquals(new int[] {1, 2, 3}, a1_buff);
    array.close();
    query.close();
    deleteQc.close();
  }
}
