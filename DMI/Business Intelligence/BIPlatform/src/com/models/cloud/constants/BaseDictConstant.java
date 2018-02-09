package com.models.cloud.constants;

public class BaseDictConstant {
	/** VALID_FLAG	是否有效	1	有效 */
	public static final short VALID_FLAG_YES = 1;
	/**VALID_FLAG	是否有效	0	无效 */
	public static final short VALID_FLAG_NO = 0;
	/** CHAN_ACT_FLAG 渠道商户标识	1	渠道商户 */
	public static final short CHAN_ACT_FLAG_YES = 1;
	/**CHAN_ACT_FLAG 渠道商户标识	0	合作商户 */
	public static final short CHAN_ACT_FLAG_NO = 0;
	/** SP_OPEN_CHK_FLAG开户审核状态	0待审核 */
	public static final short SP_OPEN_CHK_FLAG_WAIT = 0;
	/** SP_OPEN_CHK_FLAG	开户审核状态	1	审核通过*/	
	public static final short SP_OPEN_CHK_FLAG_OK = 1;
	/** SP_OPEN_CHK_FLAG	开户审核状态	-1	驳回	*/	
	public static final short SP_OPEN_CHK_FLAG_REJECT= -1;
	/**IMAGE_TYPE_ID	图片类型	1	用户头像 */
	/**DEL_FLAG	删除标识	0	正常 */
	/**DEL_FLAG	删除标识	1	已删除 */
	/**CHILD_FLAG	是否末级节点	1	是 */
	/**CHILD_FLAG	是否末级节点	0	否 */
	/**INTF_RES_FLAG	接口结果状态	1	正常运行 */
	/**INTF_RES_FLAG	接口结果状态	0	运行错误 */
	/**INTF_LOG_FLAG	接口日志标识	1	需要记录接口日志 */
	/**INTF_LOG_FLAG	接口日志标识	0	不需要记录接口日志 */
	/**PHONE_OS_TYPE_ID	手机操作系统类型	1	Android */
	/**PHONE_OS_TYPE_ID	手机操作系统类型	2	IOS */
	/**LOGIN_FAIL_TYPE_ID	登录失败原因类型	101	密码错误 */
	/**LOGIN_FAIL_TYPE_ID	登录失败原因类型	199	其他 */
				
	/**BG_USER_TYPE_ID	后台用户类型	1	系统管理员 */
	/**BG_USER_TYPE_ID	后台用户类型	2	普通用户 */
	/**BG_USER_TYPE_ID	后台用户类型	3	后台程序 */
	/**BG_ROLE_TYPE_ID	后台角色类型	1	功能角色 */
	/**BG_ROLE_TYPE_ID	后台角色类型	2	数据角色 */
	/**BG_MENU_TYPE_ID	后台菜单类型	1	功能菜单 */
	/**BG_MENU_TYPE_ID	后台菜单类型	2	功能按钮 */
	/**PAY_ROLE_TYPE_ID	支付平台角色类型	1	通用角色 */
	/**PAY_ROLE_TYPE_ID	支付平台角色类型	2	特殊角色 */
	/**PAY_MENU_TYPE_ID	支付平台菜单类型	1	功能菜单 */
	/**PAY_MENU_TYPE_ID	支付平台菜单类型	2	无需权限菜单 */
	/**PAY_USER_STAT_ID	支付平台用户状态	1	正常 */
	/**PAY_USER_STAT_ID	支付平台用户状态	2	已禁用 */
	/**PAY_USER_STAT_ID	支付平台用户状态	9	已注销 */
	/**PAY_USER_TYPE_ID	支付平台用户类别	1	普通用户 */
	/**PAY_USER_TYPE_ID	支付平台用户类别	2	测试用户 */
	/**SP_USER_STAT_ID	商户用户状态	1	正常 */
	public static final short SP_USER_STAT_ID_NORMAL = 1;
	/**SP_USER_STAT_ID	商户用户状态	2	已禁用 */
	public static final short SP_USER_STAT_ID_FORBIDDEN = 2;
	/**SP_USER_STAT_ID	商户用户状态	9	已注销 */
	public static final short SP_USER_STAT_ID_CANCEL = 9;
	/**SP_USER_TYPE_ID	商户用户类别	1	普通用户 */
	public static final short SP_USER_TYPE_ID_NORMAL = 1;
	/**SP_USER_TYPE_ID	商户用户类别	2	测试用户 */
	public static final short SP_USER_TYPE_ID_TEST = 2;
	/**SP_ROLE_TYPE_ID	商户角色类型	1	菜单角色 */
	public static final short SP_ROLE_TYPE_ID_MENU = 1;
	/**SP_ROLE_TYPE_ID	商户角色类型	2	接口角色 */
	public static final short SP_ROLE_TYPE_ID_INTERFACE = 2;
	/**P_CERT_TYPE_ID	证件类型	1	身份证 */
	/**P_CERT_TYPE_ID	证件类型	2	军官证 */
	/**P_CERT_TYPE_ID	证件类型	3	护照 */
	/**P_CERT_TYPE_ID	证件类型	99	其他 */
	/**OP_USER_TYPE_ID	操作人类别	1	后台用户 */
	public static final short OP_USER_TYPE_ID_BACK = 1;
	/**OP_USER_TYPE_ID	操作人类别	2	支付平台用户 */
	public static final short OP_USER_TYPE_ID_PAY = 2;
	/**OP_USER_TYPE_ID	操作人类别	3	商户用户 */
	public static final short OP_USER_TYPE_ID_SUPPLIER = 3;
				
	/**ENT_TYPE_ID	企业类型	1	医疗机构 */
	public static final short ENT_TYPE_ID_MED_ORG = 1;
	/**ENT_TYPE_ID	企业类型	2	药店 */
	public static final short ENT_TYPE_ID_DRUG_STORE = 2;
	/**ENT_TYPE_ID	企业类型	11	社保123 */
	public static final short ENT_TYPE_ID_SOCIAL_SECURITY_123 = 11;
	/**ENT_TYPE_ID	企业类型	12	其他平台渠道 */
	public static final short ENT_TYPE_ID_OTHER = 12;
	/**ENT_TYPE_ID	企业类型	21	社保中心 */
	public static final short ENT_TYPE_ID_SOCIAL_SECURITY_CENTER = 21;
	/**PAY_ACT_TYPE_ID	支付账号类别	101	对公银行账号 */
	public static final short PAY_ACT_TYPE_ID_PUB = 101;
	/**PAY_ACT_TYPE_ID	支付账号类别	102	个人借记卡 */
	/**PAY_ACT_TYPE_ID	支付账号类别	103	个人信用卡 */
	/**PAY_ACT_TYPE_ID	支付账号类别	109	银联支付 */
	/**PAY_ACT_TYPE_ID	支付账号类别	201	支付宝账号 */
	/**PAY_ACT_TYPE_ID	支付账号类别	301	微信支付账号 */
	/**PAY_ACT_TYPE_ID	支付账号类别	401	医保个人账户 */
	/**PAY_ACT_TYPE_ID	支付账号类别	402	医保当年个人账户 */
	/**PAY_ACT_TYPE_ID	支付账号类别	403	医保历年个人账户 */	
	/**SI_CHK_FLAG	社保身份验证标识	0	未验证 */
	/**SI_CHK_FLAG	社保身份验证标识	1	已验证通过 */
	/**TD_BUSI_TYPE_ID	交易业务类型	201	缴费 */
	/**TD_BUSI_TYPE_ID	交易业务类型	202	挂号 */
	/**TD_BUSI_TYPE_ID	交易业务类型	203	购药 */
	/**TD_TYPE_ID	交易类型	101	支付 */
	public static final short TD_TYPE_ID_PAY = 101;
	/**TD_TYPE_ID	交易类型	111	转账 */
	public static final short TD_TYPE_ID_TRANS = 111;
	/**TD_TYPE_ID	交易类型	121	提现 */
	public static final short TD_TYPE_ID_WITHDRAW = 121;
	/**TD_TYPE_ID	交易类型	131	退款 */
	public static final short TD_TYPE_ID_REFUNDMENT = 131;
	/**TD_TYPE_ID	交易类型	141	充值 */
	public static final short TD_TYPE_ID_RECHARGE = 141;
	/**ACT_TYPE_ID	平台账户类型	1	平台私人账户 */
	public static final short ACT_TYPE_ID_PRI = 1;
	/**ACT_TYPE_ID	平台账户类型	2	平台商户账户 */
	public static final short ACT_TYPE_ID_SP = 2;
	/**ACT_STAT_ID	平台账户状态	1	正常 */
	public static final short ACT_STAT_ID_NORMAL = 1;
	/**ACT_STAT_ID	平台账户状态	9	冻结 */
	public static final short ACT_STAT_ID_FREEZEN = 9;
	/**TD_STAT_ID	交易状态	100	初始化 */
	public static final short TD_STAT_ID_INIT = 100;
	/**TD_STAT_ID	交易状态	101	进行中 */
	public static final short TD_STAT_ID_DOING = 101;
	/**TD_STAT_ID	交易状态	110	成功 */
	public static final short TD_STAT_ID_SUCCESS = 110;
	/**TD_STAT_ID	交易状态	120	失败 */
	public static final short TD_STAT_ID_FAIL = 120;
	/**TD_STAT_ID	交易状态	130	异常 */
	public static final short TD_STAT_ID_EXCEPTION= 130;
	/**CONFIRM_STAT_ID	对账状态	600	核对无误 */
	public static final short CONFIRM_STAT_ID_RIGHT = 600;
	/**CONFIRM_STAT_ID	对账状态	610	核对出错 */
	public static final short CONFIRM_STAT_ID_WRONG = 610;
	/**CONFIRM_STAT_ID	对账状态	619	待核对	待核对	619 */
	public static final short CONFIRM_STAT_ID_NONE = 619;
	/**IN_OUT_FLAG	收入支出标识	1	收入 */
	public static final short IN_OUT_FLAG_IN = 1;
	/**IN_OUT_FLAG	收入支出标识	2	支出 */
	public static final short IN_OUT_FLAG_OUT = 2;
	/**TD_RET_STAT_ID	退款状态	1	初始化 */
	public static final short TD_RET_STAT_ID_INI = 1;
	/**TD_RET_STAT_ID	退款状态	2	申请成功 */
	public static final short TD_RET_STAT_ID_UNDERWAY = 2;
	/**TD_RET_STAT_ID	退款状态	6	退款成功 */
	public static final short TD_RET_STAT_ID_SUCESS = 6;
	/**TD_RET_STAT_ID	退款状态	9	退款失败 */
	public static final short TD_RET_STAT_ID_FAILURE = 9;
	/**TD_WITHDR_STAT_ID	提现状态	1	初始化 */
	public static final short TD_WITHDR_STAT_ID_INIT = 1;
	/**TD_WITHDR_STAT_ID	提现状态	2	申请成功 */
	public static final short TD_WITHDR_STAT_ID_APPLY = 2;
	/**TD_WITHDR_STAT_ID	提现状态	6	提现成功 */
	public static final short TD_WITHDR_STAT_ID_SUCCESS = 6;
	/**TD_WITHDR_STAT_ID	提现状态	9	提现失败 */
	public static final short TD_WITHDR_STAT_ID_FAIL = 9;
	
	/** ACT_PERSON    是否实名认证  0 未实名认证*/
	public static final short ACT_PERSON_REAL_NAME_FLAG_FALSE = 0;
	/** ACT_PERSON    是否实名认证  1 已实名认证*/
	public static final short ACT_PERSON_REAL_NAME_FLAG_TRUE = 1;

	/**
	 * 生成支付凭证标识
	 */
	public static final String PAY_TOKEN_STRING_FLAG = "PayToken";
	public static final String PAY_TOKEN_STRING_FLAG_NO = "11";

	/**
	 * 生成退款订单号标识
	 */
	public static final String REFUND_ORDER_ID_FLAG = "RefundOrderId";
	public static final String REFUND_ORDER_ID_FLAG_NO = "6";

	/**
	 * 生成支付订单号标识
	 */
	public static final String PAY_ORDER_ID_FLAG = "PayOrderId";
	public static final String PAY_ORDER_ID_FLAG_NO = "12";

	/**
	 * 生成银行(第三方)账号表-商户标识
	 */
	public static final String SP_BANK_ACT_ID = "SpBankActId";
	public static final String SP_BANK_ACT_ID_NO = "13";
	/**
	 * 生成账户交易流水号标识
	 */
	public static final String ACCOUNT_TRADE_RECORD_ID_FLAG = "AccountTradeRecordId";
	public static final String ACCOUNT_TRADE_RECORD_ID_FLAG_NO = "1";
	/**
	 * 私人银行账号ID标识
	 */
	public static final String PERSON_BANK_ACCOUNT_ID_FLAG = "PersonBankAccountId";
	public static final String PERSON_BANK_ACCOUNT_ID_FLAG_NO = "16";
	/**
	 * 生成发送短信唯一ID
	 */
	public static final String SMS_ID_FLAG = "SMS";
	public static final String SMS_ID_FLAG_NO = "15";
	/**
	 * 提现交易流水号
	 * cashTradeRecordIdVo
	 */
	public static final String CASH_TRADE_RECORD_ID_FLAG = "CashTradeRecordId";
	public static final String CASH_TRADE_RECORD_ID_FLAG_NO = "16";
	/**
	 * 提现交易单
	 * cashTradeRecordIdVo
	 */
	public static final String CASH_TRADE_ORDER_ID_FLAG = "CashTradeOrderId";
	public static final String CASH_TRADE_ORDER_ID_FLAG_NO = "17";

	/**
	 * 短信日志表主键
	 */
	public static final String LOG_SMS_REC_ID_FLAG = "LogSmsRecId";
	public static final String LOG_SMS_REC_ID_FLAG_NO = "18";
	
	/**
	 * 生成主键号后顺序号位数
	 */
	public static final int EOP_FORMAT_SEQ_DIGIT = 3;

	/**
	 * 支付类型：绑卡支付
	 */
	public static final String PAYMENT_TYPE_BIND_CARD = "1";

	/**
	 * 支付类型：首次或绑卡过期储蓄卡
	 */
	public static final String PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_DEBIT_CARD = "2";

	/**
	 * 支付类型：首次或绑卡过期信用卡
	 */
	public static final String PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_CREDIT_CARD = "3";

	/**
	 * 易宝支付使用的交易币种：156-人民币
	 */
	public static final String YEEPAY_TRADE_CURRENCY_RMB = "156";

	/**
	 * 用户标识类型码：2-用户ID
	 */
	public static final String YEEPAY_IDENTITY_TYPE_USER_ID = "2";

	/**
	 * 终端设备类型：0：IMEI 1：MAC 2：UUID 3：OTHER
	 */
	public static final String YEEPAY_TERMINAL_TYPE_OTHER = "3";

	/**
	 * 1：建议需要进行短信校验
	 */
	public static final String YEEPAY_SUGGEST_NEED_SMS_VERIFICATION = "1";

	/**
	 * 短信验证码超过发送次数上限（目前同一笔订单最多支持发送5 次短信校验码）
	 */
	public static final String YEEPAY_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM = "600121";

	/**
	 * 短信验证码发送频率过高（目前同一笔订单发送短信验证码间隔≥50s）
	 */
	public static final String YEEPAY_SEND_VALIDATE_CODE_FREQUENCY_TOO_HIGH = "600122";

	/**
	 * 订单重复提交
	 */
	public static final String YEEPAY_ORDER_REPEAT_SUBMIT = "600049";

	/**
	 * 订单已支付
	 */
	public static final String YEEPAY_ORDER_ALREADY_PAYMENT_SUCCESS = "600050";

	/**
	 * 订单已成功
	 */
	public static final String YEEPAY_ORDER_ALREADY_TRADE_SUCCESS = "600090";

	/**
	 * 订单已过期或已撤销
	 */
	public static final String YEEPAY_ORDER_TIMEOUT_OR_CANCEL = "600110";

	/**
	 * 订单金额过低受限
	 */
	public static final String YEEPAY_ORDER_AMOUNT_TOO_LOW = "600113";

	/**
	 * 短信验证码错误
	 */
	public static final String YEEPAY_SMS_VERIFY_CODE_ERROR = "600116";

	/**
	 * 短信验证码无效或已过期
	 */
	public static final String YEEPAY_SMS_VERIFY_CODE_INVALID_OR_TIMEOUT = "600123";

	/**
	 * 短信验证码错误次数太多
	 */
	public static final String YEEPAY_SMS_VERIFY_CODE_ERROR_COUNT_TOO_MUCH = "600124";

	/**
	 * 可用余额不足
	 */
	public static final String YEEPAY_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH = "600102";

	/**
	 * 支付失败，请联系发卡银行
	 */
	public static final String YEEPAY_PAYMENT_FAILURE_600096 = "600096";

	/**
	 * 支付失败，请稍候重试
	 */
	public static final String YEEPAY_PAYMENT_FAILURE_600097 = "600097";

	/**
	 * 银行系统异常
	 */
	public static final String YEEPAY_PAYMENT_FAILURE_600106 = "600106";

	/**
	 * 支付失败
	 */
	public static final String YEEPAY_PAYMENT_FAILURE_600044 = "600044";

	/**
	 * 易宝支付结果通知-成功
	 */
	public static final String YEEPAY_PAYMENT_RESULT_SUCCESS = "SUCCESS";

	/**
	 * 易宝支付结果通知-失败
	 */
	public static final String YEEPAY_PAYMENT_RESULT_FAILURE = "FAILURE";

	/**
	 * 支付宝支付结果通知-成功
	 */
	public static final String ALIPAY_PAYMENT_RESULT_SUCCESS = "success";

	/**
	 * 支付宝支付结果通知-失败
	 */
	public static final String ALIPAY_PAYMENT_RESULT_FAILURE = "failure";

	/**
	 * 微信支付结果通知-成功
	 */
	public static final String WECHAT_PAYMENT_RESULT_SUCCESS = "SUCCESS";

	/**
	 * 微信支付结果通知-失败
	 */
	public static final String WECHAT_PAYMENT_RESULT_FAILURE = "FAIL";

	/**
	 * 建行支付结果通知-成功
	 */
	public static final String CCB_PAYMENT_RESULT_SUCCESS = "SUCCESS";

	/**
	 * 建行支付结果通知-失败
	 */
	public static final String CCB_PAYMENT_RESULT_FAILURE = "FAILURE";

	/**
	 * 银联支付结果通知-成功
	 */
	public static final String BKU_PAYMENT_RESULT_SUCCESS = "SUCCESS";

	/**
	 * 银联支付结果通知-失败
	 */
	public static final String BKU_PAYMENT_RESULT_FAILURE = "FAILURE";

	/**
	 * 支付类型：首次或绑卡过期储蓄卡
	 */
	public static final int PAYMENT_TYPE_FOB_EXPIRE_DEBIT_CARD_INT = 1;

	/**
	 * 银行卡类型名称：储蓄卡
	 */
	public static final String PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_DEBIT_CARD_INT = "储蓄卡";

	/**
	 * 支付类型：首次或绑卡过期信用卡
	 */
	public static final int PAYMENT_TYPE_FOB_EXPIRE_CREDIT_CARD_INT = 2;

	/**
	 * 银行卡类型名称：信用卡
	 */
	public static final String PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_CREDIT_CARD_INT = "信用卡";

	/**
	 * 银行卡类型名称：未知银行
	 */
	public static final String PAYMENT_TYPE_CARD_NAME_UNKNOWN = "未知银行";
	
	/**
	 * 冻结商户
	 */
	public static final String OPER_FREEZE_SP = "1";
	
	/**
	 * 解冻商户
	 */
	public static final String OPER_UNFREEZE_SP = "2";

	/**
	 * 当前使用资金平台
	 */
	public static final String CUR_PP_FUND_ID = "CUR_PP_FUND_ID";
	
	//字典表缓存
	/**
	 * 表DIM_DICT
	 */
	public static final String CACHE_DICT_DIM_DICT = "DIM_DICT";
	/**
	 * 表DIM_SYS_CONF
	 */
	public static final String CACHE_DICT_DIM_SYS_CONF = "DIM_SYS_CONF";

	/**
	 * 支付密码错误锁定次数
	 */
	public static final String PAY_PWD_FAIL_CNT = "PAY_PWD_FAIL_CNT";

	/**
	 * 支付密码错误锁定时长
	 */
	public static final String PAY_PWD_LOCK_TIME = "PAY_PWD_LOCK_TIME";

	/**
	 * 个人绑定银行卡数量上限
	 */
	public static final String P_BIND_BANK_MAX = "P_BIND_BANK_MAX";

	/**
	 * 个人绑定银行卡数量上限-默认3张
	 */
	public static final String P_BIND_BANK_MAX_DEFAULT = "3";

	/**
	 * 支付平台验证短信次数上限（每订单）
	 */
	public static final String SMS_PAY_MAX_PER_ORD = "SMS_PAY_MAX_PER_ORD";

	/**
	 * 支付平台验证短信发送间隔（秒每订单）
	 */
	public static final String SMS_PAY_BETWE_SEC = "SMS_PAY_BETWE_SEC";

	/**
	 * 交易序号
	 */
	public static final double TRANSACTION_SERIAL_NUMBER = 10000000;

	/**
	 * 支付结果通知or交易单查询防并发redis key
	 */
	public static final String PAYMENT_RESULT_REDIS_KEY = "paymentResultProcess_{payOrderId}";

	/**
	 * 600095:卡信息或银行预留手机号有误，请更改信息并重新获取新验证码
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_CODE_600095 = "600095";

	/**
	 * 600095:卡信息或银行预留手机号有误，请更改信息并重新获取新验证码
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_MESSAGE_600095 = "卡信息或银行预留手机号有误，请更改信息并重新获取新验证码";

	/**
	 * 600102:银行卡余额不足，请核实
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_CODE_600102 = "600102";

	/**
	 * 600102:银行卡余额不足，请核实
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_MESSAGE_600102 = "银行卡余额不足，请核实";

	/**
	 * 600051:无效或不支持的卡号，请换卡支付
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_CODE_600051 = "600051";

	/**
	 * 600051:无效或不支持的卡号，请换卡支付
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_MESSAGE_600051 = "无效或不支持的卡号，请换卡支付";

	/**
	 * 601036:姓名错误，请重新输入
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_CODE_601036 = "601036";

	/**
	 * 600093:本卡在该商户不允许此交易，请联系收单机构
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_CODE_600093 = "600093";

	/**
	 * 601036:姓名错误，请重新输入
	 */
	public static final String YEEPAY_PAY_RESULT_ERROR_MESSAGE_601036 = "姓名错误，请重新输入";

	/**
	 * 同一张卡最多被5人绑定
	 */
	public static final int YEEPAY_MANY_PEOPLE_BIND_SAME_CARD_MAX = 5;

	/**
	 * 证件类型：身份证
	 */
	public static final String CERT_TYPE_ID_SHENFENZHENG = "01";

	/**
	 * 是否主操作，1是0否
	 */
	public static final String MAIN_FLAG_YES = "1";
	/**
	 * 是否主操作，1是0否
	 */
	public static final String MAIN_FLAG_NO = "0";

	/**
	 * 设置yeepay订单最小有效期（单位：分钟）
	 */
	public static final int YEEPAY_MIN_ORDER_EXP_DATE = 5;

	/**
	 * 日志类型1:用户修改登录账号
	 */
	public static final Short LOG_OPER_ACT_TYPE_ID = 1;

	/** 是否加密处理 是 */
	public static final short ENCRYPT_FLAG_YES = 1;
	
	/** 实名认证uc_p_cert_rec */
	public static final short UC_P_CERT_REC_VALID_FLAG_YES = 1;
	
	/** PP_MI_PROJECT 0无效 */
	public static final short PP_MI_PROJECT_VALID_FLAG_TRUE = 1;
	/** PP_MI_PROJECT 1有效*/
	public static final short PP_MI_PROJECT_VALID_FLAG_FALSE = 0;
	/** UC_BIND_AUTH_COL 0无效 */
	public static final short UC_BIND_AUTH_COL_VALID_FLAG_TRUE = 1;
	/** UC_BIND_AUTH_COL 1有效*/
	public static final short UC_BIND_AUTH_COL_VALID_FLAG_FALSE = 0;
	
	
	/** UC_BIND_MI 0副卡 */
	public static final short UC_BIND_MI_MAIN_BIND_FLAG_FALSE = 0;
	/** UC_BIND_MI 1主卡 */
	public static final short UC_BIND_MI_MAIN_BIND_FLAG_TRUE = 1;
	/** UC_BIND_MI 0无效 */
	public static final short UC_BIND_MI_UC_BIND_STAT_ID_DISABLED = 0;
	/** UC_BIND_MI 1有效 */
	public static final short UC_BIND_MI_UC_BIND_STAT_ID_ABLED = 1;
	/** UC_BIND_MI 2删除 */
	public static final short UC_BIND_MI_UC_BIND_STAT_ID_DEL = 2;
	
	/** UC_BIND_MI 参保人权限类型 1.全部权限 2.仅养老库权限 3.仅医保库权限 */
	public static final short UC_BIND_MI_KF_PRIV_TYPE_ID_1 = 1;
	public static final short UC_BIND_MI_KF_PRIV_TYPE_ID_2 = 2;
	public static final short UC_BIND_MI_KF_PRIV_TYPE_ID_3 = 3;

	public static final String WEB_TERMINAL_ACCESS_REDIS_KEY = "WEB_TERMINAL_ACCESS_REDIS_KEY";

	/**
	 * 医疗类别-医院门诊
	 */
	public static final String MEDICAL_CLASS_HOSPITAL_CLINIC = "1";
	/**
	 * 医疗类别-药店购药
	 */
	public static final String MEDICAL_CLASS_BUYING_MEDICINE = "2";

	/**
	 * 参保人员类别-职工
	 */
	public static final String INSURED_PERSON_TYPE_EMPLOYEE = "1";
	/**
	 * 参保人员类别-居民
	 */
	public static final String INSURED_PERSON_TYPE_RESIDENT = "2";

	/**
	 * 医保退款任务表主键前缀
	 */
	public static final String MEDICAL_REFUND_PREFIX = "5";

	public static final String PP_FUND_TYPE_ID_EBZF = "EBZF";

	public static final String PP_FUND_TYPE_ID_ALI = "ALI";

	public static final String PP_FUND_TYPE_ID_ALI_IN = "ALI-IN";

	public static final String PP_FUND_TYPE_ID_CCB = "CCB";

	public static final String PP_FUND_TYPE_ID_BKU = "BKU";

	public static final String PP_FUND_TYPE_ID_WECHAT = "WECHAT";

	/**
	 * 商户多种资金通道
	 */
	public static final String MERCHANT_FUND_ID_MULTIPLE = "MULTIPLE";

	/**
	 * 医保支付是否安全距离 1:是
	 */
	public static final short PAY_SAFE_FLAG_YES = 1;

	/**
	 * 医保支付是否安全距离 0:否
	 */
	public static final short PAY_SAFE_FLAG_NO = 0;

	public static final String FACE_RECOGNITION_MAX_COUNT_TIPS = "\n\n提示：验证失败可能是社保局留存的本人照片与目前差距较大，建议本人携带身份证和社保卡原件去当地社保局办理更新。";
}
