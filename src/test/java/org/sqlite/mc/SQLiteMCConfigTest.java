package org.sqlite.mc;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.sqlite.SQLiteConfig.Pragma.LEGACY_PAGE_SIZE;

class SQLiteMCConfigTest {

    @Test
    public void builderSetLegacyPageSizeTest() {
        Properties p = new SQLiteMCConfig.Builder().setLegacyPageSize(0).build().toProperties();
        assertThat(p.get(LEGACY_PAGE_SIZE.getPragmaName())).isEqualTo("0");

        p = new SQLiteMCConfig.Builder().setLegacyPageSize(65536).build().toProperties();
        assertThat(p.get(LEGACY_PAGE_SIZE.getPragmaName())).isEqualTo("65536");

        p = new SQLiteMCConfig.Builder().setLegacyPageSize(1024).build().toProperties();
        assertThat(p.get(LEGACY_PAGE_SIZE.getPragmaName())).isEqualTo("1024");

        assertThatIllegalArgumentException().isThrownBy(() -> new SQLiteMCConfig.Builder().setLegacyPageSize(-1));
        assertThatIllegalArgumentException().isThrownBy(() -> new SQLiteMCConfig.Builder().setLegacyPageSize(65637));
        assertThatIllegalArgumentException().isThrownBy(() -> new SQLiteMCConfig.Builder().setLegacyPageSize(1233));
    }
}