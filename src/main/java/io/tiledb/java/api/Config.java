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
 * Sets configuration parameters for a TileDB Context.
 *
 * <pre><b>Example:</b>
 *   Config conf = new Config();
 *   conf.set("vfs.s3.region", "us-east-1a");
 *   conf.set("vfs.s3.use_virtual_addressing", "true");
 *   Context ctx = new Context(conf);
 * </pre>
 * */
public class Config implements AutoCloseable {

  private SWIGTYPE_p_p_tiledb_config_t configpp;
  private SWIGTYPE_p_tiledb_config_t configp;

  /**
   * Constructor that creates a new config object with default configuration values.
   *
   * @exception TileDBError A TileDB exception
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
   * @param filename local path to config file
   * @exception TileDBError A TileDB exception
   */
  public Config(String filename) throws TileDBError {
    createConfig();
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_load_from_file(configp, filename, errorpp);
    checkConfigError(rc, errorpp);
  }

  /**
   * Get a parameter from the Config by name.
   * <br>
   * <a href="https://docs.tiledb.io/en/stable/tutorials/config.html#summary-of-parameters">
   * Summary of config parameters</a>
   * <br>
   *
   * @param parameter parameter name
   * @return config parameter string value
   * @exception TileDBError A TileDB exception
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
   * <br>
   * <a href="https://docs.tiledb.io/en/stable/tutorials/config.html#summary-of-parameters">
   * Summary of config parameters</a>
   * <br>
   *
   * @param parameter config parameter to set
   * @param value config parameter value to set
   * @exception TileDBError A TileDB exception
   */
  public void set(String parameter, String value) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_set(configp, parameter, value, errorpp);
    checkConfigError(rc, errorpp);
  }

  /**
   * Resets a config parameter to its default value.
   *
   * @param parameter config parameter to reset
   * @throws TileDBError A TileDB exception
   */
  public void unset(String parameter) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    int rc = tiledb.tiledb_config_unset(configp, parameter, errorpp);
    checkConfigError(rc, errorpp);
  }
   
  /**
   * Returns a map of TileDB config parameter, value pairs
   *
   * @return HashMap containing all parameters as key/value pairs
   * @throws TileDBError A TileDB exception
   */
  public HashMap<String, String> parameters() throws TileDBError {
    return parameters("");
  }

  /**
   * Returns a map of TileDB config paramter, value pairs
   * with parameter names starting with a given prefix.
   *
   * @param prefix A parameter prefix. Use "" to get all parameters.
   * @return HashMap containing all parameters as key-value pairs.
   * @throws TileDBError A TileDB exception
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
   * Saves config parameters to a local file path.
   *
   * @param filename The name of the file where the parameters will be written.
   * @exception TileDBError A TileDB exception
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
   * Free's native TileDB resources associated with the Config object.
   */
  public void close() throws TileDBError {
    if(configp != null) {
      tiledb.tiledb_config_free(configpp);
    }
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
}
