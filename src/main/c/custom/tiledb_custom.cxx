
#ifndef SWIGJAVA
#define SWIGJAVA
#endif



#ifdef __cplusplus
/* SwigValueWrapper is described in swig.swg */
template<typename T> class SwigValueWrapper {
  struct SwigMovePointer {
    T *ptr;
    SwigMovePointer(T *p) : ptr(p) { }
    ~SwigMovePointer() { delete ptr; }
    SwigMovePointer& operator=(SwigMovePointer& rhs) { T* oldptr = ptr; ptr = 0; delete oldptr; ptr = rhs.ptr; rhs.ptr = 0; return *this; }
  } pointer;
  SwigValueWrapper& operator=(const SwigValueWrapper<T>& rhs);
  SwigValueWrapper(const SwigValueWrapper<T>& rhs);
public:
  SwigValueWrapper() : pointer(0) { }
  SwigValueWrapper& operator=(const T& t) { SwigMovePointer tmp(new T(t)); pointer = tmp; return *this; }
  operator T&() const { return *pointer.ptr; }
  T *operator&() { return pointer.ptr; }
};

template <typename T> T SwigValueInit() {
  return T();
}
#endif

/* -----------------------------------------------------------------------------
 *  This section contains generic SWIG labels for method/variable
 *  declarations/attributes, and other compiler dependent labels.
 * ----------------------------------------------------------------------------- */

/* template workaround for compilers that cannot correctly implement the C++ standard */
#ifndef SWIGTEMPLATEDISAMBIGUATOR
# if defined(__SUNPRO_CC) && (__SUNPRO_CC <= 0x560)
#  define SWIGTEMPLATEDISAMBIGUATOR template
# elif defined(__HP_aCC)
/* Needed even with `aCC -AA' when `aCC -V' reports HP ANSI C++ B3910B A.03.55 */
/* If we find a maximum version that requires this, the test would be __HP_aCC <= 35500 for A.03.55 */
#  define SWIGTEMPLATEDISAMBIGUATOR template
# else
#  define SWIGTEMPLATEDISAMBIGUATOR
# endif
#endif

/* inline attribute */
#ifndef SWIGINLINE
# if defined(__cplusplus) || (defined(__GNUC__) && !defined(__STRICT_ANSI__))
#   define SWIGINLINE inline
# else
#   define SWIGINLINE
# endif
#endif

/* attribute recognised by some compilers to avoid 'unused' warnings */
#ifndef SWIGUNUSED
# if defined(__GNUC__)
#   if !(defined(__cplusplus)) || (__GNUC__ > 3 || (__GNUC__ == 3 && __GNUC_MINOR__ >= 4))
#     define SWIGUNUSED __attribute__ ((__unused__))
#   else
#     define SWIGUNUSED
#   endif
# elif defined(__ICC)
#   define SWIGUNUSED __attribute__ ((__unused__))
# else
#   define SWIGUNUSED
# endif
#endif

#ifndef SWIG_MSC_UNSUPPRESS_4505
# if defined(_MSC_VER)
#   pragma warning(disable : 4505) /* unreferenced local function has been removed */
# endif
#endif

#ifndef SWIGUNUSEDPARM
# ifdef __cplusplus
#   define SWIGUNUSEDPARM(p)
# else
#   define SWIGUNUSEDPARM(p) p SWIGUNUSED
# endif
#endif

/* internal SWIG method */
#ifndef SWIGINTERN
# define SWIGINTERN static SWIGUNUSED
#endif

/* internal inline SWIG method */
#ifndef SWIGINTERNINLINE
# define SWIGINTERNINLINE SWIGINTERN SWIGINLINE
#endif

/* exporting methods */
#if defined(__GNUC__)
#  if (__GNUC__ >= 4) || (__GNUC__ == 3 && __GNUC_MINOR__ >= 4)
#    ifndef GCC_HASCLASSVISIBILITY
#      define GCC_HASCLASSVISIBILITY
#    endif
#  endif
#endif

#ifndef SWIGEXPORT
# if defined(_WIN32) || defined(__WIN32__) || defined(__CYGWIN__)
#   if defined(STATIC_LINKED)
#     define SWIGEXPORT
#   else
#     define SWIGEXPORT __declspec(dllexport)
#   endif
# else
#   if defined(__GNUC__) && defined(GCC_HASCLASSVISIBILITY)
#     define SWIGEXPORT __attribute__ ((visibility("default")))
#   else
#     define SWIGEXPORT
#   endif
# endif
#endif

/* calling conventions for Windows */
#ifndef SWIGSTDCALL
# if defined(_WIN32) || defined(__WIN32__) || defined(__CYGWIN__)
#   define SWIGSTDCALL __stdcall
# else
#   define SWIGSTDCALL
# endif
#endif

/* Deal with Microsoft's attempt at deprecating C standard runtime functions */
#if !defined(SWIG_NO_CRT_SECURE_NO_DEPRECATE) && defined(_MSC_VER) && !defined(_CRT_SECURE_NO_DEPRECATE)
# define _CRT_SECURE_NO_DEPRECATE
#endif

/* Deal with Microsoft's attempt at deprecating methods in the standard C++ library */
#if !defined(SWIG_NO_SCL_SECURE_NO_DEPRECATE) && defined(_MSC_VER) && !defined(_SCL_SECURE_NO_DEPRECATE)
# define _SCL_SECURE_NO_DEPRECATE
#endif

/* Deal with Apple's deprecated 'AssertMacros.h' from Carbon-framework */
#if defined(__APPLE__) && !defined(__ASSERT_MACROS_DEFINE_VERSIONS_WITHOUT_UNDERSCORES)
# define __ASSERT_MACROS_DEFINE_VERSIONS_WITHOUT_UNDERSCORES 0
#endif

/* Intel's compiler complains if a variable which was never initialised is
 * cast to void, which is a common idiom which we use to indicate that we
 * are aware a variable isn't used.  So we just silence that warning.
 * See: https://github.com/swig/swig/issues/192 for more discussion.
 */
#ifdef __INTEL_COMPILER
# pragma warning disable 592
#endif


/* Fix for jlong on some versions of gcc on Windows */
#if defined(__GNUC__) && !defined(__INTEL_COMPILER)
  typedef long long __int64;
#endif

/* Fix for jlong on 64-bit x86 Solaris */
#if defined(__x86_64)
# ifdef _LP64
#   undef _LP64
# endif
#endif

#include <jni.h>
#include <stdlib.h>
#include <string.h>


/* Support for throwing Java exceptions */
typedef enum {
  SWIG_JavaOutOfMemoryError = 1,
  SWIG_JavaIOException,
  SWIG_JavaRuntimeException,
  SWIG_JavaIndexOutOfBoundsException,
  SWIG_JavaArithmeticException,
  SWIG_JavaIllegalArgumentException,
  SWIG_JavaNullPointerException,
  SWIG_JavaDirectorPureVirtual,
  SWIG_JavaUnknownError
} SWIG_JavaExceptionCodes;

typedef struct {
  SWIG_JavaExceptionCodes code;
  const char *java_exception;
} SWIG_JavaExceptions_t;


static void SWIGUNUSED SWIG_JavaThrowException(JNIEnv *jenv, SWIG_JavaExceptionCodes code, const char *msg) {
  jclass excep;
  static const SWIG_JavaExceptions_t java_exceptions[] = {
    { SWIG_JavaOutOfMemoryError, "java/lang/OutOfMemoryError" },
    { SWIG_JavaIOException, "java/io/IOException" },
    { SWIG_JavaRuntimeException, "java/lang/RuntimeException" },
    { SWIG_JavaIndexOutOfBoundsException, "java/lang/IndexOutOfBoundsException" },
    { SWIG_JavaArithmeticException, "java/lang/ArithmeticException" },
    { SWIG_JavaIllegalArgumentException, "java/lang/IllegalArgumentException" },
    { SWIG_JavaNullPointerException, "java/lang/NullPointerException" },
    { SWIG_JavaDirectorPureVirtual, "java/lang/RuntimeException" },
    { SWIG_JavaUnknownError,  "java/lang/UnknownError" },
    { (SWIG_JavaExceptionCodes)0,  "java/lang/UnknownError" }
  };
  const SWIG_JavaExceptions_t *except_ptr = java_exceptions;

  while (except_ptr->code != code && except_ptr->code)
    except_ptr++;

  jenv->ExceptionClear();
  excep = jenv->FindClass(except_ptr->java_exception);
  if (excep)
    jenv->ThrowNew(excep, msg);
}


/* Contract support */

#define SWIG_contract_assert(nullreturn, expr, msg) if (!(expr)) {SWIG_JavaThrowException(jenv, SWIG_JavaIllegalArgumentException, msg); return nullreturn; } else


#include <stdio.h>
#include <typeinfo>
#include "tiledb/tiledb.h"


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

  JNIEXPORT jintArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_int32ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    int32_t *arr = *(int32_t **)&array;
    arr = arr + pos;
    jintArray jresult = jenv->NewIntArray(sz);
    if (!jresult)
      return NULL;
    jenv->SetIntArrayRegion(jresult, 0, sz, (jint*) arr);
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

  JNIEXPORT jlongArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_int64ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    int64_t *arr = *(int64_t **)&array;
    arr = arr + pos;
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

  JNIEXPORT jstring JNICALL Java_io_tiledb_libtiledb_tiledbJNI_charArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    char *arr = *(char **)&array;
    arr = arr + pos;
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

  JNIEXPORT jfloatArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_floatArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    float *arr = *(float **)&array;
    arr = arr + pos;
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

  JNIEXPORT jdoubleArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_doubleArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    double *arr = *(double **)&array;
    arr = arr + pos;
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


  JNIEXPORT jbyteArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_int8ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    int8_t *arr = *(int8_t **)&array;
    arr = arr + pos;
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

  JNIEXPORT jshortArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_uint8ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    uint8_t *arr = *(uint8_t **)&array;
    arr = arr + pos;
    jshortArray jresult = jenv->NewShortArray(sz);
    if (!jresult)
      return NULL;
    jshort * short_arr = new jshort[sz];
    for(int i = 0; i<sz; i++){
      short_arr[i] = (jshort)arr[i];
    }
    jenv->SetShortArrayRegion(jresult, 0, sz, short_arr);
    delete short_arr;
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

  JNIEXPORT jshortArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_int16ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    int16_t *arr = *(int16_t **)&array;
    arr = arr + pos;
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

  JNIEXPORT jintArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_uint16ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    uint16_t *arr = *(uint16_t **)&array;
    arr = arr + pos;
    jintArray jresult = jenv->NewIntArray(sz);
    if (!jresult)
      return NULL;
    jint * int_arr = new jint[sz];
    for(int i = 0; i<sz; i++){
      int_arr[i] = (jint)arr[i];
    }
    jenv->SetIntArrayRegion(jresult, 0, sz, int_arr);
    delete int_arr;
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

  JNIEXPORT jlongArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_uint32ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    uint32_t *arr = *(uint32_t **)&array;
    arr = arr + pos;
    jlongArray jresult = jenv->NewLongArray(sz);
    if (!jresult)
      return NULL;
    jlong * long_arr = new jlong[sz];
    for(int i = 0; i<sz; i++){
      long_arr[i] = (jlong)arr[i];
    }
    jenv->SetLongArrayRegion(jresult, 0, sz, long_arr);
    delete long_arr;
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

  JNIEXPORT jlongArray JNICALL Java_io_tiledb_libtiledb_tiledbJNI_uint64ArrayGet(JNIEnv *jenv, jobject obj, jlong array, jint pos, jint sz) {
    uint64_t *arr = *(uint64_t **)&array;
    arr = arr + pos;
    jlongArray jresult = jenv->NewLongArray(sz);
    if (!jresult)
      return NULL;
    jlong * long_arr = new jlong[sz];
    for(int i = 0; i<sz; i++){
      long_arr[i] = (jlong)arr[i];
    }
    jenv->SetLongArrayRegion(jresult, 0, sz, long_arr);
    delete long_arr;
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

  SWIGEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_tiledb_1query_1set_1subarray_1nio(JNIEnv *jenv, jclass jcls, jlong jarg1, jlong jarg2, jobject jarg3) {
    void* buffer = (void *)jenv->GetDirectBufferAddress(jarg3);

    jint jresult = 0 ;
    tiledb_ctx_t *arg1 = (tiledb_ctx_t *) 0 ;
    tiledb_query_t *arg2 = (tiledb_query_t *) 0 ;
    void *arg3 = (void *) 0 ;
    int32_t result;

    (void)jenv;
    (void)jcls;
    arg1 = *(tiledb_ctx_t **)&jarg1;
    arg2 = *(tiledb_query_t **)&jarg2;
    arg3 = *(void **)&jarg3;
    result = (int32_t)tiledb_query_set_subarray(arg1,arg2,buffer);
    jresult = (jint)result;
    return jresult;
  }

  SWIGEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_tiledb_1query_1set_1data_1buffer_1nio(JNIEnv *jenv, jclass jcls, jlong jarg1, jlong jarg2, jstring jarg3, jobject jarg4, jlong jarg5) {
    void* buffer = (void *)jenv->GetDirectBufferAddress(jarg4);

    jint jresult = 0 ;
    tiledb_ctx_t *arg1 = (tiledb_ctx_t *) 0 ;
    tiledb_query_t *arg2 = (tiledb_query_t *) 0 ;
    char *arg3 = (char *) 0 ;
    void *arg4 = (void *) 0 ;
    uint64_t *arg5 = (uint64_t *) 0 ;
    int32_t result;

    (void)jenv;
    (void)jcls;
    arg1 = *(tiledb_ctx_t **)&jarg1;
    arg2 = *(tiledb_query_t **)&jarg2;
    arg3 = 0;
    if (jarg3) {
      arg3 = (char *)jenv->GetStringUTFChars(jarg3, 0);
      if (!arg3) return 0;
    }
    arg4 = *(void **)&jarg4;
    arg5 = *(uint64_t **)&jarg5;
    result = (int32_t)tiledb_query_set_data_buffer(arg1,arg2,(char const *)arg3, buffer,arg5);
    jresult = (jint)result;
    if (arg3) jenv->ReleaseStringUTFChars(jarg3, (const char *)arg3);
    return jresult;
  }

  SWIGEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_tiledb_1query_1set_1offsets_1buffer_1nio(JNIEnv *jenv, jclass jcls, jlong jarg1, jlong jarg2, jstring jarg3, jobject jarg4, jlong jarg5) {
    void* buffer = (void *)jenv->GetDirectBufferAddress(jarg4);

    jint jresult = 0 ;
    tiledb_ctx_t *arg1 = (tiledb_ctx_t *) 0 ;
    tiledb_query_t *arg2 = (tiledb_query_t *) 0 ;
    char *arg3 = (char *) 0 ;
    uint64_t *arg4 = (uint64_t *) 0 ;
    uint64_t *arg5 = (uint64_t *) 0 ;
    int32_t result;

    (void)jenv;
    (void)jcls;
    arg1 = *(tiledb_ctx_t **)&jarg1;
    arg2 = *(tiledb_query_t **)&jarg2;
    arg3 = 0;
    if (jarg3) {
      arg3 = (char *)jenv->GetStringUTFChars(jarg3, 0);
      if (!arg3) return 0;
    }
    arg4 = *(uint64_t **)&jarg4;
    arg5 = *(uint64_t **)&jarg5;
    result = (int32_t)tiledb_query_set_offsets_buffer(arg1,arg2,(char const *)arg3,(uint64_t *)buffer,arg5);
    jresult = (jint)result;
    if (arg3) jenv->ReleaseStringUTFChars(jarg3, (const char *)arg3);
    return jresult;
  }

  SWIGEXPORT jint JNICALL Java_io_tiledb_libtiledb_tiledbJNI_tiledb_1query_1set_1validity_1buffer_1nio(JNIEnv *jenv, jclass jcls, jlong jarg1, jlong jarg2, jstring jarg3, jobject jarg4, jlong jarg5) {
    void* buffer = (void *)jenv->GetDirectBufferAddress(jarg4);

    jint jresult = 0 ;
    tiledb_ctx_t *arg1 = (tiledb_ctx_t *) 0 ;
    tiledb_query_t *arg2 = (tiledb_query_t *) 0 ;
    char *arg3 = (char *) 0 ;
    uint8_t *arg4 = (uint8_t *) 0 ;
    uint64_t *arg5 = (uint64_t *) 0 ;
    int32_t result;

    (void)jenv;
    (void)jcls;
    arg1 = *(tiledb_ctx_t **)&jarg1;
    arg2 = *(tiledb_query_t **)&jarg2;
    arg3 = 0;
    if (jarg3) {
      arg3 = (char *)jenv->GetStringUTFChars(jarg3, 0);
      if (!arg3) return 0;
    }
    arg4 = *(uint8_t **)&jarg4;
    arg5 = *(uint64_t **)&jarg5;
    result = (int32_t)tiledb_query_set_validity_buffer(arg1,arg2,(char const *)arg3,arg4,arg5);
    jresult = (jint)result;
    if (arg3) jenv->ReleaseStringUTFChars(jarg3, (const char *)arg3);
    return jresult;
  }
#ifdef __cplusplus
}
#endif
