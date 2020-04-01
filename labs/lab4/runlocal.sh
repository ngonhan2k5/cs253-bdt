#!/bin/bash
main=WordCountAvroOptput

classpath=/usr/java/jdk1.7.0_67-cloudera/jre/lib/*:/usr/java/jdk1.7.0_67-cloudera/jre/lib/ext/*:/usr/lib/hadoop/client-0.20/*:/usr/lib/hadoop/*:/usr/lib/hadoop/lib/*:/usr/lib/avro/*

#classpath=/usr/java/jdk1.7.0_67-cloudera/jre/lib/*:/usr/java/jdk1.7.0_67-cloudera/jre/lib/ext/*:/usr/lib/hadoop/client-0.20/*:/usr/lib/hadoop/*:/usr/lib/hadoop/lib/*

if [ "$1" == "1" ]; then
	echo "lab4.WordCount.$main WordCount/input output"
	java -classpath $classpath:lab4.jar lab4.WordCount.$main WordCount/input_1 output_1
else
	if [ "$1" == "2" ]; then
		main=AvroGenericStationTempYear
		echo "lab4.AvroStationTempYear.$main AvroStationTempYear/input output_2 weather.avsc"
		java -classpath $classpath:lab4.jar lab4.AvroStationTempYear.$main AvroStationTempYear/input output_2 AvroStationTempYear/weather.avsc
	else
		main=AvroMaxStationTempYear
                echo "lab4.AvroStationTempYear.$main AvroStationTempYear/input output_3 maxweather.avsc"
                java -classpath $classpath:lab4.jar lab4.AvroStationTempYear.$main AvroStationTempYear/input output_3 AvroStationTempYear/maxweather.avsc
	fi
fi

