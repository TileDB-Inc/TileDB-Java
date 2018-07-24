package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_array_type_t;
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_walk_order_t;
import org.junit.Test;

import java.io.File;

public class ObjectTest {
  private Context ctx;

  @Test
  public void test() throws Exception {
    ctx = new Context();
    File dir = new File("my_group");
    if (dir.exists())
      TileDBObject.remove(ctx, "my_group");
    dir = new File("my_group2");
    if (dir.exists())
      TileDBObject.remove(ctx, "my_group2");

    createHierarchy();
    listObjects("my_group");
    moveRemoveObject();  // Renames `my_group` to `my_group_2`
    listObjects("my_group2");
  }

  private void moveRemoveObject() throws Exception {
    TileDBObject.move(ctx,"my_group", "my_group2");
    TileDBObject.remove(ctx,"my_group2/dense_arrays");
    TileDBObject.remove(ctx,"my_group2/sparse_arrays/array_C");
  }

  private void createHierarchy() throws Exception {
    // Create groups
    Group group1 = new Group(ctx, "my_group");
    Group group2 = new Group(ctx, "my_group/dense_arrays");
    Group group3 = new Group(ctx, "my_group/sparse_arrays");

    // Create arrays
    createArray("my_group/dense_arrays/array_A", tiledb_array_type_t.TILEDB_DENSE);
    createArray("my_group/dense_arrays/array_B", tiledb_array_type_t.TILEDB_DENSE);
    createArray("my_group/sparse_arrays/array_C", tiledb_array_type_t.TILEDB_SPARSE);
    createArray("my_group/sparse_arrays/array_D", tiledb_array_type_t.TILEDB_SPARSE);
  }

  private void createArray(String arrayURI, tiledb_array_type_t type) throws Exception {
    Dimension<Integer> rows = new Dimension<Integer>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols = new Dimension<Integer>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);
    Attribute a = new Attribute(ctx, "a", Integer.class);
    ArraySchema schema = new ArraySchema(ctx, type);
    schema.setTileOrder(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.setCellOrder(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a);
    Array.create(arrayURI, schema);
  }

  private void listObjects(String uri) throws Exception {
    // List children
    System.out.println( "Listing hierarchy: ");
    TileDBObjectIterator obj_iter = new TileDBObjectIterator(ctx, uri);
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);

    // Walk in a path with a pre- and post-order traversal
    System.out.println( "\nPreorder traversal: ");
    obj_iter.setRecursive(); // Default order is preorder
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);

    System.out.println( "\nPostorder traversal: ");
    obj_iter.setRecursive(tiledb_walk_order_t.TILEDB_POSTORDER);
    for (TileDBObject object : obj_iter.getAllObjects())
      System.out.println(object);
  }
}
