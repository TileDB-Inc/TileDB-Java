package io.tiledb.java.api;

import org.junit.Test;

public class CurrentDomainTest {

  @Test
  public void rangeTest() throws Exception {
    Config cf = new Config();
    Context ctx = new Context();
    Dimension<Long> d1 =
        new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 4l), 2l);
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);

    Range range = new Range(ctx, domain);
    range.setMin(0, 1L);
    range.setMax(0, 2L);
    System.out.println("a");
  }
}
