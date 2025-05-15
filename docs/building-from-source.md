# Building TileDB-Java from Source

## Dependencies

To build the JNI extension, you need to install the following:

* Cmake (>=3.3)
* JDK (>=1.8)

## Build

To build the library with the native library bundled in run:

```bash
./gradlew assemble
```

This will create the TileDB JNI library `build/tiledb_jni/libtiledbjni.dylib`. This will also download and build the TileDB core library if it is not found installed in a global system path, and place it in `build/externals/install/lib/libtiledb.dylib`.

If you wish to build with a custom version of the TileDB core library, you can define the environment variable `TILEDB_HOME`, e.g.:

```bash
env TILEDB_HOME=/path/to/TileDB/dist ./gradlew assemble
```

Note that if you build with a custom native TileDB library, it will only be bundled into the jar if the native static library was produced.

### Properties

If TileDB is not globally installed in the system where the JNI library is being compiled, the TileDB core Library will be compiled. There are multiple properties which can be configured, including S3 and HDFS support.

See [gradle.properties](https://github.com/TileDB-Inc/TileDB-Java/blob/master/gradle.properties) for all properties which can be set for building.

The properties can be set via the `-P` option to `gradlew`:

```bash
./gradlew -P TILEDB_S3=ON -P TILEDB_VERBOSE=ON assemble
```

### Tests

To run the tests use:

```bash
./gradlew test
```

