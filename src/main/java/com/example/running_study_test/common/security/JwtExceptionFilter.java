package com.example.running_study_test.common.security;

import com.example.running_study_test.common.MessageResponse;
import com.example.running_study_test.common.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

  private final AntPathMatcher pathMatcher = new AntPathMatcher();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("jwt Exception filter");
    try {
      filterChain.doFilter(request, response);
    } catch (CustomException e) {
      e.printStackTrace();
      responseHandle(response, e.getStatusCode(), e.getMessage());
    } catch (RuntimeException e) {
      e.printStackTrace();
      responseHandle(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버에러");
    }
  }

  private void responseHandle(HttpServletResponse response, HttpStatus statusCode, String message)
      throws IOException {

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(statusCode.value());
    response.getWriter()
        .write(objectMapper.writeValueAsString(new MessageResponse(message)));
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    List<String> skipPath = new ArrayList<>();
    skipPath.add("/");
    skipPath.add("/api/member");
    skipPath.add("/api/member/login");
    return skipPath.stream()
        .anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));

  }
}
