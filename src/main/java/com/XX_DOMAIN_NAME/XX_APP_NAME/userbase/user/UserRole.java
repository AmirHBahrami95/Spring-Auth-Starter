package com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * It's saved as "rol" in database to avoid probable reserved keywords in different 
 * databases.
 * */
@Entity @Table(name="rol")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserRole{
	
	@Id 
	private String title;
}

/*
public enum UserRole {
	
	USER("USER"),
	ADMIN("ADMIN"),
	CONTRIBUTOR("CONTRIBUTOR");
	
	private final String title;
	
	private UserRole(String title) {
		this.title=title;
	}
	
	public String getTitle() {
		return title;
	}

}
*/