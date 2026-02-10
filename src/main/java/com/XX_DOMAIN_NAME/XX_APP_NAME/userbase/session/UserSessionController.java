package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto.LoginRequestDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto.RegisterRequestDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto.SessionRefreshRequestDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto.UserSessionResponseDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.User;
import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.BusinessLogicException;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/v1/auth")
public class UserSessionController {
	
	private final Logger log = LoggerFactory.getLogger(UserSessionController.class);
	
	@Autowired
	private UserSessionService usessServ;

	// ---------------------------------------------------- TESTED
	
	/**
	 * @return AccessToken
	 * */
	@PostMapping(path="/refresh-access",consumes="application/json",produces = "application/json")
	public ResponseEntity<Map<String,String>> getRefToken(@RequestBody(required = true) SessionRefreshRequestDto dto){
		Optional<UserSession> us=usessServ.refreshSession(dto.getRefreshToken());
		if(us.isEmpty()) throw new BusinessLogicException("err.auth.session_not_found",404);
		Map<String,String> res=new HashMap<>();
		res.put("accessToken", us.get().getAccessToken());
		// resEntity.getHeaders().add("X-Related-Links", "api/v1/user/auth/logout");
		return ResponseEntity.ok(res);
	}
	
	/** This method has no usage in production, as the functinoality is split between logout and refresh-access
	 * but you still need it for the tests since you don't really have 15 minutes to wait in each fucking unit
	 * test session.
	 *  */  
	@GetMapping(path="/test/revoke-access",produces="application/json")
	public ResponseEntity<Map<String,String>> getRevokeAccess(Authentication a){
		String tokenString=(String)a.getCredentials();
		usessServ.revokeAccessToken(tokenString);
		Map<String,String> res=new HashMap<>();
		res.put("accessToken", "REVOKED");
		return ResponseEntity.ok(res);
	}
	
	@Transactional
	@PostMapping(path = "/register",consumes="application/json",produces="application/json")
	public ResponseEntity<UserSessionResponseDto> postRegister(@RequestBody RegisterRequestDto u){
		// log.error("new register incoming:{}",u);
		Optional<UserSession> registered=usessServ.register(User.of(u));
		if(registered.isEmpty()) {
			log.error("[.../user/register] - this error must logically be impossible to display, but here we are!");
			throw new BusinessLogicException("err.auth.internal_server_error", 500);
		}
		return ResponseEntity.ok(registered.get().toResponseDto(true));
	}
	
	@PostMapping(path = "/login",consumes="application/json",produces="application/json")
	public ResponseEntity<UserSessionResponseDto> postLogin(@RequestBody LoginRequestDto ud,Authentication a){
		if(a!=null && a.isAuthenticated())
			throw new BusinessLogicException("err.auth.already_logged_in",400);
		Optional<UserSession> sess=usessServ.login(ud);
		if(sess.isEmpty()) {
			log.error("[.../user/login] - this error must logically be impossible to display, but here we are!");
			throw new BusinessLogicException("err.auth.fuck_me_rigid_on_a_rainy_tuesday_afternoon",500);
		}
		return ResponseEntity.ok(sess.get().toResponseDto());
	}
	
	@GetMapping(path="/logout")
	public ResponseEntity<Map<String,String>> logout(Authentication a){
		return ResponseEntity.ok(Map.of(
			"response",String.valueOf(usessServ.logout((String) a.getCredentials()))		
		));
	}

}
