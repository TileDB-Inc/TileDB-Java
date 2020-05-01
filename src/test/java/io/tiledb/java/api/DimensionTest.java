package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Test;

public class DimensionTest {

  @Test
  public void testDimension() throws Exception {
    try (Context ctx = new Context();
        Dimension<Integer> dim = new Dimension<>(ctx, "d1", Integer.class, new Pair<>(1, 10), 10)) {
      try (FilterList filterList = new FilterList(ctx)) {
        filterList.addFilter(new GzipFilter(ctx, 3));
        filterList.addFilter(new Bzip2Filter(ctx, 1));
        filterList.addFilter(new DoubleDeltaFilter(ctx, -1));
        dim.setFilterList(filterList);
      }
      Assert.assertEquals("d1", dim.getName());
      Assert.assertEquals(Datatype.TILEDB_INT32, dim.getType());
      Assert.assertEquals((Integer) 10, dim.getTileExtent());
      Assert.assertEquals((Integer) 1, dim.getDomain().getFirst());
      Assert.assertEquals((Integer) 10, dim.getDomain().getSecond());

      // Filter List Tests
      FilterList filterList = dim.getFilterList();
      Assert.assertEquals(GzipFilter.class, filterList.getFilter(0).getClass());
      Assert.assertEquals(3, ((GzipFilter) filterList.getFilter(0)).getLevel());

      Assert.assertEquals(Bzip2Filter.class, filterList.getFilter(1).getClass());
      Assert.assertEquals(1, ((Bzip2Filter) filterList.getFilter(1)).getLevel());

      Assert.assertEquals(DoubleDeltaFilter.class, filterList.getFilter(2).getClass());
      Assert.assertEquals(-1, ((DoubleDeltaFilter) filterList.getFilter(2)).getLevel());
    }
  }

  @Test
  public void testDimensionDatatype() throws Exception {
    try (Context ctx = new Context();
        Dimension<Integer> dim =
            new Dimension<>(ctx, "d1", Datatype.TILEDB_INT32, new Pair<>(1, 10), 10)) {
      Assert.assertEquals("d1", dim.getName());
      Assert.assertEquals(Datatype.TILEDB_INT32, dim.getType());
      Assert.assertEquals((Integer) 10, dim.getTileExtent());
      Assert.assertEquals((Integer) 1, dim.getDomain().getFirst());
      Assert.assertEquals((Integer) 10, dim.getDomain().getSecond());
    }
  }

  @Test(expected = java.lang.ClassCastException.class)
  public void testInvalidDimensionDatatype() throws Exception {
    try (Context ctx = new Context();
        Dimension<Integer> dim =
            new Dimension<>(ctx, "d1", Datatype.TILEDB_FLOAT64, new Pair<>(1, 10), 10)) {
      Assert.assertEquals("d1", dim.getName());
    }
  }

  @Test
  public void testSetCellValNum() throws Exception {
    try (Context ctx = new Context();
        Dimension<Integer> dim =
            new Dimension<>(ctx, "d1", Datatype.TILEDB_INT32, new Pair<>(1, 10), 10)) {

      dim.setCellValNum(1);
    }
  }

  @Test
  public void testGetCellValNum() throws Exception {
    try (Context ctx = new Context();
        Dimension<Integer> dim =
            new Dimension<>(ctx, "d1", Datatype.TILEDB_INT32, new Pair<>(1, 10), 10)) {

      Assert.assertEquals(1, dim.getCellValNum());
    }
  }
}
