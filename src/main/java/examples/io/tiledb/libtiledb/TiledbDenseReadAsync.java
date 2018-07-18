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

public class TiledbDenseReadAsync {

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
    long[] subarray_ = {1, 4, 1, 4};
    uint64_tArray subarray = Utils.newUint64Array(subarray_);
    
    uint64_tArray a1_size = new uint64_tArray(1);
    tiledb.tiledb_array_max_buffer_size(ctx, arrayp, "a1",
        PointerUtils.toVoid(subarray), a1_size.cast());
    
    uint64_tArray a2_size = new uint64_tArray(1);
    uint64_tArray var_a2_size = new uint64_tArray(1);
    tiledb.tiledb_array_max_buffer_size_var(ctx, arrayp, "a2",
        PointerUtils.toVoid(subarray), a2_size.cast(), var_a2_size.cast());

    uint64_tArray a3_size = new uint64_tArray(1);
    tiledb.tiledb_array_max_buffer_size(ctx, arrayp, "a3",
	PointerUtils.toVoid(subarray), a3_size.cast()); 

    System.out.println("Maximum buffer sizes:");
    System.out.println("a1: " + a1_size.getitem(0));
    System.out.println("a2: (" + a2_size.getitem(0) + ", "
        + var_a2_size.getitem(0) + ")");
    System.out.println("a3: " + a3_size.getitem(0));

    // Prepare cell buffers
    int a1_num = a1_size.getitem(0).intValue() / tiledb.sizeOfInt32();
    int a2_num = a2_size.getitem(0).intValue() / tiledb.sizeOfUint64();
    int var_a2_num = var_a2_size.getitem(0).intValue() / tiledb.sizeOfInt8();
    int a3_num = a3_size.getitem(0).intValue() / tiledb.sizeOfFloat();
    
    int32_tArray buffer_a1 = new int32_tArray(a1_num);
    uint64_tArray buffer_a2 = new uint64_tArray(a2_num);
    int8_tArray buffer_var_a2 = new int8_tArray(var_a2_num);
    floatArray buffer_a3 = new floatArray(a3_num);
    
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
	buffer_a2.cast(), a2_size.cast(),
        PointerUtils.toVoid(buffer_var_a2), var_a2_size.cast());
    tiledb.tiledb_query_set_buffer(ctx, query, "a3",
	PointerUtils.toVoid(buffer_a3), a3_size.cast()); 

    // Submit query
//    String s_ = "Callback: Query completed";
//    charArray s = Utils.newCharArray(s_);
    ReadCallback callback = new ReadCallback("Java Callback: Query completed");
    Utils.tiledb_query_submit_async(ctx, query, callback);

    // Wait for query to complete
    System.out.printf("Query in progress\n");
    SWIGTYPE_p_tiledb_query_status_t status = tiledb.new_tiledb_query_status_tp();
    do {
      tiledb.tiledb_query_get_status(ctx, query, status);
    } while (tiledb.tiledb_query_status_tp_value(status) != tiledb_query_status_t.TILEDB_COMPLETED);


    a1_num = a1_size.getitem(0).intValue() / tiledb.sizeOfInt32();
    a2_num = a2_size.getitem(0).intValue() / tiledb.sizeOfUint64();
    var_a2_num = var_a2_size.getitem(0).intValue() / tiledb.sizeOfInt8();
    a3_num = a3_size.getitem(0).intValue() / tiledb.sizeOfFloat();

    // Print cell values (assumes all getAttributes are read)
    int[] a1 = Utils.int32ArrayGet(buffer_a1, a1_num);
    long[] a2 = Utils.uint64ArrayGet(buffer_a2, a2_num);
    byte[] a2_var = Utils.int8ArrayGet(buffer_var_a2, var_a2_num);
    float[] a3 = Utils.floatArrayGet(buffer_a3, a3_num);

    int result_num = a1_num;
    System.out.println("Result num: " + result_num);
    System.out.println("a1, a2, a3[0], a3[1]");
    System.out.println("-----------------------------------------");
    for (int i = 0; i < result_num; ++i) {
      System.out.print(a1[i] + ", ");
      int var_size = (i != result_num - 1) ? (int) a2[i + 1] - (int) a2[i]
          : var_a2_num - (int) a2[i];
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

  private static class ReadCallback implements Callback {

    private final String data;

    public ReadCallback(String data) {
      this.data = data;
    }

    public void call() {
      System.out.println(data);
    }
  }
}
