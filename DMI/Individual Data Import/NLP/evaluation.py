#!/usr/bin/env python
# coding: utf-8

import generateFeature

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
    ('social_insurance' , '社保'),
    ('all', '总计')
]

def measurePRF(ent_count_result):
    epsilon = 0.000001
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

def countClassPrecision(sen_tag_lists1, sen_tag_lists2, sen_ent_list, ebao_dic):
    ebao_dic = {}
    ent_types = generateFeature.typelist
    count_result = {}
    for ent_type in ent_types:
        count_result[ent_type] = [0, 0, 0] # TP, FP, FN
    count_result['all'] = [0, 0, 0]
    print 'sen_tag_lists1:%d, sen_ent_list:%d\n' % (len(sen_tag_lists1), len(sen_ent_list))
    for i in range(len(sen_tag_lists1)):
        if ebao_dic.has_key(sen_ent_list[i][1]):
            system_type = ebao_dic[sen_ent_list[i][1]]
            if system_type in ['medicine_ot', 'medicare']:
                system_type = 'medicine'
        else:
            system_type = sen_tag_lists1[i][0]
        if system_type == sen_tag_lists2[i][0]:
            count_result[system_type][0] += 1
            count_result['all'][0] += 1
        else:
            if sen_tag_lists2[i][0] not in ent_types: continue
            # print '%s\t\t\t%s' % (system_type, sen_tag_lists2[i][0])
            # print '%s\t\t\t%s' % (sen_ent_list[i][0], sen_ent_list[i][1])
            count_result[system_type][1] += 1
            count_result[sen_tag_lists2[i][0]][2] += 1
            count_result['all'][1] += 1
            count_result['all'][2] += 1
    return count_result

def sameStartIndex(ent_start, ent_set_list):
    for i in range(len(ent_set_list)):
        ent_s = ent_set_list[i][0]
        if ent_start == ent_s:
            return i
    return -1

def countEntList(sen_ent_list1, sen_ent_list2, start_end_list1, start_end_list2, sen_inside_ent_list):
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
    count_result_o['medicine_all'] = [0, 0, 0]
    count_result_o['diagnosis_treatment'] = [0, 0, 0]
    
    # count_result['without_type'] = [0, 0, 0]
    # # without_type
    # count_result['entity'] = [0, 0, 0]
    print 'sen_ent_list1:%d, sen_ent_list2:%d, sen_inside_ent_list:%d\n' % (len(sen_ent_list1), len(sen_ent_list2), len(sen_inside_ent_list))
    flag = 0
    for i in range(len(sen_ent_list1)):
        for j in range(len(sen_ent_list1[i])): # sen_ent_list1[i][j]: (start, end, type)
            if sen_ent_list1[i][j] in sen_ent_list2[i]: # TP
                count_result[sen_ent_list1[i][j][2]][0] += 1
                count_result['all'][0] += 1
                count_result_o[sen_ent_list1[i][j][2]][0] += 1
                count_result_o['all'][0] += 1
            else: # FP
                count_result[sen_ent_list1[i][j][2]][1] += 1
                count_result['all'][1] += 1
                sentence = sen_inside_ent_list[i][0][0]
                sentence_unicode = sentence.decode('utf-8')
                system_content = sentence_unicode[sen_ent_list1[i][j][0]:sen_ent_list1[i][j][1]].encode('utf-8')
                # print 'system: %s\t\t\t%s\t%s' % (sentence, system_content, sen_ent_list1[i][j][2])
                flag = 1
                # 判断list2中是否有start相同的entity
                start1 = sen_ent_list1[i][j][0]
                m = sameStartIndex(start1, sen_ent_list2[i])
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
                        # print 'same_start, system: %s\t\t\t%s\t%s' % (sentence, sentence_unicode[start1:end1].encode('utf-8'), sen_ent_list1[i][j][2])
                        # print 'same_start, gold: %s\t\t\t%s\t%s' % (sentence, sentence_unicode[start2:end2].encode('utf-8'), sen_ent_list2[i][m][2])
                        continue
                count_result_o[sen_ent_list1[i][j][2]][1] += 1 # FP
                count_result_o['all'][1] += 1
                # print 'system: %s\t\t\t%s\t%s' % (sentence, system_content, sen_ent_list1[i][j][2])
            # if start_end_list1[i][j] in start_end_list2[i]: # TP
            #     count_result['without_type'][0] += 1
            # else: # FP
            #     count_result['without_type'][1] += 1
        for k in range(len(sen_ent_list2[i])):
            if sen_ent_list2[i][k][2] not in ent_types: continue
            if sen_ent_list2[i][k] not in sen_ent_list1[i]: # FN
                count_result[sen_ent_list2[i][k][2]][2] += 1
                count_result['all'][2] += 1
                sentence = sen_inside_ent_list[i][0][0]
                sentence_unicode = sentence.decode('utf-8')
                system_content = sentence_unicode[sen_ent_list2[i][k][0]:sen_ent_list2[i][k][1]].encode('utf-8')
                # print 'gold: %s\t\t\t%s\t%s' % (sentence, system_content, sen_ent_list2[i][k][2])
                flag = 1
            else:
                continue
            start2 = sen_ent_list2[i][k][0]
            m = sameStartIndex(start2, sen_ent_list1[i])
            if m != -1:
                end2 = sen_ent_list2[i][k][1]
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
            # print 'gold: %s\t\t\t%s\t%s' % (sentence, system_content, sen_ent_list2[i][k][2])
            # if start_end_list2[i][k] not in start_end_list1[i]: # FN
            #     count_result['without_type'][2] += 1
        if flag == 1:
            flag = 0
            # print '\n'
    return count_result, count_result_o

# [[(start11, end11, type11), ..., (start1n, end1n, type1n)], ..., [(startm1, endm1, typem1), ..., (startmk, endmk, typemk)]]
def generateEntList(sen_tag_lists):
    ent_list = []
    start_end_list = []
    for sen_tag_list in sen_tag_lists:
        sen_ent_list = []
        ent_start_end_list = []
        type_begin = 'O'
        type_end = 'O'
        start = 0
        end = 0
        length = len(sen_tag_list)
        for i in range(length):
            if sen_tag_list[i] == 'O':
                if end != 0:
                    end = i
                    sen_ent_list.append((start, end, type_end))
                    ent_start_end_list.append((start, end))
                    start = 0
                    end = 0
                continue
            tag_parts = sen_tag_list[i].split('-')
            tag = tag_parts[0]
            ent_type = tag_parts[1]
            if tag == 'S':
                start = i
                end = i + 1
                sen_ent_list.append((start, end, ent_type))
                ent_start_end_list.append((start, end))
                start = 0
                end = 0
                continue
            if tag == 'B':
                start = i
                end = i + 1
                type_begin = ent_type
                type_end = ent_type
                continue
            if tag == 'I':
                if i == 0: # I开头
                    start = i
                    end = i + 1
                    type_begin = ent_type
                    type_end = ent_type
                    continue
                elif i > 0: # I开头
                    if sen_tag_list[i - 1] == 'O':
                        start = i
                        end = i + 1
                        type_begin = ent_type
                        type_end = ent_type
                        continue
                if ent_type != type_begin:
                    print sen_tag_list, i
                end += 1
                type_end = ent_type
                continue
            if tag == 'E':
                if ent_type != type_begin:
                    print sen_tag_list, i
                end += 1
                sen_ent_list.append((start, end, ent_type))
                ent_start_end_list.append((start, end))
                start = 0
                end = 0
                continue
        ent_list.append(sen_ent_list)
        start_end_list.append(ent_start_end_list)
    return ent_list, start_end_list

# [[tag11, tag12, ..., tag1n], ..., [tagm1, tagm2, tagmk]]
def generateTagList(inputfile):
    f = open(inputfile, 'r')
    fr = f.read()
    f.close()
    tag_strings = fr.strip().split('\n\n')
    tag_list = []
    for tag_string in tag_strings:
        tags = tag_string.split('\n')
        tag_list.append(tags)
    return tag_list

def eval(inputdata, result, result_type, sen_ent_file, ebao_dic):
    # 生成标准数据的标记结果
    gold_result = inputdata + '.tag'
    gr = open(gold_result, 'w')
    f_inputdata = open(inputdata, 'r')
    lines = f_inputdata.readlines()
    f_inputdata.close()
    for line in lines:
        if line.strip() == '':
            gr.write('\n')
        else:
            tag = line.strip().split('\t')[0]
            gr.write(tag + '\n')
    gr.close()

    # 生成句子-内部实体的list和句子-实体的list
    f_sen_ent = open(sen_ent_file, 'r') # sentence\tenitty\ttype
    fr = f_sen_ent.read()
    f_sen_ent.close()
    parts = fr.strip().split('\n\n')
    sen_inside_ent_list = []
    sen_ent_list = []
    for part in parts:
        s_e_list = []
        sen_ents = part.split('\n')
        for sen_ent in sen_ents:
            s_e_t = sen_ent.split('\t')
            s_e = s_e_t[:2]
            if s_e != ['']:
                sen_ent_list.append(s_e)
            s_e_list.append(s_e)
        sen_inside_ent_list.append(s_e_list)

    # 1是系统结果，2是标准数据
    sen_tag_lists1 = generateTagList(result)
    sen_tag_lists2 = generateTagList(gold_result)
    if result_type == 'boundary':
        sen_ent_list1, start_end_list1 = generateEntList(sen_tag_lists1)
        sen_ent_list2, start_end_list2 = generateEntList(sen_tag_lists2)
        ent_count_result, ent_count_result_o = countEntList(sen_ent_list1, sen_ent_list2, start_end_list1, start_end_list2, sen_inside_ent_list)
        # measurePRF(ent_count_result)
        measurePRF(ent_count_result_o)
        #print len(sen_tag_lists1), len(sen_tag_lists2), len(sen_ent_list1), len(sen_ent_list2), ent_count_result
    else:
        ent_count_result = countClassPrecision(sen_tag_lists1, sen_tag_lists2, sen_ent_list, ebao_dic)
        measurePRF(ent_count_result)
