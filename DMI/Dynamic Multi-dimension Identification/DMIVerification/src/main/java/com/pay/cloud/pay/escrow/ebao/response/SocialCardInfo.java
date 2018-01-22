package com.pay.cloud.pay.escrow.ebao.response;
/**
 * 社保信息
 * @author yanjie.ji
 * @date 2016年12月6日 
 * @time 下午2:21:32
 */
public class SocialCardInfo {
	private String code;
	private String message;
	/**
	 * 医保个人编号
	 */
	private String medicarePersonNo;
	/**
	 * 社保个人编号
	 */
	private String socialSecurityPersonNo;
	/**
	 * 姓名
	 */
	private String cardName;
	/**
	 * 身份证号
	 */
	private String cardNo;
	/**
	 * 社保卡号
	 */
	private String socialSecurityNo;
	/**
	 * 绑定卡状态
	 */
	private String cardStatus;
	/**
	 * 人员类别  1城镇职工;2城乡居民;3灵活就业人员
	 */
	private String pSiCatId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMedicarePersonNo() {
		return medicarePersonNo;
	}

	public void setMedicarePersonNo(String medicarePersonNo) {
		this.medicarePersonNo = medicarePersonNo;
	}

	public String getSocialSecurityPersonNo() {
		return socialSecurityPersonNo;
	}

	public void setSocialSecurityPersonNo(String socialSecurityPersonNo) {
		this.socialSecurityPersonNo = socialSecurityPersonNo;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSocialSecurityNo() {
		return socialSecurityNo;
	}

	public void setSocialSecurityNo(String socialSecurityNo) {
		this.socialSecurityNo = socialSecurityNo;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getpSiCatId() {
		return pSiCatId;
	}

	public void setpSiCatId(String pSiCatId) {
		this.pSiCatId = pSiCatId;
	}
}
