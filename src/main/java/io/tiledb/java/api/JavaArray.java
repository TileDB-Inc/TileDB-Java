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

public class JavaArray {
  private Object array;
  private Datatype dataType;
  private int numElements;

  /**
   * Creates an empty JavaArray based on the given Datatype dt.
   *
   * @param dt The Datatype
   * @param numElements The array capacity (number of elements)
   * @throws TileDBError
   */
  public JavaArray(Datatype dt, int numElements) throws TileDBError {
    this.dataType = dt;
    this.numElements = numElements;

    Class c = Types.getJavaType(dt);

    if (c == Integer.class) this.array = new int[numElements];
    else if (c == Long.class) this.array = new long[numElements];
    else if (c == Double.class) this.array = new double[numElements];
    else if (c == Float.class) this.array = new float[numElements];
    else if (c == Short.class) this.array = new short[numElements];
    else if (c == Byte.class) this.array = new byte[numElements];
    else if (c == String.class) {
      this.array = new byte[numElements];
    }
  }

  /**
   * Returns the native Java array as Object
   *
   * @return The native Java array
   */
  public Object get() {
    return this.array;
  }

  /**
   * Returns the number of elements
   *
   * @return The number of elements
   */
  public int getNumElements() {
    return this.numElements;
  }

  /**
   * Sets an element to an int array
   *
   * @param position The position
   * @param o The element
   */
  public void set(int position, int o) throws ArrayIndexOutOfBoundsException {
    ((int[]) array)[position] = o;
  }

  /**
   * Sets an element to a long array
   *
   * @param position The position
   * @param o The element
   */
  public void set(int position, long o) throws ArrayIndexOutOfBoundsException {
    ((long[]) array)[position] = o;
  }

  /**
   * Sets an element to a float array
   *
   * @param position The position
   * @param o The element
   */
  public void set(int position, float o) throws ArrayIndexOutOfBoundsException {
    ((float[]) array)[position] = o;
  }

  /**
   * Sets an element to a double array
   *
   * @param position The position
   * @param o The element
   */
  public void set(int position, double o) throws ArrayIndexOutOfBoundsException {
    ((double[]) array)[position] = o;
  }

  /**
   * Sets an element to a byte array
   *
   * @param position The position
   * @param o The element
   */
  public void set(int position, byte o) throws ArrayIndexOutOfBoundsException {
    ((byte[]) array)[position] = o;
  }

  /**
   * Sets an element to a short array
   *
   * @param position The position
   * @param o The element
   */
  public void set(int position, short o) throws ArrayIndexOutOfBoundsException {
    ((short[]) array)[position] = o;
  }

  /**
   * Appends the element of the input byte array
   *
   * @param position The position
   * @param o The element
   */
  public void set(int position, byte[] o) throws ArrayIndexOutOfBoundsException {
    int curr = position;
    for (byte b : o) {
      ((byte[]) array)[curr] = b;
      ++curr;
    }
  }

  /**
   * Returns the datatype
   *
   * @return The Dataype datatype
   */
  public Datatype getDataType() {
    return this.dataType;
  }
}
