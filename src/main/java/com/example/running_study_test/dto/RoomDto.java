package com.example.running_study_test.dto;

import com.example.running_study_test.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomDto {

  private Long roomId;

  private String roomName;

  private Long adminId;

  private int memberCount;

  public static RoomDto from(Room room) {
    return new RoomDto(
        room.getId(), room.getName(), room.getAdminId(), room.getMemberCount()
    );
  }
}
