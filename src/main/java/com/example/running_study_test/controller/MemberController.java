package com.example.running_study_test.controller;

import com.example.running_study_test.common.SuccessResponse;
import com.example.running_study_test.dto.LoginRequest;
import com.example.running_study_test.dto.LoginResponse;
import com.example.running_study_test.dto.MemberDto;
import com.example.running_study_test.dto.RegisterMemberRequest;
import com.example.running_study_test.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("")
  public ResponseEntity<?> registerMember(@RequestBody @Valid RegisterMemberRequest dto) {
    memberService.register(dto);
    return SuccessResponse.ok("등록 성공");
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest dto) {
    LoginResponse result = memberService.login(dto);
    return SuccessResponse.withData(result);
  }

  @GetMapping("/info")
  public ResponseEntity<?> getUser(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Long userId = Long.valueOf(auth.getPrincipal().toString());
    MemberDto result =  memberService.getUser(userId);
    return SuccessResponse.withData(result);
  }

}
