package com.XX_DOMAIN_NAME.XX_APP_NAME.location.country;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountryConfig {
	
	@Bean
	public CountryService countryService() {
		return new CountryService();
	}

}
