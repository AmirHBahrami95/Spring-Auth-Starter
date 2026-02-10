package com.XX_DOMAIN_NAME.XX_APP_NAME.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import com.XX_DOMAIN_NAME.XX_APP_NAME.security.jwa.TokenAuthFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private TokenAuthFilter tokAuthFilth;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		// TODO NOKAT wiki : explain that when u change a method's PrePost-Authorize 
		// u shouldn't forget to authenticate the endpoint here
		return http
			.authorizeHttpRequests(c -> c
	    		
				.requestMatchers("/api/v1/user/whoami").authenticated()
	    		.requestMatchers("/api/v1/user/change-password").authenticated()
	    		.requestMatchers("/api/v1/user/admin/change-password").authenticated()
	    		.requestMatchers("/api/v1/user/admin/promote-user").authenticated()
	    		.requestMatchers("/api/v1/user/admin/demote-user").authenticated()
	    		.requestMatchers("/api/v1/user/personal-info/update").authenticated()
	    		.requestMatchers("/api/v1/user/Tewb0.graihys").authenticated()
	    		
	    		.requestMatchers("/api/v1/auth/logout").authenticated()
	    		.requestMatchers("/api/v1/auth/test/revoke-access").authenticated()
	    		.requestMatchers("/api/v1/auth/refresh-access").anonymous()
	    		.requestMatchers("/api/v1/auth/login").anonymous()
	    		.requestMatchers("/api/v1/auth/register").permitAll()
	    		 
	    		.requestMatchers("/api/v1/country/**").authenticated()
		        .anyRequest().permitAll()
			)
			.exceptionHandling(jack->
				jack.authenticationEntryPoint((req,res,ex)->{
					res.sendError(401, "err.auth.anauthorized");
				})
				.accessDeniedHandler((req,res,ex)->{
					res.sendError(403, "err.auth.access_denied");
				})
			)
			
		    // disable crap from fucking thymeleaf and html processing wank material. JEZUIS!
		    .httpBasic(c->c.disable())
		    .formLogin(c -> c.disable())
		    .logout(c->c.disable())
		    .csrf(c->c.disable())
		    .cors(c->c.disable())
		    // .addFilterBefore(new FingerprintingFilter(), ChannelProcessingFilter.class)
		    .addFilterAfter(tokAuthFilth, AnonymousAuthenticationFilter.class)
		    // .anonymous(c->c.disable())
		    .build();
	}

}
