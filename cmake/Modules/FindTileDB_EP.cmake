#
# FindTileDB_EP.cmake
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

# Also search for the config file inside the external projects install dir.
list(APPEND CMAKE_PREFIX_PATH "${TILEDB_JNI_EP_INSTALL_PREFIX}")

if (DEFINED ENV{TILEDB_HOME})
  list(APPEND CMAKE_PREFIX_PATH "$ENV{TILEDB_HOME}")
endif()

find_package(TileDB CONFIG QUIET)

if (NOT TILEDB_FOUND)
  if (TILEDB_JNI_SUPERBUILD)
    message(STATUS "Could NOT find TileDB")
    message(STATUS "Adding TileDB as an external project")
    message(STATUS "TileDB downloading git repo ${TILEDB_GIT_REPOSITORY}")
    message(STATUS "TileDB checkout git tag ${TILEDB_GIT_TAG}")
    ExternalProject_Add(ep_tiledb
      GIT_REPOSITORY "${TILEDB_GIT_REPOSITORY}"
      GIT_TAG ${TILEDB_GIT_TAG}
      CMAKE_ARGS
        -DCMAKE_INSTALL_PREFIX=${TILEDB_JNI_EP_INSTALL_PREFIX}
        -DCMAKE_PREFIX_PATH=${TILEDB_JNI_EP_INSTALL_PREFIX}
        -DTILEDB_VERBOSE=${TILEDB_VERBOSE}
        -DTILEDB_S3=${TILEDB_S3}
        -DTILEDB_HDFS=${TILEDB_HDFS}
        -DTILEDB_FORCE_ALL_DEPS=ON
        -DTILEDB_WERROR=OFF
        -E CXXFLAGS="-Wno-error=deprecated-copy"
        -E CXX_FLAGS="-Wno-error=deprecated-copy"
        -E CFLAGS="-Wno-error=deprecated-copy"
      UPDATE_COMMAND ""
      INSTALL_COMMAND
        ${CMAKE_COMMAND} --build . --target install-tiledb
      LOG_DOWNLOAD TRUE
      LOG_CONFIGURE FALSE
      LOG_BUILD FALSE
      LOG_INSTALL FALSE
    )
    list(APPEND TILEDB_JNI_EXTERNAL_PROJECTS ep_tiledb)
  else()
    message(FATAL_ERROR "Could not find TileDB (required).")
  endif()
else()
  get_target_property(TILEDB_LIBRARIES
    TileDB::tiledb_shared
    IMPORTED_LOCATION_RELEASE
  )
  message(STATUS "Found TileDB: ${TILEDB_LIBRARIES}")
endif()
