package com.hackathon.inditex.Exceptions;

public class LoadCapacityExceededException extends RuntimeException {

  public LoadCapacityExceededException() {
    super("Current load cannot exceed max capacity.");
  }
}
