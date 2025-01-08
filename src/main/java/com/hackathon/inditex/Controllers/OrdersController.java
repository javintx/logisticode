package com.hackathon.inditex.Controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.hackathon.inditex.DTO.OrderCreated;
import com.hackathon.inditex.DTO.OrderRequest;
import com.hackathon.inditex.DTO.Orders;
import com.hackathon.inditex.Services.OrdersService;
import java.util.Map;
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

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<OrderCreated> createOrder(@RequestBody OrderRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(OrderCreated.of(ordersService.create(request.toOrder())));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Orders> getAllOrders() {
    return ResponseEntity.ok(Orders.of(ordersService.getAllOrders()));
  }

  @PostMapping(path = "/order-assignations", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, ?>> orderAssignations() {
    return ResponseEntity.ok(Map.of("processed-orders", ordersService.orderAssignations()));
  }
}

