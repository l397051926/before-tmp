package com.gennlife.platform.util;

import com.gennlife.platform.bean.conf.SystemDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chenjinfeng on 2017/2/22.
 */
@Component
public class GStringUtils {
    /**
     * 字符串转16进制
     */
    private static SystemDefault systemDefault ;
    @Autowired
    public void setSystemDefault(SystemDefault systemDefault)
    {
        GStringUtils.systemDefault=systemDefault;
    }

    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public static String str2Password(String str) {
        if (systemDefault != null && "v2".equalsIgnoreCase(systemDefault.getPasswdOperator()))
            return getMD5(toHexString(str));
        else return str;
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

    public static String getDefaultPasswd() {
        String password = "ls123456";
        if (systemDefault != null && "v2".equalsIgnoreCase(systemDefault.getPasswdOperator()))
            return GStringUtils.str2Password(password);
        else
            return password;

    }

    private static Pattern emailPattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

    public static boolean checkEmail(String email) {
        Matcher match = emailPattern.matcher(email);
        return match.find();
    }

    public static void main(String[] args) {
        System.out.println(checkEmail("1@qq.com"));
    }
}
