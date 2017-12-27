package com.aghit.base;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import com.framework.db.jdbc.JdbcService;

@Service
public abstract class BaseService extends ApplicationObjectSupport {
	
	// 日志
	public static Logger log = Logger.getLogger(BaseService.class);
	
	@Resource(name = "jdbcService")
	private JdbcService jdbcService;
	
	/**
	 * 提供一个获取Spring容器中Service类的方法
	 * 
	 * @param <T> 声明一个泛型
	 * @param id Spring ServiceID
	 * @param c 返回的类型Class
	 * @return
	 */
	public <T> T getSpringServiceById(String id, Class<T> c){
		
		T rtn = null;
		try{
			ApplicationContext ctx = getApplicationContext();
			rtn = ctx.getBean(id, c);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtn;
	}

	public JdbcService getJdbcService() {
		return jdbcService;
	}

	public void setJdbcService(JdbcService jdbcService) {
		this.jdbcService = jdbcService;
	}
	
}