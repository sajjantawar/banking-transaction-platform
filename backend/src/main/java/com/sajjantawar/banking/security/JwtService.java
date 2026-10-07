package com.sajjantawar.banking.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
@Service public class JwtService {
 private final SecretKey key; private final long expirationSeconds;
 public JwtService(@Value("${security.jwt.secret}") String secret,@Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds){key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.expirationSeconds=expirationSeconds;}
 public String generate(String username,String role){Instant now=Instant.now();return Jwts.builder().subject(username).claim("role",role).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expirationSeconds))).signWith(key).compact();}
 public Claims parse(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
}
