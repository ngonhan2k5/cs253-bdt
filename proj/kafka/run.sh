#!/bin/bash

. /etc/profile

cwd=$(pwd)
KSERVER="[Kafka Server]"
KCONNECT="[Python connector]"

if [ "$1" == "stop" ]; then
    echo "$KSERVER stoping../"
    kill -9 $(cat $cwd/pid/server.pid)
    rm -f $cwd/pid/server.pid

    exit 0;
    echo "$KCONNECT stoping../"
    kill -9 $(cat $cwd/pid/connect.pid)
    rm -f $cwd/pid/connect.pid
    
    exit 0;
fi

if [ ! -f ./pid/server.pid ]; then
    echo "Run $KSERVER server"
    nohup kafka-server-start.sh config/server.properties > log/server.log 2>&1
    echo $! > $cwd/pid/server.pid
else
    echo "$KSERVER running"
    cat $cwd/pid/server.pid
fi

exit 0;
if [ ! -f ./pid/connect.pid ]; then 
    echo "Run $KCONNECT"
    python3 socketserver.py & > log/connect.log 2>&1
    echo $! > $cwd/pid/connect.pid
else
   echo "$KCONNECT running"
   cat $cwd/pid/connect.pid
fi

