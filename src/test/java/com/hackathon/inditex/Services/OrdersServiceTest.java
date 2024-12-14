package com.hackathon.inditex.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Repositories.OrdersRepository;
import com.hackathon.inditex.Services.OrdersService.UnknownOrderSizeException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrdersServiceTest {

  @Mock
  private OrdersRepository ordersRepository;
  @InjectMocks
  private OrdersService ordersService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateOrderWithValidSize() {
    Order order = new Order();
    order.setSize("m");

    when(ordersRepository.save(order)).thenReturn(order);

    Order createdOrder = ordersService.create(order);

    assertNotNull(createdOrder);
    assertEquals("PENDING", createdOrder.getStatus());
    verify(ordersRepository, times(1)).save(order);
  }

  @Test
  void testCreateOrderWithInvalidSize() {
    Order order = new Order();
    order.setSize("invalid");

    assertThrows(UnknownOrderSizeException.class, () -> ordersService.create(order));
    verify(ordersRepository, never()).save(order);
  }

  @Test
  void testGetAllOrders() {
    Order order1 = new Order();
    Order order2 = new Order();
    List<Order> orders = Arrays.asList(order1, order2);

    when(ordersRepository.findAll()).thenReturn(orders);

    List<Order> result = ordersService.getAllOrders();

    assertEquals(2, result.size());
    verify(ordersRepository, times(1)).findAll();
  }
}