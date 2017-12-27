package com.ebmi.std.pclaimevlist.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.pclaimevlist.dao.PClaimEvListDao;
import com.ebmi.std.pclaimevlist.dto.PClaimEvList;
import com.ebmi.std.pclaimevlist.dto.PClaimModel;
import com.ebmi.std.pclaimevlist.service.PClaimEvListService;

@Service("pClaimEvListService")
public class PClaimEvListServiceImpl implements PClaimEvListService {

	@Resource
	private PClaimEvListDao pClaimEvListDao;
	
	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;
	
	
	@Override
	public List<PClaimEvList> queryPMiClaimEvList(String p_mi_id,
			String ent_id, Integer stat_id, Integer days, Integer start,
			Integer count) throws Exception {
		Date crt_time = null;
		if (days != null) {
			Calendar cl = Calendar.getInstance();
			cl.add(Calendar.DATE, -days);
			crt_time = cl.getTime();
		}
		return pClaimEvListDao.queryPMiClaimEvList(p_mi_id, ent_id, stat_id,
				crt_time, start, count);
	}
	
	@Override
	public List<PClaimModel> queryPMiClaimEvListNew(String p_mi_id, String userName, String startDate, String endDate, String clmId){
		List<PClaimModel> lists = new ArrayList<PClaimModel>();
		try{
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", p_mi_id);//公民身份证号
			map.put("parm1", userName);//姓名
			map.put("parm2", "");//社会保障卡号
			map.put("parm3", startDate);//开始日期
			map.put("parm4", endDate);//结束日期
			
			//hsoft.yibao.demeinpayinfo.get(定点医疗机构端消费信息查询)
			TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.demeinpayinfo);
			if(res!=null && "ok".equals(res.getStatus())){
				List<Map<String, String>> lm = res.getData();
				if((lm!=null && !lm.isEmpty())){
					for (Map<String, String> map2 : lm) {
						PClaimModel pmi = new PClaimModel();
						//结算时间
						if(map2.get("col1").length() > 10){
							pmi.setClaim_date(map2.get("col1").substring(0, 10));
						}else{
							pmi.setClaim_date(map2.get("col1"));
						}
						pmi.setClaim_id(map2.get("col13"));//流水号
						pmi.setEnt_name(map2.get("col2"));//消费地点
						pmi.setMed_type_name(map2.get("col3"));//医疗类别
						pmi.setP_mi_id(map2.get("col0"));//个人编号
						pmi.setHospFee(map2.get("col4"));//就诊费用
						pmi.setOrg_code(map2.get("col12"));//定点医疗服务机构编号
						if(clmId!=null && clmId.length()>0){
							if(clmId.equals(map2.get("col13"))){
								lists.add(pmi);
								break;
							}
						}else{
							lists.add(pmi);
						}
					}
				}
			}
			
			//hsoft.yibao.miaacareinfo.get(医疗保险经办机构现金报销信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.miaacareinfo);
			if(res!=null && "ok".equals(res.getStatus())){
				List<Map<String, String>> lm = res.getData();
				if((lm!=null && !lm.isEmpty())){
				for (Map<String, String> map2 : lm) {
					PClaimModel pmi = new PClaimModel();
					//结算时间
					if(map2.get("col1").length() > 10){
						pmi.setClaim_date(map2.get("col1").substring(0, 10));
					}else{
						pmi.setClaim_date(map2.get("col1"));
					}
					pmi.setClaim_id(map2.get("col13"));//流水号
					pmi.setEnt_name(map2.get("col2"));//消费地点
					pmi.setMed_type_name(map2.get("col3"));//医疗类别
					pmi.setP_mi_id(map2.get("col0"));//个人编号
					pmi.setHospFee(map2.get("col4"));//就诊费用
					pmi.setOrg_code(map2.get("col12"));//定点医疗服务机构编号
					if(clmId!=null && clmId.length()>0){
						if(clmId.equals(map2.get("col13"))){
							lists.add(pmi);
							break;
						}
					}else{
						lists.add(pmi);
					}
				}
			}
		}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return lists;
	}
	
	@Override
	public void updatePMiClaimEvListState(List<PClaimEvList> list)
			throws Exception {
		List<Object[]> objectArrayList = null;
		if (list != null && list.size() > 0) {
			objectArrayList = new ArrayList<Object[]>(list.size());
			for (int i = 0; i < list.size(); i++) {
				PClaimEvList pClaimEvList = list.get(i);
				objectArrayList.add(new Object[] {
						pClaimEvList.getEv_list_stat_id(),
						pClaimEvList.getUpd_time(), pClaimEvList.getP_mi_id(),
						pClaimEvList.getClm_id() });
			}
			pClaimEvListDao.updatePMiClaimEvListState(objectArrayList);
		}

	}

	@Override
	public String queryPMiClaimEvListCount(String p_mi_id, Integer stat_id)
			throws Exception {
		return pClaimEvListDao.queryPMiClaimEvListCount(p_mi_id, stat_id);
	}

	@Override
	public void updatePMiClaimEvListState(PClaimEvList pClaimEvList)
			throws Exception {
		pClaimEvListDao.updatePMiClaimEvListState(pClaimEvList.getP_mi_id(),
				pClaimEvList.getClm_id(), pClaimEvList.getUpd_time(),
				pClaimEvList.getEv_list_stat_id());

	}

}