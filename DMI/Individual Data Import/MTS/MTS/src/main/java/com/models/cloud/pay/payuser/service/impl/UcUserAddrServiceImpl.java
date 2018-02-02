package com.models.cloud.pay.payuser.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.payuser.dao.UcUserAddrMapper;
import com.models.cloud.pay.payuser.entity.PayUser;
import com.models.cloud.pay.payuser.entity.UcUserAddr;
import com.models.cloud.pay.payuser.service.PayUserService;
import com.models.cloud.pay.payuser.service.UcUserAddrService;
import com.models.cloud.util.hint.Hint;

@Service("ucUserAddrServiceImpl")
public class UcUserAddrServiceImpl implements UcUserAddrService{
	
	private static final Logger logger = Logger.getLogger(UcUserAddrServiceImpl.class);
	
	@Resource(name="payUserServiceImpl")
	private PayUserService payUserService;
	
	@Autowired
	private UcUserAddrMapper ucUserAddrMapper;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> saveUcUserAddr(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(logger.isInfoEnabled()){
			logger.info("请求用户中心-添加地址，请求参数："+receiveMap);
		}
		String payUserId=receiveMap.get("userId").toString();
		String ucAddrId = String.valueOf(receiveMap.get("ucAddrId")==null?"":receiveMap.get("ucAddrId")).trim();//地址id
		String provId=String.valueOf(receiveMap.get("provId")).trim().replace("", "");
		String provName=String.valueOf(receiveMap.get("provName")).trim().replace("", "");
		String cityId=String.valueOf(receiveMap.get("cityId")).trim().replace("", "");
		String cityName=String.valueOf(receiveMap.get("cityName")).trim().replace("", "");
		String distId=String.valueOf(receiveMap.get("distId")).trim().replace("", "");
		String distName=String.valueOf(receiveMap.get("distName")).trim().replace("", "");
		String commId=String.valueOf(receiveMap.get("commId")).trim().replace("", "");
		String commName=String.valueOf(receiveMap.get("commName")).trim().replace("", "");
		String recvAddrDet=String.valueOf(receiveMap.get("recvAddrDet")).trim().replace("", "");
		String longitude=String.valueOf(receiveMap.get("longitude")).trim().replace("", "");
		String latitude=String.valueOf(receiveMap.get("latitude")).trim().replace("", "");
		String phone=String.valueOf(receiveMap.get("phone")).trim().replace("", "");
		String recvMan=String.valueOf(receiveMap.get("recvMan")).trim().replace("", "");
		String ucAddrStatId=String.valueOf(receiveMap.get("ucAddrStatId")).trim().replace("", "");
		String pGendId=String.valueOf(receiveMap.get("pGendId")).trim().replace("", "");
		String chanActId=String.valueOf(receiveMap.get("chanActId")).trim().replace("", "");
		ArrayList addrList=new ArrayList();
		if (logger.isInfoEnabled()) {
			logger.info("查询登陆账号:"+receiveMap.get("userId").toString());
		}
		if (payUserId!=null && payUserId!="") {
			PayUser user;
			try {
				user = payUserService.findByPayUserId(payUserId);
				if (logger.isInfoEnabled()) {
					logger.info("判断用户是否存在");
				}
				if (user!=null) {
					//判断addrId空
					//如果不为空 查询地址通过addrId 如果存在修改 不存在返回错误码
//					UcUserAddr ucUserAddr =ucUserAddrMapper.selectByUcAddrId(user.getActId());
					if(ucAddrId.equals("")){
						//如果空 insert
						//添加
						UcUserAddr addr=new UcUserAddr();
						addr.setActId(Long.valueOf(user.getActId()));
						if (("").equals(chanActId) || chanActId==null) {
							addr.setChanActId(Long.valueOf(0));
						}else{
							addr.setChanActId(Long.valueOf(chanActId));
						}
						addr.setCityId(cityId);
						addr.setCityName(cityName);
						addr.setCommId(commId);
						addr.setCommName(commName);
						addr.setDistId(distId);
						addr.setDistName(distName);
						addr.setLatitude(latitude);
						addr.setLongitude(longitude);
						if (("").equals(pGendId)) {
							addr.setpGendId(Short.valueOf("1"));
						}else{
							addr.setpGendId(Short.valueOf(pGendId));
						}
						addr.setPhone(phone);
						addr.setProvId(provId);
						addr.setProvName(provName);
						addr.setRecvAddrDet(recvAddrDet);
						addr.setRecvMan(recvMan);
						addr.setUcAddrStatId(Short.valueOf(ucAddrStatId));
						addr.setCrtTime(new Date());
						addr.setUpdTime(new Date());
						ucUserAddrMapper.insert(addr);
						logger.info("用户中心收货地址添加成功"+addr);
						addrList.add(addr);
						resultMap.put("addrList", addrList);
						resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
						resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
					}else{
						UcUserAddr a= ucUserAddrMapper.selectByAddr(ucAddrId);
						//编辑
						if (a!=null) {
							if (a.getActId().equals(user.getActId())) {
								a.setProvId(provId);
								a.setProvName(provName);
								a.setCityId(cityId);
								a.setCityName(cityName);
								a.setCommId(commId);
								a.setCommName(commName);
								a.setDistId(distId);
								a.setDistName(distName);
								a.setLatitude(latitude);
								a.setLongitude(longitude);
								a.setPhone(phone);
								a.setRecvAddrDet(recvAddrDet);
								a.setRecvMan(recvMan);
								a.setUcAddrStatId(Short.valueOf(ucAddrStatId));
								if (("").equals(chanActId) || chanActId==null) {
									a.setChanActId(Long.valueOf(0));
								}else{
									a.setChanActId(Long.valueOf(chanActId));
								}
								a.setUpdTime(new Date());
								if (("").equals(pGendId) || pGendId==null) {
									a.setpGendId(Short.valueOf("1"));
								}else{
									a.setpGendId(Short.valueOf(pGendId));
								}
								ucUserAddrMapper.updateAddr(a);
								logger.info("用户中心收货地址添加成功"+a);
								addrList.add(a);
								resultMap.put("addrList", addrList);
								resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
								resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
							}else{
								//修改的地址id存在，但是登路的不是同一个，所以在这里抛异常
								resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32017_USER_ID_ERROR_FOR_UCUSERADDR.getCodeString());
								resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32017_USER_ID_ERROR_FOR_UCUSERADDR.getMessage());
							}
						}else{
							resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32016_UCUSERADD_ID_ERROR.getCodeString());
							resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32016_UCUSERADD_ID_ERROR.getMessage());
						}
					}
				}else{
					resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
					resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
				}
			} catch (Exception e) {
				logger.error("访问接口出现异常：" + e.getMessage(), e);
				resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			}
		}else{
			resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
			resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
		}
		return resultMap;
	}
	@Override
	public Map<String, Object> ucUserAddrQuery(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(logger.isInfoEnabled()){
			logger.info("请求用户中心-查询该用户所有地址，请求参数："+receiveMap);
		}
		String payUserId=receiveMap.get("userId").toString();
		if (payUserId!=null && payUserId!="") {
			try {
				PayUser user = payUserService.findByPayUserId(payUserId);
				if (user!=null &&!user.getActId().equals("")) {
					List<UcUserAddr> ucUserAddr =ucUserAddrMapper.selectByUcAddrId(user.getActId());
					if (ucUserAddr!=null && ucUserAddr.size()>0) {
						List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
						for (UcUserAddr ucUserAddr2 : ucUserAddr) {
							Map<String, Object> map = new HashMap<String,Object>();
							map.put("ucAddrId", ucUserAddr2.getUcAddrId());
							map.put("chanActId", ucUserAddr2.getChanActId());
							map.put("cityId", ucUserAddr2.getCityId());
							map.put("cityName", ucUserAddr2.getCityName());
							map.put("provId", ucUserAddr2.getProvId());
							map.put("provName", ucUserAddr2.getProvName());
							map.put("commId", ucUserAddr2.getCommId());
							map.put("commName", ucUserAddr2.getCommName());
							map.put("distId", ucUserAddr2.getDistId());
							map.put("distName", ucUserAddr2.getDistName());
							map.put("pGendId", ucUserAddr2.getpGendId());
							map.put("phone", ucUserAddr2.getPhone());
							map.put("latitude", ucUserAddr2.getLatitude());
							map.put("longitude", ucUserAddr2.getLongitude());
							map.put("recvAddrDet", ucUserAddr2.getRecvAddrDet());
							map.put("recvMan", ucUserAddr2.getRecvMan());
							map.put("actId", ucUserAddr2.getActId());
							map.put("ucAddrStatId", ucUserAddr2.getUcAddrStatId());
//							map.put("updTime", ucUserAddr2.getUpdTime());
//							map.put("crtTime", ucUserAddr2.getCrtTime());
							list.add(map);
						}
						resultMap.put("addrList", list);
						resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
						resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
					}else{
						resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32019_QUERY_NULL.getCodeString());
						resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32019_QUERY_NULL.getMessage());
					}
				}else{
					resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
					resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
				}
			} catch (Exception e) {
				logger.error("访问接口出现异常：" + e.getMessage(), e);
				resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			}
		}else{
			resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
			resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
		}
		return resultMap;
	}
	

}
