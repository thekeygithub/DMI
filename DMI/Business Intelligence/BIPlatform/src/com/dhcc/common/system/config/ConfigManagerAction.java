package com.dhcc.common.system.config;

import com.dhcc.common.util.AnyTypeAction;
import com.dhcc.common.util.CommUtil;
import com.dhcc.modal.system.Tsconfig;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigManagerAction extends AnyTypeAction<Tsconfig, Tsconfig> implements ModelDriven<Tsconfig> {

	private static final long serialVersionUID = 1L;
	private ConfigDao dao = new ConfigDao();
	
	private Tsconfig model = new Tsconfig();
	private String ids;
	
	private String sysLogTitle;//日志标题
	private String sysLogContent;//日志内容
	private String querySql;
	
	public String configAdd(){
		model.setId(CommUtil.getID());
		querySql = dao.QuerySql(model.getId());
		sysLogTitle = "添加系统配置信息";
		sysLogContent = "添加ID为"+model.getId()+"系统配置信息记录！";
		return super.Add(model, model, querySql, sysLogTitle, sysLogContent);
	}
	
	public String configDel(){
		sysLogTitle = "删除系统配置信息";
		return super.Del(ids, "tsconfig", sysLogTitle);
	}
	
	public String configQueryById(){
		querySql = dao.QuerySql(model.getId());
		return super.Mess(model, querySql);
	}
	
	public String configUpdate(){
		querySql = dao.QuerySql(model.getId());
		sysLogTitle = "修改系统配置信息";
		sysLogContent = "修改ID为"+model.getId()+"系统配置信息记录！";
		return super.Updept(model, model, querySql, sysLogTitle, sysLogContent);
	}

	public Tsconfig getModel() {
		return model;
	}
	public void setModel(Tsconfig model) {
		this.model = model;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
