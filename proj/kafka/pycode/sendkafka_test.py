# from time import sleep
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

# we have 3 type of header, but only 2 case of needed columns

cols = {'state':'Province/State', 'country':'Country/Region', 'update':'Last Update', \
    'confirm':'Confirmed', 'death':'Deaths', 'recov':'Recovered', 'lat':'Latitude', 'long':'Longitude', \
    'state2':'Province_State', 'country2':'Country_Region', 'update2':'Last_Update'} # variation \

producer = KafkaProducer(bootstrap_servers=[bserver],
                         value_serializer=lambda x: x.encode('utf-8') )

def main():
    count = 0
    counta = 0
    # for line in sys.stdin:
    for row in csv.DictReader(iter(sys.stdin.readline, '')):
        counta = counta + 1
        line = parseRow(row)
        print("-" * 20)
        data = line.rstrip()
        print("Send: " + data)
        producer.send(topic, value=data)
        count = count + 1

    print("("+ str(count) +"/" + str(counta) + ") lines sent to kafka") 


def parseRow(row):
    # print("Read: ({}) {!r}".format(time.time(), row))
    print(row)
    return row.get(cols['state'],   row.get(cols['state2'],      '')) + ',' + \
        row.get(cols['country'],    row.get(cols['country2'],    '')) + ',' + \
        row.get(cols['update'],     row.get(cols['update2'],     '')) + ',' + \
        row.get(cols['confirm'],    '') + ',' + \
        row.get(cols['death'],      '') + ',' + \
        row.get(cols['recov'],      '') + ',' + \
        row.get(cols['lat'],        '') + ',' + \
        row.get(cols['long'],       '') 

main()