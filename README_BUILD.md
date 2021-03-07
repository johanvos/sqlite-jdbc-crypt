How to compile a new version of SQLiteJDBC
==========================================

Prerequisites
-------------

1.	JDK 1.5
2.	Perl
3.	Maven
4.	make
5.	gcc
6.	curl
7.	unzip
8.	git
9.	docker

Build
-----

1.	Edit the `VERSION` file and set the SQLite version to use as available on https://github.com/utelle/SQLite3MultipleCiphers/releases (use base SQLite3 version not the SQLiteMC version). Update the artifact version you want to produce accordingly.
2.	Then, run:

	```
	$ make
	```
	
How to build only the native library for your system (without cross-compilation)
====================================================

1. SQLite version to use should be set in the `VERSION` file
2. Run `make native`
3. It will produce shared library files in the `target` folder

How to build all native libraries (cross-compilation)
=====================================================

1. Set the SQLite version in the `VERSION` file
2. Run `make native-all` (this step will use docker to cross-compile)
3. It will produce shared library files in the `target` folder

How to build a native library for a single target (armv5 for example)
=====================================================================

1. Set the SQLite version in the `VERSION` file
2. Run `make linux-armv5` (this step will use docker to cross-compile)
3. It will produce th armv5 shared library file in the `target` folder

How to build final jars
=======================

1. Set the SQLite version in the `VERSION` file
2. Run `make native-all package test`
3. Get the final jar in the  `target` directory.

How to build pure-java library
==============================

***The pure-java library is no longer supported as of version 3.7.15.https://bitbucket.org/xerial/sqlite-jdbc/issue/10/dropping-pure-java-support***

-	Use Mac OS X or Linux with gcc-3.x

	```
	make purejava
	```

-	The build will fail due to the broken regex libray, so copy the non-corrupted archive I downloaded:

	```
	$ cp archive/regex3.8a.tar.gz target/build/nestedvm-2009-08-09/upstream/downlolad/
	```

-	then do

	```
	'make purejava'
	```

(for deployer only) How to build pure-java and native libraries
===============================================================

```
    make -fMakefile.package
```
