/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 TileDB, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @section DESCRIPTION
 *
 * This example explores the C API for the array schema.
 *
 * Simply run the following to make it work.
 *
 * ```
 * $ java -cp build/libs/tiledb-jni-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbArraySchema
 * First dump:
 * - Array type: sparse
 * - Cell order: row-major
 * - Tile order: row-major
 * - Capacity: 10000
 * - Coordinates compressor: BLOSC_ZSTD
 * - Coordinates compression level: -1
 *
 * Second dump:
 * - Array type: sparse
 * - Cell order: col-major
 * - Tile order: row-major
 * - Capacity: 10
 * - Coordinates compressor: ZSTD
 * - Coordinates compression level: 4
 *
 * Third dump:
 * - Array type: sparse
 * - Cell order: col-major
 * - Tile order: row-major
 * - Capacity: 10
 * - Coordinates compressor: ZSTD
 * - Coordinates compression level: 4
 *
 * === Domain ===
 * - Dimensions type: UINT64
 *
 * ### Dimension ###
 * - Name: <anonymous>
 * - Domain: [1,1000]
 * - Tile extent: 10
 *
 * ### Dimension ###
 * - Name: d2
 * - Domain: [101,10000]
 * - Tile extent: 100
 *
 * ### Attribute ###
 * - Name: <anonymous>
 * - Type: INT32
 * - Compressor: NO_COMPRESSION
 * - Compression level: -1
 * - Cell val num: 3
 *
 * ### Attribute ###
 * - Name: a2
 * - Type: FLOAT32
 * - Compressor: GZIP
 * - Compression level: -1
 * - Cell val num: 1
 *
 * From getters:
 * - Array type: sparse
 * - Cell order: col-major
 * - Tile order: row-major
 * - Capacity: 10
 * - Coordinates compressor: (ZSTD, 4)
 * - Offsets compressor: (BLOSC_LZ, 5)
 *
 * Array schema attribute names:
 * * __attr
 * * a2
 *
 * === Domain ===
 * - Dimensions type: UINT64
 *
 * ### Dimension ###
 * - Name: <anonymous>
 * - Domain: [1,1000]
 * - Tile extent: 10
 *
 * ### Dimension ###
 * - Name: d2
 * - Domain: [101,10000]
 * - Tile extent: 100
 *
 * Array schema dimension names:
 * * __dim_0
 * * d2
 * ```
 *
 */

package examples.io.tiledb.libtiledb;

import java.math.BigInteger;

import io.tiledb.libtiledb.*;

public class TiledbArraySchema {

  public static void main(String[] args) {
    // Create TileDB context
    SWIGTYPE_p_p_tiledb_ctx_t ctxpp = tiledb.new_tiledb_ctx_tpp();
    tiledb.tiledb_ctx_alloc(null, ctxpp);
    SWIGTYPE_p_tiledb_ctx_t ctx = tiledb.tiledb_ctx_tpp_value(ctxpp);

    // Create array schema
    SWIGTYPE_p_p_tiledb_array_schema_t array_schemapp = tiledb.new_tiledb_array_schema_tpp();
    tiledb.tiledb_array_schema_alloc(ctx,
        tiledb_array_type_t.TILEDB_SPARSE, array_schemapp);
    SWIGTYPE_p_tiledb_array_schema_t array_schema = tiledb.tiledb_array_schema_tpp_value(array_schemapp);

    // Print array schema contents
    System.out.println("First dump:");
    tiledb.tiledb_array_schema_dump_stdout(ctx, array_schema);

    // Set some values
    tiledb.tiledb_array_schema_set_tile_order(ctx, array_schema,
        tiledb_layout_t.TILEDB_ROW_MAJOR);
    tiledb.tiledb_array_schema_set_cell_order(ctx, array_schema,
        tiledb_layout_t.TILEDB_COL_MAJOR);
    tiledb.tiledb_array_schema_set_capacity(ctx, array_schema,
        new BigInteger("10"));
    tiledb.tiledb_array_schema_set_coords_compressor(ctx, array_schema,
        tiledb_compressor_t.TILEDB_ZSTD, 4);
    tiledb.tiledb_array_schema_set_offsets_compressor(ctx, array_schema,
        tiledb_compressor_t.TILEDB_BLOSC_LZ, 5);

    // Print array schema contents again
    System.out.println("Second dump:");
    tiledb.tiledb_array_schema_dump_stdout(ctx, array_schema);

    // Create getDimensions

    long[] d1_domain_ = {1, 1000};
    uint64_tArray d1_domain = Utils.newUint64Array(d1_domain_);
    long[] d1_extents_ = {10};
    uint64_tArray d1_extent = Utils.newUint64Array(d1_extents_);
    SWIGTYPE_p_p_tiledb_dimension_t d1pp = tiledb.new_tiledb_dimension_tpp();
    tiledb.tiledb_dimension_alloc(ctx, "",
        tiledb_datatype_t.TILEDB_UINT64,
        PointerUtils.toVoid(d1_domain), 
	PointerUtils.toVoid(d1_extent),
	d1pp);
    SWIGTYPE_p_tiledb_dimension_t d1 = tiledb.tiledb_dimension_tpp_value(d1pp);

    long[] d2_domain_ = {101, 10000};
    uint64_tArray d2_domain = Utils.newUint64Array(d2_domain_);
    long[] d2_extents_ = {10};
    uint64_tArray d2_extent = Utils.newUint64Array(d2_extents_);
    SWIGTYPE_p_p_tiledb_dimension_t d2pp = tiledb.new_tiledb_dimension_tpp();
    tiledb.tiledb_dimension_alloc(ctx, "d2",
        tiledb_datatype_t.TILEDB_UINT64,
        PointerUtils.toVoid(d2_domain), 
	PointerUtils.toVoid(d2_extent),
	d2pp);
    SWIGTYPE_p_tiledb_dimension_t d2 = tiledb.tiledb_dimension_tpp_value(d2pp);

    // Create and set getDomain
    SWIGTYPE_p_p_tiledb_domain_t domainpp = tiledb.new_tiledb_domain_tpp();
    tiledb.tiledb_domain_alloc(ctx, domainpp);
    SWIGTYPE_p_tiledb_domain_t domain = tiledb.tiledb_domain_tpp_value(domainpp);
    tiledb.tiledb_domain_add_dimension(ctx, domain, d1);
    tiledb.tiledb_array_schema_set_domain(ctx, array_schema, domain);

    // Create and add getAttributes

    SWIGTYPE_p_p_tiledb_attribute_t a1pp = tiledb.new_tiledb_attribute_tpp();
    tiledb.tiledb_attribute_alloc(ctx, "",
        tiledb_datatype_t.TILEDB_INT32, a1pp);
    SWIGTYPE_p_tiledb_attribute_t a1 = tiledb.tiledb_attribute_tpp_value(a1pp);
    SWIGTYPE_p_p_tiledb_attribute_t a2pp = tiledb.new_tiledb_attribute_tpp();
    tiledb.tiledb_attribute_alloc(ctx, "a2",
        tiledb_datatype_t.TILEDB_FLOAT32, a2pp);
    SWIGTYPE_p_tiledb_attribute_t a2 = tiledb.tiledb_attribute_tpp_value(a2pp);

    tiledb.tiledb_attribute_set_compressor(ctx, a1,
        tiledb_compressor_t.TILEDB_GZIP, -1);
    tiledb.tiledb_attribute_set_cell_val_num(ctx, a1, 1);

    tiledb.tiledb_attribute_set_cell_val_num(ctx, a1, 3);
    tiledb.tiledb_attribute_set_compressor(ctx, a2,
        tiledb_compressor_t.TILEDB_GZIP, -1);
    tiledb.tiledb_array_schema_add_attribute(ctx, array_schema, a1);
    tiledb.tiledb_array_schema_add_attribute(ctx, array_schema, a2);

    // Print array schema contents again
    System.out.println("Third dump:");
    tiledb.tiledb_array_schema_dump_stdout(ctx, array_schema);

    // Get some values using getters
    SWIGTYPE_p_tiledb_array_type_t array_typep = tiledb
        .new_tiledb_array_type_tp();
    SWIGTYPE_p_unsigned_long_long capacity = tiledb.new_ullp();
    SWIGTYPE_p_tiledb_compressor_t coords_compressor = tiledb
        .new_tiledb_compressor_tp(), offsets_compressor = tiledb
        .new_tiledb_compressor_tp();
    SWIGTYPE_p_int coords_compression_level = tiledb.new_intp(), offsets_compression_level = tiledb
        .new_intp();
    SWIGTYPE_p_tiledb_layout_t tile_order = tiledb.new_tiledb_layout_tp(), cell_order = tiledb
        .new_tiledb_layout_tp();
    tiledb.tiledb_array_schema_get_array_type(ctx, array_schema,
        array_typep);
    tiledb.tiledb_array_schema_get_capacity(ctx, array_schema, capacity);
    tiledb.tiledb_array_schema_get_tile_order(ctx, array_schema, tile_order);
    tiledb.tiledb_array_schema_get_cell_order(ctx, array_schema, cell_order);
    tiledb.tiledb_array_schema_get_coords_compressor(ctx, array_schema,
        coords_compressor, coords_compression_level);
    tiledb.tiledb_array_schema_get_offsets_compressor(ctx, array_schema,
        offsets_compressor, offsets_compression_level);

    // Print from getters
    System.out.printf("\nFrom getters:\n");
    System.out
        .printf("- Array getType: %s\n",
            (tiledb.tiledb_array_type_tp_value(array_typep) == tiledb_array_type_t.TILEDB_DENSE) ? "dense"
                : "sparse");
    System.out
        .printf("- Cell order: %s\n",
            (tiledb.tiledb_layout_tp_value(cell_order) == tiledb_layout_t.TILEDB_ROW_MAJOR) ? "row-major"
                : "col-major");
    System.out
        .printf("- Tile order: %s\n",
            (tiledb.tiledb_layout_tp_value(tile_order) == tiledb_layout_t.TILEDB_ROW_MAJOR) ? "row-major"
                : "col-major");
    System.out.println("- Capacity: " + tiledb.ullp_value(capacity));
    System.out
        .printf("- Coordinates compressor: %s",
            (tiledb.tiledb_compressor_tp_value(coords_compressor) == tiledb_compressor_t.TILEDB_ZSTD) ? "(ZSTD"
                : "error");
    System.out.printf(", %d)\n",
        tiledb.intp_value(coords_compression_level));
    System.out
        .printf("- Offsets compressor: %s",
            (tiledb.tiledb_compressor_tp_value(offsets_compressor) == tiledb_compressor_t.TILEDB_BLOSC_LZ) ? "(BLOSC"
                : "error");
    System.out.printf(", %d)\n",
        tiledb.intp_value(offsets_compression_level));

    // Print the attribute names
    System.out.printf("\nArray schema attribute names: \n");
    SWIGTYPE_p_unsigned_int nattrp = tiledb.new_uintp();
    tiledb.tiledb_array_schema_get_attribute_num(ctx, array_schema, nattrp);
    SWIGTYPE_p_p_tiledb_attribute_t attrpp = tiledb.new_tiledb_attribute_tpp();
    SWIGTYPE_p_p_char attr_namepp = tiledb.new_charpp();
    long nattr = tiledb.uintp_value(nattrp);
    for (long i = 0; i < nattr; i++) {
      tiledb.tiledb_array_schema_get_attribute_from_index(ctx,
          array_schema, i, attrpp);

      tiledb.tiledb_attribute_get_name(ctx,
          tiledb.tiledb_attribute_tpp_value(attrpp), attr_namepp);
      System.out.printf("* %s\n", tiledb.charpp_value(attr_namepp));
      tiledb.tiledb_attribute_free(attrpp);
    }
    System.out.printf("\n");

    // Get and print getDomain
    SWIGTYPE_p_p_tiledb_domain_t got_domainpp = tiledb.new_tiledb_domain_tpp();
    tiledb.tiledb_array_schema_get_domain(ctx, array_schema, got_domainpp);
    SWIGTYPE_p_tiledb_domain_t got_domain = tiledb.tiledb_domain_tpp_value(got_domainpp);
    tiledb.tiledb_domain_dump_stdout(ctx, got_domain);

    // Print the dimension names
    System.out.printf("\nArray schema dimension names: \n");
    SWIGTYPE_p_unsigned_int ndimp = tiledb.new_uintp();
    tiledb.tiledb_domain_get_ndim(ctx, domain, ndimp);
    long ndim = tiledb.uintp_value(ndimp);
    SWIGTYPE_p_p_tiledb_dimension_t dimpp = tiledb.new_tiledb_dimension_tpp();
    SWIGTYPE_p_p_char dim_namepp = tiledb.new_charpp();
    for (long i = 0; i < ndim; i++) {
      tiledb.tiledb_domain_get_dimension_from_index(ctx, domain, i, dimpp);
      SWIGTYPE_p_tiledb_dimension_t dim = tiledb.tiledb_dimension_tpp_value(dimpp);
      tiledb.tiledb_dimension_get_name(ctx, dim, dim_namepp);
      System.out.printf("* %s\n", tiledb.charpp_value(dim_namepp));
      tiledb.tiledb_dimension_free(dimpp);
    }

    // Clean up
    tiledb.tiledb_attribute_free(a1pp);
    tiledb.tiledb_attribute_free(a2pp);
    tiledb.tiledb_dimension_free(d1pp);
    tiledb.tiledb_dimension_free(d2pp);
    tiledb.tiledb_domain_free(domainpp);
    tiledb.tiledb_domain_free(got_domainpp);
    tiledb.tiledb_array_schema_free(array_schemapp);
    tiledb.tiledb_ctx_free(ctxpp);
  }
}
