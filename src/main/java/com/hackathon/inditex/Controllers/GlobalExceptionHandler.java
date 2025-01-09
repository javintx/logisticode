package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.DTO.MessageResponse;
import com.hackathon.inditex.Exceptions.CenterNotFoundException;
import com.hackathon.inditex.Exceptions.LoadCapacityExceededException;
import com.hackathon.inditex.Exceptions.LocationAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(LocationAlreadyInUseException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public MessageResponse handleLogisticCenterLocationException(LocationAlreadyInUseException ex) {
    return new MessageResponse(ex.getMessage());
  }

  @ExceptionHandler(LoadCapacityExceededException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public MessageResponse handleLogisticCenterLocationException(LoadCapacityExceededException ex) {
    return new MessageResponse(ex.getMessage());
  }

  @ExceptionHandler(CenterNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public MessageResponse handleCenterNotFoundException(CenterNotFoundException ex) {
    return new MessageResponse(ex.getMessage());
  }
}
