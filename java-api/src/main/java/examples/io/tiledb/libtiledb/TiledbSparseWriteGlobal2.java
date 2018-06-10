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

public class TiledbSparseWriteGlobal2 {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Open array
    SWIGTYPE_p_p_tiledb_array_t arraypp = Utils.new_tiledb_array_tpp();
    tiledb.tiledb_array_alloc(ctx, "my_sparse_array", arraypp);
    SWIGTYPE_p_tiledb_array_t arrayp = Utils.tiledb_array_tpp_value(arraypp);
    tiledb.tiledb_array_open(ctx, arrayp, tiledb_query_type_t.TILEDB_WRITE);

    // Prepare cell buffers - #1
    int[] buffer_a1 = {0, 1, 2};
    intArray a1 = Utils.newIntArray(buffer_a1);
    long[] buffer_a2 = {0, 1, 3, 6, 10, 11, 13, 16};
    uint64_tArray a2 = Utils.newUint64Array(buffer_a2);
    String buffer_var_a2 = "abbcccddddeffggghhhh";
    charArray var_a2 = Utils.newCharArray(buffer_var_a2);

    float buffer_a3[] = {0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
        4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f};
    floatArray a3 = Utils.newFloatArray(buffer_a3);

    long buffer_coords_[] = {1, 1, 1, 2};
    uint64_tArray buffer_coords = Utils.newUint64Array(buffer_coords_);
     
    // Create query
    SWIGTYPE_p_p_tiledb_query_t querypp = Utils.new_tiledb_query_tpp();
    tiledb.tiledb_query_alloc(ctx, arrayp,
        tiledb_query_type_t.TILEDB_WRITE, querypp);
    SWIGTYPE_p_tiledb_query_t query = Utils.tiledb_query_tpp_value(querypp);
    tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_GLOBAL_ORDER);
    
    long[] buffer_a1_size = {buffer_a1.length * 4};
    uint64_tArray a1_size = Utils.newUint64Array(buffer_a1_size);
    tiledb.tiledb_query_set_buffer(ctx, query, "a1", 
        PointerUtils.toVoid(a1), a1_size.cast());
    
    long[] buffer_a2_size = {buffer_a2.length * 8};
    uint64_tArray a2_size = Utils.newUint64Array(buffer_a2_size);
    long[] buffer_var_a2_size = {buffer_var_a2.length()};
    uint64_tArray var_a2_size = Utils.newUint64Array(buffer_var_a2_size);
    tiledb.tiledb_query_set_buffer_var(ctx, query, "a2", 
        a2.cast(), a2_size.cast(),
	PointerUtils.toVoid(var_a2), var_a2_size.cast());

    long[] buffer_a3_size = {buffer_a3.length * 4};
    uint64_tArray a3_size = Utils.newUint64Array(buffer_a3_size);
    tiledb.tiledb_query_set_buffer(ctx, query, "a3",
        PointerUtils.toVoid(a3), a3_size.cast());
	
    long[] buffer_coords_size = {buffer_coords_.length * 8};
    uint64_tArray coords_size = Utils.newUint64Array(buffer_coords_size);
    tiledb.tiledb_query_set_buffer(ctx, query, tiledb.tiledb_coords(),
	PointerUtils.toVoid(buffer_coords), coords_size.cast());

    // Submit query - #1
    tiledb.tiledb_query_submit(ctx, query);

    // Prepare cell buffers - #2
    int[] buffer_a1_2 = {3, 4, 5, 6, 7};
    intArray a1_2 = Utils.newIntArray(buffer_a1_2);
    long[] buffer_a2_2 = {};
    uint64_tArray a2_2 = Utils.newUint64Array(buffer_a2_2);
    String buffer_var_a2_2 = "";
    charArray var_a2_2 = Utils.newCharArray(buffer_var_a2_2);

    float buffer_a3_2[] = {};
    floatArray a3_2 = Utils.newFloatArray(buffer_a3_2);

    long buffer_coords_2_[] = {1, 4, 2, 3, 3, 1, 4, 2, 3, 3, 3, 4};
    uint64_tArray buffer_coords_2 = Utils.newUint64Array(buffer_coords_2_);

    long[] buffer_a1_2_size = {buffer_a1_2.length * 4};
    uint64_tArray a1_2_size = Utils.newUint64Array(buffer_a1_2_size);
    tiledb.tiledb_query_set_buffer(ctx, query, "a1", 
        PointerUtils.toVoid(a1_2), a1_2_size.cast());
    
    long[] buffer_a2_2_size = {buffer_a2_2.length * 8};
    uint64_tArray a2_2_size = Utils.newUint64Array(buffer_a2_2_size);
    long[] buffer_var_a2_2_size = {buffer_var_a2_2.length()};
    uint64_tArray var_a2_2_size = Utils.newUint64Array(buffer_var_a2_2_size);
    tiledb.tiledb_query_set_buffer_var(ctx, query, "a2", 
        a2_2.cast(), a2_2_size.cast(),
	PointerUtils.toVoid(var_a2_2), var_a2_2_size.cast());

    long[] buffer_a3_2_size = {buffer_a3_2.length * 4};
    uint64_tArray a3_2_size = Utils.newUint64Array(buffer_a3_2_size);
    tiledb.tiledb_query_set_buffer(ctx, query, "a3",
        PointerUtils.toVoid(a3_2), a3_2_size.cast());
	
    long[] buffer_coords_2_size = {buffer_coords_2_.length * 8};
    uint64_tArray coords_2_size = Utils.newUint64Array(buffer_coords_2_size);
    tiledb.tiledb_query_set_buffer(ctx, query, tiledb.tiledb_coords(),
	PointerUtils.toVoid(buffer_coords_2), coords_2_size.cast());

    // Submit query - #2
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
