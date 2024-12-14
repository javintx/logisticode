package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Controllers.CentersController.BadCenterException;
import com.hackathon.inditex.Services.CentersService.CenterNotFoundException;
import com.hackathon.inditex.Services.CentersService.LoadCapacityExceededException;
import com.hackathon.inditex.Services.CentersService.LocationAlreadyInUseException;
import com.hackathon.inditex.Services.CentersService.UnknownCapacitySizeException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CentersExceptionHandler {

  @ExceptionHandler(LocationAlreadyInUseException.class)
  public ResponseEntity<Object> handleLogisticCenterLocationException(LocationAlreadyInUseException ex) {
    return ResponseEntity
        .internalServerError()
        .body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(LoadCapacityExceededException.class)
  public ResponseEntity<Object> handleLogisticCenterLocationException(LoadCapacityExceededException ex) {
    return ResponseEntity
        .internalServerError()
        .body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(CenterNotFoundException.class)
  public ResponseEntity<Object> handleCenterNotFoundException(CenterNotFoundException ex) {
    return ResponseEntity
        .status(404)
        .body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(UnknownCapacitySizeException.class)
  public ResponseEntity<Object> handleUnknownCapacitySizeException(UnknownCapacitySizeException ex) {
    return ResponseEntity
        .badRequest()
        .body(Map.of("message", ex.getMessage()));
  }

  @ExceptionHandler(BadCenterException.class)
  public ResponseEntity<Object> handleBadCenterException(BadCenterException ex) {
    return ResponseEntity
        .badRequest()
        .body(Map.of("message", ex.getMessage()));
  }
}
