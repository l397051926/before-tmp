package com.gennlife.platform.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Chenjinfeng on 2017/2/22.
 */
public class GStringUtils {
    /**
     * 字符串转16进制
     * */
    public static String toHexString(String s)
    {
        String str="";
        for (int i=0;i<s.length();i++)
        {
            int ch = (int)s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
    public static String str2Password(String str)
    {
        return getMD5(toHexString(str));
    }
    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
           return str;
        }
    }
    public static String getDefaultPasswd()
    {
        return GStringUtils.str2Password("ls123456");
    }
    public static void main(String[] args)
    {
        System.out.println(getDefaultPasswd());
    }
}
