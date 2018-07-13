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

public class TiledbError {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = tiledb.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = tiledb.tiledb_ctx_tpp_value(ctxpp);

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

    SWIGTYPE_p_p_tiledb_error_t errpp = tiledb.new_tiledb_error_tpp();
    tiledb.tiledb_ctx_get_last_error(ctx, errpp);
    SWIGTYPE_p_tiledb_error_t err = tiledb.tiledb_error_tpp_value(errpp);

    SWIGTYPE_p_p_char msg = tiledb.new_charpp();
    tiledb.tiledb_error_message(err, msg);
    System.out.printf("%s\n", tiledb.charpp_value(msg));
    tiledb.tiledb_error_free(errpp);
  }

}
