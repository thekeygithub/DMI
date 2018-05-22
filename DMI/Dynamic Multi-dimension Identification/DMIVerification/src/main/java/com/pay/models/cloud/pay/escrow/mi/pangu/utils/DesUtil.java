package com.models.cloud.pay.escrow.mi.pangu.utils;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Encoder;

public class DesUtil {

	private static Key key;

	/**
	 * 静态块初始化key
	 */
	static {
		setKey("panku&ybhl20161201");

	}

	/**
	 * 通过字符串生成一个固定算法的key，因为linux和windows的随机key生成方式不同，所以需要固定
	 * 
	 * @param keyStr
	 *            密钥字符
	 */
	public static void setKey(String keyStr) {
		try {
			KeyGenerator _key = KeyGenerator.getInstance("DES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(keyStr.getBytes());
			_key.init(sr);
			key = _key.generateKey();
			_key = null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 加密 String 明文输入 ,String 密文输出
	 * 
	 * @param mwStr
	 * @return
	 */
	public static String encryptStr(String mwStr) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = mwStr.getBytes("UTF-8");
			byteMi = encryptByte(byteMing);
			strMi = base64en.encode(byteMi);
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			base64en = null;
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	
	/**
	 * 加密以 byte[] 明文输入 ,byte[] 密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	private static byte[] encryptByte(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	public static void main(String[] args) {
		System.out.println(DesUtil.encryptStr("201611301520|33"));
	}
	
}
