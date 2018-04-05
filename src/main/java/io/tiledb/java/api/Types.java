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

package io.tiledb.java.api;

import io.tiledb.api.*;

import java.math.BigInteger;

public class Types {

  public static tiledb_datatype_t getNativeType(Class atrrType) throws TileDBError {
    if(atrrType.equals(int[].class)) {
      return tiledb_datatype_t.TILEDB_INT32;
    } else if(atrrType.equals(long[].class)) {
      return tiledb_datatype_t.TILEDB_INT64;
    } else if(atrrType.equals(char[].class)) {
      return tiledb_datatype_t.TILEDB_CHAR;
    } else if(atrrType.equals(float[].class)) {
      return tiledb_datatype_t.TILEDB_FLOAT32;
    } else if(atrrType.equals(double[].class)) {
      return tiledb_datatype_t.TILEDB_FLOAT64;
    } else if(atrrType.equals(byte[].class)) {
      return tiledb_datatype_t.TILEDB_INT8;
    } else if(atrrType.equals(short[].class)) {
      return tiledb_datatype_t.TILEDB_INT16;
    } else if(atrrType.equals(boolean[].class)) {
      return tiledb_datatype_t.TILEDB_INT8;
    } else if(atrrType.equals(BigInteger[].class)) {
      return tiledb_datatype_t.TILEDB_UINT64;
    } else if(atrrType.equals(Integer.class)) {
      return tiledb_datatype_t.TILEDB_INT32;
    } else if(atrrType.equals(Long.class)) {
      return tiledb_datatype_t.TILEDB_INT64;
    } else if(atrrType.equals(Character.class)) {
      return tiledb_datatype_t.TILEDB_CHAR;
    } else if(atrrType.equals(Float.class)) {
      return tiledb_datatype_t.TILEDB_FLOAT32;
    } else if(atrrType.equals(Double.class)) {
      return tiledb_datatype_t.TILEDB_FLOAT64;
    } else if(atrrType.equals(Byte.class)) {
      return tiledb_datatype_t.TILEDB_INT8;
    } else if(atrrType.equals(Short.class)) {
      return tiledb_datatype_t.TILEDB_INT16;
    } else if(atrrType.equals(Boolean.class)) {
      return tiledb_datatype_t.TILEDB_INT8;
    } else if(atrrType.equals(BigInteger.class)) {
      return tiledb_datatype_t.TILEDB_UINT64;
    } else {
      return tiledb_datatype_t.TILEDB_INT8;
//      throw new TileDBError("Not supported type: "+atrrType);
    }
  }

  public static Class getJavaType(tiledb_datatype_t type) throws TileDBError {
    switch(type) {
      case TILEDB_FLOAT32: {
        return Float.class;
      }
      case TILEDB_FLOAT64: {
        return Double.class;
      }
      case TILEDB_INT8: {
        return Byte.class;
      }
      case TILEDB_INT16: {
        return Short.class;
      }
      case TILEDB_INT32: {
        return Integer.class;
      }
      case TILEDB_INT64: {
        return Long.class;
      }
      case TILEDB_UINT8: {
        return Short.class;
      }
      case TILEDB_UINT16: {
        return Integer.class;
      }
      case TILEDB_UINT32: {
        return Long.class;
      }
      case TILEDB_UINT64: {
        return Long.class;
      }
      case TILEDB_CHAR: {
        return Character.class;
      }
      default: {
        throw new TileDBError("Not supported domain type " + type);
      }
    }
  }


  public static Class getJavaPrimitiveArrayType(tiledb_datatype_t type) throws TileDBError {
    switch(type) {
      case TILEDB_FLOAT32: {
        return float[].class;
      }
      case TILEDB_FLOAT64: {
        return double[].class;
      }
      case TILEDB_INT8: {
        return byte[].class;
      }
      case TILEDB_INT16: {
        return short[].class;
      }
      case TILEDB_INT32: {
        return int[].class;
      }
      case TILEDB_INT64: {
        return long[].class;
      }
      case TILEDB_UINT8: {
        return short[].class;
      }
      case TILEDB_UINT16: {
        return int[].class;
      }
      case TILEDB_UINT32: {
        return long[].class;
      }
      case TILEDB_UINT64: {
        return long[].class;
      }
      case TILEDB_CHAR: {
        return byte[].class;
      }
      default: {
        throw new TileDBError("Not supported domain type " + type);
      }
    }
  }

  public static SWIGTYPE_p_void createEmptyNativeArray(tiledb_datatype_t type, int size) throws TileDBError {
    switch(type){
      case TILEDB_FLOAT32:{
        return PointerUtils.toVoid(new floatArray(size));
      }
      case TILEDB_FLOAT64:{
        return PointerUtils.toVoid(new doubleArray(size));
      }
      case TILEDB_INT8:{
        return PointerUtils.toVoid(new int8_tArray(size));
      }
      case TILEDB_INT16:{
        return PointerUtils.toVoid(new int16_tArray(size));
      }
      case TILEDB_INT32:{
        return PointerUtils.toVoid(new int32_tArray(size));
      }
      case TILEDB_INT64:{
        return PointerUtils.toVoid(new int64_tArray(size));
      }
      case TILEDB_UINT8:{
        return PointerUtils.toVoid(new uint8_tArray(size));
      }
      case TILEDB_UINT16:{
        return PointerUtils.toVoid(new uint16_tArray(size));
      }
      case TILEDB_UINT32: {
        return PointerUtils.toVoid(new uint32_tArray(size));
      }
      case TILEDB_UINT64: {
        return PointerUtils.toVoid(new uint64_tArray(size));
      }
      case TILEDB_CHAR: {
        return PointerUtils.toVoid(new uint8_tArray(size));
      }
      default:{
        throw new TileDBError("Not supported domain type "+type);
      }
    }
  }

  public static SWIGTYPE_p_void createNativeArrayPair(tiledb_datatype_t type, Pair d) throws TileDBError {
    switch(type){
      case TILEDB_FLOAT32:{
        float[] domain_ = {(Float) d.getFirst(), (Float) d.getSecond()};
        floatArray domain = Utils.newFloatArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_FLOAT64:{
        double[] domain_ = {(Double) d.getFirst(), (Double) d.getSecond()};
        doubleArray domain = Utils.newDoubleArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT8:{
        byte[] domain_ = {(Byte) d.getFirst(), (Byte) d.getSecond()};
        int8_tArray domain = Utils.newInt8_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT16:{
        short[] domain_ = {(Short) d.getFirst(), (Short) d.getSecond()};
        int16_tArray domain = Utils.newInt16_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT32:{
        int[] domain_ = {(Integer) d.getFirst(), (Integer) d.getSecond()};
        int32_tArray domain = Utils.newInt32_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT64:{
        long[] domain_ = {(Long) d.getFirst(), (Long) d.getSecond()};
        int64_tArray domain = Utils.newInt64_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT8:{
        short[] domain_ = {(Short) d.getFirst(), (Short) d.getSecond()};
        uint8_tArray domain = Utils.newUint8_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT16:{
        int[] domain_ = {(Integer) d.getFirst(), (Integer) d.getSecond()};
        uint16_tArray domain = Utils.newUint16_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT32: {
        long[] domain_ = {(Long) d.getFirst(), (Long) d.getSecond()};
        uint32_tArray domain = Utils.newUint32_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT64: {
        long[] domain_ = {(Long) d.getFirst(), (Long) d.getSecond()};
        uint64_tArray domain = Utils.newUint64Array(domain_);
        return PointerUtils.toVoid(domain);
      }
      default:{
        throw new TileDBError("Not supported domain type "+type);
      }
    }
  }

  public static Object createNativeArrayObject(tiledb_datatype_t type, Object primitiveTypeArray) throws TileDBError {
    switch(type){
      case TILEDB_FLOAT32:{
        return Utils.newFloatArray((float[])primitiveTypeArray);
      }
      case TILEDB_FLOAT64:{
        return Utils.newDoubleArray((double[])primitiveTypeArray);
      }
      case TILEDB_INT8:{
        return Utils.newInt8_tArray((byte[])primitiveTypeArray);
      }
      case TILEDB_INT16:{
        return Utils.newInt16_tArray((short[])primitiveTypeArray);
      }
      case TILEDB_INT32:{
        return Utils.newInt32_tArray((int[])primitiveTypeArray);
      }
      case TILEDB_INT64:{
        return Utils.newInt64_tArray((long[])primitiveTypeArray);
      }
      case TILEDB_UINT8:{
        return Utils.newUint8_tArray((short[])primitiveTypeArray);
      }
      case TILEDB_UINT16:{
        return Utils.newUint16_tArray((int[])primitiveTypeArray);
      }
      case TILEDB_UINT32: {
        return Utils.newUint32_tArray((long[])primitiveTypeArray);
      }
      case TILEDB_UINT64: {
        return Utils.newUint64Array((long[])primitiveTypeArray);
      }
      case TILEDB_CHAR: {
        return Utils.newInt8_tArray((byte[]) primitiveTypeArray);
      }
      default:{
        throw new TileDBError("Not supported domain type "+type);
      }
    }
  }

  public static SWIGTYPE_p_void createNativeArray(tiledb_datatype_t type, Object primitiveTypeArray) throws TileDBError {
    switch(type){
      case TILEDB_FLOAT32:{
        floatArray domain = Utils.newFloatArray((float[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_FLOAT64:{
        doubleArray domain = Utils.newDoubleArray((double[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT8:{
        int8_tArray domain = Utils.newInt8_tArray((byte[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT16:{
        int16_tArray domain = Utils.newInt16_tArray((short[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT32:{
        int32_tArray domain = Utils.newInt32_tArray((int[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT64:{
        int64_tArray domain = Utils.newInt64_tArray((long[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT8:{
        uint8_tArray domain = Utils.newUint8_tArray((short[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT16:{
        uint16_tArray domain = Utils.newUint16_tArray((int[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT32: {
        uint32_tArray domain = Utils.newUint32_tArray((long[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT64: {
        uint64_tArray domain = Utils.newUint64Array((long[])primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_CHAR: {
        int8_tArray domain = Utils.newInt8_tArray((byte[]) primitiveTypeArray);
        return PointerUtils.toVoid(domain);
      }
      default:{
        throw new TileDBError("Not supported domain type "+type);
      }
    }
  }

  public static SWIGTYPE_p_void createNativeArrayExtent(tiledb_datatype_t type, Object extent) throws TileDBError {
    switch(type){
      case TILEDB_FLOAT32:{
        float[] domain_ = {(Float) extent};
        floatArray domain = Utils.newFloatArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_FLOAT64:{
        double[] domain_ = {(Double) extent};
        doubleArray domain = Utils.newDoubleArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT8:{
        byte[] domain_ = {(Byte) extent};
        int8_tArray domain = Utils.newInt8_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT16:{
        short[] domain_ = {(Short) extent};
        int16_tArray domain = Utils.newInt16_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT32:{
        int[] domain_ = {(Integer) extent};
        int32_tArray domain = Utils.newInt32_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_INT64:{
        long[] domain_ = {(Long) extent};
        int64_tArray domain = Utils.newInt64_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT8:{
        short[] domain_ = {(Short) extent};
        uint8_tArray domain = Utils.newUint8_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT16:{
        int[] domain_ = {(Integer) extent};
        uint16_tArray domain = Utils.newUint16_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT32: {
        long[] domain_ = {(Long) extent};
        uint32_tArray domain = Utils.newUint32_tArray(domain_);
        return PointerUtils.toVoid(domain);
      }
      case TILEDB_UINT64: {
        long[] domain_ = {(Long) extent};
        uint64_tArray domain = Utils.newUint64Array(domain_);
        return PointerUtils.toVoid(domain);
      }
      default:{
        throw new TileDBError("Not supported domain type "+type);
      }
    }
  }

  public static Pair getPairFromNativeArray(tiledb_datatype_t type, SWIGTYPE_p_p_void nativeArray) throws TileDBError {
    switch (type) {
      case TILEDB_FLOAT32: {
        floatArray array = PointerUtils.floatArrayFromVoid(nativeArray);
        Pair ret = new Pair<Float, Float>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_FLOAT64: {
        doubleArray array = PointerUtils.doubleArrayFromVoid(nativeArray);
        Pair ret = new Pair<Double, Double>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_INT8: {
        int8_tArray array = PointerUtils.int8_tArrayFromVoid(nativeArray);
        Pair ret = new Pair<Byte, Byte>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_INT16: {
        int16_tArray array = PointerUtils.int16_tArrayFromVoid(nativeArray);
        Pair ret = new Pair<Short, Short>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_INT32: {
        int32_tArray array = PointerUtils.int32_tArrayFromVoid(nativeArray);
        Pair ret = new Pair<Integer, Integer>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_INT64: {
        int64_tArray array = PointerUtils.int64_tArrayFromVoid(nativeArray);
        Pair ret = new Pair<Long, Long>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_UINT8: {
        uint8_tArray array = PointerUtils.uint8_tArrayFromVoid(nativeArray);
        Pair ret = new Pair<Short, Short>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_UINT16: {
        uint16_tArray array = PointerUtils.uint16_tArrayFromVoid(nativeArray);
        Pair ret = new Pair<Integer, Integer>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_UINT32: {
        uint32_tArray array = PointerUtils.uint32_tArrayFromVoid(nativeArray);
        Pair ret = new Pair<Long, Long>(array.getitem(0), array.getitem(1));
        return ret;
      }
      case TILEDB_UINT64: {
        uint64_tArray array = PointerUtils.uint64_tArrayFromVoid(nativeArray);
        Pair ret = new Pair<BigInteger, BigInteger>(array.getitem(0), array.getitem(1));
        return ret;
      }
      default: {
        throw new TileDBError("Not supported domain type " + type);
      }
    }
  }

  public static Object toJavaArray(SWIGTYPE_p_void pointer, tiledb_datatype_t type, int size) throws TileDBError {
    switch (type) {
      case TILEDB_FLOAT32: {
        floatArray array = new floatArray(SWIGTYPE_p_void.getCPtr(pointer), true);
        return Utils.floatArrayGet(array,size);
      }
      case TILEDB_INT32: {
        int32_tArray array = new int32_tArray(SWIGTYPE_p_void.getCPtr(pointer), true);
        return Utils.int32ArrayGet(array,size);
      }
      case TILEDB_UINT32: {
        uint32_tArray array = new uint32_tArray(SWIGTYPE_p_void.getCPtr(pointer), true);
        return Utils.uint32ArrayGet(array,size);
      }
      case TILEDB_UINT64: {
        uint64_tArray array = new uint64_tArray(SWIGTYPE_p_void.getCPtr(pointer), true);
        return Utils.uint64ArrayGet(array,size);
      }
      case TILEDB_CHAR: {
        int8_tArray array = new int8_tArray(SWIGTYPE_p_void.getCPtr(pointer), true);
        return Utils.int8ArrayGet(array,size);
      }
      default: {
        throw new TileDBError("Not supported domain type " + type);
      }
    }
  }

  public static boolean typeCheck(Object obj, tiledb_datatype_t type) throws TileDBError {
    if(getJavaType(type).equals(obj.getClass())){
      return true;
    }
    else {
      throw new TileDBError("Type " + obj.getClass() +" is not equal to the default type: "+getJavaType(type) +" for "+type);
    }
  }

  public static boolean typeCheckArray(Object array, tiledb_datatype_t type) throws TileDBError {
    if(getJavaPrimitiveArrayType(type).equals(array.getClass())){
      return true;
    }
    else {
      throw new TileDBError("Type " + array.getClass() +" is not equal to the default type: "+getJavaPrimitiveArrayType(type) +" for " +type);
    }
  }

  public static int getArraySize(Object buf) {
    return ((Object[]) buf).length;
  }

}
