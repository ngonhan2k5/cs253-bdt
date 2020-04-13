#!/bin/bash

# . /etc/profile

cwd=$(pwd)
PYCONNECT="[Python connector]"

if [ "$1" == "stop" ]; then

    echo "$PYCONNECT stoping../"
    kill -9 $(cat $cwd/pid/pyconnect.pid)
    rm -f $cwd/pid/pyconnect.pid
    
    exit 0;
fi

if [ ! -f ./pid/pyconnect.pid ]; then
    echo "Run $PYCONNECT server"
    nohup python3 pycode/socketserver.py > log/pyconnect.log 2>&1 &
    echo $! > $cwd/pid/pyconnect.pid
else
    echo "$PYCONNECT running"
    cat $cwd/pid/pyconnect.pid
fi

