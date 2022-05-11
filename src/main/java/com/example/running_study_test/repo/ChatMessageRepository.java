package com.example.running_study_test.repo;

import com.example.running_study_test.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findByRoomId(Long roomId);
}
