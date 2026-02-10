package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto for users to change their own password.
 * */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordChangeRequestDto {
	private String oldPassword;
	private String newPassword;
}
