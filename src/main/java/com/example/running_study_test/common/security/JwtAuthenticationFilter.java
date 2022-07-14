package com.example.running_study_test.common.security;

import com.example.running_study_test.common.exception.CustomException;
import com.example.running_study_test.util.JwtUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String HEADER_PREFIX = "Bearer ";
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("JwtAuthenticationFilter");
    String jwtToken = extractToken(request);

    if (StringUtils.hasText(jwtToken) && jwtUtil.validateToken(jwtToken)) {
      String userID = jwtUtil.getUserId(jwtToken);
      Authentication authentication = new UsernamePasswordAuthenticationToken(userID, "");
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    List<String> skipPath = new ArrayList<>();
    skipPath.add("/");
    skipPath.add("/api/member");
    skipPath.add("/api/member/login");
    skipPath.add("/ws/**");
    skipPath.add("/pub/**");
    skipPath.add("/sub/**");
    log.info("patn --- " + request.getRequestURI());
    return skipPath.stream()
        .anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));

  }

  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
      return bearerToken.substring(HEADER_PREFIX.length());
    } else {
      throw new CustomException(HttpStatus.BAD_REQUEST, "Header에 token이 없습니다.");
    }
  }
}
