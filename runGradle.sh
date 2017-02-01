#!/bin/bash
# My first script
kill $(ps -aef | grep java | grep apache | awk '{print $2}')
./gradlew clean build
./gradlew bootRun > server.log 2>&1 &
exit 0
