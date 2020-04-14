# from time import sleep
import time
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

pattern = r'("(?:[^"]|"")*"|[^,"\n\r]*)(,|\r?\n|\r)'
regex = re.compile(pattern)

# producer = KafkaProducer(bootstrap_servers=[bserver],
#                          value_serializer=lambda x: x.encode('utf-8') )
                        #  dumps(x).encode('utf-8'))


cols = {'state':'\ufeffProvince/State', 'country':'Country/Region', 'update':'Last Update', \
    'confirm':'Confirmed', 'death':'Deaths', 'recov':'Recovered', 'lat':'Latitude', 'long':'Longitude'}

def main():
    for row in csv.DictReader(iter(sys.stdin.readline, '')):
        print(parseRow(row))

def parseRow(row):
    # print("Read: ({}) {!r}".format(time.time(), row))
    print(row)
    # row = defaultdict(arow)
    return row.get(cols['state'], '') + '|' + \
        row.get(cols['country'], '') + '|' + \
        row.get(cols['update'], '') + '|' + \
        row.get(cols['confirm'], '') + '|' + \
        row.get(cols['death'], '') + '|' + \
        row.get(cols['recov'], '') + '|' + \
        row.get(cols['lat'], '') + '|' + \
        row.get(cols['long'], '') 

main()