package com.pasc.lib.pay.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yintangwen952 on 2018/9/2.
 */

public class JsonUtils {

    private static class InstanceHolder {
        public static Gson gson = new Gson();
    }

    public static Gson getGson() {
        return InstanceHolder.gson;
    }

    public static String toJson(Object obj) {

        return toJson(obj, null);
    }

    public static String toJson(Object obj, Type typeOfT) {
        if (obj == null) {
            return "{}";
        }
        if (obj.getClass() == String.class) {
            return obj.toString();
        }
        if (typeOfT != null) {
            return InstanceHolder.gson.toJson(obj, typeOfT);
        } else {
            return InstanceHolder.gson.toJson(obj);
        }
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return InstanceHolder.gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return InstanceHolder.gson.fromJson(json, classOfT);
    }

    public static Map<String, String> mapFromJson(String json) {
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return fromJson(json, type);
    }

}
