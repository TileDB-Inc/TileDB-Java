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
}
