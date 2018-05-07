package com.models.cloud.pay.escrow.mi.pangu.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class PanguAes {
	static final String algorithmStr = "AES/ECB/PKCS5Padding";
	 
    static boolean isInited = false;
       
    private static byte[] encrypt(String content, String password) {
        try {
            byte[] keyStr = getKey(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr          
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);//   ʼ  
            byte[] result = cipher.doFinal(byteContent);
            return result; //     
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
        return null;
    }
  
    private static byte[] decrypt(byte[] content, String password) {
        try {
            byte[] keyStr = getKey(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr           
            cipher.init(Cipher.DECRYPT_MODE, key);//   ʼ  
            byte[] result = cipher.doFinal(content);
            return result; //     
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
        return null;
    }
     
    private static byte[] getKey(String password) {
        byte[] rByte = null;
        if (password!=null) {
            rByte = password.getBytes();
        }else{
            rByte = new byte[24];
        }
        return rByte;
    }
 
    /**
     * 将二进制转换成16进制
     * @param buf
     * @return
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
     * 将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null; 
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
     
    /**
    *加密
    */
    public static String setEncrypt(String content,String password){
    	try{
    		if(password.length()!=16) throw new Exception("password must be 16 bit!");
            //加密之后的字节数组,转成16进制的字符串形式输出
    		return parseByte2HexStr(encrypt(content, password));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
     
    /**
    *解密
    */
    public static String getDecrypt(String content,String password){
		try {
			if(password.length()!=16)
				throw new Exception("password must be 16 bit!");
	        byte[] b = decrypt(parseHexStr2Byte(content), password);
	        return new String(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
     
    //测试用例
    public static void test1()throws Exception{
        String content = "你好";
        String keyBytes = "abcdefgabcdefg12";
        String pStr = setEncrypt(content ,keyBytes);
        System.out.println("加密前："+content);
        System.out.println("加密后:" + pStr);
         
        String postStr = getDecrypt(pStr,keyBytes);
        System.out.println("解密后："+ postStr );
    }
     
    public static void main(String[] args) throws Exception{
        test1();
    }
}