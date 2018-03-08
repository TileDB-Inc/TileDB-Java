package examples;

import io.tiledb.api.*;

public class TiledbDenseWriteGlobal2 {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Prepare cell buffers - #1
    int[] buffer_a1 = {0, 1, 2, 3, 4, 5};
    intArray a1 = Utils.newIntArray(buffer_a1);
    long[] buffer_a2 = {0, 1, 3, 6, 10, 11, 13, 16};
    uint64_tArray a2 = Utils.newUint64Array(buffer_a2);
    String buffer_var_a2 = "abbcccddddeffggghhhh";
    charArray var_a2 = Utils.newCharArray(buffer_var_a2);

    float buffer_a3[] = {};
    floatArray a3 = Utils.newFloatArray(buffer_a3);

    SWIGTYPE_p_p_void buffers = tiledb.new_voidpArray(4);
    tiledb.voidpArray_setitem(buffers, 0, PointerUtils.toVoid(a1));
    tiledb.voidpArray_setitem(buffers, 1, PointerUtils.toVoid(a2));
    tiledb.voidpArray_setitem(buffers, 2, PointerUtils.toVoid(var_a2));
    tiledb.voidpArray_setitem(buffers, 3, PointerUtils.toVoid(a3));
    long buffer_sizes_[] = {buffer_a1.length * 4, buffer_a2.length * 8,
        buffer_var_a2.length(), 0};
    uint64_tArray buffer_sizes = Utils.newUint64Array(buffer_sizes_);

    // Create query
    SWIGTYPE_p_p_tiledb_query_t querypp = Utils.new_tiledb_query_tpp();
    SWIGTYPE_p_p_char attributes = tiledb.new_charpArray(3);
    tiledb.charpArray_setitem(attributes, 0, "a1");
    tiledb.charpArray_setitem(attributes, 1, "a2");
    tiledb.charpArray_setitem(attributes, 2, "a3");
    tiledb.tiledb_query_create(ctx, querypp, "my_dense_array",
        tiledb_query_type_t.TILEDB_WRITE);
    SWIGTYPE_p_tiledb_query_t query = Utils.tiledb_query_tpp_value(querypp);
    tiledb.tiledb_query_set_layout(ctx, query,
        tiledb_layout_t.TILEDB_GLOBAL_ORDER);
    tiledb.tiledb_query_set_buffers(ctx, query, attributes, 3, buffers,
        buffer_sizes.cast());

    // Submit query - #1
    tiledb.tiledb_query_submit(ctx, query);

    // Prepare cell buffers - #2
    int[] buffer_a1_2 = {6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    intArray a1_2 = Utils.newIntArray(buffer_a1_2);
    long[] buffer_a2_2 = {0, 1, 3, 6, 10, 11, 13, 16};
    uint64_tArray a2_2 = Utils.newUint64Array(buffer_a2_2);
    String buffer_var_a2_2 = "ijjkkkllllmnnooopppp";
    charArray var_a2_2 = Utils.newCharArray(buffer_var_a2_2);

    float buffer_a3_2[] = {0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
        4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f, 8.1f, 8.2f,
        9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f, 12.1f, 12.2f, 13.1f,
        13.2f, 14.1f, 14.2f, 15.1f, 15.2f};
    floatArray a3_2 = Utils.newFloatArray(buffer_a3_2);

    SWIGTYPE_p_p_void buffers_2 = tiledb.new_voidpArray(4);
    tiledb.voidpArray_setitem(buffers_2, 0, PointerUtils.toVoid(a1_2));
    tiledb.voidpArray_setitem(buffers_2, 1, PointerUtils.toVoid(a2_2));
    tiledb.voidpArray_setitem(buffers_2, 2, PointerUtils.toVoid(var_a2_2));
    tiledb.voidpArray_setitem(buffers_2, 3, PointerUtils.toVoid(a3_2));
    long buffer_sizes_2_[] = {buffer_a1_2.length * 4,
        buffer_a2_2.length * 8, buffer_var_a2_2.length(),
        buffer_a3_2.length * 4};
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
