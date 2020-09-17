#!/bin/sh

set -ex

cd /TileDB-Java
./gradlew -PTILEDB_AZURE=ON build
