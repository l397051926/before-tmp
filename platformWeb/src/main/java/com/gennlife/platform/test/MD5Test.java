package com.gennlife.platform.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chensong on 2015/12/2.
 */
public class MD5Test {
    public static void main(String[] args){
        String pwd  = "abc123456";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(pwd.getBytes());
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i<result.length;i++){
                sb.append(result[i]);
            }
            System.out.println(sb.toString());
            System.out.println(sb.toString().length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println(MD5("abc123456"));
        System.out.println(MD5("abc123456").length());
    }
    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return"";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }
}
