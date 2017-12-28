#!/usr/bin/env python
# coding: utf-8
# 命令： python main.py [prepareTrain]

import os
import sys
import datetime
import time
import threading
import math

import tools
import processing
import evaluation


def modelTraining(model_file, trainingdata):
    cmd = 'crfsuite learn -m ' + model_file + ' -a lbfgs ' + trainingdata
    os.system(cmd)
    time.sleep(5)

def main(trainfile, modelfile, prepareTraindata, flag_print=False):
    root = sys.path[0]

    datafolder = os.path.join(root, 'data')
    tempdatafolder = os.path.join(datafolder, 'tempdata')
    if not os.path.exists(tempdatafolder): os.mkdir(tempdatafolder)
    tempfolder = os.path.join(root, 'temp')
    dicfolder = os.path.join(root, 'dic')
    modelfoler = os.path.join(root, 'models')
    dic_file = os.path.join(dicfolder, 'dic.txt')
    
    ebao_dic = tools.loadDic(dic_file)

    train_filenames = [(trainfile, 'un')] # un 多了一类medicine
    train_length = len(train_filenames)
    train_file_list = []
    boundary4training_list = []
    class4training_list = []

    for i in range(train_length):
        train_file_list.append(os.path.join(datafolder, train_filenames[i][0]))
        boundary4training_list.append(os.path.join(tempdatafolder, 'boundary4training_' + str(i)))
        class4training_list.append(os.path.join(tempdatafolder, 'class4training_' + str(i)))

    test_filenames = [('jsd-test.txt', 'jsd'), ('ct-test.txt', 'un')]

    test_length = len(test_filenames)
    test_file_list = []
    boundary4testing_list = []
    class4testing_list = []
    sen_ent4testing_list = []
    for i in range(test_length):
        test_file_list.append(os.path.join(datafolder, test_filenames[i][0]))
        boundary4testing_list.append(os.path.join(tempdatafolder, 'boundary4testing_' + str(i)))
        class4testing_list.append(os.path.join(tempdatafolder, 'class4testing_' + str(i)))
        sen_ent4testing_list.append(os.path.join(tempdatafolder, 'sen_ent4testing_' + str(i)))

    sentence_list_test = []
    sen_tags_list_test = []
    if prepareTraindata == 'prepareTrain':
        for i in range(train_length):
            processing.generateFullTagFile(train_file_list[i], boundary4training_list[i], class4training_list[i], '', '0', ebao_dic, 'train', '0', train_filenames[i][1])
        if flag_print:
            print 'Train data generated!'

    for i in range(test_length):
        sentence_list, sen_tags_list = processing.generateFullTagFile(test_file_list[i], boundary4testing_list[i], class4testing_list[i], sen_ent4testing_list[i], '1', ebao_dic, 'train', '0', test_filenames[i][1])
        sentence_list_test.append(sentence_list)
        sen_tags_list_test.append(sen_tags_list)
    if flag_print:
        print 'Test data generated!'

    for k in range(train_length):
        if flag_print:
            print train_filenames[k][0]
        b_model = os.path.join(modelfoler, 'b-'+ modelfile + '-' + str(k))
        c_model = os.path.join(modelfoler, 'c-'+ modelfile + '-' + str(k))
        
        thread_b = threading.Thread(target=modelTraining, args=(b_model, boundary4training_list[k]))
        thread_b.start()
        thread_b.join()
        if flag_print:
            print train_filenames[k][0] + b_model + ' generated!'
        thread_c = threading.Thread(target=modelTraining, args=(c_model, class4training_list[k]))
        thread_c.start()
        thread_c.join()
        if flag_print:
            print train_filenames[k][0] + c_model + ' generated!'
        
        for j in range(test_length):
            if flag_print:
                print '\nTraindata: ' + train_filenames[k][0] + '\n'
                print '\nTestdata: ' + test_filenames[j][0] + '\n'
            boundary_result = boundary4testing_list[j] + '.result'
            os.system('crfsuite tag -m ' + b_model + ' ' + boundary4testing_list[j] + ' > ' + boundary_result)
            if flag_print:
                print 'boundary test result generated!'
            evaluation.eval(boundary4testing_list[j], boundary_result, 'boundary', sen_ent4testing_list[j], ebao_dic)
            if flag_print:
                print test_filenames[j][0] + 'boundary test evaluated!'
            class_result = class4testing_list[j] + '.result'
            os.system('crfsuite tag -m ' + c_model + ' ' + class4testing_list[j] + ' > ' + class_result)
            if flag_print:
                print 'class test result generated!'
            evaluation.eval(class4testing_list[j], class_result, 'class', sen_ent4testing_list[j], ebao_dic)
            if flag_print:
                print test_filenames[j][0] + 'class test evaluated!'
            # 用接口实现boundarymodel生成的数据对应的class向量，并对应每个句子中的实体预测其类别, post_processing = '1'
            processing.predictClassAfterBoundaryAndEval(boundary_result, sentence_list_test[j], sen_tags_list_test[j], c_model, ebao_dic, '0', test_filenames[j][1])
            processing.predictClassAfterBoundaryAndEval(boundary_result, sentence_list_test[j], sen_tags_list_test[j], c_model, ebao_dic, '1', test_filenames[j][1])
            if flag_print:
                print test_filenames[j][0] + 'combine develop evaluated!'
    print 'train end'

if __name__ == '__main__':
    # 训练数据准备
    trainfile = sys.argv[1]
    modelfile = sys.argv[2]
    try:
    	prepareTraindata = sys.argv[3]
    except Exception, e:
    	prepareTraindata = 'no'

    main(trainfile, modelfile, prepareTraindata, True)

