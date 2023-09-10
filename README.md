Babashka Scripts
================

Personal [babashka](https://book.babashka.org) scripts.

Getting started
---------------
* [Install babashka](https://book.babashka.org/#_installation)
* Add `./src` to your `PATH`.

Now the likes of `mvn-search.clj` can be used from your shell without qualification.

For more convenience, add some shell aliases: e.g.

```zsh
alias mvn-search="mvn-search.clj"
alias clojars-search="clojars-search.clj"
alias ep="kapi-find.clj"
```

mvn-search
----------
Search for a specific artifact in [Maven Central](https://search.maven.org/search).

Usage:

```sh
mvn-search <search_criteria>
```

### Examples

A general search

```sh
$ mvn-search slf4j       
==> WARNING: only display 30 results out of a total of 793.

1) org.slf4j:slf4j-parent
2) org.slf4j:slf4j-bom
3) org.openidentityplatform.commons.i18n-framework:slf4j
more...

==> Please select from the options above.

1
==> WARNING: only display 30 results out of a total of 95.

org.slf4j:slf4j-parent:2.0.9
org.slf4j:slf4j-parent:2.0.8
org.slf4j:slf4j-parent:2.0.7
more...
```

A more specific search

```sh
$ mvn-search org.slf4j/slf4j-api
1) org.slf4j:slf4j-api
2) org.bedework.deploy:bw-wfmodules-org-slf4j-slf4j-api

==> Please select from the options above.

1
==> WARNING: only display 30 results out of a total of 96.

org.slf4j:slf4j-api:2.0.9
org.slf4j:slf4j-api:2.0.8
more...
```

clojars-search
--------------
Search for a specific artifact in [Clojars](https://clojars.org).

Usage:

```sh
clojars-search <search_criteria>
```

### Examples

```sh
$ clojars-search wiremock
1) kelveden:clj-wiremock
2) b-social:wiremock-wrapper
3) clj-wiremock:clj-wiremock

==> Please select from the options above.

1
kelveden:clj-wiremock:1.8.0
kelveden:clj-wiremock:1.7.0
kelveden:clj-wiremock:1.6.0
more...
```

ep
--
Convert a given date/date-time "thing" to some useful data using some opinionated assumptions about what the "thing" is.

Usage:

```sh
ep <date_thing>
```

### Examples

The current date/time

```sh
$ ep
{:date-time "2023-09-10T18:28:24.623",
 :instant "2023-09-10T17:28:24.623Z",
 :epoch-millis 1694366904623,
 :epoch-days 19610}
```

A specific instant

```sh
$ ep 2023-09-10T00:00:00Z
{:date-time "2023-09-10T01:00",
 :instant "2023-09-10T00:00:00Z",
 :epoch-millis 1694304000000,
 :epoch-days 19610}      
```

A specific day

```sh
$ ep "2023-09-10"      
{:date-time "2023-09-10T01:00",
 :instant "2023-09-10T00:00:00Z",
 :epoch-millis 1694304000000,
 :epoch-days 19610} 
```

A specific epoch milli

```sh
$ ep 1694304000000 
{:date-time "2023-09-10T01:00",
 :instant "2023-09-10T00:00:00Z",
 :epoch-millis 1694304000000,
 :epoch-days 19610}
```

A specific epoch day

```sh
$ ep 1
{:date-time "1970-01-02T01:00",
 :instant "1970-01-02T00:00:00Z",
 :epoch-millis 86400000,
 :epoch-days 1}
```
