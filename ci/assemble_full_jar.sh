#!/usr/bin/env bash

mv Upload-*/* .

mkdir -p ./build/install/lib
mkdir -p ./build/install/arm/lib
mkdir ./build/install/lib64
mkdir ./build/tiledb_jni/
mkdir ./build/tiledb_jni/arm
mkdir ./build/tiledb_jni/Release
mkdir ./build/install/bin


for arch in $(ls | grep .gz.tar)
do
tar -xvf $arch
done

mv binaries_*/* .

# OSX
mv libtiledb.dylib ./build/install/lib
mv libtiledbjni.dylib ./build/tiledb_jni
mv arm/libtiledb.dylib ./build/install/arm/lib
mv arm/libtiledbjni.dylib ./build/tiledb_jni/arm

# Linux
cp libtiledb.so ./build/install/lib
mv libtiledb.so ./build/install/lib64
mv libtiledbjni.so ./build/tiledb_jni

# Windows
mv tbb.dll ./build/install/bin
mv tiledb.dll ./build/install/bin
mv tiledbjni.dll ./build/tiledb_jni/Release

./gradlew assemble

mkdir jars
cp ./build/libs/*.jar jars