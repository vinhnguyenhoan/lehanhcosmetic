package com.lehanh.pama.util;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lehanh.pama.IJsonDataObject;

public class JsonMapper {
	
	// TODO research ERROR: JDWP Unable to get JNI 1.2 environment, jvm->GetEnv() return code = -2
	//JDWP exit error AGENT_ERROR_NO_JNI_ENV(183):  [../../../src/share/back/util.c:838]

	private static Gson gson;
	static {
		gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
								//.setFieldBindingStrategy(FieldBindingStrategy.GET_METHOD)
				.serializeSpecialFloatingPointValues()
								.create();
	}
	
	public static final String toJson(IJsonDataObject obj) {
		return gson.toJson(obj);
	}
	
	public static final String toJson(Map<?, ?> obj) {
		return gson.toJson(obj);
	}
	
	public static final <T> T fromJson(String json, Class<T> classT) {
		return gson.fromJson(json, classT);
	}
	
}