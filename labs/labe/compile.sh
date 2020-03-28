#!/bin/bash
name="AvgTemp"
echo "$name$1.java"
javac -d . -classpath /usr/java/jdk1.7.0_67-cloudera/jre/lib/*:/usr/java/jdk1.7.0_67-cloudera/jre/lib/ext/*:/usr/lib/hadoop/client-0.20/*:/usr/lib/hadoop/*:/usr/lib/hadoop/lib/*:. $name$1.java *.java
