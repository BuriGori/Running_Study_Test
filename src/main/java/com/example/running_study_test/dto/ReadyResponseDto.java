package com.example.running_study_test.dto;

import com.example.running_study_test.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadyResponseDto {

    private MessageType type = MessageType.READY;

    private int readyCount;

    private int totalCount;

    public ReadyResponseDto(int readyCount, int memberCount) {
        this.readyCount = readyCount;
        this.totalCount = memberCount;
    }
}
