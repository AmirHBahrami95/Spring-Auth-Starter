package com.XX_DOMAIN_NAME.XX_APP_NAME.security.jwa;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.XX_DOMAIN_NAME.XX_APP_NAME.cache.CachingService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAuthFilter extends BasicAuthenticationFilter{

	@Autowired 
	private CachingService cServ; // to keep track of expired tokens (each token remains in cache for 15 minutes)
	
	public TokenAuthFilter(TokenAuthenticationManager manager) {
		super(manager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
	throws IOException, ServletException {
		Optional<String> accessToken=getBearerToken(request);
		if( accessToken.isPresent() && !cServ.isBlackListed(accessToken.get()) )
			initSecurityContext(accessToken.get());
		chain.doFilter(request, response);
	}

	private void initSecurityContext(String accessToken){
		SecurityContextHolder.getContext().setAuthentication(
			super.getAuthenticationManager().authenticate(
				new TokenAuthentication(accessToken,null )));
	}
	
	private Optional<String> getBearerToken(HttpServletRequest req) {
		String token=req.getHeader("Authorization");
		if(token==null) return Optional.empty(); 
		token=StringUtils.removeStart(token, "Bearer").trim();
		return Optional.of(token);
	}

}
