#!/bin/sh
sh ./hadooprun.sh E
hadoop fs -ls /user/root/output_E

sh ./hadooprun.sh E2
hadoop fs -ls /user/root/output_E2
