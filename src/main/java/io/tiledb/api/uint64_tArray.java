/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.api;

public class uint64_tArray {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected uint64_tArray(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(uint64_tArray obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        tiledbJNI.delete_uint64_tArray(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public uint64_tArray(int nelements) {
    this(tiledbJNI.new_uint64_tArray(nelements), true);
  }

  public java.math.BigInteger getitem(int index) {
    return tiledbJNI.uint64_tArray_getitem(swigCPtr, this, index);
  }

  public void setitem(int index, java.math.BigInteger value) {
    tiledbJNI.uint64_tArray_setitem(swigCPtr, this, index, value);
  }

  public SWIGTYPE_p_unsigned_long_long cast() {
    long cPtr = tiledbJNI.uint64_tArray_cast(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_long_long(cPtr, false);
  }

  public static uint64_tArray frompointer(SWIGTYPE_p_unsigned_long_long t) {
    long cPtr = tiledbJNI.uint64_tArray_frompointer(SWIGTYPE_p_unsigned_long_long.getCPtr(t));
    return (cPtr == 0) ? null : new uint64_tArray(cPtr, false);
  }

}
