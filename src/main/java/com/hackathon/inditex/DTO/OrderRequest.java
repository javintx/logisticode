package com.hackathon.inditex.DTO;

import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Entities.Order;

public record OrderRequest(Long customerId, String size, Coordinates coordinates) {

  public Order toOrder() {
    var order = new Order();
    order.setCustomerId(customerId);
    order.setSize(size);
    order.setCoordinates(coordinates);
    return order;
  }
}
