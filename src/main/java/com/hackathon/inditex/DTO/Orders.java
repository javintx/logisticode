package com.hackathon.inditex.DTO;

import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Entities.Order;
import java.util.Collection;

public record Orders(Collection<OrderResponse> orders) {

  public static Orders of(Collection<Order> orders) {
    return new Orders(orders.stream().map(OrderResponse::of).toList());
  }

  public record OrderResponse(
      Long id,
      Long customerId,
      String size,
      String status,
      String assignedCenter,
      Coordinates coordinates
  ) {

    public static OrderResponse of(Order order) {
      return new OrderResponse(
          order.getId(),
          order.getCustomerId(),
          order.getSize(),
          order.getStatus(),
          order.getAssignedCenter(),
          order.getCoordinates()
      );
    }
  }
}
