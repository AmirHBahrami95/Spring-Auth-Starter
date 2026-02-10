package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Precedence in determining the type of login (for otp) is phone>email if uname is inserted, the
 * otp is sent to email. customize this behaviour for your business needs.  
 * */
@Data @AllArgsConstructor @Builder
public class LoginRequestDto {
	
	private String uname;
	private String email;
	private String phoneNo;
	private String passw;
	
}
