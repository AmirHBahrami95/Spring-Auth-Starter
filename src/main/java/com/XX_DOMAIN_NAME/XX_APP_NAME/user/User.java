package com.XX_DOMAIN_NAME.XX_APP_NAME.user;

import java.sql.Blob;
import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usr")
public class User implements UserDetails{
	
	public static User of(UserRequestDto ud) {
		User u=new User();
		u.setUname(ud.getUname());
		u.setPassw(ud.getPassw());
		u.setLname(ud.getLname());
		u.setFname(ud.getFname());
		u.setEmail(ud.getEmail());
		u.setPhoneNo(ud.getPhoneNo());
		return u;
	}
	
	@Id	@GeneratedValue(strategy = GenerationType.UUID)	// apparently it works!
	private String uid; // saved as a uuid string
	
	@Column(nullable=false)
	@NotNull
	private String passw;
	
	@Column(nullable=false,unique = true)
	@Pattern(
		regexp = "^[A-Za-z](\\w|\\d){3,64}@(\\w|\\d){3,32}\\.(\\d|\\w){3,16}$",
		message="user.email.format"
	)
	private String email;
	
	@Pattern(
		regexp="^(\\+|00)\\d{10,14}$",
		message="user.phone.format"
	)
	@Column(nullable=true)
	private String phoneNo;
	
	/// XXX this field is optional, but not removed : if later on,
	// the application gets 100k+ users, it'll be necessary to let
	// users add a uname for themselves to be better recognizable and
	// fraud detection becomes easier
	@Column(unique=true,nullable=true)
	@Pattern(regexp = "^[A-Za-z0-9\\-_\\$#@]{3,32}$",message="user.uname.format")
	private String uname; 
	
	@Column(nullable=false)
	@NotNull(message="general.field.null")
	@Pattern(regexp = "^[A-Z][A-Za-z ]{3,32}$",message="user.name.format")
	private String lname;
	
	@Column(nullable=false)
	@Pattern(regexp = "^[A-Z][A-Za-z ]{3,32}$",message="user.name.format")
	@NotNull(message="general.field.null")
	private String fname;
	
	@Column(nullable=true)
	private Blob picture;
	
	// TODO @CreatedDate private Timestamp joined; ***
	// TODO private Timestamp lastOnline; (updated in filters)
	// TODO private String lastGeoLocation; (updated in filters)
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private UserRole role=UserRole.USER;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		GrantedAuthority ga=new SimpleGrantedAuthority(role.getRole());
		return List.of(ga);
	}

	@Override
	public String getPassword() {
		return passw;
	}

	@Override
	public String getUsername() {
		return uid;
	}
	
	public UserResponseDto toResponseDto() {
		UserResponseDto u=new UserResponseDto();
		u.setId(this.uid);
		u.setUname(this.uname);
		u.setLname(this.lname);
		u.setFname(this.fname);
		u.setEmail(this.email);
		u.setPhoneNo(this.phoneNo);
		return u;
	}
	
	public UserResponseDto toDto() {
		return toResponseDto();
	}

	public String getId() {
		return uid;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getUname() {
		return uname;
	}

	public String getLname() {
		return lname;
	}

	public String getFname() {
		return fname;
	}

	public String getRole() {
		return role.getRole();
	}

	public void setId(String id) {
		this.uid = id;
	}

	public void setPassw(String passw) {
		this.passw = passw;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Blob getPicture() {
		return picture;
	}

	public void setPicture(Blob picture) {
		this.picture = picture;
	}

	public String getUid() {
		return uid;
	}

	public String getPassw() {
		return passw;
	}

}
