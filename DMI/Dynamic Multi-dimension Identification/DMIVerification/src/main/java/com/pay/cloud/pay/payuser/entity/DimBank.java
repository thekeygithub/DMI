package com.pay.cloud.pay.payuser.entity;

public class DimBank {
	
    private Long bankId;//银行ID

    private String fundId="";//资金平台ID

    private Short payActTypeId;//支付账号类别

    private String fullName="";//银行全称

    private String shortName="";//简称

    private String bankImageId="";//银行图标

    private String bankImageUrl="";//银行图标

	private String ppBankCode = "";//平台的银行编码

	public String getPpBankCode() {
		return ppBankCode;
	}

	public void setPpBankCode(String ppBankCode) {
		this.ppBankCode = ppBankCode;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public Short getPayActTypeId() {
		return payActTypeId;
	}

	public void setPayActTypeId(Short payActTypeId) {
		this.payActTypeId = payActTypeId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getBankImageId() {
		return bankImageId;
	}

	public void setBankImageId(String bankImageId) {
		this.bankImageId = bankImageId;
	}

	
	public String getBankImageUrl() {
		return bankImageUrl;
	}
	

	public void setBankImageUrl(String bankImageUrl) {
		this.bankImageUrl = bankImageUrl;
	}
}