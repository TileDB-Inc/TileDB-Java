<a href="https://tiledb.com"><img src="https://github.com/TileDB-Inc/TileDB/raw/dev/doc/source/_static/tiledb-logo_color_no_margin_@4x.png" alt="TileDB logo" width="400"></a>

# TileDB-Java
[![Build Status](https://travis-ci.org/TileDB-Inc/TileDB-Java.svg?branch=master)](https://travis-ci.org/TileDB-Inc/TileDB-Java)

## Installation

See [installation docs](https://docs.tiledb.com/developer/installation)

## Quickstart

See [quickstart docs](https://docs.tiledb.com/developer/quickstart)

## Examples

You can run the examples located in `src/main/java/examples` using you IDE or from a terminal.

To run an example from the terminal use:

`java -cp build/libs/tiledb-java-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbArraySchema`

You may need to explicitly define the java library path if not using the bundled jar:

`java -Djava.library.path=".:<path/to/TileDB-Java/build/tiledb_jni>" -cp build/libs/tiledb-java-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbArraySchema`

## Development Notes

For misc development details see [Development Notes](https://github.com/TileDB-Inc/TileDB-Java/wiki/Developer-Notes) wiki entry.
