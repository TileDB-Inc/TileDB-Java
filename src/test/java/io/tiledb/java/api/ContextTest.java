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

import io.tiledb.api.tiledb_filesystem_t;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ContextTest {

  @Test
  public void testContext() throws Throwable {
    Context ctx = new Context();
//    ctx.set_error_handler(new Handler());

    System.out.println("HDFS: "+ctx.is_supported_fs(tiledb_filesystem_t.TILEDB_HDFS));
    System.out.println("S3: "+ctx.is_supported_fs(tiledb_filesystem_t.TILEDB_S3));

    ctx.free();
  }

  private class Handler extends ContextCallback{

    @Override
    public void call(String msg) throws TileDBError {
      System.out.println("my callback: "+msg);
    }
  }
}
