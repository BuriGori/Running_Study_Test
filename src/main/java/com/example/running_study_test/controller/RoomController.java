package com.example.running_study_test.controller;

import com.example.running_study_test.common.SuccessResponse;
import com.example.running_study_test.dto.CreateRoomRequest;
import com.example.running_study_test.dto.RoomDetail;
import com.example.running_study_test.dto.RoomListResponse;
import com.example.running_study_test.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @PostMapping("")
  public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequest dto) {
    roomService.createRoom(dto , getMember());
    return SuccessResponse.ok("방 생성 성공");
  }

  @GetMapping("/list")
  public ResponseEntity<?> getRoomList() {
    RoomListResponse result = roomService.getRoomList();
    return SuccessResponse.withData(result);
  }


  @GetMapping("/{idx}")
  public ResponseEntity<?> getRoom(@PathVariable("idx") Long id) {
    return  SuccessResponse.withData(roomService.getRoomById(id));
  }


  @GetMapping("/{idx}/chat")
  public ResponseEntity<?> getChatMessage(@PathVariable("idx") Long id) {
    return ResponseEntity.ok().body(roomService.getChatMessageList(id));
  }

  @GetMapping("/{idx}/join")
  public ResponseEntity<?> joinRoom(@PathVariable("idx") Long id){
    roomService.join(id, getMember());
    return SuccessResponse.ok("방 참여 완료");
  }

  private Long getMember(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Long userId = Long.valueOf(auth.getPrincipal().toString());
    return userId;
  }
}
