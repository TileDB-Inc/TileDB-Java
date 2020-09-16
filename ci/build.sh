#!/bin/sh

set -ex

export TILEDB_PY_REPO="/opt/TileDB-Py"

cd /TileDB-Java
./gradlew build
