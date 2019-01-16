/*--------------------------------------------------------------------------
 *  Copyright 2011 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------
 *
 * Originally from https://github.com/xerial/snappy-java
 * Modifications made by TileDB, Inc. 2018
 *
 * */

package io.tiledb.libtiledb;

import io.tiledb.java.api.TileDBError;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

public class tiledbJNILoader {

  private static final String UNKNOWN = "unknown";

  static final String TILEDB_JNI_LIBRARY_NAME = "tiledbjni";

  private static boolean contentsEquals(InputStream in1, InputStream in2) throws IOException {
    if (!(in1 instanceof BufferedInputStream)) {
      in1 = new BufferedInputStream(in1);
    }
    if (!(in2 instanceof BufferedInputStream)) {
      in2 = new BufferedInputStream(in2);
    }

    int ch = in1.read();
    while (ch != -1) {
      int ch2 = in2.read();
      if (ch != ch2) {
        return false;
      }
      ch = in1.read();
    }
    int ch2 = in2.read();
    return ch2 == -1;
  }

  private static String normalizeOs(String value) {
    value = normalize(value);
    if (value.startsWith("aix")) {
      return "aix";
    }
    if (value.startsWith("hpux")) {
      return "hpux";
    }
    if (value.startsWith("os400")) {
      // Avoid the names such as os4000
      if (value.length() <= 5 || !Character.isDigit(value.charAt(5))) {
        return "os400";
      }
    }
    if (value.startsWith("linux")) {
      return "linux";
    }
    if (value.startsWith("macosx") || value.startsWith("osx")) {
      return "osx";
    }
    if (value.startsWith("freebsd")) {
      return "freebsd";
    }
    if (value.startsWith("openbsd")) {
      return "openbsd";
    }
    if (value.startsWith("netbsd")) {
      return "netbsd";
    }
    if (value.startsWith("solaris") || value.startsWith("sunos")) {
      return "sunos";
    }
    if (value.startsWith("windows")) {
      return "windows";
    }

    return UNKNOWN;
  }

  private static String normalizeArch(String value) {
    value = normalize(value);
    if (value.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
      return "x86_64";
    }
    if (value.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
      return "x86_32";
    }
    if (value.matches("^(ia64w?|itanium64)$")) {
      return "itanium_64";
    }
    if ("ia64n".equals(value)) {
      return "itanium_32";
    }
    if (value.matches("^(sparc|sparc32)$")) {
      return "sparc_32";
    }
    if (value.matches("^(sparcv9|sparc64)$")) {
      return "sparc_64";
    }
    if (value.matches("^(arm|arm32)$")) {
      return "arm_32";
    }
    if ("aarch64".equals(value)) {
      return "aarch_64";
    }
    if (value.matches("^(mips|mips32)$")) {
      return "mips_32";
    }
    if (value.matches("^(mipsel|mips32el)$")) {
      return "mipsel_32";
    }
    if ("mips64".equals(value)) {
      return "mips_64";
    }
    if ("mips64el".equals(value)) {
      return "mipsel_64";
    }
    if (value.matches("^(ppc|ppc32)$")) {
      return "ppc_32";
    }
    if (value.matches("^(ppcle|ppc32le)$")) {
      return "ppcle_32";
    }
    if ("ppc64".equals(value)) {
      return "ppc_64";
    }
    if ("ppc64le".equals(value)) {
      return "ppcle_64";
    }
    if ("s390".equals(value)) {
      return "s390_32";
    }
    if ("s390x".equals(value)) {
      return "s390_64";
    }

    return UNKNOWN;
  }

  private static String normalize(String value) {
    if (value == null) {
      return "";
    }
    return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
  }

  private static String getOSClassifier() {

    final Properties allProps = new Properties(System.getProperties());
    final String osName = allProps.getProperty("os.name");
    final String osArch = allProps.getProperty("os.arch");

    final String detectedName = normalizeOs(osName);
    final String detectedArch = normalizeArch(osArch);

    return detectedName + "-" + detectedArch;
  }

  /**
   * Extract the specified library file to the target folder
   *
   * @param libFolderForCurrentOS
   * @param libraryFileName
   * @param targetFolder
   * @return
   */
  private static File extractLibraryFile(
      String libFolderForCurrentOS, String libraryFileName, String targetFolder)
      throws TileDBError {
    String nativeLibraryFilePath =
        new File(libFolderForCurrentOS, libraryFileName).getAbsolutePath();

    // Attach UUID to the native library file to ensure multiple class loaders can read the
    // libsnappy-java multiple times.
    String uuid = UUID.randomUUID().toString();
    String extractedLibFileName = String.format("tiledb-%s-%s", uuid, libraryFileName);
    File extractedLibFile = new File(targetFolder, extractedLibFileName);

    try {
      // Extract a native library file into the target directory
      InputStream reader = null;
      FileOutputStream writer = null;
      try {
        reader = tiledbJNILoader.class.getResourceAsStream(nativeLibraryFilePath);
        try {
          writer = new FileOutputStream(extractedLibFile);

          byte[] buffer = new byte[8192];
          int bytesRead = 0;
          while ((bytesRead = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, bytesRead);
          }
        } finally {
          if (writer != null) {
            writer.close();
          }
        }
      } finally {
        if (reader != null) {
          reader.close();
        }

        // Delete the extracted lib file on JVM exit.
        extractedLibFile.deleteOnExit();
      }

      // Set executable (x) flag to enable Java to load the native library
      boolean success =
          extractedLibFile.setReadable(true)
              && extractedLibFile.setWritable(true, true)
              && extractedLibFile.setExecutable(true);
      if (!success) {
        // Setting file flag may fail, but in this case another error will be thrown in later phase
      }

      // Check whether the contents are properly copied from the resource folder
      {
        InputStream nativeIn = null;
        InputStream extractedLibIn = null;
        try {
          nativeIn = tiledbJNILoader.class.getResourceAsStream(nativeLibraryFilePath);
          extractedLibIn = new FileInputStream(extractedLibFile);

          if (!contentsEquals(nativeIn, extractedLibIn)) {
            throw new TileDBError(
                String.format("Failed to write a native library file at %s", extractedLibFile));
          }
        } finally {
          if (nativeIn != null) {
            nativeIn.close();
          }
          if (extractedLibIn != null) {
            extractedLibIn.close();
          }
        }
      }

      return new File(targetFolder, extractedLibFileName);
    } catch (IOException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  static File findNativeLibrary() throws TileDBError {
    // Load an OS-dependent native library inside a jar file

    String tileDBJNINativeLibraryPath = new File("/lib", getOSClassifier()).getAbsolutePath();
    File libPath =
        new File(tileDBJNINativeLibraryPath, System.mapLibraryName(TILEDB_JNI_LIBRARY_NAME));
    boolean hasNativeLib = hasResource(libPath.getAbsolutePath());

    if (!hasNativeLib) {
      return null;
    }

    // Temporary folder for the native lib. Use the value java.io.tmpdir
    File tempFolder = new File(System.getProperty("java.io.tmpdir"));
    if (!tempFolder.exists()) {
      boolean created = tempFolder.mkdirs();
      if (!created) {
        // if created == false, it will fail eventually in the later part
      }
    }

    // Extract and load a native library inside the jar file
    return extractLibraryFile(
        tileDBJNINativeLibraryPath,
        System.mapLibraryName(TILEDB_JNI_LIBRARY_NAME),
        tempFolder.getAbsolutePath());
  }

  private static boolean hasResource(String path) {
    return tiledbJNILoader.class.getResource(path) != null;
  }
}
