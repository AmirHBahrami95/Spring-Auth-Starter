package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This class'es sole purpose is to make access-token refresh requests POST-able
 * and this secure.
 * */
@Data @AllArgsConstructor @Builder
public class SessionRefreshRequestDto {
	private UUID refreshToken;
}
