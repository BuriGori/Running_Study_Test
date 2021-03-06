package com.example.running_study_test.dto;

import com.example.running_study_test.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequest {

  private String name;

  public Room toEntity(Long adminId){
    return Room.builder()
        .adminId(adminId)
        .name(name)
        .build();
  }
}
