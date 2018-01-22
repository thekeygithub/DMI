package com.pay.cloud.pay.escrow.mi.pangu.utils;
/**
 * 盘古相关常量类
 * @author yanjie.ji
 * @date 2016年11月24日 
 * @time 上午10:19:07
 */
public class PGConstants {
	
	//盘古接口指令
	public static final int CMD_1001 = 1001;
	public static final int CMD_1002 = 1002;
	public static final int CMD_1003 = 1003;
	public static final int CMD_1004 = 1004;
	public static final int CMD_1005 = 1005;
	public static final int CMD_1006 = 1006;
	public static final int CMD_1007 = 1007;
	public static final int CMD_1008 = 1008;
	public static final int CMD_1009 = 1009;
	public static final int CMD_1010 = 1010;
	public static final int CMD_1011 = 1011;
	public static final int CMD_1012 = 1012;
	public static final int CMD_1013 = 1013;
	public static final int CMD_1014 = 1014;
	public static final int CMD_1015 = 1015;
	public static final int CMD_1016 = 1016;
	public static final int CMD_2001 = 2001;
	public static final int CMD_2002 = 2002;
	public static final int CMD_2003 = 2003;
	public static final int CMD_2004 = 2004;
	public static final int CMD_2005 = 2005;
	public static final int CMD_3001 = 3001;
	public static final int CMD_3002 = 3002;
	public static final int CMD_3003 = 3003;
	public static final int CMD_3004 = 3004;
	public static final int CMD_3005 = 3005;
	
	//返回结果编码  0失败;1成功
	public static final int RESP_CODE_SUCCESS = 1;
	public static final int RESP_CODE_FAIL = 0;
	
	public static final String PANGU_SERVICE_URL="pangu_service_url";
	public static final String PARAM_CHANNEL="channel";
	public static final String PARAM_CMD="cmd";
	public static final String PARAM_RES="req";
	
	//性别
		public static final int SEX_MALE=1;
		public static final int SEX_FEMALE=2;
		public static final int SEX_UNKNOWN=9;

		//民族
		public static final String[] NATION_CODE
		=new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58"};
		public static final String[] NATION_NAME
		= new String[]{"汉族","蒙古族","回族","藏族","维吾尔族","苗族","彝族","壮族","布依族","朝鲜族","满族","侗族","瑶族","白族","土家族","哈尼族","哈萨克族","傣族","黎族","傈傈族","佤族","畲族","高山族","拉祜族","水族","东乡族","纳西族","景颇族","柯尔克孜族","土族","达翰尔族","仫佬族","羌族","布朗族","撒拉族","毛南族","仡佬族","锡伯族","阿昌族","普米族","塔吉克族","怒族","乌孜别克族","俄罗斯族","鄂温克族","德昂族","保安族","裕固族","京族","塔塔尔族","独龙族","鄂伦春族","赫哲族","门巴族","珞巴族","基诺族","其他","不详"	};

		//医疗人员类别
		
		//计生手术类别
		
		//医保账户使用标示
		
		//收费类别
		
		//医疗类别
		//账户使用标示
		//军转人员标示
		//病种种类（疾病分类）
		//特殊病种标示（病种类别）
		//病种报销标示
		//处方药标识
		//报销类别（收费项目等级）
		//自制药品标识
		//药品种类
		//药是否需要审批
		//病床等级
}
