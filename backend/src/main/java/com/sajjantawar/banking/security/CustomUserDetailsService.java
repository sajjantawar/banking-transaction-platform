package com.sajjantawar.banking.security;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service public class CustomUserDetailsService implements UserDetailsService {
 private final AppUserRepository repository;
 public CustomUserDetailsService(AppUserRepository repository){this.repository=repository;}
 public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
  AppUser user=repository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
  return User.withUsername(user.getUsername()).password(user.getPasswordHash()).roles(user.getRole().name()).build();
 }
}
