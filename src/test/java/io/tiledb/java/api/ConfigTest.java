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

package io.tiledb.java.api;

import java.util.Map;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConfigTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void test() throws Exception {
    Config config = new Config();

    // Print the default config parameters
    Map<String, String> defaultParams = config.parameters();
    Assert.assertTrue(defaultParams.size() > 0);

    // Get only the S3 settings
    for (Map.Entry<String, String> p : config.parameters("vfs.s3").entrySet()) {
      Assert.assertTrue(defaultParams.containsKey("vfs.s3" + p.getKey()));
    }

    // Set values
    config.set("vfs.s3.connect_timeout_ms", "5000");
    config.set("vfs.s3.endpoint_override", "localhost:8888");

    // Get values
    Assert.assertEquals(config.get("vfs.s3.connect_timeout_ms"), "5000");
    Assert.assertEquals(config.get("vfs.s3.endpoint_override"), "localhost:8888");

    // Assign a config object to a context and VFS
    Context ctx = new Context(config);
    Config ctxConfig = ctx.getConfig();
    Assert.assertEquals(ctxConfig.get("vfs.s3.connect_timeout_ms"), "5000");

    String configPath =
        temp.getRoot().getAbsolutePath() + temp.getRoot().pathSeparator + "testConfig";
    ctxConfig.saveToFile(configPath);

    Config loadConfig = new Config(configPath);
    Assert.assertEquals(loadConfig.get("vfs.s3.connect_timeout_ms"), "5000");
  }
}
