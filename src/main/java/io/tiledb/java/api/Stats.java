package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_FILE;
import io.tiledb.libtiledb.tiledb;


/**
 * Encapsulates functionality related to internal TileDB statistics.
 *
 * **Example:**
 * @code{.java}
 * // Enable stats, submit a query, then dump to stdout.
 * Stats.enable();
 * query.submit();
 * Stats.dump();
 * @endcode
 */
public class Stats {
  /** Enables internal TileDB statistics gathering. */
  public static void enable() throws TileDBError {
    check_error(tiledb.tiledb_stats_enable(), "error enabling stats");
  }

  /** Disables internal TileDB statistics gathering. */
  public static void disable() throws TileDBError {
    check_error(tiledb.tiledb_stats_disable(), "error disabling stats");
  }

  /** Reset all internal statistics counters to 0. */
  public static void reset() throws TileDBError {
    check_error(tiledb.tiledb_stats_reset(), "error resetting stats");
  }

  /**
   * Dump all statistics counters to a file
   */
  public static void dump(String filename) throws TileDBError {
    check_error(tiledb.tiledb_stats_dump_file(filename), "error dumping stats");
  }

  /**
   * Dump all statistics counters to stdout.
   */
  public static void dump() throws TileDBError {
    check_error(tiledb.tiledb_stats_dump_stdout(), "error dumping stats");
  }

  /**
   * Checks the return code for TILEDB_OK and throws an exception if not.
   *
   * @param rc Return code to check
   * @param msg Exception message string
   */
  private static void check_error(int rc, String msg) throws TileDBError {
    if (rc != tiledb.TILEDB_OK)
      throw new TileDBError("Stats Error: " + msg);
  }
}
