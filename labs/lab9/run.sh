#!/bin/bash
jarfile=lab9/target/lab9-j7.jar
userlib=/usr/lib
CP=/usr/java/jdk1.7.0_67-cloudera/jre/lib/*:/usr/java/jdk1.7.0_67-cloudera/jre/lib/ext/*
CP=$CP:$userlib/hadoop/client-0.20/*
CP=$CP:$userlib/hadoop/*
CP=$CP:$userlib/hadoop/lib/*
# CP=$CP:$userlib/avro/*
CP=$CP:$userlib/hbase/*
CP=$CP:$userlib/hbase/lib/*
CP=$CP:$userlib/spark/lib/*
CP=$CP:$jarfile:.

echo $CP

java -classpath $CP lab9.SparkWordCountjdk7 $1

#C:\Users\USER\.vscode\extensions\vscjava.vscode-java-debug-0.25.1\scripts\launcher.bat "C:\Program Files\Java\jdk1.8.0_191\bin\java.exe" -Dfile.encoding=UTF-8 -cp C:\Users\USER\AppData\Local\Temp\cp_487krlwwthmgdi3k8pvr1tbb3.jar lab9.SparkWordCountjdk7 input\input.txt output
