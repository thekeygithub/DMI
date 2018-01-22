package com.pay.cloud.pay.escrow.mts.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pay.cloud.pay.escrow.mts.request.MtsParam;
/**
 * 
 * @author yanjie.ji
 * @date 2016年12月5日 
 * @time 上午11:28:36
 */
public class MtsRespResult extends BaseResp{
	/**
	 * 请求参数
	 */
	private MtsParam req;
	/**
	 * 匹配结果
	 */
	private List<MtsResult> result;
	
	public MtsParam getReq() {
		return req;
	}
	public void setReq(MtsParam req) {
		this.req = req;
	}
	public List<MtsResult> getResult() {
		return result;
	}
	public void setResult(List<MtsResult> result) {
		this.result = result;
	}



	/**
	 * MTS匹配结果
	 * @author yanjie.ji
	 * @date 2016年12月5日 
	 * @time 上午11:27:47
	 */
	public static class MtsResult{
		private static final String SPLIT_WORD="@#&";
		private static final String SPLIT_GROUP=",";
		private static final String SPLIT_CHRONIC="\\@\\|&";
		
		/**
		 * 医保端编码
		 */
		private String code;
		/**
		 * 医保端诊断名称
		 */
		private String name;
		/**
		 * 医保编码类型
		 */
		private String type;
		/**
		 * 是否是慢性病
		 */
		private boolean chronic;
		
		public static List<MtsResult> createMtsResult(String str){
			List<MtsResult> list = new ArrayList<MtsResult>();
			if(StringUtils.isEmpty(str)) return list;
			String arrs[] = str.split(SPLIT_CHRONIC);
			if(arrs==null||arrs.length<=0) return list;
			if(StringUtils.isNotEmpty(arrs[0])){
				list.addAll(getResult(arrs[0]));
			}
			if(arrs.length>1&&StringUtils.isNotEmpty(arrs[1])){
				list.addAll(getResult(arrs[1]));
			}
			return list;
		}
		
		private static List<MtsResult> getResult(String str){
			List<MtsResult> list = new ArrayList<MtsResult>();
			String[] nomalDiags = str.split(SPLIT_GROUP);
			if(nomalDiags!=null&&nomalDiags.length>0){
				MtsResult result = null;
				boolean chronic = false;
				for (int i=0;i<nomalDiags.length;i++) {
					String string = nomalDiags[i];
					if(i==nomalDiags.length-1) chronic = true;
					if(StringUtils.isEmpty(string)) continue;
					String words[]  = string.split(SPLIT_WORD);
					if(words==null) continue;
					result = new MtsResult();
					if(words.length>=1)
					result.setCode(words[0]);
					if(words.length>=2)
						result.setName(words[1]);
					if(words.length>=3)
						result.setType(words[2]);
					result.setChronic(chronic);
					list.add(result);
				}
			}
			return list;
		}
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public boolean isChronic() {
			return chronic;
		}

		public void setChronic(boolean chronic) {
			this.chronic = chronic;
		}
	}
}
