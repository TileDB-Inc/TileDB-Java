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

public class PointerUtils {

  public static SWIGTYPE_p_void toVoid(intArray p) {
    return new SWIGTYPE_p_void(intArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(int32_tArray p) {
    return new SWIGTYPE_p_void(int32_tArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(int64_tArray p) {
    return new SWIGTYPE_p_void(int64_tArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(charArray p) {
    return new SWIGTYPE_p_void(charArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(floatArray p) {
    return new SWIGTYPE_p_void(floatArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(doubleArray p) {
    return new SWIGTYPE_p_void(doubleArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(int8_tArray p) {
    return new SWIGTYPE_p_void(int8_tArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(uint8_tArray p) {
    return new SWIGTYPE_p_void(uint8_tArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(int16_tArray p) {
    return new SWIGTYPE_p_void(int16_tArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(uint16_tArray p) {
    return new SWIGTYPE_p_void(uint16_tArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(uint32_tArray p) {
    return new SWIGTYPE_p_void(uint32_tArray.getCPtr(p), false);
  }

  public static SWIGTYPE_p_void toVoid(uint64_tArray p) {
    return new SWIGTYPE_p_void(uint64_tArray.getCPtr(p), false);
  }


  public static intArray intFromVoid(SWIGTYPE_p_p_void p) {
    return new intArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static int32_tArray int32_tArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new int32_tArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static int64_tArray int64_tArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new int64_tArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static charArray charArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new charArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static floatArray floatArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new floatArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static doubleArray doubleArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new doubleArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static int8_tArray int8_tArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new int8_tArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static uint8_tArray uint8_tArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new uint8_tArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static int16_tArray int16_tArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new int16_tArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static uint16_tArray uint16_tArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new uint16_tArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static uint32_tArray uint32_tArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new uint32_tArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

  public static uint64_tArray uint64_tArrayFromVoid(SWIGTYPE_p_p_void p) {
    return new uint64_tArray(SWIGTYPE_p_void.getCPtr(tiledb.derefVoid(p)), false);
  }

}
