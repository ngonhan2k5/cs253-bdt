#!/bin/bash
dir=WordCount
if [ "$1" == "1" ] ; then
	dir=WordCount
else
	if [ "$1" == "2" ] ; then
		dir=AvroStationTempYear
	else
		exit 0
	fi
fi

echo "Compile and jar $dir"
javac -d . -classpath /usr/java/jdk1.7.0_67-cloudera/jre/lib/*:/usr/java/jdk1.7.0_67-cloudera/jre/lib/ext/*:/usr/lib/hadoop/client-0.20/*:/usr/lib/hadoop/*:/usr/lib/hadoop/lib/*:/usr/lib/avro/*:. $dir/*.java


#!/bin/bash
jarfile=lab4
rm $jarfile.jar -f
jar cvf $jarfile.jar $jarfile