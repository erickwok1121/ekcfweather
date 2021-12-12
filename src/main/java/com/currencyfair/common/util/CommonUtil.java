package com.currencyfair.common.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CommonUtil {

	private static List<String> AVAILABLE_LOCALE = new ArrayList<String>();

	static {
		AVAILABLE_LOCALE.add("en");
		AVAILABLE_LOCALE.add("zh");
	}

	public static <T> T restServiceGet(RestTemplate restTemplate, String path, Map<String, ?> uriVariables, Class<T> responseType) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(path);
		URI uri = null;
		if (uriVariables == null) {
			uri = builder.build().toUri();
		} else {
			for (String key : uriVariables.keySet()) {
				builder.queryParam(key, uriVariables.get(key));
			}
			uri = builder.build(uriVariables);
		}

		return restTemplate.getForObject(uri, responseType);
	}
	
	public static <T> T restServicePost(RestTemplate restTemplate, String path, Map<String, ?> uriVariables, Object body, Class<T> responseType) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(path);
		URI uri = null;
		if (uriVariables == null) {
			uri = builder.build().toUri();
		} else {
			uri = builder.build(uriVariables);
		}
		
		return restTemplate.postForObject(uri, body, responseType);
	}
}
