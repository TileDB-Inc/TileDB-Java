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

package examples.io.tiledb.libtiledb;

import java.math.BigInteger;

import io.tiledb.libtiledb.*;

public class TiledbSparseReadSubsetIncomplete {

  /*
   * It shows how to read from a sparse array, constraining the read to a
   * specific subarray and a subset of getAttributes. Moreover, the program shows
   * how to handle incomplete queries that did not complete because the input
   * buffers were not big enough to hold the entire result.
   *
   * You need to run the following to make it work:
   *
   * $ ./tiledb_sparse_create_c
   * $ ./tiledb_sparse_write_global_1_c
   * $ ./tiledb_sparse_read_subset_incomplete_c
   */
  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Open array
    SWIGTYPE_p_p_tiledb_array_t arraypp = Utils.new_tiledb_array_tpp();
    tiledb.tiledb_array_alloc(ctx, "my_sparse_array", arraypp);
    SWIGTYPE_p_tiledb_array_t arrayp = Utils.tiledb_array_tpp_value(arraypp);
    tiledb.tiledb_array_open(ctx, arrayp, tiledb_query_type_t.TILEDB_READ);

    // Prepare cell buffers
    intArray buffer_a1 = new intArray(2);
    SWIGTYPE_p_p_void buffers = tiledb.new_voidpArray(1);
    tiledb.voidpArray_setitem(buffers, 0, PointerUtils.toVoid(buffer_a1));

    uint64_tArray buffer_sizes = new uint64_tArray(1);
    buffer_sizes.setitem(0, new BigInteger("8"));

    // Create query
    SWIGTYPE_p_p_char attributes = tiledb.new_charpArray(1);
    tiledb.charpArray_setitem(attributes, 0, "a1");
    long[] subarray_ = {3, 4, 2, 4};
    uint64_tArray subarray = Utils.newUint64Array(subarray_);
    SWIGTYPE_p_p_tiledb_query_t querypp = Utils.new_tiledb_query_tpp();
    tiledb.tiledb_query_alloc(ctx, arrayp,
        tiledb_query_type_t.TILEDB_READ, querypp);
    SWIGTYPE_p_tiledb_query_t query = Utils.tiledb_query_tpp_value(querypp);
    tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_COL_MAJOR);
    tiledb.tiledb_query_set_subarray(ctx, query,
        PointerUtils.toVoid(subarray));
    tiledb.tiledb_query_set_buffers(ctx, query, attributes, 1, buffers,
        buffer_sizes.cast());

    // Loop until the query is completed
    System.out.printf("a1\n---\n");
    SWIGTYPE_p_tiledb_query_status_t statusp = tiledb
        .new_tiledb_query_status_tp();
    do {
      System.out.printf("Reading cells...\n");
      tiledb.tiledb_query_submit(ctx, query);

      // Print cell values
      int result_num = buffer_sizes.getitem(0).intValue() / 4;
      for (int i = 0; i < result_num; ++i)
        System.out.printf("%d\n", buffer_a1.getitem(i));

      // Get status
      tiledb.tiledb_query_get_status(ctx, query, statusp);
    } while (tiledb.tiledb_query_status_tp_value(statusp) == tiledb_query_status_t.TILEDB_INCOMPLETE);

    // Finalize query
    tiledb.tiledb_query_finalize(ctx, query);

    // Close array
    tiledb.tiledb_array_close(ctx, arrayp);

    // Clean up
    tiledb.tiledb_array_free(arraypp);
    tiledb.tiledb_query_free(querypp);
    tiledb.tiledb_ctx_free(ctxpp);
    buffer_a1.delete();
  }
}
