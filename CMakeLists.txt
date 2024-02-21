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

if(NOT CMAKE_BUILD_TYPE)
  set(CMAKE_BUILD_TYPE Release)
endif()

# Default builds the superbuild.
option(TILEDB_JNI_SUPERBUILD "If true, perform a superbuild (builds all missing dependencies)." ON)

# Set C++20 as required standard for all C++ targets.
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF) # Don't use GNU extensions

option(TILEDB_VERBOSE "Prints TileDB errors with verbosity" OFF)
option(TILEDB_S3 "Enables S3 support using aws-cpp-sdk" ON)
option(TILEDB_HDFS "Enables HDFS support using the official Hadoop JNI bindings" OFF)
option(TILEDB_SERIALIZATION "Enables TileDB Cloud support" ON)
option(TILEDB_AZURE "Enables Azure support in TileDB" OFF)
option(TILEDB_GIT_REPOSITORY "Git repository to use for building" "")
option(TILEDB_GIT_TAG "Git tag to use for building" "")
option(FORCE_BUILD_TILEDB "Force building TileDB and don't check for system installation" OFF)
option(DOWNLOAD_TILEDB_PREBUILT "If tiledb is being super built, this controls downloading prebuilt artifacts or building from source" ON)
option(TILEDB_SKIP_S3AWSSDK_DIR_LENGTH_CHECK "If true, skip check needed path length for awssdk (TILEDB_S3) dependent builds" OFF)
option(USE_AVX2 "If true TileDB-Java will use the precompiled Linux binaries with AVX2. if we build from source the architecture will match the current CPU" OFF)

############################################################
# Superbuild setup
############################################################

# Perform superbuild config and exit.
if (TILEDB_JNI_SUPERBUILD)
  project(TileDB-JNI-Superbuild)

  # Path to install everything to, for embedding in the jar.
  if (APPLE) # macOS
    if (CMAKE_OSX_ARCHITECTURES STREQUAL x86_64 OR CMAKE_SYSTEM_PROCESSOR MATCHES "(x86_64)|(AMD64|amd64)|(^i.86$)")
        set(RESOURCE_INSTALL_DIR "${CMAKE_CURRENT_BINARY_DIR}/install")
    else()
            set(RESOURCE_INSTALL_DIR "${CMAKE_CURRENT_BINARY_DIR}/install/arm")
    endif()
  else()
        set(RESOURCE_INSTALL_DIR "${CMAKE_CURRENT_BINARY_DIR}/install")
  endif()

  message(STATUS "Starting TileDB-JNI superbuild.")
  include("cmake/TileDB-JNI-Superbuild.cmake")
  message(STATUS "Install prefix is ${CMAKE_INSTALL_PREFIX}")
  message(STATUS "Resource install path is ${RESOURCE_INSTALL_DIR}")
  # Stop processing this file here.
  return()
endif()

project(TileDB-JNI)
message(STATUS "Starting TileDB-JNI regular build.")

# Paths to locate the installed external projects.
set(TILEDB_JNI_EP_SOURCE_DIR "${TILEDB_JNI_EP_BASE}/src")
set(TILEDB_JNI_EP_INSTALL_PREFIX "${RESOURCE_INSTALL_DIR}")

############################################################
# TileDB-JNI shared library target
############################################################

# Find required library dependencies
find_package(TileDB_EP REQUIRED)
find_package(JNI REQUIRED)

add_library(tiledbjni SHARED
    src/main/c/generated/tiledb_wrap.cxx
    src/main/c/custom/tiledb_custom.cxx
)

target_compile_definitions(tiledbjni PRIVATE -Dtiledb_EXPORTS)
target_include_directories(tiledbjni
  PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/swig"
    "${JAVA_INCLUDE_PATH}"
    "${JAVA_INCLUDE_PATH2}"
)

target_link_libraries(tiledbjni
  PRIVATE
    ${JAVA_JVM_LIBRARY}
)

target_link_libraries(tiledbjni
  PRIVATE
    TileDB::tiledb_shared
)

# Set rpath to be relative to the .so.
if (APPLE)
  set_target_properties(tiledbjni PROPERTIES INSTALL_RPATH "@loader_path/")
else()
  set_target_properties(tiledbjni PROPERTIES INSTALL_RPATH "$ORIGIN/")
endif()


include(GNUInstallDirs)

install(
  TARGETS tiledbjni
  LIBRARY DESTINATION "${RESOURCE_INSTALL_DIR}/${CMAKE_INSTALL_LIBDIR}"
)
