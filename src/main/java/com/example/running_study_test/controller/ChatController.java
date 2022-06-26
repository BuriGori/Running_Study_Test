package com.example.running_study_test.controller;

import com.example.running_study_test.constant.MessageType;
import com.example.running_study_test.dto.*;
import com.example.running_study_test.entity.ChatMessage;
import com.example.running_study_test.entity.GpsMessage;
import com.example.running_study_test.entity.Member;
import com.example.running_study_test.entity.Room;
import com.example.running_study_test.repo.ChatMessageRepository;
import com.example.running_study_test.repo.GpsMessageRepository;
import com.example.running_study_test.repo.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ChatController {

  private final SimpMessageSendingOperations messagingTemplate;
  private final ChatMessageRepository chatMessageRepository;
  private final RoomRepository roomRepository;
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
  @MessageMapping("/ready/{id}")
  public void readyToRoom(@DestinationVariable Long id, ReadyMessageDto dto){
    System.out.println("--레디 메세지 전송 시작--");

    if(dto.getType() != MessageType.READY){
      messagingTemplate.convertAndSend("/sub/member/"+dto.getSenderId()
              , ErrorMessageDto.createError("메세지 타입이 READY가 아닌 "+ dto.getType()+" 입니다."));
      return;
    }
    Optional<Room> findRoom = roomRepository.findById(id);
    if(!findRoom.isPresent()){
      messagingTemplate.convertAndSend("/sub/member/"+dto.getSenderId()
              , ErrorMessageDto.createError(id+"번 방이 없습니다."));
      return;
    }

    List<Member> memberList = findRoom.get().getMemberList();

    Optional<Member> findMember = memberList.stream().filter(
                    (member) -> member.getId().equals(dto.getSenderId())).findFirst();
    if(!findMember.isPresent()){
      messagingTemplate.convertAndSend("/sub/member/"+dto.getSenderId()
              , ErrorMessageDto.createError(id + "번 방에 참여하지 않았습니다."));
      return;
    }


    if(!findMember.get().getIsReady()) {
      System.out.println(findMember.get().getId()+ "가 준비 되었습니다. ");
      findMember.get().changeReadyStatus();
      findRoom.get().addReadyMembers();
    }

    if(findRoom.get().getReadyCount() == findRoom.get().getMemberCount()){
      messagingTemplate.convertAndSend("/sub/chat/" + id, StartResponseDto.builder()
              .type(MessageType.START)
              .build()
      );
      findRoom.get().setReadyMembers();
    }
    else {
      messagingTemplate.convertAndSend("/sub/chat/"+ id
              , new ReadyResponseDto(findRoom.get().getReadyCount(), findRoom.get().getMemberCount()));
    }
  }

  @Transactional
  @MessageMapping("/chat/message/{roomId}")
  public void messageToRoom(@DestinationVariable Long roomId, GpsMessageDto dto){

    if(dto.getType() != MessageType.GPS){
      messagingTemplate.convertAndSend("/sub/member/"+dto.getSenderId()
              , ErrorMessageDto.createError("메세지 타입이 GPS 가 아닌 "+ dto.getType()+" 입니다."));
      return;
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
