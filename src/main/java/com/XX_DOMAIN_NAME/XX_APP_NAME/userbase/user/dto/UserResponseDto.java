package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto;

import java.util.UUID;

import com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo.dto.PersonalInfoResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponseDto{
	
	private UUID id;
	private String uname;
	private PersonalInfoResponseDto personalInfo;
	
	@Builder.Default
	private Boolean isActive=true;
	
}
