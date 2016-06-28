package com.xiye.sclibrary.serializable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parser {

	public static <T> T parse(String json, Class<T> className) {
		try {
			JSONObject jsnObj = new JSONObject(json);
			return parse(jsnObj, className);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
	}

	public static <T> T parse(JSONObject json, Class<T> className) {
		try {
			T obj = null;
			try {
				Constructor<T> constructor = className.getDeclaredConstructor();
				if (!constructor.isAccessible()) {
					constructor.setAccessible(true);
				}
				obj = constructor.newInstance();
			} catch (Exception e) {
				// TODO: handle exception
			}
			obj = UnsafeAllocator.create().newInstance(className);

			// Field[] fields = className.getFields();
			List<Field> fields = filterAllFields(className);
			for (int i = 0; i < fields.size(); i++) {
				Field f = fields.get(i);
				if (Modifier.isStatic(f.getModifiers())){
					continue;
				}
				if (!f.isAccessible()) {
					f.setAccessible(true);
				}
				Key key = f.getAnnotation(Key.class);
				String jsKey = null;
				if (key != null) {
					jsKey = key.key();
				} else {
					jsKey = f.getName();
				}
				if (jsKey.equals("ORIGIN_DATA")){
					String jsonString = json.toString();
					f.set(obj, jsonString);
					continue;
				}
				Class type = f.getType();
				if (type.isPrimitive()) {
					obj = parsePrimitive(obj, json, f, jsKey);
				} else if (type == String.class) {
					obj = parseString(obj, json, f, jsKey);
				} else if (isList(type)) {
					obj = parseList(obj, json, f, jsKey);
				} else if (isMap(type)) {
					obj = parseMap(obj, json, f, jsKey);
				} else if (type.isArray()) {
					obj = parseArray(obj, json, f, jsKey);
				} else {
					obj = parseObj(obj, json, f, jsKey);
				}
			}
			return obj;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static <T> T parsePrimitive(T obj, JSONObject jsn, Field f,
			String key) {
		// System.out.println("parsePrimitive");
		Class type = f.getType();

		try {
			if (type.getName().equals("int")) {
				int ret = jsn.getInt(key);
				f.setInt(obj, ret);
			} else if (type.getName().equals("boolean")) {
				boolean ret = jsn.getBoolean(key);
				f.setBoolean(obj, ret);
			} else if (type.getName().equals("long")) {
				long ret = jsn.getLong(key);
				f.setLong(obj, ret);
			} else if (type.getName().equals("double")) {
				double ret = jsn.getDouble(key);
				f.setDouble(obj, ret);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			 e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	private static <T> T parseString(T obj, JSONObject jsn, Field f, String key) {
		// System.out.println("parseString");
		try {
			String value = jsn.getString(key);
			if ("null".equals(value)){
				value = null;
			}
			f.set(obj, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			 e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;
	}

	private static <T> T parseList(T obj, JSONObject jsn, Field f, String key) {
//		 System.out.println("parseList");
		try {
			ParameterizedType ft = (ParameterizedType) f.getGenericType();
			Class clazz = (Class) ft.getActualTypeArguments()[0];
			ArrayList list = new ArrayList();

			JSONArray ja = jsn.getJSONArray(key);
			for (int i = 0; i < ja.length(); i++) {
				Object o = ja.get(i);
				if (o.getClass().equals(clazz)) {
					list.add(o);
				} else {
					if (clazz.equals(String.class)) {
						if (o instanceof JSONObject) {
							list.add(((JSONObject) o).toString());
						} else {
							list.add(String.valueOf(o));
						}
					} else {
						if (o instanceof JSONObject) {
							list.add(parse((JSONObject) o, clazz));
						}
					}
				}
			}
			f.set(obj, list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			 e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	private static <T> T parseMap(T obj, JSONObject jsn, Field f, String key) {
		// System.out.println("parseMap");
		ParameterizedType ft = (ParameterizedType) f.getGenericType();
		Type[] atypes = ft.getActualTypeArguments();
		if (atypes.length < 2) {
			return obj;
		}
		Class clazz = (Class) atypes[1];
//		System.out.println("clazz: " + clazz);
		Map map = new HashMap();
		try {
			JSONObject jsnMap = jsn.getJSONObject(key);
			Iterator<String> keys = jsnMap.keys();
			while (keys.hasNext()) {
				String jsnKey = keys.next();
				Object o = jsnMap.get(jsnKey);
				if (o.getClass().equals(clazz)) {
					map.put(jsnKey, o);
				} else {
					if (clazz.equals(String.class)) {
						if (o instanceof JSONObject) {
							map.put(jsnKey, ((JSONObject) o).toString());
						} else {
							map.put(jsnKey, String.valueOf(o));
						}
					} else {
						if (o instanceof JSONObject) {
							map.put(jsnKey, parse((JSONObject) o, clazz));
						}
					}
				}
			}
			f.set(obj, map);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;
	}

	private static <T> T parseArray(T obj, JSONObject jsn, Field f, String key) {
		// System.out.println("parseArray");
		Class clazz = (Class) f.getType().getComponentType();
		// System.out.println(clazz);
		try {
			JSONArray ja = jsn.getJSONArray(key);
			Object array = Array.newInstance(clazz, ja.length());
			for (int i = 0; i < ja.length(); i++) {
				Object o = ja.get(i);
				if (o.getClass().equals(clazz)) {
					Array.set(array, i, o);
				} else {
					if (clazz.equals(String.class)) {
						if (o instanceof JSONObject) {
							Array.set(array, i, ((JSONObject) o).toString());
						} else {
							Array.set(array, i, String.valueOf(o));
						}
					} else {
						if (o instanceof JSONObject) {
							Array.set(array, i, parse((JSONObject) o, clazz));
						}
					}
				}
			}
			f.set(obj, array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	private static <T> T parseObj(T obj, JSONObject jsn, Field f, String key) {
		try {
			JSONObject jobj = jsn.getJSONObject(key);
			Class clazz = f.getType();
			f.set(obj, parse(jobj, clazz));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	private static boolean isList(Class type){
		return List.class.isAssignableFrom(type);
	}
	
	private static boolean isMap(Class type){
		return Map.class.isAssignableFrom(type);
	}

	private static List<Field> filterAllFields(Class clazz) {
		ArrayList<Field> ret = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			ret.add(fields[i]);
		}
		Class superClass = clazz.getSuperclass();
		if (superClass != null && !superClass.equals(Object.class)) {
			ret.addAll(filterAllFields(superClass));
		}
		return ret;
	}
}
