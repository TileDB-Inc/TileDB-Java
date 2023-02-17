package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_fragment_info_t;

public class FragmentInfo {
  private SWIGTYPE_p_tiledb_fragment_info_t fragmentInfop;
  private SWIGTYPE_p_p_tiledb_fragment_info_t fragmentInfopp;
  private Context ctx;
  private String uri;

  /**
   * @param ctx
   * @param uri
   * @throws TileDBError
   */
  public FragmentInfo(Context ctx, String uri) throws TileDBError {
    this.ctx = ctx;
    this.uri = uri;

    this.fragmentInfopp = tiledb.new_tiledb_fragment_info_tpp();
    ctx.handleError(tiledb.tiledb_fragment_info_alloc(ctx.getCtxp(), uri, fragmentInfopp));
    fragmentInfop = tiledb.tiledb_fragment_info_tpp_value(this.fragmentInfopp);
    ctx.handleError(tiledb.tiledb_fragment_info_load(ctx.getCtxp(), fragmentInfop));
  }

  /**
   * Set the fragment info config. Useful for passing timestamp ranges and encryption key via the
   * config before loading the fragment info.
   *
   * @param config the TileDB config
   * @throws TileDBError
   */
  public void setConfig(Config config) throws TileDBError {
    try {
      ctx.handleError(
          tiledb.tiledb_fragment_info_set_config(
              ctx.getCtxp(), this.fragmentInfop, config.getConfigp()));
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * Retrieves the non-empty domain range sizes from a fragment for a given dimension index.
   * Applicable to var-sized dimensions.
   *
   * @param fragmentID The fragment ID
   * @param dimensionID The dimension name
   * @return The non-empty domain range sizes from a fragment for a given dimension index.
   * @throws TileDBError
   */
  public Pair<Long, Long> getNonEmptyDomainVarSizeFromIndex(long fragmentID, long dimensionID)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_non_empty_domain_var_size_from_index(
            ctx.getCtxp(), fragmentInfop, fragmentID, dimensionID, startSize, endSize));
    return new Pair(
        tiledb.ullp_value(startSize).longValue(), tiledb.ullp_value(endSize).longValue());
  }

  /**
   * Retrieves the non-empty domain range sizes from a fragment for a given dimension name.
   * Applicable to var-sized dimensions.
   *
   * @param fragmentID The fragment ID
   * @param dimensionName The dimension name
   * @return The non-empty domain range sizes from a fragment for a given dimension name
   * @throws TileDBError
   */
  public Pair<Long, Long> getNonEmptyDomainVarSizeFromName(long fragmentID, String dimensionName)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_non_empty_domain_var_size_from_name(
            ctx.getCtxp(), fragmentInfop, fragmentID, dimensionName, startSize, endSize));
    return new Pair(
        tiledb.ullp_value(startSize).longValue(), tiledb.ullp_value(endSize).longValue());
  }

  /**
   * Returns the number of fragments.
   *
   * @return The number of fragments
   * @throws TileDBError
   */
  public long getFragmentNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int num = tiledb.new_uintp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_fragment_num(ctx.getCtxp(), this.fragmentInfop, num));

    return tiledb.uintp_value(num);
  }

  /**
   * Returns the URI of the fragment with the given index.
   *
   * @param fragmentID The fragment ID
   * @return The fragment URI
   * @throws TileDBError
   */
  public String getFragmentURI(long fragmentID) throws TileDBError {
    SWIGTYPE_p_p_char uri = tiledb.new_charpp();
    tiledb.tiledb_fragment_info_get_fragment_uri(ctx.getCtxp(), fragmentInfop, fragmentID, uri);

    return tiledb.charpp_value(uri);
  }

  /**
   * Returns the size of the fragment with the given index.
   *
   * @param fragmentID The fragment ID
   * @return The fragment size
   * @throws TileDBError
   */
  public long getFragmentSize(long fragmentID) throws TileDBError {
    SWIGTYPE_p_unsigned_long_long size = tiledb.new_ullp();
    tiledb.tiledb_fragment_info_get_fragment_size(ctx.getCtxp(), fragmentInfop, fragmentID, size);

    return tiledb.ullp_value(size).longValue();
  }

  /**
   * Retrieves the number of cells written to the fragments by the user.
   *
   * <p>Contributions from each fragment to the total are as described in following.
   *
   * <p>In the case of sparse fragments, this is the number of non-empty cells in the fragment.
   *
   * <p>In the case of dense fragments, TileDB may add fill values to populate partially populated
   * tiles. Those fill values are counted in the returned number of cells. In other words, the cell
   * number is derived from the number of *integral* tiles written in the file.
   *
   * <p>note: The count returned is the cumulative total of cells written to all fragments in the
   * current fragment_info entity, i.e. count may effectively include multiples for any cells that
   * may be overlapping across the various fragments.
   *
   * @return The total number of cells.
   * @throws TileDBError
   */
  public long getTotalCellNum() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long cellNum = tiledb.new_ullp();
    tiledb.tiledb_fragment_info_get_total_cell_num(ctx.getCtxp(), fragmentInfop, cellNum);

    return tiledb.ullp_value(cellNum).longValue();
  }

  /**
   * Returns true if the fragment with the given index is dense.
   *
   * @param fragmentID The fragment ID
   * @return True, if the fragment is dense, false otherwise
   * @throws TileDBError
   */
  public boolean getDense(long fragmentID) throws TileDBError {
    SWIGTYPE_p_int size = tiledb.new_intp();
    tiledb.tiledb_fragment_info_get_dense(ctx.getCtxp(), fragmentInfop, fragmentID, size);

    return tiledb.intp_value(size) == 1;
  }

  /**
   * Returns true if the fragment with the given index is sparse.
   *
   * @param fragmentID The fragment ID
   * @return True if the fragment is sparse, false otherwise
   * @throws TileDBError
   */
  public boolean getSparse(long fragmentID) throws TileDBError {
    SWIGTYPE_p_int size = tiledb.new_intp();
    tiledb.tiledb_fragment_info_get_sparse(ctx.getCtxp(), fragmentInfop, fragmentID, size);

    return tiledb.intp_value(size) == 1;
  }

  /**
   * Returns the timestamp range of the fragment with the given index.
   *
   * @param fragmentID The fragment ID
   * @return A Pair including the timestamp range
   * @throws TileDBError
   */
  public Pair<Long, Long> getTimestampRange(long fragmentID) throws TileDBError {
    SWIGTYPE_p_unsigned_long_long start = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long end = tiledb.new_ullp();
    ctx.handleError(
        tiledb.tiledb_fragment_info_get_timestamp_range(
            ctx.getCtxp(), fragmentInfop, fragmentID, start, end));

    return new Pair(tiledb.ullp_value(start).longValue(), tiledb.ullp_value(end).longValue());
  }

  /**
   * Retrieves the non-empty domain from a given fragment for a given dimension name.
   *
   * @param fragmentID The fragment ID
   * @param dimensionID The dimension name
   * @return The non-empty domain of the given fragment and dimension ID
   * @throws TileDBError
   */
  public Pair getNonEmptyDomainFromIndex(long fragmentID, long dimensionID) throws TileDBError {
    try (Array arr = new Array(ctx, uri)) {
      Datatype type = arr.getSchema().getDomain().getDimension(dimensionID).getType();

      try (NativeArray array = new NativeArray(ctx, 2, type)) {
        ctx.handleError(
            tiledb.tiledb_fragment_info_get_non_empty_domain_from_index(
                ctx.getCtxp(), fragmentInfop, fragmentID, dimensionID, array.toVoidPointer()));

        return new Pair(array.getItem(0), array.getItem(1));
      }
    }
  }

  /**
   * Retrieves the non-empty domain from a given fragment for a given dimension name.
   *
   * @param fragmentID The fragment ID
   * @param dimensionName The dimension name
   * @return The non-empty domain of the given fragment ID and dimension name
   * @throws TileDBError
   */
  public Pair getNonEmptyDomainFromName(long fragmentID, String dimensionName) throws TileDBError {
    try (Array arr = new Array(ctx, uri)) {
      Datatype type = arr.getSchema().getDomain().getDimension(dimensionName).getType();

      try (NativeArray array = new NativeArray(ctx, 2, type)) {
        ctx.handleError(
            tiledb.tiledb_fragment_info_get_non_empty_domain_from_name(
                ctx.getCtxp(), fragmentInfop, fragmentID, dimensionName, array.toVoidPointer()));

        return new Pair(array.getItem(0), array.getItem(1));
      }
    }
  }

  /**
   * Retrieves the config from fragment info.
   *
   * @return The config
   * @throws TileDBError
   */
  public Config getConfig() throws TileDBError {
    SWIGTYPE_p_p_tiledb_config_t configpp = tiledb.new_tiledb_config_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_fragment_info_get_config(ctx.getCtxp(), fragmentInfop, configpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_config_tpp(configpp);
    }
    return new Config(configpp);
  }

  /**
   * Retrieves the non-empty domain from a fragment for a given dimension index. Applicable to
   * var-sized dimensions.
   *
   * @param fragmentID The fragment ID
   * @param dimensionID The dimension ID
   * @return The non-empty domain given the fragment and dimension IDs
   * @throws TileDBError
   */
  public Pair getNonEmptyDomainVarFromIndex(long fragmentID, long dimensionID) throws TileDBError {
    try (Array arr = new Array(ctx, uri)) {
      try (Dimension dimension = arr.getSchema().getDomain().getDimension(dimensionID)) {
        Datatype type = dimension.getType();

        SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
        SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();

        ctx.handleError(
            tiledb.tiledb_fragment_info_get_non_empty_domain_var_size_from_index(
                ctx.getCtxp(), fragmentInfop, fragmentID, dimensionID, startSize, endSize));

        try (NativeArray startRange =
                new NativeArray(ctx, tiledb.ullp_value(startSize).intValue(), type);
            NativeArray endRange =
                new NativeArray(ctx, tiledb.ullp_value(endSize).intValue(), type)) {

          ctx.handleError(
              tiledb.tiledb_fragment_info_get_non_empty_domain_var_from_index(
                  ctx.getCtxp(),
                  fragmentInfop,
                  fragmentID,
                  dimensionID,
                  startRange.toVoidPointer(),
                  endRange.toVoidPointer()));

          return new Pair(startRange.toJavaArray(), endRange.toJavaArray());
        }
      }
    }
  }

  /**
   * Retrieves the non-empty domain from a fragment for a given dimension name. Applicable to
   * var-sized dimensions.
   *
   * @param fragmentID The fragment ID
   * @param dimensionName The dimension name
   * @return The non-empty domain given the fragment ID and dimension name
   * @throws TileDBError
   */
  public Pair getNonEmptyDomainVarFromName(long fragmentID, String dimensionName)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();

    try (Array arr = new Array(ctx, uri)) {
      try (Dimension dimension = arr.getSchema().getDomain().getDimension(dimensionName)) {
        Datatype type = dimension.getType();

        ctx.handleError(
            tiledb.tiledb_fragment_info_get_non_empty_domain_var_size_from_name(
                ctx.getCtxp(), fragmentInfop, fragmentID, dimensionName, startSize, endSize));

        try (NativeArray startRange =
                new NativeArray(ctx, tiledb.ullp_value(startSize).intValue(), type);
            NativeArray endRange =
                new NativeArray(ctx, tiledb.ullp_value(endSize).intValue(), type)) {

          ctx.handleError(
              tiledb.tiledb_fragment_info_get_non_empty_domain_var_from_name(
                  ctx.getCtxp(),
                  fragmentInfop,
                  fragmentID,
                  dimensionName,
                  startRange.toVoidPointer(),
                  endRange.toVoidPointer()));

          return new Pair(startRange.toJavaArray(), endRange.toJavaArray());
        }
      }
    }
  }

  /**
   * In the case of sparse fragments, this is the number of non-empty cells in the fragment.
   *
   * <p>In the case of dense fragments, TileDB may add fill values to populate partially populated
   * tiles. Those fill values are counted in the returned number of cells. In other words, the cell
   * number is derived from the number of *integral* tiles written in the file.
   *
   * @param fragmentID The fragment ID
   * @return The number of cells
   * @throws TileDBError
   */
  public long getCellNum(long fragmentID) throws TileDBError {
    SWIGTYPE_p_unsigned_long_long cellNum = tiledb.new_ullp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_cell_num(
            ctx.getCtxp(), fragmentInfop, fragmentID, cellNum));

    return tiledb.ullp_value(cellNum).longValue();
  }

  /**
   * Retrieves the format version of a fragment.
   *
   * @param fragmentID The fragment ID
   * @return The version
   * @throws TileDBError
   */
  public long getVersion(long fragmentID) throws TileDBError {
    SWIGTYPE_p_unsigned_int version = tiledb.new_uintp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_version(ctx.getCtxp(), fragmentInfop, fragmentID, version));

    return tiledb.uintp_value(version);
  }

  /**
   * Checks if a fragment has consolidated metadata.
   *
   * @param fragmentID The fragment ID
   * @return
   * @throws TileDBError
   */
  public boolean hasConsolidatedMetadata(long fragmentID) throws TileDBError {
    SWIGTYPE_p_int has = tiledb.new_intp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_has_consolidated_metadata(
            ctx.getCtxp(), fragmentInfop, fragmentID, has));

    return tiledb.intp_value(has) == 1;
  }

  /**
   * Gets the number of fragments with unconsolidated metadata.
   *
   * @return The number of fragments with unconsolidated metadata
   * @throws TileDBError
   */
  public long getUnconsolidatedMetadataNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int unconsolidated = tiledb.new_uintp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_unconsolidated_metadata_num(
            ctx.getCtxp(), fragmentInfop, unconsolidated));

    return tiledb.uintp_value(unconsolidated);
  }

  /**
   * Gets the number of fragments to vacuum.
   *
   * @return The number of fragments to vacuum
   * @throws TileDBError
   */
  public long getToVacuumNum() throws TileDBError {
    SWIGTYPE_p_unsigned_int toVacuumNum = tiledb.new_uintp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_to_vacuum_num(ctx.getCtxp(), fragmentInfop, toVacuumNum));

    return tiledb.uintp_value(toVacuumNum);
  }

  /**
   * Gets the URI of the fragment to vacuum with the given index.
   *
   * @param fragmentID The fragment ID
   * @return The URI of the fragment to vacuum with the given index
   * @throws TileDBError
   */
  public String getToVacuumUri(long fragmentID) throws TileDBError {
    SWIGTYPE_p_p_char uri = tiledb.new_charpp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_to_vacuum_uri(
            ctx.getCtxp(), fragmentInfop, fragmentID, uri));

    return tiledb.charpp_value(uri);
  }

  /**
   * Gets the URI of the fragment to vacuum with the given index.
   *
   * @return The URI of the fragment to vacuum with the given index
   * @throws TileDBError
   */
  public String dump() throws TileDBError {
    SWIGTYPE_p_p_char uri = tiledb.new_charpp();

    ctx.handleError(tiledb.tiledb_fragment_info_dump_stdout(ctx.getCtxp(), fragmentInfop));

    return tiledb.charpp_value(uri);
  }

  /**
   * Retrieves the number of MBRs from the fragment.
   *
   * <p>In the case of sparse fragments, this is the number of physical tiles.
   *
   * <p>Dense fragments do not contain MBRs.
   *
   * @param fragmentID The index of the fragment of interest.
   * @return The number of MBRs.
   * @throws TileDBError
   */
  public long getMBRNum(long fragmentID) throws TileDBError {
    SWIGTYPE_p_unsigned_long_long numFrags = tiledb.new_ullp();
    ctx.handleError(
        tiledb.tiledb_fragment_info_get_mbr_num(
            ctx.getCtxp(), this.fragmentInfop, fragmentID, numFrags));
    return tiledb.ullp_value(numFrags).longValue();
  }

  /**
   * Retrieves the MBR from a given fragment for a given dimension index.
   *
   * @param fragmentID The index of the fragment of interest.
   * @param mid The mbr of the fragment of interest.
   * @param dimensionID The dimension index, following the order as it was defined in the domain of
   *     the array schema.
   * @return The MBR.
   */
  public long[] getMBRFromIndex(long fragmentID, long dimensionID, long mid) throws TileDBError {
    long[] mbr;
    try (NativeArray mbrArray = new NativeArray(ctx, 2, Long.class)) {
      ctx.handleError(
          tiledb.tiledb_fragment_info_get_mbr_from_index(
              ctx.getCtxp(),
              this.fragmentInfop,
              fragmentID,
              mid,
              dimensionID,
              mbrArray.toVoidPointer()));
      mbr = (long[]) mbrArray.toJavaArray();
    }
    return mbr;
  }

  /**
   * Retrieves the MBR from a given fragment for a given dimension index.
   *
   * @param fragmentID The index of the fragment of interest.
   * @param mid The mbr of the fragment of interest.
   * @param dimName The dimension name.
   * @return The MBR.
   */
  public long[] getMBRFromName(long fragmentID, String dimName, long mid) throws TileDBError {
    long[] mbr;
    try (NativeArray mbrArray = new NativeArray(ctx, 2, Long.class)) {
      ctx.handleError(
          tiledb.tiledb_fragment_info_get_mbr_from_name(
              ctx.getCtxp(),
              this.fragmentInfop,
              fragmentID,
              mid,
              dimName,
              mbrArray.toVoidPointer()));
      mbr = (long[]) mbrArray.toJavaArray();
    }
    return mbr;
  }

  /**
   * Returns the MBR of the fragment with the given index on the given dimension index. Applicable
   * to string dimensions.
   *
   * @param fragmentID The index of the fragment of interest.
   * @param mid The mbr of the fragment of interest.
   * @param dimId The dimension index, following the order as it was defined in the domain of the
   *     array schema.
   * @return The MBR.
   */
  public Pair<Long, Long> getMBRVarSizeFromIndex(long fragmentID, long dimId, long mid)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();
    ctx.handleError(
        tiledb.tiledb_fragment_info_get_mbr_var_size_from_index(
            ctx.getCtxp(), this.fragmentInfop, fragmentID, mid, dimId, startSize, endSize));
    return new Pair<>(
        tiledb.ullp_value(startSize).longValue(), tiledb.ullp_value(endSize).longValue());
  }

  /**
   * Returns the MBR of the fragment with the given index on the given dimension name. Applicable to
   * string dimensions.
   *
   * @param fragmentID The index of the fragment of interest.
   * @param mid The mbr of the fragment of interest.
   * @param dimName The dimension name.
   * @return The MBR.
   */
  public Pair<Long, Long> getMBRVarSizeFromName(long fragmentID, String dimName, long mid)
      throws TileDBError {
    SWIGTYPE_p_unsigned_long_long startSize = tiledb.new_ullp();
    SWIGTYPE_p_unsigned_long_long endSize = tiledb.new_ullp();
    ctx.handleError(
        tiledb.tiledb_fragment_info_get_mbr_var_size_from_name(
            ctx.getCtxp(), this.fragmentInfop, fragmentID, mid, dimName, startSize, endSize));
    return new Pair<>(
        tiledb.ullp_value(startSize).longValue(), tiledb.ullp_value(endSize).longValue());
  }

  /**
   * Returns the MBR of the fragment with the given index on the given dimension index. Applicable
   * to string dimensions.
   *
   * @param fragmentID The index of the fragment of interest.
   * @param mid The mbr of the fragment of interest.
   * @param dimId The dimension index, following the order as it was defined in the domain of the
   *     array schema.
   * @return The MBR.
   */
  public Pair<String, String> getMBRVarFromIndex(long fragmentID, long dimId, long mid)
      throws TileDBError {
    Pair<Long, Long> size = this.getMBRVarSizeFromIndex(fragmentID, dimId, mid);

    try (NativeArray start = new NativeArray(ctx, size.getFirst().intValue(), String.class);
        NativeArray end = new NativeArray(ctx, size.getSecond().intValue(), String.class); ) {
      ctx.handleError(
          tiledb.tiledb_fragment_info_get_mbr_var_from_index(
              ctx.getCtxp(),
              this.fragmentInfop,
              fragmentID,
              mid,
              dimId,
              start.toVoidPointer(),
              end.toVoidPointer()));
      Object st = new String((byte[]) start.toJavaArray());
      Object e = new String((byte[]) end.toJavaArray());
      return new Pair(st, e);
    }
  }

  /**
   * Returns the MBR of the fragment with the given index on the given dimension name. Applicable to
   * string dimensions.
   *
   * @param fragmentID The index of the fragment of interest.
   * @param mid The mbr of the fragment of interest.
   * @param dimName The dimension name.
   * @return The MBR.
   */
  public Pair<String, String> getMBRVarFromName(long fragmentID, String dimName, long mid)
      throws TileDBError {
    Pair<Long, Long> size = this.getMBRVarSizeFromName(fragmentID, dimName, mid);

    try (NativeArray start = new NativeArray(ctx, size.getFirst().intValue(), String.class);
        NativeArray end = new NativeArray(ctx, size.getSecond().intValue(), String.class); ) {
      ctx.handleError(
          tiledb.tiledb_fragment_info_get_mbr_var_from_name(
              ctx.getCtxp(),
              this.fragmentInfop,
              fragmentID,
              mid,
              dimName,
              start.toVoidPointer(),
              end.toVoidPointer()));
      Object st = new String((byte[]) start.toJavaArray());
      Object e = new String((byte[]) end.toJavaArray());
      return new Pair(st, e);
    }
  }

  /**
   * Get the fragment info schema name.
   *
   * @param fragmentID The fragment info object.
   * @return The schema name.
   * @throws TileDBError
   */
  public String getArraySchemaName(long fragmentID) throws TileDBError {
    SWIGTYPE_p_p_char name = tiledb.new_charpp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_array_schema_name(
            ctx.getCtxp(), fragmentInfop, fragmentID, name));

    return tiledb.charpp_value(name);
  }

  /**
   * Get the fragment name.
   *
   * @param fragmentID The fragment info object.
   * @return The fragment name.
   * @throws TileDBError
   */
  public String getFragmentName(long fragmentID) throws TileDBError {
    SWIGTYPE_p_p_char name = tiledb.new_charpp();

    ctx.handleError(
        tiledb.tiledb_fragment_info_get_fragment_name(
            ctx.getCtxp(), fragmentInfop, fragmentID, name));

    return tiledb.charpp_value(name);
  }
}
