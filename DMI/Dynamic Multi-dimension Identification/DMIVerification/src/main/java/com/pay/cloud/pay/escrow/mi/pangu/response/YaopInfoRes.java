package com.pay.cloud.pay.escrow.mi.pangu.response;

import java.util.List;

/**
 * 药品目录结果
 * @author yanjie.ji
 * @date 2016年11月20日 
 * @time 下午7:39:39
 */
public class YaopInfoRes extends BaseResponse {

	private List<Yaopinfo> yaoplist;
	
	public List<Yaopinfo> getYaoplist() {
		return yaoplist;
	}
	public void setYaoplist(List<Yaopinfo> yaoplist) {
		this.yaoplist = yaoplist;
	}

	public static class Yaopinfo{
		  private String yaopbm;//药品编码
		  private String tongymc;//通用名称tongymc;//
		  private String yingwmc;//英文名称yingwmc;//
		  private String feiylb;//收费类别feiylb;//
		  private String chufybz;//处方药标志chufybz;//
		  private String shoufxmdj;//收费项目等级shoufxmdj;//  
		  private String pinyzjm;//拼音助记码pinyzjm;//
		  private String wubzjm;//五笔助记码wubzjm;//
		  private String yaopjldw;//药品剂量单位yaopjldw;//
		  private String zuigjg;//最高价格zuigjg;//
		  private String lixzgxj;//离休最高限价lixzgxj;//  
		  private String jix;//剂型jix;//
		  private String meicyl;//每次用量meicyl;//
		  private String shiypc;//使用频次shiypc;//
		  private String yongf;//用法yongf;//  
		  private String danw;//单位danw;//
		  private String guig;//规格guig;//
		  private String xiandts;//限定天数xiandts;//
		  private String yaopspm;//药品商品名yaopspm;//
		  private String shangpmjg;//商品名价格shangpmjg;//
		  private String shangpmpym;//商品名拼音码shangpmpym;//
		  private String shangpmwbm;//商品名五笔码shangpmwbm;//
		  private String yaocmc;//药厂名称yaocmc;//
		  private String zizypbz;//自制药品标志zizypbz;//
		  private String guoyzz;//国药准字guoyzz;//
		  private String menzzfbl;//门诊自付比例menzzfbl;//
		  private String zhuyzfbl;//住院自付比例zhuyzfbl;//
		  private String lixzfbl;//离休自付比例lixzfbl;//
		  private String gongszfbl;//工伤自付比例gongszfbl;//
		  private String shengyzfbl;//生育自付比例shengyzfbl;//
		  private String eryzfbl;//二乙自付比例eryzfbl;//
		  private String jumzfbl;//居民自付比例jumzfbl;//
		  private String junxmzzfbl;//军休门诊自付比例junxmzzfbl;//
		  private String junxzyzfbl;//军休住院自付比例junxzyzfbl;//
		  private String yaopzl;//药品种类yaopzl;//
		  private String shifxyspbz;//是否需要审批标志shifxyspbz;//
		  private String beiz;//备注beiz;//
		public String getYaopbm() {
			return yaopbm;
		}
		public void setYaopbm(String yaopbm) {
			this.yaopbm = yaopbm;
		}
		public String getTongymc() {
			return tongymc;
		}
		public void setTongymc(String tongymc) {
			this.tongymc = tongymc;
		}
		public String getYingwmc() {
			return yingwmc;
		}
		public void setYingwmc(String yingwmc) {
			this.yingwmc = yingwmc;
		}
		public String getFeiylb() {
			return feiylb;
		}
		public void setFeiylb(String feiylb) {
			this.feiylb = feiylb;
		}
		public String getChufybz() {
			return chufybz;
		}
		public void setChufybz(String chufybz) {
			this.chufybz = chufybz;
		}
		public String getShoufxmdj() {
			return shoufxmdj;
		}
		public void setShoufxmdj(String shoufxmdj) {
			this.shoufxmdj = shoufxmdj;
		}
		public String getPinyzjm() {
			return pinyzjm;
		}
		public void setPinyzjm(String pinyzjm) {
			this.pinyzjm = pinyzjm;
		}
		public String getWubzjm() {
			return wubzjm;
		}
		public void setWubzjm(String wubzjm) {
			this.wubzjm = wubzjm;
		}
		public String getYaopjldw() {
			return yaopjldw;
		}
		public void setYaopjldw(String yaopjldw) {
			this.yaopjldw = yaopjldw;
		}
		public String getZuigjg() {
			return zuigjg;
		}
		public void setZuigjg(String zuigjg) {
			this.zuigjg = zuigjg;
		}
		public String getLixzgxj() {
			return lixzgxj;
		}
		public void setLixzgxj(String lixzgxj) {
			this.lixzgxj = lixzgxj;
		}
		public String getJix() {
			return jix;
		}
		public void setJix(String jix) {
			this.jix = jix;
		}
		public String getMeicyl() {
			return meicyl;
		}
		public void setMeicyl(String meicyl) {
			this.meicyl = meicyl;
		}
		public String getShiypc() {
			return shiypc;
		}
		public void setShiypc(String shiypc) {
			this.shiypc = shiypc;
		}
		public String getYongf() {
			return yongf;
		}
		public void setYongf(String yongf) {
			this.yongf = yongf;
		}
		public String getDanw() {
			return danw;
		}
		public void setDanw(String danw) {
			this.danw = danw;
		}
		public String getGuig() {
			return guig;
		}
		public void setGuig(String guig) {
			this.guig = guig;
		}
		public String getXiandts() {
			return xiandts;
		}
		public void setXiandts(String xiandts) {
			this.xiandts = xiandts;
		}
		public String getYaopspm() {
			return yaopspm;
		}
		public void setYaopspm(String yaopspm) {
			this.yaopspm = yaopspm;
		}
		public String getShangpmjg() {
			return shangpmjg;
		}
		public void setShangpmjg(String shangpmjg) {
			this.shangpmjg = shangpmjg;
		}
		public String getShangpmpym() {
			return shangpmpym;
		}
		public void setShangpmpym(String shangpmpym) {
			this.shangpmpym = shangpmpym;
		}
		public String getShangpmwbm() {
			return shangpmwbm;
		}
		public void setShangpmwbm(String shangpmwbm) {
			this.shangpmwbm = shangpmwbm;
		}
		public String getYaocmc() {
			return yaocmc;
		}
		public void setYaocmc(String yaocmc) {
			this.yaocmc = yaocmc;
		}
		public String getZizypbz() {
			return zizypbz;
		}
		public void setZizypbz(String zizypbz) {
			this.zizypbz = zizypbz;
		}
		public String getGuoyzz() {
			return guoyzz;
		}
		public void setGuoyzz(String guoyzz) {
			this.guoyzz = guoyzz;
		}
		public String getMenzzfbl() {
			return menzzfbl;
		}
		public void setMenzzfbl(String menzzfbl) {
			this.menzzfbl = menzzfbl;
		}
		public String getZhuyzfbl() {
			return zhuyzfbl;
		}
		public void setZhuyzfbl(String zhuyzfbl) {
			this.zhuyzfbl = zhuyzfbl;
		}
		public String getLixzfbl() {
			return lixzfbl;
		}
		public void setLixzfbl(String lixzfbl) {
			this.lixzfbl = lixzfbl;
		}
		public String getGongszfbl() {
			return gongszfbl;
		}
		public void setGongszfbl(String gongszfbl) {
			this.gongszfbl = gongszfbl;
		}
		public String getShengyzfbl() {
			return shengyzfbl;
		}
		public void setShengyzfbl(String shengyzfbl) {
			this.shengyzfbl = shengyzfbl;
		}
		public String getEryzfbl() {
			return eryzfbl;
		}
		public void setEryzfbl(String eryzfbl) {
			this.eryzfbl = eryzfbl;
		}
		public String getJumzfbl() {
			return jumzfbl;
		}
		public void setJumzfbl(String jumzfbl) {
			this.jumzfbl = jumzfbl;
		}
		public String getJunxmzzfbl() {
			return junxmzzfbl;
		}
		public void setJunxmzzfbl(String junxmzzfbl) {
			this.junxmzzfbl = junxmzzfbl;
		}
		public String getJunxzyzfbl() {
			return junxzyzfbl;
		}
		public void setJunxzyzfbl(String junxzyzfbl) {
			this.junxzyzfbl = junxzyzfbl;
		}
		public String getYaopzl() {
			return yaopzl;
		}
		public void setYaopzl(String yaopzl) {
			this.yaopzl = yaopzl;
		}
		public String getShifxyspbz() {
			return shifxyspbz;
		}
		public void setShifxyspbz(String shifxyspbz) {
			this.shifxyspbz = shifxyspbz;
		}
		public String getBeiz() {
			return beiz;
		}
		public void setBeiz(String beiz) {
			this.beiz = beiz;
		}
	}
}
