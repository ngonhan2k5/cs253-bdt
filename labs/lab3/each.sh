#!/bin/bash
./compile.sh $1;  ./createjar.sh; ./hadooprun.sh $1
