package io.tiledb.java.api;

/** This file declares the Java API for the TileDB consolidation plan. */
import io.tiledb.libtiledb.SWIGTYPE_p_p_char;
import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_consolidation_plan_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_consolidation_plan_t;
import io.tiledb.libtiledb.SWIGTYPE_p_unsigned_long_long;
import io.tiledb.libtiledb.tiledb;
import java.math.BigInteger;

public class ConsolidationPlan implements AutoCloseable {

  private SWIGTYPE_p_p_tiledb_consolidation_plan_t conspp;

  private SWIGTYPE_p_tiledb_consolidation_plan_t consp;

  private BigInteger fragmentSize;

  private Context ctx;

  private String arrayURI;

  /**
   * Constructor
   *
   * @param fragmentSize The desired fragment size
   * @param ctx TileDB context
   * @param arrayURI The array to create the plan for
   * @throws TileDBError
   */
  public ConsolidationPlan(Context ctx, BigInteger fragmentSize, String arrayURI)
      throws TileDBError {
    this.fragmentSize = fragmentSize;
    this.ctx = ctx;
    this.arrayURI = arrayURI;
    SWIGTYPE_p_p_tiledb_consolidation_plan_t _consp = tiledb.new_tiledb_consolidation_plan_tpp();
    try (Array array = new Array(ctx, arrayURI)) {
      ctx.handleError(
          tiledb.tiledb_consolidation_plan_create_with_mbr(
              ctx.getCtxp(), array.getArrayp(), fragmentSize, _consp));
    } catch (TileDBError e) {
      tiledb.delete_tiledb_consolidation_plan_tpp(_consp);
      throw e;
    }

    this.consp = tiledb.tiledb_consolidation_plan_tpp_value(_consp);
    this.conspp = _consp;
  }

  /**
   * Get the number of fragments for a specific node of a consolidation plan object
   *
   * @param nodeIndex The node index
   * @return The number of fragments to be retrieved
   * @throws TileDBError
   */
  public long getNumFragments(BigInteger nodeIndex) throws TileDBError {
    SWIGTYPE_p_unsigned_long_long longp = tiledb.new_ullp();
    long result;
    try {
      this.ctx.handleError(
          tiledb.tiledb_consolidation_plan_get_num_fragments(
              this.ctx.getCtxp(), this.consp, nodeIndex, longp));
      result = tiledb.ullp_value(longp).longValue();
    } finally {
      tiledb.delete_ullp(longp);
    }
    return result;
  }

  /**
   * Get the number of fragments for a specific node of a consolidation plan object
   *
   * @param nodeIndex The node index
   * @param fragmentIndex The fragment index
   * @return The fragment uri to be retrieved
   */
  public String getFragmentURI(BigInteger nodeIndex, BigInteger fragmentIndex) throws TileDBError {
    String uri;
    SWIGTYPE_p_p_char uripp = tiledb.new_charpp();
    try {
      ctx.handleError(
          tiledb.tiledb_consolidation_plan_get_fragment_uri(
              ctx.getCtxp(), this.consp, nodeIndex, fragmentIndex, uripp));
      uri = tiledb.charpp_value(uripp);
    } finally {
      tiledb.delete_charpp(uripp);
    }
    return uri;
  }

  /**
   * Dumps the consolidation plan in JSON format in a String.
   *
   * @return The output string
   * @throws TileDBError
   */
  public String dumpJSONString() throws TileDBError {
    String result;
    SWIGTYPE_p_p_char dumpStrp = tiledb.new_charpp();
    try {
      ctx.handleError(
          tiledb.tiledb_consolidation_plan_dump_json_str(ctx.getCtxp(), this.consp, dumpStrp));
      result = tiledb.charpp_value(dumpStrp);
    } finally {
      tiledb.tiledb_consolidation_plan_free_json_str(dumpStrp);
      tiledb.delete_charpp(dumpStrp);
    }
    return result;
  }

  /**
   * Get the number of nodes of a consolidation plan object
   *
   * @return The number of nodes to be retrieved
   * @throws TileDBError
   */
  public long getNumNodes() throws TileDBError {
    SWIGTYPE_p_unsigned_long_long longp = tiledb.new_ullp();
    long result;
    try {
      this.ctx.handleError(
          tiledb.tiledb_consolidation_plan_get_num_nodes(this.ctx.getCtxp(), this.consp, longp));
      result = tiledb.ullp_value(longp).longValue();
    } finally {
      tiledb.delete_ullp(longp);
    }
    return result;
  }

  @Override
  public void close() throws Exception {
    if (consp != null) {
      tiledb.tiledb_consolidation_plan_free(conspp);
      consp = null;
      conspp = null;
    }
  }
}
