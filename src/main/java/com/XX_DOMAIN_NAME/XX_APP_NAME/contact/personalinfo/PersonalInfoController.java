package com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo.dto.PersonalInfoRequestDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo.dto.PersonalInfoResponseDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.BusinessLogicException;

/**
 * XXX WARNING : if you separate the personalInfo and contact endpoints and 
 * don't check at service level, an attacker might update a user's personalInfo 
 * using the contant's update method instead of personalInfo one. therefore make 
 * sure to check in future (in contact module) if a contact object belongs to the
 * user calling the contact's update endpoint. XXX 
 * */
@RestController
@RequestMapping("/api/v1/user/personal-info")
public class PersonalInfoController {
	
	@Autowired
	private PersonalInfoService piServ;
	
	@PostMapping(path = "/update",consumes = "application/json",produces = "application/json")
	public ResponseEntity<PersonalInfoResponseDto> postUpdate(@RequestBody(required=true) PersonalInfoRequestDto dto){
		Optional<PersonalInfo> pi=piServ.updateExisting(PersonalInfo.ofDto(dto));
		if(pi.isEmpty()) 
			throw new BusinessLogicException("err.user.personal_info.not_found", 404);
		return ResponseEntity.ok(pi.get().toResponseDto());
	}

}
