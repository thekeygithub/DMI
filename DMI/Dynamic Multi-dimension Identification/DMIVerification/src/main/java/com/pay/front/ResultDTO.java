package com.pay.front;

/**
 * @author xiangfeng.guan
 */
public final class ResultDTO {
	private String settle_id;// ������ˮ��
	private String result_code;// ��������
	private String result_message;// ��������Ϣ
	private String account_bal;// �˻����
	private String total_amt;// ҽ���ܷ���
	private String account_pay;// ���θ����˻�֧��
	private String personal_pay;// �����ֽ�֧��
	private String fund_pay;// �����籣���֧������������
	private String data;// ��������ݣ�ҽ�����㷵��ԭʼ��ݣ�

	public String getSettle_id() {
		return settle_id;
	}

	public void setSettle_id(String settle_id) {
		this.settle_id = settle_id;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getResult_message() {
		return result_message;
	}

	public void setResult_message(String result_message) {
		this.result_message = result_message;
	}

	public String getAccount_bal() {
		return account_bal;
	}

	public void setAccount_bal(String account_bal) {
		this.account_bal = account_bal;
	}

	public String getTotal_amt() {
		return total_amt;
	}

	public void setTotal_amt(String total_amt) {
		this.total_amt = total_amt;
	}

	public String getAccount_pay() {
		return account_pay;
	}

	public void setAccount_pay(String account_pay) {
		this.account_pay = account_pay;
	}

	public String getPersonal_pay() {
		return personal_pay;
	}

	public void setPersonal_pay(String personal_pay) {
		this.personal_pay = personal_pay;
	}

	public String getFund_pay() {
		return fund_pay;
	}

	public void setFund_pay(String fund_pay) {
		this.fund_pay = fund_pay;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
