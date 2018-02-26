package com.models.cloud.pay.escrow.alipay.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TransformMapAndInfoUtil {

	/**
	 * 将Map转换为newClass类型实例对象
	 * newClass必须是通常情况下的业务实体类
	 * 即 有get和set方法，且方法名与成员变量符合规范
	 * @param map
	 * @param newClass
	 * @return 如果map中不存在 类newClass 的属性所对应的key则返回null
	 * @throws Exception
	 */
	public static Object transformMapToInfo(Map<String,Object> map, Class<?> newClass) throws Exception {
		Object o = null;
		String key = null;
		String parameterType = null;
		if(map==null||map.isEmpty()) return newClass.newInstance();
		// 得到所有的成员变量
		Field[] fs = newClass.getDeclaredFields();
		String methodName = null;
		Object mapValue = null;
		// 查找方法时需要传入的参数
		Class<?>[] parameterTypes = new Class[1];
		// 执行invoke方法时需要传入的参数
		Object[] args = new Object[1];
		Iterator<String> it = map.keySet().iterator();
		boolean shouldNew = false;
		try {
			o = newClass.newInstance();
			while (it.hasNext()) {
				key = (String) it.next();
				if(key==null)  continue;
				if (key != null) {
					for (int i = 0; i < fs.length; i++) {
						if (key.equals(fs[i].getName())
								||key.toLowerCase().equals(fs[i].getName().toLowerCase())
								||key.toUpperCase().equals(fs[i].getName().toUpperCase())){
							mapValue = map.get(key);
							if(mapValue==null) mapValue = map.get(key.toLowerCase());
							if(mapValue==null) mapValue = map.get(key.toUpperCase());
							if(mapValue == null) continue;
							shouldNew = true;
							// 拼set方法名
							methodName = "set"+ key.substring(0, 1).toUpperCase()+ key.substring(1).toLowerCase();
							// 得到成员变量的类型
							parameterTypes[0] = fs[i].getType();
							parameterType = parameterTypes[0].getName();
							// 找到方法
							Method method = newClass.getDeclaredMethod(methodName, parameterTypes);
							// 下面代码都是参数类型是什么，如果有需求可以自行增加
							if (parameterTypes[0] == short.class || parameterTypes[0] == Short.class) {// 当set方法中的参数为short
								if (mapValue instanceof Short) {
									args[0] = mapValue;
								}else{ 
									args[0] = Short.parseShort(String.valueOf( mapValue));
								}
							}else if (parameterTypes[0] == Integer.class || parameterTypes[0] == int.class) {// 当set方法中的参数为int或者Integer
								if (mapValue instanceof Integer) {
									args[0] = mapValue;
								} else {
									args[0] = Integer.parseInt(String.valueOf( mapValue));
								}
							}else if (parameterTypes[0] == long.class || parameterTypes[0] == Long.class) {// 当set方法中的参数为long
								if (mapValue instanceof Long) {
									args[0] = mapValue;
								} else {
									args[0] = Long.parseLong(String.valueOf( mapValue));
								}
							} else if (parameterTypes[0] == float.class || parameterTypes[0] == Float.class) {// 当set方法中的参数为float
								if (mapValue instanceof Float) {
									args[0] = mapValue;
								} else {
									args[0] = Float.parseFloat(String.valueOf( mapValue));
								}
							}else if (parameterTypes[0] == double.class || parameterTypes[0] == Double.class) {// 当set方法中的参数为double
								if (mapValue instanceof Double) {
									args[0] = mapValue;
								} else {
									args[0] = Double.parseDouble(String.valueOf( mapValue));
								}
							}else if (parameterTypes[0] == char.class || parameterTypes[0] == Character.class) {// 当set方法中的参数为char   
								if (mapValue instanceof Character || mapValue instanceof char[]) {
									args[0] = mapValue;
								} else {
									args[0] = (String.valueOf( mapValue)).toCharArray()[0];
								}
							}  else if (parameterTypes[0] == byte.class || parameterTypes[0] == Byte.class) {// 当set方法中的参数为byte   
								if (mapValue instanceof Byte || mapValue instanceof byte[]) {
									args[0] = mapValue;
								} else {
									args[0] = (String.valueOf( mapValue)).getBytes()[0];
								}
							}else if (parameterTypes[0] == boolean.class|| parameterTypes[0] == Boolean.class) {// 当set方法中的参数为boolean
								if (mapValue instanceof Boolean) {
									args[0] = mapValue;
								} else {
									if("true".equals(String.valueOf( mapValue)))
										args[0] = true;
									else 
										args[0] = false;
								}
							}else if (parameterTypes[0] == Date.class) {// 当set方法中的参数为Date
								if (mapValue instanceof Date) {
									args[0] = mapValue;
								} else {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									args[0] = sdf.parse(String.valueOf( mapValue));
								}
							} else if (parameterTypes[0] == String.class) {// 当set方法中的参数为String 或 String[]
								if (mapValue instanceof String[]) {
									String[] tempArray = (String[]) mapValue;
									String result = "";
									for (int m = 0; m < tempArray.length; m++) {
										result = result + tempArray[m] + ",";
									}
									result = result.substring(0, result.length() - 1);
									args[0] = result;
								} else if(mapValue instanceof String){
									args[0] = (String)mapValue;
								}else{
									args[0] = String.valueOf( mapValue);
								}
							} else {// 当set方法中的参数为其他
								args[0] = mapValue;
							}
							// 执行set方法存储数据
							method.invoke(o, args);
							break;
						}
					}
				}
			}
		} catch (SecurityException e) {
			throw new SecurityException("[mapBind]安全异常：" + e);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodException("[mapBind]"+newClass.getName()+"中无此方法异常" + e);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(newClass.getName()+"中" + key + "属性类型" + parameterType+ "与Map中值为" + mapValue + "的类型不匹配");
		} catch (IllegalAccessException e) {
			throw new IllegalAccessException("[mapBind]IllegalAccessException异常");
		} catch (ParseException e) {
			throw new ParseException("[mapBind]ParseException异常", 0);
		} catch (Exception e) {
			throw e;
		}
		if(!shouldNew) 
			return null;
		else	
			return o;

	}
	
	/**
	 * 将Object转换为Map
	 * Object必须是通常情况下的业务实体类
	 * 即 有get和set方法，且方法名与成员变量符合规范
	 * @param obj
	 * @return
	 */
	public static Map<String,Object> transformInfoToMap(Object obj) throws Exception{
		Class<?> clazz = obj.getClass();
		// 得到所有的成员变量
		Field[] fs = clazz.getDeclaredFields();
		Field field = null;
		//方法名
		String methodName = null;
		Map<String, Object> map = new HashMap<String , Object>(); 
		Object o = null;
		try {
			for(int i=0;i<fs.length;i++){
				field = fs[i];
				methodName = "get"+field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase());
				Method method = clazz.getDeclaredMethod(methodName, new Class[0]);
				o = method.invoke(obj, new Object[0]);
				map.put(field.getName(), o);
//				map.put(field.getName().toUpperCase(), o);
			}
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodException("参数"+field.getName()+"没有对应的get方法或方法名不规范,规范格式:"+methodName);
		} catch (SecurityException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new IllegalAccessException(clazz.getName()+"中方法"+methodName+"不可访问");
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(clazz.getName()+"中" + field.getName() + "属性类型与Map中类型不匹配");
		} catch (InvocationTargetException e) {
			throw e;
		}catch(Exception e){
			throw e;
		}
		
		return map;
	}

}
