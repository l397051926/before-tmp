package com.gennlife.platform.filter;

import com.gennlife.platform.util.GsonUtil;
import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chensong on 2016/1/15.
 */
public class ScatterDrawer {
    private static JsonParser jsonParser = new JsonParser();
    private static Gson gson = GsonUtil.getGson();

    public String filter(JsonObject param, String content,String row) {
        //取过滤信息
        JsonObject condition = param.getAsJsonObject("condition");
        if (condition != null && condition.get("xoy") != null && condition.get("action") != null) {
            String xoy = condition.get("xoy").getAsString();
            String action = condition.get("action").getAsString();
            String logic = null;
            Map<String, Double> map = new HashMap<String, Double>();
            if ("filter".equals(action)) {
                JsonObject item = condition.getAsJsonObject("item");
                logic = item.get("logic").getAsString();
                JsonArray operates = item.getAsJsonArray("operates");
                for (JsonElement entry : operates) {
                    JsonObject op = entry.getAsJsonObject();
                    String operate = op.get("operate").getAsString();
                    Double threshold = Double.parseDouble(op.get("threshold").getAsString());
                    map.put(operate, threshold);
                }
            }

            JsonObject resultO = jsonParser.parse(content).getAsJsonObject();
            JsonObject data1 = resultO.get("data").getAsJsonObject();
            JsonArray plot_dataArray = data1.getAsJsonArray("plot_data");
            for(JsonElement plot_dataEntry:plot_dataArray){
                JsonObject plot_data = plot_dataEntry.getAsJsonObject();
                JsonArray series = plot_data.getAsJsonArray("series");
                for (JsonElement entry : series) {
                    JsonObject item = entry.getAsJsonObject();
                    JsonArray data2 = item.getAsJsonArray("data");
                    String name = item.get("name").getAsString();
                    if(!row.equals(name)){
                        continue;
                    }
                    JsonArray realData2 = new JsonArray();
                    item.add("data", realData2);
                    for (JsonElement entry1 : data2) {
                        JsonArray dataArray = entry1.getAsJsonArray();
                        double x = dataArray.get(0).getAsDouble();
                        double y = dataArray.get(1).getAsDouble();
                        int counter = map.size();//多少个条件
                        if("x".equals(xoy.toLowerCase())){
                            for (String op : map.keySet()) {
                                Double threshold = map.get(op);
                                if (((op.equals(">") && x > threshold) ||
                                        (op.equals(">=") && x >= threshold) ||
                                        (op.equals("<") && x < threshold) ||
                                        (op.equals("<=") && x <= threshold))) {//x满足条件
                                    counter --;//满足了减1
                                }
                            }

                        }else if ("y".equals(xoy.toLowerCase())){
                            for (String op : map.keySet()) {
                                Double threshold = map.get(op);
                                if (((op.equals(">") && y > threshold) ||
                                        (op.equals(">=") && y >= threshold) ||
                                        (op.equals("<") && y < threshold) ||
                                        (op.equals("<=") && y <= threshold))) {//x满足条件
                                    counter --;//满足了减1
                                }
                            }

                        }
                        if (counter == 0 && "and".equals(logic.toLowerCase())) {//满足所有的
                            JsonArray realDataArray2 = new JsonArray();
                            realDataArray2.add(x);
                            realDataArray2.add(y);
                            realData2.add(realDataArray2);
                        }else if(counter != map.size() && "or".equals(logic.toLowerCase())){
                            JsonArray realDataArray2 = new JsonArray();
                            realDataArray2.add(x);
                            realDataArray2.add(y);
                            realData2.add(realDataArray2);
                        }

                    }
                }
            }

            return gson.toJson(resultO);
        }
        return content;
    }
}
