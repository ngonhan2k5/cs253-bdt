from json import dumps
from kafka import KafkaProducer
import sys
import re
import csv
import sys


if len(sys.argv)>1:
    day = sys.argv[1]
    day = day[-8:-4] + '-' + day[-14:-9]  + 'T00:00:00'
else:
    day = None

if len(sys.argv)>2:
    bserver = sys.argv[2]
else:
    bserver = "localhost:9092"

if len(sys.argv)>3:
    topic = sys.argv[3]
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
        # line = "$$$"
        # print("-" * 20)
        data = line.rstrip()
        print("Send: " + data)
        # if count < 4:
        producer.send(topic, value=data)

        # if count == 2:
        #     producer.flush()

        count = count + 1

    print("("+ str(count) +"/" + str(counta) + ") lines sent to kafka") 

    # data = "$$$"
    # producer.send(topic, value=data)
    # data = "$$$,,,,,,,"
    # print("Send finished signal:" + data) 
    # producer.send(topic, value=data)
    # sys.exit(count)

    producer.flush()
    producer.close()

COLS = {'state':'Province/State', 'country':'Country/Region', 'county': 'Admin2', 'date':'Last Update', \
    'confirm':'Confirmed', 'death':'Deaths', 'recov':'Recovered', 'lat':'Latitude', 'long':'Longitude', \
    'state2':'Province_State', 'country2':'Country_Region', 'date2':'Last_Update'} # variation \

# Country/Region,Province/State,County,Last Update,Confirmed,Deaths,Recovered,Latitude,Longitude

def parseRow(row):
    _day = day or row.get(COLS['date'],     row.get(COLS['date2'],     ''))
    print("aaa", _day)
    # print("Read: ({}) {!r}".format(time.time(), row))
    # print(row)
    ret = row.get(COLS['country'],    row.get(COLS['country2'],    '')) + ',' + \
        row.get(COLS['state'],   row.get(COLS['state2'],      '')) + ',' + \
        row.get(COLS['county'],    '') + ',' + \
        _day + ',' + \
        row.get(COLS['confirm'],    '') + ',' + \
        row.get(COLS['death'],      '') + ',' + \
        row.get(COLS['recov'],      '') + ',' + \
        row.get(COLS['lat'],        '') + ',' + \
        row.get(COLS['long'],       '') 
    # print(ret)
    return ret

main()



