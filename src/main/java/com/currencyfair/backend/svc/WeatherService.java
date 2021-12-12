package com.currencyfair.backend.svc;

import java.util.Map;

public interface WeatherService {
 
	Map<String, Object> getWeather(String query);
}
