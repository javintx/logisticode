package com.hackathon.inditex.ValueObjects;

public record ProcessedOrder(
    Double distance,
    long orderId,
    String assignedLogisticsCenter,
    String status
) {

  public ProcessedOrder(Double distance, long orderId, String assignedLogisticsCenter) {
    this(distance, orderId, assignedLogisticsCenter, "ASSIGNED");
  }
}
