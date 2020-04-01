#!/bin/bash

if [ "$1" == "1" ] ; then
        base="WordCount"
        main="WordCountAvroOptput"
        input=1
else
        if [ "$1" == "2" ] ; then
                base="AvroStationTempYear"
                main="AvroGenericStationTempYear"
                arg=weather.avsc
                input=2
                if hadoop fs -ls /user/root/weather.avsc ; then
                        echo "hadoop jar lab4.jar lab4.$base.$main input_$input output_$1 $arg"
                else
                        hadoop fs -put weather.avsc /user/root/
                        hadoop fs -put maxweather.avsc /user/root/
                fi

        else
                if [ "$1" == "3" ] ; then
                        base="AvroStationTempYear"
                        main="AvroMaxStationTempYear"
                        arg=maxweather.avsc
                        input=2
                        if hadoop fs -ls /user/root/weather.avsc ; then
                                echo "hadoop jar lab4.jar lab4.$base.$main input_$input output_$1 $arg"
                        else
                                hadoop fs -put weather.avsc /user/root/
                                hadoop fs -put maxweather.avsc /user/root/
                        fi
                else
                        exit 0
                fi
        fi
fi

if hadoop fs -ls /user/root/input_$input ; then
        echo "hadoop jar lab4.jar lab4.$base.$main input_$input output_$1"
else
        hadoop fs -put $base/input_$input /user/root
fi

hadoop jar lab4.jar lab4.$base.$main input_$input output_$1 $arg