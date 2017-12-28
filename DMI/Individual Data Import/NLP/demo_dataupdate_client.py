#!/usr/bin/env python
#coding:utf-8

import urllib
import json
import urllib2
import threading
from datetime import datetime

def delete(entity, enttype=''):
    s = datetime.utcnow()  
    print '+++start delete', entity, 'at:', s  
    operation = 'delete'
    requrl = 'http://192.168.0.21:10022/'
    test_data = {'operation': operation, 'entity': entity, 'enttype': enttype}
    payload = urllib.urlencode(test_data)
    req = urllib2.Request(url=requrl, data=payload)
    resp = urllib2.urlopen(req)
    res = resp.read()
    print json.loads(res)
    e = datetime.utcnow()  
    print '+++stop delete', entity, 'at:', e, ', using:', (e-s).microseconds

def search(entity):
    s = datetime.utcnow()  
    print '+++start search', entity, 'at:', s 
    operation = 'search'
    requrl = 'http://192.168.0.21:10022/'
    test_data = {'operation': operation, 'entity': entity}
    payload = urllib.urlencode(test_data)
    req = urllib2.Request(url=requrl, data=payload)
    resp = urllib2.urlopen(req)
    res = resp.read()
    print json.loads(res)
    e = datetime.utcnow()  
    print '+++stop search', entity, 'at:', e, ', using:', (e-s).microseconds

def add(entity, enttype, replaceFlag=''):
    s = datetime.utcnow()  
    print '+++start add', entity, 'at:', s 
    operation = 'add'
    requrl = 'http://192.168.0.21:10022/'
    test_data = {'operation': operation, 'entity': entity, 'enttype': enttype, 'replaceFlag':replaceFlag}
    payload = urllib.urlencode(test_data)
    req = urllib2.Request(url=requrl, data=payload)
    resp = urllib2.urlopen(req)
    res = resp.read()
    print json.loads(res)
    e = datetime.utcnow()  
    print '+++stop add', entity, 'at:', e, ', using:', (e-s).microseconds

def revise(entity, enttype):
    s = datetime.utcnow()  
    print '+++start revise', entity, 'at:', s 
    operation = 'revise'
    requrl = 'http://192.168.0.21:10022/'
    test_data = {'operation': operation, 'entity': entity, 'enttype': enttype}
    payload = urllib.urlencode(test_data)
    req = urllib2.Request(url=requrl, data=payload)
    resp = urllib2.urlopen(req)
    res = resp.read()
    print json.loads(res)
    e = datetime.utcnow()  
    print '+++stop revise', entity, 'at:', e, ', using:', (e-s).microseconds


def multiThreads():
    threads = []
    start = datetime.utcnow()  
    print '------starting at:', start
    t1 = threading.Thread(target=add, args=('测试用例','症状','y'))
    t2 = threading.Thread(target=search, args=('测试用例',))
    t3 = threading.Thread(target=search, args=('尿道创伤',))
    t4 = threading.Thread(target=revise, args=('测试用例','疾病'))
    t5 = threading.Thread(target=delete, args=('测试用例',))
    threads.append(t1)
    threads.append(t2)
    threads.append(t3)
    threads.append(t4)
    threads.append(t5)
    for t in threads:
        t.start()
    for t in threads:
        t.join()
    end = datetime.utcnow()  
    print '---all DONE at:', end, ', using:', (end-start).microseconds 

def singleThread():
    operation = 'search'
    entity = '(男,20岁)'

    requrl = 'http://192.168.0.21:10022/'
    test_data = {
        'operation': operation, 
        'entity': entity,  
    }
    payload = urllib.urlencode(test_data)
    req = urllib2.Request(url=requrl, data=payload)
    resp = urllib2.urlopen(req)
    res = resp.read()
    print json.loads(res)

if __name__ == '__main__':
    singleThread()