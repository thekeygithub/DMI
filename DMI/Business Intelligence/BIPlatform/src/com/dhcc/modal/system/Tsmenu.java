package com.dhcc.modal.system;
public class Tsmenu {
    private String id;//用户ID
    private String title;//菜单标题
    private String url;//菜单路径
    private Integer ordernum;//菜单序号，同级子菜单，从1开始
    private String remark;//备注信息
    private String pid;//上级菜单ID
    private String whetherpublic;//是否公开：01公开，02不公开
    private String image;//菜单图标路径
    private Integer actiontype;//菜单打开方式1：当前页面刷新 2：tab页打开 3：弹出窗口

    /** 用户ID */
    public String getId() {
        return id;
    }
    /** 用户ID */
    public void setId(String id) {
        this.id = id;
    }
    /** 菜单标题 */
    public String getTitle() {
        return title;
    }
    /** 菜单标题 */
    public void setTitle(String title) {
        this.title = title;
    }
    /** 菜单路径 */
    public String getUrl() {
        return url;
    }
    /** 菜单路径 */
    public void setUrl(String url) {
        this.url = url;
    }
    /** 菜单序号，同级子菜单，从1开始 */
    public Integer getOrdernum() {
        return ordernum;
    }
    /** 菜单序号，同级子菜单，从1开始 */
    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }
    /** 备注信息 */
    public String getRemark() {
        return remark;
    }
    /** 备注信息 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /** 上级菜单ID */
    public String getPid() {
        return pid;
    }
    /** 上级菜单ID */
    public void setPid(String pid) {
        this.pid = pid;
    }
    /** 是否公开：01公开，02不公开 */
    public String getWhetherpublic() {
		return whetherpublic;
	}
    /** 是否公开：01公开，02不公开 */
	public void setWhetherpublic(String whetherpublic) {
		this.whetherpublic = whetherpublic;
	}
	/** 菜单图标路径 */
    public String getImage() {
        return image;
    }
    /** 菜单图标路径 */
    public void setImage(String image) {
        this.image = image;
    }
    /** 菜单打开方式1：当前页面刷新 2：tab页打开 3：弹出窗口 */
    public Integer getActiontype() {
        return actiontype;
    }
    /** 菜单打开方式1：当前页面刷新 2：tab页打开 3：弹出窗口 */
    public void setActiontype(Integer actiontype) {
        this.actiontype = actiontype;
    }
}