package com.hackathon.inditex.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class LocationAlreadyInUseException extends RuntimeException {

  public LocationAlreadyInUseException() {
    super("There is already a logistics center in that position.");
  }
}
