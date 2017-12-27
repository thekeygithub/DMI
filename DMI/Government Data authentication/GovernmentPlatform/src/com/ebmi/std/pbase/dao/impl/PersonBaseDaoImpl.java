package com.ebmi.std.pbase.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.core.utils.DateUtils;
import com.ebmi.jms.entity.ViewDwjbxx;
import com.ebmi.jms.entity.ViewGrjbxx;
import com.ebmi.jms.entity.ViewGrjbxxGs;
import com.ebmi.jms.entity.ViewGrjbxxSy;
import com.ebmi.std.common.dao.impl.BaseDao;
import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.pbase.cond.PMiBaseInfoCond;
import com.ebmi.std.pbase.dao.PersonBaseDao;
import com.ebmi.std.pbase.dto.PMiBaseInfo;
import com.ebmi.std.util.EncodingUtils;

@Service("personBaseDaoImpl")
public class PersonBaseDaoImpl extends BaseDao implements PersonBaseDao {
	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;
	
	
	@Override
	public PMiBaseInfo queryInsuranceinfo(PMiBaseInfoCond cond) throws Exception {
		PMiBaseInfo pmi = new PMiBaseInfo();
		if(cond != null){
			TheKeyResultEntity res = null;
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getIdcard());//公民身份证号
			map.put("parm1", cond.getUsername());//姓名
			map.put("parm2", cond.getSs_num());//社会保障卡号s
			map.put("parm3", "3");
			//map.put("parm3", cond.getSi_type());//指定传入参数3 ，医疗保险； [险种  1养老保险 2失业保险 3医疗保险 4工伤保险 5生育保险]
			res =baseApi.getDataInfo(map, BaseEntity.pinsuinfo);//hsoft.yibao.pinsuinfo.get(个人参保信息查询)
			if(res!=null){
				if("ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
					if((!lm.isEmpty()) ){//有元素的时候
						for (Map<String, String> map2 : lm) {
                            pmi.setRegn_tc_name(map2.get("col7"));//统筹区域 暂未确定 
							pmi.setP_corp(map2.get("col1"));//参保单位==单位名称
							pmi.setP_cb_date(StringUtils.isNotBlank(map2.get("col3"))?DateUtils.std(map2.get("col3"), 1):null);//参保日期
							pmi.setP_cb_stat(map2.get("col4"));//参保状态
							
							String code = map2.get("col9");
							String codeName = map2.get("col2");
							System.out.println("code>>>>>"+code+" & codeName=="+codeName);
							//根据  接口返回的  col2   职工医疗保险 险种   col9   险种代码  判断是否是居民
							//"col2":"居民医疗保险" "col9":"23" 为居民
							break;
						}
					}
				}
			}
			
			map.put("parm3", "1");//parm3 String 必须 状态 1工作表 2历史表 cond.getTable_stat()  
			//在职 灵活就业的人员   调用此接口  ///hsoft.yibao.pmediinfo.get(医疗个人参保信息查询) 直接查询 1工作表 
			///hsoft.yibao.pmediinfo.get(医疗个人参保信息查询) 在职 灵活就业的调用此接口 
			res = baseApi.getDataInfo(map, BaseEntity.pmediinfo);
			if(res!=null){
				if("ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
					if((!lm.isEmpty()) ){//有元素的时候
						for (Map<String, String> map2 : lm) {
							pmi.setP_si_cat_name(map2.get("col1"));//待遇类别
						}
					}
				}
			}
			
			// 工伤
			if("4".equals(cond.getSi_type())){
				map.put("parm3", "4");
				res = baseApi.getDataInfo(map, BaseEntity.pinsuinfo);
				if(res != null){
					if("ok".equals(res.getStatus())){
						List<Map<String, String>> lm = res.getData();
						if((!lm.isEmpty()) ){//有元素的时候
							for (Map<String, String> map2 : lm) {
								ViewGrjbxxGs gs = new ViewGrjbxxGs() ;
								gs.setCbzt(map2.get("col6"));//待遇类别
								gs.setScjfrq(DateUtils.stsd(map2.get("col3"), 1));//时间
								gs.setJfzt(map2.get("col4"));//缴费状态
								pmi.setGsbx(gs);
							}
						}
					}
				}
			}
			
			// 生育
			if("5".equals(cond.getSi_type())){
				map.put("parm3", "5");
				res = baseApi.getDataInfo(map, BaseEntity.pinsuinfo);
				if(res != null){
					if("ok".equals(res.getStatus())){
						List<Map<String, String>> lm = res.getData();
						if((!lm.isEmpty()) ){//有元素的时候
							for (Map<String, String> map2 : lm) {
								ViewGrjbxxGs sy = new ViewGrjbxxGs() ;
								sy.setJflx(map2.get("col6"));//待遇类别
								sy.setScjfrq(DateUtils.stsd(map2.get("col3"), 1));//时间
								sy.setJfzt(map2.get("col4"));//缴费状态
								pmi.setSybx(sy);
							}
						}
					}
				}
			}
	
		}
		return pmi;
	
	}

	
	@Override
	public PMiBaseInfo query(PMiBaseInfoCond cond) throws Exception {
		PMiBaseInfo pmi = new PMiBaseInfo();
		if(cond != null){
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getIdcard());//公民身份证号
			map.put("parm1", cond.getUsername());//姓名
			map.put("parm2", cond.getSs_num());//社会保障卡号
//			TheKeyResultEntity res = PbaseinfoApi.getPbaseinfo(map);
			TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.pbaseinfo);//hsoft.yibao.pbaseinfo.get(个人基本信息查询 )
			
			if(res!=null){
				if("ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
					if((!lm.isEmpty()) ){//有元素的时候
						for (Map<String, String> map2 : lm) {
							//基本信息查询–用户简要信息 
//							image_id	显示用户参保人头像id
							pmi.setP_name(map2.get("col1"));//p_name	显示用户姓名
							pmi.setP_cert_no(map2.get("col2"));//p_cert_no	显示用户社会保障号码，即身份证号，后四位为暗码，据各地情况而定
							//hsoft.general.user.register.step4  注册时候输入项 
							pmi.setSi_card_no(map2.get("col30"));//si_card_no	显示社会保障卡号。9位，中间四位为暗码，依据各地情况而定
							//基本信息查询–用户简要信息 end
							
							//基本信息查询–用户详细信息 P_GEND_ID 1 男 2 女
							pmi.setP_gend_name(map2.get("col4"));//性别
							pmi.setP_ethnic_ame(map2.get("col5"));//民族
							pmi.setP_edu(map2.get("col9"));//文化程度
							pmi.setP_birth(StringUtils.isNotBlank(map2.get("col6"))?DateUtils.std(map2.get("col6"), 1):null);//出生日期
							pmi.setP_identity(map2.get("col10"));//个人身份
							pmi.setPhone(map2.get("col20"));//参保人电话，参保人手机
							pmi.setP_addr(map2.get("col22"));//居住地址，也可以称为常驻地址
							pmi.setAddr_post_code(map2.get("col21"));//居住地邮编
							pmi.setP_num(map2.get("col0"));// 个人编号
							//基本信息查询–用户详细信息 end
							
							return pmi;
						}
					}
				}
				
			}
		}
		return pmi;
		
		
//		StringBuffer sql = new StringBuffer();
//		sql.append(
//				"select trim(grbh) p_mi_id, trim(xb) p_gend_id, trim(ickh) si_card_no, trim(mz) p_ethnic_id, trim(rylb) p_si_cat_id, trim(xm) p_name, trim(sfzh) p_cert_no, trim(rylb) retire_flag, trim(c.dwmc) p_corp, '' phone, '' hukou_addr, '' hukou_post_code, '' email, trim(b.mc) nationality, csrq p_birth, trim(jzdz) p_addr, trim(yzbm) addr_post_code, trim(sjhm) mobile, cjgzrq work_time, trim(whcd) edu_lvl_id, trim(tcqbm) regn_tc_id from view_grjbxx a left join view_gj b on a.gj=b.gj left join view_dwjbxx c on a.dwbh = c.dwbh where 1 = 1 ");
//		List<Object> args = new ArrayList<Object>();
//		if (cond != null) {
//			if (cond.getP_mi_id() != null && !"".equals(cond.getP_mi_id())) {
//				sql.append(" and grbh=?");
//				args.add(cond.getP_mi_id());
//			}
//			if (cond.getSi_card_no() != null && !"".equals(cond.getSi_card_no())) {
//				sql.append(" and ickh=?");
//				args.add(cond.getSi_card_no());
//			}
//		}
//		List<PMiBaseInfo> result = getJdbcServiceMI().findListByQueryMapper(sql.toString(), args.toArray(),
//				PMiBaseInfo.class);
//		if (result != null && !result.isEmpty()) {
//			PMiBaseInfo pmi = result.get(0);
//			pmi.setP_name(EncodingUtils.transCP1252ToGBK(pmi.getP_name()));
//			pmi.setNationality(EncodingUtils.transCP1252ToGBK(pmi.getNationality()));
//			pmi.setP_corp(EncodingUtils.transCP1252ToGBK(pmi.getP_corp()));
//			pmi.setP_addr(EncodingUtils.transCP1252ToGBK(pmi.getP_addr()));
//			pmi.setHukou_addr(EncodingUtils.transCP1252ToGBK(pmi.getHukou_addr()));
//
//			return pmi;
//		} else
//			return null;
	}

	@Override
	public ViewGrjbxx queryGrjbxx(String grbh) throws Exception {
		return getJdbcServiceMI().queryForObject("select * from view_grjbxx where grbh=trim(?)",
				new Object[] { grbh }, ViewGrjbxx.class);
	}

	@Override
	public ViewDwjbxx queryDwjbxx(String dwbh) throws Exception {
		ViewDwjbxx dw = getJdbcServiceMI().queryForObject("select * from view_dwjbxx where dwbh=trim(?)",
				new Object[] { dwbh }, ViewDwjbxx.class);
		dw.setDwmc(EncodingUtils.transCP1252ToGBK(dw.getDwmc()));
		return dw;
	}

	@Override
	public ViewGrjbxxGs queryGrjbxxGs(String grbh) throws Exception {
		return getJdbcServiceMI().queryForObject(
				"select * from view_grjbxx_gs a,view_grjbxx b where a.shbzh=b.shbzh and b.grbh=?",
				new Object[] { grbh }, ViewGrjbxxGs.class);
	}

	@Override
	public ViewGrjbxxSy queryGrjbxxSy(String grbh) throws Exception {
		return getJdbcServiceMI().queryForObject(
				"select * from view_grjbxx_sy a,view_grjbxx b where a.shbzh=b.shbzh and b.grbh=?",
				new Object[] { grbh }, ViewGrjbxxSy.class);
	}

}
