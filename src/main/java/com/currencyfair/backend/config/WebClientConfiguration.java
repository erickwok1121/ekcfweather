package com.currencyfair.backend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfiguration {
	
	@Value("${spring.profiles.active}")
    private final String activeProfile;
    
    public WebClientConfiguration(@Value("${spring.profiles.active}") final String activeProfile) {
        this.activeProfile = activeProfile;
    }
	
	@Bean
	@Qualifier("asIntSystem")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
