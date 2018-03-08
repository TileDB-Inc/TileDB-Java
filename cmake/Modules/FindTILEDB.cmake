#
# FindTILEDB.cmake
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
# Finds the TILEDB native library. This module defines:
#   - LIBTILEDB_INCLUDE_DIR, directory containing the tiledb.h header
#   - LIBTILEDB_LIBRARY, the libtiledb library path
#   - LIBTILEDB_FOUND, the tiledb.h header


if(NOT LIBTILEDB_FOUND)
    message(STATUS "Searching for libtiledb")
    if(NOT DEFINED ${TILEDB_HOME})
        if (DEFINED ENV{TILEDB_HOME})
            set(TILEDB_HOME "$ENV{TILEDB_HOME}")
	endif()
    endif()
    if( "${TILEDB_HOME}" STREQUAL "" )
	message(STATUS "TILEDB_HOME not specified")
    else()
	message(STATUS "TILEDB_HOME is set to ${TILEDB_HOME}")
        list(APPEND POSSILE_PATHS
             "${TILEDB_HOME}/lib"
             "/usr/lib"
             "${TILEDB_HOME}/include"
             "${TILEDB_HOME}/include/tiledb"
             "/usr/include")
    endif()

    find_path(LIBTILEDB_INCLUDE_DIR NAMES tiledb.h PATHS ${POSSILE_PATHS} NO_DEFAULT_PATH)

    find_library(LIBTILEDB_LIBRARY NAMES
        libtiledb${CMAKE_SHARED_LIBRARY_SUFFIX}
	libtiledb${CMAKE_STATIC_LIBRARY_SUFFIX}
	PATHS ${POSSILE_PATHS}
        NO_DEFAULT_PATH)

    if(LIBTILEDB_INCLUDE_DIR)
	message(STATUS "Found tiledb.h header file: ${LIBTILEDB_INCLUDE_DIR}")
    else()
        message(STATUS "tiledb.h header file not found")
    endif()

    if(LIBTILEDB_LIBRARY)
	message(STATUS "Found libtiledb library: ${LIBTILEDB_LIBRARY}")
    else()
        message(STATUS "libtiledb library not found")
    endif()

    if(JAVA_JVM_LIBRARY AND LIBTILEDB_LIBRARY AND LIBTILEDB_INCLUDE_DIR)
        set(LIBTILEDB_FOUND TRUE)
    else()
	set(LIBTILEDB_FOUND FALSE)
    endif()
endif()

if(LIBTILEDB_FIND_REQUIRED AND NOT LIBTILEDB_FOUND)
    message(FATAL_ERROR "Could not find the libtiledb library.")
endif()