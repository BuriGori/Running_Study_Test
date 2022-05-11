package com.example.running_study_test.repo;

import com.example.running_study_test.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


}
