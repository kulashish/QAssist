package com.aneedo.search.util;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	Properties props = new Properties();
	
	static final PropertiesUtil util = new PropertiesUtil(); 
	
	private PropertiesUtil() {
		try {
		props.load(new FileInputStream(
				SemClassConstants.PROPERTY_FILE));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PropertiesUtil getInstance() {
		return util;
	}
	
	public String getStringValue(String key) {
		return props.getProperty(key);
	}
	
	public Integer getIntegerValue(String key) {
		try {
		return Integer.parseInt(props.getProperty(key));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Double getDoubleValue(String key) {
		try {
			return Double.parseDouble(props.getProperty(key));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

	}
	
	public Float getFloatValue(String key) {
		try {
			return Float.parseFloat(props.getProperty(key));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

	}

}
