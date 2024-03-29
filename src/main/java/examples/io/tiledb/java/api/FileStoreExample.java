package examples.io.tiledb.java.api;

import io.tiledb.java.api.Array;
import io.tiledb.java.api.ArraySchema;
import io.tiledb.java.api.Config;
import io.tiledb.java.api.Context;
import io.tiledb.java.api.FileStore;
import io.tiledb.java.api.MimeType;
import io.tiledb.java.api.TileDBError;

public class FileStoreExample {
  public static void main(String[] args) throws TileDBError {
    // Set up the config with your TileDB-Cloud credentials
    try {
      Config config = new Config();
      // For s3 access
      config.set("vfs.s3.aws_access_key_id", "<ID>");
      config.set("vfs.s3.aws_secret_access_key", "<KEY>");

      // For TileDB-Cloud access.
      // You can either use rest.username and rest.password
      config.set("rest.username", "<USERNAME>");
      config.set("rest.password", "<PASSWORD>");

      // Or rest.token
      config.set("rest.token", "<TOKEN>");

      Context ctx = new Context(config);
      String filestore = "my_filestore";

      // Create the array schema of an array based on the file to be saved
      ArraySchema arraySchema = FileStore.schemaCreate(ctx, "test.txt");

      // Create a TileDB array with the schema
      Array.create(filestore, arraySchema);
      // OR
      // Array.create("tiledb://<NAMESPACE_NAME>/s3://<BUCKET_NAME>/<ARRAY_NAME>", arraySchema);

      // Import the file to be saved to the TileDB array
      FileStore.uriImport(ctx, filestore, "test.txt", MimeType.TILEDB_MIME_AUTODETECT);
      // OR
      // FileStore.uriImport(
      //    ctx,
      //    "tiledb://<NAMESPACE_NAME>/<ARRAY_NAME>",
      //    "<FILENAME>",
      //    MimeType.TILEDB_MIME_AUTODETECT);

      // Export/download the file from TileDB and save it with a given name.
      FileStore.uriExport(ctx, filestore, "test_output.txt");
      // OR
      // FileStore.uriExport(ctx, "tiledb://<NAMESPACE_NAME>/<ARRAY_NAME>", "<OUTPUT_FILENAME>");

    } catch (TileDBError e) {
      System.out.println(
          "\nTo run the FileStore example make sure you complete the missing info in the '<>'\n");
    }
  }
}
