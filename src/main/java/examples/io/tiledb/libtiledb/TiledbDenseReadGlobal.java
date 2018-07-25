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

public class TiledbDenseReadGlobal {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = tiledb.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = tiledb.tiledb_ctx_tpp_value(ctxpp);

    // Open array
    SWIGTYPE_p_p_tiledb_array_t arraypp = tiledb.new_tiledb_array_tpp();
    tiledb.tiledb_array_alloc(ctx, "my_dense_array", arraypp);
    SWIGTYPE_p_tiledb_array_t arrayp = tiledb.tiledb_array_tpp_value(arraypp);
    tiledb.tiledb_array_open(ctx, arrayp, tiledb_query_type_t.TILEDB_READ);

    // Print non-empty getDomain
    uint64_tArray domain = new uint64_tArray(4);
    SWIGTYPE_p_int is_emptyp = tiledb.new_intp();
    tiledb.tiledb_array_get_non_empty_domain(ctx, arrayp,
        PointerUtils.toVoid(domain), is_emptyp);
    System.out.println("Non-empty getDomain:" + tiledb.intp_value(is_emptyp));
    System.out.println("d1: (" + domain.getitem(0) + ", "
        + domain.getitem(1) + ")");
    System.out.println("d2: (" + domain.getitem(2) + ", "
        + domain.getitem(3) + ")");

    // Print maximum buffer sizes for each attribute
    uint64_tArray a1_size = new uint64_tArray(1);
    uint64_tArray a2_size = new uint64_tArray(1);
    uint64_tArray a2_var_size = new uint64_tArray(1);
    uint64_tArray a3_size = new uint64_tArray(1);
 
    SWIGTYPE_p_p_char attributes = tiledb.new_charpArray(3);
    tiledb.charpArray_setitem(attributes, 0, "a1");
    tiledb.charpArray_setitem(attributes, 1, "a2");
    tiledb.charpArray_setitem(attributes, 2, "a3");
    
    long[] subarray_ = {1, 4, 1, 4};
    uint64_tArray subarray = Utils.newUint64Array(subarray_);
    
    tiledb.tiledb_array_max_buffer_size(ctx, arrayp, "a1",
        PointerUtils.toVoid(subarray), a1_size.cast());
    tiledb.tiledb_array_max_buffer_size_var(ctx, arrayp, "a2",
	PointerUtils.toVoid(subarray), a2_size.cast(), a2_var_size.cast());
    tiledb.tiledb_array_max_buffer_size(ctx, arrayp, "a3",
	PointerUtils.toVoid(subarray), a3_size.cast());

    System.out.println("Maximum buffer sizes:");
    System.out.println("a1: " + a1_size.getitem(0));
    System.out.println("a2: (" + a2_size.getitem(0) + ", "
        + a2_var_size.getitem(0) + ")");
    System.out.println("a3: " + a3_size.getitem(0));

    // Prepare cell buffers
    int a1_int_size = a1_size.getitem(0).intValue() / tiledb.sizeOfInt32();
    int a2_int_size = a2_size.getitem(0).intValue() / tiledb.sizeOfUint64();
    int a2_int_var_size = a2_var_size.getitem(0).intValue() / tiledb.sizeOfInt8();
    int a3_int_size = a3_size.getitem(0).intValue() / tiledb.sizeOfFloat();

    int32_tArray buffer_a1 = new int32_tArray(a1_int_size);
    uint64_tArray buffer_a2 = new uint64_tArray(a2_int_size);
    int8_tArray buffer_var_a2 = new int8_tArray(a2_int_var_size);
    floatArray buffer_a3 = new floatArray(a3_int_size);
   
     // Create query
    SWIGTYPE_p_p_tiledb_query_t querypp = tiledb.new_tiledb_query_tpp();
    tiledb.tiledb_query_alloc(ctx, arrayp,
        tiledb_query_type_t.TILEDB_READ, querypp);
    SWIGTYPE_p_tiledb_query_t query = tiledb.tiledb_query_tpp_value(querypp);
     tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_GLOBAL_ORDER);

    tiledb.tiledb_query_set_buffer(ctx, query, "a1",
	PointerUtils.toVoid(buffer_a1), a1_size.cast());
    tiledb.tiledb_query_set_buffer_var(ctx, query, "a2", 
	buffer_a2.cast(),
	a2_size.cast(),
	PointerUtils.toVoid(buffer_var_a2),
        a2_var_size.cast());
    tiledb.tiledb_query_set_buffer(ctx, query, "a3",
	PointerUtils.toVoid(buffer_a3), a3_size.cast());

      // Submit query
    tiledb.tiledb_query_submit(ctx, query);

    a1_int_size = a1_size.getitem(0).intValue() / tiledb.sizeOfInt32();
    a2_int_size = a2_size.getitem(0).intValue() / tiledb.sizeOfUint64();
    a2_int_var_size = a2_var_size.getitem(0).intValue() / tiledb.sizeOfInt8();
    a3_int_size = a3_size.getitem(0).intValue() / tiledb.sizeOfFloat();

    // Print cell values (assumes all getAttributes are read)
    int[] a1 = Utils.int32ArrayGet(buffer_a1, a1_int_size);
    long[] a2 = Utils.uint64ArrayGet(buffer_a2, a2_int_size);
    byte[] a2_var = Utils.int8ArrayGet(buffer_var_a2, a2_int_var_size);
    float[] a3 = Utils.floatArrayGet(buffer_a3, a3_int_size);

    int result_num = a1_int_size;
    System.out.println("Result num: " + result_num);
    System.out.println("a1, a2, a3[0], a3[1]");
    System.out.println("-----------------------------------------");
    for (int i = 0; i < result_num; ++i) {
      System.out.print(a1[i] + ", ");
      int var_size = (i != result_num - 1) ? (int) a2[i + 1] - (int) a2[i]
          : a2_int_var_size - (int) a2[i];
      System.out.print(Utils.substring(a2_var, (int) a2[i], var_size)
          + ", ");
      System.out.print(a3[2 * i] + " "
          + a3[2 * i + 1] + ", ");
      System.out.println();
    }

    // Finalize query
    tiledb.tiledb_query_finalize(ctx, query);

    // Close array
    tiledb.tiledb_array_close(ctx, arrayp);

    // Clean up
    tiledb.tiledb_array_free(arraypp);
    tiledb.tiledb_query_free(querypp);
    tiledb.tiledb_ctx_free(ctxpp);
    buffer_a1.delete();
    buffer_a2.delete();
    buffer_var_a2.delete();
    buffer_a3.delete();
  }
}