package com.currencyfair.backend.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currencyfair.backend.config.CacheConfig;
import com.currencyfair.backend.svc.WeatherService;

@RestController
@RequestMapping("weather")
@PreAuthorize("permitAll()")
public class WeatherController {

	@Autowired
	@Qualifier("customCacheManager")
	private CacheManager cacheManager;
	
    @Autowired
    private WeatherService weatherService;
	
    @PostMapping(path = "/checkWeather", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("permitAll()")
    public Map<String, Object> checkWeather(@RequestParam(name = "city", defaultValue = "Melbourne,AU") String city) {	
    	Map<String, Object> r = null;
    	ValueWrapper w = cacheManager.getCache(CacheConfig.WEATHER).get(city);
    	if (w != null) {
    		r = (Map<String, Object>)w.get();
    	}
    	
    	if (r == null) {
    		r = weatherService.getWeather(city);
    		cacheManager.getCache(CacheConfig.WEATHER).putIfAbsent(city, r);
    	}
    	return r;
    }
}
