#define TILEDB_ATTRIBUTE_HANDLE_T_DEFINED 
#define TILEDB_CAPI_ATTRIBUTE_EXTERNAL_H 
#define TILEDB_CAPI_BUFFER_API_EXTERNAL_H 
#define TILEDB_CAPI_BUFFER_LIST_API_EXTERNAL_H 
#define TILEDB_CAPI_CONFIG_EXTERNAL_H 
#define TILEDB_CAPI_CONTEXT_EXTERNAL_H 
#define TILEDB_CAPI_DATATYPE_API_EXTERNAL_H 
#define TILEDB_CAPI_DATA_ORDER_API_EXTERNAL_H 
#define TILEDB_CAPI_DIMENSION_EXTERNAL_H 
#define TILEDB_CAPI_DOMAIN_EXTERNAL_H 
#define TILEDB_CAPI_ERROR_EXTERNAL_H 
#define TILEDB_CAPI_EXTERNAL_COMMON_H 
#define TILEDB_CAPI_FILESYSTEM_API_EXTERNAL_H 
#define TILEDB_CAPI_FILTER_EXTERNAL_H 
#define TILEDB_CAPI_FILTER_LIST_EXTERNAL_H 
#define TILEDB_CAPI_GROUP_API_EXTERNAL_H 
#define TILEDB_CAPI_OBJECT_API_EXTERNAL_H 
#define TILEDB_CAPI_QUERY_API_EXTERNAL_H 
#define TILEDB_CAPI_STRING_EXTERNAL_H 
#define TILEDB_CAPI_VFS_EXTERNAL_H 
#define TILEDB_COMPRESSION_FILTER_DEFAULT_LEVEL (-30000)
#define TILEDB_DEPRECATED __attribute__ ((__deprecated__))
#define TILEDB_DEPRECATED_EXPORT TILEDB_EXPORT TILEDB_DEPRECATED
#define TILEDB_DEPRECATED_NO_EXPORT TILEDB_NO_EXPORT TILEDB_DEPRECATED
#define TILEDB_ERR (-1)
#define TILEDB_EXPORT __attribute__((visibility("default")))
#define TILEDB_EXPORT_H 
#define TILEDB_H 
#define TILEDB_INVALID_CONTEXT (-3)
#define TILEDB_INVALID_ERROR (-4)
#define TILEDB_MAX_PATH tiledb_max_path()
#define TILEDB_NOEXCEPT noexcept
#define TILEDB_NO_EXPORT __attribute__((visibility("hidden")))
#define TILEDB_OFFSET_SIZE tiledb_offset_size()
#define TILEDB_OK (0)
#define TILEDB_OOM (-2)
#define TILEDB_TIMESTAMPS tiledb_timestamps()
#define TILEDB_TIMESTAMP_NOW_MS tiledb_timestamp_now_ms()
#define TILEDB_VAR_NUM tiledb_var_num()
#define TILEDB_VERSION_MAJOR 2
#define TILEDB_VERSION_MINOR 18
#define TILEDB_VERSION_PATCH 0
#define TILEDB_ATTRIBUTE_HANDLE_T_DEFINED 
#define TILEDB_CAPI_ATTRIBUTE_EXTERNAL_EXPERIMENTAL_H 
#define TILEDB_CAPI_ATTRIBUTE_EXTERNAL_H 
#define TILEDB_CAPI_BUFFER_API_EXTERNAL_H 
#define TILEDB_CAPI_BUFFER_LIST_API_EXTERNAL_H 
#define TILEDB_CAPI_CONFIG_EXTERNAL_H 
#define TILEDB_CAPI_CONTEXT_EXTERNAL_H 
#define TILEDB_CAPI_DATATYPE_API_EXTERNAL_H 
#define TILEDB_CAPI_DATA_ORDER_API_EXTERNAL_H 
#define TILEDB_CAPI_DIMENSION_EXTERNAL_H 
#define TILEDB_CAPI_DOMAIN_EXTERNAL_H 
#define TILEDB_CAPI_ENUMERATION_EXPERIMENTAL_H 
#define TILEDB_CAPI_ERROR_EXTERNAL_H 
#define TILEDB_CAPI_EXTERNAL_COMMON_H 
#define TILEDB_CAPI_FILESYSTEM_API_EXTERNAL_H 
#define TILEDB_CAPI_FILTER_EXTERNAL_H 
#define TILEDB_CAPI_FILTER_LIST_EXTERNAL_H 
#define TILEDB_CAPI_GROUP_API_EXTERNAL_EXPERIMENTAL_H 
#define TILEDB_CAPI_GROUP_API_EXTERNAL_H 
#define TILEDB_CAPI_OBJECT_API_EXTERNAL_H 
#define TILEDB_CAPI_QUERY_AGGREGATE_API_EXTERNAL_EXPERIMENTAL_H 
#define TILEDB_CAPI_QUERY_API_EXTERNAL_H 
#define TILEDB_CAPI_QUERY_FIELD_API_EXTERNAL_EXPERIMENTAL_H 
#define TILEDB_CAPI_QUERY_PLAN_API_EXTERNAL_EXPERIMENTAL_H 
#define TILEDB_CAPI_STRING_EXTERNAL_H 
#define TILEDB_CAPI_VFS_EXTERNAL_H 
#define TILEDB_COMPRESSION_FILTER_DEFAULT_LEVEL (-30000)
#define TILEDB_DEPRECATED __attribute__ ((__deprecated__))
#define TILEDB_DEPRECATED_EXPORT TILEDB_EXPORT TILEDB_DEPRECATED
#define TILEDB_DEPRECATED_NO_EXPORT TILEDB_NO_EXPORT TILEDB_DEPRECATED
#define TILEDB_DIMENSION_LABEL_EXPERIMENTAL_H 
#define TILEDB_ERR (-1)
#define TILEDB_EXPERIMENTAL_H 
#define TILEDB_EXPORT __attribute__((visibility("default")))
#define TILEDB_EXPORT_H 
#define TILEDB_H 
#define TILEDB_INVALID_CONTEXT (-3)
#define TILEDB_INVALID_ERROR (-4)
#define TILEDB_MAX_PATH tiledb_max_path()
#define TILEDB_NOEXCEPT noexcept
#define TILEDB_NO_EXPORT __attribute__((visibility("hidden")))
#define TILEDB_OFFSET_SIZE tiledb_offset_size()
#define TILEDB_OK (0)
#define TILEDB_OOM (-2)
#define TILEDB_TIMESTAMPS tiledb_timestamps()
#define TILEDB_TIMESTAMP_NOW_MS tiledb_timestamp_now_ms()
#define TILEDB_VAR_NUM tiledb_var_num()
#define TILEDB_VERSION_MAJOR 2
#define TILEDB_VERSION_MINOR 18
#define TILEDB_VERSION_PATCH 0
typedef long int ptrdiff_t;
typedef long unsigned int size_t;
typedef signed char int8_t;
typedef short int16_t;
typedef int int32_t;
typedef long long int64_t;

typedef unsigned char uint8_t;
typedef unsigned short uint16_t;
typedef unsigned int uint32_t;
typedef unsigned long long uint64_t;
typedef int8_t int_least8_t;
typedef int16_t int_least16_t;
typedef int32_t int_least32_t;
typedef int64_t int_least64_t;
typedef uint8_t uint_least8_t;
typedef uint16_t uint_least16_t;
typedef uint32_t uint_least32_t;
typedef uint64_t uint_least64_t;
typedef int8_t int_fast8_t;
typedef int16_t int_fast16_t;
typedef int32_t int_fast32_t;
typedef int64_t int_fast64_t;
typedef uint8_t uint_fast8_t;
typedef uint16_t uint_fast16_t;
typedef uint32_t uint_fast32_t;
typedef uint64_t uint_fast64_t;
typedef signed char __int8_t;
typedef unsigned char __uint8_t;
typedef short __int16_t;
typedef unsigned short __uint16_t;
typedef int __int32_t;
typedef unsigned int __uint32_t;
typedef long long __int64_t;
typedef unsigned long long __uint64_t;
typedef long __darwin_intptr_t;
typedef unsigned int __darwin_natural_t;
typedef int __darwin_ct_rune_t;
typedef union {
 char __mbstate8[128];
 long long _mbstateL;
} __mbstate_t;
typedef __mbstate_t __darwin_mbstate_t;
typedef long int __darwin_ptrdiff_t;
typedef long unsigned int __darwin_size_t;
typedef __builtin_va_list __darwin_va_list;
typedef int __darwin_wchar_t;
typedef __darwin_wchar_t __darwin_rune_t;
typedef int __darwin_wint_t;
typedef unsigned long __darwin_clock_t;
typedef __uint32_t __darwin_socklen_t;
typedef long __darwin_ssize_t;
typedef long __darwin_time_t;
typedef __int64_t __darwin_blkcnt_t;
typedef __int32_t __darwin_blksize_t;
typedef __int32_t __darwin_dev_t;
typedef unsigned int __darwin_fsblkcnt_t;
typedef unsigned int __darwin_fsfilcnt_t;
typedef __uint32_t __darwin_gid_t;
typedef __uint32_t __darwin_id_t;
typedef __uint64_t __darwin_ino64_t;
typedef __darwin_ino64_t __darwin_ino_t;
typedef __darwin_natural_t __darwin_mach_port_name_t;
typedef __darwin_mach_port_name_t __darwin_mach_port_t;
typedef __uint16_t __darwin_mode_t;
typedef __int64_t __darwin_off_t;
typedef __int32_t __darwin_pid_t;
typedef __uint32_t __darwin_sigset_t;
typedef __int32_t __darwin_suseconds_t;
typedef __uint32_t __darwin_uid_t;
typedef __uint32_t __darwin_useconds_t;
typedef unsigned char __darwin_uuid_t[16];
typedef char __darwin_uuid_string_t[37];
struct __darwin_pthread_handler_rec {
 void (*__routine)(void *);
 void *__arg;
 struct __darwin_pthread_handler_rec *__next;
};
struct _opaque_pthread_attr_t {
 long __sig;
 char __opaque[56];
};
struct _opaque_pthread_cond_t {
 long __sig;
 char __opaque[40];
};
struct _opaque_pthread_condattr_t {
 long __sig;
 char __opaque[8];
};
struct _opaque_pthread_mutex_t {
 long __sig;
 char __opaque[56];
};
struct _opaque_pthread_mutexattr_t {
 long __sig;
 char __opaque[8];
};
struct _opaque_pthread_once_t {
 long __sig;
 char __opaque[8];
};
struct _opaque_pthread_rwlock_t {
 long __sig;
 char __opaque[192];
};
struct _opaque_pthread_rwlockattr_t {
 long __sig;
 char __opaque[16];
};
struct _opaque_pthread_t {
 long __sig;
 struct __darwin_pthread_handler_rec *__cleanup_stack;
 char __opaque[8176];
};
typedef struct _opaque_pthread_attr_t __darwin_pthread_attr_t;
typedef struct _opaque_pthread_cond_t __darwin_pthread_cond_t;
typedef struct _opaque_pthread_condattr_t __darwin_pthread_condattr_t;
typedef unsigned long __darwin_pthread_key_t;
typedef struct _opaque_pthread_mutex_t __darwin_pthread_mutex_t;
typedef struct _opaque_pthread_mutexattr_t __darwin_pthread_mutexattr_t;
typedef struct _opaque_pthread_once_t __darwin_pthread_once_t;
typedef struct _opaque_pthread_rwlock_t __darwin_pthread_rwlock_t;
typedef struct _opaque_pthread_rwlockattr_t __darwin_pthread_rwlockattr_t;
typedef struct _opaque_pthread_t *__darwin_pthread_t;
typedef unsigned char u_int8_t;
typedef unsigned short u_int16_t;
typedef unsigned int u_int32_t;
typedef unsigned long long u_int64_t;
typedef int64_t register_t;
typedef unsigned long uintptr_t;
typedef u_int64_t user_addr_t;
typedef u_int64_t user_size_t;
typedef int64_t user_ssize_t;
typedef int64_t user_long_t;
typedef u_int64_t user_ulong_t;
typedef int64_t user_time_t;
typedef int64_t user_off_t;
typedef u_int64_t syscall_arg_t;

typedef __darwin_intptr_t intptr_t;
typedef long int intmax_t;
typedef long unsigned int uintmax_t;
extern "C" {
typedef int32_t capi_return_t;
typedef int32_t capi_status_t;
inline capi_status_t tiledb_status(capi_return_t x) {
  return x;
}
__attribute__((visibility("default"))) capi_status_t tiledb_status_code(capi_return_t x);
}
extern "C" {
typedef enum {
 TILEDB_INT32 = 0,
  TILEDB_INT64 = 1,
  TILEDB_FLOAT32 = 2,
  TILEDB_FLOAT64 = 3,
  TILEDB_CHAR = 4,
  TILEDB_INT8 = 5,
  TILEDB_UINT8 = 6,
  TILEDB_INT16 = 7,
  TILEDB_UINT16 = 8,
  TILEDB_UINT32 = 9,
  TILEDB_UINT64 = 10,
  TILEDB_STRING_ASCII = 11,
  TILEDB_STRING_UTF8 = 12,
  TILEDB_STRING_UTF16 = 13,
  TILEDB_STRING_UTF32 = 14,
  TILEDB_STRING_UCS2 = 15,
  TILEDB_STRING_UCS4 = 16,
  TILEDB_ANY = 17,
  TILEDB_DATETIME_YEAR = 18,
  TILEDB_DATETIME_MONTH = 19,
  TILEDB_DATETIME_WEEK = 20,
  TILEDB_DATETIME_DAY = 21,
  TILEDB_DATETIME_HR = 22,
  TILEDB_DATETIME_MIN = 23,
  TILEDB_DATETIME_SEC = 24,
  TILEDB_DATETIME_MS = 25,
  TILEDB_DATETIME_US = 26,
  TILEDB_DATETIME_NS = 27,
  TILEDB_DATETIME_PS = 28,
  TILEDB_DATETIME_FS = 29,
  TILEDB_DATETIME_AS = 30,
  TILEDB_TIME_HR = 31,
  TILEDB_TIME_MIN = 32,
  TILEDB_TIME_SEC = 33,
  TILEDB_TIME_MS = 34,
  TILEDB_TIME_US = 35,
  TILEDB_TIME_NS = 36,
  TILEDB_TIME_PS = 37,
  TILEDB_TIME_FS = 38,
  TILEDB_TIME_AS = 39,
  TILEDB_BLOB = 40,
  TILEDB_BOOL = 41,
} tiledb_datatype_t;
__attribute__((visibility("default"))) capi_return_t tiledb_datatype_to_str(
    tiledb_datatype_t datatype, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_datatype_from_str(
    const char* str, tiledb_datatype_t* datatype) noexcept;
__attribute__((visibility("default"))) uint64_t tiledb_datatype_size(tiledb_datatype_t type)
    noexcept;
}
extern "C" {
typedef struct tiledb_error_handle_t tiledb_error_t;
__attribute__((visibility("default"))) capi_return_t
tiledb_error_message(tiledb_error_t* err, const char** errmsg) noexcept;
__attribute__((visibility("default"))) void tiledb_error_free(tiledb_error_t** err) noexcept;
}
extern "C" {
typedef struct tiledb_config_handle_t tiledb_config_t;
typedef struct tiledb_config_iter_handle_t tiledb_config_iter_t;
__attribute__((visibility("default"))) capi_return_t tiledb_config_alloc(
    tiledb_config_t** config, tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) void tiledb_config_free(tiledb_config_t** config) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_set(
    tiledb_config_t* config,
    const char* param,
    const char* value,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_get(
    tiledb_config_t* config,
    const char* param,
    const char** value,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_unset(
    tiledb_config_t* config,
    const char* param,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_load_from_file(
    tiledb_config_t* config,
    const char* filename,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_save_to_file(
    tiledb_config_t* config,
    const char* filename,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_compare(
    tiledb_config_t* lhs, tiledb_config_t* rhs, uint8_t* equal) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_iter_alloc(
    tiledb_config_t* config,
    const char* prefix,
    tiledb_config_iter_t** config_iter,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_iter_reset(
    tiledb_config_t* config,
    tiledb_config_iter_t* config_iter,
    const char* prefix,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) void tiledb_config_iter_free(tiledb_config_iter_t** config_iter)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_iter_here(
    tiledb_config_iter_t* config_iter,
    const char** param,
    const char** value,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_iter_next(
    tiledb_config_iter_t* config_iter, tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_config_iter_done(
    tiledb_config_iter_t* config_iter,
    int32_t* done,
    tiledb_error_t** error) noexcept;
}
extern "C" {
typedef enum {
 TILEDB_HDFS = 0,
    TILEDB_S3 = 1,
    TILEDB_AZURE = 2,
    TILEDB_GCS = 3,
    TILEDB_MEMFS = 4,

} tiledb_filesystem_t;
__attribute__((visibility("default"))) capi_return_t tiledb_filesystem_to_str(
    tiledb_filesystem_t filesystem, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filesystem_from_str(
    const char* str, tiledb_filesystem_t* filesystem) noexcept;
}
extern "C" {
typedef struct tiledb_ctx_handle_t tiledb_ctx_t;
__attribute__((visibility("default"))) capi_return_t
tiledb_ctx_alloc(tiledb_config_t* config, tiledb_ctx_t** ctx) noexcept;
__attribute__((visibility("default"))) void tiledb_ctx_free(tiledb_ctx_t** ctx) noexcept;
__attribute__((visibility("default"))) capi_return_t
tiledb_ctx_get_stats(tiledb_ctx_t* ctx, char** stats_json) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_ctx_get_config(
    tiledb_ctx_t* ctx, tiledb_config_t** config) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_ctx_get_last_error(
    tiledb_ctx_t* ctx, tiledb_error_t** err) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_ctx_is_supported_fs(
    tiledb_ctx_t* ctx,
    tiledb_filesystem_t fs,
    int32_t* is_supported) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_ctx_cancel_tasks(tiledb_ctx_t* ctx)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_ctx_set_tag(
    tiledb_ctx_t* ctx, const char* key, const char* value) noexcept;
}
extern "C" {
typedef struct tiledb_filter_handle_t tiledb_filter_t;
typedef enum {
TILEDB_FILTER_NONE = 0,
    TILEDB_FILTER_GZIP = 1,
    TILEDB_FILTER_ZSTD = 2,
    TILEDB_FILTER_LZ4 = 3,
    TILEDB_FILTER_RLE = 4,
    TILEDB_FILTER_BZIP2 = 5,
    TILEDB_FILTER_DOUBLE_DELTA = 6,
    TILEDB_FILTER_BIT_WIDTH_REDUCTION = 7,
    TILEDB_FILTER_BITSHUFFLE = 8,
    TILEDB_FILTER_BYTESHUFFLE = 9,
    TILEDB_FILTER_POSITIVE_DELTA = 10,
    TILEDB_FILTER_CHECKSUM_MD5 = 12,
    TILEDB_FILTER_CHECKSUM_SHA256 = 13,
    TILEDB_FILTER_DICTIONARY = 14,
    TILEDB_FILTER_SCALE_FLOAT = 15,
    TILEDB_FILTER_XOR = 16,
    TILEDB_FILTER_DEPRECATED = 17,
    TILEDB_FILTER_WEBP = 18,
    TILEDB_FILTER_DELTA = 19,

} tiledb_filter_type_t;
typedef enum {
 TILEDB_COMPRESSION_LEVEL = 0,
    TILEDB_BIT_WIDTH_MAX_WINDOW = 1,
    TILEDB_POSITIVE_DELTA_MAX_WINDOW = 2,
    TILEDB_SCALE_FLOAT_BYTEWIDTH = 3,
    TILEDB_SCALE_FLOAT_FACTOR = 4,
    TILEDB_SCALE_FLOAT_OFFSET = 5,
    TILEDB_WEBP_QUALITY = 6,
    TILEDB_WEBP_INPUT_FORMAT = 7,
    TILEDB_WEBP_LOSSLESS = 8,
    TILEDB_COMPRESSION_REINTERPRET_DATATYPE = 9,

} tiledb_filter_option_t;
typedef enum {
 TILEDB_WEBP_NONE = 0,
    TILEDB_WEBP_RGB = 1,
    TILEDB_WEBP_BGR = 2,
    TILEDB_WEBP_RGBA = 3,
    TILEDB_WEBP_BGRA = 4,

} tiledb_filter_webp_format_t;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_type_to_str(
    tiledb_filter_type_t filter_type, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_type_from_str(
    const char* str, tiledb_filter_type_t* filter_type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_option_to_str(
    tiledb_filter_option_t filter_option, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_option_from_str(
    const char* str, tiledb_filter_option_t* filter_option) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_alloc(
    tiledb_ctx_t* ctx,
    tiledb_filter_type_t type,
    tiledb_filter_t** filter) noexcept;
__attribute__((visibility("default"))) void tiledb_filter_free(tiledb_filter_t** filter) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_get_type(
    tiledb_ctx_t* ctx,
    tiledb_filter_t* filter,
    tiledb_filter_type_t* type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_set_option(
    tiledb_ctx_t* ctx,
    tiledb_filter_t* filter,
    tiledb_filter_option_t option,
    const void* value) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_get_option(
    tiledb_ctx_t* ctx,
    tiledb_filter_t* filter,
    tiledb_filter_option_t option,
    void* value) noexcept;
}
extern "C" {
typedef struct tiledb_filter_list_handle_t tiledb_filter_list_t;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_list_alloc(
    tiledb_ctx_t* ctx, tiledb_filter_list_t** filter_list) noexcept;
__attribute__((visibility("default"))) void tiledb_filter_list_free(tiledb_filter_list_t** filter_list)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_list_add_filter(
    tiledb_ctx_t* ctx,
    tiledb_filter_list_t* filter_list,
    tiledb_filter_t* filter) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_list_set_max_chunk_size(
    tiledb_ctx_t* ctx,
    tiledb_filter_list_t* filter_list,
    uint32_t max_chunk_size) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_list_get_nfilters(
    tiledb_ctx_t* ctx,
    const tiledb_filter_list_t* filter_list,
    uint32_t* nfilters) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_list_get_filter_from_index(
    tiledb_ctx_t* ctx,
    const tiledb_filter_list_t* filter_list,
    uint32_t index,
    tiledb_filter_t** filter) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_filter_list_get_max_chunk_size(
    tiledb_ctx_t* ctx,
    const tiledb_filter_list_t* filter_list,
    uint32_t* max_chunk_size) noexcept;
}
extern "C" {
typedef struct tiledb_string_handle_t tiledb_string_t;
__attribute__((visibility("default"))) capi_return_t tiledb_string_view(
    tiledb_string_t* s, const char** data, size_t* length) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_string_free(tiledb_string_t** s)
    noexcept;
}
extern "C" {
typedef struct tiledb_attribute_handle_t tiledb_attribute_t;
__attribute__((visibility("default"))) int32_t tiledb_attribute_alloc(
    tiledb_ctx_t* ctx,
    const char* name,
    tiledb_datatype_t type,
    tiledb_attribute_t** attr) noexcept;
__attribute__((visibility("default"))) void tiledb_attribute_free(tiledb_attribute_t** attr)
    noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_set_nullable(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    uint8_t nullable) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_set_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    tiledb_filter_list_t* filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_set_cell_val_num(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    uint32_t cell_val_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_get_name(
    tiledb_ctx_t* ctx,
    const tiledb_attribute_t* attr,
    const char** name) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_get_type(
    tiledb_ctx_t* ctx,
    const tiledb_attribute_t* attr,
    tiledb_datatype_t* type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_get_nullable(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    uint8_t* nullable) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_get_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    tiledb_filter_list_t** filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_get_cell_val_num(
    tiledb_ctx_t* ctx,
    const tiledb_attribute_t* attr,
    uint32_t* cell_val_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_get_cell_size(
    tiledb_ctx_t* ctx,
    const tiledb_attribute_t* attr,
    uint64_t* cell_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_dump(
    tiledb_ctx_t* ctx,
    const tiledb_attribute_t* attr,
    FILE* out) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_set_fill_value(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    const void* value,
    uint64_t size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_get_fill_value(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    const void** value,
    uint64_t* size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_set_fill_value_nullable(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    const void* value,
    uint64_t size,
    uint8_t validity) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_attribute_get_fill_value_nullable(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    const void** value,
    uint64_t* size,
    uint8_t* valid) noexcept;
}
extern "C" {
typedef struct tiledb_buffer_handle_t tiledb_buffer_t;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_alloc(
    tiledb_ctx_t* ctx, tiledb_buffer_t** buffer) noexcept;
__attribute__((visibility("default"))) void tiledb_buffer_free(tiledb_buffer_t** buffer) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_set_type(
    tiledb_ctx_t* ctx,
    tiledb_buffer_t* buffer,
    tiledb_datatype_t datatype) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_get_type(
    tiledb_ctx_t* ctx,
    const tiledb_buffer_t* buffer,
    tiledb_datatype_t* datatype) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_get_data(
    tiledb_ctx_t* ctx,
    const tiledb_buffer_t* buffer,
    void** data,
    uint64_t* num_bytes) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_set_data(
    tiledb_ctx_t* ctx, tiledb_buffer_t* buffer, void* data, uint64_t size)
    noexcept;
}
extern "C" {
typedef struct tiledb_buffer_list_handle_t tiledb_buffer_list_t;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_list_alloc(
    tiledb_ctx_t* ctx, tiledb_buffer_list_t** buffer_list) noexcept;
__attribute__((visibility("default"))) void tiledb_buffer_list_free(tiledb_buffer_list_t** buffer_list)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_list_get_num_buffers(
    tiledb_ctx_t* ctx,
    const tiledb_buffer_list_t* buffer_list,
    uint64_t* num_buffers) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_list_get_buffer(
    tiledb_ctx_t* ctx,
    const tiledb_buffer_list_t* buffer_list,
    uint64_t buffer_idx,
    tiledb_buffer_t** buffer) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_list_get_total_size(
    tiledb_ctx_t* ctx,
    const tiledb_buffer_list_t* buffer_list,
    uint64_t* total_size) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_buffer_list_flatten(
    tiledb_ctx_t* ctx,
    tiledb_buffer_list_t* buffer_list,
    tiledb_buffer_t** buffer) noexcept;
}
extern "C" {
typedef enum {
 TILEDB_UNORDERED_DATA = 0,
    TILEDB_INCREASING_DATA = 1,
    TILEDB_DECREASING_DATA = 2,

} tiledb_data_order_t;
__attribute__((visibility("default"))) capi_return_t tiledb_data_order_to_str(
    tiledb_data_order_t data_order, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_data_order_from_str(
    const char* str, tiledb_data_order_t* data_order) noexcept;
}

extern "C" {
typedef struct tiledb_dimension_handle_t tiledb_dimension_t;
__attribute__((visibility("default"))) int32_t tiledb_dimension_alloc(
    tiledb_ctx_t* ctx,
    const char* name,
    tiledb_datatype_t type,
    const void* dim_domain,
    const void* tile_extent,
    tiledb_dimension_t** dim) noexcept;
__attribute__((visibility("default"))) void tiledb_dimension_free(tiledb_dimension_t** dim)
    noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_set_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_dimension_t* dim,
    tiledb_filter_list_t* filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_set_cell_val_num(
    tiledb_ctx_t* ctx,
    tiledb_dimension_t* dim,
    uint32_t cell_val_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_get_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_dimension_t* dim,
    tiledb_filter_list_t** filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_get_cell_val_num(
    tiledb_ctx_t* ctx,
    const tiledb_dimension_t* dim,
    uint32_t* cell_val_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_get_name(
    tiledb_ctx_t* ctx,
    const tiledb_dimension_t* dim,
    const char** name) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_get_type(
    tiledb_ctx_t* ctx,
    const tiledb_dimension_t* dim,
    tiledb_datatype_t* type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_get_domain(
    tiledb_ctx_t* ctx,
    const tiledb_dimension_t* dim,
    const void** domain) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_get_tile_extent(
    tiledb_ctx_t* ctx,
    const tiledb_dimension_t* dim,
    const void** tile_extent) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_dimension_dump(
    tiledb_ctx_t* ctx,
    const tiledb_dimension_t* dim,
    FILE* out) noexcept;
}
extern "C" {
typedef struct tiledb_domain_handle_t tiledb_domain_t;
__attribute__((visibility("default"))) int32_t tiledb_domain_alloc(
    tiledb_ctx_t* ctx, tiledb_domain_t** domain) noexcept;
__attribute__((visibility("default"))) void tiledb_domain_free(tiledb_domain_t** domain) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_domain_get_type(
    tiledb_ctx_t* ctx,
    const tiledb_domain_t* domain,
    tiledb_datatype_t* type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_domain_get_ndim(
    tiledb_ctx_t* ctx,
    const tiledb_domain_t* domain,
    uint32_t* ndim) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_domain_add_dimension(
    tiledb_ctx_t* ctx,
    tiledb_domain_t* domain,
    tiledb_dimension_t* dim) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_domain_get_dimension_from_index(
    tiledb_ctx_t* ctx,
    const tiledb_domain_t* domain,
    uint32_t index,
    tiledb_dimension_t** dim) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_domain_get_dimension_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_domain_t* domain,
    const char* name,
    tiledb_dimension_t** dim) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_domain_has_dimension(
    tiledb_ctx_t* ctx,
    const tiledb_domain_t* domain,
    const char* name,
    int32_t* has_dim) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_domain_dump(
    tiledb_ctx_t* ctx,
    const tiledb_domain_t* domain,
    FILE* out) noexcept;
}
extern "C" {
typedef struct tiledb_group_handle_t tiledb_group_t;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) capi_return_t
tiledb_group_create(tiledb_ctx_t* ctx, const char* group_uri) noexcept;
}
extern "C" {
typedef enum {
 TILEDB_INVALID = 0,
  TILEDB_GROUP = 1,
  TILEDB_ARRAY = 2,

} tiledb_object_t;
typedef enum {
 TILEDB_PREORDER = 0,
  TILEDB_POSTORDER = 1,

} tiledb_walk_order_t;
__attribute__((visibility("default"))) capi_return_t tiledb_object_type_to_str(
    tiledb_object_t object_type, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_object_type_from_str(
    const char* str, tiledb_object_t* object_type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_walk_order_to_str(
    tiledb_walk_order_t walk_order, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_walk_order_from_str(
    const char* str, tiledb_walk_order_t* walk_order) noexcept;
}
extern "C" {
typedef struct tiledb_query_t tiledb_query_t;
typedef enum {
 TILEDB_READ = 0,
  TILEDB_WRITE = 1,
  TILEDB_DELETE = 2,
  TILEDB_UPDATE = 3,
  TILEDB_MODIFY_EXCLUSIVE = 4,
} tiledb_query_type_t;
__attribute__((visibility("default"))) capi_return_t tiledb_query_type_to_str(
    tiledb_query_type_t query_type, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_query_type_from_str(
    const char* str, tiledb_query_type_t* query_type) noexcept;
}

extern "C" {
typedef struct tiledb_vfs_handle_t tiledb_vfs_t;
typedef struct tiledb_vfs_fh_handle_t tiledb_vfs_fh_t;
typedef enum {
TILEDB_VFS_READ = 0,
    TILEDB_VFS_WRITE = 1,
    TILEDB_VFS_APPEND = 2,

} tiledb_vfs_mode_t;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_mode_to_str(
    tiledb_vfs_mode_t vfs_mode, const char** str) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_mode_from_str(
    const char* str, tiledb_vfs_mode_t* vfs_mode) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_alloc(
    tiledb_ctx_t* ctx,
    tiledb_config_t* config,
    tiledb_vfs_t** vfs) noexcept;
__attribute__((visibility("default"))) void tiledb_vfs_free(tiledb_vfs_t** vfs) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_get_config(
    tiledb_ctx_t* ctx,
    tiledb_vfs_t* vfs,
    tiledb_config_t** config) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_create_bucket(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_remove_bucket(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_empty_bucket(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_is_empty_bucket(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri, int32_t* is_empty)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_is_bucket(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri, int32_t* is_bucket)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_create_dir(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_is_dir(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri, int32_t* is_dir)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_remove_dir(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_is_file(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri, int32_t* is_file)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_remove_file(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_dir_size(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri, uint64_t* size)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_file_size(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri, uint64_t* size)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_move_file(
    tiledb_ctx_t* ctx,
    tiledb_vfs_t* vfs,
    const char* old_uri,
    const char* new_uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_move_dir(
    tiledb_ctx_t* ctx,
    tiledb_vfs_t* vfs,
    const char* old_uri,
    const char* new_uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_copy_file(
    tiledb_ctx_t* ctx,
    tiledb_vfs_t* vfs,
    const char* old_uri,
    const char* new_uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_copy_dir(
    tiledb_ctx_t* ctx,
    tiledb_vfs_t* vfs,
    const char* old_uri,
    const char* new_uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_open(
    tiledb_ctx_t* ctx,
    tiledb_vfs_t* vfs,
    const char* uri,
    tiledb_vfs_mode_t mode,
    tiledb_vfs_fh_t** fh) noexcept;
__attribute__((visibility("default"))) capi_return_t
tiledb_vfs_close(tiledb_ctx_t* ctx, tiledb_vfs_fh_t* fh) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_read(
    tiledb_ctx_t* ctx,
    tiledb_vfs_fh_t* fh,
    uint64_t offset,
    void* buffer,
    uint64_t nbytes) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_write(
    tiledb_ctx_t* ctx, tiledb_vfs_fh_t* fh, const void* buffer, uint64_t nbytes)
    noexcept;
__attribute__((visibility("default"))) capi_return_t
tiledb_vfs_sync(tiledb_ctx_t* ctx, tiledb_vfs_fh_t* fh) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_ls(
    tiledb_ctx_t* ctx,
    tiledb_vfs_t* vfs,
    const char* path,
    int32_t (*callback)(const char*, void*),
    void* data) noexcept;
__attribute__((visibility("default"))) void tiledb_vfs_fh_free(tiledb_vfs_fh_t** fh) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_fh_is_closed(
    tiledb_ctx_t* ctx, tiledb_vfs_fh_t* fh, int32_t* is_closed) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_vfs_touch(
    tiledb_ctx_t* ctx, tiledb_vfs_t* vfs, const char* uri) noexcept;
}
extern "C" {
typedef enum {
 TILEDB_FAILED = 0,
    TILEDB_COMPLETED = 1,
    TILEDB_INPROGRESS = 2,
    TILEDB_INCOMPLETE = 3,
    TILEDB_UNINITIALIZED = 4,
    TILEDB_INITIALIZED = 5,

} tiledb_query_status_t;
typedef enum {
 TILEDB_LT = 0,
    TILEDB_LE = 1,
    TILEDB_GT = 2,
    TILEDB_GE = 3,
    TILEDB_EQ = 4,
    TILEDB_NE = 5,
    TILEDB_IN = 6,
    TILEDB_NOT_IN = 7,

} tiledb_query_condition_op_t;
typedef enum {
 TILEDB_AND = 0,
    TILEDB_OR = 1,
    TILEDB_NOT = 2,

} tiledb_query_condition_combination_op_t;
typedef enum {
 TILEDB_DENSE = 0,
    TILEDB_SPARSE = 1,

} tiledb_array_type_t;
typedef enum {
 TILEDB_ROW_MAJOR = 0,
    TILEDB_COL_MAJOR = 1,
    TILEDB_GLOBAL_ORDER = 2,
    TILEDB_UNORDERED = 3,
    TILEDB_HILBERT = 4,

} tiledb_layout_t;
typedef enum {
 TILEDB_NO_ENCRYPTION = 0,
    TILEDB_AES_256_GCM = 1,

} tiledb_encryption_type_t;
typedef enum {
 TILEDB_MIME_AUTODETECT = 0,
    TILEDB_MIME_TIFF = 1,
    TILEDB_MIME_PDF = 2,

} tiledb_mime_type_t;
__attribute__((visibility("default"))) int32_t tiledb_array_type_to_str(
    tiledb_array_type_t array_type, const char** str) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_type_from_str(
    const char* str, tiledb_array_type_t* array_type) noexcept;
__attribute__((visibility("default"))) int32_t
tiledb_layout_to_str(tiledb_layout_t layout, const char** str) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_layout_from_str(
    const char* str, tiledb_layout_t* layout) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_encryption_type_to_str(
    tiledb_encryption_type_t encryption_type, const char** str) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_encryption_type_from_str(
    const char* str, tiledb_encryption_type_t* encryption_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_status_to_str(
    tiledb_query_status_t query_status, const char** str) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_status_from_str(
    const char* str, tiledb_query_status_t* query_status) noexcept;
__attribute__((visibility("default"))) uint32_t tiledb_var_num(void) noexcept;
__attribute__((visibility("default"))) uint32_t tiledb_max_path(void) noexcept;
__attribute__((visibility("default"))) uint64_t tiledb_offset_size(void) noexcept;
__attribute__((visibility("default"))) uint64_t tiledb_timestamp_now_ms(void) noexcept;
__attribute__((visibility("default"))) const char* tiledb_timestamps(void) noexcept;
__attribute__((visibility("default"))) void tiledb_version(int32_t* major, int32_t* minor, int32_t* rev)
    noexcept;
typedef struct tiledb_array_t tiledb_array_t;
typedef struct tiledb_subarray_t tiledb_subarray_t;
typedef struct tiledb_array_schema_t tiledb_array_schema_t;
typedef struct tiledb_query_condition_t tiledb_query_condition_t;
typedef struct tiledb_fragment_info_t tiledb_fragment_info_t;
typedef struct tiledb_consolidation_plan_t tiledb_consolidation_plan_t;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_alloc(
    tiledb_ctx_t* ctx,
    tiledb_array_type_t array_type,
    tiledb_array_schema_t** array_schema) noexcept;
__attribute__((visibility("default"))) void tiledb_array_schema_free(
    tiledb_array_schema_t** array_schema) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_add_attribute(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_attribute_t* attr) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_set_allows_dups(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    int allows_dups) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_allows_dups(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    int* allows_dups) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_version(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    uint32_t* version) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_set_domain(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_domain_t* domain) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_set_capacity(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    uint64_t capacity) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_set_cell_order(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_layout_t cell_order) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_set_tile_order(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_layout_t tile_order) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_set_coords_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_filter_list_t* filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_set_offsets_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_filter_list_t* filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_set_validity_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_filter_list_t* filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_check(
    tiledb_ctx_t* ctx, tiledb_array_schema_t* array_schema) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_load(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_array_schema_t** array_schema) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_array_schema_load_with_key(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_encryption_type_t encryption_type,
    const void* encryption_key,
    uint32_t key_length,
    tiledb_array_schema_t** array_schema) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_array_type(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    tiledb_array_type_t* array_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_capacity(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    uint64_t* capacity) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_cell_order(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    tiledb_layout_t* cell_order) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_coords_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_filter_list_t** filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_offsets_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_filter_list_t** filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_validity_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_filter_list_t** filter_list) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_domain(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    tiledb_domain_t** domain) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_tile_order(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    tiledb_layout_t* tile_order) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_attribute_num(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    uint32_t* attribute_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_attribute_from_index(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    uint32_t index,
    tiledb_attribute_t** attr) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_get_attribute_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    const char* name,
    tiledb_attribute_t** attr) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_has_attribute(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    const char* name,
    int32_t* has_attr) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_dump(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    FILE* out) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_alloc(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    tiledb_query_type_t query_type,
    tiledb_query_t** query) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_stats(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    char** stats_json) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_set_config(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_config(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_config_t** config) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_set_subarray(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const void* subarray) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_set_subarray_t(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const tiledb_subarray_t* subarray) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_set_data_buffer(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* name,
    void* buffer,
    uint64_t* buffer_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_set_offsets_buffer(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* name,
    uint64_t* buffer,
    uint64_t* buffer_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_set_validity_buffer(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* name,
    uint8_t* buffer,
    uint64_t* buffer_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_data_buffer(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* name,
    void** buffer,
    uint64_t** buffer_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_offsets_buffer(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* name,
    uint64_t** buffer,
    uint64_t** buffer_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_validity_buffer(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* name,
    uint8_t** buffer,
    uint64_t** buffer_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_set_layout(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_layout_t layout) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_set_condition(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const tiledb_query_condition_t* cond) noexcept;
__attribute__((visibility("default"))) int32_t
tiledb_query_finalize(tiledb_ctx_t* ctx, tiledb_query_t* query) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_submit_and_finalize(
    tiledb_ctx_t* ctx, tiledb_query_t* query) noexcept;
__attribute__((visibility("default"))) void tiledb_query_free(tiledb_query_t** query) noexcept;
__attribute__((visibility("default"))) int32_t
tiledb_query_submit(tiledb_ctx_t* ctx, tiledb_query_t* query) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_submit_async(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    void (*callback)(void*),
    void* callback_data) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_has_results(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    int32_t* has_results) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_status(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_query_status_t* status) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_type(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_query_type_t* query_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_layout(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_layout_t* query_layout) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_array(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_array_t** array) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_add_range(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    uint32_t dim_idx,
    const void* start,
    const void* end,
    const void* stride) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_add_range_by_name(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* dim_name,
    const void* start,
    const void* end,
    const void* stride) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_add_range_var(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    uint32_t dim_idx,
    const void* start,
    uint64_t start_size,
    const void* end,
    uint64_t end_size) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_add_range_var_by_name(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* dim_name,
    const void* start,
    uint64_t start_size,
    const void* end,
    uint64_t end_size) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_get_range_num(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    uint32_t dim_idx,
    uint64_t* range_num) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_get_range_num_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    const char* dim_name,
    uint64_t* range_num) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_get_range(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    uint32_t dim_idx,
    uint64_t range_idx,
    const void** start,
    const void** end,
    const void** stride) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_get_range_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    const char* dim_name,
    uint64_t range_idx,
    const void** start,
    const void** end,
    const void** stride) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_get_range_var_size(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    uint32_t dim_idx,
    uint64_t range_idx,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_get_range_var_size_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    const char* dim_name,
    uint64_t range_idx,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_get_range_var(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    uint32_t dim_idx,
    uint64_t range_idx,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_get_range_var_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    const char* dim_name,
    uint64_t range_idx,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_est_result_size(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    const char* name,
    uint64_t* size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_est_result_size_var(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    const char* name,
    uint64_t* size_off,
    uint64_t* size_val) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_est_result_size_nullable(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    const char* name,
    uint64_t* size_val,
    uint64_t* size_validity) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_est_result_size_var_nullable(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    const char* name,
    uint64_t* size_off,
    uint64_t* size_val,
    uint64_t* size_validity) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_fragment_num(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    uint32_t* num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_fragment_uri(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    uint64_t idx,
    const char** uri) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_fragment_timestamp_range(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    uint64_t idx,
    uint64_t* t1,
    uint64_t* t2) noexcept;
__attribute__((visibility("default")))
int32_t tiledb_query_get_subarray_t(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    tiledb_subarray_t** subarray) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_condition_alloc(
    tiledb_ctx_t* ctx, tiledb_query_condition_t** cond) noexcept;
__attribute__((visibility("default"))) void tiledb_query_condition_free(tiledb_query_condition_t** cond)
    noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_condition_init(
    tiledb_ctx_t* ctx,
    tiledb_query_condition_t* cond,
    const char* attribute_name,
    const void* condition_value,
    uint64_t condition_value_size,
    tiledb_query_condition_op_t op) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_condition_combine(
    tiledb_ctx_t* ctx,
    const tiledb_query_condition_t* left_cond,
    const tiledb_query_condition_t* right_cond,
    tiledb_query_condition_combination_op_t combination_op,
    tiledb_query_condition_t** combined_cond) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_condition_negate(
    tiledb_ctx_t* ctx,
    const tiledb_query_condition_t* cond,
    tiledb_query_condition_t** negated_cond) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_alloc(
    tiledb_ctx_t* ctx,
    const tiledb_array_t* array,
    tiledb_subarray_t** subarray) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_set_config(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) void tiledb_subarray_free(tiledb_subarray_t** subarray)
    noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_set_coalesce_ranges(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    int coalesce_ranges) noexcept;
__attribute__((visibility("default")))
int32_t tiledb_subarray_set_subarray(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    const void* subarray_v) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_add_range(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    uint32_t dim_idx,
    const void* start,
    const void* end,
    const void* stride) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_add_range_by_name(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    const char* dim_name,
    const void* start,
    const void* end,
    const void* stride) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_add_range_var(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    uint32_t dim_idx,
    const void* start,
    uint64_t start_size,
    const void* end,
    uint64_t end_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_add_range_var_by_name(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    const char* dim_name,
    const void* start,
    uint64_t start_size,
    const void* end,
    uint64_t end_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_get_range_num(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    uint32_t dim_idx,
    uint64_t* range_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_get_range_num_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const char* dim_name,
    uint64_t* range_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_get_range(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    uint32_t dim_idx,
    uint64_t range_idx,
    const void** start,
    const void** end,
    const void** stride) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_get_range_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const char* dim_name,
    uint64_t range_idx,
    const void** start,
    const void** end,
    const void** stride) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_get_range_var_size(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    uint32_t dim_idx,
    uint64_t range_idx,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_get_range_var_size_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const char* dim_name,
    uint64_t range_idx,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_get_range_var(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    uint32_t dim_idx,
    uint64_t range_idx,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_get_range_var_from_name(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const char* dim_name,
    uint64_t range_idx,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_alloc(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_array_t** array) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_set_open_timestamp_start(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint64_t timestamp_start) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_set_open_timestamp_end(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint64_t timestamp_end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_open_timestamp_start(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint64_t* timestamp_start) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_open_timestamp_end(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint64_t* timestamp_end) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_array_delete_fragments(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    const char* uri,
    uint64_t timestamp_start,
    uint64_t timestamp_end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_delete_fragments_v2(
    tiledb_ctx_t* ctx,
    const char* uri_str,
    uint64_t timestamp_start,
    uint64_t timestamp_end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_delete_fragments_list(
    tiledb_ctx_t* ctx,
    const char* uri_str,
    const char* fragment_uris[],
    const size_t num_fragments) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_open(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    tiledb_query_type_t query_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_is_open(
    tiledb_ctx_t* ctx, tiledb_array_t* array, int32_t* is_open) noexcept;
__attribute__((visibility("default"))) int32_t
tiledb_array_reopen(tiledb_ctx_t* ctx, tiledb_array_t* array) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_set_config(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_config(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    tiledb_config_t** config) noexcept;
__attribute__((visibility("default"))) int32_t
tiledb_array_close(tiledb_ctx_t* ctx, tiledb_array_t* array) noexcept;
__attribute__((visibility("default"))) void tiledb_array_free(tiledb_array_t** array) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_schema(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    tiledb_array_schema_t** array_schema) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_query_type(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    tiledb_query_type_t* query_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_create(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    const tiledb_array_schema_t* array_schema) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_array_create_with_key(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    const tiledb_array_schema_t* array_schema,
    tiledb_encryption_type_t encryption_type,
    const void* encryption_key,
    uint32_t key_length) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_consolidate(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_array_consolidate_with_key(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_encryption_type_t encryption_type,
    const void* encryption_key,
    uint32_t key_length,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_vacuum(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_non_empty_domain(
    tiledb_ctx_t* ctx, tiledb_array_t* array, void* domain, int32_t* is_empty)
    noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_non_empty_domain_from_index(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint32_t idx,
    void* domain,
    int32_t* is_empty) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_non_empty_domain_from_name(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    const char* name,
    void* domain,
    int32_t* is_empty) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_non_empty_domain_var_size_from_index(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint32_t idx,
    uint64_t* start_size,
    uint64_t* end_size,
    int32_t* is_empty) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_non_empty_domain_var_size_from_name(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    const char* name,
    uint64_t* start_size,
    uint64_t* end_size,
    int32_t* is_empty) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_non_empty_domain_var_from_index(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint32_t idx,
    void* start,
    void* end,
    int32_t* is_empty) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_non_empty_domain_var_from_name(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    const char* name,
    void* start,
    void* end,
    int32_t* is_empty) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_uri(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    const char** array_uri) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_encryption_type(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_encryption_type_t* encryption_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_put_metadata(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    const char* key,
    tiledb_datatype_t value_type,
    uint32_t value_num,
    const void* value) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_delete_metadata(
    tiledb_ctx_t* ctx, tiledb_array_t* array, const char* key) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_metadata(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    const char* key,
    tiledb_datatype_t* value_type,
    uint32_t* value_num,
    const void** value) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_metadata_num(
    tiledb_ctx_t* ctx, tiledb_array_t* array, uint64_t* num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_get_metadata_from_index(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint64_t index,
    const char** key,
    uint32_t* key_len,
    tiledb_datatype_t* value_type,
    uint32_t* value_num,
    const void** value) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_has_metadata_key(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    const char* key,
    tiledb_datatype_t* value_type,
    int32_t* has_key) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_object_type(
    tiledb_ctx_t* ctx, const char* path, tiledb_object_t* type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_object_remove(tiledb_ctx_t* ctx, const char* path)
    noexcept;
__attribute__((visibility("default"))) int32_t tiledb_object_move(
    tiledb_ctx_t* ctx,
    const char* old_path,
    const char* new_path) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_object_walk(
    tiledb_ctx_t* ctx,
    const char* path,
    tiledb_walk_order_t order,
    int32_t (*callback)(const char*, tiledb_object_t, void*),
    void* data) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_object_ls(
    tiledb_ctx_t* ctx,
    const char* path,
    int32_t (*callback)(const char*, tiledb_object_t, void*),
    void* data) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_uri_to_path(
    tiledb_ctx_t* ctx, const char* uri, char* path_out, uint32_t* path_length)
    noexcept;
__attribute__((visibility("default"))) int32_t tiledb_stats_enable(void) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_stats_disable(void) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_stats_reset(void) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_stats_dump(FILE* out) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_stats_dump_str(char** out) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_stats_raw_dump(FILE* out) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_stats_raw_dump_str(char** out) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_stats_free_str(char** out) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_heap_profiler_enable(
    const char* file_name_prefix,
    uint64_t dump_interval_ms,
    uint64_t dump_interval_bytes,
    uint64_t dump_threshold_bytes) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_alloc(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_fragment_info_t** fragment_info) noexcept;
__attribute__((visibility("default"))) void tiledb_fragment_info_free(
    tiledb_fragment_info_t** fragment_info) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_set_config(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_config(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    tiledb_config_t** config) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_load(
    tiledb_ctx_t* ctx, tiledb_fragment_info_t* fragment_info) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_fragment_info_get_fragment_name(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    const char** name) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_fragment_name_v2(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    tiledb_string_t** name) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_fragment_num(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t* fragment_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_fragment_uri(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    const char** uri) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_fragment_size(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint64_t* size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_dense(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    int32_t* dense) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_sparse(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    int32_t* sparse) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_timestamp_range(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint64_t* start,
    uint64_t* end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_non_empty_domain_from_index(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t did,
    void* domain) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_non_empty_domain_from_name(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    const char* dim_name,
    void* domain) noexcept;
__attribute__((visibility("default"))) int32_t
tiledb_fragment_info_get_non_empty_domain_var_size_from_index(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t did,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) int32_t
tiledb_fragment_info_get_non_empty_domain_var_size_from_name(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    const char* dim_name,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_non_empty_domain_var_from_index(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t did,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_non_empty_domain_var_from_name(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    const char* dim_name,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_mbr_num(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint64_t* mbr_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_mbr_from_index(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t mid,
    uint32_t did,
    void* mbr) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_mbr_from_name(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t mid,
    const char* dim_name,
    void* mbr) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_mbr_var_size_from_index(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t mid,
    uint32_t did,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_mbr_var_size_from_name(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t mid,
    const char* dim_name,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_mbr_var_from_index(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t mid,
    uint32_t did,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_mbr_var_from_name(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t mid,
    const char* dim_name,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_cell_num(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint64_t* cell_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_version(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    uint32_t* version) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_has_consolidated_metadata(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    int32_t* has) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_unconsolidated_metadata_num(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t* unconsolidated) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_to_vacuum_num(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t* to_vacuum_num) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_to_vacuum_uri(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    const char** uri) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_array_schema(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    tiledb_array_schema_t** array_schema) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_array_schema_name(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint32_t fid,
    const char** schema_name) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_dump(
    tiledb_ctx_t* ctx,
    const tiledb_fragment_info_t* fragment_info,
    FILE* out) noexcept;
}
extern "C" {
__attribute__((visibility("default"))) capi_return_t tiledb_attribute_set_enumeration_name(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    const char* enumeration_name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_attribute_get_enumeration_name(
    tiledb_ctx_t* ctx,
    tiledb_attribute_t* attr,
    tiledb_string_t** name) noexcept;
}
extern "C" {
typedef struct tiledb_enumeration_handle_t tiledb_enumeration_t;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_alloc(
    tiledb_ctx_t* ctx,
    const char* name,
    tiledb_datatype_t type,
    uint32_t cell_val_num,
    int ordered,
    const void* data,
    uint64_t data_size,
    const void* offsets,
    uint64_t offsets_size,
    tiledb_enumeration_t** enumeration) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_extend(
    tiledb_ctx_t* ctx,
    tiledb_enumeration_t* old_enumeration,
    const void* data,
    uint64_t data_size,
    const void* offsets,
    uint64_t offsets_size,
    tiledb_enumeration_t** new_enumeration) noexcept;
__attribute__((visibility("default"))) void tiledb_enumeration_free(tiledb_enumeration_t** enumeration)
    noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_get_name(
    tiledb_ctx_t* ctx,
    tiledb_enumeration_t* enumeration,
    tiledb_string_t** name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_get_type(
    tiledb_ctx_t* ctx,
    tiledb_enumeration_t* enumeration,
    tiledb_datatype_t* type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_get_cell_val_num(
    tiledb_ctx_t* ctx,
    tiledb_enumeration_t* enumeration,
    uint32_t* cell_val_num) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_get_ordered(
    tiledb_ctx_t* ctx,
    tiledb_enumeration_t* enumeration,
    int* ordered) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_get_data(
    tiledb_ctx_t* ctx,
    tiledb_enumeration_t* enumeration,
    const void** data,
    uint64_t* data_size) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_get_offsets(
    tiledb_ctx_t* ctx,
    tiledb_enumeration_t* enumeration,
    const void** offsets,
    uint64_t* offsets_size) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_enumeration_dump(
    tiledb_ctx_t* ctx,
    tiledb_enumeration_t* enumeration,
    FILE* out) noexcept;
}
extern "C" {
__attribute__((visibility("default"))) capi_return_t tiledb_group_alloc(
    tiledb_ctx_t* ctx,
    const char* group_uri,
    tiledb_group_t** group) noexcept;
__attribute__((visibility("default"))) void tiledb_group_free(tiledb_group_t** group) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_open(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    tiledb_query_type_t query_type) noexcept;
__attribute__((visibility("default"))) capi_return_t
tiledb_group_close(tiledb_ctx_t* ctx, tiledb_group_t* group) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_set_config(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_config(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    tiledb_config_t** config) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_put_metadata(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    const char* key,
    tiledb_datatype_t value_type,
    uint32_t value_num,
    const void* value) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_group_delete_group(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    const char* uri,
    const uint8_t recursive) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_delete_metadata(
    tiledb_ctx_t* ctx, tiledb_group_t* group, const char* key) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_metadata(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    const char* key,
    tiledb_datatype_t* value_type,
    uint32_t* value_num,
    const void** value) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_metadata_num(
    tiledb_ctx_t* ctx, tiledb_group_t* group, uint64_t* num) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_metadata_from_index(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    uint64_t index,
    const char** key,
    uint32_t* key_len,
    tiledb_datatype_t* value_type,
    uint32_t* value_num,
    const void** value) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_has_metadata_key(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    const char* key,
    tiledb_datatype_t* value_type,
    int32_t* has_key) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_add_member(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    const char* uri,
    const uint8_t relative,
    const char* name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_remove_member(
    tiledb_ctx_t* ctx, tiledb_group_t* group, const char* name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_member_count(
    tiledb_ctx_t* ctx, tiledb_group_t* group, uint64_t* count) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_member_by_index(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    uint64_t index,
    char** uri,
    tiledb_object_t* type,
    char** name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_member_by_name(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    const char* name,
    char** uri,
    tiledb_object_t* type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_is_relative_uri_by_name(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    const char* name,
    uint8_t* relative) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_is_open(
    tiledb_ctx_t* ctx, tiledb_group_t* group, int32_t* is_open) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_uri(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    const char** group_uri) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_get_query_type(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    tiledb_query_type_t* query_type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_dump_str(
    tiledb_ctx_t* ctx,
    tiledb_group_t* group,
    char** dump_ascii,
    const uint8_t recursive) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_consolidate_metadata(
    tiledb_ctx_t* ctx,
    const char* group_uri,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_group_vacuum_metadata(
    tiledb_ctx_t* ctx,
    const char* group_uri,
    tiledb_config_t* config) noexcept;
}
extern "C" {
typedef struct tiledb_query_channel_handle_t tiledb_query_channel_t;
typedef struct tiledb_channel_operation_handle_t tiledb_channel_operation_t;
typedef struct tiledb_channel_operator_handle_t tiledb_channel_operator_t;
__attribute__((visibility("default"))) int32_t tiledb_channel_operator_sum_get(
    tiledb_ctx_t* ctx, const tiledb_channel_operator_t** op) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_channel_operator_min_get(
    tiledb_ctx_t* ctx, const tiledb_channel_operator_t** op) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_channel_operator_max_get(
    tiledb_ctx_t* ctx, const tiledb_channel_operator_t** op) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_aggregate_count_get(
    tiledb_ctx_t* ctx,
    const tiledb_channel_operation_t** operation) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_channel_operator_mean_get(
    tiledb_ctx_t* ctx, const tiledb_channel_operator_t** op) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_channel_operator_null_count_get(
    tiledb_ctx_t* ctx, const tiledb_channel_operator_t** op) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_default_channel(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_query_channel_t** channel) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_create_unary_aggregate(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const tiledb_channel_operator_t* op,
    const char* input_field_name,
    tiledb_channel_operation_t** operation) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_channel_apply_aggregate(
    tiledb_ctx_t* ctx,
    tiledb_query_channel_t* channel,
    const char* output_field_name,
    const tiledb_channel_operation_t* operation) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_aggregate_free(
    tiledb_ctx_t* ctx, tiledb_channel_operation_t** op) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_query_channel_free(
    tiledb_ctx_t* ctx, tiledb_query_channel_t** channel) noexcept;
}
extern "C" {
typedef struct tiledb_query_field_handle_t tiledb_query_field_t;
typedef enum {
  TILEDB_ATTRIBUTE_FIELD = 0,
  TILEDB_DIMENSION_FIELD,
  TILEDB_AGGREGATE_FIELD
} tiledb_field_origin_t;
__attribute__((visibility("default"))) capi_return_t tiledb_query_get_field(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* field_name,
    tiledb_query_field_t** field) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_query_field_free(
    tiledb_ctx_t* ctx, tiledb_query_field_t** field) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_field_datatype(
    tiledb_ctx_t* ctx,
    tiledb_query_field_t* field,
    tiledb_datatype_t* type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_field_cell_val_num(
    tiledb_ctx_t* ctx,
    tiledb_query_field_t* field,
    uint32_t* cell_val_num) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_field_origin(
    tiledb_ctx_t* ctx,
    tiledb_query_field_t* field,
    tiledb_field_origin_t* origin) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_field_channel(
    tiledb_ctx_t* ctx,
    tiledb_query_field_t* field,
    tiledb_query_channel_t** channel) noexcept;
}
extern "C" {
__attribute__((visibility("default"))) int32_t tiledb_query_get_plan(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_string_t** plan) noexcept;
}
extern "C" {
typedef struct tiledb_dimension_label_handle_t tiledb_dimension_label_t;
__attribute__((visibility("default"))) void tiledb_dimension_label_free(
    tiledb_dimension_label_t** dim_label) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_dimension_label_get_dimension_index(
    tiledb_ctx_t* ctx,
    tiledb_dimension_label_t* dim_label,
    uint32_t* dim_index) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_dimension_label_get_label_attr_name(
    tiledb_ctx_t* ctx,
    tiledb_dimension_label_t* dim_label,
    const char** label_attr_name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_dimension_label_get_label_cell_val_num(
    tiledb_ctx_t* ctx,
    tiledb_dimension_label_t* dim_label,
    uint32_t* label_cell_val_num) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_dimension_label_get_label_order(
    tiledb_ctx_t* ctx,
    tiledb_dimension_label_t* dim_label,
    tiledb_data_order_t* label_order) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_dimension_label_get_label_type(
    tiledb_ctx_t* ctx,
    tiledb_dimension_label_t* dim_label,
    tiledb_datatype_t* label_type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_dimension_label_get_name(
    tiledb_ctx_t* ctx,
    tiledb_dimension_label_t* dim_label,
    const char** name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_dimension_label_get_uri(
    tiledb_ctx_t* ctx,
    tiledb_dimension_label_t* dim_label,
    const char** uri) noexcept;
}
extern "C" {
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_add_dimension_label(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    const uint32_t dim_index,
    const char* name,
    tiledb_data_order_t label_order,
    tiledb_datatype_t label_type) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_get_dimension_label_from_name(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    const char* label_name,
    tiledb_dimension_label_t** dim_label) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_has_dimension_label(
    tiledb_ctx_t* ctx,
    const tiledb_array_schema_t* array_schema,
    const char* name,
    int32_t* has_dim_label) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_set_dimension_label_filter_list(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    const char* label_name,
    tiledb_filter_list_t* filter_list) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_set_dimension_label_tile_extent(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    const char* label_name,
    tiledb_datatype_t type,
    const void* tile_extent) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_get_dimension_label_num(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    uint64_t* dim_label_num) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_get_dimension_label_from_index(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    uint64_t dim_label_index,
    tiledb_dimension_label_t** dim_label) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_subarray_add_label_range(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    const char* label_name,
    const void* start,
    const void* end,
    const void* stride) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_subarray_add_label_range_var(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    const char* label_name,
    const void* start,
    uint64_t start_size,
    const void* end,
    uint64_t end_size) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_subarray_get_label_name(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    uint32_t dim_idx,
    const char** label_name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_subarray_get_label_range(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const char* label_name,
    uint64_t range_idx,
    const void** start,
    const void** end,
    const void** stride) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_subarray_get_label_range_num(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const char* label_name,
    uint64_t* range_num) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_subarray_get_label_range_var(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const char* label_name,
    uint64_t range_idx,
    void* start,
    void* end) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_subarray_get_label_range_var_size(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const char* label_name,
    uint64_t range_idx,
    uint64_t* start_size,
    uint64_t* end_size) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_subarray_has_label_ranges(
    tiledb_ctx_t* ctx,
    const tiledb_subarray_t* subarray,
    const uint32_t dim_idx,
    int32_t* has_label_range) noexcept;
}
extern "C" {
__attribute__((visibility("default"))) capi_return_t
tiledb_log_warn(tiledb_ctx_t* ctx, const char* message) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_as_built_dump(tiledb_string_t** out)
    noexcept;
typedef struct tiledb_array_schema_evolution_t tiledb_array_schema_evolution_t;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_evolution_alloc(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_evolution_t** array_schema_evolution) noexcept;
__attribute__((visibility("default"))) void tiledb_array_schema_evolution_free(
    tiledb_array_schema_evolution_t** array_schema_evolution) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_evolution_add_attribute(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_evolution_t* array_schema_evolution,
    tiledb_attribute_t* attribute) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_evolution_drop_attribute(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_evolution_t* array_schema_evolution,
    const char* attribute_name) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_evolution_add_enumeration(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_evolution_t* array_schema_evolution,
    tiledb_enumeration_t* enumeration) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_evolution_extend_enumeration(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_evolution_t* array_schema_evolution,
    tiledb_enumeration_t* enumeration) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_schema_evolution_drop_enumeration(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_evolution_t* array_schema_evolution,
    const char* enumeration_name) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_evolution_set_timestamp_range(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_evolution_t* array_schema_evolution,
    uint64_t lo,
    uint64_t hi) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_timestamp_range(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    uint64_t* lo,
    uint64_t* hi) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_schema_add_enumeration(
    tiledb_ctx_t* ctx,
    tiledb_array_schema_t* array_schema,
    tiledb_enumeration_t* enumeration) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_delete(tiledb_ctx_t* ctx, const char* uri)
    noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_array_delete_array(
    tiledb_ctx_t* ctx, tiledb_array_t* array, const char* uri) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_evolve(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_array_schema_evolution_t* array_schema_evolution) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_get_enumeration(
    tiledb_ctx_t* ctx,
    const tiledb_array_t* array,
    const char* name,
    tiledb_enumeration_t** enumeration) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_array_load_all_enumerations(
    tiledb_ctx_t* ctx, const tiledb_array_t* array) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_upgrade_version(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_add_update_value(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    const char* field_name,
    const void* update_value,
    uint64_t update_value_size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_subarray_add_point_ranges(
    tiledb_ctx_t* ctx,
    tiledb_subarray_t* subarray,
    uint32_t dim_idx,
    const void* start,
    uint64_t count) noexcept;
__attribute__((visibility("default"))) __attribute__ ((__deprecated__)) int32_t tiledb_query_add_point_ranges(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    uint32_t dim_idx,
    const void* start,
    uint64_t count) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_get_relevant_fragment_num(
    tiledb_ctx_t* ctx,
    const tiledb_query_t* query,
    uint64_t* relevant_fragment_num) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_query_condition_alloc_set_membership(
    tiledb_ctx_t* ctx,
    const char* field_name,
    const void* data,
    uint64_t data_size,
    const void* offsets,
    uint64_t offests_size,
    tiledb_query_condition_op_t op,
    tiledb_query_condition_t** cond) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_query_condition_set_use_enumeration(
    tiledb_ctx_t* ctx,
    const tiledb_query_condition_t* cond,
    int use_enumeration) noexcept;
typedef enum {
 TILEDB_REASON_NONE = 0,
    TILEDB_REASON_USER_BUFFER_SIZE = 1,
    TILEDB_REASON_MEMORY_BUDGET = 2,

} tiledb_query_status_details_reason_t;
typedef struct tiledb_experimental_query_status_details_t {
  tiledb_query_status_details_reason_t
      incomplete_reason;
} tiledb_query_status_details_t;
__attribute__((visibility("default"))) int32_t tiledb_query_get_status_details(
    tiledb_ctx_t* ctx,
    tiledb_query_t* query,
    tiledb_query_status_details_t* status_details) noexcept;
__attribute__((visibility("default"))) capi_return_t tiledb_ctx_alloc_with_error(
    tiledb_config_t* config,
    tiledb_ctx_t** ctx,
    tiledb_error_t** error) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_array_consolidate_fragments(
    tiledb_ctx_t* ctx,
    const char* array_uri,
    const char** fragment_uris,
    const uint64_t num_fragments,
    tiledb_config_t* config) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_filestore_schema_create(
    tiledb_ctx_t* ctx,
    const char* uri,
    tiledb_array_schema_t** array_schema) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_filestore_uri_import(
    tiledb_ctx_t* ctx,
    const char* filestore_array_uri,
    const char* file_uri,
    tiledb_mime_type_t mime_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_filestore_uri_export(
    tiledb_ctx_t* ctx,
    const char* file_uri,
    const char* filestore_array_uri) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_filestore_buffer_import(
    tiledb_ctx_t* ctx,
    const char* filestore_array_uri,
    void* buf,
    size_t size,
    tiledb_mime_type_t mime_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_filestore_buffer_export(
    tiledb_ctx_t* ctx,
    const char* filestore_array_uri,
    size_t offset,
    void* buf,
    size_t size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_filestore_size(
    tiledb_ctx_t* ctx,
    const char* filestore_array_uri,
    size_t* size) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_mime_type_to_str(
    tiledb_mime_type_t mime_type, const char** str) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_mime_type_from_str(
    const char* str, tiledb_mime_type_t* mime_type) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_fragment_info_get_total_cell_num(
    tiledb_ctx_t* ctx,
    tiledb_fragment_info_t* fragment_info,
    uint64_t* count) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_consolidation_plan_create_with_mbr(
    tiledb_ctx_t* ctx,
    tiledb_array_t* array,
    uint64_t fragment_size,
    tiledb_consolidation_plan_t** consolidation_plan) noexcept;
__attribute__((visibility("default"))) void tiledb_consolidation_plan_free(
    tiledb_consolidation_plan_t** consolidation_plan) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_consolidation_plan_get_num_nodes(
    tiledb_ctx_t* ctx,
    tiledb_consolidation_plan_t* consolidation_plan,
    uint64_t* num_nodes) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_consolidation_plan_get_num_fragments(
    tiledb_ctx_t* ctx,
    tiledb_consolidation_plan_t* consolidation_plan,
    uint64_t node_index,
    uint64_t* num_fragments) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_consolidation_plan_get_fragment_uri(
    tiledb_ctx_t* ctx,
    tiledb_consolidation_plan_t* consolidation_plan,
    uint64_t node_index,
    uint64_t fragment_index,
    const char** uri) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_consolidation_plan_dump_json_str(
    tiledb_ctx_t* ctx,
    const tiledb_consolidation_plan_t* consolidation_plan,
    char** str) noexcept;
__attribute__((visibility("default"))) int32_t tiledb_consolidation_plan_free_json_str(char** str)
    noexcept;
}
