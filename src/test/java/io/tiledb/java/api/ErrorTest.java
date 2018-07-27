package io.tiledb.java.api;

import examples.io.tiledb.java.api.Error;
import org.junit.Assert;
import org.junit.Test;

public class ErrorTest {

  @Test
  public void test() throws Exception {
    // Create TileDB context
    Context ctx = new Context();

    // Catch an error
    try {
      Group group = new Group(ctx, "my_group");
      group = new Group(ctx, "my_group");
      Assert.fail("Exception not thrown");
    } catch (TileDBError e) {
      System.out.println( "TileDB exception: " + e.toString());
    }

    // Set a different error handler
    ctx.setErrorHandler(new Error.CustomCallback());
    try {
      Group group = new Group(ctx, "my_group");
      Assert.fail("Exception not thrown");
    } catch (TileDBError e) {
      System.out.println( "TileDB exception: " + e.toString());
    }
  }

  public static class CustomCallback extends ContextCallback {
    /** The default error handler callback. */
    public void call(String msg) throws TileDBError {
      throw new TileDBError("Callback: " + msg);
    }
  }
}
