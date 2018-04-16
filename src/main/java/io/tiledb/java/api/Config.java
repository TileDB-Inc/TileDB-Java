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

public class Config implements Finalizable {
  private SWIGTYPE_p_p_tiledb_config_t configpp;
  private SWIGTYPE_p_tiledb_config_t configp;

  /**
   * Constructor that creates a new C config object.
   */
  public Config() throws TileDBError {
    create_config();
  }

  /**
   * Constructor that takes as input a filename (URI) that stores the config
   * parameters. The file must have the following (text) format:
   * <p>
   * {parameter} {value}
   * <p>
   * Anything following a `#` character is considered a comment and, thus, is
   * ignored.
   * <p>
   * See `set` for the various TileDB config parameters and allowed values.
   *
   * @param filename The name of the file where the parameters will be read
   *                 from.
   */
  public Config(String filename) throws TileDBError {
    create_config();
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_load_from_file(configp, filename, errorpp);
    check_config_error(rc, errorpp);
  }

  /**
   * Delete the native config object.
   */
  public void free() throws TileDBError {
    tiledb.tiledb_config_free(configpp);
  }

  /**
   * Get a parameter from the configuration by key.
   *
   * @param parameter Key
   * @return Value
   */
  public String get(String parameter) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    SWIGTYPE_p_p_char valuepp = tiledb.new_charpp();
    int rc = tiledb.tiledb_config_get(configp, parameter, valuepp, errorpp);
    check_config_error(rc, errorpp);
    String value = tiledb.charpp_value(valuepp);
    tiledb.delete_charpp(valuepp);
    return value;
  }

  /**
   * Sets a config parameter-value pair.
   * <p>
   * **Parameters**
   * <p>
   * - `sm.tile_cache_size` <br>
   * The tile cache size in bytes. Any `uint64_t` value is acceptable.
   * - `sm.array_schema_cache_size` <br>
   * The array schema cache size in bytes. Any `uint64_t` value is
   * acceptable.
   * - `sm.fragment_metadata_cache_size` <br>
   * The fragment metadata cache size in bytes. Any `uint64_t` value is
   * acceptable.
   * - `vfs.s3.region` <br>
   * The S3 region, if S3 is enabled.
   * - `vfs.s3.scheme` <br>
   * The S3 scheme (`http` or `https`), if S3 is enabled.
   * - `vfs.s3.endpoint_override` <br>
   * The S3 endpoint, if S3 is enabled.
   * - `vfs.s3.use_virtual_addressing` <br>
   * The S3 use of virtual addressing (`true` or `false`), if S3 is enabled.
   * - `vfs.s3.file_buffer_size` <br>
   * The file buffer size (in bytes) used in S3 writes, if S3 is enables. Any
   * `uint64_t` value is acceptable.
   * - `vfs.s3.connect_timeout_ms` <br>
   * The connection timeout in ms. Any `long` value is acceptable.
   * - `vfs.s3.request_timeout_ms` <br>
   * The request timeout in ms. Any `long` value is acceptable.
   * - `vfs.hdfs.name_node` <br>
   * Name node for HDFS.
   * - `vfs.hdfs.username` <br>
   * HDFS username.
   * - `vfs.hdfs.kerb_ticket_cache_path` <br>
   * HDFS kerb ticket cache path.
   */
  public void set(String parameter, String value) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_set(configp, parameter, value, errorpp);
    check_config_error(rc, errorpp);
  }

  /**
   * Unsets a config parameter.
   */
  public void unset(String parameter) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_unset(configp, parameter, errorpp);
    check_config_error(rc, errorpp);
  }

  /**
   * Returns all config parameters.
   */
  public HashMap<String, String> parameters(String prefix) throws TileDBError {
    HashMap<String, String> result = new HashMap<String, String>();
    SWIGTYPE_p_p_tiledb_config_iter_t iterpp = Utils.new_tiledb_config_iter_tpp();
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_iter_create(configp, iterpp, prefix, errorpp);
    check_config_error(rc, errorpp);

    SWIGTYPE_p_tiledb_config_iter_t iterp = Utils.tiledb_config_iter_tpp_value(iterpp);
    while (hasMoreParams(iterp)) {
      errorpp = Utils.new_tiledb_error_tpp();
      SWIGTYPE_p_p_char parampp = tiledb.new_charpp();
      SWIGTYPE_p_p_char valuepp = tiledb.new_charpp();
      rc = tiledb.tiledb_config_iter_here(iterp, parampp, valuepp, errorpp);
      check_config_error(rc, errorpp);
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
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_save_to_file(configp, filename, errorpp);
    check_config_error(rc, errorpp);
  }

  private void next(SWIGTYPE_p_tiledb_config_iter_t iterp) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_iter_next(iterp, errorpp);
    check_config_error(rc, errorpp);
  }

  private boolean hasMoreParams(SWIGTYPE_p_tiledb_config_iter_t iterp) throws TileDBError {
    SWIGTYPE_p_int done = tiledb.new_intp();
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_iter_done(iterp, done, errorpp);
    check_config_error(rc, errorpp);
    return tiledb.intp_value(done) == 0;
  }

  private void create_config() throws TileDBError {
    this.configpp = Utils.new_tiledb_config_tpp();
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_create(configpp, errorpp);
    check_config_error(rc, errorpp);
    this.configp = Utils.tiledb_config_tpp_value(configpp);
  }

  private void check_config_error(int returnCode, SWIGTYPE_p_p_tiledb_error_t error) throws TileDBError {
    if (returnCode == tiledb.TILEDB_ERR) {
      SWIGTYPE_p_p_char msgpp = tiledb.new_charpp();
      tiledb.tiledb_error_message(Utils.tiledb_error_tpp_value(error), msgpp);
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

  @Override
  protected void finalize() throws Throwable {
    free();
    super.finalize();
  }
}
