package io.tiledb.libtiledb;

import java.nio.ByteBuffer;

public class Utils {

  public static int32_tArray newInt32_tArray(int[] array) {
    int32_tArray ret = new int32_tArray(tiledb.newInt32ArraySet(array), true);
    return ret;
  }

  public static int64_tArray newInt64_tArray(long[] array) {
    int64_tArray ret = new int64_tArray(tiledb.newInt64ArraySet(array), true);
    return ret;
  }

  public static charArray newCharArray(String array) {
    charArray ret = new charArray(tiledb.newCharArraySet(array), true);
    return ret;
  }

  public static floatArray newFloatArray(float[] array) {
    floatArray ret = new floatArray(tiledb.newFloatArraySet(array), true);
    return ret;
  }

  public static doubleArray newDoubleArray(double[] array) {
    doubleArray ret = new doubleArray(tiledb.newDoubleArraySet(array), true);
    return ret;
  }

  public static int8_tArray newInt8_tArray(byte[] array) {
    int8_tArray ret = new int8_tArray(tiledb.newInt8ArraySet(array), true);
    return ret;
  }

  public static uint8_tArray newUint8_tArray(short[] array) {
    uint8_tArray ret = new uint8_tArray(tiledb.newUint8ArraySet(array), true);
    return ret;
  }

  public static int16_tArray newInt16_tArray(short[] array) {
    int16_tArray ret = new int16_tArray(tiledb.newInt16ArraySet(array), true);
    return ret;
  }

  public static uint16_tArray newUint16_tArray(int[] array) {
    uint16_tArray ret = new uint16_tArray(tiledb.newUint16ArraySet(array), true);
    return ret;
  }

  public static uint32_tArray newUint32_tArray(long[] array) {
    uint32_tArray ret = new uint32_tArray(tiledb.newUint32ArraySet(array), true);
    return ret;
  }

  public static uint64_tArray newUint64Array(long[] array) {
    uint64_tArray ret = new uint64_tArray(tiledb.newUint64ArraySet(array), true);
    return ret;
  }

  public static int[] int32ArrayGet(int32_tArray array, int pos, int size) {
    return tiledb.int32ArrayGet(int32_tArray.getCPtr(array), pos, size);
  }

  public static long[] int64ArrayGet(int64_tArray array, int pos, int size) {
    return tiledb.int64ArrayGet(int64_tArray.getCPtr(array), pos, size);
  }

  public static String charArrayGet(charArray array, int pos) {
    return tiledb.charArrayGet(charArray.getCPtr(array), pos);
  }

  public static float[] floatArrayGet(floatArray array, int pos, int size) {
    return tiledb.floatArrayGet(floatArray.getCPtr(array), pos, size);
  }

  public static double[] doubleArrayGet(doubleArray array, int pos, int size) {
    return tiledb.doubleArrayGet(doubleArray.getCPtr(array), pos, size);
  }

  public static byte[] int8ArrayGet(int8_tArray array, int pos, int size) {
    return tiledb.int8ArrayGet(int8_tArray.getCPtr(array), pos, size);
  }

  public static short[] uint8ArrayGet(uint8_tArray array, int pos, int size) {
    return tiledb.uint8ArrayGet(uint8_tArray.getCPtr(array), pos, size);
  }

  public static short[] int16ArrayGet(int16_tArray array, int pos, int size) {
    return tiledb.int16ArrayGet(int16_tArray.getCPtr(array), pos, size);
  }

  public static int[] uint16ArrayGet(uint16_tArray array, int pos, int size) {
    return tiledb.uint16ArrayGet(uint16_tArray.getCPtr(array), pos, size);
  }

  public static long[] uint32ArrayGet(uint32_tArray array, int pos, int size) {
    return tiledb.uint32ArrayGet(uint32_tArray.getCPtr(array), pos, size);
  }

  public static long[] uint64ArrayGet(uint64_tArray array, int pos, int size) {
    return tiledb.uint64ArrayGet(uint64_tArray.getCPtr(array), pos, size);
  }

  //	public static uint64_tArray newUint64Array(long[] array){
  //		uint64_tArray ret = new uint64_tArray(array.length);
  //		for (int i = 0; i < array.length; i++) {
  //			ret.setitem(i, new BigInteger(array[i]+""));
  //		}
  //		return ret;
  //	}
  //
  //	public static floatArray newFloatArray(float[] array){
  //		floatArray ret = new floatArray(array.length);
  //		for (int i = 0; i < array.length; i++) {
  //			ret.setitem(i, array[i]);
  //		}
  //		return ret;
  //	}

  //	public static charArray newCharArray(String s){
  //		char[] array = s.toCharArray();
  //		charArray ret = new charArray(array.length);
  //		for (int i = 0; i < array.length; i++) {
  //			//System.out.print(array[i]);
  //			ret.setitem(i, array[i]);
  //		}
  //		//System.out.println();
  //		return ret;
  //	}

  //	public static doubleArray newDoubleArray(double[] array) {
  //		doubleArray ret = new doubleArray(array.length);
  //		for (int i = 0; i < array.length; i++) {
  //			ret.setitem(i, array[i]);
  //		}
  //		return ret;
  //	}

  public static String substring(byte[] array, int start, int size) {
    byte[] c = new byte[size];
    for (int i = start; i < start + size; i++) {
      c[i - start] = array[i];
    }
    return new String(c);
  }

  public static String substring(charArray array, int start, int size) {
    char[] c = new char[size];
    for (int i = start; i < start + size; i++) {
      c[i - start] = array.getitem(i);
    }
    return new String(c);
  }

  public static SWIGTYPE_p_f_p_void__void java_callback() {
    long cPtr = tiledbJNI.java_callback();
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_p_void__void(cPtr, false);
  }

  public static SWIGTYPE_p_f_p_q_const__char_enum_tiledb_object_t_p_void__int java_path_callback() {
    long cPtr = tiledbJNI.java_path_callback();
    return (cPtr == 0)
        ? null
        : new SWIGTYPE_p_f_p_q_const__char_enum_tiledb_object_t_p_void__int(cPtr, false);
  }

  public static int tiledb_object_walk(
      SWIGTYPE_p_tiledb_ctx_t ctx, String path, tiledb_walk_order_t order, PathCallback callback) {
    return tiledbJNI.tiledb_object_walk_java(
        SWIGTYPE_p_tiledb_ctx_t.getCPtr(ctx), ctx, path, order.swigValue(), callback);
  }

  public static int tiledb_query_submit_async(
      SWIGTYPE_p_tiledb_ctx_t ctx, SWIGTYPE_p_tiledb_query_t query, Callback callback) {
    return tiledbJNI.tiledb_query_submit_async_java(
        SWIGTYPE_p_tiledb_ctx_t.getCPtr(ctx),
        ctx,
        SWIGTYPE_p_tiledb_query_t.getCPtr(query),
        query,
        callback);
  }

  public static int tiledb_query_set_subarray_nio(
      SWIGTYPE_p_tiledb_ctx_t ctx, SWIGTYPE_p_tiledb_query_t query, ByteBuffer subarray) {
    return tiledbJNI.tiledb_query_set_subarray_nio(
        SWIGTYPE_p_tiledb_ctx_t.getCPtr(ctx), SWIGTYPE_p_tiledb_query_t.getCPtr(query), subarray);
  }

  public static int tiledb_query_set_buffer_nio(
      SWIGTYPE_p_tiledb_ctx_t ctx,
      SWIGTYPE_p_tiledb_query_t query,
      String name,
      ByteBuffer buffer,
      SWIGTYPE_p_unsigned_long_long buffer_size) {
    return tiledbJNI.tiledb_query_set_buffer_nio(
        SWIGTYPE_p_tiledb_ctx_t.getCPtr(ctx),
        SWIGTYPE_p_tiledb_query_t.getCPtr(query),
        name,
        buffer,
        SWIGTYPE_p_unsigned_long_long.getCPtr(buffer_size));
  }

  public static int tiledb_query_set_buffer_nullable_nio(
      SWIGTYPE_p_tiledb_ctx_t ctx,
      SWIGTYPE_p_tiledb_query_t query,
      String name,
      ByteBuffer buffer,
      SWIGTYPE_p_unsigned_long_long buffer_size,
      ByteBuffer validity_bytemap,
      SWIGTYPE_p_unsigned_long_long validity_bytemap_size) {
    return tiledbJNI.tiledb_query_set_buffer_nullable_nio(
        SWIGTYPE_p_tiledb_ctx_t.getCPtr(ctx),
        SWIGTYPE_p_tiledb_query_t.getCPtr(query),
        name,
        buffer,
        SWIGTYPE_p_unsigned_long_long.getCPtr(buffer_size),
        validity_bytemap,
        SWIGTYPE_p_unsigned_long_long.getCPtr(validity_bytemap_size));
  }

  public static int tiledb_query_set_buffer_var_nio(
      SWIGTYPE_p_tiledb_ctx_t ctx,
      SWIGTYPE_p_tiledb_query_t query,
      String name,
      ByteBuffer buffer_off,
      SWIGTYPE_p_unsigned_long_long buffer_off_size,
      ByteBuffer buffer_val,
      SWIGTYPE_p_unsigned_long_long buffer_val_size) {
    return tiledbJNI.tiledb_query_set_buffer_var_nio(
        SWIGTYPE_p_tiledb_ctx_t.getCPtr(ctx),
        SWIGTYPE_p_tiledb_query_t.getCPtr(query),
        name,
        buffer_off,
        SWIGTYPE_p_unsigned_long_long.getCPtr(buffer_off_size),
        buffer_val,
        SWIGTYPE_p_unsigned_long_long.getCPtr(buffer_val_size));
  }

  public static int tiledb_query_set_buffer_var_nullable_nio(
      SWIGTYPE_p_tiledb_ctx_t ctx,
      SWIGTYPE_p_tiledb_query_t query,
      String name,
      ByteBuffer offsets,
      SWIGTYPE_p_unsigned_long_long offsets_size,
      ByteBuffer buffer,
      SWIGTYPE_p_unsigned_long_long buffer_size,
      ByteBuffer validity_bytemap,
      SWIGTYPE_p_unsigned_long_long validity_bytemap_size) {
    return tiledbJNI.tiledb_query_set_buffer_var_nullable_nio(
        SWIGTYPE_p_tiledb_ctx_t.getCPtr(ctx),
        SWIGTYPE_p_tiledb_query_t.getCPtr(query),
        name,
        offsets,
        SWIGTYPE_p_unsigned_long_long.getCPtr(offsets_size),
        buffer,
        SWIGTYPE_p_unsigned_long_long.getCPtr(buffer_size),
        validity_bytemap,
        SWIGTYPE_p_unsigned_long_long.getCPtr(validity_bytemap_size));
  }

  public static int tiledb_object_ls(
      SWIGTYPE_p_tiledb_ctx_t ctx, String path, PathCallback callback) {
    return tiledbJNI.tiledb_object_ls_java(
        SWIGTYPE_p_tiledb_ctx_t.getCPtr(ctx), ctx, path, callback);
  }
}
