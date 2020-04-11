#!/bin/bash
jarfile=lab9/target/lab9-j7.jar

hadoop fs -rm -r output$1

spark-submit --class lab9.SparkWordCount$1 --master yarn $jarfile input/input.txt output$1 $2
