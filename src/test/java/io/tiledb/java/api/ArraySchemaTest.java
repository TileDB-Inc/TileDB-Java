package io.tiledb.java.api;

import static io.tiledb.java.api.Datatype.TILEDB_INT64;
import static io.tiledb.java.api.Datatype.TILEDB_UINT64;
import static io.tiledb.libtiledb.tiledb_data_order_t.TILEDB_INCREASING_DATA;

import io.tiledb.libtiledb.tiledb_datatype_t;
import java.util.Arrays;
import java.util.Collection;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ArraySchemaTest {

  @Parameterized.Parameter(0)
  public ArrayType artype;

  @Parameterized.Parameter(1)
  public Layout layout;

  @Parameterized.Parameters(name = "{index}: Test with ArrayType={0}, Layout={1}")
  public static Collection<Object[]> data() {
    Object[][] data =
        new Object[][] {
          {ArrayType.TILEDB_DENSE, Layout.TILEDB_ROW_MAJOR},
          {ArrayType.TILEDB_SPARSE, Layout.TILEDB_HILBERT}
        };
    return Arrays.asList(data);
  }

  public ArraySchema schemaCreate(Context ctx, ArrayType arrayType, Layout cellOrder)
      throws TileDBError {
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1L, 4L), 2l);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    ArraySchema schema = new ArraySchema(ctx, arrayType);
    schema.setTileOrder(Layout.TILEDB_ROW_MAJOR);
    schema.setCellOrder(cellOrder);
    Assert.assertNotNull(schema.toString());
    schema.setDomain(domain);
    schema.addAttribute(a1);
    return schema;
  }

  public ArraySchema schemaCreate(Context ctx) throws TileDBError {
    return schemaCreate(ctx, artype, layout);
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
  public void testArraySchemaValidityFilterList() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx);
        PositiveDeltaFilter filter1 = new PositiveDeltaFilter(ctx);
        GzipFilter filter2 = new GzipFilter(ctx, 5);
        FilterList filterList = new FilterList(ctx).addFilter(filter1).addFilter(filter2)) {
      schema.setValidityFilterList(filterList);
      schema.check();
      try (FilterList valFilters = schema.getValidityFilterList()) {
        Assert.assertEquals(valFilters.getNumFilters(), 2L);
        try (Filter valFilter1 = valFilters.getFilter(0L);
            Filter valFilter2 = valFilters.getFilter(1L)) {
          Assert.assertTrue(valFilter1 instanceof PositiveDeltaFilter);
          Assert.assertTrue(((PositiveDeltaFilter) valFilter1).getWindow() > 0);
          Assert.assertTrue(valFilter2 instanceof GzipFilter);
          Assert.assertEquals(((GzipFilter) valFilter2).getLevel(), 5);
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
        ArraySchema schema = schemaCreate(ctx, ArrayType.TILEDB_SPARSE, layout); ) {
      Assert.assertNotEquals(1, schema.getAllowDups());
    }
  }

  @Test
  public void testArraySchemaSetAllowDups() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx, ArrayType.TILEDB_SPARSE, layout); ) {
      schema.setAllowDups(1);
    }
  }

  @Test
  public void testArraySchemaVersion() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx, ArrayType.TILEDB_SPARSE, layout)) {
      Assert.assertEquals(21, schema.getVersion());
    }
  }

  @Test
  public void testArraySchemaGetSetAllowDups() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx, ArrayType.TILEDB_SPARSE, layout); ) {
      Assert.assertNotEquals(1, schema.getAllowDups());
      schema.setAllowDups(1);
      Assert.assertEquals(1, schema.getAllowDups());
    }
  }

  @Test
  public void testDimensionLabels() throws Exception {
    try (Context ctx = new Context();
        ArraySchema schema = schemaCreate(ctx, ArrayType.TILEDB_SPARSE, layout)) {

      GzipFilter filter = new GzipFilter(ctx, 5);
      FilterList filterList = new FilterList(ctx).addFilter(filter);

      schema.addDimensionLabel(
          new DimensionLabel(
              ctx, 0, "TESTLABEL", TILEDB_INCREASING_DATA, tiledb_datatype_t.TILEDB_UINT64));

      schema.setDimensionLabelTileExtend("TESTLABEL", 2, TILEDB_INT64);
      schema.setDimensionLabelFilterList("TESTLABEL", filterList);

      DimensionLabel dimensionLabel = schema.getDimensionLabelFromName("TESTLABEL");

      Assert.assertTrue(schema.hasDimensionLabel("TESTLABEL"));
      Assert.assertEquals("TESTLABEL", dimensionLabel.getName());
      Assert.assertEquals(0, dimensionLabel.getDimensionIndex());
      Assert.assertEquals(TILEDB_UINT64, dimensionLabel.getLabelType());
      Assert.assertEquals("label", dimensionLabel.getLabelAttrName());
      Assert.assertEquals(1, dimensionLabel.getLabelCellValNum());
      Assert.assertEquals("__labels/l0", dimensionLabel.getURI());
      Assert.assertEquals(TILEDB_INCREASING_DATA, dimensionLabel.getLabelOrder());

      Array.create("dimension_label_array", schema);
      Array array = new Array(ctx, "dimension_label_array");

      SubArray subArray = new SubArray(ctx, array);
      Assert.assertFalse(subArray.hasLabelRanges(0));

      subArray.addLabelRange("TESTLABEL", 1L, 4L, null);
      subArray.addLabelRange("TESTLABEL", 1L, 2L, null);
      Assert.assertEquals(2, subArray.getLabelRangeNum("TESTLABEL"));

      Assert.assertEquals(1L, subArray.getLabelRange("TESTLABEL", 0).getFirst());
      Assert.assertEquals(4L, subArray.getLabelRange("TESTLABEL", 0).getSecond());

      Assert.assertEquals("TESTLABEL", subArray.getLabelName(0));

      TileDBObject.remove(ctx, "dimension_label_array");
    }
  }
}
