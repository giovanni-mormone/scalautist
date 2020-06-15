#!/bin/bash

cd ../../../../../../
echo "hello"
exec sbt "project server; run"