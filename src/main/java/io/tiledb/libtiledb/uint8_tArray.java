/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.1.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.libtiledb;

public class uint8_tArray {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected uint8_tArray(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(uint8_tArray obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected static long swigRelease(uint8_tArray obj) {
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

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        tiledbJNI.delete_uint8_tArray(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public uint8_tArray(int nelements) {
    this(tiledbJNI.new_uint8_tArray(nelements), true);
  }

  public short getitem(int index) {
    return tiledbJNI.uint8_tArray_getitem(swigCPtr, this, index);
  }

  public void setitem(int index, short value) {
    tiledbJNI.uint8_tArray_setitem(swigCPtr, this, index, value);
  }

  public SWIGTYPE_p_unsigned_char cast() {
    long cPtr = tiledbJNI.uint8_tArray_cast(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_unsigned_char(cPtr, false);
  }

  public static uint8_tArray frompointer(SWIGTYPE_p_unsigned_char t) {
    long cPtr = tiledbJNI.uint8_tArray_frompointer(SWIGTYPE_p_unsigned_char.getCPtr(t));
    return (cPtr == 0) ? null : new uint8_tArray(cPtr, false);
  }
}
