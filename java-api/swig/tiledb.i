%module tiledb

%pragma(java) jniclasscode=%{
  static {
    try {
      System.loadLibrary("tiledb");
    } catch (UnsatisfiedLinkError e) {
      System.err.println("Native code library failed to load. \n" + e);
      System.exit(1);
    }
  }

  public final static native int tiledb_query_submit_async_java(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, long jarg2, SWIGTYPE_p_tiledb_query_t jarg2_, Object jarg3);
  public final static native int tiledb_object_walk_java(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, String jarg2, int jarg3, Object jarg4);
  public final static native int tiledb_object_ls_java(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, String jarg2, Object jarg3);
  public final static native long java_path_callback();
  public final static native long java_callback();
%}

#define __attribute__(x)

%{
#include <string>
#include <vector>
#include <stdio.h>

#include "tiledb/tiledb.h"
#include "tiledb_java_extensions.h"
%}

%include "stdint.i"
%include "carrays.i"
%array_class(int32_t, int32_tArray);
%array_class(int64_t, int64_tArray);
%array_class(char, charArray);
%array_class(float, floatArray);
%array_class(double, doubleArray);
%array_class(int8_t, int8_tArray);
%array_class(uint8_t, uint8_tArray);
%array_class(int16_t, int16_tArray);
%array_class(uint16_t, uint16_tArray);
%array_class(uint32_t, uint32_tArray);
%array_class(uint64_t, uint64_tArray);

%array_functions(char *, charpArray);
%array_functions(void *, voidpArray )

%include "cpointer.i"
%pointer_functions(int, intp);
%pointer_functions(float, floatp);
%pointer_functions(void **, voidppp);
%pointer_functions(int *, intpp);
%pointer_functions(char *, charpp);
%pointer_functions(unsigned int, uintp);
%pointer_functions(unsigned long long, ullp);

%include "typemaps.i"
%include "enums.swg"

%javaconst(1);

%pointer_functions(tiledb_object_t, tiledb_object_tp);
%pointer_functions(tiledb_query_type_t, tiledb_query_type_tp);
%pointer_functions(tiledb_query_status_t, tiledb_query_status_tp);
%pointer_functions(tiledb_filesystem_t, tiledb_filesystem_tp);
%pointer_functions(tiledb_datatype_t, tiledb_datatype_tp);
%pointer_functions(tiledb_array_type_t, tiledb_array_type_tp);
%pointer_functions(tiledb_layout_t, tiledb_layout_tp);
%pointer_functions(tiledb_compressor_t, tiledb_compressor_tp);
%pointer_functions(tiledb_walk_order_t, tiledb_walk_order_tp);
%pointer_functions(tiledb_vfs_mode_t, tiledb_vfs_mode_tp);

%pointer_functions(tiledb_array_t*, tiledb_array_tpp);
%pointer_functions(tiledb_config_t*, tiledb_config_tpp);
%pointer_functions(tiledb_config_iter_t*, tiledb_config_iter_tpp);
%pointer_functions(tiledb_ctx_t*, tiledb_ctx_tpp);
%pointer_functions(tiledb_error_t*, tiledb_error_tpp);
%pointer_functions(tiledb_attribute_t*, tiledb_attribute_tpp);
%pointer_functions(tiledb_array_schema_t*, tiledb_array_schema_tpp);
%pointer_functions(tiledb_dimension_t*, tiledb_dimension_tpp);
%pointer_functions(tiledb_domain_t*, tiledb_domain_tpp);
%pointer_functions(tiledb_query_t*, tiledb_query_tpp);
%pointer_functions(tiledb_kv_schema_t*, tiledb_kv_schema_tpp);
%pointer_functions(tiledb_kv_t*, tiledb_kv_tpp);
%pointer_functions(tiledb_kv_item_t*, tiledb_kv_item_tpp);
%pointer_functions(tiledb_kv_iter_t*, tiledb_kv_iter_tpp);
%pointer_functions(tiledb_vfs_t*, tiledb_vfs_tpp);
%pointer_functions(tiledb_vfs_fh_t*, tiledb_vfs_fh_tpp);

%native (sizeOfInt32) int sizeOfInt32();
%native (sizeOfInt64) int sizeOfInt64();
%native (sizeOfChar) int sizeOfChar();
%native (sizeOfFloat) int sizeOfFloat();
%native (sizeOfDouble) int sizeOfDouble();
%native (sizeOfInt8) int sizeOfInt8();
%native (sizeOfUint8) int sizeOfUint8();
%native (sizeOfInt16) int sizeOfInt16();
%native (sizeOfUint16) int sizeOfUint16();
%native (sizeOfUint32) int sizeOfUint32();
%native (sizeOfUint64) int sizeOfUint64();

%native (newInt32ArraySet)  jlong newInt32ArraySet(jintArray jarg1);
%native (newInt64ArraySet)  jlong newInt64ArraySet(jlongArray jarg1);
%native (newCharArraySet)   jlong newCharArraySet(jstring jarg1);
%native (newFloatArraySet)  jlong newFloatArraySet(jfloatArray jarg1);
%native (newDoubleArraySet) jlong newDoubleArraySet(jdoubleArray jarg1);
%native (newInt8ArraySet)   jlong newInt8ArraySet(jbyteArray jarg1);
%native (newUint8ArraySet)  jlong newUint8ArraySet(jshortArray jarg1);
%native (newInt16ArraySet)  jlong newInt16ArraySet(jshortArray jarg1);
%native (newUint16ArraySet) jlong newUint16ArraySet(jintArray jarg1);
%native (newUint32ArraySet) jlong newUint32ArraySet(jlongArray jarg1);
%native (newUint64ArraySet) jlong newUint64ArraySet(jlongArray jarg1);

%native (int32ArrayGet) jintArray int32ArrayGet(jlong array, jint sz);
%native (int64ArrayGet) jlongArray int64ArrayGet(jlong array, jint sz);
%native (charArrayGet)  jstring charArrayGet(jlong array);
%native (floatArrayGet) jfloatArray floatArrayGet(jlong array, jint size);
%native (doubleArrayGet) jdoubleArray doubleArrayGet(jlong array, jint size);
%native (int8ArrayGet) jbyteArray int8ArrayGet(jlong array, jint size);
%native (uint8ArrayGet) jshortArray uint8ArrayGet(jlong array, jint size);
%native (int16ArrayGet) jshortArray int16ArrayGet(jlong array, jint size);
%native (uint16ArrayGet) jintArray uint16ArrayGet(jlong array, jint size);
%native (uint32ArrayGet) jlongArray uint32ArrayGet(jlong array, jint size);
%native (uint64ArrayGet) jlongArray uint64ArrayGet(jlong array, jint size);

%{

  /* signed char[] support */
  static int SWIG_JavaArrayInSchar (JNIEnv *jenv, jbyte **jarr, signed char **carr, jbyteArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetByteArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new signed char[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (signed char)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutSchar (JNIEnv *jenv, jbyte *jarr, signed char *carr, jbyteArray input) {
    jenv->ReleaseByteArrayElements(input, jarr, JNI_ABORT);
  }

  static jbyteArray SWIG_JavaArrayOutSchar (JNIEnv *jenv, signed char *result, jsize sz) {
    jbyte *arr;
    int i;
    jbyteArray jresult = jenv->NewByteArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetByteArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jbyte)result[i];
    jenv->ReleaseByteArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* unsigned char[] support */
  static int SWIG_JavaArrayInUchar (JNIEnv *jenv, jshort **jarr, unsigned char **carr, jshortArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetShortArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new unsigned char[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (unsigned char)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutUchar (JNIEnv *jenv, jshort *jarr, unsigned char *carr, jshortArray input) {
    jenv->ReleaseShortArrayElements(input, jarr, JNI_ABORT);
  }

  static jshortArray SWIG_JavaArrayOutUchar (JNIEnv *jenv, unsigned char *result, jsize sz) {
    jshort *arr;
    int i;
    jshortArray jresult = jenv->NewShortArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetShortArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jshort)result[i];
    jenv->ReleaseShortArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* short[] support */
  static int SWIG_JavaArrayInShort (JNIEnv *jenv, jshort **jarr, short **carr, jshortArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetShortArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new short[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (short)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutShort (JNIEnv *jenv, jshort *jarr, short *carr, jshortArray input) {
    jenv->ReleaseShortArrayElements(input, jarr, JNI_ABORT);
  }

  static jshortArray SWIG_JavaArrayOutShort (JNIEnv *jenv, short *result, jsize sz) {
    jshort *arr;
    int i;
    jshortArray jresult = jenv->NewShortArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetShortArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jshort)result[i];
    jenv->ReleaseShortArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* unsigned short[] support */
  static int SWIG_JavaArrayInUshort (JNIEnv *jenv, jint **jarr, unsigned short **carr, jintArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetIntArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new unsigned short[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (unsigned short)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutUshort (JNIEnv *jenv, jint *jarr, unsigned short *carr, jintArray input) {
    jenv->ReleaseIntArrayElements(input, jarr, JNI_ABORT);
  }

  static jintArray SWIG_JavaArrayOutUshort (JNIEnv *jenv, unsigned short *result, jsize sz) {
    jint *arr;
    int i;
    jintArray jresult = jenv->NewIntArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetIntArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jint)result[i];
    jenv->ReleaseIntArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* int[] support */
  static int SWIG_JavaArrayInInt (JNIEnv *jenv, jint **jarr, int **carr, jintArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetIntArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new int[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (int)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutInt (JNIEnv *jenv, jint *jarr, int *carr, jintArray input) {
    jenv->ReleaseIntArrayElements(input, jarr, JNI_ABORT);
  }


  /* unsigned int[] support */
  static int SWIG_JavaArrayInUint (JNIEnv *jenv, jlong **jarr, unsigned int **carr, jlongArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetLongArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new unsigned int[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (unsigned int)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutUint (JNIEnv *jenv, jlong *jarr, unsigned int *carr, jlongArray input) {
    jenv->ReleaseLongArrayElements(input, jarr, JNI_ABORT);
  }

  static jlongArray SWIG_JavaArrayOutUint (JNIEnv *jenv, unsigned int *result, jsize sz) {
    jlong *arr;
    int i;
    jlongArray jresult = jenv->NewLongArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetLongArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jlong)result[i];
    jenv->ReleaseLongArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* long[] support */
  static int SWIG_JavaArrayInLong (JNIEnv *jenv, jint **jarr, long **carr, jintArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetIntArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new long[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (long)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutLong (JNIEnv *jenv, jint *jarr, long *carr, jintArray input) {
    jenv->ReleaseIntArrayElements(input, jarr, JNI_ABORT);
  }

  static jintArray SWIG_JavaArrayOutLong (JNIEnv *jenv, long *result, jsize sz) {
    jint *arr;
    int i;
    jintArray jresult = jenv->NewIntArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetIntArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jint)result[i];
    jenv->ReleaseIntArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* unsigned long[] support */
  static int SWIG_JavaArrayInUlong (JNIEnv *jenv, jlong **jarr, uint64_t **carr, jlongArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetLongArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new uint64_t[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (uint64_t)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutUlong (JNIEnv *jenv, jlong *jarr, uint64_t *carr, jlongArray input) {
    jenv->ReleaseLongArrayElements(input, jarr, JNI_ABORT);
  }

  static jlongArray SWIG_JavaArrayOutUlong (JNIEnv *jenv, uint64_t *result, jsize sz) {
    jlong *arr;
    int i;
    jlongArray jresult = jenv->NewLongArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetLongArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jlong)result[i];
    jenv->ReleaseLongArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* jlong[] support */
  static int SWIG_JavaArrayInLonglong (JNIEnv *jenv, jlong **jarr, jlong **carr, jlongArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetLongArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new jlong[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (jlong)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutLonglong (JNIEnv *jenv, jlong *jarr, int64_t* carr, jlongArray input) {
    jenv->ReleaseLongArrayElements(input, jarr, JNI_ABORT);
  }

  static jlongArray SWIG_JavaArrayOutLonglong (JNIEnv *jenv, jlong *result, jsize sz) {
    jlong *arr;
    int i;
    jlongArray jresult = jenv->NewLongArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetLongArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jlong)result[i];
    jenv->ReleaseLongArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* float[] support */
  static int SWIG_JavaArrayInFloat (JNIEnv *jenv, jfloat **jarr, float **carr, jfloatArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetFloatArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new float[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (float)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutFloat (JNIEnv *jenv, jfloat *jarr, float *carr, jfloatArray input) {
    jenv->ReleaseFloatArrayElements(input, jarr, JNI_ABORT);
  }

  static jfloatArray SWIG_JavaArrayOutFloat (JNIEnv *jenv, float *result, jsize sz) {
    jfloat *arr;
    int i;
    jfloatArray jresult = jenv->NewFloatArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetFloatArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jfloat)result[i];
    jenv->ReleaseFloatArrayElements(jresult, arr, 0);
    return jresult;
  }


  /* double[] support */
  static int SWIG_JavaArrayInDouble (JNIEnv *jenv, jdouble **jarr, double **carr, jdoubleArray input) {
    int i;
    jsize sz;
    if (!input) {
      SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
      return 0;
    }
    sz = jenv->GetArrayLength(input);
    *jarr = jenv->GetDoubleArrayElements(input, 0);
    if (!*jarr)
      return 0; 
    *carr = new double[sz]; 
    if (!*carr) {
      SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
      return 0;
    }
    for (i=0; i<sz; i++)
      (*carr)[i] = (double)(*jarr)[i];
    return 1;
  }

  static void SWIG_JavaArrayArgoutDouble (JNIEnv *jenv, jdouble *jarr, double *carr, jdoubleArray input) {
    jenv->ReleaseDoubleArrayElements(input, jarr, JNI_ABORT);
  }

  static jdoubleArray SWIG_JavaArrayOutDouble (JNIEnv *jenv, double *result, jsize sz) {
    jdouble *arr;
    int i;
    jdoubleArray jresult = jenv->NewDoubleArray(sz);
    if (!jresult)
      return NULL;
    arr = jenv->GetDoubleArrayElements(jresult, 0);
    if (!arr)
      return NULL;
    for (i=0; i<sz; i++)
      arr[i] = (jdouble)result[i];
    jenv->ReleaseDoubleArrayElements(jresult, arr, 0);
    return jresult;
  }

#ifdef __cplusplus
extern "C" {
#endif

  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newInt32ArraySet(JNIEnv *jenv, jclass jcls, jintArray jarg1) {
    jlong jresult = 0 ;
    int32_t *arg1 ;
    jint *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInInt(jenv, &jarr1, (int **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutInt(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jintArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_int32ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    int32_t *arr = *(int32_t **)&array;
    jintArray jresult = jenv->NewIntArray(sz);
    if (!jresult)
      return NULL;
    jenv->SetIntArrayRegion(jresult, 0, sz, arr);
    return jresult;
  }


  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newInt64ArraySet(JNIEnv *jenv, jclass jcls, jlongArray jarg1) {
    jlong jresult = 0 ;
    int64_t *arg1 ;
    jlong *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInLonglong(jenv, &jarr1, (long long **)&arg1, jarg1)) return 0;  
    SWIG_JavaArrayArgoutLonglong(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jlongArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_int64ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    int64_t *arr = *(int64_t **)&array;
    jlongArray jresult = jenv->NewLongArray(sz);
    if (!jresult)
      return NULL;
    jenv->SetLongArrayRegion(jresult, 0, sz, (jlong*) arr);
    return jresult;
  }

  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newCharArraySet(JNIEnv *jenv, jclass jcls, jstring jarg1) {
    jlong jresult = 0 ;
    char *arg1 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = 0;
    if (jarg1) {
      char *temp = (char *)jenv->GetStringUTFChars(jarg1, 0);
      jsize length = jenv->GetStringUTFLength(jarg1);
      arg1 = new char[length];
      strcpy(arg1, temp);
      if (!arg1) return 0;
      jenv->ReleaseStringUTFChars(jarg1, (const char *)temp);
    }
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jstring JNICALL Java_io_tiledb_libtiledb_tiledbJNI_charArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    char *arr = *(char **)&array;
    jstring jresult = jenv->NewStringUTF(arr);
    if (!jresult)
      return NULL;
    return jresult;
  }

  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newFloatArraySet(JNIEnv *jenv, jclass jcls, jfloatArray jarg1) {
    jlong jresult = 0 ;
    float *arg1 ;
    jfloat *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInFloat(jenv, &jarr1, (float **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutFloat(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jfloatArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_floatArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    float *arr = *(float **)&array;
    jfloatArray jresult = jenv->NewFloatArray(sz);
    if (!jresult)
      return NULL;
    jenv->SetFloatArrayRegion(jresult, 0, sz, arr);
    return jresult;
  }


  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newDoubleArraySet(JNIEnv *jenv, jclass jcls, jdoubleArray jarg1) {
    jlong jresult = 0 ;
    double *arg1 ;
    jdouble *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInDouble(jenv, &jarr1, (double **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutDouble(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jdoubleArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_doubleArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    double *arr = *(double **)&array;
    jdoubleArray jresult = jenv->NewDoubleArray(sz);
    if (!jresult)
      return NULL;
    jenv->SetDoubleArrayRegion(jresult, 0, sz, arr);
    return jresult;
  }

  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newInt8ArraySet(JNIEnv *jenv, jclass jcls, jbyteArray jarg1) {
    jlong jresult = 0 ;
    int8_t *arg1 ;
    jbyte *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInSchar(jenv, &jarr1, (signed char **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutSchar(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }


  JNIEXPORT jbyteArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_int8ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    int8_t *arr = *(int8_t **)&array;
    jbyteArray jresult = jenv->NewByteArray(sz);
    if (!jresult)
      return NULL;
    jenv->SetByteArrayRegion(jresult, 0, sz, arr);
    return jresult;
  }

  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newUint8ArraySet(JNIEnv *jenv, jclass jcls, jshortArray jarg1) {
    jlong jresult = 0 ;
    uint8_t *arg1 ;
    jshort *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInUchar(jenv, &jarr1, (unsigned char **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutUchar(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jshortArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_uint8ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    uint8_t *arr = *(uint8_t **)&array;
    jshortArray jresult = jenv->NewShortArray(sz);
    if (!jresult)
      return NULL;
    jshort * short_arr = new jshort[sz];
    for(int i = 0; i<sz; i++){
      short_arr[i] = (jshort)arr[i];
    }
    jenv->ReleaseShortArrayElements(jresult, short_arr, 0);
    return jresult;
  }

  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newInt16ArraySet(JNIEnv *jenv, jclass jcls, jshortArray jarg1) {
    jlong jresult = 0 ;
    int16_t *arg1 ;
    jshort *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInShort(jenv, &jarr1, (short **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutShort(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jshortArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_int16ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    int16_t *arr = *(int16_t **)&array;
    jshortArray jresult = jenv->NewShortArray(sz);
    if (!jresult)
      return NULL;
    jenv->SetShortArrayRegion(jresult, 0, sz, arr);
    return jresult;
  }

  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newUint16ArraySet(JNIEnv *jenv, jclass jcls, jintArray jarg1) {
    jlong jresult = 0 ;
    uint16_t *arg1 ;
    jint *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInUshort(jenv, &jarr1, (unsigned short **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutUshort(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jintArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_uint16ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    uint16_t *arr = *(uint16_t **)&array;
    jintArray jresult = jenv->NewIntArray(sz);
    if (!jresult)
      return NULL;
    jint * int_arr = new jint[sz];
    for(int i = 0; i<sz; i++){
      int_arr[i] = (jint)arr[i];
    }
    jenv->ReleaseIntArrayElements(jresult, int_arr, 0);
    return jresult;
  }

  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newUint32ArraySet(JNIEnv *jenv, jclass jcls, jlongArray jarg1) {
    jlong jresult = 0 ;
    uint32_t *arg1 ;
    jlong *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInUint(jenv, &jarr1, (unsigned int **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutUint(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jlongArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_uint32ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    uint32_t *arr = *(uint32_t **)&array;
    jlongArray jresult = jenv->NewLongArray(sz);
    if (!jresult)
      return NULL;
    jlong * long_arr = new jlong[sz];
    for(int i = 0; i<sz; i++){
      long_arr[i] = (jlong)arr[i];
    }
    jenv->ReleaseLongArrayElements(jresult, long_arr, 0);
    return jresult;
  }


  JNIEXPORT jlong JNICALL Java_io_tiledb_libtiledb_tiledbJNI_newUint64ArraySet(JNIEnv *jenv, jclass jcls, jlongArray jarg1) {
    jlong jresult = 0 ;
    uint64_t *arg1 ;
    jlong *jarr1 ;
    
    (void)jenv;
    (void)jcls;
    if (!SWIG_JavaArrayInUlong(jenv, &jarr1, (uint64_t **)&arg1, jarg1)) return 0; 
    SWIG_JavaArrayArgoutUlong(jenv, jarr1, arg1, jarg1);
    *(void **)&jresult = (void *)arg1; 
    return jresult; 
  }

  JNIEXPORT jlongArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_uint64ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint sz) {
    uint64_t *arr = *(uint64_t **)&array;
    jlongArray jresult = jenv->NewLongArray(sz);
    if (!jresult)
      return NULL;
    jlong * long_arr = new jlong[sz];
    for(int i = 0; i<sz; i++){
      long_arr[i] = (jlong)arr[i];
    }
    jenv->ReleaseLongArrayElements(jresult, long_arr, 0);
    return jresult;
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfInt32(JNIEnv *jenv, jobject obj){
    return sizeof(int32_t);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfInt64(JNIEnv *jenv, jobject obj){
    return sizeof(int64_t);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfChar(JNIEnv *jenv, jobject obj){
    return sizeof(char);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfFloat(JNIEnv *jenv, jobject obj){
    return sizeof(float);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfDouble(JNIEnv *jenv, jobject obj){
    return sizeof(double);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfInt8(JNIEnv *jenv, jobject obj){
    return sizeof(int8_t);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfUint8(JNIEnv *jenv, jobject obj){
    return sizeof(uint8_t);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_tiledbInt16(JNIEnv *jenv, jobject obj){
    return sizeof(int16_t);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfUint16(JNIEnv *jenv, jobject obj){
    return sizeof(uint16_t);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfUint32(JNIEnv *jenv, jobject obj){
    return sizeof(uint32_t);
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_sizeOfUint64(JNIEnv *jenv, jobject obj){
    return sizeof(uint64_t);
  }

  struct Callback {
     jobject obj;
     jclass cls;
  };

  SWIGEXPORT JavaVM* getJVM(){
    JavaVM* vm;
    jsize vmCount;
    if (JNI_GetCreatedJavaVMs(&vm, 1, &vmCount) != JNI_OK || vmCount == 0) {
        fprintf(stderr, "Could not get active VM\n");
        return NULL;
    }
    return vm;
  }

  SWIGEXPORT JNIEnv* getJNI(JavaVM* vm){
    JNIEnv* env;
    jint result = vm->GetEnv((void**)&env, JNI_VERSION_1_6);
    if (result == JNI_EDETACHED) {
        result = vm->AttachCurrentThread((void**)&env, NULL);
    }
    if (result != JNI_OK) {
        fprintf(stderr, "Failed to get JNIEnv\n");
        return NULL;
    }
    return env;
  }

  SWIGEXPORT void java_callback(void* data) {
    JavaVM* vm = getJVM();
    JNIEnv* jenv = getJNI(vm);
    jobject obj = ((Callback*)data)->obj;
    jclass cls=jenv->GetObjectClass(obj);
    jmethodID mid = jenv->GetMethodID(cls, "call", "()V");
    if (mid == 0)
      return;
    jenv->CallVoidMethod(obj, mid);
    vm->DetachCurrentThread();
  }

  SWIGEXPORT int java_path_callback(const char* path, tiledb_object_t type, void* data) {
    JavaVM* vm = getJVM();
    JNIEnv* jenv = getJNI(vm);
    jobject obj = ((Callback*)data)->obj;
    jclass cls=jenv->GetObjectClass(obj);
    jmethodID mid = jenv->GetMethodID(cls, "call", "(JI)I");
    if (mid == 0)
      return -1;
    jlong jpath;
    *(const char **)&jpath = path;
    jint ret = jenv->CallIntMethod(obj, mid, jpath, (jint) type);
    vm->DetachCurrentThread();
    return ret;
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_tiledb_1query_1submit_1async_1java(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jlong jarg2, jobject jarg2_, jobject jarg3) {
    jint jresult = 0 ;
    tiledb_ctx_t *arg1 = (tiledb_ctx_t *) 0 ;
    tiledb_query_t *arg2 = (tiledb_query_t *) 0 ;
    void (*arg3)(void *) = (void (*)(void *)) java_callback ;
    jobject arg4 ;
    int result;

    (void)jenv;
    (void)jcls;
    (void)jarg1_;
    (void)jarg2_;
    arg1 = *(tiledb_ctx_t **)&jarg1;
    arg2 = *(tiledb_query_t **)&jarg2;
    struct Callback* callback = new Callback;
    callback->obj=jenv->NewGlobalRef(jarg3);
    callback->cls=jenv->GetObjectClass(jarg3);
    result = (int)tiledb_query_submit_async(arg1,arg2,arg3,(void *) callback);
    jresult = (jint)result;
    return jresult;
  }

  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_tiledb_1object_1walk_1java(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jstring jarg2, jint jarg3, jobject jarg4) {
    jint jresult = 0 ;
    tiledb_ctx_t *arg1 = (tiledb_ctx_t *) 0 ;
    char *arg2 = (char *) 0 ;
    tiledb_walk_order_t arg3 ;
    int (*arg4)(char const *,tiledb_object_t,void *) = java_path_callback;
    void *arg5 = (void *) 0 ;
    int result;

    (void)jenv;
    (void)jcls;
    (void)jarg1_;
    arg1 = *(tiledb_ctx_t **)&jarg1;
    arg2 = 0;
    if (jarg2) {
      arg2 = (char *)jenv->GetStringUTFChars(jarg2, 0);
      if (!arg2) return 0;
    }
    arg3 = (tiledb_walk_order_t)jarg3;
    struct Callback* callback = new Callback;
    callback->obj=jenv->NewGlobalRef(jarg4);
    callback->cls=jenv->GetObjectClass(jarg4);
    result = (int)tiledb_object_walk(arg1,(char const *)arg2,arg3,arg4,(void *) callback);
    jresult = (jint)result;
    if (arg2) jenv->ReleaseStringUTFChars(jarg2, (const char *)arg2);
    return jresult;
  }


  JNIEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_tiledb_1object_1ls_1java(JNIEnv *jenv, jclass jcls, jlong jarg1, jobject jarg1_, jstring jarg2, jobject jarg3) {
    jint jresult = 0 ;
    tiledb_ctx_t *arg1 = (tiledb_ctx_t *) 0 ;
    char *arg2 = (char *) 0 ;
    int (*arg3)(char const *,tiledb_object_t,void *) = java_path_callback;
    int result;

    (void)jenv;
    (void)jcls;
    (void)jarg1_;
    arg1 = *(tiledb_ctx_t **)&jarg1;
    arg2 = 0;
    if (jarg2) {
      arg2 = (char *)jenv->GetStringUTFChars(jarg2, 0);
      if (!arg2) return 0;
    }
    struct Callback* callback = new Callback;
    callback->obj=jenv->NewGlobalRef(jarg3);
    callback->cls=jenv->GetObjectClass(jarg3);
    result = (int)tiledb_object_ls(arg1,(char const *)arg2,arg3,(void *) callback);
    jresult = (jint)result;
    if (arg2) jenv->ReleaseStringUTFChars(jarg2, (const char *)arg2);
    return jresult;
  }


#ifdef __cplusplus
}
#endif
%}

%include "tiledb_generated.h"
%include "tiledb_java_extensions.h"

%pragma(java) modulecode=%{
  
  public static int sizeOfType(Object array) {
    Class arrayClass = array.getClass();
    if (arrayClass.equals(int32_tArray.class)) {
      return sizeOfInt32();
    } else if (arrayClass.equals(int64_tArray.class)) {
      return sizeOfInt64();
    } else if (arrayClass.equals(charArray.class)) {
      return sizeOfChar();
    } else if (arrayClass.equals(floatArray.class)) {
      return sizeOfFloat();
    } else if (arrayClass.equals(doubleArray.class)) {
      return sizeOfDouble();
    } else if (arrayClass.equals(int8_tArray.class)) {
      return sizeOfInt8();
    } else if (arrayClass.equals(uint8_tArray.class)) {
      return sizeOfUint8();
    } else if (arrayClass.equals(int16_tArray.class)) {
      return sizeOfInt16();
    } else if (arrayClass.equals(uint16_tArray.class)) {
      return sizeOfUint16();
    } else if (arrayClass.equals(uint32_tArray.class)) {
      return sizeOfUint32();
    } else if (arrayClass.equals(uint64_tArray.class)) {
      return sizeOfUint64();
    }
    return -1;
  }
%}
