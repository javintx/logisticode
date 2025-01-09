package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.DTO.MessageResponse;
import com.hackathon.inditex.Exceptions.CenterNotFoundException;
import com.hackathon.inditex.Exceptions.LoadCapacityExceededException;
import com.hackathon.inditex.Exceptions.LocationAlreadyInUseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(LocationAlreadyInUseException.class)
  public MessageResponse handleLocationAlreadyInUseException(LocationAlreadyInUseException ex) {
    return new MessageResponse(ex.getMessage());
  }

  @ExceptionHandler(LoadCapacityExceededException.class)
  public MessageResponse handleLoadCapacityExceededException(LoadCapacityExceededException ex) {
    return new MessageResponse(ex.getMessage());
  }

  @ExceptionHandler(CenterNotFoundException.class)
  public MessageResponse handleCenterNotFoundException(CenterNotFoundException ex) {
    return new MessageResponse(ex.getMessage());
  }
}
