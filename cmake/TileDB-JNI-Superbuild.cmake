#
# TileDB-JNI-Superbuild.cmake
#
#
# The MIT License
#
# Copyright (c) 2018 TileDB, Inc.
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

include (ExternalProject)

############################################################
# Common variables
############################################################

# The base directory for building external projects.
set(TILEDB_JNI_EP_BASE "${CMAKE_CURRENT_BINARY_DIR}/externals")
set(TILEDB_JNI_EP_SOURCE_DIR "${TILEDB_JNI_EP_BASE}/src")
set(TILEDB_JNI_EP_INSTALL_PREFIX "${RESOURCE_INSTALL_DIR}")

# Variable that will hold a list of all the external projects added
# as a part of the superbuild.
set(TILEDB_JNI_EXTERNAL_PROJECTS)

# Forward any additional CMake args to the non-superbuild.
set(INHERITED_CMAKE_ARGS
  -DCMAKE_INSTALL_PREFIX=${CMAKE_INSTALL_PREFIX}
  -DTILEDB_JNI_EP_BASE=${TILEDB_JNI_EP_BASE}
  -DRESOURCE_INSTALL_DIR=${RESOURCE_INSTALL_DIR}
)

############################################################
# Set up external projects for dependencies
############################################################

# These includes modify the TILEDB_JNI_EXTERNAL_PROJECTS and FORWARD_EP_CMAKE_ARGS
# variables.

include(${CMAKE_CURRENT_SOURCE_DIR}/cmake/Modules/FindTileDB_EP.cmake)

############################################################
# Set up the regular build (i.e. non-superbuild).
############################################################

ExternalProject_Add(tiledb_jni
  SOURCE_DIR ${PROJECT_SOURCE_DIR}
  CMAKE_ARGS
    -DTILEDB_JNI_SUPERBUILD=OFF
    ${INHERITED_CMAKE_ARGS}
  INSTALL_COMMAND ""
  BINARY_DIR ${CMAKE_CURRENT_BINARY_DIR}/tiledb_jni
  DEPENDS ${TILEDB_JNI_EXTERNAL_PROJECTS}
)
