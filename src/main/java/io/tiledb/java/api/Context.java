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
 * This file defines the JAVA API for the TileDB Context object.
 */

package io.tiledb.java.api;

import io.tiledb.api.*;


public class Context {

  private ContextCallback error_handler;
  private Config config;
  private SWIGTYPE_p_p_tiledb_ctx_t ctxpp;
  private SWIGTYPE_p_tiledb_ctx_t ctxp;

  /**
   * Constructor.
   */
  public Context() throws TileDBError {
    create_context(new Config());
  }

  /**
   * Constructor with config parameters.
   */
  public Context(Config config) throws TileDBError {
    create_context(config);
  }

  /**
   * Sets the error handler using a subclass of ContextCallback. If none is set,
   * `ContextCallback` is used. The callback accepts an error
   *  message.
   */
  public void set_error_handler(ContextCallback error_handler) {
    this.error_handler = error_handler;
  }

  /**
   * Error handler for the TileDB C API calls. Throws an exception
   * in case of error.
   *
   * @param rc If != TILEDB_OK, call error handler
   */
  public void handle_error(int rc) throws TileDBError {
    // Do nothing if there is no error
    if (rc == tiledb.TILEDB_OK)
      return;

    // Get error
    SWIGTYPE_p_p_tiledb_error_t errorpp = Utils.new_tiledb_error_tpp();
    rc = tiledb.tiledb_ctx_get_last_error(ctxp, errorpp);
    if (rc != tiledb.TILEDB_OK) {
      tiledb.tiledb_error_free(errorpp);
      error_handler.call("[TileDB::JavaAPI] Error: Non-retrievable error occurred");
    }

    // Get error message
    SWIGTYPE_p_p_char msgpp = tiledb.new_charpp();
    rc = tiledb.tiledb_error_message(Utils.tiledb_error_tpp_value(errorpp), msgpp);
    String msg = tiledb.charpp_value(msgpp);
    if (rc != tiledb.TILEDB_OK) {
      tiledb.tiledb_error_free(errorpp);
      error_handler.call("[TileDB::JavaAPI] Error: Non-retrievable error occurred");
    }

    // Clean up
    tiledb.tiledb_error_free(errorpp);

    // Throw exception
    error_handler.call(msg);
  }

  /**
   * Checks if the filesystem backend is supported.
   */
  public boolean is_supported_fs(tiledb_filesystem_t fs) throws TileDBError {
    SWIGTYPE_p_int ret = tiledb.new_intp();
    tiledb.tiledb_ctx_is_supported_fs(ctxp, fs, ret);
    return tiledb.intp_value(ret) != 0;
  }

  /**
   * Delete the native object.
   */
  public void free() throws Throwable {
    if(config!=null)
      config.free();
    int rc = tiledb.tiledb_ctx_free(ctxpp);
    handle_error(rc);
  }

  private void create_context(Config config) throws TileDBError {
    ctxpp = Utils.new_tiledb_ctx_tpp();
    if (tiledb.tiledb_ctx_create(ctxpp, config.getConfigp()) != tiledb.TILEDB_OK)
      throw new TileDBError("[TileDB::JavaAPI] Error: Failed to create context");
    ctxp = Utils.tiledb_ctx_tpp_value(ctxpp);
    this.config=config;
    error_handler = new ContextCallback();
  }

  protected SWIGTYPE_p_p_tiledb_ctx_t getCtxpp() {
    return ctxpp;
  }

  protected void setCtxpp(SWIGTYPE_p_p_tiledb_ctx_t ctxpp) {
    this.ctxpp = ctxpp;
  }

  protected SWIGTYPE_p_tiledb_ctx_t getCtxp() {
    return ctxp;
  }

  protected void setCtxp(SWIGTYPE_p_tiledb_ctx_t ctxp) {
    this.ctxp = ctxp;
  }

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  @Override
  protected void finalize() throws Throwable {
    free();
    super.finalize();
  }

}
