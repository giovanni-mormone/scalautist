#!/bin/bash
script="./call_server.sh"
nohup $script > my.log 2>&1 &
echo $! > save_pid.txt
