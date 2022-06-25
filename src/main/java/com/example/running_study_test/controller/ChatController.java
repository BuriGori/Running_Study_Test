package com.example.running_study_test.controller;

import com.example.running_study_test.common.exception.CustomException;
import com.example.running_study_test.constant.MessageType;
import com.example.running_study_test.dto.*;
import com.example.running_study_test.entity.ChatMessage;
import com.example.running_study_test.entity.GpsMessage;
import com.example.running_study_test.entity.Member;
import com.example.running_study_test.entity.Room;
import com.example.running_study_test.repo.ChatMessageRepository;
import com.example.running_study_test.repo.GpsMessageRepository;
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
  private final GpsMessageRepository gpsMessageRepository;

  @MessageMapping("/chat/message")
  public void message(ChatMessageDto message) {
    System.out.println("chatMEssage");
    chatMessageRepository.save(ChatMessage.builder()
        .message(message.getMessage())
        .room(roomRepository.getById(message.getRoomId()))
        .sender(message.getSender())
        .build());
    messagingTemplate.convertAndSend("/sub/chat/" + message.getRoomId(), message);
  }

  @Transactional
  @MessageMapping("/join/{id}")
  public void join(@DestinationVariable Long id, JoinMessageDto dto) {

    //TODO Ready
    //TODO 멤버 방 참여 검사 검사
    System.out.println("----");
    Room findRoom = roomRepository.findById(id)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "방이 없음"));

    List<Member> memberList = findRoom.getMemberList();
    memberList.stream().filter(
        (member) -> member.getId().equals(dto.getSenderId())
    ).findFirst().orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "방에 참여하고 있지 않음"));

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

  @Transactional
  @MessageMapping("/ready/{id}")
  public void readyToRoom(@DestinationVariable Long id, ReadyMessageDto dto){
    System.out.println("--레디 메세지 전송 시작--");

    if(dto.getType() != MessageType.READY){
      new CustomException(HttpStatus.BAD_REQUEST, " Ready 메세지가 아님 ");
    }

    Room findRoom = roomRepository.findById(id)
            .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, " 방이 없음 "));

    List<Member> memberList = findRoom.getMemberList();
    Member findMember = memberList.stream().filter(
                    (member) -> member.getId().equals(dto.getSenderId())).findFirst()
            .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "방에 참여하고 있지 않음"));

    if(!findMember.getIsReady()) {
      System.out.println(findMember.getId()+ "가 준비 되었습니다. ");
      findMember.changeReadyStatus();
      findRoom.setReadyCount(findRoom.getReadyCount() + 1);
    }

    if(findRoom.getReadyCount() == findRoom.getMemberCount()){
      messagingTemplate.convertAndSend("/sub/chat/" + id, StartResponseDto.builder()
              .type(MessageType.START)
              .build()
      );
      findRoom.setReadyMembers();
    }
  }

  @Transactional
  @MessageMapping("/chat/message/{roomId}")
  public void messageToRoom(@DestinationVariable Long roomId, GpsMessageDto dto){

    if(dto.getType() != MessageType.GPS){
      new CustomException(HttpStatus.BAD_REQUEST, " GPS 메세지가 아님 ");
    }

    System.out.println("--GPS Message 확인--");
    GpsMessage message = GpsMessage.builder()
            .roomId(roomId)
            .senderId(dto.getSenderId())
            .name(dto.getName())
            .latitude(dto.getLatitude())
            .longitude(dto.getLongitude())
            .build();
    gpsMessageRepository.save(message);
    messagingTemplate.convertAndSend("/sub/chat/"+roomId, dto);

  }

//  @MessageExceptionHandler
//  @SendTo("/errors")
//  public String errorHandler(Throwable exception){
//    System.out.println(exception.getMessage());
//    return "에러발생";
//  }
}
