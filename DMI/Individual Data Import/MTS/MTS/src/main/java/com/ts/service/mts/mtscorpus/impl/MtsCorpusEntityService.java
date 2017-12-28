package com.ts.service.mts.mtscorpus.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.mts.MtsCorpus;
import com.ts.entity.mts.MtsCorpusDetail;
import com.ts.entity.mts.MtsCorpusEntity;
import com.ts.service.mts.mtscorpus.MtsCorpusDetailManager;
import com.ts.service.mts.mtscorpus.MtsCorpusEntityManager;
import com.ts.service.mts.mtscorpus.MtsCorpusManager;
import com.ts.util.PageData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 系统用户
 * 
 * @author 修改时间：2015.11.2
 */
@Service("mtsCorpusEntityService")
public class MtsCorpusEntityService implements MtsCorpusEntityManager {

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	@Override
	public void saveMtsCorpusEntity(MtsCorpusEntity mtsCorpusEntity) throws Exception {
		dao.save("MtsCorpusEntityMapper.saveMtsCorpusEntity", mtsCorpusEntity);
		System.out.println("1111");
	}
	
	@Override
	public List<MtsCorpusEntity> listMtsCorpusEntity(Integer id) throws Exception {
		List<MtsCorpusEntity> list = (List<MtsCorpusEntity>) dao.findForList("MtsCorpusEntityMapper.listMtsCorpusEntity",id);
		return list;
	}

	/**
	 * 
	 * @方法名称: listMtsCorpusEntityByMtsCorpus
	 * @功能描述: 通过语料ID 查询 所有的实体数据
	 * @作者:李巍
	 * @创建时间:2017年4月20日 上午10:51:26
	 * @param mtsCorpustId
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtscorpus.MtsCorpusEntityManager#listMtsCorpusEntityByMtsCorpus(java.lang.String)
	 */
	@Override
	public List<PageData> listMtsCorpusEntityByMtsCorpus(String mtsCorpustId) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsCorpusEntityMapper.listMtsCorpusEntityByMtsCorpus",mtsCorpustId);
		return list;
	}
	
	
	/**
	 * 
	 * @方法名称: listMtsCorpusEntityByMtsCorpusDetail
	 * @功能描述: 根据明细ID 查询明细列表
	 * @作者:李巍
	 * @创建时间:2017年5月3日 下午12:50:13
	 * @param detailId
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtscorpus.MtsCorpusEntityManager#listMtsCorpusEntityByMtsCorpusDetail(java.lang.String)
	 */
	@Override
	public List<PageData> listMtsCorpusEntityByMtsCorpusDetail(String detailId) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsCorpusEntityMapper.listMtsCorpusEntityByMtsCorpusDetail",detailId);
		return list;
	}
	
	
//	@Override
//	public void editMtsCorpus(JSONObject myObj, String id) throws Exception {
			//先保存主表
//		MtsCorpus corpusTag = this.findCorpusTagById(id);
//		corpusTag.setNEW_CONTENT(myObj.getString("new_html"));
//		//更新时间
//		corpusTag.setEDIT_DATE(new Date());
//		dao.update("MtsCorpusTagMapper.updateMtsCorpusTag", corpusTag);
//		//保存从表
//		String arrayStr = myObj.getString("entityArray");
//		JSONArray myArray = JSONArray.fromObject(arrayStr);
//		
//		//删除从表所有数据，重新加入	
//		dao.delete("MtsCorpusTagMapper.delMtsCorpusTagEntity", id);
//		//保存从表
//		for (int i = 0; i < myArray.size(); i++) {
//			JSONObject object = myArray.getJSONObject(i);
//			MtsCorpusEntity corpusEntity = new MtsCorpusEntity();
//			//后期改为序列
//			corpusTagEntity.setID(UUID.randomUUID().toString());
//			String entityName = object.getString("entityName").replaceAll("\n", "").replaceAll("\t", "");
//			corpusTagEntity.setENTITY_NAME(entityName);
//			corpusTagEntity.setENTITY_TYPE_ID(object.getString("entityTypeId"));
//			corpusTagEntity.setIS_CONFRIM(object.getString("isNotConfirm"));
//			corpusTagEntity.setENTITY_TYPE_NAME(object.getString("entityTypeName"));
//			corpusTagEntity.setSTART_TEXT_OFF(object.getString("startTextOff"));
//			corpusTagEntity.setEND_TEXT_OFF(object.getString("endTextOff"));
//			corpusTagEntity.setSPAN_NUM(object.getString("spanNum"));
//			//保存主表ID
//			corpusTagEntity.setCORPUS_ID(id);
//			dao.save("MtsCorpusTagMapper.saveCorpusTagEntity", corpusTagEntity);
//		}
//	}

//	@Override
//	public List<PageData> listMtsCorpus(PageData pd) throws Exception {
//		List<PageData> list = (List<PageData>) dao.findForList("MtsCorpusMapper.listMtsCorpusPage", pd);
//		return list;
//	}

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
//	@Override
//	public MtsCorpus findMtsCorpusById(String id) throws Exception {
//		return (MtsCorpus) dao.findForObject("MtsCorpusMapper.findCorpusById", id);
//	}

/*	@Override
	public List<MtsCorpusEntity> listMtsCorpusEntity(String id) throws Exception {
		List<MtsCorpusEntity> list = (List<MtsCorpusEntity>) dao.findForList("MtsCorpusTagMapper.listCorpusTagEntity", id);
		return list;
	}*/

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
//	@Override
//	public List<MtsCorpusEntity> listMtsCorpusEntityById(String id) throws Exception {
//		List<MtsCorpusEntity> list = (List<MtsCorpusEntity>) dao.findForList("MtsCorpusTagMapper.listCorpusTagEntity", id);
//		return list;
//	}
}
