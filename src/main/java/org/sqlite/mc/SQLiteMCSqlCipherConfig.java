package org.sqlite.mc;

public class SQLiteMCSqlCipherConfig extends SQLiteMCConfig.Builder {

    public SQLiteMCSqlCipherConfig() {
        super();
        setCipher(CipherAlgorithm.SQL_CIPHER);
    }

    public SQLiteMCSqlCipherConfig setLegacy(int value) {
        assert isValid(value, 0, 4);
        super.setLegacy(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setLegacyPageSize(int value) {
        assert isValid(value, 0, 65536);
        super.setLegacyPageSize(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setKdfIter(int value) {
        assert isValid(value, 1, Integer.MAX_VALUE);
        super.setKdfIter(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setFastKdfIter(int value) {
        assert isValid(value, 1, Integer.MAX_VALUE);
        super.setFastKdfIter(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setHmacUse(boolean value) {
        super.setHmacUse(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setHmacPgno(HmacPgno value) {
        assert isValid(value.ordinal(), 0, 2);
        super.setHmacPgno(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setHmacSaltMask(int value) {
        assert isValid(value, 0, 255);
        super.setHmacSaltMask(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setKdfAlgorithm(KdfAlgorithm value) {
        assert isValid(value.ordinal(), 0, 2);
        super.setKdfAlgorithm(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setHmacAlgorithm(HmacAlgorithm value) {
        assert isValid(value.ordinal(), 0, 2);
        super.setHmacAlgorithm(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig setPlaintextHeaderSize(int value) {
        assert isValid(value, 0, 100);
        assert value % 16 == 0; // Must be multiple of 16
        super.setPlaintextHeaderSize(value);
        return this;
    }

    public SQLiteMCSqlCipherConfig withRawUnsaltedKey(byte[] key) {
        if (key.length != 32) {
            throw new IllegalArgumentException(String.format("Raw unsalted key must be exactly 32 bytes long (provided: %s)", key.length));
        }

        return withRawKey(toHexString(key));
    }

    public SQLiteMCSqlCipherConfig withRawSaltedKey(byte[] key) {
        if (key.length != 48) {
            throw new IllegalArgumentException(String.format("Raw unsalted key must be exactly 48 bytes long (provided: %s)", key.length));
        }

        return withRawKey(toHexString(key));
    }

    private SQLiteMCSqlCipherConfig withRawKey(String key) {
        if (key.length() != 64 && key.length() != 96) {
            throw new IllegalArgumentException(String.format("Raw unsalted key must be exactly 64 or 96 char long (provided: %s)", key.length()));
        }
        withKey(String.format("x'%s'", key));
        return this;
    }

    public static SQLiteMCSqlCipherConfig getDefault() {
        return new SQLiteMCSqlCipherConfig();
    }

    public static SQLiteMCSqlCipherConfig getV1Defaults() {
        return new SQLiteMCSqlCipherConfig()
                .setKdfIter(4000)
                .setFastKdfIter(2)
                .setHmacUse(false)
                .setLegacy(1)
                .setLegacyPageSize(1024)
                .setKdfAlgorithm(KdfAlgorithm.SHA1)
                .setHmacAlgorithm(HmacAlgorithm.SHA1);
    }

    public static SQLiteMCSqlCipherConfig getV2Defaults() {
        return new SQLiteMCSqlCipherConfig()
                .setKdfIter(4000)
                .setFastKdfIter(2)
                .setHmacUse(true)
                .setHmacPgno(HmacPgno.LITTLE_ENDIAN)
                .setHmacSaltMask(0x3a)
                .setLegacy(2)
                .setLegacyPageSize(1024)
                .setKdfAlgorithm(KdfAlgorithm.SHA1)
                .setHmacAlgorithm(HmacAlgorithm.SHA1);
    }

    public static SQLiteMCSqlCipherConfig getV3Defaults() {
        return new SQLiteMCSqlCipherConfig()
                .setKdfIter(64000)
                .setFastKdfIter(2)
                .setHmacUse(true)
                .setHmacPgno(HmacPgno.LITTLE_ENDIAN)
                .setHmacSaltMask(0x3a)
                .setLegacy(3)
                .setLegacyPageSize(1024)
                .setKdfAlgorithm(KdfAlgorithm.SHA1)
                .setHmacAlgorithm(HmacAlgorithm.SHA1);
    }

    public static SQLiteMCSqlCipherConfig getV4Defaults() {
        return new SQLiteMCSqlCipherConfig()
                .setKdfIter(256000)
                .setFastKdfIter(2)
                .setHmacUse(true)
                .setHmacPgno(HmacPgno.LITTLE_ENDIAN)
                .setHmacSaltMask(0x3a)
                .setLegacy(4)
                .setLegacyPageSize(4096)
                .setKdfAlgorithm(KdfAlgorithm.SHA512)
                .setHmacAlgorithm(HmacAlgorithm.SHA512)
                .setPlaintextHeaderSize(0);
    }
}
