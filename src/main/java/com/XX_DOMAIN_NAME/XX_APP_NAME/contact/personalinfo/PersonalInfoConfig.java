package com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonalInfoConfig {
	
	@Bean
	public PersonalInfoService personalInfoService() {
		return new PersonalInfoService();
	}

}
