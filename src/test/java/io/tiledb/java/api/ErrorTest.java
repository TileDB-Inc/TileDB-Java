package io.tiledb.java.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.nio.file.Files;
import java.nio.file.Paths;

import examples.io.tiledb.java.api.Error;

public class ErrorTest {

  private Context ctx;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    if (Files.exists(Paths.get("my_group"))) {
      TileDBObject.remove(ctx, "my_group");
    }
  }

  @After
  public void teardown() throws Exception {
    if (Files.exists(Paths.get("my_group"))) {
      TileDBObject.remove(ctx, "my_group");
    }
  }

  @Test
  public void test() throws Exception {
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
