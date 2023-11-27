package io.tiledb.libtiledb;

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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Comparator;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

/** Helper class that finds native libraries embedded as resources and loads them dynamically. */
public class NativeLibLoader {

  private static final Logger LOGGER = Logger.getLogger(NativeLibLoader.class.getName());

  private static final String UNKNOWN = "unknown";

  /** Path (relative to jar) where native libraries are located. */
  private static final String LIB_RESOURCE_DIR = "/lib";

  /** Temporary directory where native libraries will be extracted. */
  private static Path tempDir;

  static {
    try {
      tempDir = Files.createTempDirectory("tileDbNativeLibLoader");
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  try (Stream<Path> walk = Files.walk(tempDir)) {
                    walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                  } catch (IOException e) {
                    e.printStackTrace(System.err);
                  }
                }));
  }

  /** Finds and loads native TileDB. */
  static void loadNativeTileDB() {
    try {
      loadNativeLib("tiledb", true);
    } catch (java.lang.UnsatisfiedLinkError e) {
      LOGGER.warning("Could not load Native TIleDB");
      // If a native library fails to link, we fall back to depending on the system
      // dynamic linker to satisfy the requirement. Therefore, we do nothing here
      // (if the library is not available via the system linker, a runtime error
      // will occur later).
    }
  }

  //  /** Finds and loads native Intel Thread Building Blocks. */
  //  static void loadNativeTBB() {
  //    try {
  //      loadNativeLib("tbb", true);
  //    } catch (java.lang.UnsatisfiedLinkError e) {
  //      // If a native library fails to link, we fall back to depending on the system
  //      // dynamic linker to satisfy the requirement. Therefore, we do nothing here
  //      // (if the library is not available via the system linker, a runtime error
  //      // will occur later).
  //    }
  //  }

  /** Finds and loads native TileDB JNI. */
  static void loadNativeTileDBJNI() {
    try {
      loadNativeLib("tiledbjni", true);
    } catch (java.lang.UnsatisfiedLinkError e) {
      LOGGER.warning("Could not load Native TileDB JNI");
      // If a native library fails to link, we fall back to depending on the system
      // dynamic linker to satisfy the requirement. Therefore, we do nothing here
      // (if the library is not available via the system linker, a runtime error
      // will occur later).
    }
  }

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
   * Extract the specified library file from resources to the target directory.
   *
   * @param libraryDir Path of directory containing native library
   * @param libraryName Name of native library
   * @param mapLibraryName If true, transform libraryName with System.mapLibraryName
   * @return File pointing to the extracted library
   */
  private static Path extractLibraryFile(
      String libraryDir, String libraryName, boolean mapLibraryName) {
    String libraryFileName = mapLibraryName ? System.mapLibraryName(libraryName) : libraryName;
    String nativeLibraryFilePath = libraryDir + "/" + libraryFileName;
    Path extractedLibFile = tempDir.resolve(libraryFileName);
    System.out.println("extract from: " + nativeLibraryFilePath);

    try {
      // Extract a native library file into the target directory
      try (InputStream reader = NativeLibLoader.class.getResourceAsStream(nativeLibraryFilePath);
          FileOutputStream writer = new FileOutputStream(extractedLibFile.toFile())) {

        byte[] buffer = new byte[8192];
        int bytesRead = 0;
        while ((bytesRead = reader.read(buffer)) != -1) {
          writer.write(buffer, 0, bytesRead);
        }
      }

      // Set executable (x) flag to enable Java to load the native library on
      // UNIX platforms
      PosixFileAttributeView view =
          Files.getFileAttributeView(extractedLibFile, PosixFileAttributeView.class);
      if (view != null) {
        // On a UNIX platform
        Set<PosixFilePermission> permissions = view.readAttributes().permissions();
        permissions.add(PosixFilePermission.OWNER_READ);
        permissions.add(PosixFilePermission.OWNER_WRITE);
        permissions.add(PosixFilePermission.OWNER_EXECUTE);
        view.setPermissions(permissions);
      }

      // Check whether the contents are properly copied from the resource folder
      {
        InputStream nativeIn = null;
        InputStream extractedLibIn = null;
        try {
          nativeIn = NativeLibLoader.class.getResourceAsStream(nativeLibraryFilePath);
          extractedLibIn = new FileInputStream(extractedLibFile.toFile());

          if (!contentsEquals(nativeIn, extractedLibIn)) {
            throw new IOException(
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

      return extractedLibFile;
    } catch (IOException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  /**
   * Finds and extracts a native library from resources to a temporary directory on the filesystem.
   *
   * @param libraryName Name of native library
   * @param mapLibraryName If true, transform libraryName with System.mapLibraryName
   * @return File pointing to the extracted library
   */
  private static Path findNativeLibrary(String libraryName, boolean mapLibraryName) {
    String mappedLibraryName = mapLibraryName ? System.mapLibraryName(libraryName) : libraryName;
    String libDir = LIB_RESOURCE_DIR;

    System.out.println(System.getProperty("os.name") + " one");
    System.out.println(System.getProperty("os.arch") + " two");
    if (System.getProperty("os.name").contains("Mac") &&
            System.getProperty("os.arch").equals("aarch64")) {
      System.out.println("arm mac <<<<<<<<<<<<<<<");
      libDir = "arm" + libDir;
    }

    String libPath = libDir + "/" + mappedLibraryName;
    System.out.println(libPath + " ///////////////////");
    boolean hasNativeLib = true;
    if (!hasNativeLib) {
      return null;
    }

    // Extract and load a native library inside the jar file
    return extractLibraryFile(libDir, libraryName, mapLibraryName);
  }

  /**
   * Finds and loads a native library of the given name.
   *
   * @param libraryName Name of native library
   * @param mapLibraryName If true, transform libraryName with System.mapLibraryName
   */
  private static void loadNativeLib(String libraryName, boolean mapLibraryName) {
    Path nativeLibFile = findNativeLibrary(libraryName, mapLibraryName);
    if (nativeLibFile != null) {
      System.out.println("native not null");
      // Load extracted or specified native library.
      System.load(nativeLibFile.toString());
    } else {
      // Try loading preinstalled library (in the path -Djava.library.path)
      System.out.println("load library ");
      System.loadLibrary(libraryName);
    }
  }

  private static boolean hasResource(String path) {
    return NativeLibLoader.class.getResource(path) != null;
  }
}
