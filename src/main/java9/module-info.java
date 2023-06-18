module io.github.willena.sqlitejdbc {

    requires transitive java.sql;
    requires transitive java.sql.rowset;
    requires static org.graalvm.sdk;

    exports org.sqlite;
    exports org.sqlite.core;
    exports org.sqlite.date;
    exports org.sqlite.javax;
    exports org.sqlite.jdbc3;
    exports org.sqlite.jdbc4;
    exports org.sqlite.util;
    exports org.sqlite.mc;

    provides java.sql.Driver with org.sqlite.JDBC;

}
