package com.XX_DOMAIN_NAME.XX_APP_NAME.security.jwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.User;
import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.BusinessLogicException;
import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.JwtManager;

/**
 * Simple token based authentication provider. Supports TokenAuthentication Objects
 * and upon authentication generates a new TokenAuthentication. @see TokenAuthentication 
 * */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider{
	
	// private final Logger log = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
	
	@Autowired
	private JwtManager jwtManager;
	
	/**
	 * This method decodes a jwt and returns a (Token)Authentication to be put in security context.
	 * it has bit of a weird (retarded) way of working: this method both expects and returns an 
	 * "Authentication" object. 
	 * 
	 * @param Authentication with the jtw/token embedded in it's "credentials" field.  
	 * @return Authentication the actual Authentication object you can put in SecurityContext. 
	 * */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		User u=jwtManager.decodeJwt((String)authentication.getCredentials());
		if(u==null) throw new BusinessLogicException("err.general.unauthorized", 401);
		TokenAuthentication ta=
				new TokenAuthentication((String)authentication.getCredentials(),u );
		ta.setAuthenticated(true);
		return ta;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(TokenAuthentication.class);
	}
	
};