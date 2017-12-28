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
import xlrd
import xlwt

import tools
import generateFeature
import postProcessing
import evaluation
import crfsuite

root = sys.path[0]
dicfolder = os.path.join(root, 'dic')
ebao_dic = tools.loadDic(os.path.join(dicfolder, 'dic.txt'))

class Entity(object):
    def __init__(self, con, start, end, t):
        self.content = con
        self.start_pos = start
        self.end_pos = end
        self.type = t

en_cn_list = [
    ('disease' , '疾病'),
    ('symptom' , '症状'),
    ('diagnosis_name' , '辅助检查'),
    ('treatment' , '治疗项目'),
    ('other_diagnosis' , '其他诊疗项目'),
    ('medicine_cn' , '药品-通用名'),
    ('medicine_pn' , '药品-产品名'),
    ('medicine_mn' , '药品-商品名'),
    ('medicine' , '药品'),
    ('dosage_form' ,'剂型'),
    ('specifications' , '规格'),
    ('packing_spe' , '包装规格'),
    ('packing_material' , '包材'),
    ('instrument' , '医疗器材'),
    ('healthy_food' , '保健食品'),
    ('department' , '科室'),
    ('enterprise' , '企业机构'),
    ('address' , '地址'),
    ('diagnosis_treatment' , '诊疗'),
    ('medicine_all' , '药品汇总'),
    ('all', '总计')
]

#实体类型 英汉对照
en_cn_dic = {
    'disease' : '疾病',
    'symptom' : '症状',
    'diagnosis_treatment' : '诊疗',
    'diagnosis_name' : '辅助检查',
    'treatment' : '治疗项目',
    'other_diagnosis' : '其他诊疗项目',
    'instrument' : '医疗器材',
    'medicine' : '药品',
    'medicine_ot' : '药品其他',
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
    'entity' : '实体'
}

#实体类型 汉英对照 多对一
ch_en_dic = {   
    '疾病' : 'disease',
    '症状' : 'symptom',
    '治疗项目' : 'treatment',
    '辅助检查' : 'diagnosis_name',
    '理疗项目' : 'treatment',
    '其他诊疗项目' : 'other_diagnosis',
    '诊疗': 'diagnosis_treatment',
    '器材' : 'instrument',
    '药品-通用名' : 'medicine_cn',
    '药品-产品名' : 'medicine_pn',
    '药品-商品名' : 'medicine_mn',
    '药品名': 'medicine',
    '剂型' : 'dosage_form',
    '规格' : 'specifications',
    '药品规格' : 'specifications',
    '包装规格' : 'packing_spe',
    '药品包装规格' : 'packing_spe',
    '包材' : 'packing_material',
    '企业机构' : 'enterprise',
    '药品': 'medicine',
    '药品通用名' : 'medicine_cn',
    '药品产品名' : 'medicine_pn',
    '药品商品名' : 'medicine_mn',
    '医疗器械' : 'instrument',
    '医疗器材' : 'instrument',
    '科室' : 'department',
    '地址': 'address',
}

def measurePRF(ent_count_result):
    epsilon = 0.000001
    result = {}
    length = len(en_cn_list)
    for i in range(length):
        entity_type = en_cn_list[i][0]
        if entity_type not in ent_count_result.keys(): continue
        if ent_count_result[entity_type][0] == 0:
            precision = 0.0
            recall = 0.0
            f1 = 0.0
        else:
            precision = (ent_count_result[entity_type][0] + epsilon)/ (ent_count_result[entity_type][0] + ent_count_result[entity_type][1])
            recall = (ent_count_result[entity_type][0] + epsilon)/ (ent_count_result[entity_type][0] + ent_count_result[entity_type][2])
            f1 = 2 * precision * recall / (precision + recall)
        print '| %s | %d | %d | %d | %.4f | %.4f | %.4f|' % (en_cn_list[i][1], ent_count_result[entity_type][0], ent_count_result[entity_type][1], ent_count_result[entity_type][2], precision, recall, f1)
        result[entity_type] = (ent_count_result[entity_type][0], ent_count_result[entity_type][1], ent_count_result[entity_type][2], precision, recall, f1)
    return result

def countEntList(sen_ent_list1, sen_ent_list2, start_end_list1, start_end_list2, sen_inside_ent_list, type_error):
    off_set = ['病史', '史', '症', '症状', '检查', '后', '术后', '阴性', '阳性', '（+）', '（-）', '治疗', '诊疗']
    ent_types = generateFeature.typelist
    ent_types.append('entity')
    count_result = {}
    count_result_o = {}
    for ent_type in ent_types:
        count_result[ent_type] = [0, 0, 0] # TP, FP, FN
        count_result_o[ent_type] = [0, 0, 0]
    count_result['all'] = [0, 0, 0]
    count_result_o['all'] = [0, 0, 0]

    result_entities = [[], [], []]  #TP FP FN
    flag = 0
    for i in range(len(sen_ent_list1)):
        try:
            sentence = sen_inside_ent_list[i][0][0]
        except Exception as e:
            print e
            print i
            print sen_inside_ent_list[0]
        sentence_unicode = sentence.decode('utf-8')
        for j in range(len(sen_ent_list1[i])): # sen_ent_list1[i][j]: (start, end, type)
            system_content, enttype = sentence_unicode[sen_ent_list1[i][j][0]:sen_ent_list1[i][j][1]].encode('utf-8'), sen_ent_list1[i][j][2]
            if sen_ent_list1[i][j] in sen_ent_list2[i]: # TP
                count_result[sen_ent_list1[i][j][2]][0] += 1
                count_result['all'][0] += 1
                count_result_o[sen_ent_list1[i][j][2]][0] += 1
                count_result_o['all'][0] += 1
                result_entities[0].append((system_content, enttype))
            else: # FP
                count_result[sen_ent_list1[i][j][2]][1] += 1
                count_result['all'][1] += 1
                flag = 1
                # 判断list2中是否有start相同的entity
                start1 = sen_ent_list1[i][j][0]
                m = evaluation.sameStartIndex(start1, sen_ent_list2[i])
                if m != -1:
                    end1 = sen_ent_list1[i][j][1]
                    type1 = sen_ent_list1[i][j][2]
                    start2 = sen_ent_list2[i][m][0]
                    end2 = sen_ent_list2[i][m][1]
                    type2 = sen_ent_list2[i][m][2]
                    if end1 > end2:
                        off_string = sentence_unicode[end2:end1].encode('utf-8')
                    else:
                        off_string = sentence_unicode[end1:end2].encode('utf-8')
                    if off_string in off_set and type1 == type2: # TP
                        count_result_o[sen_ent_list1[i][j][2]][0] += 1
                        count_result_o['all'][0] += 1
                        result_entities[0].append((sentence_unicode[start2:end2].encode('utf-8'), type2))
                        continue
                count_result_o[sen_ent_list1[i][j][2]][1] += 1 # FP
                count_result_o['all'][1] += 1
                result_entities[1].append((system_content, enttype))

        for k in range(len(sen_ent_list2[i])):
            if sen_ent_list2[i][k][2] not in ent_types: continue
            if sen_ent_list2[i][k] not in sen_ent_list1[i]: # FN
                count_result[sen_ent_list2[i][k][2]][2] += 1
                count_result['all'][2] += 1
                flag = 1
            else:
                continue
            start2 = sen_ent_list2[i][k][0]
            end2 = sen_ent_list2[i][k][1]
            m = evaluation.sameStartIndex(start2, sen_ent_list1[i])
            if m != -1:
                start1 = sen_ent_list1[i][m][0]
                end1 = sen_ent_list1[i][m][1]
                if end1 > end2:
                    off_string = sentence_unicode[end2:end1].encode('utf-8')
                else:
                    off_string = sentence_unicode[end1:end2].encode('utf-8')
                if off_string in off_set: # TP, 避免重复计算TP
                    continue

            count_result_o[sen_ent_list2[i][k][2]][2] += 1
            count_result_o['all'][2] += 1
            result_entities[2].append((sentence_unicode[start2:end2].encode('utf-8'), sen_ent_list2[i][k][2]))

        if flag == 1:
            flag = 0

    return count_result, count_result_o, result_entities, type_error

def mainfunction(sen, postProcess, texttype, index):
    model_b = os.path.join(root, './models/boundarymodel-' + index)
    model_c = os.path.join(root, './models/classmodel-' + index)

    ner_lines = ''

    tagger_b = crfsuite.Tagger()
    tagger_b.open(model_b)
    tagger_c = crfsuite.Tagger()
    tagger_c.open(model_c)
    bieso = ['B-entity', 'I-entity', 'E-entity', 'S-entity', 'O']
   
    line = sen.strip()

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
    tagger_b.set(xseq)
    yseq_b = tagger_b.viterbi()

    line_unicode = line.decode('utf-8')

    model_chosen = '2layer'
    # class
    sen_ent_list1, start_end_list1 = evaluation.generateEntList([yseq_b])

    length = len(sen_ent_list1[0])
    # length 为0时
    entities = []
    new_entities = []
    for j in range(length):
        ent_start = sen_ent_list1[0][j][0]
        ent_end = sen_ent_list1[0][j][1]
        ent_type = sen_ent_list1[0][j][2]
        ent_content = line_unicode[ent_start:ent_end].encode('utf-8')
        entities.append(Entity(ent_content, ent_start, ent_end, ent_type))
    feature_c, sen_ent4error = generateFeature.classFeatureGeneration(line, entities, ebao_dic, texttype)
    instances = feature_c.strip().split('\n\n')
    ents_type = []
    for instance in instances:
        xseq = crfsuite.ItemSequence()
        fields = instance.split('\t')
        item = crfsuite.Item()
        for field in fields[1:]:
            item.append(crfsuite.Attribute(field))
        xseq.append(item)
        tagger_c.set(xseq)
        yseq_c = tagger_c.viterbi()
        ents_type.append(yseq_c[0])
    # postProcessing
    
    new_yseq = ['O' for i in range(len(line_unicode))]
    for j in range(len(entities)):
        start = entities[j].start_pos
        end = entities[j].end_pos
        enttype = ents_type[j]
        if start + 1 == end:
            new_yseq[start] = 'S-' + enttype
            continue
        new_yseq[start] = 'B-' + enttype
        for k in range(start + 1, end - 1):
            new_yseq[k] = 'I-' + enttype
        new_yseq[end - 1] = 'E-' + enttype

    if postProcess == '1': # 评价中的start_end_list没有调整
        new_yseq = postProcessing.twoProcessings(line_unicode, new_yseq, ebao_dic, texttype)

    ents1, s_e_list1 = evaluation.generateEntList([new_yseq])
    new_entities = ents1[0]

    entity_list = ''
    length = len(new_entities)
    for i in range(length):
        content = line_unicode[new_entities[i][0]:new_entities[i][1]]
        enttype = new_entities[i][2]
        if enttype == '':
            print line_unicode.encode('utf8'), line_unicode[new_entities[i][0]:new_entities[i][1]].encode('utf8')
        entity_list += content.encode('utf8') + '[' + en_cn_dic[enttype] + ']\n'
    return entity_list, new_yseq

def writeToExcel(excelfile, resultfilename, postProcess, texttype, model_index):
    testdata = xlrd.open_workbook(excelfile)
    table = testdata.sheets()[0]
    ncols = table.ncols
    nrows = table.nrows

    resultfile = xlwt.Workbook()
    result_table = resultfile.add_sheet('sheet1')

    result_table.write(0, 0, '原始数据'.decode('utf-8'))
    result_table.write(0, 1, '标准结果'.decode('utf-8'))
    result_table.write(0, 2, '系统输出'.decode('utf-8'))
    result_table.write(0, 3, '识别正确'.decode('utf-8'))
    result_table.write(0, 4, '识别错误'.decode('utf-8'))
    result_table.write(0, 5, '未识别出'.decode('utf-8'))

    ent_re = re.compile(r'^(.+)\[(.+)\]$')

    off_set_b = ['有', '可见', '口服', '可闻', '闻及', '无']
    
    result, result_o = {}, {}
    for ent_type in generateFeature.typelist:
        result[ent_type] = [0, 0, 0] # TP, FP, FN
        result_o[ent_type] = [0, 0, 0]
    result['all'] = [0, 0, 0]
    result_o['all'] = [0, 0, 0]
    result['entity'] = [0, 0, 0]
    result_o['entity'] = [0, 0, 0]

    type_error = {}
    for i in range(1, nrows):
        excel_line = table.cell(i, 0).value.encode('utf8')
        line = tools.uniformSignal(excel_line)

        line = line.replace('\n', ';') 

        correct_ents_str = table.cell(i, 2).value.encode('utf8')
        correct_ents_str = tools.uniformSignal(correct_ents_str)

        correct_ents_str = correct_ents_str.replace('【','[')
        correct_ents_str = correct_ents_str.replace('】',']')

        result_table.write(i, 0, line.decode('utf8')) #第一列:句子
        result_table.write(i, 1, correct_ents_str.decode('utf8'))  #第二列：标准答案
        
        correct_ents = []   #[(content,type), (content, type), ...]
        correct_ents_list = correct_ents_str.strip().split('\n')
        ent_re = re.compile(r'^(.+)\[(.+)\]$')
        for ent in correct_ents_list:
            match = ent_re.match(ent)
            if match:
                ent_con = match.group(1)
                if ent_con in ['神清', '瞳孔等大同圆', '对光反射正常', '查体']:
                    continue
                enttype = match.group(2)
                try:
                    enttype = ch_en_dic[enttype]

                except Exception:
                    print enttype
                correct_ents.append((ent_con, enttype))


        correct_seq = []
        system_seq = []
        ents_str = ''
        ent_index = 0

        for sen in tools.sentence_split(line):
            #将标准答案转化成tags形式
            sentence = sen.decode('utf8')
            correct_tags = ['O' for x in range(len(sentence))]
            sentence_index = 0

            while sentence_index < len(sentence) and ent_index < len(correct_ents):             
                tmp = sentence[sentence_index:]
                content, enttype = correct_ents[ent_index]
                content_unicode = content.decode('utf8')
                
                if tmp.startswith(content_unicode):
                    for keyword in off_set_b:
                        if content.startswith(keyword):
                            keyword_length = len(keyword.decode('utf-8'))
                            sentence_index += keyword_length
                            content_unicode = content_unicode[keyword_length:]
                            break
                    try:
                        content_length = len(content_unicode)
                    except Exception as e:
                        print e
                        print sen
                        print content_unicode
                    if content_length == 1:
                        correct_tags[sentence_index] = 'S-' + enttype
                    else:
                        correct_tags[sentence_index] = 'B-' + enttype
                        for index in range(sentence_index+1, sentence_index + content_length - 1):
                            correct_tags[index] = 'I-'  + enttype
                        correct_tags[sentence_index + content_length - 1] = 'E-' + enttype
                    sentence_index += content_length
                    ent_index += 1
                else:
                    sentence_index += 1
            correct_seq.extend(correct_tags)
            #系统输出与tags形式
            
            ent_str, system_tags = mainfunction(sen, postProcess, texttype, model_index)
            ents_str += ent_str
            system_seq.extend(system_tags)

        if ent_index < len(correct_ents):
            print 'Maybe there is an error.'
            print line, ent_index
            for ent in correct_ents:
                print ent[0], ent[1]
            print correct_seq
        
        s_e_list = []
        if len(correct_ents) == 0:
            s_e_list = [(line, '')]
        else:
            for k in range(len(correct_ents)):
                s_e_list.append((line, ''))

        #boundary
        sen_ent_list2, start_end_list2 = evaluation.generateEntList([correct_seq])
        sen_ent_list1, start_end_list1 = evaluation.generateEntList([system_seq])
        
        count_result, count_result_o, result_entities, type_error = countEntList(sen_ent_list1, sen_ent_list2, start_end_list1, start_end_list2, [s_e_list], type_error)

        result_table.write(i, 2, ents_str.decode('utf-8'))      #第三列：系统输出

        #TP
        tp, fp, fn = '', '', ''
        for content, enttype in result_entities[0]:
            tp += content + '【' + en_cn_dic[enttype]+ '】\n'
        for content, enttype in result_entities[1]:
            fp += content + '【' + en_cn_dic[enttype]+ '】\n'
        for content, enttype in result_entities[2]:
            fn += content + '【' + en_cn_dic[enttype]+ '】\n'
        # count_result, count_result_o, result_entities
        result_table.write(i, 3, tp.decode('utf8')) #第四列:识别正确
        result_table.write(i, 4, fp.decode('utf8')) #第五列：识别错误
        result_table.write(i, 5, fn.decode('utf8')) #第六列：未识别出
        for key in count_result.keys():
            for tmpi in range(3):
                result[key][tmpi] += count_result[key][tmpi]
                result_o[key][tmpi] += count_result_o[key][tmpi]
      
    row_index = nrows+1
    result_table.write(row_index, 0, '结果'.decode('utf8'))

    result2 = measurePRF(result_o)
    for key in result2.keys():
        row_index += 1
        if key == 'all':
            result_table.write(row_index, 0, key.decode('utf8'))
        else:
            result_table.write(row_index, 0, en_cn_dic[key].decode('utf8'))

        result_table.write(row_index, 1, 'TP=' + str(result2[key][0]))
        result_table.write(row_index, 2, 'FP=' + str(result2[key][1]))
        result_table.write(row_index, 3, 'FN=' + str(result2[key][2]))
        result_table.write(row_index, 4, 'P=' + str(result2[key][3]))
        result_table.write(row_index, 5, 'R=' + str(result2[key][4]))
        result_table.write(row_index, 6, 'F=' + str(result2[key][5]))
    resultfile.save(resultfilename)
    return

if __name__ == '__main__':
    datafolder = os.path.join(root, 'data')

    excelfile = os.path.join(datafolder, 'jsd4evaluation.xlsx')
    resultfile = os.path.join(datafolder, 'jsd4evaluation_result_post.xls')
    writeToExcel(excelfile, resultfile, '1', 'jsd', '2')

    excelfile = os.path.join(datafolder, 'unstructured4evaluation.xlsx')
    resultfile = os.path.join(datafolder, 'unstructured4evaluation_result_post.xls')
    writeToExcel(excelfile, resultfile, '1', 'un', '3')
