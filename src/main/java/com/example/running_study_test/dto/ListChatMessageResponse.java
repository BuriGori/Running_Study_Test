package com.example.running_study_test.dto;

import com.example.running_study_test.entity.ChatMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListChatMessageResponse {

  private List<ChatMessage> messages;

  public static ListChatMessageResponse from(List<ChatMessage> messageList){
    return new ListChatMessageResponse(messageList);
  }
}
