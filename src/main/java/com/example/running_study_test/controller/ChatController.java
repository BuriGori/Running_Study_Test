package com.example.running_study_test.controller;

import com.example.running_study_test.dto.ChatMessageDto;
import com.example.running_study_test.entity.ChatMessage;
import com.example.running_study_test.repo.ChatMessageRepository;
import com.example.running_study_test.repo.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final RoomRepository roomRepository;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message){
        chatMessageRepository.save(ChatMessage.builder()
                .message(message.getMessage())
                .room(roomRepository.getById(message.getRoomId()))
                .sender(message.getSender())
                .build());
        messagingTemplate.convertAndSend("/sub/chat/"+message.getRoomId(), message);
    }
}
