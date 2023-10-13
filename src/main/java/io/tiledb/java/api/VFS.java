package io.tiledb.java.api;

import io.tiledb.libtiledb.*;
import java.math.BigInteger;
import java.net.URI;

public class VFS implements AutoCloseable {

  private Context ctx;
  private SWIGTYPE_p_tiledb_vfs_handle_t vfsp;
  private SWIGTYPE_p_p_tiledb_vfs_handle_t vfspp;

  /**
   * Constructor for creating new TileDB VFS handle with a given configuration
   *
   * @param ctx The TileDB context
   * @param config A TileDB config object
   * @throws TileDBError A TileDB exception
   */
  public VFS(Context ctx, Config config) throws TileDBError {
    SWIGTYPE_p_p_tiledb_vfs_handle_t vfspp = tiledb.new_tiledb_vfs_tpp();
    try {
      ctx.handleError(tiledb.tiledb_vfs_alloc(ctx.getCtxp(), config.getConfigp(), vfspp));
    } catch (TileDBError err) {
      tiledb.delete_tiledb_vfs_tpp(vfspp);
      throw err;
    }
    this.ctx = ctx;
    this.vfsp = tiledb.tiledb_vfs_tpp_value(vfspp);
    this.vfspp = vfspp;
  }

  /**
   * Constructor for creating a new TileDB VFS handle
   *
   * @param ctx A TileDB context
   * @throws TileDBError A TileDB exception
   */
  public VFS(Context ctx) throws TileDBError {
    this(ctx, new Config());
  }

  /**
   * Returns a handle to the underlying VFS SWIG pointer
   *
   * @return A SWIG pointer wrapper to a tiledb_vfs_t object handle
   */
  protected SWIGTYPE_p_tiledb_vfs_handle_t getVFSp() {
    return this.vfsp;
  }

  /**
   * Returns the Config object associated with the VFS instance
   *
   * @return THe Config object
   * @throws TileDBError A TileDB exception
   */
  public Config getConfig() throws TileDBError {
    Config config;
    SWIGTYPE_p_p_tiledb_config_t configpp = tiledb.new_tiledb_config_tpp();
    try {
      ctx.handleError(tiledb.tiledb_vfs_get_config(ctx.getCtxp(), vfsp, configpp));
      config = new Config(configpp);
    } catch (TileDBError err) {
      tiledb.delete_tiledb_config_tpp(configpp);
      throw err;
    }
    return config;
  }

  /**
   * Returns the Context associated with the VFS instance
   *
   * @return A TileDB Context object
   */
  public Context getContext() {
    return this.ctx;
  }

  /**
   * Checks if the filesystem backend is supported for a given VFS instance
   *
   * @param fs TileDB Filesystem enum
   * @return true if the filesystem is supported, false otherwise
   * @throws TileDBError A TileDB excpetion
   */
  public boolean isSupportedFs(Filesystem fs) throws TileDBError {
    return ctx.isSupportedFs(fs);
  }

  /**
   * Checks if the filesystem backend is supported for a given VFS URI scheme
   *
   * @param fs TileDB filesystem string (ex. file, file://, s3, s3://, hdfs, hdfs://)
   * @return true if the filesystem backend is supported, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isSupportedFs(String fs) throws TileDBError {
    if (fs.equalsIgnoreCase("file") || fs.equalsIgnoreCase("file://")) {
      return true;
    } else if (fs.equalsIgnoreCase("hdfs") || fs.equalsIgnoreCase("hdfs://")) {
      return ctx.isSupportedFs(Filesystem.TILEDB_HDFS);
    } else if (fs.equalsIgnoreCase("s3") || fs.equalsIgnoreCase("s3://")) {
      return ctx.isSupportedFs(Filesystem.TILEDB_S3);
    }
    return false;
  }

  /**
   * Checks if the filesystem backend if supported for a given VFS URI (scheme)
   *
   * @param uri A URI with a given scheme
   * @return true if the uri scheme backend is supported, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isSupportedFs(URI uri) throws TileDBError {
    return isSupportedFs(uri.getScheme());
  }

  /**
   * Checks if an object-store bucket exists.
   *
   * @param path String URI path to bucket resource
   * @return true if the bucket exists, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isBucket(String path) throws TileDBError {
    boolean isBucket;
    SWIGTYPE_p_int retp = tiledb.new_intp();
    try {
      ctx.handleError(tiledb.tiledb_vfs_is_bucket(ctx.getCtxp(), vfsp, path, retp));
      isBucket = tiledb.intp_value(retp) != 0;
    } finally {
      tiledb.delete_intp(retp);
    }
    return isBucket;
  }

  /**
   * Checks if an object-store bucket exists.
   *
   * @param uri URI to bucket resource
   * @return true if the bucket exists, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isBucket(URI uri) throws TileDBError {
    return isBucket(uri.toString());
  }

  /**
   * Creates an object store bucket.
   *
   * @param path String URI path of bucket to be created
   * @return String URI of created bucket
   * @throws TileDBError A TileDB exception
   */
  public String createBucket(String path) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_create_bucket(ctx.getCtxp(), vfsp, path));
    return path;
  }

  /**
   * Creates an object store bucket.
   *
   * @param uri URI of bucket to be created
   * @return The URI of the created bucket
   * @throws TileDBError A TileDB exception
   */
  public URI createBucket(URI uri) throws TileDBError {
    createBucket(uri.toString());
    return uri;
  }

  /**
   * Deletes an object-store bucket.
   *
   * @param path String URI path of bucket to be delted
   * @return String URI of deleted bucket
   * @throws TileDBError A TileDB excpetion
   */
  public String removeBucket(String path) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_remove_bucket(ctx.getCtxp(), vfsp, path));
    return path;
  }

  /**
   * Deletes an object-store bucket.
   *
   * @param uri URI of bucket to be deleted
   * @return URI of deleted bucket
   * @throws TileDBError A TileDB exception
   */
  public URI removeBucket(URI uri) throws TileDBError {
    removeBucket(uri.toString());
    return uri;
  }

  /**
   * Checks if an object-store bucket is empty
   *
   * @param path A String uri of the bucket
   * @return true if the bucket is empty, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isEmptyBucket(String path) throws TileDBError {
    boolean isEmpty;
    SWIGTYPE_p_int retp = tiledb.new_intp();
    try {
      ctx.handleError(tiledb.tiledb_vfs_is_empty_bucket(ctx.getCtxp(), vfsp, path, retp));
      isEmpty = tiledb.intp_value(retp) != 0;
    } finally {
      tiledb.delete_intp(retp);
    }
    return isEmpty;
  }

  /**
   * Checks if an object-store bucket is empty
   *
   * @param uri URI of the bucket
   * @return
   * @throws TileDBError
   */
  public boolean isEmtpyBucket(URI uri) throws TileDBError {
    return isEmptyBucket(uri.toString());
  }

  /**
   * @param path
   * @return
   * @throws TileDBError A TileDB exception
   */
  public String emptyBucket(String path) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_empty_bucket(ctx.getCtxp(), vfsp, path));
    return path;
  }

  /**
   * @param uri
   * @return
   * @throws TileDBError A TileDB exception
   */
  public URI emptyBucket(URI uri) throws TileDBError {
    emptyBucket(uri.toString());
    return uri;
  }

  /**
   * Checks if the given URI resource path string is a file
   *
   * @param path String URI path to file resource
   * @return true if the given resource path is a file, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isFile(String path) throws TileDBError {
    boolean isFile;
    SWIGTYPE_p_int ret = tiledb.new_intp();
    try {
      ctx.handleError(tiledb.tiledb_vfs_is_file(ctx.getCtxp(), vfsp, path, ret));
      isFile = tiledb.intp_value(ret) != 0;
    } finally {
      tiledb.delete_intp(ret);
    }
    return isFile;
  }

  /**
   * Checks if the given URI is a file resource
   *
   * @param uri URI path to resource
   * @return true if the given resource path is a file, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isFile(URI uri) throws TileDBError {
    return isFile(uri.toString());
  }

  /**
   * Checks if the given URI resource path is a directory
   *
   * @param path String URI path to directory resource
   * @return true if the given resource path is a directory, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isDirectory(String path) throws TileDBError {
    boolean isDir;
    SWIGTYPE_p_int ret = tiledb.new_intp();
    try {
      ctx.handleError(tiledb.tiledb_vfs_is_dir(ctx.getCtxp(), vfsp, path, ret));
      isDir = tiledb.intp_value(ret) != 0;
    } finally {
      tiledb.delete_intp(ret);
    }
    return isDir;
  }

  /**
   * Checks if the given URI is a directory resource
   *
   * @param uri URI path to resource
   * @return true if the given uri is a directory resource, false otherwise
   * @throws TileDBError A TileDB exception
   */
  public boolean isDirectory(URI uri) throws TileDBError {
    return isDirectory(uri.toString());
  }

  /**
   * Creates a directory resource at the given path
   *
   * @param path String URI path of directory to create
   * @return String URI path of created directory
   * @throws TileDBError A TileDB exception
   */
  public String createDirectory(String path) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_create_dir(ctx.getCtxp(), vfsp, path));
    return path;
  }

  /**
   * Creates a directory resource at the given URI
   *
   * @param uri URI of directory to create
   * @return URI of the created directory
   * @throws TileDBError A TileDB exception
   */
  public URI createDirectory(URI uri) throws TileDBError {
    createDirectory(uri.toString());
    return uri;
  }

  /**
   * Creates an empty file resource at the given path
   *
   * @param path String URI path of the empty file to create
   * @return String URI path of created file
   * @throws TileDBError A TileDB exception
   */
  public String createFile(String path) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_touch(ctx.getCtxp(), vfsp, path));
    return path;
  }

  /**
   * Creates an empty file resource
   *
   * @param uri URI of file to create
   * @return URI of created file
   * @throws TileDBError A TileDB exception
   */
  public URI createFile(URI uri) throws TileDBError {
    createFile(uri.toString());
    return uri;
  }

  /**
   * Removes a VFS directory resource (if exists)
   *
   * @param path String URI path of the directory to remove
   * @return String uri path of removed directory
   * @throws TileDBError A TileDB Exception
   */
  public String removeDirectory(String path) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_remove_dir(ctx.getCtxp(), vfsp, path));
    return path;
  }

  /**
   * Removes a VFS directory resource (if exists)
   *
   * @param uri URI of directory to remove
   * @return URI of removed directory
   * @throws TileDBError A TileDB exception
   */
  public URI removeDirectory(URI uri) throws TileDBError {
    removeDirectory(uri.toString());
    return uri;
  }

  /**
   * Removes a VFS file resource (if exists)
   *
   * @param path String URI path of the file to remove
   * @return String uri path of removed file
   * @throws TileDBError A TileDB exception
   */
  public String removeFile(String path) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_remove_file(ctx.getCtxp(), vfsp, path));
    return path;
  }

  /**
   * Removes a VFS file resource (if exists)
   *
   * @param uri URI of the file to remove
   * @return URI of the removed file
   * @throws TileDBError
   */
  public URI removeFile(URI uri) throws TileDBError {
    removeFile(uri.toString());
    return uri;
  }

  /**
   * Returns the size of the VFS file resource in bytes
   *
   * @param path String URI path of the file
   * @return File size in bytes
   * @throws TileDBError A TileDB exception
   */
  public long fileSize(String path) throws TileDBError {
    BigInteger byteSize;
    SWIGTYPE_p_unsigned_long_long sizep = tiledb.new_ullp();
    try {
      ctx.handleError(tiledb.tiledb_vfs_file_size(ctx.getCtxp(), vfsp, path, sizep));
      byteSize = tiledb.ullp_value(sizep);
    } finally {
      tiledb.delete_ullp(sizep);
    }
    return byteSize.longValue();
  }

  /**
   * Returns the size fo the VFS file resource in bytes
   *
   * @param uri URI of file resource
   * @return File size in bytes
   * @throws TileDBError A TileDB exception
   */
  public long fileSize(URI uri) throws TileDBError {
    return fileSize(uri.toString());
  }

  /**
   * Renames a VFS directory resource
   *
   * @param source Old URI string
   * @param dest New URI string
   * @return String URI path of moved file resou
   * @throws TileDBError
   */
  public String moveDirectory(String source, String dest) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_move_dir(ctx.getCtxp(), vfsp, source, dest));
    return dest;
  }

  /**
   * Renames a VFS directory resource
   *
   * @param source Old URI
   * @param dest New URI
   * @return URI of moved directory resource
   * @throws TileDBError
   */
  public URI moveDirectory(URI source, URI dest) throws TileDBError {
    moveDirectory(source.toString(), dest.toString());
    return dest;
  }

  /**
   * Renames a VFS file resource
   *
   * @param source Old URI string
   * @param dest New URI string
   * @return String URI path of moved file resource
   * @throws TileDBError A TileDB exception
   */
  public String moveFile(String source, String dest) throws TileDBError {
    ctx.handleError(tiledb.tiledb_vfs_move_file(ctx.getCtxp(), vfsp, source, dest));
    return dest;
  }

  /**
   * Renames a VFS file resource
   *
   * @param source Old URI
   * @param dest New URI
   * @return URI of moved file resource
   * @throws TileDBError A TileDB exception
   */
  public URI moveFile(URI source, URI dest) throws TileDBError {
    moveFile(source.toString(), dest.toString());
    return dest;
  }

  /**
   * Reads all the bytes from a VFS file resource.
   *
   * <p>Ensures that the file has been closed when all bytes have been read or an IO error is
   * thrown. This is a convenience method and is not intended for reading large VFS files.
   *
   * @param path the string URI path to the file resource
   * @return a byte array containing
   * @throws TileDBError
   */
  public byte[] readAllBytes(String path) throws TileDBError {
    Long nbytes = fileSize(path);
    SWIGTYPE_p_p_tiledb_vfs_fh_handle_t vfsFHpp = tiledb.new_tiledb_vfs_fh_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_vfs_open(
              ctx.getCtxp(), vfsp, path, VFSMode.TILEDB_VFS_READ.toSwigEnum(), vfsFHpp));
    } catch (TileDBError err) {
      tiledb.tiledb_vfs_fh_free(vfsFHpp);
      throw err;
    }
    byte[] resultBuffer;
    SWIGTYPE_p_tiledb_vfs_fh_handle_t vfsFHp = tiledb.tiledb_vfs_fh_tpp_value(vfsFHpp);
    try (NativeArray byteBuffer = new NativeArray(ctx, nbytes.intValue(), Datatype.TILEDB_INT8)) {
      ctx.handleError(
          tiledb.tiledb_vfs_read(
              ctx.getCtxp(),
              vfsFHp,
              BigInteger.valueOf(0L),
              byteBuffer.toVoidPointer(),
              BigInteger.valueOf(nbytes)));
      resultBuffer = (byte[]) byteBuffer.toJavaArray();
    } finally {
      tiledb.tiledb_vfs_close(ctx.getCtxp(), vfsFHp);
      tiledb.tiledb_vfs_fh_free(vfsFHpp);
    }
    return resultBuffer;
  }

  public byte[] readAllBytes(URI path) throws TileDBError {
    return readAllBytes(path.toString());
  }

  /**
   * Write bytes to a given VFS file resource.
   *
   * @param path The URI string resource path
   * @param bytes The byte array with the bytes to write
   * @param mode VFSMode specifying how the file is opened, defaults to TILEDB_VFS_WRITE if
   *     unspecified
   * @return The URI string resource path
   * @throws TileDBError A TileDB exception
   */
  public String write(String path, byte[] bytes, VFSMode... mode) throws TileDBError {
    if (mode.length == 0) {
      mode =
          new VFSMode[] {
            VFSMode.TILEDB_VFS_WRITE,
          };
    }
    if (mode[0] == VFSMode.TILEDB_VFS_READ) {
      throw new TileDBError("VFSMode for write must be TILEDB_VFS_WRITE or TILEDB_VFS_APPEND");
    }
    SWIGTYPE_p_p_tiledb_vfs_fh_handle_t vfsFHpp = tiledb.new_tiledb_vfs_fh_tpp();
    try {
      ctx.handleError(
          tiledb.tiledb_vfs_open(ctx.getCtxp(), vfsp, path, mode[0].toSwigEnum(), vfsFHpp));
    } catch (TileDBError err) {
      tiledb.tiledb_vfs_fh_free(vfsFHpp);
      throw err;
    }
    SWIGTYPE_p_tiledb_vfs_fh_handle_t vfsFHp = tiledb.tiledb_vfs_fh_tpp_value(vfsFHpp);
    try (NativeArray byteBuffer = new NativeArray(ctx, bytes, Byte.class)) {
      ctx.handleError(
          tiledb.tiledb_vfs_write(
              ctx.getCtxp(), vfsFHp, byteBuffer.toVoidPointer(), BigInteger.valueOf(bytes.length)));
    } finally {
      tiledb.tiledb_vfs_close(ctx.getCtxp(), vfsFHp);
      tiledb.tiledb_vfs_fh_free(vfsFHpp);
    }
    return path;
  }

  /**
   * Write bytes to a given VFS file resource.
   *
   * @param uri The URI string resource path
   * @param bytes The byte array with the bytes to write
   * @param mode VFSMode specifying how the file is opened, defaults to TILEDB_VFS_WRITE if
   *     unspecified
   * @return The URI string resource path
   * @throws TileDBError A TileDB exception
   */
  public URI write(URI uri, byte[] bytes, VFSMode... mode) throws TileDBError {
    write(uri.toString(), bytes, mode);
    return uri;
  }

  /** Free's native TileDB resources associated with the VFS object */
  @Override
  public void close() {
    if (vfsp != null) {
      tiledb.tiledb_vfs_free(vfspp);
      tiledb.delete_tiledb_vfs_tpp(vfspp);
      vfsp = null;
      vfspp = null;
    }
  }
}
