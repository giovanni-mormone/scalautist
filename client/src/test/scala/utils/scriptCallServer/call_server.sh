#!/bin/bash

cd ../../../../../../
echo "hello"
exec sbt "project server; run" &
cd  client/src/test/scala/utils/scriptCallServer
echo $! > save_pid_java.txt