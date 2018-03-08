package examples;

import io.tiledb.api.*;

public class TiledbSparseWriteUnordered1Again {

  /*
   * It shows how to write unordered cells to a sparse array in a single
   * write. This time we write 4 cells.
   *
   * You need to run the following to make this work:
   *
   * ./tiledb_sparse_create_c ./tiledb_sparse_write_unordered_1_again_c
   */
  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Prepare cell buffers - #1
    int[] buffer_a1 = {107, 104, 106, 105};
    intArray a1 = Utils.newIntArray(buffer_a1);
    long[] buffer_a2 = {0, 3, 4, 5};
    uint64_tArray a2 = Utils.newUint64Array(buffer_a2);
    String buffer_var_a2 = "yyyuwvvvv";
    charArray var_a2 = Utils.newCharArray(buffer_var_a2);

    float buffer_a3[] = {107.1f, 107.2f, 104.1f, 104.2f, 106.1f, 106.2f,
        105.1f, 105.2f};
    floatArray a3 = Utils.newFloatArray(buffer_a3);

    long buffer_coords_[] = {3, 4, 3, 2, 3, 3, 4, 1};
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

    // Submit query
    tiledb.tiledb_query_submit(ctx, query);

    // Clean up
    tiledb.tiledb_query_free(ctx, querypp);
    tiledb.tiledb_ctx_free(ctxpp);

  }
}
