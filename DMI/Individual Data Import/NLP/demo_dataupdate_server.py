#!/usr/bin/env python
#coding:utf-8
import os
import sys
import re
import math
import datetime
import random
import HTMLParser
import urllib
import json
import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
import tornado.escape
from tornado.options import define, options
import threading
import tools
import rwlock

define("port", default=10022, help="run on the given port", type=int)

dicfile = ''
medi_dic = {}
file_lock = rwlock.RWLock()

enttype_dic = {
    u'疾病' : 'disease',
    u'症状' : 'symptom',
    u'治疗项目' : 'treatment',
    u'辅助检查' : 'diagnosis_name',
    u'其他诊疗项目' : 'other_diagnosis',
    u'器材' : 'instrument',
    u'医疗器材' : 'instrument',
    u'药品-通用名' : 'medicine_cn',
    u'药品-产品名' : 'medicine_pn',
    u'药品-商品名' : 'medicine_mn',
    u'药品名':'medicine',
    u'剂型' : 'dosage_form',
    u'规格' : 'specifications',
    u'药品规格' : 'specifications',
    u'包装规格' : 'packing_spe',
    u'药品包装规格' : 'packing_spe',
    u'包材' : 'packing_material',
    u'企业机构' : 'enterprise',
    u'药品':'medicine',
    u'药名':'medicine',
    u'药品通用名' : 'medicine_cn',
    u'药品产品名' : 'medicine_pn',
    u'药品商品名' : 'medicine_mn',
    u'医疗器械' : 'instrument',
    u'器械' : 'instrument',
    u'科室' : 'department',
    u'地址' : 'address'
}

def addEntity(dicfile, dic, entity, enttype, replaceFlag):
    if enttype in enttype_dic:
        enttype = enttype_dic[enttype]
    else:
        returndata = {"code":-1, "info":'The type of entity is not allowed.'}
        return returndata

    entity = entity.encode('utf8')
    update_str = '%s\t%s\n' % (entity, enttype)

    wlock = file_lock.writer_lock
    if wlock.acquire():#lock
        if entity in dic:
            if replaceFlag=='y' and dic[entity] != enttype:         
                try:
                    lines = open(dicfile).readlines()
                    flen = len(lines)
                    for i in range(flen):
                        tmpent = lines[i].split('\t')[0]
                        if tmpent == entity:    #UTF8
                            lines[i] = update_str
                    open(dicfile,'w').writelines(lines)

                    dic[entity] = enttype
                    returndata = {"code":1, "info":'Replaced Successfully.'}
                except Exception, e:
                    returndata = {"code":-1, "info":str(e)}           
            else:
                returndata = {"code":2, "info":'The entity has existed.'}
        else:
            dic[entity] = enttype
            try:
                with open(dicfile, 'a') as fw:
                    fw.write(update_str)
                returndata = {"code":0, "info":'Added Successfully.'}
            except Exception, e:
                returndata = {"code":-1, "info":str(e)}
        wlock.release()
    else:
        returndata = {"code":-1, "info": 'Sorry, another user is operating on the dictionary file.'}
    return returndata

def delEntity(dicfile, dic, entity, enttype=''):
    if enttype != '' and enttype in enttype_dic:
        enttype = enttype_dic[enttype]
    elif enttype != '':
        returndata = {"code":-1, "info":'The type of entity is not allowed.'}
        return returndata

    entity = entity.encode('utf8')
    if enttype == '':
        del_str = '%s\t' % (entity)
    else:
        del_str = '%s\t%s\n' % (entity, enttype)

    wlock = file_lock.writer_lock
    if wlock.acquire():
        if entity in medi_dic:
            try:
                lines = open(dicfile).readlines()
                delFlag = False
                for i, line in enumerate(lines):
                    if line.startswith(del_str):
                        del lines[i]
                        delFlag = True
                open(dicfile,'w').writelines(lines)

                if delFlag and (enttype == '' or medi_dic[entity] == enttype):
                    medi_dic.pop(entity)
                    returndata =  {"code":0, "info":'Deleted Successfully.'}
                else:
                    returndata =  {"code":2, "info":'This entity is not in the dictionary.'}
            except Exception, e:
                returndata =  {"code":-1, "info":str(e)}
        else:
            returndata =  {"code":1, "info": 'There is no entity named as %s.' % entity}
        wlock.release()
    else:
        returndata = {"code":-1, "info": 'Sorry, another user is operating on the dictionary file.'}
    return returndata

def reviseEntity(dicfile, dic, entity, enttype):
    if enttype in enttype_dic:
        enttype = enttype_dic[enttype]
    else:
        returndata = {"code":-1, "info":'The type of entity is not allowed.'}
        return returndata

    wlock = file_lock.writer_lock
    entity = entity.encode('utf8')
    update_str = '%s\t%s\n' % (entity, enttype)

    if wlock.acquire():#lock
        if entity in dic:
            try:
                lines = open(dicfile).readlines()
                flen = len(lines)
                for i in range(flen):
                    tmpent = lines[i].split('\t')[0]
                    if tmpent == entity:    #UTF8
                        lines[i] = update_str
                open(dicfile,'w').writelines(lines)

                dic[entity] = enttype
                returndata = {"code":0, "info":'Revised Successfully.'}
            except Exception, e:
                returndata = {"code":-1, "info":str(e)}
        else:
            returndata = {"code":1, "info":'There is no entity named as %s.' % entity}   
        wlock.release()
    else:
        returndata = {"code":-1, "info": 'Sorry, another user is operating on the dictionary file.'}
    return returndata

def searchEntity(dic, entity):
    entity = entity.encode('utf8')
    if entity in dic:
        returndata = {"code":0, "info": 'True'}
    else:
        returndata = {"code":-1, "info":'False'}     
    return returndata            

def excuteOperation(operation, entity, enttype, replaceFlag):
    global dicfile, medi_dic, nomedi_dic
    if operation == 'add':
        if entity == '' or enttype == '':
            return {"code":-1, "info":'The entity and type couldn\'t be empty.'}
        if replaceFlag == '':
            replaceFlag = 'y'
        return addEntity(dicfile, medi_dic, entity, enttype, replaceFlag)
    elif operation == 'delete':
        if entity == '':
            return {"code":-1, "info":'The entity couldn\'t be empty.'}
        return delEntity(dicfile, medi_dic, entity, enttype)
    elif operation == 'revise':
        if entity == '' or enttype == '':
            return {"code":-1, "info":'The entity and type couldn\'t be empty.'}
        return reviseEntity(dicfile, medi_dic, entity, enttype)     
    elif operation == 'search':
        return searchEntity(nomedi_dic, entity)
    else:
        return {"code":-1, "info":'The operation is illegal.'}

class IndexHandler(tornado.web.RequestHandler):
    def post(self):
        operation = self.get_argument("operation", '')
        entity = self.get_argument("entity", '')
        enttype = self.get_argument("enttype", '')
        replaceFlag = self.get_argument("replaceFlag", '')
        
        result = excuteOperation(operation, entity, enttype, replaceFlag)
        self.write(json.dumps(result))

if __name__ == '__main__':
    #global dicfile, medi_dic, nomedi_dic
    root = sys.path[0]
    dicfolder = os.path.join(root, 'dic')
    dicfile = os.path.join(dicfolder, 'dic.txt')
    nomedi_dicfile = os.path.join(dicfolder, 'nonmedicine.txt')
    medi_dic = tools.loadDic(dicfile)
    nomedi_dic = tools.loadList(nomedi_dicfile)

    tornado.options.parse_command_line()
    app = tornado.web.Application([(r'/', IndexHandler)], debug=False) # True 为服务自动重启
    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.instance().start()

