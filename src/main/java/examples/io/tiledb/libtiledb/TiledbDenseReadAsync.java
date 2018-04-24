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
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Print non-empty getDomain
    uint64_tArray domain = new uint64_tArray(4);
    SWIGTYPE_p_int is_emptyp = tiledb.new_intp();
    tiledb.tiledb_array_get_non_empty_domain(ctx, "my_dense_array",
        PointerUtils.toVoid(domain), is_emptyp);
    System.out.println("Non-empty getDomain:" + tiledb.intp_value(is_emptyp));
    System.out.println("d1: (" + domain.getitem(0) + ", "
        + domain.getitem(1) + ")");
    System.out.println("d2: (" + domain.getitem(2) + ", "
        + domain.getitem(3) + ")");

    // // Print maximum buffer sizes for each attribute
    SWIGTYPE_p_p_char attributes = tiledb.new_charpArray(3);
    tiledb.charpArray_setitem(attributes, 0, "a1");
    tiledb.charpArray_setitem(attributes, 1, "a2");
    tiledb.charpArray_setitem(attributes, 2, "a3");
    uint64_tArray buffer_sizes = new uint64_tArray(4);
    long[] subarray_ = {1, 4, 1, 4};
    uint64_tArray subarray = Utils.newUint64Array(subarray_);
    tiledb.tiledb_array_compute_max_read_buffer_sizes(ctx,
        "my_dense_array", PointerUtils.toVoid(subarray), attributes, 3,
        buffer_sizes.cast());
    System.out.println("Maximum buffer sizes:");
    System.out.println("a1: " + buffer_sizes.getitem(0));
    System.out.println("a2: (" + buffer_sizes.getitem(1) + ", "
        + buffer_sizes.getitem(2) + ")");
    System.out.println("a3: " + buffer_sizes.getitem(3));

    // Prepare cell buffers
    int a1_size = buffer_sizes.getitem(0).intValue() / UtilsJNI.sizeOfInt32();
    int a2_size = buffer_sizes.getitem(1).intValue() / UtilsJNI.sizeOfUint64();
    int a2_var_size = buffer_sizes.getitem(2).intValue() / UtilsJNI.sizeOfInt8();
    int a3_size = buffer_sizes.getitem(3).intValue() / UtilsJNI.sizeOfFloat();

    int32_tArray buffer_a1 = new int32_tArray(a1_size);
    uint64_tArray buffer_a2 = new uint64_tArray(a2_size);
    int8_tArray buffer_var_a2 = new int8_tArray(a2_var_size);
    floatArray buffer_a3 = new floatArray(a3_size);


    SWIGTYPE_p_p_void buffers = tiledb.new_voidpArray(4);
    tiledb.voidpArray_setitem(buffers, 0, PointerUtils.toVoid(buffer_a1));
    tiledb.voidpArray_setitem(buffers, 1, PointerUtils.toVoid(buffer_a2));
    tiledb.voidpArray_setitem(buffers, 2,
        PointerUtils.toVoid(buffer_var_a2));
    tiledb.voidpArray_setitem(buffers, 3, PointerUtils.toVoid(buffer_a3));

    // Create query
    SWIGTYPE_p_p_tiledb_query_t querypp = Utils.new_tiledb_query_tpp();
    tiledb.tiledb_query_create(ctx, querypp, "my_dense_array",
        tiledb_query_type_t.TILEDB_READ);
    SWIGTYPE_p_tiledb_query_t query = Utils.tiledb_query_tpp_value(querypp);
    tiledb.tiledb_query_set_buffers(ctx, query, attributes, 3, buffers,
        buffer_sizes.cast());
    tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_GLOBAL_ORDER);

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


    a1_size = buffer_sizes.getitem(0).intValue() / UtilsJNI.sizeOfInt32();
    a2_size = buffer_sizes.getitem(1).intValue() / UtilsJNI.sizeOfUint64();
    a2_var_size = buffer_sizes.getitem(2).intValue() / UtilsJNI.sizeOfInt8();
    a3_size = buffer_sizes.getitem(3).intValue() / UtilsJNI.sizeOfFloat();

    // Print cell values (assumes all getAttributes are read)
    int[] a1 = Utils.int32ArrayGet(buffer_a1, a1_size);
    long[] a2 = Utils.uint64ArrayGet(buffer_a2, a2_size);
    byte[] a2_var = Utils.int8ArrayGet(buffer_var_a2, a2_var_size);
    float[] a3 = Utils.floatArrayGet(buffer_a3, a3_size);

    int result_num = a1_size;
    System.out.println("Result num: " + result_num);
    System.out.println("a1, a2, a3[0], a3[1]");
    System.out.println("-----------------------------------------");
    for (int i = 0; i < result_num; ++i) {
      System.out.print(a1[i] + ", ");
      int var_size = (i != result_num - 1) ? (int) a2[i + 1] - (int) a2[i]
          : a2_var_size - (int) a2[i];
      System.out.print(Utils.substring(a2_var, (int) a2[i], var_size)
          + ", ");
      System.out.print(a3[2 * i] + " "
          + a3[2 * i + 1] + ", ");
      System.out.println();
    }

    // Clean up
    tiledb.tiledb_query_free(ctx, querypp);
    tiledb.tiledb_ctx_free(ctxpp);

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
