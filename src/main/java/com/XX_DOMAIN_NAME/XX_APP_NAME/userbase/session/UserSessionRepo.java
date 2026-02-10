package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepo extends CrudRepository<UserSession,UUID>{
	
	UserSession save(UserSession ut);
	Optional<UserSession> findByRefreshToken(UUID refToken);
	Optional<UserSession> findByAccessToken(String accessToken);
	
	// TODO add to wiki : sometimes when you refactor/rename an @Entity
	// it's @Query's in Repo's do NOT get changed XXX
	@Modifying
	@Query("DELETE FROM UserSession ut WHERE ut.refreshToken=?1")
	int deleteByRefToken(UUID id);
	
	@Modifying
	@Query("DELETE FROM UserSession ut WHERE ut.usr.uname LIKE '%test%'")
	int deleteTestTokens();
	
	/*
		@Modifying
		@Query("UPDATE UserSession us SET us.accessToken='REVOKED' WHERE us.accessToken=?1")
		void revokeAccessToken(String accessToken);
	*/

}
