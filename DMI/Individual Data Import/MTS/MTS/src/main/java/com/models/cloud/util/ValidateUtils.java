package com.models.cloud.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.util.hint.Hint;



public class ValidateUtils {
	
	/**
	 * 
	 * @Description: Object字符串验证
	 * @Title: isBlank 
	 * @param value
	 * @return boolean
	 */
	public static boolean isBlank(Object value){
		if(value==null) return false;
		if(!StringUtils.isBlank(value.toString())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 手机号验证
	 * @Title: isPhoneNumber 
	 * @param value
	 * @return boolean
	 */
	public static boolean isPhoneNumber(Object value){
		if(value==null) return false;
		if(StringUtils.isBlank(value.toString())) return false;
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		Matcher m = p.matcher(value.toString());
		return m.matches();
	}

	/**
	 * 验证是否为中文
	 * @param value
	 * @return
     */
	public static boolean isChinese(String value){
		if(isEmpty(value)){
			return false;
		}
		Pattern p = Pattern.compile("^([\\u4e00-\\u9fa5]{1,20})$");
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	public static boolean checkChineseName(String value){
		if(isEmpty(value)){
			return false;
		}
		if(value.length()>25){
			return false;
		}
		String reg = "^([\\u4e00-\\u9fa5.·•▪•]{2,25})$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(value);
		return m.matches();
	}
    
  public static void main(String[] args) {}

	/**
	 * 
	 * @Description: 身份证号验证
	 * @Title: isIdCard 
	 * @param value
	 * @return boolean
	 */
//	public static boolean isIdCard(String value){
//		if(StringUtils.isBlank(value)) return false;
//		//Pattern p = Pattern.compile("(/^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$/)");
//		Pattern p1 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");//15位
//		Pattern p2 = Pattern.compile("/^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$/");//18位
//		Matcher m1 = p1.matcher(value);
//		Matcher m2 = p2.matcher(value);
//		return m1.matches() || m2.matches();
//	}
	final static Map<Integer, String> zoneNum = new HashMap<Integer, String>();  
    static {  
        zoneNum.put(11, "北京");  
        zoneNum.put(12, "天津");  
        zoneNum.put(13, "河北");  
        zoneNum.put(14, "山西");  
        zoneNum.put(15, "内蒙古");  
        zoneNum.put(21, "辽宁");  
        zoneNum.put(22, "吉林");  
        zoneNum.put(23, "黑龙江");  
        zoneNum.put(31, "上海");  
        zoneNum.put(32, "江苏");  
        zoneNum.put(33, "浙江");  
        zoneNum.put(34, "安徽");  
        zoneNum.put(35, "福建");  
        zoneNum.put(36, "江西");  
        zoneNum.put(37, "山东");  
        zoneNum.put(41, "河南");  
        zoneNum.put(42, "湖北");  
        zoneNum.put(43, "湖南");  
        zoneNum.put(44, "广东");  
        zoneNum.put(45, "广西");  
        zoneNum.put(46, "海南");  
        zoneNum.put(50, "重庆");  
        zoneNum.put(51, "四川");  
        zoneNum.put(52, "贵州");  
        zoneNum.put(53, "云南");  
        zoneNum.put(54, "西藏");  
        zoneNum.put(61, "陕西");  
        zoneNum.put(62, "甘肃");  
        zoneNum.put(63, "青海");  
        zoneNum.put(64, "新疆");  
        zoneNum.put(71, "台湾");  
        zoneNum.put(81, "香港");  
        zoneNum.put(82, "澳门");  
        zoneNum.put(91, "外国");  
    }  
      
    final static int[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};  
    final static int[] POWER_LIST = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10,   
        5, 8, 4, 2};  
      
    /** 
     *  
     * 身份证验证 
     *  
     *@param s  
     *      号码内容 
     *@return 是否有效 null和"" 都是false  
     */  
    public static boolean isIdCard(String s){  
        if(s == null || (s.length() != 15 && s.length() != 18))  
            return false;  
        final char[] cs = s.toUpperCase().toCharArray();  
        //校验位数  
        int power = 0;  
        for(int i=0; i<cs.length; i++){  
            if(i==cs.length-1 && cs[i] == 'X')  
                break;//最后一位可以 是X或x  
            if(cs[i]<'0' || cs[i]>'9')  
                return false;  
            if(i < cs.length -1){  
                power += (cs[i] - '0') * POWER_LIST[i];  
            }  
        }  
          
        //校验区位码  
        if(!zoneNum.containsKey(Integer.valueOf(s.substring(0,2)))){  
            return false;  
        }  
          
        //校验年份  
        String year = s.length() == 15 ? getIdcardCalendar() + s.substring(6,8) :s.substring(6, 10);  
          
        final int iyear = Integer.parseInt(year);  
        if(iyear < 1900 || iyear > Calendar.getInstance().get(Calendar.YEAR))  
            return false;//1900年的PASS，超过今年的PASS  
          
        //校验月份  
        String month = s.length() == 15 ? s.substring(8, 10) : s.substring(10,12);  
        final int imonth = Integer.parseInt(month);  
        if(imonth <1 || imonth >12){  
            return false;  
        }  
          
        //校验天数        
        String day = s.length() ==15 ? s.substring(10, 12) : s.substring(12, 14);  
        final int iday = Integer.parseInt(day);  
        if(iday < 1 || iday > 31)  
            return false;  
          
        //校验一个合法的年月日:已经得到校验了  
        /*if(!validate(iyear, imonth, iday)) 
            return false;*/  
          
        //校验"校验码"  
        if(s.length() == 15)  
            return true;  
        return cs[cs.length -1 ] == PARITYBIT[power % 11];  
    }
      
    private static int getIdcardCalendar() {          
         GregorianCalendar curDay = new GregorianCalendar();  
         int curYear = curDay.get(Calendar.YEAR);  
         int year2bit = Integer.parseInt(String.valueOf(curYear).substring(2));            
         return  year2bit;  
    }       
      
    @Deprecated  
    static boolean validate(int year, int imonth, int iday){  
        //比如考虑闰月，大小月等  
        return true;  
    }  

	/**
	 * 
	 * @Description: 邮箱验证
	 * @Title: isEmail 
	 * @param value
	 * @return boolean
	 */
	public static boolean isEmail(String value){
		if(StringUtils.isBlank(value)) return false;
		Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,5}$");
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	/**
	 * 
	 * @Description: 正整数验证
	 * @Title: isNumber 
	 * @param value
	 * @return boolean
	 */
	public static boolean isNumber(Object value){
		if(value==null) return false;
		if(StringUtils.isBlank(value.toString())) return false;
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(value.toString());
		return m.matches();
	}
	
    /**
     * 
     * @Description: 字符串长度验证（大于等于最小长度并且小于等于最大长度，返回true）
     * @Title: checkLength 
     * @param value 字符串
     * @param minlength 最小长度
     * @param maxlength 最大长度
     * @return boolean
     */
	public static boolean checkLength(String value, int minlength, int maxlength){	
		if(StringUtils.isBlank(value)) return false;
		int length =  StringUtils.isBlank(value)? 0:value.length();	
		boolean flg = false;
		if(length >= minlength && length <= maxlength){
			flg = true;
		}
		return  flg;
	}
	
	/**
	 * 
	 * @Description: 自定义验证
	 * @Title: isRegex 
	 * @param value 值
	 * @param regex 正则表达式
	 * @return boolean
	 */
	public static boolean isRegex(String value, String regex){
		if(StringUtils.isBlank(value)||StringUtils.isBlank(regex)) return false;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.matches();
	}
	
	/**
	 * 
	 * @Description: 判断Object对象非NULL并且转字符串后值等于某个字符串
	 * @Title: isEmpty 
	 * @param str
	 * @param value
	 * @return Boolean
	 */
	public static Boolean isEmpty(Object str,String value){
		if(null!=str&&str.toString().trim().equals(value)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 判断Object数组对象非NULL并且数组Length大于零
	 * @Title: isEmpty 
	 * @param strArray
	 * @return Boolean
	 */
	public static Boolean isEmpty(Object [] strArray){
		if(null!=strArray&&strArray.length>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @Description: 判断MAP含有某个key并且key所属值非NULL并且转字符串后非空
	 * @Title: isMapEmpty 
	 * @param map
	 * @param key
	 * @return Boolean
	 */
	public static Boolean isMapEmpty(Map<String,String> map,String key){
		if(map!=null&&map.containsKey(key)&&map.get(key)!=null&&!map.get(key).toString().trim().equals("")){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @Description: 判断MAP含有某个key并且key所属值非NULL并且转字符串值等于某个字符串
	 * @Title: isMapEmpty 
	 * @param map
	 * @param key
	 * @param value
	 * @return Boolean
	 */
	public static Boolean isMapEmpty(Map<Object,Object> map,Object key,String value){
		if(map!=null&&map.containsKey(key)&&map.get(key)!=null&&map.get(key).toString().trim().equals(value)){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @Description: 判断List非NULL并且SIZE大于零
	 * @Title: isEmpty 
	 * @param li
	 * @return Boolean
	 */
	public static Boolean isEmpty(List<Object> li){
		if(li!=null&&li.size()>0){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * @Description: 判断文件夹是否存在,如果不存在则创建文件夹
	 * @Title: isFile 
	 * @param path 
	 * @return void
	 */
	public static void isFile(String path)
	{
		 File file = new File(path); 
		 if (!file.exists()) 
		 {   
			 file.mkdir();  
		 }
	}
	
	public static boolean isEmpty(String value){
        if(null == value){
            return true;
        }
        value = String.valueOf(value).trim();
        if("".equals(value) || "null".equals(value.toLowerCase()) || "ｎｕｌｌ".equals(value.toLowerCase())){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String value){
        return !isEmpty(value);
    }

    /**
     * 是否正整数
     * @param value
     * @return
     */
    public static boolean isPositiveInteger(String value) {
        if (isEmpty(value)) {
            return false;
        }
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

	public static boolean isFloatNumber(String value) {
		if(isEmpty(value)){
			return false;
		}
		Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
		Matcher m = p.matcher(value);
		return m.matches();
	}

    /**
     * 是否合法金额
     * @param value
     * @param isContainZero
     * @return
     */
    public static boolean isMoney(String value, boolean isContainZero) {
        if (isEmpty(value)) {
            return false;
        }
		Pattern p = Pattern.compile("(^[+]?[1-9]\\d*(\\.\\d{1,13})?$)|(^[+]?[0]{1}(\\.\\d{1,13})?$)");
        Matcher m = p.matcher(value);
		boolean checkResult = m.matches();
        if(isContainZero){
            return checkResult;
        }
        if(checkResult){
            if(Double.parseDouble(value) <= 0){
                return false;
            }
        }
        return checkResult;
    }
    
    /**
     * 
     * @Description:判断空
     * @Title: isEmpty 
     * @param receiveMap
     * @param name
     * @return boolean
     */
	public static boolean isEmpty(Map<String, Object> receiveMap, String name) {
		Object operId = receiveMap.get(name);
		if (operId == null || StringUtils.isEmpty(operId.toString())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Description: 判断日期
	 * @Title: isDate 
	 * @param date 日期字符串
	 * @param pattern 日期格式
	 * @return boolean
	 */
	public static boolean isDate(String date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		try {
			sdf.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否合法的ip地址
	 * @param str
	 * @return
	 */
	public static boolean checkIP(String str) {
		if(str!=null && !"".equals(str)){
			Pattern pattern = Pattern
					.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
							+ "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
			return pattern.matcher(str).matches();
		}
		return false;
    }

	/**
	 * 判断空
	 * @param receiveMap
	 * @param name
	 * @return
	 */
	public static boolean isBlank(Map<String, Object> receiveMap, String name) {
		Object operId = receiveMap.get(name);
		if (operId == null || StringUtils.isBlank(operId.toString())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否包含汉字
	 * @param str
	 * @return
	 */
	public static boolean isContainsChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find()) {
			flg = true;
		}
		return flg;
	}
	
	/**
	 * 参数校验
	 * @param receiveMap
	 * @param name
	 * @param type
	 * @param allowNull
	 * @param length
	 * @param range
	 * @param resultMap
	 * @return
	 */
	public static boolean checkParam(Map<String, Object> receiveMap,String name,
			String type,boolean allowNull,int length,char[] range,String regex,Map<String, Object> resultMap) {
		//判断是否可为空
		if (false == allowNull){
			if(ValidateUtils.isBlank(receiveMap, name) ){
				resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", name));
                return false;
			}
		}
		String value = "";
		if (receiveMap.get(name) == null || receiveMap.get(name).toString().trim().length() == 0) {
			return true;
		}else{
			value = receiveMap.get(name).toString().trim();
		}
		boolean flag = true;
		if ("string".equals(type)){
			if(StringUtils.isBlank(regex)){
				if(false == ValidateUtils.isRegex(value,"[A-Za-z0-9_]+") || false == ValidateUtils.checkLength(receiveMap.get(name).toString().trim(), 0, length)){
					flag = false;
				}
			}else{
				if(false == ValidateUtils.isRegex(value,regex) || false == ValidateUtils.checkLength(receiveMap.get(name).toString().trim(), 0, length)){
					flag = false;
				}
			}
			
		}else if("char".equals(type)){
			for (int i = 0; i < range.length; i++) {
				if(value.equals(String.valueOf(range[i]))){
					return true;
				}
			}
			flag = false;
		}else if("money".equals(type)){
			if(!ValidateUtils.isMoney(value, false) || false == ValidateUtils.checkLength(value, 0, length)){
				flag = false;
			}
		}else if("integer".equals(type)){
			if(false == ValidateUtils.isNumber(value) || false == ValidateUtils.checkLength(value, 0, length)){
				flag = false;
			}
		}else if("date".equals(type)){
			if(false == ValidateUtils.isDate(receiveMap.get(name).toString().trim(),regex)){
				flag = false;
			}
		}else if(TYPE_CHINESE.equals(type)){
			if(false == ValidateUtils.isRegex(value,"[\u4e00-\u9fa5\\w]+") || false == ValidateUtils.checkLength(receiveMap.get(name).toString().trim(), 0, length)){
				flag = false;
			}
		}
		if(false == flag){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", name));
		}
		return flag;
	}
	
	/** String 类型*/
	public static final String TYPE_STRING = "string";
	/** char 类型*/
	public static final String TYPE_CHAR = "char";
	/** money 类型*/
	public static final String TYPE_MONEY = "money";
	/** integer 类型*/
	public static final String TYPE_INTEGER = "integer";
	/**date*/
	public static final String TYPE_DATE = "date";
	/**包含汉字字符串*/
	public static final String TYPE_CHINESE  = "Chinese ";
}
