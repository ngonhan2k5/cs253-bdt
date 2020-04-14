
from json import dumps
from kafka import KafkaProducer
import sys
import re
import csv

if len(sys.argv)>1:
    bserver = sys.argv[1]
else:
    bserver = "localhost:9092"

if len(sys.argv)>2:
    topic = sys.argv[2]
else:
    topic = 'corona_cases'



producer = KafkaProducer(bootstrap_servers=[bserver],
                         value_serializer=lambda x: x.encode('utf-8') )

def main():
    count = 0
    counta = 0
    # for line in sys.stdin:
    for row in csv.DictReader(iter(sys.stdin.readline, '')):
        counta = counta + 1
        line = parseRow(row)
        # print("-" * 20)
        data = line.rstrip()
        # print("Send: " + data)
        producer.send(topic, value=data)
        count = count + 1

    print("("+ str(count) +"/" + str(counta) + ") lines sent to kafka") 

COLS = {'state':'Province/State', 'country':'Country/Region', 'update':'Last Update', \
    'confirm':'Confirmed', 'death':'Deaths', 'recov':'Recovered', 'lat':'Latitude', 'long':'Longitude', \
    'state2':'Province_State', 'country2':'Country_Region', 'update2':'Last_Update'} # variation \

def parseRow(row):
    # print("Read: ({}) {!r}".format(time.time(), row))
    print(row)
    return row.get(COLS['state'],   row.get(COLS['state2'],      '')) + ',' + \
        row.get(COLS['country'],    row.get(COLS['country2'],    '')) + ',' + \
        row.get(COLS['update'],     row.get(COLS['update2'],     '')) + ',' + \
        row.get(COLS['confirm'],    '') + ',' + \
        row.get(COLS['death'],      '') + ',' + \
        row.get(COLS['recov'],      '') + ',' + \
        row.get(COLS['lat'],        '') + ',' + \
        row.get(COLS['long'],       '') 

main()




# # from time import sleep
# from json import dumps
# from kafka import KafkaProducer
# import sys
# import re

# if len(sys.argv)>1:
#     bserver = sys.argv[1]
# else:
#     bserver = "localhost:9092"

# if len(sys.argv)>2:
#     topic = sys.argv[2]
# else:
#     topic = 'corona_cases'


# pattern = r'("(?:[^"]|"")*"|[^,"\n\r]*)(,|\r?\n|\r)'
# regex = re.compile(pattern)

# producer = KafkaProducer(bootstrap_servers=[bserver],
#                          value_serializer=lambda x: x.encode('utf-8') )
#                         #  dumps(x).encode('utf-8'))

# count = 0
# counta = 0
# for line in sys.stdin:
#     counta = counta + 1
#     # if len(line.split(',')) == 6 :
#     if regex.match(line):
#         print("-" * 20)
#         data = line.rstrip()
#         print("Send: " + data)

#         producer.send(topic, value=data)
#         count = count + 1
#     else:
#         print("Unexpected format:" + line)

# print("("+ str(count) +"/" + str(counta) + ") lines sent to kafka") 