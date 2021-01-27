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

import static io.tiledb.java.api.QueryType.*;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

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
    this(ctx, uri, TILEDB_READ);
  }

  /**
   * Constructs an Array object opening the array for reading at a user-given timestamp
   * (time-travelling).
   *
   * <pre><b>Example:</b>
   * {@code
   *   Context ctx = new Context();
   *   Array array new Array(ctx, "s3://bucket-name/array-name");
   * }</pre>
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @param timestamp The timestamp
   * @exception TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri, BigInteger timestamp) throws TileDBError {
    this(ctx, uri, TILEDB_READ, timestamp);
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
    this(ctx, uri, query_type, EncryptionType.TILEDB_NO_ENCRYPTION, new byte[] {});
  }

  /**
   * Constructs an Array object, opening the array for the given query type at a user-given
   * timestamp (time-travelling).
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
   * @param timestamp The timestamp
   * @exception TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri, QueryType query_type, BigInteger timestamp)
      throws TileDBError {
    this(ctx, uri, query_type, EncryptionType.TILEDB_NO_ENCRYPTION, new byte[] {}, timestamp);
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
   * @param timestamp The timestamp
   * @throws TileDBError A TileDB exception
   */
  public Array(
      Context ctx,
      String uri,
      QueryType query_type,
      EncryptionType encryption_type,
      byte[] key,
      BigInteger timestamp)
      throws TileDBError {
    openArray(ctx, uri, query_type, encryption_type, key, timestamp);
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
   * Cleans up the array, such as consolidated fragments and array metadata. Note that this will
   * coarsen the granularity of time traveling (see docs for more information).
   *
   * <p>This method uses as the vacuum configuration the configuration instance that is encapsulated
   * in the context (ctx) instance (ctx.getConfig()).
   *
   * @param arrayURI The array URI
   * @param ctx The TileDB context
   * @throws TileDBError A TileDB exception
   */
  public static void vacuum(Context ctx, String arrayURI) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_vacuum(ctx.getCtxp(), arrayURI, ctx.getConfig().getConfigp()));
  }

  /**
   * Cleans up the array, such as consolidated fragments and array metadata. Note that this will
   * coarsen the granularity of time traveling (see docs for more information).
   *
   * @param arrayURI The array URI
   * @param ctx The TileDB context
   * @param config The TileDB config that will be used for the vacuum process
   * @throws TileDBError A TileDB exception
   */
  public static void vacuum(Context ctx, String arrayURI, Config config) throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_vacuum(ctx.getCtxp(), arrayURI, config.getConfigp()));
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
    HashMap<String, Pair> ret = new HashMap<>();
    try {
      Domain domain = schema.getDomain();
      long numDims = domain.getNDim();
      for (long dimIdx = 0; dimIdx < numDims; ++dimIdx) {
        Dimension dimension = domain.getDimension(dimIdx);
        Pair p = getNonEmptyDomainFromIndex(dimIdx);
        ret.put(dimension.getName(), p);
      }
    } catch (TileDBError error) {
      throw error;
    }
    return ret;
  }

  /**
   * Given a dimension's index, return the bounding coordinates for that dimension. The method
   * checks if the dimension is var-sized or not, and it works for both cases.
   *
   * @param index THe dimension's index
   * @return A Pair that contains the dimension's bounds
   * @exception TileDBError A TileDB exception
   */
  public Pair getNonEmptyDomainFromIndex(long index) throws TileDBError {
    checkIsOpen();
    try (Domain domain = schema.getDomain();
        NativeArray domainArray = new NativeArray(ctx, 2, domain.getDimension(index).getType())) {

      if (domain.getDimension(index).isVar()) return getNonEmptyDomainVarFromIndex(index);

      SWIGTYPE_p_int emptyp = tiledb.new_intp();
      try {
        ctx.handleError(
            tiledb.tiledb_array_get_non_empty_domain_from_index(
                ctx.getCtxp(), arrayp, index, domainArray.toVoidPointer(), emptyp));
        if (tiledb.intp_value(emptyp) == 1) {
          return Pair.empty();
        }
      } finally {
        tiledb.delete_intp(emptyp);
      }

      return new Pair(domainArray.getItem(0), domainArray.getItem(1));
    }
  }

  /**
   * Given a dimension's name, return the bounding coordinates for that dimension. The method checks
   * if the dimension is var-sized or not, and it works for both cases.
   *
   * @param name THe dimension's name
   * @return A Pair that contains the dimension's bounds
   * @exception TileDBError A TileDB exception
   */
  public Pair getNonEmptyDomainFromName(String name) throws TileDBError {
    checkIsOpen();
    try (Domain domain = schema.getDomain();
        NativeArray domainArray = new NativeArray(ctx, 2, domain.getDimension(name).getType())) {

      if (domain.getDimension(name).isVar()) return this.getNonEmptyDomainVarFromName(name);

      SWIGTYPE_p_int emptyp = tiledb.new_intp();
      try {
        ctx.handleError(
            tiledb.tiledb_array_get_non_empty_domain_from_name(
                ctx.getCtxp(), arrayp, name, domainArray.toVoidPointer(), emptyp));
        if (tiledb.intp_value(emptyp) == 1) {
          return Pair.empty();
        }
      } finally {
        tiledb.delete_intp(emptyp);
      }
      return new Pair(domainArray.getItem(0), domainArray.getItem(1));
    }
  }

  /**
   * Retrieves the non-empty domain range sizes from an array for a given dimension index. This is
   * the union of the non-empty domains of the array fragments on the given dimension. Applicable
   * only to var-sized dimensions.
   *
   * @param index The dimension index
   * @return The non-empty domain range sizes
   * @throws TileDBError A TileDB exception
   */
  public Pair<BigInteger, BigInteger> getNonEmptyDomainVarSizeFromIndex(long index)
      throws TileDBError {
    SWIGTYPE_p_int emptyp = tiledb.new_intp();
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();

    ctx.handleError(
        tiledb.tiledb_array_get_non_empty_domain_var_size_from_index(
            ctx.getCtxp(), arrayp, index, startSize, endSize, emptyp));

    return new Pair(tiledb.ullp_value(startSize), tiledb.ullp_value(endSize));
  }

  /**
   * Retrieves the non-empty domain range sizes from an array for a given dimension name. This is
   * the union of the non-empty domains of the array fragments on the given dimension. Applicable
   * only to var-sized dimensions.
   *
   * @param name The dimension name
   * @return The non-empty domain range sizes
   * @throws TileDBError A TileDB exception
   */
  public Pair<BigInteger, BigInteger> getNonEmptyDomainVarSizeFromName(String name)
      throws TileDBError {
    SWIGTYPE_p_int emptyp = tiledb.new_intp();
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();

    ctx.handleError(
        tiledb.tiledb_array_get_non_empty_domain_var_size_from_name(
            ctx.getCtxp(), arrayp, name, startSize, endSize, emptyp));

    return new Pair(tiledb.ullp_value(startSize), tiledb.ullp_value(endSize));
  }

  /**
   * Retrieves the non-empty domain from an array for a given dimension index. This is the union of
   * the non-empty domains of the array fragments on the given dimension. Applicable only to
   * var-sized dimensions.
   *
   * @param index The dimension index
   * @return The non-empty domain
   * @throws TileDBError A TileDB exception
   */
  public Pair<String, String> getNonEmptyDomainVarFromIndex(long index) throws TileDBError {
    SWIGTYPE_p_int emptyp = tiledb.new_intp();

    Dimension dim = this.schema.getDomain().getDimension(index);
    Pair<BigInteger, BigInteger> size = this.getNonEmptyDomainVarSizeFromIndex(index);

    Datatype dimType = dim.getType();
    int startSize = size.getFirst().intValue();
    int endSize = size.getSecond().intValue();

    NativeArray start = new NativeArray(ctx, startSize, dimType);
    NativeArray end = new NativeArray(ctx, endSize, dimType);

    ctx.handleError(
        tiledb.tiledb_array_get_non_empty_domain_var_from_index(
            ctx.getCtxp(), arrayp, index, start.toVoidPointer(), end.toVoidPointer(), emptyp));

    return new Pair(
        new String((byte[]) start.toJavaArray()), new String((byte[]) end.toJavaArray()));
  }

  /**
   * Retrieves the non-empty domain from an array for a given dimension name. This is the union of
   * the non-empty domains of the array fragments on the given dimension. Applicable only to
   * var-sized dimensions.
   *
   * @param name The dimension name
   * @return The non-empty domain
   * @throws TileDBError A TileDB exception
   */
  public Pair<String, String> getNonEmptyDomainVarFromName(String name) throws TileDBError {
    SWIGTYPE_p_int emptyp = tiledb.new_intp();

    Dimension dim = this.schema.getDomain().getDimension(name);

    Pair<BigInteger, BigInteger> size = this.getNonEmptyDomainVarSizeFromName(name);

    Datatype dimType = dim.getType();
    int startSize = size.getFirst().intValue();
    int endSize = size.getSecond().intValue();

    NativeArray start = new NativeArray(ctx, startSize, dimType);
    NativeArray end = new NativeArray(ctx, endSize, dimType);

    ctx.handleError(
        tiledb.tiledb_array_get_non_empty_domain_var_from_name(
            ctx.getCtxp(), arrayp, name, start.toVoidPointer(), end.toVoidPointer(), emptyp));

    return new Pair(
        new String((byte[]) start.toJavaArray()), new String((byte[]) end.toJavaArray()));
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
  @Deprecated
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

      for (Dimension dim : domain.getDimensions()) {
        ret.put(
            dim.getName(),
            new Pair(
                0l,
                val_nbytes.getitem(0).longValue()
                    / tiledb.tiledb_datatype_size(dim.getType().toSwigEnum()).longValue()));
      }
    } finally {
      off_nbytes.delete();
      val_nbytes.delete();
    }
    return ret;
  }

  /**
   * Get a metadata key-value item from an open array. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @param key a key to retrieve from the metadata key-value
   * @return NativeArray which contains the metadata
   * @throws TileDBError A TileDB exception
   */
  public NativeArray getMetadata(String key) throws TileDBError {
    return getMetadata(key, null);
  }

  /**
   * Get a metadata key-value item from an open array. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @param key a key to retrieve from the metadata key-value
   * @param nativeType The Datatype
   * @return NativeArray which contains the metadata
   * @throws TileDBError A TileDB exception
   */
  public NativeArray getMetadata(String key, Datatype nativeType) throws TileDBError {
    checkIsOpen();

    SWIGTYPE_p_p_void resultArrpp = tiledb.new_voidpArray(0);
    SWIGTYPE_p_unsigned_int value_num = tiledb.new_uintp();
    SWIGTYPE_p_tiledb_datatype_t value_type =
        (nativeType == null)
            ? tiledb.new_tiledb_datatype_tp()
            : tiledb.copy_tiledb_datatype_tp(nativeType.toSwigEnum());

    ctx.handleError(
        tiledb.tiledb_array_get_metadata(
            ctx.getCtxp(), arrayp, key, value_type, value_num, resultArrpp));

    Datatype derivedNativeType = Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(value_type));

    long value = tiledb.uintp_value(value_num);
    NativeArray result = new NativeArray(ctx, derivedNativeType, resultArrpp, (int) value);

    tiledb.delete_uintp(value_num);
    tiledb.delete_tiledb_datatype_tp(value_type);

    return result;
  }

  /**
   * Deletes a metadata key-value item from an open array. The array must be opened in WRITE mode,
   * otherwise the function will error out.
   *
   * @param key a key to delete from the metadata key-value
   * @throws TileDBError A TileDB exception
   */
  public void deleteMetadata(String key) throws TileDBError {
    checkIsOpen();

    ctx.handleError(tiledb.tiledb_array_delete_metadata(ctx.getCtxp(), arrayp, key));
  }

  /**
   * Gets the number of metadata items in an open array. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @return the number of metadata items
   * @throws TileDBError A TileDB exception
   */
  public BigInteger getMetadataNum() throws TileDBError {
    checkIsOpen();

    SWIGTYPE_p_unsigned_long_long value_num = tiledb.new_ullp();

    ctx.handleError(tiledb.tiledb_array_get_metadata_num(ctx.getCtxp(), arrayp, value_num));

    BigInteger value = tiledb.ullp_value(value_num);

    tiledb.delete_ullp(value_num);

    return value;
  }

  /**
   * Gets a metadata item from an open array using an index. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @param index index to retrieve metadata from
   * @return a pair, key and the metadata
   * @throws TileDBError A TileDB exception
   */
  public Pair<String, NativeArray> getMetadataFromIndex(long index) throws TileDBError {
    return getMetadataFromIndex(BigInteger.valueOf(index));
  }

  /**
   * Gets a metadata item from an open array using an index. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @param index index to retrieve metadata from
   * @return a pair, key and the metadata
   * @throws TileDBError A TileDB exception
   */
  public Pair<String, NativeArray> getMetadataFromIndex(BigInteger index) throws TileDBError {
    checkIsOpen();

    SWIGTYPE_p_p_char key = tiledb.new_charpp();
    SWIGTYPE_p_unsigned_int key_len = tiledb.new_uintp();
    SWIGTYPE_p_tiledb_datatype_t value_type = tiledb.new_tiledb_datatype_tp();
    SWIGTYPE_p_unsigned_int value_num = tiledb.new_uintp();
    SWIGTYPE_p_p_void value = tiledb.new_voidpArray(0);

    ctx.handleError(
        tiledb.tiledb_array_get_metadata_from_index(
            ctx.getCtxp(), arrayp, index, key, key_len, value_type, value_num, value));

    String keyString = tiledb.charpp_value(key);
    long valueLength = tiledb.uintp_value(value_num);
    Datatype nativeType = Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(value_type));

    NativeArray result = new NativeArray(ctx, nativeType, value, (int) valueLength);

    tiledb.delete_uintp(value_num);
    tiledb.delete_uintp(key_len);
    tiledb.delete_charpp(key);
    tiledb.delete_tiledb_datatype_tp(value_type);

    return new Pair<String, NativeArray>(keyString, result);
  }

  /**
   * Returns a HashMap with all array metadata in a key-value manner.
   *
   * @return The metadata
   * @throws TileDBError
   */
  public Map<String, Object> getMetadataMap() throws TileDBError {
    Map<String, Object> result = new HashMap<>();

    for (int i = 0; i < this.getMetadataNum().intValue(); ++i) {
      Pair meta = this.getMetadataFromIndex(i);
      String key = meta.getFirst().toString();
      NativeArray value = (NativeArray) meta.getSecond();
      if (value.getSize() == 1) result.put(key, value.getItem(0));
      else result.put(key, value);
    }

    return result;
  }

  /**
   * Checks if the key is present in the Array metadata. The array must be opened in READ mode,
   * otherwise the function will error out.
   *
   * @param key a key to retrieve from the metadata key-value
   * @return true if the key is present in the metadata, false if it is not
   * @throws TileDBError A TileDB exception
   */
  public Boolean hasMetadataKey(String key) throws TileDBError {
    checkIsOpen();

    SWIGTYPE_p_tiledb_datatype_t value_type = tiledb.new_tiledb_datatype_tp();
    SWIGTYPE_p_int has_key = tiledb.new_intp();

    ctx.handleError(
        tiledb.tiledb_array_has_metadata_key(ctx.getCtxp(), arrayp, key, value_type, has_key));

    Boolean result = tiledb.intp_value(has_key) > 0;

    tiledb.delete_intp(has_key);
    tiledb.delete_tiledb_datatype_tp(value_type);

    return result;
  }

  /**
   * Puts a metadata key-value item to an open array. The array must be opened in WRITE mode,
   * otherwise the function will error out.
   *
   * @param key a key to assign to the input value
   * @param buffer the metadata to put into the Array metadata
   * @throws TileDBError A TileDB exception
   */
  public void putMetadata(String key, Object buffer) throws TileDBError {
    putMetadata(key, new NativeArray(ctx, buffer, buffer.getClass()));
  }

  /**
   * Puts a metadata key-value item to an open array. The array must be opened in WRITE mode,
   * otherwise the function will error out.
   *
   * @param key a key to assign to the input value
   * @param value the metadata to put into the Array metadata
   * @throws TileDBError A TileDB exception
   */
  public void putMetadata(String key, NativeArray value) throws TileDBError {
    checkIsOpen();

    ctx.handleError(
        tiledb.tiledb_array_put_metadata(
            ctx.getCtxp(),
            arrayp,
            key,
            value.getNativeType().toSwigEnum(),
            value.getSize(),
            value.toVoidPointer()));
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

  /**
   * Reopens a TileDB array (the array must be already open). This is useful when the array got
   * updated after it got opened. To sync-up with the updates, the user must either close the array
   * and open with `tiledb_array_open`, or just use `reopen` without closing. This function will be
   * generally faster than the former alternative.
   *
   * <p>Note: reopening encrypted arrays does not require the encryption key.
   *
   * @throws TileDBError
   */
  public void reopen() throws TileDBError {
    ctx.handleError(tiledb.tiledb_array_reopen(ctx.getCtxp(), getArrayp()));
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
