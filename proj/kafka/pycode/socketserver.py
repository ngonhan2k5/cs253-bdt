# -*- coding: utf-8 -*-
from time import sleep
# from json import dumps
from kafka import KafkaProducer

import socket
import os
import sys

if len(sys.argv)>1:
    bserver = sys.argv[1]
else:
    bserver = "localhost:9092"

if len(sys.argv)>2:
    topic = sys.argv[2]
else:
    topic = 'connect-test'


if os.path.exists("/tmp/python_unix_sockets_example"):
    os.remove("/tmp/python_unix_sockets_example")

print("Opening socket...to:" + bserver + ", topic:" + topic)
server = socket.socket(socket.AF_UNIX, socket.SOCK_DGRAM)
server.bind("/tmp/python_unix_sockets_example")

producer = KafkaProducer(bootstrap_servers=[bserver],
                         value_serializer=lambda x: x.encode('utf-8') )
                        #  dumps(x). .encode('utf-8'))

print("Listening... and send to:" + bserver + ", topic:" + topic)
while True:
    try:
        datagram = server.recv(1024)
        if not datagram:
            break
        else:
            print("-" * 20)
            data = datagram.decode('utf-8').rstrip()
            print("Received: " + data)

            producer.send(topic, value=data)

    finally:
        print("-" * 20)
        

print("-" * 20)
print("Shutting down...")
server.close()
os.remove("/tmp/python_unix_sockets_example")
print("Done")
