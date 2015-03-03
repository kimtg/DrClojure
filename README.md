# DrClojure

DrClojure is a Clojure IDE.

It is written in Clojure.

Features: 

* Newbie-friendly
* Standard out redirection

## Screenshot
![DrClojure Screenshot](https://bitbucket.org/ktg/drclojure/downloads/DrClojure-0.1.8.PNG)
```
+----------------------------------+
| - DrClojure               _ [] X |
+----------------------------------+
| File                             |
+----------------------------------+
|[              Eval              ]|
+----------------------------------+
|(dotimes [i 3] (prn i))           |
|                                  |
|                                  |
|                                  |
+----------------------------------+
|                                  |
+----------------------------------+
|0                                 |
|1                                 |
|2                                 |
+----------------------------------+
```

## Usage

### Compile
```
lein uberjar
```

### Run

Double click JAR_FILE

or
```
java -jar JAR_FILE
```

### GUI

TextArea at the top is for code (Press Eval).

TextField in the middle is for immediate execution (Press Enter).

TextArea at the bottom is output.

## License

Copyright 2012-2015 Kim, Taegyoon

Distributed under the Eclipse Public License, the same as Clojure.
