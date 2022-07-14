package com.example.running_study_test.repo;

import com.example.running_study_test.entity.GpsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpsMessageRepository extends JpaRepository<GpsMessage, Long> {
}
