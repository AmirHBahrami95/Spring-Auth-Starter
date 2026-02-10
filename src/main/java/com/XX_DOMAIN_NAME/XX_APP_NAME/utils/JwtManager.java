package com.XX_DOMAIN_NAME.XX_APP_NAME.utils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.User;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.UserRole;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.UserService;

@Component
@Configuration
public class JwtManager {
	
	private final Logger log = LoggerFactory.getLogger(UserService.class);
	
	public static final long EXPIRATION_TIME=900_000; // 15 min
	public static final String JWT_ISSUER="com.XX_DOMAIN_NAME.XX_APP_NAME";
	
	@Autowired private RSAPrivateKey jwtSigningKey;
	@Autowired private RSAPublicKey jwtValidationKey;
	
	/**
	 * Creates an semi-opaque jwt token, where the only included user information is
	 * his ID and his roles. No PersonalInfo Field is includeed in the jwt. 
	 * */
	public String create(User u) {
		final long rn=System.currentTimeMillis();
		
		return JWT.create()
		.withIssuer(JWT_ISSUER)
		.withSubject(u.getId().toString())
		.withClaim("uname", u.getUname())
		.withClaim("roles",
			u.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList())
		).withIssuedAt(new Date(rn))
		.withExpiresAt(new Date(rn+EXPIRATION_TIME))
		.sign(Algorithm.RSA256(jwtValidationKey, jwtSigningKey));
	}
	
	/**
	 * Due to the nature of a jwt string, you cannot retrieve the refresh token
	 * from it, cuz otherwise what's the fucking point of separating the tokens.
	 * @return UserSession without refresh_token
	 * */
	public User decodeJwt(String jwt) {
		Date rn=new Date(System.currentTimeMillis());
		DecodedJWT dec=getDecoder(jwt);
		if(dec.getExpiresAt().before(rn))
			throw new BusinessLogicException("err.auth.session_expired", 401);
		return toUserSession(dec,jwt);
	}

	private DecodedJWT getDecoder(String jwt){
		JWTVerifier verifier=JWT
			.require(Algorithm.RSA256(jwtValidationKey, jwtSigningKey))
			.withIssuer(JWT_ISSUER).build();
		try {
		    return verifier.verify(jwt);
		} catch (JWTVerificationException e) {
			log.error("invalid jwt:'{}'",jwt);
		    return null;
		}
	}
	
	public boolean isValid(String jwt) {
		return getDecoder(jwt)!=null;
	}
	
	public Instant getExpiresAt(String jwt){
		return getDecoder(jwt).getExpiresAtAsInstant();
	}
	
	public boolean isExpired(String jwt) {
		final long rn=System.currentTimeMillis();
		Instant nowI=new Date(rn).toInstant();
		return getExpiresAt(jwt).isAfter(nowI);
	}


	private User toUserSession(DecodedJWT decJew,String jwt) {
		return User.builder()
			.id(UUID.fromString(decJew.getSubject()))
			.uname(decJew.getClaim("uname").toString())
			.roles(decJew.getClaim("roles").asList(UserRole.class))
			.build();
		
	}

}
