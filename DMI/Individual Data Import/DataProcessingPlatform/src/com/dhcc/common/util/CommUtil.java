package com.dhcc.common.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


public class CommUtil {
	/**
	 * @author tiger
	 * @date 2012-9-15 上午08:45:46
	 * @return String
	 * @Description: 生成不带-的GUID
	 */
	public static String getID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	public static String classToJson(List list) throws ClassNotFoundException, IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String json = "{rows:[";
		// 对所有对象执行循环
		for (int i = 0; i < list.size(); i++) {
			Field[] fs = list.get(i).getClass().getDeclaredFields();
			PropertyDescriptor pd = new PropertyDescriptor("ID", list.get(i).getClass());
			Method method = pd.getReadMethod();
			Object retObj = method.invoke(list.get(i), fs);
			json = json + "{ id:" + retObj + ",data:[";
			
			//对每个字段执行循环
			for (int j = 0; j < fs.length; j++) {
				Class objClass = Class.forName(list.get(i).getClass().getName());
				pd = new PropertyDescriptor(fs[j].getName(), list.get(i).getClass());
				method = pd.getReadMethod();
				if (pd.getName() != "ID") {
					retObj = method.invoke(list.get(i), fs);
					if (j == fs.length - 1) {
						if (i==list.size()-1){
							json = json + "\"" + retObj + "\"]}]}";
						}else{
							json = json + "\"" + retObj + "\"]},";
						}
					} else {
						json = json + "\"" + retObj + "\",";
					}
				}

			}
		}
		return json;

	}
	public static double add(double v1,double v2){   
		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
		return b1.add(b2).doubleValue();   
	}   
	public static double sub(double v1,double v2){   
		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
		return b1.subtract(b2).doubleValue();   
    }   
	public static void main(String args[]) throws IllegalArgumentException, IntrospectionException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, InvocationTargetException{

		System.out.println(CommUtil.getID());
	}

}
