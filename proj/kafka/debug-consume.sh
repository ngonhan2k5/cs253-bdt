#!/bin/bash

. /etc/profile

kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic corona_cases --from-beginning