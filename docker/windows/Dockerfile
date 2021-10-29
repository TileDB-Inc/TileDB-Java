FROM winamd64/openjdk:18-ea-20-jdk-windowsservercore-1809

SHELL ["cmd", "/S", "/C"] 

ADD https://github.com/TileDB-Inc/TileDB-Java/releases/download/0.5.4/tiledb-java-0.5.4.jar tiledb-java-0.5.4.jar

ADD VersionPrinter.java VersionPrinter.java

RUN javac -cp tiledb-java-0.5.4.jar;. VersionPrinter.java

ENTRYPOINT java -cp tiledb-java-0.5.4.jar;. VersionPrinter

