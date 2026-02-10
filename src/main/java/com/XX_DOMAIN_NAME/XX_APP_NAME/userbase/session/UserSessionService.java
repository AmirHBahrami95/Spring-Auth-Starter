package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.XX_DOMAIN_NAME.XX_APP_NAME.cache.CachingService;
import com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo.PersonalInfoService;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto.LoginRequestDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.User;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.UserService;
import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.BusinessLogicException;
import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.JwtManager;

import jakarta.validation.Valid;

@Service
public class UserSessionService {
	
	private final Logger log = LoggerFactory.getLogger(UserSessionService.class);
	
	@Autowired
	private UserSessionRepo sessionRepo;
	
	@Autowired
	private UserService uServ;
	
	@Autowired
	private JwtManager jwtManager;
	
	@Autowired
	private PersonalInfoService piServ;

	@Autowired
	private CachingService cacheServ;
	
	
	/**
	 * Deletes an entire session including the refresh token associated with the accessToken. 
	 * The accessToken is also added to the blacklisting cache to ensure expired access tokens
	 * won't be authorized.
	 * @param String accessToken
	 * @return boolean whether or not any session has been deleted 
	 * */
	@PreAuthorize("#accessToken==authentication.credentials or hasRole('ADMIN')")
	@Transactional
	public boolean logout(String accessToken) {
		Optional<UserSession> sess=sessionRepo.findByAccessToken(accessToken);
		revokeAccessToken(accessToken); // NOTE that bc of token-based auth filter,
		if(sess.isEmpty()) {
			log.error("UserSessionService.logout() - impossible logic happened for token {}",accessToken);
			throw new BusinessLogicException("err.auth.logout.not_authenticated", 401); 
		}
		return sessionRepo.deleteByRefToken(sess.get().getRefreshToken())>0;
	}

	/**
	 * Input User object should already be existent, so check before passing down here.
	 * @param LoginRequestDto dto
	 * @return Optional<UserSession>
	 * */
	@Transactional
	public Optional<UserSession> login(LoginRequestDto dto){
		
		// back checks
		Optional<User> found=uServ.findByUniqueFields(dto.getUname(),dto.getEmail());
		if(found.isEmpty()) throw new BusinessLogicException("err.auth.login.user_not_found",404);
		
		// checking passwords (nested exception throwing)
		uServ.checkPasswordsMatch(dto.getPassw(),found.get().getPassword());
	
		// making & returning a user token
		UserSession saved=createUserSession(found.get());
		return Optional.of(saved);
	}
	
	@Transactional
	public Optional<UserSession> register(@Valid User u){
		if(u.getId()!=null && !u.getId().toString().isBlank())
			throw new BusinessLogicException("err.general.bad_request", 400);
		uServ.checkExists(u);
		if(u.getPassword()==null || u.getPassword().isBlank()) 
			throw new BusinessLogicException("err.user.invalid_password", 400);
		uServ.addNewUser(u);
		return Optional.of(createUserSession(u));
	}
	
	@Transactional
	public Optional<UserSession> refreshSession(UUID refreshToken) {
		Optional<UserSession> found=sessionRepo.findByRefreshToken(refreshToken);
		if(found.isEmpty())
			throw new BusinessLogicException("err.auth.ref_token_not_found",404);
		
		if(!cacheServ.isBlackListed(found.get().getAccessToken())
		&& jwtManager.isValid(found.get().getAccessToken()) // u should revoke a token first before trying to refresh it! 
		&& jwtManager.isExpired(found.get().getAccessToken()))
			throw new BusinessLogicException("err.auth.session_still_active",403);
		
		// blacklisting the previous session
		cacheServ.putExpiredSession(found.get().getAccessToken());
		
		// update the accessToken in DB
		found.get().setAccessToken(jwtManager.create(found.get().getUsr()));
		sessionRepo.save(found.get()); // WHAT HAPPENS TO THIS FUCKER?????????
		
		return found;
	}
	
	/** 
	 * The sole purpose of this method is to put "valid" access tokens inside
	 * the access token cache. To do this, it first makes a database call to
	 * check if the session is valid.
	 * 
	 * XXX This method is NOT PrePost/Authorize-able due to it being called in
	 * anonymous calls. (also beware of the effects)
	 *  
	 * @param jwt
	 * @throws BusinessLogicException inside nested method calls */
	@Transactional
	public void revokeAccessToken(String accessToken){
		Optional<UserSession> found=sessionRepo.findByAccessToken(accessToken);
		if(found.isEmpty())
			throw new BusinessLogicException("err.auth.invalid_token",401);
		cacheServ.putExpiredSession(accessToken);
	}

	@Transactional
	private UserSession createUserSession(@Valid User u) {
		UserSession ut=new UserSession();
		ut.setUsr(u);
		ut.setAccessToken(jwtManager.create(u));
		UserSession saved=sessionRepo.save(ut);
		return saved;
	}
	
	// XXX NOKAT wiki - in database a role must be saved as 'ROLE_X'
	// but here you need to catch it with hasRole('X') or hasAuthority('ROLE_X')
	@PreAuthorize("hasRole('ADMIN') or hasRole('CONTRIBUTOR') ")
	@Transactional
	public int deleteTestData() {
		sessionRepo.deleteTestTokens();
		int res=uServ.deleteTestData(null);
		piServ.delTestData();
		return res;
	}
	
}
