package com.hackathon.inditex.DTO;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;

public record CenterResponse(Long id, String name, String capacity, String status, Integer currentLoad, Integer maxCapacity, Coordinates coordinates) {

  public static CenterResponse of(Center center) {
    return new CenterResponse(center.getId(), center.getName(), center.getCapacity(), center.getStatus(), center.getCurrentLoad(), center.getMaxCapacity(), center.getCoordinates());
  }
}

