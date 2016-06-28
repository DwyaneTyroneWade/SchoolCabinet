package com.xiye.schoolcabinet.manager;

import android.content.Context;

import com.xiye.sclibrary.base.C;
import com.xiye.sclibrary.utils.ObjectSerializer;

import java.io.Serializable;

/**
 * Created by xqq on 2016/4/13.
 */
public class CacheManager {

    public static final String CACHE_FILE = "com_xiye_schoolcabinet_cache";

    public static final String CACHE_KEY_CABINET_ID_ALREADY = "cabinet_id_already";


    public static void setCache(String key, String value) {
        C.get().getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE).edit().putString(generatedKey(key), value).commit();
    }

    public static String loadCache(String key) {
        return C.get().getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE).getString(generatedKey(key), "");
    }

    public static void setIntCache(String key, int value) {
        C.get().getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE).edit().putInt(generatedKey(key), value).commit();
    }

    public static int loadIntCache(String key) {
        return C.get().getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE).getInt(generatedKey(key), 0);
    }

    public static void setSerializableCache(String key, Serializable value) {
        try {
            C.get().getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE).edit().putString(generatedKey(key), ObjectSerializer.serialize(value)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object loadSerializableCache(String key) {
        Object obj = null;
        try {
            obj = ObjectSerializer.deserialize(C.get().getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE).getString(generatedKey(key), null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void clearCache(String key) {
        C.get().getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE).edit().remove(generatedKey(key)).commit();
    }

    public static void clearAllCache() {
        C.get().getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE).edit().clear().commit();
    }

    private static String generatedKey(String key) {
        //TODO 设备ID
        String uid = "";
        key = key + "_" + uid;
        return key;
    }
}
