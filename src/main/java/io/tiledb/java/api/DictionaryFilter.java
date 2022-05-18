package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_filter_t;
import io.tiledb.libtiledb.tiledb_filter_type_t;

public class DictionaryFilter extends CompressionFilter {
  public DictionaryFilter(Context ctx) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_DICTIONARY);
  }

  public DictionaryFilter(Context ctx, int level) throws TileDBError {
    super(ctx, tiledb_filter_type_t.TILEDB_FILTER_DICTIONARY, level);
  }

  protected DictionaryFilter(Context ctx, SWIGTYPE_p_p_tiledb_filter_t filterpp) {
    super(ctx, filterpp);
  }
}
