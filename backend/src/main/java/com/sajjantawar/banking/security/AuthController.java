package com.sajjantawar.banking.security;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth") public class AuthController {
 private final AuthenticationManager authenticationManager; private final JwtService jwtService;
 public AuthController(AuthenticationManager authenticationManager,JwtService jwtService){this.authenticationManager=authenticationManager;this.jwtService=jwtService;}
 @PostMapping("/login") public LoginResponse login(@RequestBody LoginRequest request){
  var auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
  var principal=(org.springframework.security.core.userdetails.UserDetails)auth.getPrincipal();
  String role=principal.getAuthorities().iterator().next().getAuthority().replace("ROLE_","");
  return new LoginResponse(jwtService.generate(principal.getUsername(),role),"Bearer");
 }
}
