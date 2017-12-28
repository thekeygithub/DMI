# encoding:utf-8

import os
import sys
import xlrd
import re
import tools

reload(sys)
sys.setdefaultencoding('utf8')

data_types = [
    '非结构化',
    '医疗半结构化数据',
    '医疗非结构化数据',
    '一般知识',
    '电子病历',
    '结算单（结构化数据）',
    '',
]

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
    u'药品通用名' : 'medicine_cn',
    u'药品产品名' : 'medicine_pn',
    u'药品商品名' : 'medicine_mn',
    u'医疗器械' : 'instrument',
    u'科室' : 'department',
    u'地址' : 'address'
}

def combinedata(data_folder, input_list, outputfile):
    f = open(os.path.join(data_folder, outputfile), 'w')
    for filename in input_list:
        input_file = os.path.join(data_folder, filename)
        fr = open(input_file, 'r')
        lines = fr.read()
        fr.close()
        f.write(lines.strip() + '\n')
    f.close()

def combineTxt(file, target):
    fw = open(target, 'a')
    fr = open(file, 'r')
    lines = fr.readlines()
    for line in lines:
        fw.write(line)
    fr.close()
    fw.close()

def genTrainData(text, entities_str):
    off_set_b = ['有', '可见', '口服', '可闻', '闻及', '无']
    result_list = []    #utf8
    text = text.replace(' ', '')
    text = text.replace('\r', '')
    entities_str = entities_str.replace(' ', '')

    sentence_list = tools.sentence_split(text)  #unicode
    entity_list = []    #unicode

    entities_info = entities_str.strip().split('\n')
    if len(entities_info) <= 1:
        entities_info = entities_str.strip().split('；')

    ent_re = re.compile(u'(.+)\u3010(.+)\u3011')
    for entity_info in entities_info:
        entity_i = entity_info.strip()
        if entity_i == '':
            continue
        match = ent_re.search(entity_i)
        if match:
            content, enttype = match.group(1), match.group(2)
            try:
                enttype = enttype_dic[enttype]
            except Exception, e:
                print '程序中没有', enttype , '对应的英文。'

            entity_list.append((content.decode('utf8'), enttype))
    
    if len(entity_list) == 0:
        return []
    else:
        ent_index = 0
        for sentence in sentence_list:
            sentence = sentence.decode('utf8')
            length = len(sentence)
            tmp = sentence
            sen_ents_list = []
            sen_index = 0
            while sen_index < length and ent_index < len(entity_list):
                tmp = sentence[sen_index:]
                content = entity_list[ent_index][0]
                if tmp.startswith(content):
                    new_con = content
                    start = sen_index
                    end = start + len(content)
                    for keyword in off_set_b:
                        if content.startswith(keyword.decode('utf8')):
                            index = len(keyword.decode('utf8'))
                            new_con = content[index:]
                            start = sen_index + index
                            break
                    sen_ents_list.append((new_con.encode('utf8'), start, end, entity_list[ent_index][1]))
                    sen_index = end
                    ent_index += 1
                else:
                    sen_index += 1

            if len(sen_ents_list) != 0:
                sen_ents_list.sort(key = lambda x: x[1])
                sen_ents_str = []
                for ent in sen_ents_list:
                    sen_ents_str.append('%s\t%d\t%d\t%s' % (ent[0], ent[1], ent[2], ent[3]))
                result_list.append((sentence, sen_ents_str))
    return result_list

def getDataFromExcel(excelfile):
    result_list = []
    try:
        data = xlrd.open_workbook(excelfile)
    except Exception, e:
        return -1
    table = data.sheets()[0]
    nrows = table.nrows
    for i in range(1, nrows):
        row = table.row_values(i)

        #if row[1].strip() in data_types:
        text = row[0]
        text = text.replace('\\r', '\n')
        text = tools.uniformSignal(text)
        if text.strip() == '': continue
        try:
            entities_str = row[1]   
        except IndexError, e:
            continue
        sen_ent_list = genTrainData(text, entities_str)
        #print i, sen_ent_list
        result_list.extend(sen_ent_list)
    return result_list

def writeExcel2Txt(data_folder, inputfilename, outputfilename):
    file1 = os.path.join(data_folder, inputfilename)
    result = os.path.join(data_folder, outputfilename)
    result_list = getDataFromExcel(file1)
    # result_list.extend(getDataFromExcel(file2))
    if result_list == -1:
        return -1

    with open(result, 'w') as f:
        for sen_ents in result_list:
            f.write(sen_ents[0] + '\t\t\t')
            f.write('\t\t'.join(sen_ents[1]))
            f.write('\n')
    return 0

def main():
    root = sys.path[0]
    datafile = sys.argv[1]
    targetfile = sys.argv[2]
    try:
        combinefile = sys.argv[3]
        if not combinefile.endswith('txt'):
            print '错误:待合并的文件需为TXT文件。'
            exit(1)
        needCombine = True
    except IndexError, e:
        needCombine = False

    data_folder = os.path.join(root, os.path.join('data', 'update_data'))
    if not os.path.exists(data_folder): os.mkdir(data_folder)

    if (datafile.endswith('.xls') or datafile.endswith('.xlsx')) and targetfile.endswith('.txt'):
        if writeExcel2Txt(data_folder, datafile, targetfile) == -1:
            print "错误:打开EXCEL文件失败。"
            exit(1)
    else:
        print '错误:输入的文件需为EXCEL文件，输出的文件需为TXT文件。'
        exit(1)

    # combine data
    if needCombine:
        combineTxt(os.path.join(data_folder,targetfile), combinefile)

if __name__ == '__main__':
    main()

