package com.hackathon.inditex.DTO;

import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Entities.Order;

public record OrderCreated(
    Long orderId,
    Long customerId,
    String size,
    String assignedLogisticsCenter,
    Coordinates coordinates,
    String status,
    String message
) {

  public static OrderCreated of(Order order) {
    return new OrderCreated(order.getId(), order.getCustomerId(), order.getSize(), order.getAssignedCenter(),
        order.getCoordinates(),
        order.getStatus(), "Order created successfully in PENDING status.");
  }
}
