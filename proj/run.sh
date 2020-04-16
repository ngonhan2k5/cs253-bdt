#!/bin/bash

. /etc/profile

spark-submit --master yarn --class bdt.Application cs523/target/Kafka-1.0-jar-with-dependencies.jar
