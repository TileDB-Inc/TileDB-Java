package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ArrayTest {

  private Context ctx;
  private String arrayURI;

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    arrayURI = temp.getRoot().toString();
  }

  public void arrayCreate() throws Exception {
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    ArraySchema schema = new ArraySchema(ctx, ArrayType.TILEDB_DENSE);
    schema.setTileOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setCellOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.check();

    Array.create(arrayURI, schema);
  }

  @Test
  public void testArrayExists() throws Exception {
    Assert.assertFalse(Array.exists(ctx, arrayURI));
    arrayCreate();
    Assert.assertTrue(Array.exists(ctx, arrayURI));
  }
}
