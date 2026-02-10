package com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.XX_DOMAIN_NAME.XX_APP_NAME.location.address.dto.AddressRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PersonalInfoRequestDto {
	
	private UUID id;
	private String fname;
	private String lname;
	private String email;
	private String phoneNo;
	private AddressRequestDto address;
	private LocalDate birthDate;
	private String picture;
	
}
