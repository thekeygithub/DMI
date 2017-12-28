package com.ts.controller.app.SearchAPI.PaymentAPI.FaceComparison;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Controller;

import com.hisign.bean.RequestResult;
import com.hisign.hbveClient.HBVENettyClient;
import com.hisign.util.SystemUtil;
import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.dao.redis.RedisDao;
import com.ts.entity.FaceComparison.P_face_compare;
import com.ts.util.FaceLinkedQueue;
import com.ts.util.FaceUtils;
import com.ts.util.UuidUtil;

import net.sf.json.JSONObject;

/**
 * 
 * ProjectName：API
 * ClassName：FaceComparisonPAY
 * Description：TODO(人脸比对接口)
 * @Copyright：
 * @Company：
 * @author： 刘峰
 * @version 
 * Create Date：2017年6月13日 下午13:00:21
 */

@Controller
public class FaceComparisonPAY extends PayRSHandler {
	
	@Resource(name = "redisDao")
	private RedisDao redisDao;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@Override
	protected Object exceBusiness(JSONObject value) {
		Long ls = System.currentTimeMillis();
		HBVENettyClient hc = null;
		Map param = new LinkedHashMap();//返回值
		try{
			//***************** 得到入参及配置的值  start ****************************************
			String face_url = ReadPropertiesFiles.getValue("face_url");//调用第三方接口地址
			String face_addr = ReadPropertiesFiles.getValue("face_addr");//调用比对接口IP
			String face_port = ReadPropertiesFiles.getValue("face_port");//调用比对接口端口
			String face_result = ReadPropertiesFiles.getValue("face_result");//阀值
			String face_img_path = ReadPropertiesFiles.getValue("face_img_path");//生成照片地址
			String cancel_time = ReadPropertiesFiles.getValue("cancel_time");//redis失效时间
			String is_redis = ReadPropertiesFiles.getValue("is_redis");//是否从缓存中取值：1是0否
			String id_card = value.getString("id_card");//身份证号码
			String verify_img = value.getString("verify_img");//照片Base64码
			String hosp_code = value.getString("hosp_code");//医院编码
			String doctor_code = value.getString("doctor_code");//就诊号
			String group_id = value.getString("group_id");//机构编号
			//***************** end *******************************************************
			
			//判断是否从缓存中取值
			if(value.get("is_cache")!=null && is_redis.equals(value.getString("is_cache"))){
				P_face_compare entity = (P_face_compare)redisDao.getObject(hosp_code+"_"+doctor_code+"_"+group_id);
				if(entity != null){
					param.put("similarity", entity.getSimilarity());
					param.put("result", entity.getResult());
				}else{
					param.put("similarity", "0");
					param.put("result", "0");
				}
			}else{
				//******************************* 创建目录生成图片 start *************************
				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dates = sdformat.format(new Date());
				String times = dates.substring(0, 10);
				String year = times.split("-")[0];
				String month = times.split("-")[1];
				String day = times.split("-")[2];
				String UUID = UuidUtil.get32UUID();
				String img_path = face_img_path + year + File.separator + month + File.separator + day + File.separator + id_card + File.separator;
				FaceUtils.createFilePath(img_path);
				String verify_img_path = img_path + UUID + "_verify.jpg";
				String query_img_path = img_path + UUID + "_query.jpg";
				//******************************* 创建路径  end ********************************
				
				//********************** 调用第三方接口得到源图片信息 start ******************************
				HttpClient client = new HttpClient();
				PostMethod method = new PostMethod(face_url);
				method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
				method.addParameter("certno", id_card);
				client.setConnectionTimeout(30000);
				client.executeMethod(method);
				String josnStr = FaceUtils.getInputStreamString(method.getResponseBodyAsStream());
				JSONObject jo = getJsonObject(josnStr);
				//********************** 源图片信息  end ******************************
				
				//***************** 调用人脸比对接口  start *********************************
				FaceUtils.GenerateImage(verify_img, verify_img_path);//传入的参数要比对的图片
				FaceUtils.GenerateImage(jo.getString("photo"), query_img_path);//调用接口得到的源图片
				hc = new HBVENettyClient(face_addr, Integer.parseInt(face_port));
				String connId = hc.similarityOfTwoImage(HBVENettyClient.readFile(verify_img_path), HBVENettyClient.readFile(query_img_path));
				RequestResult result = hc.getResultSynchronized(connId);
				float score = Float.intBitsToFloat(SystemUtil.fourByteArrayToInt(result.getResult()));
				param.put("similarity", score);
				//***************** 人脸比对  end **********************************************
				
				//******************** 放入队列缓存  start ***************************************
				P_face_compare face_model = new P_face_compare();
				if(score >= Float.parseFloat(face_result)){
					param.put("result", "1");
					face_model.setResult(1);
				}else{
					param.put("result", "0");
					face_model.setResult(0);
				}
				face_model.setId(UUID);
				face_model.setId_card(id_card);
				face_model.setHosp_code(hosp_code);
				face_model.setDoctor_code(doctor_code);
				face_model.setGroup_id(group_id);
				face_model.setVerify_img(verify_img);
				face_model.setVerify_path(verify_img_path);
				face_model.setQuery_img(jo.getString("photo"));
				face_model.setQuery_path(query_img_path);
				face_model.setSimilarity(String.valueOf(score));
				face_model.setCreate_time(dates);
				FaceLinkedQueue.setQueuePay(face_model);
				redisDao.setObject(hosp_code+"_"+doctor_code+"_"+group_id, Integer.parseInt(cancel_time), face_model);
				//******************** 放入结束  end  *******************************************
			}
			param.put("code", "0");
		}catch(Exception e){
			param.put("code", "-1");
			logger.error(e.getMessage(), e);
		}finally{
			if(hc != null){
				hc.close();
			}
		
		}
		BaseController.logBefore(logger, "f01--->>>人脸比对接口执行时间:" + (System.currentTimeMillis() - ls));
		return param;
	}

}