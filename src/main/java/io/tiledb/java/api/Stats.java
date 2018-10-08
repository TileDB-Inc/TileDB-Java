package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb;

/**
 * Encapsulates functionality related to internal TileDB statistics.
 *
 * <p><b>Example:</b>
 *
 * <pre>{@code
 * // Enable stats, submit a query, then dump to stdout.
 * Stats.enable();
 * query.submit();
 * Stats.dump();
 * }</pre>
 */
public class Stats {

  /**
   * Enables internal TileDB statistics gathering.
   *
   * @exception TileDBError A TileDB exception
   */
  public static void enable() throws TileDBError {
    check_error(tiledb.tiledb_stats_enable(), "error enabling stats");
  }

  /**
   * Disables internal TileDB statistics gathering.
   *
   * @exception TileDBError A TileDB exception
   */
  public static void disable() throws TileDBError {
    check_error(tiledb.tiledb_stats_disable(), "error disabling stats");
  }

  /**
   * Reset all internal statistics counters.
   *
   * @exception TileDBError A TileDB exception
   */
  public static void reset() throws TileDBError {
    check_error(tiledb.tiledb_stats_reset(), "error resetting stats");
  }

  /**
   * Dump all statistics counters to a file
   *
   * @param filename A path string
   * @exception TileDBError A TileDB exception
   */
  public static void dump(String filename) throws TileDBError {
    check_error(tiledb.tiledb_stats_dump_file(filename), "error dumping stats");
  }

  /**
   * Dump all statistics counters to stdout.
   *
   * @exception TileDBError A TileDB exception
   */
  public static void dump() throws TileDBError {
    check_error(tiledb.tiledb_stats_dump_stdout(), "error dumping stats");
  }

  /**
   * Checks the return code for TILEDB_OK and throws an exception if not.
   *
   * @param rc Return code to check
   * @param msg Exception message string
   * @exception TileDBError
   */
  private static void check_error(int rc, String msg) throws TileDBError {
    if (rc != tiledb.TILEDB_OK) throw new TileDBError("Stats Error: " + msg);
  }
}
