#!/usr/bin/env python
#coding:utf-8

import os
import sys
import json
import time
import uuid

import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web

import main


dir_run = "./run/"
dir_backup = "./data/backup"
file_middle = "./data/train.txt"
file_log = "./data/backup/server2.log"

class TrainHandler(tornado.web.RequestHandler):
    def get(self):
        # 申请锁
        for root, dirs, files in os.walk(dir_run):
            for fname in files:
                fpname = os.path.join(root, fname)
                if fpname.endswith('.lock'):
                    self.write('Busy')
                    return
            break
        file_lock = os.path.join(dir_run, str(os.getpid()) + ".lock")
        open(file_lock, 'w').close()

        time.sleep(1)
        count = 0;
        for root, dirs, files in os.walk(dir_run):
            for fname in files:
                fpname = os.path.join(root, fname)
                if fpname.endswith('.lock'):
                    count += 1
            break
        if count >= 2:
            self.write('Busy')
            os.remove(file_lock)
            return
        ########
        uu = uuid.uuid4()
        flog = open(file_log, 'a')
        msg = "%s\t%s\t%s\t%s\t%s- Start training.\n"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri, uu)
        flog.write(msg)
        flog.close()

        if os.path.exists(file_middle):
            try:
                main.main('train.txt', 'train', 'prepareTrain', False)
                self.write("OK")
                flog = open(file_log, 'a')
                msg = "%s\t%s\t%s\t%s\t%s- OK.\n"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri, uu)
                flog.write(msg)
                flog.close()
            except:
                self.write("Error\n语料解析错误。")
                flog = open(file_log, 'a')
                msg = "%s\t%s\t%s\t%s\t%s- Error.语料解析错误。\n"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri, uu)
                flog.write(msg)
                flog.close()
        else:
            self.write("Error\n没有待训练的语料文件。")
            flog = open(file_log, 'a')
            msg = "%s\t%s\t%s\t%s\t%s- Error.没有待训练的语料文件。\n"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri, uu)
            flog.write(msg)
            flog.close()

        # 释放锁
        os.remove(file_lock)
        return


class UploadHandler(tornado.web.RequestHandler):
    def post(self):
        # 申请锁
        for root, dirs, files in os.walk(dir_run):
            for fname in files:
                fpname = os.path.join(root, fname)
                if fpname.endswith('.lock'):
                    self.write('Busy')
                    return
            break
        file_lock = os.path.join(dir_run, str(os.getpid()) + ".lock")
        open(file_lock, 'w').close()

        time.sleep(1)
        count = 0;
        for root, dirs, files in os.walk(dir_run):
            for fname in files:
                fpname = os.path.join(root, fname)
                if fpname.endswith('.lock'):
                    count += 1
            break
        if count >= 2:
            self.write('Busy')
            os.remove(file_lock)
            return
        ########
        uu = uuid.uuid4()
        flog = open(file_log, 'a')
        msg = "%s\t%s\t%s\t%s\t%s- Upload.\n"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri, uu)
        flog.write(msg)
        flog.close()

        # 测试
        self.parse_data(self.request.body)
        self.write("OK")
        os.remove(file_lock)
        return
        #######

        try:
            if self.parse_data(self.request.body):
                self.write("OK")
                flog = open(file_log, 'a')
                msg = "%s\t%s\t%s\t%s\t%s- OK.\n"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri, uu)
                flog.write(msg)
                flog.close()
            else:
                self.write("Error\n语料内容错误1。")
                flog = open(file_log, 'a')
                msg = "%s\t%s\t%s\t%s\t%s- Error.语料内容错误1.\n"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri, uu)
                flog.write(msg)
                flog.close()
        except:
            self.write("Error\n语料内容错误2。")
            flog = open(file_log, 'a')
            msg = "%s\t%s\t%s\t%s\t%s- Error.语料内容错误2.\n"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri, uu)
            flog.write(msg)
            flog.close()

        # 释放锁
        os.remove(file_lock)
        return

    def parse_data(self, data):
        '''
以 critical_char 所指定的4个字符（英文分号，中文分号，中文句号，换行符）为分隔符，
对句子进行分割，同时也对句子所包含的实体进行分离，重新计算位置坐标。
        '''
        critical_char = u';；。\n'
        f = open(file_middle, 'a+')
        list_yl = json.loads(data)
        for dic_line in list_yl:
            str_ori_sentence = dic_line["origCorpus"]
            lines = []

            list_pos = []
            for i in xrange(len(str_ori_sentence)):
                if str_ori_sentence[i] in critical_char:
                    list_pos.append(i)

            if len(list_pos) == 0:
                line = str_ori_sentence + '\t'
                for dic_entity_item in dic_line["entityList"]:
                    line += "\t\t" + dic_entity_item["entityName"]
                    line += "\t" + dic_entity_item["startOff"]
                    line += "\t" + dic_entity_item["endOff"]
                    line += "\t" + dic_entity_item["entityType"]
                if len(line.strip()) > 0:
                    lines.append(line)
            else:
                ptr_pos = 0
                ptr_entity = 0
                number_entity = len(dic_line["entityList"])
                for pos in list_pos:
                    line = str_ori_sentence[ptr_pos:pos] + "\t"
                    while ptr_entity < number_entity:
                        dic_entity_item = dic_line["entityList"][ptr_entity]
                        if pos < int(dic_entity_item["startOff"]):
                            break
                        elif int(dic_entity_item["endOff"])-1 < pos:
                            line += "\t\t" + dic_entity_item["entityName"]
                            line += "\t" + str(int(dic_entity_item["startOff"]) - ptr_pos)
                            line += "\t" + str(int(dic_entity_item["endOff"]) - ptr_pos)
                            line += "\t" + dic_entity_item["entityType"]
                        else:
                            str_ori_sentence[pos] = " "
                        ptr_entity += 1
                    ptr_pos = pos+1
                    if len(line.strip()) > 0:
                        lines.append(line)

            for line in lines:
                f.write(line.encode('utf-8'))
                f.write('\n')
        f.close()
        return True


class StatusHandler(tornado.web.RequestHandler):
    def get(self):
        flog = open(file_log, 'a')
        msg = "%s\t%s\t%s\t%s\t"%(time.asctime(time.localtime()), self.request.remote_ip, self.request.method, self.request.uri)
        flog.write(msg)
        flog.close()

        for root, dirs, files in os.walk(dir_run):
            for fname in files:
                fpname = os.path.join(root, fname)
                if fpname.endswith('.lock'):
                    self.write('Busy')
                    flog = open(file_log, 'a')
                    msg = "Busy.\n"
                    flog.write(msg)
                    flog.close()
                    return
            break
        self.write("Idle")
        flog = open(file_log, 'a')
        msg = "Idle.\n"
        flog.write(msg)
        flog.close()
        return


class MainHandler(tornado.web.RequestHandler):
    def get(self):
        str_info = 'help'
        self.write(str_info)


def make_app():
    return tornado.web.Application([
        (r'/', MainHandler),
        (r'/upload', UploadHandler),
        (r'/train', TrainHandler),
        (r'/status', StatusHandler),
    ], debug=False) # True 为服务自动重启


def init():
    if not os.path.exists(dir_backup):
        os.mkdir(dir_backup)

    if not os.path.exists(dir_run):
        os.mkdir(dir_run)
    else:
        for root, dirs, files in os.walk(dir_run):
            for fname in files:
                if fname.endswith(".lock"):
                    os.remove(os.path.join(root, fname))
            break

    flog = open(file_log, 'a+')
    msg = "%s\tStart\n"%(time.asctime(time.localtime()))
    flog.write(msg)
    flog.close()



if __name__ == '__main__':
    os.chdir(sys.path[0])
    init()

    app = make_app()
    server = tornado.httpserver.HTTPServer(app)
    server.bind(10033)
    server.start(0)
    tornado.ioloop.IOLoop.current().start()
################################################################################

