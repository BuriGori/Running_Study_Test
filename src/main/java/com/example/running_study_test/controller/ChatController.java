package com.example.running_study_test.controller;

import com.example.running_study_test.common.exception.CustomException;
import com.example.running_study_test.constant.MessageType;
import com.example.running_study_test.dto.ChatMessageDto;
import com.example.running_study_test.dto.ChatResponseDto;
import com.example.running_study_test.dto.JoinMessageDto;
import com.example.running_study_test.entity.ChatMessage;
import com.example.running_study_test.entity.Member;
import com.example.running_study_test.entity.Room;
import com.example.running_study_test.repo.ChatMessageRepository;
import com.example.running_study_test.repo.MemberRepository;
import com.example.running_study_test.repo.RoomRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class ChatController {

  private final SimpMessageSendingOperations messagingTemplate;
  private final ChatMessageRepository chatMessageRepository;
  private final MemberRepository memberRepository;
  private final RoomRepository roomRepository;
  private final HashMap<Long, ArrayList<Long>> readyUser = new HashMap<>();

  @MessageMapping("/chat/message")
  public void message(ChatMessageDto message) {
    System.out.println("chatMEssage tq");
    chatMessageRepository.save(ChatMessage.builder()
        .message(message.getMessage())
        .room(roomRepository.getById(message.getRoomId()))
        .sender(message.getSender())
        .build());
    messagingTemplate.convertAndSend("/sub/chat/" + message.getRoomId(), message);
  }

  @Transactional
  @MessageMapping("/chat/{id}")
  public void join(@DestinationVariable Long id, JoinMessageDto dto) {
    //TODO Ready
    //TODO 멤버 방 참여 검사 검사
    System.out.println("----");
    Room findRoom = roomRepository.findById(id)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "방이 없음"));

    List<Member> memberList = findRoom.getMemberList();
    memberList.stream().filter(
        (member) -> member.getId().equals(dto.getSenderId())
    ).findFirst().orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "fail"));

    //TODO 원래는 Redis에 저장하고 리턴.
    if (!readyUser.containsKey(id)) {
      readyUser.put(id, new ArrayList<Long>(List.of(dto.getSenderId())));
    } else {
      readyUser.get(id).add(dto.getSenderId());
    }

    messagingTemplate.convertAndSend("/sub/chat/" + id, ChatResponseDto.builder()
        .message("가입성공")
        .senderId(dto.getSenderId())
        .type(MessageType.JOIN)
        .build()
    );
  }

//  @MessageExceptionHandler
//  @SendTo("/errors")
//  public String errorHandler(Throwable exception){
//    System.out.println(exception.getMessage());
//    return "에러발생";
//  }
}
