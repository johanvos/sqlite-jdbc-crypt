[![Build Status](https://travis-ci.com/Willena/sqlite-jdbc-crypt.svg?branch=master)](https://travis-ci.com/Willena/sqlite-jdbc-crypt)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.willena/sqlite-jdbc/badge.svg)](https://search.maven.org/artifact/io.github.willena/sqlite-jdbc/)

# SQLite JDBC Driver

This is a library for accessing and creating [SQLite](http://sqlite.org) database files in Java.

This SQLiteJDBC library requires no configuration since native libraries for major OSs, including Windows, Mac OS X,
Linux etc., are assembled into a single JAR (Java Archive) file (the native library is provided
by [Utelle](http://github.com/utelle) as part of
the [SQLite3MultipleCiphers](https://github.com/utelle/SQLite3MultipleCiphers) project.

The usage is quite simple;
Download the sqlite-jdbc library from [Maven Central](https://search.maven.org/artifact/io.github.willena/sqlite-jdbc/)
or from [Github Release](https://github.com/Willena/sqlite-jdbc-crypt/releases/latest), then append the library (JAR
file) to your class path or use Maven, Gradle.

## Table of content

- [SQLite JDBC Driver](#sqlite-jdbc-driver)
    * [Table of content](#table-of-content)
    * [Setup](#setup)
        + [Supported Operating Systems](#supported-operating-systems)
        + [Manual Download](#manual-download)
        + [Maven](#maven)
        + [Gradle](#gradle)
        + [Configuration](#configuration)
        + [Build from scratch](#build-from-scratch)
    * [Usage and examples](#usage-and-examples)
        + [Basic usage](#basic-usage)
            - [Simple example](#simple-example)
            - [Specify database file](#specify-database-file)
            - [Special database access](#special-database-access)
                * [Database files in classpaths or network](#database-files-in-classpaths-or-network)
                * [In-memory databases](#in-memory-databases)
            - [Using the restore feature](#using-the-restore-feature)
            - [Using the Blob datatype](#using-the-blob-datatype)
            - [Connection configuration](#connection-configuration)
        + [Get started with encryption](#get-started-with-encryption)
            - [Introduction](#introduction)
            - [Supported ciphers](#supported-ciphers)
                * [Introduction](#introduction-1)
                * [AES 128 Bit CBC - No HMAC (wxSQLite3)](#aes-128-bit-cbc---no-hmac--wxsqlite3-)
                * [AES 256 Bit CBC - No HMAC (wxSQLite3)](#aes-256-bit-cbc---no-hmac--wxsqlite3-)
                * [ChaCha20 - Poly1305 HMAC (sqleet)](#chacha20---poly1305-hmac--sqleet-)
                * [AES 256 Bit CBC - SHA1/SHA256/SHA512 HMAC (SQLCipher)](#aes-256-bit-cbc---sha1-sha256-sha512-hmac--sqlcipher-)
            - [Configuration methods](#configuration-methods)
                * [Configure using SQLiteMCConfig objects](#configure-using-sqlitemcconfig-objects)
                * [Configure using SQL specific SQL functions](#configure-using-sql-specific-sql-functions)
                * [Configure using URI](#configure-using-uri)
            - [Using an encryption key](#using-an-encryption-key)
                * [ASCII](#ascii)
                * [Hex](#hex)
            - [SQLite3 backup API and encryption](#sqlite3-backup-api-and-encryption)
            - [Encryption key manipulations](#encryption-key-manipulations)
                * [Encrypt a plain database](#encrypt-a-plain-database)
                * [Open an encrypted DB](#open-an-encrypted-db)
                * [Change the key used for a database](#change-the-key-used-for-a-database)
                * [Remove the key and go back to plain](#remove-the-key-and-go-back-to-plain)
    * [Licenses](#licenses)
        + [Utelle (sqlite3mc)](#utelle--sqlite3mc-)
        + [Willena](#willena)
        + [Xerial](#xerial)

## Setup

### Supported Operating Systems

The native Sqlite library is compiled and *automaticaly* tested for the following platforms and OSs:

| Operating System / Architecture    | x86    | x86_64    | arm    | armv6    | armv7    | arm64    | ppc64    |
|---------------------------------	|-----	|--------	|-----	|-------	|-------	|-------	|-------	|
| Windows                            | ✅    | ✅        | ❌    | ❌        | ✅        | ✅        | ❌        |
| Mac Os X                          | ❌    | ✅        | ❌    | ❌        | ❌        | ❌        | ❌        |
| Linux Generic                     | ✅    | ✅        | ✅    | ✅        | ✅        | ✅        | ✅        |
| Android                           | ✅    | ✅        | ✅    | ✅        | ✅        | ✅        | ✅        |

If your os is not listed and you want to use the native library for your OS, build the source from scratch (see the
build from scratch section).

### Manual Download

1. Download the latest version of SQLiteJDBC from
   the [Maven Central](https://search.maven.org/artifact/io.github.willena/sqlite-jdbc/) or
   from [Github Release](https://github.com/Willena/sqlite-jdbc-crypt/releases/latest)
2. Add the downloaded jar to your Java classpath

### Maven

If you are familiar with [Maven](http://maven.apache.org), add the following XML fragments into your pom.xml file. With
those settings, your Maven will automatically download our SQLiteJDBC library into your local Maven repository, since
our sqlite-jdbc libraries are synchronized with
the [Maven's central repository](http://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/).

        <dependency>
          <groupId>io.github.willena</groupId>
          <artifactId>sqlite-jdbc</artifactId>
          <version> version_number </version>
        </dependency>

### Gradle

If you are familiar with [Gradle](https://gradle.org/), use the following line.
This will automatically download the SQLiteJDBC library into your project.

        implementation 'io.github.willena:sqlite-jdbc:version_number'

### Configuration

This library is very limited in global configuration settings.
You only need to know that it will extracts the native library for your OS to the directory specified
by `java.io.tmpdir` JVM property.

To use another directory, set `org.sqlite.tmpdir` JVM property to your favorite path.

### Build from scratch

See [README_BUILD.md](./README_BUILD.md) file

## Usage and examples

### Basic usage

#### Simple example

To open an SQLite database connection from your code, here is an example.

**Sample.java**

```java
    import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sample {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}
```    

#### Specify database file

Here is an example to establishing a connection to a database file `C:\work\mydatabase.db` (in Windows)

```java
Connection connection=DriverManager.getConnection("jdbc:sqlite:C:/work/mydatabase.db");
```

Opening a UNIX (Linux, Mac OS X, etc.) file `/home/leo/work/mydatabase.db`

```java
Connection connection=DriverManager.getConnection("jdbc:sqlite:/home/leo/work/mydatabase.db");
```

#### Special database access

##### Database files in classpaths or network

To load db files that can be found from the class loader (e.g., db
files inside a jar file in the classpath),
use `jdbc:sqlite::resource:` prefix.

For example, here is an example to access an SQLite DB file, `sample.db`
in a Java package `org.yourdomain`:

```java
Connection conn=DriverManager.getConnection("jdbc:sqlite::resource:org/yourdomain/sample.db"); 
```

In addition, external DB resources can be used as follows:

```java
Connection conn=DriverManager.getConnection("jdbc:sqlite::resource:http://www.xerial.org/svn/project/XerialJ/trunk/sqlite-jdbc/src/test/java/org/sqlite/sample.db"); 
```

To access db files inside some specific jar file (in local or remote),
use the [JAR URL](http://java.sun.com/j2se/1.5.0/docs/api/java/net/JarURLConnection.html):

```java
Connection conn=DriverManager.getConnection("jdbc:sqlite::resource:jar:http://www.xerial.org/svn/project/XerialJ/trunk/sqlite-jdbc/src/test/resources/testdb.jar!/sample.db"); 
```

DB files will be extracted to a temporary folder specified in `System.getProperty("java.io.tmpdir")`.

##### In-memory databases

SQLite supports on-memory database management, which does not create any database files. To use a memory database in
your Java code, get the database connection as follows:

```java
Connection connection=DriverManager.getConnection("jdbc:sqlite::memory:");
```

And also, you can create memory database as follows:

```java
Connection connection=DriverManager.getConnection("jdbc:sqlite:");
```

#### Using the restore feature

Take a backup of the whole database to `backup.db` file:

```java
// Create a memory database
Connection conn=DriverManager.getConnection("jdbc:sqlite:");
Statement stmt=conn.createStatement();
// Do some updates
stmt.executeUpdate("create table sample(id, name)");
stmt.executeUpdate("insert into sample values(1, \"leo\")");
stmt.executeUpdate("insert into sample values(2, \"yui\")");
// Dump the database contents to a file
stmt.executeUpdate("backup to backup.db");
Restore the database from a backup file:
// Create a memory database
Connection conn=DriverManager.getConnection("jdbc:sqlite:");
// Restore the database from a backup file
Statement stat=conn.createStatement();
stat.executeUpdate("restore from backup.db");
```

#### Using the Blob datatype

1. Create a table with a column of blob type: `create table T (id integer, data blob)`
2. Create a prepared statement with `?` symbol: `insert into T values(1, ?)`
3. Prepare a blob data in byte array (e.g., `byte[] data = ...`)
4. `preparedStatement.setBytes(1, data)`
5. `preparedStatement.execute()...`

#### Connection configuration

Using the SQLiteConfig you can configure a number of things. Here is an example.

```java
SQLiteConfig config=new SQLiteConfig();
// config.setReadOnly(true);   
config.setSharedCache(true);
config.recursiveTriggers(true);
// ... other configuration can be set via SQLiteConfig object
Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db", config.toProperties());
```

### Get started with encryption

The main goal of this library is to allow users to encrypt databases they are producing.
In this section we will walk through the main aspect to understand to make this library
work correctly with your requirements.

The content of this section is mainly extracted from the WxSQLite3 repository.

#### Introduction

This library is compiled with a modified SQLite native library that support multiple
cipher schemes. In order to be used the user must choose a cipher scheme manually. If
not using the default one (at the moment the default cipher is CHACHA20) is applied.

Before applying a configuration, choose the encryption scheme you would like to use in the
supported cipher list.

The full documentation of the native library used can be found here: https://utelle.github.io/SQLite3MultipleCiphers/

#### Configuration methods

##### Configure using SQLiteMCConfig objects

Starting with version 3.32.0 the java implementation has a new configuration object called SQLiteMCConfig that can hold
the cipher configuration.
The interface allows for very simple and quick configuration of the choosen cipher algorithm.

For each cipher supported a ready to use and a customizable object is present. If you want to go completely custom it
is also possible. All parameters are available through setters.

Each conbinaison of paramters presented in previous section are implemented as getDefault (SQLiteMC default) or
get<name>Defautls (ex: getV2Defaults() for SQLCipher))

The object names are :

````

SQLiteMCConfig.Builder; Use this to build a configuration object from scratch
    
SQLiteMCSqlCipherConfig; // Generate a configuration for SQLCipher
SQLiteMCChacha20Config; // Generate a configuration for Chacha20
SQLiteMCWxAES256Config; // Generate a configuration for legacy AES 256 WxSQLite3
SQLiteMCWxAES128Config; // Generate a configuration for legacy AES 128 WxSQLite3
SQLiteMCRC4Config; // Generate a configuration for System.Data.SQLite
````

To specify the key you just need to use the `withKey(String Key)` or any of the configuration object.
To create the connection it is now very simple:

```java

//Using the SQLiteMC default parameters
Connection connection=DriverManager.getConnection("jdbc:sqlite:file:file.db",new SQLiteMCConfig.Builder().withKey("Key").build().toProperties());
Connection connection=new SQLiteMCConfig.Builder().withKey("Key").build().createConnection("jdbc:sqlite:file:file.db");

//Using Chacha20
Connection connection=DriverManager.getConnection("jdbc:sqlite:file:file.db",SQLiteMCChacha20Config.getDefault().withKey("Key").build().toProperties());
Connection connection=SQLiteMCChacha20Config.getDefault().withKey("Key").build().createConnection("jdbc:sqlite:file:file.db");

```

##### Configure using SQL specific SQL functions

**Note:** See
the [SQLite3 Multiple Ciphers SQL Documentation](https://utelle.github.io/SQLite3MultipleCiphers/docs/configuration/config_sql/)
for more details.

##### Configure using URI

**Note:** See
the [SQLite3 Multiple Ciphers URI Documentation](https://utelle.github.io/SQLite3MultipleCiphers/docs/configuration/config_uri/)
for more details.

Example: URI query string to select the legacy SQLCipher Version 2 encryption scheme:

```
file:databasefile?cipher=sqlcipher&legacy=1&kdf_iter=4000
```

#### Using an encryption key

##### ASCII

Passing the key to SQLite in order to decrypt the database is quite simple.
It can be done using either the SQL syntax or the URI syntax.

Keep in mind that you always need to configure the cipher algorithm before applying the key !
If your key is an ASCII key you can provide it using

```sqlite
PRAGMA key = 'mykey';
```

or

```
file:databasefile?cipher=sqlcipher&legacy=1&kdf_iter=4000&key=mykey
```

##### Hex

Passing the hexadecimal version of the key to SQLite in order to decrypt the database is quite simple.
It is very handy in case of a binary key. It can be done using either the SQL syntax or the URI syntax.

Keep in mind that you always need to configure the cipher algorithm before applying the key !
If your key is an Hexadecimal key you can provide it using

```sqlite
PRAGMA hexkey = "hexkey";
```

or

```
file:databasefile?cipher=sqlcipher&legacy=1&kdf_iter=4000&hexkey=myHexKey
```

#### SQLite3 backup API and encryption

When using the SQLite3 backup API to create a backup copy of a SQLite database, the most common case is that source and
target database use the same encryption cipher, if any. However, the **sqlite3mc** multi-cipher encryption extension
allows to assign different ciphers to the source and target database.

Problems can arise from the fact that different ciphers may require a different number of reserved bytes per database
page. If the number of reserved bytes for the target database is greater than that for the source database, performing a
backup via the SQLite3 backup API is unfortunately not possible. In such a case the backup will be aborted.

To allow as many cipher combinations as possible the **sqlite3mc** multi-cipher encryption extension implements fallback
solutions for the most common case where the source database is not encrypted, but a cipher usually requiring a certain
number of reserved bytes per database page was selected for the target database. In this case no reserved bytes will be
used by the ciphers. The drawback is that the resulting encryption is less secure and that the resulting databases will
not be compatible with the corresponding legacy ciphers.

Please find below a table describing with which encryption cipher combinations the backup API can be used.

| **Backup**&nbsp;&nbsp;**To** |  SQLite3 |  sqlite3mc |  sqlite3mc | sqlite3mc | SQLCipher v1 | SQLCipher v2+ |
| --- | :---: | :---: | :---: | :---: | :---: | :---: |
<br/>**From** | Plain<br/>&nbsp; | AES-128<br/>&nbsp; | AES-256<br/>&nbsp; | ChaCha20<br/>Poly1305 | AES-256<br/>&nbsp; | AES-256<br/>SHA1 |
SQLite3<br/>Plain<br/>&nbsp; | :ok: | :ok: | :ok: | :ok: :exclamation: | :ok: :exclamation: | :ok: :exclamation:
sqlite3mc<br/> AES-128<br/>&nbsp; | :ok: | :ok: | :ok: | :ok: :exclamation: | :ok: :exclamation: | :ok: :exclamation:
sqlite3mc<br/>AES-256<br/>&nbsp; | :ok: | :ok: | :ok: | :ok: :exclamation: | :ok: :exclamation: | :ok: :exclamation:
sqlite3mc<br/>ChaCha20<br/>Poly1305 |  :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: | :x: | :x:
SQLCipher v1<br/>AES-256<br/>&nbsp; | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :x: | :ok: | :x:
SQLCipher&nbsp;v2+<br/>AES-256<br/>SHA1 | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :ok: <sup>:small_red_triangle_down:</sup> | :x: | :ok:

Symbol | Description
:---: | :---
:ok:  | Works
:x: | Does **not** work
:exclamation: | Works only for non-legacy ciphers with reduced security
<sup>:small_red_triangle_down:</sup> | Keeps reserved bytes per database page

**Note**: It is strongly recommended to use the same encryption cipher for source **and** target database.

#### Encryption key manipulations

Several manipulation can be very usefull when encrypting a database.
For example you may want to change the password used, remove it, or encrypt a plain database.

Here is what you need to know.

##### Encrypt a plain database

1. Open the database file
2. Set cipher configuration
3. Apply the key for the first time using the `PRAGMA` syntax
4. Use as usual

##### Open an encrypted DB

1. Open the database file
2. set cipher configuration
3. Apply the corresponding key using the `PRAGMA` syntax
4. Use normally

##### Change the key used for a database

1. Open the database file
2. Set cipher configuration
3. Apply the current key using the `PRAGMA key='mykey'` syntax (It needs to be adapted if using an hexadecimal key)
4. Change the key using the `PRAGMA rekey='my_new_key'` syntax (It needs to be adapted if using an hexadecimal key)
5. Use normally

##### Remove the key and go back to plain

1. Open the database file
2. Set cipher configuration
3. Apply the current key using the `PRAGMA key='mykey'` syntax (It needs to be adapted if using an hexadecimal key)
4. Change the key to `null` using`PRAGMA rekey=''`
5. Use normally

## Licenses

### Utelle (SQLite3MultipleCiphers)

This project is using the enormous work of Utelle that bring an alternative to the paid SEE via a modified version of
SQLite3

MIT License

Copyright (c) 2019-2020 Ulrich Telle

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

### Willena

This project includes modification done by Guillaume VILLENA (Willena) that are under the following licence.

This program follows the Apache License version 2.0 (<http://www.apache.org/licenses/> ) That means:

It allows you to:

* freely download and use this software, in whole or in part, for personal, company internal, or commercial purposes;
* use this software in packages or distributions that you create.

It forbids you to:

* redistribute any piece of our originated software without proper attribution;
* use any marks owned by us in any way that might state or imply that we xerial.org endorse your distribution;
* use any marks owned by us in any way that might state or imply that you created this software in question.

It requires you to:

* include a copy of the license in any redistribution you may make that includes this software;
* provide clear attribution to us, xerial.org for any distributions that include this software

It does not require you to:

* include the source of this software itself, or of any modifications you may have
  made to it, in any redistribution you may assemble that includes it;
* submit changes that you make to the software back to this software (though such feedback is encouraged).

See License FAQ <http://www.apache.org/foundation/licence-FAQ.html> for more details.

### Xerial

This project is based on xerial work and is frequently synchronized with
their [repository](https://github.com/xerial/sqlite-jdbc).
Here is their Licence.

This program follows the Apache License version 2.0 (<http://www.apache.org/licenses/> ) That means:

It allows you to:

* freely download and use this software, in whole or in part, for personal, company internal, or commercial purposes;
* use this software in packages or distributions that you create.

It forbids you to:

* redistribute any piece of our originated software without proper attribution;
* use any marks owned by us in any way that might state or imply that we xerial.org endorse your distribution;
* use any marks owned by us in any way that might state or imply that you created this software in question.

It requires you to:

* include a copy of the license in any redistribution you may make that includes this software;
* provide clear attribution to us, xerial.org for any distributions that include this software

It does not require you to:

* include the source of this software itself, or of any modifications you may have
  made to it, in any redistribution you may assemble that includes it;
* submit changes that you make to the software back to this software (though such feedback is encouraged).

See License FAQ <http://www.apache.org/foundation/licence-FAQ.html> for more details.
