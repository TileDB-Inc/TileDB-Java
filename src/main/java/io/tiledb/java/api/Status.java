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

import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_query_status_t;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_query_status_t;

/**
 * Enumeration for Query status.
 */
public enum Status {
  FAILED, COMPLETE, INPROGRESS, INCOMPLETE, UNDEF;


  public static Status toStatus(SWIGTYPE_p_tiledb_query_status_t statusp){
    tiledb_query_status_t status = tiledb.tiledb_query_status_tp_value(statusp);
    switch (status) {
      case TILEDB_INCOMPLETE:
        return INCOMPLETE;
      case TILEDB_COMPLETED:
        return COMPLETE;
      case TILEDB_INPROGRESS:
        return INPROGRESS;
      case TILEDB_FAILED:
        return FAILED;
    }
    return UNDEF;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}
