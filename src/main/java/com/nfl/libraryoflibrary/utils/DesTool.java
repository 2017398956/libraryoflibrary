package com.nfl.libraryoflibrary.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by nfl 2016-11-23
 */
public class DesTool {

    private static String key = "j83rks0n"  ;

    // 解密数据
    private static String decrypt(String message, String key) throws Exception {
        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    // 解密数据
    private static byte[] encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    private void test() throws Exception {
        String value = "test12341345";
        String jiami = java.net.URLEncoder.encode(value, "utf-8").toLowerCase();
        String a = toHexString(encrypt(jiami, key)).toUpperCase();
        System.out.println("加密后的数据为:" + a);
        String b = java.net.URLDecoder.decode(decrypt(a, key), "utf-8");
        System.out.println("解密后的数据:" + b);
    }

    private static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    private static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    /**
     * @param string 原始数据
     * @return 加密后的数据
     */
    public static String encrypt(String string){
        if(null == string){
            string = "" ;
        }
        String encryptCode = null;
        try {
            encryptCode = toHexString(encrypt(URLEncoder.encode(string, "utf-8").toLowerCase(), key)).toUpperCase() ;
        } catch (Exception e) {
            encryptCode = "" ;
        }
        return encryptCode ;
    }

    /**
     * @param string 加密后的数据
     * @return 原始数据
     */
    public static String decrypt(String string){
        if(null == string){
            string = "" ;
        }
        String decryCode = null ;
        try {
            decryCode = URLDecoder.decode(decrypt(string , key), "utf-8") ;
        } catch (Exception e) {
            decryCode = "" ;
        }
        return decryCode ;
    }
}
