package com.XX_DOMAIN_NAME.XX_APP_NAME.location.country;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<Country, String>{
	
	// XXX may need testing
	@Query("SELECT c FROM Country c WHERE c.name LIKE %?1% ")
	Optional<Country> findByName(String name);
	Optional<Country> findByDialCode(String dialCode);
	Optional<Country> findByIso2(String iso2);
	Country save(Country c);
}
