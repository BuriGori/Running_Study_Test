package com.example.running_study_test.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterMemberRequest {

  @NotNull
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String name;
}
