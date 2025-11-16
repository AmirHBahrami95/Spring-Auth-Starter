package XX_DOMAIN_NAME.XX_APP_NAME.app.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenAuthConfig {
	
	@Bean
	public TokenAuthenticationManager tokenAuthenticationManager(){
		return new TokenAuthenticationManager();
	};
	
	@Bean
	public TokenAuthenticationProvider tokenAuthenticationProvider() {
		return new TokenAuthenticationProvider();
	}
	
}
