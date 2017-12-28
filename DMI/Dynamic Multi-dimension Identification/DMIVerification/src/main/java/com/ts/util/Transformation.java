package com.ts.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Maps;

import net.sf.cglib.beans.BeanMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
  
public class Transformation {
	
	@SuppressWarnings("unchecked")
	public JSONObject toJson(Object entity) {
		JSONObject param = new JSONObject();
		try{
			Field[] fields = entity.getClass().getDeclaredFields();
			for(Field fie : fields) {
				Annotation annotation = fie.getAnnotation(XmlElement.class);
				XmlElement element = (XmlElement) annotation;
				String getname = fie.getName();
				getname = getname.substring(0, 1).toUpperCase() + getname.substring(1);
				String setname = element.name().toUpperCase();
				String type = fie.getGenericType().toString();
				Method md = entity.getClass().getMethod("get"+getname);
				
				if("class java.lang.String".equals(type)){
					param.put(setname, (String) md.invoke(entity));
				} else if ("class java.lang.Integer".equals(type)){
					param.put(setname, (Integer) md.invoke(entity));
				} else if ("class java.lang.Double".equals(type)){
					param.put(setname, (Double) md.invoke(entity));
				} else if ("class java.lang.Long".equals(type)){
					param.put(setname, (Long) md.invoke(entity));
				} else if ("class java.lang.Short".equals(type)){
					param.put(setname, (Short) md.invoke(entity));
				} else if ("class java.util.Date".equals(type)) {
					param.put(setname, (Date) md.invoke(entity));
				} else if ("class java.lang.Float".equals(type)) {
					param.put(setname, (Float) md.invoke(entity));
				} else if ("class java.lang.Boolean".equals(type)) {
					param.put(setname, (Boolean) md.invoke(entity));
				} else if (type.indexOf("java.util.List") != -1 && type.indexOf("com.ts.CXFClient") != -1) {
					
					List<Object> resutl = new ArrayList<Object>();
					List<Object> list = (List<Object>) md.invoke(entity);
					for(int i=0; i<list.size(); i++){
						resutl.add(toJson(list.get(i)));
					}
					param.put(setname, resutl);
					
				} else{
					//class com.ts.CXFClient.bean
					Object obj = (Object) md.invoke(entity);
					if(obj != null){
						if(setname.indexOf("SUB") != -1 && setname.indexOf("LIST") == -1){
							param.put(setname, toJson(obj));
						}
						if(setname.indexOf("SUB") == -1 && setname.indexOf("LIST") != -1){
							JSONArray ja = new JSONArray();
							ja.add(toJson(obj));
							param.put(setname, ja);
						}
					}else{
						param.put(setname, obj);
					}
				
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
    	
		return param;
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object setParam(Object entity, JSONObject value) {
		try{
			Field[] fields = entity.getClass().getDeclaredFields();
			for(Field fie : fields) {
				Annotation annotation = fie.getAnnotation(XmlElement.class);
				XmlElement element = (XmlElement) annotation;
				String getname = fie.getName();
				getname = getname.substring(0, 1).toUpperCase() + getname.substring(1);
				String setname = element.name().toUpperCase();
				String type = fie.getGenericType().toString();
				
				if ("class java.lang.String".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, String.class);
					md.invoke(entity, value.get(setname)==null?"":value.getString(setname));
					/*Method[] methods = entity.getClass().getMethods();
					String mNanme = "set" + getname;
					for(Method m : methods)
					{
						if(m.getName().toUpperCase().equals(mNanme.toUpperCase())){
							m.invoke(entity, value.getString(setname));
						}
					}*/
				} else if ("class java.lang.Integer".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Integer.class);
					md.invoke(entity, value.get(setname)==null?"":value.getInt(setname));
				} else if ("class java.lang.Double".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Double.class);
					md.invoke(entity, value.get(setname)==null?"":value.getDouble(setname));
				} else if ("class java.util.Date".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Date.class);
					md.invoke(entity, toDate(value.get(setname)==null?"":value.getString(setname)));
				} else if ("class java.lang.Long".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Long.class);
					md.invoke(entity, value.get(setname)==null?"":value.getLong(setname));
				} else if ("class java.lang.Boolean".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Boolean.class);
					md.invoke(entity, value.get(setname)==null?"":value.getBoolean(setname));
				} else if (type.indexOf("java.util.List") != -1 && type.indexOf("com.ts.CXFClient") != -1) {
					
					Method md = entity.getClass().getMethod("get"+getname);
					List myList = (List) md.invoke(entity);
					
					ParameterizedType pt = (ParameterizedType) fie.getGenericType();  
					Class clazz = (Class) pt.getActualTypeArguments()[0];
					
					JSONArray jsonList = value.getJSONArray(setname);
					for(Object jo : jsonList.toArray())
					{
						myList.add(setParam(clazz.newInstance(), (JSONObject)jo));
					}
					
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return entity;
	}
	
	@SuppressWarnings("rawtypes")
	public Map toMap(Object entity) {
		Map<String, Object> param = new HashMap<String, Object>();
		try{
			Field[] fields = entity.getClass().getDeclaredFields();
			for(Field fie : fields) {
				String getname = fie.getName();
				getname = getname.substring(0, 1).toUpperCase() + getname.substring(1);
				String type = fie.getGenericType().toString();
				
				if(!"serialVersionUID".equals(fie.getName())){
					
					Method md = entity.getClass().getMethod("get" + getname);
					
					if ("class java.lang.String".equals(type)) {
						param.put(fie.getName(), (String) md.invoke(entity));
					} else if ("class java.lang.Integer".equals(type)) {
						param.put(fie.getName(), (Integer) md.invoke(entity));
					} else if ("class java.lang.Double".equals(type)) {
						param.put(fie.getName(), (Double) md.invoke(entity));
					} else if ("class java.util.Date".equals(type)) {
						param.put(fie.getName(), (Date) md.invoke(entity));
					} else if ("class java.lang.Float".equals(type)) {
						param.put(fie.getName(), (Float) md.invoke(entity));
					} else if ("class java.lang.Long".equals(type)) {
						param.put(fie.getName(), (Long) md.invoke(entity));
					} else if ("class java.lang.Short".equals(type)) {
						param.put(fie.getName(), (Short) md.invoke(entity));
					} else if ("class java.lang.Boolean".equals(type)) {
						param.put(fie.getName(), (Boolean) md.invoke(entity));
					}
					
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return param;
	}
	
	public static String toCast(String str, Object obj) {
		if(str == null || "".equals(str)){
			obj = obj.toString();
			if ("class java.lang.Integer".equals(obj)) {
				return "0";
			}
			if ("class java.lang.Double".equals(obj)) {
				return "0.0";
			}
			if ("class java.util.Date".equals(obj)) {
				return null;
			}
		}
		
		return str;
	}
	
	public static String toDate(String timeDate) {
		try{
			if(timeDate!=null && timeDate.length()>0){
				SimpleDateFormat sdformat = null;
				if(timeDate.indexOf("-") != -1 && timeDate.indexOf(":") != -1){
					sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}else if(timeDate.indexOf("-") == -1 && timeDate.indexOf(":") == -1){
					sdformat = new SimpleDateFormat("yyyyMMdd HHmmss");
				}else if(timeDate.indexOf("-") != -1 && timeDate.indexOf(":") == -1){
					sdformat = new SimpleDateFormat("yyyy-MM-dd");
				}
				if(sdformat != null){
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateTime = sf.format(sdformat.parse(timeDate));
					return dateTime;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDate() {
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sf.format(new Date());
			return sf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Double doubleAdd(Double dou1, Double dou2) {
		BigDecimal b1 = new BigDecimal(Double.toString(dou1));
		BigDecimal b2 = new BigDecimal(Double.toString(dou2));
		return b1.add(b2).doubleValue();  
	}
	
	public static boolean doubleCompare(Double dou1, Double dou2) {
		Double obj1 = new Double(dou1);
		Double obj2 = new Double(dou2);
		int retval = obj1.compareTo(obj2);
		if(retval == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static String decimalToStr(Object obj) {
		if(obj != null) {
			BigDecimal bd = (BigDecimal) obj;
			//第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			return bd.toString();
		} else {
			return null;
		}
	}
	
	public static <T> Map<String, Object> beanToMap(T bean) {  
		Map<String, Object> map = Maps.newHashMap();  
	    if (bean != null) {  
	        BeanMap beanMap = BeanMap.create(bean);  
	        for (Object key : beanMap.keySet()) {  
	            map.put(key+"", beanMap.get(key));  
	        }
	    }
	    return map;
	}
	
	/**
	 * 一个设置参数的方法 适用于参数包含数组的类型
	 * @param entity
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object setParam_arr(Object entity, JSONObject value) {
		try{
			Field[] fields = entity.getClass().getDeclaredFields();
			for(Field fie : fields) {
				String getname = fie.getName();
				getname = getname.substring(0, 1).toUpperCase() + getname.substring(1);
				String setname = getname.toUpperCase();
				String type = fie.getGenericType().toString();
				
				if ("class java.lang.String".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, String.class);
					md.invoke(entity, value.get(setname)==null?"":value.getString(setname));
					/*Method[] methods = entity.getClass().getMethods();
					String mNanme = "set" + getname;
					for(Method m : methods)
					{
						if(m.getName().toUpperCase().equals(mNanme.toUpperCase())){
							m.invoke(entity, value.getString(setname));
						}
					}*/
				} else if ("class java.lang.Integer".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Integer.class);
					md.invoke(entity, value.get(setname)==null?"":value.getInt(setname));
				} else if ("class java.lang.Double".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Double.class);
					md.invoke(entity, value.get(setname)==null?"":value.getDouble(setname));
				} else if ("class java.util.Date".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Date.class);
					md.invoke(entity, toDate(value.get(setname)==null?"":value.getString(setname)));
				} else if ("class java.lang.Long".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Long.class);
					md.invoke(entity, value.get(setname)==null?"":value.getLong(setname));
				} else if ("class java.lang.Boolean".equals(type)) {
					Method md = entity.getClass().getMethod("set"+getname, Boolean.class);
					md.invoke(entity, value.get(setname)==null?"":value.getBoolean(setname));
				} else if (type.indexOf("class") != -1 && type.indexOf("com.ts.CXFClient") != -1) {
					
//					ParameterizedType pt = (ParameterizedType) fie.getGenericType();  
//					Class clazz = (Class) pt.getActualTypeArguments()[0];
					Class<?> pt = (Class<?>) fie.getGenericType();  
					
					JSONArray jsonList = value.getJSONArray(setname);
					if(!jsonList.isEmpty()){
						Object list = Array.newInstance(pt.getComponentType(), jsonList.size()); 
						for (int j = 0; j < jsonList.size(); j++) {
							Object jo = jsonList.get(j);
							Array.set(list,j,setParam_arr(pt.getComponentType().newInstance(), (JSONObject)jo));
						}
						Method md = entity.getClass().getMethod("set"+getname,pt);
						md.invoke(entity,list);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return entity;
	}
	/**
	 * 一个转换为Json的方法 适用于参数包含数组的类型
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJson_arr(Object entity) {
		JSONObject param = new JSONObject();
		try{
			Field[] fields = entity.getClass().getDeclaredFields();
			for(Field fie : fields) {
				String getname = fie.getName();
				getname = getname.substring(0, 1).toUpperCase() + getname.substring(1);
				String setname = getname.toUpperCase();
				String type = fie.getGenericType().toString();
				Method md = entity.getClass().getMethod("get"+getname);
				
				if("class java.lang.String".equals(type)){
					param.put(setname, (String) md.invoke(entity));
				} else if ("class java.lang.Integer".equals(type)){
					param.put(setname, (Integer) md.invoke(entity));
				} else if ("class java.lang.Double".equals(type)){
					param.put(setname, (Double) md.invoke(entity));
				} else if ("class java.lang.Long".equals(type)){
					param.put(setname, (Long) md.invoke(entity));
				} else if ("class java.lang.Short".equals(type)){
					param.put(setname, (Short) md.invoke(entity));
				} else if ("class java.util.Date".equals(type)) {
					param.put(setname, (Date) md.invoke(entity));
				} else if ("class java.lang.Float".equals(type)) {
					param.put(setname, (Float) md.invoke(entity));
				} else if ("class java.lang.Boolean".equals(type)) {
					param.put(setname, (Boolean) md.invoke(entity));
				} else if (type.indexOf("class") != -1 && type.indexOf("com.ts.CXFClient") != -1) {
					
					List<Object> resutl = new ArrayList<Object>();
					Object[] list =(Object[] ) md.invoke(entity);
					if(list!=null){
						for(int i=0; i<list.length; i++){
							resutl.add(toJson_arr(list[i]));
						}
					}
					
					param.put(setname, resutl);
					
				} else{
					//class com.ts.CXFClient.bean
					Object obj = (Object) md.invoke(entity);
					if(obj != null){
						if(setname.indexOf("SUB") != -1 && setname.indexOf("LIST") == -1){
							param.put(setname, toJson(obj));
						}
						if(setname.indexOf("SUB") == -1 && setname.indexOf("LIST") != -1){
							JSONArray ja = new JSONArray();
							ja.add(toJson(obj));
							param.put(setname, ja);
						}
					}else{
						param.put(setname, obj);
					}
				
				}
				
			}
			
		}catch(Exception e){
			//e.printStackTrace();
		}
    	
		return param;
    }
}