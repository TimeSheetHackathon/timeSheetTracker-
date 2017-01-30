#!/bin/bash
# My first script
gradle -stop
gradle clean build
gradle bootRun > server.log 2>&1 &
exit 0
