package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto;

import java.util.UUID;

import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * At this stage, both refresh and access tokens are sent to user, since we only
 * want to achieve SSO and no oatuh2 bullshit is at play here.
 * */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserSessionResponseDto {
	
	private UUID refreshToken;
	private String accessToken;
	
	// TODO add to NOKAT about "nested dto's policy"
	private UserResponseDto user; // nested dto's => safe informations
}
