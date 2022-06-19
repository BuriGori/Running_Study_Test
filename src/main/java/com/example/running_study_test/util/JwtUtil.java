package com.example.running_study_test.util;

import com.example.running_study_test.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

  private static final String AUTH_KEY = "7IaM7LyT7Iqk7YSw65SU7Im97KeA7JWK64uk7IaM7LyT7Iqk7YSw65SU7Im97KeA7JWK64uk7IaM7LyT7Iqk7YSw65SU7Im97KeA7JWK64uk";
  private static final int EXPIRE_TIME = 5000000;
  private final Key key;

  public JwtUtil() {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(AUTH_KEY));
  }

  public String createToken(Long userId) {
    long now = (new Date()).getTime();
    Date accessToken = new Date(now + EXPIRE_TIME * 1000L);
    String jwt = Jwts.builder()
        .setSubject(userId.toString())
        .setExpiration(accessToken)
        .signWith(key)
        .compact();
    return jwt;
  }

  public String getUserId(String token) {
    Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
      throw new CustomException(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
      throw new CustomException(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
      throw new CustomException(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
      throw new CustomException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다.");
    }
  }
}
