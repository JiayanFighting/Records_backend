package com.demo.records.utils;

import com.alibaba.fastjson.JSONObject;

public class Result extends JSONObject {
    private static final long serialVersionUID = 1L;

    public Result() {
        put("code", 0);
        put("msg", "Success");
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put("msg", msg);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    public static Result error() {
        return error(1, "Fail");
    }

    public static Result error(String msg) {
        return error(500, msg);
    }

    public static Result error(int code, String msg) {
        Result r = new Result();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Result put(int key, Object value) {
        super.put(String.valueOf(key),value);
        return this;
    }

}
