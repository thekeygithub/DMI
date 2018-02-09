package com.models.cloud.util;

/**
 * 加密秘钥枚举
 */
public enum CipherAesEnum {
	
	
	/**
	 * 商户秘钥类
	 */
	SECRETKEY("2VHPXYa4HEVpfnlAwBpLCTSDBLJA3EPBBYp+m4uznjg=","y32mNvj5KAKInkPLJuJ//A==",CipherAesEnum.KEY_SIZE_256),
	
	/**
	 * 商户秘钥类
	 */
	H5PAYORDER("apYDaxXCLOxAb9rmqP6+zS+7SRXoomLBg+SndbhZ5bU=","B2N1EJ7VNTJiTBg3fce+NA==",CipherAesEnum.KEY_SIZE_256),
	
	/**
	 * 银行卡号类
	 */
	CARDNOKEY("aDI2noOmBKBx4GOMruxfTA==","rivTx/coZZZQ3iDCSiiz+w==",CipherAesEnum.KEY_SIZE_128),

	/**
	 * 通用256
	 */
	COMMON256("sU1KLenn2p9MCbgMfSP2f1PKYm2snJTbwoMSUVvJdFQ=","E8XcAuK4+8TiItBDILo4bg==",CipherAesEnum.KEY_SIZE_256),

	/**
	 * 通用128
	 */
	COMMON128("S/wGXLo/AF8+8MmxncjKIA==","hkDOMDjpzp0rv6bH2biXRw",CipherAesEnum.KEY_SIZE_128),

	/**
	 * 私有调用
	 */
	PRIVATECALL("DV7kJVoW3I7qfVo8ruTWRKQg8o7m8ST+qH2MSWmcdRE=","mnnuykCo4KEYz5Er63uWEw==",CipherAesEnum.KEY_SIZE_256),

	/**
	 * H5自动登录会话token生成秘钥
	 */
	H5LOGINTOKEN("gWLV+sUf3w3WdFlgdINXtmxURTDluzTgn+7obsS/ncU=","UzzHZ9hF8FyexPwA1lAzOQ==",CipherAesEnum.KEY_SIZE_256),
	
	/**
	 * 商户属性信息类
	 */
	PPINTFPRAR("bAqIlNrjlIQIVXyRCOtUbg==","/qpHBM6R4Edyk9BPwOOsBA==",CipherAesEnum.KEY_SIZE_128),

	/**
	 * Http调用
	 */
	HTTPINVOKE("8pn5gLNEtLaMDZHC+nSMbgG9zzW4Eg/99WCHGl4K3eo=","GC731lLK7oDRHAn1RWAJlA==",CipherAesEnum.KEY_SIZE_256);

	//AES key
	private final String aesKey;
	//AES 补码
	private final String aesIvbytes;
	//AES 位数
	private final int keySize; 
	
	//构造函数
	private CipherAesEnum(String aesKey,String aesIvbytes,int keySize) {
    	this.aesKey = aesKey;
    	this.aesIvbytes = aesIvbytes;
    	this.keySize = keySize;
    }

	/**
	 * 读取AES key
	 * @return
	 */
	public String getAesKey() {
		return aesKey;
	}
    /**
     * 读取AES 补码
     * @return
     */
	public String getAesIvbytes() {
		return aesIvbytes;
	}
    /**
     * 读取AES 位数
     * @return
     */
	public int getKeySize() {
		return keySize;
	}
	
	//KeySize
	private static final int KEY_SIZE_128 = 128;
	private static final int KEY_SIZE_192 = 192;
	private static final int KEY_SIZE_256 = 256;

}
