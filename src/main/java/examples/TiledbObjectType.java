package examples;

import io.tiledb.api.*;

public class TiledbObjectType {

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

    // Get object type for group
    SWIGTYPE_p_tiledb_object_t type = tiledb.new_tiledb_object_tp();
    tiledb.tiledb_object_type(ctx, "my_group", type);
    print_object_type(tiledb.tiledb_object_tp_value(type));

    // Get object type for array
    tiledb.tiledb_object_type(ctx, "my_dense_array", type);
    print_object_type(tiledb.tiledb_object_tp_value(type));

    // Get object type for key-value
    tiledb.tiledb_object_type(ctx, "my_kv", type);
    print_object_type(tiledb.tiledb_object_tp_value(type));

    // Get invalid object type
    tiledb.tiledb_object_type(ctx, "some_invalid_path", type);
    print_object_type(tiledb.tiledb_object_tp_value(type));

    // Clean up
    tiledb.tiledb_ctx_free(ctxpp);
  }

  public static void print_object_type(tiledb_object_t type) {
    switch (type) {
      case TILEDB_ARRAY:
        System.out.printf("ARRAY\n");
        break;
      case TILEDB_GROUP:
        System.out.printf("GROUP\n");
        break;
      case TILEDB_KEY_VALUE:
        System.out.printf("KEY_VALUE\n");
        break;
      case TILEDB_INVALID:
        System.out.printf("INVALID\n");
        break;
    }
  }

}
