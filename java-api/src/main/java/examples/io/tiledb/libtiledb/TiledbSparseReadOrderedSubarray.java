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

public class TiledbSparseReadOrderedSubarray {

  /*
   * It shows how to read from a sparse array, constraining the read to a
   * specific subarray. This time the cells are returned in row-major order
   * within the specified subarray.
   *
   * You need to run the following to make it work:
   *
   * $ ./tiledb_sparse_create_c
   * $ ./tiledb_sparse_write_global_1_c
   * $ ./tiledb_sparse_read_ordered_subarray_c
   */
  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = tiledb.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = tiledb.tiledb_ctx_tpp_value(ctxpp);

    // Open array
    SWIGTYPE_p_p_tiledb_array_t arraypp = tiledb.new_tiledb_array_tpp();
    tiledb.tiledb_array_alloc(ctx, "my_sparse_array", arraypp);
    SWIGTYPE_p_tiledb_array_t arrayp = tiledb.tiledb_array_tpp_value(arraypp);
    tiledb.tiledb_array_open(ctx, arrayp, tiledb_query_type_t.TILEDB_READ);

    // Compute maximum buffer sizes for each attribute
    uint64_tArray a1_size = new uint64_tArray(1);
    uint64_tArray a2_size = new uint64_tArray(1);
    uint64_tArray var_a2_size = new uint64_tArray(1);
    uint64_tArray a3_size = new uint64_tArray(1);
    uint64_tArray coords_size = new uint64_tArray(1);
    
    long[] subarray_ = {3, 4, 2, 4};
    uint64_tArray subarray = Utils.newUint64Array(subarray_);
    
    tiledb.tiledb_array_max_buffer_size(ctx, arrayp, "a1", 
	PointerUtils.toVoid(subarray), a1_size.cast());
    tiledb.tiledb_array_max_buffer_size_var(ctx, arrayp, "a2", 
        PointerUtils.toVoid(subarray), a2_size.cast(), var_a2_size.cast());
    tiledb.tiledb_array_max_buffer_size(ctx, arrayp, "a3", 
	PointerUtils.toVoid(subarray), a3_size.cast());
    tiledb.tiledb_array_max_buffer_size(ctx, arrayp, tiledb.tiledb_coords(), 
	PointerUtils.toVoid(subarray), coords_size.cast());  
   
    // Prepare cell buffers
    int32_tArray buffer_a1 = new int32_tArray(
        a1_size.getitem(0).intValue() / 4);
    uint64_tArray buffer_a2 = new uint64_tArray(a2_size.getitem(0)
        .intValue() / 8);
    charArray buffer_var_a2 = new charArray(var_a2_size.getitem(0)
        .intValue());
    floatArray buffer_a3 = new floatArray(a3_size.getitem(0)
        .intValue() / 4);
    uint64_tArray buffer_coords = new uint64_tArray(coords_size.getitem(0)
        .intValue() / 8);

    // Create query
    SWIGTYPE_p_p_tiledb_query_t querypp = tiledb.new_tiledb_query_tpp();
    tiledb.tiledb_query_alloc(ctx, arrayp,
        tiledb_query_type_t.TILEDB_READ, querypp);
    SWIGTYPE_p_tiledb_query_t query = tiledb.tiledb_query_tpp_value(querypp);
    tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_ROW_MAJOR);
    tiledb.tiledb_query_set_subarray(ctx, query,
        PointerUtils.toVoid(subarray));

    tiledb.tiledb_query_set_buffer(ctx, query, "a1",
	PointerUtils.toVoid(buffer_a1), a1_size.cast());
    tiledb.tiledb_query_set_buffer_var(ctx, query, "a2",
	buffer_a2.cast(), a2_size.cast(), 
	PointerUtils.toVoid(buffer_var_a2), var_a2_size.cast());
    tiledb.tiledb_query_set_buffer(ctx, query, "a3", 
	PointerUtils.toVoid(buffer_a3), a3_size.cast());
    tiledb.tiledb_query_set_buffer(ctx, query, tiledb.tiledb_coords(), 
	PointerUtils.toVoid(buffer_coords), coords_size.cast());	    

    // Submit query
    tiledb.tiledb_query_submit(ctx, query);

    // Print cell values (assumes all getAttributes are read)
    int result_num = a1_size.getitem(0).intValue() / 4;
    System.out.println("Result num: " + result_num);
    System.out.printf("%8s%9s%9s%11s%10s\n", tiledb.tiledb_coords(), "a1", "a2", "a3[0]", "a3[1]");
    System.out.printf("------------------------------------\n");
    for (int i = 0; i < result_num; ++i) {
      System.out.printf(
          "(%s, %s)",
          buffer_coords.getitem(2 * i),
          buffer_coords.getitem(2 * i + 1));
      System.out.printf("%10d ", buffer_a1.getitem(i));
      int var_size = (i != result_num - 1) ? buffer_a2.getitem(i + 1)
          .intValue() - buffer_a2.getitem(i).intValue()
          : var_a2_size.getitem(0).intValue()
          - buffer_a2.getitem(i).intValue();
      System.out.printf("%10s", Utils.substring(buffer_var_a2, buffer_a2
          .getitem(i).intValue(), var_size));
      System.out.printf("%10.1f%10.1f\n", buffer_a3.getitem(2 * i),
          buffer_a3.getitem(2 * i + 1));
    }

    // Finalize query
    tiledb.tiledb_query_finalize(ctx, query);

    // Close array
    tiledb.tiledb_array_close(ctx, arrayp);

    // Clean up
    tiledb.tiledb_query_free(querypp);
    tiledb.tiledb_array_free(arraypp);
    tiledb.tiledb_ctx_free(ctxpp);
    buffer_a1.delete();
    buffer_a2.delete();
    buffer_var_a2.delete();
    buffer_a3.delete();
  }
}
