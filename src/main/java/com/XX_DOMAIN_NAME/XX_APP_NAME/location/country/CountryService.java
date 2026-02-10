package com.XX_DOMAIN_NAME.XX_APP_NAME.location.country;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
	
	@Autowired
	private CountryRepo countryRepo;
	
	public Optional<Country> findByIso2(String iso2){
		return countryRepo.findByIso2(iso2.toLowerCase());
	}
	
	public Optional<Country> findByName(String name){
		return countryRepo.findByName(name);
	}
	
	public Optional<Country> findByDialCode(String dCode){
		sanitizeDialCode(dCode);
		return countryRepo.findByDialCode(dCode);
	}
	
	private void sanitizeDialCode(String dialCode) {
		if(dialCode.startsWith("00"))
			dialCode.replaceFirst("00", "");
		
		else if(dialCode.startsWith("0"))
			dialCode.replaceFirst("0", "");
		
		else if(dialCode.startsWith("+"))
			dialCode.replaceFirst("+", "");
	}
	
}
