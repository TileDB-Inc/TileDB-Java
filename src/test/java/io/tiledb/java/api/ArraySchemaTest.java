package io.tiledb.java.api;

import org.junit.*;

public class ArraySchemaTest {

  public ArraySchema schemaCreate(Context ctx, ArrayType arrayType) throws TileDBError {
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    ArraySchema schema = new ArraySchema(ctx, arrayType);
    schema.setTileOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setCellOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    return schema;
  }

  public ArraySchema schemaCreate(Context ctx) throws TileDBError {
    return schemaCreate(ctx, ArrayType.TILEDB_DENSE);
  }

  @Test
  public void testArraySchemaCoordsFilterList() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx);
        GzipFilter filter = new GzipFilter(ctx, 5);
        FilterList filterList = new FilterList(ctx).addFilter(filter)) {
      schema.setCoodsFilterList(filterList);
      schema.check();
      try (FilterList coordsFilters = schema.getCoordsFilterList()) {
        Assert.assertEquals(coordsFilters.getNumFilters(), 1L);
        try (Filter coordsFilter = coordsFilters.getFilter(0L)) {
          Assert.assertTrue(coordsFilter instanceof GzipFilter);
          Assert.assertEquals(((GzipFilter) coordsFilter).getLevel(), 5);
        }
      }
    }
  }

  @Test
  public void testArraySchemaOffsetsFilterList() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx);
        PositiveDeltaFilter filter1 = new PositiveDeltaFilter(ctx);
        GzipFilter filter2 = new GzipFilter(ctx, 5);
        FilterList filterList = new FilterList(ctx).addFilter(filter1).addFilter(filter2)) {
      schema.setCoodsFilterList(filterList);
      schema.check();
      try (FilterList offsFilters = schema.getCoordsFilterList()) {
        Assert.assertEquals(offsFilters.getNumFilters(), 2L);
        try (Filter offsFilter1 = offsFilters.getFilter(0L);
            Filter offsFilter2 = offsFilters.getFilter(1L)) {
          Assert.assertTrue(offsFilter1 instanceof PositiveDeltaFilter);
          Assert.assertTrue(((PositiveDeltaFilter) offsFilter1).getWindow() > 0);
          Assert.assertTrue(offsFilter2 instanceof GzipFilter);
          Assert.assertEquals(((GzipFilter) offsFilter2).getLevel(), 5);
        }
      }
    }
  }

  @Test
  public void testArraySchemaHasAttribute() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx)) {
      Assert.assertTrue(schema.hasAttribute("a1"));
      Assert.assertFalse(schema.hasAttribute(""));
      Assert.assertFalse(schema.hasAttribute("b1"));
    }
  }

  @Test
  public void testArraySchemaDomainHasDimension() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx);
        Domain domain = schema.getDomain()) {
      Assert.assertTrue(domain.hasDimension("d1"));
      Assert.assertFalse(domain.hasDimension(""));
      Assert.assertFalse(domain.hasDimension("a1"));
    }
  }

  @Test
  public void testArraySchemaGetAllowDups() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx, ArrayType.TILEDB_SPARSE); ) {
      Assert.assertNotEquals(1, schema.getAllowDups());
    }
  }

  @Test
  public void testArraySchemaSetAllowDups() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx, ArrayType.TILEDB_SPARSE); ) {
      schema.setAllowDups(1);
    }
  }

  @Test
  public void testArraySchemaGetSetAllowDups() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx, ArrayType.TILEDB_SPARSE); ) {
      Assert.assertNotEquals(1, schema.getAllowDups());
      schema.setAllowDups(1);
      Assert.assertEquals(1, schema.getAllowDups());
    }
  }
}
