/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.libtiledb;

public class tiledbJNI {

  static {
    try {
      System.loadLibrary("tiledb");
    } catch (UnsatisfiedLinkError e) {
      System.err.println("Native code library failed to load. \n" + e);
      System.exit(1);
    }
  }

  public final static native int tiledb_query_submit_async_java(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, long jarg2, SWIGTYPE_p_tiledb_query_t jarg2_, Object jarg3);
  public final static native int tiledb_object_walk_java(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, String jarg2, int jarg3, Object jarg4);
  public final static native int tiledb_object_ls_java(long jarg1, SWIGTYPE_p_tiledb_ctx_t jarg1_, String jarg2, Object jarg3);
  public final static native long java_path_callback();
  public final static native long java_callback();

  public final static native long new_int32_tArray(int jarg1);
  public final static native void delete_int32_tArray(long jarg1);
  public final static native int int32_tArray_getitem(long jarg1, int32_tArray jarg1_, int jarg2);
  public final static native void int32_tArray_setitem(long jarg1, int32_tArray jarg1_, int jarg2, int jarg3);
  public final static native long int32_tArray_cast(long jarg1, int32_tArray jarg1_);
  public final static native long int32_tArray_frompointer(long jarg1);
  public final static native long new_int64_tArray(int jarg1);
  public final static native void delete_int64_tArray(long jarg1);
  public final static native long int64_tArray_getitem(long jarg1, int64_tArray jarg1_, int jarg2);
  public final static native void int64_tArray_setitem(long jarg1, int64_tArray jarg1_, int jarg2, long jarg3);
  public final static native long int64_tArray_cast(long jarg1, int64_tArray jarg1_);
  public final static native long int64_tArray_frompointer(long jarg1);
  public final static native long new_charArray(int jarg1);
  public final static native void delete_charArray(long jarg1);
  public final static native char charArray_getitem(long jarg1, charArray jarg1_, int jarg2);
  public final static native void charArray_setitem(long jarg1, charArray jarg1_, int jarg2, char jarg3);
  public final static native String charArray_cast(long jarg1, charArray jarg1_);
  public final static native long charArray_frompointer(String jarg1);
  public final static native long new_floatArray(int jarg1);
  public final static native void delete_floatArray(long jarg1);
  public final static native float floatArray_getitem(long jarg1, floatArray jarg1_, int jarg2);
  public final static native void floatArray_setitem(long jarg1, floatArray jarg1_, int jarg2, float jarg3);
  public final static native long floatArray_cast(long jarg1, floatArray jarg1_);
  public final static native long floatArray_frompointer(long jarg1);
  public final static native long new_doubleArray(int jarg1);
  public final static native void delete_doubleArray(long jarg1);
  public final static native double doubleArray_getitem(long jarg1, doubleArray jarg1_, int jarg2);
  public final static native void doubleArray_setitem(long jarg1, doubleArray jarg1_, int jarg2, double jarg3);
  public final static native long doubleArray_cast(long jarg1, doubleArray jarg1_);
  public final static native long doubleArray_frompointer(long jarg1);
  public final static native long new_int8_tArray(int jarg1);
  public final static native void delete_int8_tArray(long jarg1);
  public final static native byte int8_tArray_getitem(long jarg1, int8_tArray jarg1_, int jarg2);
  public final static native void int8_tArray_setitem(long jarg1, int8_tArray jarg1_, int jarg2, byte jarg3);
  public final static native long int8_tArray_cast(long jarg1, int8_tArray jarg1_);
  public final static native long int8_tArray_frompointer(long jarg1);
  public final static native long new_uint8_tArray(int jarg1);
  public final static native void delete_uint8_tArray(long jarg1);
  public final static native short uint8_tArray_getitem(long jarg1, uint8_tArray jarg1_, int jarg2);
  public final static native void uint8_tArray_setitem(long jarg1, uint8_tArray jarg1_, int jarg2, short jarg3);
  public final static native long uint8_tArray_cast(long jarg1, uint8_tArray jarg1_);
  public final static native long uint8_tArray_frompointer(long jarg1);
  public final static native long new_int16_tArray(int jarg1);
  public final static native void delete_int16_tArray(long jarg1);
  public final static native short int16_tArray_getitem(long jarg1, int16_tArray jarg1_, int jarg2);
  public final static native void int16_tArray_setitem(long jarg1, int16_tArray jarg1_, int jarg2, short jarg3);
  public final static native long int16_tArray_cast(long jarg1, int16_tArray jarg1_);
  public final static native long int16_tArray_frompointer(long jarg1);
  public final static native long new_uint16_tArray(int jarg1);
  public final static native void delete_uint16_tArray(long jarg1);
  public final static native int uint16_tArray_getitem(long jarg1, uint16_tArray jarg1_, int jarg2);
  public final static native void uint16_tArray_setitem(long jarg1, uint16_tArray jarg1_, int jarg2, int jarg3);
  public final static native long uint16_tArray_cast(long jarg1, uint16_tArray jarg1_);
  public final static native long uint16_tArray_frompointer(long jarg1);
  public final static native long new_uint32_tArray(int jarg1);
  public final static native void delete_uint32_tArray(long jarg1);
  public final static native long uint32_tArray_getitem(long jarg1, uint32_tArray jarg1_, int jarg2);
  public final static native void uint32_tArray_setitem(long jarg1, uint32_tArray jarg1_, int jarg2, long jarg3);
  public final static native long uint32_tArray_cast(long jarg1, uint32_tArray jarg1_);
  public final static native long uint32_tArray_frompointer(long jarg1);
  public final static native long new_uint64_tArray(int jarg1);
  public final static native void delete_uint64_tArray(long jarg1);
  public final static native java.math.BigInteger uint64_tArray_getitem(long jarg1, uint64_tArray jarg1_, int jarg2);
  public final static native void uint64_tArray_setitem(long jarg1, uint64_tArray jarg1_, int jarg2, java.math.BigInteger jarg3);
  public final static native long uint64_tArray_cast(long jarg1, uint64_tArray jarg1_);
  public final static native long uint64_tArray_frompointer(long jarg1);
  public final static native long new_charpArray(int jarg1);
  public final static native void delete_charpArray(long jarg1);
  public final static native String charpArray_getitem(long jarg1, int jarg2);
  public final static native void charpArray_setitem(long jarg1, int jarg2, String jarg3);
  public final static native long new_voidpArray(int jarg1);
  public final static native void delete_voidpArray(long jarg1);
  public final static native long voidpArray_getitem(long jarg1, int jarg2);
  public final static native void voidpArray_setitem(long jarg1, int jarg2, long jarg3);
  public final static native long new_intp();
  public final static native long copy_intp(int jarg1);
  public final static native void delete_intp(long jarg1);
  public final static native void intp_assign(long jarg1, int jarg2);
  public final static native int intp_value(long jarg1);
  public final static native long new_floatp();
  public final static native long copy_floatp(float jarg1);
  public final static native void delete_floatp(long jarg1);
  public final static native void floatp_assign(long jarg1, float jarg2);
  public final static native float floatp_value(long jarg1);
  public final static native long new_voidppp();
  public final static native long copy_voidppp(long jarg1);
  public final static native void delete_voidppp(long jarg1);
  public final static native void voidppp_assign(long jarg1, long jarg2);
  public final static native long voidppp_value(long jarg1);
  public final static native long new_intpp();
  public final static native long copy_intpp(long jarg1);
  public final static native void delete_intpp(long jarg1);
  public final static native void intpp_assign(long jarg1, long jarg2);
  public final static native long intpp_value(long jarg1);
  public final static native long new_charpp();
  public final static native long copy_charpp(String jarg1);
  public final static native void delete_charpp(long jarg1);
  public final static native void charpp_assign(long jarg1, String jarg2);
  public final static native String charpp_value(long jarg1);
  public final static native long new_uintp();
  public final static native long copy_uintp(long jarg1);
  public final static native void delete_uintp(long jarg1);
  public final static native void uintp_assign(long jarg1, long jarg2);
  public final static native long uintp_value(long jarg1);
  public final static native long new_ullp();
  public final static native long copy_ullp(java.math.BigInteger jarg1);
  public final static native void delete_ullp(long jarg1);
  public final static native void ullp_assign(long jarg1, java.math.BigInteger jarg2);
  public final static native java.math.BigInteger ullp_value(long jarg1);
  public final static native long new_tiledb_object_tp();
  public final static native long copy_tiledb_object_tp(int jarg1);
  public final static native void delete_tiledb_object_tp(long jarg1);
  public final static native void tiledb_object_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_object_tp_value(long jarg1);
  public final static native long new_tiledb_query_type_tp();
  public final static native long copy_tiledb_query_type_tp(int jarg1);
  public final static native void delete_tiledb_query_type_tp(long jarg1);
  public final static native void tiledb_query_type_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_query_type_tp_value(long jarg1);
  public final static native long new_tiledb_query_status_tp();
  public final static native long copy_tiledb_query_status_tp(int jarg1);
  public final static native void delete_tiledb_query_status_tp(long jarg1);
  public final static native void tiledb_query_status_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_query_status_tp_value(long jarg1);
  public final static native long new_tiledb_filesystem_tp();
  public final static native long copy_tiledb_filesystem_tp(int jarg1);
  public final static native void delete_tiledb_filesystem_tp(long jarg1);
  public final static native void tiledb_filesystem_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_filesystem_tp_value(long jarg1);
  public final static native long new_tiledb_datatype_tp();
  public final static native long copy_tiledb_datatype_tp(int jarg1);
  public final static native void delete_tiledb_datatype_tp(long jarg1);
  public final static native void tiledb_datatype_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_datatype_tp_value(long jarg1);
  public final static native long new_tiledb_array_type_tp();
  public final static native long copy_tiledb_array_type_tp(int jarg1);
  public final static native void delete_tiledb_array_type_tp(long jarg1);
  public final static native void tiledb_array_type_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_array_type_tp_value(long jarg1);
  public final static native long new_tiledb_layout_tp();
  public final static native long copy_tiledb_layout_tp(int jarg1);
  public final static native void delete_tiledb_layout_tp(long jarg1);
  public final static native void tiledb_layout_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_layout_tp_value(long jarg1);
  public final static native long new_tiledb_compressor_tp();
  public final static native long copy_tiledb_compressor_tp(int jarg1);
  public final static native void delete_tiledb_compressor_tp(long jarg1);
  public final static native void tiledb_compressor_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_compressor_tp_value(long jarg1);
  public final static native long new_tiledb_walk_order_tp();
  public final static native long copy_tiledb_walk_order_tp(int jarg1);
  public final static native void delete_tiledb_walk_order_tp(long jarg1);
  public final static native void tiledb_walk_order_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_walk_order_tp_value(long jarg1);
  public final static native long new_tiledb_vfs_mode_tp();
  public final static native long copy_tiledb_vfs_mode_tp(int jarg1);
  public final static native void delete_tiledb_vfs_mode_tp(long jarg1);
  public final static native void tiledb_vfs_mode_tp_assign(long jarg1, int jarg2);
  public final static native int tiledb_vfs_mode_tp_value(long jarg1);
  public final static native long new_tiledb_array_tpp();
  public final static native long copy_tiledb_array_tpp(long jarg1);
  public final static native void delete_tiledb_array_tpp(long jarg1);
  public final static native void tiledb_array_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_array_tpp_value(long jarg1);
  public final static native long new_tiledb_config_tpp();
  public final static native long copy_tiledb_config_tpp(long jarg1);
  public final static native void delete_tiledb_config_tpp(long jarg1);
  public final static native void tiledb_config_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_config_tpp_value(long jarg1);
  public final static native long new_tiledb_config_iter_tpp();
  public final static native long copy_tiledb_config_iter_tpp(long jarg1);
  public final static native void delete_tiledb_config_iter_tpp(long jarg1);
  public final static native void tiledb_config_iter_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_config_iter_tpp_value(long jarg1);
  public final static native long new_tiledb_ctx_tpp();
  public final static native long copy_tiledb_ctx_tpp(long jarg1);
  public final static native void delete_tiledb_ctx_tpp(long jarg1);
  public final static native void tiledb_ctx_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_ctx_tpp_value(long jarg1);
  public final static native long new_tiledb_error_tpp();
  public final static native long copy_tiledb_error_tpp(long jarg1);
  public final static native void delete_tiledb_error_tpp(long jarg1);
  public final static native void tiledb_error_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_error_tpp_value(long jarg1);
  public final static native long new_tiledb_attribute_tpp();
  public final static native long copy_tiledb_attribute_tpp(long jarg1);
  public final static native void delete_tiledb_attribute_tpp(long jarg1);
  public final static native void tiledb_attribute_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_attribute_tpp_value(long jarg1);
  public final static native long new_tiledb_array_schema_tpp();
  public final static native long copy_tiledb_array_schema_tpp(long jarg1);
  public final static native void delete_tiledb_array_schema_tpp(long jarg1);
  public final static native void tiledb_array_schema_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_array_schema_tpp_value(long jarg1);
  public final static native long new_tiledb_dimension_tpp();
  public final static native long copy_tiledb_dimension_tpp(long jarg1);
  public final static native void delete_tiledb_dimension_tpp(long jarg1);
  public final static native void tiledb_dimension_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_dimension_tpp_value(long jarg1);
  public final static native long new_tiledb_domain_tpp();
  public final static native long copy_tiledb_domain_tpp(long jarg1);
  public final static native void delete_tiledb_domain_tpp(long jarg1);
  public final static native void tiledb_domain_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_domain_tpp_value(long jarg1);
  public final static native long new_tiledb_query_tpp();
  public final static native long copy_tiledb_query_tpp(long jarg1);
  public final static native void delete_tiledb_query_tpp(long jarg1);
  public final static native void tiledb_query_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_query_tpp_value(long jarg1);
  public final static native long new_tiledb_kv_schema_tpp();
  public final static native long copy_tiledb_kv_schema_tpp(long jarg1);
  public final static native void delete_tiledb_kv_schema_tpp(long jarg1);
  public final static native void tiledb_kv_schema_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_kv_schema_tpp_value(long jarg1);
  public final static native long new_tiledb_kv_tpp();
  public final static native long copy_tiledb_kv_tpp(long jarg1);
  public final static native void delete_tiledb_kv_tpp(long jarg1);
  public final static native void tiledb_kv_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_kv_tpp_value(long jarg1);
  public final static native long new_tiledb_kv_item_tpp();
  public final static native long copy_tiledb_kv_item_tpp(long jarg1);
  public final static native void delete_tiledb_kv_item_tpp(long jarg1);
  public final static native void tiledb_kv_item_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_kv_item_tpp_value(long jarg1);
  public final static native long new_tiledb_kv_iter_tpp();
  public final static native long copy_tiledb_kv_iter_tpp(long jarg1);
  public final static native void delete_tiledb_kv_iter_tpp(long jarg1);
  public final static native void tiledb_kv_iter_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_kv_iter_tpp_value(long jarg1);
  public final static native long new_tiledb_vfs_tpp();
  public final static native long copy_tiledb_vfs_tpp(long jarg1);
  public final static native void delete_tiledb_vfs_tpp(long jarg1);
  public final static native void tiledb_vfs_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_vfs_tpp_value(long jarg1);
  public final static native long new_tiledb_vfs_fh_tpp();
  public final static native long copy_tiledb_vfs_fh_tpp(long jarg1);
  public final static native void delete_tiledb_vfs_fh_tpp(long jarg1);
  public final static native void tiledb_vfs_fh_tpp_assign(long jarg1, long jarg2);
  public final static native long tiledb_vfs_fh_tpp_value(long jarg1);
  public final static native int sizeOfInt32();
  public final static native int sizeOfInt64();
  public final static native int sizeOfChar();
  public final static native int sizeOfFloat();
  public final static native int sizeOfDouble();
  public final static native int sizeOfInt8();
  public final static native int sizeOfUint8();
  public final static native int sizeOfInt16();
  public final static native int sizeOfUint16();
  public final static native int sizeOfUint32();
  public final static native int sizeOfUint64();
  public final static native long newInt32ArraySet(int[] jarg1);
  public final static native long newInt64ArraySet(long[] jarg1);
  public final static native long newCharArraySet(String jarg1);
  public final static native long newFloatArraySet(float[] jarg1);
  public final static native long newDoubleArraySet(double[] jarg1);
  public final static native long newInt8ArraySet(byte[] jarg1);
  public final static native long newUint8ArraySet(short[] jarg1);
  public final static native long newInt16ArraySet(short[] jarg1);
  public final static native long newUint16ArraySet(int[] jarg1);
  public final static native long newUint32ArraySet(long[] jarg1);
  public final static native long newUint64ArraySet(long[] jarg1);
  public final static native int[] int32ArrayGet(long jarg1, int jarg2);
  public final static native long[] int64ArrayGet(long jarg1, int jarg2);
  public final static native String charArrayGet(long jarg1);
  public final static native float[] floatArrayGet(long jarg1, int jarg2);
  public final static native double[] doubleArrayGet(long jarg1, int jarg2);
  public final static native byte[] int8ArrayGet(long jarg1, int jarg2);
  public final static native short[] uint8ArrayGet(long jarg1, int jarg2);
  public final static native short[] int16ArrayGet(long jarg1, int jarg2);
  public final static native int[] uint16ArrayGet(long jarg1, int jarg2);
  public final static native long[] uint32ArrayGet(long jarg1, int jarg2);
  public final static native long[] uint64ArrayGet(long jarg1, int jarg2);
  public final static native String tiledb_coords();
  public final static native long tiledb_var_num();
  public final static native long tiledb_max_path();
  public final static native java.math.BigInteger tiledb_datatype_size(int jarg1);
  public final static native java.math.BigInteger tiledb_offset_size();
  public final static native void tiledb_version(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_error_message(long jarg1, long jarg2);
  public final static native void tiledb_error_free(long jarg1);
  public final static native int tiledb_config_alloc(long jarg1, long jarg2);
  public final static native void tiledb_config_free(long jarg1);
  public final static native int tiledb_config_set(long jarg1, String jarg2, String jarg3, long jarg4);
  public final static native int tiledb_config_get(long jarg1, String jarg2, long jarg3, long jarg4);
  public final static native int tiledb_config_load_from_file(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_config_unset(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_config_save_to_file(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_config_iter_alloc(long jarg1, String jarg2, long jarg3, long jarg4);
  public final static native int tiledb_config_iter_reset(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native void tiledb_config_iter_free(long jarg1);
  public final static native int tiledb_config_iter_here(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_config_iter_next(long jarg1, long jarg2);
  public final static native int tiledb_config_iter_done(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_ctx_alloc(long jarg1, long jarg2);
  public final static native void tiledb_ctx_free(long jarg1);
  public final static native int tiledb_ctx_get_config(long jarg1, long jarg2);
  public final static native int tiledb_ctx_get_last_error(long jarg1, long jarg2);
  public final static native int tiledb_ctx_is_supported_fs(long jarg1, int jarg2, long jarg3);
  public final static native int tiledb_ctx_cancel_tasks(long jarg1);
  public final static native int tiledb_group_create(long jarg1, String jarg2);
  public final static native int tiledb_attribute_alloc(long jarg1, String jarg2, int jarg3, long jarg4);
  public final static native void tiledb_attribute_free(long jarg1);
  public final static native int tiledb_attribute_set_compressor(long jarg1, long jarg2, int jarg3, int jarg4);
  public final static native int tiledb_attribute_set_cell_val_num(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_attribute_get_name(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_attribute_get_type(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_attribute_get_compressor(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_attribute_get_cell_val_num(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_attribute_get_cell_size(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_attribute_dump(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_domain_alloc(long jarg1, long jarg2);
  public final static native void tiledb_domain_free(long jarg1);
  public final static native int tiledb_domain_get_type(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_domain_get_ndim(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_domain_add_dimension(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_domain_get_dimension_from_index(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_domain_get_dimension_from_name(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native int tiledb_domain_dump(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_dimension_alloc(long jarg1, String jarg2, int jarg3, long jarg4, long jarg5, long jarg6);
  public final static native void tiledb_dimension_free(long jarg1);
  public final static native int tiledb_dimension_get_name(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_dimension_get_type(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_dimension_get_domain(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_dimension_get_tile_extent(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_dimension_dump(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_alloc(long jarg1, int jarg2, long jarg3);
  public final static native void tiledb_array_schema_free(long jarg1);
  public final static native int tiledb_array_schema_add_attribute(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_set_domain(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_set_capacity(long jarg1, long jarg2, java.math.BigInteger jarg3);
  public final static native int tiledb_array_schema_set_cell_order(long jarg1, long jarg2, int jarg3);
  public final static native int tiledb_array_schema_set_tile_order(long jarg1, long jarg2, int jarg3);
  public final static native int tiledb_array_schema_set_coords_compressor(long jarg1, long jarg2, int jarg3, int jarg4);
  public final static native int tiledb_array_schema_set_offsets_compressor(long jarg1, long jarg2, int jarg3, int jarg4);
  public final static native int tiledb_array_schema_check(long jarg1, long jarg2);
  public final static native int tiledb_array_schema_load(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_array_schema_get_array_type(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_get_capacity(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_get_cell_order(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_get_coords_compressor(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_array_schema_get_offsets_compressor(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_array_schema_get_domain(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_get_tile_order(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_get_attribute_num(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_schema_get_attribute_from_index(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_array_schema_get_attribute_from_name(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native int tiledb_array_schema_dump(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_query_alloc(long jarg1, long jarg2, int jarg3, long jarg4);
  public final static native int tiledb_query_set_subarray(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_query_set_buffer(long jarg1, long jarg2, String jarg3, long jarg4, long jarg5);
  public final static native int tiledb_query_set_buffer_var(long jarg1, long jarg2, String jarg3, long jarg4, long jarg5, long jarg6, long jarg7);
  public final static native int tiledb_query_set_layout(long jarg1, long jarg2, int jarg3);
  public final static native int tiledb_query_finalize(long jarg1, long jarg2);
  public final static native void tiledb_query_free(long jarg1);
  public final static native int tiledb_query_submit(long jarg1, long jarg2);
  public final static native int tiledb_query_submit_async(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_query_has_results(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_query_get_status(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_query_get_type(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_alloc(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_array_open(long jarg1, long jarg2, int jarg3);
  public final static native int tiledb_array_is_open(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_reopen(long jarg1, long jarg2);
  public final static native int tiledb_array_close(long jarg1, long jarg2);
  public final static native void tiledb_array_free(long jarg1);
  public final static native int tiledb_array_get_schema(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_get_query_type(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_array_create(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_array_consolidate(long jarg1, String jarg2);
  public final static native int tiledb_array_get_non_empty_domain(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_array_max_buffer_size(long jarg1, long jarg2, String jarg3, long jarg4, long jarg5);
  public final static native int tiledb_array_max_buffer_size_var(long jarg1, long jarg2, String jarg3, long jarg4, long jarg5, long jarg6);
  public final static native int tiledb_object_type(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_object_remove(long jarg1, String jarg2);
  public final static native int tiledb_object_move(long jarg1, String jarg2, String jarg3);
  public final static native int tiledb_object_walk(long jarg1, String jarg2, int jarg3, long jarg4, long jarg5);
  public final static native int tiledb_object_ls(long jarg1, String jarg2, long jarg3, long jarg4);
  public final static native int tiledb_kv_schema_alloc(long jarg1, long jarg2);
  public final static native void tiledb_kv_schema_free(long jarg1);
  public final static native int tiledb_kv_schema_add_attribute(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_schema_set_capacity(long jarg1, long jarg2, java.math.BigInteger jarg3);
  public final static native int tiledb_kv_schema_check(long jarg1, long jarg2);
  public final static native int tiledb_kv_schema_load(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_kv_schema_get_capacity(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_schema_get_attribute_num(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_schema_get_attribute_from_index(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_kv_schema_get_attribute_from_name(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native int tiledb_kv_schema_dump(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_item_alloc(long jarg1, long jarg2);
  public final static native void tiledb_kv_item_free(long jarg1);
  public final static native int tiledb_kv_item_set_key(long jarg1, long jarg2, long jarg3, int jarg4, java.math.BigInteger jarg5);
  public final static native int tiledb_kv_item_set_value(long jarg1, long jarg2, String jarg3, long jarg4, int jarg5, java.math.BigInteger jarg6);
  public final static native int tiledb_kv_item_get_key(long jarg1, long jarg2, long jarg3, long jarg4, long jarg5);
  public final static native int tiledb_kv_item_get_value(long jarg1, long jarg2, String jarg3, long jarg4, long jarg5, long jarg6);
  public final static native int tiledb_kv_create(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_kv_consolidate(long jarg1, String jarg2);
  public final static native int tiledb_kv_set_max_buffered_items(long jarg1, long jarg2, java.math.BigInteger jarg3);
  public final static native int tiledb_kv_alloc(long jarg1, String jarg2, long jarg3);
  public final static native int tiledb_kv_open(long jarg1, long jarg2, long jarg3, long jarg4);
  public final static native int tiledb_kv_reopen(long jarg1, long jarg2);
  public final static native int tiledb_kv_close(long jarg1, long jarg2);
  public final static native void tiledb_kv_free(long jarg1);
  public final static native int tiledb_kv_get_schema(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_is_dirty(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_add_item(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_flush(long jarg1, long jarg2);
  public final static native int tiledb_kv_get_item(long jarg1, long jarg2, long jarg3, int jarg4, java.math.BigInteger jarg5, long jarg6);
  public final static native int tiledb_kv_has_key(long jarg1, long jarg2, long jarg3, int jarg4, java.math.BigInteger jarg5, long jarg6);
  public final static native int tiledb_kv_iter_alloc(long jarg1, long jarg2, long jarg3);
  public final static native void tiledb_kv_iter_free(long jarg1);
  public final static native int tiledb_kv_iter_here(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_iter_next(long jarg1, long jarg2);
  public final static native int tiledb_kv_iter_done(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_kv_iter_reset(long jarg1, long jarg2);
  public final static native int tiledb_vfs_alloc(long jarg1, long jarg2, long jarg3);
  public final static native void tiledb_vfs_free(long jarg1);
  public final static native int tiledb_vfs_get_config(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_vfs_create_bucket(long jarg1, long jarg2, String jarg3);
  public final static native int tiledb_vfs_remove_bucket(long jarg1, long jarg2, String jarg3);
  public final static native int tiledb_vfs_empty_bucket(long jarg1, long jarg2, String jarg3);
  public final static native int tiledb_vfs_is_empty_bucket(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native int tiledb_vfs_is_bucket(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native int tiledb_vfs_create_dir(long jarg1, long jarg2, String jarg3);
  public final static native int tiledb_vfs_is_dir(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native int tiledb_vfs_remove_dir(long jarg1, long jarg2, String jarg3);
  public final static native int tiledb_vfs_is_file(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native int tiledb_vfs_remove_file(long jarg1, long jarg2, String jarg3);
  public final static native int tiledb_vfs_file_size(long jarg1, long jarg2, String jarg3, long jarg4);
  public final static native int tiledb_vfs_move_file(long jarg1, long jarg2, String jarg3, String jarg4);
  public final static native int tiledb_vfs_move_dir(long jarg1, long jarg2, String jarg3, String jarg4);
  public final static native int tiledb_vfs_open(long jarg1, long jarg2, String jarg3, int jarg4, long jarg5);
  public final static native int tiledb_vfs_close(long jarg1, long jarg2);
  public final static native int tiledb_vfs_read(long jarg1, long jarg2, java.math.BigInteger jarg3, long jarg4, java.math.BigInteger jarg5);
  public final static native int tiledb_vfs_write(long jarg1, long jarg2, long jarg3, java.math.BigInteger jarg4);
  public final static native int tiledb_vfs_sync(long jarg1, long jarg2);
  public final static native void tiledb_vfs_fh_free(long jarg1);
  public final static native int tiledb_vfs_fh_is_closed(long jarg1, long jarg2, long jarg3);
  public final static native int tiledb_vfs_touch(long jarg1, long jarg2, String jarg3);
  public final static native int tiledb_uri_to_path(long jarg1, String jarg2, String jarg3, long jarg4);
  public final static native int tiledb_stats_enable();
  public final static native int tiledb_stats_disable();
  public final static native int tiledb_stats_reset();
  public final static native int tiledb_stats_dump(long jarg1);
  public final static native int tiledb_dimension_dump_stdout(long jarg1, long jarg2);
  public final static native int tiledb_kv_schema_dump_stdout(long jarg1, long jarg2);
  public final static native int tiledb_attribute_dump_stdout(long jarg1, long jarg2);
  public final static native int tiledb_domain_dump_stdout(long jarg1, long jarg2);
  public final static native int tiledb_array_schema_dump_stdout(long jarg1, long jarg2);
  public final static native long derefVoid(long jarg1);
  public final static native void print_upon_completion(long jarg1);
  public final static native int print_path(String jarg1, int jarg2, long jarg3);
  public final static native long native_callback();
  public final static native long native_walk_callback();
  public final static native int tiledb_query_submit_async_jc(long jarg1, long jarg2, long jarg3, Object jarg4);
  public final static native int tiledb_object_walk_jc(long jarg1, String jarg2, int jarg3, long jarg4, long jarg5);
}
