package com.example.running_study_test.dto;

import com.example.running_study_test.constant.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatResponseDto {
  private MessageType type;
  private Long senderId;
  private String message;
}
