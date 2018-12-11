package io.tiledb.java.api;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class VFSTest {

  @Rule public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void testVFSWithoutConfig() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Assert.assertTrue(vfs instanceof VFS);
    }
  }

  @Test
  public void testVFSWithConfig() throws Exception {
    try (Context ctx = new Context();
        Config config = new Config()) {
      // check default s3 scheme
      Assert.assertEquals("https", config.get("vfs.s3.scheme"));
      // set config to not use default s3 scheme
      config.set("vfs.s3.scheme", "http");
      Assert.assertEquals("http", config.get("vfs.s3.scheme"));
      try (VFS vfs = new VFS(ctx, config);
          Config vfsConfig = vfs.getConfig()) {
        Assert.assertTrue(vfsConfig instanceof Config);
        Assert.assertEquals("http", vfsConfig.get("vfs.s3.scheme"));
      }
    }
  }

  @Test
  public void testVFSIsSupportedFs() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Assert.assertTrue(vfs.isSupportedFs("file://"));
      Assert.assertTrue(vfs.isSupportedFs(new URI("file:///path/to/resource")));

      Assert.assertEquals(vfs.isSupportedFs("s3://"), vfs.isSupportedFs(Filesystem.TILEDB_S3));

      Assert.assertEquals(
          vfs.isSupportedFs("s3"), vfs.isSupportedFs(new URI("s3://bucket/path/to/resource")));

      Assert.assertEquals(vfs.isSupportedFs("hdfs://"), vfs.isSupportedFs(Filesystem.TILEDB_HDFS));

      Assert.assertEquals(
          vfs.isSupportedFs("hdfs"), vfs.isSupportedFs(new URI("hdfs:///path/to/resource")));
    }
  }

  @Test
  public void testVFSIsFile() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      File fooFile = tmp.newFile("foo");
      File barDir = tmp.newFolder("bar");

      Assert.assertTrue(vfs.isFile(fooFile.toString()));
      // workaround for TileDB #1097
      Assert.assertTrue(vfs.isFile(URI.create("file://".concat(fooFile.getPath()))));

      Assert.assertFalse(vfs.isFile(barDir.toString()));
      // workaround for TileDB #1097
      Assert.assertFalse(vfs.isFile(URI.create("file://".concat(barDir.getPath()))));
    }
  }

  @Test
  public void testVFSIsDir() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      File fooFile = tmp.newFile("foo");
      File barDir = tmp.newFolder("bar");

      Assert.assertTrue(vfs.isDirectory(barDir.toString()));
      // workaround for TileDB #1097
      Assert.assertTrue(vfs.isDirectory(URI.create("file://".concat(barDir.getPath()))));

      Assert.assertFalse(vfs.isDirectory(fooFile.toString()));
      // workaround for TileDB #1097
      Assert.assertFalse(vfs.isDirectory(URI.create("file://".concat(fooFile.getPath()))));
    }
  }

  @Test
  public void testVFSCreateDirectory() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String dirPath = Paths.get(tmp.getRoot().toString(), "foo").toString();
      String newDir = vfs.createDirectory(dirPath);
      Assert.assertTrue(vfs.isDirectory(newDir));
    }
  }

  @Test
  public void testVFSCreateFile() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String filePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      vfs.createFile(filePath);
      Assert.assertTrue(vfs.isFile(filePath));
    }
  }

  @Test
  public void testVFSCreateFileURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String filePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      URI fileURI = URI.create("file://".concat(filePath));
      vfs.createFile(fileURI);
      Assert.assertTrue(vfs.isFile(fileURI));
    }
  }

  @Test
  public void testVFSRemoveDirectory() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String dirPath = Paths.get(tmp.getRoot().toString(), "bar").toString();

      vfs.createDirectory(dirPath);
      Assert.assertTrue(vfs.isDirectory(dirPath));

      vfs.removeDirectory(dirPath);
      Assert.assertFalse(vfs.isDirectory(dirPath));
    }
  }

  @Test
  public void testVFSRemoveDirectoryURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String dirPath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      URI dirURI = URI.create("file://".concat(dirPath));

      vfs.createDirectory(dirURI);
      Assert.assertTrue(vfs.isDirectory(dirURI));

      vfs.removeDirectory(dirURI);
      Assert.assertFalse(vfs.isDirectory(dirURI));
    }
  }

  @Test
  public void testVFSRemoveFile() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String filePath = Paths.get(tmp.getRoot().toString(), "bar").toString();

      vfs.createFile(filePath);
      Assert.assertTrue(vfs.isFile(filePath));

      vfs.removeFile(filePath);
      Assert.assertFalse(vfs.isFile(filePath));
    }
  }

  @Test
  public void testVFSRemoveFileURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String filePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      URI fileURI = URI.create("file://".concat(filePath));

      vfs.createFile(fileURI);
      Assert.assertTrue(vfs.isFile(fileURI));

      vfs.removeFile(fileURI);
      Assert.assertFalse(vfs.isFile(fileURI));
    }
  }

  @Test
  public void testVFSFileSize() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String filePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      vfs.createFile(filePath);
      Assert.assertEquals(vfs.fileSize(filePath), 0L);
      Files.write(Paths.get(filePath), new byte[] {1, 2, 3});
      Assert.assertEquals(vfs.fileSize(filePath), 3L);
    }
  }

  @Test
  public void testVFSDirectoryMove() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      // create directory
      String sourcePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      vfs.createDirectory(sourcePath);
      Assert.assertTrue(vfs.isDirectory(sourcePath));

      // rename, move directory
      String destPath = Paths.get(tmp.getRoot().toString(), "baz").toString();
      vfs.moveDirectory(sourcePath, destPath);
      Assert.assertFalse(vfs.isDirectory(sourcePath));
      Assert.assertTrue(vfs.isDirectory(destPath));
    }
  }

  @Test
  public void testVFSDirectoryMoveURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      // create directory
      String sourcePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      URI sourceURI = URI.create("file://".concat(sourcePath));
      vfs.createDirectory(sourceURI);
      Assert.assertTrue(vfs.isDirectory(sourceURI));

      // rename, move directory
      String destPath = Paths.get(tmp.getRoot().toString(), "baz").toString();
      URI destURI = URI.create("file://".concat(destPath));
      vfs.moveDirectory(sourceURI, destURI);
      Assert.assertFalse(vfs.isDirectory(sourceURI));
      Assert.assertTrue(vfs.isDirectory(destURI));
    }
  }

  @Test
  public void testVFSFileMove() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      // create file
      String sourcePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      vfs.createFile(sourcePath);
      Assert.assertTrue(vfs.isFile(sourcePath));

      // rename, move file
      String destPath = Paths.get(tmp.getRoot().toString(), "baz").toString();
      vfs.moveFile(sourcePath, destPath);
      Assert.assertFalse(vfs.isFile(sourcePath));
      Assert.assertTrue(vfs.isFile(destPath));
    }
  }

  @Test
  public void testVFSFileMoveURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      // create file
      String sourcePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      URI sourceURI = URI.create("file://".concat(sourcePath));
      vfs.createFile(sourceURI);
      Assert.assertTrue(vfs.isFile(sourceURI));

      // rename, move file
      String destPath = Paths.get(tmp.getRoot().toString(), "baz").toString();
      URI destURI = URI.create("file://".concat(destPath));
      vfs.moveFile(sourceURI, destURI);
      Assert.assertFalse(vfs.isFile(sourceURI));
      Assert.assertTrue(vfs.isFile(destURI));
    }
  }

  @Test
  public void testVFSWriteRead() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String sourcePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      vfs.write(sourcePath, new byte[] {1, 2, 3});
      Assert.assertEquals(vfs.fileSize(sourcePath), 3L);
      byte[] resultBytes = vfs.readAllBytes(sourcePath);
      Assert.assertTrue(Arrays.equals(new byte[] {1, 2, 3}, resultBytes));
      // append bytes
      vfs.write(sourcePath, new byte[] {4, 5, 6}, VFSMode.TILEDB_VFS_APPEND);
      Assert.assertEquals(vfs.fileSize(sourcePath), 6L);
      resultBytes = vfs.readAllBytes(sourcePath);
      Assert.assertTrue(Arrays.equals(new byte[] {1, 2, 3, 4, 5, 6}, resultBytes));
    }
  }

  @Test
  public void testVFSWriteReadEmpty() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String sourcePath = Paths.get(tmp.getRoot().toString(), "bar").toString();
      URI sourceURI = URI.create("file://".concat(sourcePath));
      vfs.write(sourceURI, new byte[] {});
      Assert.assertEquals(vfs.fileSize(sourceURI), 0L);
      byte[] resultBytes = vfs.readAllBytes(sourceURI);
      Assert.assertTrue(Arrays.equals(new byte[] {}, resultBytes));
    }
  }
}
