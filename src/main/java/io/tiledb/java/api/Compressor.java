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

/**
 * Represents a compression scheme. Composed of a compression algorithm + a
 * compression level. A compression level of -1 indicates the default
 * level.
 *
 * **Example:**
 *
 * @code{.java}
 * Attribute a1 = new Attribute(ctx, "a1", Integer.class);
 * a1.setCompressor(new Compressor(TILEDB_BLOSC_LZ4, -1));
 * @endcode
 */
public class Compressor {
  private TileDBCompressorEnum compressor;
  private int level;

  @Override
  public String toString() {
    return "Compressor{" +
        "compressor=" + compressor +
        ", level=" + level +
        '}';
  }

  /**
   * Create a compressor with a given algorithm and level.
   *
   * @param compressor Enumerated compression algorithm.
   * @param level Compression level (-1 for the compression algorithm's
   * default).
   */
  public Compressor(TileDBCompressorEnum compressor, int level) {
    this.compressor = compressor;
    this.level = level;
  }

  /**
   *
   * @return The Enumerated compressor algortihm.
   */
  public TileDBCompressorEnum getCompressor() {
    return compressor;
  }

  /**
   * Sets the compressor algorithm.
   * @param compressor Enumerated compression algorithm.
   */
  public void setCompressor(TileDBCompressorEnum compressor) {
    this.compressor = compressor;
  }

  /**
   *
   * @return The compression level.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Sets the compression level
   * @param level Compression level (-1 for the compression algorithm's
   * default).
   */
  public void setLevel(int level) {
    this.level = level;
  }

}
