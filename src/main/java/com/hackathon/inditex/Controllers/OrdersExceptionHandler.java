package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Services.OrdersService.UnknownOrderSizeException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrdersExceptionHandler {

  @ExceptionHandler(UnknownOrderSizeException.class)
  public ResponseEntity<Object> handleUnknownOrderSizeException(UnknownOrderSizeException ex) {
    return ResponseEntity
        .badRequest()
        .body(Map.of("message", ex.getMessage()));
  }
}
