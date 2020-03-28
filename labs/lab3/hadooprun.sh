#!/bin/bash
name="AvgTemp"
jar=lab3
if hadoop fs -ls /user/root/input ; then
        echo "hadoop jar $jar.jar $jar.$name$1 input output_$1"
else
        hadoop fs -rm -f -r /user/root/input
        hadoop fs -put input /user/root
fi
hadoop fs -rm -f -r /user/root/output_$1
hadoop jar $jar.jar $jar.$name$1 input output_$1
echo "hadoop jar $jar.jar $jar.$name$1 input output_$1"