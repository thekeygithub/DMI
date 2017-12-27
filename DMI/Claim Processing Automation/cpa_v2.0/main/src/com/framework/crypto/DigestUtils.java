package com.framework.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import com.framework.utils.EncodeUtils;

public final class DigestUtils {

	private static final String SHA1 = "SHA-1";
	private static final String MD5 = "MD5";

	//-- String Hash function --//
	/**
	 * 对输入字符串进行sha1散列, 返回Hex编码的结果.
	 */
	public final static String sha1ToHex(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.hexEncode(digestResult);
	}

	/**
	 * 对输入字符串进行sha1散列, 返回Base64编码的结果.
	 */
	public final static String sha1ToBase64(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.base64Encode(digestResult);
	}

	/**
	 * 对输入字符串进行sha1散列, 返回Base64编码的URL安全的结果.
	 */
	public final static String sha1ToBase64UrlSafe(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.base64UrlSafeEncode(digestResult);
	}
	
	/**
	 * 字符串MD5加密
	 * @param s
	 * @param charset
	 * @return String
	 */
	public static String stringMd5(String s, String charset) {
		byte[] digestResult = DigestUtils.digest(s, DigestUtils.MD5, charset);
		return EncodeUtils.hexEncode(digestResult);
	}
	
	public static String stringMd5(String s) {
		byte[] digestResult = DigestUtils.digest(s, DigestUtils.MD5);
		return EncodeUtils.hexEncode(digestResult);
	}
	
	private static byte[] digest(String input, String algorithm, String charset) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			return messageDigest.digest(input.getBytes(charset));
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Security exception UnsupportedEncodingException", e);
		}
	}

	/**
	 * 对字符串进行散列, 支持md5与sha1算法.
	 */
	private static byte[] digest(String input, String algorithm) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			return messageDigest.digest(input.getBytes());
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	//-- File Hash function --//
	/**
	 * 对文件进行md5散列,返回Hex编码结果.
	 */
	public final static String md5ToHex(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * 对文件进行sha1散列,返回Hex编码结果.
	 */
	public final static String sha1ToHex(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	/**
	 * 对文件进行散列, 支持md5与sha1算法.
	 */
	private static String digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return EncodeUtils.hexEncode(messageDigest.digest());

		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}
}
