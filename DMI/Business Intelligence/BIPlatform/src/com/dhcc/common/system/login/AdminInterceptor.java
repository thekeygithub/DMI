package com.dhcc.common.system.login;


import java.util.Map;
import org.apache.log4j.Logger;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


/**
 * 拦截器
 * @author GYR
 *
 */
public class AdminInterceptor extends AbstractInterceptor{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AdminInterceptor.class);

	 @Override 
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionContext context = invocation.getInvocationContext();
		Map map = context.getSession();
//		System.out.println("-----开始权限校验---");
		
		if (!map.containsKey("userid")) {
//			System.out.println("=="+Action.LOGIN);
			return Action.LOGIN;
		} else{
//			System.out.println("----登录过---");
			return invocation.invoke(); // 用户已登入，放行
		}
		
	}

}
