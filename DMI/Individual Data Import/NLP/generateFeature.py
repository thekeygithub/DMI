#!/usr/bin/env python
# coding: utf-8

import tools
import postProcessing
import re

class Entity(object):
    def __init__(self, con, start, end, t):
        self.content = con
        self.start_pos = start
        self.end_pos = end
        self.type = t

# ss and ct
typelist = [
    'disease',
    'symptom',
    'diagnosis_treatment',
    'diagnosis_name',
    'treatment',
    'other_diagnosis',
    'instrument',
    # 'medicine_all',
    'medicine',
    'medicine_cn',
    'medicine_pn',
    'medicine_mn',
    'dosage_form',
    'specifications',
    'packing_spe',
    'packing_material',
    'enterprise',
    'department',
    'address',
    'healthy_food',
    'social_insurance',
]

def isSpecial(character):
    punc = '[!"#$%&\'()*+,-./:;<=>?@\\^_`{}~]|！￥……（）—；‘’、【】《》？“：，”★≥'
    if character in punc:
        return 's'
    if character.isdigit():
        return 'd'
    if character.isalpha():
        return 'c'
    return 'o'

def getCharEntityPartialTag(char_fulltag_list):
    punc = '()*+-./%*（）—+'
    punc_o = u'[!"#$%&\':;<=>?@\\^_`{|}~]！#￥%……&|；‘’【】《》？“：”★≥、' # ,，
    length = len(char_fulltag_list)
    # 整个句子实体标记重新赋值
    # S只有在包装规格和包材出现，故去除该标记
    # ct里可能出现S，比如T,R等检查，故需要加S
    for i in range(length):
        if i == 0: # 句首 bso, bso, bso
            if char_fulltag_list[i][1] == ['O']: # bso, bso, bso
                char_fulltag_list[i][1].append('S-')
                if length == 1: # o, o, o
                    continue
                else:
                    if char_fulltag_list[i+1][1] == ['O']: # 后一个是实体，so, bso, bso
                        char_fulltag_list[i][1].append('B-')
                    continue
        if char_fulltag_list[i][1] == ['O']: # bieso, bieso, bieso
            char_fulltag_list[i][1].append('S-')
            if len(char_fulltag_list[i-1][1]) == 1: # 前一个是实体，bso, bieso, bieso
                if i + 1 < length: # 非句尾，bso, bieso, bieso
                    if char_fulltag_list[i+1][1] == ['O']: # 后一个是实体，so, bieso, bieso
                        char_fulltag_list[i][1].append('B-')
                        continue
            else: # bieso, bieso, bieso
                if i + 1 < length: # 非句尾，bieso, bieso, bieso
                    if char_fulltag_list[i+1][1] != ['O']: # 后一个是实体，eso, bieso, bieso
                        char_fulltag_list[i][1].append('E-')
                        continue
                    else: # bieso, bieso, bieso
                        char_fulltag_list[i][1].append('B-')
                        char_fulltag_list[i][1].append('I-')
                        char_fulltag_list[i][1].append('E-')
                        continue
                else: # i为句尾，eso, eso, eso
                    char_fulltag_list[i][1].append('E-')
                    continue
    # 标点标记优化
    for i in range(length):
        if i == 0:
            if char_fulltag_list[i][0] in punc_o:
                char_fulltag_list[i][1] = ['O']
            else:
                if length == 1:
                    continue
                else:
                    if char_fulltag_list[i+1][0] in punc_o:
                        if 'B-' in char_fulltag_list[i][1]: char_fulltag_list[i][1].remove('B-')
                        continue
        else:
            if char_fulltag_list[i][0] in punc_o:
                char_fulltag_list[i][1] = ['O']
            else:
                if char_fulltag_list[i-1][0] in punc_o:
                    if 'I-' in char_fulltag_list[i][1]: char_fulltag_list[i][1].remove('I-')
                    if 'E-' in char_fulltag_list[i][1]: char_fulltag_list[i][1].remove('E-')
                if i + 1 < length:
                    if char_fulltag_list[i+1][0] in punc_o:
                        if 'I-' in char_fulltag_list[i][1]: char_fulltag_list[i][1].remove('I-')
                        if 'B-' in char_fulltag_list[i][1]: char_fulltag_list[i][1].remove('B-')

    # 标记加上类型
    char_partialtag_list = []
    for i in range(length):
        tags = char_fulltag_list[i][1]
        if len(tags[0]) > 2: # 匹配到的位置是以'B-entity'或者'I-entity'开始的，未匹配的以'O'开始
            tag_string = ''
            for j in range(len(tags)):
                tag_string += tags[j] + '|'
            char_partialtag_list.append([char_fulltag_list[i][0], [tag_string[:-1]]])
        else:
            tag_string = ''
            for j in range(len(tags)):
                if char_fulltag_list[i][1][j] == 'O':
                    continue
                else:
                    tag_string += char_fulltag_list[i][1][j] + 'entity|'

            tag_string += 'O'
            char_partialtag_list.append([char_fulltag_list[i][0], [tag_string]])
    return char_partialtag_list

def string2Entity(ents):
    new_ents = []
    for ent in ents:
        con, start, end, t = ent.split('\t')
        new_ents.append(Entity(con, int(start), int(end), t))
    return new_ents

def getCharEntityFPTag(sen, entities, tag_strategy):
    character_ent_tag = [[sen[i] , ['O']] for i in range(len(sen))]
    if len(entities) == 0:
        return character_ent_tag
    for i in range(len(entities)):
        entity = entities[i]
        begin = entity.start_pos
        end = entity.end_pos
        ent_type = entity.type
        if ent_type in ['medicine_cn', 'medicine_pn', 'medicine_mn', 'medicine_ot', 'medicare']:
            ent_type = 'medicine'

        # # without_type
        enttype = 'entity'

        if tag_strategy == '0':
            if ent_type == 'medicine' and end - begin >= 3:
                tag_strategy = '1'
            else:
                tag_strategy = '3'

        if tag_strategy == '1':
            character_ent_tag[begin][1] = ['B-' + enttype]
            character_ent_tag[end - 1][1] = ['E-' + enttype]
        elif tag_strategy == '2':
            character_ent_tag[begin][1] = ['B-' + enttype, 'I-' + enttype]
            character_ent_tag[end - 1][1] = ['I-' + enttype, 'E-' + enttype]
        else:
            character_ent_tag[begin][1] = ['B-' + enttype, 'I-' + enttype, 'E-' + enttype]
            character_ent_tag[end - 1][1] = ['B-' + enttype, 'I-' + enttype, 'E-' + enttype]
        for index in range(begin+1, end - 1):
            if tag_strategy == '3':
                character_ent_tag[index][1] = ['B-' + enttype, 'I-' + enttype, 'E-' + enttype]
            else:
                character_ent_tag[index][1] = ['I-' + enttype]
    new_ent_tag_list = []
    for i in range(len(sen)):
        if sen[i].strip() != '':
            new_ent_tag_list.append(character_ent_tag[i])
    return new_ent_tag_list

def getCharEntityFullTag(sen, entities, ebao_dic):
    character_ent_tag = [[sen[i] , ['O']] for i in range(len(sen))]
    if len(entities) == 0:
        return character_ent_tag
    for i in range(len(entities)):
        entity = entities[i]
        con = entity.content
        begin = entity.start_pos
        end = entity.end_pos
        enttype = entity.type

        if begin == end - 1:
            character_ent_tag[begin][1] = ['S-' + enttype]
        else:
            character_ent_tag[begin][1] = ['B-' + enttype]
            character_ent_tag[end - 1][1] = ['E-' + enttype]
            for index in range(begin+1, end - 1):
                character_ent_tag[index][1] = ['I-' + enttype]
    new_ent_tag_list = []
    for i in range(len(sen)):
        if sen[i].strip() != '':
            new_ent_tag_list.append(character_ent_tag[i])
    return new_ent_tag_list

# 字的分词标记, 词性标记
def getCharSegPosTag(words, postags):
    character_seg_tag = []
    character_pos_tag = []
    for i in range(len(words)):
        word = words[i].decode('utf-8')
        if len(word) == 1:
            character_seg_tag.append((word.encode('utf8'), 'S'))
            character_pos_tag.append((word.encode('utf8'), 'S-' + postags[i]))
        else:
            lenofword = len(word)
            character_seg_tag.append((word[0].encode('utf8'), 'B'))
            character_pos_tag.append((word[0].encode('utf8'), 'B-' + postags[i]))
            if lenofword > 2:
                for j in range(1, lenofword-1):
                    character_seg_tag.append((word[j].encode('utf8'), 'I'))
                    character_pos_tag.append((word[j].encode('utf8'), 'I-' + postags[i]))
            character_seg_tag.append((word[-1].encode('utf8'), 'E'))
            character_pos_tag.append((word[-1].encode('utf8'), 'E-' + postags[i]))
    return character_seg_tag, character_pos_tag

# 实体位置对齐
def get_entity_pos(entities, sentence, sentence_seg):
    #print sentence
    #print sentence_seg
    # for ent in entities:
    #     print ent.content, ent.start_pos, ent.end_pos
    i, j, word_id = 0, 0, 0
    pos_list = []

    sentence = sentence.decode('utf-8')
    sentence_seg = sentence_seg.decode('utf-8')
    new_sentence_seg = ''
    for ent in entities:
        con, start, end = ent.content, ent.start_pos, ent.end_pos
        while j < len(sentence_seg) and i < len(sentence):
            #print 'i=', i, sentence[i], 'j=', j, sentence_seg[j], 'len=', len(sentence), 'len_seg=', len(sentence_seg)
            if sentence[i] in ' \t' and i != start:
                i += 1
                continue
            if sentence_seg[j] == ' ':
                new_sentence_seg += sentence_seg[j]
                word_id += 1
                j += 1
                continue
            if i == start:
                # 实体开始切分
                if j > 0 and sentence_seg[j - 1] != ' ' and new_sentence_seg[-1] != ' ':
                    new_sentence_seg += ' '
                    word_id += 1
                pos = [word_id, 0]
                while i < end:
                    # try:
                    if sentence[i] == ' ':
                        i += 1
                        continue
                    if sentence_seg[j] == ' ':
                        word_id += 1
                        i -= 1
                    new_sentence_seg += sentence_seg[j]
                    i += 1
                    j += 1
                pos[1] = word_id
                pos_list.append(pos)
                if j < len(sentence_seg) and sentence_seg[j] != ' ':
                    new_sentence_seg += ' '
                    word_id += 1
                break
            new_sentence_seg += sentence_seg[j]
            j += 1
            i += 1
    while j < len(sentence_seg):
        new_sentence_seg += sentence_seg[j]
        j += 1
    #print pos_list
    return pos_list, new_sentence_seg.encode('utf8') 

# 特征生成
def boundaryFeatureGeneration(sentence, entities_in_sen, ebao_dic, label_type, tag_strategy):
    sentence_seg = tools.segment(sentence) # 输入的句子无空格

    if len(entities_in_sen) > 0: # 考虑到标注数据的训练文件中可能出现的没有实体的句子
        entity_position_list, sentence_reseg = get_entity_pos(entities_in_sen, sentence, sentence_seg)
    else:
        sentence_reseg = sentence_seg

    # 词列表， 词性列表
    word_list, postag_list = tools.pos(sentence_reseg)
    # 字对应的分词、词性标记
    char_seg_tag_list, char_pos_tag_list = getCharSegPosTag(word_list, postag_list) #无空格

    # 字对应的实体标记, 字对应的字典匹配边界标记
    if label_type == 'demo':
        sen_no_use, ents_matched = tools.matchEntityCombine(sentence, ebao_dic)
        ents_matched = string2Entity(ents_matched)
        dic_match_tag_list = getCharEntityFullTag(sentence.decode('utf-8'), ents_matched, ebao_dic) # 有类型
        char_entity_tag_list = [[sentence.decode('utf-8')[i] , ['O']] for i in range(len(sentence.decode('utf-8')))]
    else:
        if label_type == 'full':
            char_entity_tag_list = getCharEntityFullTag(sentence.decode('utf-8'), entities_in_sen, ebao_dic)
            sen_no_use, ents_matched = tools.matchEntityCombine(sentence, ebao_dic)
            ents_matched = string2Entity(ents_matched)
            dic_match_tag_list = getCharEntityFullTag(sentence.decode('utf-8'), ents_matched, ebao_dic) # 有类型
        if label_type == 'partial':
            char_entity_tag_list = getCharEntityFPTag(sentence.decode('utf-8'), entities_in_sen, tag_strategy)
            dic_match_tag_list = getCharEntityFPTag(sentence.decode('utf-8'), entities_in_sen, '1') # 无类型
            char_entity_tag_list = getCharEntityPartialTag(char_entity_tag_list, tag_strategy)
            
    # 生成 字-分词标记-词性标记 的list
    char_seg_pos_list = []
    for i in range(len(char_seg_tag_list)):
        char_seg_pos_list.append((char_seg_tag_list[i][0], char_seg_tag_list[i][1], char_pos_tag_list[i][1]))
    bos = '__BOS__'
    eos = '__EOS__'
    new_csp_list = [(bos, bos, bos), (bos, bos, bos)] + char_seg_pos_list + [(eos, eos, eos), (eos, eos, eos)]
    length = len(new_csp_list)
    features = ''
    tags_in_sentence = []
    for i in range(2, length - 2):
        feature_vec = []
        
        # 字特征
        feature_vec += [new_csp_list[i-2][0], new_csp_list[i-1][0], new_csp_list[i][0], new_csp_list[i+1][0], new_csp_list[i+2][0], new_csp_list[i-2][0] + '/' + new_csp_list[i-1][0], new_csp_list[i-1][0] + '/' + new_csp_list[i][0], new_csp_list[i][0] + '/' + new_csp_list[i+1][0], new_csp_list[i+1][0] + '/' + new_csp_list[i+2][0]]
        # 分词标记特征
        feature_vec += [new_csp_list[i-2][1], new_csp_list[i-1][1], new_csp_list[i][1], new_csp_list[i+1][1], new_csp_list[i+2][1], new_csp_list[i-2][1] + '/' + new_csp_list[i-1][1], new_csp_list[i-1][1] + '/' + new_csp_list[i][1], new_csp_list[i][1] + '/' + new_csp_list[i+1][1], new_csp_list[i+1][1] + '/' + new_csp_list[i+2][1]]
        # 词性标记特征
        feature_vec += [new_csp_list[i-2][2], new_csp_list[i-1][2], new_csp_list[i][2], new_csp_list[i+1][2], new_csp_list[i+2][2], new_csp_list[i-2][2] + '/' + new_csp_list[i-1][2], new_csp_list[i-1][2] + '/' + new_csp_list[i][2], new_csp_list[i][2] + '/' + new_csp_list[i+1][2], new_csp_list[i+1][2] + '/' + new_csp_list[i+2][2]]
        # 字符类型特征
        feature_vec += [isSpecial(new_csp_list[i-1][0]), isSpecial(new_csp_list[i][0]), isSpecial(new_csp_list[i+1][0]), isSpecial(new_csp_list[i-1][0]) + '/' + isSpecial(new_csp_list[i][0]), isSpecial(new_csp_list[i][0]) + '/' + isSpecial(new_csp_list[i+1][0])]
        
        # 字典特征
        dic_boundary_tag = dic_match_tag_list[i-2][1][0]
        dic_b_tag = dic_boundary_tag[:1]
        feature_vec += [dic_b_tag]

        # 实体标记
        try:
            entity_tag = char_entity_tag_list[i-2][1][0]
            if '|' in entity_tag and len(re.findall('-', entity_tag)) > 1: # for partial tags
            # if '|' in entity_tag:
                features += entity_tag
            else:
                if '-' in entity_tag: # for training data that contains entity types
                    parts = entity_tag.split('-')
                    if '|' in parts[1]:
                        entity_tag = parts[0] + '-entity' # 由于测试集中不带部分标记，这里不做具体处理
                    ent_tag = parts[0] + '-entity'
                    features += ent_tag
                else: # O
                    features += entity_tag
        except IndexError as e: 
            print sentence
            print i
            print new_csp_list
            print char_entity_tag_list
            return None

        # for strategy 4, demo
        if label_type == 'demo':
            if '-' in dic_boundary_tag:
                features += '\t' + dic_boundary_tag[0] + '-entity'
            else:
                features += '\t' + dic_boundary_tag

        for j in range(len(feature_vec)):
            features += '\tf' + str(j) + '=' + str(feature_vec[j])
        features += '\n'
        tags_in_sentence.append(entity_tag)
    features += '\n'
    return features, tags_in_sentence

def classFeatureGeneration(sentence, entities_in_sen, ebao_dic, texttype):
    bos = '__BOS__'
    eos = '__EOS__'
    length = len(entities_in_sen)
    sentence_unicode = sentence.decode('utf8')
    features_in_sen = ''
    sen_ent = ''
    for i in range(length):
        features = ''
        feature_vec = []
        # 实体
        entity_type = entities_in_sen[i].type
        entity_start = entities_in_sen[i].start_pos
        entity_end = entities_in_sen[i].end_pos
        entity_content = sentence_unicode[entity_start:entity_end].encode('utf8')

        # 长度
        entity_length = len(entity_content.decode('utf8'))
        # 尾字特征
        ent_type = postProcessing.changeEntType(entity_content, 'default', ebao_dic, texttype)
        if ent_type in ['medicine_cn', 'medicine_mn', 'medicine_pn', 'medicine_ot', 'medicare', 'medicine']:
            ent_type = 'medicine_all'
        
        # 考虑增加字典匹配类型特征，扩充针对疾病和症状
        if ebao_dic.has_key(entity_content):
            ent_type_dic = ebao_dic[entity_content]
        else:
            ent_type_dic = 'default'

        # 上下文
        if entity_start > 0:
            context_p1 = sentence_unicode[entity_start - 1].encode('utf8')
        else:
            context_p1 = bos
        if entity_start > 1:
            context_p2 = sentence_unicode[entity_start - 2].encode('utf8')
        else:
            context_p2 = bos
        if entity_end < len(sentence_unicode):
            context_n1 = sentence_unicode[entity_end].encode('utf8')
        else:
            context_n1 = eos
        if entity_end < len(sentence_unicode) - 1:
            context_n2 = sentence_unicode[entity_end + 1].encode('utf8')
        else:
            context_n2 = eos
        # 实体特征
        feature_vec += [entity_content, entity_length]
        # 实体内部字特征, 前2, 前1, 后1, 后2
        entity_content_unicode = entity_content.decode('utf8')
        feature_vec += [entity_content_unicode[:2].encode('utf8'), entity_content_unicode[:1].encode('utf8'), entity_content_unicode[-1:].encode('utf8'), entity_content_unicode[-2:].encode('utf8')]
        # 尾字特征, 实体类型
        feature_vec += [ent_type]
        # 字典中对应的实体类型
        feature_vec += [ent_type_dic]
        # 上下文特征, -2, -1, 1, 2
        feature_vec += [context_p2, context_p1, context_n1, context_n2]

        features += entity_type
        for j in range(len(feature_vec)):
            features += '\tf' + str(j) + '=' + str(feature_vec[j])
        features += '\n'
        features_in_sen += features + '\n'
        sen_ent += sentence + '\t' + entity_content + '\t' + entity_type + '\n'
    if sen_ent == '':
        sen_ent = '\n'
    sen_ent += '\n'
    return features_in_sen, sen_ent


