package com.ebmi.std.interfaceapi;


import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ebmi.std.common.finalkey.Constant;


/**
 * 
 * @author xuhai 2016-5-17 11:10:03
 *
 */
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1344636179506553001L;
	
	public static boolean TESTFLAG=Boolean.valueOf(Constant.ISTEST_FLAG); //接口测试标志 false 代表真实调用接口   true代表返回模拟数据 
	//接口列表 
	
	//hsoft.yibao.micdinfo.get(医疗保险费用明细信息查询)
	public static String micdinfo = "hsoft.yibao.micdinfo.get";
	
	//hsoft.yibao.pmetrinfo.get(个人医疗待遇信息查询)
	public static String pmetrinfo="hsoft.yibao.pmetrinfo.get";
	//hsoft.yibao.demeinpayinfo.get(定点医疗机构端消费信息查询)
	public static String demeinpayinfo="hsoft.yibao.demeinpayinfo.get";
	//hsoft.yibao.miaacareinfo.get(医疗保险经办机构现金报销信息查询)
	public static String miaacareinfo="hsoft.yibao.miaacareinfo.get";
	//hsoft.yibao.pbamepayinfo.get(个人基本医疗缴费信息查询)
	public static String pbamepayinfo="hsoft.yibao.pbamepayinfo.get";
	//hsoft.common.pbaseinfo.get(个人基本信息查询 )
	//public static String pbaseinfo="hsoft.yibao.pbaseinfo.get";//xuhai notes 2016613
	public static String pbaseinfo="hsoft.common.pbaseinfo.get";//xuhai add 2016613
	//hsoft.yibao.pinsuinfo.get(个人参保信息查询)
	//public static String pinsuinfo="hsoft.yibao.pinsuinfo.get";//xuhai notes 2016613
	public static String pinsuinfo="hsoft.common.pinsuinfo.get";//xuhai add 2016613
	//hsoft.yibao.pmediinfo.get(医疗个人参保信息查询) 指定查询工作表
	public static String pmediinfo="hsoft.yibao.pmediinfo.get";
	//hsoft.yibao.pmeoutreinfo.get(个人医疗转外就医登记信息查询)
	public static String pmeoutreinfo="hsoft.yibao.pmeoutreinfo.get";
	//hsoft.yibao.pspdireinfo.get(个人特殊疾病登记信息查询)
	public static String pspdireinfo="hsoft.yibao.pspdireinfo.get";
	
	//hsoft.yibao.pbigmepayinfo.get(个人大额医疗缴费信息查询)
   public static String pbigmepayinfo="hsoft.yibao.pbigmepayinfo.get";
	
   //hsoft.yibao.pbameaccininfo.get(个人基本医疗账户收入信息查询)
   public static String pbameaccininfo="hsoft.yibao.pbameaccininfo.get";
   
   //hsoft.yibao.pmeendtranaccininfo.get(个人医疗年终结转账户收入信息查询)
   public static String pmeendtranaccininfo="hsoft.yibao.pmeendtranaccininfo.get";
   
   //hsoft.general.passowrd.getSMS 密码找回获取短信验证码
   public static String SMS_SEND = "hsoft.general.passowrd.getSMS" ;
   //hsoft.general.password.get 密码找回
   public static String PASS_GET = "hsoft.general.password.get" ;
   //hsoft.general.passowrd.modify 密码修改
   public static String PASS_MODIFY = "hsoft.general.passowrd.modify" ;
   //hsoft.yibao.mespdipayli.get 医疗特殊疾病支付限额查询
   public static String mespdipayli = "hsoft.yibao.mespdipayli.get" ;
   
   //个人医保账户变动
   //hsoft.yibao.tspmapayinfo.get(医疗两定个人账户消费信息查询)
   public static String tspmapayinfo = "hsoft.yibao.tspmapayinfo.get" ;
   
   //hsoft.yibao.crpapayinfo.get(现金报销个人账户消费信息查询)
   public static String crpapayinfo = "hsoft.yibao.crpapayinfo.get" ;
 //个人医保账户变动 end 
   
   //hsoft.shebao.policeimg.get(公安库照片查询)
   public static String policeimg = "hsoft.shebao.policeimg.get" ;
   //hsoft.shebao.img.get 照片获取服务
   public static String img = "hsoft.shebao.img.get" ;
   
   
   //居民使用
   //hsoft.yibao.pbamepayinfof.get 个人基本医疗缴费信息查询
   public static String pbamepayinfof = "hsoft.yibao.pbamepayinfof.get" ;
   //hsoft.yibao.inmeinsuinfo.get 居民医疗参保信息查询 指定查询工作表 "1"
   public static String inmeinsuinfo = "hsoft.yibao.inmeinsuinfo.get" ;
   //hsoft.yibao.inmetrinfo.get 居民医疗待遇信息查询
   public static String inmetrinfo = "hsoft.yibao.inmetrinfo.get" ;
   //hsoft.yibao.inbameaccininfo.get 居民基本医疗账户收入信息查询
   public static String inbameaccininfo = "hsoft.yibao.inbameaccininfo.get" ;
   //居民使用 end
   
   // hsoft.yibao.psemesupayinfo.get(个人公务员医疗补助缴费信息)
   public static String psemesupayinfo = "hsoft.yibao.psemesupayinfo.get" ;
   
   // hsoft.yibao.spmepayinfo.get(特殊人员医疗缴费信息查询)
   public static String spmepayinfo = "hsoft.yibao.spmepayinfo.get" ;
   
   // hsoft.yibao.pmerelocatreinfo.get(个人医疗异地安置登记信息查询)
   public static String pmerelocatreinfo = "hsoft.yibao.pmerelocatreinfo.get" ;
   
   //  hsoft.yibao.spcapaccininfo.get(特殊人员收缴个人账户收入信息查询) --缴费收入
   public static String spcapaccininfo = "hsoft.yibao.spcapaccininfo.get" ;
   // hsoft.yibao.spcapaccseininfo.get(特殊人员收缴个人账户二次收入信息查询) --缴费收入
   public static String spcapaccseininfo = "hsoft.yibao.spcapaccseininfo.get" ;
   // hsoft.yibao.dfundaccininfo.get(铺底资金账户收入信息查询)----缴费收入
   public static String dfundaccininfo = "hsoft.yibao.dfundaccininfo.get" ;
   // hsoft.yibao.semesupaccininfo.get(公务员医疗补助个人账户收入信息查询)----缴费收入
   public static String semesupaccininfo = "hsoft.yibao.semesupaccininfo.get" ;
   
   // hsoft.yibao.twofixmeinfo.get 医疗两定信息查询
   public static String twofixmeinfo = "hsoft.yibao.twofixmeinfo.get" ;
   
   //hsoft.shebao.policeimg.get(社保卡挂失查询)
   public static String cardstatus = "hsoft.shebao.cardstatus.get" ;
   //hsoft.shebao.img.get(社保卡挂失服务)
   public static String cardloss = "hsoft.shebao.cardloss.modify" ;
   
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
