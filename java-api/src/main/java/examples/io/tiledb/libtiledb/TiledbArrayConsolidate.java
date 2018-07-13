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
 *
 * @section DESCRIPTION
 *This program shows how to consolidate arrays.
 *
 * One way to make this work is by first creating a dense array and making three
 * different writes:
 *
 * ```
 * $ java -cp build/libs/tiledb-jni-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbDenseCreate
 * $ java -cp build/libs/tiledb-jni-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbDenseWriteGlobal1
 * $ java -cp build/libs/tiledb-jni-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbDenseWriteGlobalSubarray
 * $ java -cp build/libs/tiledb-jni-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbDenseWriteUnordered
 * $ ls -1 my_dense_array
 * __0x7ffff10e73c0_1517941950491
 * __0x7ffff10e73c0_1517941954698
 * __0x7ffff10e73c0_1517941959273
 * __array_schema.tdb
 * __lock.tdb
 * ```
 *
 * The above will create three fragments (appearing as separate subdirectories).
 *
 * Running this program will consolidate the 3 fragments/directories into a
 * single one.
 *
 * ```
 * $ java -cp build/libs/tiledb-jni-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbArrayConsolidate
 * $ ls -1 my_dense_array
 * __0x7ffff10e73c0_1517941970634_1517941959273
 * __lock.tdb
 * __array_schema.tdb
 * ```
 */

package examples.io.tiledb.libtiledb;

import io.tiledb.libtiledb.*;

public class TiledbArrayConsolidate {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = tiledb.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = tiledb.tiledb_ctx_tpp_value(ctxpp);

    // Consolidate array
    tiledb.tiledb_array_consolidate(ctx, "my_dense_array");

    // Clean up
    tiledb.tiledb_ctx_free(ctxpp);
  }
}
