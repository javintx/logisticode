package com.hackathon.inditex.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Repositories.OrdersRepository;
import java.util.Arrays;
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
  void testCreateOrder() {
    var order = new Order();
    order.setSize("m");

    when(ordersRepository.save(order)).thenReturn(order);

    var createdOrder = ordersService.create(order);

    assertNotNull(createdOrder);
    assertEquals("PENDING", createdOrder.getStatus());
    verify(ordersRepository, times(1)).save(order);
  }


  @Test
  void testGetAllOrders() {
    var order1 = new Order();
    var order2 = new Order();
    var orders = Arrays.asList(order1, order2);

    when(ordersRepository.findAll()).thenReturn(orders);

    var result = ordersService.getAllOrders();

    assertEquals(2, result.size());
    verify(ordersRepository, times(1)).findAll();
  }
}