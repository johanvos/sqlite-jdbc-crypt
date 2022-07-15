package org.sqlite.mc;

public class SQLiteMCWxAES256Config extends SQLiteMCConfig.Builder {

    public SQLiteMCWxAES256Config() {
        super();
        setCipher(CipherAlgorithm.WX_AES256);
    }

    @Override
    public SQLiteMCWxAES256Config setLegacy(int value) {
        assert isValid(value, 0, 1);
        super.setLegacy(value);
        return this;
    }

    @Override
    public SQLiteMCWxAES256Config setLegacyPageSize(int value) {
        assert isValid(value, 0, 65536);
        super.setLegacyPageSize(value);
        return this;
    }

    @Override
    public SQLiteMCWxAES256Config setKdfIter(int value) {
        assert isValid(value, 1, Integer.MAX_VALUE);
        super.setKdfIter(value);
        return this;
    }

    public static SQLiteMCWxAES256Config getDefault() {
        return new SQLiteMCWxAES256Config()
                .setLegacy(0)
                .setLegacyPageSize(0)
                .setKdfIter(4001);

    }
}
