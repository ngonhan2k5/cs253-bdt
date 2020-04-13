# from time import sleep
from json import dumps
from kafka import KafkaProducer
import sys
import re

if len(sys.argv)>1:
    bserver = sys.argv[1]
else:
    bserver = "localhost:9092"

if len(sys.argv)>2:
    topic = sys.argv[2]
else:
    topic = 'connect-test'


pattern = r'("(?:[^"]|"")*"|[^,"\n\r]*)(,|\r?\n|\r)'
regex = re.compile(pattern)

producer = KafkaProducer(bootstrap_servers=[bserver],
                         value_serializer=lambda x: x.encode('utf-8') )
                        #  dumps(x).encode('utf-8'))

count = 0
counta = 0
for line in sys.stdin:
    counta = counta + 1
    # if len(line.split(',')) == 6 :
    if regex.match(line):
        print("-" * 20)
        data = line.rstrip()
        print("Send: " + data)

        producer.send(topic, value=data)
        count = count + 1
    else:
        print("Unexpected format:" + line)

print("("+ str(count) +"/" + str(counta) + ") lines sent to kafka") 