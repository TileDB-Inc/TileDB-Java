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

import static io.tiledb.java.api.QueryType.*;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * Class representing a TileDB array object.
 *
 * <p>An Array object represents array data in TileDB at some persisted location, e.g. on disk, in
 * an S3 bucket, etc. Once an array has been opened for reading or writing, interact with the data
 * through {@link Query} objects.
 *
 * <pre><b>Example:</b>
 * {@code
 *   // Create an ArraySchema, add attributes, domain, etc.
 *   Context ctx = new Context();
 *   ArraySchema schema = new ArraySchema(...);
 *   // Create empty array named "my_array" on persistent storage.
 *   Array.create("my_array", schema);
 * }
 * </pre>
 */
public class Array implements AutoCloseable {

  private Context ctx;
  private String uri;
  private ArraySchema schema;
  private QueryType query_type;

  private SWIGTYPE_p_tiledb_array_t arrayp;
  private SWIGTYPE_p_p_tiledb_array_t arraypp;

  /**
   * Constructs an Array object opening the array for reading.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   Array array new Array(ctx, "s3://bucket-name/array-name");
   * }</pre>
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @exception TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri) throws TileDBError {
    openArray(ctx, uri, TILEDB_READ, EncryptionType.TILEDB_NO_ENCRYPTION, new byte[] {});
  }

  /**
   * Constructs an Array object opening the array for reading.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   Array array new Array(ctx, "s3://bucket-name/array-name");
   * }</pre>
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @exception TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri, BigInteger timestamp) throws TileDBError {
    openArray(ctx, uri, TILEDB_READ, EncryptionType.TILEDB_NO_ENCRYPTION, new byte[] {}, timestamp);
  }

  /**
   * Constructs an Array object, opening the array for the given query type.
   *
   * <pre><b>Example:</b>
   * {@code
   * Context ctx = new Context();
   * Array array new Array(ctx, "s3://bucket-name/array-name", TILEDB_READ);
   * }
   * </pre>
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @param query_type Query type to open the array for.
   * @exception TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri, QueryType query_type) throws TileDBError {
    openArray(ctx, uri, query_type, EncryptionType.TILEDB_NO_ENCRYPTION, new byte[] {});
  }

  /**
   * Constructs an Array object, opening the encrypted array for the given query type.
   *
   * <pre><b>Example:</b>
   * {@code
   * Context ctx = new Context();
   * String key = "0123456789abcdeF0123456789abcdeF";
   * Array array new Array(ctx, "s3://bucket-name/array-name",
   *                       TILEDB_READ,
   *                       TILEDB_AES_256_GCM,
   *                       key.getBytes(StandardCharsets.UTF_8));
   * }
   * </pre>
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @param query_type Query type to open the array for
   * @param encryption_type The encryption type to use
   * @param key The encryption key to use
   * @throws TileDBError A TileDB exception
   */
  public Array(
      Context ctx, String uri, QueryType query_type, EncryptionType encryption_type, byte[] key)
      throws TileDBError {
    openArray(ctx, uri, query_type, encryption_type, key);
  }

  private synchronized void openArray(
      Context ctx, String uri, QueryType query_type, EncryptionType encryption_type, byte[] key)
      throws TileDBError {
    SWIGTYPE_p_p_tiledb_array_t _arraypp = tiledb.new_tiledb_array_tpp();
    try {
      ctx.handleError(tiledb.tiledb_array_alloc(ctx.getCtxp(), uri, _arraypp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_tpp(_arraypp);
      throw err;
    }
    SWIGTYPE_p_tiledb_array_t _arrayp = tiledb.tiledb_array_tpp_value(_arraypp);
    ArraySchema _schema;
    try (NativeArray keyArray = new NativeArray(ctx, key, Byte.class)) {
      try {
        ctx.handleError(
            tiledb.tiledb_array_open_with_key(
                ctx.getCtxp(),
                _arrayp,
                query_type.toSwigEnum(),
                encryption_type.toSwigEnum(),
                keyArray.toVoidPointer(),
                keyArray.getSize()));
      } catch (TileDBError err) {
        tiledb.delete_tiledb_array_tpp(_arraypp);
        throw err;
      }
      _schema = new ArraySchema(ctx, uri, encryption_type, key);
    }
    this.ctx = ctx;
    this.uri = uri;
    this.query_type = query_type;
    this.schema = _schema;
    this.arraypp = _arraypp;
    this.arrayp = _arrayp;
  }

  private synchronized void openArray(
      Context ctx,
      String uri,
      QueryType query_type,
      EncryptionType encryption_type,
      byte[] key,
      BigInteger timestamp)
      throws TileDBError {
    SWIGTYPE_p_p_tiledb_array_t _arraypp = tiledb.new_tiledb_array_tpp();
    try {
      ctx.handleError(tiledb.tiledb_array_alloc(ctx.getCtxp(), uri, _arraypp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_tpp(_arraypp);
      throw err;
    }
    SWIGTYPE_p_tiledb_array_t _arrayp = tiledb.tiledb_array_tpp_value(_arraypp);
    ArraySchema _schema;
    try (NativeArray keyArray = new NativeArray(ctx, key, Byte.class)) {
      try {
        ctx.handleError(
            tiledb.tiledb_array_open_at_with_key(
                ctx.getCtxp(),
                _arrayp,
                query_type.toSwigEnum(),
                encryption_type.toSwigEnum(),
                keyArray.toVoidPointer(),
                keyArray.getSize(),
                timestamp));
      } catch (TileDBError err) {
        tiledb.delete_tiledb_array_tpp(_arraypp);
        throw err;
      }
      _schema = new ArraySchema(ctx, uri, encryption_type, key);
    }
    this.ctx = ctx;
    this.uri = uri;
    this.query_type = query_type;
    this.schema = _schema;
    this.arraypp = _arraypp;
    this.arrayp = _arrayp;
  }

  private void checkIsOpen() throws TileDBError {
    if (arrayp == null) {
      throw new TileDBError("TileDB Array " + uri + " is closed");
    }
    return;
  }

  /**
   * Consolidates the fragments of an array into a single fragment.
   *
   * <p>All queries to the array before consolidation must be finalized before consolidation can
   * begin. Consolidation temporarily aquires an exclusive lock on the array when finalizing the
   * resulting merged fragment.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   Array.consolidate(ctx, "s3://bucket-name/array-name");
   * }
   * </pre>
   *
   * @param ctx TileDB context object
   * @param uri TileDB URI string
   * @exception TileDBError A TileDB exception
   */
  public static void consolidate(Context ctx, String uri) throws TileDBError {
    consolidate(ctx, uri, EncryptionType.TILEDB_NO_ENCRYPTION, new byte[] {});
  }

  /**
   * Consolidates the fragments of an array into a single fragment.
   *
   * <p>All queries to the array before consolidation must be finalized before consolidation can
   * begin. Consolidation temporarily aquires an exclusive lock on the array when finalizing the
   * resulting merged fragment.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   Array.consolidate(ctx, "s3://bucket-name/array-name");
   * }
   * </pre>
   *
   * @param ctx TileDB context object
   * @param uri TileDB URI string
   * @param config A TileDB config object with configuration parameters for the consolidation
   * @exception TileDBError A TileDB exception
   */
  public static void consolidate(Context ctx, String uri, Config config) throws TileDBError {
    consolidate(ctx, uri, EncryptionType.TILEDB_NO_ENCRYPTION, new byte[] {}, config);
  }

  /**
   * Consolidates the fragments of an array into a single fragment.
   *
   * <p>All queries to the array before consolidation must be finalized before consolidation can
   * begin. Consolidation temporarily aquires an exclusive lock on the array when finalizing the
   * resulting merged fragment.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   String key = "0123456789abcdeF0123456789abcdeF";
   *   Array.consolidate(ctx, "s3://bucket-name/array-name",
   *                     TILEDB_AES_256_GCM,
   *                     key.getBytes(StandardCharsets.UTF_8));
   * }
   * </pre>
   *
   * @param ctx A TileDB Context
   * @param uri URI string to TileDB array
   * @param encryption_type Encryption type the array is encrypted with
   * @param key A byte array key to decrypt array
   * @throws TileDBError
   */
  public static void consolidate(
      Context ctx, String uri, EncryptionType encryption_type, byte[] key) throws TileDBError {
    consolidate(ctx, uri, encryption_type, key, new Config());
  }

  /**
   * Consolidates the fragments of an array into a single fragment.
   *
   * <p>All queries to the array before consolidation must be finalized before consolidation can
   * begin. Consolidation temporarily aquires an exclusive lock on the array when finalizing the
   * resulting merged fragment.
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   String key = "0123456789abcdeF0123456789abcdeF";
   *   Array.consolidate(ctx, "s3://bucket-name/array-name",
   *                     TILEDB_AES_256_GCM,
   *                     key.getBytes(StandardCharsets.UTF_8));
   * }
   * </pre>
   *
   * @param ctx A TileDB Context
   * @param uri URI string to TileDB array
   * @param encryption_type Encryption type the array is encrypted with
   * @param key A byte array key to decrypt array
   * @param config A TileDB config object with configuration parameters for the consolidation
   * @throws TileDBError
   */
  public static void consolidate(
      Context ctx, String uri, EncryptionType encryption_type, byte[] key, Config config)
      throws TileDBError {
    try (NativeArray keyArray = new NativeArray(ctx, key, Byte.class)) {
      ctx.handleError(
          tiledb.tiledb_array_consolidate_with_key(
              ctx.getCtxp(),
              uri,
              encryption_type.toSwigEnum(),
              keyArray.toVoidPointer(),
              keyArray.getSize(),
              config.getConfigp()));
    }
  }

  /**
   * Checks if a given URI is an existing TileDB array object
   *
   * @param ctx TileDB context object
   * @param uri TileDB URI array string
   * @return true if the uri is an array object, false otherwise
   * @throws TileDBError
   */
  public static boolean exists(Context ctx, String uri) throws TileDBError {
    TileDBObjectType objtype;
    SWIGTYPE_p_tiledb_object_t objtypep = tiledb.new_tiledb_object_tp();
    try {
      ctx.handleError(tiledb.tiledb_object_type(ctx.getCtxp(), uri, objtypep));
      objtype = TileDBObjectType.fromSwigEnum(tiledb.tiledb_object_tp_value(objtypep));
    } finally {
      tiledb.delete_tiledb_object_tp(objtypep);
    }
    if (objtype == TileDBObjectType.TILEDB_ARRAY) {
      return true;
    }
    return false;
  }

  /**
   * Creates a persisted TileDB array given an input {@link ArraySchema}
   *
   * <pre><b>Example:</b>
   * {@code
   *   Array.create("my_array", schema);
   * }
   * </pre>
   *
   * @param uri The array URI string
   * @param schema The TileDB ArraySchema
   * @exception TileDBError A TileDB exception
   */
  public static void create(String uri, ArraySchema schema) throws TileDBError {
    Context ctx = schema.getCtx();
    ctx.handleError(tiledb.tiledb_array_schema_check(ctx.getCtxp(), schema.getSchemap()));
    ctx.handleError(tiledb.tiledb_array_create(ctx.getCtxp(), uri, schema.getSchemap()));
  }

  /**
   * Creates an encrypted persisted TileDBArray given input {@link ArraySchema} and encryption key
   *
   * <pre><b>Example:</b>
   * {@code
   *   String key = "0123456789abcdeF0123456789abcdeF";
   *   Array.create("my_array", schema,
   *                TILEDB_AES_256_GCM,
   *                key.getBytes(StandardCharsets.UTF_8));
   * }
   * </pre>
   *
   * @param uri The array URI string
   * @param schema The TileDB ArraySchema
   * @param encryption_type The encryption type to use
   * @param key The encryption key to use
   * @throws TileDBError A TileDB exception
   */
  public static void create(
      String uri, ArraySchema schema, EncryptionType encryption_type, byte[] key)
      throws TileDBError {
    Context ctx = schema.getCtx();
    ctx.handleError(tiledb.tiledb_array_schema_check(ctx.getCtxp(), schema.getSchemap()));
    try (NativeArray keyArray = new NativeArray(ctx, key, Byte.class)) {
      ctx.handleError(
          tiledb.tiledb_array_create_with_key(
              ctx.getCtxp(),
              uri,
              schema.getSchemap(),
              encryption_type.toSwigEnum(),
              keyArray.toVoidPointer(),
              keyArray.getSize()));
    }
  }

  /**
   * Get the non-empty domain of an array, returning the bounding coordinates for each dimension.
   *
   * @return A HashMap of dimension names and (lower, upper) inclusive bounding coordinate range
   *     pair. Empty HashMap if the array has no data.
   * @exception TileDBError A TileDB exception
   */
  public HashMap<String, Pair> nonEmptyDomain() throws TileDBError {
    checkIsOpen();
    HashMap<String, Pair> ret = new HashMap<String, Pair>();
    try (Domain domain = schema.getDomain();
        NativeArray domainArray =
            new NativeArray(ctx, 2 * (int) domain.getRank(), domain.getType())) {
      SWIGTYPE_p_int emptyp = tiledb.new_intp();
      try {
        ctx.handleError(
            tiledb.tiledb_array_get_non_empty_domain(
                ctx.getCtxp(), arrayp, domainArray.toVoidPointer(), emptyp));
        if (tiledb.intp_value(emptyp) == 1) {
          return ret;
        }
      } finally {
        tiledb.delete_intp(emptyp);
      }
      for (int i = 0; i < domain.getRank(); i++) {
        try (Dimension d = domain.getDimension(i)) {
          ret.put(
              d.getName(),
              new Pair(domainArray.getItem((2 * i) + 0), domainArray.getItem((2 * i) + 1)));
        }
      }
    }
    return ret;
  }

  /**
   * Compute an upper bound on the buffer elements needed to read a subarray.
   *
   * @param subarray Domain subarray
   * @return The maximum number of elements for each array attribute. If the Array is sparse, max
   *     number of coordinates are also returned. Note that two numbers are returned per attribute.
   *     The first is the maximum number of elements in the offset buffer (0 for fixed-sized
   *     attributes and coordinates), the second is the maximum number of elements of the value
   *     buffer.
   * @throws TileDBError A TileDB exception
   */
  public HashMap<String, Pair<Long, Long>> maxBufferElements(NativeArray subarray)
      throws TileDBError {
    checkIsOpen();

    HashMap<String, Pair<Long, Long>> ret = new HashMap<String, Pair<Long, Long>>();

    uint64_tArray off_nbytes = new uint64_tArray(1);
    uint64_tArray val_nbytes = new uint64_tArray(1);

    try (Domain domain = schema.getDomain()) {
      Types.typeCheck(subarray.getNativeType(), domain.getType());

      for (long i = 0; i < schema.getAttributeNum(); i++) {
        try (Attribute attr = schema.getAttribute(i)) {
          String attrName = attr.getName();
          if (attr.isVar()) {
            ctx.handleError(
                tiledb.tiledb_array_max_buffer_size_var(
                    ctx.getCtxp(),
                    arrayp,
                    attrName,
                    subarray.toVoidPointer(),
                    off_nbytes.cast(),
                    val_nbytes.cast()));
            ret.put(
                attrName,
                new Pair(
                    off_nbytes.getitem(0).longValue() / tiledb.tiledb_offset_size().longValue(),
                    val_nbytes.getitem(0).longValue()
                        / tiledb.tiledb_datatype_size(attr.getType().toSwigEnum()).longValue()));
          } else {
            // fixed sized
            ctx.handleError(
                tiledb.tiledb_array_max_buffer_size(
                    ctx.getCtxp(), arrayp, attrName, subarray.toVoidPointer(), val_nbytes.cast()));
            ret.put(
                attrName,
                new Pair(
                    0l,
                    val_nbytes.getitem(0).longValue()
                        / tiledb.tiledb_datatype_size(attr.getType().toSwigEnum()).longValue()));
          }
        }
      }
      ctx.handleError(
          tiledb.tiledb_array_max_buffer_size(
              ctx.getCtxp(),
              arrayp,
              tiledb.tiledb_coords(),
              subarray.toVoidPointer(),
              val_nbytes.cast()));
      ret.put(
          tiledb.tiledb_coords(),
          new Pair(
              0l,
              val_nbytes.getitem(0).longValue()
                  / tiledb.tiledb_datatype_size(domain.getType().toSwigEnum()).longValue()));
    } finally {
      off_nbytes.delete();
      val_nbytes.delete();
    }
    return ret;
  }

  /** @return The TileDB Context object associated with the Array instance. */
  public Context getCtx() {
    return ctx;
  }

  /** @return The URI string of the array */
  public String getUri() {
    return uri;
  }

  /** @return The TileDB ArraySchema of the Array instance. */
  public ArraySchema getSchema() throws TileDBError {
    ArraySchema _schema;
    SWIGTYPE_p_p_tiledb_array_schema_t schemapp = tiledb.new_tiledb_array_schema_tpp();
    try {
      ctx.handleError(tiledb.tiledb_array_get_schema(ctx.getCtxp(), getArrayp(), schemapp));
      _schema = new ArraySchema(ctx, schemapp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_tpp(schemapp);
      throw err;
    }
    return _schema;
  }

  /** @return The TileDB QueryType enum value that the Array instance. */
  public QueryType getQueryType() {
    return query_type;
  }

  /** @return SWIG tiledb_array_t pointer wrapper object. */
  protected SWIGTYPE_p_tiledb_array_t getArrayp() {
    return arrayp;
  }

  /** Free's the native objects and closes the Array. */
  public synchronized void close() {
    if (arrayp != null && arraypp != null) {
      tiledb.tiledb_array_close(ctx.getCtxp(), arrayp);
      tiledb.tiledb_array_free(arraypp);
      arrayp = null;
      arraypp = null;
      if (schema != null) {
        schema.close();
      }
    }
  }
}
