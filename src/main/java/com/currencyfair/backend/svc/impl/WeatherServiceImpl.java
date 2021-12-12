package com.currencyfair.backend.svc.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.currencyfair.backend.svc.WeatherService;
import com.currencyfair.common.util.CommonUtil;
import com.google.common.collect.ImmutableMap;

import io.netty.util.internal.StringUtil;

@Service
public class WeatherServiceImpl implements WeatherService {
	
	@Autowired
	@Qualifier("asIntSystem")
	private RestTemplate restTemplate;
	
	@Value("${weather-stack.api}")
    private String weatherStackApi;
	
	@Value("${open-weather-map.api}")
    private String openWeatherMapApi;
	
	@Override
	public Map<String, Object> getWeather(String city) {
		String result = null;
		BigDecimal temperature = null;
		BigDecimal windSpeed = null;
		try {
			Map<String, String> paramters = new HashMap<String, String>();
			paramters.put("query", city);
			result = CommonUtil.restServiceGet(restTemplate, weatherStackApi, paramters, String.class);
			if (!StringUtil.isNullOrEmpty(result)) {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject != null) {
					temperature = jsonObject.getJSONObject("current").getBigDecimal("temperature");
					windSpeed = jsonObject.getJSONObject("current").getBigDecimal("wind_speed");
				}
			}

		} catch (RestClientException e) {
		}
		
		try {
			if (StringUtil.isNullOrEmpty(result)) {
				Map<String, String> paramters = new HashMap<String, String>();
				paramters.put("q", city);
				result = CommonUtil.restServiceGet(restTemplate, openWeatherMapApi, paramters, String.class);
				if (!StringUtil.isNullOrEmpty(result)) {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject != null) {
						temperature = jsonObject.getJSONObject("main").getBigDecimal("temp");
						windSpeed = jsonObject.getJSONObject("wind").getBigDecimal("speed");
					}
				}
				
			}
		} catch (RestClientException e) {
		}
		
		return ImmutableMap.of("temperature_degrees", temperature.setScale(2, RoundingMode.HALF_UP).doubleValue()
				,"wind_speed", windSpeed.setScale(2, RoundingMode.HALF_UP).doubleValue());
	}
}
