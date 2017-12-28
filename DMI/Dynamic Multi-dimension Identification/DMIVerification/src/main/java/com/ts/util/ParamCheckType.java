package com.ts.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public enum ParamCheckType {
	T1Maxlength		(	"maxlength", 		"参数长度限制",	"长度校验失败"),
	T2Notnull		(	"notnull", 			"参数非空限制",	"不允许为空"),
	T3Paramtype		(	"paramtype", 		"参数类型限制",	"类型错误"),
	T4Fixedvalue	(	"fixedvalue", 		"参数固定值限制",	"未知参数值"),
	T5DateFormat	(	"dateformat", 		"日期格式限制",	"日期格式错误"),
	;
	private String checkType;
	private String desc;
	private String failMsg;
	private ParamCheckType(String checkType, String desc, String failMsg){
		this.checkType = checkType;
		this.desc = desc;
		this.failMsg = failMsg;
	}
	public static Map<String, ParamCheckType> map = new HashMap<String, ParamCheckType>();
	static{
		for (ParamCheckType paramCheckType : ParamCheckType.values()) {  
			map.put(paramCheckType.getCheckType(), paramCheckType);
		}  
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getFailMsg() {
		return failMsg;
	}
	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}
	/**
	 * @param thisCheckType		检查规则类型
	 * @param thisCheckValue	检查规则值
	 * @param paramValue 		要检查的参数值
	 * @return
	 */
	public static boolean validateOneParam(ParamCheckType thisCheckType, String thisCheckValue, Object paramValue){
		switch (thisCheckType) {
		case T1Maxlength:
			String tmpValue = String.valueOf(paramValue);
			int tmpLength = Integer.valueOf(thisCheckValue);
			if(tmpValue.length() > tmpLength){
				return false;
			}
			break;
		case T2Notnull:
			if("1".equals(thisCheckValue)){//非空
				if(paramValue == null){
					return false;
				}
				String tmpValue1 = String.valueOf(paramValue);
				if(tmpValue1.length()<=0 || "null".equals(tmpValue1)){
					return false;
				}
			}
			break;
		case T3Paramtype:
			if(paramValue != null){
				String tmpParamValue = paramValue.getClass().getName();
				tmpParamValue = tmpParamValue.substring(tmpParamValue.lastIndexOf(".")+1).toUpperCase();
				if(!tmpParamValue.equals(thisCheckValue.toUpperCase()) && !"JSONNULL".equals(tmpParamValue) ){
					if("STRING".equals(thisCheckValue.toUpperCase())){
						break;
					}
					//这里有种情况  double long 为整数时  在字符串种和int相同
					if("INTEGER".equals(tmpParamValue) && "DOUBLE||LONG".contains(thisCheckValue.toUpperCase())){
						break;
					}
					//有些参数会以字符串类型传值  校验下内容
					if("STRING".equals(tmpParamValue)){
						try{
							String valueStr= String.valueOf(paramValue);
							if("DOUBLE".equals(thisCheckValue.toUpperCase())){
								Double tmp = Double.valueOf(valueStr);
							}
							if("INTEGER".equals(thisCheckValue.toUpperCase())){
								Integer tmp = Integer.valueOf(valueStr);
							}
							if("LONG".equals(thisCheckValue.toUpperCase())){
								Long tmp = Long.valueOf(valueStr);
							}
						}catch (Exception e) {
							//类型转换异常  校验失败
							return false;
						}
					}
				}
			}
			break;
		case T4Fixedvalue:
			if(paramValue == null || "".equals(paramValue)){
				break;
			}
			String[] valueArr = thisCheckValue.split(";");
			boolean flagFixed = false;
			for (String string : valueArr) {
				if(string.equals(String.valueOf(paramValue))){
					flagFixed = true;
				}
			}
			if(!flagFixed){
				return false;
			}
			break;
		case T5DateFormat:
			if(paramValue == null || "".equals(paramValue)){
				break;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(thisCheckValue);
			try{
				String paramValueStr = String.valueOf(paramValue);
				Date date = sdf.parse(paramValueStr);
			}catch (Exception e) {
				e.getMessage();
				return false;
			}
			break;
		default:
			return false;
		}
		return true;
	}
}
