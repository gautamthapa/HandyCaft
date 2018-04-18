package com.strontech.imgautam.handycaft;

public class UserInfo {



  private String username;
  private String email;
  private String mob_number;
  private String password;



  public UserInfo() {
  }

  public UserInfo(String username, String email, String mob_number, String password) {
    this.username = username;
    this.email = email;
    this.mob_number = mob_number;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMob_number() {
    return mob_number;
  }

  public void setMob_number(String mob_number) {
    this.mob_number = mob_number;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
