package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ArraySchemaEvolutionTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private Context ctx;
  private String arrayURI;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toPath().resolve("schema_evolution_test_array").toString();
  }

  @After
  public void tearDown() throws Exception {
    ctx.close();
  }

  public void arrayCreate() throws Exception {
    // Create getDimensions
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 3l), 2l);
    Dimension<Long> d2 =
        new Dimension<Long>(ctx, "d2", Long.class, new Pair<Long, Long>(1l, 3l), 2l);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);
    domain.addDimension(d2);

    // Create and add getAttributes
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    a1.setNullable(true);
    Attribute a2 = new Attribute(ctx, "a2", Float.class);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);

    schema.check();

    Array.create(arrayURI, schema);
  }

  @Test
  public void testSchemaEvolution() throws Exception {

    arrayCreate();
    Array array = new Array(ctx, arrayURI);

    ArraySchemaEvolution schemaEvolution = new ArraySchemaEvolution(ctx);

    Attribute a3 = new Attribute(ctx, "a3", Float.class);
    schemaEvolution.addAttribute(a3);

    schemaEvolution.dropAttribute("a2");

    schemaEvolution.evolveArray(arrayURI);
    //    OR
    //    array.evolve(ctx, schemaEvolution);

    array.close();
    array = new Array(ctx, arrayURI);
    ArraySchema schema = array.getSchema();
    Assert.assertTrue(schema.hasAttribute("a1"));
    Assert.assertFalse(schema.hasAttribute("a2"));
    Assert.assertTrue(schema.hasAttribute("a3"));
  }
}
