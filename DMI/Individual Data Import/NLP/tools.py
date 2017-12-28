#!/usr/bin/env python
# coding: utf-8
import os
import sys
import re
import CRFPP
import math
import postProcessing

rootpath = sys.path[0]
temppath = os.path.join(rootpath, 'temp')
seg_model = os.path.join(rootpath, './models/segmenter.model')
pos_model = os.path.join(rootpath, './models/postagger.model')
tagger_seg = CRFPP.Tagger('-m ' + seg_model + ' -v 3 -n2')
tagger_pos = CRFPP.Tagger('-m ' + pos_model + ' -v 3 -n2')

def countCharProb(dic_input):
    char_dic = {}
    count_char = 0
    for key in dic_input.keys():
        key_unicode = key.decode('utf8')
        for i in range(len(key_unicode)):
            if char_dic.has_key(key_unicode[i]):
                char_dic[key_unicode[i]] += 1
            else:
                char_dic[key_unicode[i]] = 1
            count_char += 1
    # sort
    char_dic_sorted = sorted(char_dic.iteritems(), key=lambda d:d[1], reverse = True)
    char_prob = {}
    char_prob[char_dic_sorted[0][0].encode('utf8')] = 1.0
    first_prob = - (float(math.log(char_dic_sorted[0][1], 2)) - (float(math.log(count_char, 2))))
    for i in range(1, len(char_dic_sorted)):
        char_prob[char_dic_sorted[i][0].encode('utf8')] = - (float(math.log(char_dic_sorted[i][1],2)) - (float(math.log(count_char, 2)))) / first_prob
    return char_prob

# 载入词典
def loadDic(dicfile):
    dic = {}
    dic_file = open(dicfile)
    dic_data = dic_file.readlines()
    for line in dic_data:
        new_line = line.strip().split('\t')
        if len(new_line) != 2:
            print line
            continue
        word = new_line[0].replace(' ', '')
        if word not in dic:
            term_type = new_line[1]
            dic[word] = term_type
    dic_file.close()
    return dic

# 载入列表
def loadList(listfile):
    dic = []
    dicfile = open(listfile)
    dic_data = dicfile.readlines()
    dicfile.close()
    for line in dic_data:
        new_line = line.strip()
        if line not in dic:
            dic.append(new_line)
    return dic

# 分句
def sentence_split(inputsentence): # 输入utf8,输出utf8
    try:
        text = inputsentence.decode('utf-8')
    except UnicodeDecodeError as e: 
        print inputsentence
        return None
    lines = []
    sentence = ''
    for i, a in enumerate(text):
        sentence += a
        if a == u'.':
            if i > 0 and i < len(text) - 1:
                if not (text[i - 1].isdigit()):
                    lines.append(sentence.encode('utf8'))
                    sentence = ''
            else:
                lines.append(sentence.encode('utf8'))
        elif a in u';；。\n':
            if a == u'\n':
                sentence = sentence[:-1]
                if sentence == u'': continue # 两个分隔符挨着出现的处理
            lines.append(sentence.encode('utf8'))
            sentence = ''
        else: # 句子末尾没有分隔符
            if i == len(text) - 1:
                lines.append(sentence.encode('utf8'))
    return lines

def changeEntListType(ent_list):
    new_ent_list = []
    for ent_string in ent_list:
        parts = ent_string.split('\t')
        new_ent_list.append((parts[0], int(parts[1]), int(parts[2]), parts[3]))
    return new_ent_list

def matchEntityCombine(sentence, dic): # 结合逆向最大、最小匹配
    sen, entities1 = matchEntity(sentence, dic)
    sen, entities2 = matchEntityMin(sentence, dic)
    entity_list1 = changeEntListType(entities1)
    entity_list2 = changeEntListType(entities2)
    length1 = len(entity_list1)
    length2 = len(entity_list2)

    i = 0 # 数组是逆向的，句中最后的实体在最前面
    j = 0
    k = 0 # end1和end2对齐的entity_list2的index
    flag = 0
    new_entity_list = []

    while 1:
        if i == length1: break
        start1 = entity_list1[i][1]
        end1 = entity_list1[i][2]
        if flag == 1:
            if end1 > start2:
                end1 = start2
            flag = 0
            j += 1
            if start1 >= start2: # 1在2中，虽然几率很小
                new_entity_list.append('%s\t%d\t%d\t%s' % entity_list1[i])
                i += 1
                continue
        if j == length2: break
        while 1:
            if j == length2: break
            start2 = entity_list2[j][1]
            end2 = entity_list2[j][2]
            if end1 == end2:
                k = j
                if start2 > start1: # 1内
                    j += 1
                    continue
                else: # 1左边界
                    j += 1
                    break
            else:
                if start2 > start1: # 1内
                    j += 1
                    continue
                elif start2 == start1: # 1左边界
                    j += 1
                    break
                else:
                    if end2 <= start1: # 1外
                        break
                    else: # 2与1交错，需切分
                        flag = 1
                        break
        if flag == 0: # 用entity_list1的结果
            new_entity_list.append('%s\t%d\t%d\t%s' % entity_list1[i])
            i += 1
            continue
        else: # 切分entity_list1的实体
            for l in range(k, j + 1):
                new_entity_list.append('%s\t%d\t%d\t%s' % entity_list2[l])
            i += 1
            continue
    if i == length1:
        if flag == 1:
            j += 1
        for l in range(j, length2):
            new_entity_list.append('%s\t%d\t%d\t%s' % entity_list2[l])
    if j == length2:
        for l in range(i, length1):
            new_entity_list.append('%s\t%d\t%d\t%s' % entity_list1[l])
    return sen, new_entity_list

def matchEntityMin(sentence, dic): # 逆向最小匹配
    sen = uniformSignal(sentence).decode('utf-8')
    entities = []
    begin = 0
    end = len(sen)
    while end > 0:
        begin = end - 1
        while begin >= 0:
            word = sen[begin:end].encode('utf-8')
            #print word
            if word in dic:
                entities.append('%s\t%d\t%d\t%s' % (word, begin, end, dic[word]))
                end = begin
                begin = end - 1
                break
            else:
                begin -= 1
            # print begin, end
        if end == 1: break
        if begin == -1 and end != 1:
            end = end - 1
    return sen.encode('utf-8'), entities

def matchEntity(sentence, dic): # 逆向最大匹配
    sen = uniformSignal(sentence).decode('utf-8')
    entities = []
    begin = 0
    end = len(sen)
    while end > 0:
        begin = 0
        while end > begin:
            word = sen[begin:end].encode('utf-8')
            #print word
            if word in dic:
                entities.append('%s\t%d\t%d\t%s' % (word, begin, end, dic[word]))
                end = begin
                begin = 0
                break
            else:
                begin += 1
        if begin != 0 and end == begin:
            end = end - 1
    return sen.encode('utf-8'), entities

def uniformSignal(inputstring):
    inputstring = inputstring.strip()
    inputstring = inputstring.replace('(','（')
    inputstring = inputstring.replace(')','）')
    inputstring = inputstring.replace(',','，')
    inputstring = inputstring.replace(';','；')
    inputstring = inputstring.replace('\t',' ')
    inputstring = inputstring.replace('　',' ')
    inputstring = inputstring.replace('',' ')
    inputstring = inputstring.replace(' ','')
    inputstring = inputstring.replace('\r','')
    return inputstring

# 分词
#unicode编码下
def getCharType(char):
    zhPattern = re.compile(u'[\u4e00-\u9fa5]+')
    if (char in u'[!"#$%&\'()*+,-./:;<=>?@\^_`{|}~]！#￥%……&*（）—+|；‘’、【】《》？“：，”'):
        return 'sign'
    elif char.isalpha():
        return 'alpha'
    elif char.isdigit():
        return 'digit'
    else:
        if zhPattern.search(char):
            return 'Chinese'
        else:
            return 'others'

def segDifferentType(s):
    if len(s) < 2:
        return s
    new_s = ''
    ctypes = []
    s = s.decode('utf-8')
    for pos, char in enumerate(s):
        ctype = getCharType(char)
        ctypes.append(ctype)
    for i in range(len(s)):
        if i == len(s) - 1 or ctypes[i] == ctypes[i+1]:
            new_s += s[i]
        else:
            new_s += s[i] + ' '
    return new_s.encode('utf8')

def combineLettersAndNum(s):
    s = s.decode('utf-8')
    numnum =  re.compile('(\d)\s+(\d)')
    letterletter = re.compile('([a-zA-Z])\s+([a-zA-Z])')
    num = re.search(numnum, s)
    while num != None:
        b, e = num.span()
        #print b, e
        new_s = ''
        if b > 0:
            new_s += s[:b]
        new_s += s[b] 
        new_s += s[e-1]
        if e < len(s):
            new_s += s[e:]
        s = new_s
        num = re.search(numnum, s)

    letter = re.search(letterletter, s)
    
    while letter != None: 
        b, e = letter.span()
        new_s = ''
        if b > 0:
            new_s += s[:b]
        new_s += s[b]
        new_s += s[e-1]
        if e < len(s):
            new_s += s[e:]
        s = new_s
        letter = re.search(letterletter, s)
    return s

def segment(s): # 输入utf8, 输出utf8
    tagger_seg.clear()

    inLine = s.replace(",","，") # 替换中英标点
    inLine = inLine.replace("(","（")
    inLine = inLine.replace(")","）")
    inLine = inLine.replace(";","；")
    inLine = inLine.replace(":","：")
    inLine = inLine.replace('#','$')
    inLine = inLine.replace('×','*')
    inLine = inLine.replace('μ','u')
    inLine = inLine.replace('β','B')
    inLine = inLine.replace('γ','r')
    inLine = inLine.replace('°','度')
    parts = inLine.strip().split(' ')
    for x in parts:
        x = x.decode('utf8')
        for y in x:
            tagger_seg.add(y.encode('utf-8'))
    tagger_seg.parse()
    size = tagger_seg.size()
    tokens = []
    token = ''
    for i in range(0, size):
        if tagger_seg.y2(i) == 'S':
            tokens.append(tagger_seg.x(i, 0))
            token = ''
            continue
        if tagger_seg.y2(i) == 'B' or tagger_seg.y2(i) == 'M':
            token += tagger_seg.x(i, 0)
            continue
        if tagger_seg.y2(i) == 'E':
            token += tagger_seg.x(i, 0)
            tokens.append(token)
            token = ''
            continue
    s_seg = ''
    for j in range(len(tokens)):
        s_seg += segDifferentType(tokens[j]) + ' '
    s_seg = re.subn(r'\s+',' ',s_seg)[0]
    s_seg = combineLettersAndNum(s_seg)
    return s_seg.encode('utf8')

# 字典切词
def get_position(index,l):#获得位置index在句子的第几个词
    count=-1
    for i in range(len(l)):
        if count<index:
            temp = l[i].replace(' ','')
            temp = temp.replace('　'.decode('utf-8'),'')
            count=count+len(temp)
        else:
            return i-1
    return len(l)-1

def ent_in_dic(sen,l, dic):#查找句子中的实体及其起始位置
    sen=sen.replace(' ','')
    sen=sen.replace('　'.decode('utf-8'),'')
    result=[]
    position=[]
    d_position=[]
    for key,value in dic.items():
        if key not in sen:
            continue
        index=0
        start_p=0
        while index!=-1:
            ss=sen.find(key,start_p)
            ee=sen.find(key,start_p)+len(key)-1
            start=get_position(sen.find(key,start_p),l)
            end=get_position(sen.find(key,start_p)+len(key)-1,l)
            start_p=sen.find(key,start_p)+len(key)
            index=sen.find(key,start_p)

            if len(position)==0:
                position.append((start,end))
                d_position.append((ss,ee))
            else:
                flag = True
                for i in range(len(position)):
                    if key in sen[d_position[i][0]:d_position[i][1]+1]:
                        flag = False
                        break
                    if sen[d_position[i][0]:d_position[i][1]+1] in key:
                        position.append((start,end))
                        d_position.append((ss,ee))
                        del position[i]
                        del d_position[i]
                        flag = False
                        break
                    if (d_position[i][0]<=ee and ee<d_position[i][1]) or (d_position[i][0]<ss and ss<=d_position[i][1]):
                        if d_position[i][0]<=ee and ee<d_position[i][1]:
                            pass;
                        else:
                            position.append((start,end))
                            d_position.append((ss,ee)) 
                            del position[i]
                            del d_position[i]
                        flag = False
                        break
                if flag:
                    position.append((start,end))
                    d_position.append((ss,ee))
    temp=[]
    for item in position:
        str=''
        for i in range(item[0],item[1]+1):
            str+=l[i]
        temp.append(str)
        temp.append(dic[str])
        temp.append(item[0])
        temp.append(item[1])

        result.append(temp)
        temp=[]


    return result

def devide_ent(sen,ent,l):#将句子用字典再切分
    ent_count=sen.count(ent)
    start=sen.find(ent)
    end=start+len(ent)-1
    str_count=0
    while start!=-1 and str_count!=ent_count:
        s=get_position(start,l)
        e=get_position(end,l)
        if l[s].find(ent[0])!=0 :
            l[s]=l[s].replace(ent[0],' '+ent[0])
        if l[e].find(ent[-1])!=len(l[e])-1:
            l[e]=l[e].replace(ent[-1],ent[-1]+' ')
        str_count=str_count+1
        start=sen.find(ent,end+1)
        end=start+len(ent)-1
    newl = ' '.join(l)
    l = newl.split(' ')
    return l

# 字典匹配
'''
句子:  患者 有 吞咽 困难 、 呕吐 症状 。
wordid: 0   1   2    3   4    5   6   7
假如“症状”字典中有“吞咽困难”、“呕吐”，则entity_in_dic = [(吞咽困难, symptom, 2, 3), (呕吐, symptom, 5, 5)]
'''
def seg_with_dic(sen_seg, dic):
    line_list=re.split(r'\s+', sen_seg.decode('utf-8').strip())#以空格分

    sentence=sen_seg.decode('utf-8').replace(' ','')
    sentence=sentence.replace('　'.decode('utf-8'),'')
    for key,value in dic.items():
        if key not in sentence:
            continue
        line_list=devide_ent(sentence,key,line_list)
    newline=" ".join(line_list)
    newlist=re.split(r'\s+', newline)
    vector=ent_in_dic(newline,newlist,dic)
    sen_dic_seg = ''
    for token in newlist:
        sen_dic_seg += token.replace(' ','') + ' '
    return sen_dic_seg,vector

# 词性
def pos(seg):
    tagger_pos.clear()
    try:
        cblocks = re.split(r'\s+', seg.strip())
        pre = ''
        suf = ''
        for i in range(0,len(cblocks)) :
            pn = len(cblocks[i].decode('utf8'))
            pre = cblocks[i].decode('utf8')[0].encode('utf8')
            suf = cblocks[i].decode('utf8')[-1].encode('utf8')
            tagger_pos.add(cblocks[i] + '\t' + pre + '\t' + suf + '\t' + str(pn))
        tagger_pos.parse()
        size = tagger_pos.size()
        s_token = []
        s_pos = []
        for i in range(size):
            s_token.append(tagger_pos.x(i, 0))
            s_pos.append(tagger_pos.y2(i))
    except Exception as e:
        print e
        print seg
        print cblocks
        print i
    return s_token, s_pos

#1106 添加未登陆词
def addNewTerms(term_list, dic, newtermfile, cn_en_dic):
    newwordsdic = {}
    with open(newtermfile, 'r') as fr:
        lines = fr.readlines()
        for line in lines:
            parts = line.split('\t')
            newwordsdic[parts[0]] = parts[1]

    with open(newtermfile, 'a') as fw:
        for term_type in term_list.split('<br/>')[:-1]:
            parts = term_type.split('【')
            # 识别出的新实体名字中包含【，导致parts中包含多个成员
            if len(parts) > 2:
                continue
            term = parts[0]
            if dic.has_key(term) or newwordsdic.has_key(term):
                continue
            fw.write(term + '\t' + cn_en_dic[parts[1][:-3]] + '\n')
            newwordsdic[term] = cn_en_dic[parts[1][:-3]]

#1107 根据tagseq生成实体list
def genEntlistByTags(tagseq):
    i = 0
    entlist = []
    while i < len(tagseq):
        if tagseq[i] == 'O':
            i += 1
            continue
        elif tagseq[i].startswith('B-') or tagseq[i].startswith('I-'):
            start = i
            enttype = tagseq[i][2:]
            i += 1
            while tagseq[i] == 'I-' + enttype:
                i += 1
            if tagseq[i] == 'E-' + enttype:
                i += 1
            entlist.append((start, i, enttype))
            continue
        elif tagseq[i].startswith('S-') or tagseq[i].startswith('E-'):
            entlist.append((i, i+1, tagseq[i][2:]))
            i += 1
            continue
    return entlist

