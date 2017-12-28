#!/usr/bin/env python
#coding:utf-8

import sys
import os
import threading
import random
import time
import math
import crfsuite
import generateFeature
import processing
import tools
import evaluation

class Entity(object):
	def __init__(self, con, start, end, t):
		self.content = con
		self.start_pos = start
		self.end_pos = end
		self.enttype = t

root = sys.path[0]
datafolder = os.path.join(root, 'data')
modelfolder = os.path.join(root, 'models')
tempfolder = os.path.join(root, 'temp')
dicfolder = os.path.join(root, 'dic')
ebao_dic = tools.loadDic(os.path.join(dicfolder, 'dic.txt'))
logfile = os.path.join(tempfolder, 'active.log')

def modelTraining(model_file, trainingdata):
	cmd = 'crfsuite learn -m ' + model_file + ' -a lbfgs ' + trainingdata + ' > ' + logfile
	os.system(cmd)
	time.sleep(5)

def selectActiveData(unselected, selected, model, num):
	select = []
	tagger_bp = crfsuite.Tagger()
	tagger_bp.open(model)
	bieso = ['B-entity', 'I-entity', 'E-entity', 'S-entity', 'O']
	entropy = []
	for line in unselected:
		sentence, entities_in_sentence = processing.generateSenEntities(line, '')
		new_sentence, new_entities = processing.symbolProcess(sentence, entities_in_sentence)
		sentence_unicode = new_sentence.decode('utf-8')
		tag_seq = processing.generateTagSeq(sentence_unicode, new_entities)
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
		ie_entity = 0.0
		for i in range(length):
			yseq.append(yseq_b[i])
		
		for j in range(len(yseq)):
			ie = 0.0 # 信息熵
			for ent_tag in bieso:
				try:
					tag_prob = tagger_bp.marginal(ent_tag, j)
					ie += tag_prob * math.log(1 / tag_prob, 2)
				except Exception, e:
					print line
					exit(0)
			ie_entity += ie	
		entropy.append((line, ie_entity))
	entropy.sort(key=lambda x:x[1], reverse=True)

	for i in range(num):
		unselected.remove(entropy[i][0])
		selected.append(entropy[i][0])

def main():
	train_filename = os.path.join(datafolder, 'ct-train.txt')
	test_filename = os.path.join(datafolder, 'ct-test.txt')

	#训练数据准备
	boundary4training = os.path.join(tempfolder, 'boundary4training_all')
	processing.generateBoundaryTagFile(train_filename, boundary4training, ebao_dic, '0', '')

	#测试数据准备
	boundary4testing = os.path.join(tempfolder, 'boundary4testing')
	boundary4testing_senent = os.path.join(tempfolder, 'boundary4testing_senent')
	processing.generateBoundaryTagFile(test_filename, boundary4testing, ebao_dic, '1', boundary4testing_senent)

	#全部数据模型
	b_model = os.path.join(modelfolder, 'boundarymodel_all')
	thread_b = threading.Thread(target=modelTraining, args=(b_model, boundary4training))
	thread_b.start()
	thread_b.join()
	print boundary4training + ' ' + b_model + ' generated!'

	boundary_result = boundary4testing + '.result'
	os.system('crfsuite tag -m ' + b_model + ' ' + boundary4testing + ' > ' + boundary_result)
	print 'boundary test result generated!'
	evaluation.eval(boundary4testing, boundary_result, 'boundary', boundary4testing_senent, ebao_dic)

	#初始训练数据
	first_train_size = 100
	with open(train_filename, 'r') as fr:
		data = fr.readlines()
	lines = []
	for line in data:
		if line not in lines:
			lines.append(line)

	random.shuffle(lines)
	selected = lines[:first_train_size]
	unselected = lines[first_train_size:]

	active_learning_times = 6
	active_learning_pace = 100
	
	for i in range(1, 1+active_learning_times):
		print 'Time ' + str(i) + ' starts...'
		print 'trainset size: ' + str(len(selected))
		boundary4training_i = os.path.join(tempfolder, 'boundary4training_' + str(i))
		processing.generateBoundaryTagFile(selected, boundary4training_i, ebao_dic, '0', '')

		model_i = os.path.join(tempfolder, 'boundarymodel_' + str(i))
		thread_b = threading.Thread(target=modelTraining, args=(model_i, boundary4training_i))
		thread_b.start()
		thread_b.join()
		print model_i + ' generated!'
		
		boundary_result_i = boundary4testing + str(i) + '.result'
		os.system('crfsuite tag -m ' + model_i + ' ' + boundary4testing + ' > ' + boundary_result_i)
		print 'boundary test result generated!'
		evaluation.eval(boundary4testing, boundary_result_i, 'boundary', boundary4testing_senent, ebao_dic)

		print 'Time ' + str(i) + ' ends.'
		selectActiveData(unselected, selected, model_i, active_learning_pace)
	
	print 'random starts...'
	lines2 = lines[first_train_size:]
	random.shuffle(lines2)
	
	randomset = lines[:first_train_size]
	randomset.extend(lines2[:active_learning_pace * (active_learning_times - 1)])

	print 'random size: ' + str(len(randomset))
	boundary4training_random = os.path.join(tempfolder, 'boundary4training_random')
	processing.generateBoundaryTagFile(randomset, boundary4training_random, ebao_dic, '0', '')
	random_model = os.path.join(tempfolder, 'boundarymodel_random')
	thread_r = threading.Thread(target=modelTraining, args=(random_model, boundary4training_random))
	thread_r.start()
	thread_r.join()
	print boundary4training_random + ' ' + random_model + ' generated!'

	boundary_result_random = boundary4testing + '_random.result'
	os.system('crfsuite tag -m ' + random_model + ' ' + boundary4testing + ' > ' + boundary_result_random)
	print 'boundary test result generated!'
	evaluation.eval(boundary4testing, boundary_result_random, 'boundary', boundary4testing_senent, ebao_dic)	


if __name__ == '__main__':
	main()
