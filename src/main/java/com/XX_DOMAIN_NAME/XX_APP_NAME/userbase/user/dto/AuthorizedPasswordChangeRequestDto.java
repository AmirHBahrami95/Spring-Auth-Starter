package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto to be sent by an admin user in order to change some other user's password.
 * As you can imagine oldPassword is not needed! 
 * */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthorizedPasswordChangeRequestDto {
	private UUID id;
	private String newPassword;
}
