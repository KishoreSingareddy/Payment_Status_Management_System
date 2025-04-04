package com.hulkhiretech.payments.util;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GsonUtils {

private final Gson gson;
	
	//convert json string to java object. if exception return null. Expect generic class type & return object accordingly.
	public <T> T fromJson(String json, Class<T> clazz) {
		try {
			return gson.fromJson(json, clazz);
		} catch (Exception e) {
			log.error("Unable to convert jsonString to Java object. Exception in fromJson||json:" + json + "||clazz:" + clazz);
			return null;
		}	
	
	}
}
