package examples;

import io.tiledb.api.*;

public class TiledbGroupCreate {

  public static void main(String[] args) {

    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Create a group
    tiledb.tiledb_group_create(ctx, "my_group");

    // Create two groups inside the first group
    tiledb.tiledb_group_create(ctx, "my_group/dense_arrays");
    tiledb.tiledb_group_create(ctx, "my_group/sparse_arrays");

    // Clean up
    tiledb.tiledb_ctx_free(ctxpp);
  }

}
