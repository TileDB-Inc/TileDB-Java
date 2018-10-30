package io.tiledb.java.api;

import io.tiledb.libtiledb.tiledb_encryption_type_t;

public enum EncryptionType {
    TILEDB_NO_COMPRESSION,
    TILEDB_AES_256_GCM;

    protected tiledb_encryption_type_t toSwigEnum() throws TileDBError {
        switch (this) {
            case TILEDB_NO_COMPRESSION:
                return tiledb_encryption_type_t.TILEDB_NO_ENCRYPTION;
            case TILEDB_AES_256_GCM:
                return tiledb_encryption_type_t.TILEDB_AES_256_GCM;
            default:
                throw new TileDBError("No such enum value " + this.name());
        }
    }

    protected static EncryptionType fromSwigEnum(tiledb_encryption_type_t e) throws TileDBError {
        switch (e) {
            case TILEDB_NO_ENCRYPTION:
                return TILEDB_NO_COMPRESSION;
            case TILEDB_AES_256_GCM:
                return TILEDB_AES_256_GCM;
            default:
                throw new TileDBError("No such enum value " + e.name());
        }
    }
}
