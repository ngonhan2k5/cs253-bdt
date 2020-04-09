#!/bin/bash
jarfile=lab8/target/lab8-1.0-full.jar

hadoop jar $jarfile lab8.MyFirstHbaseTable $1
