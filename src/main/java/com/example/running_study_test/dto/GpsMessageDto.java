package com.example.running_study_test.dto;

import com.example.running_study_test.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GpsMessageDto {

    private MessageType type;

    private Long senderId;

    private String name;

    private Double latitude;

    private Double longitude;
}
