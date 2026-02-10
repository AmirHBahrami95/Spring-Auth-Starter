package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserSessionConfig {
	
	@Bean
	public UserSessionService userSessionService() {
		return new UserSessionService();
	}
}
