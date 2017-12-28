package com.ts.service.mts.mtscorpus.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.mts.MtsCorpus;
import com.ts.entity.mts.MtsCorpusDetail;
import com.ts.entity.mts.MtsCorpusEntity;
import com.ts.listener.RedisDataLoadListener;
import com.ts.service.mts.mtscorpus.MtsCorpusDetailManager;
import com.ts.service.mts.mtscorpus.MtsCorpusManager;
import com.ts.util.CommonUtils;
import com.ts.util.FileUtil;
import com.ts.util.PageData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 系统用户
 * 
 * @author 修改时间：2015.11.2
 */
@Service("mtsCorpusService")
public class MtsCorpusService implements MtsCorpusManager {
	private static Logger logger = Logger.getLogger(MtsCorpusService.class);
	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	@Resource(name = "mtsCorpusDetailService")
	private MtsCorpusDetailManager mtsCorpusDetailManager;
	
	private static  String  uploadNLPCorpus ;
	private static  String  traningNLPCorpus ;
	
	static{
		uploadNLPCorpus = CommonUtils.getPropValue("nlp.properties", "uploadNLPCorpus");
		traningNLPCorpus = CommonUtils.getPropValue("nlp.properties", "traningNLPCorpus");
	}

	/**
	 * 
	 * @方法名称: saveMtsCorpus
	 * @功能描述: 添加语料标识出来的实体
	 * @作者:李巍
	 * @创建时间:2017年4月18日 上午8:43:59
	 * @param myObj
	 * @throws Exception
	 * @see com.ts.service.mts.mtscorpus.MtsCorpusManager#saveMtsCorpus(net.sf.json.JSONObject)
	 */
	@Override
	public void saveMtsCorpus(JSONObject myObj) throws Exception {
		JSONArray jsonarray = myObj.getJSONArray("listDetail");
		MtsCorpus mtsCorpus = new MtsCorpus();
		mtsCorpus.setORIG_CONTENT(myObj.getString("orig_content"));
		mtsCorpus.setNEW_CONTENT(myObj.getString("new_content"));
		mtsCorpus.setFILE_FULL_NAME(myObj.getString("fileFullName"));
		mtsCorpus.setFILE_NAME(myObj.getString("fileName"));
		mtsCorpus.setEDIT_DATE(new Date());
		int corpusCount = (Integer) dao.save("MtsCorpusMapper.saveMtsCorpus", mtsCorpus);
		if (corpusCount > 0) {
			for (int i = 0; i < jsonarray.size(); i++) {
				JSONObject jobj = (JSONObject) jsonarray.get(i);
				MtsCorpusDetail corpusDetail = new MtsCorpusDetail();
				corpusDetail.setORIG_CORPUS(jobj.getString("origCorpus").split("_")[1]);
				corpusDetail.setCORPUS_ID(mtsCorpus.getID());
				corpusDetail.setP_ID(jobj.getString("pid"));
				corpusDetail.setP_HTML(jobj.getString("phtml"));
				// 语料来源 1、人工语料工具标注 2、AI更新语料
				corpusDetail.setTYPE("1");
				corpusDetail.setEDIT_DATE(new Date());
				// ==================添加原始语料===========================
				int corpusDetailCount = (Integer) dao.save("MtsCorpusDetailMapper.saveMtsCorpusDetail", corpusDetail);
				if (corpusDetailCount > 0) {
					JSONArray valueStr = jobj.getJSONArray("entityList");
					for (int j = 0; j < valueStr.size(); j++) {
						JSONObject vobj = (JSONObject) valueStr.get(j);
						MtsCorpusEntity corpusEntity = new MtsCorpusEntity();
						corpusEntity.setCORPUS_DETAIL_ID(corpusDetail.getID());
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
			}
		}
		System.out.println("添加成功");
	}

	/**
	 * 
	 * @方法名称: editMtsCorpus
	 * @功能描述: 更新语料
	 * @作者:李巍
	 * @创建时间:2017年4月21日 上午10:20:21
	 * @param myObj
	 * @param mtsCorpusId
	 * @throws Exception
	 * @see com.ts.service.mts.mtscorpus.MtsCorpusManager#editMtsCorpus(net.sf.json.JSONObject,
	 *      java.lang.String)
	 */
	@Override
	public String editMtsCorpus(JSONObject myObj, String mtsCorpusId) throws Exception {
		int mtsCorpusEntityCount = 0;
		int mtsCorpusDetialCount = 0;
		// //删除子孙表所有数据，重新加入
		List<MtsCorpusDetail> detailList = (List<MtsCorpusDetail>) dao
				.findForList("MtsCorpusDetailMapper.listMtsCorpusDetailByCorpus", Integer.parseInt(mtsCorpusId));
		if (detailList != null && detailList.size() > 0) {
			for (MtsCorpusDetail mtsCorpusDetail : detailList) {
				mtsCorpusEntityCount = (Integer) dao.delete("MtsCorpusEntityMapper.delMtsCorpusEntity",
						mtsCorpusDetail.getID());
				// 孙表 删除成功，在删除子表
			}
			if (mtsCorpusEntityCount > 0) {
				mtsCorpusDetialCount = (Integer) dao.delete("MtsCorpusDetailMapper.delMtsCorpusDetail", mtsCorpusId);
			} else {
				return "failed";
			}
			if (mtsCorpusDetialCount > 0) {
				// ***************更新主表信息******************
				MtsCorpus mtsCorpus = this.findMtsCorpusById(mtsCorpusId);
				mtsCorpus.setNEW_CONTENT(myObj.getString("new_content"));
				// 更新时间
				mtsCorpus.setEDIT_DATE(new Date());
				// 保存主表
				int corpusCount = (Integer) dao.update("MtsCorpusMapper.updateMtsCorpus", mtsCorpus);
				// ***************更新主表信息******************
				if (corpusCount > 0) {
					JSONArray jsonarray = myObj.getJSONArray("listDetail");
					for (int i = 0; i < jsonarray.size(); i++) {
						JSONObject jobj = (JSONObject) jsonarray.get(i);
						MtsCorpusDetail corpusDetail = new MtsCorpusDetail();
						corpusDetail.setORIG_CORPUS(jobj.getString("origCorpus").split("_")[1]);
						corpusDetail.setCORPUS_ID(mtsCorpus.getID());
						corpusDetail.setP_ID(jobj.getString("pid"));
						corpusDetail.setP_HTML(jobj.getString("phtml"));
						// 语料来源 1、人工语料工具标注 2、AI更新语料
						corpusDetail.setTYPE("1");
						corpusDetail.setEDIT_DATE(new Date());
						// ==================添加原始语料===========================
						int corpusDetailCount = (Integer) dao.save("MtsCorpusDetailMapper.saveMtsCorpusDetail",
								corpusDetail);
						if (corpusDetailCount > 0) {
							JSONArray valueStr = jobj.getJSONArray("entityList");
							for (int j = 0; j < valueStr.size(); j++) {
								JSONObject vobj = (JSONObject) valueStr.get(j);
								MtsCorpusEntity corpusEntity = new MtsCorpusEntity();
								corpusEntity.setCORPUS_DETAIL_ID(corpusDetail.getID());
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
							return "failed";
						}
					}
				} else {
					return "failed";
				}
			} else {
				return "failed";
			}
		} else {
			return "failed";
		}
		return "success";
	}

	@Override
	public List<PageData> listMtsCorpus(PageData pd) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsCorpusMapper.listMtsCorpusPage", pd);
		return list;
	}

	/**
	 * 
	 * @方法名称: findCorpusTagById
	 * @功能描述: 查询单个实体
	 * @作者:李巍
	 * @创建时间:2017年2月21日 下午3:34:41
	 * @param id
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtscorpus.MtsCorpusManager#findCorpusTagById(java.lang.String)
	 */
	@Override
	public MtsCorpus findMtsCorpusById(String id) throws Exception {
		return (MtsCorpus) dao.findForObject("MtsCorpusMapper.findCorpusById", Integer.parseInt(id));
	}

	public static void main(String[] args) throws IOException {
		File dir = new File("C:/Users/heroliyaoa/Desktop/实体识别工具/全部原始语料/yl2");
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String strFileName = files[i].getAbsolutePath();
				byte[] bs = FileUtil.getContent(strFileName);
				String content = new String(bs);
				String[] strs = content.split("\\n");
				for (int j = 0; j < strs.length; j++) {
					String readLine = strs[j];
					// System.out.println(readLine);
					// System.out.println("\t\t\t".length());
					// System.out.println(readLine.indexOf("\t\t\t")+"======="+readLine.lastIndexOf("\t\t\t"));
					if (readLine.indexOf("\t\t\t") != readLine.lastIndexOf("\t\t\t")) {
						String entity = readLine.substring(readLine.lastIndexOf("\t\t\t") + 3, readLine.length());
						readLine = readLine.substring(0, readLine.lastIndexOf("\t\t\t"));
						// readLine =
						// readLine.substring(readLine.lastIndexOf("\t\t\t")+"\t\t\t".length(),
						// readLine.length());
						// readLine = readLine.replace("\t\t\t", "");
						System.out.println(readLine);
						System.out.println(entity);
					}
					// String[] lines = readLine.split("\\t\\t\\t");
				}
			}
		}
	}

	@Override
	public void importAllMtsCorpus(File dir) throws Exception {
		String TAB = "\t\t\t";
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String strFileName = files[i].getAbsolutePath();
				byte[] bs = FileUtil.getContent(strFileName);
				String content = new String(bs);
				String[] strs = content.split("\\n");
				// 主表
				MtsCorpus mtsCorpus = new MtsCorpus();
				mtsCorpus.setFILE_NAME(files[i].getName());
				mtsCorpus.setFILE_FULL_NAME(strFileName);
				mtsCorpus.setEDIT_DATE(new Date());
				mtsCorpus.setORIG_CONTENT(content);
				int corpusCount = (Integer) dao.save("MtsCorpusMapper.saveMtsCorpus", mtsCorpus);
				int count = 0;
				StringBuffer sbCorpus = new StringBuffer("");
				if (corpusCount > 0) {
					for (int j = 0; j < strs.length; j++) {
						MtsCorpusDetail mtsCorpusDetail = new MtsCorpusDetail();
						String pid = "p" + (count++);
						mtsCorpusDetail.setP_ID(pid);
						StringBuffer sb = new StringBuffer("<p id=\"" + pid + "\">");
						String readLine = strs[j];
						String[] lines = new String[2];
						String origCorpus = "";
						if (readLine.indexOf(TAB) != readLine.lastIndexOf(TAB)) {
							String corpusLine = readLine.substring(0, readLine.lastIndexOf("\t\t\t"));
							String entityLine = readLine.substring(readLine.lastIndexOf(TAB) + TAB.length(),
									readLine.length());
							lines[0] = corpusLine;
							lines[1] = entityLine;
						} else if (readLine.indexOf(TAB) == readLine.lastIndexOf(TAB)) {
							lines = readLine.split("\\t\\t\\t");
						}
						origCorpus = lines[0];
						mtsCorpusDetail.setORIG_CORPUS(origCorpus);
						mtsCorpusDetail.setEDIT_DATE(new Date());
						mtsCorpusDetail.setTYPE("1");
						mtsCorpusDetail.setCORPUS_ID(mtsCorpus.getID());
						int corpusDetailCount = (Integer) dao.save("MtsCorpusDetailMapper.saveMtsCorpusDetail",
								mtsCorpusDetail);
						if (corpusDetailCount > 0 && lines.length > 1) {
							String[] entityArray = lines[1].split("\\s\\t");
							if (entityArray.length > 0) {
								for (int k = 0; k < entityArray.length; k++) {
									MtsCorpusEntity mtsCorpusEntity = new MtsCorpusEntity();
									String[] entity = entityArray[k].split("\\t");
									String entityName = entity[0];
									String startOff = entity[1];
									String endOff = entity[2];
									String color = selectColor(entity[3]);
									mtsCorpusEntity.setENTITY_NAME(entityName);
									mtsCorpusEntity.setIS_CONFRIM("1");
									mtsCorpusEntity.setSTART_TEXT_OFF(startOff);
									mtsCorpusEntity.setEND_TEXT_OFF(endOff);
									mtsCorpusEntity.setCORPUS_DETAIL_ID(mtsCorpusDetail.getID());
									mtsCorpusEntity.setCOLOR(color);
									String entityTypeId = RedisDataLoadListener.enNameMap.get(entity[3]);
									mtsCorpusEntity.setENTITY_TYPE_ID(entityTypeId);
									mtsCorpusEntity.setENTITY_TYPE_NAME(entityTypeId);
									mtsCorpusEntity.setEDIT_DATE(new Date());
									mtsCorpusEntity.setSPAN_NUM(pid + "_" + startOff + "_" + endOff);
									dao.save("MtsCorpusEntityMapper.saveMtsCorpusEntity", mtsCorpusEntity);
									if (k == 0) {
										sb.append(origCorpus.substring(0, Integer.parseInt(startOff)));
										sb.append("<span id=\"" + pid + "_" + startOff + "_" + endOff
												+ "\" style=\"color: " + color + "\">" + entityName + "</span>");
									} else {
										String[] entityPre = entityArray[k - 1].split("\\t");
										String endOffPre = entityPre[2];
										try {
											sb.append(origCorpus.substring(Integer.parseInt(endOffPre),
													Integer.parseInt(startOff)));
										} catch (Exception e) {
											logger.error(origCorpus+"**********"+strFileName);
											e.printStackTrace();

										}
										sb.append("<span id=\"" + pid + "_" + startOff + "_" + endOff
												+ "\" style=\"color: " + color + ";padding-left: 10px;\">" + entityName
												+ "</span>");
										if (k == (entityArray.length - 1)) {
											sb.append(origCorpus.substring(Integer.parseInt(endOff),
													origCorpus.length()));
										}
									}
								}
							}
						}
						sb.append("</p>");
						mtsCorpusDetail = (MtsCorpusDetail) dao.findForObject(
								"MtsCorpusDetailMapper.findMtsCorpusDetailById", mtsCorpusDetail.getID());
						mtsCorpusDetail.setP_HTML(sb.toString());
						dao.update("MtsCorpusDetailMapper.updateMtsCorpusDetail", mtsCorpusDetail);
						sbCorpus.append(sb.toString());
					}
				}
				mtsCorpus = (MtsCorpus) dao.findForObject("MtsCorpusMapper.findCorpusById", mtsCorpus.getID());
				mtsCorpus.setNEW_CONTENT(sbCorpus.toString());
				dao.update("MtsCorpusMapper.updateMtsCorpus", mtsCorpus);
			}
		}
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

	/*
	 * @Override public List<MtsCorpusEntity> listMtsCorpusEntity(String id)
	 * throws Exception { List<MtsCorpusEntity> list = (List<MtsCorpusEntity>)
	 * dao.findForList("MtsCorpusMapper.listCorpusTagEntity", id); return list;
	 * }
	 */

	/**
	 * 
	 * @方法名称: listCorpusTagEntityById
	 * @功能描述: 通过主ID 查询 标注实体列表
	 * @作者:李巍
	 * @创建时间:2017年2月22日 下午2:39:11
	 * @param id
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.corpustag.MtsCorpusManager#listCorpusTagEntityById(java.lang.String)
	 */
	// @Override
	// public List<MtsCorpusEntity> listMtsCorpusEntityById(String id) throws
	// Exception {
	// List<MtsCorpusEntity> list = (List<MtsCorpusEntity>)
	// dao.findForList("MtsCorpusMapper.listCorpusTagEntity", id);
	// return list;
	// }

}
