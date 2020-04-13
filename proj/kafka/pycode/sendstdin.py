import socket
import os
import sys
import re

# 
regex = r'/("(?:[^"]|"")*"|[^,"\n\r]*)(,|\r?\n|\r)/'

print("Connecting...")
if os.path.exists("/tmp/python_unix_sockets_example"):
    client = socket.socket(socket.AF_UNIX, socket.SOCK_DGRAM)
    client.connect("/tmp/python_unix_sockets_example")

    count = 0
    counta = 0
    for line in sys.stdin:
        counta = counta + 1
        # if len(line.split(',')) == 6 :
        if re.match(regex, line):
            client.send(line.encode('utf-8'))
            count = count + 1
        else:
            print("Unexpected format:" + line)

    print("("+ str(count) +"/" + str(counta) + ") lines sent to kafka") 
    # print("Ready.")
    # print("Ctrl-C to quit.")
    # print("Sending 'DONE' shuts down the server and quits.")
    # while True:
    #     try:
    #         x = input("> ")
    #         if "" != x:
    #             print("SEND:", x)
    #             client.send(x.encode('utf-8'))
    #             if "DONE" == x:
    #                 print("Shutting down.")
    #                 break
    #     except KeyboardInterrupt as k:
    #         print("Shutting down.")
    #         client.close()
    #         break
else:
    print("Couldn't Connect!")
    print("Done")

