package com.hackathon.inditex.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hackathon.inditex.Exceptions.CenterNotFoundException;
import com.hackathon.inditex.Exceptions.LoadCapacityExceededException;
import com.hackathon.inditex.Exceptions.LocationAlreadyInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler;

  @BeforeEach
  void setUp() {
    globalExceptionHandler = new GlobalExceptionHandler();
  }

  @Test
  void testHandleLogisticCenterLocationException_LocationAlreadyInUse() {
    var exception = new LocationAlreadyInUseException();
    var response = globalExceptionHandler.handleLogisticCenterLocationException(exception);
    assertEquals("There is already a logistics center in that position.", response.message());
  }

  @Test
  void testHandleLogisticCenterLocationException_LoadCapacityExceeded() {
    var exception = new LoadCapacityExceededException();
    var response = globalExceptionHandler.handleLogisticCenterLocationException(exception);
    assertEquals("Current load cannot exceed max capacity.", response.message());
  }

  @Test
  void testHandleCenterNotFoundException() {
    var exception = new CenterNotFoundException();
    var response = globalExceptionHandler.handleCenterNotFoundException(exception);
    assertEquals("Center not found.", response.message());
  }
}