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

public class TiledbDenseWriteGlobal1 {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = tiledb.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = tiledb.tiledb_ctx_tpp_value(ctxpp);

    // Open array
    SWIGTYPE_p_p_tiledb_array_t arraypp = tiledb.new_tiledb_array_tpp();
    tiledb.tiledb_array_alloc(ctx, "my_dense_array", arraypp);
    SWIGTYPE_p_tiledb_array_t arrayp = tiledb.tiledb_array_tpp_value(arraypp);
    tiledb.tiledb_array_open(ctx, arrayp, tiledb_query_type_t.TILEDB_WRITE);

    // Prepare cell buffers
    int[] buffer_a1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
        15};
    int32_tArray a1 = Utils.newInt32Array(buffer_a1);
    long[] buffer_a2 = {0, 1, 3, 6, 10, 11, 13, 16, 20, 21, 23, 26, 30,
        31, 33, 36};
    uint64_tArray a2 = Utils.newUint64Array(buffer_a2);
    String buffer_var_a2 = "aβcccdddd" + "effggghhhh" + "ijjkkkllll"
        + "mnnooopppp";
    charArray var_a2 = Utils.newCharArray(buffer_var_a2);

    float buffer_a3[] = {0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
        4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f, 8.1f, 8.2f,
        9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f, 12.1f, 12.2f, 13.1f,
        13.2f, 14.1f, 14.2f, 15.1f, 15.2f};
    floatArray a3 = Utils.newFloatArray(buffer_a3);

    // Create query
    SWIGTYPE_p_p_tiledb_query_t querypp = tiledb.new_tiledb_query_tpp();
    tiledb.tiledb_query_alloc(ctx, arrayp,
        tiledb_query_type_t.TILEDB_WRITE, querypp);
    SWIGTYPE_p_tiledb_query_t query = tiledb.tiledb_query_tpp_value(querypp);
    tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_GLOBAL_ORDER);
    
    long[] buffer_a1_size = {buffer_a1.length * tiledb.sizeOfType(a1)};
    uint64_tArray a1_size = Utils.newUint64Array(buffer_a1_size);
    tiledb.tiledb_query_set_buffer(ctx, query, "a1",
	PointerUtils.toVoid(a1), a1_size.cast());
   
    long[] buffer_a2_size = {buffer_a2.length * tiledb.sizeOfType(a2)};
    uint64_tArray a2_size = Utils.newUint64Array(buffer_a2_size);
    long[] buffer_var_a2_size = {buffer_var_a2.length() * tiledb.sizeOfType(var_a2)};
    uint64_tArray var_a2_size = Utils.newUint64Array(buffer_var_a2_size);
    tiledb.tiledb_query_set_buffer_var(ctx, query, "a2",
        a2.cast(), a2_size.cast(),
	PointerUtils.toVoid(var_a2), var_a2_size.cast());

    long[] buffer_a3_size = {buffer_a3.length * tiledb.sizeOfType(a3)};
    uint64_tArray a3_size = Utils.newUint64Array(buffer_a3_size);
    tiledb.tiledb_query_set_buffer(ctx, query, "a3", 
        PointerUtils.toVoid(a3), a3_size.cast());

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
