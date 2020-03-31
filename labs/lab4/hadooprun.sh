#!/bin/bash

if [ "$1" == "1" ] ; then
	base="WordCount"
	main="WordCountAvroOptput"
	
else
	if [ "$1" == "2" ] ; then
		base="AvroStationTempYear"
		main="AvroGenericStationTempYear"
	else
		exit 0
	fi
fi

if hadoop fs -ls /user/root/input_$1 ; then
	echo "hadoop jar lab4.jar lab4.$base.$main input_$1 output_$1"
else
	hadoop fs -put $base/input_$1 /user/root
fi

hadoop jar lab4.jar lab4.$base.$main input_$1 output_$1