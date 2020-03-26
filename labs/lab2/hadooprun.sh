#!/bin/bash

if hadoop fs -ls /user/root/input ; then
	echo "hadoop jar wcount.jar WordCount$1 input output_$1"
else
	hadoop fs -put input /user/root
fi

	hadoop jar wcount.jar WordCount$1 input output_$1
