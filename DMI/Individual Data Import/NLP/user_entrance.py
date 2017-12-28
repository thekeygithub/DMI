#!/usr/bin/env python
#coding:utf8
import xlrd
import xlwt
import os
import sys
import crfsuite
import generateFeature
import evaluation
import tools
import postProcessing
import csv

reload(sys)
sys.setdefaultencoding('utf-8')

root = sys.path[0]

dicfolder = os.path.join(root, 'dic')
ebao_m_dic = tools.loadDic(os.path.join(dicfolder, 'dic.txt'))
ebao_si_dic = tools.loadDic(os.path.join(dicfolder, 'dic_si.txt'))

class Entity(object):
	def __init__(self, con, start, end, t):
		self.content = con
		self.start_pos = start
		self.end_pos = end
		self.type = t

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
	'social_insurance' : '社保',
}

def setModel(modeltype):
	global model_b, model_c, texttype, ebao_dic
	ebao_dic = ebao_m_dic
	if modeltype == "0":  #默认模型
		model_b = os.path.join(root, './models/boundarymodel-6')
		model_c = os.path.join(root, './models/classmodel-6')
		texttype = 'un'
	elif modeltype == "1":	#结算单模型
		model_b = os.path.join(root, './models/boundarymodel-4')
		model_c = os.path.join(root, './models/classmodel-4')
		texttype = 'jsd'
	elif modeltype == "2":	#非结构化模型
		model_b = os.path.join(root, './models/boundarymodel-5')
		model_c = os.path.join(root, './models/classmodel-5')
		texttype = 'un'
	elif modeltype == "3":	#社保模型
		model_b = os.path.join(root, './models/boundarymodel-si-0')
		model_c = os.path.join(root, './models/classmodel-si-0')
		texttype = 'si'
		ebao_dic = ebao_si_dic
	else:
		model_b = os.path.join(root, './models/boundarymodel-6')
		model_c = os.path.join(root, './models/classmodel-6')
		texttype = 'un'

	if not os.path.exists(model_b):
		print '未找到对应的模型文件%s。' % model_b
		exit(1)
	if not os.path.exists(model_c):
		print '未找到对应的模型文件%s。' % model_c
		exit(1)

def generateNerInSentence(inputline, yseq, model_type, ebao_dic): # input: unicode; output: utf8
	sen_ner = ''
	entity_list = ''
	tag_list = []
	tag_list1 = []
	if texttype != 'si':
		yseq = postProcessing.twoProcessings(inputline, yseq, ebao_dic, '')

	tag_list.append(yseq)
	ents, s_e_list = evaluation.generateEntList(tag_list)

	entities = ents[0]
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
			entity_list += content.encode('utf8') + '【' + en_cn_dic[enttype] + '】；'
		else:
			entity_list += content.encode('utf8') + '【' + en_cn_dic[enttype] + '】；'
			continue
	return entity_list

def getNerResult(inputstring, tagger_b, tagger_c, bieso):
	# inputstring = unicode(inputstring)
	# inputsentence = tools.uniformSignal(inputstring.encode('utf8'))
	lines = tools.sentence_split(inputstring)

	ent_list = ''
	for line in lines:
		line = line.strip()
		# 去除标签部分，以<开头且以>结尾的过滤
		#if line == '' or line[0] == '<' and line[-1] == '>' : continue
		if line == '': continue

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
		prob_b = tagger_b.probability(yseq_b)
		line_unicode = line.decode('utf-8')

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
		feature_c, sen_ent4error = generateFeature.classFeatureGeneration(sentence, entities, ebao_dic, '')
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

		ents = generateNerInSentence(line_unicode, new_yseq, model_chosen, ebao_dic)
		ent_list += ents
	return ent_list

def excute(excelfile, resultfile):
	
	tagger_b = crfsuite.Tagger()
	tagger_b.open(model_b)
	tagger_c = crfsuite.Tagger()
	tagger_c.open(model_c)

	print model_b, model_c
	bieso = ['B-entity', 'I-entity', 'E-entity', 'S-entity', 'O']


	if not (excelfile.endswith('.xls') or excelfile.endswith('.xlsx')):
		print '请输入正确的文件格式。'
		exit(1)

	data = xlrd.open_workbook(excelfile)
	workbook = xlwt.Workbook()
	for sheeti in range(len(data.sheets())):
		table = data.sheets()[sheeti]
		nrows = table.nrows

		if resultfile.endswith('.xls') or resultfile.endswith('.xlsx'):			
			sheet = workbook.add_sheet(table.name)

			style = xlwt.easyxf('font: bold 1;')
			sheet.write(0, 0, u'原始数据', style)
			sheet.write(0, 1, u'实体识别结果', style)

			print 'generating %s...' % table.name
			for i in range(1, nrows):

				line = table.row_values(i)[0]
				new_line = tools.uniformSignal(line)
				print 'new line: ', new_line
				sheet.write(i, 0, unicode(line))
				sheet.write(i, 1, unicode(getNerResult(new_line, tagger_b, tagger_c, bieso)))
		else:
			print '请输入正确的文件格式。'
			exit(1)
	workbook.save(resultfile) 

if __name__ == '__main__':
	excelfile = sys.argv[1]		#数据文件
	resultfile = sys.argv[2]	#结果文件
	try:
		bmodeltype = sys.argv[3]		#模型选择
		if not bmodeltype.isdigit():
			try:
				cmodeltype = sys.argv[4]
				global model_b, model_c, texttype, ebao_dic
				ebao_dic = ebao_m_dic
				texttype = 'un'
				model_b = bmodeltype
				model_c = cmodeltype
				if not os.path.exists(model_b):
					print '未找到对应的模型文件%s。' % model_b
					exit(1)
				if not os.path.exists(model_c):
					print '未找到对应的模型文件%s。' % model_c
					exit(1)
			except Exception, e:
				print '若要指定模型文件，请输入边界模型与分类模型。'
				exit(1)		
		else:
			modeltype = bmodeltype
			setModel(modeltype)
	except Exception, e:
		modeltype = "0"
		setModel(modeltype)

	excute(excelfile, resultfile)

