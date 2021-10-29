// javac -cp tiledb-java-0.5.4.tar VersionPrinter.java
// java -cp tiledb-java-0.5.4.jar VersionPrinter

import io.tiledb.java.api.Version;


public class VersionPrinter {
  public static void main(String[] args) throws Exception {
    Version version = new Version();
    System.out.println(version);
  }
}
