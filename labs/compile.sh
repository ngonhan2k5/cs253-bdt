#!/bin/bash
dir=lab8
# if [ "$1" == "1" ] ; then
# 	dir=WordCount
# else
# 	if [ "$1" == "2" ] ; then
# 		dir=AvroStationTempYear
# 	else
# 		exit 0
# 	fi
# fi

echo "Compile and jar $dir"
userlib=usr/lib
CP=/usr/java/jdk1.7.0_67-cloudera/jre/lib/*:/usr/java/jdk1.7.0_67-cloudera/jre/lib/ext/*
CP=$CP:$userlib/hadoop/client-0.20/*
CP=$CP:$userlib/hadoop/*
CP=$CP:$userlib/hadoop/lib/*
# CP=$CP:$userlib/avro/*
CP=$CP:$userlib/hbase/*
CP=$CP:$userlib/hbase/lib/*
CP=$CP:.

hbase
javac -d . -classpath $CP $dir/*.java


#!/bin/bash
jarfile=lab8
rm $jarfile.jar -f
jar cvf $jarfile.jar $jarfile