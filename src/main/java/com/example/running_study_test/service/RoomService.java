package com.example.running_study_test.service;

import com.example.running_study_test.dto.CreateRoomRequest;
import com.example.running_study_test.dto.ListChatMessageResponse;
import com.example.running_study_test.dto.RoomDto;
import com.example.running_study_test.dto.RoomListResponse;
import com.example.running_study_test.entity.ChatMessage;
import com.example.running_study_test.entity.Room;
import com.example.running_study_test.repo.ChatMessageRepository;
import com.example.running_study_test.repo.RoomRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;
  private final ChatMessageRepository chatMessageRepository;

    public Room createRoom(CreateRoomRequest dto) {
      Room room = dto.toEntity();
      return roomRepository.save(room);
    }

    public RoomDto getRoomById(Long roomId) {
      Optional<Room> findRoom = roomRepository.findById(roomId);
      findRoom.orElseThrow(() -> new NoSuchElementException("room not found"));

    return RoomDto.from(findRoom.get());
  }

  public RoomListResponse getRoomList() {
      List<Room> roomList = roomRepository.findAll();
      List<RoomDto> roomDtoList = roomList.stream().map(RoomDto::from).toList();

    return RoomListResponse.from(roomDtoList);
  }

  public ListChatMessageResponse getChatMessageList(Long roomId) {
    return new ListChatMessageResponse(chatMessageRepository.findByRoomId(roomId));
  }
}
