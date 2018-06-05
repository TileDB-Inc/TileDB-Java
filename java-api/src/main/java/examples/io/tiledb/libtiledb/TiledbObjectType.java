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
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Get object getType for group
    SWIGTYPE_p_tiledb_object_t type = tiledb.new_tiledb_object_tp();
    tiledb.tiledb_object_type(ctx, "my_group", type);
    print_object_type(tiledb.tiledb_object_tp_value(type));

    // Get object getType for array
    tiledb.tiledb_object_type(ctx, "my_dense_array", type);
    print_object_type(tiledb.tiledb_object_tp_value(type));

    // Get object getType for key-value
    tiledb.tiledb_object_type(ctx, "my_kv", type);
    print_object_type(tiledb.tiledb_object_tp_value(type));

    // Get invalid object getType
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
