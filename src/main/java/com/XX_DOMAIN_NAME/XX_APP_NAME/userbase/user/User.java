package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo.PersonalInfo;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.session.dto.RegisterRequestDto;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.dto.UserResponseDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="usr")
@EntityListeners(AuditingEntityListener.class)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	@Column(updatable = false)
	@Id	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(unique=true,nullable=true)
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9\\-_\\$#@]{3,32}$",message="err.user.uname.format")
	@NotBlank(message="err.user.uname.blank")
	private String uname;

	// NOTE: do not place a @Valid bullshit on this since it's saved as a hash!
	@Column(nullable=false)
	@NotBlank(message="err.user.passw.blank")
	private String passw;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(referencedColumnName = "id",nullable=false)
	private PersonalInfo personalInfo;
	
	@Column
	@Builder.Default // TODO NOKAT WIKI : add a note about Builder.Default
	private Boolean isActive=true;
	 
	// XXX please do not change this to enum or String, bc we need non-insertable
	// user roles (and String won't do it) in the mean time, enum doesn't match too
	// good with jwt and co.
	@Column(nullable=false)
	@ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@Builder.Default
	private List<UserRole> roles=
		List.of(UserRole.builder().title("ROLE_USER").build());
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime lastUpdated;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> sgas=new ArrayList<>();
		ListIterator<UserRole> lir=roles.listIterator();
		while(lir.hasNext())
			sgas.add(new SimpleGrantedAuthority(lir.next().getTitle()));
		return sgas;
	}
	
	/**
	 * This method has NOTHING to do with ANY spring security whatsoever and is there to 
	 * merely make the process of finding user roles easier.
	 * @param String role
	 * @return boolean whether or not role was in the objects roles list
	 * */
	public boolean hasRole(String role) {
		ListIterator<UserRole> lir=roles.listIterator();
		while(lir.hasNext()) 
			if(lir.next().getTitle().equals(role)) 
				return true;
		return false;
	}
	
	public boolean removeRole(String role) {
		UserRole curr=null;
		ListIterator<UserRole> lir=roles.listIterator();
		while(lir.hasNext()) {
			curr=lir.next();
			if(curr.getTitle().equals(role)) { 
				break;
			}
			else curr=null;
		}
		if(curr!=null) 
			roles.remove(curr);
		
		return curr!=null;
			
	}

	@Override
	public String getPassword() {
		return passw;
	}
	
	/**
	 * It's Spring Security mess, you gotta return the primary key. PK in SS is considered by
	 * default to be the fucking uname.
	 * @return UUID id 
	 * */
	@Override 
	public String getUsername() {
		return this.id.toString();
	}
	
	/**
	 * Mutates then current object with new object's fields if they're not null. This method is
	 * suitable after you retrieve an object from database and wanna update it's fields then 
	 * persist the object and overwriting the old database row.
	 * @param User
	 * @return boolean if any change has occured, so you know whether to put back in db
	*/
	public boolean update(User src) {
		boolean hasChanged=false;
		if(src.getUname()!=null && !src.getUname().equals(uname)) {
			hasChanged=true;
			this.uname=src.getUname();
		}
		if(src.getPersonalInfo()!=null && !src.getPersonalInfo().equals(personalInfo)) {
			hasChanged=true;
			
			// avoiding the null fields from src being copied by direct assigning to 
			// the current personalInfo
			if(this.personalInfo==null) this.personalInfo=src.getPersonalInfo();
			else this.personalInfo.update(src.getPersonalInfo());
		}
		return hasChanged;
	}
	
	@Override
	public boolean equals(Object o){
		// generated by gen_update_method in side_progs/
		if(!(o instanceof User)) return false;
		User target=(User) o;
		return 	( (uname==null && target.getUname()==null ) || ( uname!=null && uname.equals(target.getUname())) )
		&&	( (personalInfo==null && target.getPersonalInfo()==null ) || ( personalInfo!=null && personalInfo.equals(target.getPersonalInfo())) )
		&&	true;
	}
	
	@Override
	public int hashCode() {return id.hashCode();}
	
	public UserResponseDto toResponseDto() {
		return toResponseDto(false);
	}

	public UserResponseDto toResponseDto(boolean authorized) {
		
		// TODO in NOKAT: remember to null-check the nested toResponseDto() calls (like the personalInfo here)
		return UserResponseDto.builder()
				.id(id)
				.isActive(isActive)
				.personalInfo(personalInfo!=null?personalInfo.toResponseDto(authorized):null)
				.uname(uname)
				.build();
	}
	
	public static User of(RegisterRequestDto ud) {
		return User.builder()
				.uname(ud.getUname())
				.passw(ud.getPassw())
				.personalInfo(ud.getPersonalInfo()!=null?PersonalInfo.ofDto(ud.getPersonalInfo()):new PersonalInfo())
				.build();
	}

}
