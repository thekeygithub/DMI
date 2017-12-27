package com.dhcc.ts.yzd;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.net.httpserver.Authenticator.Success;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class YzdAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private YzdDao dao = new YzdDao();
	/**
	 * @描述：临时医嘱单，长期医嘱单
	 * @作者：SZ
	 * @时间：2017年2月28日 下午7:47:31
	 * @return
	 */
	private String zyh;
	private String type;// 1:临时医嘱单 2:长期医嘱单
	private String yztime;// 医嘱时间

	public String queryYzd() {
		List<YzdModel> listmodel = dao.queryYzd(zyh, type, yztime);

		JSONArray json = JSONArray.fromObject(listmodel);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * @描述：查询临时医嘱单列表，长期医嘱单列表
	 * @作者：SZ
	 * @时间：2017年3月1日 下午12:56:05
	 * @return
	 */
	public String queryYzdList() {
		List<YzdModel> listmodel = dao.queryYzdList(zyh, type);

		JSONArray json = JSONArray.fromObject(listmodel);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	/**
	 * @描述：病程记录查询  1:首次病程   2:日常病程
	 * @作者：SZ
	 * @时间：2017年3月2日 上午9:54:55
	 * @return
	 */
	public String queryBcjl(){
		List<BcjlModel> map = dao.queryBcjl(zyh, type);
		
		JSONArray json = JSONArray.fromObject(map);
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String querySjList(){
		JSONObject json = JSONObject.fromObject(dao.querySj(zyh));
		PrintWriter pw = null;
		try {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			pw = ServletActionContext.getResponse().getWriter();
			pw.print(json);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String userSignOut(){
		String userid = (String) ActionContext.getContext().getSession().get("userid");
		ActionContext.getContext().getSession().remove("userid");
		return SUCCESS;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getYztime() {
		return yztime;
	}

	public void setYztime(String yztime) {
		this.yztime = yztime;
	}
}
