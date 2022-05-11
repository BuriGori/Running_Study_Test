package com.example.running_study_test.dto;

import lombok.Getter;

@Getter
public class ChatMessageDto {

    private String sender;

    private String message;

    private Long roomId;
}
