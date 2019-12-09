package com.bigdata.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationPrefs extends Properties {
	public static ApplicationPrefs prefs = null;

	public synchronized static ApplicationPrefs getApplicationPrefs() {
		if (prefs == null) {
			prefs = new ApplicationPrefs();
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
			try {
				prefs.load(is);
			} catch (IOException e) {
				log.debug("加载系统配置文件失败!", e);
			}
		}
		return prefs;
	}

	public static String value(String key) {
		return getApplicationPrefs().getProperty(key);
	}

	public static String value(String key, String defaultvalue) {
		String value = value(key);
		if (value == null || "".equals(value.replace(" ", ""))) {
			value = defaultvalue;
		}
		return value;
	}

	public static String getString(String key) {
		return getApplicationPrefs().getProperty(key);
	}

	public static Boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

}
