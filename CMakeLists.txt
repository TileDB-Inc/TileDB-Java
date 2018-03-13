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

project(TileDB_JNI)
cmake_minimum_required(VERSION 2.8)

# Add project cmake modules to CMake module path
list(APPEND CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmake/Modules/")

# Default user definitions
set(TILEDB_VERBOSE False CACHE BOOL "Prints TileDB errors with verbosity")
if(NOT CMAKE_BUILD_TYPE)
  set(CMAKE_BUILD_TYPE Release)
endif()

# Only for Mac OS X warnings
if(APPLE)
  set(CMAKE_MACOSX_RPATH 1)
endif()

set(TILEDB_JNI_LIB_DEPENDENCIES)

# Find required library dependencies
find_package(JNI)

if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif()

include_directories(${JNI_INCLUDE_DIRS})
set(TILEDB_JNI_LIB_DEPENDENCIES ${TILEDB_JNI_LIB_DEPENDENCIES} ${JNI_LIBRARIES})


find_package(TILEDB REQUIRED)
include_directories(${LIBTILEDB_INCLUDE_DIR})
set(TILEDB_JNI_LIB_DEPENDENCIES ${TILEDB_JNI_LIB_DEPENDENCIES} ${LIBTILEDB_LIBRARY})

include_directories(swig)

# On Windows, set a variable for the dependency install location.
# Defaults to install in the same TileDB installation location.
if (WIN32)
  set(TILEDB_DEPENDENCY_INSTALL_DIR "${CMAKE_INSTALL_PREFIX}")
  add_definitions(DTILEDB_MSVC_EXPORTS)
endif()

# Set C++ 2011 flag
include(CheckCXXCompilerFlag)

# Set support for C++ 2011. MSVC does not allow you to enforce a standard pre-C++14.
if (NOT WIN32)
  CHECK_CXX_COMPILER_FLAG("-std=c++11" COMPILER_SUPPORTS_CXX11)
  if(COMPILER_SUPPORTS_CXX11)
    message(STATUS "Compiler supports C++ 2011.")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
  else()
    message(FATAL_ERROR "The compiler ${CMAKE_CXX_COMPILER} has no C++11 support. Please use a different C++ compiler.")
  endif()
endif()

# Set compiler flags
if (WIN32)
  # We disable some warnings that are not present in gcc/clang -Wall:
  #   C4101: unreferenced local variable
  #   C4244: conversion warning of floating point to integer type.
  #   C4456: local variable hiding previous local variable
  #   C4457: local variable hiding function parameter
  #   C4702: unreachable code
  #   C4996: deprecation warning about e.g. sscanf.
  set(MSVC_DISABLE_WARNINGS "/wd4101 /wd4244 /wd4456 /wd4457 /wd4702 /wd4996")
  # Note: MSVC has the behavior of -fvisibility=hidden by default (you must
  # explicitly export all symbols you want exported).
  set(CMAKE_CXX_FLAGS_DEBUG "/DDEBUG /Od /Zi /W4 /WX ${MSVC_DISABLE_WARNINGS}")
  set(CMAKE_CXX_FLAGS_RELEASE "/DNDEBUG /Ox /W4 /WX ${MSVC_DISABLE_WARNINGS}")
  # Disable GDI (which we don't need, and causes some macro
  # re-definition issues if wingdi.h is included)
  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /DNOGDI")
  # Add /MPn flag from CMake invocation (if defined).
  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${MSVC_MP_FLAG}")
else()
  set(CMAKE_CXX_FLAGS_DEBUG "-DDEBUG -O0 -g3 -ggdb3 -gdwarf-3 -fvisibility=hidden")
  set(CMAKE_CXX_FLAGS_RELEASE "-DNDEBUG -O3 -fvisibility=hidden")
  set(CMAKE_CXX_FLAGS_COVERAGE "-DDEBUG -g3 -gdwarf-3 --coverage")

  set(CMAKE_EXE_LINKER_FLAGS  "${CMAKE_EXE_LINKER_FLAGS} -dynamiclib" )

  if(NOT APPLE)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wno-literal-suffix")
  endif()
endif()

if(TILEDB_VERBOSE)
  add_definitions(-DTILEDB_VERBOSE)
  message(STATUS "The TileDB library is compiled with verbosity.")
endif()

# Enable testing
#enable_testing()

add_executable(libtiledb.jnilib
    src/main/c/generated/tiledb_wrap.cxx
    src/main/c/custom/tiledb_wrap_custom.cxx)

target_link_libraries(libtiledb.jnilib
    ${TILEDB_JNI_LIB_DEPENDENCIES})
