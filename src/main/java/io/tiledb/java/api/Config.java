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
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Sets configuration parameters for a TileDB Context.
 *
 * <pre><b>Example:</b>
 *   Config conf = new Config();
 *   conf.set("vfs.s3.region", "us-east-1a");
 *   conf.set("vfs.s3.use_virtual_addressing", "true");
 *   Context ctx = new Context(conf);
 * </pre>
 */
public class Config implements AutoCloseable {

  private SWIGTYPE_p_p_tiledb_config_t configpp;
  private SWIGTYPE_p_tiledb_config_t configp;

  /**
   * Constructor from native object handle
   *
   * @param configpp A Swig wrapper object to a tiledb_config_t pointer
   */
  protected Config(SWIGTYPE_p_p_tiledb_config_t configpp) {
    this.configpp = configpp;
    this.configp = tiledb.tiledb_config_tpp_value(configpp);
  }

  /**
   * Constructor that creates a new config object with default configuration values.
   *
   * @exception TileDBError A TileDB exception
   */
  public Config() throws TileDBError {
    SWIGTYPE_p_p_tiledb_config_t _configpp = tiledb.new_tiledb_config_tpp();
    SWIGTYPE_p_p_tiledb_error_t _errorpp = tiledb.new_tiledb_error_tpp();
    try {
      int rc = tiledb.tiledb_config_alloc(_configpp, _errorpp);
      checkConfigError(rc, _errorpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_config_tpp(_configpp);
      tiledb.delete_tiledb_error_tpp(_errorpp);
      throw err;
    }
    tiledb.delete_tiledb_error_tpp(_errorpp);
    this.configpp = _configpp;
    this.configp = tiledb.tiledb_config_tpp_value(_configpp);
  }

  /**
   * Constructor that takes as input a filename String (URI) that stores the config parameters. The
   * file must have the following (text) format:
   *
   * <p>`{parameter} {value}`
   *
   * <p>Anything following a `#` character is considered a comment and, thus, is ignored.
   *
   * <p>See `Config.set` for the various TileDB config parameters and allowed values.
   *
   * @param filename local path to config file
   * @exception TileDBError A TileDB exception
   */
  public Config(String filename) throws TileDBError {
    init(filename);
  }

  /**
   * Constructor that takes as input a URI that stores the config parameters. The file must have the
   * following (text) format:
   *
   * <p>`{parameter} {value}`
   *
   * <p>Anything following a `#` character is considered a comment and, thus, is ignored.
   *
   * <p>See `Config.set` for the various TileDB config parameters and allowed values.
   *
   * @param uri path to local config file (file://)
   * @throws TileDBError A TileDB exception
   */
  public Config(URI uri) throws TileDBError {
    String scheme = uri.getScheme();
    if (!scheme.equals("file")) {
      throw new TileDBError(
          "Config can an only read from a local file scheme URI (file://), got: " + uri);
    }
    init(uri.getPath());
  }

  /**
   * Constructor that takes as input a Map of config string parameters.
   *
   * @param config map of string, value tiledb config parameters
   * @throws TileDBError A TileDB exception
   */
  public Config(Map<String, String> config) throws TileDBError {
    // call the default constructor to init the config tiledb object handle
    this();
    // set the passed configuration parameters
    for (Map.Entry<String, String> v : config.entrySet()) {
      set(v.getKey(), v.getValue());
    }
  }

  private void init(String filename) throws TileDBError {
    SWIGTYPE_p_p_tiledb_config_t _configpp = tiledb.new_tiledb_config_tpp();
    SWIGTYPE_p_p_tiledb_error_t _errorpp = tiledb.new_tiledb_error_tpp();
    try {
      int rc = tiledb.tiledb_config_alloc(_configpp, _errorpp);
      checkConfigError(rc, _errorpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_config_tpp(_configpp);
      tiledb.delete_tiledb_error_tpp(_errorpp);
      throw err;
    }
    SWIGTYPE_p_tiledb_config_t _configp = tiledb.tiledb_config_tpp_value(_configpp);
    try {
      int rc = tiledb.tiledb_config_load_from_file(_configp, filename, _errorpp);
      checkConfigError(rc, _errorpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_config_tpp(_configpp);
      tiledb.delete_tiledb_error_tpp(_errorpp);
      throw err;
    }
    tiledb.delete_tiledb_error_tpp(_errorpp);
    this.configp = _configp;
    this.configpp = _configpp;
  }

  /**
   * Get a parameter from the Config by name. <br>
   * <a href="https://docs.tiledb.io/en/stable/tutorials/config.html#summary-of-parameters">Summary
   * of config parameters</a> <br>
   *
   * @param parameter parameter name
   * @return config parameter string value
   * @exception TileDBError A TileDB exception
   */
  public String get(String parameter) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    SWIGTYPE_p_p_char valuepp = tiledb.new_charpp();
    try {
      int rc = tiledb.tiledb_config_get(configp, parameter, valuepp, errorpp);
      checkConfigError(rc, errorpp);
      tiledb.delete_tiledb_error_tpp(errorpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_error_tpp(errorpp);
      tiledb.delete_charpp(valuepp);
      throw err;
    }
    String value = tiledb.charpp_value(valuepp);
    tiledb.delete_charpp(valuepp);
    return value;
  }

  /**
   * Sets a config parameter-value pair. <br>
   * <a href="https://docs.tiledb.io/en/stable/tutorials/config.html#summary-of-parameters">Summary
   * of config parameters</a> <br>
   *
   * @param parameter config parameter to set
   * @param value config parameter value to set
   * @exception TileDBError A TileDB exception
   */
  public void set(String parameter, String value) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    try {
      int rc = tiledb.tiledb_config_set(configp, parameter, value, errorpp);
      checkConfigError(rc, errorpp);
    } finally {
      tiledb.delete_tiledb_error_tpp(errorpp);
    }
  }

  /**
   * Resets a config parameter to its default value.
   *
   * @param parameter config parameter to reset
   * @throws TileDBError A TileDB exception
   */
  public void unset(String parameter) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    try {
      int rc = tiledb.tiledb_config_unset(configp, parameter, errorpp);
      checkConfigError(rc, errorpp);
    } finally {
      tiledb.delete_tiledb_error_tpp(errorpp);
    }
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
   * Returns a map of TileDB config paramter, value pairs with parameter names starting with a given
   * prefix.
   *
   * @param prefix A parameter prefix. Use "" to get all parameters.
   * @return HashMap containing all parameters as key-value pairs.
   * @throws TileDBError A TileDB exception
   */
  public HashMap<String, String> parameters(String prefix) throws TileDBError {
    HashMap<String, String> result = new HashMap<String, String>();
    SWIGTYPE_p_p_tiledb_config_iter_t iterpp = tiledb.new_tiledb_config_iter_tpp();
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    try {
      int rc = tiledb.tiledb_config_iter_alloc(configp, prefix, iterpp, errorpp);
      checkConfigError(rc, errorpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_config_iter_tpp(iterpp);
      tiledb.delete_tiledb_error_tpp(errorpp);
      throw err;
    }
    SWIGTYPE_p_tiledb_config_iter_t iterp = tiledb.tiledb_config_iter_tpp_value(iterpp);
    while (hasMoreParams(iterp)) {
      errorpp = tiledb.new_tiledb_error_tpp();
      SWIGTYPE_p_p_char parampp = tiledb.new_charpp();
      SWIGTYPE_p_p_char valuepp = tiledb.new_charpp();
      try {
        int rc = tiledb.tiledb_config_iter_here(iterp, parampp, valuepp, errorpp);
        checkConfigError(rc, errorpp);
      } catch (TileDBError err) {
        tiledb.delete_charpp(parampp);
        tiledb.delete_charpp(valuepp);
        tiledb.delete_tiledb_error_tpp(errorpp);
        tiledb.delete_tiledb_config_iter_tpp(iterpp);
        throw err;
      }
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
    try {
      int rc = tiledb.tiledb_config_save_to_file(configp, filename, errorpp);
      checkConfigError(rc, errorpp);
    } finally {
      tiledb.delete_tiledb_error_tpp(errorpp);
    }
  }

  /**
   * Saves config parameters to a local file system (file://) path.
   *
   * @param uri The URI of the file where the parameters will be written
   * @throws TileDBError A TileDB exception
   */
  public void saveToFile(URI uri) throws TileDBError {
    String scheme = uri.getScheme();
    if (!scheme.equals("file")) {
      throw new TileDBError(
          "Config can an only save to a local file scheme URI (file://), got: " + uri);
    }
    saveToFile(uri.getPath());
  }

  private void next(SWIGTYPE_p_tiledb_config_iter_t iterp) throws TileDBError {
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    try {
      int rc = tiledb.tiledb_config_iter_next(iterp, errorpp);
      checkConfigError(rc, errorpp);
    } finally {
      tiledb.delete_tiledb_error_tpp(errorpp);
    }
  }

  private boolean hasMoreParams(SWIGTYPE_p_tiledb_config_iter_t iterp) throws TileDBError {
    SWIGTYPE_p_int done = tiledb.new_intp();
    SWIGTYPE_p_p_tiledb_error_t errorpp = tiledb.new_tiledb_error_tpp();
    try {
      int rc = tiledb.tiledb_config_iter_done(iterp, done, errorpp);
      checkConfigError(rc, errorpp);
    } catch (TileDBError err) {
      tiledb.delete_intp(done);
      tiledb.delete_tiledb_error_tpp(errorpp);
      throw err;
    }
    boolean hasMore = tiledb.intp_value(done) == 0;
    tiledb.delete_intp(done);
    return hasMore;
  }

  private void checkConfigError(int returnCode, SWIGTYPE_p_p_tiledb_error_t error)
      throws TileDBError {
    if (returnCode == tiledb.TILEDB_ERR) {
      SWIGTYPE_p_p_char msgpp = tiledb.new_charpp();
      int ret = tiledb.tiledb_error_message(tiledb.tiledb_error_tpp_value(error), msgpp);
      if (ret == tiledb.TILEDB_ERR) {
        tiledb.delete_charpp(msgpp);
        tiledb.tiledb_error_free(error);
        throw new TileDBError("Config Error: Unknown error, could not retrieve error message");
      }
      String msg = tiledb.charpp_value(msgpp);
      tiledb.delete_charpp(msgpp);
      tiledb.tiledb_error_free(error);
      throw new TileDBError("Config Error: " + msg);
    }
  }

  /** @return Swig wrapper object to a tiledb_config_t pointer */
  protected SWIGTYPE_p_tiledb_config_t getConfigp() {
    return configp;
  }

  /** Free's native TileDB resources associated with the Config object. */
  @Override
  public void close() {
    if (configp != null) {
      tiledb.tiledb_config_free(configpp);
      configp = null;
      configpp = null;
    }
  }
}
