package com.gennlife.platform.test;

import com.gennlife.platform.model.Lab;
import com.gennlife.platform.model.Uprofession;
import com.gennlife.platform.model.User;
import com.gennlife.platform.util.HttpRequestUtils;
import com.gennlife.platform.util.ParamUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liumingxin
 * @create 2018 04 18:46
 * @desc
 **/
public class TestModel {
    public static void main(String[] args) throws Exception {

        JsonObject p = new JsonObject();
        p.addProperty("indexName", "bbb");
        p.addProperty("data", "asdf");
        String paramStr = new Gson().toJson(p);
        System.out.println(paramStr);

//        String url="http://10.0.2.53:8989/synonyms";
//        Map<String,String> map=new HashMap<>();
//        map.put("field","...");
//        map.put("keyword","...");
//        String aaa=HttpRequestUtils.doGet(url,map,null);
//        System.out.println(aaa);
//        String x=null;
//        Uprofession up=new Uprofession();
//        if(!StringUtils.isEmpty(x) &&!up.getUprofession().contains(x)){
//            System.out.println("cuowu");
//        }else{
//            System.out.println("meiyou");
//        }
//        System.out.println("abcdefg");
//
//        String x=new SimpleDateFormat().format(new Date());
//
//        System.out.println(x);
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat time1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        String date1="2020/1/1 0:00";
//        Date date=time1.parse(date1);
//        System.out.println("Aaa");
//
//        Lab lab=new Lab();
//        lab.setLab_name("aaa");
//        System.out.println(new Gson().toJson(lab));

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

//        JsonParser jsonParser=new JsonParser();
//        String param = "{\"role\":\"aaa\",\"staff\":[\"admin@ytyhdyy.com\"],\"resources\":[],\"desctext\":\"\"}";
//        JsonObject paramObj = (JsonObject) jsonParser.parse(param);
//        System.out.println("aaa");
//
//        User user =new User();
//        user.setStatus_now("agcd");
//        System.out.println("asdf");
    }

}
