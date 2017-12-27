package com.dhcc.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * 密码加密类MD5
 * @author leije
 * @since 2010/08/27
 */
public class HashUtil
{
	private static MessageDigest mesDigest = null;

	public static final synchronized String hashCode(String data)
	{
		if (mesDigest == null)
		{
			try
			{
				mesDigest = MessageDigest.getInstance("MD5");
			}
			catch (NoSuchAlgorithmException nsae)
			{
				System.err.println("Failed to load the MD5 MessageDigest.");

				nsae.printStackTrace();
			}
		}

		mesDigest.update(data.getBytes());
		return encodeCode(mesDigest.digest());
	}

	public static final String encodeCode(byte[] bytes)
	{
		StringBuffer buffer = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; ++i)
		{
			if ((bytes[i] & 0xFF) < 16)
			{
				buffer.append("0");
			}
			buffer.append(Long.toString(bytes[i] & 0xFF, 16));
		}
		return buffer.toString();
	}

	public static final byte[] decodeCode(String hex)
	{
		char[] chars = hex.toCharArray();
		byte[] bytes = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2)
		{
			byte newByte = 0;
			newByte = (byte) (newByte | hexCharToByte(chars[i]));
			newByte = (byte) (newByte << 4);
			newByte = (byte) (newByte | hexCharToByte(chars[(i + 1)]));
			bytes[byteCount] = newByte;
			++byteCount;
		}
		return bytes;
	}

	private static final byte hexCharToByte(char ch)
	{
		switch (ch)
		{
			case '0':
				return 0;
			case '1':
				return 1;
			case '2':
				return 2;
			case '3':
				return 3;
			case '4':
				return 4;
			case '5':
				return 5;
			case '6':
				return 6;
			case '7':
				return 7;
			case '8':
				return 8;
			case '9':
				return 9;
			case 'a':
				return 10;
			case 'b':
				return 11;
			case 'c':
				return 12;
			case 'd':
				return 13;
			case 'e':
				return 14;
			case 'f':
				return 15;
		}
		return 0;
	}
}