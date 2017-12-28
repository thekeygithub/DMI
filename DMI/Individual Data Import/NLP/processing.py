#!/usr/bin/env python
# coding: utf-8

import generateFeature
import tools
import evaluation
import postProcessing

import re
import crfsuite
import math
import random

class Entity(object):
    def __init__(self, con, start, end, t):
        self.content = con
        self.start_pos = start
        self.end_pos = end
        self.type = t

# 注意给句子中的实体加一个实体顺序编号，方便对齐
def predictClassAfterBoundaryAndEval(boundary_result, sentence_list, sen_tags_list, classmodel_file, ebao_dic, post_processing, texttype):
    tagger = crfsuite.Tagger()
    tagger.open(classmodel_file)
    result_tags_list = evaluation.generateTagList(boundary_result)
    # 1是系统结果，2是标准数据
    sen_ent_list1, start_end_list1 = evaluation.generateEntList(result_tags_list) # 只有一个entity类
    sen_ent_list2, start_end_list2 = evaluation.generateEntList(sen_tags_list) # 多个类别
    length = len(sen_ent_list1)
    new_sen_ent_list1 = []
    sen_inside_ent_list = []
    for i in range(length):
        # 生成对应的实体数组
        sentence = sentence_list[i]
        sentence_unicode = sentence.decode('utf-8')
        entities = []
        new_entities = []
        s_e_list = []
        if len(sen_ent_list1[i]) == 0:
            sen_inside_ent_list.append([['']])
            new_sen_ent_list1.append(sen_ent_list1[i])
            continue
        for j in range(len(sen_ent_list1[i])):
            ent_start = sen_ent_list1[i][j][0]
            ent_end = sen_ent_list1[i][j][1]
            ent_type = sen_ent_list1[i][j][2]
            ent_content = sentence_unicode[ent_start:ent_end].encode('utf-8')
            entities.append(Entity(ent_content, ent_start, ent_end, ent_type))
            s_e_list.append([sentence, ent_content])
        sen_inside_ent_list.append(s_e_list)
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
            tagger.set(xseq)
            yseq = tagger.viterbi()
            ents_type.append(yseq[0])
        # postProcessing
        if post_processing == '1': # 评价中的start_end_list没有调整
            new_yseq = ['O' for i in range(len(sentence_unicode))]
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
            new_yseq1 = postProcessing.twoProcessings(sentence_unicode, new_yseq, ebao_dic, texttype)
            tag_list1 = []
            tag_list1.append(new_yseq1)
            ents1, s_e_list1 = evaluation.generateEntList(tag_list1)
            new_entities = ents1[0]
        else:
            for k in range(len(ents_type)):
                try:
                    new_entities.append((sen_ent_list1[i][k][0], sen_ent_list1[i][k][1], ents_type[k]))
                except Exception as e:
                    print e
                    print len(sen_ent_list1[i]), len(ents_type)
                    print sentence
                    print feature_c
        new_sen_ent_list1.append(new_entities)

    # 错误分析
    ent_count_result, ent_count_result_o = evaluation.countEntList(new_sen_ent_list1, sen_ent_list2, start_end_list1, start_end_list2, sen_inside_ent_list)
    evaluation.measurePRF(ent_count_result_o)

#汇总各类数据到训练文件
def combineData(trainingdata_previous, cdd4training_s, trainingdata):
    fw = open(trainingdata, 'w')
    f = open(trainingdata_previous, 'r')
    fr = f.read()
    f.close()
    sentences = fr.strip().split('\n\n')
    for sentence in sentences:
        fw.write(sentence + '\n\n')
    for s in cdd4training_s:
        fw.write(s + '\n\n')
    fw.close()
    print 'data combined!'

def tagProcess(taglist, ylength):
    tl = []
    for i in range(ylength):
        tl.append([])
        for j in range(len(taglist[i])):
            tl[i].append(taglist[i][j])
    for i in range(ylength):
        if i == 0 or len(tl[i]) == 1: # bso
            continue
        # i - 1
        if tl[i - 1] == ['O'] or tl[i - 1] == ['S-entity']: # delete I, E
            if 'I-entity' in tl[i]: tl[i].remove('I-entity')
            if 'E-entity' in tl[i]: tl[i].remove('E-entity')
        elif tl[i - 1] == ['B-entity'] or tl[i - 1] == ['I-entity']: # delete B, S, O
            if 'B-entity' in tl[i]: tl[i].remove('B-entity')
            if 'S-entity' in tl[i]: tl[i].remove('S-entity')
            if 'O' in tl[i]: tl[i].remove('O')
        elif tl[i - 1] == ['E-entity']: # delete I, E
            if 'I-entity' in tl[i]: tl[i].remove('I-entity')
            if 'E-entity' in tl[i]: tl[i].remove('E-entity')
        # i + 1
        if i == ylength - 1: # eso
            continue
        if tl[i + 1] == ['O'] or tl[i + 1] == ['S-entity'] or tl[i + 1] == ['B-entity']: # delete B, I
            if 'B-entity' in tl[i]: tl[i].remove('B-entity')
            if 'I-entity' in tl[i]: tl[i].remove('I-entity')
        elif tl[i + 1] == ['I-entity'] or tl[i + 1] == ['E-entity']: # delete E, S, O
            if 'E-entity' in tl[i]: tl[i].remove('E-entity')
            if 'S-entity' in tl[i]: tl[i].remove('S-entity')
            if 'O' in tl[i]: tl[i].remove('O')
        if len(tl[i]) == 0:
            tl[i].append('O')
    return tl

def generateTagSeq(input_sentence, ents):
    sen_length = len(input_sentence)
    tagSeq = ['O' for i in range(sen_length)]
    for i in range(len(ents)):
        ent = ents[i]
        begin = ent.start_pos
        end = ent.end_pos
        if begin == end - 1:
            tagSeq[begin] = 'S-entity'
        else:
            tagSeq[begin] = 'B-entity'
            tagSeq[end -1] = 'E-entity'
            for index in range(begin+1, end-1):
                tagSeq[index] = 'I-entity'
    return tagSeq

def semiSupervisedProcessing(model_previous, fsamples, ie_value, ebao_dic):
    tagger_bp = crfsuite.Tagger()
    tagger_bp.open(model_previous)
    bieso = ['B-entity', 'I-entity', 'E-entity', 'S-entity', 'O']
    cdd4training_semi = []
    cdd4training_semi_number = 0
    for line in fsamples:
        # 用识别的实体过滤样例
        sentence, entities_in_sentence = generateSenEntities(line)
        new_sentence, new_entities = symbolProcess(sentence, entities_in_sentence)
        sentence_unicode = new_sentence.decode('utf-8')
        tag_seq = generateTagSeq(sentence_unicode, new_entities)
        feature_string, tags = generateFeature.boundaryFeatureGeneration(new_sentence, [], ebao_dic, 'demo', '0')
        try:
            instances = feature_string.strip().split('\n')
        except AttributeError as e:
            print 'feature_string:%s.' % line
        xseq = crfsuite.ItemSequence()
        features = []
        for instance in instances:
            fields = instance.split('\t')
            features.append(fields[2:])
            item = crfsuite.Item()
            for field in fields[2:]:
                item.append(crfsuite.Attribute(field))
            xseq.append(item)
        tagger_bp.set(xseq)

        yseq_b = tagger_bp.viterbi()
        length = len(yseq_b)

        yseq = []
        for i in range(length):
            yseq.append(yseq_b[i])
        # 标记优化处理
        sen_ent_list1, start_end_list1 = evaluation.generateEntList([yseq_b])
        sen_ent_list2, start_end_list2 = evaluation.generateEntList([tag_seq])
        tagged_ents_length = len(start_end_list1[0])
        if tagged_ents_length == 0: continue
        
        ents = []
        selected_entity = 0
        ent_index = 0
        for i in range(tagged_ents_length):
            ent_start = start_end_list1[0][i][0]
            if ent_start < ent_index: continue
            flag = 0
            ent_end = start_end_list1[0][i][1]
            ent_content = sentence_unicode[ent_start:ent_end].encode('utf-8')
            ie_entity = 0.0
            for j in range(ent_start, ent_end):
                ie = 0.0 # 信息熵
                for ent_tag in bieso:
                    tag_prob = tagger_bp.marginal(ent_tag, j)
                    ie += tag_prob * math.log(1 / tag_prob, 2)
                ie_entity += ie
            # ie_ave = ie_entity / (ent_end - ent_start)
            # if ebao_dic.has_key(ent_content) and ie_ave > ie_value:
            if ie_entity > ie_value:
                for k in range(len(start_end_list2[0])):
                    start_m = start_end_list2[0][k][0]
                    end_m = start_end_list2[0][k][1]
                    if ent_start >= start_m and ent_end <= end_m:
                        # if end_m - start_m < 3: break
                        ents.append(Entity(sentence_unicode[start_m:end_m].encode('utf-8'), int(start_m), int(end_m), 'entity'))
                        ent_index = end_m
                        flag = 1
                        break
                if flag == 0:
                    continue

                    if not ebao_dic.has_key(ent_content): continue
                    ents.append(Entity(ent_content, int(ent_start), int(ent_end), 'entity'))
                    ent_index = end_m
                selected_entity += 1

        if selected_entity == 0: continue

        char_entity_tag_list = generateFeature.getCharEntityFPTag(sentence_unicode, ents, '1')
        char_entity_tag_list = generateFeature.getCharEntityPartialTag(char_entity_tag_list)

        new_feature_str = ''
        for j in range(length):
            new_feature_str += '%s\t%s\n' % (char_entity_tag_list[j][1][0], '\t'.join(features[j]))

        cdd4training_semi.append(new_feature_str.strip())
        cdd4training_semi_number += 1
    return cdd4training_semi, cdd4training_semi_number

def predictValue(feature_str):
    try:
        instances = feature_str.strip().split('\n')
    except AttributeError as e:
        print 'feature_string:%s.' % feature_str
    xseq = crfsuite.ItemSequence()
    for instance in instances:
        fields = instance.split('\t')
        item = crfsuite.Item()
        for field in fields[2:]: # S3tag\tS1tag\tFeatures
            item.append(crfsuite.Attribute(field))
        xseq.append(item)
    tagger_b.set(xseq)

    yseq_b = tagger_b.viterbi()
    prob_b = tagger_b.probability(yseq_b)
    return prob_b

# 生成部分标注的文件
def generatePartialTagFile(partial_tag_file, ebao_dic, tag_strategy, sen_num):
    f = open(partial_tag_file, 'r')
    lines = f.readlines()
    f.close()

    predict_value_dic = {}
    sentence_num = 0
    for line in lines:
        if sentence_num > sen_num: break
        sentence_num += 1
        line = line.replace('\n', '') # 保留原句中的起始空格
        line = line.replace('\r', '')
        line = line.replace('　', ' ')
        try:
            sentence, entities_in_sentence = generateSenEntities(line)

            # 过滤实体：全英文、长度为1、实体类型不对http:
            if len(entities_in_sentence) == 0: continue

            feature_string, tags_in_sen = generateFeature.boundaryFeatureGeneration(sentence, entities_in_sentence, ebao_dic, 'partial', tag_strategy) # S3tag\tS1tag\tFeatures
            predict_value = predictValue(feature_string)
        except Exception as e:
            print e
            print sentence
            continue
        if feature_string == None:
            print 'None: %s' % line
            continue
        predict_value_dic[sentence + '\t\t\t' + feature_string.strip()] = predict_value

    # sort predict_value_dic
    sorted_predict_value_dic = sorted(predict_value_dic.iteritems(), key=lambda d:d[1], reverse = True)

    sample_feature_list = []
    
    for key_value in sorted_predict_value_dic:
        print str(key_value[1])
        sen_feature = key_value[0].split('\t\t\t')
        print sen_feature[0] + '\n'
        sample_feature_list.append(sen_feature[1])
        
    return sample_feature_list

def symbolProcess(sentence, entities_in_sentence):
    new_sen = tools.uniformSignal(sentence)
    new_ents = []
    # 所有字单独成词
    new_sen_seg = ''
    new_sen_unicode = new_sen.decode('utf-8')
    for i in range(len(new_sen_unicode)):
        if i == 0:
            new_sen_seg += new_sen_unicode[i].encode('utf-8')
        else:
            new_sen_seg += ' ' + new_sen_unicode[i].encode('utf-8')
    
    ent_pos_list, new_sen_reseg = generateFeature.get_entity_pos(entities_in_sentence, sentence, new_sen_seg)

    for i in range(len(entities_in_sentence)):
        try:
            start = ent_pos_list[i][0]
            end = ent_pos_list[i][1] + 1
            t = entities_in_sentence[i].type
            con = new_sen_unicode[start:end].encode('utf-8')
            new_ents.append(Entity(con, start, end, t))
        except Exception as e:
            print e
            print i, len(entities_in_sentence), len(ent_pos_list)
            print sentence
            print new_sen_reseg
            print ent_pos_list
            return new_sen, []
    return new_sen, new_ents

def filterTerm(term):
    tag = 0
    # 长度小于2则过滤
    if len(term.decode('utf-8')) < 2:
        tag = 1
        return tag
    # 匹配汉字，无则过滤
    cw = re.compile(u'[\u4e00-\u9fa5]')
    matches = re.findall(cw, term.decode('utf-8'))
    if len(matches) == 0:
        tag = 1
    return tag

def generateSenEntities(input_line, texttype):
    if '\t' not in input_line.strip():
        return input_line.strip(), []
    sen_ent = input_line.split('\t\t\t')

    sen = sen_ent[0]
    ent_string = sen_ent[1]
        
    ents_in_sen = ent_string.split('\t\t')
    ents = []
    for ent in ents_in_sen:
        ent_content, ent_start, ent_end, ent_type = ent.split('\t')
       
        if ent_content in ['综合治疗', '明显异常', '明显减轻', '异常']: continue
        ents.append(Entity(ent_content, int(ent_start), int(ent_end), ent_type))
    return sen, ents

# 生成完整标注的文件
def generateFullTagFile(full_tag_file, boundary4training, class4training, sen_ent4developing, not4train, ebao_dic, datatype, tag_strategy, texttype):
    f = open(full_tag_file, 'r')
    lines = f.readlines()
    f.close()

    fw_b = open(boundary4training, 'w')
    fw_c = open(class4training, 'w')
    if not4train == '1':
        fw_e = open(sen_ent4developing, 'w')
        sen_tags = []
        sentences = []
    for line in lines:
        line = line.replace('\n', '') # 保留原句中的起始空格
        line = line.replace('\r', '')
        line = line.replace('　', ' ')
        try:
            sentence, entities_in_sentence = generateSenEntities(line, texttype) # 替换全角空格
        except Exception as e:
            print line
            print sentence
            continue

        # 过滤训练数据的ds中整个句子标为一个实体的例子
        if datatype == 'train':
            if len(entities_in_sentence) == 1 and entities_in_sentence[0].content == sentence and entities_in_sentence[0].type == 'specifications':
                continue

        # 增加符号替换及空格处理
        #(该部分操作重复，在分词的时候做了该处理，不过该操作在加字典特征的时候起到了作用)
        new_sentence, new_entities = symbolProcess(sentence, entities_in_sentence)
        if len(new_entities) == 0: continue # 类别评价

        feature_b, tags_in_sen = generateFeature.boundaryFeatureGeneration(new_sentence, new_entities, ebao_dic, 'full', tag_strategy)

        fw_b.write(feature_b)
        feature_c, sen_ent4error = generateFeature.classFeatureGeneration(new_sentence, new_entities, ebao_dic, texttype)
        fw_c.write(feature_c)
        if not4train == '1':
            fw_e.write(sen_ent4error)
            sentences.append(new_sentence.replace('\r', ''))
            sen_tags.append(tags_in_sen)
    fw_b.close()
    fw_c.close()
    if not4train == '1':
        fw_e.close()
        print sen_ent4developing + 'generated!'
    print boundary4training + ' generated!'
    print class4training + ' generated!'
    if not4train == '1':
        return sentences, sen_tags

def generateBoundaryTagFile(source, target_file, ebao_dic, not4train, sen_ent4developing):
    if isinstance(source, list):
        lines = source
    else:
        f = open(source, 'r')
        lines = f.readlines()
        f.close()

    fw_b = open(target_file, 'w')
    if not4train == '1':
        fw_e = open(sen_ent4developing, 'w')
        sen_tags = []
        sentences = []

    for line in lines:
        line = line.replace('\n', '') # 保留原句中的起始空格
        line = line.replace('\r', '')
        line = line.replace('　', ' ')
        try:
            sentence, entities_in_sentence = generateSenEntities(line, '') # 替换全角空格
        except Exception as e:
            print line
            print sentence
            continue

        new_sentence, new_entities = symbolProcess(sentence, entities_in_sentence)
        if len(new_entities) == 0: continue # 类别评价
        feature_b, tags_in_sen = generateFeature.boundaryFeatureGeneration(new_sentence, new_entities, ebao_dic, 'full', '')
        fw_b.write(feature_b)
        if not4train == '1':
            feature_c, sen_ent4error = generateFeature.classFeatureGeneration(new_sentence, new_entities, ebao_dic, '')
            fw_e.write(sen_ent4error)
            sentences.append(new_sentence.replace('\r', ''))
            sen_tags.append(tags_in_sen)
    fw_b.close()
    print target_file + ' generated!'
    if not4train == '1':
        fw_e.close()
        print sen_ent4developing + 'generated!'
        return sentences, sen_tags
