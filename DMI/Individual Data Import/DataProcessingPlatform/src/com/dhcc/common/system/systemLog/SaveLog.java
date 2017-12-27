package com.dhcc.common.system.systemLog;

/**
 * 
 * @author sz
 *
 */
public class SaveLog {
	private LogDao dao = new LogDao();
	/**
	 * 将增删改操作日志存入表中
	 * @param action 1：添加 2：修改 3：删除 4：模板转换虚机 5:克隆 6 ：克隆模板
	 * @param tablename 表名称
	 * @param id 主键id
	 */
	public void saveLog(int action, String tablename, String id){
		dao.saveLog(action, tablename, id);
	}
	/**
	 * 将用户登录系统存入表中
	 */
	public void saveLogForLogin(){
		dao.saveLogForLogin();
	}
	/**
	 * 将用户查看菜单的记录存入表中
	 * @param menuName 菜单名称
	 */
	public void saveLogForJSP(String menuName){
		dao.saveLogForJSP(menuName);
	}
	
	/**
	 *@作者 GYR
	 *@日期 201411:43:57 AM
	 *@param logtitle
	 *@param content
	 */
	public void saveLog(String logtitle,String content){
		dao.saveLogInfo(logtitle, content);
	}
	
	
	public static void main(String[] args) {
		SaveLog ss = new SaveLog();
		ss.saveLogForLogin();
	}
}
