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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConfigTest {

  @Rule public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void testConfig() throws Exception {
    try (Config config = new Config()) {
      // Print the default config parameters
      Map<String, String> defaultParams = config.parameters();
      Assert.assertTrue(defaultParams.size() > 0);
    }
  }

  @Test
  public void testConfigMapConstructor() throws Exception {
    HashMap<String, String> settings = new HashMap<>();
    // Set values
    settings.put("vfs.s3.connect_timeout_ms", "5000");
    settings.put("vfs.s3.endpoint_override", "localhost:8888");
    try (Config config = new Config(settings)) {
      Assert.assertEquals(config.get("vfs.s3.connect_timeout_ms"), "5000");
      Assert.assertEquals(config.get("vfs.s3.endpoint_override"), "localhost:8888");
    }
  }

  @Test
  public void testConfigParamterFiltering() throws Exception {
    // Get only the S3 settings
    try (Config config = new Config()) {
      Map<String, String> defaultParams = config.parameters();
      for (Map.Entry<String, String> p : config.parameters("vfs.s3").entrySet()) {
        Assert.assertTrue(defaultParams.containsKey("vfs.s3" + p.getKey()));
      }
    }
  }

  @Test
  public void testConfigSetGetValues() throws Exception {
    try (Config config = new Config()) {
      // Set values
      config.set("vfs.s3.connect_timeout_ms", "5000");
      config.set("vfs.s3.endpoint_override", "localhost:8888");

      // Get values
      Assert.assertEquals(config.get("vfs.s3.connect_timeout_ms"), "5000");
      Assert.assertEquals(config.get("vfs.s3.endpoint_override"), "localhost:8888");
    }
  }

  @Test
  public void testCtxConfig() throws Exception {
    // Assign a config object to a context and VFS
    try (Config config = new Config()) {
      config.set("vfs.s3.connect_timeout_ms", "5000");
      try (Context ctx = new Context(config);
          Config ctxConfig = ctx.getConfig()) {
        Assert.assertEquals(ctxConfig.get("vfs.s3.connect_timeout_ms"), "5000");
      }
    }
  }

  @Test
  public void testConfigSaveLoad() throws Exception {
    String configPath =
        temp.getRoot().getAbsolutePath() + temp.getRoot().pathSeparator + "testConfigString";
    try (Config config = new Config()) {
      config.set("vfs.s3.connect_timeout_ms", "5000");
      config.saveToFile(configPath);
    }
    // Try loading from a string path
    try (Config loadConfig = new Config(configPath)) {
      Assert.assertEquals(loadConfig.get("vfs.s3.connect_timeout_ms"), "5000");
    }
  }

  @Test
  public void testConfigSaveLoadURI() throws Exception {
    String configPath =
        temp.getRoot().getAbsolutePath() + temp.getRoot().pathSeparator + "testConfigStringURI";
    URI configURI = new URI("file://" + configPath);
    // Try lo
    try (Config config = new Config()) {
      config.set("vfs.s3.connect_timeout_ms", "5000");
      config.saveToFile(configURI);
    }
    try (Config loadConfig = new Config(configURI)) {
      Assert.assertEquals(loadConfig.get("vfs.s3.connect_timeout_ms"), "5000");
    }
  }
}
