package com.models.cloud.util;

import com.models.secrity.crypto.AesCipher;

/**
 * 加密解密UTILS
 */
public class EncryptUtils {
	
	//AES 加解密静态变量
	private static AesCipher ac = new AesCipher();
	
	/**
	 * AES加密
	 * @param content
	 * @param cipherAesEnum
	 * @return
	 */
	public static String aesEncrypt(String content,CipherAesEnum cipherAesEnum){
		return ac.encrypt(content, cipherAesEnum.getAesKey(), cipherAesEnum.getAesIvbytes());
	}
	
	/**
	 *  AES解密
	 * @param content
	 * @param cipherAesEnum
	 * @return
	 */
	public static String aesDecrypt(String content,CipherAesEnum cipherAesEnum){
		return ac.decrypt(content, cipherAesEnum.getAesKey(), cipherAesEnum.getAesIvbytes());
	}
	
	
}
