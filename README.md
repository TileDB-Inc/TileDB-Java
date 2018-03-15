# TileDB-Java
[![Build Status](https://travis-ci.org/TileDB-Inc/TileDB-Java.svg?branch=master)](https://travis-ci.org/TileDB-Inc/TileDB-Java)

## Dependencies

It is required to first install or build from source TileDB.

* [Install Instructions](https://docs.tiledb.io/en/latest/installation.html)
* [TileDB Repo](https://github.com/TileDB-Inc/TileDB)

To build the JNI extension you need to install:

* Cmake (>=3.2)

## Build

If TileDB is not installed in global system path, the ENV variable `TILEDB_HOME` must be defined.

To build the JNI library run:

`./gradlew assemble`

This will create the TileDB JNI library in `build/libtiledb.jnilib`.

Before running the Java code you should copy this file in your system library path. 
Alternatively you can add the build folder in your `LD_LIBRARY_PATH` ENV variable.

## Tests

To run the tests use:

`./gradlew test`

## Examples

You can run the examples located in `src/main/java/examples` using you IDE or from a terminal.

To run an example from the terminal use:

`java -cp build/libs/tiledb-jni-1.0-SNAPSHOT.jar examples.TiledbArraySchema`

You may need to explitly define the java library path:

`java -Djava.library.path=".:<path/to/TileDB-Java/build>" -cp build/libs/tiledb-jni-1.0-SNAPSHOT.jar examples.TiledbArraySchema`

