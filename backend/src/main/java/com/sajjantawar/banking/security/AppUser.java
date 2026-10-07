package com.sajjantawar.banking.security;
import jakarta.persistence.*;
import java.util.UUID;
@Entity @Table(name="app_users")
public class AppUser {
 @Id private UUID id;
 @Column(nullable=false,unique=true,length=80) private String username;
 @Column(name="password_hash",nullable=false) private String passwordHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private Role role;
 protected AppUser(){}
 public AppUser(UUID id,String username,String passwordHash,Role role){this.id=id;this.username=username;this.passwordHash=passwordHash;this.role=role;}
 public UUID getId(){return id;} public String getUsername(){return username;} public String getPasswordHash(){return passwordHash;} public Role getRole(){return role;}
}
