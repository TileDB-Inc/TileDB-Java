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
 */

package examples.io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.*;
import static io.tiledb.java.api.CompressorType.*;
import static io.tiledb.java.api.Layout.*;

import io.tiledb.java.api.*;
import java.util.List;
import java.util.Map;

public class ArraySchemaExample {
  public static void main(String[] args) throws Exception {
    // Create TileDB context
    Context ctx = new Context();

    // Create array schema
    ArraySchema schema = new ArraySchema(ctx, TILEDB_SPARSE);
    schema.setCapacity(10);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_COL_MAJOR);
    schema.setCoordsCompressor(new Compressor(TILEDB_ZSTD, 4));
    schema.setOffsetsCompressor(new Compressor(TILEDB_GZIP, 5));

    // Create getDimensions
    Dimension<Integer> d1 =
        new Dimension<Integer>(ctx, "d1", Integer.class, new Pair<Integer, Integer>(1, 1000), 10);
    Dimension<Integer> d2 =
        new Dimension<Integer>(
            ctx, "d2", Integer.class, new Pair<Integer, Integer>(101, 10000), 100);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(d1);
    domain.addDimension(d2);
    schema.setDomain(domain);

    // Create and add getAttributes
    Attribute a1 = new Attribute(ctx, "a1", Integer.class);
    a1.setCellValNum(3);
    Attribute a2 = new Attribute(ctx, "a2", Float.class);
    a2.setCompressor(new Compressor(TILEDB_GZIP, -1));
    schema.addAttribute(a1);
    schema.addAttribute(a2);

    try {
      schema.check();
    } catch (Exception e) {
      e.printStackTrace();
    }

    schema.dump();

    // Print from getters
    System.out.println(
        "\nFrom getters:"
            + "\n- Array getType: "
            + ((schema.getArrayType() == TILEDB_DENSE) ? "dense" : "sparse")
            + "\n- Cell order: "
            + (schema.getCellOrder() == TILEDB_ROW_MAJOR ? "row-major" : "col-major")
            + "\n- Tile order: "
            + (schema.getTileOrder() == TILEDB_ROW_MAJOR ? "row-major" : "col-major")
            + "\n- Capacity: "
            + schema.getCapacity()
            + "\n- Coordinates compressor: "
            + schema.getCoordsCompressor()
            + "\n- Offsets compressor: "
            + schema.getOffsetsCompressor());

    // Print the attribute names
    System.out.println("\n\nArray schema attribute names: ");
    for (Map.Entry<String, Attribute> a : schema.getAttributes().entrySet()) {
      System.out.println("* " + a.getKey());
    }

    // Print getDomain
    schema.getDomain().dump();

    // Print the dimension names using getters
    System.out.println("\nArray schema dimension names: ");
    for (Dimension d : (List<Dimension>) schema.getDomain().getDimensions()) {
      System.out.println(
          "* " + d.getName() + " domain: " + d.domainToStr() + " extent: " + d.tileExtentToStr());
    }
  }
}
