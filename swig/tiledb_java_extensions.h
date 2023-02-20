/**
 * @file   tiledb.h
 *
 * @section LICENSE
 *
 * The MIT License
 *
 * @copyright Copyright (c) 2017-2018 TileDB, Inc.
 * @copyright Copyright (c) 2016 MIT and Intel Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @section DESCRIPTION
 *
 * This file declares the C API for TileDB.
 */

#ifndef TILEDB_OLD_H
#define TILEDB_OLD_H

#include <cstdint>
#include <cstdio>

#include "tiledb/tiledb.h"

/* ********************************* */
/*               MACROS              */
/* ********************************* */

#ifdef __cplusplus
extern "C" {
#endif
    
	TILEDB_EXPORT int tiledb_dimension_dump_stdout(
    	tiledb_ctx_t* ctx, const tiledb_dimension_t* dim){
      int ret = tiledb_dimension_dump(ctx, dim, stdout);
      fflush(stdout);
      return ret;
    };
    
	TILEDB_EXPORT int tiledb_attribute_dump_stdout(
    	tiledb_ctx_t* ctx, const tiledb_attribute_t* attr){
      int ret = tiledb_attribute_dump(ctx, attr, stdout);
      fflush(stdout);
      return ret;
    };
    
	TILEDB_EXPORT int tiledb_domain_dump_stdout(
    	tiledb_ctx_t* ctx, const tiledb_domain_t* domain){
      int ret = tiledb_domain_dump(ctx, domain, stdout);
      fflush(stdout);
      return ret;
    };
    
    TILEDB_EXPORT int tiledb_array_schema_dump_stdout(
    	tiledb_ctx_t* ctx, const tiledb_array_schema_t* array_schema){
      int ret = tiledb_array_schema_dump(ctx, array_schema, stdout);
      fflush(stdout);
      return ret;
    };

    TILEDB_EXPORT int tiledb_stats_dump_stdout(){
      int ret = tiledb_stats_dump(stdout);
      fflush(stdout);
      return ret;
    };

    TILEDB_EXPORT int tiledb_fragment_info_dump_stdout(
        	tiledb_ctx_t* ctx, const tiledb_fragment_info_t* fragment_info){
          int ret = tiledb_fragment_info_dump(ctx, fragment_info, stdout);
          fflush(stdout);
          return ret;
        };

    TILEDB_EXPORT int tiledb_dimension_dump_file(
    	tiledb_ctx_t* ctx, const tiledb_dimension_t* dim, const char* filename){
      FILE *out = (fopen(filename, "w"));
      if(out == NULL)
        return TILEDB_ERR;

      int ret = tiledb_dimension_dump(ctx, dim, out);
      fclose(out);

      return ret;
    };

    TILEDB_EXPORT int tiledb_attribute_dump_file(
    	tiledb_ctx_t* ctx, const tiledb_attribute_t* attr, const char* filename){
      FILE *out = (fopen(filename, "w"));
      if(out == NULL)
        return TILEDB_ERR;

      int ret = tiledb_attribute_dump(ctx, attr, out);
      fclose(out);

      return ret;
    };

    TILEDB_EXPORT int tiledb_domain_dump_file(
    	tiledb_ctx_t* ctx, const tiledb_domain_t* domain, const char* filename){
      FILE *out = (fopen(filename, "w"));
      if(out == NULL)
        return TILEDB_ERR;

      int ret = tiledb_domain_dump(ctx, domain, out);
      fclose(out);

      return ret;
    };

    TILEDB_EXPORT int tiledb_array_schema_dump_file(
    	tiledb_ctx_t* ctx, const tiledb_array_schema_t* array_schema, const char* filename){
      FILE *out = (fopen(filename, "w"));
      if(out == NULL)
        return TILEDB_ERR;

      int ret = tiledb_array_schema_dump(ctx, array_schema, out);
      fclose(out);

      return ret;
    };

    TILEDB_EXPORT int tiledb_stats_dump_file(const char* filename){
      FILE *out = (fopen(filename, "w"));
      if(out == NULL)
        return TILEDB_ERR;

      int ret = tiledb_stats_dump(out);
      fclose(out);

      return ret;
    };
    
    TILEDB_EXPORT void * derefVoid(void** in){
    	return *in;
    };


    TILEDB_EXPORT void print_upon_completion(void* s) {
      printf("%s\n", (char*)s);
      fflush(stdout);
    }

    TILEDB_EXPORT int print_path(const char* path, tiledb_object_t type, void* data) {
      // Simply print the path and type
      (void)data;
      printf("%s ", path);
      switch (type) {
        case TILEDB_ARRAY:
          printf("ARRAY");
          break;
        case TILEDB_GROUP:
          printf("GROUP");
          break;
        default:
          printf("INVALID");
      }
      printf("\n");
      fflush(stdout);

      // Always iterate till the end
      return 1;
    }

    TILEDB_EXPORT void (*native_callback())(void*){
        return print_upon_completion;
    }

    TILEDB_EXPORT int (*native_walk_callback())(const char*, tiledb_object_t, void *){
        return print_path;
    }

    TILEDB_EXPORT int tiledb_object_walk_jc(
        tiledb_ctx_t* ctx,
        const char* path,
        tiledb_walk_order_t order,
        int (*callback)(const char*, tiledb_object_t, void*),
        void* callback_data){
        return tiledb_object_walk(ctx, path, order, callback, (void *) &callback_data);
    }

#ifdef __cplusplus
}
#endif

#endif  // TILEDB_OLD_H
