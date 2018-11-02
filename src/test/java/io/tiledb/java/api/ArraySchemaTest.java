package io.tiledb.java.api;

import org.junit.*;

public class ArraySchemaTest {

  public ArraySchema schemaCreate(Context ctx) throws Exception {
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
    return schema;
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
}
