package com.hackathon.inditex.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CenterNotFoundException extends RuntimeException {

  public CenterNotFoundException() {
    super("Center not found.");
  }
}
