package com.example.running_study_test.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomListResponse {

  private List<RoomDto> rooms;

  public static RoomListResponse from(List<RoomDto> rooms){
    return new RoomListResponse(rooms);
  }
}
