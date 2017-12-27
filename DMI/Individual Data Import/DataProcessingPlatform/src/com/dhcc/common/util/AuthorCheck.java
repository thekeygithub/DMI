/**
 * 
 */
package com.dhcc.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.inject.Scope.Strategy;

/**
 * @author tiger
 * 
 */
public class AuthorCheck implements Filter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public void destroy() {

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		String currentURL = request.getRequestURI(); // 取得根目录所对应的绝对路径:
//		System.out.println(currentURL);
		String targetURL="";
		String[] strarray= currentURL.split("/");
		if (strarray.length==0){
			//targetURL="login.jsp";
		}else{
			targetURL = strarray[strarray.length-1];
		}
		
		
		HttpSession session = request.getSession(false);
//		System.out.println("----------------判断用户是否登录开始-----------------");
//		System.out.println("判断是否登录时输出截取的targetURL="+targetURL);
		
		if (!"login.html".equals(targetURL) && !"noauthority.jsp".equals(targetURL)) {
			// 判断当前页是否是重定向以后的登录页面页面，如果是就不做session的判断，防止出现死循环
//			System.out.println("不是/login.jsp界面是session的值为："+session);
			if(session != null){
//				System.out.println("session不为空时获取session中的userid："+session.getAttribute("userid"));
//				System.out.println(session.getAttribute("userid") == null);
			}
			if (session == null || session.getAttribute("userid") == null) {
				// *用户登录以后需手动添加session
				
				//response.sendRedirect(request.getContextPath() + "/login.jsp");
				// 如果session为空表示用户没有登录就重定向到login.jsp页面
				PrintWriter out = response.getWriter();
				String url = request.getContextPath() + "/login.html";
				out.print("<script> window.top.document.location.href='"+url+"'</script>");
				out.flush();
				out.close();
				return;
			}
		}
		// 加入filter链继续向下执行
		arg2.doFilter(request, response);
		/**
		 * 调用FilterChain对象的doFilter方法。Filter接口的doFilter方法取一个FilterChain对象作 为它
		 * 的一个参数。在调用此对象的doFilter方法时，激活下一个相关的过滤器。如果没有另
		 * 一个过滤器与servlet或JSP页面关联，则servlet或JSP页面被激活。
		 */
//		System.out.println("----------------判断用户是否登录结束-----------------");
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
