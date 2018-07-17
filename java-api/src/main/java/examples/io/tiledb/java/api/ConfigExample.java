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

import io.tiledb.java.api.Config;
import io.tiledb.java.api.Context;
import io.tiledb.java.api.TileDBError;
import io.tiledb.libtiledb.tiledb;

import java.util.Map;

public class ConfigExample {

  public static void main(String[] args) throws Exception {
    // Create TileDB context
    Config config = new Config();

    // Print the default config parameters
    System.out.println( "Default settings:\n");
    for ( Map.Entry<String, String> p : config.parameters("").entrySet()) {
      System.out.println( "\"" + p.getKey() + "\" : \"" + p.getValue() + "\"");
    }

    // Set values
    config.set("vfs.s3.connect_timeout_ms", "5000");
    config.set("vfs.s3.endpoint_override", "localhost:8888");

    // Get values
    String tile_cache_size = config.get("sm.tile_cache_size");
    System.out.println("\nTile cache size: " + tile_cache_size);

    // Print only the S3 settings
    System.out.println( "\nVFS S3 settings:");
    for ( Map.Entry<String, String> p : config.parameters("vfs.s3").entrySet()) {
      System.out.println( "\"" + p.getKey() + "\" : \"" + p.getValue() + "\"");
    }

    // Assign a config object to a context and VFS
    Context ctx =new Context(config);

  }
}
