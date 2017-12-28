package com.ts.service.mts.standardize.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.entity.mts.MtsQuestion;
import com.ts.entity.mts.MtsRecord;
import com.ts.entity.mts.MtsRecordDetail;
import com.ts.entity.mts.MtsRecordInfo;
import com.ts.entity.mts.MtsRecordParameters;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.MtsConfig;
import com.ts.service.mts.matchrule.impl.DataMatchRuleDetail;
import com.ts.service.mts.question.MTSQuestionService;
import com.ts.service.mts.record.MTSRecordService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.util.DateUtil;
import com.ts.util.RandomUtil;

import net.sf.json.JSONObject;

@Service("StandardizeService")
public class StandardizeServiceImpl implements StandardizeService {

	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	@Resource(name = "DataMatchRuleDetail")
	private DataMatchRuleDetail dmrd;

	@Resource(name = "MtsConfig")
	private MtsConfig mc;

	@Resource(name = "MTSQuestionService")
	private MTSQuestionService qs;

	@Resource(name = "MTSRecordService")
	private MTSRecordService mtsRecordService;

	@SuppressWarnings("unchecked")
	@Override
	public String standardizeData(String visitId, String visitType, String dataSource, String dataType,
			String parameters, String batchNum, String application, String areaId) throws Exception {
		System.out.println("parameters==============>>>" + parameters);
		Map<String, MatchRuleDetailT> cacheMap = mcs.getCacheMap();

		Iterator<Map.Entry<String, MatchRuleDetailT>> entries = cacheMap.entrySet().iterator();

		/*
		 * while (entries.hasNext()) {
		 * 
		 * Map.Entry<String, MatchRuleDetailT> entry = entries.next();
		 * 
		 * System.out.println("Key = " + entry.getKey() + ", Value = " +
		 * entry.getValue());
		 * 
		 * }
		 */

		JSONObject operateDataTypeJSON = null;
		JSONObject operateParametersJSON = null;
		JSONObject resultJSON = null;
		MatchRuleDetailT matchRuleDetailT = null;
		MtsQuestion mtsQuestion = null;
		MtsRecord mtsRecord = null;
		MtsRecordInfo mtsRecordInfo = null;
		MtsRecordDetail mtsRecordDetail = null;
		MtsRecordParameters mtsRecordParameters = null;
		Map<String, String> paraMap = null;

		String[] paraStr = null;
		String para = "";
		String paraKey = "";
		String paraVal = "";
		String noc = "";
		String standardizeResult = "";
		String NLPResult = "";
		String statusStr = "";
		String nlpOrder = "";
		String matchRes = "";
		String doubtDiag = "";
		String stanStr = "";
		String stanStrNew = "";
		String[] resStr = null;
		String res = "";
		String resVal = "";
		String NlpValue = "";
		String NLPDrug = "";
		String jsonarr = "";
		String special = "0";
		String inver = "";
		String finallyResult = "";
		String NlpValueStr = "";

		resultJSON = new JSONObject();
		resultJSON.put("status", "0");
		resultJSON.put("result", "");
		resultJSON.put("nlpResult", "");
		resultJSON.put("visitId", visitId);
		resultJSON.put("visitType", visitType);
		resultJSON.put("dataSource", dataSource);
		resultJSON.put("batchNum", batchNum);
		resultJSON.put("dataType", dataType);
		resultJSON.put("areaId", areaId);
		resultJSON.put("dataClass", "");
		resultJSON.put("operTime", DateUtil.getTime());
		resultJSON.put("parameters", parameters);

		mtsQuestion = new MtsQuestion();
		mtsQuestion.setSTATUS("0");
		mtsQuestion.setRESULT("");
		mtsQuestion.setNLP_ORDER("");
		mtsQuestion.setVISIT_ID(visitId);
		mtsQuestion.setVISIT_TYPE(visitType);
		mtsQuestion.setDATA_SOURCE(dataSource);
		mtsQuestion.setBATCH_NUM(batchNum);
		mtsQuestion.setDATA_TYPE(dataType);
		mtsQuestion.setDATA_CLASS("");
		mtsQuestion.setNLP_ORDER("");
		mtsQuestion.setOPERATE_TIME(new Date());
		mtsQuestion.setPARAMETERS(parameters);

		mtsRecordInfo = new MtsRecordInfo();
		String RECORD_INFO_ID = RandomUtil.getRandomId();
		mtsRecordInfo.setRECORD_INFO_ID(RECORD_INFO_ID);
		mtsRecordInfo.setBATCH_NUM(batchNum);
		mtsRecordInfo.setBUSINESS_NUM(visitId);
		mtsRecordInfo.setBUSINESS_TYPE(visitType);
		mtsRecordInfo.setDATA_CLASS(dataType);
		mtsRecordInfo.setDATA_SOURCE(dataSource);
		mtsRecordInfo.setINFO_STATUS("0");
		mtsRecordInfo.setINFO_TYPE("0");
		mtsRecordInfo.setOPERATE_TIME(new Date());
		// mtsRecordService.addMtsRecordInfo(mtsRecordInfo);

		mtsRecord = new MtsRecord();
		mtsRecord.setSTATUS("0");
		mtsRecord.setRESULT("");
		mtsRecord.setNLP_ORDER("");
		mtsRecord.setVISIT_ID(visitId);
		mtsRecord.setVISIT_TYPE(visitType);
		mtsRecord.setDATA_SOURCE(dataSource);
		mtsRecord.setBATCH_NUM(batchNum);
		mtsRecord.setDATA_TYPE(dataType);
		mtsRecord.setDATA_CLASS("");
		mtsRecord.setNLP_ORDER("");
		mtsRecord.setOPERATE_TIME(new Date());
		mtsRecord.setPARAMETERS(parameters);

		// if (visitId == null || "".equals(visitId)) {
		// if(!"AI".equals(application)){
		// this.recordQuestion(mtsQuestion);
		// }
		// this.recordLog(mtsRecord);
		//
		// return resultJSON.toString();
		// }
		//
		// if (dataType == null || "".equals(dataType)) {
		// if(!"AI".equals(application)){
		// this.recordQuestion(mtsQuestion);
		// }
		// this.recordLog(mtsRecord);
		//
		// return resultJSON.toString();
		// }

		if (areaId == null || "".equals(areaId)) {
			if (!"AI".equals(application)) {
				this.recordQuestion(mtsQuestion);
			}
			this.recordLog(mtsRecord);

			return resultJSON.toString();
		}

		if (parameters == null || "".equals(parameters)) {
			if (!"AI".equals(application)) {
				this.recordQuestion(mtsQuestion);
			}
			this.recordLog(mtsRecord);

			return resultJSON.toString();
		}

		paraStr = parameters.split("@\\|&");

		if (paraStr == null || paraStr.length == 0) {
			if (!"AI".equals(application)) {
				this.recordQuestion(mtsQuestion);
			}
			this.recordLog(mtsRecord);

			return resultJSON.toString();
		}
		for (int i = 0; i < paraStr.length; i++) {
			para = paraStr[i];
			if (para != null && !"".equals(para)) {
				paraKey = para.substring(0, para.indexOf("@#&"));
				paraVal = para.substring(para.indexOf("@#&") + "@#&".length(), para.length());

				mtsRecordParameters = new MtsRecordParameters();
				mtsRecordParameters.setRECORD_PARAMETERS_ID(RandomUtil.getRandomId());
				mtsRecordParameters.setPARAMETERS_CONTENT(paraVal);
				mtsRecordParameters.setPARAMETERS_ORDER(i + "");
				mtsRecordParameters.setPARAMETERS_TYPE(paraKey);
				mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
				mtsRecordService.addMtsRecordParameters(mtsRecordParameters);

				if (paraMap == null) {
					paraMap = new HashMap<String, String>();
				}
				paraMap.put(paraKey, paraVal);
			}
		}

		if (paraMap == null) {
			if (!"AI".equals(application)) {
				this.recordQuestion(mtsQuestion);
			}
			this.recordLog(mtsRecord);

			return resultJSON.toString();
		}

		matchRuleDetailT = cacheMap.get("startCode_" + dataType + areaId);

		List<String> NLPList = null;
		boolean bool = true;
		while (bool) {
			noc = matchRuleDetailT.getNOC();
			mtsQuestion.setDATA_CLASS(noc);
			mtsRecord.setDATA_CLASS(noc);
			operateDataTypeJSON = new JSONObject();
			operateDataTypeJSON.put("dataType", dataType);

			operateParametersJSON = new JSONObject();

			operateParametersJSON.put("data", matchRuleDetailT.getMEM_DATA_CODE() + "@#&" + paraMap.get(noc));
			mtsQuestion.setPARAMETERS(paraMap.get(noc));

			String applyMethod = matchRuleDetailT.getAPPLY_METHOD();

			if ("matchNlp".equals(applyMethod)) {
				matchRes = mc.matchNlp(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("doubt".equals(applyMethod)) {
				matchRes = mc.doubt(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("special".equals(applyMethod)) {
				matchRes = mc.special(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("match".equals(applyMethod)) {
				if ("ALL".equals(noc)) {
					operateParametersJSON = new JSONObject();
					operateParametersJSON.put("data", parameters);
				}
				matchRes = mc.match(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("matchPRKS".equals(applyMethod)) {
				if ("ALL".equals(noc)) {
					operateParametersJSON = new JSONObject();
					operateParametersJSON.put("data", parameters);
				}
				matchRes = mc.matchPRKS(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("inerver".equals(applyMethod)) {
				matchRes = mc.inerver(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			}

			JSONObject matchResultJSON = JSONObject.fromObject(matchRes);
			String matchStatus = "";
			String matchResult = "";
			String NLP = "";

			if (matchResultJSON.containsKey("status")) {
				matchStatus = matchResultJSON.getString("status");
				if ("1".equals(matchStatus)) {
					statusStr = "1";
				} else if ("0".equals(matchStatus)) {
					statusStr = "0";
				}
			}

			if (matchResultJSON.containsKey("result")) {
				matchResult = matchResultJSON.getString("result");
			} else {
				matchResult = "";
			}
			if (matchResultJSON.containsKey("NlpValue")) {
				NlpValue = matchResultJSON.getString("NlpValue");
			}
			if (matchResultJSON.containsKey("NLPDrug")) {
				NLPDrug = matchResultJSON.getString("NLPDrug");
			}
			if (matchResultJSON.containsKey("jsonarr")) {
				jsonarr = matchResultJSON.getString("jsonarr");
			}

			/*
			 * else{ NlpValue = ""; }
			 */
			if (matchResultJSON.containsKey("SPEC")) {
				special = matchResultJSON.getString("SPEC");
			}
			if (matchResultJSON.containsKey("doubt")) {
				doubtDiag = matchResultJSON.getString("doubt");
			}
			if (matchResultJSON.containsKey("inver")) {
				inver = matchResultJSON.getString("inver");
			}

			if (!"".equals(NlpValue) && null != NlpValue && "".equals(NlpValueStr)) {
				NlpValueStr = NlpValue;
			}

			if (matchResultJSON.containsKey("NLP")) {
				NLP = matchResultJSON.getString("NLP");
				if (!"".equals(NLP)) {
					nlpOrder = NLP;
				}
				NLPList = (List<String>) matchResultJSON.get("NLP");
			} else {
				NLPList = null;
			}
			if ("1".equals(matchStatus)) {
				if ("1".equals(matchRuleDetailT.getQUESTION())) {
					// 生成问题单
					mtsQuestion.setSTATUS(statusStr);
					mtsQuestion.setNLP_ORDER(nlpOrder);
					mtsQuestion.setNLP_RESULT(NlpValue);
					mtsQuestion.setDOUBT_DIAG(doubtDiag);
					mtsQuestion.setRESULT(stanStr);
					mtsQuestion.setEXPORT_STATUS("0");
					if (!"AI".equals(application)) {
						this.recordQuestion(mtsQuestion);
					}
				}

				if ("matchNlp".equals(applyMethod)) {
					if ("1".equals(matchRuleDetailT.getMARK())) {
						if (!"".equals(NlpValue) && null != NlpValue) {
							if (null != NLPResult && !"".equals(NLPResult)) {
								stanStr = stanStr + "@|&" + NlpValue;
								;
								NLPResult = NLPResult + "@|&" + NlpValue;
							} else {
								NLPResult = NlpValue;
								stanStr = NlpValue;
							}
						} else {
							if (null != standardizeResult && !"".equals(standardizeResult)
									&& !"none".equals(standardizeResult)) {
								stanStr = standardizeResult + "@|&" + matchResult;
								standardizeResult = standardizeResult + "@|&" + matchResult;

							} else {
								if (null != stanStr && !"".equals(stanStr)) {
									standardizeResult = stanStr + "@|&" + matchResult;
									stanStr = stanStr + "@|&" + matchResult;
								} else {
									standardizeResult = matchResult;
									stanStr = matchResult;
								}
							}
						}
					} else if ("0".equals(matchRuleDetailT.getMARK())) {
						standardizeResult = matchResult;
						NLPResult = NlpValue;
						if (!"".equals(matchResult) && null != matchResult && !"none".equals(matchResult)) {
							stanStr = matchResult;
						} else if (!"".equals(NlpValue) && null != NlpValue && !"none".equals(NlpValue)) {
							stanStr = NlpValue;
						}
					}
				} else if ("matchPRKS".equals(applyMethod) || "match".equals(applyMethod)
						|| "special".equals(applyMethod)) {
					if ("1".equals(matchRuleDetailT.getMARK())) {
						if (!"".equals(NlpValue) && null != NlpValue) {
							if (null != NLPResult && !"".equals(NLPResult)) {
								stanStr = stanStr + "@|&" + NlpValue;
								;
								NLPResult = NLPResult + "@|&" + NlpValue;
							} else {
								NLPResult = NlpValue;
								stanStr = NlpValue;
							}
						} else {
							if (null != standardizeResult && !"".equals(standardizeResult)
									&& !"none".equals(standardizeResult)) {
								stanStr = standardizeResult + "@|&" + matchResult;
								standardizeResult = standardizeResult + "@|&" + matchResult;

							} else {
								if (null != stanStr && !"".equals(stanStr)) {
									standardizeResult = stanStr + "@|&" + matchResult;
									stanStr = stanStr + "@|&" + matchResult;
								} else {
									standardizeResult = matchResult;
									stanStr = matchResult;
								}
							}
						}
					} else if ("0".equals(matchRuleDetailT.getMARK())) {
						standardizeResult = matchResult;
						NLPResult = NlpValue;
						if (!"".equals(matchResult) && null != matchResult && !"none".equals(matchResult)) {
							stanStr = matchResult;
						} else if (!"".equals(NlpValue) && null != NlpValue && !"none".equals(NlpValue)) {
							stanStr = NlpValue;
						}
					}
				}

				if (null != matchRuleDetailT.getSUCCESS_REDIRECT_TO()
						&& "null" != matchRuleDetailT.getSUCCESS_REDIRECT_TO()
						&& !"".equals(matchRuleDetailT.getSUCCESS_REDIRECT_TO())) {
					matchRuleDetailT = cacheMap
							.get("memDataCode_" + matchRuleDetailT.getSUCCESS_REDIRECT_TO() + areaId);
				} else {
					bool = false;
				}
			} else {
				if ("1".equals(matchRuleDetailT.getQUESTION())) {
					// 生成问题单
					mtsQuestion.setSTATUS(statusStr);
					mtsQuestion.setNLP_ORDER(nlpOrder);
					mtsQuestion.setNLP_RESULT(NlpValue);
					mtsQuestion.setDOUBT_DIAG(doubtDiag);
					mtsQuestion.setRESULT(stanStr);
					mtsQuestion.setEXPORT_STATUS("0");
					if (!"AI".equals(application)) {
						this.recordQuestion(mtsQuestion);
					}
				}

				if ("matchNlp".equals(applyMethod)) {
					if ("1".equals(matchRuleDetailT.getMARK())) {
						if (!"".equals(NlpValue) && null != NlpValue) {
							if (null != NLPResult && !"".equals(NLPResult)) {
								stanStr = stanStr + "@|&" + NlpValue;
								;
								NLPResult = NLPResult + "@|&" + NlpValue;
							} else {
								NLPResult = NlpValue;
								stanStr = NlpValue;
							}
						} else {
							if (null != standardizeResult && !"".equals(standardizeResult)
									&& !"none".equals(standardizeResult)) {
								stanStr = standardizeResult + "@|&" + matchResult;
								standardizeResult = standardizeResult + "@|&" + matchResult;

							} else {
								if (null != stanStr && !"".equals(stanStr)) {
									standardizeResult = stanStr + "@|&" + matchResult;
									stanStr = stanStr + "@|&" + matchResult;
								} else {
									standardizeResult = matchResult;
									stanStr = matchResult;
								}
							}
						}
					} else if ("0".equals(matchRuleDetailT.getMARK())) {
						standardizeResult = matchResult;
						NLPResult = NlpValue;
						if (!"".equals(matchResult) && null != matchResult && !"none".equals(matchResult)) {
							stanStr = matchResult;
						} else if (!"".equals(NlpValue) && null != NlpValue && !"none".equals(NlpValue)) {
							stanStr = NlpValue;
						}
					}
				} else if ("matchPRKS".equals(applyMethod) || "match".equals(applyMethod)
						|| "special".equals(applyMethod)) {
					if ("1".equals(matchRuleDetailT.getMARK())) {
						if (!"".equals(NlpValue) && null != NlpValue) {
							if (null != NLPResult && !"".equals(NLPResult)) {
								stanStr = stanStr + "@|&" + NlpValue;
								;
								NLPResult = NLPResult + "@|&" + NlpValue;
							} else {
								NLPResult = NlpValue;
								stanStr = NlpValue;
							}
						} else {
							if (null != standardizeResult && !"".equals(standardizeResult)
									&& !"none".equals(standardizeResult)) {
								stanStr = standardizeResult + "@|&" + matchResult;
								standardizeResult = standardizeResult + "@|&" + matchResult;

							} else {
								if (null != stanStr && !"".equals(stanStr)) {
									standardizeResult = stanStr + "@|&" + matchResult;
									stanStr = stanStr + "@|&" + matchResult;
								} else {
									standardizeResult = matchResult;
									stanStr = matchResult;
								}
							}
						}
					} else if ("0".equals(matchRuleDetailT.getMARK())) {
						standardizeResult = matchResult;
						NLPResult = NlpValue;
						if (!"".equals(matchResult) && null != matchResult && !"none".equals(matchResult)) {
							stanStr = matchResult;
						} else if (!"".equals(NlpValue) && null != NlpValue && !"none".equals(NlpValue)) {
							stanStr = NlpValue;
						}
					}
				}

				if (null != matchRuleDetailT.getFAILURE_REDIRECT_TO()
						&& "null" != matchRuleDetailT.getFAILURE_REDIRECT_TO()
						&& !"".equals(matchRuleDetailT.getFAILURE_REDIRECT_TO())) {
					matchRuleDetailT = cacheMap
							.get("memDataCode_" + matchRuleDetailT.getFAILURE_REDIRECT_TO() + areaId);
				} else {
					bool = false;
				}
			}
		}

		if (!"".equals(NLPResult) && null != NLPResult && !"".equals(nlpOrder) && null != nlpOrder) {
			nlpOrder = nlpOrder.replace("[", "");
			nlpOrder = nlpOrder.replace("]", "");
			nlpOrder = nlpOrder.replaceAll("\"", "");
			String nlpOrderStr[] = nlpOrder.split(",");

			JSONObject jsonObj = JSONObject.fromObject(NLPResult);
			Iterator<String> it = jsonObj.keys();
			int pp = 0;
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = jsonObj.getString(key);
				System.out.println(" key======>>" + key);
				System.out.println(" value====>>" + value);
				if (pp <= nlpOrderStr.length) {
					System.out.println(" nlpValue====>>" + nlpOrderStr[pp]);
				}
				resVal = value;
				if ("".equals(stanStrNew)) {
					stanStrNew = nlpOrderStr[pp] + ":" + value;
				} else {
					stanStrNew = stanStrNew + "@,&" + nlpOrderStr[pp] + ":" + value;
				}
				pp++;
			}
			finallyResult = stanStrNew;

			recordInfo(finallyResult, RECORD_INFO_ID, doubtDiag, special, "");
			mtsRecord.setRESULT(stanStrNew);
			/*
			 * stanStr = stanStr.replace("{", ""); stanStr =
			 * stanStr.replace("}", ""); stanStr = stanStr.replace("\"", "");
			 * resStr = stanStr.split(","); if (resStr != null && resStr.length
			 * > 0) { for (int i = 0; i < resStr.length; i++) { res = resStr[i];
			 * if (res != null && !"".equals(res)) { if(res.indexOf(":") != -1){
			 * resVal = res.substring(res.indexOf(":") + ":".length(),
			 * res.length()); }else{ resVal = res; } if("".equals(stanStrNew)){
			 * stanStrNew = resVal; }else{ stanStrNew = stanStrNew + "@,&" +
			 * resVal; } } } } finallyResult = stanStrNew;
			 * mtsRecord.setRESULT(stanStrNew);
			 */
		} else {
			stanStr = stanStr.replaceAll(",", "@,&");
			finallyResult = stanStr;

			String standName = "";
			if (null != paraMap) {
				for (Map.Entry<String, String> entry : paraMap.entrySet()) {
					System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
					if ("".equals(standName)) {
						standName = entry.getValue();
					} else {
						standName = standName + "/" + entry.getValue();
					}
				}
			}
			if (!"".equals(standName) && null != standName) {
				recordInfo(standName + ":" + finallyResult, RECORD_INFO_ID, doubtDiag, special, "");
			}
			mtsRecord.setRESULT(stanStr);
		}

		if (null != statusStr && !"".equals(statusStr)) {
			mtsRecordInfo.setSTANDARD_TYPE(statusStr);
			mtsRecordInfo.setOPERATE_TIME(new Date());
			mtsRecordService.editMtsRecordInfo(mtsRecordInfo);
		}

		resultJSON.put("result", finallyResult);
		resultJSON.put("status", statusStr);
		resultJSON.put("nlpResult", NlpValueStr);
		resultJSON.put("doubtDiag", doubtDiag);
		resultJSON.put("special", special);
		resultJSON.put("nlpOrder", nlpOrder);
		resultJSON.put("inver", inver);
		resultJSON.put("NLPDrug", NLPDrug);
		resultJSON.put("jsonarr", jsonarr);

		mtsRecord.setSTATUS(statusStr);
		mtsRecord.setNLP_ORDER(nlpOrder);
		mtsRecord.setNLP_RESULT(NlpValue);
		mtsRecord.setDOUBT_DIAG(doubtDiag);

		this.recordLog(mtsRecord);
		return resultJSON.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String standardizeDataForZNWD(String parameters, String areaId) throws Exception {
		String matchRes = mc.ZnwdNlp(parameters, areaId);
		JSONObject resultJSON = null;
		resultJSON = new JSONObject();
		resultJSON.put("result", matchRes);
		return resultJSON.toString();
	}

	public void recordQuestion(MtsQuestion mtsQuestion) {
		try {
			if (mtsQuestion != null) {
				mtsQuestion.setQUESTION_ID(RandomUtil.getRandomId());
				qs.addMTSQuestion(mtsQuestion);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void recordLog(MtsRecord mtsRecord) {
		try {
			if (mtsRecord != null) {
				mtsRecord.setRECORD_ID(RandomUtil.getRandomId());
				mtsRecordService.addMTSRecord(mtsRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String standardizeDataNew(String visitId, String visitType, String dataSource, String dataType,
			String parameters, String batchNum, String application, String areaId, String RECORD_INFO_ID,
			String DB_DATA_ID) throws Exception {

		Map<String, MatchRuleDetailT> cacheMap = mcs.getCacheMap();

		Iterator<Map.Entry<String, MatchRuleDetailT>> entries = cacheMap.entrySet().iterator();

		JSONObject operateDataTypeJSON = null;
		JSONObject operateParametersJSON = null;
		JSONObject resultJSON = null;
		MatchRuleDetailT matchRuleDetailT = null;
		/* MtsQuestion mtsQuestion = null; */
		// MtsRecord mtsRecord = null;
		MtsRecordInfo mtsRecordInfo = null;
		MtsRecordDetail mtsRecordDetail = null;
		// MtsRecordParameters mtsRecordParameters = null;
		Map<String, String> paraMap = null;

		String[] paraStr = null;
		String para = "";
		String paraKey = "";
		String paraVal = "";
		String noc = "";
		String standardizeResult = "";
		String NLPResult = "";
		String statusStr = "";
		String nlpOrder = "";
		String matchRes = "";
		String doubtDiag = "";
		String stanStr = "";
		String stanStrNew = "";
		String[] resStr = null;
		String res = "";
		String resVal = "";
		String NlpValue = "";
		String NLPDrug = "";
		String jsonarr = "";
		String special = "0";
		String inver = "";
		String finallyResult = "";
		String NlpValueStr = "";
		String TSBH = "";

		resultJSON = new JSONObject();
		resultJSON.put("status", "0");
		resultJSON.put("result", "");
		resultJSON.put("nlpResult", "");
		resultJSON.put("visitId", visitId);
		resultJSON.put("visitType", visitType);
		resultJSON.put("dataSource", dataSource);
		resultJSON.put("batchNum", batchNum);
		resultJSON.put("dataType", dataType);
		resultJSON.put("areaId", areaId);
		resultJSON.put("dataClass", "");
		resultJSON.put("TSBH", TSBH);
		resultJSON.put("operTime", DateUtil.getTime());
		resultJSON.put("parameters", parameters);

		if (areaId == null || "".equals(areaId)) {
			if (!"AI".equals(application)) {

			}
			return resultJSON.toString();
		}

		if (parameters == null || "".equals(parameters)) {
			if (!"AI".equals(application)) {
			}
			return resultJSON.toString();
		}

		paraStr = parameters.split("@\\|&");

		if (paraStr == null || paraStr.length == 0) {
			if (!"AI".equals(application)) {
			}
			return resultJSON.toString();
		}
		for (int i = 0; i < paraStr.length; i++) {
			para = paraStr[i];
			if (para != null && !"".equals(para)) {
				paraKey = para.substring(0, para.indexOf("@#&"));
				paraVal = para.substring(para.indexOf("@#&") + "@#&".length(), para.length());

				if (paraMap == null) {
					paraMap = new HashMap<String, String>();
				}
				paraMap.put(paraKey, paraVal);
			}
		}

		if (paraMap == null) {
			if (!"AI".equals(application)) {
			}
			return resultJSON.toString();
		}

		matchRuleDetailT = cacheMap.get("startCode_" + dataType + areaId);

		List<String> NLPList = null;
		boolean bool = true;
		while (bool) {
			noc = matchRuleDetailT.getNOC();
			operateDataTypeJSON = new JSONObject();
			operateDataTypeJSON.put("dataType", dataType);

			operateParametersJSON = new JSONObject();

			operateParametersJSON.put("data", matchRuleDetailT.getMEM_DATA_CODE() + "@#&" + paraMap.get(noc));

			String applyMethod = matchRuleDetailT.getAPPLY_METHOD();

			if ("matchNlp".equals(applyMethod)) {
				matchRes = mc.matchNlp(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("matchNlpPRKS".equals(applyMethod)) {
				matchRes = mc.matchNlpPRKS(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("doubt".equals(applyMethod)) {
				matchRes = mc.doubt(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("special".equals(applyMethod)) {
				matchRes = mc.special(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("match".equals(applyMethod)) {
				if ("ALL".equals(noc)) {
					operateParametersJSON = new JSONObject();
					operateParametersJSON.put("data", parameters);
				}
				matchRes = mc.match(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("matchPRKS".equals(applyMethod)) {
				if ("ALL".equals(noc)) {
					operateParametersJSON = new JSONObject();
					operateParametersJSON.put("data", parameters);
				}
				matchRes = mc.matchPRKS(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("inerver".equals(applyMethod)) {
				matchRes = mc.inerver(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("matchPRKS23".equals(applyMethod)) {
				matchRes = mc.matchPRKS23(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("matchNlpPRKS23".equals(applyMethod)) {
				matchRes = mc.matchNlpPRKS23(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("specialPRKS23".equals(applyMethod)) {
				matchRes = mc.specialPRKS23(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("tsckZL".equals(applyMethod)) {
				matchRes = mc.tsckZL(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			} else if ("tsckSHZT".equals(applyMethod)) {
				matchRes = mc.tsckSHZT(operateDataTypeJSON.toString(), operateParametersJSON.toString(), areaId);
			}

			JSONObject matchResultJSON = JSONObject.fromObject(matchRes);
			String matchStatus = "";
			String matchResult = "";
			String NLP = "";

			if (matchResultJSON.containsKey("status")) {
				matchStatus = matchResultJSON.getString("status");
				if ("1".equals(matchStatus)) {
					statusStr = "1";
				} else if ("0".equals(matchStatus)) {
					statusStr = "0";
				}
			}

			if (matchResultJSON.containsKey("result")) {
				matchResult = matchResultJSON.getString("result");
			} else {
				matchResult = "";
			}
			if (matchResultJSON.containsKey("NlpValue")) {
				NlpValue = matchResultJSON.getString("NlpValue");
			}
			if (matchResultJSON.containsKey("NLPDrug")) {
				NLPDrug = matchResultJSON.getString("NLPDrug");
			}
			if (matchResultJSON.containsKey("jsonarr")) {
				jsonarr = matchResultJSON.getString("jsonarr");
			}

			/*
			 * else{ NlpValue = ""; }
			 */
			if (matchResultJSON.containsKey("SPEC")) {
				special = matchResultJSON.getString("SPEC");
			}
			if (matchResultJSON.containsKey("doubt")) {
				doubtDiag = matchResultJSON.getString("doubt");
			}
			if (matchResultJSON.containsKey("inver")) {
				inver = matchResultJSON.getString("inver");
			}

			if (matchResultJSON.containsKey("TSBH")) {
				TSBH = matchResultJSON.getString("TSBH");
			}

			if (!"".equals(NlpValue) && null != NlpValue && "".equals(NlpValueStr)) {
				NlpValueStr = NlpValue;
			}

			if (matchResultJSON.containsKey("NLP")) {
				NLP = matchResultJSON.getString("NLP");
				if (!"".equals(NLP)) {
					nlpOrder = NLP;
				}
				NLPList = (List<String>) matchResultJSON.get("NLP");
			} else {
				NLPList = null;
			}
			
			if ("1".equals(matchStatus)) {
				if ("matchNlp".equals(applyMethod)) {
					if ("1".equals(matchRuleDetailT.getMARK())) {
						if (!"".equals(NlpValue) && null != NlpValue) {
							if (null != NLPResult && !"".equals(NLPResult)) {
								stanStr = stanStr + "@|&" + NlpValue;
								;
								NLPResult = NLPResult + "@|&" + NlpValue;
							} else {
								NLPResult = NlpValue;
								stanStr = NlpValue;
							}
						} else {
							if(null != matchResult && !"".equals(matchResult)){
								if (null != standardizeResult && !"".equals(standardizeResult)
										&& !"none".equals(standardizeResult)) {
									stanStr = standardizeResult + "@|&" + matchResult;
									standardizeResult = standardizeResult + "@|&" + matchResult;
								} else {
									if (null != stanStr && !"".equals(stanStr)) {
										standardizeResult = stanStr + "@|&" + matchResult;
										stanStr = stanStr + "@|&" + matchResult;
									} else {
										standardizeResult = matchResult;
										stanStr = matchResult;
									}
								}
							}
						}
					} else if ("0".equals(matchRuleDetailT.getMARK())) {
						standardizeResult = matchResult;
						NLPResult = NlpValue;
						if (!"".equals(matchResult) && null != matchResult && !"none".equals(matchResult)) {
							stanStr = matchResult;
						} else if (!"".equals(NlpValue) && null != NlpValue && !"none".equals(NlpValue)) {
							stanStr = NlpValue;
						}
					}

					
					
				} else if ("matchNlpPRKS".equals(applyMethod) || "doubt".equals(applyMethod)
						|| "special".equals(applyMethod) || "match".equals(applyMethod)
						|| "matchPRKS".equals(applyMethod) || "inerver".equals(applyMethod)
						|| "matchPRKS23".equals(applyMethod) || "matchNlpPRKS23".equals(applyMethod)
						|| "specialPRKS23".equals(applyMethod) || "tsckZL".equals(applyMethod)
						|| "tsckSHZT".equals(applyMethod)) {
					
					if ("1".equals(matchRuleDetailT.getMARK())) {
						if (!"".equals(NlpValue) && null != NlpValue) {
							if (null != NLPResult && !"".equals(NLPResult)) {
								stanStr = stanStr + "@|&" + NlpValue;
								;
								NLPResult = NLPResult + "@|&" + NlpValue;
							} else {
								NLPResult = NlpValue;
								stanStr = NlpValue;
							}
						} else {
							if(null != matchResult && !"".equals(matchResult)){
								if (null != standardizeResult && !"".equals(standardizeResult)
										&& !"none".equals(standardizeResult)) {
									stanStr = standardizeResult + "@|&" + matchResult;
									standardizeResult = standardizeResult + "@|&" + matchResult;
								} else {
									if (null != stanStr && !"".equals(stanStr)) {
										standardizeResult = stanStr + "@|&" + matchResult;
										stanStr = stanStr + "@|&" + matchResult;
									} else {
										standardizeResult = matchResult;
										stanStr = matchResult;
									}
								}
							}
						}
					} else if ("0".equals(matchRuleDetailT.getMARK())) {
						standardizeResult = matchResult;
						NLPResult = NlpValue;
						if (!"".equals(matchResult) && null != matchResult && !"none".equals(matchResult)) {
							stanStr = matchResult;
						} else if (!"".equals(NlpValue) && null != NlpValue && !"none".equals(NlpValue)) {
							stanStr = NlpValue;
						}
					}
				}

				if (null != matchRuleDetailT.getSUCCESS_REDIRECT_TO()
						&& "null" != matchRuleDetailT.getSUCCESS_REDIRECT_TO()
						&& !"".equals(matchRuleDetailT.getSUCCESS_REDIRECT_TO())) {
					matchRuleDetailT = cacheMap
							.get("memDataCode_" + matchRuleDetailT.getSUCCESS_REDIRECT_TO() + areaId);
				} else {
					bool = false;
				}
			} else {
				if ("matchNlp".equals(applyMethod)) {
					if ("1".equals(matchRuleDetailT.getMARK())) {
						if (!"".equals(NlpValue) && null != NlpValue) {
							if (null != NLPResult && !"".equals(NLPResult)) {
								stanStr = stanStr + "@|&" + NlpValue;
								;
								NLPResult = NLPResult + "@|&" + NlpValue;
							} else {
								NLPResult = NlpValue;
								stanStr = NlpValue;
							}
						} else {
							if (null != standardizeResult && !"".equals(standardizeResult)
									&& !"none".equals(standardizeResult)) {
								stanStr = standardizeResult + "@|&" + matchResult;
								standardizeResult = standardizeResult + "@|&" + matchResult;

							} else {
								if (null != stanStr && !"".equals(stanStr)) {
									standardizeResult = stanStr + "@|&" + matchResult;
									stanStr = stanStr + "@|&" + matchResult;
								} else {
									standardizeResult = matchResult;
									stanStr = matchResult;
								}
							}
						}
					} else if ("0".equals(matchRuleDetailT.getMARK())) {
						standardizeResult = matchResult;
						NLPResult = NlpValue;
						if (!"".equals(matchResult) && null != matchResult && !"none".equals(matchResult)) {
							stanStr = matchResult;
						} else if (!"".equals(NlpValue) && null != NlpValue && !"none".equals(NlpValue)) {
							stanStr = NlpValue;
						}
					}
				} else if ("matchNlpPRKS".equals(applyMethod) || "doubt".equals(applyMethod)
						|| "special".equals(applyMethod) || "match".equals(applyMethod)
						|| "matchPRKS".equals(applyMethod) || "inerver".equals(applyMethod)
						|| "matchPRKS23".equals(applyMethod) || "matchNlpPRKS23".equals(applyMethod)
						|| "specialPRKS23".equals(applyMethod) || "tsckZL".equals(applyMethod)
						|| "tsckSHZT".equals(applyMethod)) {
					if ("1".equals(matchRuleDetailT.getMARK())) {
						if (!"".equals(NlpValue) && null != NlpValue) {
							if (null != NLPResult && !"".equals(NLPResult)) {
								stanStr = stanStr + "@|&" + NlpValue;
								;
								NLPResult = NLPResult + "@|&" + NlpValue;
							} else {
								NLPResult = NlpValue;
								stanStr = NlpValue;
							}
						} else {
							if (null != standardizeResult && !"".equals(standardizeResult)
									&& !"none".equals(standardizeResult)) {
								stanStr = standardizeResult + "@|&" + matchResult;
								standardizeResult = standardizeResult + "@|&" + matchResult;

							} else {
								if (null != stanStr && !"".equals(stanStr)) {
									standardizeResult = stanStr + "@|&" + matchResult;
									stanStr = stanStr + "@|&" + matchResult;
								} else {
									standardizeResult = matchResult;
									stanStr = matchResult;
								}
							}
						}
					} else if ("0".equals(matchRuleDetailT.getMARK())) {
						standardizeResult = matchResult;
						NLPResult = NlpValue;
						if (!"".equals(matchResult) && null != matchResult && !"none".equals(matchResult)) {
							stanStr = matchResult;
						} else if (!"".equals(NlpValue) && null != NlpValue && !"none".equals(NlpValue)) {
							stanStr = NlpValue;
						}
					}
				}

				if (null != matchRuleDetailT.getFAILURE_REDIRECT_TO()
						&& "null" != matchRuleDetailT.getFAILURE_REDIRECT_TO()
						&& !"".equals(matchRuleDetailT.getFAILURE_REDIRECT_TO())) {
					matchRuleDetailT = cacheMap
							.get("memDataCode_" + matchRuleDetailT.getFAILURE_REDIRECT_TO() + areaId);
				} else {
					bool = false;
				}
			}
		}

		if (!"".equals(NLPResult) && null != NLPResult && !"".equals(nlpOrder) && null != nlpOrder) {
			nlpOrder = nlpOrder.replace("[", "");
			nlpOrder = nlpOrder.replace("]", "");
			nlpOrder = nlpOrder.replaceAll("\"", "");
			String nlpOrderStr[] = nlpOrder.split(",");

			if(nlpOrderStr != null && nlpOrderStr.length > 0){
				for(int mt = 0;mt < nlpOrderStr.length;mt ++){
					String nlpSSS = nlpOrderStr[mt];
					if(nlpSSS.indexOf("【") != -1){
						nlpSSS = nlpSSS.substring(0, nlpSSS.indexOf("【"));
					}
					JSONObject jsonObj = JSONObject.fromObject(NLPResult);
					String value = "";
					if(jsonObj.containsKey(nlpSSS)){
						value = jsonObj.getString(nlpSSS);
					}
					
					if ("".equals(stanStrNew)) {
						stanStrNew = nlpOrderStr[mt] + ":" + value;
					} else {
						stanStrNew = stanStrNew + "@,&" + nlpOrderStr[mt] + ":" + value;
					}
				}
			}
			
			/*JSONObject jsonObj = JSONObject.fromObject(NLPResult);
			Iterator<String> it = jsonObj.keys();
			int pp = 0;
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = jsonObj.getString(key);
				System.out.println(" key======>>" + key);
				System.out.println(" value====>>" + value);
				if (pp <= nlpOrderStr.length) {
					System.out.println(" nlpValue====>>" + nlpOrderStr[pp]);
				}
				resVal = value;
				if ("".equals(stanStrNew)) {
					stanStrNew = nlpOrderStr[pp] + ":" + value;
				} else {
					stanStrNew = stanStrNew + "@,&" + nlpOrderStr[pp] + ":" + value;
				}
				pp++;
			}*/
			finallyResult = stanStrNew;

			recordInfo(finallyResult, RECORD_INFO_ID, doubtDiag, special, DB_DATA_ID);
		} else {
			finallyResult = stanStr;

			String standName = "";
			if (null != paraMap) {
				for (Map.Entry<String, String> entry : paraMap.entrySet()) {
					System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
					if ("".equals(standName)) {
						standName = entry.getValue();
					} else {
						standName = standName + "/" + entry.getValue();
					}
				}
			}
			if (!"".equals(standName) && null != standName) {
				recordInfo(standName + ":" + finallyResult, RECORD_INFO_ID, doubtDiag, special, DB_DATA_ID);
			}
		}

		if (null != statusStr && !"".equals(statusStr)) {
			mtsRecordInfo = new MtsRecordInfo();
			mtsRecordInfo.setRECORD_INFO_ID(RECORD_INFO_ID);
			mtsRecordInfo.setSTANDARD_TYPE(statusStr);
			mtsRecordInfo.setOPERATE_TIME(new Date());
			mtsRecordService.editMtsRecordInfo(mtsRecordInfo);
		}

		resultJSON.put("result", finallyResult);
		resultJSON.put("status", statusStr);
		resultJSON.put("nlpResult", NlpValueStr);
		resultJSON.put("doubtDiag", doubtDiag);
		resultJSON.put("special", special);
		resultJSON.put("nlpOrder", nlpOrder);
		resultJSON.put("inver", inver);
		resultJSON.put("NLPDrug", NLPDrug);
		resultJSON.put("jsonarr", jsonarr);
		return resultJSON.toString();
	}

	public void recordInfo(String finallyResult, String RECORD_INFO_ID, String doubtDiag, String special,
			String DB_DATA_ID) throws Exception {
		MtsRecordDetail mtsRecordDetail = null;
		if (null != finallyResult && !"".equals(finallyResult)) {
			String[] finallyRes = null;
			if (finallyResult.contains("@,&")) {
				finallyRes = finallyResult.split("@,&");
			} else {
				finallyRes = new String[] { finallyResult };// 直接初始化
			}
			if (finallyRes != null && finallyRes.length > 0) {
				for (int p = 0; p < finallyRes.length; p++) {
					String finStr = finallyRes[p];
					if (finStr.contains(":")) {
						String nlpValue = finStr.substring(0, finStr.indexOf(":"));
						String nlpOrder = p + "";
						finStr = finStr.substring(finStr.indexOf(":") + 1);
						String finStrFinal[] = null;
						if (finStr.contains(",")) {
							finStrFinal = finStr.split(",");
						} else {
							finStrFinal = new String[] { finStr };
						}
						if (finStrFinal != null && finStrFinal.length > 0) {
							for (int uri = 0; uri < finStrFinal.length; uri++) {
								String finStrDan = finStrFinal[uri];

								if (finStrDan.contains("@#&")) {
									String[] fRes = finStrDan.split("@#&");
									if (fRes != null && fRes.length > 0) {
										String TERMINOLOGY_TYPE = fRes[fRes.length - 1];
										if ("none".equals(TERMINOLOGY_TYPE) && fRes.length == 4) {
											// 诊断词 @#& 诊断码 @#& 诊断附码 @#& 术语类型
											mtsRecordDetail = new MtsRecordDetail();
											mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
											mtsRecordDetail.setRECORD_DETAIL_ID(RandomUtil.getRandomId());
											mtsRecordDetail.setXY_STANDARD_WORD(fRes[0]);
											mtsRecordDetail.setXY_STANDARD_MAIN_CODE(fRes[1]);
											mtsRecordDetail.setXY_STANDARD_ATTACH_CODE(fRes[2]);
											mtsRecordDetail.setZY_STANDARD_WORD("");
											mtsRecordDetail.setZY_STANDARD_MAIN_CODE("");
											mtsRecordDetail.setSS_STANDARD_WORD("");
											mtsRecordDetail.setSS_STANDARD_MAIN_CODE("");
											mtsRecordDetail.setTERMINOLOGY_TYPE("0");
											mtsRecordDetail.setNLP_RESULT(nlpValue);
											mtsRecordDetail.setNLP_ORDER(nlpOrder);
											mtsRecordDetail.setSTATUS("1");
											mtsRecordDetail.setDB_DATA_ID(DB_DATA_ID);
											if ("1".equals(doubtDiag)) {
												mtsRecordDetail.setDOUBT_DIAG("1");
											} else {
												mtsRecordDetail.setDOUBT_DIAG("0");
											}
											if ("1".equals(special)) {
												mtsRecordDetail.setSPECIAL_CHARACTERS("1");
											} else {
												mtsRecordDetail.setSPECIAL_CHARACTERS("0");
											}
											mtsRecordDetail.setCAN_NOT_STANDARD_TYPE("");
											mtsRecordService.addMtsRecordDetail(mtsRecordDetail);
											System.out.println("诊断词==========>>" + fRes[0]);
											System.out.println("诊断码==========>>" + fRes[1]);
											System.out.println("诊断附码========>>" + fRes[2]);
											System.out.println("术语类型========>>" + fRes[3]);
										} else if ("概念转换".equals(TERMINOLOGY_TYPE)) {
											// 标准词1 @#& 标准词2 @#& 标准词3 @#& 标准词4
											// @#& 标准词 5 @#& 术语类型
											for (int j = 0; j < fRes.length - 1; j++) {
												mtsRecordDetail = new MtsRecordDetail();
												mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
												mtsRecordDetail.setRECORD_DETAIL_ID(RandomUtil.getRandomId());
												mtsRecordDetail.setXY_STANDARD_WORD(fRes[j]);
												mtsRecordDetail.setXY_STANDARD_MAIN_CODE("");
												mtsRecordDetail.setXY_STANDARD_ATTACH_CODE("");
												mtsRecordDetail.setZY_STANDARD_WORD("");
												mtsRecordDetail.setZY_STANDARD_MAIN_CODE("");
												mtsRecordDetail.setSS_STANDARD_WORD("");
												mtsRecordDetail.setSS_STANDARD_MAIN_CODE("");
												mtsRecordDetail.setTERMINOLOGY_TYPE("0");
												mtsRecordDetail.setSTATUS("1");
												mtsRecordDetail.setNLP_RESULT(nlpValue);
												mtsRecordDetail.setNLP_ORDER(nlpOrder);
												mtsRecordDetail.setDB_DATA_ID(DB_DATA_ID);
												if ("1".equals(doubtDiag)) {
													mtsRecordDetail.setDOUBT_DIAG("1");
												} else {
													mtsRecordDetail.setDOUBT_DIAG("0");
												}
												if ("1".equals(special)) {
													mtsRecordDetail.setSPECIAL_CHARACTERS("1");
												} else {
													mtsRecordDetail.setSPECIAL_CHARACTERS("0");
												}
												mtsRecordDetail.setCAN_NOT_STANDARD_TYPE("");
												mtsRecordService.addMtsRecordDetail(mtsRecordDetail);
											}
											System.out.println("标准词1==========>>" + fRes[0]);
											System.out.println("标准词2==========>>" + fRes[1]);
											System.out.println("标准词3==========>>" + fRes[2]);
											System.out.println("标准词4==========>>" + fRes[3]);
											System.out.println("标准词5==========>>" + fRes[4]);
											System.out.println("术语类型=========>>" + fRes[5]);
										} else if ("中医".equals(TERMINOLOGY_TYPE) && fRes.length == 7) {
											// 中医名 @#& 中医码 @#& 西医名1 @#& 西医码1 @#&
											// 西医名2 @#& 西医码2 @#& 术语类型
											mtsRecordDetail = new MtsRecordDetail();
											mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
											mtsRecordDetail.setRECORD_DETAIL_ID(RandomUtil.getRandomId());
											mtsRecordDetail.setTERMINOLOGY_TYPE("1");
											mtsRecordDetail.setZY_STANDARD_WORD(fRes[0]);
											mtsRecordDetail.setZY_STANDARD_MAIN_CODE(fRes[1]);
											mtsRecordDetail.setXY_STANDARD_WORD(fRes[2]);
											mtsRecordDetail.setXY_STANDARD_MAIN_CODE(fRes[3]);
											mtsRecordDetail.setXY_STANDARD_ATTACH_CODE("");
											mtsRecordDetail.setSS_STANDARD_WORD("");
											mtsRecordDetail.setSS_STANDARD_MAIN_CODE("");
											mtsRecordDetail.setSTATUS("1");
											mtsRecordDetail.setNLP_RESULT(nlpValue);
											mtsRecordDetail.setNLP_ORDER(nlpOrder);
											if ("1".equals(doubtDiag)) {
												mtsRecordDetail.setDOUBT_DIAG("1");
											} else {
												mtsRecordDetail.setDOUBT_DIAG("0");
											}
											if ("1".equals(special)) {
												mtsRecordDetail.setSPECIAL_CHARACTERS("1");
											} else {
												mtsRecordDetail.setSPECIAL_CHARACTERS("0");
											}
											mtsRecordDetail.setCAN_NOT_STANDARD_TYPE("");
											mtsRecordDetail.setDB_DATA_ID(DB_DATA_ID);
											mtsRecordService.addMtsRecordDetail(mtsRecordDetail);

											if (!"".equals(fRes[4]) && null != fRes[4]) {
												mtsRecordDetail = new MtsRecordDetail();
												mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
												mtsRecordDetail.setRECORD_DETAIL_ID(RandomUtil.getRandomId());
												mtsRecordDetail.setTERMINOLOGY_TYPE("1");
												mtsRecordDetail.setZY_STANDARD_WORD(fRes[0]);
												mtsRecordDetail.setZY_STANDARD_MAIN_CODE(fRes[1]);
												mtsRecordDetail.setXY_STANDARD_WORD(fRes[4]);
												mtsRecordDetail.setXY_STANDARD_MAIN_CODE(fRes[5]);
												mtsRecordDetail.setXY_STANDARD_ATTACH_CODE("");
												mtsRecordDetail.setSS_STANDARD_WORD("");
												mtsRecordDetail.setSS_STANDARD_MAIN_CODE("");
												mtsRecordDetail.setSTATUS("1");
												mtsRecordDetail.setNLP_RESULT(nlpValue);
												mtsRecordDetail.setNLP_ORDER(nlpOrder);
												if ("1".equals(doubtDiag)) {
													mtsRecordDetail.setDOUBT_DIAG("1");
												} else {
													mtsRecordDetail.setDOUBT_DIAG("0");
												}
												if ("1".equals(special)) {
													mtsRecordDetail.setSPECIAL_CHARACTERS("1");
												} else {
													mtsRecordDetail.setSPECIAL_CHARACTERS("0");
												}
												mtsRecordDetail.setCAN_NOT_STANDARD_TYPE("");
												mtsRecordDetail.setDB_DATA_ID(DB_DATA_ID);
												mtsRecordService.addMtsRecordDetail(mtsRecordDetail);
											}
											System.out.println("中医名==========>>" + fRes[0]);
											System.out.println("中医码==========>>" + fRes[1]);
											System.out.println("西医名1========>>" + fRes[2]);
											System.out.println("西医码1========>>" + fRes[3]);
											System.out.println("西医名2==========>>" + fRes[4]);
											System.out.println("西医码2========>>" + fRes[5]);
											System.out.println("术语类型========>>" + fRes[6]);
										} else if ("手术".equals(TERMINOLOGY_TYPE) && fRes.length == 3) {
											// 手术标准词 @#& 手术码 @#& 术语类型
											mtsRecordDetail = new MtsRecordDetail();
											mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
											mtsRecordDetail.setRECORD_DETAIL_ID(RandomUtil.getRandomId());
											mtsRecordDetail.setTERMINOLOGY_TYPE("2");
											mtsRecordDetail.setXY_STANDARD_WORD("");
											mtsRecordDetail.setXY_STANDARD_MAIN_CODE("");
											mtsRecordDetail.setXY_STANDARD_ATTACH_CODE("");
											mtsRecordDetail.setZY_STANDARD_WORD("");
											mtsRecordDetail.setZY_STANDARD_MAIN_CODE("");
											mtsRecordDetail.setSS_STANDARD_WORD(fRes[0]);
											mtsRecordDetail.setSS_STANDARD_MAIN_CODE(fRes[1]);
											mtsRecordDetail.setSTATUS("1");
											mtsRecordDetail.setNLP_RESULT(nlpValue);
											mtsRecordDetail.setNLP_ORDER(nlpOrder);
											if ("1".equals(doubtDiag)) {
												mtsRecordDetail.setDOUBT_DIAG("1");
											} else {
												mtsRecordDetail.setDOUBT_DIAG("0");
											}
											if ("1".equals(special)) {
												mtsRecordDetail.setSPECIAL_CHARACTERS("1");
											} else {
												mtsRecordDetail.setSPECIAL_CHARACTERS("0");
											}
											mtsRecordDetail.setCAN_NOT_STANDARD_TYPE("");
											mtsRecordDetail.setDB_DATA_ID(DB_DATA_ID);
											mtsRecordService.addMtsRecordDetail(mtsRecordDetail);

											System.out.println("手术标准词==========>>" + fRes[0]);
											System.out.println("手术码 ==========>>" + fRes[1]);
											System.out.println("术语类型========>>" + fRes[2]);
										} else if ("无法标化".equals(TERMINOLOGY_TYPE) && fRes.length == 2) {
											// 无法标化-类型 @#& 术语类型
											mtsRecordDetail = new MtsRecordDetail();
											mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
											mtsRecordDetail.setRECORD_DETAIL_ID(RandomUtil.getRandomId());

											mtsRecordDetail.setXY_STANDARD_WORD("");
											mtsRecordDetail.setXY_STANDARD_MAIN_CODE("");
											mtsRecordDetail.setXY_STANDARD_ATTACH_CODE("");
											mtsRecordDetail.setZY_STANDARD_WORD("");
											mtsRecordDetail.setZY_STANDARD_MAIN_CODE("");
											mtsRecordDetail.setSS_STANDARD_WORD("");
											mtsRecordDetail.setSS_STANDARD_MAIN_CODE("");

											mtsRecordDetail.setCAN_NOT_STANDARD_TYPE(fRes[0]);
											mtsRecordDetail.setSTATUS("2");
											mtsRecordDetail.setNLP_RESULT(nlpValue);
											mtsRecordDetail.setNLP_ORDER(nlpOrder);
											if ("1".equals(doubtDiag)) {
												mtsRecordDetail.setDOUBT_DIAG("1");
											} else {
												mtsRecordDetail.setDOUBT_DIAG("0");
											}
											if ("1".equals(special)) {
												mtsRecordDetail.setSPECIAL_CHARACTERS("1");
											} else {
												mtsRecordDetail.setSPECIAL_CHARACTERS("0");
											}
											mtsRecordDetail.setDB_DATA_ID(DB_DATA_ID);
											mtsRecordService.addMtsRecordDetail(mtsRecordDetail);
											System.out.println("无法标化-类型==========>>" + fRes[0]);
											System.out.println("术语类型===============>>" + fRes[1]);
										}
									}
								} else {
									System.out.println("============未匹配上");
									mtsRecordDetail = new MtsRecordDetail();
									mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
									mtsRecordDetail.setRECORD_DETAIL_ID(RandomUtil.getRandomId());
									mtsRecordDetail.setXY_STANDARD_WORD("");
									mtsRecordDetail.setXY_STANDARD_MAIN_CODE("");
									mtsRecordDetail.setXY_STANDARD_ATTACH_CODE("");
									mtsRecordDetail.setZY_STANDARD_WORD("");
									mtsRecordDetail.setZY_STANDARD_MAIN_CODE("");
									mtsRecordDetail.setSS_STANDARD_WORD("");
									mtsRecordDetail.setSS_STANDARD_MAIN_CODE("");
									mtsRecordDetail.setSTATUS("0");
									mtsRecordDetail.setNLP_RESULT(nlpValue);
									mtsRecordDetail.setNLP_ORDER(nlpOrder);
									mtsRecordDetail.setCAN_NOT_STANDARD_TYPE("");
									mtsRecordDetail.setDB_DATA_ID(DB_DATA_ID);
									mtsRecordService.addMtsRecordDetail(mtsRecordDetail);
								}
							}
						}
					}
				}
			}
			MtsRecordInfo mtsRecordInfo = new MtsRecordInfo();
			mtsRecordInfo.setRECORD_INFO_ID(RECORD_INFO_ID);
			mtsRecordInfo.setINFO_STATUS("1");
			mtsRecordInfo.setOPERATE_TIME(new Date());
			mtsRecordService.editMtsRecordInfo(mtsRecordInfo);
		}
	}
}
