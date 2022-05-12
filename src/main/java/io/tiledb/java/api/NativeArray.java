/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 TileDB, Inc.
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

/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NativeArray implements AutoCloseable {
  private Class javaType;
  private Datatype nativeType;
  private int nativeTypeSize;
  private floatArray floatArray;
  private doubleArray doubleArray;
  private int8_tArray int8_tArray;
  private int16_tArray int16_tArray;
  private int32_tArray int32_tArray;
  private int64_tArray int64_tArray;
  private uint8_tArray uint8_tArray;
  private uint16_tArray uint16_tArray;
  private uint32_tArray uint32_tArray;
  private int64_tArray uint64_tArray;
  private int size;
  //  private charArray charArray;

  /**
   * Creates an empty native array using a native datatype and a provided size
   *
   * @param ctx A TileDB context
   * @param size Number of array elements
   * @param nativeType A TileDB datatype
   * @exception TileDBError A TileDB exception
   */
  public NativeArray(Context ctx, int size, Datatype nativeType) throws TileDBError {
    this.size = size;
    this.javaType = Types.getJavaType(nativeType);
    this.nativeType = nativeType;
    this.nativeTypeSize = tiledb.tiledb_datatype_size(nativeType.toSwigEnum()).intValue();
    allocateEmptyArray();
  }

  /**
   * Creates an empty native array using a java datatype and a provided size
   *
   * @param ctx A TileDB context
   * @param size Number of array elements
   * @param javaType A Java scalar type class
   * @exception TileDBError A TileDB exception
   */
  public NativeArray(Context ctx, int size, Class javaType) throws TileDBError {
    this.size = size;
    this.javaType = javaType;
    this.nativeType = Types.getNativeType(javaType);
    this.nativeTypeSize = tiledb.tiledb_datatype_size(this.nativeType.toSwigEnum()).intValue();
    allocateEmptyArray();
  }

  /**
   * Creates a native array using a java datatype. It takes as input a Java buffer (i.e long[],
   * int[]) and copies its values to the C native array.
   *
   * @param ctx A TileDB context
   * @param buffer A Java array
   * @param javaType A Java scalar type class
   * @exception TileDBError A TileDB exception
   */
  public NativeArray(Context ctx, Object buffer, Class javaType) throws TileDBError {
    this.javaType = javaType;
    this.nativeType = Types.getNativeType(javaType);
    this.nativeTypeSize = tiledb.tiledb_datatype_size(this.nativeType.toSwigEnum()).intValue();
    this.size = getSize(buffer);
    createNativeArrayFromBuffer(buffer);
  }

  /**
   * Creates a native array using a native datatype. It takes as input a Java buffer (i.e long[],
   * int[]) and copies its values to the C native array.
   *
   * @param ctx A TileDB context
   * @param buffer A Java array
   * @param nativeType A TileDB datatype
   * @param size The array size
   * @exception TileDBError A TileDB exception
   */
  public NativeArray(Context ctx, Object buffer, Datatype nativeType, int size) throws TileDBError {
    this.javaType = Types.getJavaType(nativeType);
    this.nativeType = nativeType;
    this.nativeTypeSize = tiledb.tiledb_datatype_size(this.nativeType.toSwigEnum()).intValue();
    this.size = size;
    createNativeArrayFromBuffer(buffer);
  }

  /**
   * Creates a native array using a native datatype. It takes as input a Java buffer (i.e long[],
   * int[]) and copies its values to the C native array.
   *
   * @param ctx A TileDB context
   * @param buffer A Java array
   * @param nativeType A TileDB datatype
   * @exception TileDBError A TileDB exception
   */
  public NativeArray(Context ctx, Object buffer, Datatype nativeType) throws TileDBError {
    this.javaType = Types.getJavaType(nativeType);
    this.nativeType = nativeType;
    this.nativeTypeSize = tiledb.tiledb_datatype_size(this.nativeType.toSwigEnum()).intValue();
    this.size = getSize(buffer);
    createNativeArrayFromBuffer(buffer);
  }

  protected NativeArray(Context ctx, Datatype nativeType, SWIGTYPE_p_p_void pointer, int nelements)
      throws TileDBError {
    this.javaType = Types.getJavaType(nativeType);
    this.nativeType = nativeType;
    this.nativeTypeSize = tiledb.tiledb_datatype_size(this.nativeType.toSwigEnum()).intValue();
    this.size = nelements;
    createNativeArrayFromVoidPointer(pointer);
  }

  protected NativeArray(Context ctx, Datatype nativeType, SWIGTYPE_p_void pointer, int nelements)
      throws TileDBError {
    this.javaType = Types.getJavaType(nativeType);
    this.nativeType = nativeType;
    this.nativeTypeSize = tiledb.tiledb_datatype_size(this.nativeType.toSwigEnum()).intValue();
    this.size = nelements;
    createNativeArrayFromVoidPointer(pointer);
  }

  private int getSize(Object buffer) throws TileDBError {
    switch (this.nativeType) {
      case TILEDB_FLOAT32:
        {
          return ((float[]) buffer).length;
        }
      case TILEDB_FLOAT64:
        {
          return ((double[]) buffer).length;
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          return ((byte[]) buffer).length;
        }
      case TILEDB_INT16:
        {
          return ((short[]) buffer).length;
        }
      case TILEDB_INT32:
        {
          return ((int[]) buffer).length;
        }
      case TILEDB_INT64:
        {
          return ((long[]) buffer).length;
        }
      case TILEDB_UINT8:
        {
          return ((short[]) buffer).length;
        }
      case TILEDB_UINT16:
        {
          return ((int[]) buffer).length;
        }
      case TILEDB_UINT32:
        {
          return ((long[]) buffer).length;
        }
      case TILEDB_UINT64:
        {
          return ((long[]) buffer).length;
        }
      case TILEDB_CHAR:
      case TILEDB_STRING_ASCII:
        {
          Charset charset = StandardCharsets.ISO_8859_1;
          return stringToBytes((String) buffer, charset).length;
        }
      case TILEDB_STRING_UTF8:
        {
          Charset charset = StandardCharsets.UTF_8;
          return stringToBytes((String) buffer, charset).length;
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_DATETIME_AS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          return ((long[]) buffer).length;
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  private void createNativeArrayFromBuffer(Object buffer) throws TileDBError {
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          floatArray = Utils.newFloatArray((float[]) buffer);
          break;
        }
      case TILEDB_FLOAT64:
        {
          doubleArray = Utils.newDoubleArray((double[]) buffer);
          break;
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          int8_tArray = Utils.newInt8_tArray((byte[]) buffer);
          break;
        }
      case TILEDB_INT16:
        {
          int16_tArray = Utils.newInt16_tArray((short[]) buffer);
          break;
        }
      case TILEDB_INT32:
        {
          int32_tArray = Utils.newInt32_tArray((int[]) buffer);
          break;
        }
      case TILEDB_INT64:
        {
          int64_tArray = Utils.newInt64_tArray((long[]) buffer);
          break;
        }
      case TILEDB_UINT8:
        {
          uint8_tArray = Utils.newUint8_tArray((short[]) buffer);
          break;
        }
      case TILEDB_UINT16:
        {
          uint16_tArray = Utils.newUint16_tArray((int[]) buffer);
          break;
        }
      case TILEDB_UINT32:
        {
          uint32_tArray = Utils.newUint32_tArray((long[]) buffer);
          break;
        }
      case TILEDB_UINT64:
        {
          uint64_tArray = Utils.newInt64_tArray((long[]) buffer);
          break;
        }
      case TILEDB_CHAR:
      case TILEDB_STRING_ASCII:
        {
          Charset charset = StandardCharsets.ISO_8859_1;
          int8_tArray = Utils.newInt8_tArray(stringToBytes((String) buffer, charset));
          break;
        }
      case TILEDB_STRING_UTF8:
        {
          Charset charset = StandardCharsets.UTF_8;
          int8_tArray = Utils.newInt8_tArray(stringToBytes((String) buffer, charset));
          break;
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_DATETIME_AS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          int64_tArray = Utils.newInt64_tArray((long[]) buffer);
          break;
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  private void allocateEmptyArray() throws TileDBError {
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          floatArray = new floatArray(size);
          break;
        }
      case TILEDB_FLOAT64:
        {
          doubleArray = new doubleArray(size);
          break;
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          int8_tArray = new int8_tArray(size);
          break;
        }
      case TILEDB_INT16:
        {
          int16_tArray = new int16_tArray(size);
          break;
        }
      case TILEDB_INT32:
        {
          int32_tArray = new int32_tArray(size);
          break;
        }
      case TILEDB_INT64:
        {
          int64_tArray = new int64_tArray(size);
          break;
        }
      case TILEDB_UINT8:
        {
          uint8_tArray = new uint8_tArray(size);
          break;
        }
      case TILEDB_UINT16:
        {
          uint16_tArray = new uint16_tArray(size);
          break;
        }
      case TILEDB_UINT32:
        {
          uint32_tArray = new uint32_tArray(size);
          break;
        }
      case TILEDB_UINT64:
        {
          uint64_tArray = new int64_tArray(size);
          break;
        }
      case TILEDB_STRING_ASCII:
      case TILEDB_CHAR:
        {
          int8_tArray = new int8_tArray(size);
          break;
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_DATETIME_AS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          int64_tArray = new int64_tArray(size);
          break;
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  /**
   * Returns the item on index position of the native array.
   *
   * @param index Index of array scalar to return
   * @return item A Java scalar
   * @exception TileDBError A TileDB exception
   */
  public Object getItem(int index) throws ArrayIndexOutOfBoundsException, TileDBError {
    if (index >= size || index < 0) {
      throw new ArrayIndexOutOfBoundsException(index);
    }
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          return floatArray.getitem(index);
        }
      case TILEDB_FLOAT64:
        {
          return doubleArray.getitem(index);
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          return int8_tArray.getitem(index);
        }
      case TILEDB_INT16:
        {
          return int16_tArray.getitem(index);
        }
      case TILEDB_INT32:
        {
          return int32_tArray.getitem(index);
        }
      case TILEDB_INT64:
        {
          return int64_tArray.getitem(index);
        }
      case TILEDB_UINT8:
        {
          return uint8_tArray.getitem(index);
        }
      case TILEDB_UINT16:
        {
          return uint16_tArray.getitem(index);
        }
      case TILEDB_UINT32:
        {
          return uint32_tArray.getitem(index);
        }
      case TILEDB_UINT64:
        {
          return uint64_tArray.getitem(index);
        }
      case TILEDB_STRING_ASCII:
      case TILEDB_CHAR:
        {
          return int8_tArray.getitem(index);
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_DATETIME_AS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          return int64_tArray.getitem(index);
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  /**
   * Sets the item on index position of the native array.
   *
   * @param index array index
   * @param value array value to set at index
   * @exception TileDBError A TileDB exception
   */
  public void setItem(int index, Object value) throws ArrayIndexOutOfBoundsException, TileDBError {
    if (index >= size || index < 0) {
      throw new ArrayIndexOutOfBoundsException(index);
    }
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          floatArray.setitem(index, (float) value);
          break;
        }
      case TILEDB_FLOAT64:
        {
          doubleArray.setitem(index, (double) value);
          break;
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          int8_tArray.setitem(index, (byte) value);
          break;
        }
      case TILEDB_INT16:
        {
          int16_tArray.setitem(index, (short) value);
          break;
        }
      case TILEDB_INT32:
        {
          int32_tArray.setitem(index, (int) value);
          break;
        }
      case TILEDB_INT64:
        {
          int64_tArray.setitem(index, (long) value);
          break;
        }
      case TILEDB_UINT8:
        {
          uint8_tArray.setitem(index, (short) value);
          break;
        }
      case TILEDB_UINT16:
        {
          uint16_tArray.setitem(index, (int) value);
          break;
        }
      case TILEDB_UINT32:
        {
          uint32_tArray.setitem(index, (long) value);
          break;
        }
      case TILEDB_UINT64:
        {
          uint64_tArray.setitem(index, (long) value);
          break;
        }
      case TILEDB_CHAR:
      case TILEDB_STRING_ASCII:
        {
          Charset charset = StandardCharsets.ISO_8859_1;
          for (byte b : stringToBytes((String) value, charset)) {
            int8_tArray.setitem(index, b);
            index++;
          }
          break;
        }
      case TILEDB_STRING_UTF8:
        {
          Charset charset = StandardCharsets.UTF_8;
          for (byte b : stringToBytes((String) value, charset)) {
            int8_tArray.setitem(index, b);
            index++;
          }
          break;
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_DATETIME_AS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          int64_tArray.setitem(index, (long) value);
          break;
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  public SWIGTYPE_p_void toVoidPointer() throws TileDBError {
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          return PointerUtils.toVoid(floatArray);
        }
      case TILEDB_FLOAT64:
        {
          return PointerUtils.toVoid(doubleArray);
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          return PointerUtils.toVoid(int8_tArray);
        }
      case TILEDB_INT16:
        {
          return PointerUtils.toVoid(int16_tArray);
        }
      case TILEDB_INT32:
        {
          return PointerUtils.toVoid(int32_tArray);
        }
      case TILEDB_INT64:
        {
          return PointerUtils.toVoid(int64_tArray);
        }
      case TILEDB_UINT8:
        {
          return PointerUtils.toVoid(uint8_tArray);
        }
      case TILEDB_UINT16:
        {
          return PointerUtils.toVoid(uint16_tArray);
        }
      case TILEDB_UINT32:
        {
          return PointerUtils.toVoid(uint32_tArray);
        }
      case TILEDB_UINT64:
        {
          return PointerUtils.toVoid(uint64_tArray);
        }
      case TILEDB_STRING_ASCII:
      case TILEDB_STRING_UTF8:
      case TILEDB_CHAR:
        {
          return PointerUtils.toVoid(int8_tArray);
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          return PointerUtils.toVoid(int64_tArray);
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  public long toCPointer() throws TileDBError {
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          return PointerUtils.toCPtr(floatArray);
        }
      case TILEDB_FLOAT64:
        {
          return PointerUtils.toCPtr(doubleArray);
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          return PointerUtils.toCPtr(int8_tArray);
        }
      case TILEDB_INT16:
        {
          return PointerUtils.toCPtr(int16_tArray);
        }
      case TILEDB_INT32:
        {
          return PointerUtils.toCPtr(int32_tArray);
        }
      case TILEDB_INT64:
        {
          return PointerUtils.toCPtr(int64_tArray);
        }
      case TILEDB_UINT8:
        {
          return PointerUtils.toCPtr(uint8_tArray);
        }
      case TILEDB_UINT16:
        {
          return PointerUtils.toCPtr(uint16_tArray);
        }
      case TILEDB_UINT32:
        {
          return PointerUtils.toCPtr(uint32_tArray);
        }
      case TILEDB_UINT64:
        {
          return PointerUtils.toCPtr(uint64_tArray);
        }
      case TILEDB_CHAR:
        {
          return PointerUtils.toCPtr(int8_tArray);
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          return PointerUtils.toCPtr(int64_tArray);
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  /**
   * Return a Java array (i.e. int[], long[]) that is a copy of the entire native array
   *
   * @return A java array
   * @throws TileDBError A TileDB exception
   */
  public Object toJavaArray() throws TileDBError {
    return toJavaArray(0, size);
  }

  /**
   * Return a Java array (i.e. int[], long[]) that is a copy of the native array values with the
   * given size, starting at position 0
   *
   * @param elements number of elements to return
   * @return A java array
   * @exception TileDBError A TileDB exception
   */
  public Object toJavaArray(int elements) throws TileDBError {
    return toJavaArray(0, elements);
  }

  /**
   * Return a Java array (i.e. int[], long[]) that is a copy of the native array values with the
   * given size, starting at given position
   *
   * @param position position to start copying from
   * @param elements number of elements to return
   * @return A java array
   * @exception TileDBError A TileDB exception
   */
  public Object toJavaArray(int position, int elements) throws TileDBError {
    if (position + elements > size || position < 0 || elements < 0) {
      throw new ArrayIndexOutOfBoundsException(position + elements);
    }
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          return Utils.floatArrayGet(floatArray, position, elements);
        }
      case TILEDB_FLOAT64:
        {
          return Utils.doubleArrayGet(doubleArray, position, elements);
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          return Utils.int8ArrayGet(int8_tArray, position, elements);
        }
      case TILEDB_INT16:
        {
          return Utils.int16ArrayGet(int16_tArray, position, elements);
        }
      case TILEDB_INT32:
        {
          return Utils.int32ArrayGet(int32_tArray, position, elements);
        }
      case TILEDB_INT64:
        {
          return Utils.int64ArrayGet(int64_tArray, position, elements);
        }
      case TILEDB_UINT8:
        {
          return Utils.uint8ArrayGet(uint8_tArray, position, elements);
        }
      case TILEDB_UINT16:
        {
          return Utils.uint16ArrayGet(uint16_tArray, position, elements);
        }
      case TILEDB_UINT32:
        {
          return Utils.uint32ArrayGet(uint32_tArray, position, elements);
        }
      case TILEDB_UINT64:
        {
          return Utils.int64ArrayGet(uint64_tArray, position, elements);
        }
      case TILEDB_STRING_ASCII:
      case TILEDB_STRING_UTF8:
      case TILEDB_CHAR:
        {
          return Utils.int8ArrayGet(int8_tArray, position, elements);
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          return Utils.int64ArrayGet(int64_tArray, position, elements);
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  private void createNativeArrayFromVoidPointer(SWIGTYPE_p_p_void pointer) throws TileDBError {
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          floatArray = PointerUtils.floatArrayFromVoid(pointer);
          break;
        }
      case TILEDB_FLOAT64:
        {
          doubleArray = PointerUtils.doubleArrayFromVoid(pointer);
          break;
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          int8_tArray = PointerUtils.int8_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_INT16:
        {
          int16_tArray = PointerUtils.int16_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_INT32:
        {
          int32_tArray = PointerUtils.int32_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_INT64:
        {
          int64_tArray = PointerUtils.int64_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_UINT8:
        {
          uint8_tArray = PointerUtils.uint8_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_UINT16:
        {
          uint16_tArray = PointerUtils.uint16_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_UINT32:
        {
          uint32_tArray = PointerUtils.uint32_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_UINT64:
        {
          uint64_tArray = PointerUtils.int64_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          int64_tArray = PointerUtils.int64_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_STRING_ASCII:
      case TILEDB_STRING_UTF8:
      case TILEDB_CHAR:
        {
          int8_tArray = PointerUtils.int8_tArrayFromVoid(pointer);
          break;
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  private void createNativeArrayFromVoidPointer(SWIGTYPE_p_void pointer) throws TileDBError {
    switch (nativeType) {
      case TILEDB_FLOAT32:
        {
          floatArray = PointerUtils.floatArrayFromVoid(pointer);
          break;
        }
      case TILEDB_FLOAT64:
        {
          doubleArray = PointerUtils.doubleArrayFromVoid(pointer);
          break;
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          int8_tArray = PointerUtils.int8_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_INT16:
        {
          int16_tArray = PointerUtils.int16_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_INT32:
        {
          int32_tArray = PointerUtils.int32_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_INT64:
        {
          int64_tArray = PointerUtils.int64_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_UINT8:
        {
          uint8_tArray = PointerUtils.uint8_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_UINT16:
        {
          uint16_tArray = PointerUtils.uint16_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_UINT32:
        {
          uint32_tArray = PointerUtils.uint32_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_UINT64:
        {
          uint64_tArray = PointerUtils.int64_tArrayFromVoid(pointer);
          break;
        }
      case TILEDB_DATETIME_YEAR:
      case TILEDB_DATETIME_MONTH:
      case TILEDB_DATETIME_WEEK:
      case TILEDB_DATETIME_DAY:
      case TILEDB_DATETIME_HR:
      case TILEDB_DATETIME_MIN:
      case TILEDB_DATETIME_SEC:
      case TILEDB_DATETIME_MS:
      case TILEDB_DATETIME_US:
      case TILEDB_DATETIME_NS:
      case TILEDB_DATETIME_PS:
      case TILEDB_DATETIME_FS:
      case TILEDB_TIME_HR:
      case TILEDB_TIME_MIN:
      case TILEDB_TIME_SEC:
      case TILEDB_TIME_MS:
      case TILEDB_TIME_US:
      case TILEDB_TIME_NS:
      case TILEDB_TIME_PS:
      case TILEDB_TIME_FS:
      case TILEDB_TIME_AS:
        {
          int64_tArray = PointerUtils.int64_tArrayFromVoid(pointer);
          break;
        }
      default:
        {
          throw new TileDBError("Unsupported TileDB NativeArray Datatype enum: " + this.nativeType);
        }
    }
  }

  private byte[] stringToBytes(String buffer, Charset charset) {
    return buffer.getBytes(charset);
  }

  protected Datatype getNativeType() {
    return nativeType;
  }

  public Class getJavaType() {
    return javaType;
  }

  public int getSize() {
    return size;
  }

  public int getNativeTypeSize() {
    return nativeTypeSize;
  }

  public long getNBytes() {
    return ((long) size) * nativeTypeSize;
  }

  /** Free's NativeArray off heap allocated resources */
  public void close() {
    if (floatArray != null) {
      floatArray.delete();
    }
    if (doubleArray != null) {
      doubleArray.delete();
    }
    if (int8_tArray != null) {
      int8_tArray.delete();
    }
    if (int16_tArray != null) {
      int16_tArray.delete();
    }
    if (int32_tArray != null) {
      int32_tArray.delete();
    }
    if (int64_tArray != null) {
      int64_tArray.delete();
    }
    if (uint8_tArray != null) {
      uint8_tArray.delete();
    }
    if (uint16_tArray != null) {
      uint16_tArray.delete();
    }
    if (uint32_tArray != null) {
      uint32_tArray.delete();
    }
    if (uint64_tArray != null) {
      uint64_tArray.delete();
    }
  }

  public io.tiledb.libtiledb.floatArray getFloatArray() {
    return floatArray;
  }

  public io.tiledb.libtiledb.doubleArray getDoubleArray() {
    return doubleArray;
  }

  public io.tiledb.libtiledb.int8_tArray getInt8_tArray() {
    return int8_tArray;
  }

  public io.tiledb.libtiledb.int16_tArray getInt16_tArray() {
    return int16_tArray;
  }

  public io.tiledb.libtiledb.int32_tArray getInt32_tArray() {
    return int32_tArray;
  }

  public io.tiledb.libtiledb.int64_tArray getInt64_tArray() {
    return int64_tArray;
  }

  public io.tiledb.libtiledb.uint8_tArray getUint8_tArray() {
    return uint8_tArray;
  }

  public io.tiledb.libtiledb.uint16_tArray getUint16_tArray() {
    return uint16_tArray;
  }

  public io.tiledb.libtiledb.uint32_tArray getUint32_tArray() {
    return uint32_tArray;
  }

  public io.tiledb.libtiledb.int64_tArray getUint64_tArray() {
    return uint64_tArray;
  }
}
