package com.pay.cloud.pay.escrow.mts.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
/**
 * 
 * @author yanjie.ji
 * @date 2016年12月5日 
 * @time 上午10:21:44
 */
public class MtsParam extends BaseReq {
	
	private static final String SPLIT_WORD="@#&";
	private static final String SPLIT_GROUP_JOIN="@|&";
	private static final String SPLIT_GROUP_SEPARATE="\\@\\|&";
	
	private static final String TYPE_CODE="09";
	private static final String TYPE_NAME="05";
	
	/**
	 * 待标化诊断列表
	 */
	private DiagInfo diag;

	public DiagInfo getDiag() {
		return diag;
	}

	public void setDiag(DiagInfo diag) {
		this.diag = diag;
	}

	@Override
	protected Map<String, String> getProp() {
		return new HashMap<String, String>();
	}
	
	public String getDiagParams(){
		if(diag==null) return "";
		StringBuilder str = new StringBuilder();
		if(StringUtils.isNotEmpty(diag.getDiag()))
			str.append(TYPE_NAME).append(SPLIT_WORD).append(diag.getDiag());
		if(StringUtils.isNotEmpty(str.toString()))
			str.append(SPLIT_GROUP_JOIN);
		if(StringUtils.isNotEmpty(diag.getCode()))
			str.append(TYPE_CODE).append(SPLIT_WORD).append(diag.getCode());
		return str.toString();
	}
	/**
	 * 诊断
	 * @author yanjie.ji
	 * @date 2016年12月5日 
	 * @time 上午10:14:48
	 */
	public static class DiagInfo {
		/**
		 * 原始诊断编码
		 */
		private String code;
		/**
		 * 原始诊断名称
		 */
		private String diag;
		
		public DiagInfo(){
			super();
		}
		
		public DiagInfo(String str){
			if(StringUtils.isEmpty(str)) return;
			String[] arr = str.split(SPLIT_GROUP_SEPARATE);
			if(arr==null||arr.length<1) return;
			for (String string : arr) {
				if(StringUtils.isEmpty(string)) continue;
				String[] diag = string.split(SPLIT_WORD);
				if(diag==null||diag.length!=2) continue;
				if(diag[0].equals(TYPE_NAME)) this.diag = diag[1];
				else if(diag[0].equals(TYPE_CODE)) this.code = diag[1];
				else continue;
			}
		}
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getDiag() {
			return diag;
		}
		public void setDiag(String diag) {
			this.diag = diag;
		}
	}
	
}
