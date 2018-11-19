#
# CMakeLists.txt
#
#
# The MIT License
#
# Copyright (c) 2017-2018 TileDB, Inc.
# Copyright (c) 2016 MIT and Intel Corporation
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#

############################################################
# CMake setup
############################################################

cmake_minimum_required(VERSION 3.3)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/cmake/Modules/")

# Default builds the superbuild.
option(TILEDB_JNI_SUPERBUILD "If true, perform a superbuild (builds all missing dependencies)." ON)

# Set C++11 as required standard for all C++ targets.
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF) # Don't use GNU extensions

option(TILEDB_VERBOSE "Prints TileDB errors with verbosity" OFF)
option(TILEDB_S3 "Enables S3/minio support using aws-cpp-sdk" OFF)
option(TILEDB_HDFS "Enables HDFS support using the official Hadoop JNI bindings" OFF)

############################################################
# Superbuild setup
############################################################

# Perform superbuild config and exit.
if (TILEDB_JNI_SUPERBUILD)
  project(TileDB-JNI-Superbuild)
  message(STATUS "Starting TileDB-JNI superbuild.")
  include("cmake/TileDB-JNI-Superbuild.cmake")
  message(STATUS "Install prefix is ${CMAKE_INSTALL_PREFIX}")
  # Stop processing this file here.
  return()
endif()

project(TileDB-JNI)
message(STATUS "Starting TileDB-JNI regular build.")

# Paths to locate the installed external projects.
set(TILEDB_JNI_EP_SOURCE_DIR "${TILEDB_JNI_EP_BASE}/src")
set(TILEDB_JNI_EP_INSTALL_PREFIX "${TILEDB_JNI_EP_BASE}/install")

############################################################
# TileDB-JNI shared library target
############################################################

# Find required library dependencies
find_package(TileDB_EP REQUIRED)
find_package(JNI REQUIRED)

if (WIN32)
  set(LIB_EXTENSION dll)
elseif(APPLE)
  set(LIB_EXTENSION jnilib)
else()
  set(LIB_EXTENSION so)
endif()

add_library(tiledbjni SHARED
    src/main/c/generated/tiledb_wrap.cxx
    src/main/c/custom/tiledb_custom.cxx
)

target_include_directories(tiledbjni
  PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/swig"
    "${JAVA_INCLUDE_PATH}"
    "${JAVA_INCLUDE_PATH2}"
)

target_link_libraries(tiledbjni
  PRIVATE
    TileDB::tiledb_shared
    ${JAVA_JVM_LIBRARY}
)

set_target_properties(tiledbjni PROPERTIES SUFFIX ".${LIB_EXTENSION}")
