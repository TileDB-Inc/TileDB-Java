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

package examples;

import io.tiledb.libtiledb.*;

public class TiledbSparseWriteUnordered2 {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Prepare cell buffers - #1
    int[] buffer_a1 = {7, 5, 0};
    intArray a1 = Utils.newIntArray(buffer_a1);
    long[] buffer_a2 = {0, 4, 6};
    uint64_tArray a2 = Utils.newUint64Array(buffer_a2);
    String buffer_var_a2 = "hhhhffa";
    charArray var_a2 = Utils.newCharArray(buffer_var_a2);

    float buffer_a3[] = {7.1f, 7.2f, 5.1f, 5.2f, 0.1f, 0.2f};
    floatArray a3 = Utils.newFloatArray(buffer_a3);

    long buffer_coords_[] = {3, 4, 4, 2, 1, 1};
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
    tiledb.tiledb_query_create(ctx, querypp, "my_sparse_array",
        tiledb_query_type_t.TILEDB_WRITE);
    SWIGTYPE_p_tiledb_query_t query = Utils.tiledb_query_tpp_value(querypp);
    tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_UNORDERED);
    tiledb.tiledb_query_set_buffers(ctx, query, attributes, 4, buffers,
        buffer_sizes.cast());

    // Submit query - #1
    tiledb.tiledb_query_submit(ctx, query);

    // Prepare cell buffers - #2
    int[] buffer_a1_2 = {6, 4, 3, 1, 2};
    intArray a1_2 = Utils.newIntArray(buffer_a1_2);
    long[] buffer_a2_2 = {0, 3, 4, 8, 10};
    uint64_tArray a2_2 = Utils.newUint64Array(buffer_a2_2);
    String buffer_var_a2_2 = "gggeddddbbccc";
    charArray var_a2_2 = Utils.newCharArray(buffer_var_a2_2);

    float buffer_a3_2[] = {6.1f, 6.2f, 4.1f, 4.2f, 3.1f, 3.2f, 1.1f, 1.2f,
        2.1f, 2.2f};
    floatArray a3_2 = Utils.newFloatArray(buffer_a3_2);

    long buffer_coords_2_[] = {3, 3, 3, 1, 2, 3, 1, 2, 1, 4};
    uint64_tArray buffer_coords_2 = Utils
        .newUint64Array(buffer_coords_2_);

    SWIGTYPE_p_p_void buffers_2 = tiledb.new_voidpArray(5);
    tiledb.voidpArray_setitem(buffers_2, 0, PointerUtils.toVoid(a1_2));
    tiledb.voidpArray_setitem(buffers_2, 1, PointerUtils.toVoid(a2_2));
    tiledb.voidpArray_setitem(buffers_2, 2, PointerUtils.toVoid(var_a2_2));
    tiledb.voidpArray_setitem(buffers_2, 3, PointerUtils.toVoid(a3_2));
    tiledb.voidpArray_setitem(buffers_2, 4,
        PointerUtils.toVoid(buffer_coords_2));
    long buffer_sizes_2_[] = {buffer_a1_2.length * 4,
        buffer_a2_2.length * 8, buffer_var_a2_2.length(),
        buffer_a3_2.length * 4, buffer_coords_2_.length * 8};
    uint64_tArray buffer_sizes_2 = Utils
        .newUint64Array(buffer_sizes_2_);

    // Reset buffers
    tiledb.tiledb_query_reset_buffers(ctx, query, buffers_2,
        buffer_sizes_2.cast());

    // Submit query - #2
    tiledb.tiledb_query_submit(ctx, query);

    // Clean up
    tiledb.tiledb_query_free(ctx, querypp);
    tiledb.tiledb_ctx_free(ctxpp);

  }
}
