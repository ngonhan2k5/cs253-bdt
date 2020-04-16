#!/bin/bash

# . /etc/profile

kafka-topics.sh --delete --topic corona_cases --bootstrap-server localhost:9092
kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic corona_cases
kafka-topics.sh --list --bootstrap-server localhost:9092