package com.pay.cloud.util.hint;

public enum Hint {
//############################################################ 系统提示  start ############################################################
    /** 成功 */
    SYS_SUCCESS(0),
    /** 无权限，拒绝访问 */
    SYS_403_ERROR(403),
    /** 不存在此服务*/
    SYS_404_ERROR(404),
    /** 服务端发生未知错误*/
    SYS_500_ERROR(500),
    /** 未登录*/
    SYS_1000_NOT_LOGIN_ERROR(1000),
    /** 剔除已登录SESSION出错*/
    SYS_1001_DELETE_LOGINSESSION_ERROR(1001),
    /** 上传文件失败*/
    SYS_1100_UPLOAD_FILE_ERROR(1100),
    /** 加载字典失败*/
    SYS_1200_LOAD_DICT_ERROR(1200),
    /** 系统错误*/
    SYS_10001_SYSTEM_ERROR(10001),
    /** 参数错误*/
    SYS_10002_PARAM_ERROR(10002),
    /** 验签错误*/
    SYS_10003_SIGN_ERROR(10003),
    /** {param}不能为空*/
    SYS_10004_PARAM_NOT_NULL_ERROR(10004),
    /** 系统繁忙*/
    SYS_10005_SYSTEM_BUSY_ERROR(10005),
    /** 访问接口受限*/
    SYS_10006_ACCESS_INTERFACE_LIMIT_ERROR(10006),
    /** 访问接口不存在 */
    SYS_10007_ACCESS_INTERFACE_NOTEXIST_ERROR(10007),
    /** 访问接口异常*/
    SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR(10008),
    /** {param}不合法*/
    SYS_10009_PARAM_INVALID_ERROR(10009),
    /** 请求参数为空*/
    SYS_10010_REQUEST_PARAM_NULL_ERROR(10010),
    /** 请求第三方接口异常 */
    SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION(10011),
    /**未知渠道appid*/
    SYS_10012_APPID_ERROR(10012),
    /**无效用户*/
    SYS_10013_VALID_FLAG_NO(10013),
    /**{param}参数错误*/
    SYS_10014_VALID_ERROR(10014),
    /**并发请求已阻止*/
    SYS_10015_INTERFACE_CONCURRENT_PREVENT(10015),
    /**长时间未操作，订单超时，请关闭重新支付*/
    SYS_10016_ORDER_OVER_TIME(10016),
//############################################################ 系统提示  end ############################################################
    
    
//############################################################  优化接口的验证码 start############################################################
    /**用户编号不能为空*/
    SYS_10017_ERROR_USER_ID(10017),
    /**银行卡号不能为空*/
    SYS_10018_ERROR_BANK_CODE(10018),
    /**身份证号不能为空*/
    SYS_10019_ERROR_CARD_NO(10019),
    /**身份证号输入格式不符，请重新输入*/
    SYS_10020_ERROR_CARD_NO(10020),
    /**姓名不能为空*/
    SYS_10021_ERROR_CARD_NAME(10021),
    /**姓名限制2-25字符*/
    SYS_10022_CARDNAME_INVALID_ERROR(10022),
    /**城市编号不能为空*/
    SYS_10023_ERROR_CITY_ID(10023),
    /**手机号不能为空*/
    SYS_10024_USER_CODE_NOT_NULL_ERROR(10024),
    /**密码不能为空*/
    SYS_10025_PASSWORD_NOT_NULL_ERROR(10025),
    /**手机号输入格式不符，请重新输入*/
    SYS_10026_PHONE_INVALID_ERROR(10026),
    /**支付密码不能为空*/
    SYS_10027_PAY_PASSWORD_NOT_NULL_ERROR(10027),
    /**用户账户不能为空*/
    SYS_10028_ACCOUNTID_NOT_NULL_ERROR(10028),
    /**用户账户不正确*/
    SYS_10029_ACCOUNTID_INVALID_ERROR(10029),
    /**支付通行证不能为空*/
    SYS_10030_PAYTOKEN_NOT_NULL_ERROR(10030),
    /**银行卡号不能为空*/
    SYS_10031_CARDNO_NOT_NULL_ERROR(10031),
    /**银行卡号输入格式不符，请重新输入*/
    SYS_10032_CARDNO_INVALID_ERROR(10032),
    /**商户订单号不能为空*/
    SYS_10034_MERORDERID_NOT_NULL_ERROR(10034),
    /**商户订单有效期不正确*/
    SYS_10035_MERORDEREXPIRE_INVALID_ERROR(10035),
    /**商品描述信息不能为空*/
    SYS_10036_GOODSDESC_NOT_NULL_ERROR(10036),
    /**合作商户账号编号不能为空*/
    SYS_10037_WORKSPACTID_NOT_NULL_ERROR(10037),
    /**合作商户账号编号不正确*/
    SYS_10038_WORKSPACTID_INVALID_ERROR(10038),
    /**交易业务类型不能为空*/
    SYS_10039_TRANSBUSITYPE_NOT_NULL_ERROR(10039),
    /**交易业务类型不正确*/
    SYS_10040_TRANSBUSITYPE_INVALID_ERROR(10040),
    /**支付金额不能为空*/
    SYS_10041_PAYMONEY_NOT_NULL_ERROR(10041),
    /**支付金额不正确*/
    SYS_10042_PAYMONEY_INVALID_ERROR(10042),
    /**商户后台系统的回调地址不能为空*/
    SYS_10043_CALLBACKURL_NOT_NULL_ERROR(10043),
    /**支付类型不能为空*/
    SYS_10044_PAYMENTTYPE_NOT_NULL_ERROR(10044),
    /**绑卡编号不能为空*/
    SYS_10045_BINDID_NOT_NULL_ERROR(10045),
    /**证件类型不能为空*/
    SYS_10045_IDCARDTYPE_NOT_NULL_ERROR(10045),
    /**证件类型输不正确*/
    SYS_10046_IDCARDTYPE_INVALID_ERROR(10046),
    /**支付类型格式不正确*/
    SYS_10047_PAYMENTTYPE_INVALID_ERROR(10047),
    /**支付平台订单号不能为空*/
    SYS_10048_PAYORDERID_NOT_NULL_ERROR(10048),
    /**密码不能为空*/
    SYS_10049_PASSWORD_NOT_NULL_ERROR(10049),
    /**手机号不能为空*/
    SYS_10050_USERCODE_NOT_NULL_ERROR(10050),
    /**手机号输入格式不符，请重新输入*/
    SYS_10051_USERCODE_INVALID_ERROR(10051),
    /**验证码不能为空*/
    SYS_10052_VERIFYCODE_NOT_NULL_ERROR(10052),
    /**用户编号不能为空*/
    SYS_10053_USERID_NOT_NULL_ERROR(10053),
    /**原手机号不能为空*/
    SYS_10054_OLDUSERCODE_NOT_NULL_ERROR(10054),
    /**新手机号不能为空*/
    SYS_10055_NEWUSERCODE_NOT_NULL_ERROR(10055),
//############################################################  优化接口的验证码 end ############################################################

//############################################################ 短信提示  start ############################################################
    /** 发送短信验证码失败*/
    SMS_60000_SEND_FAILED(60000),
    /** 发送验证码过于频繁，发送失败*/
    SMS_60001_SED_FREQUENTLY(60001),
    /** 手机号格式不正确，发送失败*/
    SMS_60002_PHONE_FORMAT_ERROR(60002),
    /** 验证验证码失败*/
    SMS_60003_CHECKVERIFYCODE_FAILED(60003),
    /** 验证码不存在，验证失败*/
    SMS_60004_VERIFYCODE_NONE(60004),
    /** 验证码超出有效期，验证失败*/
    SMS_60005_CHECKVERIFYCODE_TIMEOUT(60005),
    /** 验证码不一致，验证失败 */
    SMS_60006_VERIFYCODE_DIFFERENT(60006),
    /** 手机号码已绑定 */
    SMS_60007_THEPHONE_REGISTERED(60007),
    /** 手机号码未绑定*/
    SMS_60008_THEPHONE_UNDEFINE(60008),
    /** 手机号验证操作未执行*/
    SMS_60009_PHONEUNVERIFY_FAILED(60009),
    
    /** 手机号（phone）验证不通过！ */
    SMS_60011_PHONE_FORMAT_FAILED(60011),

    /** 请求类型（operateType）验证不通过！*/
    SMS_60012_OPERATETYPE_FORMAT_FAILED(60012),

    /** 硬件ID（hardWareId）验证不通过！*/
    SMS_60013_HARDWAREID_FORMAT_FAILED(60013),

    /** 验证码格式（verifyCode）验证不通过！*/
    SMS_60014_VERIFYCODE_FORMAT_FAILED(60014),
    
    /** 发送短信验证码抛出异常！*/
    SMS_60015_SMSSENDCODE_EXCEPTION(60015),

    /** 验证短信验证码抛出异常！*/
    SMS_60016_SMSVALIDATECODE_EXCEPTION(60016),
    
    /** 验证码已使用！*/
    SMS_60017_VERIFYCODEUSED_FAILED(60017),
    /** 请求类型不一致 */
	SMS_60018_OPERATETYPE_FAILED(60018),
	/** 发送短信失败*/
    SMS_60019_SEND_FAILED(60019),
    /** 获取验证码次数不能大于五次*/
    SMS_60020_SENDCOUNTOUT_FAILED(60020),
//############################################################ 短信提示  end ############################################################

//############################################################ 用户提示  start ############################################################
    /*** 登录状态超时失效，请重新登录*/
    USER_25001_LOGINSTATUS_FAILED(25001),
    /** 注册抛出异常*/
    USER_25004_REGISTER_THROWERROR(25004),
    /** 用户名已被使用*/
    USER_25005_USERNAME_REGISTERED(25005),
    /** 用户名为空*/
    USER_25006_USERNAME_EMPTY(25006),
    /** 用户已经登录*/
    USER_25007_DUPLICATE_LOGIN(25007),
    /** 密码验证不通过*/
    USER_25008_VALIDATEPASS_FAILED(25008),
    /** 用户不存在*/
    USER_25009_USER_UNDEFINED(25009),
    /** 账号或密码错误，请重新输入 */
    USER_25010_USER_LOGIN_FAILED(25010),
    /** 你的e保钱包账号于{0}在另一台{1}设备上登录，请确认是否本人操作，非本人操作请尽快修改登录密码 */
    USER_25011_DEVICEDIFFERENT_FAILED(25011),
    /**  用户被禁用 */
    USER_25012_USER_FORBIDDEN(25012),
    /** 用户已注销 */
    USER_25013_USER_WRITTEN_OFF(25013),
    /** 密码输入错误{0}次，账号已被锁定，请{1}分钟之后重试 */
    USER_25014_PASS_CONTINUOUS_ERRO(25014),
    /** 绑定手机号抛出异常 */
    USER_25015_BINDPHONE_THROWERROR(25015),
    /** 注销抛出异常 */
    USER_25016_LOGINOUT_THROWERROR(25016),
    /** 第三方推送密码失败 */
    USER_25017_PUSHPASS_FAILED(25017),
    /** 密码找回抛出异常 */
    USER_25018_PASSCALLBACK_THROWERROR(25018),
    /** 密码验证抛出异常 */
    USER_25019_VALIDATEPASS_THROWERROR(25019),
    /** 修改密码抛出异常 */
    USER_25020_UPDATEPASS_THROWERROR(25020),
    /** 意见反馈抛出异常 */
    USER_25021_ADVICE_THROWERROR(25021),
   
    
    /**  用户编号（userId）验证不通过！ */
    USER_25022_USERID_FAILED(25022),
    
    /** 获取支持银行卡列表抛出异常！ */
    USER_25023_BANKCARDLIST_EXCEPTION(25023),
    
    /**
     * 获取配置文件抛出异常！
     */
    USER_25024_CONFIGURATION_EXCEPTION(25024),
    
    /** 会员名（userCode）验证不通过！ */
    USER_25025_USERCODE_FAILED(25025),
    
    /** 密码（password）验证不通过！ */
    USER_25026_PASSWORD_FAILED(25026),
    /** 用户登录抛出异常！*/
    USER_25027_LOGIN_EXCEPTION(25027),
    
    /** 设置登录密码抛出异常！*/
    USER_25028_RESTLOGINPASS_EXCEPTION(25028),
    
    /** 注册抛出异常！*/
    USER_25029_REGISTER_EXCEPTION(25029),
    /** 用户账户编号（accountId）验证不通过！ */
    USER_25030_ACCOUNTID_FAILED(25030),
    /** 设置支付密码抛出异常！ */
    USER_25031_SETPAYMENTPASS_EXCEPTION(25031),
    /** 登出系统抛出异常！*/
    USER_25032_LOGINOUT_EXCEPTION(25032),
    /** 支付密码存在，不能设置支付密码！*/
    USER_25033_SETPAYMENTPASS_FAILED(25033),
    /** 获取医保项目ID失败 */
    USER_25034_OBTAINMIC_FAILED(25034),
    /** 解除银行卡绑定失败 */
    USER_25035_UNBINDBACKCARD_FAILED(25035),
    
    /** 支付用户信息不存在 */
    USER_11001_LOGININFO_NOTEXIST(11001),
    /** 支付用户状态不正确 */
    USER_11002_USER_STATUS_INVALID(11002),
    /** 支付用户账户信息不存在 */
    USER_11003_ACCOUNTINFO_NOTEXIST(11003),
    /** 支付用户账户状态不正确 */
    USER_11004_ACCOUNT_STATUS_INVALID(11004),
    /** 支付密码错误，您还可以输入{X}次 */
    USER_11005_PAY_PASSWORD_ERROR(11005),
    /** 支付通行证过期或不存在  */
    USER_11006_PAYTOKEN_INVALID(11006),
    /** 当前设备硬件ID与支付通行证中不一致 */
    USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID(11007),
    /** 支付通行证无效 */
    USER_11008_PAYTOKEN_INVALID(11008),
    /** 您还未设置支付密码 */
    USER_11009_NOT_SET_PAY_PASSWORD(11009),
    /** 支付密码输入错误{X}次，账号已被锁定，请{Y}分钟之后重试 */
    USER_11010_PAY_PASSWORD_LOCKED(11010),
    /** 通行证失效或不存在 */
    USER_11011_TOKEN_INVALID(11011),
    /** 当前用户未实名认证 */
    USER_11012_NOT_REAL_NAME(11012),
    /** 今日验证已达上限{X}次，不能进行医保结算，请明天再试 */
    USER_11013_FACE_ERROR(11013),
    /** 人脸识别验证失败，你可以继续扫描识别 */
    USER_11014_FACE_ERROR(11014),
    /** 人脸活体验证今日已达上限，您可以使用本人银行卡验证身份 */
    HY_11015_HTRZ_ERROR(11015),
    /** 人脸活体认证失败，你可以继续扫描识别 */
    HY_11016_HTRZ_ERROR(11016),
    /** 姓名与身份证不匹配，请核对后修改 */
    HY_11017_HTRZ_ERROR(11017),
    /** 人脸照片比对失败，您可以使用本人银行卡验证身份 */
    HY_11018_HTRZ_ERROR(11018),
    /** 数据连接失败，请稍候再试 */
    GM_11030_HTRZ_ERROR(11030),
    /**人脸识别匹配度过低，请重新扫描*/
    GM_11031_HTRZ_ERROR(11031),
    /**未找到匹配的照片，暂不能进行医保账户验证*/
    GM_11032_HTRZ_ERROR(11032),
    
//############################################################ 用户提示  end ############################################################

//############################################################ 交易提示  start ############################################################
    /** 渠道商户账户信息不存在 */
    CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST(13001),
    /** 渠道商户账户状态不正确 */
    CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID(13002),
    /** 渠道商户订单号已存在 */
    CHAN_USER_13003_MERORDERID_EXIST(13003),
    /** 支付订单信息不存在或已失效 */
    PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID(13004),
    /** 渠道商户订单号无效 */
    PAY_ORDER_13005_MERORDERID_INVALID(13005),
    /** 短信验证码超过发送次数上限 */
    YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM(13006),
    /** 短信验证码发送频率过高 */
    YEEPAY_13007_SEND_VALIDATE_CODE_FREQUENCY_TOO_HIGH(13007),
    /** 订单重复提交 */
    YEEPAY_13008_ORDER_REPEAT_SUBMIT(13008),
    /** 订单已成功 */
    YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS(13010),
    /** 订单已过期或已撤销 */
    YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL(13011),
    /** 订单金额过低受限 */
    YEEPAY_13012_ORDER_AMOUNT_TOO_LOW(13012),
    /** 短信验证码错误 */
    YEEPAY_13013_SMS_VERIFY_CODE_ERROR(13013),
    /** 短信验证码无效或已过期 */
    YEEPAY_13014_SMS_VERIFY_CODE_INVALID_OR_TIMEOUT(13014),
    /** 短信验证码错误次数太多 */
    YEEPAY_13015_SMS_VERIFY_CODE_ERROR_COUNT_TOO_MUCH(13015),
    /** 可用余额不足*/
    YEEPAY_13016_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH(13016),
    /** 支付失败 */
    YEEPAY_13017_PAYMENT_FAILURE(13017),
    /** 资金平台无效或不存在 */
    PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND(13018),
    /** 支付订单扩展信息不存在或已失效 */
    PAY_ORDER_13019_ORDERINFO_EXT_NOT_EXIST_OR_INVALID(13019),
    /** 无效的绑卡ID */
    PAY_ORDER_13040_INVALID_CARD_BIND_ID(13040),
    /** 银行预留手机号有误 */
    PAY_ORDER_13041_CARDINFO_OR_RESERVE_PHONE_INVALID(13041),
    /** 无效或不支持的卡号，请换卡支付 */
    PAY_ORDER_13042_CARD_NO_INVALID_OR_NOT_SUPPORTED(13042),
    /** 卡已过期，请换卡重新支付 */
    PAY_ORDER_13043_BANK_CARD_OVERDUE(13043),
    /** 该卡为储蓄卡，请用信用卡支付 */
    PAY_ORDER_13044_MUST_USE_CREDIT_CARD(13044),
    /** 该卡为信用卡，请用储蓄卡支付 */
    PAY_ORDER_13045_MUST_USE_DEBIT_CARD(13045),
    /** 本卡被发卡方没收，请联系发卡银行 */
    PAY_ORDER_13046_CARD_BE_CONFISCATED(13046),
    /** 本卡未激活或睡眠卡，请联系发卡银行 */
    PAY_ORDER_13047_CARD_NOT_ACTIVE_OR_SLEEP(13047),
    /** 个人绑定银行卡数量上限 */
    PAY_ORDER_13048_PERSONAL_BIND_BANK_CARD_MAX(13048),
    /** 请求频次过高，请过几秒重试 */
    PAY_ORDER_13049_601101(13049),
    /** 订单金额过低受限 */
    PAY_ORDER_13050_600113(13050),
    /** 单卡超过当日累积支付限额 */
    PAY_ORDER_13051_600043(13051),
    /** 单卡超过单笔支付限额 */
    PAY_ORDER_13052_600045(13052),
    /** 单卡超过单月累积支付限额 */
    PAY_ORDER_13053_600046(13053),
    /** 单卡超过单日累积支付次数上限 */
    PAY_ORDER_13054_600047(13054),
    /** 单卡超过单月累积支付次数上限 */
    PAY_ORDER_13055_600048(13055),
    /** 消费金额超限，请联系发卡银行 */
    PAY_ORDER_13056_600098(13056),
    /** 商户状态冻结不能进行交易 */
    PAY_ORDER_13057_611110(13057),
    /** CVV2输入格式不符，请重新输入 */
    PAY_ORDER_13058_600023(13058),
    /** 有效期输入格式不符，请重新输入 */
    PAY_ORDER_13059_600024(13059),
    /** 身份证输入格式不符，请重新输入 */
    PAY_ORDER_13060_600119(13060),
    /** 资金平台交易流水无效或不存在 */
    PAY_ORDER_13061_FUND_ID_CODE_INVALID(13061),
    /** 渠道账户ID不能为空 */
    PAY_ORDER_13062_CHANNEL_ACCOUNT_ID_NOT_NULL(13062),
    /** 合作商户账户信息不存在 */
    WORK_MERCHANT_13063_ACCOUNT_ID_NOT_NULL(13063),
    /** 合作商户账户状态不正确 */
    WORK_MERCHANT_13064_ACCOUNT_STATUS_INVALID(13064),
    /** 合作商户所属渠道账户ID不合法 */
    WORK_MERCHANT_13065_BELONGTO_CHANNEL_ACCOUNT_INVALID(13065),
    /** 订单状态不合法 */
    PAY_ORDER_13066_TRADE_STATUS_INVALID(13066),
    /** 姓名格式不符，请重新输入 */
    PAY_ORDER_13068_USER_NAME_INVALID(13068),
    /** 手机号输入格式不符，请重新输入 */
    PAY_ORDER_13070_USER_PHONE_INVALID(13070),
    /** 输入姓名有误 */
    PAY_ORDER_13071_INPUT_USERNAME_INVALID(13071),
    /** 请确认身份证号是否正确 */
    PAY_ORDER_13072_INPUT_USER_IDCARD_INVALID(13072),
    /** 订单状态更新失败 */
    PAY_ORDER_13073_STATUS_UPDATE_FAIL(13073),
    /** 用户撤销订单 */
    PAY_ORDER_13074_QUIT_ORDER(13074),
    /** 订单状态不是成功或者失败 */
    PAY_ORDER_13075_ORDER_NOT_TOP(13075),
    /** 卡信息或银行预留手机号有误，请联系银行客服 */
    PAY_ORDER_13076_600095(13076),
    /** 该卡暂不支持，请换卡支付 */
    BANK_CARD_CHECK_13077(13077),
    /** 该卡已被超过{X}人绑定，请换卡支付 */
    BANK_CARD_CHECK_13078(13078),
    /** 有效期输入格式不符，请重新输入 */
    PAY_ORDER_13079_CREDIT_DATE_INVALID(13079),
    /** 社保卡ID无效或不支持医保支付 */
    PAY_ORDER_13080(13080),
    /** 业务类型ID无效 */
    PAY_ORDER_13081(13081),
    /** 渠道商户不支持此业务 */
    PAY_ORDER_13082(13082),
    /** 合作商户不支持此业务 */
    PAY_ORDER_13083(13083),
    /** 交易业务类型不合法 */
    PAY_ORDER_13084(13084),
    /** 支付订单扩展信息不存在 */
    PAY_ORDER_13085_ORDEREXT_NOT_EXIST(13085),
    /** 该笔订单非医保支付方式 */
    PAY_ORDER_13086_PAY_TYPE_ID_NOT_INVALID(13086),
    /** 确认支付时分账信息与下单时不一致 */
    PAY_ORDER_13087(13087),
    /** 非本人绑定的社保卡 */
    PAY_ORDER_13088(13088),
    /** 社保卡所在项目地与商户不一致 */
    PAY_ORDER_13089(13089),
    /** 该支付方式暂不支持*/
    PAY_ORDER_13090_PAY_TYPE_IS_NOT_INVALID(13090),
    /** 混合支付时社保卡未指定 */
    PAY_ORDER_13091(13091),
    /** 纯社保支付不允许使用含商业支付模式 */
    PAY_ORDER_13092(13092),
    /** 当前账户ID与下单时不一致 */
    PAY_ORDER_13093(13093),
    /** 当前订单已发起支付，不允许重新预结算 */
    PAY_ORDER_13094(13094),
    /** 渠道商户收单模式不合法 */
    PAY_ORDER_13095(13095),
    /** 资金平台类型无效或不存在 */
    PAY_ORDER_13096(13096),
    /** 资金平台不支持此种收单模式 */
    PAY_ORDER_13097(13097),   
    /** 合作商户资金平台编码不能为空 */
    PAY_ORDER_13098(13098),
    /** 渠道商户不支持此模式 */
    PAY_ORDER_13099(13099),
    /** 该用户社保卡无效或未绑定 */
    PAY_ORDER_13100(13100),
    /** 未获取到社保个人信息 */
    PAY_ORDER_13101(13101),
    /** 当前用户医疗待遇封锁 */
    PAY_ORDER_13102(13102),
    /** 暂不支持此医疗类别 */
    PAY_ORDER_13103(13103),
    /** MTS诊断未全部匹配 */
    PAY_ORDER_13104(13104),
    /** 医保预结算出现异常 */
    PAY_ORDER_13105(13105),
    /** 获取慢性病信息失败 */
    PAY_ORDER_13106(13106),
    /** 查询用户医疗待遇封锁信息失败 */
    PAY_ORDER_13107(13107),
    /** 目前暂不支持此种业务进行医保结算 */
    PAY_ORDER_13108(13108),
    /** 读取医保预结算数据异常 */
    PAY_ORDER_13109(13109),
    /** 该处方中存在多种慢性病诊断，违反医保结算要求，请到医院就诊科室重新开立处方后再下单支付 */
    PAY_ORDER_13110(13110),
    /** 订单总额与收费明细实际金额不一致，请返回商户重新生成订单 */
    PAY_ORDER_13111(13111),
    /** 为确保您的医保支付安全，医保在线支付只能在就诊的机构内进行支付，您当前的位置已超出机构范围，请回到就诊机构内支付 */
    PAY_ORDER_13112(13112),
    /** 获取位置失败，无法进行医保支付，请重新开启位置或稍候重试 */
    PAY_ORDER_13113(13113),
//############################################################ 交易提示  end ############################################################

//############################################################ 渠道商户提示  start ############################################################
    /**
     * 目标商户账户不存在
     */
    SP_12001_DEST_SP_NOT_EXIST(12001),
    /**
     * 查询账户信息失败
     */
    SP_12002_SEARCH_ACCOUNT_INFO_ERROR(12002),
    /**
     * 账户无接口访问权限
     */
    SP_12003_INTERFACE_ACCESS_ERROR(12003),
    /**
     * 操作账户不存在
     */
    SP_12004_OPER_ACT_NOT_EXIST(12004),
    /**
     * 目标账户不是该渠道下的合作商户,无法操作
     */
    SP_12005_DEST_SP_OPER_ERROR(12005),
    /**
     * 手机号码已注册
     */
    SP_12006_SP_TELNO_EXIST(12006),
    /**
     * 目标账户已冻结
     */
    SP_12007_DEST_ACT_FREEZEN(12007),
    /**
     * 账户未冻结
     */
    SP_12008_DEST_ACT_UNFREEZEN(12008),
    /**
     * 有銀行卡未解除綁定,无法绑定新的银行卡
     */
    SP_12009_HAVE_VALID_CARD(12009),
    /**
     * 该账户无此卡号
     */
    SP_12010_BANK_ACCOUNT_NOT_EXIST(12010),
    /**
     * 银行帐号已有效
     */
    SP_12011_BANK_ACCOUNT_VALID(12011),
    /**
     * 银行帐号已无效
     */
    SP_12012_BANK_ACCOUNT_UNVALID(12012),
    
    /**
     * 商户开户失败
     */
    SP_12013_ADD_SP_ERROR(12013),
    /**
     * 商户冻结或者解冻失败
     */
    SP_12014_FREEZE_SP_ERROR(12014),
    
    /**
     * 合作商户查询失败
     */
    SP_12015_QUERY_SUBSP_ERROR(12015),
    /**
     * 绑定银行卡失败
     */
    SP_12016_BIND_BANK_CARD_ERROR(12016),
    /**
     * 银行卡操作失败
     */
    SP_12017_OPER_BANK_CARD_ERROR(12017),
    /**
     * 应用id与账户id不匹配
     */
    SP_12018_APPID_OPERID_UNMATCHED(12018),
    /**
     * 银行账号和户名必须同时输入
     */
    SP_12019_ACCOUNT_NUM_NAME_REQUIRED(12019),   
    /**
     * 商户账户审核状态不是已通过
     */
    SP_12020_SP_OPEN_CHECK_NOT_OK(12020), 
    /**
     * 商户状态已冻结
     */
    SP_12021_SP_ACT_STATE_NOT_OK(12021), 
    /**
     * 超级管理员不允许创建合作商户
     */
    SP_12022_SP_ACT_ADM_CREATE_ERROR(12022), 
    /**
     * 发起账户不存在
     */
    TD_13019_WITH_OPER_SP_NOT_EXIST(13019),
    /**
     * 超出每次最大提现金额
     */
    TD_13020_WITH_EXCEED_LIMIT_WITHDRAW_TIME(13020),
    /**
     * 超出每日最大提现金额
     */
    TD_13021_WITH_EXCEED_LIMIT_WITHDRAW_DAY(13021),
    /**
     *  发起账户余额不足
     */
    TD_13022_WITH_OPER_ACT_NOT_ENOUGH_MONEY(13022),
    /**
     *  目标账户状态不合法,已冻结或者未通过审核
     */
    TD_13023_WITH_DEST_ACT_FREEZEN(13023),
    /**
     *  提现交易单生成失败
     */
    TD_13024_WITH_SAVE_ERROR(13024),
    /**
     *  提现预扣款失败
     */
    TD_13025_WITH_PRE_SUBSTRAT_ERROR(13025),
    /**
     * 查询大账户可用金额失败
     */
    TD_13026_SEARCH_YEEPAY_ACCOUNT_ERROR(13026),
    /**
     * 大账户可用金额不足
     */
    TD_13027_YEEPAY_ACCOUNT_NOT_ENOUGH_ERROR(13027),
    /**
     * 查询目标银行账户失败
     */
    TD_13028_SEARCH_BANK_ACCOUNT_ERROR(13028),
    /**
     * 提现被拒绝
     */
    TD_13029_WITH_REJECTED_ERROR(13029),
    /**
     * 提现打款失败
     */
    TD_13030_WITH_FAIL(13030),
    /**
     * 提现异常
     */
    TD_13031_WITH_EXCEPTION(13031),
    /**
     * 账户未绑定银行卡
     */
    TD_13032_WITH_ACT_NOT_HAVE_VALID_CARD(13032),
    /**
     * 交易单查询失败
     */
    TD_13033_WITH_SEARCH_FAIL(13033),
    /**
     * 易宝批次号生成失败
     */
    TD_13034_WITH_YEEPAY_BATCH_NO_GEN_ERROR(13034),
    /**
     * 重复的提现流水号
     */
    TD_13035_WITH_REPEAT_SERIAL_ERROR(13035),
    /** 实名认证信息有误 */
    TD_13036_ACT_PERSON_REAL_NAME_FALSE(13036),
    /** 实名认证抛出异常 */
    TD_13037_REAL_NAME_WITH_EXCEPTION(13037),
//############################################################ 渠道商户提示  end ############################################################

//############################################################ 退费提示 start ##############################################################
    /**退款原有交易单号不存在 */
    PAY_ORDER_REFUND_14001_TDID_INVALID(14001),
    /**退款原有交易单状态非成功*/
    PAY_ORDER_REFUND_14002_TDSTATID_NON_SUCCESS(14002),
    /**退款交易单类型与原交易单类型不一致 */
    PAY_ORDER_REFUND_14003_TDTYPEID_INVALID(14003),
    /**退款原有交易单支付扩展信息不存在*/
    PAY_ORDER_REFUND_14004_EXT_TDID_INVALID(14004),
    /**退款金额大于原交易单剩余可退金额*/
    PAY_ORDER_REFUND_14005_INSUFFICIENT_BALANCE(14005),
    /**平台私人账户资金属性信息不存在*/
    PAY_ORDER_14006_PFIN_NOT_EXISTS(14006),
    /**平台商户账户资金属性信息不存在*/
    PAY_ORDER_14007_SPFIN_NOT_EXISTS(14007),
    /**该订单已成功退款*/
    PAY_ORDER_14008_SUCCESS_REFUND(14008),
    /**原有订单存在正在退款业务*/
    PAY_ORDER_14009_REFUND_UNDERWAY(14009),
    /**退款交易单不存在*/
    PAY_ORDER_14010_REFUND_TDID_INVALID(14010),
    /**退款交易单号与商户退款订单号不一致*/
    PAY_ORDER_14011_ORDERCODE_TDID_INVALID(14011),
    /**退款交易单扩展信息不存在*/
    PAY_ORDER_14012_EXT_TDID_INVALID(14012),
    /**退款状态未成功*/
    PAY_ORDER_14013_STATE_NOT_SUCESS(14013),
    /**资金平台交易流水号为空*/
    PAY_ORDER_14014_FUND_TD_CODE_BLANK(14014),
    /**退款渠道商户不是原有支付渠道商户*/
    PAY_ORDER_14015_APP_ID_INCONFORMITY(14015),
    /**退款订单号已存在*/
    PAY_ORDER_14016_APP_ID_INCONFORMITY(14016),
    /**已超过当月截止日期，无法医保结算撤销*/
    PAY_ORDER_REFUND_14017(14017),
    /**医保结算跨月不允许结算撤销*/
    PAY_ORDER_REFUND_14018(14018),
    
    /**系统异常*/
    SP_14101_SYSTEM_ABNORMAL(14101),
    /**传入参数错误或非法请求（参数错误，有必要参数为空）*/
    SP_14102_PARAMS_INVALID(14102),
    /**没有可以返回的数据*/
    SP_14103_NO_DATA_RETURN(14103),
    /**商户账户已冻结*/
    SP_14104_ACCOUNT_FROZEN(14104),
    /**商户账户不存在*/
    SP_14105_ACCOUNT_NOT_EXISTS(14105),
    /**交互解密失败*/
    SP_14106_DECRYPT_FAIL(14106),
    /**sign 验签失败*/
    SP_14107_CHECK_SIGN_FAIL(14107),
    /**请求失败，请检查参数格式*/
    SP_14108_CHECK_PARAM_FAIL(14108),
    /**查不到此交易订单*/
    SP_14109_NO_ORDER(14109),
    /**退款金额超过可退款额*/
    SP_14110_NSUFFICIENT_BALANCE(14110),
    /**查不到此退款请求订单*/
    SP_14111_NO_REFUND_ORDER(14111),
    /**退款请求重复*/
    SP_14112_REPEAT_REFUND_ORDER(14112),
    /**接口不支持商户提交的method*/
    SP_14113_METHOD_INVALID(14113),
    /**时间间隔超过31 天*/
    SP_14114_INTERVAL_GTR_31(14114),
    /**记录数量过多，请尝试缩短日期间隔*/
    SP_14115_DATA_TOO_MUCH(14115),

//############################################################ 退费提示 end ##############################################################    
  //####################################实名认证 start##########################################
    /**实名认证未知错误! */
    CERT_30000_ERROR(30000),//实名认证未知错误!
    /**参数cert.config.ph.uid配置有误！ */
    CERT_30001_CONFIG_PH_UID_ERROR(30001),
    /**参数cert.config.ph.four.url配置有误！ */
	CERT_30002_CONFIG_PH_FOUR_URL_ERROR(30002),
    /**参数cert.config.ph.safety.code配置有误！ */
	CERT_30003_CONFIG_PH_SAFETY_CODE_EMPTY(30003),
    /**参数cert.config.ph.rsa.private.key.file.path配置有误！ */
	CERT_30004_CONFIG_PH_PRIVATE_KEY_FILE_EMPTY(30004),

    /**商户uid不能为空(普惠)！ */
	CERT_30101_PH_UID_ERROR(30101),
    /**私钥文件未找到(普惠) */
	CERT_30102_PH_RSA_PRIVATE_KEY_FILE_NOT_FOUND(30102),
    /**私钥文件读取有误(普惠)！ */
	CERT_30103_PH_RSA_PRIVATE_KEY_ERROR(30103),
    /**私钥不能为空(普惠)！ */
	CERT_30104_PH_RSA_PRIVATE_KEY_EMPTY(30104),
    /**RSA签名失败(普惠)！ */
	CERT_30105_PH_RSA_SIGN_EMPTY(30105),
    /**请求四要素接口异常(普惠)！ */
	CERT_30106_PH_FOUR_POST_ERROR(30106),
    /**四要素接口返回数据结构异常(普惠)！ */
	CERT_30107_PH_FOUR_RESULT_ERROR(30107),
    /**系统级异常(普惠) */
	CERT_30108_PH_SYS_ERROR(30108),
    /**业务级异常(普惠) */
	CERT_30109_PH_BUSINESS_ERROR(30109),
	/**报文版本不正确 */
	CERT_30110_PH_BUSINESS_MSG_ERROR(30110),
	/**报文类型不正确 */
	CERT_30111_PH_BUSINESS_MSGTYPE_ERROR(30111),
	/**身份证号为空或者不合法 */
	CERT_30112_PH_BUSINESS_IDCARD_ERROR(30112),
	/**姓名为空或者长度太长 */
	CERT_30113_PH_BUSINESS_NAME_ERROR(30113),
	/**请求终端类型为空或者输入项不合法 */
	CERT_30114_PH_BUSINESS_CLIENT_ERROR(30114),
	/**交易流水号为空或者长度超长 */
	CERT_30115_PH_BUSINESS_ORDERNO_ERROR(30115),
	/**交易流水号不合法 */
	CERT_30116_PH_BUSINESS_ORDERNO_ERROR(30116),
	/**银行卡类型输入输入不合法 */
	CERT_30117_PH_BUSINESS_BANKTYPE_ERROR(30117),
	/**查找验证交易路由失败，请联系管理员 */
	CERT_30118_PH_BUSINESS_ROUTE_ERROR(30118),
	/**验证失败，用户提交信息不合法或者验证超时 */
	CERT_30119_PH_BUSINESS_INFO_ERROR(30119),
	/**此卡未开通银联在线支付功能，实名认证失败 */
	CERT_30120_PH_BUSINESS_CARD_ERROR(30120),
	/**该银行卡bin不支持 */
	CERT_30121_PH_BUSINESS_CARDBIN_ERROR(30121),
	/**不支持此银行卡的验证 */
	CERT_30122_PH_BUSINESS_CARDVALID_ERROR(30122),
	/**银行卡类型不符 */
	CERT_30123_PH_BUSINESS_CARDTYPE_ERROR(30123),
	/**验证不一致 */
	CERT_30124_PH_UNMATCH(30124),
	/**不支持该银行卡或银行卡未开通在线支付 */
	CERT_30125_PH_BAKNCARD_ERROR(30125),
	/**信息不合法（身份证或银行卡） */
	CERT_30126_PH_INFO_ERROR(30126),
	/**照片数据过大（头像验证时） */
	CERT_30127_PH_HEAD_ERROR(30127),
	/**账户余额不足 */
	CERT_30128_PH_NOMONEY(30128),
	/**校验失败，稍后再试 */
	CERT_30129_PH_CHECK_ERROR(30129),
	/**数据校验失败 */
	CERT_30130_PH_DATA_CHECK_ERROR(30130),

    /**身份证号有误! */
	CERT_31001_ID_CARD_ERROR(31001),
    /**银行卡号有误! */
	CERT_31002_BANK_CARD_ERROR(31002),
    /**手机号有误! */
	CERT_31003_PHONE_NO_ERROR(31003),
    /**姓名有误! */
	CERT_31004_REAL_NAME_ERROR(31004),
    /**未获取到实名认证结果! */
	CERT_31005_NO_RESULT(31005),
    /**认证数据不全! */
	CERT_31006_PARAM_ERROR(31006),
	 /**实名认证错误{a}次，每天限制错误{b}次! */
	CERT_31007_ERROR_ERROR(31007),
	/** 同步失败，此用户已实名认证 */
	CERT_31008_ERROR_ERROR(31008),
	/** 未知实名认证唯一编号 */
	CERT_31009_PERSON_ID_ERROR(31009),
	//####################################实名认证 end##########################################
	
	//####################################用户中心begin#########################################
	/**未知授权码 */
	USER_CENTER_32001_TOKEN_ERROR(32001),
	/**授权设备不是同一设备 */
	USER_CENTER_32002_TOKEN_ERROR(32002),
	/**用户ID不能为空*/
	UC_CENTER_ADDR_32003_NOT_NULL_PAY_USER_ID(32003),
	/**账号不能为空*/
	UC_CENTER_ADDR_32004_NOT_NULL_ACT_ID(32004),
	/**地址不能为空*/
	UC_CENTER_ADDR_32005_NOT_NULL_RECV_ADDR_DET(32005),
	/**收货电话不能为空*/
	UC_CENTER_ADDR_32006_NOT_NULL_PHONE(32006),
	/**收货人不能为空*/
	UC_CENTER_ADDR_32007_NOT_NULL_RECV_MAN(32007),
	/**收货地址状态不能为空*/
	UC_CENTER_ADDR_32008_NOT_NULL_UCADDR_STATID(32008),
	/**性别必须是0或者1数字表示*/
	UC_CENTER_ADDR_32009_NUMBER_FORMAT_PGEND_ID(32009),
	/**更新用渠道账户ID格式不正确（必须是数字格式）*/
	UC_CENTER_ADDR_32010_NUMBER_FORMAT_CHAN_ACTID(32010),
	/**收货地址状态必须用数字表示（1.可用，2不可用，3，删除）*/
	UC_CENETER_ADDR_32011_NUMBER_FORMAT_UCADDR_STATID(32011),
	/**省id长度不能超过8个字符*/
	UC_CENTER_ADDR_32012_NUMBER_FORMAT_PROV_ID(32012),
	/**城市id长度不能超过8个字符*/
	UC_CENTER_ADDR_32013_NUMBER_FORMAT_CITY_ID(32013),
	/**区id长度不能超过8个字符*/
	UC_CENTER_ADDR_32014_NUMBER_FORMAT_DIST_ID(32014),
	/**社区id长度不能超过8个字符*/
	UC_CENTER_ADDR_32015_NUMBER_FORMAT_COMM_ID(32015),
	/**地址id不存在（如测试请从数据库取地址id，由于地址id数据库自动生成）*/
	UC_CENTER_ADDR_32016_UCUSERADD_ID_ERROR(32016),
	/**用户id不正确(如地址id正确，登录账号不是同一个，所以不能修改)*/
	UC_CENTER_ADDR_32017_USER_ID_ERROR_FOR_UCUSERADDR(32017),
	/** 两次手机号重复 */
	UC_CENTER_ADDR_32018_ERROR(32018),
	/**该用户下无地址列表*/
	UC_CENTER_ADDR_32019_QUERY_NULL(32019),
	/** 此手机号被占用 */
	UC_CENTER_ADDR_32020_PHONE_USERD_NULL(32020),
	//####################################用户中心end###########################################
	
	//####################################社保卡begin#########################################
	/** 查询社保配置失败 */
	SOCIAL_SECURITY_33001_QUERY_COL_ERROR(33001),
	/** 未实名认证 */
	SOCIAL_SECURITY_33002_REAL_NAME_ERROR(33002),
	/** 未知城市 */
	SOCIAL_SECURITY_33003_CITY_ID_ERROR(33003),
	/** 社保卡无效 */
	SOCIAL_SECURITY_33004_MI_ERROR(33004),
	/** 未知社保卡信息 */
	SOCIAL_SECURITY_33005_NOT_MESSAGE_ERROR(33005),
	/** 社保卡信息为空 */
	SOCIAL_SECURITY_33006_NULL_ERROR(33006),
	/** 用户和社保卡信息不匹配 */
	SOCIAL_SECURITY_33007_USERID_ERROR(33007),
	/** 此卡已绑定 */
	SOCIAL_SECURITY_33008_ERROR(33008),
	/** 非主卡操作，操作无效 */
	SOCIAL_SECURITY_33009_ERROR(33009),
	/** 实名认证预留手机号与发送验证码手机号不一致 */
	SOCIAL_SECURITY_33010_ERROR(33010),
	/** 社保卡与登录账户不一致 */
	SOCIAL_SECURITY_33011_ERROR(33011),
    /** 用户注册手机号与发送验证码手机号不一致 */
    SOCIAL_SECURITY_33012_ERROR(33012),
	//####################################社保卡end###########################################
	
	//####################################人脸识别begin#########################################
	/** 人脸识别验证失败，你可以继续扫描识别 */
	FACE_Recognition_34001_ERROR(34001),
	/** 今日验证已达上限3次，请明天再试 */
	FACE_Recognition_34002_ERROR(34002),
	//####################################人脸识别end###########################################
	
	//####################################人脸识别begin#########################################
	/** 请获取验证码信息  */
	LOGIN_VERIFYCODE_35001_ERROR(35001),
	/** 验证码输入有误 */
	LOGIN_VERIFYCODE_35002_ERROR(35002)
	//####################################人脸识别end###########################################
	;
    Hint(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }

    public String getCodeString() {
        return String.valueOf(code);
    }

    public String getMessage() {
        return Propertie.HINT.value(code);
    }

	public static String getMessage(int code) {
		return Propertie.HINT.value(code);
	}
}
