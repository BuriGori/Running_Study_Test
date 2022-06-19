package com.example.running_study_test.common;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class SuccessResponse {

  private Object data;

  private SuccessResponse(Object data) {
    this.data = data;
  }

  public static ResponseEntity<?> withData(Object data) {
    return ResponseEntity.ok(
        new SuccessResponse(data)
    );
  }

  public static ResponseEntity<?> ok(String message) {
    MessageResponse response = new MessageResponse(message);
    return ResponseEntity.ok(
        new SuccessResponse(response)
    );
  }
}
