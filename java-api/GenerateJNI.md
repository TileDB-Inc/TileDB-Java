# Generate JNI bindings for TileDB C-API

## Dependencies

### JDK (>=1.8)

### Swig (>=3.0)

* For mac swig is available using:
`brew install swig`
* Installation instructions for several operating systems can be found here:
http://www.swig.org/Doc3.0/Preface.html#Preface_installation


## Genrate bindings

1) Set the ENV variable `TILEDB_HOME` to the install location of TileDB.

2) Generate the JNI code using

`./gradlew generateJNI`
