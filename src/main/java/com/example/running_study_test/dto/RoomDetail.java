package com.example.running_study_test.dto;

import com.example.running_study_test.entity.Room;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomDetail {

  private Long roomId;

  private Long adminId;

  private String roomName;

  private List<MemberDto> roomMembers;

  @Builder
  RoomDetail(Room room){
    this.roomId = room.getId();
    this.adminId = room.getAdminId();
    this.roomName = room.getName();
    this.roomMembers = room.getMemberList().stream()
        .map((member)-> new MemberDto(member.getId(), member.getName(), member.getEmail()))
        .collect(Collectors.toList());
  }
}
