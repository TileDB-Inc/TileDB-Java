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

if (FORCE_BUILD_TILEDB)
  find_package(TileDB CONFIG PATHS ${TILEDB_JNI_EP_INSTALL_PREFIX} NO_DEFAULT_PATH)
else()
  find_package(TileDB CONFIG)
endif()

if (NOT TILEDB_FOUND)
  if (TILEDB_JNI_SUPERBUILD)
    message(STATUS "Could NOT find TileDB")
    message(STATUS "Adding TileDB as an external project")
    message(STATUS "TileDB downloading git repo ${TILEDB_GIT_REPOSITORY}")
    message(STATUS "TileDB checkout git tag ${TILEDB_GIT_TAG}")

    # Try to download prebuilt artifacts unless the user specifies to build from source
    if(DOWNLOAD_TILEDB_PREBUILT)
        if (WIN32) # Windows
          SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.25.0/tiledb-windows-x86_64-2.25.0-bbcbd3f.zip")
          SET(DOWNLOAD_SHA1 "b2b8cdfefbcbb07296c86e52cb9a2979ef455d2a")
        elseif(APPLE) # macOS

          if (CMAKE_OSX_ARCHITECTURES STREQUAL x86_64 OR CMAKE_SYSTEM_PROCESSOR MATCHES "(x86_64)|(AMD64|amd64)|(^i.86$)")
            message(STATUS "Building for intel mac")

            SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.25.0/tiledb-macos-x86_64-2.25.0-bbcbd3f.tar.gz")
            SET(DOWNLOAD_SHA1 "9c799d7de64e78d9e95cfa7db95b569cd3d3c8b0")

          elseif (CMAKE_OSX_ARCHITECTURES STREQUAL arm64 OR CMAKE_SYSTEM_PROCESSOR MATCHES "^aarch64" OR CMAKE_SYSTEM_PROCESSOR MATCHES "^arm")
            message(STATUS "Building for apple silicon mac")
            SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.25.0/tiledb-macos-arm64-2.25.0-bbcbd3f.tar.gz")
            SET(DOWNLOAD_SHA1 "c6a1ab5213c36e53dd8e6da87c18b8bc695c532d")
          endif()
        else() # Linux
          if (USE_AVX2)
            message(STATUS "Using Linux binaries with AVX2")
            SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.25.0/tiledb-linux-x86_64-2.25.0-bbcbd3f.tar.gz")
            SET(DOWNLOAD_SHA1 "1d789af5d88ce09edf60d16ea3988631e780252a")
          else()
            message(STATUS "Using Linux binaries without AVX2")
            SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.25.0/tiledb-linux-x86_64-noavx2-2.25.0-bbcbd3f.tar.gz")
            SET(DOWNLOAD_SHA1 "02f3b9951e5d21614ea0d577190161aed2b04240")
          endif()
        endif()

        ExternalProject_Add(ep_tiledb
                PREFIX "externals"
                URL ${DOWNLOAD_URL}
                URL_HASH SHA1=${DOWNLOAD_SHA1}
                CONFIGURE_COMMAND ""
                BUILD_COMMAND ""
                UPDATE_COMMAND ""
                PATCH_COMMAND ""
                TEST_COMMAND ""
                INSTALL_COMMAND
                    ${CMAKE_COMMAND} -E copy_directory ${TILEDB_JNI_EP_BASE}/src/ep_tiledb ${TILEDB_JNI_EP_INSTALL_PREFIX}
                LOG_DOWNLOAD TRUE
                LOG_CONFIGURE FALSE
                LOG_BUILD FALSE
                LOG_INSTALL FALSE
                )
    else() # build from source
        ExternalProject_Add(ep_tiledb
          GIT_REPOSITORY "${TILEDB_GIT_REPOSITORY}"
          GIT_TAG ${TILEDB_GIT_TAG}
          CMAKE_ARGS
            -DCMAKE_INSTALL_PREFIX=${TILEDB_JNI_EP_INSTALL_PREFIX}
            -DCMAKE_PREFIX_PATH=${TILEDB_JNI_EP_INSTALL_PREFIX}
            -DTILEDB_VERBOSE=${TILEDB_VERBOSE}
            -DTILEDB_S3=${TILEDB_S3}
            -DTILEDB_SKIP_S3AWSSDK_DIR_LENGTH_CHECK=${TILEDB_SKIP_S3AWSSDK_DIR_LENGTH_CHECK}
            -DTILEDB_AZURE=${TILEDB_AZURE}
            -DTILEDB_HDFS=${TILEDB_HDFS}
            -DTILEDB_SERIALIZATION=${TILEDB_SERIALIZATION}
            -DTILEDB_FORCE_ALL_DEPS=ON
          UPDATE_COMMAND ""
          INSTALL_COMMAND
            ${CMAKE_COMMAND} --build . --target install-tiledb
          LOG_DOWNLOAD TRUE
          LOG_CONFIGURE FALSE
          LOG_BUILD FALSE
          LOG_INSTALL FALSE
        )
    endif()

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