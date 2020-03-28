# run E1
hadoop jar labe.jar labe.AvgTempE input output_E

#show result
hadoop fs -ls /user/root/output_E
hadoop fs -cat /user/root/output_E/part-r-00000

# run E2
hadoop jar labe.jar labe.AvgTempE2 input output_E2

#result
hadoop fs -ls /user/root/output_E2
hadoop fs -cat /user/root/output_E2/StationTempRecord

#rebuild jar
sh rebuild-all.sh


