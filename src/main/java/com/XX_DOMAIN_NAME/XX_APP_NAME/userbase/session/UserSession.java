package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto.UserSessionResponseDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.User;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * A UserSession is an association between a User and his Tokens. It saves both refresh and access token.
 * 
 * With refresh token you can ONLY get an access token. Then using the access token you can be authenticated.
 * 
 * A UserSession would rarely be deleted, instead it's tokens can be rotated, to prevent rotten token hijacking,
 * whether refresh or access token. As of this version though, only access tokens are rotated bc to rotate
 * refresh tokens, you need to store the previous refresh token and upon expiration, the user still gets to SSO
 * but this time instead of a single access token, the response looks like a first time login and also holds an RT. 
 *  
 * so basically:
 * 	/api/v1/user/register -> {refToken}
 * 	/api/v1/user/login -> {refToken} || /api/v1/user/token/refresh -> {accessToken}
 *  
 * then have this in all http headers:
 * 	Authorization: "Bearer $JWT"
 * */
@Entity
@Table(name="usr_token")
@EntityListeners(AuditingEntityListener.class)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserSession {
	
	public static final int TOKEN_LENGTH=16;
	
	// TODO in NOKAT: NotBlank doesn't work on UUID! 
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull(message = "err.user_token.refresh_token_blank")
	private UUID refreshToken;
	
	// TODO in NOKAT: length=-1 for "TEXT" SQL type
	@Column(nullable=false,length = -1)
	@NotBlank(message = "err.user_token.access_token_blank")
	private String accessToken;
	
	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "usr_id",nullable = false, updatable = false)
	private User usr;
	
	@CreatedDate
	private LocalDateTime createdAt; // registeration time, or login after a long time
	
	// can (un)safely be used as accessToken generation time (only beware of changing the refToken
	@UpdateTimestamp
	private LocalDateTime lastUpdated; 
	
	@Version // optimistic locking
	private Long version; // or Integer version;
	
	// NOTE you can add a SessionInfo to save user's current 
	// sessions including device,operating system,etc. 
	// (must not be updatable tho, instead each device one session!)
	
	public boolean isExpired() {
		LocalDateTime rn=LocalDateTime.now();
		return rn.isAfter(lastUpdated);
	}
	
	public UserSessionResponseDto toResponseDto() {
		return toResponseDto(false);
	}
	
	public UserSessionResponseDto toResponseDto(boolean authorized) {
		return UserSessionResponseDto.builder()
				.refreshToken(refreshToken)
				.accessToken(accessToken)
				.user(usr.toResponseDto(authorized))
				.build();
	}

}
