#!/bin/bash
main=WordCountAvroOptput

classpath=/usr/java/jdk1.7.0_67-cloudera/jre/lib/*:/usr/java/jdk1.7.0_67-cloudera/jre/lib/ext/*:/usr/lib/hadoop/client-0.20/*:/usr/lib/hadoop/*:/usr/lib/hadoop/lib/*:/usr/lib/avro/*

if [ "$1" == "" ]; then
        echo "lab4.WordCount.$main WordCount/input output"
        java -classpath $classpath:lab4.jar lab4.WordCount.$main WordCount/input_1 output_1
else
        main=AvroGenericStationTempYear
        echo "lab4.AvroStationTempYear.$main AvroStationTempYear/input output2 weather.avsc"
        java -classpath $classpath:lab4.jar lab4.AvroStationTempYear.$main AvroStationTempYear/input_2 output_2 AvroStationTempYear/weather.avsc
fi