# encoding:utf-8

import os
import sys
import xlrd
import re
import tools
import random

reload(sys)
sys.setdefaultencoding('utf8')

def divide_train_test(output_file, train_file, test_file):
    f = open(output_file, 'r')
    lines = f.readlines()
    f.close()
    random.shuffle(lines)

    fw_train = open(train_file, 'w')
    fw_test = open(test_file, 'w')
    train_num = 0
    test_num = 0
    length = len(lines)
    train_test = 1 # 训练测试比
    train_length = length - length / (train_test + 1)
    for i in range(length):
        if lines[i].find('|') != -1:
            fw_train.write(lines[i])
            train_num += 1
            continue
        if train_num < train_length:
            fw_train.write(lines[i])
            train_num += 1
            continue
        # 写入ct-train.new和ct-test.new
        fw_test.write(lines[i])
    fw_train.close()
    fw_test.close()

def genTrainData(text, entities_str):
	# off_set_b = ['有', '可见', '口服', '可闻', '闻及', '无']
	off_set_b = []
	result_list = []	#utf8
	text = text.replace(' ', '')
	text = text.replace('\r', '')
	entities_str = entities_str.replace(' ', '')

	sentence_list = tools.sentence_split(text)	#unicode
	entity_list = []	#unicode

	entities_info = entities_str.strip().split('；')
	ent_re = re.compile(u'(.+)\u3010(.+)\u3011')
	for entity_info in entities_info:
		entity_i = entity_info.strip()
		if entity_i == '':
			continue
		# match = ent_re.search(entity_i)
		# if match:
		# 	content, enttype = match.group(1), match.group(2)
		# 	try:
		# 		enttype = enttype_dic[enttype]
		# 	except Exception, e:
		# 		print '程序中没有', enttype , '对应的英文。'
		entity_list.append((entity_i.decode('utf8'), 'social_insurance'))
	
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
	data = xlrd.open_workbook(excelfile)
	table = data.sheets()[0]
	nrows = table.nrows
	for i in range(1, nrows):
		row = table.row_values(i)

		# if row[1].strip() in data_types:
		text = row[0]
		text = text.replace('\\r', '\n')
		if text.strip() == '': continue
		entities_str = row[1]			
		sen_ent_list = genTrainData(text, entities_str)
		result_list.extend(sen_ent_list)
		print text, entities_str, sen_ent_list
	return result_list

def writeExcel2Txt(data_folder, inputfilename, outputfilename):
	file1 = os.path.join(data_folder, inputfilename)
	result = os.path.join(data_folder, outputfilename)
	result_list = getDataFromExcel(file1)
	# result_list.extend(getDataFromExcel(file2))

	with open(result, 'w') as f:
		for sen_ents in result_list:	
			f.write(sen_ents[0] + '\t\t\t')
			f.write('\t\t'.join(sen_ents[1]))
			f.write('\n')

def main():
	root = sys.path[0]
	data_folder = os.path.join(root, 'data')
	
	writeExcel2Txt(data_folder, 'si20161125.xlsx', 'si.txt')
	
	si = os.path.join(data_folder, 'si.txt')
	si_train = os.path.join(data_folder, 'si-train.txt')
	si_test = os.path.join(data_folder, 'si-test.txt')
	divide_train_test(si, si_train, si_test)

if __name__ == '__main__':
	main()