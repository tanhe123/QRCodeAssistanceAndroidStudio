package com.sdutlinux.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataService {
	private static final String PREFS_NAME = "data";
	
	private Context context;
	private SharedPreferences preferences;
	
	public DataService(Context context) {
		this.context = context;
		preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
	}
	
	public void saveString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public void saveBoolean(String key, Boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public String getString(String key) {
		String value = preferences.getString(key, "");
		return value;
	}
	
	public Boolean getBoolean(String key) {
		Boolean value = preferences.getBoolean(key, false);
		return value;
	}
	
	public void clear() {
		Editor editor = preferences.edit();
		
		editor.clear();
		editor.commit();
	}
}
