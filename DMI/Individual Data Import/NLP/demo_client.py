#!/usr/bin/env python
#coding:utf-8

import urllib
import json
import urllib2

if __name__ == '__main__':
    sentence = '门诊行头CT检查,显示左侧侧脑室体旁低密度病灶,以"脑梗死"收入我科.'

    requrl = 'http://192.168.0.21:10011/'
    test_data = {'s': sentence}
    payload = urllib.urlencode(test_data)
    req = urllib2.Request(url=requrl, data=payload)

    resp = urllib2.urlopen(req)
    res = resp.read()
    print res
    res = json.loads(res)
    print 'entity:', res["entity"]
    print 'segment:', res["segment"]
    
