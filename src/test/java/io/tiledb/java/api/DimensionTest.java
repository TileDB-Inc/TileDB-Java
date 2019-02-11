package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Test;

public class DimensionTest {

  @Test
  public void testDimension() throws Exception {
    try (Context ctx = new Context();
        Dimension<Integer> dim = new Dimension<>(ctx, "d1", Integer.class, new Pair<>(1, 10), 10)) {
      Assert.assertEquals("d1", dim.getName());
      Assert.assertEquals(Datatype.TILEDB_INT32, dim.getType());
      Assert.assertEquals((Integer) 10, dim.getTileExtent());
      Assert.assertEquals((Integer) 1, dim.getDomain().getFirst());
      Assert.assertEquals((Integer) 10, dim.getDomain().getSecond());
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
}
