/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 TileDB, Inc.
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

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_array_schema_t;
import io.tiledb.libtiledb.SWIGTYPE_p_unsigned_long;
import io.tiledb.libtiledb.tiledb;

public class FileStore {

  /**
   * Creates an array schema based on the properties of the provided URI or a default schema if no
   * URI is provided
   *
   * @param ctx The TileDB context.
   * @param uri The file URI.
   * @return The created TileDB array schema.
   * @throws TileDBError
   */
  public static ArraySchema schemaCreate(Context ctx, String uri) throws TileDBError {
    SWIGTYPE_p_p_tiledb_array_schema_t array_schema_pp = tiledb.new_tiledb_array_schema_tpp();
    try {
      ctx.handleError(tiledb.tiledb_filestore_schema_create(ctx.getCtxp(), uri, array_schema_pp));
      return new ArraySchema(ctx, array_schema_pp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_tpp(array_schema_pp);
      throw err;
    }
  }

  /**
   * Imports a file into a TileDB filestore array
   *
   * @param ctx The TileDB Context.
   * @param filestoreArrayURI The array URI.
   * @param fileURI The file URI.
   * @param mimeType The mime type of the file.
   * @throws TileDBError
   */
  public static void uriImport(
      Context ctx, String filestoreArrayURI, String fileURI, MimeType mimeType) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_filestore_uri_import(
            ctx.getCtxp(), filestoreArrayURI, fileURI, mimeType.toSwigEnum()));
  }

  /**
   * Exports a filestore array into a bare file
   *
   * @param ctx The TileDB context
   * @param filestoreArrayURI The array URI
   * @param fileURI The file URI.
   * @throws TileDBError
   */
  public static void uriExport(Context ctx, String filestoreArrayURI, String fileURI)
      throws TileDBError {
    ctx.handleError(tiledb.tiledb_filestore_uri_export(ctx.getCtxp(), fileURI, filestoreArrayURI));
  }

  /**
   * Writes 'bufferSize' bytes starting at address buf into filestore array
   *
   * @param ctx The TileDB context.
   * @param arrayUri The array URI.
   * @param buffer The input buffer.
   * @param bufferSize NUmber of bytes to be imported.
   * @param mimeType The mime type of the data.
   * @throws TileDBError
   */
  public static void bufferImport(
      Context ctx, String arrayUri, NativeArray buffer, long bufferSize, MimeType mimeType)
      throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_filestore_buffer_import(
            ctx.getCtxp(), arrayUri, buffer.toVoidPointer(), bufferSize, mimeType.toSwigEnum()));
  }

  /**
   * Dump the content of a filestore array into a buffer
   *
   * @param ctx The TileDB context.
   * @param filestoreArrayURI The array URI.
   * @param offset The offset at which we should start exporting from the array.
   * @param bufferLength The number of bytes to be exported into the buffer.
   * @return
   * @throws TileDBError
   */
  public static Object bufferExport(
      Context ctx, String filestoreArrayURI, long offset, int bufferLength) throws TileDBError {
    NativeArray buffer = new NativeArray(ctx, bufferLength, Datatype.TILEDB_BLOB);
    try {
      ctx.handleError(
          tiledb.tiledb_filestore_buffer_export(
              ctx.getCtxp(), filestoreArrayURI, offset, buffer.toVoidPointer(), bufferLength));
      return buffer.toJavaArray();
    } finally {
      buffer.close();
    }
  }

  /**
   * Get the uncompressed size of a filestore array
   *
   * @param ctx The TileDB contect.
   * @param filestoreURI The array URI.
   * @return The uncompressed size of the filestore array.
   * @throws TileDBError
   */
  public static long getSize(Context ctx, String filestoreURI) throws TileDBError {
    SWIGTYPE_p_unsigned_long sizep = tiledb.new_ulp();

    try {
      ctx.handleError(tiledb.tiledb_filestore_size(ctx.getCtxp(), filestoreURI, sizep));
      return tiledb.ulp_value(sizep);
    } finally {
      tiledb.delete_ulp(sizep);
    }
  }
}
