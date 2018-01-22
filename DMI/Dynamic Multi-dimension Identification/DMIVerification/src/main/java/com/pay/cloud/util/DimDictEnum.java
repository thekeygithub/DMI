package com.pay.cloud.util;

/**
 * 支付平台数据字典
 * Created by yacheng.ji on 2016/4/13.
 */
public enum DimDictEnum {

    //*************支付平台用户状态 begin***************/
    PAY_USER_STAT_NORMAL(1, "正常"),
    PAY_USER_STAT_DISABLE(2, "已禁用"),
    PAY_USER_STAT_CANCEL(9, "已注销"),
    //*************支付平台用户状态 end***************/

    //*************平台账户状态 begin***************/
    ACCOUNT_STAT_NORMAL(1, "正常"),
    ACCOUNT_STAT_FREEZE(9, "冻结"),
    //*************平台账户状态 end***************/

    //*************平台账户类型 begin***************/
    ACCOUNT_TYPE_PRIVATE(1, "平台私人账户"),
    ACCOUNT_TYPE_MERCHANT(2, "平台商户账户"),
    //*************平台账户类型 end***************/

    //*************订单交易状态 begin***************/
    TD_TRANS_STAT_INIT(100, "初始化"),
    TD_TRANS_STAT_PROCESSING(101, "进行中"),
    TD_TRANS_STAT_SUCCESS(110, "成功"),
    TD_TRANS_STAT_FAILED(120, "失败"),
    TD_TRANS_STAT_EXCEPTION(130, "异常"),
    //*************订单交易状态 end***************/

    //*************对账状态 begin***************/
    CONFIRM_STAT_CHECK_NO_ERROR(600, "核对无误"),
    CONFIRM_STAT_CHECK_ERROR(610, "核对出错"),
    CONFIRM_STAT_WAIT_CHECK(619, "待核对"),
    //*************对账状态 end***************/

    //*************收入支出标识 begin***************/
    IN_OUT_FLAG_INCOME(1, "收入"),
    IN_OUT_FLAG_EXPENDITURE(2, "支出"),
    IN_OUT_FLAG_BANK_CARD_EXPENDITURE(3, "银行卡支出"),
    IN_OUT_FLAG_BANK_CARD_INCOME(4,"银行卡收入"),
    //*************收入支出标识 end***************/

    //*************订单退款状态 begin***************/
    TD_RET_STAT_INIT(1, "初始化"),
    TD_RET_STAT_APPLY_SUCC(2, "申请成功"),
    TD_RET_STAT_REFUND_SUCC(6, "退款成功"),
    TD_RET_STAT_REFUND_FAIL(9, "退款失败"),
    //*************订单退款状态 end***************/

    //*************交易业务类型 begin***************/
    TD_TRANS_BUSI_TYPE_JIAOFEI(201, "缴费"),
    TD_TRANS_BUSI_TYPE_GUAHAO(202, "挂号"),
    TD_TRANS_BUSI_TYPE_GOUYAO(203, "购药"),
    //*************交易业务类型 end***************/

    //*************交易类型 begin***************/
    TD_TRANS_TYPE_PAYMENT(101, "支付"),
    TD_TRANS_TYPE_TRANSFER(111, "转账"),
    TD_TRANS_TYPE_CASH(121, "提现"),
    TD_TRANS_TYPE_REFUND(131, "退款"),
    TD_TRANS_TYPE_RECHARGE(141, "充值"),
    //*************交易类型 end***************/

    //*************交易操作通道类别 begin***************/
    TD_OPER_CHAN_TYPE_ANDROID(11, "Android"),
    TD_OPER_CHAN_TYPE_IOS(12, "IOS"),
    TD_OPER_CHAN_TYPE_WECHAT(21, "微信"),
    TD_OPER_CHAN_TYPE_WEB(31, "WEB"),
    //*************交易操作通道类别 end***************/

    //*************支付账号类别 begin***************/
    PAY_ACT_TYPE_DEBIT_CARD(102, "个人借记卡"),
    PAY_ACT_TYPE_CREDIT_CARD(103, "个人信用卡"),
    //*************支付账号类别 end***************/

    //*************是否有效 begin***************/
    VALID_FLAG_VALID(1, "有效"),
    VALID_FLAG_INVALID(0, "无效"),
    //*************是否有效 end***************/

    //*************证件类型 begin***************/
    P_CERT_TYPE_ID_SHENFENZHENG(1, "身份证"),
    P_CERT_TYPE_ID_JUNGUANZHENG(2, "军官证"),
    P_CERT_TYPE_ID_HUZHAO(3, "护照"),
    P_CERT_TYPE_ID_QITA(99, "其他"),
    //*************证件类型 end***************/

    //*************性别 begin***************/
    P_GEND_ID_MALE(1, "男"),
    P_GEND_ID_FEMALE(0, "女"),
    //*************性别 end***************/

    //*************使用支付通道方式 begin***************/
    USE_PAY_CHANNEL_TYPE_SHANGYE(1, "商业支付"),
    USE_PAY_CHANNEL_TYPE_SHEBAO(2, "社保支付"),
    USE_PAY_CHANNEL_TYPE_HUNHE(3, "混合支付"),
    //*************使用支付通道方式 end***************/

    //*************自费标识 begin***************/
    BUY_SELF_YES(0, "自费"),
    BUY_SELF_NO(1, "非自费"),
    //*************自费标识 end***************/

    //*************交易任务处理状态 begin***************/
    TD_PROC_STAT_ID_TREATMENT_PENDING(0, "待处理"),
    TD_PROC_STAT_ID_TREATMENT_IN(1, "处理中"),
    TD_PROC_STAT_ID_TREATMENT_SUCCESS(6, "处理成功"),
    TD_PROC_STAT_ID_TREATMENT_FAILURE(-1, "处理失败"),
    //*************交易任务处理状态 end***************

	//*************收单模式 begin***************/
    FUND_MODEL_ID_1(1, "大账户集中收单"),
    FUND_MODEL_ID_2(2, "多商户直接收单"),
    FUND_MODEL_ID_3(3, "通道直转子账户(内部户)"),
	//*************收单模式 end***************/

    //*************医疗类别 begin***************/
    MEDICAL_CLASS_PUTONGMENZHEN(11, "普通门诊"),
    MEDICAL_CLASS_YAODIANGOUYAO(14, "药店购药"),
    MEDICAL_CLASS_MANXINGBING(16, "门诊规定病种(慢性病)"),
    MEDICAL_CLASS_JIHUASHENGYUSHOUSHU(45, "计划生育手术(门诊)"),
    //*************医疗类别 end***************

    //*************不允许医保结算交互 begin***************/
    MEDICAL_SETTLE_INTERACTIVE_UNBIND_SI_CARD(1001, "您还未绑定社保卡，无法进行医保支付，请先绑定本人的社保卡\n\n注：暂不支持城乡居医保用户在线医保支付"),
    MEDICAL_SETTLE_INTERACTIVE_NOT_THE_SAME_PERSON(1002, "实名认证与绑定社保卡的身份信息不一致，无法进行医保支付，请先绑定本人的社保卡\n\n注：暂不支持城乡居民医保用户在线医保支付"),
    MEDICAL_SETTLE_INTERACTIVE_MEDICAL_TREATMENT_BLOCK(1003, "您的社保卡可能已封锁或已挂失，无法进行医保支付，请前往当地社保局办事大厅处理"),
    MEDICAL_SETTLE_INTERACTIVE_MTS_NOT_MATCH(1004, "诊断信息未匹配，无法进行在线医保支付，只能个人自费\n\n注：若需进行医保支付，请携带社保卡到就诊医院的结算窗口办理"),
    MEDICAL_SETTLE_INTERACTIVE_MI_PERSON_IS_NULL(1005, "您的社保卡只绑定了养老保险，未绑定医保参保身份，不能进行医保支付，只能个人自费\n\n注：城乡居民医保用户目前无法使用本系统绑定医疗保险，功能正在建设中，敬请期待"),
    MEDICAL_SETTLE_INTERACTIVE_INSURED_PERSONNEL_TYPE(1006, "获取当前参保人员类型异常，将无法进行医保支付，只能个人自费"),
    MEDICAL_SETTLE_INTERACTIVE_MI_SETTLE_CLOSING_DATE(1007, "每月月底{X}后进行医保结账清算业务，不能进行医保支付，只能个人自费"),
    MEDICAL_SETTLE_INTERACTIVE_GET_SOCIAL_CARD_INFO_ERROR(1008, "获取社保卡信息失败，暂不能进行医保支付，只能个人自费"),
    MEDICAL_SETTLE_INTERACTIVE_SOCIAL_CARD_INFO_NOT_EXIST(1009, "您还未申办社保卡，无法进行医保支付，请携带身份证前往当地社保局经办大厅办理"),
    MEDICAL_SETTLE_INTERACTIVE_SOCIAL_CARD_NOT_ACTIVE(1010, "您的社保卡可能在办理中或未激活，无法进行医保支付，请前往当地社保局申办或激活"),
    MEDICAL_SETTLE_INTERACTIVE_GET_MI_TREATMENT_BLOCKADE_INFO_ERROR(1011, "获取医疗待遇封锁信息失败，暂不能进行医保支付，只能个人自费"),
    MEDICAL_SETTLE_INTERACTIVE_MI_PRE_SETTLE_EXCEPTION(1011, "医保分账异常，暂不能进行医保支付，只能个人自费"),
    MEDICAL_SETTLE_INTERACTIVE_MI_YP_SEX_LIMIT(1011, "因该处方中存在与您性别不符的药品，请联系您的就诊医生解决"),
    MEDICAL_SETTLE_INTERACTIVE_MI_FYP_SEX_LIMIT(1011, "因该处方中存在与您性别不符的诊疗服务，请联系您的就诊医生解决"),
    MEDICAL_SETTLE_INTERACTIVE_MI_YP_AGE_LIMIT(1011, "该处方中存在与您年龄不符的药品，请联系您的就诊医生"),
    MEDICAL_SETTLE_INTERACTIVE_MI_FYP_AGE_LIMIT(1011, "该处方中存在与您年龄不符的诊疗服务，请联系您的就诊医生"),
    //*************不允许医保结算交互 end***************

    //*************短信通道 begin***************/
    SMS_CHANNEL_MENGWANG(1, "梦网"),
    SMS_CHANNEL_MANDAO(2, "漫道"),
    //*************短信通道 end***************/

    //*************实名认证通道 begin***************/
    REAL_NAME_FOUR_ELEMENTS_PUHUI(1, "银联四要素验证(普惠)"),
    REAL_NAME_THREE_ELEMENTS_HUIYUE(2, "实人三要素验证(慧阅)");
    //*************实名认证通道 end***************/

    private int code;
    private String name;

    DimDictEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public static boolean checkTransBusiType(int code){
        return code == TD_TRANS_BUSI_TYPE_JIAOFEI.getCode() || code == TD_TRANS_BUSI_TYPE_GUAHAO.getCode() || code == TD_TRANS_BUSI_TYPE_GOUYAO.getCode();
    }

    public int getCode() {
        return code;
    }

    public String getCodeString() {
        return String.valueOf(code);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}