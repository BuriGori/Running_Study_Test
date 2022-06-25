package com.example.running_study_test.dto;

import com.example.running_study_test.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartResponseDto {
    private MessageType type;
}
