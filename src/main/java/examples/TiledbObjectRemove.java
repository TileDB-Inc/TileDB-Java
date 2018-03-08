package examples;

import io.tiledb.api.*;

public class TiledbObjectRemove {

  /*
   * It shows how to remove a TileDB resource.
   *
   * You need to run the following to make this work:
   *
   * ./tiledb_group_create_c ./tiledb_dense_create_c
   * ./tiledb_dense_write_global_1_c ./tiledb_object_remove_c
   */
  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Delete a valid group and array
    tiledb.tiledb_object_remove(ctx, "my_group");
    tiledb.tiledb_object_remove(ctx, "my_dense_array");

    // Delete an invalid path
    int rc = tiledb.tiledb_object_remove(ctx, "invalid_path");
    if (rc == tiledb.TILEDB_ERR)
      System.out.printf("Failed to delete invalid path\n");

    // Clean up
    tiledb.tiledb_ctx_free(ctxpp);
  }
}
