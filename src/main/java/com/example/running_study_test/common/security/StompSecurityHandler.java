package com.example.running_study_test.common.security;

import com.example.running_study_test.util.JwtUtil;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class StompSecurityHandler implements ChannelInterceptor {

  private final JwtUtil jwtUtil;
  private static final String HEADER_PREFIX = "Bearer ";

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    if (StompCommand.CONNECT == accessor.getCommand()) {
      String jwt = accessor.getFirstNativeHeader("Authorization");
      if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer")) {
        jwt = jwt.substring(HEADER_PREFIX.length());
      }
      if(jwtUtil.validateToken(jwt)){
        String userId = jwtUtil.getUserId(jwt);
        System.out.println(userId);
      }
    }
    return ChannelInterceptor.super.preSend(message, channel);
  }

  @Override
  public Message<?> postReceive(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    String sessionId = accessor.getSessionId();
    switch (Objects.requireNonNull(accessor.getCommand())) {
      case CONNECT:
        log.info("유저 연결: "+ sessionId);
        break;
      case DISCONNECT:
        log.info("DISCONNECT");
        log.info("sessionId: {}",sessionId);
        log.info("channel:{}",channel);
        // 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
        break;
      default:
        break;
    }
    return ChannelInterceptor.super.postReceive(message, channel);
  }
}
