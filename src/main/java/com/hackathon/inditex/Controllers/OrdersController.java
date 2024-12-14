package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Services.OrdersService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

  private final OrdersService ordersService;

  @Autowired
  public OrdersController(OrdersService ordersService) {
    this.ordersService = ordersService;
  }

  @PostMapping
  public ResponseEntity<?> createOrder(@RequestBody Order order) {
    Order savedOrder = ordersService.create(order);
    return ResponseEntity.status(201)
        .body(new OrderCreated(savedOrder, "Order created successfully in PENDING status."));
  }

  @GetMapping
  public ResponseEntity<List<Order>> getAllOrders() {
    List<Order> orders = ordersService.getAllOrders();
    return ResponseEntity.ok(orders);
  }

  public record OrderCreated(
      Long orderId,
      Long customerId,
      String size,
      String assignedLogisticsCenter,
      Coordinates coordinates,
      String status,
      String message
  ) {

    public OrderCreated(Order order, String message) {
      this(order.getId(), order.getCustomerId(), order.getSize(), order.getAssignedCenter(), order.getCoordinates(),
          order.getStatus(), message);
    }
  }
}

