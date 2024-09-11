# JNI Generated Code

This directory contains the C++ code that is used by the JNI native methods, under the `io.tiledb.java.libtiledb.tiledb` class.
The code is separated into two directoriesn, namely custom and generated.

## ./generated
The `tiledb_wrap.cxx` file under this directory contains the auto-generated JNI C++ code, using SWIG.

## ./custom
The `tiledb_custom.cxx` file under this directory contains custom methods defined by the developer that are  not
auto-generated. Any custom method that needs to be added should be put in this file. Also, the signature of the
new custom method should be also put in the `tiledb.i` file under the `TileDB-Java/swig` dir. For implementation details,
all `*_nio` methods (e.g. `tiledb_query_set_offsets_buffer_nio`) are good examples.
