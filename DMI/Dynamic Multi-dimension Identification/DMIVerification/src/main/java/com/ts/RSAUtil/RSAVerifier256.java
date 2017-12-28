package com.ts.RSAUtil;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


/**
 * RSA数据签名器
 */
public class RSAVerifier256{
	
    public static final String tag = "RSAVerifier";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA256WithRSA";
    private String charset = "UTF-8";
    
    /**
     * key类型，包括BASE64格式(BASE64)，16进制格式(HEX)，文件格式(FILE)等 init中根据keyType和
     * publicKey来初始化Signature 上下文
     */
    private String publicKey;
    private String keyType = "BASE64";

    private PublicKey pubKey;

    /**
     * 对数据进行签名验证
     */
    public boolean verify(Object data, String signature) {
        Signature sig = null;
        byte[] dataBytes = null;
        try {
            if (data instanceof byte[]) {
                dataBytes = (byte[]) data;
            } else if (data instanceof String) {
                dataBytes = ((String) data).getBytes(charset);
            }
            sig = Signature.getInstance(SIGN_ALGORITHM);
            sig.initVerify(pubKey);
            byte[] sigBytes = Hex.decodeHex(signature.toCharArray());
            sig.update(dataBytes);
            return sig.verify(sigBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 初始化Signature实例
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws IOException
     * @throws UnrecoverableKeyException
     */
    public void init() throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException,
            CertificateException, KeyStoreException, IOException, UnrecoverableKeyException {
        if (publicKey != null) {
            if ("BASE64".equals(keyType)) {
                byte[] publicKeyBytes = Base64.decodeBase64(publicKey.getBytes(charset));
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                pubKey = keyFactory.generatePublic(x509KeySpec);
            } else if ("FILE_CER".equals(keyType)) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream input = new FileInputStream(publicKey);
                Certificate cert = cf.generateCertificate(input);
                pubKey = cert.getPublicKey();
            }
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }


    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RSAVerifier [charset=");
        builder.append(charset);
        builder.append(", publicKey=");
        builder.append(publicKey);
        builder.append(", keyType=");
        builder.append(keyType);
        builder.append(", pubKey=");
        builder.append(pubKey);
        builder.append("]");
        return builder.toString();
    }
    
}
