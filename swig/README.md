# SWIG

This directory contains the basic SWIG files. 

### customCode
Contains custom method implementations.

- NativeLibLoader.java: Helper methods for loading native libraries (e.g. `.so` files)
- PointerUtils.java: Pointer conversion methods
- Utils.java: Custom JNI methods

The rest of the files are auto-generated.

### tiledb.i
The SWIG interface (`.i`) file. More info can be found at the official SWIG documentation: http://www.swig.org/tutorial.html

### tiledb_java_extensions.h
Contains some extensions of the `tiledb.h` (https://github.com/TileDB-Inc/TileDB/blob/dev/tiledb/sm/c_api/tiledb.h) library that we use in Java. Most of these extensions are implementations of
`dump()` methods that print the dumb to the `stdout` (e.g. `tiledb_fragment_info_dump_stdout`).