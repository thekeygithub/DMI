package com.aghit.utils;

public class ComConstants {

	public static final String SESSION_KEY = "SYSSESSIONKEY";

	public static final String FWD_JSON = "json";
	
	public static final String PAGE_LOGING_ACTION = "/login/userlogin.htm";

	public static String PAGE_LONGIN_URL = "/login.jsp";
	
	public static String PAGE_MAIN_URL = "/login/main.htm";
	
	public static String PAGE_SYSMAIN_URL = "/login/adminmain.htm";

	public static String PAGE_HOST_URL = "http://yt-server";
	
	public static int SC_SESSION_TIMEOUT = 10001;
	
	/*日期格式*/
	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-mm-dd";
	
	/*角色ID*/
	public static final String ROLE_CODE_ID = "2001";
	
	public static final String ROLE_FIRST_AUDITOR_ID = "2002";
	
	public static final String ROLE_FINAL_AUDITOR_ID = "2003";
	
	/*任务类型*/
	public static final String TASK_TYPE_RELEVANCE = "1";//任务类型:药品管理
	
	public static final String TASK_TYPE_FMT_SPLIT = "2";//任务类型:药品规格拆分
	
	public static final String TASK_TYPE_DESC_SPLIT = "3";//任务类型:药品管理
	
	/*任务步骤*/
	public static final String TASK_STEP_0 = "0";//任务步骤:初始化
	
	public static final String TASK_STEP_1 = "1";//任务步骤:编码
	
	public static final String TASK_STEP_2 = "2";//任务步骤:审核
	
	public static final String TASK_STEP_3 = "3";//任务步骤:管理员审核
	
	/*任务状态*/
	public static final String TASK_STATE_UNTREATED = "1";//任务状态:未处理
	
	public static final String TASK_STATE_TREATED = "2";//任务状态:已处理
	
	public static final String TASK_STATE_SUBMITTED = "3";//任务状态:已提交
	
	public static final String TASK_STATE_END = "4";//任务状态:已完成
	
	public static final String TASK_STATE_RELEVANCE_MORE_PK = "5";//任务状态:有关联多包装拆分
	
	public static final String TASK_STATE_UNRELEVANCE_MORE_PK = "6";//任务状态:无关联多包装拆分
	
	
	/*任务处理结论状态*/
	public static final String TASK_EVALUATE_STATE_OK = "1";//任务处理结论状态:正确
	
	public static final String TASK_EVALUATE_STATE_ERR = "2";//任务处理结论状态:错误
	
	/*任务处理评价类型*/
	/**
	 * <p>任务处理评价类型:审核评价编码</p>
	 */
	public static final String TASK_EVALUATE_TYPE_01 = "1";//任务处理评价类型:审核评价编码
	
	public static final String TASK_EVALUATE_TYPE_02 = "2";//任务处理评价类型:管理员评价编码 
	
	public static final String TASK_EVALUATE_TYPE_03 = "3";//任务处理评价类型:管理员评价审核
	
	/*多包装拆分类型*/
	public static final String MULTI_PACK_TYPE_RELEVANCE = "1";//多包装拆分类型:关联
	public static final String MULTI_PACK_TYPE_FMTSPLIT = "2";//多包装拆分类型:规格拆分
	
	/*规格拆分小数点后的位数*/
	public static final int SPLIT_DECIMAL_BIT_NUM = 6;
	
	/*多包装拆分日志操作类型*/
	public static final String MULTI_PACK_OPERATE_TYPE_SAVE = "SAVE";
	
	public static final String MULTI_PACK_OPERATE_TYPE_ADD = "ADD";
	
	public static final String MULTI_PACK_OPERATE_TYPE_UPDATE = "UPDATE";
	
	public static final String MULTI_PACK_OPERATE_TYPE_DELETE = "DELETE";
	
	public static final String MULTI_PACK_OPERATE_TYPE_CANCEL = "CANCEL";
	
	public static String getPAGE_HOST_URL() {
		return PAGE_HOST_URL;
	}

	public static void setPAGE_HOST_URL(String pAGE_HOST_URL) {
		PAGE_HOST_URL = pAGE_HOST_URL;
	}

	public static String getPAGE_LONGIN_URL() {
		return PAGE_LONGIN_URL;
	}

	public static void setPAGE_LONGIN_URL(String pAGE_LONGIN_URL) {
		PAGE_LONGIN_URL = pAGE_LONGIN_URL;
	}
}