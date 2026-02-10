package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.UserSessionService;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto.AuthorizedPasswordChangeRequestDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto.PasswordChangeRequestDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto.UserResponseDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto.UserRoleChangeRequestDto;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {
	
	@Autowired
	private UserSessionService usessServ;
	
	@Autowired
	private UserService uServ;
	
	// ---------------------------------------------------- NOT TESTED
	
	// public ResponseEntity<UserResponseDto> postUpdateUname()
	
	// you can add searchByUname(String %uname%); to get suggestions 
	// when users are chosing candidates... (if you give a fuck, that is)
	
	
	// ---------------------------------------------------- TESTED
	
	@PostMapping(path="/admin/change-password",consumes = "application/json",produces = "application/json")
	public ResponseEntity<Map<String,String>> postPasswordChangeAuthorized(
	@RequestBody(required=true) AuthorizedPasswordChangeRequestDto dto){
		uServ.changePassword(dto.getId(),dto.getNewPassword());
		return ResponseEntity.ok(Map.of("status.status","status.changed"));
	}
	
	@PostMapping(path="/change-password",consumes = "application/json",produces = "application/json")
	public ResponseEntity<Map<String,String>> postPasswordChange(
	@RequestBody(required=true) PasswordChangeRequestDto dto,
	Authentication a){
		uServ.changePassword(((User)a.getPrincipal()).getId(),dto.getOldPassword(),dto.getNewPassword());
		return ResponseEntity.ok(Map.of("status.status","status.changed"));
	}
	
	
	@PostMapping(path="/admin/demote-user",produces="application/json")
	public ResponseEntity<Map<String,String>> demoteUser(
	@RequestBody(required=true) UserRoleChangeRequestDto upd){
		uServ.demoteUser(upd.getId(),upd.getRole());
		return ResponseEntity.ok(Map.of("status.status","status.demoted"));
	}
	
	@PostMapping(path="/admin/promote-user",produces="application/json")
	public ResponseEntity<Map<String,String>> promoteUser(
	@RequestBody(required=true) UserRoleChangeRequestDto upd){
		uServ.promoteUser(upd.getId(),upd.getRole());
		return ResponseEntity.ok(Map.of("status.status","status.promoted"));
	}
	
	@GetMapping(path="/{id}",produces="application/json")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") UUID id){
		Optional<User> u=uServ.findById(id);
		if(u.isEmpty()) return ResponseEntity.notFound().build();
		u.get().setPassw(null);
		return ResponseEntity.ofNullable(u.get().toResponseDto(false)); // 
	}
	
	@GetMapping(path="/whoami",produces="application/json")
	public ResponseEntity<UserResponseDto> whoami(Authentication a){
		User u=(User)a.getPrincipal();
		Optional<User> extended=uServ.findById(u.getId()); // lazy loading activated automatically
		return ResponseEntity.ofNullable(extended.get().toResponseDto(true));
	}
	
	@Transactional
	@GetMapping(path="/Tewb0.graihys",produces="application/json")
	public ResponseEntity<Map<String,Integer>> deleteTestUsers(){
		Map<String,Integer> res=new HashMap<>();
		res.put("affected",usessServ.deleteTestData());
		return ResponseEntity.ofNullable(res);
	}
	
	
}
