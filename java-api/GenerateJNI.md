# Generate JNI bindings for TileDB C-API

## Dependencies

### JDK (>=1.8)

### Swig (>=3.0)

* For mac swig is available using:
`brew install swig`
* Installation instructions for several operating systems can be found here:
http://www.swig.org/Doc3.0/Preface.html#Preface_installation


## Genrate bindings

1) Copy the new TileDB header (tiledb.h) to swig/

2) Swig is not able to correctly understand the linked header files that are used in TileDB API (tiledb_version.h, tiledb_enum.h, tiledb_export.h).
To overcome this we need to manually flatten tiledb.h by copying the code from all included header files.

e.g. First section of tiledb.h should look like the following:
```
 #define TILEDB_VERSION_MAJOR 1
 #define TILEDB_VERSION_MINOR 3
 #define TILEDB_VERSION_PATCH 0



 #include <stdint.h>
 #include <stdio.h>

 /* ********************************* */
 /*               MACROS              */
 /* ********************************* */
 #ifdef __cplusplus
 extern "C" {
 #endif

 /**@{*/
 /** C Library export. */
 #if (defined __GNUC__ && __GNUC__ >= 4) || defined __INTEL_COMPILER
 #define TILEDB_EXPORT __attribute__((visibility("default")))
 #elif defined _MSC_VER
 #ifdef TILEDB_MSVC_EXPORTS
 #define TILEDB_EXPORT __declspec(dllexport)
 #else
 #define TILEDB_EXPORT __declspec(dllimport)
 #endif
 #else
 #define TILEDB_EXPORT
 #pragma message("TILEDB_EXPORT is not defined for this compiler")
 #endif
 /**@}*/

 /**@{*/
 /** Deprecated symbol. */
 #if (defined __GNUC__) || defined __INTEL_COMPILER
 #define TILEDB_DEPRECATED __attribute__((deprecated, visibility("default")))
 #elif defined _MSC_VER
 #define TILEDB_DEPRECATED __declspec(deprecated)
 #else
 #define DEPRECATED
 #pragma message("TILEDB_DEPRECATED is not defined for this compiler")
 #endif
 /**@}*/

 /* ****************************** */
 /*          TILEDB ENUMS          */
 /* ****************************** */

 /** TileDB object type. */
 typedef enum {
 #define TILEDB_OBJECT_TYPE_ENUM(id) TILEDB_##id
 TILEDB_OBJECT_TYPE_ENUM(INVALID) = 1,
 TILEDB_OBJECT_TYPE_ENUM(GROUP),
 TILEDB_OBJECT_TYPE_ENUM(ARRAY),
 TILEDB_OBJECT_TYPE_ENUM(KEY_VALUE)
 #undef TILEDB_OBJECT_TYPE_ENUM
 } tiledb_object_t;

 /** Query mode. */
 typedef enum {
 #define TILEDB_QUERY_TYPE_ENUM(id) TILEDB_##id
 TILEDB_QUERY_TYPE_ENUM(READ),
 TILEDB_QUERY_TYPE_ENUM(WRITE),
 #undef TILEDB_QUERY_TYPE_ENUM
 } tiledb_query_type_t;

 /** Query status. */
 typedef enum {
 #define TILEDB_QUERY_STATUS_ENUM(id) TILEDB_##id
 TILEDB_QUERY_STATUS_ENUM(FAILED) = -1,
 TILEDB_QUERY_STATUS_ENUM(COMPLETED) = 0,
 TILEDB_QUERY_STATUS_ENUM(INPROGRESS) = 1,
 TILEDB_QUERY_STATUS_ENUM(INCOMPLETE) = 2,
 #undef TILEDB_QUERY_STATUS_ENUM
 } tiledb_query_status_t;

 /** Filesystem. */
 typedef enum {
 #define TILEDB_FILESYSTEM_ENUM(id) TILEDB_##id
 TILEDB_FILESYSTEM_ENUM(HDFS),
 TILEDB_FILESYSTEM_ENUM(S3),
 #undef TILEDB_FILESYSTEM_ENUM
 } tiledb_filesystem_t;

 /** Data type. */
 typedef enum {
 #define TILEDB_DATATYPE_ENUM(id) TILEDB_##id
 TILEDB_DATATYPE_ENUM(INT32),
 TILEDB_DATATYPE_ENUM(INT64),
 TILEDB_DATATYPE_ENUM(FLOAT32),
 TILEDB_DATATYPE_ENUM(FLOAT64),
 TILEDB_DATATYPE_ENUM(CHAR),
 TILEDB_DATATYPE_ENUM(INT8),
 TILEDB_DATATYPE_ENUM(UINT8),
 TILEDB_DATATYPE_ENUM(INT16),
 TILEDB_DATATYPE_ENUM(UINT16),
 TILEDB_DATATYPE_ENUM(UINT32),
 TILEDB_DATATYPE_ENUM(UINT64),
 #undef TILEDB_DATATYPE_ENUM
 } tiledb_datatype_t;

 /** Array type. */
 typedef enum {
 #define TILEDB_ARRAY_TYPE_ENUM(id) TILEDB_##id
 TILEDB_ARRAY_TYPE_ENUM(DENSE),
 TILEDB_ARRAY_TYPE_ENUM(SPARSE),
 #undef TILEDB_ARRAY_TYPE_ENUM
 } tiledb_array_type_t;

 /** Tile or cell layout. */
 typedef enum {
 #define TILEDB_LAYOUT_ENUM(id) TILEDB_##id
 TILEDB_LAYOUT_ENUM(ROW_MAJOR),
 TILEDB_LAYOUT_ENUM(COL_MAJOR),
 TILEDB_LAYOUT_ENUM(GLOBAL_ORDER),
 TILEDB_LAYOUT_ENUM(UNORDERED),
 #undef TILEDB_LAYOUT_ENUM
 } tiledb_layout_t;

 /** Compression type. */
 typedef enum {
 #define TILEDB_COMPRESSOR_ENUM(id) TILEDB_##id
 #undef BLOSC_LZ4
 #undef BLOSC_LZ4HC
 #undef BLOSC_SNAPPY
 #undef BLOSC_ZLIB
 #undef BLOSC_ZSTD
 TILEDB_COMPRESSOR_ENUM(NO_COMPRESSION),
 TILEDB_COMPRESSOR_ENUM(GZIP),
 TILEDB_COMPRESSOR_ENUM(ZSTD),
 TILEDB_COMPRESSOR_ENUM(LZ4),
 TILEDB_COMPRESSOR_ENUM(BLOSC),
 TILEDB_COMPRESSOR_ENUM(BLOSC_LZ4),
 TILEDB_COMPRESSOR_ENUM(BLOSC_LZ4HC),
 TILEDB_COMPRESSOR_ENUM(BLOSC_SNAPPY),
 TILEDB_COMPRESSOR_ENUM(BLOSC_ZLIB),
 TILEDB_COMPRESSOR_ENUM(BLOSC_ZSTD),
 TILEDB_COMPRESSOR_ENUM(RLE),
 TILEDB_COMPRESSOR_ENUM(BZIP2),
 TILEDB_COMPRESSOR_ENUM(DOUBLE_DELTA),
 #undef TILEDB_COMPRESSOR_ENUM
 } tiledb_compressor_t;

 /** Walk traversal order. */
 typedef enum {
 #define TILEDB_WALK_ORDER_ENUM(id) TILEDB_##id
 TILEDB_WALK_ORDER_ENUM(PREORDER),
 TILEDB_WALK_ORDER_ENUM(POSTORDER),
 #undef TILEDB_WALK_ORDER_ENUM
 } tiledb_walk_order_t;

 /** VFS mode. */
 typedef enum {
 #define TILEDB_VFS_MODE_ENUM(id) TILEDB_##id
 TILEDB_VFS_MODE_ENUM(VFS_READ),
 TILEDB_VFS_MODE_ENUM(VFS_WRITE),
 TILEDB_VFS_MODE_ENUM(VFS_APPEND),
 #undef TILEDB_VFS_MODE_ENUM
 } tiledb_vfs_mode_t;
```

3) Generate C JNI code for the new tiledb.h file

`swig -java -c++ -package io.tiledb.libtiledb -outdir src/main/java/io/tiledb/libtiledb -o src/main/c/generated/tiledb_wrap.cxx swig/tiledb.i`

4) Open `src/main/c/generated/tiledb_wrap.cxx` and change method `charpArray_setitem` to
```
static void charpArray_setitem(char * *ary, int index, char * value) {
  if(strcmp(value,TILEDB_COORDS) == 0)
    ary[index]=(char *) TILEDB_COORDS;
  else{
    ary[index]= new char[sizeof(value)];
    strcpy(ary[index], value);
  }
}
```

5) Open `src/main/java/io/tiledb/libtiledb/tiledbJNI.java` and add the following at the top of the class
```
public class tiledbJNI {

  static {
    System.loadLibrary("tiledb");
  }
```
