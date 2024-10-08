package io.tiledb.java.api;

import static io.tiledb.java.api.CurrentDomainType.TILEDB_NDRECTANGLE;
import static io.tiledb.java.api.QueryType.TILEDB_READ;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

public class TestCurrentDomain {

  private Context ctx;
  private ArraySchema schema;
  private Domain domain;
  private String arrayUri = "current_domain_test";

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    if (Files.exists(Paths.get(arrayUri))) {
      TileDBObject.remove(ctx, arrayUri);
    }
    schema = new ArraySchema(ctx, ArrayType.TILEDB_SPARSE);
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Datatype.TILEDB_INT64, new Pair<Long, Long>(1l, 4l), 1l);
    Dimension<Integer> d2 = new Dimension<>(ctx, "d2", Datatype.TILEDB_STRING_ASCII, null, null);
    domain = new Domain(ctx);
    domain.addDimension(d1);
    domain.addDimension(d2);
    schema.setDomain(domain);

    Range range = new Range(ctx, domain, 0);
    range.setMin(1L);
    range.setMax(3L);

    Range rangeVar = new Range(ctx, domain, 1);
    rangeVar.setMin("aa");
    rangeVar.setMinSize(2);
    rangeVar.setMax("bbb");
    rangeVar.setMaxSize(3);

    NDRectangle ndRectangle = new NDRectangle(ctx, domain);
    ndRectangle.setRange(0, range);
    ndRectangle.setRange(1, rangeVar);

    CurrentDomain currentDomain = new CurrentDomain(ctx, domain);
    currentDomain.setNDRectangle(ndRectangle);

    schema.setCurrentDomain(currentDomain);

    Array.create(arrayUri, schema);

    range.close();
    ndRectangle.close();
    currentDomain.close();
  }

  @After
  public void tearDown() throws Exception {
    if (Files.exists(Paths.get(arrayUri))) {
      TileDBObject.remove(ctx, arrayUri);
    }
    ctx.close();
  }

  @Test
  public void currentDomainTest() throws TileDBError {
    // Create range and test values
    Array array = new Array(ctx, arrayUri, TILEDB_READ);
    ArraySchema schema = array.getSchema();

    CurrentDomain cd = schema.getCurrentDomain();
    Assert.assertFalse(cd.isEmpty());

    Assert.assertEquals(TILEDB_NDRECTANGLE, cd.getType());

    NDRectangle nd = cd.getNDRectangle();
    Range range = nd.getRange(0);

    Assert.assertEquals(8, range.getMinSize());
    Assert.assertEquals(1L, (long) range.getMin());

    Assert.assertEquals(8, range.getMaxSize());
    Assert.assertEquals(3L, (long) range.getMax());
    range.close();

    // Evolution test
    ArraySchemaEvolution schemaEvolution = new ArraySchemaEvolution(ctx);

    Range newRange = new Range(ctx, domain, 0);
    newRange.setMin(1L);
    newRange.setMax(4L);
    Range newRangeVar = new Range(ctx, domain, 1);
    newRangeVar.setMin("aa");
    newRangeVar.setMinSize(2);
    newRangeVar.setMax("bbb");
    newRangeVar.setMaxSize(3);

    CurrentDomain newCurrentDomain = new CurrentDomain(ctx, domain);
    NDRectangle newND = new NDRectangle(ctx, domain);
    newND.setRange(0, newRange);
    newND.setRange(1, newRangeVar);
    newCurrentDomain.setNDRectangle(newND);
    schemaEvolution.expandCurrentDomain(newCurrentDomain);
    schemaEvolution.evolveArray(arrayUri);

    Assert.assertEquals(
        4L,
        new Array(ctx, arrayUri, TILEDB_READ)
            .getSchema()
            .getCurrentDomain()
            .getNDRectangle()
            .getRange(0)
            .getMax());

    // clean up
    newRange.close();
    array.close();
    schemaEvolution.close();
    newND.close();
    newCurrentDomain.close();
  }

  @Test
  public void currentDomainVarTest() throws TileDBError {
    Array array = new Array(ctx, arrayUri, TILEDB_READ);
    ArraySchema schema = array.getSchema();

    CurrentDomain cd = schema.getCurrentDomain();
    Assert.assertFalse(cd.isEmpty());

    Assert.assertEquals(TILEDB_NDRECTANGLE, cd.getType());

    NDRectangle nd = cd.getNDRectangle();
    Range range = nd.getRange(1);

    Assert.assertEquals("aa", range.getMinVar());
    Assert.assertEquals("bbb", range.getMaxVar());
  }
}
