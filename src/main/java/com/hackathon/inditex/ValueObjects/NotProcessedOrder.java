package com.hackathon.inditex.ValueObjects;

public record NotProcessedOrder(
    Double distance,
    long orderId,
    String assignedLogisticsCenter,
    String message,
    String status
) {

  public static NotProcessedOrder byType(long orderId, String status) {
    return new NotProcessedOrder(null, orderId, null, "No available centers support the order type.", status);
  }

  public static NotProcessedOrder byCapacity(long orderId, String status) {
    return new NotProcessedOrder(null, orderId, null, "All centers are at maximum capacity.", status);
  }
}
