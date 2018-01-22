package com.pay.cloud.util;

import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

/**
 * 
 * @author qingsong.li
 *
 */
public class SysFilter implements Filter {

	private static final Logger logger = Logger.getLogger(SysFilter.class);

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		int random = (int) (Math.random()*100000000);
		String logId = System.currentTimeMillis() + "" + random;  
		NDC.push(logId);
		
		Date beginDate = new Date();
		Long beginl = beginDate.getTime();
		
		chain.doFilter(request, response);
		
		Date endDate = new Date();
		Long endl = endDate.getTime();
		if(logger.isInfoEnabled()){
			logger.info("总共耗时"+(endl-beginl)+"毫秒");
		}
		NDC.remove();
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
