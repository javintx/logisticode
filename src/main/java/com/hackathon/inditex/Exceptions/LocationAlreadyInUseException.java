package com.hackathon.inditex.Exceptions;

public class LocationAlreadyInUseException extends RuntimeException {

  public LocationAlreadyInUseException() {
    super("There is already a logistics center in that position.");
  }
}
