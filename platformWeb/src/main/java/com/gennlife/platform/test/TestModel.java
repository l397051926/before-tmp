package com.gennlife.platform.test;

import com.gennlife.platform.model.User;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liumingxin
 * @create 2018 04 18:46
 * @desc
 **/
public class TestModel {
    public static void main(String[] args) throws ParseException {
//        System.out.println("abcdefg");
//
//        String x=new SimpleDateFormat().format(new Date());
//
//        System.out.println(x);
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date d1=new Date();
//        System.out.println(d1);
//        String a =time.format(d1);
//        System.out.println(a);
//        Date date =time.parse(a);
//        System.out.println(date);
//
//        try{
//            String x1="2212.22.2";
//            Date x2= time.parse(x1);
//            System.out.println(x2);
//
//        }catch (Exception e){
//            System.out.println("cc");
//        }
//
//
//        System.out.println(1111);
//        String a="1";
//        System.out.println("1".equals(a));
//        System.out.println("1".equals(null));

        JsonParser jsonParser=new JsonParser();
        String param = "{\"role\":\"aaa\",\"staff\":[\"admin@ytyhdyy.com\"],\"resources\":[],\"desctext\":\"\"}";
        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
        System.out.println("aaa");
    }

}
