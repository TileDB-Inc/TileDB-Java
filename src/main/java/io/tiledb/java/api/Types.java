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

package io.tiledb.java.api;

import static io.tiledb.java.api.Datatype.*;

public class Types {

  public static Datatype getNativeType(Class atrrType) throws TileDBError {
    if (atrrType.equals(int[].class)) {
      return TILEDB_INT32;
    } else if (atrrType.equals(long[].class)) {
      return TILEDB_INT64;
    } else if (atrrType.equals(char[].class)) {
      return TILEDB_CHAR;
    } else if (atrrType.equals(float[].class)) {
      return TILEDB_FLOAT32;
    } else if (atrrType.equals(double[].class)) {
      return TILEDB_FLOAT64;
    } else if (atrrType.equals(byte[].class)) {
      // conflict when byte refers to TILEDB_BLOB
      return TILEDB_INT8;
    } else if (atrrType.equals(short[].class)) {
      return TILEDB_INT16;
    } else if (atrrType.equals(boolean[].class)) {
      return TILEDB_INT8;
    } else if (atrrType.equals(Integer.class)) {
      return TILEDB_INT32;
    } else if (atrrType.equals(Long.class)) {
      return TILEDB_INT64;
    } else if (atrrType.equals(Character.class)) {
      return TILEDB_CHAR;
    } else if (atrrType.equals(String.class)) {
      return TILEDB_CHAR;
    } else if (atrrType.equals(Float.class)) {
      return TILEDB_FLOAT32;
    } else if (atrrType.equals(Double.class)) {
      return TILEDB_FLOAT64;
    } else if (atrrType.equals(Byte.class)) {
      return TILEDB_INT8;
    } else if (atrrType.equals(Short.class)) {
      return TILEDB_INT16;
    } else if (atrrType.equals(Boolean.class)) {
      return TILEDB_BOOL;
    } else {
      throw new TileDBError("Not supported getType: " + atrrType);
    }
  }

  public static Class getJavaType(Datatype type) throws TileDBError {
    switch (type) {
      case TILEDB_FLOAT32:
        {
          return Float.class;
        }
      case TILEDB_FLOAT64:
        {
          return Double.class;
        }
      case TILEDB_INT8:
      case TILEDB_BLOB:
        {
          return Byte.class;
        }
      case TILEDB_INT16:
        {
          return Short.class;
        }
      case TILEDB_INT32:
        {
          return Integer.class;
        }
      case TILEDB_INT64:
        {
          return Long.class;
        }
      case TILEDB_UINT8:
      case TILEDB_BOOL:
        {
          return Short.class;
        }
      case TILEDB_UINT16:
        {
          return Integer.class;
        }
      case TILEDB_UINT32:
        {
          return Long.class;
        }
      case TILEDB_UINT64:
        {
          return Long.class;
        }
      case TILEDB_STRING_UTF8:
      case TILEDB_STRING_ASCII:
      case TILEDB_CHAR:
        {
          return String.class;
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
          return Long.class;
        }
      default:
        {
          throw new TileDBError("Not supported getDomain getType " + type);
        }
    }
  }

  public static boolean typeCheck(Datatype first, Datatype second) throws TileDBError {
    if (!first.equals(second)) {
      throw new TileDBError("Type " + first + " is not equal to the default getType: " + second);
    }
    return true;
  }

  /**
   * Checks two class types are equal
   *
   * @param first class to compare
   * @param second class to compare
   * @return true if classes are equal else false
   * @throws TileDBError
   */
  public static boolean javaTypeCheck(Class first, Class second) throws TileDBError {
    if (!first.equals(second)) {
      throw new TileDBError(
          "Type " + first.getName() + " is not equal to the default getType: " + second.getName());
    }
    return true;
  }
}
