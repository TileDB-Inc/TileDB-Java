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

package io.tiledb.libtiledb.examples;

import io.tiledb.libtiledb.*;

public class TiledbKVCreate {

  /*
   * It shows how to create a key-value store.
   */
  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Create attributes
    SWIGTYPE_p_p_tiledb_attribute_t a1pp = Utils
        .new_tiledb_attribute_tpp();
    tiledb.tiledb_attribute_create(ctx, a1pp, "a1",
        tiledb_datatype_t.TILEDB_INT32);
    SWIGTYPE_p_tiledb_attribute_t a1 = Utils.tiledb_attribute_tpp_value(a1pp);
    tiledb.tiledb_attribute_set_compressor(ctx, a1,
        tiledb_compressor_t.TILEDB_BLOSC, -1);
    tiledb.tiledb_attribute_set_cell_val_num(ctx, a1, 1);

    SWIGTYPE_p_p_tiledb_attribute_t a2pp = Utils
        .new_tiledb_attribute_tpp();
    tiledb.tiledb_attribute_create(ctx, a2pp, "a2",
        tiledb_datatype_t.TILEDB_CHAR);
    SWIGTYPE_p_tiledb_attribute_t a2 = Utils.tiledb_attribute_tpp_value(a2pp);
    tiledb.tiledb_attribute_set_compressor(ctx, a2,
        tiledb_compressor_t.TILEDB_GZIP, -1);
    tiledb.tiledb_attribute_set_cell_val_num(ctx, a2,
        tiledb.tiledb_var_num());

    SWIGTYPE_p_p_tiledb_attribute_t a3pp = Utils
        .new_tiledb_attribute_tpp();
    tiledb.tiledb_attribute_create(ctx, a3pp, "a3",
        tiledb_datatype_t.TILEDB_FLOAT32);
    SWIGTYPE_p_tiledb_attribute_t a3 = Utils.tiledb_attribute_tpp_value(a3pp);
    tiledb.tiledb_attribute_set_compressor(ctx, a3,
        tiledb_compressor_t.TILEDB_ZSTD, -1);
    tiledb.tiledb_attribute_set_cell_val_num(ctx, a3, 2);

    // Create kv schema
    SWIGTYPE_p_p_tiledb_kv_schema_t kv_schemapp = Utils
        .new_tiledb_kv_schema_tpp();
    tiledb.tiledb_kv_schema_create(ctx, kv_schemapp);
    SWIGTYPE_p_tiledb_kv_schema_t kv_schema = Utils
        .tiledb_kv_schema_tpp_value(kv_schemapp);
    tiledb.tiledb_kv_schema_add_attribute(ctx, kv_schema, a1);
    tiledb.tiledb_kv_schema_add_attribute(ctx, kv_schema, a2);
    tiledb.tiledb_kv_schema_add_attribute(ctx, kv_schema, a3);

    if (tiledb.tiledb_kv_schema_check(ctx, kv_schema) != tiledb.TILEDB_OK) {
      System.out.println("Invalid key-value metadata");
      System.exit(1);
    }
    // Create kv
    tiledb.tiledb_kv_create(ctx, "my_kv", kv_schema);

    // Dump the key-value schema in ASCII format in standard output
    tiledb.tiledb_kv_schema_dump_stdout(ctx, kv_schema);

    // Clean up
    tiledb.tiledb_attribute_free(ctx, a1pp);
    tiledb.tiledb_attribute_free(ctx, a2pp);
    tiledb.tiledb_attribute_free(ctx, a3pp);
    tiledb.tiledb_kv_schema_free(ctx, kv_schemapp);
    tiledb.tiledb_ctx_free(ctxpp);
  }
}
