package com.models.cloud.util;

import java.security.MessageDigest;
import java.util.Random;

import com.models.cloud.util.hint.Propertie;

/**
 * md5+salt加密
 * @author qingsong.li
 *
 */
public class Md5SaltUtils {
	
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d","e", "f" };

	
	private static String algorithm = "MD5";

    
    /**
     * 密码+盐值
     * @param rawPass
     * @param salt
     * @return
     */
	public static String encodeMd5Salt(String rawPass,String salt) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			// 加密后的字符串
			result = byteArrayToHexString(md.digest(mergePasswordAndSalt(rawPass,salt).getBytes("utf-8")));
		} catch (Exception ex) {
		}
		return result;
	}

    /**
     * 密码
     * @param rawPass
     * @return
     */
	public static String encodeMd5(String rawPass) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			// 加密后的字符串
			result = byteArrayToHexString(md.digest(mergePasswordAndSalt(rawPass,null).getBytes("utf-8")));
		} catch (Exception ex) {
		}
		return result;
	}
	
	/**
	 * 认证密码
	 * @param encPass
	 * @param rawPass
	 * @param salt
	 * @return
	 */
	public static boolean isPasswordValid(String encPass, String rawPass,String salt) {
		String pass1 = "" + encPass;
		String pass2 = encodeMd5Salt(rawPass,salt);

		return pass1.equals(pass2);
	}

	/**
	 * 获取加盐值后的密码
	 * @param password
	 * @param salt
	 * @return
	 */
	private static String mergePasswordAndSalt(String password,String salt) {
		if (password == null) {
			password = "";
		}
		
		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	
	/**
	 * 生成随机码
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "ABCDEFGHIJKLMNOPQRSTUVWXVZabcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }
	
	/**
	 * 获取盐值
	 * @return
	 */
	public static String getSalt(){
		return getRandomString(16);
	}

	public static void main(String[] args) {
	
	
		String md5 = Md5SaltUtils.encodeMd5("test");//将test生成md5
		System.out.println(md5);
		
		
		String salt =getSalt();//生成盐值放入数据库
		System.out.println(salt);
		String md5Salt = Md5SaltUtils.encodeMd5Salt(md5,salt);//将md5在进行md5+salt加密，放入数据库
		
		System.out.println(md5Salt);
		boolean passwordValid = Md5SaltUtils.isPasswordValid(md5Salt,md5,salt);
		System.out.println(passwordValid);

		//Md5SaltUtils encoderSha = new Md5SaltUtils(salt, "SHA");
//		String pass2 = Md5SaltUtils.encodeMd5("test");
//		System.out.println(pass2);
//		boolean passwordValid2 = Md5SaltUtils.isPasswordValid("1bd98ed329aebc7b2f89424b5a38926e", "test",salt);
//		System.out.println(passwordValid2);
	}
}
