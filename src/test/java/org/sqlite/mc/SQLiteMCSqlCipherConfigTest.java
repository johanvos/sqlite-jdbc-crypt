package org.sqlite.mc;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteConfig;

class SQLiteMCSqlCipherConfigTest {

    private static final String unsaltedHexKeyValid =
            "54686973206973206D792076657279207365637265742070617373776F72642E".toLowerCase();
    private static final String unsaltedHexKeyInvalid =
            "54686973206973206D79207665765742070617373776F72642".toLowerCase();
    private static final String saltedHexKeyValid =
            "54686973206973206D792076657279207365637265742070617373776F72642E2E73616C7479206B65792073616C742E"
                    .toLowerCase();
    private static final String saltedHexKeyInvalid =
            "54686973206973206D79207070617373776F72642E2E73616C7479206B65792073616C742E"
                    .toLowerCase();

    // https://www.baeldung.com/java-byte-arrays-hex-strings
    private static byte[] toBytes(String hexString) {
        byte[] byteArray = new BigInteger(hexString, 16).toByteArray();
        if (byteArray[0] == 0) {
            byte[] output = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, output, 0, output.length);
            return output;
        }
        return byteArray;
    }

    @Test
    void withRawUnsaltedKey() {
        SQLiteMCSqlCipherConfig config = new SQLiteMCSqlCipherConfig();
        assertThrows(
                IllegalArgumentException.class,
                () -> config.withRawUnsaltedKey(toBytes(unsaltedHexKeyInvalid)));

        config.withRawUnsaltedKey(toBytes(unsaltedHexKeyValid));
        assertEquals(
                config.build().toProperties().getProperty(SQLiteConfig.Pragma.KEY.pragmaName),
                ("x'" + unsaltedHexKeyValid + "'"));
    }

    @Test
    void withRawSaltedKey() {
        SQLiteMCSqlCipherConfig config = new SQLiteMCSqlCipherConfig();
        assertThrows(
                IllegalArgumentException.class,
                () -> config.withRawSaltedKey(toBytes(saltedHexKeyInvalid)));

        config.withRawSaltedKey(toBytes(saltedHexKeyValid));
        assertEquals(
                config.build().toProperties().getProperty(SQLiteConfig.Pragma.KEY.pragmaName),
                ("x'" + saltedHexKeyValid + "'"));
    }
}
