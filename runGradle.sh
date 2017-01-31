#!/bin/bash
# My first script
./gradlew -stop
./gradlew clean build
./gradlew bootRun > server.log 2>&1 &
exit 0
