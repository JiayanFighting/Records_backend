package com.demo.records.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.records.translate.TransApi;
import com.demo.records.utils.Result;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tool")
public class ToolController {
    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20200921000570393";
    private static final String SECURITY_KEY = "CY7SP6WlPm7sf0Q4WZRc";

    @ResponseBody
    @RequestMapping("/removeLine")
    public Result removeLine(String content){
        System.out.println(content);
        StringBuilder sb = new StringBuilder();
        String[] lineList = content.split("\n");
        for (String s:lineList){
            sb.append(s);
            if (s.charAt(s.length()-1) == '.') {
                sb.append("\n");
            }else{
                sb.append(" ");
            }
        }
        Result res = new Result();
        String translatedContent = translate(sb.toString());
        res.put("data",sb.toString());
        res.put("translatedData",translatedContent);
        return res;
    }


    public String translate(String query) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String json = api.getTransResult(query, "en", "zh");
        System.out.println(json);
        JSONObject jsonObject = JSONObject.parseObject(json);

        if (jsonObject.containsKey("trans_result")){
            Object jsonArray = jsonObject.get("trans_result");
            if (jsonArray instanceof JSONArray){
                Object[] objects = ((JSONArray) jsonArray).toArray();
                StringBuilder sb = new StringBuilder();
                for (Object item:objects) {
                    if (item instanceof JSONObject){
                        sb.append(((JSONObject) item).getString("dst"));
                    }
                    sb.append("\n");
                }
                return sb.toString();

            }
        }
        return "error";
    }
}
