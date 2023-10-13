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
  private Domain domain;
  private QueryType query_type;

  private SWIGTYPE_p_tiledb_array_t arrayp;
  private SWIGTYPE_p_p_tiledb_array_t arraypp;

  /**
   * Constructs an Array object opening the array for reading.
   *
   * <pre><b>Example:</b>
   * {@code
   *
   * String key = "0123456789abcdeF0123456789abcdeF";
   * Config config = new Config();
   * config.set("sm.encryption_type", "AES_256_GCM");
   * config.set("sm.encryption_key", key);
   * Context ctx = new Context(config);
   * Array array new Array(ctx, "s3://bucket-name/array-name");
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
   * Constructs an Array object, opening the array for the given query type at a user-given
   * timestamp (time-travelling).
   *
   * <p>Example
   *
   * <p>String key = "0123456789abcdeF0123456789abcdeF"; Config config = new Config();
   * config.set("sm.encryption_type", "AES_256_GCM"); config.set("sm.encryption_key", key); Context
   * ctx = new Context(config); Array array new Array(ctx, "s3://bucket-name/array-name",
   * TILEDB_READ, end);
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @param query_type Query type to open the array for.
   * @param timestamp_end The end timestamp
   * @exception TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri, QueryType query_type, BigInteger timestamp_end)
      throws TileDBError {
    openArray(ctx, uri, query_type, null, timestamp_end);
  }

  /**
   * Constructs an Array object opening the array for reading at a user-given interval
   * (time-travelling).
   *
   * <p>Example
   *
   * <p>String key = "0123456789abcdeF0123456789abcdeF"; Config config = new Config();
   * config.set("sm.encryption_type", "AES_256_GCM"); config.set("sm.encryption_key", key); Context
   * ctx = new Context(config); Array array new Array(ctx, "s3://bucket-name/array-name",
   * TILEDB_READ, end, start);
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @param query_type Query type to open the array for.
   * @param timestamp_start The start timestamp
   * @param timestamp_end The end timestamp
   * @exception TileDBError A TileDB exception
   */
  public Array(
      Context ctx,
      String uri,
      QueryType query_type,
      BigInteger timestamp_start,
      BigInteger timestamp_end)
      throws TileDBError {
    openArray(ctx, uri, query_type, timestamp_start, timestamp_end);
  }

  /**
   * Constructs an Array object, opening the encrypted array for the given query type.
   *
   * <pre><b>Example:</b>
   * {@code
   * String key = "0123456789abcdeF0123456789abcdeF";
   * Config config = new Config();
   * config.set("sm.encryption_type", "AES_256_GCM");
   * config.set("sm.encryption_key", key);
   * Context ctx = new Context(config);
   * Array array new Array(ctx, "s3://bucket-name/array-name", TILEDB_READ);
   * }
   * </pre>
   *
   * @param ctx TileDB context
   * @param uri The array URI
   * @param query_type Query type to open the array for
   * @throws TileDBError A TileDB exception
   */
  public Array(Context ctx, String uri, QueryType query_type) throws TileDBError {
    openArray(ctx, uri, query_type, null, null);
  }

  private synchronized void openArray(
      Context ctx,
      String uri,
      QueryType query_type,
      BigInteger timestamp_start,
      BigInteger timestamp_end)
      throws TileDBError {
    SWIGTYPE_p_p_tiledb_array_t _arraypp = tiledb.new_tiledb_array_tpp();
    try {
      ctx.handleError(tiledb.tiledb_array_alloc(ctx.getCtxp(), uri, _arraypp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_tpp(_arraypp);
      throw err;
    }
    SWIGTYPE_p_tiledb_array_t _arrayp = tiledb.tiledb_array_tpp_value(_arraypp);

    if (timestamp_start != null) {
      Util.checkBigIntegerRange(timestamp_start);
      ctx.handleError(
          tiledb.tiledb_array_set_open_timestamp_start(ctx.getCtxp(), _arrayp, timestamp_start));
    }

    if (timestamp_end != null) {
      Util.checkBigIntegerRange(timestamp_end);
      ctx.handleError(
          tiledb.tiledb_array_set_open_timestamp_end(ctx.getCtxp(), _arrayp, timestamp_end));
    }

    try {
      ctx.handleError(tiledb.tiledb_array_open(ctx.getCtxp(), _arrayp, query_type.toSwigEnum()));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_tpp(_arraypp);
      throw err;
    }
    ArraySchema _schema = new ArraySchema(ctx, uri);

    this.ctx = ctx;
    this.uri = uri;
    this.query_type = query_type;
    this.schema = _schema;
    this.domain = this.schema.getDomain();
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
    consolidate(ctx, uri, ctx.getConfig());
  }

  /**
   * Consolidates the given fragment URIs into a single fragment.
   *
   * <p>Note: This API needs to be used with caution until we implement consolidation with
   * timestamps. For now, if the non-empty domain of the consolidated fragments overlap anything in
   * the fragments that come in between, this could lead to unpredictable behavior.
   *
   * @param ctx A TileDB Context
   * @param uri URI string to TileDB array
   * @param fragURIs The URIs of the fragments to consolidate
   * @param numFragments The number of fragments to consolidate
   * @param config The TileDB config
   */
  public static void consolidate(
      Context ctx, String uri, String[] fragURIs, int numFragments, Config config)
      throws TileDBError {
    // TODO
    //    NativeArray uris = new NativeArray(ctx, fragURIs[0], Datatype.TILEDB_STRING_ASCII,
    // numFragments);
    //
    //    try {
    //      ctx.handleError(
    //          tiledb.tiledb_array_consolidate_fragments(
    //              ctx.getCtxp(),
    //              uri,
    //              uris.toCharpp(),
    //              BigInteger.valueOf(numFragments),
    //              config.getConfigp()));
    //    } catch (TileDBError error) {
    //      throw error;
    //    }
  }

  /**
   * Consolidates the fragments of an array into a single fragment.
   *
   * <p>All queries to the array before consolidation must be finalized before consolidation can
   * begin. Consolidation temporarily acquires an exclusive lock on the array when finalizing the
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
   * @param config A TileDB config object with configuration parameters for the consolidation
   * @throws TileDBError
   */
  public static void consolidate(Context ctx, String uri, Config config) throws TileDBError {
    try {
      ctx.handleError(tiledb.tiledb_array_consolidate(ctx.getCtxp(), uri, config.getConfigp()));
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * Evolves the schema of an array.
   *
   * <p>**Example:** ArraySchemaEvolution schemaEvolution = new ArraySchemaEvolution(ctx);
   * schemaEvolution.dropAttribute("attName"); array.evolve(ctx, schemaEvolution);
   *
   * @param ctx A TileDb context
   * @param schemaEvolution The schemaEvolution object
   * @throws TileDBError
   */
  public void evolve(Context ctx, ArraySchemaEvolution schemaEvolution) throws TileDBError {
    try {
      ctx.handleError(
          tiledb.tiledb_array_evolve(ctx.getCtxp(), uri, schemaEvolution.getEvolutionp()));
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * Upgrades an array to the latest format version.
   *
   * <p>**Example:** String uri = "test_array" array.upgradeVersion(ctx)
   *
   * @param ctx The TileDB context.
   * @param config Configuration parameters for the upgrade (`nullptr` means default, which will use
   *     the config from `ctx`).
   */
  public void upgradeVersion(Context ctx, Config config) throws TileDBError {
    try {
      ctx.handleError(tiledb.tiledb_array_upgrade_version(ctx.getCtxp(), uri, config.getConfigp()));
    } catch (TileDBError err) {
      throw err;
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
   * Deletes all written array data.
   *
   * @throws TileDBError
   */
  public void delete() throws TileDBError {
    this.close();
    ctx.handleError(tiledb.tiledb_array_delete(ctx.getCtxp(), this.uri));
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

    long numDims = domain.getNDim();
    for (long dimIdx = 0; dimIdx < numDims; ++dimIdx) {
      Dimension dimension = domain.getDimension(dimIdx);
      Pair p = getNonEmptyDomainFromIndex(dimIdx);
      ret.put(dimension.getName(), p);
      dimension.close();
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
    try (Dimension dim = domain.getDimension(index);
        NativeArray domainArray = new NativeArray(ctx, 2, dim.getType())) {

      if (dim.isVar()) return getNonEmptyDomainVarFromIndex(index);

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
    try (Dimension dim = domain.getDimension(name);
        NativeArray domainArray = new NativeArray(ctx, 2, dim.getType())) {

      if (dim.isVar()) return this.getNonEmptyDomainVarFromName(name);

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
    try {
      ctx.handleError(
          tiledb.tiledb_array_get_non_empty_domain_var_size_from_index(
              ctx.getCtxp(), arrayp, index, startSize, endSize, emptyp));

      return new Pair(tiledb.ullp_value(startSize), tiledb.ullp_value(endSize));
    } finally {
      tiledb.delete_ullp(startSize);
      tiledb.delete_ullp(endSize);
      tiledb.delete_intp(emptyp);
    }
  }

  /**
   * Retrieves an enumeration given the name (key)
   *
   * @param name The Enumeration name
   * @return The Enumeration
   * @throws TileDBError
   */
  public Enumeration getEnumeration(String name) throws TileDBError {
    Enumeration _enumeration;
    SWIGTYPE_p_p_tiledb_enumeration_t enumerationpp = tiledb.new_tiledb_enumeration_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_get_enumeration(ctx.getCtxp(), getArrayp(), name, enumerationpp));
      _enumeration = new Enumeration(ctx, enumerationpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_enumeration_tpp(enumerationpp);
      throw err;
    }
    return _enumeration;
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

    try {
      ctx.handleError(
          tiledb.tiledb_array_get_non_empty_domain_var_size_from_name(
              ctx.getCtxp(), arrayp, name, startSize, endSize, emptyp));

      return new Pair(tiledb.ullp_value(startSize), tiledb.ullp_value(endSize));
    } finally {
      tiledb.delete_ullp(startSize);
      tiledb.delete_ullp(endSize);
      tiledb.delete_intp(emptyp);
    }
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

    Dimension dim = domain.getDimension(index);
    Pair<BigInteger, BigInteger> size = this.getNonEmptyDomainVarSizeFromIndex(index);

    Datatype dimType = dim.getType();
    int startSize = size.getFirst().intValue();
    int endSize = size.getSecond().intValue();
    try (NativeArray start = new NativeArray(ctx, startSize, dimType);
        NativeArray end = new NativeArray(ctx, endSize, dimType)) {
      ctx.handleError(
          tiledb.tiledb_array_get_non_empty_domain_var_from_index(
              ctx.getCtxp(), arrayp, index, start.toVoidPointer(), end.toVoidPointer(), emptyp));

      return new Pair(
          new String((byte[]) start.toJavaArray()), new String((byte[]) end.toJavaArray()));
    } finally {
      tiledb.delete_intp(emptyp);
      dim.close();
    }
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

    Dimension dim = domain.getDimension(name);

    Pair<BigInteger, BigInteger> size = this.getNonEmptyDomainVarSizeFromName(name);

    Datatype dimType = dim.getType();
    int startSize = size.getFirst().intValue();
    int endSize = size.getSecond().intValue();

    try (NativeArray start = new NativeArray(ctx, startSize, dimType);
        NativeArray end = new NativeArray(ctx, endSize, dimType); ) {
      ctx.handleError(
          tiledb.tiledb_array_get_non_empty_domain_var_from_name(
              ctx.getCtxp(), arrayp, name, start.toVoidPointer(), end.toVoidPointer(), emptyp));

      return new Pair(
          new String((byte[]) start.toJavaArray()), new String((byte[]) end.toJavaArray()));
    } finally {
      tiledb.delete_intp(emptyp);
      dim.close();
    }
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

    try {
      ctx.handleError(
          tiledb.tiledb_array_get_metadata(
              ctx.getCtxp(), arrayp, key, value_type, value_num, resultArrpp));

      Datatype derivedNativeType =
          Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(value_type));

      long value = tiledb.uintp_value(value_num);
      NativeArray result = new NativeArray(ctx, derivedNativeType, resultArrpp, (int) value);
      return result;
    } finally {
      tiledb.delete_uintp(value_num);
      tiledb.delete_tiledb_datatype_tp(value_type);
      tiledb.delete_voidpArray(resultArrpp);
    }
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

    try {
      ctx.handleError(tiledb.tiledb_array_get_metadata_num(ctx.getCtxp(), arrayp, value_num));
      BigInteger value = tiledb.ullp_value(value_num);
      return value;
    } finally {
      tiledb.delete_ullp(value_num);
    }
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
    Util.checkBigIntegerRange(index);
    SWIGTYPE_p_p_char key = tiledb.new_charpp();
    SWIGTYPE_p_unsigned_int key_len = tiledb.new_uintp();
    SWIGTYPE_p_tiledb_datatype_t value_type = tiledb.new_tiledb_datatype_tp();
    SWIGTYPE_p_unsigned_int value_num = tiledb.new_uintp();
    SWIGTYPE_p_p_void value = tiledb.new_voidpArray(0);

    try {
      ctx.handleError(
          tiledb.tiledb_array_get_metadata_from_index(
              ctx.getCtxp(), arrayp, index, key, key_len, value_type, value_num, value));

      String keyString = tiledb.charpp_value(key);
      long valueLength = tiledb.uintp_value(value_num);
      Datatype nativeType = Datatype.fromSwigEnum(tiledb.tiledb_datatype_tp_value(value_type));

      NativeArray result = new NativeArray(ctx, nativeType, value, (int) valueLength);
      return new Pair<String, NativeArray>(keyString, result);

    } finally {
      tiledb.delete_uintp(value_num);
      tiledb.delete_uintp(key_len);
      tiledb.delete_charpp(key);
      tiledb.delete_tiledb_datatype_tp(value_type);
      tiledb.delete_voidpArray(value);
    }
  }

  /**
   * Deletes array fragments written between the input timestamps.
   *
   * @param timestampStart The epoch timestamp in milliseconds.
   * @param timestampEnd The epoch timestamp in milliseconds. Use UINT64_MAX for the current
   *     timestamp.
   * @throws TileDBError
   */
  public void deleteFragments(BigInteger timestampStart, BigInteger timestampEnd)
      throws TileDBError {
    Util.checkBigIntegerRange(timestampStart);
    Util.checkBigIntegerRange(timestampEnd);
    ctx.handleError(
        tiledb.tiledb_array_delete_fragments(
            ctx.getCtxp(), getArrayp(), uri, timestampStart, timestampEnd));
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

    try {
      ctx.handleError(
          tiledb.tiledb_array_has_metadata_key(ctx.getCtxp(), arrayp, key, value_type, has_key));

      Boolean result = tiledb.intp_value(has_key) > 0;
      return result;
    } finally {
      tiledb.delete_intp(has_key);
      tiledb.delete_tiledb_datatype_tp(value_type);
    }
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
   * Gets the starting timestamp used when opening (and reopening) the array. This is an inclusive
   * bound.
   *
   * @throws TileDBError
   */
  public long getOpenTimestampStart() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long start_t = tiledb.new_ullp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_get_open_timestamp_start(ctx.getCtxp(), getArrayp(), start_t));
      return tiledb.ullp_value(start_t).longValue();
    } finally {
      tiledb.delete_ullp(start_t);
    }
  }

  /**
   * Gets the ending timestamp used when opening (and reopening) the array. This is an inclusive
   * bound. If UINT64_MAX was set, this will return the timestamp at the time the array was opened.
   * If the array has not yet been opened, it will return UINT64_MAX.`
   */
  public long getOpenTimestampEnd() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long end_t = tiledb.new_ullp();
    try {
      ctx.handleError(
          tiledb.tiledb_array_get_open_timestamp_end(ctx.getCtxp(), getArrayp(), end_t));
      return tiledb.ullp_value(end_t).longValue();
    } finally {
      tiledb.delete_ullp(end_t);
    }
  }

  /** Sets the array config. */
  public void setConfig(Config config) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_set_config(ctx.getCtxp(), getArrayp(), config.getConfigp()));
  }

  /**
   * Gets the array config.
   *
   * @return The config
   * @throws TileDBError
   */
  public Config getConfig() throws TileDBError {
    SWIGTYPE_p_p_tiledb_config_t configpp = tiledb.new_tiledb_config_tpp();
    try {
      ctx.handleError(tiledb.tiledb_array_get_config(ctx.getCtxp(), getArrayp(), configpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_config_tpp(configpp);
    }
    return new Config(configpp);
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
    if (schema != null) {
      schema.close();
    }
    if (this.domain != null) {
      domain.close();
    }
    if (arrayp != null && arraypp != null) {
      tiledb.tiledb_array_close(ctx.getCtxp(), arrayp);
      tiledb.tiledb_array_free(arraypp);
      tiledb.delete_tiledb_array_tpp(arraypp);
      arrayp = null;
      arraypp = null;
    }
  }
}
