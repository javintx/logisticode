package com.hackathon.inditex.Controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.hackathon.inditex.DTO.OrderCreated;
import com.hackathon.inditex.DTO.OrderRequest;
import com.hackathon.inditex.DTO.OrderResponse;
import com.hackathon.inditex.Services.OrdersService;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
  @ResponseStatus(HttpStatus.CREATED)
  public OrderCreated createOrder(@RequestBody OrderRequest request) {
    return OrderCreated.of(ordersService.create(request.toOrder()));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public Collection<OrderResponse> getAllOrders() {
    return ordersService.getAllOrders().stream().map(OrderResponse::of).toList();
  }

  @PostMapping(path = "/order-assignations", produces = APPLICATION_JSON_VALUE)
  public Map<String, Collection<Record>> orderAssignations() {
    return Map.of("processed-orders", ordersService.orderAssignations());
  }
}

