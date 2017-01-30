package crave.com.android.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import crave.com.android.bll.EasyFoodApplication;

/**
 * An object that helps persist and retrieve values to/from shared preferences files.
 * @author sumanth@wilshireaxon.com
 *
 */
public enum PreferencesManager {

	Instance;

	public static final String USER_PREF_FILE = "user";
	public static final String SERVER_CONFIG_FILE = "ServerConfig";

	public <T> boolean storeKeyValueList(Class<T> typeClass, String prefType, Map<String, T> map) {
        if (TextUtils.isEmpty(prefType) || map == null || map.isEmpty()) {
            throw new IllegalArgumentException("prefType or key value map is null or empty");
        }
		SharedPreferences pref = getPreferencesObject(prefType.toLowerCase());
        SharedPreferences.Editor editor = pref.edit();
		for(String keyName : map.keySet()) {
			if(typeClass == String.class) {
				editor.putString(keyName, (String)map.get(keyName));
			}
			else if(typeClass == Integer.class) {
				editor.putInt(keyName, (Integer)map.get(keyName));
			}
			else if(typeClass == Long.class) {
				editor.putLong(keyName, (Long)map.get(keyName));
			}
			else if(typeClass == Boolean.class) {
				editor.putBoolean(keyName, (Boolean)map.get(keyName));
			}
			else {
				throw new IllegalArgumentException("Unsupported class type passed to storeKeyValueList");
			}
		}
		return editor.commit();
	}

	
	public <T> boolean storeKeyValue(Class<T> typeClass, String prefType, String keyName, T val) {
        if (TextUtils.isEmpty(prefType) || TextUtils.isEmpty(keyName)) {
            throw new IllegalArgumentException("prefType or keyName is null or empty");
        }
        Map<String, T> map = new HashMap<String, T>();
        map.put(keyName, val);
        return storeKeyValueList(typeClass, prefType, map);
	}

	public <T> Map<String, T> retrieveValueList(Class<T> typeClass, String prefType, String keyArr[]) {
        if (TextUtils.isEmpty(prefType) || keyArr == null || keyArr.length == 0) {
            throw new IllegalArgumentException("keyType or keyArr is null or empty");
        }
        Map<String, T> map = new HashMap<String, T>();
		SharedPreferences pref = getPreferencesObject(prefType.toLowerCase());
		for(String keyName : keyArr) {
			map.put(keyName, retrieveValueInternal(typeClass, pref, prefType, keyName, null));
		}
		return map;
	}

	public <T> T retrieveValue(Class<T> typeClass, String prefType, String keyName, T defVal) {
        if (TextUtils.isEmpty(prefType) || TextUtils.isEmpty(keyName)) {
            throw new IllegalArgumentException("keyType or keyName is null or empty");
        }
		SharedPreferences pref = getPreferencesObject(prefType.toLowerCase());
		return retrieveValueInternal(typeClass, pref, prefType, keyName, defVal);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T retrieveValueInternal(Class<T> typeClass, SharedPreferences pref, String prefType, String keyName, T defVal) {
		if(typeClass == String.class) {
			return (T)pref.getString(keyName, (String) defVal);
		}
		else if(typeClass == Integer.class) {
			return (T)(Integer)pref.getInt(keyName, (Integer)((defVal==null) ? 0 : defVal));
		}
		else if(typeClass == Long.class) {
			return (T)(Long)pref.getLong(keyName, (Long)((defVal==null) ? 0L : defVal));
		}
		else if(typeClass == Boolean.class) {
			return (T)(Boolean)pref.getBoolean(keyName, (Boolean)((defVal==null) ? false : defVal));
		}
        throw new IllegalArgumentException("Unsupported class type passed to retrieveValue");
	}
	
    private SharedPreferences getPreferencesObject(String fileName) {
        Context ctxt = EasyFoodApplication.getInstance().getApplicationContext();
        return ctxt.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
}