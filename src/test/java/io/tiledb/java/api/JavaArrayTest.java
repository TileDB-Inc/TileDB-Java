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

import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JavaArrayTest {

  private Context ctx;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
  }

  @Test
  public void testJavaArrayExists() throws Exception {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_INT64, 4);
    Assert.assertNotNull(testArray);
  }

  @Test
  public void testGetDatatypeExists() throws Exception {
    Datatype type = Datatype.TILEDB_CHAR;
    JavaArray testArray = new JavaArray(type, 4);

    Assert.assertEquals(testArray.getDataType(), type);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testJavaArraySetItemIntegerIndexOutOfBoundsException() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_INT32, 4);
    Assert.assertNotNull(testArray);
    testArray.set(testArray.getNumElements(), 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testJavaArraySetItemLongIndexOutOfBoundsException() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_INT64, 4);
    Assert.assertNotNull(testArray);
    testArray.set(testArray.getNumElements(), 0L);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testJavaArraySetItemFloatIndexOutOfBoundsException() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_FLOAT32, 4);
    Assert.assertNotNull(testArray);
    testArray.set(testArray.getNumElements(), (float) 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testJavaArraySetItemDoubleIndexOutOfBoundsException() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_FLOAT64, 4);
    Assert.assertNotNull(testArray);
    testArray.set(testArray.getNumElements(), (double) 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testJavaArraySetItemByteIndexOutOfBoundsException() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_INT8, 4);
    Assert.assertNotNull(testArray);
    testArray.set(testArray.getNumElements(), (byte) 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testJavaArraySetItemStringIndexOutOfBoundsException() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_CHAR, 4);
    Assert.assertNotNull(testArray);
    testArray.set(testArray.getNumElements(), "123".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testGetInteger() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_INT32, 4);
    Assert.assertNotNull(testArray);

    Assert.assertEquals(((int[]) testArray.get()).length, 4);
  }

  @Test
  public void testGetLong() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_INT64, 4);
    Assert.assertNotNull(testArray);

    Assert.assertEquals(((long[]) testArray.get()).length, 4);
  }

  @Test
  public void testGetDouble() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_FLOAT64, 4);
    Assert.assertNotNull(testArray);

    Assert.assertEquals(((double[]) testArray.get()).length, 4);
  }

  @Test
  public void testGetFloat() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_FLOAT32, 4);
    Assert.assertNotNull(testArray);

    Assert.assertEquals(((float[]) testArray.get()).length, 4);
  }

  @Test
  public void testGetShort() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_INT16, 4);
    Assert.assertNotNull(testArray);

    Assert.assertEquals(((short[]) testArray.get()).length, 4);
  }

  @Test
  public void testGetByte() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_INT8, 4);
    Assert.assertNotNull(testArray);

    Assert.assertEquals(((byte[]) testArray.get()).length, 4);
  }

  @Test
  public void testGetString() throws TileDBError {
    JavaArray testArray = new JavaArray(Datatype.TILEDB_CHAR, 4);
    Assert.assertNotNull(testArray);

    Assert.assertEquals(((byte[]) testArray.get()).length, 4);
  }
}
