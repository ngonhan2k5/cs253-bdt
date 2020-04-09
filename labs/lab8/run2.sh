#!/bin/bash
jarfile=lab8.jar
userlib=/usr/lib
CP=/usr/java/jdk1.7.0_67-cloudera/jre/lib/*:/usr/java/jdk1.7.0_67-cloudera/jre/lib/ext/*
CP=$CP:$userlib/hadoop/client-0.20/*
CP=$CP:$userlib/hadoop/*
CP=$CP:$userlib/hadoop/lib/*
# CP=$CP:$userlib/avro/*
CP=$CP:$userlib/hbase/*
CP=$CP:$userlib/hbase/lib/*
CP=$CP:$jarfile:.

echo $CP

java -classpath $CP lab8.MyFirstHbaseTable $1
