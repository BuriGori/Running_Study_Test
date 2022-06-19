package com.example.running_study_test.config;

import com.example.running_study_test.common.security.StompSecurityHandler;
import com.example.running_study_test.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class SocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

  private final JwtUtil jwtUtil;

//  private StompSecurityHandler stompSecurityHandler(){
//    return new StompSecurityHandler(jwtUtil);
//  }
//
//  @Override
//  public void configureClientInboundChannel(ChannelRegistration registration) {
//    registration.interceptors(stompSecurityHandler());
//  }
}
