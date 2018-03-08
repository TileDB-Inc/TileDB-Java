package examples;

import io.tiledb.api.*;

public class TiledbObjectLsWalk {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // List children
    System.out.printf("List children:\n");
    Utils.tiledb_object_ls(ctx, "my_group", new ReadCallback("java printing:"));

    // Walk in a path with a pre- and post-order traversal
    System.out.printf("\nPreorder traversal:\n");
    Utils.tiledb_object_walk(ctx, "my_group", tiledb_walk_order_t.TILEDB_PREORDER, new ReadCallback("java printing:"));
    System.out.printf("\nPostorder traversal:\n");
    Utils.tiledb_object_walk(ctx, "my_group", tiledb_walk_order_t.TILEDB_POSTORDER, new ReadCallback("java printing:"));

    // Clean up
    tiledb.tiledb_ctx_free(ctxpp);
  }

  private static class ReadCallback extends PathCallback {

    private final String data;

    public ReadCallback(String data){
      this.data = data;
    }

    @Override
    public int call(String path, tiledb_object_t type) {
      System.out.printf("%s %s ", data, path);
      switch (type) {
        case TILEDB_ARRAY:
          System.out.printf("ARRAY");
          break;
        case TILEDB_KEY_VALUE:
          System.out.printf("KEY_VALUE");
          break;
        case TILEDB_GROUP:
          System.out.printf("GROUP");
          break;
        default:
          System.out.printf("INVALID");
      }
      System.out.printf("\n");

      // Always iterate till the end
      return 1;
    }
  }
}
