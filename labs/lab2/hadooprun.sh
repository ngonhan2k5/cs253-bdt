#!/bin/bash

if hadoop fs -ls /user/root/input ; then
	echo "hadoop jar wcount.jar WordCount input ouput"
else
	hadoop fs -put input /user/root
fi

	hadoop jar wcount.jar WordCount input ouput
