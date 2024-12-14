package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Repositories.OrdersRepository;
import java.util.List;
import java.util.stream.Stream;
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
    verifySize(order.getSize());
    order.setStatus("PENDING");
    return ordersRepository.save(order);
  }

  private void verifySize(String size) {
    if (Stream.of("B", "M", "S").noneMatch(s -> s.equals(size))) {
      throw new UnknownOrderSizeException(size);
    }
  }

  public List<Order> getAllOrders() {
    return ordersRepository.findAll();
  }

  public static class UnknownOrderSizeException extends RuntimeException {

    public UnknownOrderSizeException(String unknownSize) {
      super("Unknown order size: %s".formatted(unknownSize));
    }
  }
}