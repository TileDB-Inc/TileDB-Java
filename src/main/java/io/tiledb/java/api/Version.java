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

import io.tiledb.api.SWIGTYPE_p_int;
import io.tiledb.api.tiledb;

/**  TileDB version. Format: `major_minor_rev`.*/
public class Version {
  private int major;
  private int minor;
  private int rev;

  /**
   * Constructor for TileDB library version object
   */
  public Version(){
    SWIGTYPE_p_int majorp = tiledb.new_intp(), minorp = tiledb.new_intp(), revp = tiledb
        .new_intp();
    tiledb.tiledb_version(majorp, minorp, revp);
    this.major = tiledb.intp_value(majorp);
    this.minor = tiledb.intp_value(minorp);
    this.rev = tiledb.intp_value(revp);
    tiledb.delete_intp(majorp);
    tiledb.delete_intp(minorp);
    tiledb.delete_intp(revp);
  }

  /** Returns the major number. */
  public int getMajor() {
    return major;
  }

  /** Returns the minor number. */
  public int getMinor() {
    return minor;
  }

  /** Returns the revision number. */
  public int getRevision() {
    return rev;
  }

  /** Prints version to string. */
  public String toString(){
    return "TileDB v"+major+"."+minor+"."+rev;
  }
}
