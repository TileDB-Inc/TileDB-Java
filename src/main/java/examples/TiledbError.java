package examples;

import io.tiledb.api.*;

public class TiledbError {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Create a group
    int rc = tiledb.tiledb_group_create(ctx, "my_group");
    if (rc == tiledb.TILEDB_OK)
      System.out.printf("Group created successfully!\n");
    else if (rc == tiledb.TILEDB_ERR)
      print_error(ctx);

    // Create the same group again - ERROR
    rc = tiledb.tiledb_group_create(ctx, "my_group");
    if (rc == tiledb.TILEDB_OK)
      System.out.printf("Group created successfully!\n");
    else if (rc == tiledb.TILEDB_ERR)
      print_error(ctx);

    // Clean up
    tiledb.tiledb_ctx_free(ctxpp);
  }

  public static void print_error(SWIGTYPE_p_tiledb_ctx_t ctx) {

    SWIGTYPE_p_p_tiledb_error_t errpp = Utils.new_tiledb_error_tpp();
    tiledb.tiledb_ctx_get_last_error(ctx, errpp);
    SWIGTYPE_p_tiledb_error_t err = Utils.tiledb_error_tpp_value(errpp);

    SWIGTYPE_p_p_char msg = tiledb.new_charpp();
    tiledb.tiledb_error_message(err, msg);
    System.out.printf("%s\n", tiledb.charpp_value(msg));
    tiledb.tiledb_error_free(errpp);
  }

}
