package com.dhcc.common.system.systemLog;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.util.CommUtil;
import com.dhcc.common.util.DateUtil;
import com.dhcc.modal.system.Tsuser;
import com.opensymphony.xwork2.ActionContext;

/**
 * 表tsemailsmsrecord 短信，电子邮件通知
 * 
 * @author sz
 * 
 */
public class LogDao {
	private HttpServletRequest request = ServletActionContext.getRequest();
	/**
	 * 用于系统登录
	 * ipaddress,hostname,hostMAC
	 */
	public void saveLogForLogin(){
		DBManager dbm = new DBManager();
		String userid = (String)ActionContext.getContext().getSession().get("userid");
		//String createtime = CreateDate.getDateString();
		String createtime = DateUtil.toTime()+"";
		try {
			String sql = String.format("insert into tslog(id,userid,logdate,ipaddress,otherMessage,action,message) " +
					" values('%s','%s','%s','%s','%s','%s','%s')",
					CommUtil.getID(),userid,createtime,getIpAddress(request),getOtherMessage(request),"登录系统",getMessage(userid, createtime));
			dbm.addBatch(sql);
			dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbm.close();
		}
	}
	/**
	 * 用于页面查看
	 */
	public void saveLogForJSP(String menuName){
		DBManager dbm = new DBManager();
		String userid = (String)ActionContext.getContext().getSession().get("userid");
		//String createtime = CreateDate.getDateString();
		String createtime = DateUtil.toTime()+"";
		try {
			String sql = String.format("insert into tslog(id,userid,logdate,ipaddress,otherMessage,action,message) " +
					" values('%s','%s','%s','%s','%s','%s','%s')",
					CommUtil.getID(),userid,createtime,getIpAddress(request),getOtherMessage(request),"查看 "+menuName +" 页面",getMessage(userid, createtime,menuName));
			dbm.addBatch(sql);
			dbm.executeBatch();
		} catch (Exception e) {
		} finally {
			dbm.close();
		}
	}
	/**
	 *将增删改操作日志存入表中
	 */
	public void saveLog(int action, String tablename, String id){
		DBManager dbm = new DBManager();
		String userid = (String)ActionContext.getContext().getSession().get("userid");
		//String createtime = CreateDate.getDateString();
		String createtime = DateUtil.toTime()+"";
		
		try {		
			String sql = String.format("insert into tslog(id,userid,logdate,ipaddress,otherMessage,action,message) " +
					" values('%s','%s','%s','%s','%s','%s','%s')",
					CommUtil.getID(),userid,createtime,getIpAddress(request),getOtherMessage(request),getActionName(action),getMessage(userid,createtime,action,tablename,id));
			dbm.addBatch(sql);
			dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbm.close();
		}
	}
	
	/**
	 *@作者 GYR
	 *@日期 2014-07-18 11:42:15 AM
	 *@param logtitle
	 *@param content
	 */
	public void saveLogInfo(String logtitle,String content){
		DBManager dbm = new DBManager();
		String userid = (String)ActionContext.getContext().getSession().get("userid");
		//String createtime = CreateDate.getDateString();
		String createtime = DateUtil.toTime() + "";//当前时间
		
		try {
			String sql = String.format("insert into tslog(id,userid,logdate,ipaddress,otherMessage,action,message) " +
					" values('%s','%s','%s','%s','%s','%s','%s')",
					CommUtil.getID(),userid,createtime,getIpAddress(request),getOtherMessage(request),logtitle,content);
			dbm.addBatch(sql);
			dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbm.close();
		}
	}
	
	/**
	 * 获取用户信息
	 * @param id
	 * @return
	 */
	public Tsuser getUser(String id){
		DBManager dbm = new DBManager();
		String sql = "select * from tsuser where id='"+id+"'";
		Tsuser model = (Tsuser)dbm.getObject(Tsuser.class,sql);
		dbm.close();
		return model;
	}
	
	public String getMessage(String userid,String createtime){
		return "用户 "+getUser(userid).getUsername()+" 于 "+createtime+" 登录系统！";
	}
	public String getMessage(String userid,String createtime,String menuName){
		return "用户 "+getUser(userid).getUsername()+" 于 "+createtime+" 查看了 "+menuName+"！";
	}
	public String getMessage(String userid,String createtime,int action, String tablename, String id){
		String actionName = "";
		if(1==action){
			actionName += "添加";
		}
		if(2==action){
			actionName += "修改";
		}
		if(3==action){
			actionName += "删除";
		}
		if(4==action){
			actionName += "模板转换虚机";
		}
		if(5==action){
			actionName += "克隆虚机";
		}
		if(6==action){
			actionName += "克隆模板";
		}
		return "用户 "+getUser(userid).getUsername()+" 于 "+createtime+" 对表："+tablename+" 中主键id为："+id+" 的记录执行了"+actionName+"操作！";
	}
	
	public String getActionName(int action){
		String actionName = "执行了";
		if(1==action){
			actionName += "添加";
		}
		if(2==action){
			actionName += "修改";
		}
		if(3==action){
			actionName += "删除";
		}
		if(4==action){
			actionName += "模板转换虚机";
		}
		if(5==action){
			actionName += "克隆虚机";
		}
		if(6==action){
			actionName += "克隆模板";
		}
		actionName += "操作";
		return actionName;
	}
	/**
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	//获取客户端的系统和浏览器
	//在下面的网址上可以查看返回串的意思  即：什么浏览器在什么系统上登录
	//http://www.whatismybrowser.com/developers/custom-parse?useragent=Mozilla/5.0%20(compatible;%20MSIE%209.0;%20Windows%20NT%206.1;%20WOW64;%20Trident/5.0;%20BOIE9;ENGB)&SecurityID=aa20514ae380bb0bceb3947745c92526439bca58
	public static String getOtherMessage(HttpServletRequest request){
		return request.getHeader("User-Agent");
	}
	// 获取客户端IP地址 
	public static String getIpAddress(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		
//		System.out.println(request.getHeader("User-Agent"));    //就是取得客户端的系统版本     
//		System.out.println(request.getRemoteAddr());    //取得客户端的IP     
		
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            //如果ip是   0:0:0:0:0:0:0:1  表示用localhost登录
            if(ip.equals("127.0.0.1")||ip.equals("0:0:0:0:0:0:0:1")){
            	//根据网卡取本机配置的IP    
            	InetAddress inet=null;
            	try {
            		inet = InetAddress.getLocalHost();
            	} catch (UnknownHostException e) {
            		e.printStackTrace();
            	}
            	ip= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割    
        if(ip!=null && ip.length()>15){ //"***.***.***.***".length() = 15    
        	if(ip.indexOf(",")>0){
        		ip = ip.substring(0,ip.indexOf(","));
        	}
        }
        return ip; 
	}
}
