#!/bin/bash

# git pull
git pull origin master

# build bootjar
./gradlew bootjar

# docker build Recruit Jogbo API Server
docker build -t recruit-jogbo ./recruit-jogbo-common/

# docker build Recruit Jogbo Mail Server
docker build -t recruit-jogbo-mail ./mail-sender/

# docker run with compose
docker-compose up

