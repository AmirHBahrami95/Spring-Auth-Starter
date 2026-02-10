package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto;

import com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo.dto.PersonalInfoRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequestDto {
	
	private String uname;
	private String passw;
	private PersonalInfoRequestDto personalInfo;
	
}
