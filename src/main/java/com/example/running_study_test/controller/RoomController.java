package com.example.running_study_test.controller;

import com.example.running_study_test.dto.CreateRoomRequest;
import com.example.running_study_test.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

  @GetMapping("/list")
  public ResponseEntity<?> getRoomList() {
    return ResponseEntity.ok().body(roomService.getRoomList());
  }

  @PostMapping("")
  public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequest dto) {
    return ResponseEntity.ok().body(roomService.createRoom(dto));
  }

  @GetMapping("/{idx}")
  public ResponseEntity<?> getRoom(@PathVariable("idx") Long id) {
    return ResponseEntity.ok().body(roomService.getRoomById(id));
  }


  @GetMapping("/{idx}/chat")
  public ResponseEntity<?> getChatMessage(@PathVariable("idx") Long id) {
    return ResponseEntity.ok().body(roomService.getChatMessageList(id));
  }

}
