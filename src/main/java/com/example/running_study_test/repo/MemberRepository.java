package com.example.running_study_test.repo;

import com.example.running_study_test.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail(String email);
  Boolean existsByEmail(String email);
}
