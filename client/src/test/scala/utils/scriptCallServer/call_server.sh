#!/bin/sh

cd ../../../../../../
echo "hello"
start sbt "project server; run" &
echo "Ok"
sleep 20
kill -9 $!
exit