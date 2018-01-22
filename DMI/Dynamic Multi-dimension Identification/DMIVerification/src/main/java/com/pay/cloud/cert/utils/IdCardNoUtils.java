package com.pay.cloud.cert.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.pay.cloud.constants.PropertiesConstant;
import com.pay.cloud.util.DateUtils;
import com.pay.cloud.util.DimDictEnum;
import com.pay.cloud.util.hint.Propertie;

/**
 * 身份证工具类
 * @author yanjie.ji
 * @date 2016年7月22日 
 * @time 下午4:41:29
 */
public class IdCardNoUtils {
	/**
	 * 校验身份证合法性
	 * @param idNo
	 * @return
	 */
	public static boolean isIdCard(String idNo){
		//idNo为空
		if(idNo==null) return false;
		idNo = idNo.trim();
		int length = idNo.length();
		//长度不是15或18
		if(length!=15&&length!=18){
			return false;
		}
		//归属地判断
		String homeOwerNo = idNo.substring(0, 6);
		//归属地有新旧版本区分，无法收集完善，不进行归属地判断
//		if(StringUtils.isEmpty(Propertie.IDCARD.value(homeOwerNo)))
//			return false;
		String birth_day = null;
		int birth_year = 0;
		if(length==18){
			//除最后一位其他均应为数字
			if(!NumberUtils.isNumber(idNo.substring(0,length-1)))
				return false;
			birth_day= idNo.substring(6,14);
			birth_year = Integer.parseInt(idNo.substring(6,10));
		}else{
			//应全部为数字
			if(!NumberUtils.isNumber(idNo))
				return false;
			birth_day = "19"+idNo.substring(6,12);
			birth_year = Integer.parseInt("19"+idNo.substring(6,8));
		}
		//判断出生年月日是否是有效的年月日
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date birth = null;
		try {
			birth = df.parse(birth_day);
			//日期字符串转化为时间后再转化为字符串，如果初始字符串与最终字符串相同则说明是有效的日期
			if(!df.format(birth).equals(birth_day)) return false;
		} catch (ParseException e) {
			return false;
		}
		
		//判断出生年是否在正常范围内  默认正常范围是1900 -- 当前年
		int birth_start = 1900,birth_end=Calendar.getInstance().get(Calendar.YEAR);
		String year_start = Propertie.IDCARD.value(PropertiesConstant.ID_CARD_NO_BIRTH_START);
		String year_end = Propertie.IDCARD.value(PropertiesConstant.ID_CARD_NO_BIRTH_END);
		if(StringUtils.isNotEmpty(year_start)
				&&NumberUtils.isNumber(year_start)
				&&year_start.length()==4)
			birth_start=Integer.parseInt(year_start);
		if(StringUtils.isNotEmpty(year_end)
				&&NumberUtils.isNumber(year_end)
				&&year_start.length()==4)
			birth_end=Integer.parseInt(year_end);
		
		if(birth_year<birth_start||birth_year>birth_end)
			return false;
		
		if(length==18&&!check18(idNo)){
			return false;
		}
		return true;
	}
	/**
	 * 对18位身份证最后一位进行校验
	 * @param idNo
	 * @return
	 */
	private static boolean check18(String idNo){
		//十七位数字本体码权重
		String[] valCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4","3", "2" };
		//对应校验码字符值
        String[] wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7","9", "10", "5", "8", "4", "2" };
        String str17 = idNo.substring(0,17);
        int total = 0;
        //加权求和
        for (int i = 0; i < 17; i++) {
            total = total + Integer.parseInt(String.valueOf(str17.charAt(i)))*Integer.parseInt(wi[i]);
        }
        //取模
        int modValue = total % 11;
        //得到校验码
        String strVerifyCode = valCodeArr[modValue];
        //如果校验码匹配则为有效身份证
        if(strVerifyCode.equals(idNo.substring(17, 18).toLowerCase())) return true;
        else return false;
	}

	/**
	 * 根据身份证号获取性别
	 * @param idNum 身份证号
	 * @return 1-男 0-女
	 * @throws Exception
     */
	public static short getUserSexByIdNum(String idNum) throws Exception {
		if(!isIdCard(idNum)) {
			return -1;
		}
		idNum = idNum.trim();
		int sexFlag;
		if(idNum.length() == 18){
			sexFlag = Integer.parseInt(idNum.substring(16, 17));
		}else {
			sexFlag = Integer.parseInt(idNum.substring(14));
		}
		if(sexFlag % 2 == 0){
			return Short.parseShort(DimDictEnum.P_GEND_ID_FEMALE.getCodeString());
		}else {
			return Short.parseShort(DimDictEnum.P_GEND_ID_MALE.getCodeString());
		}
	}

	/**
	 * 根据身份证号获取生日
	 * @param idNum 身份证号
	 * @return 生日，格式：yyyy-MM-dd
	 * @throws Exception
     */
	public static Date getUserBirthByIdNum(String idNum) throws Exception {
		if(!isIdCard(idNum)) {
			return null;
		}
		String birth;
		String year;
		String month;
		String day;
		idNum = idNum.trim();
		if(idNum.length() == 18){
			year = idNum.substring(6, 10);
			month = idNum.substring(10, 12);
			day = idNum.substring(12, 14);
		}else {
			year = "19".concat(idNum.substring(6, 8));
			month = idNum.substring(8, 10);
			day = idNum.substring(10, 12);
		}
		birth = year.concat("-").concat(month).concat("-").concat(day);
		return DateUtils.strToDate(birth, "yyyy-MM-dd");
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(getUserBirthByIdNum("110105710923582"));
	}
}
