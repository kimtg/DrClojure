# DrClojure

DrClojure is a Clojure IDE.

It is written in Clojure with (set! *warn-on-reflection* true).

Features: 

* Newbie-friendly
* Standard out redirection

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

Copyright 2012-2013 Kim, Taegyoon

Distributed under the Eclipse Public License, the same as Clojure.
