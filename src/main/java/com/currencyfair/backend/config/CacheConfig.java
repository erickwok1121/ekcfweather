package com.currencyfair.backend.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.common.cache.CacheBuilder;

@Configuration
public class CacheConfig {

	public static final String WEATHER = "WEATHER";
	
	@Value("${cache-expiry}")
	private int expiry;
	
    @Bean
    @Qualifier("customCacheManager")
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(WEATHER) {
        	@Override
        	protected Cache createConcurrentMapCache(final String name) {
        		return new ConcurrentMapCache(name, CacheBuilder.newBuilder().expireAfterWrite(expiry, TimeUnit.SECONDS).build().asMap(), false);
        	}
        };
        return cacheManager;
    }

    @CacheEvict(allEntries = true, value = {WEATHER})
    @Scheduled(fixedDelay = 10 * 60 * 1000,  initialDelay = 500)
    public void reportCacheEvict() {
        System.out.println("Flush Cache");
    }
}
