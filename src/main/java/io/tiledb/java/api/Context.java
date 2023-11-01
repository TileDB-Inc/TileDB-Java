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
 *
 * @section DESCRIPTION
 *
 * This file defines the JAVA API for the TileDB Context object.
 */

package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import java.util.Map;

/**
 * A TileDB context wraps a TileDB storage manager instance. Most objects and functions will require
 * a Context. <br>
 * Internal error handling is also defined by the Context; the default error handler throws a
 * TileDBError with a specific message. <br>
 *
 * <pre><b>Example:</b>
 *   Context ctx = new Context();
 *   // Use ctx when creating other objects:
 *   ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_SPARSE);
 *
 *   // Set a custom error handler:
 *   ctx.setErrorHandler(new MyContextCallback());
 *
 *   //Context custom callback class example
 *   private static class MyContextCallback extends ContextCallback {
 *     {@literal @Override}
 *     public void call(String msg) throws TileDBError {
 *       System.out.println("Callback error message: " + msg);
 *     }
 *   }
 * </pre>
 */
public class Context implements AutoCloseable {

  private SWIGTYPE_p_p_tiledb_ctx_t ctxpp;
  private SWIGTYPE_p_tiledb_ctx_t ctxp;
  private ContextCallback errorHandler;
  private Config config;

  /**
   * Constructor. Creates a TileDB Context with default configuration.
   *
   * @throws TileDBError if construction fails
   */
  public Context() throws TileDBError {
    try (Config config = new Config()) {
      createContext(config);
    }
  }

  /**
   * Creates a TileDB context with the given TileDB config object.
   *
   * @param config A TileDB Config object
   * @throws TileDBError if construction fails
   */
  public Context(Config config) throws TileDBError {
    createContext(config);
  }

  /**
   * Creates a TileDB context with the given TileDB parameter, value settings.
   *
   * @param config A Map of TileDB setting, value string pairs
   * @throws TileDBError if construction fails
   */
  public Context(Map<String, String> config) throws TileDBError {
    try (Config tiledbConfig = new Config(config)) {
      createContext(tiledbConfig);
    }
  }

  /**
   * Sets the error handler using a subclass of ContextCallback. If none is set, ContextCallback is
   * used. The callback accepts an error message.
   *
   * @param errorHandler A custom ContextCallback error handler
   */
  public void setErrorHandler(ContextCallback errorHandler) {
    this.errorHandler = errorHandler;
  }

  /**
   * Error handler for the TileDB C API (JNI) calls. Throws an exception in case of error.
   *
   * @param rc If != TILEDB_OK, call error handler
   * @exception TileDBError A TileDB exception
   */
  public void handleError(int rc) throws TileDBError {
    // Do nothing if there is no error
    if (rc == tiledb.TILEDB_OK) {
      return;
    }

    // Get error
    SWIGTYPE_p_p_tiledb_error_handle_t errorpp = tiledb.new_tiledb_error_tpp();
    rc = tiledb.tiledb_status_code(tiledb.tiledb_ctx_get_last_error(ctxp, errorpp));
    if (rc != tiledb.TILEDB_OK) {
      tiledb.tiledb_error_free(errorpp);
      tiledb.delete_tiledb_error_tpp(errorpp);
      errorHandler.call("[TileDB::JavaAPI] Error: Non-retrievable error occurred");
    }

    // Get error message
    SWIGTYPE_p_p_char msgpp = tiledb.new_charpp();
    rc =
        tiledb.tiledb_status_code(
            tiledb.tiledb_error_message(tiledb.tiledb_error_tpp_value(errorpp), msgpp));
    String msg = tiledb.charpp_value(msgpp);
    if (rc != tiledb.TILEDB_OK) {
      tiledb.tiledb_error_free(errorpp);
      tiledb.delete_tiledb_error_tpp(errorpp);
      errorHandler.call("[TileDB::JavaAPI] Error: Non-retrievable error occurred");
    }

    // Clean up
    tiledb.tiledb_error_free(errorpp);
    tiledb.delete_tiledb_error_tpp(errorpp);

    // Throw exception
    errorHandler.call(msg);
  }

  /**
   * Error handler for the TileDB C API (JNI) calls. Throws an exception in case of error.
   *
   * @param return_t
   * @throws TileDBError
   */
  public void handleError(capi_return_t return_t) throws TileDBError {
    int rc = tiledb.tiledb_status_code(return_t);
    this.handleError(rc);
  }

  /**
   * Checks if the filesystem backend is supported.
   *
   * @param fs TileDB filesystem enum
   * @return true if the filesystem is supported, false otherwise
   * @exception TileDBError A TileDB exception
   */
  public boolean isSupportedFs(Filesystem fs) throws TileDBError {
    boolean isSupported;
    SWIGTYPE_p_int ret = tiledb.new_intp();
    try {
      handleError(tiledb.tiledb_ctx_is_supported_fs(ctxp, fs.toSwigEnum(), ret));
      isSupported = tiledb.intp_value(ret) != 0;
    } finally {
      tiledb.delete_intp(ret);
    }
    return isSupported;
  }

  private void createContext(Config config) throws TileDBError {
    SWIGTYPE_p_p_tiledb_ctx_t _ctxpp = tiledb.new_tiledb_ctx_tpp();
    if (tiledb.tiledb_status_code(tiledb.tiledb_ctx_alloc(config.getConfigp(), _ctxpp))
        != tiledb.TILEDB_OK) {
      tiledb.delete_tiledb_ctx_tpp(_ctxpp);
      throw new TileDBError("[TileDB::JavaAPI] Error: Failed to create context");
    }
    this.config = config;
    this.ctxpp = _ctxpp;
    this.ctxp = tiledb.tiledb_ctx_tpp_value(_ctxpp);
    this.errorHandler = new ContextCallback();

    // Set default tags
    setDefaultTags();
  }

  protected SWIGTYPE_p_tiledb_ctx_t getCtxp() {
    return ctxp;
  }

  /** @return A Config object containing all configuration values of the Context. */
  public Config getConfig() throws TileDBError {
    SWIGTYPE_p_p_tiledb_config_t _configpp = tiledb.new_tiledb_config_tpp();
    int rc = tiledb.tiledb_status_code(tiledb.tiledb_ctx_get_config(ctxp, _configpp));
    if (rc != tiledb.TILEDB_OK) {
      tiledb.delete_tiledb_config_tpp(_configpp);
      handleError(rc);
    }
    return new Config(_configpp);
  }

  /**
   * Set context tasks
   *
   * @param key to set
   * @param value value to set
   * @throws TileDBError
   */
  public void setTag(String key, String value) throws TileDBError {
    handleError(tiledb.tiledb_ctx_set_tag(ctxp, key, value));
  }

  /**
   * Set default context tags
   *
   * @throws TileDBError
   */
  private void setDefaultTags() throws TileDBError {
    setTag("x-tiledb-api-language", "java");
    Package pkg = Context.class.getPackage();
    if (pkg != null) {
      String version = pkg.getImplementationVersion();
      if (version != null) {
        setTag("x-tiledb-api-language-version", version);
      }
    }
    String platform =
        System.getProperty("os.name")
            + "-"
            + System.getProperty("os.verions")
            + "-"
            + System.getProperty("os.arch");
    setTag("x-tiledb-api-sys-platform", platform);
  }

  /**
   * @return Retrieves the stats from a TileDB context.
   * @exception TileDBError A TileDB exception
   */
  public String getStats() throws TileDBError {
    String stats;
    SWIGTYPE_p_p_char statspp = tiledb.new_charpp();
    try {
      handleError(tiledb.tiledb_ctx_get_stats(getCtxp(), statspp));
      stats = tiledb.charpp_value(statspp);
    } finally {
      tiledb.delete_charpp(statspp);
    }

    return stats;
  } // context file

  /**
   * Close the context and delete all native objects. Should be called always to cleanup the context
   */
  public void close() {
    config.close();
    if (ctxp != null) {
      tiledb.tiledb_ctx_free(ctxpp);
      tiledb.delete_tiledb_ctx_tpp(ctxpp);
      ctxp = null;
      ctxpp = null;
    }
  }
}
