package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Repositories.OrdersRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

  private final OrdersRepository ordersRepository;

  @Autowired
  public OrdersService(OrdersRepository ordersRepository) {
    this.ordersRepository = ordersRepository;
  }

  public Order create(Order order) {
    order.setStatus("PENDING");
    return ordersRepository.save(order);
  }

  public Collection<Order> getAllOrders() {
    return ordersRepository.findAll();
  }
}