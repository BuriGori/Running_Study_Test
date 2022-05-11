package com.example.running_study_test.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessage extends JpaRepository<ChatMessage, Long> {
}
