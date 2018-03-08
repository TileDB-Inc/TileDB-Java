package io.tiledb.api;

public class Utils {

  public static intArray newIntArray(int[] array) {
    intArray ret = new intArray(UtilsJNI.newIntArraySet(array), true);
    return ret;
  }


  public static int32_tArray newInt32_tArray(int[] array) {
    int32_tArray ret = new int32_tArray(UtilsJNI.newInt32ArraySet(array), true);
    return ret;
  }

  public static int64_tArray newInt64_tArray(long[] array) {
    int64_tArray ret = new int64_tArray(UtilsJNI.newInt64ArraySet(array), true);
    return ret;
  }

  public static charArray newCharArray(String array) {
    charArray ret = new charArray(UtilsJNI.newCharArraySet(array), true);
    return ret;
  }

  public static floatArray newFloatArray(float[] array) {
    floatArray ret = new floatArray(UtilsJNI.newFloatArraySet(array), true);
    return ret;
  }

  public static doubleArray newDoubleArray(double[] array) {
    doubleArray ret = new doubleArray(UtilsJNI.newDoubleArraySet(array), true);
    return ret;
  }

  public static int8_tArray newInt8_tArray(byte[] array) {
    int8_tArray ret = new int8_tArray(UtilsJNI.newInt8ArraySet(array), true);
    return ret;
  }

  public static uint8_tArray newUint8_tArray(short[] array) {
    uint8_tArray ret = new uint8_tArray(UtilsJNI.newUint8ArraySet(array), true);
    return ret;
  }

  public static int16_tArray newInt16_tArray(short[] array) {
    int16_tArray ret = new int16_tArray(UtilsJNI.newInt16ArraySet(array), true);
    return ret;
  }

  public static uint16_tArray newUint16_tArray(int[] array) {
    uint16_tArray ret = new uint16_tArray(UtilsJNI.newUint16ArraySet(array), true);
    return ret;
  }

  public static uint32_tArray newUint32_tArray(long[] array) {
    uint32_tArray ret = new uint32_tArray(UtilsJNI.newUint32ArraySet(array), true);
    return ret;
  }

  public static uint64_tArray newUint64Array(long[] array) {
    uint64_tArray ret = new uint64_tArray(UtilsJNI.newUint64ArraySet(array), true);
    return ret;
  }


  public static int[] intArrayGet(intArray array, int size) {
    return UtilsJNI.intArrayGet(intArray.getCPtr(array), size);
  }

  public static int[] int32ArrayGet(int32_tArray array, int size) {
    return UtilsJNI.int32ArrayGet(int32_tArray.getCPtr(array), size);
  }

  public static long[] int64ArrayGet(int64_tArray array, int size) {
    return UtilsJNI.int64ArrayGet(int64_tArray.getCPtr(array), size);
  }

  public static String charArrayGet(charArray array) {
    return UtilsJNI.charArrayGet(charArray.getCPtr(array));
  }

  public static float[] floatArrayGet(floatArray array, int size) {
    return UtilsJNI.floatArrayGet(floatArray.getCPtr(array), size);
  }

  public static double[] doubleArrayGet(doubleArray array, int size) {
    return UtilsJNI.doubleArrayGet(doubleArray.getCPtr(array), size);
  }

  public static byte[] int8ArrayGet(int8_tArray array, int size) {
    return UtilsJNI.int8ArrayGet(int8_tArray.getCPtr(array), size);
  }

  public static short[] uint8ArrayGet(uint8_tArray array, int size) {
    return UtilsJNI.uint8ArrayGet(uint8_tArray.getCPtr(array), size);
  }

  public static short[] int16ArrayGet(int16_tArray array, int size) {
    return UtilsJNI.int16ArrayGet(int16_tArray.getCPtr(array), size);
  }

  public static int[] uint16ArrayGet(uint16_tArray array, int size) {
    return UtilsJNI.uint16ArrayGet(uint16_tArray.getCPtr(array), size);
  }

  public static long[] uint32ArrayGet(uint32_tArray array, int size) {
    return UtilsJNI.uint32ArrayGet(uint32_tArray.getCPtr(array), size);
  }

  public static long[] uint64ArrayGet(uint64_tArray array, int size) {
    return UtilsJNI.uint64ArrayGet(uint64_tArray.getCPtr(array), size);
  }


  public static int sizeOfType(Object array) {
    Class arrayClass = array.getClass();
    if(arrayClass.equals(intArray.class)){
      return UtilsJNI.sizeOfInt();
    }
    else if(arrayClass.equals(int32_tArray.class)){
      return UtilsJNI.sizeOfInt32();
    }
    else if(arrayClass.equals(int64_tArray.class)){
      return UtilsJNI.sizeOfInt64();
    }
    else if(arrayClass.equals(charArray.class)){
      return UtilsJNI.sizeOfChar();
    }
    else if(arrayClass.equals(floatArray.class)){
      return UtilsJNI.sizeOfFloat();
    }
    else if(arrayClass.equals(doubleArray.class)){
      return UtilsJNI.sizeOfDouble();
    }
    else if(arrayClass.equals(int8_tArray.class)){
      return UtilsJNI.sizeOfInt8();
    }
    else if(arrayClass.equals(uint8_tArray.class)){
      return UtilsJNI.sizeOfUint8();
    }
    else if(arrayClass.equals(int16_tArray.class)){
      return UtilsJNI.sizeOfInt16();
    }
    else if(arrayClass.equals(uint16_tArray.class)){
      return UtilsJNI.sizeOfUint16();
    }
    else if(arrayClass.equals(uint32_tArray.class)){
      return UtilsJNI.sizeOfUint32();
    }
    else if(arrayClass.equals(uint64_tArray.class)){
      return UtilsJNI.sizeOfUint64();
    }
    return -1;
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
    long cPtr = UtilsJNI.java_callback();
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_p_void__void(cPtr, false);
  }

  public static SWIGTYPE_p_f_p_q_const__char_enum_tiledb_object_t_p_void__int java_path_callback() {
    long cPtr = UtilsJNI.java_path_callback();
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_p_q_const__char_enum_tiledb_object_t_p_void__int(cPtr, false);
  }

  public static int tiledb_object_walk(tiledb_ctx_t ctx, String path, tiledb_walk_order_t order, PathCallback callback) {
    return UtilsJNI.tiledb_object_walk(tiledb_ctx_t.getCPtr(ctx), ctx, path, order.swigValue(), callback);
  }

  public static int tiledb_query_submit_async(tiledb_ctx_t ctx, tiledb_query_t query, Callback callback) {
    return UtilsJNI.tiledb_query_submit_async(tiledb_ctx_t.getCPtr(ctx), ctx, tiledb_query_t.getCPtr(query), query, callback);
  }

  public static int tiledb_ls(tiledb_ctx_t ctx, String path, PathCallback callback) {
    return UtilsJNI.tiledb_ls(tiledb_ctx_t.getCPtr(ctx), ctx, path, callback);
  }
}
