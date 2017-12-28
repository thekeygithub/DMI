package com.ts.RSAUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;


public class RSASignaturer256 {

    private final static Logger log = LoggerFactory.getLogger(RSASignaturer256.class);

    private String keyType;
    private String privateKey;

    private PrivateKey priKey;

    public static final String tag = "RSASignaturer";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA256WithRSA";
    private String charset = "UTF-8";


    public Object sign(Object data) throws SignatureException {
        try {
            
            Signature dsa = Signature.getInstance(SIGN_ALGORITHM);
            dsa.initSign(priKey);
            if (data instanceof String) {
                byte[] dataBytes = ((String) data).getBytes(charset);
                dsa.update(dataBytes);
                byte[] signature = dsa.sign();
                
                return transferHexString(signature);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("加签错误NoSuchAlgorithmException:" + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new SignatureException("加签错误InvalidKeyException:" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("加签错误UnsupportedEncodingException:" + e.getMessage());
        }
        return null;
    }

    public static String transferHexString(byte[] signature) {
        String hex = new String(Hex.encodeHex(signature));
        return hex;
    }

    public void init() throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, CertificateException,
            KeyStoreException, IOException, UnrecoverableKeyException { 
        
        if (privateKey != null) { 
            if ("BASE64".equals(keyType)) {
                byte[] privateKeyBytes = Base64.decodeBase64(privateKey.getBytes(charset));
                PKCS8EncodedKeySpec pkcsKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                priKey = keyFactory.generatePrivate(pkcsKeySpec);
            }
        }
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RSASignaturer [charset=");
        builder.append(charset);
        builder.append(", privateKey=");
        builder.append(privateKey);
        builder.append("]");
        return builder.toString();
    }
}