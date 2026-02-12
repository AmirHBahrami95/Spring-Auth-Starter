package com.XX_DOMAIN_NAME.XX_APP_NAME.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.XX_DOMAIN_NAME.XX_APP_NAME.user.UserService;
import com.XX_DOMAIN_NAME.XX_APP_NAME.user.UserToken;

/**
 * Simple token based authentication provider. Supports TokenAuthentication Objects
 * and upon authentication generates a new TokenAuthentication. @see TokenAuthentication 
 * */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Optional<UserToken> du=userService.findUserByToken(authentication.getCredentials().toString());
		TokenAuthentication ta=null;
		if(du.isPresent()) {
			ta=new TokenAuthentication(du.get().getToken(),du.get().getUsr());
			ta.setAuthenticated(true);
		}
		return ta;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(TokenAuthentication.class);
	}
	
};