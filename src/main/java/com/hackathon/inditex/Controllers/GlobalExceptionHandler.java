package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.DTO.MessageResponse;
import com.hackathon.inditex.Services.CentersService.CenterNotFoundException;
import com.hackathon.inditex.Services.CentersService.LoadCapacityExceededException;
import com.hackathon.inditex.Services.CentersService.LocationAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<MessageResponse> buildErrorResponse(HttpStatus status, String message) {
    return ResponseEntity
        .status(status)
        .body(new MessageResponse(message));
  }

  @ExceptionHandler(LocationAlreadyInUseException.class)
  public ResponseEntity<MessageResponse> handleLocationAlreadyInUseException(LocationAlreadyInUseException ex) {
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(LoadCapacityExceededException.class)
  public ResponseEntity<MessageResponse> handleLoadCapacityExceededException(LoadCapacityExceededException ex) {
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(CenterNotFoundException.class)
  public ResponseEntity<MessageResponse> handleCenterNotFoundException(CenterNotFoundException ex) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }
}
