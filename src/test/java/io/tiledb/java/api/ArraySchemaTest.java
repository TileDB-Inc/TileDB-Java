package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Test;

public class ArraySchemaTest {

  @Test
  public void testArraySchema() throws Exception {
    Context ctx = new Context();
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    Attribute a2 = new Attribute(ctx, "a2", Long.class);
    ArraySchema schema = new ArraySchema(ctx, ArrayType.TILEDB_DENSE);
    schema.setTileOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setCellOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);
    schema.check();

    Assert.assertEquals(schema.getArrayType(), ArrayType.TILEDB_DENSE);
    Assert.assertEquals(schema.getTileOrder(), Layout.TILEDB_ROW_MAJOR);
    Assert.assertEquals(schema.getCellOrder(), Layout.TILEDB_ROW_MAJOR);

    try (Domain dom = schema.getDomain()) {
      Assert.assertEquals(dom.getRank(), 1);
    }
    try (Attribute a = schema.getAttribute(0)) {
      Assert.assertEquals(a.getName(), "a1");
    }
    try (Attribute a = schema.getAttribute(0)) {
      Assert.assertEquals(a.getName(), "a1");
    }
    try (Attribute a = schema.getAttribute(1)) {
      Assert.assertEquals(a.getName(), "a2");
    }
    try (Attribute a = schema.getAttribute("a2")) {
      Assert.assertEquals(a.getName(), "a2");
    }

    long nattr = 0l;
    AttributeIterator iter = schema.iterator();
    while (iter.hasNext()) {
      try (Attribute attr1 = iter.next();
          Attribute attr2 = schema.getAttribute(nattr)) {
        Assert.assertEquals(attr1.getName(), attr2.getName());
        nattr++;
      }
    }
    Assert.assertEquals(nattr, 2);
  }
}
