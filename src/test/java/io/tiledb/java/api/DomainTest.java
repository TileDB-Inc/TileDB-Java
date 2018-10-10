package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Test;

public class DomainTest {

  @Test
  public void testDomain() throws Exception {
    Context ctx = new Context();
    try (Domain domain = new Domain(ctx);
        Dimension<Integer> d1 =
            new Dimension<Integer>(ctx, "d1", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
        Dimension<Integer> d2 =
            new Dimension<Integer>(ctx, "d2", Integer.class, new Pair<Integer, Integer>(1, 4), 2)) {

      domain.addDimension(d1);
      domain.addDimension(d2);

      Assert.assertEquals(domain.getRank(), 2l);
      Assert.assertEquals(domain.getType(), Datatype.TILEDB_INT32);

      // test positional domain indexing
      try (Dimension d = domain.getDimension(0)) {
        Assert.assertEquals(d.getName(), d1.getName());
      }

      try (Dimension d = domain.getDimension(1)) {
        Assert.assertEquals(d.getName(), d2.getName());
      }

      // test associative domain indexing
      try (Dimension d = domain.getDimension("d1")) {
        Assert.assertEquals(d.getName(), "d1");
      }

      try (Dimension d = domain.getDimension("d2")) {
        Assert.assertEquals(d.getName(), "d2");
      }

      // test domain dimension iterator api
      long ndims = 0l;
      DimensionIterator iter = domain.iterator();
      while (iter.hasNext()) {
        try (Dimension dim1 = iter.next();
            Dimension dim2 = domain.getDimension(ndims)) {
          Assert.assertEquals(dim1.getName(), dim2.getName());
          ndims++;
        }
      }
      Assert.assertEquals(ndims, 2l);
    }
  }
}
