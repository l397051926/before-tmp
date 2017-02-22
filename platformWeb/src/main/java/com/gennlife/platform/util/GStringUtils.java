package com.gennlife.platform.util;

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
}
