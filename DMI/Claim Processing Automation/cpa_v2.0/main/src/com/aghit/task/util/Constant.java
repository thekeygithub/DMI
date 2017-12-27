package com.aghit.task.util;

import java.util.*;

public class Constant {
	
	public static Map<Long,String> gender=new HashMap<Long,String>();
	
	static{
		gender.put((long) 90001, "男");
		gender.put((long) 90002, "女");
		gender.put((long) 90009, "不详");
	}
	
	public final static int ENABLED = 1; // 有效
	public final static int DISABLED = 0; // 失效
	
	public final static int INDICATION = 2; //适用症
	public final static int CONTRAINDICATION = 1; //禁忌症
	

	// 规则类别
	public final static int RULE_CLASS_ALONE = 1; // 单项目同类互斥规则类型
	public final static int RULE_CLASS_LORE = 2; // 两项目知识库规则
	public final static int RULE_CLASS_HOSPITAL = 3;// 分解住院规则
	public final static int RULE_CLASS_MULTI_CONDITION = 4; // 多项目规则
	public final static int RULE_CLASS_KL=5; //知识库规则

	// 就医类型
	public final static int MEDICAL_TYPE_OUTPATIENT = 1; // 门诊
	public final static int MEDICAL_TYPE_SPECIAL = 2;// 门特
	public final static int MEDICAL_TYPE_HOSPITAL = 3;// 住院

	// 审核项目
	public final static int CHECK_PROJECT_DRUG = 1; // 药品
	public final static int CHECK_PROJECT_DIAGNOSIS = 2; // 诊断
	public final static int CHECK_PROJECT_TREAT = 4; // 诊疗
	public final static int CHECK_PROJECT_CONSUM = 5; // 耗材

	// 药品分类标准
	public static final int CHECK_PROJECT_DRUG_ATC = 1;//ATC
	public static final int CHECK_PROJECT_DRUG_YBYJH = 2;//医保研究会
	public static final int CHECK_PROJECT_DRUG_GENERIC_NAME = 3;//药物通用名
	public static final int CHECK_PROJECT_DRUG_MEDICINE_NAME = 4;//药品名称
	public static final int CHECK_PROJECT_DIAG_ICD_10 = 10;//诊断分类
//	public static final int CHECK_PROJECT_OP_ICD_9 = 9;//手术分类

	// 关系类型
	public final static int NEXUS_TYPE_BLACKBALL = 1;// 互斥
	public final static int NEXUS_TYPE_ATTRACT = 2; // 相符

	// 关系属性
	public final static int NEXUS_ATTRIBUTE_KNOWLEDGE = 1;// 常识
	public final static int NEXUS_ATTRIBUTE_MEDICINE = 2;// 医学
	public final static int NEXUS_ATTRIBUTE_LAW = 3;// 法规
	public final static int NEXUS_ATTRIBUTE_SOCIAL_SECURITY = 4;// 社保

	// 条件项目
	public final static int CONDITION_PROJECT_DRUG = 1; // 药品
	public final static int CONDITION_PROJECT_DIAGNOSIS = 2;// 诊断
	public final static int CONDITION_PROJECT_TREAT = 4;// 诊疗
	public final static int CONDITION_PROJECT_CONSUM = 5;// 耗材
	public final static int CONDITION_PROJECT_HOSPITAL = 6; // 医院
	public final static int CONDITION_PROJECT_AGE = 7; // 年龄
	public final static int CONDITION_PROJECT_SEX = 8;// 性别
	public final static int CONDITION_PROJECT_PAY_TYPE = 9;// 支付类别
	public final static int CONDITION_PROJECT_DOCTOR_TITLE = 10; // 医师职称
	public final static int CONDITION_PROJECT_TIME_REPEAL = 11; // 时间重复性
	public final static int CONDITION_PROJECT_OVERDOSAGE = 12; // 超量审核
//	public final static int CONDITION_PROJECT_DRUG_RULE = 13; // 用药规则性检查
	public final static int CONDITION_PROJECT_OVERFREQ = 14; // 高频审核
	public final static int CONDITION_PROJECT_HOSP_RANK = 15; // 医院等级
	public final static int CONDITION_PROJECT_INDICATION = 16; //适用症
	public final static int CONDITION_PROJECT_CONTRAINDICATION = 17; //禁忌症
	public final static int CONDITION_PROJECT_DDD = 18; //日剂量（DDD值，defined daily dose）

	// 时间重复性审核时，日剂量计算方式
	public final static int DAY_DOSE_CALCWAY_PC = 1; // 频次单位最大剂量
	public final static int DAY_DOSE_CALCWAY_XD = 2; // 协定日剂量
	public final static int DAY_DOSE_CALCWAY_YB = 3; // 医保日剂量

	// 知识分类
	// public final static int KL_DOUBLEITEM_TYPE = 1; // 两项目知识类型
	// public final static int KL_PROPERUSAGE_TYPE = 2; // 药品合理用量知识类型
	// public final static int KL_PROJECT_DRUG_TYPE = 3; // 用药规则性检查
	// public final static int KL_HOSPITAL_TYPE = 4; // 医院
	// public final static int KL_AGE_TYPE = 5; // 年龄
	// public final static int KL_SEX_TYPE = 6; // 性别
	// public final static int KL_PAY_TYPE = 7; // 支付类别
	// public final static int KL_DOCTOR_TITLE_TYPE = 8; // 医师职称

	// 标点符号替换
	public final static String REPLACE_SYMBOL_COMMA = "@!="; // 逗号用 @!= 替换
	public final static String REPLACE_SYMBOL_NEWLINE = "!@=";// 换行号 换行符用 !@= 替换
	
	//任务运行状态，0 待运行，1 运行中，2 正常运行完成， 3 运行失败（异常或其他原因）
	public static final int SUBTASK_STATE_WILL_OPERATE = 0;//待运行
	public static final int SUBTASK_STATE_OPERATING = 1;//运行中
	public static final int SUBTASK_STATE_OPERATE_OK = 2;//正常运行完成
	public static final int SUBTASK_STATE_OPERATE_FAIL = 3;//运行失败
	
	//医嘱类型编号
//	public static final String ORD_TYPE_ID_TREAT = "84104";//诊疗
//	public static final String ORD_TYPE_ID_CONSUM = "84105";//耗材
//	public static final String ORD_TYPE_ID_OTHER = "84106";//收费项目分类干预
	
	public static final String ORD_TYPE_ID_TREAT = "51002";//诊疗
	public static final String ORD_TYPE_ID_CONSUM = "51004";//耗材
	public static final String ORD_TYPE_ID_OTHER = "51009";//收费项目分类干预
	

	// 中间表数据类型
	public static final String MID_DATA_TYPE_PRE = "1";	//处方
	public static final String MID_DATA_TYPE_CLM = "2";	//结算单
	public static final String MID_DATA_TYPE_ORD = "3";	//医嘱
	

	/**
	 * 医院申诉和医保复审违规数据状态
	 */
	public static final String APPEAL_STATE_FOUL = "01";//违规
	
	public static final String APPEAL_STATE_ISSUED = "02";//下发
	
	public static final String APPEAL_STATE_HOLD = "03";//暂存
	
	public static final String APPEAL_STATE_SUBMITED = "04";//提交
	
	public static final String APPEAL_STATE_REPLY = "05";//批复
	
	public static final String APPEAL_STATE_ACCEPTION = "06";//接受
	
	public static final String APPEAL_STATE_TERMINATION = "07";//终止
	
	public static final String CPA_SYS_ID = "2";// CPA合理性引擎系统ID
	
	//规则运行模型 0：根据项目匹配；1：根据药品分类进行匹配
	public static final String RUN_MODEL_ITEM = "0"; //项目
	public static final String RUN_MODEL_CAT = "1";  //分类		
	
	public static final String DRUG_CATE_ATC_WEST = "24122";	// 医保研究会西药ATC
	public static final String DRUG_CATE_ATC_CHINA = "24121";	// 医保研究会中药ATC
	public static final String DRUG_CHINA = "10406";			// 中药药品
	
	public static final long DRUG_TYPE_ATC_WEST = 25104;   //西药
	public static final long DRUG_TYPE_ATC_CHINA = 25103;  //中成药	

	//知识-元素类型
	public static final int ELEM_TYPE_AGE = 0;            //年龄
	public static final int ELEM_TYPE_DRUG_CATE = 1;      //药品分类树
	public static final int ELEM_TYPE_DRUG = 2;           //药品
	public static final int ELEM_TYPE_DIAG = 3;           //诊断
	public static final int ELEM_TYPE_TREAT = 4;          //诊疗
	public static final int ELEM_TYPE_CONSUM_CATE = 5;    //耗材分类树
	public static final int ELEM_TYPE_CONSUM = 6;         //耗材
	public static final int ELEM_TYPE_MI_DRUG = 101;      //医保药品目录
	public static final int ELEM_TYPE_MI_TREAT = 102;     //医保诊疗目录
	public static final int ELEM_TYPE_MI_CONSUM = 104;    //医保耗材目录
	public static final int ELEM_TYPE_FROMTYPE = 50;      //医保耗材目录
	public static final int ELEM_TYPE_GENDER = 51;        //医保耗材目录
	public static final int ELEM_TYPE_TOTAL_FEE= 204;     //结算单总费用
	public static final int ELEM_TYPE_HLOS= 205;          //住院天数
	public static final int ELEM_TYPE_HOSP_TYPE= 203;     //医院类型 中医 专科等
	public static final int ELEM_TYPE_DR_LVL_DETAIL= 113; //明细医师级别
	public static final int ELEM_TYPE_MI_FLAG= 111;       //医保标识
	public static final int ELEM_TYPE_ITEM_PRICE= 112;    //单价
	public static final int ELEM_TYPE_SETTLEMENT_TYPE= 206;//结算方式
	public static final int ELEM_TYPE_P_MI_CAT= 207;       //参保人员类别
	public static final int ELME_TYPE_ITEM_FEE = 301;      //同类项目总金额
	public static final int ELEM_TYPE_ITEM_COUNT = 302;    //同类项目总数量
	public static final int ELEM_TYPE_ITEM_INDAYS = 311;   //同类项目总数量减住院天数
	

	public static final int ELEM_TYPE_HOSP_RANK = 201;    //医院等级
	public static final int ELEM_TYPE_PAY_TYPE = 202;    //支付类别

	//元素定义 条件符号
	public static final String CON_CODE_EQ = "EQ";  //等于
	public static final String CON_CODE_LE = "LE";  //小于等于
	public static final String CON_CODE_GE = "GE";  //大于等于
	public static final String CON_CODE_LT = "LT";  //小于
	public static final String CON_CODE_GT = "GT";  //大于
	public static final String CON_CODE_IN = "IN";  //存在
	public static final String CON_CODE_EX = "EX";  //排除
	public static final String CON_CODE_RG = "RG";  //区间
	public static final String CON_CODE_NX = "NX";  //不存在
	public static final String CON_CODE_OI = "OI";  //有一个不存在就满足违规
	//条件表达式关系
	public static final int COND_STAT_REL_ADD = 1;   //与
	public static final int COND_STAT_REL_OR = 2;    //或
	public static final int COND_STAT_REL_OTHER = 9; //其它
	
	//停止状态
	public final static int STOP_ENABLED = 0; // 有效
	public final static int STOP_DISABLED = 1; // 失效
	
	public final static int STD_FLAG_UNCONFIRMED = -1; //-1自动拆分未确认 0未标准化 1完全标准化 2部分标准化
	public final static int STD_FLAG_NOT = 0; // 0未标准化
	public final static int STD_FLAG_COMPLETE = 1; // 1完全标准化
	public final static int STD_FLAG_PART = 2; //2部分标准化
	
	//条件项目是否包含审核项目
	public final static int EX_OBJ_FLAG_YES = 1; //排除
	public final static int EX_OBJ_FLAG_NO = 0; //不排除
	
	//条件项目范围判断
	public final static int RANGE_FLAG_CLM = 0; //结算单级别
	public final static int RANGE_FLAG_PRE = 1;  //处方级别
	public final static int RANGE_FLAG_SEQ = 2;  //药品组
	
	//条件项目为空时是否参与审核
	public final static int NULL_FLAG_YES = 1; //审核
	public final static int NULL_FLAG_NO = 0;  //不审核
	
    //元素类型
	public final static int DATA_TYPE_INT = 0;
	public final static int DATA_TYPE_FLOAT= 1;
	public final static int DATA_TYPE_STRING = 2;
	public final static int DATA_TYPE_DATE = 3;
	
	//执行类型
	public final static String EXE_TYPE_BATCH_TIMING = "1";
	public final static String EXE_TYPE_BATCH_MANUAL = "2";
	public final static String EXE_TYPE_REALTIME = "3";
	
	//是否参与审核
	public final static int CHK_FLAG_YES = 1; //参与审核
	public final static int CHK_FLAG_NO = 0;  //不参与审核
 	
}
