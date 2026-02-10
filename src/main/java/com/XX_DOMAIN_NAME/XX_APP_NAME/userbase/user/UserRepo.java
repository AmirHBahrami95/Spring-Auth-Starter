package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User,UUID>{
	
	// List<User> findByLname(String lname);
	Optional<User> findById(UUID id);
	Optional<User> findByUname(String uname); // needed for spring
	  
	@Query("SELECT u FROM User u WHERE u.uname=?1 OR u.personalInfo.email=?2")
	Optional<User> findByUniqueFields(String uname, String email);
	  
	@Query("SELECT u FROM User u WHERE u.personalInfo.id=?1")
	Optional<User> findByPersonalInfoId(UUID id);
	  
	@SuppressWarnings("unchecked")
	User save(User u); // what the fuck!?
	 
	/**
	 * TODO NOKAT WIKI: if a motherFUCKING field is unique, in an attempt to update
	 * an object you actually trigger a "unique field constraint exception" or some
	 * fuckwank bullshit like that. in order to stop that, provide update query's FUCK.
	 * */
	@Modifying
	@Query("UPDATE User usr set usr.passw=?2 WHERE usr.id=?1")
	int updateUserPassword(UUID id,String password); 
	
	/*
	 * check this guy out
	@Modifying
	@Query("UPDATE User usr set usr.password=?2 WHERE usr.id=?1")
	int updateUserRole(UUID id,UserRole newRole);
	*/
	
	List<User> findAll();
	void deleteById(UUID id);
	  
	@Modifying
	@Query("DELETE FROM User u WHERE u.uname LIKE '%this-is-a-test%'")
	int deleteTestUsers();
}
