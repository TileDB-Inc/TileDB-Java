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

package io.tiledb.libtiledb;


public class UtilsJNI {

  static {
    System.loadLibrary("tiledb");
  }

  public final static native int sizeOfInt();

  public final static native int sizeOfInt32();

  public final static native int sizeOfInt64();

  public final static native int sizeOfChar();

  public final static native int sizeOfFloat();

  public final static native int sizeOfDouble();

  public final static native int sizeOfInt8();

  public final static native int sizeOfUint8();

  public final static native int sizeOfInt16();

  public final static native int sizeOfUint16();

  public final static native int sizeOfUint32();

  public final static native int sizeOfUint64();

  public final static native long newIntArray(int[] array, int size);

  public final static native long newIntArraySet(int[] jarg1);

  public final static native long newInt32ArraySet(int[] jarg1);

  public final static native long newInt64ArraySet(long[] jarg1);

  public final static native long newCharArraySet(String jarg1);

  public final static native long newFloatArraySet(float[] jarg1);

  public final static native long newDoubleArraySet(double[] jarg1);

  public final static native long newInt8ArraySet(byte[] jarg1);

  public final static native long newUint8ArraySet(short[] jarg1);

  public final static native long newInt16ArraySet(short[] jarg1);

  public final static native long newUint16ArraySet(int[] jarg1);

  public final static native long newUint32ArraySet(long[] jarg1);

  public final static native long newUint64ArraySet(long[] jarg1);


  public final static native int[] intArrayGet(long array, int size);

  public final static native int[] int32ArrayGet(long array, int size);

  public final static native long[] int64ArrayGet(long array, int size);

  public final static native String charArrayGet(long array);

  public final static native float[] floatArrayGet(long array, int size);

  public final static native double[] doubleArrayGet(long array, int size);

  public final static native byte[] int8ArrayGet(long array, int size);

  public final static native short[] uint8ArrayGet(long array, int size);

  public final static native short[] int16ArrayGet(long array, int size);

  public final static native int[] uint16ArrayGet(long array, int size);

  public final static native long[] uint32ArrayGet(long array, int size);

  public final static native long[] uint64ArrayGet(long array, int size);

  public final static native int tiledb_query_submit_async(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, long jarg2, SWIGTYPE_p_tiledb_query_t jarg2_, Object jarg3);

  public final static native long java_callback();

  public final static native int tiledb_object_walk(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, String jarg2, int jarg3, Object jarg4);

  public final static native int tiledb_object_ls(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, String jarg2, Object jarg3);

  public final static native long java_path_callback();

  public final static native long new_tiledb_config_tpp();

  public final static native long tiledb_config_tpp_value(long jarg1);

  public final static native long new_tiledb_config_iter_tpp();

  public final static native long tiledb_config_iter_tpp_value(long jarg1);

  public final static native long new_tiledb_ctx_tpp();

  public final static native long tiledb_ctx_tpp_value(long jarg1);

  public final static native long new_tiledb_error_tpp();

  public final static native long tiledb_error_tpp_value(long jarg1);

  public final static native long new_tiledb_attribute_tpp();

  public final static native long tiledb_attribute_tpp_value(long jarg1);

  public final static native long new_tiledb_array_schema_tpp();

  public final static native long tiledb_array_schema_tpp_value(long jarg1);

  public final static native long new_tiledb_dimension_tpp();

  public final static native long tiledb_dimension_tpp_value(long jarg1);

  public final static native long new_tiledb_domain_tpp();

  public final static native long tiledb_domain_tpp_value(long jarg1);

  public final static native long new_tiledb_query_tpp();

  public final static native long tiledb_query_tpp_value(long jarg1);

  public final static native long new_tiledb_kv_schema_tpp();

  public final static native long tiledb_kv_schema_tpp_value(long jarg1);

  public final static native long new_tiledb_kv_tpp();

  public final static native long tiledb_kv_tpp_value(long jarg1);

  public final static native long new_tiledb_kv_item_tpp();

  public final static native long tiledb_kv_item_tpp_value(long jarg1);

  public final static native long new_tiledb_kv_iter_tpp();

  public final static native long tiledb_kv_iter_tpp_value(long jarg1);

  public final static native long new_tiledb_vfs_tpp();

  public final static native long tiledb_vfs_tpp_value(long jarg1);

  public final static native long new_tiledb_vfs_fh_tpp();

  public final static native long tiledb_vfs_fh_tpp_value(long jarg1);

}
