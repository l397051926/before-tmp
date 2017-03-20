package com.gennlife.platform.test;

import com.csvreader.CsvReader;
import com.google.gson.JsonObject;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by luoxupan on 2017/2/28.
 */
public class csvInputToMysql {

    public static Map<String, ArrayList<String>> readFileFromCsv(String url) {
        JsonObject json = new JsonObject();
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        File file = new File(url);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            CsvReader creader = new CsvReader(reader, ',');
            while (creader.readRecord()) {
                ArrayList<String> array = new ArrayList<String>();
                String str = creader.getRawRecord();
                String[] s = str.split(",");
//                json.addProperty(s[0], s[1]);
//                map.put(s[0], s[1]);

                ArrayList<String> arrayList = map.get(s[0]);
                if (arrayList != null && arrayList.size() != 0) {
                    arrayList.add(s[1]);
                    map.put(s[0], arrayList);
                } else {
                    array.add(s[1]);
                    map.put(s[0], array);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void writeJsonToFile(String pathname, String json) {
        File file = new File(pathname);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        JsonObject json = new JsonObject();
        Map<String, ArrayList<String>> mapFromCsv;
        Map<String, String> map = new HashMap<String, String>();
        String url = "jdbc:mysql://119.253.135.14:3306/userInfoDB?useUnicode=true&characterEncoding=UTF-8&" + "user=userInfoDB&password=@userInfoDB2015";
        String sql = "SELECT labID, lab_name FROM gennlife_lab";

        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (!rs.wasNull()) {

                String key;
                String value;
                while (rs.next()) {
                    key = rs.getString(1);
                    value = rs.getString(2);
                    json.addProperty(key, value);
                    map.put(value, key);
                }

                mapFromCsv = readFileFromCsv("/Users/luoxupan/Documents/ksysgx.csv");
                Iterator it = mapFromCsv.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    // String sq = "INSERT INTO gennlife_lab_map (lab_id, depart_name) VALUES('" + map.get(entry.getKey()) + "','" + entry.getValue() + "')";
                    // stmt.executeUpdate(sq);
                    ArrayList<String> arr = (ArrayList<String>) entry.getValue();
                    for (String str : arr) {
                        String sq = "INSERT INTO gennlife_lab_map (lab_id, depart_name) VALUES('" + map.get(entry.getKey()) + "','" + str + "')";
                        stmt.executeUpdate(sq);
                    }
                }

                // writeJsonToFile("/Users/luoxupan/json_txt", json.toString());
            }

            // if (stmt.execute(sql)) {
            //      rs = stmt.getResultSet();
            // }
        } catch (SQLException e) {
            System.out.println("MySQL操作错误: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
