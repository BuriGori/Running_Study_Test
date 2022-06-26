package com.example.running_study_test.dto;

import com.example.running_study_test.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ErrorMessageDto {

    private final MessageType type = MessageType.ERROR;

    private String message;

    public static ErrorMessageDto createError(String message){
        return builder().message(message).build();
    }
}
