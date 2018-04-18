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

public class TiledbKVWrite {

  public static void main(String[] args) throws Exception {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = Utils.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_create(ctxpp, null);
    SWIGTYPE_p_tiledb_ctx_t ctx = Utils.tiledb_ctx_tpp_value(ctxpp);

    // Key-values with three getAttributes
    int key1_[] = {100};
    intArray key1 = Utils.newIntArray(key1_);
    int key1_a1_[] = {1};
    intArray key1_a1 = Utils.newIntArray(key1_a1_);
    charArray key1_a2 = Utils.newCharArray("a");
    float key1_a3_[] = {1.1f, 1.2f};
    floatArray key1_a3 = Utils.newFloatArray(key1_a3_);

    float key2_[] = {(float) 200.0};
    floatArray key2 = Utils.newFloatArray(key2_);
    int key2_a1_[] = {2};
    intArray key2_a1 = Utils.newIntArray(key2_a1_);
    charArray key2_a2 = Utils.newCharArray("bb");
    float key2_a3_[] = {1.1f, 1.2f};
    floatArray key2_a3 = Utils.newFloatArray(key2_a3_);

    double key3_[] = {300.0, 300.1};
    doubleArray key3 = Utils.newDoubleArray(key3_);
    int key3_a1_[] = {3};
    intArray key3_a1 = Utils.newIntArray(key3_a1_);
    charArray key3_a2 = Utils.newCharArray("ccc");
    float key3_a3_[] = {3.1f, 3.2f};
    floatArray key3_a3 = Utils.newFloatArray(key3_a3_);

    charArray key4 = Utils.newCharArray("key_4");
    int key4_a1_[] = {4};
    intArray key4_a1 = Utils.newIntArray(key4_a1_);
    charArray key4_a2 = Utils.newCharArray("dddd");
    float key4_a3_[] = {4.1f, 4.2f};
    floatArray key4_a3 = Utils.newFloatArray(key4_a3_);

    // Open the key-value store
    SWIGTYPE_p_p_tiledb_kv_t kvpp = Utils.new_tiledb_kv_tpp();
    tiledb.tiledb_kv_open(ctx, kvpp, "my_kv", null, 0);
    SWIGTYPE_p_tiledb_kv_t kv = Utils.tiledb_kv_tpp_value(kvpp);
    // Flush every 100 added items
    tiledb.tiledb_kv_set_max_buffered_items(ctx, kv, new BigInteger("100"));

    // Create first key-value item object
    SWIGTYPE_p_p_tiledb_kv_item_t kv_item1pp = Utils
        .new_tiledb_kv_item_tpp();
    tiledb.tiledb_kv_item_create(ctx, kv_item1pp);
    SWIGTYPE_p_tiledb_kv_item_t kv_item1 = Utils.tiledb_kv_item_tpp_value(kv_item1pp);

    tiledb.tiledb_kv_item_set_key(ctx, kv_item1, PointerUtils.toVoid(key1),
        tiledb_datatype_t.TILEDB_INT32, new BigInteger("4"));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item1, "a1",
        PointerUtils.toVoid(key1_a1), tiledb_datatype_t.TILEDB_INT32,
        new BigInteger("" + key1_a1_.length * 4));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item1, "a2",
        PointerUtils.toVoid(key1_a2), tiledb_datatype_t.TILEDB_CHAR,
        new BigInteger("1"));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item1, "a3",
        PointerUtils.toVoid(key1_a3), tiledb_datatype_t.TILEDB_FLOAT32,
        new BigInteger("" + key1_a3_.length * 4));

    // Create second key-value item object
    SWIGTYPE_p_p_tiledb_kv_item_t kv_item2pp = Utils
        .new_tiledb_kv_item_tpp();
    tiledb.tiledb_kv_item_create(ctx, kv_item2pp);
    SWIGTYPE_p_tiledb_kv_item_t kv_item2 = Utils.tiledb_kv_item_tpp_value(kv_item2pp);

    tiledb.tiledb_kv_item_set_key(ctx, kv_item2, PointerUtils.toVoid(key2),
        tiledb_datatype_t.TILEDB_FLOAT32, new BigInteger("4"));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item2, "a1",
        PointerUtils.toVoid(key2_a1), tiledb_datatype_t.TILEDB_INT32,
        new BigInteger("" + key2_a1_.length * 4));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item2, "a2",
        PointerUtils.toVoid(key2_a2), tiledb_datatype_t.TILEDB_CHAR,
        new BigInteger("2"));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item2, "a3",
        PointerUtils.toVoid(key2_a3), tiledb_datatype_t.TILEDB_FLOAT32,
        new BigInteger("" + key2_a3_.length * 4));

    // Create third key-value item object
    SWIGTYPE_p_p_tiledb_kv_item_t kv_item3pp = Utils
        .new_tiledb_kv_item_tpp();
    tiledb.tiledb_kv_item_create(ctx, kv_item3pp);
    SWIGTYPE_p_tiledb_kv_item_t kv_item3 = Utils.tiledb_kv_item_tpp_value(kv_item3pp);

    tiledb.tiledb_kv_item_set_key(ctx, kv_item3, PointerUtils.toVoid(key3),
        tiledb_datatype_t.TILEDB_FLOAT64, new BigInteger("16"));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item3, "a1",
        PointerUtils.toVoid(key3_a1), tiledb_datatype_t.TILEDB_INT32,
        new BigInteger("" + key3_a1_.length * 4));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item3, "a2",
        PointerUtils.toVoid(key3_a2), tiledb_datatype_t.TILEDB_CHAR,
        new BigInteger("3"));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item3, "a3",
        PointerUtils.toVoid(key3_a3), tiledb_datatype_t.TILEDB_FLOAT32,
        new BigInteger("" + key3_a3_.length * 4));

    // Create fourth key-value item object
    SWIGTYPE_p_p_tiledb_kv_item_t kv_item4pp = Utils
        .new_tiledb_kv_item_tpp();
    tiledb.tiledb_kv_item_create(ctx, kv_item4pp);
    SWIGTYPE_p_tiledb_kv_item_t kv_item4 = Utils.tiledb_kv_item_tpp_value(kv_item4pp);

    tiledb.tiledb_kv_item_set_key(ctx, kv_item4, PointerUtils.toVoid(key4),
        tiledb_datatype_t.TILEDB_CHAR, new BigInteger("5"));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item4, "a1",
        PointerUtils.toVoid(key4_a1), tiledb_datatype_t.TILEDB_INT32,
        new BigInteger("" + key4_a1_.length * 4));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item4, "a2",
        PointerUtils.toVoid(key4_a2), tiledb_datatype_t.TILEDB_CHAR,
        new BigInteger("4"));
    tiledb.tiledb_kv_item_set_value(ctx, kv_item4, "a3",
        PointerUtils.toVoid(key4_a3), tiledb_datatype_t.TILEDB_FLOAT32,
        new BigInteger("" + key4_a3_.length * 4));

    // Add key-value items to key-value store
    tiledb.tiledb_kv_add_item(ctx, kv, kv_item1);
    tiledb.tiledb_kv_add_item(ctx, kv, kv_item2);

    // Force-write the buffered items to the persistent storage
    tiledb.tiledb_kv_flush(ctx, kv);

    tiledb.tiledb_kv_add_item(ctx, kv, kv_item3);
    tiledb.tiledb_kv_add_item(ctx, kv, kv_item4);

    // The following will flush the buffered key-value items to the disk
    tiledb.tiledb_kv_close(ctx, kvpp);

    // Consolidate key-value store (optional)
    tiledb.tiledb_kv_consolidate(ctx, "my_kv");

    // Clean up
    tiledb.tiledb_kv_item_free(ctx, kv_item1pp);
    tiledb.tiledb_kv_item_free(ctx, kv_item2pp);
    tiledb.tiledb_kv_item_free(ctx, kv_item3pp);
    tiledb.tiledb_kv_item_free(ctx, kv_item4pp);
    tiledb.tiledb_ctx_free(ctxpp);

  }
}
