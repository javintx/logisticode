package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Services.OrdersService;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<OrderCreated> createOrder(@RequestBody OrderRequest request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new OrderCreated(
            ordersService.create(request.toOrder()))
        );
  }

  @GetMapping
  public ResponseEntity<Collection<Order>> getAllOrders() {
    return ResponseEntity.ok(ordersService.getAllOrders());
  }

  public record OrderRequest(
      Long customerId,
      String size,
      Coordinates coordinates
  ) {

    Order toOrder() {
      var order = new Order();
      order.setCustomerId(customerId);
      order.setSize(size);
      order.setCoordinates(coordinates);
      return order;
    }
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

    public OrderCreated(Order order) {
      this(order.getId(), order.getCustomerId(), order.getSize(), order.getAssignedCenter(), order.getCoordinates(),
          order.getStatus(), "Order created successfully in PENDING status.");
    }
  }
}

