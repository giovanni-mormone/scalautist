#!/bin/bash

cd ../../../../../../
echo "hello"
start sbt "project server; run" &
echo $! > save_pid.txt