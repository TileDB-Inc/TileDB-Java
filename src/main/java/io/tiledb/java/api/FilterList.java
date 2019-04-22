package io.tiledb.java.api;

import io.tiledb.libtiledb.*;

public class FilterList implements AutoCloseable {

  private Context ctx;

  private SWIGTYPE_p_tiledb_filter_list_t filter_listp;
  private SWIGTYPE_p_p_tiledb_filter_list_t filter_listpp;

  public FilterList(Context ctx) throws TileDBError {
    SWIGTYPE_p_p_tiledb_filter_list_t _filter_listpp = tiledb.new_tiledb_filter_list_tpp();
    try {
      ctx.handleError(tiledb.tiledb_filter_list_alloc(ctx.getCtxp(), _filter_listpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_filter_list_tpp(_filter_listpp);
      throw err;
    }
    this.ctx = ctx;
    this.filter_listp = tiledb.tiledb_filter_list_tpp_value(_filter_listpp);
    this.filter_listpp = _filter_listpp;
  }

  protected FilterList(Context ctx, SWIGTYPE_p_p_tiledb_filter_list_t filter_listpp) {
    this.ctx = ctx;
    this.filter_listp = tiledb.tiledb_filter_list_tpp_value(filter_listpp);
    this.filter_listpp = filter_listpp;
  }

  protected SWIGTYPE_p_tiledb_filter_list_t getFilterListp() {
    return this.filter_listp;
  }

  public FilterList addFilter(Filter filter) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_filter_list_add_filter(ctx.getCtxp(), filter_listp, filter.getFilterp()));
    return this;
  }

  public Filter getFilter(long idx) throws TileDBError {
    SWIGTYPE_p_tiledb_filter_t _filterp;
    SWIGTYPE_p_p_tiledb_filter_t _filterpp = tiledb.new_tiledb_filter_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_filter_list_get_filter_from_index(
              ctx.getCtxp(), filter_listp, idx, _filterpp));
      _filterp = tiledb.tiledb_filter_tpp_value(_filterpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_filter_tpp(_filterpp);
      throw err;
    }
    tiledb_filter_type_t filter_type;
    SWIGTYPE_p_tiledb_filter_type_t _filter_typep = tiledb.new_tiledb_filter_type_tp();
    try {
      ctx.handleError(tiledb.tiledb_filter_get_type(ctx.getCtxp(), _filterp, _filter_typep));
      filter_type = tiledb.tiledb_filter_type_tp_value(_filter_typep);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_filter_tpp(_filterpp);
      throw err;
    } finally {
      tiledb.delete_tiledb_filter_type_tp(_filter_typep);
    }
    switch (filter_type) {
      case TILEDB_FILTER_NONE:
        return new NoneFilter(this.ctx, _filterpp);
      case TILEDB_FILTER_GZIP:
        return new GzipFilter(this.ctx, _filterpp);
      case TILEDB_FILTER_ZSTD:
        return new ZstdFilter(this.ctx, _filterpp);
      case TILEDB_FILTER_LZ4:
        return new LZ4Filter(this.ctx, _filterpp);
      case TILEDB_FILTER_BZIP2:
        return new Bzip2Filter(this.ctx, _filterpp);
      case TILEDB_FILTER_RLE:
        return new RleFilter(this.ctx, _filterpp);
      case TILEDB_FILTER_DOUBLE_DELTA:
        return new DoubleDeltaFilter(this.ctx, _filterpp);
      case TILEDB_FILTER_BITSHUFFLE:
        return new BitShuffleFilter(this.ctx, _filterpp);
      case TILEDB_FILTER_BYTESHUFFLE:
        return new ByteShuffleFilter(this.ctx, _filterpp);
      case TILEDB_FILTER_BIT_WIDTH_REDUCTION:
        return new BitWidthReductionFilter(this.ctx, _filterpp);
      case TILEDB_FILTER_POSITIVE_DELTA:
        return new PositiveDeltaFilter(this.ctx, _filterpp);
      default:
        {
          tiledb.delete_tiledb_filter_tpp(_filterpp);
          throw new TileDBError("Unknown TileDB filter type: " + filter_type.name());
        }
    }
  }

  public long getNumFilters() throws TileDBError {
    long nfilters;
    uint32_tArray nfiltersArray = new uint32_tArray(1);
    try {
      ctx.handleError(
          tiledb.tiledb_filter_list_get_nfilters(
              ctx.getCtxp(), filter_listp, nfiltersArray.cast()));
      nfilters = nfiltersArray.getitem(0);
    } finally {
      nfiltersArray.delete();
    }
    return nfilters;
  }

  public FilterList setMaxChunkSize(long chunksize) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_filter_list_set_max_chunk_size(ctx.getCtxp(), filter_listp, chunksize));
    return this;
  }

  public long getMaxChunkSize() throws TileDBError {
    long chunk_size;
    uint32_tArray chunkSizeArray = new uint32_tArray(1);
    try {
      ctx.handleError(
          tiledb.tiledb_filter_list_get_max_chunk_size(
              ctx.getCtxp(), filter_listp, chunkSizeArray.cast()));
      chunk_size = chunkSizeArray.getitem(0);
    } finally {
      chunkSizeArray.delete();
    }
    return chunk_size;
  }

  public void close() {
    if (filter_listp != null && filter_listpp != null) {
      tiledb.tiledb_filter_list_free(filter_listpp);
      filter_listp = null;
      filter_listpp = null;
    }
  }
}
