package com.shujie.uploader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * <p> description: TODO
 * <p> 2019/01/19
 *
 * @author ssj
 * @version 1.0.0
 */
@Component
public class DesService {

    @Value("${uploader.key:test-key}")
    private String key;
    private SecretKey secretKey;


    @PostConstruct
    public void init() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        DESKeySpec desKeySpec=new DESKeySpec(key.getBytes());
        SecretKeyFactory factory= SecretKeyFactory.getInstance("DES");
        secretKey = factory.generateSecret(desKeySpec);
    }

    public String encode(String src) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Cipher cipher=Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] result=cipher.doFinal(src.getBytes());
        byte[] encode = Base64.getUrlEncoder().encode(result);
        return new String(encode, "utf-8");
    }

    public String decode(String src) throws UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        byte[] decode = Base64.getUrlDecoder().decode(src.getBytes("utf-8"));
        Cipher cipher=Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] res = cipher.doFinal(decode);
        return new String(res, "utf-8");
    }

}
