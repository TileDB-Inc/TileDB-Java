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

public class TiledbDenseReadSubsetIncomplete {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

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
    tiledb.tiledb_query_create(ctx, querypp, "my_dense_array",
        tiledb_query_type_t.TILEDB_READ);
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
      tiledb.tiledb_query_get_attribute_status(ctx, query, "a1", statusp);
    } while (tiledb.tiledb_query_status_tp_value(statusp) == tiledb_query_status_t.TILEDB_INCOMPLETE);

    // Clean up
    tiledb.tiledb_query_free(ctx, querypp);
    tiledb.tiledb_ctx_free(ctxpp);
  }
}
