package com.XX_DOMAIN_NAME.XX_APP_NAME.location.country.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder 
public class CountryResponseDto {
	
	private String iso2;
	private String name;
	private String dialCode;
	private String iconUrl;
}
