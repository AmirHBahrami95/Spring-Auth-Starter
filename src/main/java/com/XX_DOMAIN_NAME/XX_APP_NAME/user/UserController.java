package com.XX_DOMAIN_NAME.XX_APP_NAME.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {
	
	@Autowired
	private UserService uServ;
	
	@PostMapping(path = "/register",consumes="application/json",produces="application/json")
	public ResponseEntity<Map<String,String>> postRegister(@RequestBody UserRequestDto u){
		Map<String,String> answer=new HashMap<>();
		if(u.getId()!=null && !u.getId().isBlank()) {
			answer.put("error","error.user.register.invalid_field_id"); // Id should be null
			answer.put("code","400");
			return ResponseEntity.badRequest().body(answer);
			// throw new BusinessLogicException("registerDto should have no uid", 400);
		}
		if(uServ.isUnameTaken(u.getUname())) {
			answer.put("error","error.user.register.already_exists"); // TODO later use i18n in front
			answer.put("token",null);
			return ResponseEntity.badRequest().body(answer);
		}
		Optional<String> token=uServ.register(User.of(u));
		if(token.isEmpty()) return ResponseEntity.internalServerError().build();
		answer.put("token",token.get());
		return ResponseEntity.ok(answer);
	}
	
	// TODO add searchByUname(String %uname%); to get suggestions when users are chosing candidates
	
	/** 
	 * 	TODO fix the behaviour of when the user is already logged in (has a bearer header).
	 *  in the Auth Filters not here!, this endpoint is Finalized, so think thrice before
	 *  using it.  
	 *  
	 *  TODO fix the problem of only one session at a time being saved, but the guy logs in 
	 *  again - just tell him he's already logged in (although this needs a lot more thinking
	 *  but for now it's ok)
	 *  
	 * */
	@PostMapping(path = "/login",consumes="application/json",produces="application/json")
	public ResponseEntity<Map<String,String>> postLogin(@RequestBody UserRequestDto ud){
		Map<String,String> answer=new HashMap<>();
		Optional<String> token=Optional.empty();
		
		// TODO make login available by email and phone
		// TODO add a subsystem : email validation for register and login
		try{
			token=uServ.login(ud);
			answer.put("token",token.get());
			return ResponseEntity.ok(answer);
		}catch(PasswordsMismatchException pme) {
			answer.put("error","error.user.login.wrong_password");
			return ResponseEntity.badRequest().body(answer);
		}catch(UsernameNotFoundException unfe) {
			answer.put("error","error.user.login.user_not_found");
			return ResponseEntity.status(404).body(answer);
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping(path="/logout")
	public ResponseEntity<Map<String,String>> logout(Authentication a){
		Map<String,String> response=new HashMap<>();
		response.put("response",String.valueOf(uServ.logout((String)a.getCredentials())));
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping(path="/{id}",produces="application/json")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") String id){
		Optional<User> u=uServ.findById(id);
		if(u.isEmpty()) return ResponseEntity.notFound().build();
		u.get().setPassw(null);
		u.get().setPhoneNo(null);
		return ResponseEntity.ofNullable(u.get().toDto());
	}
	
	@GetMapping(path="/whoami",produces="application/json")
	public ResponseEntity<UserResponseDto> whoami(Authentication a){
		return ResponseEntity.ofNullable(((UserResponseDto)a.getDetails()));
	}
	
	@Transactional
	@GetMapping(path="/Tewb0.graihys",produces="application/json")
	public ResponseEntity<Map<String,Integer>> deleteTestUsers(){
		Map<String,Integer> res=new HashMap<>();
		res.put("affected",uServ.deleteTestUsers());
		return ResponseEntity.ofNullable(res);
	}
	
}
