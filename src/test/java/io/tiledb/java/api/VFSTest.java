package io.tiledb.java.api;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
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
      Path fooFile = tmp.newFile("foo").toPath();
      Path barDir = tmp.newFolder("bar").toPath();

      Assert.assertTrue(vfs.isFile(fooFile.toString()));
      // workaround for TileDB #1097
      Assert.assertTrue(vfs.isFile(new File(fooFile.toUri()).toString()));

      Assert.assertFalse(vfs.isFile(barDir.toString()));
      // workaround for TileDB #1097
      Assert.assertFalse(vfs.isFile(barDir.toUri()));
    }
  }

  @Test
  public void testVFSIsDir() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path fooFile = tmp.newFile("foo").toPath();
      Path barDir = tmp.newFolder("bar").toPath();

      Assert.assertTrue(vfs.isDirectory(barDir.toString()));
      // workaround for TileDB #1097
      Assert.assertTrue(vfs.isDirectory(barDir.toUri()));

      Assert.assertFalse(vfs.isDirectory(fooFile.toString()));
      // workaround for TileDB #1097
      Assert.assertFalse(vfs.isDirectory(fooFile.toUri()));
    }
  }

  @Test
  public void testVFSCreateDirectory() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path dirPath = tmp.getRoot().toPath().resolve("foo");
      String newDir = vfs.createDirectory(dirPath.toString());
      Assert.assertTrue(vfs.isDirectory(newDir));
    }
  }

  @Test
  public void testVFSCreateFile() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path filePath = tmp.getRoot().toPath().resolve("bar");
      String newFile = vfs.createFile(filePath.toString());
      Assert.assertTrue(vfs.isFile(newFile));
    }
  }

  @Test
  public void testVFSCreateFileURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path filePath = tmp.getRoot().toPath().resolve("bar");
      URI fileURI = vfs.createFile(filePath.toUri());
      Assert.assertTrue(vfs.isFile(fileURI));
    }
  }

  @Test
  public void testVFSRemoveDirectory() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path dirPath = tmp.getRoot().toPath().resolve("bar");

      String newDir = vfs.createDirectory(dirPath.toString());
      Assert.assertTrue(vfs.isDirectory(newDir));

      vfs.removeDirectory(newDir);
      Assert.assertFalse(vfs.isFile(newDir));
      Assert.assertFalse(vfs.isDirectory(newDir));
    }
  }

  @Test
  public void testVFSRemoveDirectoryURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path dirPath = tmp.getRoot().toPath().resolve("bar");

      URI newDir = vfs.createDirectory(dirPath.toUri());
      Assert.assertTrue(vfs.isDirectory(newDir));

      vfs.removeDirectory(newDir);
      Assert.assertFalse(vfs.isFile(newDir));
      Assert.assertFalse(vfs.isDirectory(newDir));
    }
  }

  @Test
  public void testVFSRemoveFile() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path filePath = tmp.getRoot().toPath().resolve("bar");

      String newFile = vfs.createFile(filePath.toString());
      Assert.assertTrue(vfs.isFile(newFile));

      vfs.removeFile(newFile);
      Assert.assertFalse(vfs.isFile(newFile));
      Assert.assertFalse(vfs.isDirectory(newFile));
    }
  }

  @Test
  public void testVFSRemoveFileURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path filePath = tmp.getRoot().toPath().resolve("bar");

      URI newFile = vfs.createFile(filePath.toUri());
      Assert.assertTrue(vfs.isFile(newFile));

      vfs.removeFile(newFile);
      Assert.assertFalse(vfs.isFile(newFile));
      Assert.assertFalse(vfs.isDirectory(newFile));
    }
  }

  @Test
  public void testVFSFileSize() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      Path filePath = tmp.getRoot().toPath().resolve("bar");

      String newFile = vfs.createFile(filePath.toString());
      Assert.assertEquals(vfs.fileSize(newFile), 0L);
      Files.write(filePath, new byte[] {1, 2, 3});
      Assert.assertEquals(vfs.fileSize(newFile), 3L);
    }
  }

  @Test
  public void testVFSDirectoryMove() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      // create directory
      Path sourcePath = tmp.getRoot().toPath().resolve("bar");
      vfs.createDirectory(sourcePath.toString());
      Assert.assertTrue(vfs.isDirectory(sourcePath.toString()));

      // rename, move directory
      Path destPath = tmp.getRoot().toPath().resolve("baz");
      vfs.moveDirectory(sourcePath.toString(), destPath.toString());
      Assert.assertFalse(vfs.isDirectory(sourcePath.toString()));
      Assert.assertTrue(vfs.isDirectory(destPath.toString()));
    }
  }

  @Test
  public void testVFSDirectoryMoveURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      // create directory
      Path sourcePath = tmp.getRoot().toPath().resolve("bar");
      URI sourceURI = vfs.createDirectory(sourcePath.toUri());
      Assert.assertTrue(vfs.isDirectory(sourceURI));

      // rename, move directory
      Path destPath = tmp.getRoot().toPath().resolve("baz");
      URI destURI = vfs.moveDirectory(sourceURI, destPath.toUri());
      Assert.assertFalse(vfs.isDirectory(sourceURI));
      Assert.assertTrue(vfs.isDirectory(destURI));
    }
  }

  @Test
  public void testVFSFileMove() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      // create file
      Path sourcePath = tmp.getRoot().toPath().resolve("bar");
      vfs.createFile(sourcePath.toString());
      Assert.assertTrue(vfs.isFile(sourcePath.toString()));

      // rename, move file
      Path destPath = tmp.getRoot().toPath().resolve("baz");
      vfs.moveFile(sourcePath.toString(), destPath.toString());
      Assert.assertFalse(vfs.isFile(sourcePath.toString()));
      Assert.assertTrue(vfs.isFile(destPath.toString()));
    }
  }

  @Test
  public void testVFSFileMoveURI() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      // create file
      Path sourcePath = tmp.getRoot().toPath().resolve("bar");
      URI sourceURI = vfs.createFile(sourcePath.toUri());
      Assert.assertTrue(vfs.isFile(sourceURI));

      // rename, move file
      Path destPath = tmp.getRoot().toPath().resolve("baz");
      URI destURI = vfs.moveFile(sourceURI, destPath.toUri());
      Assert.assertFalse(vfs.isFile(sourceURI));
      Assert.assertTrue(vfs.isFile(destURI));
    }
  }

  @Test
  public void testVFSWriteRead() throws Exception {
    try (Context ctx = new Context();
        VFS vfs = new VFS(ctx)) {
      String sourcePath = tmp.getRoot().toPath().resolve("bar").toString();
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
      Path sourcePath = tmp.getRoot().toPath().resolve("bar");
      URI sourceURI = vfs.write(sourcePath.toUri(), new byte[] {});
      Assert.assertEquals(vfs.fileSize(sourceURI), 0L);
      byte[] resultBytes = vfs.readAllBytes(sourceURI);
      Assert.assertTrue(Arrays.equals(new byte[] {}, resultBytes));
    }
  }
}
