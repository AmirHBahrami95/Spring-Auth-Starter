package com.XX_DOMAIN_NAME.XX_APP_NAME.location.address.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder 
public class AddressRequestDto {

	private String countryIso2;
	private String state;
	private String city;
	private String local;
}
