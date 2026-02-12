package com.XX_DOMAIN_NAME.XX_APP_NAME.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserTokenRepo extends CrudRepository<UserToken,String>{
	
	// TODO add logout
	
	// @Transactional
	UserToken save(UserToken ut);
	Optional<UserToken> findByToken(String token);
	
	@Modifying
	@Query("DELETE FROM UserToken ut WHERE ut.token=?1")
	int deleteToken(String token);
	
	@Modifying
	@Query("DELETE FROM UserToken ut WHERE ut.usr.uname LIKE '%test%'")
	int deleteTestTokens();
}
