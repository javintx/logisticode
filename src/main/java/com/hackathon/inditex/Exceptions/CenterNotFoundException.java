package com.hackathon.inditex.Exceptions;

public class CenterNotFoundException extends RuntimeException {

  public CenterNotFoundException() {
    super("Center not found.");
  }
}
