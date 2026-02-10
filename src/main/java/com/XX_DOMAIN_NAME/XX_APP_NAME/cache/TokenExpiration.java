package com.XX_DOMAIN_NAME.XX_APP_NAME.cache;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
	This class is not yet used in this program, but will later be used to 
	set redis caching time more directly.
*/
@Data @NoArgsConstructor @AllArgsConstructor @Builder 
public class TokenExpiration{

	private String token;
	private Instant expiresAt;

}
