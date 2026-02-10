package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Only sent via admin or other managetorial folks who want to increase the 
 * span of a user's permissions in the spectrum of the entire program. Note 
 * that this is NOT ACL or whatever, we're talking about USER to ADMIN here!
 * 
 * The newRole must not be the same as the previous role, and the value can be:
 * ADMIN
 * CONTRIBUTOR
 * 
 * feel free to add other roles but also their logic in the program. and also -
 * FUCK YOU SPRING SECURITY. 
 * */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserRoleChangeRequestDto {
	
	private UUID id;
	private String role;

}
