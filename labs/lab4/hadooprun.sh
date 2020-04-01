#!/bin/bash

if [ "$1" == "1" ] ; then
	base="WordCount"
	main="WordCountAvroOptput"
	#arg="aa"
else
	if [ "$1" == "2" ] ; then

		base="AvroStationTempYear"
		main="AvroGenericStationTempYear"
		arg=weather.avsc

		if hadoop fs -ls /user/root/weather.avsc ; then
        		echo Found /user/root/weather.avsc
		else
	        	hadoop fs -put $base/weather.avsc
        		hadoop fs -put $base/maxweather.avsc
		fi
	else
		exit 0
	fi
fi

if hadoop fs -ls /user/root/input_$1 ; then
	echo Found /user/root/input_$1
else
	hadoop fs -put $base/input_$1 /user/root
fi

echo "hadoop jar lab4.jar lab4.$base.$main input_$1 output_$1 $arg"

hadoop jar lab4.jar lab4.$base.$main input_$1 output_$1 $arg
