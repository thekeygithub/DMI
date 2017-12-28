#!/bin/sh

ps -ef |grep server2.py |grep -v grep
ps -ef |grep server2.py |grep -v grep  |cut -c 9-15 |xargs kill
