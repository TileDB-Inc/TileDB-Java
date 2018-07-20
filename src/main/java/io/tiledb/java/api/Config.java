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
 *
 * This file defines the JAVA API for the TileDB Config object.
 */

package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

import java.util.HashMap;

/**
 * Carries configuration parameters for a context.
 *
 * **Example:**
 *
 * @code{.java}
 * Config conf = new Config();
 * conf.set("vfs.s3.region", "us-east-1a");
 * conf.set("vfs.s3.use_virtual_addressing", "true");
 * Context ctx = new Context(conf);
 * @endcode
 * */
public class Config implements AutoCloseable {
  private SWIGTYPE_p_p_tiledb_config_t configpp;
  private SWIGTYPE_p_tiledb_config_t configp;

  /**
   * Constructor that creates a new config object with default configuration values.
   */
  public Config() throws TileDBError {
    createConfig();
  }

  /**
   * Constructor that takes as input a filename (URI) that stores the config
   * parameters. The file must have the following (text) format:
   *
   * `{parameter} {value}`
   *
   * Anything following a `#` character is considered a comment and, thus, is
   * ignored.
   *
   * See `Config.set` for the various TileDB config parameters and allowed
   * values.
   *
   * @param filename The name of the file where the parameters will be read
   *     from.
   */
  public Config(String filename) throws TileDBError {
    createConfig();
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_load_from_file(configp, filename, errorpp);
    checkConfigError(rc, errorpp);
  }

  /**
   * Get a parameter from the configuration by name.
   *
   * @param parameter Name of parameter
   * @return Value
   */
  public String get(String parameter) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    SWIGTYPE_p_p_char valuepp = tiledb.new_charpp();
    int rc = tiledb.tiledb_config_get(configp, parameter, valuepp, errorpp);
    checkConfigError(rc, errorpp);
    String value = tiledb.charpp_value(valuepp);
    tiledb.delete_charpp(valuepp);
    return value;
  }

  /**
   * Sets a config parameter-value pair.
   *
   * **Parameters**
   *
   * - `sm.dedup_coords` <br>
   *    If `true`, cells with duplicate coordinates will be removed during
   *    sparse array writes. Note that ties during deduplication are
   *    arbitrary. <br>
   *    **Default**: false
   * - `sm.check_coord_dups` <br>
   *    This is applicable only if `sm.dedup_coords` is `false`.
   *    If `true`, an error will be thrown if there are cells with duplicate
   *    coordinates during sparse array writes. If `false` and there are
   *    duplicates, the duplicates will be written without errors, but the
   *    TileDB behavior could be unpredictable. <br>
   *    **Default**: true
   * - `sm.tile_cache_size` <br>
   *    The tile cache size in bytes. Any `uint64_t` value is acceptable. <br>
   *    **Default**: 10,000,000
   * - `sm.array_schema_cache_size` <br>
   *    The array schema cache size in bytes. Any `uint64_t` value is
   *    acceptable. <br>
   *    **Default**: 10,000,000
   * - `sm.fragment_metadata_cache_size` <br>
   *    The fragment metadata cache size in bytes. Any `uint64_t` value is
   *    acceptable. <br>
   *    **Default**: 10,000,000
   * - `sm.enable_signal_handlers` <br>
   *    Whether or not TileDB will install signal handlers. <br>
   *    **Default**: true
   * - `sm.num_async_threads` <br>
   *    The number of threads allocated for async queries. <br>
   *    **Default**: 1
   * - `sm.num_tbb_threads` <br>
   *    The number of threads allocated for the TBB thread pool (if TBB is
   *    enabled). Note: this is a whole-program setting. Usually this should not
   *    be modified from the default. See also the documentation for TBB's
   *    `task_scheduler_init` class.<br>
   *    **Default**: TBB automatic
   * - `vfs.num_threads` <br>
   *    The number of threads allocated for VFS operations (any backend), per
   *    VFS instance. <br>
   *    **Default**: number of cores
   * - `vfs.min_parallel_size` <br>
   *    The minimum number of bytes in a parallel VFS operation
   *    (except parallel S3 writes, which are controlled by
   *    `vfs.s3.multipart_part_size`.) <br>
   *    **Default**: 10MB
   * - `vfs.file.max_parallel_ops` <br>
   *    The maximum number of parallel operations on objects with `file:///`
   *    URIs. <br>
   *    **Default**: `vfs.num_threads`
   * - `vfs.s3.region` <br>
   *    The S3 region, if S3 is enabled. <br>
   *    **Default**: us-east-1
   * - `vfs.s3.scheme` <br>
   *    The S3 scheme (`http` or `https`), if S3 is enabled. <br>
   *    **Default**: https
   * - `vfs.s3.endpoint_override` <br>
   *    The S3 endpoint, if S3 is enabled. <br>
   *    **Default**: ""
   * - `vfs.s3.use_virtual_addressing` <br>
   *    The S3 use of virtual addressing (`true` or `false`), if S3 is
   *    enabled. <br>
   *    **Default**: true
   * - `vfs.s3.max_parallel_ops` <br>
   *    The maximum number of S3 backend parallel operations. <br>
   *    **Default**: `vfs.num_threads`
   * - `vfs.s3.multipart_part_size` <br>
   *    The part size (in bytes) used in S3 multipart writes.
   *    Any `uint64_t` value is acceptable. Note: `vfs.s3.multipart_part_size *
   *    vfs.s3.max_parallel_ops` bytes will be buffered before issuing multipart
   *    uploads in parallel. <br>
   *    **Default**: 5MB
   * - `vfs.s3.connect_timeout_ms` <br>
   *    The connection timeout in ms. Any `long` value is acceptable. <br>
   *    **Default**: 3000
   * - `vfs.s3.connect_max_tries` <br>
   *    The maximum tries for a connection. Any `long` value is acceptable. <br>
   *    **Default**: 5
   * - `vfs.s3.connect_scale_factor` <br>
   *    The scale factor for exponential backofff when connecting to S3.
   *    Any `long` value is acceptable. <br>
   *    **Default**: 25
   * - `vfs.s3.request_timeout_ms` <br>
   *    The request timeout in ms. Any `long` value is acceptable. <br>
   *    **Default**: 3000
   * - `vfs.s3.proxy_host` <br>
   *    The proxy host. <br>
   *    **Default**: ""
   * - `vfs.s3.proxy_port` <br>
   *    The proxy port. <br>
   *    **Default**: 0
   * - `vfs.s3.proxy_scheme` <br>
   *    The proxy scheme. <br>
   *    **Default**: "https"
   * - `vfs.s3.proxy_username` <br>
   *    The proxy username. Note: this parameter is not serialized by
   *    `tiledb_config_save_to_file`. <br>
   *    **Default**: ""
   * - `vfs.s3.proxy_password` <br>
   *    The proxy password. Note: this parameter is not serialized by
   *    `tiledb_config_save_to_file`. <br>
   *    **Default**: ""
   * - `vfs.hdfs.name_node"` <br>
   *    Name node for HDFS. <br>
   *    **Default**: ""
   * - `vfs.hdfs.username` <br>
   *    HDFS username. <br>
   *    **Default**: ""
   * - `vfs.hdfs.kerb_ticket_cache_path` <br>
   *    HDFS kerb ticket cache path. <br>
   *    **Default**: ""
   */
  public void set(String parameter, String value) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_set(configp, parameter, value, errorpp);
    checkConfigError(rc, errorpp);
  }

  /**
   * Resets a config parameter to its default value.
   * @param parameter Name of parameter
   * @throws TileDBError
   */
  public void unset(String parameter) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_unset(configp, parameter, errorpp);
    checkConfigError(rc, errorpp);
  }

  /**
   * Returns all config parameters starting with a prefix.
   * @param prefix A parameter prefix. Use "" to get all parameters.
   * @return HashMap containing all parametes as key-value pairs.
   * @throws TileDBError
   */
  public HashMap<String, String> parameters(String prefix) throws TileDBError {
    HashMap<String, String> result = new HashMap<String, String>();
    SWIGTYPE_p_p_tiledb_config_iter_t iterpp = tiledb.new_tiledb_config_iter_tpp();
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_iter_alloc(configp, prefix, iterpp, errorpp);
    checkConfigError(rc, errorpp);

    SWIGTYPE_p_tiledb_config_iter_t iterp = tiledb.tiledb_config_iter_tpp_value(iterpp);
    while (hasMoreParams(iterp)) {
      errorpp = tiledb.new_tiledb_error_tpp();
      SWIGTYPE_p_p_char parampp = tiledb.new_charpp();
      SWIGTYPE_p_p_char valuepp = tiledb.new_charpp();
      rc = tiledb.tiledb_config_iter_here(iterp, parampp, valuepp, errorpp);
      checkConfigError(rc, errorpp);
      result.put(tiledb.charpp_value(parampp), tiledb.charpp_value(valuepp));
      next(iterp);
    }
    return result;
  }

  /**
   * Saves config parameters to a specified file
   *
   * @param filename The name of the file where the parameters will be written.
   */
  public void saveToFile(String filename) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_save_to_file(configp, filename, errorpp);
    checkConfigError(rc, errorpp);
  }

  private void next(SWIGTYPE_p_tiledb_config_iter_t iterp) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_iter_next(iterp, errorpp);
    checkConfigError(rc, errorpp);
  }

  private boolean hasMoreParams(SWIGTYPE_p_tiledb_config_iter_t iterp) throws TileDBError {
    SWIGTYPE_p_int done = tiledb.new_intp();
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_iter_done(iterp, done, errorpp);
    checkConfigError(rc, errorpp);
    return tiledb.intp_value(done) == 0;
  }

  private void createConfig() throws TileDBError {
    this.configpp = tiledb.new_tiledb_config_tpp();
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_alloc(configpp, errorpp);
    checkConfigError(rc, errorpp);
    this.configp = tiledb.tiledb_config_tpp_value(configpp);
  }

  private void checkConfigError(int returnCode, SWIGTYPE_p_p_tiledb_error_t error) throws TileDBError {
    if (returnCode == tiledb.TILEDB_ERR) {
      SWIGTYPE_p_p_char msgpp = tiledb.new_charpp();
      tiledb.tiledb_error_message(tiledb.tiledb_error_tpp_value(error), msgpp);
      String msg = tiledb.charpp_value(msgpp);
      tiledb.delete_charpp(msgpp);
      tiledb.tiledb_error_free(error);
      throw new TileDBError("Config Error: " + msg);
    }
  }

  private SWIGTYPE_p_p_tiledb_config_t getConfig() {
    return configpp;
  }

  private void setConfig(SWIGTYPE_p_p_tiledb_config_t config) {
    this.configpp = config;
  }

  protected SWIGTYPE_p_p_tiledb_config_t getConfigpp() {
    return configpp;
  }

  protected void setConfigpp(SWIGTYPE_p_p_tiledb_config_t configpp) {
    this.configpp = configpp;
  }

  protected SWIGTYPE_p_tiledb_config_t getConfigp() {
    return configp;
  }

  protected void setConfigp(SWIGTYPE_p_tiledb_config_t configp) {
    this.configp = configp;
  }

  /**
   * Delete the native object.
   */
  public void close() throws TileDBError {
    if(configp!=null)
      tiledb.tiledb_config_free(configpp);
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
