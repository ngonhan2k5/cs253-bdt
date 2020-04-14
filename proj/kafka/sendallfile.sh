#!/bin/bash

BASE=~/cloudera/proj
cd $BASE/kafka

# . /etc/profile

#CSV_FOLDER=~/COVID-19/csse_covid_19_data/csse_covid_19_daily_reports
CSV_FOLDER=~/cloudera/proj/data/csse_covid_19_daily_reports/

echo "Read CSV file in forder: $CSV_FOLDER"

SLEEP=2s
if [ $# -eq 1 ]; then
    SLEEP=$1
else
    if [ $# -eq 2 ]; then
        CSV_FOLDER = $1
        SLEEP=$2
    fi
fi

find $CSV_FOLDER -type f -name "*.csv" -print0 | sort -z | xargs -r0 -I % sh sendfile.sh all % $SLEEP

# if [ "$1" == "all" ]; then
#     sleep 2s
#     if [ -f $2 ]; then
#         # cat "$CSV_FOLDER/$1" | python3 pycode/sendstdin.py
#         cat "$2" | python3 pycode/sendkafka.py
#     else
#         echo "$2 not exits"
#         # ls $CSV_FOLDER
#     fi
# else
#     if [ -f $CSV_FOLDER/$1 ]; then
#         # cat "$CSV_FOLDER/$1" | python3 pycode/sendstdin.py
#         cat "$CSV_FOLDER/$1" | python3 pycode/sendkafka.py
#     else
#         echo "$CSV_FOLDER/$1 not exits"
#         ls $CSV_FOLDER
#     fi
# fi