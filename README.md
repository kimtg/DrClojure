# DrClojure

DrClojure is a Clojure IDE.

It is written in Clojure.

Features: 

* Newbie-friendly

## Screenshot
![DrClojure Screenshot](https://raw.githubusercontent.com/kimtg/DrClojure/master/DrClojure.PNG)
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

```

## Usage

### Compile
```
lein uberjar
```

### Run
```
java -jar JAR_FILE
```

### GUI

TextArea at the top is for code (Press Eval).

TextField in the middle is for immediate execution (Press Enter).

## License

Copyright 2012-2020 Kim, Taegyoon

Distributed under the Eclipse Public License, the same as Clojure.
