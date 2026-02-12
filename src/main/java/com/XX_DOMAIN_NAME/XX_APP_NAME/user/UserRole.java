package com.XX_DOMAIN_NAME.XX_APP_NAME.user;

public enum UserRole {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN"),
  MOD("ROLE_MOD");

  private final String role;

  UserRole(String role) {
      this.role = role;
  }

  public String getRole() {
      return role;
  }
}