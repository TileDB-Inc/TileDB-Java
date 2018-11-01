# TileDB-Java
[![Build Status](https://travis-ci.org/TileDB-Inc/TileDB-Java.svg?branch=master)](https://travis-ci.org/TileDB-Inc/TileDB-Java)

## Dependencies

To build the JNI extension you need to install:

* Cmake (>=3.3)
* JDK (>=1.8)

## Build

To build the JNI library run:

`./gradlew assemble`

This will create the TileDB JNI library `build/tiledb_jni/libtiledbjni.jnilib`. This will also download and build the [TileDB](https://github.com/TileDB-Inc/TileDB) library first, if it is not found installed in a global system path, in which case the native library gets placed in `build/externals/install/lib/libtiledb.dylib`.

If you wish to build with a custom version of the native TileDB library, you can define the environment variable `TILEDB_HOME`, e.g.:

`env TILEDB_HOME=/path/to/TileDB/dist ./gradlew assemble`

Before running the Java code you should copy the `libtiledbjni.dylib` file into your system library path, or add the build folder in your `LD_LIBRARY_PATH` ENV variable.

## Tests

To run the tests use:

`./gradlew test`

## Examples

You can run the examples located in `src/main/java/examples` using you IDE or from a terminal.

To run an example from the terminal use:

`java -cp build/libs/tiledb-java-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbArraySchema`

You may need to explitly define the java library path:

`java -Djava.library.path=".:<path/to/TileDB-Java/build/tiledb_jni>" -cp build/libs/tiledb-java-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbArraySchema`

## Format 
 
To auto-format the Java source code run:

`./gradlew format`

to check Java source code formatting:

`./gradlew checkFormat`


## Generate JNI bindings for TileDB C-API

### Dependencies

* Swig (>=3.0)

For OSX swig is available using `brew install swig`

Installation instructions for other operating systems can be found here:
http://www.swig.org/Doc3.0/Preface.html#Preface_installation

### Genrate bindings

1) Set the ENV variable `TILEDB_HOME` to the install location of TileDB.

2) Generate the JNI code using

`env TILEDB_HOME=/path/to/TileDB/dist ./gradlew generateJNI`

## Cross Compile JNI

The JNI library (and TileDB by extent) can be cross compiled using docker and
the dockcross project.

This works well except java is not included in the dockcross. To work around
this we require that the hosts java be mounted to the docker image.

Example usage:

This will produce a build-linux-arv7 directory with the cross compiled libtiledb and libtiledbjni

```
export IMAGE="linux-armv7" && ./docker/dockcross-${IMAGE} --args "-v $JAVA_HOME:/java --rm" bash -c 'mkdir build-${IMAGE} && cd build-${IMAGE} && cmake .. -DTILEDB_USE_NATIVE_JNI=OFF && make -j4'
```
