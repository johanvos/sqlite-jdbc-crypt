package org.sqlite.mc;

public class SQLiteMCWxAES128Config extends SQLiteMCConfig.Builder {

    public SQLiteMCWxAES128Config() {
        super();
        setCipher(CipherAlgorithm.WX_AES128);
    }

    @Override
    public SQLiteMCWxAES128Config setLegacy(int value) {
        assert isValid(value, 0, 1);
        super.setLegacy(value);
        return this;
    }

    @Override
    public SQLiteMCWxAES128Config setLegacyPageSize(int value) {
        assert isValid(value, 0, 65536);
        super.setLegacyPageSize(value);
        return this;
    }

    public static SQLiteMCWxAES128Config getDefault() {
        return new SQLiteMCWxAES128Config().setLegacy(0).setLegacyPageSize(0);
    }
}
