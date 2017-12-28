#!/usr/bin/env python
# coding: utf-8

import sys
import os
import re
import codecs
import tools

#字典匹配：双向匹配策略
def matchEntityInDic(sen, dic, texttype):
	#逆向
	entities = ['O' for i in range(len(sen))]
	begin = 0
	end = len(sen)
	while end > 0:
		begin = 0
		matchedFlag = False
		while end > begin:
			word = sen[begin:end].encode('utf8')
			if word in dic and end > begin:
				enttype = dic[word]
				if enttype == 'medicine_tc':
					enttype = 'medicine'
				if end == begin + 1:
					entities[begin] = 'S-' + enttype
				else:
					entities[begin] = 'B-' + enttype
					entities[end-1] = 'E-' + enttype
					for index in range(begin+1, end-1):
						entities[index] = 'I-' + enttype
				end = begin
				matchedFlag = True
				break
			else:
				begin += 1
		if not matchedFlag:
			end = end - 1
	#正向
	entities2 = ['O' for i in range(len(sen))]
	begin, end = 0, len(sen)
	while begin < len(sen):
		end = len(sen)
		matchedFlag = False
		while end > begin:
			word = sen[begin:end].encode('utf8')
			if word in dic and end > begin:
				enttype = dic[word]
				if enttype == 'medicine_tc':
					enttype = 'medicine'
				if end == begin + 1:
					entities2[begin] = 'S-' + enttype
				else:
					entities2[begin] = 'B-' + enttype
					entities2[end-1] = 'E-' + enttype
					for index in range(begin+1, end-1):
						entities2[index] = 'I-' + enttype
				begin = end
				matchedFlag = True
				break
			else:
				end -= 1
		if not matchedFlag:
			begin += 1
	ent1 = tools.genEntlistByTags(entities)
	ent2 = tools.genEntlistByTags(entities2)
	ent_all = []
	for ent in ent1:
		if ent in ent2:
			ent_all.append(ent)

	seq = ['O' for i in range(len(sen))]
	for ent in ent_all:
		start, end, enttype = ent
		if end - start == 1:
			seq[start] = 'S-' + enttype
		else:
			seq[start] = 'B-' + enttype
			seq[end-1] = 'E-' + enttype
			for index in range(start+1, end-1):
				seq[index] = 'I-' + enttype
	return seq

# 自动识别的结果结合字典匹配的结果，得到二者结合的输出
# 输入：格式统一后的句子，系统输出的字的标签
def combineDicAndSystem(input_unicode, tags_in_system, ebao_dic, texttype):
	tags_in_dic = matchEntityInDic(input_unicode, ebao_dic, texttype)
	#二者合并 - 逆向最大匹配思想
	new_tags = ['O' for i in range(len(input_unicode))]
	i = len(input_unicode) - 1
	while i >= 0:
		if tags_in_system[i].startswith('E-'):
			#均为E-
			if tags_in_dic[i].startswith('E-'):
				k = i - 1
				while tags_in_dic[k] != 'O' and tags_in_system[k].startswith('I-'):
					k -= 1
				#系统输出结果较长,按系统输出
				if tags_in_dic[k] == 'O':
					while tags_in_system[k].startswith('I-'):
						k -= 1
					for index in range(k, i+1):
						new_tags[index] = tags_in_system[index]	
				#一样长
				elif tags_in_dic[k].startswith('B-'):
					#1014 99:129
					isSameType = True
					sametype = tags_in_system[i][2:]
					for tmp_i in range(k, i+1):
						if tags_in_dic[tmp_i][2:] == sametype:
							continue
						else:
							isSameType = False
							break
					if isSameType:#类别都相同时，按照字典切分并输出
						for index in range(k, i+1):	#tags_in_dic[k]='B-...'
							new_tags[index] = tags_in_dic[index]	
					else:	#类型有区别
						# # 系统输出结果为非症状类时，按照字典输出
						# if not tags_in_system[i].endswith('symptom'):
						for index in range(k, i+1):	#tags_in_dic[k]='B-...'
							if tags_in_system[index][2:] in ['medicine_cn', 'medicine_mn', 'medicine_pn'] and tags_in_dic[index][2:] == 'medicine':
								new_tags[index] = tags_in_system[index]
							# elif tags_in_dic[index][2:] == 'medicine_tc':
							# 	new_tags[index] = tags_in_dic[index][:2] + 'medicine'
							else:
								new_tags[index] = tags_in_dic[index]
								
				#字典匹配的结果较长
				else:
					while tags_in_dic[k].startswith('I-'):
						k -= 1
					for index in range(k, i+1):
						new_tags[index] = tags_in_dic[index]					

				i = k - 1
			#系统输出E-，字典没有
			else:
				new_tags[i] = tags_in_system[i]
				i -= 1
				while tags_in_system[i].startswith('I-'):
					new_tags[i] = tags_in_system[i]
					i -= 1
				if tags_in_system[i].startswith('B-'):
					new_tags[i] = tags_in_system[i]
					i -= 1
				continue
		elif tags_in_system[i].startswith('S-'):
			if tags_in_dic[i].startswith('E-'):
				new_tags[i] = tags_in_dic[i]
				i -= 1
				while tags_in_dic[i].startswith('I-'):
					new_tags[i] = tags_in_dic[i]
					i -= 1
				if tags_in_dic[i].startswith('B-'):
					new_tags[i] = tags_in_dic[i]
					i -= 1
				continue
			elif tags_in_dic[i].startswith('S-'):
				new_tags[i] = tags_in_dic[i]
				i -= 1
			else:
				new_tags[i] = tags_in_system[i]
				i -= 1
		else:
			# i -= 1
			if tags_in_dic[i].startswith('E-'):
				matched_start = i
				matched_end = i + 1
				i -= 1
				while tags_in_dic[i].startswith('I-'):
					matched_start -= 1
					i -= 1
				if tags_in_dic[i].startswith('B-'):
					matched_start -= 1
					i -= 1
				#if matched_end - matched_start < 4: continue
				for j in range(matched_start, matched_end):
					new_tags[j] = tags_in_dic[j]
			else:
				i -= 1
	return new_tags

def changeEntType(input_utf8, ogtype, ebao_dic, texttype):

	#1031 尾字处理 分离括号
	input_uniform = tools.uniformSignal(input_utf8)
	input_re = re.compile(u'(.+)\uFF08.+\uFF09$')
	input_unicode = input_utf8.decode('utf-8')
	if '（' in input_uniform:
		m = re.match(input_re, input_uniform.decode('utf8'))
		if m:
			endpos = m.end(1)
			entity = input_unicode[:endpos].encode('utf8')
		else:
			entity = input_utf8
	else:
		entity = input_utf8

	if entity in ['自然分娩', '终止妊娠']:
		return 'O'

	diagnosis_name = ['像', '目', '查', '造影', '测定', '检测', \
		'检查', '病理诊断', '摄影', '显像', '成像', '图像融合', '多普勒', '试验', '测量', '定量', '测序', '鉴定', '常规', '图', '化学分析', '超声', '筛查', 'CT', '平扫', '透视', '透析', '扩增', '计数', '项', '曲线', '描记', '功能', '分析', '彩超', '培养', 
	] # '试剂', '镜',

	treatment = ['术', '治疗', \
		'化疗', '放疗', '成形', '溶栓', '置入', '置管', '植入', '结扎', '包扎', '换药', '切除', '针灸', '拆线', '反搏', '引流', '固定', '法', '扩张',
	]

	other_diagnosis = ['设计', '标本', '定位',  '照射', '制备', '会诊', '细胞学',
	]

	medicine_pn = ['片', '丸', '贴', '膏', '颗粒', '胶囊', '糖浆', '凝胶',
	]

	instrument = ['钉', '钩', '椅', '具', '夹', '钳', '械', '箱', '刀', '剪', '柄', '镊', '机', '仪', '板', '器', '台', '床', '统', '套', '件', '包', '灯', '极', '架', '帽', '置', '罩', '车', '钻', '巾', '杯', '基', '签', '针', '泵', '计', '带', '鞘液', '棉球', '引管', '导丝', '敷料', '氧管', '接头', '推车', '养基', '眼镜', '控液', '棉棒', '染液', '胶带', '性能', '射泵', '垫单', '型号', '流袋', '带针', '假体', '口罩', '面罩', '探头', '缝线', '温计', '用品', '义齿', '色液', '疗贴', '绷带', '刺针', '管路', '棉签', '控品', '敷贴', '光源', '准品', '血带', '定带', '设备', '桥体', '合金', '作站', '标液', '装置', '材料', '释液', '导管', '血剂', '压计', '系列', '插管', '流管', '道镜', '喉镜', '剂盒', '理液', '触镜', '试纸', '纸条', '接管', '微镜', '窥镜', '晶体', '冲液', '痛贴', '电极', '支架','球囊', '胶片', '导尿管', '用试剂', '清洗液', '显微镜', '定试剂', '内窥镜', '缓冲液', '创可贴',
	] # '试剂', 

	enterprise = ['铺', '厂', '店', '处', '社', '房', '院', '司', '场', '所', '堂', '城', '行', '中心', '药号', '药行', '送站', '发站', '专柜', '商行', '分号', '药站', '应站', '药柜', '药堂', '售点', '超市', '门市', '药庄', 
	] # '部', 

	address = ['市', '村', '号', '组', '县', '庄', '都', '昌',
	]

	disease = ['病', '疝', '癜', '娠', '痫', '疽', '瘘', '癣', '疣', '生症', '息肉', '光眼', '湿疹', '乏症', '过速', '紫癜', '化症', '合症', '多症', '合征', '芽肿', '斜视', '妊娠', '积症', '结核', '中毒', '憩室', '尿症', '阻滞', '贫血', '脓肿', '脉狭窄', '骨骨折', '酸中毒', '性肝炎', '养不良', '栓形成', '膜病变', '毒感染', '性贫血', '心脏病', '过敏', '梗塞', '梗死',
	] # '病变', '瘤', '癌', 

	healthy_food = ['饮料', '藻片'
	]

	if ogtype in ['medicine_cn', 'medicine_mn', 'medicine_pn', 'medicine']:
		for keyword in medicine_pn + ['液']:
			if entity.endswith(keyword):
				ogtype = 'medicine_pn'
		if keyword in ['动力剂', '调节剂']:
			if entity.endswith(keyword):
				ogtype = 'medicine'
		return ogtype

	if entity.endswith('科'):
		return 'department'
	if entity.endswith('史'):
		term = entity.decode('utf8')[:-1].encode('utf8')
		if ebao_dic.has_key(term):
			return ebao_dic[term]

	if ogtype == 'dt_med_ins':
		for keyword in ['注意']:
			if entity.startswith(keyword):
				return 'other_diagnosis'
		for keyword in ['术后']:
			if entity.endswith(keyword):
				return 'treatment'
	else:
		for keyword in instrument:
			if entity.endswith(keyword):
				ogtype = 'instrument'

	for keyword in ['取']:
		if entity.startswith(keyword):
			return 'treatment'

	if entity in ['肠镜', 'T管', '肝上引流管', '宫腔镜', '喉镜', '颈托', '腹腔引流管', '弹力袜', '内镜', '肠梗阻导管', '引流管']: # '腹腔镜'
		return 'instrument'

	for keyword in diagnosis_name:
		if entity.endswith(keyword):
			return 'diagnosis_name'
	for keyword in treatment:
		if entity.endswith(keyword):
			return 'treatment'
	for keyword in other_diagnosis:
		if entity.endswith(keyword):
			return 'other_diagnosis'
	if '术:' in entity or '术：' in entity:
		return 'treatment'
	if entity.endswith('征'):
		term = entity.decode('utf8')[:-1]
		# 匹配汉字，无则过滤
		cw = re.compile(u'[\u4e00-\u9fa5]')
		matches = re.findall(cw, term)
		if len(matches) == 0 and len(term) > 0:
			return 'diagnosis_name'

	for keyword in ['药物', '药', '动力剂', '调节剂']: # , '剂'
		if entity.endswith(keyword):
			return 'medicine'
	if entity in ['激素', '去痛片', '抗生素', '眼药水', '疫苗']:
		return 'medicine'
	if entity in ['新鲜冰冻血浆', '红细胞']:
		return 'medicine_cn'
	if entity in ['美卓乐', '诺和锐30', '诺和平']:
		return 'medicine_mn'
	for keyword in ['疫苗']:
		if entity.endswith(keyword):
			return 'medicine_cn'

	for keyword in enterprise:
		if entity.endswith(keyword):
			return 'enterprise'

	for keyword in address:
		if entity.endswith(keyword):
			return 'address'
	if entity.endswith('路'):
		if '市' in entity or '省' in entity or '县' in entity:
			return'address'
	if entity in ['流产']:
		return 'disease'
	for keyword in disease:
		if entity.endswith(keyword):
			return 'disease'
	for keyword in ['恶变']:
		if entity.endswith(keyword):
			return 'symptom'
	for keyword in healthy_food:
		if entity.endswith(keyword):
			return 'healthy_food'

	for keyword in ['支持', '抗炎', '保肝', '镇痛', '术史', '护肝', '改善肾脏血流', '纠正贫血', '降浊', '控制血压', '调节血脂', '纠正离子紊乱', '监护', '吸氧', '强心', '利尿', '扩血管', '对症', '抗凝', '抗血小板', '抗感染', '保护脏器', '循环', '改善脑循环', '改善微循环', '保护脑组织', '营养', '化痰', '消肿', '护理', '补液', '缝合', '剖宫产', '剖腹产', '运动', '锻炼', '训练', '准备', '除颤', '麻醉', '置换', '方案', '降颅压', '冲击', '休息', '止血', '填塞', '升血', '就诊', '随诊', '复诊', 'CAG', '敷料', '移植', '吸引', '灌注', '填充', '碎石', '清扫', '清理', '阻滞', '截肢', '松解', '疏通', '通气', '开放', '干预', '重构', '切除史', '治疗史', '理疗', '活血化瘀', '制动', '抑制', '取出', '靶向', '抗肿瘤', '复位', '穿刺']: # 呼吸
		if entity.endswith(keyword):
			if ogtype in ['diagnosis_name', 'treatment', 'other_diagnosis']:
				return ogtype
			return 'diagnosis_treatment'
	for keyword in ['治疗', '改善', '提高', '加强', '促进', '纠正', '控制', '调节', '调整', '调控', '平衡', '扩', '保护', '维持', '保持', '稳定', '预防', '防止', '防治', '抑制', '合理', '避免', '抗血小板', '调脂', '吸氧', '营养', '包扎', '拆除', '手术', '禁', '升白', '低盐', '输血', '规律', '雾化', '按时', '监测', '对症', '抬高', '卧床']:
		if entity.startswith(keyword):
			if ogtype in ['diagnosis_name', 'treatment', 'other_diagnosis']:
				return ogtype
			return 'diagnosis_treatment'
	if entity in ['输血', '止咳', '止痛', '平喘', '保暖', '扶正', '解痉', '降纤', '降血氨', '降压', '降糖', '降酶', '降温', '降脂', '降血脂', '降尿蛋白', '降蛋白尿', '抗结核', '抗过敏', '抗心律失常', '抗免疫排斥', '退热', '保守', '功能练习', '抗病毒', '拔牙', '消炎', '消酮', '脑保护', 'PCI', '利胆', 'IA', 'MA', 'AA', 'mFOLFOX6', '活血', '抑酸', '气管插管', '促醒', '促觉醒', '醒脑', '剌激足底', '留置导尿管', '留置尿管', '导尿', '补钙', '转复心律', '解毒', '坐浴', '祛痰', '免疫调节', '戒酒', '全麻', '切脾', '臀高位', '通便', '高压氧', '暂禁食水', '综合监测生命体征', '抗血栓', '抗真菌', '碘131', '驱虫', 'MTX杀胚', '套扎', '微波', '局部放射', '负压腹腔冲洗', '肝上引流管持续引流胆汁', 'NX解救', '疏通血管', '放射', '腹部切口辅料', '灌肠', '间断技术维持', '补钾', '祛脂', '窒息复苏', '抗乙肝病毒', '降眼压', '血管重建', '降肺动脉压力', '水化', '抗骨质疏松', '抗乙肝病毒', '抗栓', '胃镜放置鼻空肠营养管']:
		if ogtype in ['diagnosis_name', 'treatment', 'other_diagnosis']:
			return ogtype
		return 'diagnosis_treatment'
	if '诊治' in entity or '术（' in entity or '术 (' in entity:
		if ogtype in ['diagnosis_name', 'treatment', 'other_diagnosis']:
			return ogtype
		return 'diagnosis_treatment'
	for keyword in ['复查', '多普勒']:
		if keyword in entity:
			return 'diagnosis_name'

	for keyword in medicine_pn:
		if entity.endswith(keyword):
			ent = re.sub('\d+', '', entity)
			if len(ent.decode('utf-8')) > 3: # X线片
				return 'medicine_pn'

	return ogtype

def loadNonmedicineWords(nonmedicinefile):
	dic = []
	dic_file = codecs.open(nonmedicinefile)
	dic_data = dic_file.readlines()
	for line in dic_data:
		newline = line.strip().replace(' ', '')	#字典中的词不能有空格
		dic.append(newline.decode('utf-8'))
	dic_file.close()
	return dic

def loadDeterminer(determinerfile):
	dic = []
	dic_file = codecs.open(determinerfile)
	dic_data = dic_file.readlines()
	for line in dic_data:
		newline = line.strip().replace(' ', '')	#字典中的词不能有空格
		dic.append(newline.decode('utf-8'))
	dic_file.close()
	return dic

# 实体识别后处理
# sentence为原句经处理后的unicode形式, tags_in_system是与之一一对应的标签
def specialRulesForEmr(sentence, tags_in_system, ebao_dic, texttype):
	if len(tags_in_system) < 1:	
		return tags_in_system	
	entities = []
	new_entities = []
	determiners = loadDeterminer(os.path.join(os.path.join(sys.path[0], 'dic'), 'determiner.txt'))
	non_medicine_words = loadNonmedicineWords(os.path.join(os.path.join(sys.path[0], 'dic'), 'nonmedicine.txt'))
	determiners_of_treatment = loadDeterminer(os.path.join(os.path.join(sys.path[0], 'dic'), 'determiner_diagnosis_treatment.txt'))
	specifications = loadDeterminer(os.path.join(os.path.join(sys.path[0], 'dic'), 'specifications.txt'))

	k = 0
	while k < len(tags_in_system):
		if tags_in_system[k].startswith('S-'):
			entities.append((sentence[k], k, k+1, tags_in_system[k][2:]))
			k += 1
		elif tags_in_system[k].startswith('B-'):
			start = k
			k += 1
			while tags_in_system[k].startswith('I-'):
				k += 1
			if tags_in_system[k].startswith('E-'):
				k += 1
			end = k
			entities.append((sentence[start:end], start, end, tags_in_system[start][2:]))
		else:
			k += 1

	for i, entity in enumerate(entities):
		content, start, end, enttype = entities[i][0], int(entities[i][1]), int(entities[i][2]), entities[i][3]

		#括号相关处理
		left = u'[【(（{'
		right = u']】)）}'
		
		#右边界多左括号
		if content[-1] in left:
			content = content[:-1]
			end -= 1
		#左边界多右括号
		if content[0] in right:
			content = content[1:]
			start += 1

		left_count, right_count = 0, 0
		left_list, right_list = [], []
		for tmp_i in range(len(content)):
			if content[tmp_i] in left:
				left_count += 1
				left_list.append(tmp_i)
			elif content[tmp_i] in right:
				right_count += 1
				right_list.append(tmp_i)
			else:
				continue

		if left_count > right_count and end < len(sentence) and sentence[end] in right:	#左括号比右括号多 且实体后有括号
			right_list.append(end)
			content += sentence[end]
			end += 1
			right_count += 1
		elif right_count > left_count and start > 0 and sentence[start-1] in left:#左括号比右括号少 且实体前有括号
			start -= 1
			left_list.append(start)
			content = sentence[start] + content
			left_count += 1

		if left_count == 1 and right_count == 1 and content[0] in left and content[-1] in right:
			right_list = []
			left_list = []
			start += 1
			end -= 1
			left_count -= 1
			right_count -= 1
			content = content[1:-1]

		ents = []

		if left_count > right_count:
			if right_count != 0:
				for right_pos in right_list:#从最前面的右括号开始，依次匹配到离它最近的在前面的左括号
					for left_pos in reversed(left_list):
						if right_pos > left_pos:
							right_list.remove(right_pos)
							left_list.remove(left_pos)
							break
			curr = 0
			for left_pos in left_list:
				s, e = curr, left_pos
				c = content[s : e]
				curr = e + 1
				if not e > s:
					continue
				else:
					ents.append((c, s+start, e+start, enttype))

			if curr < end - start:
				s, e = curr, end
				c = content[s : e]
				ents.append((c, s+start, e, enttype))

		elif right_count > left_count:
			if left_count != 0:
				for left_pos in reversed(left_list):
					for right_pos in right_list:
						if right_pos > left_pos:
							right_list.remove(right_pos)
							left_list.remove(left_pos)
							break
			curr = 0
			for right_pos in right_list:
				s, e = curr, right_pos
				c = content[s : e]
				curr = e + 1
				if not e > s:
					continue
				else:
					ents.append((c, s+start, e+start, enttype))

			if curr < end - start:
				s, e = curr, end
				c = content[s : e]
				ents.append((c, s+start, e, enttype))
		else:
			ents.append((content, start, end, enttype))


		for ent in ents:
			content, start, end, enttype = ent[0], ent[1], ent[2], ent[3]
			
			
			#去除"2.XXX"这种情况
			pointToBeDeleted = False
			index_pos = content.find('.')
			if index_pos != -1:
				pointToBeDeleted = True
				for tmpi in range(index_pos):
					if not content[tmpi].isdigit():
						pointToBeDeleted = False
						break
			if pointToBeDeleted:
				if index_pos != len(content)-1 and not content[index_pos+1].isdigit():
					start += index_pos + 1
					content = content[index_pos + 1:]

			#c.	去除识别结果中的非医学术语
			for non_medicine_word in non_medicine_words:
				if content.endswith(non_medicine_word):
					len_of_nmw = len(non_medicine_word)
					content = content[:-len_of_nmw]
					end -= len_of_nmw
				elif content.startswith(non_medicine_word):
					len_of_nmw = len(non_medicine_word)
					content = content[len_of_nmw:]
					start += len_of_nmw
				else:
					continue
			if content == '':
				continue
			
			#a.	疾病、症状程度问题
			#i.	把修饰疾病程度的疾病修饰与疾病实体合成一个实体
			if enttype == 'disease':
				for determiner in determiners:
					if content.endswith(determiner) or content.endswith(determiner + u'）'):	#修饰词在后面
						len_of_determiner = len(content)			
						if len_of_determiner <= 5 and (i==len(entities)-1 or entities[i+1][3]!='disease' or end<int(entities[i+1][1])):
							if i>0  and len(new_entities) > 0 and new_entities[-1][3] == 'disease':
								if start - new_entities[-1][2] == 0:
									content = new_entities[-1][0] + content
									start = new_entities[-1][1]
									del(new_entities[-1])
								elif start - new_entities[-1][2] == 1:		#把括号加进去
									if sentence[start-1] in left and end < len(sentence) and sentence[end] in right:
										content = new_entities[-1][0] + sentence[start-1] + content + sentence[end]
										end += 1
										start = new_entities[-1][1]
										del(new_entities[-1])
					elif len(new_entities) != 0:	#修饰词在前面
						last_content = new_entities[-1][0]
						if last_content.endswith(determiner):
							len_of_determiner = len(last_content)
							if len_of_determiner <= 5 and new_entities[-1][2]==start:
								content = determiner + content
								start = new_entities[-1][1]
								del(new_entities[-1])
						break
					else:
						continue
			
			#添加未识别出的疾病程度词
			if enttype == 'disease' or enttype == 'symptom':
				if end < len(sentence) and sentence[end] in left:#限定词在后面，且有括号				
					for determiner in determiners:
						len_of_determiner = len(determiner)
						if end + len_of_determiner + 1 < len(sentence) and sentence[end+len_of_determiner+1] in right:
							if sentence[end+1: end+len_of_determiner+1] == determiner:
								determiner_add_flag = True
								for determiner_index in range(end+1, end+len_of_determiner+1):
									if tags_in_system[determiner_index] != 'O':
										determiner_add_flag = False
										break 
								if determiner_add_flag:
									#补上程度词
									content += sentence[end: end+len_of_determiner+2]
									end += len_of_determiner + 2
									break
				
				else:
					for determiner in determiners:
						len_of_determiner = len(determiner)
						
						if len_of_determiner>1 and start - len_of_determiner >= 0 :#限定词在前面
								if sentence[start - len_of_determiner: start] == determiner:
									determiner_add_flag = True
									for determiner_index in range(start-len_of_determiner, start):
										if tags_in_system[determiner_index] != 'O':
											determiner_add_flag = False
											break
									if determiner_add_flag:
										content = determiner + content
										start -= len_of_determiner
										for determiner_index in range(start-len_of_determiner+1, start+1):
											tags_in_system[determiner_index] = 'I-disease'
										tags_in_system[start-len_of_determiner] = 'B-disease'
										break
						if end + len_of_determiner < len(sentence):	#限定词在后面,且限定词与后面的疾病实体不相邻（限定词优先与后面的实体相接）
							if i == len(entities)-1 or entities[i+1][3]!='disease' or end + len_of_determiner < int(entities[i+1][1]):
								if sentence[end: end+len_of_determiner] == determiner:
									determiner_add_flag = True
									for determiner_index in range(end, end+len_of_determiner):
										if tags_in_system[determiner_index] != 'O':
											determiner_add_flag = False
											break
									if determiner_add_flag:
										#补上程度词 并更改tags -- 防止限定词左右冲突
										content += determiner
										for determiner_index in range(end-1, end+len_of_determiner):
											tags_in_system[determiner_index] = 'I-disease'
										tags_in_system[end+len_of_determiner-1] = 'E-disease'
										end += len_of_determiner
										break

			
			#1014对疾病和症状实体 连接‘伴’连接的实体
			if enttype == 'disease' or enttype == 'symptom':
				if sentence[start-1] == u'伴':
					if len(new_entities) != 0 and new_entities[-1][3] == enttype and start - new_entities[-1][1] == 1:
						content = new_entities[-1][0] + sentence[start-1] + content
						start = new_entities[-1][1]
						del(new_entities[-1])

			#i.	把修饰诊疗程度的修饰与诊疗实体合成一个实体
			if enttype in ['diagnosis_name', 'treatment', 'other_diagnosis']:
				for determiner in determiners_of_treatment:
					if content.endswith(determiner) or content.endswith(determiner + u'）'):				
						if i > 0 and len(new_entities) > 0:					
							if new_entities[-1][3] in ['diagnosis_name', 'treatment', 'other_diagnosis']:
								if start - new_entities[-1][2] == 0:
									content = new_entities[-1][0] + content
									start = new_entities[-1][1]
									del(new_entities[-1])
								elif start - new_entities[-1][2] == 1:
									if sentence[start-1] in left and end < len(sentence) and sentence[end] in right:
										content = new_entities[-1][0] + sentence[start-1] + content + sentence[end]
										end += 1
										start = new_entities[-1][1]
										del(new_entities[-1])
									elif sentence[start-1] in u'：:':
										content = new_entities[-1][0] + sentence[start-1] + content
										start = new_entities[-1][1]
										del(new_entities[-1])
								elif start - new_entities[-1][2] == 2:	
									if sentence[start-1] == '-' and sentence[start-2] == '-':
										content = new_entities[-1][0] + sentence[start-2] + sentence[start-1] + content
										start = new_entities[-1][1]
										del(new_entities[-1])
						break
					else:
						continue
			#添加未识别出的诊疗程度词
			if enttype in ['diagnosis_name', 'treatment', 'other_diagnosis']:
				if end < len(sentence) and sentence[end] in left:				
					for determiner in determiners_of_treatment:
						len_of_determiner = len(determiner)
						if end + len_of_determiner + 1 < len(sentence) and sentence[end+len_of_determiner+1] in right:
							if sentence[end+1: end+len_of_determiner+1] == determiner:
								determiner_add_flag = True
								for determiner_index in range(end+1, end+len_of_determiner+1):
									if tags_in_system[determiner_index] != 'O':
										determiner_add_flag = False
										break 
								#补上程度词
								if determiner_add_flag:
									content += sentence[end: end+len_of_determiner+2]
									end += len_of_determiner + 2
									break
				
				elif end < len(sentence) and sentence[end] in u':：':
					for determiner in determiners_of_treatment:
						len_of_determiner = len(determiner)
						if end + len_of_determiner < len(sentence):
							if sentence[end+1: end+len_of_determiner+1] == determiner:
								determiner_add_flag = True
								for determiner_index in range(end+1, end+len_of_determiner+1):
									if tags_in_system[determiner_index] != 'O':
										determiner_add_flag = False
										break 
								#补上程度词
								if determiner_add_flag:
									content += sentence[end: end+len_of_determiner+1]
									end += len_of_determiner + 1
									break
				elif end+1 < len(sentence) and sentence[end] == '-' and sentence[end+1] == '-':
					for determiner in determiners_of_treatment:
						len_of_determiner = len(determiner)
						if end + len_of_determiner + 1 < len(sentence):
							if sentence[end+2: end+len_of_determiner+2] == determiner:
								determiner_add_flag = True
								for determiner_index in range(end+2, end+len_of_determiner+2):
									if tags_in_system[determiner_index] != 'O':
										determiner_add_flag = False
										break 
								#补上程度词
								if determiner_add_flag:
									content += sentence[end: end+len_of_determiner+2]
									end += len_of_determiner + 2
									break				

			
			#尾字特征
			if enttype != 'social_insurance':
				enttype = changeEntType(content.encode('utf-8'), enttype, ebao_dic, texttype)
			if enttype == 'diagnosis_treatment':
				enttype = 'treatment'

			#规格实体从药物实体中分离
			if enttype in ['medicine_cn', 'medicine_pn', 'medicine_mn', 'medicine_ot', 'medicare', 'medicine']:
				specification_list = []
				for word in specifications:
					if content.endswith(word):
						specification_list.append(word)
				if specification_list:	#不为空
					specification_list.sort(cmp = lambda x,y: len(y)-len(x))
					specification = specification_list[0]
					spec_length = len(specification)
					ent1 = (content[:-spec_length], start, end-spec_length, enttype)
					if isAddableForEnt(ent1, new_entities):
						new_entities.append(ent1)
					content = specification
					start = end-spec_length
					enttype = 'specifications'
			# 包装规格去括号
			if enttype == 'packing_spe':
				if '（'.decode('utf8') in content and not content.startswith('（'.decode('utf8')):
					content = content.split('（'.decode('utf8'))[0]
					end -= (end - start) - len(content)

			# 去尾字标点符号与前后空格
			while content.startswith('\t'):
				content = content[1:]
				start += 1
			while content.endswith('\t'):
				content = content[:-1]
				end -= 1
			if content != '' and content[-1] in u'。，！？；.,!?;':
				content = content[:-1]
				end -= 1

			newent = (content, start, end, enttype)
			if isAddableForEnt(newent, new_entities):
				new_entities.append(newent)

	#print new_entities
	new_tags = ['O' for i in range(len(sentence))]
	for ent in new_entities:
		start, end, ent_type = ent[1], ent[2], ent[3]
		if end - start == 1:
			new_tags[start] = 'S-' + ent_type
		else:
			new_tags[start] = 'B-' + ent_type
			new_tags[end-1] = 'E-' + ent_type
			for i in range(start+1, end-1):
				new_tags[i] = 'I-' + ent_type
	return new_tags

#判断newent是否可以加入entities列表中, 其中ent的存储形式均为元组(content,b,e,type)
def isAddableForEnt(newent, entities):
	start, end = newent[1], newent[2]
	if newent[0].strip() == '': 
		return False
	addable = True
	for ent in entities:
		if start >= ent[2] or end <= ent[1]:
			continue
		else:
			addable = False
			break
	return addable

def twoProcessings(input_sentence, tag_seq, ebao_dic, texttype):
	tag_seq1 = specialRulesForEmr(input_sentence, tag_seq, ebao_dic, texttype)
	tag_seq2 = combineDicAndSystem(input_sentence, tag_seq1, ebao_dic, texttype)
	return tag_seq2

if __name__ == '__main__':
	sentence = u'糖尿病原发性高血压哦'
	# og_tags = ['B-disease','I-disease', 'I-disease','I-disease', 'E-disease', 'O', 'B-medicine','I-medicine', 'E-medicine','O', 'B-disease','I-disease', 'E-disease', 'O']
	# print specialRulesForEmr(sentence, og_tags)
	#dic_full = tools.loadDic(os.path.join(os.path.join(sys.path[0],'dic'), 'dic.txt'))
	#sentence = '自然显像（辅助）'.decode('utf8')
	dic = {'糖尿病':'disease', '原发性高血压':'disease', '压哦': 'disease'}
	print matchEntityInDic(sentence, dic, 'un')
	pass
