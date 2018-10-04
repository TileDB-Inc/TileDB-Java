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

package io.tiledb.jni;

import io.tiledb.libtiledb.*;
import org.junit.Assert;
import org.junit.Test;

public class ArrayTest {

  @Test
  public void testInt32() {
    int[] a_ = {Integer.MIN_VALUE, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, Integer.MAX_VALUE};
    int32_tArray a1 = Utils.newInt32_tArray(a_);
    int[] res = Utils.int32ArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res);
  }

  @Test
  public void testInt64() {
    long[] a_ = {Long.MIN_VALUE, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, Long.MAX_VALUE};
    int64_tArray a1 = Utils.newInt64_tArray(a_);
    long[] res = Utils.int64ArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res);
  }

  @Test
  public void testchar() {
    String a_ = "abcdefg" + "αβγδεζη";
    charArray a1 = Utils.newCharArray(a_);
    String res = Utils.charArrayGet(a1, 0);
    Assert.assertEquals(a_, res);
  }

  @Test
  public void testfloat() {
    float[] a_ = {
      Float.MIN_VALUE,
      (float) -5.1,
      (float) -4.1,
      (float) -3.1,
      (float) -2.1,
      (float) -1.1,
      (float) 0.1,
      (float) 1.1,
      (float) 2.1,
      (float) 3.1,
      (float) 4.1,
      Float.MAX_VALUE
    };
    floatArray a1 = Utils.newFloatArray(a_);
    float[] res = Utils.floatArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res, (float) 0.0001);
  }

  @Test
  public void testdouble() {
    double[] a_ = {
      Double.MIN_VALUE, -5.1, -4.1, -3.1, -2.1, -1.1, 0.1, 1.1, 2.1, 3.1, 4.1, Double.MAX_VALUE
    };
    doubleArray a1 = Utils.newDoubleArray(a_);
    double[] res = Utils.doubleArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res, 0.0001);
  }

  @Test
  public void testint8() {
    byte[] a_ = {Byte.MIN_VALUE, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, Byte.MAX_VALUE};
    int8_tArray a1 = Utils.newInt8_tArray(a_);
    byte[] res = Utils.int8ArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res);
  }

  @Test
  public void testuint8() {
    short[] a_ = {0, 1, 2, 3, 4, 128, 255};
    uint8_tArray a1 = Utils.newUint8_tArray(a_);
    short[] res = Utils.uint8ArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res);
  }

  @Test
  public void testint16() {
    short[] a_ = {Short.MIN_VALUE, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, Short.MAX_VALUE};
    int16_tArray a1 = Utils.newInt16_tArray(a_);
    short[] res = Utils.int16ArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res);
  }

  @Test
  public void testuint16() {
    int[] a_ = {0, 1, 2, 3, 4, 128, Short.MAX_VALUE, 2 * Short.MAX_VALUE};
    uint16_tArray a1 = Utils.newUint16_tArray(a_);
    int[] res = Utils.uint16ArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res);
  }

  @Test
  public void testuint32() {
    long[] a_ = {0, 1, 2, 3, 4, 128, Integer.MAX_VALUE, (long) 2 * (long) Integer.MAX_VALUE};
    uint32_tArray a1 = Utils.newUint32_tArray(a_);
    long[] res = Utils.uint32ArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res);
  }

  @Test
  public void testuint64() {
    long[] a_ = {0, 1, 2, 3, 4, 128, Long.MAX_VALUE};
    uint64_tArray a1 = Utils.newUint64Array(a_);
    long[] res = Utils.uint64ArrayGet(a1, 0, a_.length);
    Assert.assertArrayEquals(a_, res);
  }
}
