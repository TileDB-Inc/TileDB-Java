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
        return PointerUtils.toVoid(new charArray(size));
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

  public static SWIGTYPE_p_void createNativeArray(tiledb_datatype_t type, Object extent) throws TileDBError {
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
}
