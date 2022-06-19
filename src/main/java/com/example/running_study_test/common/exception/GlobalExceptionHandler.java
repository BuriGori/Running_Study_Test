package com.example.running_study_test.common.exception;

import com.example.running_study_test.common.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  private ResponseEntity<?> handleCustomError(CustomException e) {
    System.out.println(e.getMessage());
    return ResponseEntity.status(e.getStatusCode())
        .body(new MessageResponse(e.getMessage()));
  }

}
