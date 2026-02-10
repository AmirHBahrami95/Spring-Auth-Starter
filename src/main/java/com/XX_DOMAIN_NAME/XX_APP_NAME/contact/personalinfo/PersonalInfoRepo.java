package com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInfoRepo extends JpaRepository<PersonalInfo, UUID> {
	
	Optional<PersonalInfo> findById(UUID id);
	PersonalInfo save(PersonalInfo pi);
	
	@Modifying
	@Query("DELETE FROM PersonalInfo pi WHERE pi.lname LIKE '%test%'")
	int deleteTestPis();
}
