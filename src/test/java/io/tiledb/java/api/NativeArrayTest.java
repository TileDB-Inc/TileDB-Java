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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NativeArrayTest {

  private Context ctx;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
  }

  @Test
  public void testArrayExists() throws Exception {
    NativeArray testArray = new NativeArray(ctx, 1, Long.class);
    Assert.assertNotNull(testArray);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetItemByteIndexOutOfBoundsExceptionNegativeIndex() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(-1, (byte) 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetItemByteIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(1, (byte) 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetItemShortIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Short.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(1, (short) 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetItemIntegerIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Integer.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(1, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetItemLongIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Long.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(1, 0L);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetItemFloatIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Float.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(1, 0f);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetItemDoubleIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Double.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(1, 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetItemStringIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, String.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(1, "1");
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayGetItemByteIndexOutOfBoundsExceptionNegativeIndex() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayGetItemByteIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayGetItemShortIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Short.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayGetItemIntegerIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Integer.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayGetItemLongIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Long.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayGetItemFloatIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Float.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayGetItemDoubleIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Double.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayGetItemStringIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, String.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayByteIndexOutOfBoundsExceptionNegativeIndex() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(-1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayByteIndexOutOfBoundsExceptionNegativeSize() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(1, -1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayByteIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayShortIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Short.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayIntegerIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Integer.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayLongIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Long.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayFloatIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Float.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayDoubleIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, Double.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayToJavaArrayCharIndexOutOfBoundsException() throws TileDBError {
    NativeArray testArray = new NativeArray(ctx, 1, String.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(1, 1);
  }

  @Test
  public void testArrayBufferExists() throws Exception {
    long[] buffer = {1L, 2L, 3L};
    NativeArray testArray = new NativeArray(ctx, buffer, Long.class);
    Assert.assertNotNull(testArray);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferSetItemByteIndexOutOfBoundsExceptionNegativeIndex()
      throws TileDBError {
    byte[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(-1, (byte) 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferSetItemByteIndexOutOfBoundsException() throws TileDBError {
    byte[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(3, (byte) 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArraySetBufferItemShortIndexOutOfBoundsException() throws TileDBError {
    short[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Short.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(3, (short) 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferSetItemIntegerIndexOutOfBoundsException() throws TileDBError {
    int[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Integer.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(3, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferSetItemLongIndexOutOfBoundsException() throws TileDBError {
    long[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Long.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(3, 0L);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferSetItemFloatIndexOutOfBoundsException() throws TileDBError {
    float[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Float.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(3, 0f);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferSetItemDoubleIndexOutOfBoundsException() throws TileDBError {
    double[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Double.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(3, 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferSetItemStringIndexOutOfBoundsException() throws TileDBError {
    String buffer = "123";
    NativeArray testArray = new NativeArray(ctx, buffer, String.class);
    Assert.assertNotNull(testArray);
    testArray.setItem(3, "1");
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferGetItemByteIndexOutOfBoundsExceptionNegativeIndex()
      throws TileDBError {
    byte[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferGetItemByteIndexOutOfBoundsException() throws TileDBError {
    byte[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferGetItemShortIndexOutOfBoundsException() throws TileDBError {
    short[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Short.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferGetItemIntegerIndexOutOfBoundsException() throws TileDBError {
    int[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Integer.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferGetItemLongIndexOutOfBoundsException() throws TileDBError {
    long[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Long.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferGetItemFloatIndexOutOfBoundsException() throws TileDBError {
    float[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Float.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferGetItemDoubleIndexOutOfBoundsException() throws TileDBError {
    double[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Double.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferGetItemStringIndexOutOfBoundsException() throws TileDBError {
    String buffer = "123";
    NativeArray testArray = new NativeArray(ctx, 1, String.class);
    Assert.assertNotNull(testArray);
    testArray.getItem(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayByteIndexOutOfBoundsExceptionNegativeIndex()
      throws TileDBError {
    byte[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(-1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayByteIndexOutOfBoundsExceptionNegativeSize()
      throws TileDBError {
    byte[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(-1, -1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayByteIndexOutOfBoundsException() throws TileDBError {
    byte[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Byte.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(3, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayShortIndexOutOfBoundsException() throws TileDBError {
    short[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Short.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(3, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayIntegerIndexOutOfBoundsException() throws TileDBError {
    int[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Integer.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(3, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayLongIndexOutOfBoundsException() throws TileDBError {
    long[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Long.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(3, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayFloatIndexOutOfBoundsException() throws TileDBError {
    float[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Float.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(3, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayDoubleIndexOutOfBoundsException() throws TileDBError {
    double[] buffer = {1, 2, 3};
    NativeArray testArray = new NativeArray(ctx, buffer, Double.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(3, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testArrayBufferToJavaArrayCharIndexOutOfBoundsException() throws TileDBError {
    String buffer = "123";
    NativeArray testArray = new NativeArray(ctx, buffer, String.class);
    Assert.assertNotNull(testArray);
    testArray.toJavaArray(3, 1);
  }
}
