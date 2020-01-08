package com.dim.ke.framework.core.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: xiongxiaojun
 * @Date: 2019/6/18 14:50
 */
public class AESCipher {
    private static final String charset = "UTF-8";
    public static final String API_KEY = "1234567890123456";
    public static final String API_IV = "0392039203920300";

    public static String aesEncryptString(String content, String key,String ivString) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] contentBytes = content.getBytes(charset);
        byte[] keyBytes = key.getBytes(charset);
        byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes,ivString);
//        Encoder encoder = Base64.getEncoder();
//        return encoder.encodeToString(encryptedBytes);
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
    }

    public static String aesDecryptString(String content, String key,String ivString) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
//        Decoder decoder = Base64.getDecoder();
        byte[] encryptedBytes = Base64.decode(content, Base64.NO_WRAP);
        byte[] keyBytes = key.getBytes(charset);
        byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes,ivString);
        return new String(decryptedBytes, charset);
    }

    private static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes,String ivString) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE,ivString);
    }

    private static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes,String ivString) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE,ivString);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode,String ivString) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = ivString.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);

        return cipher.doFinal(contentBytes);
    }

    public static void main(String[] args) throws Exception{
        String before = aesDecryptString("JoXpDJXA0aaccEZ3DFWRsgAxFgY7KDj1t5t6JiBcScH6HGq3c6DaHbNNq4fjNJ01mk8CxbtwXOQBfELkugJ2X6AIgi+mV01usz9ZhPgXUiyqi05Yok1Ws/UrLbxrDFZhcyoPrJoSbED+8QnSKJKRVw==","1234567890123456","0392039203920300");
        System.out.println(before);
    }
}
