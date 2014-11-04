package com.sdutlinux.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
	
	
	public static String getTime() {	// 获得当前时间
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(date);
		return time;
	}
}
