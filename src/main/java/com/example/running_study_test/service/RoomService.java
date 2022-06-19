package com.example.running_study_test.service;

import com.example.running_study_test.common.exception.CustomException;
import com.example.running_study_test.dto.CreateRoomRequest;
import com.example.running_study_test.dto.ListChatMessageResponse;
import com.example.running_study_test.dto.RoomDetail;
import com.example.running_study_test.dto.RoomDto;
import com.example.running_study_test.dto.RoomListResponse;
import com.example.running_study_test.entity.Member;
import com.example.running_study_test.entity.Room;
import com.example.running_study_test.repo.ChatMessageRepository;
import com.example.running_study_test.repo.MemberRepository;
import com.example.running_study_test.repo.RoomRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;
  private final MemberRepository memberRepository;
  private final ChatMessageRepository chatMessageRepository;

  @Transactional
  public void createRoom(CreateRoomRequest dto, Long userId) {
    Room room = dto.toEntity(userId);
    Member findMember = memberRepository.findById(userId)
        .orElseThrow(()-> new CustomException(HttpStatus.BAD_REQUEST, "잘못된 정보입니다."));
    room.addMember(findMember);
    roomRepository.save(room);
  }

  @Transactional(readOnly = true)
  public RoomDetail getRoomById(Long roomId) {
    Optional<Room> findRoom = roomRepository.findById(roomId);
    findRoom.orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "방을 찾을 수 없습니다."));

    return RoomDetail.builder()
        .room(findRoom.get())
        .build();
  }

  @Transactional(readOnly = true)
  public RoomListResponse getRoomList() {
    List<Room> roomList = roomRepository.findAll();
    List<RoomDto> roomDtoList = roomList.stream().map(RoomDto::from).toList();

    return RoomListResponse.from(roomDtoList);
  }

  @Transactional
  public void join(Long roomId, Long userId){
    Room findRoom = roomRepository.findById(roomId)
        .orElseThrow(()-> new CustomException(HttpStatus.BAD_REQUEST, "잘못된 정보입니다."));

    Optional<Member> members =  findRoom.getMemberList().stream()
        .filter((member)-> member.getId().equals(userId))
        .findFirst();
    if(members.isPresent()){
      throw new CustomException(HttpStatus.BAD_REQUEST, "방에 이미 참여중입니다.");
    }

    Member findMember = memberRepository.findById(userId)
        .orElseThrow(()-> new CustomException(HttpStatus.BAD_REQUEST, "잘못된 정보입니다."));

    findRoom.addMember(findMember);
  }

  public ListChatMessageResponse getChatMessageList(Long roomId) {
    return new ListChatMessageResponse(chatMessageRepository.findByRoomId(roomId));
  }
}
