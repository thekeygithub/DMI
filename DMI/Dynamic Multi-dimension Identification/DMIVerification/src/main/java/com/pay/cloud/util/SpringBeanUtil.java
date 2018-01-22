package com.pay.cloud.util;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Repository;

/**
 * 
 * @Description: 提供Spring容器、获取Srping bean的工具类
 * @ClassName: SpringBeanUtil 
 * @author: zhengping.hu
 * @date: 2015年12月29日 下午5:57:57
 */
@Repository("springBeanUtil")
public class SpringBeanUtil extends ApplicationObjectSupport{
	
	public static final Logger logger = Logger.getLogger(SpringBeanUtil.class);
	
	private static ApplicationContext ctx = null;
	
	@PostConstruct
	public void fetchSpringContext(){
		
		ctx = getApplicationContext();
		if(ctx == null){
			throw new RuntimeException("无法获取Spring ApplicationContext容器");
		}
		
		logger.info("获取Spring ApplicationContext容器");
	}
	
	
	/**
	 * 提供一个获取Spring容器中Service类的方法
	 * 
	 * @param beanId spring的beanID
	 * @param c 对象类型
	 * @return
	 */
	public static <T> T getSpringBean(String beanId,Class<T> c){
		
		T rtn = null;
		try{
			rtn = ctx.getBean(beanId, c);
		}catch(Exception ex){
			logger.error("无法获取Spring ApplicationContext中的管理的对象");
		}
		
		return rtn;
	}
	
	/**
	 * 提供一个获取Spring容器中Service类的方法
	 * 
	 * @param beanId spring的beanID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSpringBean(String beanId){
		
		T rtn = null;
		try{
			rtn = (T)ctx.getBean(beanId);
		}catch(Exception ex){
			logger.error("无法获取Spring ApplicationContext中的管理的对象");
		}
		return rtn;
	}
}
