package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// why is this guy here?! doesn't matter, just leave it here (it works) 
@Configuration
public class UserConfig {
	
	@Bean
	public UserService userService() {
		return new UserService();
	}
	
	@Bean 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
