package com.XX_DOMAIN_NAME.XX_APP_NAME.cache;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CachingService{
	
	@Autowired
	private RedisTemplate<String,Boolean> expiredAccessTokens;
	
	/**
	 * Check if a jwt string is blacklisted. this method is used inside jwt manager
	 * and thus not PreAuthorize-able.
	 * 
	 * @param jwt
	 * @return boolean if the key is in expiredAccessTokens list.
	 * */
	public boolean isBlackListed(String key){
		Object shit=expiredAccessTokens.opsForValue().get(key);
		// log.error("isBlacklisted? -> {}",shit==null?"false":"true");
		return shit!=null;
	}
	
	/**
	* Rn this method sets a default of 15 minutes for sessions (which is the maximum 
	* amount of time for an access token. if you want to optimize your cache db by
	* decreasing the time,you can feed a 'java.time.Duration' to redistemplate.
	* 
	* if consider this a technical debt, chill out, it's just fucking 15 minutes 
	* of holding a random string inside redis!
	* 
	*  otherwise if u wanna optimize it be my guest, but just know that time-related
	*  wank is the worst type of wank to deal with, in cs (PERIOD).
	* 
	* Btw you cannot logically PreAuthroize this method, since it's used in anonymous
	* calls to (for instance) revoke a full session, which in turn puts the revoked
	* access token inside a cache.
	* 
	* @param jwt
	*/
	public void putExpiredSession(String accessToken){
		expiredAccessTokens.opsForValue().set(accessToken,true,Duration.ofMinutes(15));
	}
	
	/*
	public void putExpiredSession(TokenExpiration exp){
		Instant i=exp.getExpiresAt().minusMillis(System.currentTimeMillis());
		expiredAccessTokens
		.opsForValue()
		.set(
				exp.getToken(),
				exp.getExpiresAt()
			)
		;
	}	

	private Instant getNowAsInstant(){
		final long rn=System.currentTimeMillis();
		return new Date(rn).toInstant();
	}

	*/
}
