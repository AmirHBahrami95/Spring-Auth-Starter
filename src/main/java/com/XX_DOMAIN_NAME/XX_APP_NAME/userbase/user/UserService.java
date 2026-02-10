package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.BusinessLogicException;

@Service
public class UserService implements UserDetailsManager {
	
	private final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepo  userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public void demoteUser(UUID uid,String role) {
		Optional<User> found=findById(uid);
		if(found.isEmpty()) 
			throw new BusinessLogicException("err.user.not_found",404);
		if(!found.get().hasRole(role)) 
			throw new BusinessLogicException("err.user.role_not_assigned", 400);
		found.get().removeRole(role);
		userRepo.save(found.get()); // update operation
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public void promoteUser(UUID uid,String newRole) {
		Optional<User> found=findById(uid);
		if(found.isEmpty()) 
			throw new BusinessLogicException("err.user.not_found",404);
		if(found.get().hasRole(newRole)) 
			throw new BusinessLogicException("err.user.role_already_assigned", 400);
		found.get().getRoles().add(UserRole.builder().title(newRole).build());
		userRepo.save(found.get()); // update operation
	}
	
	/**
	 * As of rn it's only used by PersonalInfoService to make sure user is only editing his
	 * own bullshit (and he exists too). Why not just return a simple object outside Optional?
	 * habit! it's just my habit and it's fucking shit.
	 * @param UUID of the personalInfo of any User
	 * @return Optional<User> if found
	 * */
	@PostAuthorize("returnObject.get().id==authentication.principal.id or hasRole('ADMIN')")
	public Optional<User> getByPersonalInfoId(UUID id){
		Optional<User> u=userRepo.findByPersonalInfoId(id);
		if(u.isEmpty()) // this is to prevent PostAuthorize throwing weird exceptions 
			throw new BusinessLogicException("err.personal_info.not_found", 404);
		return u;
	}
	
	public Optional<User> findById(UUID id) {
		return userRepo.findById(id);
	}
	
	/**
	 * Use this method instead of SS's default one since this one checks for email, too.
	 * 
	 * TODO NOKAT WIKI:
	 * this mess of a exception throwing is still better than fucking MAGIC jpa exeption
	 * handling bullshit bc you can at least debug it easily AND apply business logic to
	 * it (no pun intended). 
	 * */
	public void checkExists(User u) {
		Optional<User> found=userRepo.findByUniqueFields(u.getUname(), u.getPersonalInfo().getEmail());
		if(found.isPresent()) {
			if(u.getUname().equals(found.get().getUname()))
				throw new BusinessLogicException("err.user.uname_taken", 400);
			else if(u.getPersonalInfo().getEmail().equals(found.get().getPersonalInfo().getEmail()))
				throw new BusinessLogicException("err.user.email_taken", 400);
			else if(u.getPersonalInfo().getPhoneNo().equals(found.get().getPersonalInfo().getPhoneNo()))
				throw new BusinessLogicException("err.user.phone_no_taken", 400);
		}
	}
	
	public Optional<User> findByUniqueFields(String uname,String email){
		return userRepo.findByUniqueFields(uname, email);
	}
	
	public void checkPasswordsMatch(String passw1, String passw2) {
		if(!passwordEncoder.matches(passw1,passw2))
			throw new BusinessLogicException("err.user.login.passwords_mismatch",400);
	}
	
	@Override
	public void createUser(UserDetails user) {
		updateUser(user);
	}

	@Override 
	public boolean userExists(String username) {
		return userRepo.findByUname(username).isPresent();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Override 
	public void deleteUser(String uuid) {
		userRepo.deleteById(UUID.fromString(uuid));
	}
	
	@PreAuthorize("authentication.principal.id==#user.id or hasRole('ADMIN')")
	@Override
	public void updateUser(UserDetails user) {
		updateExistingUser((User) user);
	}
	
	/** Not used as of now, reserved for Authorization Server (maaaayybe, who gaf). */
	@PreAuthorize("auhtentication.principal.uname=#username")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findById(UUID.fromString(username)).get();
	}

	/***
	 * You can optionally add some PreAuthorize and call it a day, but I just don't fucking
	 * like this method's bloody signature.
	 * */
	@Override
	public void changePassword(String oldPassword, String newPassword) {}
	
	
	/** To be used to save a user's password. 
	 * you know - the way it's fucking meant to be (without an authz server)
	 * */
	@PreAuthorize("#id==authentication.principal.id")
	@Transactional
	public void changePassword(UUID id,String oldPassword,String newPassword) {
		Optional<User> u=userRepo.findById(id);
		if(u.isEmpty()) 
			throw new BusinessLogicException("err.user.not_found", 404);
		if(!passwordEncoder.matches(oldPassword,u.get().getPassword()))
			throw new BusinessLogicException("err.user.password.mismatch", 400);
		userRepo.updateUserPassword(id, passwordEncoder.encode(newPassword));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public void changePassword(UUID id, String newPassword) {
		Optional<User> u=userRepo.findById(id);
		if(u.isEmpty()) 
			throw new BusinessLogicException("err.user.not_found", 404);
		userRepo.updateUserPassword(id, passwordEncoder.encode(newPassword));
	}
	
	public void addNewUser(User u) {
		if(u.getId()!=null) {
			log.warn("probable user injection attempt : {}",u);
			throw new BusinessLogicException("err.user.already_inserted", 400);
		}
		String passString=passwordEncoder.encode(u.getPassword());
		u.setPassw(passString);
		userRepo.save(u);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public int deleteTestData(Authentication a) {
		if(a!=null) {
			User u=(User)a.getPrincipal();
			log.error("UserService.deleteTestUsers() - {}",u);
		}
		return userRepo.deleteTestUsers();
	}
	
	@PreAuthorize("authentication.principal.id==u.id or hasRole('ADMIN')")
	@Transactional
	private void updateExistingUser(User u) {
		if(u.getId()==null) {
			log.warn("probable user injection attempts: {}",u);
			throw new BusinessLogicException("err.user.no_id", 400);
		}
		Optional<User> found=findById(u.getId());
		if(u.getId()==null)
			throw new BusinessLogicException("err.user.not_found", 404);
		if(found.get().update(u))
			userRepo.save(found.get());
	}

}
