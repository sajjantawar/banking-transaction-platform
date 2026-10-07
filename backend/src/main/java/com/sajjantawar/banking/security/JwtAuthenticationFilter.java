package com.sajjantawar.banking.security;
import jakarta.servlet.*; import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException; import java.util.List;
@Component public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private final JwtService jwtService;
 public JwtAuthenticationFilter(JwtService jwtService){this.jwtService=jwtService;}
 protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{
  String header=request.getHeader("Authorization");
  if(header!=null&&header.startsWith("Bearer ")){try{var c=jwtService.parse(header.substring(7));String role=c.get("role",String.class);var a=new UsernamePasswordAuthenticationToken(c.getSubject(),null,List.of(new SimpleGrantedAuthority("ROLE_"+role)));SecurityContextHolder.getContext().setAuthentication(a);}catch(Exception ignored){}}
  chain.doFilter(request,response);
 }
}
