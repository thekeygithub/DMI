package com.ts.util.Enum;

/**
 * 
 * ClassName: EnumStatus 
 * 
 * Status=1（成功）    (success) 
 * Status=404（页面丢失）(page_missing)
 * Status=500（服务器内部错误）( internal_server_error) 
 * Status=-1（非法身份） (illegalidentity) 
 * Status=999(非法使用) ，多次请求 (illegal_use), multiple requests
 * Status=100(未选择正确的API数据类型) (not_API_data_type) 
 * Status=-111(无权访问)  (Unauthorized_access) 
 * Status=-222(参数错误) (Parameter_error) 
 * Status=-2(请求超时)  (request_timeout) 
 * Status=-1000(错误码) (requst_error)
 * Status=-333(验签错误) (Check_error)
 * Status=101(人脸识别失败) (FaceComparisonPAY_fail)
 * 
 * *******************************************************************************************
 * 如果新增加错误状态码，需要在类
 * com.ts.controller.app.SearchAPI.ResultHandler.impl.ResultHandler;
 * 中statusList集合增加状态码信息
 * *******************************************************************************************
 * 
 * 
 * @Description: TODO
 * @author 李世博
 * @date 2016年10月28日
 */
public enum EnumStatus {
	success {
		public String getEnumValue() {
			return "1";
		}
	},
	illegal_identity {
		public String getEnumValue() {
			return "-1";
		}
	},
	page_missing {
		public String getEnumValue() {
			return "404";
		}
	},
	internal_server_error {
		public String getEnumValue() {
			return "500";
		}
	},
	illegal_use {
		public String getEnumValue() {
			return "999";
		}
	},
	not_API_data_type {
		public String getEnumValue() {
			return "100";
		}
	},
	Unauthorized_access {
		public String getEnumValue() {
			return "-111";
		}
	},
	request_timeout {
		public String getEnumValue() {
			return "-2";
		}
	},
	Parameter_error {
		public String getEnumValue() {
			return "-222";
		}
	},
	Check_error {
	public String getEnumValue() {
		return "-333";
		}
	},
	Requst_error {
		public String getEnumValue() {
			return "-1000";
		}
	},
	FaceComparisonPAY_fail {
		public String getEnumValue() {
			return "101";
		}
	};
	abstract public String getEnumValue();

}
