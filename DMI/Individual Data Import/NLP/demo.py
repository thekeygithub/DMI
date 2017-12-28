#!/usr/bin/env python
#coding:utf-8
from __future__ import division
import os
import sys
import re
import math
import datetime
import random
import HTMLParser

import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
from tornado.options import define, options
define("port", default=10010, help="run on the given port", type=int)

import crfsuite

import generateFeature
import postProcessing
import evaluation
import tools

root = sys.path[0]
dicfolder = os.path.join(root, 'dic')
ebao_m_dic = tools.loadDic(os.path.join(dicfolder, 'dic.txt'))
ebao_si_dic = tools.loadDic(os.path.join(dicfolder, 'dic_si.txt'))
newtermfile = os.path.join(dicfolder, 'new_term.txt')
if not os.path.exists(newtermfile):
    fw = open(newtermfile, 'w')
    fw.close()

colordic = {\
    'disease': 'ff0000',\
    'symptom': '0000ff',\
    'diagnosis_treatment': '1fb21c',\
    'diagnosis_name': '1fb21c',\
    'treatment': '1fb21c',\
    'other_diagnosis': '1fb21c',\
    'instrument': '0099ff',\
    'medicine': 'fc9037',\
    'medicine_cn': 'fc9037',\
    'medicine_pn': 'fc9037',\
    'medicine_mn': 'fc9037',\
    'dosage_form': 'b1b695',\
    'specifications': 'a864af',\
    'packing_spe': 'd30c7b',\
    'packing_material': 'B29C00',\
    'enterprise': '7246f2',\
    'department': '5d576b',\
    'address': '7246f2',\
    'healthy_food': 'FF56A2',\
    'social_insurance': '0099ff',\
}

en_cn_dic = {
    'disease' : '疾病',
    'symptom' : '症状',
    'diagnosis_treatment' : '诊疗',
    'diagnosis_name': '辅助检查',
    'treatment': '治疗项目',
    'other_diagnosis': '其他诊疗项目',
    'instrument' : '医疗器材',
    'medicine' : '药品',
    'medicine_cn' : '药品-通用名',
    'medicine_pn' : '药品-产品名',
    'medicine_mn' : '药品-商品名',
    'dosage_form' :'剂型',
    'specifications' : '规格',
    'packing_spe' : '包装规格',
    'packing_material' : '包材',
    'enterprise' : '企业机构',
    'department' : '科室',
    'address' : '地址',
    'healthy_food' : '保健食品',
    'social_insurance': '社保',
}

cn_en_dic = dict((y,x) for x,y in en_cn_dic.iteritems())

tagger_b_defalt = crfsuite.Tagger()
tagger_b_defalt.open(os.path.join(root, './models/boundarymodel-6'))
tagger_c_defalt = crfsuite.Tagger()
tagger_c_defalt.open(os.path.join(root, './models/classmodel-6'))

tagger_b_jsd = crfsuite.Tagger()
tagger_b_jsd.open(os.path.join(root, './models/boundarymodel-4'))
tagger_c_jsd = crfsuite.Tagger()
tagger_c_jsd.open(os.path.join(root, './models/classmodel-4'))

tagger_b_un = crfsuite.Tagger()
tagger_b_un.open(os.path.join(root, './models/boundarymodel-5'))
tagger_c_un = crfsuite.Tagger()
tagger_c_un.open(os.path.join(root, './models/classmodel-5'))

tagger_b_si = crfsuite.Tagger()
tagger_b_si.open(os.path.join(root, './models/boundarymodel-si-0'))
tagger_c_si = crfsuite.Tagger()
tagger_c_si.open(os.path.join(root, './models/classmodel-si-0'))

class Entity(object):
    def __init__(self, con, start, end, t):
        self.content = con
        self.start_pos = start
        self.end_pos = end
        self.type = t

def setModel(modeltype):
    global texttype, ebao_dic
    ebao_dic = ebao_m_dic
    if modeltype == "0":  #默认模型
        tagger_b = tagger_b_defalt
        tagger_c = tagger_c_defalt
        texttype = 'un'
    elif modeltype == "1":    #结算单模型
        tagger_b = tagger_b_jsd
        tagger_c = tagger_c_jsd
        texttype = 'jsd'
    elif modeltype == "2":    #非结构化模型
        tagger_b = tagger_b_un
        tagger_c = tagger_c_un
        texttype = 'un'
    elif modeltype == "3":    #社保模型
        tagger_b = tagger_b_si
        tagger_c = tagger_c_si
        texttype = 'si'
        ebao_dic = ebao_si_dic
    else:
        tagger_b = tagger_b_defalt
        tagger_c = tagger_c_defalt
        texttype = 'un'
    return tagger_b, tagger_c

def generateNerInSentence(inputline, yseq, model_type, ebao_dic): # input: unicode; output: utf8
    sen_ner = ''
    entity_list = ''
    tag_list = []
    tag_list1 = []
    if texttype != 'si':
        yseq = postProcessing.twoProcessings(inputline, yseq, ebao_dic, texttype)

    # for t, y in enumerate(yseq1):        
    #     entity_list += '<p>%s\t%s\n</p>' % (inputline[t].encode('utf8'), y)

    tag_list.append(yseq)
    # tag_list1.append(yseq1)
    ents, s_e_list = evaluation.generateEntList(tag_list)
    # ents1, s_e_list1 = evaluation.generateEntList(tag_list1)
    entities = ents[0]
    # print entities
    length = len(entities)
    for i in range(length):
        if i == 0:
            sen_ner += inputline[:entities[i][0]]

        content = inputline[entities[i][0]:entities[i][1]]
        enttype = entities[i][2]
        if model_type == '2layer':
            if enttype in ['medicine_ot', 'medicare']:
                enttype = 'medicine'

        if i == length - 1:
            sen_ner += '<span style="color:#' + colordic[enttype] + ';">' + content + '</span>'
            sen_ner += inputline[entities[i][1]:]
            entity_list += content.encode('utf8') + '【' + en_cn_dic[enttype] + '】<br/>'
        else:
            sen_ner += '<span style="color:#' + colordic[enttype] + ';">' + content + '</span>'
            sen_ner += inputline[entities[i][1]:entities[i+1][0]]
            entity_list += content.encode('utf8') + '【' + en_cn_dic[enttype] + '】<br/>'
            continue
    return sen_ner.encode('utf8'), entity_list

def mainfunction(inputstring, taggerb, taggerc):
    if inputstring == '':
        sentence_ner = '请输入句子'
        return sentence_ner

    # 一些句子预处理
    inputsentence = tools.uniformSignal(inputstring)

    ner_lines = ''

    bieso = ['B-entity', 'I-entity', 'E-entity', 'S-entity', 'O']

    new_term_list = ''

    for single_line in inputsentence.split('\n'):
        lines = tools.sentence_split(single_line)
        ner_line = ''
        term_list = ''
        for line in lines:
            line = line.strip()
            # 去除标签部分，以<开头且以>结尾的过滤
            if line == '' or line[0] == '<' and line[-1] == '>' : continue

            # model_2_layer
            # boundary
            feature_string = ''
            instances = []
            feature_string, tags = generateFeature.boundaryFeatureGeneration(line, [], ebao_dic, 'demo', '0')
            try:
                instances = feature_string.strip().split('\n')
            except AttributeError as e: 
                print 'feature_string:%s.' % feature_string
            xseq = crfsuite.ItemSequence()
            for instance in instances:
                fields = instance.split('\t')
                item = crfsuite.Item()
                for field in fields[2:]:
                    item.append(crfsuite.Attribute(field))
                xseq.append(item)
            taggerb.set(xseq)

            yseq_b = taggerb.viterbi()
            prob_b = taggerb.probability(yseq_b)
            line_unicode = line.decode('utf-8')

            # for t, y in enumerate(yseq_b):
            # # Output the predicted labels with their marginal probabilities.
            #     ner_line  += '%s:%f\n' % (y, taggerb.marginal(y, t))

            model_chosen = '2layer'
            # class
            sen_ent_list1, start_end_list1 = evaluation.generateEntList([yseq_b])

            length = len(sen_ent_list1[0])
            # length 为0时
            sentence = line
            entities = []
            for j in range(length):
                ent_start = sen_ent_list1[0][j][0]
                ent_end = sen_ent_list1[0][j][1]
                ent_type = sen_ent_list1[0][j][2]
                ent_content = sentence.decode('utf-8')[ent_start:ent_end].encode('utf-8')
                entities.append(Entity(ent_content, ent_start, ent_end, ent_type))
            feature_c, sen_ent4error = generateFeature.classFeatureGeneration(sentence, entities, ebao_dic, texttype)
            instances = feature_c.strip().split('\n\n')
            ents_type = []
            for instance in instances:
                xseq = crfsuite.ItemSequence()
                fields = instance.split('\t')
                item = crfsuite.Item()
                for field in fields[1:]:
                    item.append(crfsuite.Attribute(field))
                xseq.append(item)
                taggerc.set(xseq)
                yseq_c = taggerc.viterbi()
                ents_type.append(yseq_c[0])
            new_yseq = ['O' for i in range(len(line_unicode))]
            for j in range(len(entities)):
                start = entities[j].start_pos
                end = entities[j].end_pos
                if start + 1 == end:
                    new_yseq[start] = 'S-' + ents_type[j]
                    continue
                new_yseq[start] = 'B-' + ents_type[j]
                for k in range(start + 1, end - 1):
                    new_yseq[k] = 'I-' + ents_type[j]
                new_yseq[end - 1] = 'E-' + ents_type[j]

            sen_ent_colored, ent_list = generateNerInSentence(line_unicode, new_yseq, model_chosen, ebao_dic)

            new_term_list += ent_list

            if sen_ent_colored == '': sen_ent_colored = line
            # ner_lines += '<p>' + sen_ent_colored + '</p>'
            # ner_lines += '<p>' + ent_list + '</p>'
            ner_line += sen_ent_colored
            term_list += ent_list

        ner_lines += '<p>' + ner_line + '</p>'
        ner_lines += '<p>' + term_list + '</p>'
        ner_lines += '<br/>'
        
    return ner_lines, new_term_list

class IndexHandler(tornado.web.RequestHandler):
    def get(self):
        source = '(甲)氯化钠注射液(0.9%*500ml)\n门诊行头CT检查,显示左侧侧脑室体旁低密度病灶,以"脑梗死"收入我科.\n在诊断本病的时候，除了要抓住延长而严重的恶心、呕吐、脱水、酮症及体重下降等特征外，还应当注意与其他疾病鉴别'
        ner_result = ''
        modeltype = '0'
        self.render('test.html', source=source, ner_result=ner_result, modeltype=modeltype)

    def post(self):
        source_text = self.get_argument('source')
        modeltype = self.get_argument('modeltype')
        #print 'modeltype = ' + modeltype
        taggerB, taggerC = setModel(modeltype)
        gt = source_text.encode('utf-8')
        ner_result, ner_term_result = mainfunction(gt, taggerB, taggerC)
        self.render('test.html', source=gt, ner_result=ner_result, modeltype=modeltype)
        tools.addNewTerms(ner_term_result, ebao_dic, newtermfile, cn_en_dic)

if __name__ == '__main__':
    tornado.options.parse_command_line()
    app = tornado.web.Application([(r'/ebao/', IndexHandler)],\
        template_path=os.path.join(os.path.dirname(__file__), "templates"),\
        static_path=os.path.join(os.path.dirname(__file__), "statics"),\
        debug=False) # True为服务自动重启
    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.instance().start()
