package com.framework.crypto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.framework.utils.EncodeUtils;

/**
 * @author xiaowang AES 128位加密，
 * 加密与解密 返回值为"" 时，为执行失败
 * A:defaultSecretKey
 * try {
 * 			String content = "我的幸福，加密！";
 * 			content = Aes.setEncrypt(content);
 * 			content = Aes.getDecrypt(content);
 *		} catch (Exception e) {
 *			e.printStackTrace();
 *		}
 *		
 *	B:set key
 *	try {
 *			String[] content = {"我的幸福，加密！","懂得","改单费夫","染头发","吃饭"};
 *			String key = "!2wsde";
 *			byte[] btkey = Aes.getSecretKey(key);
 *			for(String ct : content) {
 *				String a = Aes.setEncrypt(ct, btkey);
 *				String b = Aes.getDecrypt(a, btkey);
 *			}
 *		} catch (Exception e) {
 *			e.printStackTrace();
 *		}
 * 
 */
public class Aes {

	public static final String AES = "AES";

	public static final String CODE = "UTF-8";

	/**
	 * 当前是128位，如果需要大于196或256位的需要替换虚拟机的加密jar包 无特殊需要，不用替换
	 */
	public static final int BIT = 128;

	/**
	 * 默认种子
	 */
	public static final String key = "1D3f$Hj%Lk_e#bh@)_df";

	private static byte[] defaultSecretKey = null;

	static {
		defaultSecretKey = Aes.getSecretKey(Aes.key);
	}

	/**
	 * 获取秘钥，如果 password 为null|""获取随机秘钥，不为空获取指定seed的秘钥
	 * 
	 * @param password seed
	 * @return byte[] 秘钥字节数组
	 */
	public static byte[] getSecretKey(String password) {
		KeyGenerator kgen;
		byte[] btSecretKey = null;
		try {
			kgen = KeyGenerator.getInstance(Aes.AES);
			if (StringUtils.isEmpty(password)) {
				kgen.init(Aes.BIT);
			} else {
				// 防止Linux下随机生成key
				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
				secureRandom.setSeed(password.getBytes());
				// 根据密钥初始化密钥生成器
				kgen.init(Aes.BIT, secureRandom);

				// kgen.init(Aes.BIT, new SecureRandom(password.getBytes()));
			}
			SecretKey secretKey = kgen.generateKey();
			btSecretKey = secretKey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return btSecretKey;
	}

	/**
	 * 加密
	 * 
	 * @param content 需要加密的内容
	 * @param password 加密KEY
	 * @return byte[]
	 * */
	public static byte[] encrypt(String content, byte[] secretKey) {
		byte[] result = null;
		try {
			SecretKeySpec key = new SecretKeySpec(secretKey, Aes.AES);
			Cipher cipher = Cipher.getInstance(Aes.AES);
			byte[] byteContent = content.getBytes(Aes.CODE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			result = cipher.doFinal(byteContent);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解密
	 * 
	 * @param content 待解密内容
	 * @param secretKey 解密密钥
	 * @return byte[]
	 * */

	public static byte[] decrypt(byte[] content, byte[] secretKey) {
		byte[] result = null;
		try {
			SecretKeySpec key = new SecretKeySpec(secretKey, Aes.AES);
			Cipher cipher = Cipher.getInstance(Aes.AES);
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(content);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf byte[]
	 * @return String
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 字符串转换为二进制字节数组
	 * 
	 * @param hexStr
	 * @return byte[]
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 获取解密信息，根据默认的秘钥
	 * 
	 * @param content 需解密的信息
	 * @return String
	 * @throws IOException
	 */
	public static String getDecrypt(String content) throws IOException {
		String value = "";
		if (StringUtils.isNotEmpty(content)) {
			byte[] result = Aes.decrypt(EncodeUtils.base64Decode(content), Aes.defaultSecretKey);
			if (result != null)
				value = new String(result, Aes.CODE);
		}
		return value;
	}

	/**
	 * 获取解密信息，每次密钥不同时使用
	 * 
	 * @param content 需解密的信息
	 * @param key 秘钥
	 * @return String
	 * @throws IOException
	 */
	public static String getDecrypt(String content, String key) throws IOException {
		String value = "";
		if (StringUtils.isNotEmpty(content)) {
			byte[] secretKey = Aes.getSecretKey(key);
			if (secretKey != null) {
				value = Aes.getDecrypt(content, secretKey);
			}
		}
		return value;
	}

	/**
	 * 获取解密信息，主要用于密钥相同加密内容不同时
	 * 
	 * @param content 需解密的信息
	 * @param secretKey 秘钥
	 * @return String
	 * @throws IOException
	 */
	public static String getDecrypt(String content, byte[] secretKey) throws IOException {
		String value = "";
		if (StringUtils.isNotEmpty(content)) {
			if (secretKey != null) {
				byte[] result = Aes.decrypt(EncodeUtils.base64Decode(content), secretKey);
				if (result != null)
					value = new String(result, Aes.CODE);
			}
		}
		return value;
	}

	/**
	 * 加密字符串，根据默认秘钥
	 * 
	 * @param content 需要加密的字符串
	 * @return String
	 */
	public static String setEncrypt(String content) {
		String value = "";
		if (StringUtils.isNotEmpty(content)) {
			byte[] result = Aes.encrypt(content, Aes.defaultSecretKey);
			if (result != null)
				value = EncodeUtils.base64Encode(result);
		}
		return value;
	}

	/**
	 * 加密字符串，每次密钥不同时使用
	 * 
	 * @param content 需要加密的字符串
	 * @param key 秘钥
	 * @return String
	 */
	public static String setEncrypt(String content, String key) {
		String value = "";
		if (StringUtils.isNotEmpty(content)) {
			byte[] secretKey = Aes.getSecretKey(key);
			if (secretKey != null) {
				value = Aes.setEncrypt(content, secretKey);
			}
		}
		return value;
	}

	/**
	 * 加密字符串，主要用于密钥相同加密内容不同时
	 * 
	 * @param content 需要加密的字符串
	 * @param secretKey 秘钥
	 * @return String
	 */
	public static String setEncrypt(String content, byte[] secretKey) {
		String value = "";
		if (StringUtils.isNotEmpty(content)) {
			if (secretKey != null) {
				byte[] result = Aes.encrypt(content, secretKey);
				if (result != null)
					value = EncodeUtils.base64Encode(result);
			}
		}
		return value;
	}
}