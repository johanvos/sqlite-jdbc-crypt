package org.sqlite.mc;

public class SQLiteMCChacha20Config extends SQLiteMCConfig.Builder {

    public SQLiteMCChacha20Config() {
        super();
        setCipher(CipherAlgorithm.CHACHA20);
    }

    public SQLiteMCChacha20Config setLegacy(int value) {
        assert isValid(value, 0, 4);
        super.setLegacy(value);
        return this;
    }

    public SQLiteMCChacha20Config setLegacyPageSize(int value) {
        assert isValid(value, 0, 65536);
        super.setLegacyPageSize(value);
        return this;
    }

    public SQLiteMCChacha20Config setKdfIter(int value) {
        assert isValid(value, 1, Integer.MAX_VALUE);
        super.setKdfIter(value);
        return this;
    }

    public SQLiteMCChacha20Config withRawUnsaltedKey(byte[] key) {
        if (key.length != 32) {
            throw new IllegalArgumentException(String.format("Raw unsalted key must be exactly 32 bytes long (provided: %s)", key.length));
        }

        return withRawKey(toHexString(key));
    }

    public SQLiteMCChacha20Config withRawSaltedKey(byte[] key) {
        if (key.length != 48) {
            throw new IllegalArgumentException(String.format("Raw unsalted key must be exactly 48 bytes long (provided: %s)", key.length));
        }

        return withRawKey(toHexString(key));
    }

    private SQLiteMCChacha20Config withRawKey(String key) {
        if (key.length() != 64 && key.length() != 96) {
            throw new IllegalArgumentException(String.format("Raw unsalted key must be exactly 64 or 96 char long (provided: %s)", key.length()));
        }
        return (SQLiteMCChacha20Config) withKey(String.format("raw:%s", key));
    }

    public static SQLiteMCChacha20Config getDefault() {
        return new SQLiteMCChacha20Config()
                .setKdfIter(64007)
                .setLegacy(0)
                .setLegacyPageSize(4096);
    }

    public static SQLiteMCChacha20Config getSqlleetDefaults() {
        return new SQLiteMCChacha20Config()
                .setKdfIter(12345)
                .setLegacy(1)
                .setLegacyPageSize(4096);
    }

}
