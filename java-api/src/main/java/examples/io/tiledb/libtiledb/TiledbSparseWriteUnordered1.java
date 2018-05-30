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

import io.tiledb.libtiledb.*;

public class TiledbSparseWriteUnordered1 {

  /*
   * It shows how to write unordered cells to a sparse array in a single
   * write.
   *
   * You need to run the following to make this work:
   *
   * ./tiledb_sparse_create_c ./tiledb_sparse_write_unordered_1_c
   */
  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Open array
    SWIGTYPE_p_p_tiledb_array_t arraypp = Utils.new_tiledb_array_tpp();
    tiledb.tiledb_array_alloc(ctx, "my_sparse_array", arraypp);
    SWIGTYPE_p_tiledb_array_t arrayp = Utils.tiledb_array_tpp_value(arraypp);
    tiledb.tiledb_array_open(ctx, arrayp);

    // Prepare cell buffers - #1
    int[] buffer_a1 = {7, 5, 0, 6, 4, 3, 1, 2};
    intArray a1 = Utils.newIntArray(buffer_a1);
    long[] buffer_a2 = {0, 4, 6, 7, 10, 11, 15, 17};
    uint64_tArray a2 = Utils.newUint64Array(buffer_a2);
    String buffer_var_a2 = "hhhhffagggeddddbbccc";
    charArray var_a2 = Utils.newCharArray(buffer_var_a2);

    float buffer_a3[] = {7.1f, 7.2f, 5.1f, 5.2f, 0.1f, 0.2f, 6.1f, 6.2f,
        4.1f, 4.2f, 3.1f, 3.2f, 1.1f, 1.2f, 2.1f, 2.2f};
    floatArray a3 = Utils.newFloatArray(buffer_a3);

    long buffer_coords_[] = {3, 4, 4, 2, 1, 1, 3, 3, 3, 1, 2, 3, 1, 2, 1, 4};
    uint64_tArray buffer_coords = Utils.newUint64Array(buffer_coords_);

    SWIGTYPE_p_p_void buffers = tiledb.new_voidpArray(5);
    tiledb.voidpArray_setitem(buffers, 0, PointerUtils.toVoid(a1));
    tiledb.voidpArray_setitem(buffers, 1, PointerUtils.toVoid(a2));
    tiledb.voidpArray_setitem(buffers, 2, PointerUtils.toVoid(var_a2));
    tiledb.voidpArray_setitem(buffers, 3, PointerUtils.toVoid(a3));
    tiledb.voidpArray_setitem(buffers, 4,
        PointerUtils.toVoid(buffer_coords));
    long buffer_sizes_[] = {buffer_a1.length * 4, buffer_a2.length * 8,
        buffer_var_a2.length(), buffer_a3.length * 4,
        buffer_coords_.length * 8};
    uint64_tArray buffer_sizes = Utils.newUint64Array(buffer_sizes_);

    // Create query
    SWIGTYPE_p_p_tiledb_query_t querypp = Utils.new_tiledb_query_tpp();
    SWIGTYPE_p_p_char attributes = tiledb.new_charpArray(4);
    tiledb.charpArray_setitem(attributes, 0, "a1");
    tiledb.charpArray_setitem(attributes, 1, "a2");
    tiledb.charpArray_setitem(attributes, 2, "a3");
    tiledb.charpArray_setitem(attributes, 3, tiledb.tiledb_coords());
    tiledb.tiledb_query_alloc(ctx, querypp, arrayp,
        tiledb_query_type_t.TILEDB_WRITE);
    SWIGTYPE_p_tiledb_query_t query = Utils.tiledb_query_tpp_value(querypp);
    tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_UNORDERED);
    tiledb.tiledb_query_set_buffers(ctx, query, attributes, 4, buffers,
        buffer_sizes.cast());

    // Submit query
    tiledb.tiledb_query_submit(ctx, query);

    // Finalize query
    tiledb.tiledb_query_finalize(ctx, query);

    // Close array
    tiledb.tiledb_array_close(ctx, arrayp);

    // Clean up
    tiledb.tiledb_array_free(arraypp);
    tiledb.tiledb_query_free(querypp);
    tiledb.tiledb_ctx_free(ctxpp);

  }
}
