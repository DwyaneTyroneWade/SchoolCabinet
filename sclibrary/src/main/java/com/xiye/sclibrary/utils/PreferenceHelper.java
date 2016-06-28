package com.xiye.sclibrary.utils;

import java.io.Serializable;

import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.xiye.sclibrary.base.C;


public class PreferenceHelper {
	
	public static void setInt(String key, int value) {
		PreferenceManager.getDefaultSharedPreferences(C.get()).edit()
				.putInt(key, value).commit();
	}

	public static void setString(String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(C.get()).edit()
				.putString(key, value).commit();
	}

	public static void setBoolean(String key, boolean value) {
		PreferenceManager.getDefaultSharedPreferences(C.get()).edit()
				.putBoolean(key, value).commit();
	}

	public static void setLong(String key, long value) {
		PreferenceManager.getDefaultSharedPreferences(C.get()).edit()
				.putLong(key, value).commit();
	}

	public static void setFloat(String key, float value) {
		PreferenceManager.getDefaultSharedPreferences(C.get()).edit()
				.putFloat(key, value).commit();
	}

	public static int getInt(String key, int defValue) {
		return PreferenceManager.getDefaultSharedPreferences(C.get())
				.getInt(key, defValue);
	}

	public static String getString(String key, String defValue) {
		return PreferenceManager.getDefaultSharedPreferences(C.get())
				.getString(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return PreferenceManager.getDefaultSharedPreferences(C.get())
				.getBoolean(key, defValue);
	}

	public static long getLong(String key, long defValue) {
		return PreferenceManager.getDefaultSharedPreferences(C.get())
				.getLong(key, defValue);
	}

	public static float getFloat(String key, float defValue) {
		return PreferenceManager.getDefaultSharedPreferences(C.get())
				.getFloat(key, defValue);
	}
	
	public static void setSerializeObj(String key, Serializable obj) {
		try {
			Editor editor = PreferenceManager.getDefaultSharedPreferences(
					C.get()).edit();
			editor.putString(key, ObjectSerializer.serialize(obj));
			editor.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	public static Object getSerializeObj(String key) {
		try {
			Object obj = ObjectSerializer.deserialize(PreferenceManager
					.getDefaultSharedPreferences(C.get()).getString(key, null));
			return obj;
		} catch (Exception e) {
			return null;
			// TODO Auto-generated catch block
		}
	}
	
	public static void remove(String key) {
		PreferenceManager.getDefaultSharedPreferences(C.get()).edit()
				.remove(key).commit();
	}
 
}
