package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.*;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;

import java.nio.file.Path;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ObjectTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private Context ctx;

  private Path root;

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    root = temp.getRoot().toPath();
  }

  @Test
  public void test() throws Exception {
    createHierarchy();

    listTest("my_group", new String[] {"my_group/dense_arrays", "my_group/sparse_arrays"});

    listPreorderTest(
        "my_group",
        new String[] {
          "my_group/dense_arrays",
          "my_group/dense_arrays/array_A",
          "my_group/dense_arrays/array_B",
          "my_group/sparse_arrays",
          "my_group/sparse_arrays/array_C",
          "my_group/sparse_arrays/array_D"
        });

    listPostorderTest(
        "my_group",
        new String[] {
          "my_group/dense_arrays/array_A",
          "my_group/dense_arrays/array_B",
          "my_group/dense_arrays",
          "my_group/sparse_arrays/array_C",
          "my_group/sparse_arrays/array_D",
          "my_group/sparse_arrays"
        });

    moveRemoveObject(); // Renames `my_group` to `my_group_2`

    listTest("my_group2", new String[] {"my_group2/sparse_arrays"});

    listPreorderTest(
        "my_group2", new String[] {"my_group2/sparse_arrays", "my_group2/sparse_arrays/array_D"});

    listPostorderTest(
        "my_group2", new String[] {"my_group2/sparse_arrays/array_D", "my_group2/sparse_arrays"});
  }

  private void moveRemoveObject() throws Exception {
    TileDBObject.move(
        ctx, root.resolve("my_group").toString(), root.resolve("my_group2").toString());
    TileDBObject.remove(ctx, root.resolve("my_group2/dense_arrays").toString());
    TileDBObject.remove(ctx, root.resolve("my_group2/sparse_arrays/array_C").toString());
  }

  private void createHierarchy() throws Exception {
    // Create groups
    new Group(ctx, root.resolve("my_group").toString());
    new Group(ctx, root.resolve("my_group/dense_arrays").toString());
    new Group(ctx, root.resolve("my_group/sparse_arrays").toString());

    // Create arrays
    createArray(root.resolve("my_group/dense_arrays/array_A").toString(), TILEDB_DENSE);
    createArray(root.resolve("my_group/dense_arrays/array_B").toString(), TILEDB_DENSE);
    createArray(root.resolve("my_group/sparse_arrays/array_C").toString(), TILEDB_SPARSE);
    createArray(root.resolve("my_group/sparse_arrays/array_D").toString(), TILEDB_SPARSE);
  }

  private void createArray(String arrayURI, ArrayType type) throws Exception {
    Dimension<Integer> rows =
        new Dimension<Integer>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols =
        new Dimension<Integer>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);
    Attribute a = new Attribute(ctx, "a", Integer.class);
    ArraySchema schema = new ArraySchema(ctx, type);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a);
    Array.create(arrayURI, schema);
  }

  private void assertUriEndsWith(List<TileDBObject> objs, String[] expected) throws Exception {
    Assert.assertEquals(objs.size(), expected.length);
    for (int i = 0; i < expected.length; i++) {
      Assert.assertTrue(objs.get(i).getUri().endsWith(expected[i]));
    }
  }

  private void listTest(String uri, String[] expected) throws Exception {
    TileDBObjectIterator obj_iter = new TileDBObjectIterator(ctx, root.resolve(uri).toString());
    List<TileDBObject> objs = obj_iter.getAllObjects();
    assertUriEndsWith(objs, expected);
  }

  private void listPreorderTest(String uri, String[] expected) throws Exception {
    TileDBObjectIterator obj_iter = new TileDBObjectIterator(ctx, root.resolve(uri).toString());
    obj_iter.setRecursive(WalkOrder.TILEDB_PREORDER);
    List<TileDBObject> objs = obj_iter.getAllObjects();
    assertUriEndsWith(objs, expected);
  }

  private void listPostorderTest(String uri, String[] expected) throws Exception {
    TileDBObjectIterator obj_iter = new TileDBObjectIterator(ctx, root.resolve(uri).toString());
    obj_iter.setRecursive(WalkOrder.TILEDB_POSTORDER);
    List<TileDBObject> objs = obj_iter.getAllObjects();
    assertUriEndsWith(objs, expected);
  }
}
