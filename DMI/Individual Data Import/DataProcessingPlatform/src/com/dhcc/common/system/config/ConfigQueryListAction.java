package com.dhcc.common.system.config;

import com.dhcc.common.util.AnyTypeAction;
import com.dhcc.modal.system.Tsconfig;
/**
 * @描述：系统配置列表查询
 * @作者：SZ
 * @时间：2014-11-25 下午03:14:52
 */
public class ConfigQueryListAction extends AnyTypeAction<Tsconfig, Tsconfig>{

	private static final long serialVersionUID = 1L;
	private ConfigDao dao = new ConfigDao();
	
	private String dtype;
	private String dvalue;
	private String sortname = "dtype,dkey";// 排序列名
	private String sortorder = "asc";// 排序方向 asc
	
	public String execute() throws Exception {
		Tsconfig model = new Tsconfig();
		String sql = dao.QueryList(dtype, dvalue,sortname,sortorder);
		return super.List(model,sql);
	}

	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	public String getDvalue() {
		return dvalue;
	}
	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public java.util.List<Tsconfig> getListmodel() {
		// TODO Auto-generated method stub
		return super.getListmodel();
	}

	@Override
	public Integer getPage() {
		// TODO Auto-generated method stub
		return super.getPage();
	}

	@Override
	public Integer getPagesize() {
		// TODO Auto-generated method stub
		return super.getPagesize();
	}

	@Override
	public Integer getRecord() {
		// TODO Auto-generated method stub
		return super.getRecord();
	}

	@Override
	public Integer getTotal() {
		// TODO Auto-generated method stub
		return super.getTotal();
	}
}
