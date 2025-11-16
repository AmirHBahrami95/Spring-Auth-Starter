package XX_DOMAIN_NAME.XX_APP_NAME.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import XX_DOMAIN_NAME.XX_APP_NAME.app.filters.TokenAuthFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private TokenAuthFilter tokAuthFilth;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		// TODO make /api/v1/location path also authenticated so not every schmuck with
		// cheap internet tarif and "hacked" wifi access in low life's midnight cafe can 
		// brute force us + set some rate limit ffs, will ya?
    http
	    .authorizeHttpRequests(c -> c
	    		.requestMatchers("/api/v1/user/logout").authenticated()
	    		.requestMatchers("/api/v1/user/whoami").authenticated()
	    		.requestMatchers("/api/v1/user/Tewb0.graihys").permitAll()
	    		.requestMatchers("/api/v1/cat/all").permitAll()
	        // .requestMatchers("/api/user/**").permitAll()
	        // .requestMatchers("/h2-ui").permitAll()
	        // .requestMatchers("/actuator/**").permitAll()
	    		.requestMatchers("/api/v1/carry-offer/by-issuer/**").permitAll()
	    		.requestMatchers("/api/v1/carry-offer/by-route").permitAll()
	    		.requestMatchers("/api/v1/carry-offer/**").authenticated()
	    		.requestMatchers("/api/v1/packet/srx").permitAll()
	    		.requestMatchers("/api/v1/packet/**").authenticated()
	    		.requestMatchers("/api/v1/packet/my-packets/**").authenticated()
	    		.requestMatchers("/api/v1/packet/my-packets-for/**").authenticated()
	        .anyRequest().permitAll()
	    )
	    // disable crap from fucking thymeleaf and html processing wank material. JEZUIS!
	    .httpBasic(c->c.disable())
	    .formLogin(c -> c.disable())
	    .logout(c->c.disable())
	    .csrf(c->c.disable())
	    .cors(c->c.disable())	    
	    // .addFilterBefore(new FingerprintingFilter(), ChannelProcessingFilter.class)
	    .addFilterBefore(tokAuthFilth, AnonymousAuthenticationFilter.class)
	    .anonymous(c->c.disable())
	    /**/
    ;
    return http.build();
	}

}
