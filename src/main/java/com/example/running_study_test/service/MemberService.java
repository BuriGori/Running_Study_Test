package com.example.running_study_test.service;


import com.example.running_study_test.common.exception.CustomException;
import com.example.running_study_test.dto.LoginRequest;
import com.example.running_study_test.dto.LoginResponse;
import com.example.running_study_test.dto.RegisterMemberRequest;
import com.example.running_study_test.dto.MemberDto;
import com.example.running_study_test.entity.Member;
import com.example.running_study_test.repo.MemberRepository;
import com.example.running_study_test.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public void register(RegisterMemberRequest dto) {
    if (memberRepository.existsByEmail(dto.getEmail())) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
    }

    memberRepository.save(Member.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .build());
  }

  public LoginResponse login(LoginRequest dto) {
    Member findMember = memberRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."));

    if (!passwordEncoder.matches(dto.getPassword(), findMember.getPassword())) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
    }
    String jwt = jwtUtil.createToken(findMember.getId());

    return new LoginResponse(jwt);
  }

  public MemberDto getUser(Long userId) {
    Member findMember = memberRepository.findById(userId)
        .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."));
    return new MemberDto(findMember.getId(),findMember.getName(), findMember.getEmail());
  }
}
