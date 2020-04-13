#!/bin/bash

. /etc/profile

cwd=$(pwd)
KSERVER="[Kafka Server]"
PIDFILE=$cwd/pid/server.pid

if [ "$1" == "stop" ]; then
    echo "$KSERVER stoping../"
    kill -9 $(cat $PIDFILE)
    rm -f $PIDFILE

    # exit 0;
    # echo "$KCONNECT stoping../"
    # kill -9 $(cat $cwd/pid/connect.pid)
    # rm -f $cwd/pid/connect.pid

    # if [ -f "$DOCKER_SSD_PIDFILE" ]; then
    #         log_begin_msg "Stopping $KSERVER"
    #         start-stop-daemon --stop --pidfile "PIDFILE" --retry 10
    #         log_end_msg $?
    # else
    #         log_warning_msg "Docker already stopped - file $PIDFILE not found."
    # fi
    
    exit 0;
fi

if [ -f $PIDFILE ] && ps -p $(cat $PIDFILE) ; then
    echo "$KSERVER running"
    cat $PIDFILE
else


    echo "Run $KSERVER server"
    
    CMD="kafka-server-start.sh"
    OPTS=config/server.properties
    LOG=log/server.log
    nohup $CMD $OPTS > $LOG 2>&1 &
    echo $! > $PIDFILE
fi

# exit 0;
# if [ ! -f ./pid/connect.pid ]; then 
#     echo "Run $KCONNECT"
#     python3 socketserver.py & > log/connect.log 2>&1
#     echo $! > $cwd/pid/connect.pid
# else
#    echo "$KCONNECT running"
#    cat $cwd/pid/connect.pid
# fi

