/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.libtiledb;

public class int32_tArray {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected int32_tArray(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(int32_tArray obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(int32_tArray obj) {
    long ptr = 0;
    if (obj != null) {
      if (!obj.swigCMemOwn)
        throw new RuntimeException("Cannot release ownership as memory is not owned");
      ptr = obj.swigCPtr;
      obj.swigCMemOwn = false;
      obj.delete();
    }
    return ptr;
  }

  @SuppressWarnings({"deprecation", "removal"})
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        tiledbJNI.delete_int32_tArray(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public int32_tArray(long nelements) {
    this(tiledbJNI.new_int32_tArray(nelements), true);
  }

  public int getitem(long index) {
    return tiledbJNI.int32_tArray_getitem(swigCPtr, this, index);
  }

  public void setitem(long index, int value) {
    tiledbJNI.int32_tArray_setitem(swigCPtr, this, index, value);
  }

  public SWIGTYPE_p_int cast() {
    long cPtr = tiledbJNI.int32_tArray_cast(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_int(cPtr, false);
  }

  public static int32_tArray frompointer(SWIGTYPE_p_int t) {
    long cPtr = tiledbJNI.int32_tArray_frompointer(SWIGTYPE_p_int.getCPtr(t));
    return (cPtr == 0) ? null : new int32_tArray(cPtr, false);
  }
}
