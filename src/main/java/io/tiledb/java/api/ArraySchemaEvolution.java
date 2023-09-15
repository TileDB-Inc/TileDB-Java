package io.tiledb.java.api;

import io.tiledb.libtiledb.SWIGTYPE_p_p_tiledb_array_schema_evolution_t;
import io.tiledb.libtiledb.SWIGTYPE_p_tiledb_array_schema_evolution_t;
import io.tiledb.libtiledb.tiledb;
import java.math.BigInteger;

public class ArraySchemaEvolution implements AutoCloseable {
  private Context ctx;
  private SWIGTYPE_p_tiledb_array_schema_evolution_t evolutionp;
  private SWIGTYPE_p_p_tiledb_array_schema_evolution_t evolutionpp;

  public ArraySchemaEvolution(
      Context ctx, SWIGTYPE_p_p_tiledb_array_schema_evolution_t evolutionpp) {
    this.ctx = ctx;
    this.evolutionp = tiledb.tiledb_array_schema_evolution_tpp_value(evolutionpp);
    this.evolutionpp = evolutionpp;
  }

  public ArraySchemaEvolution(Context ctx) throws TileDBError {
    evolutionpp = tiledb.new_tiledb_array_schema_evolution_tpp();
    try {
      ctx.handleError(tiledb.tiledb_array_schema_evolution_alloc(ctx.getCtxp(), evolutionpp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_evolution_tpp(evolutionpp);
      throw err;
    }

    evolutionp = tiledb.tiledb_array_schema_evolution_tpp_value(evolutionpp);
    this.ctx = ctx;
  }

  public SWIGTYPE_p_tiledb_array_schema_evolution_t getEvolutionp() {
    return this.evolutionp;
  }

  protected Context getCtx() {
    return this.ctx;
  }

  public void close() {
    if (evolutionp != null && evolutionpp != null) {
      tiledb.tiledb_array_schema_evolution_free(evolutionpp);
      evolutionpp = null;
      evolutionp = null;
    }
  }

  /**
   * Adds an Attribute to the array schema evolution.
   *
   * <p>**Example:** ArraySchemaEvolution schemaEvolution = new ArraySchemaEvolution(ctx);
   * schemaEvolution.addAttribute(new Attribute(ctx, "newAtt", Float.class));
   *
   * @param att The Attribute to add
   * @throws TileDBError
   */
  public void addAttribute(Attribute att) throws TileDBError {
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_evolution_add_attribute(
              ctx.getCtxp(), evolutionp, att.getAttributep()));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_evolution_tpp(evolutionpp);
      throw err;
    }
  }

  /**
   * Drops an Attribute from the array schema evolution.
   *
   * <p>**Example:** ArraySchemaEvolution schemaEvolution = new ArraySchemaEvolution(ctx);
   * schemaEvolution.dropAttribute("attName");
   *
   * @param attName The name of the Attribute to drop
   * @throws TileDBError
   */
  public void dropAttribute(String attName) throws TileDBError {
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_evolution_drop_attribute(ctx.getCtxp(), evolutionp, attName));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_evolution_tpp(evolutionpp);
      throw err;
    }
  }

  /**
   * Drops an Attribute from the array schema evolution.
   *
   * <p>**Example:** ArraySchemaEvolution schemaEvolution = new ArraySchemaEvolution(ctx);
   * schemaEvolution.dropAttribute("attName");
   *
   * @param att The Attribute to drop
   * @throws TileDBError
   */
  public void dropAttribute(Attribute att) throws TileDBError {
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_evolution_drop_attribute(
              ctx.getCtxp(), evolutionp, att.getName()));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_evolution_tpp(evolutionpp);
      throw err;
    }
  }

  /**
   * Sets timestamp range in an array schema evolution.
   *
   * @param high high value of range
   * @param low low value of range
   * @throws TileDBError
   */
  public void setTimeStampRange(BigInteger high, BigInteger low) throws TileDBError {
    Util.checkBigIntegerRange(high);
    Util.checkBigIntegerRange(low);
    try {
      ctx.handleError(
          tiledb.tiledb_array_schema_evolution_set_timestamp_range(
              ctx.getCtxp(), evolutionp, low, high));
    } catch (TileDBError err) {
      throw err;
    }
  }

  /**
   * Adds an enumeration to an array schema evolution
   *
   * @param e The enumeration to be added
   * @throws TileDBError
   */
  public void addEnumeration(Enumeration e) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_evolution_add_enumeration(
            ctx.getCtxp(), getEvolutionp(), e.getEnumerationp()));
  }

  /**
   * Drops an enumeration from an array schema evolution.
   *
   * @param name The name of the enumeration to be dropped
   * @throws TileDBError
   */
  public void dropEnumeration(String name) throws TileDBError {
    ctx.handleError(
        tiledb.tiledb_array_schema_evolution_drop_enumeration(
            ctx.getCtxp(), getEvolutionp(), name));
  }

  /**
   * Evolves the schema of an array.
   *
   * <p>**Example:** ArraySchemaEvolution schemaEvolution = new ArraySchemaEvolution(ctx);
   * schemaEvolution.dropAttribute("attName"); schemaEvolution.evolveArray("testUri")
   *
   * @param uri
   * @throws TileDBError
   */
  public void evolveArray(String uri) throws TileDBError {
    try {
      ctx.handleError(tiledb.tiledb_array_evolve(ctx.getCtxp(), uri, evolutionp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_array_schema_evolution_tpp(evolutionpp);
      throw err;
    }
  }
}
