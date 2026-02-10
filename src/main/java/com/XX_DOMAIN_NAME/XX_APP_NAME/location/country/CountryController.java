package com.XX_DOMAIN_NAME.XX_APP_NAME.location.country;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.XX_DOMAIN_NAME.XX_APP_NAME.location.country.dto.CountryResponseDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.BusinessLogicException;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {
	
	@Autowired
	private CountryService cServ;
	
	@GetMapping(path="/{iso2}",produces = "application/json")
	public ResponseEntity<CountryResponseDto> getByIso2(@PathVariable(required = true,name="iso2") String iso2){
		Optional<Country> found=cServ.findByIso2(iso2);
		if(found.isEmpty()) throw new BusinessLogicException("err.location.country.not_found", 404);
		return ResponseEntity.ok(found.get().toDto());
	}
	
	@GetMapping(path="/by-name/{name}",produces = "application/json")
	public ResponseEntity<CountryResponseDto> getByName(@PathVariable(required = true,name = "name")  String name){
		Optional<Country> found=cServ.findByIso2(name);
		if(found.isEmpty()) throw new BusinessLogicException("err.location.country.not_found", 404);
		return ResponseEntity.ok(found.get().toDto());
	}
	
	@GetMapping(path="/by-code/{dialCode}",produces = "application/json")
	public ResponseEntity<CountryResponseDto> getByDialCode(@PathVariable(required = true,name = "dialCode")String dialCode){
		Optional<Country> found=cServ.findByDialCode(dialCode);
		if(found.isEmpty()) throw new BusinessLogicException("err.location.country.not_found", 404);
		return ResponseEntity.ok(found.get().toDto());
	}

}
