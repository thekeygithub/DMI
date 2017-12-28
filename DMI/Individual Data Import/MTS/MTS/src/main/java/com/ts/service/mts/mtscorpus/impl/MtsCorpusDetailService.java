package com.ts.service.mts.mtscorpus.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsCorpus;
import com.ts.entity.mts.MtsCorpusDetail;
import com.ts.entity.mts.MtsCorpusEntity;
import com.ts.entity.mts.NlpTerm;
import com.ts.listener.RedisDataLoadListener;
import com.ts.service.mts.mtscorpus.MtsCorpusDetailManager;
import com.ts.util.PageData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 系统用户
 * 
 * @author 修改时间：2015.11.2
 */
@Service("mtsCorpusDetailService")
public class MtsCorpusDetailService implements MtsCorpusDetailManager {
	private static Logger logger = Logger.getLogger(MtsCorpusDetailService.class);
	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	@Override
	public Integer saveMtsCorpusDetail(MtsCorpusDetail mtsCorpusDetail) throws Exception {
		return (Integer) dao.save("MtsCorpusDetailMapper.saveMtsCorpusDetail", mtsCorpusDetail);
	}

	@Override
	public Integer updateMtsCorpusDetail(MtsCorpusDetail mtsCorpusDetail) throws Exception {
		return (Integer) dao.update("MtsCorpusDetailMapper.updateMtsCorpusDetail", mtsCorpusDetail);
	}

	@Override
	public List<MtsCorpusDetail> listMtsCorpusDetail() throws Exception {
		List<MtsCorpusDetail> list = (List<MtsCorpusDetail>) dao
				.findForList("MtsCorpusDetailMapper.listMtsCorpusDetail", null);
		return list;
	}

	@Override
	public int maxId() throws Exception {
		return (Integer) dao.findForObject("MtsCorpusDetailMapper.maxId", null);

	}

	@Override
	public List<MtsCorpusDetail> listMtsCorpusDetailByCorpus(Integer mtsCorpusId) throws Exception {
		List<MtsCorpusDetail> list = (List<MtsCorpusDetail>) dao
				.findForList("MtsCorpusDetailMapper.listMtsCorpusDetailByCorpus", mtsCorpusId);
		return list;
	}

	/**
	 * 
	 * @方法名称: listMtsCorpusDetailView
	 * @功能描述: 按语料模糊查询
	 * @作者:李巍
	 * @创建时间:2017年5月5日 上午10:16:38
	 * @param page
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtscorpus.MtsCorpusDetailManager#listMtsCorpusDetailView(com.ts.entity.Page)
	 */
	@Override
	public List<PageData> listMtsCorpusDetailView(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsCorpusDetailMapper.mtsCorpusDetailViewlistPage",
				page);
		return list;
	}

	@Override
	public List<PageData> listMtsCorpusView(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsCorpusDetailMapper.mtsCorpusViewlistPage", page);
		return list;
	}

	@Override
	public List<PageData> listMtsCorpusDetailViewForExcel(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao
				.findForList("MtsCorpusDetailMapper.mtsCorpusDetailViewlistPageForExcel", page);
		return list;
	}

	@Override
	public List<PageData> listMtsCorpusEntityViewForExcel(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao
				.findForList("MtsCorpusDetailMapper.mtsCorpusEntityViewlistPageForExcel", page);
		return list;
	}

	/**
	 * 
	 * @方法名称: listMtsCorpusEntityViewPage
	 * @功能描述: 按实体精确查询
	 * @作者:李巍
	 * @创建时间:2017年5月5日 上午10:16:48
	 * @param page
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtscorpus.MtsCorpusDetailManager#listMtsCorpusEntityViewPage(com.ts.entity.Page)
	 */
	@Override
	public List<PageData> listMtsCorpusEntityViewPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsCorpusDetailMapper.mtsCorpusEntityViewlistPage",
				page);
		return list;
	}

	@Override
	public MtsCorpusDetail findMtsCorpusDetailById(String detailId) throws Exception {
		return (MtsCorpusDetail) dao.findForObject("MtsCorpusDetailMapper.findMtsCorpusDetailById",
				Integer.parseInt(detailId));
	}

	/**
	 * 
	 * @方法名称: editAssertMtsCorpus
	 * @功能描述: 语料维护更新
	 * @作者:李巍
	 * @创建时间:2017年5月4日 上午9:29:18
	 * @param myObj
	 * @param mtsCorpusId
	 * @return
	 * @throws Exception
	 *             String
	 */
	@Override
	public String editAssertMtsCorpus(JSONObject myObj, String corpusDetailId) throws Exception {
		int mtsCorpusEntityCount = 0;
		int mtsCorpusDetialCount = 0;
		// //删除子孙表所有数据，重新加入
		mtsCorpusEntityCount = (Integer) dao.delete("MtsCorpusEntityMapper.delMtsCorpusEntity",
				Integer.parseInt(corpusDetailId));
		// 孙表 删除成功，在删除子表
		if (mtsCorpusEntityCount > 0) {
			String pid = myObj.getString("pid");
			String phtml = myObj.getString("phtml");
			MtsCorpusDetail mtsCorpusDetail = this.findMtsCorpusDetailById(corpusDetailId);
			mtsCorpusDetail.setEDIT_DATE(new Date());
			mtsCorpusDetail.setP_HTML(phtml);
			mtsCorpusDetialCount = this.updateMtsCorpusDetail(mtsCorpusDetail);
			if (mtsCorpusDetialCount > 0) {
				JSONArray myArray = myObj.getJSONArray("entityList");
				// ==================添加原始语料===========================
				for (int j = 0; j < myArray.size(); j++) {
					JSONObject vobj = (JSONObject) myArray.get(j);
					MtsCorpusEntity corpusEntity = new MtsCorpusEntity();
					corpusEntity.setCORPUS_DETAIL_ID(Integer.parseInt(corpusDetailId));
					corpusEntity.setENTITY_NAME(vobj.getString("entityName"));
					corpusEntity.setENTITY_TYPE_ID(vobj.getString("entityTypeId"));
					corpusEntity.setENTITY_TYPE_NAME(vobj.getString("entityTypeName"));
					corpusEntity.setSTART_TEXT_OFF(vobj.getString("startTextOff"));
					corpusEntity.setEND_TEXT_OFF(vobj.getString("endTextOff"));
					corpusEntity.setIS_CONFRIM(vobj.getString("isNotConfirm"));
					corpusEntity.setSPAN_NUM(vobj.getString("spanNum"));
					corpusEntity.setEDIT_DATE(new Date());
					corpusEntity.setCOLOR(vobj.getString("color"));
					// ==================添加实体===========================
					dao.save("MtsCorpusEntityMapper.saveMtsCorpusEntity", corpusEntity);
				}
			}
		} else {
			return "failed";
		}
		return "success";
	}

	/**
	 * 
	 * @方法名称: saveAllMtsCorpus
	 * @功能描述: 导入全部原始语料
	 * @作者:李巍
	 * @创建时间:2017年5月8日 下午4:32:21
	 * @param myObj
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtscorpus.MtsCorpusDetailManager#saveAllMtsCorpus(net.sf.json.JSONObject)
	 */
	@Override
	public String saveAllMtsCorpus(JSONObject myObj) throws Exception {
		int mtsCorpusDetialCount = 0;
		String pid = myObj.getString("pid");
		String phtml = myObj.getString("phtml");
		String origCorpus = myObj.getString("origCorpus");
		MtsCorpusDetail mtsCorpusDetail = new MtsCorpusDetail();
		mtsCorpusDetail.setEDIT_DATE(new Date());
		mtsCorpusDetail.setP_HTML(phtml);
		mtsCorpusDetail.setP_ID(pid);
		mtsCorpusDetail.setORIG_CORPUS(origCorpus);
		mtsCorpusDetail.setTYPE("1");
		mtsCorpusDetialCount = this.saveMtsCorpusDetail(mtsCorpusDetail);
		if (mtsCorpusDetialCount > 0 && myObj.containsKey("entityList")) {
			JSONArray myArray = myObj.getJSONArray("entityList");
			// ==================添加原始语料===========================
			for (int j = 0; j < myArray.size(); j++) {
				JSONObject vobj = (JSONObject) myArray.get(j);
				MtsCorpusEntity corpusEntity = new MtsCorpusEntity();
				corpusEntity.setCORPUS_DETAIL_ID(mtsCorpusDetail.getID());
				corpusEntity.setENTITY_NAME(vobj.getString("entityName"));
				corpusEntity.setENTITY_TYPE_ID(vobj.getString("entityTypeId"));
				corpusEntity.setENTITY_TYPE_NAME(vobj.getString("entityTypeName"));
				corpusEntity.setSTART_TEXT_OFF(vobj.getString("startTextOff"));
				corpusEntity.setEND_TEXT_OFF(vobj.getString("endTextOff"));
				corpusEntity.setIS_CONFRIM(vobj.getString("isNotConfirm"));
				corpusEntity.setSPAN_NUM(vobj.getString("spanNum"));
				corpusEntity.setEDIT_DATE(new Date());
				corpusEntity.setCOLOR(vobj.getString("color"));
				// ==================添加实体===========================
				dao.save("MtsCorpusEntityMapper.saveMtsCorpusEntity", corpusEntity);
			}
		} else {
			return "fail";
		}
		return "success";
	}
	
	 /**
    *
    * @param a
    *            被匹配的长字符串
    * @param b
    *            匹配的短字符串
    * @return 匹配次数
    */
   public int hit(String a, String b) {
       if (a.length() < b.length()) {
           return 0;
       }
       char[] a_t = a.toCharArray();
       int count = 0;

       for (int i = 0; i < a.length() - b.length(); i++) {
           StringBuffer buffer = new StringBuffer();
           for (int j = 0; j < b.length(); j++) {
               buffer.append(a_t[i + j]);
           }
           if(buffer.toString().equals(b)){
               count ++;
           }
       }

       return count;
   }

	@Override
	public void importCorpusExcel(List<PageData> listPd, String filePath, String fileName) throws Exception {
		// 主表
		MtsCorpus mtsCorpus = new MtsCorpus();
		mtsCorpus.setFILE_NAME(fileName);
		mtsCorpus.setFILE_FULL_NAME(filePath + fileName);
		mtsCorpus.setEDIT_DATE(new Date());
		mtsCorpus.setORIG_CONTENT("");
		int corpusCount = (Integer) dao.save("MtsCorpusMapper.saveMtsCorpus", mtsCorpus);
		// 新语料串儿
		StringBuffer sbCorpus = new StringBuffer("");
		if (corpusCount > 0) {
			for (int i = 0; i < listPd.size(); i++) {
				String origCorpus = listPd.get(i).getString("var0");
				String ENTITY_STR = listPd.get(i).getString("var1");
				int count = 0;
				MtsCorpusDetail mtsCorpusDetail = new MtsCorpusDetail();
				String pid = "p" + (count++);
				mtsCorpusDetail.setP_ID(pid);
				StringBuffer sb = new StringBuffer("<p id=\"" + pid + "\">");
				mtsCorpusDetail.setORIG_CORPUS(origCorpus);
				mtsCorpusDetail.setEDIT_DATE(new Date());
				mtsCorpusDetail.setTYPE("1");
				mtsCorpusDetail.setCORPUS_ID(mtsCorpus.getID());
				int corpusDetailCount = (Integer) dao.save("MtsCorpusDetailMapper.saveMtsCorpusDetail",
						mtsCorpusDetail);
				if (corpusDetailCount > 0) {
					// 实体列表
					String[] entityArray = ENTITY_STR.split("\\n");
					if (entityArray.length > 0) {
						Map<Integer, Integer> entityOffMap = new HashMap<Integer, Integer>();
						Map<String,String> entityRepeatOffMap = new HashMap<String,String>();
						//统计实体名称出现的次数 ，并加入缓存
						for (int k = 0; k < entityArray.length; k++) {
							String entityName = entityArray[k].substring(0, entityArray[k].indexOf("【"));
							if(hit(origCorpus,entityName) > 1){
								if(entityRepeatOffMap.get(entityName) != null){
									String value = entityRepeatOffMap.get(entityName);
									entityRepeatOffMap.put(entityName, value+"_"+k);
								}else{
									entityRepeatOffMap.put(entityName, k+"");
								}
							}
						}
						for (int k = 0; k < entityArray.length; k++) {
							MtsCorpusEntity mtsCorpusEntity = new MtsCorpusEntity();
							String entityName = entityArray[k].substring(0, entityArray[k].indexOf("【"));
							String cnName = entityArray[k].substring(entityArray[k].indexOf("【") + 1,entityArray[k].indexOf("】"));
							
							Map<String, Integer> map = getIndexOff(entityName, origCorpus,entityRepeatOffMap,k);
							
							Integer startOff = map.get("startOff");
							Integer endOff = map.get("endOff");
							// 将 实体名称 --> 索引位置 缓存
							entityOffMap.put(k, endOff);
							NlpTerm nlpTerm = (NlpTerm) dao.findForObject("TermTypeMapper.findNlpTermByName", cnName);
							String color = selectColor(nlpTerm.getTERM_EN_NAME());
							mtsCorpusEntity.setENTITY_NAME(entityName);
							mtsCorpusEntity.setIS_CONFRIM("1");
							mtsCorpusEntity.setSTART_TEXT_OFF(String.valueOf(startOff));
							mtsCorpusEntity.setEND_TEXT_OFF(String.valueOf(endOff));
							mtsCorpusEntity.setCORPUS_DETAIL_ID(mtsCorpusDetail.getID());
							mtsCorpusEntity.setCOLOR(color);
							String entityTypeId = RedisDataLoadListener.enNameMap.get(nlpTerm.getTERM_EN_NAME());
							mtsCorpusEntity.setENTITY_TYPE_ID(entityTypeId);
							mtsCorpusEntity.setENTITY_TYPE_NAME(entityTypeId);
							mtsCorpusEntity.setEDIT_DATE(new Date());
							mtsCorpusEntity.setSPAN_NUM(pid + "_" + startOff + "_" + endOff);
							dao.save("MtsCorpusEntityMapper.saveMtsCorpusEntity", mtsCorpusEntity);
							if (k == 0) {
								sb.append(origCorpus.substring(0, startOff));
								sb.append("<span id=\"" + pid + "_" + startOff + "_" + endOff + "\" style=\"color: "
										+ color + "\">" + entityName + "</span>");
							} else {
								Integer endOffPre = entityOffMap.get(k - 1);
								logger.info(origCorpus + "****上一个位置结束**" + endOffPre + "**下一个位置开始" + startOff);
								sb.append(origCorpus.substring(endOffPre, startOff));
								sb.append("<span id=\"" + pid + "_" + startOff + "_" + endOff + "\" style=\"color: "
										+ color + ";padding-left: 10px;\">" + entityName + "</span>");
								if (k == (entityArray.length - 1)) {
									sb.append(origCorpus.substring(endOff, origCorpus.length()));
								} 
							}
						}
					}
				}
				sb.append("</p>");
				mtsCorpusDetail = (MtsCorpusDetail) dao.findForObject("MtsCorpusDetailMapper.findMtsCorpusDetailById",
						mtsCorpusDetail.getID());
				mtsCorpusDetail.setP_HTML(sb.toString());
				dao.update("MtsCorpusDetailMapper.updateMtsCorpusDetail", mtsCorpusDetail);
				sbCorpus.append(sb.toString());
			}
			mtsCorpus = (MtsCorpus) dao.findForObject("MtsCorpusMapper.findCorpusById", mtsCorpus.getID());
			mtsCorpus.setNEW_CONTENT(sbCorpus.toString());
			dao.update("MtsCorpusMapper.updateMtsCorpus", mtsCorpus);
		}

	}

	private Map<String, Integer> getIndexOff(String entityName, String origCorpus,Map<String,String> entityRepeatOffMap,int k) {
		Map<String, Integer> indexMap = new HashMap<String, Integer>();
		int index = origCorpus.indexOf(entityName);
		if (origCorpus.indexOf(entityName) != -1 ) {
			if(entityRepeatOffMap.get(entityName) == null){//没有重复情况
					int startOff = index;
					int endOff = index + entityName.length();
					indexMap.put("startOff", startOff);
					indexMap.put("endOff", endOff);
			}else{
				String temp = entityRepeatOffMap.get(entityName);
				if(temp.indexOf("_") != -1){
					String[] offs = temp.split("_");
					//现在只能假设一个词在原始语料中至多 重复两次,如果在多，需另外给出解决方案
					if(k == Integer.parseInt(offs[0])){//如果是第一个，那么取indexOf 
						int startOff = index;
						int endOff = startOff + entityName.length();
						indexMap.put("startOff", startOff);
						indexMap.put("endOff", endOff);
					}else if(k == Integer.parseInt(offs[1])){//如果是第二个，那么取lastIndexOf 
						int startOff = origCorpus.lastIndexOf(entityName);
						int endOff = startOff + entityName.length();
						indexMap.put("startOff", startOff);
						indexMap.put("endOff", endOff);
					}
				}
				logger.info("实体名称*****" + entityName + "****在****" + origCorpus + "****出现次数多余一次");
			}
		}
		return indexMap;
	}

	private String selectColor(String name) {
		String color = "";
		switch (name) {
		case "disease":
			color = "#FF0000";
			break;
		case "symptom":
			color = "##0000CC";
			break;
		case "treatment":
			color = "#993300";
			break;
		case "diagnosis_name":
			color = "#FF6600";
			break;
		case "instrument":
			color = "#00FFFF";
			break;
		case "other_diagnosis":
			color = "#FF00FF";
			break;
		case "medicine_cn":
			color = "#FF8000";
			break;
		case "medicine_pn":
			color = "#FF8000";
			break;
		case "medicine_mn":
			color = "#FF8000";
			break;
		case "medicine":
			color = "#C0C0C0";
			break;
		case "dosage_form":
			color = "#FF6699";
			break;
		case "specifications":
			color = "#CC00FF";
			break;
		case "packing_spe":
			color = "#FFCC00";
			break;
		case "packing_material":
			color = "#FF6633";
			break;
		case "enterprise":
			color = "#FF0080";
			break;
		case "department":
			color = "#008040";
			break;
		case "address":
			color = "#00FF40";
			break;
		default:
			break;
		}
		return color;
	}

}
