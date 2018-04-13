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

import java.math.BigInteger;

import io.tiledb.libtiledb.*;

public class TiledbKVRead {

	/*
   * It shows how to read from a key-value store.
	 * 
	 * You need to run the following to make it work:
	 * 
	 * $ ./tiledb_kv_create $ ./tiledb_kv_write $ ./tiledb_kv_read
	 */

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Prepare key
//		int key_[] = { 100 };
//		intArray key = Utils.newIntArray(key_);

//		float key_[] = { (float)100.0 };
//		floatArray key = Utils.newFfoatArray(key_);

//		double key_[] = { 300.0, 300.1 };
//		doubleArray key = Utils.newDoubleArray(key_);

    charArray key = Utils.newCharArray("key_4");
    tiledb_datatype_t key_type = tiledb_datatype_t.TILEDB_CHAR;
    SWIGTYPE_p_tiledb_datatype_t key_typep = tiledb
        .new_tiledb_datatype_tp();
    tiledb.tiledb_datatype_tp_assign(key_typep, key_type);
    BigInteger key_size = new BigInteger("5");

    // Open the key-value store
    SWIGTYPE_p_p_tiledb_kv_t kvpp = Utils.new_tiledb_kv_tpp();
    tiledb.tiledb_kv_open(ctx, kvpp, "my_kv", null, 0);
    SWIGTYPE_p_tiledb_kv_t kv = Utils.tiledb_kv_tpp_value(kvpp);

    // Get key-value item
    SWIGTYPE_p_p_tiledb_kv_item_t kv_itempp = Utils
        .new_tiledb_kv_item_tpp();
    tiledb.tiledb_kv_get_item(ctx, kv, kv_itempp, PointerUtils.toVoid(key),
        key_type, key_size);
    SWIGTYPE_p_tiledb_kv_item_t kv_item = Utils.tiledb_kv_item_tpp_value(kv_itempp);
    //
    // // Check if item exists
    // if (kv_item == NULL) {
    // printf("Item does not exist.\n");
    // return 0;
    // }

    // Get values
    SWIGTYPE_p_p_void a1 = tiledb.new_voidpArray(1);
    SWIGTYPE_p_p_void a2 = tiledb.new_voidpArray(1);
    SWIGTYPE_p_p_void a3 = tiledb.new_voidpArray(1);
    SWIGTYPE_p_tiledb_datatype_t a1_type = tiledb.new_tiledb_datatype_tp(), a2_type = tiledb
        .new_tiledb_datatype_tp(), a3_type = tiledb
        .new_tiledb_datatype_tp();
    SWIGTYPE_p_unsigned_long_long a1_size = tiledb.new_ullp(), a2_size = tiledb
        .new_ullp(), a3_size = tiledb.new_ullp();
    tiledb.tiledb_kv_item_get_value(ctx, kv_item, "a1", a1, a1_type,
        a1_size);
    tiledb.tiledb_kv_item_get_value(ctx, kv_item, "a2", a2, a2_type,
        a2_size);
    tiledb.tiledb_kv_item_get_value(ctx, kv_item, "a3", a3, a3_type,
        a3_size);

    // Verify correct types
    assert (tiledb.tiledb_datatype_tp_value(a1_type) == tiledb_datatype_t.TILEDB_INT32
        && tiledb.tiledb_datatype_tp_value(a2_type) == tiledb_datatype_t.TILEDB_CHAR && tiledb
        .tiledb_datatype_tp_value(a3_type) == tiledb_datatype_t.TILEDB_FLOAT32);

    // Print result
    int a2_size_ = tiledb.ullp_value(a2_size).intValue();
    System.out.printf("a1, a2, (a3.first, a3.second)\n");
    System.out.printf("-----------------------------\n");
    System.out.printf("%d", PointerUtils.intFromVoid(a1).getitem(0));
    System.out.printf(", %s", Utils.substring(PointerUtils.charArrayFromVoid(a2), 0, a2_size_));
    System.out.printf(", (%f, %f)\n", PointerUtils.floatArrayFromVoid(a3).getitem(0),
        PointerUtils.floatArrayFromVoid(a3).getitem(1));

    // Clean up
    tiledb.tiledb_kv_close(ctx, kvpp);
    tiledb.tiledb_kv_item_free(ctx, kv_itempp);
    tiledb.tiledb_ctx_free(ctxpp);

  }
}
