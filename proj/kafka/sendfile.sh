#!/bin/bash

#find $CSV_FOLDER -type f -name "*.csv" -print0 | sort -z | xargs -r0
# . /etc/profile
CSV_FOLDER=~/COVID-19/csse_covid_19_data/csse_covid_19_daily_reports
echo "Wait $3 and send $2"
if [ $# -eq 3 ]; then
    sleep $3
    if [ -f $2 ]; then
        # cat "$CSV_FOLDER/$1" | python3 pycode/sendstdin.py
        
        sed '1s/^\xEF\xBB\xBF//' "$2" | python3 pycode/sendkafka.py $2
        echo "($?)"
    else
        echo "$2 not exits"
        # ls $CSV_FOLDER
    fi
else
    if [ -f $CSV_FOLDER/$1 ]; then
        # cat "$CSV_FOLDER/$1" | python3 pycode/sendstdin.py
        sed '1s/^\xEF\xBB\xBF//' "$CSV_FOLDER/$1" | python3 pycode/sendkafka.py $2
    else
        echo "$CSV_FOLDER/$1 not exits."
        ls $CSV_FOLDER
    fi
fi