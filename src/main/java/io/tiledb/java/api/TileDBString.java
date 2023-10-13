package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_char;
import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_string_handle_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_string_handle_t;
import io.tiledb.libtiledb.SWIGTYPE_p_unsigned_long;
import io.tiledb.libtiledb.tiledb;

public class TileDBString {
  private Context ctx;
  private SWIGTYPE_p_tiledb_string_handle_t stringp;
  private SWIGTYPE_p_p_tiledb_string_handle_t stringpp;

  /** Constructor */
  protected TileDBString(Context ctx, SWIGTYPE_p_p_tiledb_string_handle_t stringpp) {
    this.ctx = ctx;
    this.stringp = tiledb.tiledb_string_handle_tpp_value(stringpp);
    this.stringpp = stringpp;
  }

  protected SWIGTYPE_p_tiledb_string_handle_t getStringp() {
    return this.stringp;
  }

  protected Context getCtx() {
    return this.ctx;
  }

  /**
   * Returns a view (i.e. data and length) of a TileDB string object.
   *
   * @return A pair with the String data and size
   */
  public Pair<String, Long> getView() throws TileDBError {
    String data;
    long size;
    SWIGTYPE_p_p_char datapp = tiledb.new_charpp();
    SWIGTYPE_p_unsigned_long sizep = tiledb.new_ulp();
    try {
      ctx.handleError(tiledb.tiledb_string_view(this.getStringp(), datapp, sizep));
      data = tiledb.charpp_value(datapp);
      size = tiledb.ulp_value(sizep);
      return new Pair<>(data, size);
    } finally {
      tiledb.delete_charpp(datapp);
      tiledb.delete_ulp(sizep);
    }
  }

  public void close() {
    if (stringp != null && stringpp != null) {
      tiledb.tiledb_string_free(stringpp);
      tiledb.delete_tiledb_string_handle_tpp(stringpp);
      stringpp = null;
      stringp = null;
    }
  }
}
