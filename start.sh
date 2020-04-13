#!/bin/bash
docker-compose up -d
docker logs cloudera_cloudera_1
docker exec -it cloudera_cloudera_1 bash -l
