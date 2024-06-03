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
          SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.24.0-rc0/tiledb-windows-x86_64-2.24.0-rc0-52713e1.zip")
          SET(DOWNLOAD_SHA1 "a758916e8b8f965ae98a37b2af990b9d22b31536")
        elseif(APPLE) # macOS

          if (CMAKE_OSX_ARCHITECTURES STREQUAL x86_64 OR CMAKE_SYSTEM_PROCESSOR MATCHES "(x86_64)|(AMD64|amd64)|(^i.86$)")
            message(STATUS "Building for intel mac")

            SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.24.0-rc0/tiledb-macos-x86_64-2.24.0-rc0-52713e1.tar.gz")
            SET(DOWNLOAD_SHA1 "aea4539696009fb2668c2333f38e99476b3ae2a1")

          elseif (CMAKE_OSX_ARCHITECTURES STREQUAL arm64 OR CMAKE_SYSTEM_PROCESSOR MATCHES "^aarch64" OR CMAKE_SYSTEM_PROCESSOR MATCHES "^arm")
            message(STATUS "Building for apple silicon mac")
            SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.24.0-rc0/tiledb-macos-arm64-2.24.0-rc0-52713e1.tar.gz")
            SET(DOWNLOAD_SHA1 "6fa7cd5ea7c4ad82756ae5fd3b7e926a5989e3bd")
          endif()
        else() # Linux
          if (USE_AVX2)
            message(STATUS "Using Linux binaries with AVX2")
            SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.24.0-rc0/tiledb-linux-x86_64-2.24.0-rc0-52713e1.tar.gz")
            SET(DOWNLOAD_SHA1 "2fae95e14a216e2380178a4c1c3ec03653027b6e")
          else()
            message(STATUS "Using Linux binaries without AVX2")
            SET(DOWNLOAD_URL "https://github.com/TileDB-Inc/TileDB/releases/download/2.24.0-rc0/tiledb-linux-x86_64-noavx2-2.24.0-rc0-52713e1.tar.gz")
            SET(DOWNLOAD_SHA1 "e9f0bd922bfd2c966cae0e25112c9436e4dc969f")
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