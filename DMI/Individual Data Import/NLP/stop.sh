#!/bin/sh

ps -ef |grep demo_server.py |grep -v grep
ps -ef |grep demo_server.py |grep -v grep  |cut -c 9-15 |xargs kill
